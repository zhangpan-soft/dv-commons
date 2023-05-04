package com.dv.commons.exception;

import com.dv.commons.http.impl.HttpApiResponse;

public class RequestException extends Exception {
    private HttpApiResponse response;

    public RequestException(String message) {
        super(message);
    }

    public RequestException(HttpApiResponse response, Throwable e) {
        super(e);
        this.response = response;
    }

    public RequestException(HttpApiResponse response, String message) {
        super(message);
        this.response = response;
    }

    public HttpApiResponse getResponse() {
        return response;
    }
}
