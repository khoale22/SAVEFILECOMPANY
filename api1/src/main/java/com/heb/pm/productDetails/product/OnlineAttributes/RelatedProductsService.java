package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.entity.ProductRelationship;
import com.heb.pm.repository.ProductRelationshipRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Holds business logic related to related products
 *
 * @author a786878
 * @since 2.14.0
 */
@Service
public class RelatedProductsService {

	public static final String ACTION_CODE_DELETE = "D";
	public static final String ACTION_CODE_ADD = "A";

	@Autowired
	ProductRelationshipRepository repository;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	/**
	 * This method will get the list of Related products
	 *
	 * @param prodId the product identification number
	 * @return
	 */
	public List<ProductRelationship> getRelatedProducts(Long prodId) {

		List<String> relationshipCodes = Arrays.asList(
				ProductRelationship.ProductRelationshipCode.ADD_ON_PRODUCT.getValue(),
				ProductRelationship.ProductRelationshipCode.UPSELL.getValue(),
				ProductRelationship.ProductRelationshipCode.FIXED_RELATED_PRODUCT.getValue());

		List<ProductRelationship> relationships = this.repository.findByKeyProductIdAndKeyProductRelationshipCodeIn(prodId, relationshipCodes);

		return relationships;
	}

	/**
	 * This method will save the list of related products changes
	 *
	 * @param relatedProducts products to be updated
	 */
	public void saveRelatedProducts(List<ProductRelationship> relatedProducts) {
		this.productManagementServiceClient.updateRelatedProducts(relatedProducts);
	}
}
