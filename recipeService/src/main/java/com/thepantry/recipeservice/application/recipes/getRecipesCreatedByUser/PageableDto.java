package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
public abstract class PageableDto {

    private final static short MIN_PAGE_NUMBER = 0;
    private final static short DEFAULT_PAGE_NUMBER = 0;

    private final static short MIN_PAGE_SIZE = 1;
    private final static short MAX_PAGE_SIZE = 50;
    private final static short DEFAULT_PAGE_SIZE = 50;

    private int pageNumber;
    private int pageSize;
    private String sortBy;

    @Setter
    private Sort.Direction sortDirection;

    public PageableDto(int pageNumber, int pageSize, String sortBy) {
        this.setPageNumber(pageNumber);
        this.setPageSize(pageSize);
        this.setSortBy(sortBy);
    }

    public PageableDto() {
        this.setPageNumber(this.getDefaultPageNumber());
        this.setPageSize(this.getDefaultPageSize());
        this.setSortBy(this.getDefaultSortField());
    }

    /**
     * Sets the page number. If the page number is less than 0, it will be set to 0.
     * @param pageNumber The page number to set.
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = Math.max(MIN_PAGE_NUMBER, pageNumber);;
    }

    /**
     * Sets the page size. If the page size is less than 1, it will be set to 1. If the page size
     * is greater than getMaxPageSize, it will be set to getMaxPageSize.
     * @param pageSize The page size to set.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = Math.max(MIN_PAGE_SIZE, Math.min(pageSize, this.getMaxPageSize()));
    }

    /**
     * Sets the sort by field. If the sort by field is not in the list of getSortableFields,
     * it will be set to getDefaultSortField.
     * @param sortBy The sort by field to set.
     */
    public void setSortBy(String sortBy) {
        this.sortBy = this.getSortableFields().contains(sortBy) ? sortBy : this.getDefaultSortField();
    }

    public abstract List<String> getSortableFields();

    public abstract String getDefaultSortField();

    public int getDefaultPageNumber() {
        return DEFAULT_PAGE_NUMBER;
    }

    public int getDefaultPageSize() {
        return DEFAULT_PAGE_SIZE;
    }

    public int getMaxPageSize() {
        return MAX_PAGE_SIZE;
    }
}