/*
 *  CandidateProductPkVariationRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CandidateProductPkVariation;
import com.heb.pm.entity.CandidateProductPkVariationKey;
import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import com.heb.pm.entity.ProductPkVariation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * The repository for the PS_PROD_PK_VAR database table.
 * @author vn87351
 * @since 2.12.0
 */
public interface CandidateProductPkVariationRepository extends JpaRepository<CandidateProductPkVariation, CandidateProductPkVariationKey> {
	/**
	 * Returns the list of CandidateProductPkVariation by upc
	 * @param upc The scan code.
	 * @return the list of CandidateProductPkVariation.
	 */
	List<CandidateProductPkVariation> findByKeyUpc(Long upc);
}
