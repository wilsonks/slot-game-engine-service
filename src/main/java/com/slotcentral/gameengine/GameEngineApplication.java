package com.slotcentral.gameengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GameEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameEngineApplication.class, args);
    }
}
