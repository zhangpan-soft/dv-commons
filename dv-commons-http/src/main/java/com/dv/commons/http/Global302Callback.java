package com.dv.commons.http;

import com.dv.commons.http.impl.HttpApiRequest;
import com.dv.commons.http.impl.HttpApiResponse;

/**
 * 全局302处理,如果为空,不影响设置的自动重定向等,
 * 此等于全局额外处理,可暴露出去用户自定义
 */
public interface Global302Callback {
    void callback(HttpApiRequest request, HttpApiResponse response, String location);
}
