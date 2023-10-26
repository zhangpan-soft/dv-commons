package com.dv.commons.jwt;

import lombok.NonNull;

public interface Audience {
    Issuer issuer(@NonNull String issuer);
}
