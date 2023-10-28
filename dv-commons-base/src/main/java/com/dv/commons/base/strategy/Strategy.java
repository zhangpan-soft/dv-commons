package com.dv.commons.base.strategy;

/**
 * 策略
 * @param <S>
 * @param <C>
 * @param <R>
 */
public interface Strategy<S, C, R> {
    S strategy();
    Condition<S,C,R> condition();
    Action<C, R> action();

    Strategy<S,C,R> strategy(S s);
    Strategy<S,C,R> condition(Condition<S,C,R> condition);
    Strategy<S,C,R> action(Action<C,R> action);
}
