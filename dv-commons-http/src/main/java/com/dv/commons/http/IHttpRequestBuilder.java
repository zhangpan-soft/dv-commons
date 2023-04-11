package com.dv.commons.http;

import com.dv.commons.http.impl.HttpRequestConfig;

import java.util.Map;

public interface IHttpRequestBuilder {


    IHttpRequestBuilder cookie(Map<String, String> cookies);

    IHttpRequestBuilder header(Map<String, String> headers);

    IHttpRequestBuilder header(String name, String value);

    IHttpRequestBuilder referer(String referer);

    IHttpRequestBuilder userAgent(String userAgent);

    IHttpRequestBuilder autoRedirect();

    IHttpRequestBuilder requestConfig(HttpRequestConfig requestConfig);

    IHttpRequestBuilder global401Callback(Global401Callback global401Callback);

    IHttpRequestBuilder global302Callback(Global302Callback global302Callback);

    IHttpRequestOperator build();
}
