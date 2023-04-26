package com.dv.commons.secure;

import com.dv.commons.secure.aes.AesBuilder;
import com.dv.commons.secure.aes.AesUtil;
import com.dv.commons.secure.rsa.RsaBuilder;
import com.dv.commons.secure.rsa.RsaUtil;
import org.apache.commons.codec.digest.DigestUtils;

public class SecureUtil extends DigestUtils {

    public static RsaBuilder rsa(){
        return new RsaUtil();
    }

    public static AesBuilder aes() {
        return new AesUtil();
    }

    public static void main(String[] args) {
        System.out.println(SecureUtil.md5Hex("张三"));;
    }
}
