package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;


import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageableDtoTests {

    @Test
    void testConstructorWithParameters() {
        PageableDto dto = new PageableDto(1, 20, "name") {
            @Override
            public List<String> getSortableFields() {
                return Arrays.asList("name", "date");
            }

            @Override
            public String getDefaultSortField() {
                return "name";
            }
        };

        assertEquals(1, dto.getPageNumber());
        assertEquals(20, dto.getPageSize());
        assertEquals("name", dto.getSortBy());
    }

    @Test
    void testDefaultConstructor() {
        PageableDto dto = new PageableDto() {
            @Override
            public List<String> getSortableFields() {
                return Arrays.asList("name", "date");
            }

            @Override
            public String getDefaultSortField() {
                return "name";
            }
        };

        assertEquals(0, dto.getPageNumber());
        assertEquals(10, dto.getPageSize());
        assertEquals("name", dto.getSortBy());
    }

    @Test
    void testSettersAndGetters() {
        PageableDto dto = new PageableDto() {
            @Override
            public List<String> getSortableFields() {
                return Arrays.asList("name", "date");
            }

            @Override
            public String getDefaultSortField() {
                return "name";
            }
        };

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
    void testPageSizeBoundaries() {
        PageableDto dto = new PageableDto(-1, 100, "name") {
            @Override
            public List<String> getSortableFields() {
                return Arrays.asList("name", "date");
            }

            @Override
            public String getDefaultSortField() {
                return "name";
            }
        };

        assertEquals(0, dto.getPageNumber());
        assertEquals(50, dto.getPageSize()); // Assuming getMaxPageSize() returns 50
    }
}
