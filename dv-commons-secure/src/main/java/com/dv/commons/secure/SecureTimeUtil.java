package com.dv.commons.secure;

import lombok.SneakyThrows;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 秘钥时间工具类
 */
public class SecureTimeUtil {

    private final static byte[] translations = new byte[]{50, 51, 52, 53, 54, 55, 56, 57, 66, 67, 68, 70, 71,
            72, 74, 75, 77, 78, 80, 81, 82, 84, 86, 87, 88, 89};

    /**
     * 利用时间生成code码,
     * @param time 时间,单位秒
     * @param range 范围值 表示多大时间误差是可以接受的, 比如30,单表30秒内生成的code码是一样的,如果<=1,则=1
     * @return code码
     */
    @SneakyThrows
    public static String generatorCode(long time, int range, Key key,String algorithm){
        range = Math.max(1,range);
        time /= range;
        byte[] _code = new byte[8];
        int length = 8;

        while (true) {
            int temp = length - 1;
            if (length == 0) {
                Mac mac = Mac.getInstance(algorithm);
                mac.init(key);
                _code = mac.doFinal(_code);


                int _length = _code[19] & 15;
                byte _b = _code[_length];
                byte _b1 = _code[_length + 1];
                byte _b2 = _code[_length + 2];
                temp = _code[_length + 3] & 255 | (_b2 & 255) << 8 | (_b & 127) << 24 | (_b1 & 255) << 16;
                _code = new byte[5];

                for (_length = 0; _length < 5; ++_length) {
                    byte[] var13 = translations;
                    _code[_length] = var13[temp % var13.length];
                    temp /= var13.length;
                }
                return new String(_code);
            }

            _code[temp] = (byte) ((int) time);
            time >>>= 4;
            length = temp;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        KeyGenerator instance = KeyGenerator.getInstance("HmacSHA1");
        instance.init(256,new SecureRandom());
        SecretKey key = instance.generateKey();
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
        System.out.println(generatorCode(System.currentTimeMillis()/1000,30,key,"HmacSHA1"));

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(),"HmacSHA1");
        System.out.println(generatorCode(System.currentTimeMillis()/1000,30,secretKeySpec,"HmacSHA1"));
    }
}
