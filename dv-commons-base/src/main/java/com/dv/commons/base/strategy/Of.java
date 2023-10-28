package com.dv.commons.base.strategy;

/**
 * 关于
 * @param <S>
 * @param <C>
 * @param <R>
 */
public interface Of<S,C,R> {
    When<S,C,R> when(Condition<S,C,R> condition);

    StrategyMachineBuilder<S,C,R> perform(Action<C,R> action);
}
