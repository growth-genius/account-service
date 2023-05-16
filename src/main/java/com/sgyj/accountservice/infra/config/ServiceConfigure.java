package com.sgyj.accountservice.infra.config;

import com.sgyj.accountservice.infra.properties.JwtProperties;
import com.sgyj.accountservice.infra.security.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfigure {

    @Bean
    public Jwt jwt(JwtProperties jwtProperties) {
        return new Jwt(jwtProperties.getIssuer(), jwtProperties.getClientSecret());
    }
}
