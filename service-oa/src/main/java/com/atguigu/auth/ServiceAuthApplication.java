package com.atguigu.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author l moonlight
 * @create 2023-03-18 18:34
 */
@SpringBootApplication
@ComponentScan("com.atguigu")
public class ServiceAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class,args);
    }
}
