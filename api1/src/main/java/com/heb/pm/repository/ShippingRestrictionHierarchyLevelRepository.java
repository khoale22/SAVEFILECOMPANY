package com.heb.pm.repository;

import com.heb.pm.entity.ShippingRestrictionHierarchyLevel;
import com.heb.pm.entity.ShippingRestrictionHierarchyLevelKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for shipping restrictions for hierarchy levels.
 *
 * @author m314029
 * @since 2.5.0
 */
public interface ShippingRestrictionHierarchyLevelRepository extends
		JpaRepository<ShippingRestrictionHierarchyLevel, ShippingRestrictionHierarchyLevelKey> {

	List<ShippingRestrictionHierarchyLevel> findByKeyDepartmentAndKeySubDepartmentAndKeyItemClassAndKeyCommodityAndKeySubCommodity(
			String department, String subDepartment, Integer itemClass, Integer commodity, Integer subCommodity
	);
}
