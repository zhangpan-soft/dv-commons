package com.dv.commons.base.strategy;

import java.util.Map;

/**
 * 缓存
 */
class StrategyCache {

    private static final Map<String,StrategyMachine<?,?,?>> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    public static void put(String id, StrategyMachine<?,?,?> machine) {
        CACHE.put(id, machine);
    }

    public static StrategyMachine<?,?,?> get(String id) {
        return CACHE.get(id);
    }
}
