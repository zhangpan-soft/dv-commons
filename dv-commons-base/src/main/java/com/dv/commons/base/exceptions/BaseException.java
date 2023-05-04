package com.dv.commons.base.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    protected int code;
    protected String message;
    protected BaseException(int code,String message,Throwable e){
        super(message,e);
        this.code = code;
        this.message = message;
    }

    protected BaseException(int code,String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public static BaseException of(int code,String message){
        return new BaseException(code,message);
    }

    public static BaseException of(int code,String message,Throwable e){
        return new BaseException(code,message,e);
    }
}
