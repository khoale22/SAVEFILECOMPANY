package com.heb.pm.taxonomy;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.AttributeMetaData;
import com.heb.pm.repository.AttributeMetaDataRepository;
import com.heb.pm.repository.SimpleCriteriaBuilder;
import com.heb.pm.taxonomy.predicateBuilder.AttributeMetaDataPredicateBuilder;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * Business functions for meta data.
 *
 * @author m314029
 * @since 2.21.0
 */
@Service
public class AttributeMetaDataService {

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;
	private static final Boolean DEFAULT_INCLUDE_COUNTS = Boolean.TRUE;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	@Autowired
	private AttributeMetaDataRepository attributeMetaDataRepository;

	/**
	 * Finds all generic code tables matching the given query, with parameters:
	 *
	 * @param name Name to search for containing text.
	 * @param customerFacing Customer facing to search for.
	 * @param global Global to search for.
	 * @param attributeStateCode Attribute state code to search for.
	 * @param hasStandardCodeTable Standard code table to search for.
	 * @param firstSearch Identifies first fetch where count query will also be ran for pagination.
	 * @param page Page requested.
	 * @param pageSize Number of records per page.
	 * @return All generic code tables matching the request.
	 */
	public PageableResult<AttributeMetaData> findAll(
			String name, Boolean customerFacing, Boolean global, String attributeStateCode,
			Boolean hasStandardCodeTable, Boolean firstSearch, Integer page, Integer pageSize) {

		// set defaults
		page = page == null ? DEFAULT_PAGE : page;
		pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
		firstSearch = firstSearch == null ? DEFAULT_INCLUDE_COUNTS : firstSearch;

		// create predicate builder for attribute meta data
		AttributeMetaDataPredicateBuilder predicateBuilder = new AttributeMetaDataPredicateBuilder(
				name,
				customerFacing,
				global,
				attributeStateCode,
				hasStandardCodeTable);

		SimpleCriteriaBuilder<AttributeMetaData> simpleCriteriaBuilder =
				new SimpleCriteriaBuilder<>(this.entityManager, AttributeMetaData.class);

		// get pageable result from criteria builder
		return simpleCriteriaBuilder.getPageableResult(page, pageSize,
				AttributeMetaData.getDefaultSort(), firstSearch, predicateBuilder);
	}

	/**
	 * Saves an AttributeMetadata.
	 *
	 * @param attributeMetaData the AttributeMetadata to be saved
	 * @return Saved AttributeMetadata.
	 */
	public AttributeMetaData saveAttributeMetadata(AttributeMetaData attributeMetaData) {
		return this.attributeMetaDataRepository.save(attributeMetaData);
	}
}
