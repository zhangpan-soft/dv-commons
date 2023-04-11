package com.dv.commons.http.impl;

import java.util.HashMap;
import java.util.Map;

public class JsonObject extends HashMap<String, Object> {
    private static final String EMPTY = "\\s*";
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public JsonObject(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public JsonObject(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonObject() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public JsonObject(Map<? extends String, ?> m) {
        super(m);
    }

    public String getAsString(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not null");
        }
        Object o = this.getOrDefault(key, "");
        return String.valueOf(o);
    }

    public Object opt(String key) {
        return key == null ? null : this.get(key);
    }

    public long getAsLong(String key) {
        if (key == null) {
            return 0L;
        }
        String asString = getAsString(key);
        if (isEmpty(asString)) {
            return 0L;
        }
        return Long.parseLong(asString);
    }

    public boolean isEmpty(String target) {
        if (target == null) {
            return true;
        }
        return target.matches(EMPTY);
    }
}
