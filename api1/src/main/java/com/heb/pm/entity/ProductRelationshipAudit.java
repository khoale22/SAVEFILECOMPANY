package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents Product Relationship Audit Data from prod_rlshp_aud table.
 *
 * @since 2.15.0
 */
@Entity
@Table(name = "prod_rlshp_aud")
public class ProductRelationshipAudit implements Serializable, Audit {

	@EmbeddedId
	private ProductRelationshipAuditKey key;

	@Column(name = "act_cd")
	private String action;

	@Column(name = "lst_updt_uid")
	private String changedBy;

	@Column(name = "LST_UPDT_TS",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
	private LocalDateTime lastUpdateTs;

	@Column(name = "scn_cd_id")
	private Long elementUpc;

	@Column(name = "prod_qty")
	private Double productQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="related_prod_id", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
	private ProductMaster relatedProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
    private ProductMaster parentProduct;

	public LocalDateTime getLastUpdateTs() {
		return lastUpdateTs;
	}

	public void setLastUpdateTs(LocalDateTime lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

	/**
	 * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
	 * 	@return ActionCode
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Updates the action code
	 * @param action the new action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String getChangedBy() {
		return changedBy;
	}

	@Override
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	@Override
	public LocalDateTime getChangedOn() {
		return this.key.getChangedOn();
	}

	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}

	/**
	 * Gets related product master.
	 *
	 * @return the product master data that is joined by the related_prod_id.
	 */
	public ProductMaster getRelatedProduct() {
		return relatedProduct;
	}

	/**
	 * Sets related product master.
	 *
	 * @param relatedProduct the product master data that is joined by the related_prod_id.
	 */
	public void setRelatedProduct(ProductMaster relatedProduct) {
		this.relatedProduct = relatedProduct;
	}

	/**
	 * Gets scan code id.
	 *
	 * @return the scan code id identifier.
	 */
	public Long getElementUpc() {
		return elementUpc;
	}

	/**
	 * Sets scan code id.
	 *
	 * @param elementUpc the scan code id identifier.
	 */
	public void setElementUpc(Long elementUpc) {
		this.elementUpc = elementUpc;
	}

	/**
	 * Gets the elemental quantity.
	 *
	 * @return the elemental quantity.
	 */
	public Double getProductQuantity() {
		return productQuantity;
	}

	/**
	 * Sets the elemental quantity.
	 *
	 * @param productQuantity the elemental quantity.
	 */
	public void setProductQuantity(Double productQuantity) {
		this.productQuantity = productQuantity;
	}

    /**
     * The returns the product master for the parent product
     * @return parent product
     */
    public ProductMaster getParentProduct() {
        return parentProduct;
    }

    /**
     * Updates the product master for the parent product
     * @param parentProduct - parent product for this related product
     */
    public void setParentProduct(ProductMaster parentProduct) {
        this.parentProduct = parentProduct;
    }

	public ProductRelationshipAuditKey getKey() {
		return key;
	}

	public void setKey(ProductRelationshipAuditKey key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductRelationshipAudit that = (ProductRelationshipAudit) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	@Override
	public int hashCode() {
		return this.key != null ? this.key.hashCode() : 0;
	}

	public String getActionCode() {
		return action;
	}

	public void setActionCode(String action) {
		this.action = action;
	}

}
