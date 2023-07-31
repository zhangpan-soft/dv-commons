package com.dv.commons.http;

import java.util.Map;

public interface IHttpParam {
    IHttpParam param(String name, Object value);

    IHttpRequestBuilder params(Map<String, Object> nameValues);

    IHttpRequestBuilder param();

}
