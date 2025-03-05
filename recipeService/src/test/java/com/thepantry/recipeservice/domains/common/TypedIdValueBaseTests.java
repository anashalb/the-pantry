package com.thepantry.recipeservice.domains.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TypedIdValueBaseTests {

    private UUID uuid1;
    private UUID uuid2;
    private TypedIdValueBase id1;
    private TypedIdValueBase id2;
    private TypedIdValueBase id3;

    @BeforeEach
    void setUp() {
        uuid1 = UUID.randomUUID();
        uuid2 = UUID.randomUUID();
        id1 = new ConcreteTypedId(uuid1);
        id2 = new ConcreteTypedId(uuid1); // Same UUID as id1
        id3 = new ConcreteTypedId(uuid2); // Different UUID
    }

    @Test
    void testEquals_ShouldReturnTrueForSameObjectInstance() {
        assertEquals(id1, id1, "An instance should be equal to itself");
    }

    @Test
    void testEquals_ShouldReturnTrueForSameUUID() {
        assertEquals(id1, id2, "Two instances with the same UUID should be equal");
    }

    @Test
    void testEquals_ShouldReturnFalseForDifferentUUIDs() {
        assertNotEquals(id1, id3, "Instances with different UUIDs should not be equal");
    }

    @Test
    void testHashCode_ShouldBeSameForEqualObjects() {
        assertEquals(id1.hashCode(), id2.hashCode(), "Equal objects must have the same hash code");
    }

    @Test
    void testHashCode_ShouldBeDifferentForDifferentObjects() {
        assertNotEquals(id1.hashCode(), id3.hashCode(), "Different objects should have different hash codes");
    }

    @Test
    void testEqualsMethod_ShouldHandleNull() {
        assertNotEquals(id1, null, "An object should not be equal to null");
    }

    @Test
    void testEqualsMethod_ShouldHandleDifferentClass() {
        assertNotEquals(id1, "String", "An object should not be equal to a different class");
    }

    @Test
    void testStaticEquals_ShouldReturnTrueForEqualObjects() {
        assertTrue(TypedIdValueBase.equals(id1, id2), "Static equals method should return true for equal objects");
    }

    @Test
    void testStaticEquals_ShouldReturnFalseForDifferentObjects() {
        assertFalse(TypedIdValueBase.equals(id1, id3), "Static equals method should return false for different objects");
    }

    @Test
    void testStaticEquals_ShouldReturnTrueForBothNull() {
        assertTrue(TypedIdValueBase.equals(null, null), "Static equals method should return true when both are null");
    }

    @Test
    void testStaticEquals_ShouldReturnFalseIfOneIsNull() {
        assertFalse(TypedIdValueBase.equals(id1, null), "Static equals method should return false when one is null");
    }

    @Test
    void testStaticNotEquals_ShouldReturnFalseForEqualObjects() {
        assertFalse(TypedIdValueBase.notEquals(id1, id2), "Static notEquals method should return false for equal objects");
    }

    @Test
    void testStaticNotEquals_ShouldReturnTrueForDifferentObjects() {
        assertTrue(TypedIdValueBase.notEquals(id1, id3), "Static notEquals method should return true for different objects");
    }

    // Concrete class to test abstract class
    static class ConcreteTypedId extends TypedIdValueBase {
        protected ConcreteTypedId(UUID value) {
            super(value);
        }
    }
}