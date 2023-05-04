package com.dv.commons.secure;

public interface Key {
    Plaintext plaintext(String plaintext);

    Plaintext plaintext(byte[] plaintext);

    Plaintext base64Plaintext(String plaintext);

    Plaintext base64Plaintext(byte[] plaintext);

    Ciphertext ciphertext(String ciphertext);

    Ciphertext ciphertext(byte[] ciphertext);

    Ciphertext base64Ciphertext(String ciphertext);

    Ciphertext base64Ciphertext(byte[] ciphertext);
}
