package com.dv.commons.test;

import com.dv.commons.jwt.JwtUtil;
import com.dv.commons.jwt.Operator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AppTests {
    @Test
    public void test() throws IOException {
        String key = UUID.randomUUID().toString().toUpperCase();
        Date expireAt = new Date(System.currentTimeMillis() + 10 * 1000);
        Map<String,Object> payload = new HashMap<>();
        payload.put("sub","123123123");
        payload.put("test","test");
        // 加签,并获取最终对象
        Operator operator = JwtUtil.builder().secret(key).audience("admin").issure("admin").expireAt(expireAt).generator(payload).build();
        // 打印Token
        System.out.println(operator.getToken());
        System.out.println(operator);
        // 验签,并获取对象
        Operator operator1 = JwtUtil.builder().secret(key).audience("admin").issure("admin").token(operator.getToken()).verify().build();
        System.out.println(operator1);
    }
}
