package com.dv.commons.http;

import org.apache.http.entity.ContentType;

import java.util.Map;

public interface IHttpBody extends IHttpParam {
    IHttpRequestBuilder json(Object json);

    IHttpRequestBuilder json();

    IHttpRequestBuilder body(String body, ContentType contentType);

    IHttpRequestBuilder body();

    IHttpRequestBuilder body(ContentType contentType);

    IHttpBody param(String name, Object value);

    IHttpRequestBuilder params(Map<String, Object> nameValues);

    IHttpRequestBuilder param();

    IHttpBody data(String name, Object value);

    IHttpRequestBuilder data(Map<String, Object> nameValues);

    IHttpRequestBuilder data();
}
