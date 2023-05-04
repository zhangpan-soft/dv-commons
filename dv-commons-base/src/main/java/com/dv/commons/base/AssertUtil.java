package com.dv.commons.base;

import com.dv.commons.base.exceptions.BaseException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class AssertUtil {

    public static void notEmpty(Object target,int code,String message){
        if (target==null) throw BaseException.of(code,message);
        if (target instanceof String s){
            if (s.isBlank()) throw BaseException.of(code, message);
        }else if (target instanceof Collection<?> c){
            if (c.isEmpty()) throw BaseException.of(code, message);
        }else if (target instanceof Map<?,?> m){
            if (m.isEmpty()) throw BaseException.of(code,message);
        }
    }

    public static void equals(Object source,Object target,int code,String message){
        if (!Objects.equals(source,target)) throw BaseException.of(code, message);
    }
}
