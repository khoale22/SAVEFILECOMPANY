package com.heb.pm.scaleManagement;

import com.heb.pm.entity.Ingredient;
import com.heb.pm.entity.IngredientSub;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Represents a dynamic attribute of a IngredientStatementDetail.
 *
 * @author m594201
 * @since 2.0.9
 */
public class IngredientResolver implements LazyObjectResolver<Ingredient>{

    /**
     * Resolves the Ingredient object. This will load its category and statementDetails properties.
     *
     * @param ingredient The object to resolve.
     */
    @Override
    public void fetch(Ingredient ingredient) {

        this.getCurrentIngredientSubSize(ingredient);
        ingredient.getIngredientCategory().getDisplayText();
    }

    private void getCurrentIngredientSubSize(Ingredient ingredient){

        long count;
        for(IngredientSub sub: ingredient.getIngredientSubs()){

            count = sub.getSubIngredient().getIngredientSubs().size();

            if(count > 0){
                this.getCurrentIngredientSubSize(sub.getSubIngredient());
            }
        }
    }

}
