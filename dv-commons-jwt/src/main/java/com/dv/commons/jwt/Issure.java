package com.dv.commons.jwt;

import lombok.NonNull;

import java.util.Date;

public interface Issure {
    Token token(@NonNull String token);

    ExpireAt expireAt(@NonNull Date expireAt);
}
