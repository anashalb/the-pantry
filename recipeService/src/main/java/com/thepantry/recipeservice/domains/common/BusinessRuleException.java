package com.thepantry.recipeservice.domains.common;

import lombok.Getter;

@Getter
public class BusinessRuleException extends Exception {

    private final IBusinessRule brokenRule;
    private final String details;
    private final String message;

    public BusinessRuleException(IBusinessRule brokenRule) {
        this.brokenRule = brokenRule;
        this.details = brokenRule.getMessage();
        this.message = this.details;
    }

    @Override
    public String toString() {
        return this.message;
    }
}