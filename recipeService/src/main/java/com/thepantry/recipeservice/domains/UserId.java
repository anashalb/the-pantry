package com.thepantry.recipeservice.domains;

import com.thepantry.recipeservice.domains.common.TypedIdValueBase;

import java.util.UUID;

public class UserId extends TypedIdValueBase {
    protected UserId(UUID value) {
        super(value);
    }
}
