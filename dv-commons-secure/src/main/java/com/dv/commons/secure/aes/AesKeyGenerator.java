package com.dv.commons.secure.aes;

import com.dv.commons.secure.Generator;
import lombok.SneakyThrows;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

public interface AesKeyGenerator extends Generator {
    byte[] bytes();

    String base64();

    class AesKeyGeneratorImpl implements AesKeyGenerator {
        private final SecretKey secretKey;

        @SneakyThrows
        public AesKeyGeneratorImpl(int keySize) {
            KeyGenerator instance = KeyGenerator.getInstance(AesUtil.ALGORITHM);
            instance.init(keySize,new SecureRandom());
            this.secretKey = instance.generateKey();
        }

        @Override
        public byte[] bytes() {
            return this.secretKey.getEncoded();
        }

        @Override
        public String base64() {
            return Base64.getEncoder().encodeToString(this.secretKey.getEncoded());
        }
    }
}
