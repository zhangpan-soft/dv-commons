package com.dv.commons.http;

public interface IHttpUri {
    IHttpData get(String url);

    IHttpBody post(String url);

    IHttpData delete(String url);

    IHttpBody put(String url);

    IHttpData head(String url);

    IHttpBody patch(String url);
}
