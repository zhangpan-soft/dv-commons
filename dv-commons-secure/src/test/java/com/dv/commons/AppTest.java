package com.dv.commons;

import com.dv.commons.secure.SecureTimeUtil;
import com.dv.commons.secure.SecureUtil;
import com.dv.commons.secure.aes.AesKeyGenerator;
import com.dv.commons.secure.rsa.RsaGenerator;
import com.dv.commons.secure.rsa.RsaKeyGenerator;
import com.dv.commons.secure.sign.SignType;
import com.dv.commons.secure.sign.SignUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testGeneratorCode() throws NoSuchAlgorithmException {
        KeyGenerator instance = KeyGenerator.getInstance("HmacSHA1");
        instance.init(256, new SecureRandom());
        SecretKey key = instance.generateKey();
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
        System.out.println(SecureTimeUtil.generatorCode(System.currentTimeMillis() / 1000, 30, key, "HmacSHA1"));

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "HmacSHA1");
        System.out.println(SecureTimeUtil.generatorCode(System.currentTimeMillis() / 1000, 30, secretKeySpec, "HmacSHA1"));

    }

    public void testGenerate() {

        RsaGenerator generator = SecureUtil.rsa().generator(2048);
        {
            RsaKeyGenerator<String> rsaKeyGenerator = generator.base64();
            System.out.println(rsaKeyGenerator.publicKey());
            System.out.println(rsaKeyGenerator.privateKey());
        }

        {
            RsaKeyGenerator<String> xml = generator.xml();
            System.out.println(xml.publicKey());
            System.out.println(xml.privateKey());
        }

        {
            System.out.println("-------------------");
            RsaKeyGenerator<byte[]> bytes = generator.bytes();
            System.out.println(new String(bytes.publicKey(), StandardCharsets.UTF_8));
            System.out.println("++++++++++++++++++++");
            System.out.println(new String(bytes.privateKey(), StandardCharsets.UTF_8));
            System.out.println("--------------------");
        }

        String strings = SecureUtil.rsa().base64Key(generator.base64().publicKey()).plaintext("这是个测试").base64Strings();
        System.out.println(strings);
        String strings1 = SecureUtil.rsa().base64Key(generator.base64().privateKey()).base64Ciphertext(strings).strings();
        System.out.println(strings1);

    }

    public void testSecret(){
        {
            RsaGenerator generator = SecureUtil.rsa().generator(2048);
            RsaKeyGenerator<String> keyGenerator = generator.base64();
            String publicKey = keyGenerator.publicKey();
            String privateKey = keyGenerator.privateKey();

            System.out.printf("公钥:%s\n",publicKey);
            System.out.printf("私钥:%s\n",privateKey);

            String ciphertext = SecureUtil.rsa().base64Key(publicKey).plaintext("这是个测试").base64Strings();
            System.out.printf("密文:%s\n", ciphertext);
            String plaintext = SecureUtil.rsa().base64Key(privateKey).base64Ciphertext(ciphertext).strings();
            System.out.printf("明文:%s\n", plaintext);
        }

        {
            AesKeyGenerator generator = SecureUtil.aes().generator(256);
            String s = generator.base64();
            System.out.printf("秘钥:%s\n",s);
            String ciphertext = SecureUtil.aes().base64Key(s).plaintext("这是个测试").base64Strings();
            System.out.printf("密文:%s\n", ciphertext);
            String plaintext = SecureUtil.aes().base64Key(s).base64Ciphertext(ciphertext).strings();
            System.out.printf("明文:%s\n", plaintext);
        }

    }

    public void testSign(){
        {
            String key = UUID.randomUUID().toString();
            Map<String, String> signMap = SignUtils.signMap(new HashMap<String, String>() {{
                this.put("test", "123");
            }}, key, SignType.HMACSHA256);
            System.out.println(signMap);
            boolean valid = SignUtils.isValid(signMap, key);
            System.out.println(valid);
        }

        {
            String key = UUID.randomUUID().toString();
            Map<String, String> signMap = SignUtils.signMap(new HashMap<String, String>() {{
                this.put("test", "123");
            }}, key, SignType.MD5);
            System.out.println(signMap);
            boolean valid = SignUtils.isValid(signMap, key);
            System.out.println(valid);
        }

        {
            RsaKeyGenerator<String> generator = SecureUtil.rsa().generator(2048).base64();
            Map<String, String> signMap = SignUtils.signMap(new HashMap<String, String>() {{
                this.put("test", "123");
            }}, generator.privateKey(), SignType.RSA2);
            System.out.println(signMap);
            boolean valid = SignUtils.isValid(signMap, generator.publicKey());
            System.out.println(valid);
        }

    }

    public void testAes() {
        System.out.println(SecureUtil.aes().key(DigestUtils.sha256("234567")).plaintext("123456").base64Strings());;
    }
}
