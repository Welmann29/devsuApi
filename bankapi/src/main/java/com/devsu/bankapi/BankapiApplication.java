package com.devsu.bankapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.devsu.bankapi.entities")
@SpringBootApplication
public class BankapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankapiApplication.class, args);
    }

}
