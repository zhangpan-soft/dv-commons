package com.dv.commons.http;

import org.apache.http.entity.ContentType;

public interface IHttpBody extends IHttpData {
    IHttpRequestBuilder json(Object json);

    IHttpRequestBuilder json();

    IHttpRequestBuilder body(String body, ContentType contentType);

    IHttpRequestBuilder body();

    IHttpRequestBuilder body(ContentType contentType);

}
