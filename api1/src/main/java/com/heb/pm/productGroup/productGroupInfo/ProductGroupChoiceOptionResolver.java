

/**
 * Created by dieunguyen on 1/5/2018.
 */

package com.heb.pm.productGroup.productGroupInfo;

import com.heb.pm.entity.ProductGroupChoiceOption;
import com.heb.util.jpa.LazyObjectResolver;

import java.util.List;

/**
 * Resolves a ProductGroupChoiceType object returned by the ProductGroupChoiceType REST endpoint.
 *
 *  @author vn70633
 * @since 2.12
 */
public class ProductGroupChoiceOptionResolver implements LazyObjectResolver<List<ProductGroupChoiceOption>> {

    /**
     * Performs the actual resolution of lazily loaded parameters.
     * @param productGroupChoiceOptions The object to resolve.
     */
    @Override
    public void fetch(List<ProductGroupChoiceOption> productGroupChoiceOptions){
        for (ProductGroupChoiceOption productGroupChoiceOption:productGroupChoiceOptions) {
            productGroupChoiceOption.getChoiceOption().getProductChoiceText();
        }
    }
}

