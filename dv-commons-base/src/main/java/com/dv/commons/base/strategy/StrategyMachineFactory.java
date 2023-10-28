package com.dv.commons.base.strategy;

/**
 * 策略机工厂
 */
public class StrategyMachineFactory {

    public static <S,C,R> StrategyMachineBuilder<S,C,R> create() {
        return new StrategyMachineBuilderImpl<>();
    }

    public static <S,C,R> StrategyMachine<S,C,R> get(String id) {
        return (StrategyMachine<S, C, R>) StrategyCache.get(id);
    }
}
