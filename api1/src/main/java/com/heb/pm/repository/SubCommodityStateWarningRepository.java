package com.heb.pm.repository;

import com.heb.pm.entity.SubCommodityStateWarning;
import com.heb.pm.entity.SubCommodityStateWarningKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for sub-commodity state warning.
 *
 * @author m314029
 * @since 2.12.0
 */
public interface SubCommodityStateWarningRepository extends JpaRepository<SubCommodityStateWarning, SubCommodityStateWarningKey> {

	/**
	 * Find all SubCommodityStateWarnings attached to a sub-commodity by searching for: key -> subCommodityCode.
	 *
	 * @param subCommodityCode Sub-commodity code to search for.
	 * @return All SubCommodityStateWarnings attached to the given sub-commodity.
	 */
	List<SubCommodityStateWarning> findByKeySubCommodityCode(Integer subCommodityCode);
}
