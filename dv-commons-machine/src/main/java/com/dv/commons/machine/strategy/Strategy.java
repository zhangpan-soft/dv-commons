package com.dv.commons.machine.strategy;

/**
 * {@link Strategy}
 * @param <S> strategy
 * @param <C> context
 * @param <R> result
 */
public interface Strategy<S, C, R> {
    S strategy();
    Condition<S,C,R> condition();
    Action<C, R> action();

    Strategy<S,C,R> strategy(S s);
    Strategy<S,C,R> condition(Condition<S,C,R> condition);
    Strategy<S,C,R> action(Action<C,R> action);
}
