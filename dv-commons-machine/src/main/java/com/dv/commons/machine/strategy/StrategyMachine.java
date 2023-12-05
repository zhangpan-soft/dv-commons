package com.dv.commons.machine.strategy;

/**
 * {@link StrategyMachine}
 * @param <S> strategy
 * @param <C> context
 * @param <R> result
 */
public interface StrategyMachine<S,C,R> {

    R apply(S s, C c);
}
