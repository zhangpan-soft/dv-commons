package com.dv.commons.secure;

public interface Signature {
    Sign sign();

    boolean verify(Sign sign);
}
