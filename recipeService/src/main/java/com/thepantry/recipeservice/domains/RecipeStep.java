package com.thepantry.recipeservice.domains;

import com.thepantry.recipeservice.domains.businessRules.RecipeStepInstructionsMustBeAtLeastThreeChars;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.domains.common.DomainModel;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RecipeStep extends DomainModel {

    String instructions;

    private RecipeStep(String instructions) {
        this.instructions = instructions;
    }

    public static RecipeStep of(String instructions) throws BusinessRuleException {
        checkRule(new RecipeStepInstructionsMustBeAtLeastThreeChars(instructions));
        return new RecipeStep(instructions);
    }
}
