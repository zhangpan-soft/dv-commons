package com.dv.commons.http;

public interface IHttpUri {
    IHttpParam get(String url);

    IHttpBody post(String url);

    IHttpParam delete(String url);

    IHttpBody put(String url);

    IHttpParam head(String url);

    IHttpBody patch(String url);
}
