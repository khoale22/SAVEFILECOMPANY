package com.heb.pm.repository;

import com.heb.pm.entity.SellingRestrictionHierarchyLevel;
import com.heb.pm.entity.SellingRestrictionHierarchyLevelKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for selling restrictions for hierarchy levels.
 *
 * @author m314029
 * @since 2.5.0
 */
public interface SellingRestrictionHierarchyLevelRepository extends
		JpaRepository<SellingRestrictionHierarchyLevel, SellingRestrictionHierarchyLevelKey> {

	List<SellingRestrictionHierarchyLevel> findByKeyDepartmentAndKeySubDepartmentAndKeyItemClassAndKeyCommodityAndKeySubCommodity(
			String department, String subDepartment, Integer itemClass, Integer commodity, Integer subCommodity
	);
}
