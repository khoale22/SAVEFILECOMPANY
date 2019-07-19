package com.heb.pm.repository;

import com.heb.pm.entity.Nutrient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * JPA repository for data from the ppd_nutrient table for queries without parameters.
 * @author m594201
 * @since 2.1.0
 */
public interface NutrientRepositoryDelegate extends NutrientRepositoryCommon {

	/**
	 * Find by nutrient description contains list.
	 *
	 * @param nutrientDescriptions the nutrient descriptions
	 * @param nutrientRequest      the nutrient request
	 * @return the list
	 */
	List<Nutrient> findByNutrientDescriptionContains(List<String> nutrientDescriptions, Pageable nutrientRequest);

}
