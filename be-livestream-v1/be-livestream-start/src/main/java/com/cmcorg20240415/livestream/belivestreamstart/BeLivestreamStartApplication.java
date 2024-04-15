package com.cmcorg20240415.livestream.belivestreamstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BeLivestreamStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeLivestreamStartApplication.class, args);
    }

}
