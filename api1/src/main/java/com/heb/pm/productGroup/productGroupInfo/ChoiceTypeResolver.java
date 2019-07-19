package com.heb.pm.productGroup.productGroupInfo;

import com.heb.pm.entity.ChoiceOption;
import com.heb.pm.entity.ChoiceType;
import com.heb.util.jpa.LazyObjectResolver;

import java.util.List;

/**
 * Resolves a ChoiceType object returned by the ChoiceType REST endpoint.
 *
 * @author vn70529
 * @since 2.12
 */
public class ChoiceTypeResolver implements LazyObjectResolver<List<ChoiceType>> {

    /**
     * Performs the actual resolution of lazily loaded parameters.
     * @param choiceTypes The object to resolve.
     */
    @Override
    public void fetch(List<ChoiceType> choiceTypes){
        for (ChoiceType choiceType:choiceTypes) {
            for (ChoiceOption choiceOption:choiceType.getChoiceOptions()) {
                choiceOption.getProductChoiceText();
            }
        }
    }
}
