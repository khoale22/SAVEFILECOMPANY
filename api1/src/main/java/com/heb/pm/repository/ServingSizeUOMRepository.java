/*
 *  ServingSizeUOMRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ServingSizeUOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * the repositoy for serving size uom table
 * @author vn87351
 * @since 2.12.0
 */
public interface ServingSizeUOMRepository extends JpaRepository<ServingSizeUOM, String> {
	/**
	 * find serving size uom by description and source system
	 * @param sourceSystem
	 * @param desc
	 * @return
	 */
	@Query("select uom from ServingSizeUOM uom where sourceSystem = ? and trim(servingSizeUomDescription)= ?")
	List<ServingSizeUOM> findBySourceSystemAndServingSizeUomDescription(int sourceSystem, String desc);
}
