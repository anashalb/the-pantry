package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
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

    @InjectMocks
    private GetRecipesCreatedByUserHandler handler;

    @Mock
    private IRecipeRepository recipeRepository;

    private UUID testUserId;
    private UserRecipeDto sampleRecipeDto;
    private GetRecipesCreatedByUserDto requestDto;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        sampleRecipeDto = new UserRecipeDto(
                UUID.randomUUID(),
                "Test Recipe",
                "A test recipe description",
                30L,
                15L,
                45L,
                (short) 4
        );

        requestDto = new GetRecipesCreatedByUserDto(
                testUserId,
                0,
                10,
                "name"
        );
    }

    @Test
    void testHandle() {
        Sort sort = Sort.by(Sort.Direction.DESC, requestDto.getSortBy());
        Pageable pageable = PageRequest.of(requestDto.getPageNumber(), requestDto.getPageSize(), sort);
        Page<UserRecipeDto> mockPage = new PageImpl<>(List.of(sampleRecipeDto), pageable, 1);

        when(recipeRepository.getRecipesCreatedByUserId(testUserId, pageable)).thenReturn(mockPage);

        PaginatedResult<UserRecipeDto> result = handler.handle(requestDto);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getCount());
        assertEquals(sampleRecipeDto.getName(), result.getItems().getFirst().getName());

        verify(recipeRepository, times(1)).getRecipesCreatedByUserId(testUserId, pageable);
    }
}
