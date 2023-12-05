package com.dv.commons.machine.strategy;

/**
 * {@link When}
 * @param <S> strategy
 * @param <C> context
 * @param <R> result
 */
public interface When<S,C,R> {

    StrategyMachineBuilder<S,C,R> perform(Action<C,R> action);
}
