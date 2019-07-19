package com.heb.pm.repository;

import com.heb.pm.entity.ProductRelationship;
import com.heb.pm.entity.ProductRelationshipKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve ProductRelationship data.
 *
 * @author m594201
 * @since 2.8.0
 */
public interface ProductRelationshipRepository extends JpaRepository<ProductRelationship, ProductRelationshipKey>{
	/**
	 * Returns a list of product relationships of type kit based on their product id
	 * @param productId
	 * @param productRelationshipCode
	 * @return
	 */
	List<ProductRelationship> findByKeyProductIdOrRelatedProductProdIdAndKeyProductRelationshipCodeOrderByKeyProductId(Long productId, Long relatedId,String productRelationshipCode);

	/**
	 * Returns all product relationships with the selected product id and type
	 * @param productId the id of the product relationship
	 * @param productRelationshipCode the type of product relationship
	 * @return
	 */
	List<ProductRelationship> findByKeyProductIdAndKeyProductRelationshipCode(Long productId,String productRelationshipCode);

	/**
	 * This query will find all product relationships based on the list of product id provided that are kits
	 * @param productId the list of potential product ids
	 * @param productRelationshipCode the type of product relationship
	 * @return
	 */
	List<ProductRelationship> findByKeyProductIdInAndKeyProductRelationshipCode(List<Long> productId,String productRelationshipCode);

	/**
	 * Returns a list of product relationships of type kit based on their element upc
	 * @param upcs
	 * @param productRelationshipCode
	 * @return
	 */
	List<ProductRelationship> findByElementUpcInAndKeyProductRelationshipCode(List<Long> upcs, String productRelationshipCode);

	/**
	 * Get product relationship data by product id.
	 *
	 * @param productId                the product id to find.
	 * @param productRelationshipCodes the list of product relationship codes to find.
	 * @return the list of product relationship.
	 */
	List<ProductRelationship> findByKeyProductIdAndKeyProductRelationshipCodeIn(Long productId, List<String> productRelationshipCodes);

	/**
	 * This query will find all product relationships based on the list of related product id provided that are kits
	 * @param relatedProductIds the list of potential related product ids
	 * @param productRelationshipCode the type of product relationship
	 * @return
	 */
	List<ProductRelationship> findByKeyRelatedProductIdInAndKeyProductRelationshipCode(List<Long> relatedProductIds,
																					   String productRelationshipCode);

	/**
	 * Thís query will count all product relationships based on related product or product id provided.
	 * @param productId The product id
	 * @param productRelationshipCode1 The product Relationship code
	 * @param relatedProdId The related product
	 * @param productRelationshipCode2 The product Relationship code
	 * @return int
	 */
	int countByKeyRelatedProductIdAndKeyProductRelationshipCodeOrKeyProductIdAndKeyProductRelationshipCode(Long productId,
																										  String productRelationshipCode1,
																										  Long relatedProdId,
																										  String productRelationshipCode2);

	/**
	 * Returns a list of product relationships based on their related product id and product Relationship code
	 * @param relatedId
	 * @param productRelationshipCode
	 * @return
	 */
	List<ProductRelationship> findByRelatedProductProdIdAndKeyProductRelationshipCodeOrderByKeyProductId(Long relatedId,String productRelationshipCode);
}