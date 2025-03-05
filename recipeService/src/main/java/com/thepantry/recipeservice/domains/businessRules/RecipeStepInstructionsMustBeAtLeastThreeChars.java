package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.common.IBusinessRule;

public class RecipeStepInstructionsMustBeAtLeastThreeChars implements IBusinessRule {

    private final short MIN_STEP_INSTRUCTIONS_CHAR_LENGTH = 3;

    private final String stepInstructions;

    public RecipeStepInstructionsMustBeAtLeastThreeChars(String stepInstructions) {
        this.stepInstructions = stepInstructions;
    }

    @Override
    public boolean isBroken() {
        return stepInstructions != null && stepInstructions.length() < MIN_STEP_INSTRUCTIONS_CHAR_LENGTH;
    }

    @Override
    public String getMessage() {
        return "Step instructions should be at least three characters long";
    }
}
