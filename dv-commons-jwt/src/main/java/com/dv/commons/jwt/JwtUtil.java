package com.dv.commons.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.*;

public class JwtUtil implements Builder, Secret, Audience, Issure, Token, ExpireAt, Generator, Verify, Operator {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String BEARER = "Bearer ";

    private String secret;
    private String[] audience;
    private String issure;
    private String token;
    private Date expireAt;
    private String jwtId;
    private String keyId;
    private Date issuedAt;
    private final Map<String, Object> payload = new HashMap<>();
    private JWTVerifier jwtVerifier;
    private DecodedJWT decodedJWT;

    private JwtUtil() {

    }

    public static Builder builder() {
        return new JwtUtil();
    }

    @Override
    public Secret secret(String secret) {
        this.secret = secret;
        return this;
    }

    @Override
    public Audience audience(String... audience) {
        this.audience = audience;
        return this;
    }

    @Override
    public Issure issure(String issure) {
        this.issure = issure;
        return this;
    }

    @Override
    public Token token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public ExpireAt expireAt(Date expireAt) {
        this.expireAt = expireAt;
        return this;
    }

    @Override
    public Generator generator(Map<String, Object> payload) {
        JWTCreator.Builder builder = JWT.create();
        this.issuedAt = new Date();
        this.jwtId = UUID.randomUUID().toString().toUpperCase();
        this.keyId = UUID.randomUUID().toString().toUpperCase();
        if (payload != null) {
            this.payload.putAll(payload);
        }
        builder
                .withExpiresAt(this.expireAt)
                .withIssuedAt(this.issuedAt)
                .withIssuer(this.issure)
                .withAudience(this.audience)
                .withJWTId(this.jwtId)
                .withKeyId(this.keyId)
                .withPayload(this.payload)
        ;
        this.token = BEARER+builder.sign(Algorithm.HMAC256(this.secret));
        this.payload.put("iss", this.issure);
        this.payload.put("aud", this.audience);
        this.payload.put("exp", this.expireAt.getTime() / 1000);
        this.payload.put("jti", this.jwtId);
        this.payload.put("iat", this.issuedAt.getTime()/1000);
        return this;
    }

    @Override
    public Generator generator() {
        return this.generator(null);
    }

    @Override
    public Verify verify() throws IOException {
        if (this.jwtVerifier == null) {
            this.jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();

        }
        if (this.decodedJWT == null) {
            this.decodedJWT = jwtVerifier.verify(token.replace(BEARER,""));
            this.audience = this.decodedJWT.getAudience().toArray(new String[0]);
            this.issure = this.decodedJWT.getIssuer();
            this.issuedAt = this.decodedJWT.getIssuedAt();
            this.expireAt = this.decodedJWT.getExpiresAt();
            this.jwtId = this.decodedJWT.getId();
            this.keyId = this.decodedJWT.getKeyId();
            this.payload.putAll(OBJECT_MAPPER.readValue(Base64.getUrlDecoder().decode(this.decodedJWT.getPayload()), new TypeReference<Map<String, Object>>() {
            }));
        }
        return this;
    }

    @Override
    public Operator build() {
        return this;
    }

    @Override
    public String getSecret() {
        return this.secret;
    }

    @Override
    public String[] getAudience() {
        return this.audience;
    }

    @Override
    public String getIssure() {
        return this.issure;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public Date getExpireAt() {
        return this.expireAt;
    }

    @Override
    public String getJwtId() {
        return this.jwtId;
    }

    @Override
    public String getKeyId() {
        return this.keyId;
    }

    @Override
    public Date getIssuedAt() {
        return this.issuedAt;
    }

    @Override
    public Map<String, ?> getPayload() {
        return this.payload;
    }

    @Override
    public boolean getClaimAsBool(String name) {
        String claimAsString = this.getClaimAsString(name);
        if (claimAsString == null) return false;
        return Boolean.parseBoolean(claimAsString);
    }

    @Override
    public int getClaimAsInt(String name) {
        String claimAsString = this.getClaimAsString(name);
        if (claimAsString == null) return 0;
        return Integer.parseInt(claimAsString);
    }

    @Override
    public long getClaimAsLong(String name) {
        String claimAsString = this.getClaimAsString(name);
        if (claimAsString == null) return 0;
        return Long.parseLong(claimAsString);
    }

    @Override
    public Object getClaim(String name) {
        return this.payload.get(name);
    }

    @Override
    public String getClaimAsString(String name) {
        if (name == null) return null;
        return String.valueOf(this.getClaim(name));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("jwt:{");
        if (this.audience == null) {
            sb.append("audience=null");
        } else {
            sb.append("audience=").append(String.join(",",this.audience));
        }
        sb.append(",issure=").append(this.issure);
        sb.append(",token=").append(this.token);
        sb.append(",expireAt=").append(this.expireAt);
        sb.append(",jwtId=").append(this.jwtId);
        sb.append(",keyId=").append(this.keyId);
        sb.append(",issuedAt=").append(this.issuedAt);
        sb.append(",payload={");
        this.payload.forEach((k, v) -> {
            if (v.getClass().isArray()){
                if (Objects.equals(v.getClass().getComponentType().getTypeName(), String.class.getTypeName())){
                    String[] vv = (String[]) v;
                    sb.append(k).append("=").append(String.join(",", vv)).append(",");
                }else {
                    sb.append(k).append("=").append(v).append(",");
                }
            }else {
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
