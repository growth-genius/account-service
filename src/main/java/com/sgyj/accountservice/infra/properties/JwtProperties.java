package com.sgyj.accountservice.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("jwt.token")
public class JwtProperties {

    private String header;

    private String issuer;

    private String clientSecret;

    private String expirySeconds;
}
