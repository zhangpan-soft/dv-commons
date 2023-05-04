package com.dv.commons.http;

import com.dv.commons.exception.RequestException;
import com.dv.commons.http.impl.HttpApiResponse;

public interface IHttpRequestOperator {
    HttpApiResponse doRequest() throws RequestException;
}
