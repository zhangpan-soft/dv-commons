package com.dv.commons.http.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HttpRequestConfig {
    private static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    private static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 2000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 6000;
    private static final int DEFAULT_MAX_REDIRECT_TERMS = 6;
    private int connectTimeout;
    private int connectRequestTimeout;
    private int socketTimeout;
    private HttpProxy proxy;
    private boolean autoRedirect;
    private int maxRedirectTerms;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HttpProxy {
        private String host;
        private int port;
        private String username;
        private String password;
    }

    public int getConnectRequestTimeout() {
        return connectRequestTimeout <= 0L ? DEFAULT_CONNECT_REQUEST_TIMEOUT : connectRequestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout <= 0 ? DEFAULT_CONNECT_TIMEOUT : connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout <= 0 ? DEFAULT_SOCKET_TIMEOUT : socketTimeout;
    }

    public int getMaxRedirectTerms() {
        return maxRedirectTerms <= 0 ? DEFAULT_MAX_REDIRECT_TERMS : maxRedirectTerms;
    }
}
