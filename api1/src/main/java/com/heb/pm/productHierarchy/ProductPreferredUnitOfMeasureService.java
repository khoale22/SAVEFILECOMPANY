package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ProductPreferredUnitOfMeasure;
import com.heb.pm.repository.ProductPreferredUnitOfMeasureRepository;
import com.heb.pm.ws.ProductHierarchyManagementServiceClient;
import com.heb.util.ws.SoapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business functions related to product preferred units of measure.
 *
 * @author m314029
 * @since 2.12.0
 */
@Service
public class ProductPreferredUnitOfMeasureService {

	@Autowired
	private ProductHierarchyManagementServiceClient productHierarchyManagementServiceClient;

	@Autowired
	private ProductPreferredUnitOfMeasureRepository repository;

	/**
	 * This method calls the product hierarchy management service client to update a list of
	 * ProductPreferredUnitOfMeasure.
	 *
	 * @param preferredUnitOfMeasures ProductPreferredUnitOfMeasures to update.
	 */
	public void update(List<ProductPreferredUnitOfMeasure> preferredUnitOfMeasures) {
		try {
			this.productHierarchyManagementServiceClient.
					updateProductPreferredUnitOfMeasures(preferredUnitOfMeasures);
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}
	}

	/**
	 * This method calls the ProductPreferredUnitOfMeasure repository to find all ProductPreferredUnitOfMeasures
	 * associated with a particular sub-commodity.
	 *
	 * @param preferredUnitOfMeasure ProductPreferredUnitOfMeasure that has the sub-commodity code to search for.
	 * @return List of all ProductPreferredUnitOfMeasures attached to a given sub-commodity.
	 */
	public List<ProductPreferredUnitOfMeasure> findBySubCommodity(ProductPreferredUnitOfMeasure preferredUnitOfMeasure) {
		if(preferredUnitOfMeasure.getKey() != null && preferredUnitOfMeasure.getKey().getSubCommodityCode() != null) {
			return this.repository.findByKeySubCommodityCode(preferredUnitOfMeasure.getKey().getSubCommodityCode());
		} else {
			return new ArrayList<>();
		}
	}
}
