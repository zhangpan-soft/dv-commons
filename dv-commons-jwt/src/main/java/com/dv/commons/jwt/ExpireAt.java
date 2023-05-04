package com.dv.commons.jwt;

import java.util.Map;

public interface ExpireAt {
    Operator generator(Map<String, Object> payload, String subject);

    Operator generator(Map<String, Object> payload);

    Operator generator(String subject);

    Operator generator();
}
