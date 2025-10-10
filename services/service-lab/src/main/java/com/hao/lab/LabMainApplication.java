package com.hao.lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LabMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabMainApplication.class, args);
    }
}