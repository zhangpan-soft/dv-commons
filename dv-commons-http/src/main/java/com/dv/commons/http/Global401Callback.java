package com.dv.commons.http;

import com.dv.commons.http.impl.HttpApiRequest;
import com.dv.commons.http.impl.HttpApiResponse;

/**
 * 全局http-401处理, 如果为空则不处理,
 */
public interface Global401Callback {
    void callback(HttpApiRequest request, HttpApiResponse response);
}
