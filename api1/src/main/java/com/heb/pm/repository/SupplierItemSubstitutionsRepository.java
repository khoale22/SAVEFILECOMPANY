package com.heb.pm.repository;

import com.heb.pm.entity.SupplierItemSubstitutions;
import com.heb.pm.entity.SupplierItemSubstitutionsKey;
import com.heb.util.jpa.PageableResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA Repository for SupplierItemSubstitutions.
 *
 * @author m594201
 * @since 2.8.0
 */
public interface SupplierItemSubstitutionsRepository extends JpaRepository<SupplierItemSubstitutions, SupplierItemSubstitutionsKey> {

	List<SupplierItemSubstitutions> findByKeyItemIdAndKeyLocationTypeCodeAndKeyItemKeyTypeCode(Long itemCode, String locationTypeCode, String itemKeyTypeCode);

}
