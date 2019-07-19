package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.entity.CustomerProductGroupMembership;
import com.heb.util.jpa.LazyObjectResolver;

import java.util.List;

/**
 * This is the resolver for the CustomerProductGroupMembership.
 *
 * @author vn70529
 * @since 2.15.0
 */
public class CustomerProductGroupMembershipResolver implements LazyObjectResolver<List<CustomerProductGroupMembership>> {
    /**
     * Performs the actual resolution of lazily loaded parameters.
     * @param customerProductGroupMemberships The object to resolve.
     */
    @Override
    public void fetch(List<CustomerProductGroupMembership> customerProductGroupMemberships){
        for (CustomerProductGroupMembership customerProductGroupMembership:customerProductGroupMemberships) {
            customerProductGroupMembership.getCustomerProductGroup().getProductGroupType().getProductGroupTypeCode();
        }
    }
}