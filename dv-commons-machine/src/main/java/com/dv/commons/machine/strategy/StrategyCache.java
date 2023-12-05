package com.dv.commons.machine.strategy;

import java.util.Map;

/**
 * {@link StrategyCache}
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
