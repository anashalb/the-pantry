package com.thepantry.recipeservice.domains.common;

public class DomainModel {
    protected static void checkRule(IBusinessRule rule) throws BusinessRuleException {
        if (rule.isBroken()) {
            throw new BusinessRuleException(rule);
        }
    }
}
