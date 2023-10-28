package com.dv.commons.base.strategy;

/**
 * 语法糖
 * @param <S>
 * @param <C>
 * @param <R>
 */
public interface When<S,C,R> {

    StrategyMachineBuilder<S,C,R> perform(Action<C,R> action);
}
