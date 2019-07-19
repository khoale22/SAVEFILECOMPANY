package com.heb.pm.repository;

import com.heb.pm.productHierarchy.SubCommodityDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repository for indexed SubCommodities.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface SubCommodityIndexRepository extends ElasticsearchRepository<SubCommodityDocument, String> {

	String REGEX_SEARCH_QUERY =
			"{\"bool\":{\"must\":{\"query_string\":{\"query\":\"?0\",\"fields\":[\"key\", \"data.name\"],\"analyze_wildcard\": true}}}}";

	/**
	 * Searches the index for sub-commodities by sub-commodity code or name.
	 *
	 * @param searchString The regular expression to use when searching for sub-commodities.
	 * @param pageRequest The page request to use when searching.
	 * @return A list of sub-commodities that match the regular expression passed in.
	 */
	@Query(SubCommodityIndexRepository.REGEX_SEARCH_QUERY)
	Page<SubCommodityDocument> findByRegularExpression(String searchString, Pageable pageRequest);
}
