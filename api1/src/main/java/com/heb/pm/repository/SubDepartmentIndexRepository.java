/*
 * SubDepartmentIndexRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.productHierarchy.SubDepartmentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repository for the index of sub-department information.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface SubDepartmentIndexRepository extends ElasticsearchRepository<SubDepartmentDocument, String> {

	String REGEX_SEARCH_QUERY =
			"{\"bool\":{\"must\":{\"query_string\":{\"query\":\"?0\",\"fields\":[\"key\", \"data.name\"],\"analyze_wildcard\": true}}}}";

	/**
	 * Searches the index for sub-departments by name.
	 *
	 * @param searchString The regular expression to use when searching for sub-departments.
	 * @param pageRequest The page request to use when searching.
	 * @return A list of sub-departments that match the regular expression passed in.
	 */
	@Query(SubDepartmentIndexRepository.REGEX_SEARCH_QUERY)
	Page<SubDepartmentDocument> findByRegularExpression(String searchString, Pageable pageRequest);
}
