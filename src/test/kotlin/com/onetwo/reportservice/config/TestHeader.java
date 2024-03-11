package com.onetwo.reportservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import onetwo.mailboxcommonconfig.common.GlobalStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpHeaders;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@TestConfiguration
public class TestHeader {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${access-id}")
    private String accessId;
    @Value("${access-key}")
    private String accessKey;

    public HttpHeaders getRequestHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(GlobalStatus.ACCESS_ID, accessId);
        httpHeaders.add(GlobalStatus.ACCESS_KEY, accessKey);
        return httpHeaders;
    }

    public HttpHeaders getRequestHeaderWithMockAccessKey(String userId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(GlobalStatus.ACCESS_ID, accessId);
        httpHeaders.add(GlobalStatus.ACCESS_KEY, accessKey);

        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Key key = Keys.hmacShaKeyFor(encodedKey.getBytes());

        Date now = new Date();
        Date validity = new Date(now.getTime() + 100000);

        String token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();

        httpHeaders.add(GlobalStatus.ACCESS_TOKEN, token);

        return httpHeaders;
    }
}
