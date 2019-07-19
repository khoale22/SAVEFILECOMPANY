package com.heb.pm.repository;

import com.heb.pm.entity.AttributeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by thanhtran on 8/31/2017.
 */
public interface AttributeCodeRepository extends JpaRepository<AttributeCode, Long> {
	@Query("select a from AttributeCode a where LOWER(attributeCodeValue) = ?1")
	AttributeCode findAttributeCodeByAttributeCodeValue(String code);

	/**
	 * Returns a list of attribute codes that have a given external ID.
	 *
	 * @param attributeValueXtrnlId The external ID to search for.
	 * @return The list of attribute codes with the given external ID.
	 */
	List<AttributeCode> findByAttributeValueXtrnlId(String attributeValueXtrnlId);

}
