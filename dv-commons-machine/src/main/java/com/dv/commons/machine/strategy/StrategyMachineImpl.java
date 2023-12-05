package com.dv.commons.machine.strategy;

import java.util.List;
import java.util.Map;

/**
 * Strategy Machine Impl
 * @param <S> strategy
 * @param <C> context
 * @param <R> result
 */
class StrategyMachineImpl<S,C,R> implements StrategyMachine<S,C,R> {

    private final Map<S, List<Strategy<S,C,R>>> map;

    public StrategyMachineImpl(Map<S, List<Strategy<S,C,R>>> map){
        this.map = map;
    }


    @Override
    public R apply(S s, C c) {
        List<Strategy<S, C, R>> strategies = map.get(s);
        if (strategies==null||strategies.isEmpty()){
            throw new RuntimeException("no strategy found for "+s);
        }

        for (Strategy<S, C, R> strategy : strategies) {
            // 如果没有condition，直接执行action
            if (strategy.condition()==null) {
                return strategy.action().apply(c);
            }
            // 如果有condition，先判断是否满足condition，满足则执行action
            if (strategy.condition().isSatisfied(s,c)){
                return strategy.action().apply(c);
            }
        }
        // 未发现策略关于s的condition
        throw new RuntimeException("no strategy found of met condition for "+s);
    }


}
