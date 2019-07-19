package com.heb.pm.scaleManagement;

import com.heb.pm.entity.IngredientStatementDetail;
import com.heb.pm.entity.IngredientStatementHeader;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves a IngredientStatementHeader object returned by the Ingredient REST endpoint.
 *
 * @author m594201
 * @since 2.0.9
 */
public class IngredientStatementHeaderResolver  implements LazyObjectResolver<IngredientStatementHeader> {

    @Override
    public void fetch(IngredientStatementHeader header) {
     header.getIngredientStatementDetails().size();
        for(IngredientStatementDetail ingredientStatementDetail: header.getIngredientStatementDetails()){
            ingredientStatementDetail.getIngredient().getIngredientCategory().getDisplayText();
        }
    }
}
