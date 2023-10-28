package com.dv.commons.base.strategy;

/**
 * 条件
 * @param <S>
 * @param <C>
 * @param <R>
 */
public interface Condition<S,C,R> {

    boolean isSatisfied(S s,C c);
}
