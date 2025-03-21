package com.thepantry.recipeservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class RecipeServiceApplicationTests {

    @Test
    void contextLoads() {
        String[] args = { "--spring.profiles.active=test" };
        assertDoesNotThrow(() -> RecipeServiceApplication.main(args));
    }

}
