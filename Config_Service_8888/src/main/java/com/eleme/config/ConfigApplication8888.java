package com.eleme.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigApplication8888 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication8888.class, args);
    }
}
