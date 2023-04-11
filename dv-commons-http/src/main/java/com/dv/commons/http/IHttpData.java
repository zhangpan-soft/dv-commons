package com.dv.commons.http;

import java.util.Map;

public interface IHttpData {
    IHttpRequestBuilder data(String name, Object value);

    IHttpRequestBuilder data(Map<String, Object> nameValues);

    IHttpRequestBuilder data();

}
