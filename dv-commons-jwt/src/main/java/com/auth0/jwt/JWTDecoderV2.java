package com.auth0.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Header;
import com.auth0.jwt.interfaces.Payload;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JWTDecoderV2 implements DecodedJWT, Serializable {
    private static final long serialVersionUID = 1873362438023312895L;
    private final String[] parts;
    private final Header header;
    private final Payload payload;

    public JWTDecoderV2(String jwt) throws JWTDecodeException {
        this(new JWTParser(), jwt);
    }

    public JWTDecoderV2(JWTParser converter, String jwt) throws JWTDecodeException {
        this.parts = TokenUtils.splitToken(jwt);

        String headerJson;
        String payloadJson;
        try {
            headerJson = new String(Base64.getUrlDecoder().decode(this.parts[0]), StandardCharsets.UTF_8);
            payloadJson = new String(Base64.getUrlDecoder().decode(this.parts[1]), StandardCharsets.UTF_8);
        } catch (NullPointerException var6) {
            throw new JWTDecodeException("The UTF-8 Charset isn't initialized.", var6);
        } catch (IllegalArgumentException var7) {
            throw new JWTDecodeException("The input is not a valid base 64 encoded string.", var7);
        }

        this.header = converter.parseHeader(headerJson);
        this.payload = converter.parsePayload(payloadJson);
    }

    public String getAlgorithm() {
        return this.header.getAlgorithm();
    }

    public String getType() {
        return this.header.getType();
    }

    public String getContentType() {
        return this.header.getContentType();
    }

    public String getKeyId() {
        return this.header.getKeyId();
    }

    public Claim getHeaderClaim(String name) {
        return this.header.getHeaderClaim(name);
    }

    public String getIssuer() {
        return this.payload.getIssuer();
    }

    public String getSubject() {
        return this.payload.getSubject();
    }

    public List<String> getAudience() {
        return this.payload.getAudience();
    }

    public Date getExpiresAt() {
        return this.payload.getExpiresAt();
    }

    public Instant getExpiresAtAsInstant() {
        return this.payload.getExpiresAtAsInstant();
    }

    public Date getNotBefore() {
        return this.payload.getNotBefore();
    }

    public Instant getNotBeforeAsInstant() {
        return this.payload.getNotBeforeAsInstant();
    }

    public Date getIssuedAt() {
        return this.payload.getIssuedAt();
    }

    public Instant getIssuedAtAsInstant() {
        return this.payload.getIssuedAtAsInstant();
    }

    public String getId() {
        return this.payload.getId();
    }

    public Claim getClaim(String name) {
        return this.payload.getClaim(name);
    }

    public Map<String, Claim> getClaims() {
        return this.payload.getClaims();
    }

    public String getHeader() {
        return this.parts[0];
    }

    public String getPayload() {
        return this.parts[1];
    }

    public String getSignature() {
        return this.parts[2];
    }

    public String getToken() {
        return String.format("%s.%s.%s", this.parts[0], this.parts[1], this.parts[2]);
    }
}
