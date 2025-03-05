package com.thepantry.recipeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RecipeServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(RecipeServiceApplication.class, args);
    }
}
