package com.dv.commons.base.exceptions;

import com.dv.commons.base.BaseStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    protected BaseStatus status;
    protected String tips;
    protected BaseException(BaseStatus status, Throwable e){
        super(status.message(),e);
        this.status = status;
    }

    protected BaseException(BaseStatus status){
        super(status.message());
        this.status = status;
    }

    protected BaseException(BaseStatus status, String tips, Throwable e) {
        super(tips,e);
        this.status = status;
    }

    protected BaseException(BaseStatus status, String tips) {
        super(tips);
        this.status = status;
    }

    public static BaseException of(BaseStatus status){
        return new BaseException(status);
    }

    public static BaseException of(BaseStatus status,Throwable e){
        return new BaseException(status,e);
    }

    public static BaseException of(BaseStatus status,String message, Throwable e){
        return new BaseException(status,message,e);
    }

    public static BaseException of(BaseStatus status,String message){
        return new BaseException(status,message);
    }
}
