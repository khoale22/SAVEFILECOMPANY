package com.heb.pm.repository;

import com.heb.pm.entity.Nutrient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * JPA repository for data from the ppd_nutrient table for queries parameters.
 * @author m594201
 * @since 2.1.0
 */
public interface NutrientRepositoryDelegateWithCount extends NutrientRepositoryCommon{

	/**
	 * Find by nutrient description contains page.
	 *
	 * @param nutrientDescriptions the nutrient descriptions
	 * @param nutrientRequest      the nutrient request
	 * @return the page
	 */
	Page<Nutrient> findByNutrientDescriptionContains(List<String> nutrientDescriptions, Pageable nutrientRequest);

}
