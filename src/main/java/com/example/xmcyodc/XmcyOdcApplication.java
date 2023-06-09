package com.example.xmcyodc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class XmcyOdcApplication {

    public static void main(String[] args) {
        SpringApplication.run(XmcyOdcApplication.class, args);
    }

}
