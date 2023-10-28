package com.dv.commons;

import com.dv.commons.base.DefaultStatus;
import com.dv.commons.base.strategy.StrategyMachine;
import com.dv.commons.base.strategy.StrategyMachineBuilder;
import com.dv.commons.base.strategy.StrategyMachineFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

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
    private static SpelExpressionParser PARSER = new SpelExpressionParser();
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        MessageFormat format = new MessageFormat("{0}:{1}", Locale.US);
        String format1 = format.format(new Object[]{"中国", "china"});
        System.out.println(format1);
        System.out.println(new MessageFormat("zhangsan").format(null));

        System.out.println(DefaultStatus.OK.message());

        Arrays.stream(DefaultStatus.values())
                .forEach(defaultStatus -> {
                    System.out.println("status."+defaultStatus.code()+"="+defaultStatus.desc());
                });

        StrategyMachineBuilder<String,StrategyContext, Number> machineBuilder = StrategyMachineFactory.create();
        machineBuilder.of("加法").perform(strategyContext -> strategyContext.a+strategyContext.b);
        machineBuilder.of("减法").perform(strategyContext -> strategyContext.a-strategyContext.b);
        machineBuilder.of("乘法").perform(strategyContext -> strategyContext.a*strategyContext.b);
        // 除法,当c==1时,忽略小数位, 当c==2时不忽略
        machineBuilder.of("除法").when((s, strategyContext) -> strategyContext.c==1).perform(strategyContext -> strategyContext.a/strategyContext.b);
        machineBuilder.of("除法").when((s, strategyContext) -> strategyContext.c==2).perform(strategyContext -> (strategyContext.a*1.0d)/(strategyContext.b*1.0d));
        StrategyMachine<String, StrategyContext, Number> strategyMachine = machineBuilder.build("test");
        // StrategyMachine<String, StrategyContext, Number> strategyMachine =  StrategyMachineFactory.get("test");
        System.out.println(strategyMachine.apply("加法",new StrategyContext(1,2,1)));
        System.out.println(strategyMachine.apply("减法",new StrategyContext(1,2,1)));
        System.out.println(strategyMachine.apply("乘法",new StrategyContext(1,2,1)));
        System.out.println(strategyMachine.apply("除法",new StrategyContext(1,2,1)));
        System.out.println(strategyMachine.apply("除法",new StrategyContext(1,2,2)));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrategyContext{
        private int a;
        private int b;
        private int c;
    }
}
