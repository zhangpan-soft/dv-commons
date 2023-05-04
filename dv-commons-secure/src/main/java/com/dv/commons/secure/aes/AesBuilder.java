package com.dv.commons.secure.aes;

import com.dv.commons.secure.Builder;

public interface AesBuilder extends Builder {
    AesKeyGenerator generator(int keySize);
}
