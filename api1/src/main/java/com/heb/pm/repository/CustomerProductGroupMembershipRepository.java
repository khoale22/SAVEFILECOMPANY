/*
 *  CustomerProductGroupMembershipRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CustomerProductGroupMembership;
import com.heb.pm.entity.CustomerProductGroupMembershipKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This is the repository for CustomerProductGroupMembership.
 *
 *
 * @author l730832
 * @since 2.14.0
 */
public interface CustomerProductGroupMembershipRepository extends JpaRepository<CustomerProductGroupMembership, CustomerProductGroupMembershipKey> {

	List<CustomerProductGroupMembership> findByKeyProdId(Long prodId);

	/**
	 * find customer product membership by product group id
	 * @param customerProductGroupId product group id
	 * @return list
	 */
	List<CustomerProductGroupMembership> findByKeyCustomerProductGroupId(Long customerProductGroupId);
}
