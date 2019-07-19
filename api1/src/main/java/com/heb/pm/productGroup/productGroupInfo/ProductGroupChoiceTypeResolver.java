package com.heb.pm.productGroup.productGroupInfo;

import com.heb.pm.entity.CustomerProductGroup;
import com.heb.pm.entity.ProductGroupChoiceType;
import com.heb.util.jpa.LazyObjectResolver;

import java.util.List;

/**
 * Resolves a ProductGroupChoiceType object returned by the ProductGroupChoiceType REST endpoint.
 *
 *  @author vn70529
 * @since 2.12
 */
public class ProductGroupChoiceTypeResolver implements LazyObjectResolver<List<ProductGroupChoiceType>> {

    /**
     * Performs the actual resolution of lazily loaded parameters.
     * @param productGroupChoiceTypes The object to resolve.
     */
    @Override
    public void fetch(List<ProductGroupChoiceType> productGroupChoiceTypes){
        for (ProductGroupChoiceType productGroupChoiceType:productGroupChoiceTypes) {
            for (CustomerProductGroup customerProductGroup:productGroupChoiceType.getProductGroupType().getCustomerProductGroups()) {
                customerProductGroup.getCustProductGroupName();
            }
        }
    }
}
