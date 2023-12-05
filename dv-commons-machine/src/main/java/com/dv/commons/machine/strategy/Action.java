package com.dv.commons.machine.strategy;

/**
 * {@link Action}
 * @param <C> context
 * @param <R> result
 */
public interface Action<C,R> {
    R apply(C c);
}
