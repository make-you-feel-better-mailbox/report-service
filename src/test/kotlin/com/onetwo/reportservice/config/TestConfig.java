package com.onetwo.reportservice.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import onetwo.mailboxcommonconfig.common.domain.RoleNames;
import onetwo.mailboxcommonconfig.common.filter.AccessKeyCheckFilter;
import onetwo.mailboxcommonconfig.common.jwt.TokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.anyRequest().permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    @Primary
    public TokenProvider tokenProvider() {
        return new TokenProvider() {
            @Override
            public Authentication getAuthentication(String token) {
                return new UsernamePasswordAuthenticationToken(token, token);
            }

            @Override
            public Claims getClaimsByToken(String token) {
                return null;
            }

            @Override
            public boolean validateToken(String token) {
                return false;
            }
        };
    }

    @Bean
    @Primary
    public AccessKeyCheckFilter accessKeyCheckFilter(Environment environment) {
        return new TestAccessKeyCheckFilter(environment);
    }

    class TestAccessKeyCheckFilter extends AccessKeyCheckFilter {

        public TestAccessKeyCheckFilter(Environment environment) {
            super(environment);
        }

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            filterChain.doFilter(request, response);
        }
    }

    public Set<GrantedAuthority> getGrantedAuthoritiesByUserId(String userId) {
        Set<GrantedAuthority> authorities = new HashSet();
        authorities.add(new SimpleGrantedAuthority(RoleNames.ROLE_USER.getValue()));
        return authorities;
    }
}
