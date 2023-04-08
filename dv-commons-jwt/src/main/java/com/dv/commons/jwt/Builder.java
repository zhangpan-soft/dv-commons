package com.dv.commons.jwt;

import lombok.NonNull;

public interface Builder {
    Secret secret(@NonNull String secret);
}
