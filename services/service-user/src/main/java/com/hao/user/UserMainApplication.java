package com.hao.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;


@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.hao.user.mapper")
@ComponentScan("com.hao")
public class UserMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserMainApplication.class, args);
    }
}