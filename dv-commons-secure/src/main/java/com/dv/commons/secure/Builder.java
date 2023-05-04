package com.dv.commons.secure;

/**
 * 基础构建者,定义规范
 */
public interface Builder {

    /**
     *
     * @param keySize
     * @return
     */
    Generator generator(int keySize);

    Key key(String key);

    Key key(byte[] key);

    Key base64Key(String key);

    Key base64Key(byte[] key);
}
