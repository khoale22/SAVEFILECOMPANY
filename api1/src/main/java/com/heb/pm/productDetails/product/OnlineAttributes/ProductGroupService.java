/*
 *  ProductGroupService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.entity.CustomerProductGroupMembership;
import com.heb.pm.repository.CustomerProductGroupMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This holds all of the businesss logic for a product group service.
 *
 * @author l730832
 * @since 2.14.0
 */
@Service
public class ProductGroupService {

	@Autowired
	private CustomerProductGroupMembershipRepository repository;

	/**
	 * This retrieves the product group list.
	 * @param prodId
	 * @return a list of customer product group membership
	 */
	public List<CustomerProductGroupMembership> getProductGroupList(Long prodId){
		return this.repository.findByKeyProdId(prodId);
	}
}
