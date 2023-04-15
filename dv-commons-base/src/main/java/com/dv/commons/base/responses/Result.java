package com.dv.commons.base.responses;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private String message;
    private Long timestamp;
    private T data;
    private String tips;


    public static <T> Create<T> create(){
        return new BuilderImpl<T>();
    }

    public static <T> Builder<T> ok(){
        return new BuilderImpl<T>().ok();
    }

    public static <T> Builder<T> fail(){
        return new BuilderImpl<T>().fail();
    }

    public static <T> Builder<T> with(int code,String message){
        return new BuilderImpl<T>().with(code,message);
    }


    public interface Builder<T>{

        Builder<T> tips(String tips);

        Result<T> data(T data);

        Result<T> data();
    }

    public interface Create<T>{
        Builder<T> ok();

        Builder<T> fail();

        Builder<T> with(int code,String message);

    }




    public static class BuilderImpl<T> implements Builder<T>,Create<T>{
        private static final int DEFAULT_SUCCESS_CODE = 0;
        private static final String DEFAULT_SUCCESS_MESSAGE = "success";

        private static final int DEFAULT_FAILURE_CODE = -1;
        private static final String DEFAULT_FAILURE_MESSAGE = "failure";

        private int code = DEFAULT_SUCCESS_CODE;
        private String message = DEFAULT_SUCCESS_MESSAGE;
        private String tips;


        @Override
        public Builder<T> tips(String tips) {
            this.tips = tips;
            return this;
        }

        @Override
        public Result<T> data(T data) {
            Result<T> response = new Result<>();
            response.data = data;
            response.tips = this.tips;
            response.code = this.code;
            response.message = this.message;
            return response;
        }

        @Override
        public Result<T> data() {
            return this.data(null);
        }

        @Override
        public Builder<T> ok() {
            return this;
        }

        @Override
        public Builder<T> fail() {
            this.code = DEFAULT_FAILURE_CODE;
            this.message = DEFAULT_FAILURE_MESSAGE;
            return this;
        }

        @Override
        public Builder<T> with(int code, String message) {
            this.code = code;
            this.message = message;
            return this;
        }
    }

}
