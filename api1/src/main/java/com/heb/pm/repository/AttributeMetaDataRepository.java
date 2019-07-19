package com.heb.pm.repository;

import com.heb.pm.entity.AttributeMetaData;
import com.heb.pm.entity.AttributeMetaDataKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for AttributeMetaData.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface AttributeMetaDataRepository extends JpaRepository<AttributeMetaData, AttributeMetaDataKey> {

	/**
	 * Finds by given name ignore case and given 'is standard code table'.
	 *
	 * @param query Text to search for name by.
	 * @param isStandard Value of is standard code table to look for.
	 * @param pageRequest Request containing page, size, and sort information.
	 * @return List matching the search.
	 */
	List<AttributeMetaData> findByNameIgnoreCaseContainingAndCodeTableStandard(String query, Boolean isStandard, Pageable pageRequest);

	/**
	 * Finds by given name ignore case.
	 *
	 * @param query Text to search for name by.
	 * @param pageRequest Request containing page, size, and sort information.
	 * @return List matching the search.
	 */
	List<AttributeMetaData> findByNameIgnoreCaseContaining(String query, Pageable pageRequest);
}
