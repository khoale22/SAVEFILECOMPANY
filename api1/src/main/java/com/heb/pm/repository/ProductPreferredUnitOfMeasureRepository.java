package com.heb.pm.repository;

import com.heb.pm.entity.ProductPreferredUnitOfMeasure;
import com.heb.pm.entity.ProductPreferredUnitOfMeasureKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for product preferred unit of measure.
 *
 * @author m314029
 * @since 2.12.0
 */
public interface ProductPreferredUnitOfMeasureRepository extends JpaRepository<ProductPreferredUnitOfMeasure, ProductPreferredUnitOfMeasureKey> {

	/**
	 * Find all ProductPreferredUnitOfMeasures attached to a sub-commodity by searching for: key -> subCommodityCode.
	 *
	 * @param subCommodityCode Sub-commodity code to search for.
	 * @return All ProductPreferredUnitOfMeasures attached to the given sub-commodity.
	 */
	List<ProductPreferredUnitOfMeasure> findByKeySubCommodityCode(Integer subCommodityCode);
}
