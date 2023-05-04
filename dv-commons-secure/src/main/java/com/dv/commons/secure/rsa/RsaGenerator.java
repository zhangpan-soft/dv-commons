package com.dv.commons.secure.rsa;

import com.dv.commons.secure.Generator;
import lombok.SneakyThrows;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.dv.commons.secure.rsa.RsaUtil.KEY_ALGORITHM;

public interface RsaGenerator extends Generator {
    RsaKeyGenerator<byte[]> bytes();

    RsaKeyGenerator<String> base64();

    RsaKeyGenerator<String> xml();

    KeyPair keyPair();
    
    final class RsaGeneratorImpl implements RsaGenerator {


        private final KeyPair keyPair;

        @SneakyThrows
        public RsaGeneratorImpl(int keySize) {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(keySize, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            this.keyPair = keyPairGen.generateKeyPair();
        }

        @Override
        public RsaKeyGenerator<byte[]> bytes() {
            return new RsaKeyGenerator<>() {
                @Override
                public byte[] publicKey() {
                    return keyPair.getPublic().getEncoded();
                }

                @Override
                public byte[] privateKey() {
                    return keyPair.getPrivate().getEncoded();
                }
            };
        }

        @Override
        public RsaKeyGenerator<String> base64() {
            return new RsaKeyGenerator<>() {
                @Override
                public String publicKey() {
                    return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                }

                @Override
                public String privateKey() {
                    return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                }
            };
        }

        @Override
        public RsaKeyGenerator<String> xml() {
            return new RsaKeyGenerator<>() {
                @Override
                @SneakyThrows
                public String publicKey() {

                    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                    RSAPublicKey pukKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyPair.getPublic().getEncoded()));
                    return ("<RSAKeyValue>" +
                            "<Modulus>" +
                            Base64.getEncoder().encodeToString(pukKey.getModulus().toByteArray()) +
                            "</Modulus>" +
                            "<Exponent>" +
                            Base64.getEncoder().encodeToString(pukKey.getPublicExponent().toByteArray()) +
                            "</Exponent>" +
                            "</RSAKeyValue>")
                            .replaceAll("[ \t\n\r]", "");
                }

                @Override
                @SneakyThrows
                public String privateKey() {


                    PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(
                            keyPair.getPrivate().getEncoded());
                    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                    RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory
                            .generatePrivate(pvkKeySpec);

                    return ("<RSAKeyValue>" +
                            "<Modulus>" +
                            Base64.getEncoder().encodeToString(pvkKey.getModulus().toByteArray()) +
                            "</Modulus>" +
                            "<Exponent>" +
                            Base64.getEncoder().encodeToString(pvkKey.getPublicExponent().toByteArray()) +
                            "</Exponent>" +
                            "<P>" +
                            Base64.getEncoder().encodeToString(pvkKey.getPrimeP().toByteArray()) +
                            "</P>" +
                            "<Q>" +
                            Base64.getEncoder().encodeToString(pvkKey.getPrimeQ().toByteArray()) +
                            "</Q>" +
                            "<DP>" +
                            Base64.getEncoder().encodeToString(pvkKey.getPrimeExponentP().toByteArray()) +
                            "</DP>" +
                            "<DQ>" +
                            Base64.getEncoder().encodeToString(pvkKey.getPrimeExponentQ().toByteArray()) +
                            "</DQ>" +
                            "<InverseQ>" +
                            Base64.getEncoder().encodeToString(pvkKey.getCrtCoefficient().toByteArray()) +
                            "</InverseQ>" +
                            "<D>" +
                            Base64.getEncoder().encodeToString(pvkKey.getPrivateExponent().toByteArray()) +
                            "</D>" +
                            "</RSAKeyValue>")
                            .replaceAll("[ \t\n\r]", "");
                }
            };
        }

        @Override
        public KeyPair keyPair() {
            return this.keyPair;
        }
    }
}
