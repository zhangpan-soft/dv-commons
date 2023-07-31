package com.dv.commons.http.impl;

import com.dv.commons.exception.RequestException;
import com.dv.commons.http.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpApiRequest implements IHttpRequestOperator {
    private static final String URI_PATTERN = "^([hH][tT]{2}[pP]:/{2}|[hH][tT]{2}[pP][sS]:/{2})(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~/_])+$";

    /**
     * 声明请求的cookies
     */
    private final Map<String, String> cookies = new HashMap<>();
    /**
     * 声明请求header
     */
    private final Map<String, String> headers = new HashMap<>();
    private ContentType contentType;
    /**
     * 请求的url
     */
    private String url;
    /**
     * 设置referer
     */
    private String referer;
    /**
     * 设置ua
     */
    private String userAgent;
    /**
     * 请求体参数
     */
    private final Map<String, Object> data = new HashMap<>();
    /**
     * url参数
     */
    private final Map<String, String> params = new HashMap<>();
    /**
     * 请求方式
     */
    private HttpMethod method;
    /**
     * 声明请求配置
     */
    private HttpRequestConfig requestConfig;
    /**
     * 自动重定向,默认关闭
     */
    private boolean autoRedirect;
    /**
     * 声明cookieStore
     */
    private final CookieStore cookieStore = new BasicCookieStore();
    /**
     * 声明HttpContext
     */
    private final HttpClientContext httpContext = HttpClientContext.create();
    /**
     * 声明返回构建者
     */
    private final HttpApiResponse.Builder responseBuilder = HttpApiResponse.builder();
    private Global401Callback global401Callback;
    private Global302Callback global302Callback;
    private Object json;
    private String body;


    /**
     * handle Request
     *
     * @return {@link HttpApiResponse}
     */
    @Override
    public HttpApiResponse doRequest() throws RequestException {
        if (!checkUrl(this.url)) {
            throw new RequestException("uri不合法");
        }
        try {
            // 处理请求头
            handleHeaders();
            // 处理cookie
            handleCookies();
            // 设置cookieStore
            this.httpContext.setCookieStore(this.cookieStore);
            // 获取请求
            HttpUriRequest httpUriRequest = getUriRequest();
            // 声明HttpClient
            try (CloseableHttpClient httpClient = getHttpClient()) {
                // 执行请求
                execute(httpClient, httpContext, httpUriRequest, requestConfig.getMaxRedirectTerms());
                // 返回HttpApiResponse
                return responseBuilder.build();
            }
        } catch (Exception e) {
            throw new RequestException(responseBuilder.build(), e);
        }
    }

    /**
     * 检测目标url是否合法
     *
     * @param url url
     * @return true|false
     */
    private boolean checkUrl(String url) {
        if (url == null) {
            return false;
        }
        return (url.matches(URI_PATTERN));
    }

    /**
     * 执行请求
     *
     * @param httpClient     {@link CloseableHttpClient}
     * @param httpContext    {@link HttpClientContext}
     * @param httpUriRequest {@link HttpUriRequest}
     * @param terms          {@link int} 重试次数
     * @throws Exception 执行过程出的异常抛出
     */
    private void execute(CloseableHttpClient httpClient, HttpClientContext httpContext, HttpUriRequest httpUriRequest, int terms) throws Exception {
        // 执行请求
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpUriRequest, httpContext)) {
            // 如果httpResponse为空,则直接结束请求
            if (httpResponse == null) {
                return;
            }
            // 处理response
            handleResponse(httpResponse);
            // 如果状态码为302,则进行重定向
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY && terms > 0 && (this.requestConfig.isAutoRedirect() || this.autoRedirect)) {
                Header location = httpResponse.getFirstHeader(HttpHeaders.LOCATION);
                HttpGet httpGet = new HttpGet(location.getValue());
                httpGet.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
                execute(httpClient, httpContext, httpGet, --terms);
            }
        }
    }


    /**
     * handle Response
     *
     * @param httpResponse {@link HttpResponse}
     * @throws IOException handle Response body exception
     */
    private void handleResponse(HttpResponse httpResponse) throws IOException {
        // http状态码
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        // 处理cookieStore
        List<Cookie> _resCookies = this.cookieStore.getCookies();
        if (_resCookies != null) {
            for (Cookie resCookie : _resCookies) {
                // 加入到返回体中
                responseBuilder.cookie(resCookie.getName(), resCookie.getValue());
            }
        }
        // 处理header
        Header[] allHeaders = httpResponse.getAllHeaders();

        for (Header allHeader : allHeaders) {
            responseBuilder.header(allHeader.getName(), allHeader.getValue());
        }
        Header[] headers = httpResponse.getHeaders(HttpConstants.HEADER_SET_COOKIE_NAME);
        // 找寻所需的值
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                for (HeaderElement element : header.getElements()) {
                    // 如果找到则设置到缓存中
                    responseBuilder.cookie(element.getName(), element.getValue());
                }

            }
        }
        // 处理url和状态码
        responseBuilder.code(statusCode).requestUrl(this.url);
        // 如果返回体不等于空,处理返回体
        if (httpResponse.getEntity() != null) {
            responseBuilder.content(EntityUtils.toByteArray(httpResponse.getEntity()));
        }
        // 如果返回401,处理异常
        if (statusCode == HttpStatus.SC_UNAUTHORIZED && global401Callback != null) {
            global401Callback.callback(this, this.responseBuilder.build());
        }
        // 如果返回302
        if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY && global302Callback != null) {
            Header firstHeader = httpResponse.getFirstHeader(HttpHeaders.LOCATION);
            global302Callback.callback(this, this.responseBuilder.build(), firstHeader.getValue());
        }
    }

    /**
     * 将请求体转为基本表单
     *
     * @return {@link List<NameValuePair>}
     */
    private List<NameValuePair> getNameValuePairs() {
        List<NameValuePair> list = new ArrayList<>();
        this.data.forEach((k, v) -> list.add(new BasicNameValuePair(k, v == null ? "" : String.valueOf(v))));
        return list;
    }

    /**
     * 获得http请求体
     *
     * @return {@link HttpEntity}
     */
    private HttpEntity getHttpEntity() throws RequestException {
        HttpEntity httpEntity;
        if (!this.data.isEmpty()) {
            if (contentType == ContentType.APPLICATION_FORM_URLENCODED) {
                httpEntity = new UrlEncodedFormEntity(getNameValuePairs(), StandardCharsets.UTF_8);
                return httpEntity;
            }
            if (contentType == ContentType.MULTIPART_FORM_DATA) {
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                multipartEntityBuilder.setCharset(StandardCharsets.UTF_8);
                this.data.forEach((k, v) -> {
                    if (v instanceof byte[] vb) {
                        multipartEntityBuilder.addBinaryBody(k, vb);
                    } else if (v instanceof File vf) {
                        multipartEntityBuilder.addBinaryBody(k, vf);
                    } else if (v instanceof InputStream vis) {
                        multipartEntityBuilder.addBinaryBody(k, vis);
                    } else {
                        multipartEntityBuilder.addTextBody(k, String.valueOf(v));
                    }
                });
                httpEntity = multipartEntityBuilder.build();
                return httpEntity;
            }
            throw new RequestException("unsupport content-type");
        }
        StringEntity stringEntity;
        if (this.json != null) {
            if (json instanceof String s) {
                if (StringUtils.isBlank(s)) {
                    s = "{}";
                }
                stringEntity = new StringEntity(s, StandardCharsets.UTF_8);
                stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                httpEntity = stringEntity;
            } else {
                stringEntity = new StringEntity(JacksonUtil.writeValueAsString(json), StandardCharsets.UTF_8);
                stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                httpEntity = stringEntity;
            }
            return httpEntity;
        }

        if (contentType == ContentType.APPLICATION_JSON) {
            if (StringUtils.isBlank(this.body)) {
                this.body = "{}";
            }
        }
        stringEntity = new StringEntity(this.body, StandardCharsets.UTF_8);
        stringEntity.setContentType(contentType.getMimeType());
        httpEntity = stringEntity;
        return httpEntity;
    }


    /**
     * get httpclient need request
     *
     * @return {@link HttpUriRequest}
     * @throws URISyntaxException uri
     */
    private HttpUriRequest getUriRequest() throws URISyntaxException, RequestException {
        // 声明url
        URIBuilder uriBuilder = new URIBuilder(url);
        if (!this.params.isEmpty()) {
            params.forEach(uriBuilder::addParameter);
        }
        HttpUriRequest httpUriRequest;
        switch (method) {
            case GET -> {
                // 如果是get方法,则拼接请求参数
                if (!this.data.isEmpty()) {
                    uriBuilder.addParameters(getNameValuePairs());
                }
                httpUriRequest = new HttpGet(uriBuilder.build());
            }
            case POST -> {
                // 如果是post方法,则声明请求体
                HttpPost httpPost = new HttpPost(uriBuilder.build());
                httpPost.setEntity(getHttpEntity());
                httpUriRequest = httpPost;
            }
            case PUT -> {
                HttpPut httpPut = new HttpPut(uriBuilder.build());
                httpPut.setEntity(getHttpEntity());
                httpUriRequest = httpPut;
            }
            case HEAD -> {
                if (!this.data.isEmpty()) {
                    uriBuilder.addParameters(getNameValuePairs());
                }
                httpUriRequest = new HttpHead(uriBuilder.build());
            }
            case PATCH -> {
                HttpPatch httpPatch = new HttpPatch(uriBuilder.build());
                httpPatch.setEntity(getHttpEntity());
                httpUriRequest = httpPatch;
            }
            case DELETE -> {
                // 如果是delete方法,则拼接请求参数
                if (!this.data.isEmpty()) {
                    uriBuilder.addParameters(getNameValuePairs());
                }
                httpUriRequest = new HttpDelete(uriBuilder.build());
            }
            default ->
                    throw new RequestException(responseBuilder.build(), "unsupport request method [" + method.name() + "]");
        }
        return httpUriRequest;
    }

    /**
     * get httpclient
     *
     * @return {@link CloseableHttpClient}
     * @throws MalformedChallengeException 1
     */
    private CloseableHttpClient getHttpClient() throws MalformedChallengeException {
        List<Header> headerList = new ArrayList<>();
        this.headers.forEach((k, v) -> headerList.add(new BasicHeader(k, v == null ? "" : v)));
        HttpClientBuilder httpClientBuilder = HttpClients
                .custom()
                .setUserAgent(this.userAgent) // 设置ua
                .setDefaultCookieStore(this.cookieStore) // 设置cookieStore
                .setDefaultHeaders(headerList); // 设置header
        if (this.requestConfig == null) this.requestConfig = new HttpRequestConfig();
        // 处理代理
        if (this.requestConfig.getProxy() != null && StringUtils.isNotBlank(this.requestConfig.getProxy().getHost())) {
            HttpHost httpHost = new HttpHost(this.requestConfig.getProxy().getHost(), this.requestConfig.getProxy().getPort());
            httpClientBuilder.setProxy(httpHost);
            if (StringUtils.isNotBlank(this.requestConfig.getProxy().getUsername())) {
                BasicScheme proxyAuth = new BasicScheme();
                proxyAuth.processChallenge(new BasicHeader(AUTH.PROXY_AUTH, "BASIC realm=default"));
                BasicAuthCache authCache = new BasicAuthCache();
                authCache.put(httpHost, proxyAuth);
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(httpHost), new UsernamePasswordCredentials(this.requestConfig.getProxy().getUsername(), this.requestConfig.getProxy().getPassword()));
                httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
            }
        }
        // 处理请求配置
        httpClientBuilder.setDefaultRequestConfig(RequestConfig
                .custom()
                // 设置超时
                .setSocketTimeout(this.requestConfig.getSocketTimeout())
                .setConnectTimeout(this.requestConfig.getConnectTimeout())
                .setConnectionRequestTimeout(this.requestConfig.getConnectRequestTimeout())
                // 禁用自动重定向
                .setRedirectsEnabled(false)
                .build());
        return httpClientBuilder.build();
    }

    /**
     * handle the request Cookie
     */
    private void handleCookies() {
        if (this.cookies.isEmpty()) {
            return;
        }
        JsonObject jsonObject = new JsonObject(this.cookies);
        this.headers.put("Cookie", CookieUtil.toString(jsonObject));
        this.cookies.forEach((k, v) -> this.cookieStore.addCookie(new BasicClientCookie2(k, v)));
    }

    /**
     * handle the request header
     */
    private void handleHeaders() {
        if (StringUtils.isNotBlank(this.referer)) this.headers.put(HttpHeaders.REFERER, this.referer);
        if (StringUtils.isNotBlank(this.userAgent)) this.headers.put(HttpHeaders.USER_AGENT, this.userAgent);
        if (this.headers.keySet().stream().noneMatch(HttpHeaders.ACCEPT::equalsIgnoreCase)) {
            this.headers.put(HttpHeaders.ACCEPT, "application/json, text/javascript, text/html, application/xml, text/xml, */*");
        }
        if (this.headers.keySet().stream().noneMatch(HttpHeaders.REFERER::equalsIgnoreCase)) {
            this.headers.put(HttpHeaders.REFERER, this.url);
        }
        if (this.headers.keySet().stream().noneMatch(HttpHeaders.ACCEPT_LANGUAGE::equalsIgnoreCase)) {
            this.headers.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
        }
        if (this.headers.keySet().stream().noneMatch(HttpHeaders.ACCEPT_ENCODING::equalsIgnoreCase)) {
            this.headers.put(HttpHeaders.ACCEPT_ENCODING, "gzip");
        }
        {
            Optional<String> optional = this.headers.keySet().stream().filter(HttpHeaders.REFERER::equalsIgnoreCase).findFirst();
            this.referer = optional.isEmpty() ? "" : this.headers.get(optional.get());
        }
    }


    private HttpApiRequest() {
        super();
    }

    public static IHttpUri builder() {
        return new Builder();
    }

    public static class Builder implements IHttpUri, IHttpBody, IHttpParam, IHttpRequestBuilder {
        private final HttpApiRequest request;

        private Builder() {
            this.request = new HttpApiRequest();
        }

        @Override
        public Builder data(Map<String, Object> data) {
            if (data == null) return this;
            data.forEach(Builder.this::data);
            return this;
        }


        @Override
        public Builder data() {
            this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            return this;
        }

        @Override
        public Builder data(String name, Object value) {
            if (this.request.contentType == null) {
                this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            }
            if (value instanceof byte[] || value instanceof File) {
                this.request.contentType = ContentType.MULTIPART_FORM_DATA;
            }
            this.request.data.put(name, value);
            return this;
        }

        @Override
        public Builder cookie(Map<String, String> cookies) {
            if (cookies == null) return this;
            this.request.cookies.putAll(cookies);
            return this;
        }

        @Override
        public Builder header(Map<String, String> headers) {
            if (headers == null) return this;
            headers.forEach(Builder.this::header);
            return this;
        }

        @Override
        public Builder header(String name, String value) {
            this.request.headers.put(name, value);
            return this;
        }

        @Override
        public Builder referer(String referer) {
            this.request.referer = referer;
            return this;
        }

        @Override
        public Builder userAgent(String userAgent) {
            this.request.userAgent = userAgent;
            return this;
        }

        @Override
        public Builder autoRedirect() {
            this.request.autoRedirect = true;
            return this;
        }

        @Override
        public Builder requestConfig(HttpRequestConfig requestConfig) {
            this.request.requestConfig = requestConfig;
            return this;
        }

        @Override
        public Builder global401Callback(Global401Callback global401Callback) {
            this.request.global401Callback = global401Callback;
            return this;
        }

        @Override
        public Builder global302Callback(Global302Callback global302Callback) {
            this.request.global302Callback = global302Callback;
            return this;
        }

        @Override
        public IHttpRequestOperator build() {
            return this.request;
        }

        @Override
        public Builder json(Object json) {
            this.request.json = json;
            this.request.contentType = ContentType.APPLICATION_JSON;
            return this;
        }

        @Override
        public Builder json() {
            this.request.contentType = ContentType.APPLICATION_JSON;
            return this;
        }

        @Override
        public Builder body(String body, ContentType contentType) {
            this.request.body = body;
            this.request.contentType = contentType;
            return this;
        }

        @Override
        public Builder body() {
            this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            return this;
        }

        @Override
        public Builder body(ContentType contentType) {
            this.request.contentType = contentType;
            return this;
        }

        @Override
        public Builder param(String name, Object value) {
            this.request.params.put(name, value.toString());
            return this;
        }

        @Override
        public Builder params(Map<String, Object> nameValues) {
            nameValues.forEach(Builder.this::param);
            return this;
        }

        @Override
        public Builder param() {
            return this;
        }

        @Override
        public Builder get(String url) {
            this.request.url = url;
            this.request.method = HttpMethod.GET;
            this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            return this;
        }

        @Override
        public Builder post(String url) {
            this.request.url = url;
            this.request.method = HttpMethod.POST;
            this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            return this;
        }

        @Override
        public Builder delete(String url) {
            this.request.url = url;
            this.request.method = HttpMethod.DELETE;
            this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            return this;
        }

        @Override
        public Builder put(String url) {
            this.request.url = url;
            this.request.method = HttpMethod.PUT;
            this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            return this;
        }

        @Override
        public Builder head(String url) {
            this.request.url = url;
            this.request.method = HttpMethod.HEAD;
            this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            return this;
        }

        @Override
        public Builder patch(String url) {
            this.request.url = url;
            this.request.method = HttpMethod.PATCH;
            this.request.contentType = ContentType.APPLICATION_FORM_URLENCODED;
            return this;
        }
    }

    public static IHttpParam get(String url) {
        return new Builder().get(url);
    }

    public static IHttpBody post(String url) {
        return new Builder().post(url);
    }

    public static IHttpParam delete(String url) {
        return new Builder().delete(url);
    }

    public static IHttpBody put(String url) {
        return new Builder().put(url);
    }

    public static IHttpBody head(String url) {
        return new Builder().head(url);
    }

    public static IHttpBody patch(String url) {
        return new Builder().post(url);
    }

}
