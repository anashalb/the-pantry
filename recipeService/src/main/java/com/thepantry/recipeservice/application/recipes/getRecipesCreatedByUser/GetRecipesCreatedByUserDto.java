package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class GetRecipesCreatedByUserDto extends PageableDto {

    private final UUID userId;

    public GetRecipesCreatedByUserDto(UUID userId, int pageNumber, int pageSize, String sortBy) {
        super(pageNumber, pageSize, sortBy);
        this.userId = userId;
    }

    @Override
    public String getDefaultSortField() {
        return "createdAt";
    }

    @Override
    public List<String> getSortableFields() {
        return List.of("createdAt", "createdBy", "updatedAt");
    }
}
