package com.dv.commons.secure;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface Sign {
    String base64Strings();

    byte[] base64Bytes();

    byte[] bytes();

    String strings();

    class SignImpl implements Sign{

        private final byte[] sign;

        public SignImpl(byte[] sign){
            this.sign = sign;
        }

        @Override
        public String base64Strings() {
            return Base64.getEncoder().encodeToString(sign);
        }

        @Override
        public byte[] base64Bytes() {
            return Base64.getEncoder().encode(sign);
        }

        @Override
        public byte[] bytes() {
            return sign;
        }

        @Override
        public String strings() {
            return new String(sign, StandardCharsets.UTF_8);
        }
    }

}
