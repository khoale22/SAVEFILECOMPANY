/*
 * AttributeDomainRepository.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.AttributeDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Returns attribute domain table information.
 *
 * @author a786878
 * @since 2.15.0
 */
public interface AttributeDomainRepository extends JpaRepository<AttributeDomain, String> {
	@Query("select a from AttributeDomain a where TRIM(attributeDomainCode) = ?1")
	AttributeDomain findAttributeDomainByAttributeDomainCode(String code);

	@Query("select a from AttributeDomain a")
	List<AttributeDomain> findAttributeDomains();
}
