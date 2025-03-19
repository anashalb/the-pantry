package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageableDtoTests {

    @Test
    void testConstructorWithParameters_ShouldInitializeCorrectly_WhenValuesAreValid() {
        ConcretePageableDto dto = new ConcretePageableDto(1, 20, "name");

        assertEquals(1, dto.getPageNumber());
        assertEquals(20, dto.getPageSize());
        assertEquals("name", dto.getSortBy());
    }

    @Test
    void testConstructorWithParameters_ShouldSetDefaultValues_WhenValuesAreInvalid() {
        ConcretePageableDto dto = new ConcretePageableDto(-1, 100, "invalid");

        assertEquals(dto.getDefaultPageSize(), dto.getPageSize());
        assertEquals(dto.getDefaultPageNumber(), dto.getPageNumber());
        assertEquals(dto.getDefaultSortField(), dto.getSortBy());
    }

    @Test
    void testDefaultConstructor_ShouldSetDefaultValues() {
        ConcretePageableDto dto = new ConcretePageableDto();

        assertEquals(dto.getDefaultPageNumber(), dto.getPageNumber());
        assertEquals(dto.getDefaultPageSize(), dto.getPageSize());
        assertEquals(dto.getDefaultSortField(), dto.getSortBy());
    }

    @Test
    void testSettersAndGetters_ShouldReturnSetValues_WhenSetValuesAreValid() {
        ConcretePageableDto dto = new ConcretePageableDto();

        dto.setPageNumber(2);
        dto.setPageSize(15);
        dto.setSortBy("date");
        dto.setSortDirection(Sort.Direction.DESC);

        assertEquals(2, dto.getPageNumber());
        assertEquals(15, dto.getPageSize());
        assertEquals("date", dto.getSortBy());
        assertEquals(Sort.Direction.DESC, dto.getSortDirection());
    }

    @Test
    void testSetPageSize_ShouldSetDefaultPageSize_WhenPageSizeIsMoreThanMaxPageSize() {
        ConcretePageableDto dto = new ConcretePageableDto();
        dto.setPageSize(dto.getMaxPageSize()+1);
        assertEquals(dto.getDefaultPageNumber(), dto.getPageNumber());
    }

    static class ConcretePageableDto extends PageableDto {

        public ConcretePageableDto(int pageNumber, int pageSize, String sortBy) {
            super(pageNumber, pageSize, sortBy);
        }

        public ConcretePageableDto() {
            super();
        }

        @Override
        public List<String> getSortableFields() {
            return Arrays.asList("name", "date");
        }

        @Override
        public String getDefaultSortField() {
            return "name";
        }
    }
}
