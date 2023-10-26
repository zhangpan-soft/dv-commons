package com.dv.commons.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTDecoderV2;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtSource implements Secret, Audience, Issuer, Token, ExpireAt {

    private static final String BEARER = "Bearer ";

    private String[] audience;
    private String issuer;
    private String token;
    private Date expireAt;
    private String jwtId;
    private String keyId;
    private final Map<String, Object> payload = new HashMap<>();
    private final Algorithm algorithm;

    private JwtSource(String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    private JwtSource(Algorithm algorithm){
        this.algorithm = algorithm;
    }

    public static Secret secret(@NonNull String secret){
        return new JwtSource(secret);
    }

    public static Secret algorithm(@NonNull Algorithm algorithm){
        return new JwtSource(algorithm);
    }


    @Override
    public Audience audience(String... audience) {
        this.audience = audience;
        return this;
    }

    @Override
    public Issuer issuer(String issuer) {
        this.issuer = issuer;
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
    public Operator generator(Map<String, Object> payload, String subject) {
        JWTCreator.Builder builder = JWT.create();
        Date issuedAt = new Date();

        if (payload != null) {
            if (payload.get("jti") != null) {
                this.jwtId = String.valueOf(payload.get("jti"));
            }
            if (payload.get("kid") != null) {
                this.keyId = String.valueOf(payload.get("kid"));
            }
            payload.remove("jti");
            payload.remove("kid");
            this.payload.putAll(payload);
        }
        if (this.jwtId == null || this.jwtId.isBlank()) {
            this.jwtId = UUID.randomUUID().toString().toUpperCase();
        }
        if (this.keyId == null || this.keyId.isBlank()) {
            this.keyId = UUID.randomUUID().toString().toUpperCase();
        }
        builder
                .withExpiresAt(this.expireAt)
                .withIssuedAt(issuedAt)
                .withIssuer(this.issuer)
                .withAudience(this.audience)
                .withJWTId(this.jwtId)
                .withKeyId(this.keyId)
                .withPayload(this.payload);
        if (subject != null) {
            this.payload.put("sub", subject);
            builder.withSubject(subject);
        }
        this.token = BEARER + builder.sign(algorithm);
        return new OperatorImpl(new JWTDecoderV2(this.token.replace(BEARER, "")));
    }

    @Override
    public Operator generator(Map<String, Object> payload) {
        return this.generator(payload, null);
    }

    @Override
    public Operator generator(String subject) {
        return this.generator(null, subject);
    }

    @Override
    public Operator generator() {
        return this.generator(null, null);
    }

    @Override
    public VerifyOperator verify() {
        return new VerifyOperatorImpl(new JWTDecoderV2(this.token.replace(BEARER, "")),JWT.require(algorithm).build());
    }
}