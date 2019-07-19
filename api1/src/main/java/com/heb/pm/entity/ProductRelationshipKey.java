package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents product relationship composite key.
 *
 * @author m594201
 * @since 2.8.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductRelationshipKey implements Serializable{

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "prod_id")
	private Long productId;

	@Column(name = "prod_rlshp_cd")
	@Type(type="fixedLengthCharPK")
	private String productRelationshipCode;

	@Column(name = "related_prod_id")
	private Long relatedProductId;

	public ProductRelationshipKey() {
	}

	public ProductRelationshipKey(Long productId, String productRelationshipCode, Long relatedProductId) {
		this.productId = productId;
		this.productRelationshipCode = productRelationshipCode;
		this.relatedProductId = relatedProductId;
	}

	/**
	 * Gets product id.  The numeric representation of the identifier of a product.
	 *
	 * @return the product id representation of the identifier of a product.
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Sets product id. The numeric representation of the identifier of a product.
	 *
	 * @param productId the product id numeric representation of the identifier of a product.
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Gets product relationship code.  The code that represents the relationship between entities.
	 *
	 * @return the product relationship code that represents the relationship between entities.
	 */
	public String getProductRelationshipCode() {
		return productRelationshipCode;
	}

	/**
	 * Sets product relationship code.  The code that represents the relationship between entities.
	 *
	 * @param productRelationshipCode the product relationship code that represents the relationship between entities.
	 */
	public void setProductRelationshipCode(String productRelationshipCode) {
		this.productRelationshipCode = productRelationshipCode;
	}

	/**
	 * Gets related product id that is the numeric representation of the identifier of a product.
	 *
	 * @return the related product id which is numeric representation of the identifier of a product.
	 */
	public Long getRelatedProductId() {
		return relatedProductId;
	}

	/**
	 * Sets related product id that is numeric representation of the identifier of a product.
	 *
	 * @param relatedProductId the related product id which is numeric representation of the identifier of a product.
	 */
	public void setRelatedProductId(Long relatedProductId) {
		this.relatedProductId = relatedProductId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductRelationshipKey that = (ProductRelationshipKey) o;

		if (productId != that.productId) return false;
		if (relatedProductId != that.relatedProductId) return false;
		return productRelationshipCode != null ? productRelationshipCode.equals(that.productRelationshipCode) : that.productRelationshipCode == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (productId ^ (productId >>> 32));
		result = 31 * result + (productRelationshipCode != null ? productRelationshipCode.hashCode() : 0);
		result = 31 * result + (int) (relatedProductId ^ (relatedProductId >>> 32));
		return result;
	}
}
