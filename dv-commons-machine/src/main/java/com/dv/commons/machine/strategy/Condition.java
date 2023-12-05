package com.dv.commons.machine.strategy;

/**
 * {@link Condition}
 * @param <S> strategy
 * @param <C> context
 * @param <R> result
 */
public interface Condition<S,C,R> {

    boolean isSatisfied(S s,C c);
}
