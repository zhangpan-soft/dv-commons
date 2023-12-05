package com.dv.commons.machine.strategy;

/**
 * {@link Of}
 * @param <S> strategy
 * @param <C> context
 * @param <R> result
 */
public interface Of<S,C,R> {
    When<S,C,R> when(Condition<S,C,R> condition);

    StrategyMachineBuilder<S,C,R> perform(Action<C,R> action);
}
