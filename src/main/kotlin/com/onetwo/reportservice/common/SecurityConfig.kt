package com.onetwo.reportservice.common

import onetwo.mailboxcommonconfig.common.RequestMatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun requestMatcher(mvc: MvcRequestMatcher.Builder): RequestMatcher {
        return RequestMatcher {
            val mvcRequestMatcherList: MutableList<MvcRequestMatcher> = ArrayList()
            mvcRequestMatcherList
        }
    }
}