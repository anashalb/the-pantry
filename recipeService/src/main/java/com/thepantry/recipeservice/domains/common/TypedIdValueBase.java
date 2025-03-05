package com.thepantry.recipeservice.domains.common;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class TypedIdValueBase {
    private final UUID value;

    protected TypedIdValueBase() {
        this.value = UUID.randomUUID();
    }

    protected TypedIdValueBase(UUID value) {
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    public static boolean equals(TypedIdValueBase obj1, TypedIdValueBase obj2) {
        return Objects.equals(obj1, obj2);
    }

    public static boolean notEquals(TypedIdValueBase obj1, TypedIdValueBase obj2) {
        return !equals(obj1, obj2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TypedIdValueBase other = (TypedIdValueBase) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
