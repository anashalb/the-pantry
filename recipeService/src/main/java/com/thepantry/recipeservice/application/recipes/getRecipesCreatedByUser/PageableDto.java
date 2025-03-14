package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Setter
@Getter
public abstract class PageableDto {

    private int pageNumber;
    private int pageSize;
    private String sortBy;
    private Sort.Direction sortDirection;

    public PageableDto(int pageNumber, int pageSize, String sortBy) {
        this.pageNumber = Math.max(0, pageNumber);
        this.pageSize = Math.max(1, Math.min(pageSize, this.getMaxPageSize()));
        this.sortBy = this.getSortableFields().contains(sortBy) ? sortBy : this.getDefaultSortField();
    }

    public PageableDto() {
        this.pageNumber = this.getDefaultPageNumber();
        this.pageSize = this.getDefaultPageSize();
        this.sortBy = this.getDefaultSortField();
    }

    public abstract List<String> getSortableFields();

    public abstract String getDefaultSortField();

    public int getDefaultPageNumber() {
        return 0;
    }

    public int getDefaultPageSize() {
        return 10;
    }

    public int getMaxPageSize() {
        return 50;
    }
}