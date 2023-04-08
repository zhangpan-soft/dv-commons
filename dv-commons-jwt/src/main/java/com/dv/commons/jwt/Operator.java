package com.dv.commons.jwt;

import java.util.Date;
import java.util.Map;

public interface Operator {

    String getSecret();

    String[] getAudience();

    String getIssure();

    String getToken();

    Date getExpireAt();

    String getJwtId();

    String getKeyId();

    Date getIssuedAt();

    Map<String,?> getPayload();

    boolean getClaimAsBool(String name);

    int getClaimAsInt(String name);

    long getClaimAsLong(String name);

    Object getClaim(String name);

    String getClaimAsString(String name);
}
