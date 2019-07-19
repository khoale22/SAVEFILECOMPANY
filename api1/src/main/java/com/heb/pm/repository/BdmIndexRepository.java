/*
 * BdmIndexRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.productHierarchy.BdmDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repository for indexed Bdms.
 *
 * @author m314029
 * @since 2.0.6
 */
public interface BdmIndexRepository extends ElasticsearchRepository<BdmDocument, String> {

	String REGEX_SEARCH_QUERY =
			"{\"bool\":{\"must\":{\"query_string\":{\"query\":\"?0\",\"fields\":[\"key\", \"data.fullName\"],\"analyze_wildcard\": true}}}}";

	/**
	 * Searches the index for sub-commodities by sub-commodity code or name.
	 *
	 * @param searchString The regular expression to use when searching for sub-commodities.
	 * @param pageRequest The page request to use when searching.
	 * @return A list of sub-commodities that match the regular expression passed in.
	 */
	@Query(BdmIndexRepository.REGEX_SEARCH_QUERY)
	Page<BdmDocument> findByRegularExpression(String searchString, Pageable pageRequest);
}
