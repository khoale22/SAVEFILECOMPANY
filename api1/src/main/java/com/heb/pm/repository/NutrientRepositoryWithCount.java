package com.heb.pm.repository;

import com.heb.pm.entity.Nutrient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository implementation for data from the PD_NUTRIENT table for queries by nutrient code.
 *
 * @author m594201
 * @since 2.1.0
 */
public interface NutrientRepositoryWithCount extends JpaRepository<Nutrient, Long>, NutrientRepositoryDelegateWithCount{

	/**
	 * Find by nutrient code in page.
	 *
	 * @param nutrientCodes   the nutrient codes
	 * @param nutrientRequest the nutrient request
	 * @return populated page containing Nutrient information
	 */
	Page<Nutrient> findByNutrientCodeInAndMaintenanceSwitchNot(List<Long> nutrientCodes, char deleteCode, Pageable nutrientRequest);

	/**
	 * This replaces findAll to not include the 'D' delete.
	 * @param deleteCode
	 * @param nutrientRequest
	 * @return
	 */
	Page<Nutrient> findByMaintenanceSwitchNot(char deleteCode, Pageable nutrientRequest);

	/**
	 * Find by uom code in page.
	 *
	 * @param uomCode   the uom codes
	 * @param uomRequest the nutrient request
	 * @return populated page containing Nutrient information
	 */
	Page<Nutrient> findByUomCode(long uomCode, Pageable uomRequest);
}

