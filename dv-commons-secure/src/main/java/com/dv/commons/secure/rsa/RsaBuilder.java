package com.dv.commons.secure.rsa;

import com.dv.commons.secure.Builder;

public interface RsaBuilder extends Builder {
    RsaGenerator generator(int keySize);

    @Override
    RsaKey key(byte[] key);

    @Override
    RsaKey key(String key);

    @Override
    RsaKey base64Key(byte[] key);

    @Override
    RsaKey base64Key(String key);
}