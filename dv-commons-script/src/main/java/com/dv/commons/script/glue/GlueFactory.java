package com.dv.commons.script.glue;

import com.dv.commons.script.base.IHandler;
import com.dv.commons.script.utils.SpringContextHolder;
import groovy.lang.GroovyClassLoader;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum GlueFactory {
    Instance;
    private final ConcurrentMap<String, Class<?>> CLASS_CACHE;
    private final ConcurrentMap<String,Long> LAST_USE_TIME;
    private final GroovyClassLoader groovyClassLoader;
    private final Thread thread;
    private final long CACHE_TIMEOUT = 60 * 60 * 1000L;
    private final long DAEMON_TIME = 5*60*1000L;
    private boolean interrupt = false;
    GlueFactory(){
        groovyClassLoader = new GroovyClassLoader();
        LAST_USE_TIME = new ConcurrentHashMap<>();
        CLASS_CACHE = new ConcurrentHashMap<>();
        thread = new Thread(()->{
            while (!interrupt){
                try {
                    Thread.sleep(DAEMON_TIME);
                    long l = System.currentTimeMillis();
                    Set<String> keys = LAST_USE_TIME.keySet();
                    for (String key : keys) {
                        if ((l-LAST_USE_TIME.getOrDefault(key,0L))>CACHE_TIMEOUT){
                            LAST_USE_TIME.remove(key);
                            CLASS_CACHE.remove(key);
                        }
                    }
                } catch (InterruptedException ignore) {
                }
            }
        },"GlueFactory 守护线程");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * load new instance, prototype
     *
     * @param codeSource {@link String}
     * @return {@link IHandler}
     */
    public IHandler loadNewInstance(String codeSource) throws Exception{
        if (codeSource!=null && codeSource.trim().length()>0) {
            Class<?> clazz = getCodeSourceClass(codeSource);
            if (clazz != null) {
                Object instance = clazz.getConstructor().newInstance();
                if (instance instanceof IHandler) {
                    this.injectService(instance);
                    return (IHandler) instance;
                } else {
                    throw new IllegalArgumentException(">>>>>>>>>>> xxl-glue, loadNewInstance error, "
                            + "cannot convert from instance["+ instance.getClass() +"] to IJobHandler");
                }
            }
        }
        throw new IllegalArgumentException(">>>>>>>>>>> xxl-glue, loadNewInstance error, instance is null");
    }
    private Class<?> getCodeSourceClass(String codeSource){
        try {
            // md5
            byte[] md5 = MessageDigest.getInstance("MD5").digest(codeSource.getBytes());
            String md5Str = new BigInteger(1, md5).toString(16);

            Class<?> clazz = CLASS_CACHE.get(md5Str);
            if(clazz == null){
                clazz = groovyClassLoader.parseClass(codeSource);
                CLASS_CACHE.putIfAbsent(md5Str, clazz);
            }
            return clazz;
        } catch (Exception e) {
            return groovyClassLoader.parseClass(codeSource);
        }
    }

    private void injectService(Object instance){
        if (instance==null) {
            return;
        }

        if (SpringContextHolder.getApplicationContext() == null) {
            return;
        }

        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Object fieldBean = null;
            // with bean-id, bean could be found by both @Resource and @Autowired, or bean could only be found by @Autowired

            if (AnnotationUtils.getAnnotation(field, Resource.class) != null) {
                try {
                    Resource resource = AnnotationUtils.getAnnotation(field, Resource.class);
                    assert resource != null;
                    if (resource.name()!=null && resource.name().length()>0){
                        fieldBean = SpringContextHolder.getApplicationContext().getBean(resource.name());
                    } else {
                        fieldBean = SpringContextHolder.getApplicationContext().getBean(field.getName());
                    }
                } catch (Exception ignored) {
                }
                if (fieldBean==null ) {
                    fieldBean = SpringContextHolder.getApplicationContext().getBean(field.getType());
                }
            } else if (AnnotationUtils.getAnnotation(field, Autowired.class) != null) {
                Qualifier qualifier = AnnotationUtils.getAnnotation(field, Qualifier.class);
                if (qualifier != null && qualifier.value().length() > 0) {
                    fieldBean = SpringContextHolder.getApplicationContext().getBean(qualifier.value());
                } else {
                    fieldBean = SpringContextHolder.getApplicationContext().getBean(field.getType());
                }
            }

            if (fieldBean!=null) {
                field.setAccessible(true);
                try {
                    field.set(instance, fieldBean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void destroy(){
        interrupt = true;
        CLASS_CACHE.clear();
        LAST_USE_TIME.clear();
        thread.interrupt();
    }
}
