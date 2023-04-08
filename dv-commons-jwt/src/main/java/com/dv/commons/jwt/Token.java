package com.dv.commons.jwt;

import java.io.IOException;

public interface Token {
    Verify verify() throws IOException;
}
