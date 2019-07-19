package com.heb.pm.repository;

import com.heb.pm.entity.Nutrient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by m594201 on 12/5/2016.
 */
public interface NutrientInformationRepository extends JpaRepository<Nutrient, Long>{
	/**
	 * Find by nutrient code list.
	 *
	 * @param nutrientCodes   the nutrient codes
	 * @param nutrientRequest the nutrient request
	 * @return the list
	 */
	List<Nutrient> findByNutrientCode(List<Nutrient> nutrientCodes, Pageable nutrientRequest);
}
