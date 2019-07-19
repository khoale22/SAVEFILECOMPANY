package com.heb.pm.productDetails.product.taxonomy;

import com.heb.pm.customHierarchy.CustomerHierarchyService;
import com.heb.pm.customHierarchy.GenericEntityService;
import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to product taxonomy levels.
 *
 * @author m314029
 * @since 2.18.4
 */
@Service
public class ProductTaxonomyLevelService {

	@Value("${app.mat.HierarchyContext}")
	private String hierarchyContext;

	@Autowired
	private CustomerHierarchyService customerHierarchyService;

	@Autowired
	private GenericEntityService genericEntityService;

	/**
	 * Finds all taxonomy relationship levels for the given product id.
	 *
	 * @param productId Product to look for.
	 * @return List of generic entity relationships matching the search.
	 */
	public List<GenericEntityRelationship> findAll(Long productId) {

		GenericEntity entity = this.genericEntityService.findByProductId(productId);
		if(entity == null){
			return new ArrayList<>();
		}
		return this.customerHierarchyService
				.getAllParentsOfChild(
						entity.getId(), hierarchyContext);
	}
}
