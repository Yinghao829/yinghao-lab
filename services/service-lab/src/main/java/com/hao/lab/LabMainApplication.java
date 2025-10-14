package com.hao.lab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan("com.hao")
@MapperScan("com.hao.lab.mapper")
public class LabMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabMainApplication.class, args);
    }
}