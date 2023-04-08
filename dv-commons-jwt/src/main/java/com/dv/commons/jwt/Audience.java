package com.dv.commons.jwt;

import lombok.NonNull;

public interface Audience {
    Issure issure(@NonNull String issure);
}
