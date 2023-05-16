package com.sgyj.accountservice.infra.properties;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties( "app" )
public class AppProperties {

    List<String> hosts;
}
