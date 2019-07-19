package com.heb.pm.repository;

import com.heb.pm.entity.AttributeMetaData;
import com.heb.pm.entity.AttributeMetaDataKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for AttributeMetaData.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface AttributeMetaDataRepositoryWithCount extends JpaRepository<AttributeMetaData, AttributeMetaDataKey> {

	/**
	 * Finds by given name ignore case and given 'is standard code table'.
	 *
	 * @param query Text to search for name by.
	 * @param isStandard Value of is standard code table to look for.
	 * @param pageRequest Request containing page, size, and sort information.
	 * @return List matching the search.
	 */
	Page<AttributeMetaData> findByNameIgnoreCaseContainingAndCodeTableStandard(String query, Boolean isStandard, Pageable pageRequest);

	/**
	 * Finds by given name ignore case.
	 *
	 * @param query Text to search for name by.
	 * @param pageRequest Request containing page, size, and sort information.
	 * @return List matching the search.
	 */
	Page<AttributeMetaData> findByNameIgnoreCaseContaining(String query, Pageable pageRequest);
}
