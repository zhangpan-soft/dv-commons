package com.dv.commons.secure;

public interface Ciphertext {

    String base64Strings();

    byte[] base64Bytes();

    byte[] bytes();

    String strings();

}
