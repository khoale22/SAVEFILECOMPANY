/*
 * CommodityIndexRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.productHierarchy.CommodityDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repository for the indexed list of commodities.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface CommodityIndexRepository extends ElasticsearchRepository<CommodityDocument, String> {

	String REGEX_SEARCH_QUERY =
			"{\"bool\":{\"must\":{\"query_string\":{\"query\":\"?0\",\"fields\":[\"key\", \"data.name\"],\"analyze_wildcard\": true}}}}";

	/**
	 * Searches the index for commodities by commodity code or name.
	 *
	 * @param searchString The regular expression to use when searching for commodities.
	 * @param pageRequest The page request to use when searching.
	 * @return A list of commodities that match the regular expression passed in.
	 */
	@Query(CommodityIndexRepository.REGEX_SEARCH_QUERY)
	Page<CommodityDocument> findByRegularExpression(String searchString, Pageable pageRequest);

}
