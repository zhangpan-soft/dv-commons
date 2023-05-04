package com.dv.commons.secure.rsa;

import com.dv.commons.secure.Key;

public interface RsaKey extends Key {
    @Override
    RsaPlaintext plaintext(byte[] plaintext);

    @Override
    RsaPlaintext plaintext(String plaintext);

    @Override
    RsaPlaintext base64Plaintext(byte[] plaintext);

    @Override
    RsaPlaintext base64Plaintext(String plaintext);
}
