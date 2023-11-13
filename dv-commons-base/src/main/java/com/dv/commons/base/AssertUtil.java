package com.dv.commons.base;

import com.dv.commons.base.exceptions.BaseException;

import java.util.Collection;
import java.util.Map;

public final class AssertUtil {

    /**
     * 新增异常
     *
     * @param status 状态码
     */
    public static void newException(BaseStatus status) {
        throw BaseException.of(status);
    }

    /**
     * 新增异常
     *
     * @param status 状态码
     * @param e      异常
     */
    public static void newException(BaseStatus status, Throwable e) {
        throw BaseException.of(status, e);
    }

    /**
     * 新增异常
     *
     * @param e 异常
     */
    public static void newException(BaseException e) {
        throw e;
    }

    /**
     * 新增异常
     *
     * @param status 状态码
     * @param tips    提示
     */
    public static void newException(BaseStatus status, String tips) {
        throw BaseException.of(status, tips);
    }

    public static void newException(RuntimeException e){
        throw e;
    }

    /**
     * 新增异常
     *
     * @param status 状态码
     * @param msg    消息
     * @param e      异常
     */
    public static void newException(BaseStatus status, String msg, Throwable e) {
        throw BaseException.of(status, msg,e);
    }

    /**
     * 验证表达式
     *
     * @param expression 布尔表达式
     * @param status     状态码
     */
    public static void state(boolean expression, BaseStatus status) {
        if (!expression) {
            newException(status);
        }
    }

    /**
     * 验证表达式
     *
     * @param expression 布尔表达式
     * @param message    消息
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 验证表达式
     *
     * @param expression 布尔表达式
     * @param status     状态码
     * @param message    消息
     */
    public static void state(boolean expression, BaseStatus status, String message) {
        if (!expression) {
            newException(status, message);
        }
    }

    public static void state(boolean expression,RuntimeException e){
        if (!expression){
            newException(e);
        }
    }

    /**
     * 对象为null
     *
     * @param object 对象
     * @param status 状态码
     */
    public static void isNull(Object object, BaseStatus status) {
        if (object != null) {
            newException(status);
        }
    }

    /**
     * 对象为null
     *
     * @param object  对象
     * @param message 消息
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 对象为null
     *
     * @param object  对象
     * @param status  状态码
     * @param message 消息
     */
    public static void isNull(Object object, BaseStatus status, String message) {
        if (object != null) {
            newException(status, message);
        }
    }

    /**
     * 对象不为null
     *
     * @param object 对象
     * @param status 状态码
     */
    public static void notNull(Object object, BaseStatus status) {
        if (object == null) {
            newException(status);
        }
    }

    /**
     * 对象不为null
     *
     * @param object  对象
     * @param status  状态码
     * @param message 消息
     */
    public static void notNull(Object object, BaseStatus status, String message) {
        if (object == null) {
            newException(status, message);
        }
    }

    /**
     * 对象不为null
     *
     * @param object  对象
     * @param message 消息
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 目标字符串为空
     *
     * @param text   目标字符串
     * @param status 状态码
     */
    public static void notEmpty(String text, BaseStatus status) {
        if (text == null || text.isBlank()) {
            newException(status);
        }
    }

    public static void notEmpty(String text, BaseStatus status, String message) {
        if (text == null || text.isBlank()) {
            newException(status, message);
        }
    }

    /**
     * 目标字符串有长度
     *
     * @param text   目标字符串
     * @param status 状态码
     */
    public static void hasLength(String text, BaseStatus status) {
        if (text==null||text.isEmpty()) {
            newException(status);
        }
    }

    /**
     * 目标字符串是否有长度
     *
     * @param text    目标字符串
     * @param status  状态码
     * @param message 消息
     */
    public static void hasLength(String text, BaseStatus status, String message) {
        if (text==null||text.isEmpty()) {
            newException(status, message);
        }
    }

    /**
     * 目标字符串是否有长度
     *
     * @param text    目标字符串
     * @param message 消息
     */
    public static void hasLength(String text, String message) {
        if (text==null||text.isEmpty()) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 是否有文本
     *
     * @param text   目标字符串
     * @param status 状态码
     */
    public static void hasText(String text, BaseStatus status) {
        if (text==null||text.isBlank()) {
            newException(status);
        }
    }

    /**
     * 是否是文本
     *
     * @param text    目标字符串
     * @param status  状态码
     * @param message 消息
     */
    public static void hasText(String text, BaseStatus status, String message) {
        if (text==null||text.isBlank()) {
            newException(status, message);
        }
    }

    /**
     * 是否是文本
     *
     * @param text    目标字符串
     * @param message 消息
     */
    public static void hasText(String text, String message) {
        if (text==null||text.isBlank()) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 字符串不包含目标字符串
     *
     * @param textToSearch 字符串
     * @param substring    目标字符串
     * @param status       状态码
     */
    public static void doesNotContain(String textToSearch, String substring, BaseStatus status) {
        if (textToSearch!=null&&!textToSearch.isEmpty() && substring!=null&&!substring.isEmpty() && textToSearch.contains(substring)) {
            newException(status);
        }
    }

    /**
     * 字符串不包含目标字符串
     *
     * @param textToSearch 字符串
     * @param substring    目标字符串
     * @param status       状态码
     * @param message      提示
     */
    public static void doesNotContain(String textToSearch, String substring, BaseStatus status, String message) {
        if (textToSearch!=null&&!textToSearch.isEmpty() && substring!=null&&!substring.isEmpty() && textToSearch.contains(substring)) {
            newException(status, message);
        }
    }


    /**
     * 字符串不包含目标字符串
     *
     * @param textToSearch 字符串
     * @param substring    目标字符串
     * @param message      提示
     */
    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (textToSearch!=null&&!textToSearch.isEmpty() && substring!=null&&!substring.isEmpty() && textToSearch.contains(substring)) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 数组不为空
     *
     * @param array  数组
     * @param status 状态码
     */
    public static void notEmpty(Object[] array, BaseStatus status) {
        if (array == null || array.length == 0) {
            newException(status);
        }
    }

    /**
     * 数组不为空
     *
     * @param array  数组
     * @param status 状态码
     */
    public static void notEmpty(Object[] array, BaseStatus status, String message) {
        if (array == null || array.length == 0) {
            newException(status, message);
        }
    }

    /**
     * 数组不为空
     *
     * @param array   数组
     * @param message 提示
     */
    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 数组没有null元素
     *
     * @param array  数组
     * @param status 状态码
     */
    public static void noNullElements(Object[] array, BaseStatus status) {
        if (array != null) {

            for (Object element : array) {
                if (element == null) {
                    newException(status);
                }
            }
        }

    }

    /**
     * 数组没有null元素
     *
     * @param array   数组
     * @param status  状态码
     * @param message 提示
     */
    public static void noNullElements(Object[] array, BaseStatus status, String message) {
        if (array != null) {

            for (Object element : array) {
                if (element == null) {
                    newException(status, message);
                }
            }
        }

    }

    /**
     * 数组没有null元素
     *
     * @param array   数组
     * @param message 提示
     */
    public static void noNullElements(Object[] array, String message) {
        if (array != null) {

            for (Object element : array) {
                if (element == null) {
                    throw new IllegalStateException(message);
                }
            }
        }

    }

    /**
     * 集合不为空
     *
     * @param collection 集合
     * @param status     状态码
     */
    public static void notEmpty(Collection<?> collection, BaseStatus status) {
        if (collection == null || collection.isEmpty()) {
            newException(status);
        }
    }

    /**
     * 集合不为空
     *
     * @param collection 集合
     * @param status     状态码
     * @param message    提示
     */
    public static void notEmpty(Collection<?> collection, BaseStatus status, String message) {
        if (collection == null || collection.isEmpty()) {
            newException(status, message);
        }
    }

    /**
     * 集合不为空
     *
     * @param collection 集合
     * @param message    提示
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * 集合没有空元素
     *
     * @param collection 集合
     * @param status     状态码
     */
    public static void noNullElements(Collection<?> collection, BaseStatus status) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    newException(status);
                }
            }
        }

    }

    /**
     * 集合没有空元素
     *
     * @param collection 集合
     * @param status     状态码
     * @param message    提示
     */
    public static void noNullElements(Collection<?> collection, BaseStatus status, String message) {
        if (collection != null) {

            for (Object element : collection) {
                if (element == null) {
                    newException(status, message);
                }
            }
        }

    }

    /**
     * 集合没有空元素
     *
     * @param collection 集合
     * @param message    提示
     */
    public static void noNullElements(Collection<?> collection, String message) {
        if (collection != null) {

            for (Object element : collection) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }

    }

    /**
     * map不为空
     *
     * @param map    map
     * @param status 状态码
     */
    public static void notEmpty(Map<?, ?> map, BaseStatus status) {
        if (map == null || map.isEmpty()) {
            newException(status);
        }
    }

    /**
     * map不为空
     *
     * @param map     map
     * @param status  状态码
     * @param message 提示
     */
    public static void notEmpty(Map<?, ?> map, BaseStatus status, String message) {
        if (map == null || map.isEmpty()) {
            newException(status, message);
        }
    }

    /**
     * map不为空
     *
     * @param map     map
     * @param message 提示
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 参考Class对象isInstance方法
     *
     * @param type   类对象
     * @param obj    对象
     * @param status 状态码
     */
    public static void isInstanceOf(Class<?> type, Object obj, BaseStatus status) {
        if (!type.isInstance(obj)) {
            newException(status);
        }

    }

    /**
     * 参考Class对象isInstance方法
     *
     * @param type    类对象
     * @param obj     对象
     * @param status  状态码
     * @param message 提示
     */
    public static void isInstanceOf(Class<?> type, Object obj, BaseStatus status, String message) {
        if (!type.isInstance(obj)) {
            newException(status, message);
        }

    }

    /**
     * 参考Class对象isInstance方法
     *
     * @param type    类对象
     * @param obj     对象
     * @param message 提示
     */
    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        if (!type.isInstance(obj)) {
            throw new IllegalStateException(message);
        }

    }

    /**
     * 参考Class对象isAssignableFrom方法
     *
     * @param superType 父类
     * @param subType   子类
     * @param status    状态码
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, BaseStatus status) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            newException(status);
        }

    }

    /**
     * 参考Class对象isAssignableFrom方法
     *
     * @param superType 父类
     * @param subType   子类
     * @param status    状态码
     * @param message   提示
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, BaseStatus status, String message) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            newException(status, message);
        }

    }

    /**
     * 参考Class对象isAssignableFrom方法
     *
     * @param superType 父类
     * @param subType   子类
     * @param message   提示
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalStateException();
        }

    }

    /**
     * 比较相等
     *
     * @param s1     字符串1
     * @param s2     字符串2
     * @param flag   是否区分大小写
     * @param status 状态码
     */
    public static void equal(String s1, String s2, boolean flag, BaseStatus status) {
        if (flag) {
            state(s1.equals(s2), status);
        } else {
            state(s1.equalsIgnoreCase(s2), status);
        }
    }

    /**
     * 比较相等
     *
     * @param s1      字符串1
     * @param s2      字符串2
     * @param flag    是否区分大小写
     * @param message 提示
     */
    public static void equal(String s1, String s2, boolean flag, String message) {
        if (flag) {
            state(s1.equals(s2), message);
        } else {
            state(s1.equalsIgnoreCase(s2), message);
        }
    }

    /**
     * 比较相等
     *
     * @param s1      字符串1
     * @param s2      字符串2
     * @param flag    是否区分大小写
     * @param status  状态码
     * @param message 提示
     */
    public static void equal(String s1, String s2, boolean flag, BaseStatus status, String message) {
        if (flag) {
            state(s1.equals(s2), status, message);
        } else {
            state(s1.equalsIgnoreCase(s2), status, message);
        }
    }
}
