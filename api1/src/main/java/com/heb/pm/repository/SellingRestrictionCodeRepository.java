package com.heb.pm.repository;

import com.heb.pm.entity.SellingRestrictionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for selling restriction codes.
 *
 * @author m314029
 * @since 2.8.0
 */
public interface SellingRestrictionCodeRepository extends JpaRepository<SellingRestrictionCode, String> {

	/**
	 * Finds selling restriction codes where the selling restriction group code is a particular value, and ordered by
	 * restriction description. Usually used to find all shipping restrictions, which would have a '9' for restriction
	 * group code.
	 *
	 * @param restrictionGroupCode Restriction group code to search for.
	 * @return All selling restriction codes that match the given restriction group code.
	 */
	List<SellingRestrictionCode> findByRestrictionGroupCodeOrderByRestrictionDescription(String restrictionGroupCode);
}
