package com.smartwardrobe;

import com.smartwardrobe.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@MapperScan("com.smartwardrobe.mapper")
public class SmartWardrobeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartWardrobeApplication.class, args);
    }
}
