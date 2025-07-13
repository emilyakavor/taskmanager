package com.emilyakavor.taskmanager.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JacksonConfig {

    @Bean
    public SimpleModule securityModule() {
        SimpleModule module = new SimpleModule();
        module.addAbstractTypeMapping(
                org.springframework.security.core.GrantedAuthority.class,
                SimpleGrantedAuthority.class
        );
        return module;
    }
}
