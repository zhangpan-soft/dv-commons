package com.dv.commons.secure.rsa;

public interface RsaKeyGenerator<T> {
    T publicKey();

    T privateKey();
}