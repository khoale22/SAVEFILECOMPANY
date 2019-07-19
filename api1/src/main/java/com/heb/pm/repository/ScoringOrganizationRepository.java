/*
 *  WineScoringOrganizationRepository
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ScoringOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about Scoring Organization.
 *
 * @author vn70529
 * @since 2.12
 */
public interface ScoringOrganizationRepository extends JpaRepository<ScoringOrganization, Integer> {

	/**
	 * Returns a list of all Scoring Organizations ordered by Scoring Organization id.
	 *
	 * @return a list of all Scoring Organizations ordered by Scoring Organization id.
	 */
	List<ScoringOrganization> findAllByOrderByScoringOrganizationIdAsc();

}
