package com.lhj.bookstore.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.p6spy.engine.spy.P6SpyOptions;

/**
 * query multi line custom configure
 */
@Configuration
public class P6spyLogMessageFormatConfig {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(CustomP6spySqlFormat.class.getName());
    }
}

