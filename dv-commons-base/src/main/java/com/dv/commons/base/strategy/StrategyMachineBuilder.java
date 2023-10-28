package com.dv.commons.base.strategy;

/**
 * 策略机建造者
 * @param <S>
 * @param <C>
 * @param <R>
 */
public interface StrategyMachineBuilder<S,C,R> {

    Of<S,C,R> of(S s);

    StrategyMachine<S,C,R> build(String id);
}
