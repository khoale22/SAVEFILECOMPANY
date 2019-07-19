/*
 * AssociatedProduct
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productGroup.productGroupInfo;

import com.heb.pm.entity.CustomerProductChoice;
import com.heb.pm.entity.ProductGroupChoiceOption;

import java.util.List;

/**
 * object to represent data associated product on product group info screen.
 *
 * @author vn70633
 * @since 2.14.0
 */
public class AssociatedProductDetail {
    List<ProductGroupChoiceOption> productGroupChoiceOptions;
    List<CustomerProductChoice> customerProductChoices;
    /**
     * Get list of customerProductChoices.
     *
     * @return list of customerProductChoices
     */
    public List<CustomerProductChoice> getCustomerProductChoices() {
        return customerProductChoices;
    }

    /**
     * Set  list of customerProductChoices.
     *
     * @param customerProductChoices  list of customerProductChoices
     */
    public void setCustomerProductChoices(List<CustomerProductChoice> customerProductChoices) {
        this.customerProductChoices = customerProductChoices;
    }

    /**
     * Get the list of productGroupChoiceOptions.
     *
     * @return the list of productGroupChoiceOptions.
     */
    public List<ProductGroupChoiceOption> getProductGroupChoiceOptions() {
        return productGroupChoiceOptions;
    }

    /**
     * Set list of productGroupChoiceOptions.
     *
     * @param productGroupChoiceOptions  list of productGroupChoiceOptions
     */
    public void setProductGroupChoiceOptions(List<ProductGroupChoiceOption> productGroupChoiceOptions) {
        this.productGroupChoiceOptions = productGroupChoiceOptions;
    }
}
