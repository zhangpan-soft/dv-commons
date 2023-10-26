package com.dv.commons.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

class VerifyOperatorImpl extends OperatorImpl implements VerifyOperator {
    private final JWTVerifier jwtVerifier;
    private final DecodedJWT decodedJWT;
    VerifyOperatorImpl(DecodedJWT decodedJWT, JWTVerifier jwtVerifier) {
        super(decodedJWT);
        this.jwtVerifier = jwtVerifier;
        this.decodedJWT = decodedJWT;
    }

    @Override
    public void verify() {
        DecodedJWT verify = this.jwtVerifier.verify(decodedJWT);
        if (verify==null){
            throw new JWTVerificationException("verify failed");
        }
    }
}
