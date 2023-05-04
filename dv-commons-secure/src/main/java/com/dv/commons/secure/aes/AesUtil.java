package com.dv.commons.secure.aes;

import com.dv.commons.secure.*;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Aes工具类
 */
public class AesUtil implements AesBuilder,Builder, Key, Ciphertext, Plaintext {
    protected static final String ALGORITHM          = "AES";
    /**
     * 算法/模式/补码方式
     */
    protected static final String ALGORITHM_PROVIDER = "AES/ECB/PKCS5Padding";

    private byte[] key;
    private byte[] content;
    private int mode;

    @Override
    public AesKeyGenerator generator(int keySize) {
        return new AesKeyGenerator.AesKeyGeneratorImpl(keySize);
    }

    @Override
    public Key key(String key) {
        this.key = key.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    @Override
    public Key key(byte[] key) {
        this.key = key;
        return this;
    }

    @Override
    public Key base64Key(String key) {
        this.key = Base64.getDecoder().decode(key);
        return this;
    }

    @Override
    public Key base64Key(byte[] key) {
        this.key = Base64.getDecoder().decode(key);
        return this;
    }

    @Override
    public String base64Strings() {
        return Base64.getEncoder().encodeToString(bytes());
    }

    @Override
    public byte[] base64Bytes() {
        return Base64.getEncoder().encode(bytes());
    }

    @Override
    @SneakyThrows
    public byte[] bytes() {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM_PROVIDER);
        if (mode==Cipher.ENCRYPT_MODE){
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(this.content);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(this.content);
        }
        return new byte[0];
    }

    @Override
    public String strings() {
        return new String(bytes(),StandardCharsets.UTF_8);
    }

    @Override
    public Plaintext plaintext(String plaintext) {
        this.content = plaintext.getBytes(StandardCharsets.UTF_8);
        this.mode = Cipher.ENCRYPT_MODE;
        return this;
    }

    @Override
    public Plaintext plaintext(byte[] plaintext) {
        this.content = plaintext;
        this.mode = Cipher.ENCRYPT_MODE;
        return this;
    }

    @Override
    public Plaintext base64Plaintext(String plaintext) {
        this.content = Base64.getDecoder().decode(plaintext);
        this.mode = Cipher.ENCRYPT_MODE;
        return this;
    }

    @Override
    public Plaintext base64Plaintext(byte[] plaintext) {
        this.content = Base64.getDecoder().decode(plaintext);
        this.mode = Cipher.ENCRYPT_MODE;
        return this;
    }

    @Override
    public Ciphertext ciphertext(String ciphertext) {
        this.content = ciphertext.getBytes(StandardCharsets.UTF_8);
        this.mode = Cipher.DECRYPT_MODE;
        return this;
    }

    @Override
    public Ciphertext ciphertext(byte[] ciphertext) {
        this.content = ciphertext;
        this.mode = Cipher.DECRYPT_MODE;
        return this;
    }

    @Override
    public Ciphertext base64Ciphertext(String ciphertext) {
        this.content = Base64.getDecoder().decode(ciphertext);
        this.mode = Cipher.DECRYPT_MODE;
        return this;
    }

    @Override
    public Ciphertext base64Ciphertext(byte[] ciphertext) {
        this.content = Base64.getDecoder().decode(ciphertext);
        this.mode = Cipher.DECRYPT_MODE;
        return this;
    }
}
