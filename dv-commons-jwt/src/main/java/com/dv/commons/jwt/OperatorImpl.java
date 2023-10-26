package com.dv.commons.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

class OperatorImpl implements Operator {
    private final DecodedJWT decodedJWT;

    OperatorImpl(DecodedJWT decodedJWT) {
        this.decodedJWT = decodedJWT;
    }


    @Override
    public String[] audiences() {
        return this.decodedJWT.getAudience().toArray(new String[0]);
    }

    @Override
    public String issuer() {
        return this.decodedJWT.getIssuer();
    }

    @Override
    public String token() {
        return this.decodedJWT.getToken();
    }

    @Override
    public Date expireAt() {
        return this.decodedJWT.getExpiresAt();
    }

    @Override
    public String jti() {
        return this.decodedJWT.getId();
    }

    @Override
    public String kid() {
        return this.decodedJWT.getKeyId();
    }

    @Override
    public Date issuedAt() {
        return this.decodedJWT.getIssuedAt();
    }

    @Override
    public Map<String, ?> payload() {
        return this.decodedJWT.getClaims();
    }

    @Override
    public boolean claimAsBool(String name) {
        return this.decodedJWT.getClaim(name).asBoolean();
    }

    @Override
    public int claimAsInt(String name) {
        return this.decodedJWT.getClaim(name).asInt();
    }

    @Override
    public long claimAsLong(String name) {
        return this.decodedJWT.getClaim(name).asLong();
    }

    @Override
    public Object claim(String name) {
        return this.decodedJWT.getClaim(name).as(Object.class);
    }

    @Override
    public String claimAsString(String name) {
        return this.decodedJWT.getClaim(name).asString();
    }

    @Override
    public String subject() {
        return this.decodedJWT.getSubject();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("jwt:{");
        if (this.audiences() == null) {
            sb.append("audiences=null");
        } else {
            sb.append("audiences=").append(String.join(",", this.audiences()));
        }
        sb.append(",issuer=").append(this.issuer());
        sb.append(",token=").append(this.token());
        sb.append(",expireAt=").append(this.expireAt().getTime());
        sb.append(",jwtId=").append(this.jti());
        sb.append(",keyId=").append(this.kid());
        sb.append(",issuedAt=").append(this.issuedAt().getTime());
        sb.append(",subject=").append(this.subject());
        sb.append(",payload={");
        this.payload().forEach((k, v) -> {
            if (v.getClass().isArray()) {
                if (Objects.equals(v.getClass().getComponentType().getTypeName(), String.class.getTypeName())) {
                    String[] vv = (String[]) v;
                    sb.append(k).append("=").append(String.join(",", vv)).append(",");
                } else {
                    sb.append(k).append("=").append(v).append(",");
                }
            } else {
                sb.append(k).append("=").append(v).append(",");
            }
        });
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }
}
