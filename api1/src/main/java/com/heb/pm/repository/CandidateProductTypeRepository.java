/*
 *  CandidateProductTypeRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CandidateProductType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for ItemType
 *
 * @author vn73545
 * @since 2.7.0
 */
public interface CandidateProductTypeRepository extends JpaRepository<CandidateProductType, String> {
}