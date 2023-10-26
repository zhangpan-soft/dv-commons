package com.dv.commons.jwt;

import lombok.NonNull;

public interface Secret {
    Audience audience(@NonNull String... audience);

    Token token(@NonNull String token);
}
