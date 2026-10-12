package com.tanmaysharma.designledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DesignLedgerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DesignLedgerApplication.class, args);
    }
}
