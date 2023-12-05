package com.dv.commons;

import com.dv.commons.machine.strategy.StrategyMachine;
import com.dv.commons.machine.strategy.StrategyMachineBuilder;
import com.dv.commons.machine.strategy.StrategyMachineFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        StrategyMachineBuilder<String, StrategyContext, Number> machineBuilder = StrategyMachineFactory.create();
        machineBuilder.of("加法").perform(strategyContext -> strategyContext.a + strategyContext.b);
        machineBuilder.of("减法").perform(strategyContext -> strategyContext.a - strategyContext.b);
        machineBuilder.of("乘法").perform(strategyContext -> strategyContext.a * strategyContext.b);
        // 除法,当c==1时,忽略小数位, 当c==2时不忽略
        machineBuilder.of("除法").when((s, strategyContext) -> strategyContext.c == 1).perform(strategyContext -> strategyContext.a / strategyContext.b);
        machineBuilder.of("除法").when((s, strategyContext) -> strategyContext.c == 2).perform(strategyContext -> (strategyContext.a * 1.0d) / (strategyContext.b * 1.0d));
        StrategyMachine<String, StrategyContext, Number> strategyMachine = machineBuilder.build("test");
        // StrategyMachine<String, StrategyContext, Number> strategyMachine =  StrategyMachineFactory.get("test");
        System.out.println(strategyMachine.apply("加法", new StrategyContext(1, 2, 1)));
        System.out.println(strategyMachine.apply("减法", new StrategyContext(1, 2, 1)));
        System.out.println(strategyMachine.apply("乘法", new StrategyContext(1, 2, 1)));
        System.out.println(strategyMachine.apply("除法", new StrategyContext(1, 2, 1)));
        System.out.println(strategyMachine.apply("除法", new StrategyContext(1, 2, 2)));
        MedicineStrategy.get().apply("test", new MedicineStrategy.MedicineContext(12));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrategyContext {
        private int a;
        private int b;
        private int c;
    }

    class MedicineStrategy {
        private static StrategyMachine<String, MedicineContext, Void> strategy;

        static {
            StrategyMachineBuilder<String, MedicineContext, Void> machineBuilder = StrategyMachineFactory.create();
            strategy = machineBuilder
                    .of("").when((s, c) -> c.age < 12).perform((c) -> {
                        System.out.println("Under the age of 12, take 20 milligrams of medication per day;");
                        return Void.TYPE.cast(null);
                    })
                    .of("").when((s, c) -> c.age >= 12 && c.age < 18).perform((c) -> {
                        System.out.println("12-18 years old, taking 30 milligrams a day");
                        return Void.TYPE.cast(null);
                    })
                    .of("").when((s, c) -> c.age >= 18 && c.age < 30).perform((c) -> {
                        System.out.println("18-30 years old, taking 40 milligrams a day");
                        return Void.TYPE.cast(null);
                    })
                    .of("").when((s, c) -> c.age >= 30 && c.age < 50).perform((c) -> {
                        System.out.println("30-50 years old, taking 45 milligrams a day");
                        return Void.TYPE.cast(null);
                    })
                    .of("").when((s, c) -> c.age >= 50).perform((c) -> {
                        System.out.println("Eating 42 milligrams for those over 50 years old");
                        return Void.TYPE.cast(null);
                    })
                    .build("medicine");
        }

        public static StrategyMachine<String, MedicineContext, Void> get() {
            // StrategyMachine<String, MedicineContext, Void> strategy = StrategyMachineFactory.get("medicine");
            return strategy;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MedicineContext {
            private int age;
        }

    }
}
