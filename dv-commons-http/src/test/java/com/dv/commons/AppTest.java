package com.dv.commons;

import com.dv.commons.exception.RequestException;
import com.dv.commons.http.impl.HttpApiRequest;
import com.dv.commons.http.impl.HttpApiResponse;
import com.dv.commons.http.impl.HttpRequestConfig;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;

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
    public void testApp() throws RequestException {
        HttpApiResponse response = HttpApiRequest.builder().get("https://www.baidu.com").data().build().doRequest();
        System.out.println(response);
    }

    public void test() throws RequestException{
        System.out.println(HttpApiRequest.builder().get("https://www.baidu.com").data().build().doRequest());
        System.out.println(HttpApiRequest.builder().get("https://www.baidu.com").data("test","test").build().doRequest());
        System.out.println(HttpApiRequest.builder().get("https://www.baidu.com").data(new HashMap<>()).autoRedirect().global302Callback((request, response, location) -> {

        }).global401Callback((request, response) -> {

        }).cookie(new HashMap<>())
                .header("s","s")
                .referer("s").userAgent("s")
                .requestConfig(HttpRequestConfig.builder().proxy(HttpRequestConfig.HttpProxy.builder().host("").port(80).build()).build()).build()
                .doRequest());
//        System.out.println(HttpApiRequest.builder().post("").json(new HashMap<>()).build().doRequest());
    }
}
