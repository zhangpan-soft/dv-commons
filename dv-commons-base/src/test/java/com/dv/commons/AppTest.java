package com.dv.commons;

import com.dv.commons.base.DefaultStatus;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    private static SpelExpressionParser PARSER = new SpelExpressionParser();

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        MessageFormat format = new MessageFormat("{0}:{1}", Locale.US);
        String format1 = format.format(new Object[]{"中国", "china"});
        System.out.println(format1);
        System.out.println(new MessageFormat("zhangsan").format(null));

        System.out.println(DefaultStatus.OK.message());

        Arrays.stream(DefaultStatus.values())
                .forEach(defaultStatus -> {
                    System.out.println("status." + defaultStatus.code() + "=" + defaultStatus.desc());
                });
    }
}
