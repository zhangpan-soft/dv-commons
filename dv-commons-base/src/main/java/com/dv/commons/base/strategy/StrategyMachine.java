package com.dv.commons.base.strategy;

/**
 * 策略机
 * @param <S>
 * @param <C>
 * @param <R>
 */
public interface StrategyMachine<S,C,R> {

    R apply(S s, C c);
}
