package com.dv.commons.base.strategy;

/**
 * 动作
 * @param <C> Context
 * @param <R> Result
 */
public interface Action<C,R> {
    R apply(C c);
}
