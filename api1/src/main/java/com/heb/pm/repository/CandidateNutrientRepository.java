/*
 *  CandidateNutrientRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CandidateNutrient;
import com.heb.pm.entity.CandidateNutrientKey;
import com.heb.pm.entity.ProductNutrient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The repository for the PS_PROD_PK_VAR database table.
 * @author vn87351
 * @since 2.12.0
 */
public interface CandidateNutrientRepository extends JpaRepository<CandidateNutrient, CandidateNutrientKey> {
	/**
	 * Returns the list of CandidateNutrient by upc.
	 * @param upc The scan code.
	 * @return the list of CandidateNutrient.
	 */
	List<CandidateNutrient> findByKeyUpcOrderByValPreprdTypCdDescMasterIdAsc(Long upc);
}
