package com.heb.pm.productDetails.product;

import com.heb.pm.entity.ProductRelationship;
import com.heb.pm.repository.ProductRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to serve the request and update kits data.
 *
 * @author m594201
 * @since 2.8.0
 */
@Service
public class ProductService {

	@Autowired
	private ProductRelationshipRepository productRelationshipRepository;

	/**
	 * Gets kits data based on the product Id.
	 *
	 * @param productId the product Id of the product.
	 * @return the kits data and related productMaster data.
	 */
	public List<ProductRelationship> getKitsData(Long productId) {
		List<ProductRelationship> associatedKits = this.productRelationshipRepository.findByKeyProductIdOrRelatedProductProdIdAndKeyProductRelationshipCodeOrderByKeyProductId(productId, productId, ProductRelationship.KIT_TYPE);
		List<Long> kitIds=new ArrayList<>();
		for (ProductRelationship relationship: associatedKits) {
			kitIds.add(relationship.getKey().getProductId());
		}
		return this.productRelationshipRepository.findByKeyProductIdInAndKeyProductRelationshipCode(kitIds, ProductRelationship.KIT_TYPE);
	}

	/**
	 *The list of product relations based on their element upc
	 * @param upcs the list of upcs that could be in a kit.
	 * @return the kits data and related productMaster data.
	 */
	public List<ProductRelationship> getKitsDataByElements(List<Long> upcs) {
		return this.productRelationshipRepository.findByElementUpcInAndKeyProductRelationshipCode(upcs, ProductRelationship.KIT_TYPE);
	}
}
