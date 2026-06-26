package com.eleme.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication13000 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication13000.class, args);
    }
}
