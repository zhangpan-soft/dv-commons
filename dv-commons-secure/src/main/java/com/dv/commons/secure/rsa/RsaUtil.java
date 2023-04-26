package com.dv.commons.secure.rsa;

import com.dv.commons.secure.*;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * rsa工具类
 */
public class RsaUtil implements RsaBuilder, RsaKey, Ciphertext, RsaPlaintext {

    protected static final String KEY_ALGORITHM = "RSA";
    protected static final String SIGN_ALGORITHM = "SHA256withRSA";

    private byte[] key;
    private byte[] content;

    private int mode;


    @Override
    public RsaGenerator generator(int keySize) {
        return new RsaGenerator.RsaGeneratorImpl(keySize);
    }

    @Override
    public RsaKey key(String key) {
        this.key = key.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    @Override
    public RsaKey key(byte[] key) {
        this.key = key;
        return this;
    }

    @Override
    public RsaKey base64Key(String key) {
        this.key = Base64.getDecoder().decode(key);
        return this;
    }

    @Override
    public RsaKey base64Key(byte[] key) {
        this.key = Base64.getDecoder().decode(key);
        return this;
    }

    @Override
    @SneakyThrows
    public String base64Strings() {
       return Base64.getEncoder().encodeToString(bytes());
    }

    @Override
    @SneakyThrows
    public byte[] base64Bytes() {
        return Base64.getEncoder().encode(bytes());
    }

    @Override
    @SneakyThrows
    public byte[] bytes() {
        if (this.mode==Cipher.ENCRYPT_MODE){
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM)
                    .generatePublic(new X509EncodedKeySpec(this.key));
            //RSA加密
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(this.content);
        }else if (this.mode==Cipher.DECRYPT_MODE){
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM)
                    .generatePrivate(new PKCS8EncodedKeySpec(this.key));
            //RSA解密
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return cipher.doFinal(this.content);
        }
        return new byte[0];
    }

    @Override
    public String strings() {
        return new String(bytes(),StandardCharsets.UTF_8);
    }

    @Override
    @SneakyThrows
    public Sign sign() {
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(this.key));
        signature.initSign(priKey);
        signature.update(this.content);
        byte[] sign = signature.sign();
        return new Sign.SignImpl(sign);
    }

    @Override
    public boolean verify(Sign sign) {
        try {
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM)
                    .generatePublic(new X509EncodedKeySpec(this.key));
            signature.initVerify(pubKey);
            signature.update(this.content);
            return signature.verify(sign.bytes());
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public RsaPlaintext plaintext(String plaintext) {
        this.content = plaintext.getBytes(StandardCharsets.UTF_8);
        this.mode = Cipher.ENCRYPT_MODE;
        return this;
    }

    @Override
    public RsaPlaintext plaintext(byte[] plaintext) {
        this.content = plaintext;
        this.mode = Cipher.ENCRYPT_MODE;
        return this;
    }

    @Override
    public RsaPlaintext base64Plaintext(String plaintext) {
        this.content = Base64.getDecoder().decode(plaintext);
        this.mode = Cipher.ENCRYPT_MODE;
        return this;
    }

    @Override
    public RsaPlaintext base64Plaintext(byte[] plaintext) {
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
