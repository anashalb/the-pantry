package com.thepantry.recipeservice.domains.common;


public interface IBusinessRule {

    boolean isBroken();

    String getMessage();
}