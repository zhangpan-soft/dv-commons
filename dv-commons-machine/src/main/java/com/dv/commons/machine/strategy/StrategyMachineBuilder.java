package com.dv.commons.machine.strategy;

/**
 * {@link StrategyMachineBuilder}
 * @param <S> strategy
 * @param <C> context
 * @param <R> result
 */
public interface StrategyMachineBuilder<S,C,R> {

    Of<S,C,R> of(S s);

    StrategyMachine<S,C,R> build(String id);
}
