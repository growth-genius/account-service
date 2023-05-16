package com.sgyj.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableDiscoveryClient
public class AccountServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
