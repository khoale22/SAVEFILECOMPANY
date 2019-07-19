package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents product relationship audit composite key.
 *
 * @since 2.15.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductRelationshipAuditKey implements Serializable{

	@Column(name = "AUD_REC_CRE8_TS")
	private LocalDateTime changedOn;

	@Column(name = "PROD_ID")
	private Long productId;

	@Column(name = "PROD_RLSHP_CD")
	@Type(type="fixedLengthCharPK")
	private String productRelationshipCode;

	@Column(name = "RELATED_PROD_ID")
	private Long relatedProductId;

	/**
	 * Return the product relationship code
	 * @return product relationship code
	 */
	public String getProductRelationshipCode() {
		return productRelationshipCode;
	}

	/**
	 * Set the product relationship code
	 * @param productRelationshipCode code
	 */
	public void setProductRelationshipCode(String productRelationshipCode) {
		this.productRelationshipCode = productRelationshipCode;
	}

	/**
	 * Get the timestamp for when the record was created
	 * @return the timestamp for when the record was created
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Updates the timestamp for when the record was created
	 * @param changedOn the new timestamp for when the record was created
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
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

		ProductRelationshipAuditKey that = (ProductRelationshipAuditKey) o;

		if (!productId.equals(that.productId)) {
			return false;
		}
		if (relatedProductId.equals(that.relatedProductId))
			if ((productRelationshipCode != null) ? productRelationshipCode.equals(that.productRelationshipCode) : (that.productRelationshipCode == null))
				return true;
		return false;
	}

	@Override
	public int hashCode() {
		int result = (int) (productId ^ (productId >>> 32));
		result = 31 * result + (productRelationshipCode != null ? productRelationshipCode.hashCode() : 0);
		result = 31 * result + (int) (relatedProductId ^ (relatedProductId >>> 32));
		return result;
	}
}
