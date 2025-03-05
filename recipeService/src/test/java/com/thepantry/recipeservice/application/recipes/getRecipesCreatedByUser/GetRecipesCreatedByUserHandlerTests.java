package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRecipesCreatedByUserHandlerTests {

    @Mock
    private IRecipeRepository recipeRepository;

    @InjectMocks
    private GetRecipesCreatedByUserHandler handler;

    private UUID testUserId;
    private RecipeEntity sampleRecipe;
    private GetRecipesCreatedByUserDto requestDto;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        sampleRecipe = new RecipeEntity();
        sampleRecipe.setRecipeId(UUID.randomUUID());
        sampleRecipe.setName("Test Recipe");
        sampleRecipe.setDescription("A test recipe description");
        sampleRecipe.setCookingTimeMinutes(30L);
        sampleRecipe.setPreparationTimeMinutes(15L);
        sampleRecipe.setReadyInTimeMinutes(45L);
        sampleRecipe.setServings((short) 4);

        requestDto = new GetRecipesCreatedByUserDto(
                testUserId,
                0,
                10,
                "name"
        );
    }

    @Test
    void testHandle() {
        // Arrange
        Sort sort = Sort.by(Sort.Direction.DESC, requestDto.getSortBy());
        Pageable pageable = PageRequest.of(requestDto.getPageNumber(), requestDto.getPageSize(), sort);
        Page<RecipeEntity> mockPage = new PageImpl<>(List.of(sampleRecipe), pageable, 1);

        when(recipeRepository.getRecipesCreatedByUserId(testUserId, pageable)).thenReturn(mockPage);

        // Act
        PaginatedResult<UserRecipeDto> result = handler.handle(requestDto);

        // Assert
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getCount());
        assertEquals(sampleRecipe.getName(), result.getItems().iterator().next().getName());

        verify(recipeRepository, times(1)).getRecipesCreatedByUserId(testUserId, pageable);
    }
}