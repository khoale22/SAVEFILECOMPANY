/*
 * EcommerUserGroupAttributeRepository.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.EcommerUserGroupAttribute;
import com.heb.pm.entity.EcommerUserGroupAttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * A repository to query EcommerUserGroupAttributes in the database
 * @author a786878
 * @version 2.15.0
 */
public interface EcommerUserGroupAttributeRepository extends JpaRepository<EcommerUserGroupAttribute, EcommerUserGroupAttributeKey> {
	/**
	 * Returns all user group attributes for a given attribute ID
	 *
	 * @return the attribute
	 */
	List<EcommerUserGroupAttribute> findByKeyAttributeId(long attributeId);
}
