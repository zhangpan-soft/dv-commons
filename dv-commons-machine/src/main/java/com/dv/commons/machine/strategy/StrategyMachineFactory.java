package com.dv.commons.machine.strategy;

/**
 * Strategy Machine Factory
 */
public class StrategyMachineFactory {

    public static <S,C,R> StrategyMachineBuilder<S,C,R> create() {
        return new StrategyMachineBuilderImpl<>();
    }

    public static <S,C,R> StrategyMachine<S,C,R> get(String id) {
        return (StrategyMachine<S, C, R>) StrategyCache.get(id);
    }
}
