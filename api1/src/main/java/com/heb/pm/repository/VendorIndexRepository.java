package com.heb.pm.repository;

import com.heb.pm.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Interface to the vendor Elasticsearch index.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface VendorIndexRepository extends ElasticsearchRepository<Vendor, Integer> {

	String REGEX_SEARCH_QUERY =
			"{\"bool\" : { \"should\" : [{\"regexp\":{\"vendorName\":\"?0\"}} , {\"regexp\":{\"vendorNumberAsString\":\"?0\"}}]}}";
	/**
	 * Searches the index for a vendor by name and by number using a regular expression.
	 *
	 * @param searchString The regular expression to use when searching for vendors.
	 * @param pageRequest The page request to use when searching.
	 * @return A list of vendors that match the regular expression passed in.
	 */
	@Query(VendorIndexRepository.REGEX_SEARCH_QUERY)
	Page<Vendor> findByRegularExpression(String searchString, Pageable pageRequest);
}
