package com.binance.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.binance","com.huobi"},exclude={DataSourceAutoConfiguration.class})
@EnableScheduling
public class ApplicationLoader {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ApplicationLoader.class);
        application.run(args);
    }
}


