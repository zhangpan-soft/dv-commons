package com.dv.commons.jwt;

import java.util.Map;

public interface ExpireAt {
    Generator generator(Map<String, Object> payload);

    Generator generator();
}
