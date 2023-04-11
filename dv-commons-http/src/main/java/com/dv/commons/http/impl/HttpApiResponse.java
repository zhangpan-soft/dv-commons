package com.dv.commons.http.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http统一返回体
 */
@Getter
@ToString(of = {"code", "requestUrls", "text"})
public class HttpApiResponse {
    /**
     * http状态码
     */
    private int code;
    /**
     * 解压后的返回体
     */
    private byte[] content;
    /**
     * 请求的url集合,如果重定向的话,此会将重定向的所有url加入进来
     */
    private final List<String> requestUrls = new ArrayList<>();
    /**
     * http返回头,如果多次重定向,则多次header都会加入进来
     */
    private final Map<String, String> headers = new HashMap<>();
    /**
     * http返回的cookie,如果多次重定向,会将多次的cookie加入进来, 如果有set-cookie,也会加入进来
     */
    private final Map<String, String> cookies = new HashMap<>();
    /**
     * 字符串的请求体
     */
    private String text;
    /**
     * 请求是否是ok的
     */
    private boolean scOk;
    /**
     * 是否有内容
     */
    private boolean hasContent;

    /**
     * 根据泛型,自动解压返回体
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getBody(Class<T> clazz) {
        if (!hasContent) {
            return null;
        }
        return JacksonUtil.readValue(this.text, clazz);
    }

    /**
     * 根据TypeReference自动解压返回体
     *
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T getBody(TypeReference<T> typeReference) {
        if (!hasContent) {
            return null;
        }
        return JacksonUtil.readValue(this.text, typeReference);
    }

    /**
     * 根据Type,自动解压返回体
     *
     * @param type 泛型的类型
     * @param <T>  泛型
     * @return {@link Response<T>}
     */
    public <T> Response<T> getBody(Type type) {
        if (!hasContent) {
            return null;
        }
        ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type};
            }

            @Override
            public Type getRawType() {
                return Response.class;
            }

            @Override
            public Type getOwnerType() {
                return Response.class;
            }
        };
        return JacksonUtil.readValue(this.text, new TypeReference<>() {
            @Override
            public Type getType() {
                return parameterizedType;
            }
        });
    }

    @Data
    public static class Response<T> {
        private T response;
    }

    private HttpApiResponse() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final HttpApiResponse response;

        private Builder() {
            super();
            this.response = new HttpApiResponse();
        }

        public Builder code(int code) {
            this.response.code = code;
            this.response.scOk = code == HttpStatus.SC_OK;
            return this;
        }

        public Builder content(byte[] content) {
            this.response.content = content;
            this.response.text = content == null ? "" : new String(content, StandardCharsets.UTF_8);
            this.response.hasContent = content != null && content.length > 0 && !"null".equalsIgnoreCase(this.response.text) && StringUtils.isNotBlank(this.response.text);
            return this;
        }

        public Builder requestUrl(String requestUrl) {
            this.response.requestUrls.add(requestUrl);
            return this;
        }

        public Builder requestUrl(List<String> requestUrls) {
            if (requestUrls == null) return this;
            this.response.requestUrls.addAll(requestUrls);
            return this;
        }

        public Builder header(Map<String, String> headers) {
            if (headers == null) return this;
            this.response.headers.putAll(headers);
            return this;
        }

        public Builder cookie(Map<String, String> cookies) {
            if (cookies == null) return this;
            this.response.cookies.putAll(cookies);
            return this;
        }


        public Builder cookie(String name, String value) {
            this.response.cookies.put(name, value);
            return this;
        }

        public Builder header(String name, String value) {
            this.response.headers.put(name, value);
            return this;
        }

        public HttpApiResponse build() {
            return this.response;
        }

    }
}
