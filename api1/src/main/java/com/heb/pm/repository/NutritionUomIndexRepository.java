package com.heb.pm.repository;

import com.heb.pm.scaleManagement.NutrientUomDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;

/**
 * JPA repository for NutrientUomDocument.
 *
 * @author m594201
 * @since 2.0.6
 */
public interface NutritionUomIndexRepository extends ElasticsearchRepository<NutrientUomDocument, String> {

	String FIND_BY_FILTER_AND_SYSTEM_QUERY =
			"{\"bool\":{\"must\":{\"query_string\":{\"query\":\"*?0*\",\"fields\":[\"key\", \"data.nutrientUomDescription\"],\"analyze_wildcard\": true}},\"must\":{\"query_string\":{\"query\":\"?1\",\"fields\":[\"data.systemOfMeasure\"]}}}}\";";

	String FIND_BY_FILTER_AND_SYSTEM_AND_FORM_QUERY =
			"{\"bool\":{\"must\":{\"query_string\":{\"query\":\"*?0*\",\"fields\":[\"key\", \"data.nutrientUomDescription\"],\"analyze_wildcard\": true}},\"must\":{\"query_string\":{\"query\":\"?1\",\"fields\":[\"data.systemOfMeasure\"]}},\"must\":{\"query_string\":{\"query\":\"?2\",\"fields\":[\"data.form\"]}}}}";

	/**
	 * Find searching by a string and limiting to a particular measuring system.
	 *
	 * @param searchString The string to limit descriptions and IDs by.
	 * @param measureSystem The measurement system to limit the results to.
	 * @param pageRequest  The request to say which page and limit the sizes to.
	 * @return A list of NutrientUnitOfMeasureDocuments that match the search criteria.
	 */
	@Query(NutritionUomIndexRepository.FIND_BY_FILTER_AND_SYSTEM_QUERY)
	Page<NutrientUomDocument> findByFilterAndMeasureSystem(String searchString,
														   String measureSystem, Pageable pageRequest);

	/**
	 * Find searching by a string and limiting to a particular measuring system and product form.
	 *
	 * @param searchString The string to limit descriptions and IDs by.
	 * @param measureSystem The measurement system to limit the results to.
	 * @param form The form the measure is for (solid or liquid).
	 * @param pageRequest  The request to say which page and limit the sizes to.
	 * @return A list of NutrientUnitOfMeasureDocuments that match the search criteria.
	 */
	@Query(NutritionUomIndexRepository.FIND_BY_FILTER_AND_SYSTEM_AND_FORM_QUERY)
	Page<NutrientUomDocument> findByFilterAndMeasureSystemAndForm(String searchString,
																  String measureSystem, String form,
																  Pageable pageRequest);

}
