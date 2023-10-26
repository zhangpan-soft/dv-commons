package com.dv.commons.jwt;

import lombok.NonNull;

import java.util.Date;

public interface Issuer {
    ExpireAt expireAt(@NonNull Date expireAt);
}
