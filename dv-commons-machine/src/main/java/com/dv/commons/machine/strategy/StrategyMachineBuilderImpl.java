package com.dv.commons.machine.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link StrategyMachineBuilderImpl}
 * @param <S> strategy
 * @param <C> context
 * @param <R> result
 */
class StrategyMachineBuilderImpl<S,C,R> implements StrategyMachineBuilder<S,C,R>{
    private final Map<S, List<Strategy<S,C,R>>> map = new ConcurrentHashMap<>();


    @Override
    public Of<S, C, R> of(S s) {
        map.computeIfAbsent(s, k -> new ArrayList<>());
        Strategy<S,C,R> strategy = new StrategyImpl();
        map.get(s).add(strategy);
        return new OfImpl(strategy);
    }

    @Override
    public StrategyMachine<S, C, R> build(String id) {
        StrategyMachineImpl<S, C, R> machine = new StrategyMachineImpl<>(map);
        StrategyCache.put(id, machine);
        return machine;
    }

    public class OfImpl implements Of<S,C,R>{
        private final Strategy<S,C,R> strategy;
        OfImpl(Strategy<S,C,R> strategy){
            this.strategy = strategy;
        }

        @Override
        public When<S, C, R> when(Condition<S,C,R> condition) {
            this.strategy.condition(condition);
            return new WhenImpl(strategy);
        }

        @Override
        public StrategyMachineBuilder<S, C, R> perform(Action<C, R> action) {
            this.strategy.action(action);
            return StrategyMachineBuilderImpl.this;
        }
    }

    public class WhenImpl implements When<S,C,R> {

        private final Strategy<S,C,R> strategy;
        WhenImpl(Strategy<S,C,R> strategy){
            this.strategy = strategy;
        }

        @Override
        public StrategyMachineBuilder<S, C, R> perform(Action<C, R> action) {
            this.strategy.action(action);
            return StrategyMachineBuilderImpl.this;
        }
    }

    public class StrategyImpl implements Strategy<S, C, R> {
        private S strategy;
        private Condition<S,C,R> condition;
        private Action<C, R> action;


        @Override
        public S strategy() {
            return this.strategy;
        }

        @Override
        public Condition<S,C,R> condition() {
            return this.condition;
        }

        @Override
        public Action<C, R> action() {
            return this.action;
        }

        @Override
        public Strategy<S, C, R> strategy(S s) {
            this.strategy = s;
            return this;
        }

        @Override
        public Strategy<S, C, R> condition(Condition<S,C,R> condition) {
            this.condition = condition;
            return this;
        }

        @Override
        public Strategy<S, C, R> action(Action<C, R> action) {
            this.action = action;
            return this;
        }
    }
}
