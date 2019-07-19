package com.heb.pm.repository;

import com.heb.pm.productHierarchy.ItemClassDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repository for the index of item-class information.
 *
 * @author m314029
 * @since 2.6.0
 */
public interface ItemClassIndexRepository extends ElasticsearchRepository<ItemClassDocument, String> {

	String REGEX_SEARCH_QUERY =
			"{\"bool\":{\"must\":{\"query_string\":{\"query\":\"?0\",\"fields\":[\"key\", \"data.itemClassDescription\"],\"analyze_wildcard\": true}}}}";

	/**
	 * Searches the index for item-classes by name.
	 *
	 * @param searchString The regular expression to use when searching for item-classes.
	 * @param pageRequest The page request to use when searching.
	 * @return A list of item-classes that match the regular expression passed in.
	 */
	@Query(ItemClassIndexRepository.REGEX_SEARCH_QUERY)
	Page<ItemClassDocument> findByRegularExpression(String searchString, Pageable pageRequest);
}
