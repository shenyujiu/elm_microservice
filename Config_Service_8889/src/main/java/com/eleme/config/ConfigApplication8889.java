package com.eleme.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigApplication8889 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication8889.class, args);
    }
}
