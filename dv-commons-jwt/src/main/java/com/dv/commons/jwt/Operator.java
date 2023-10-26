package com.dv.commons.jwt;

import java.util.Date;
import java.util.Map;

public interface Operator {

    String[] audiences();

    String issuer();

    String token();

    Date expireAt();

    String jti();

    String kid();

    Date issuedAt();

    Map<String, ?> payload();

    boolean claimAsBool(String name);

    int claimAsInt(String name);

    long claimAsLong(String name);

    Object claim(String name);

    String claimAsString(String name);

    String subject();
}
