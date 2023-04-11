package com.dv.commons.jwt;

import java.io.IOException;

public interface Token {
    Operator verify() throws IOException;
}
