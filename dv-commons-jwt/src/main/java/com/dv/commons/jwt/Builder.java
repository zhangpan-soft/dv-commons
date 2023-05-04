package com.dv.commons.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.NonNull;

public interface Builder {
    Secret secret(@NonNull String secret);

    Secret algorithm(@NonNull Algorithm algorithm);
}
