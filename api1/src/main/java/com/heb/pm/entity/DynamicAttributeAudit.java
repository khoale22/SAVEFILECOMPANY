/*
 * DynamicAttributeAudit
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a dynamic attribute audit of a product.
 *
 */
@Entity
@Table(name = "mst_dta_extn_aud")
public class DynamicAttributeAudit implements Audit, Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DynamicAttributeAuditKey key;

	@Column(name = "act_cd")
	private String action;

	@AuditableField(displayName = "Attribute Value", filter = FilterConstants.TAGS_AND_SPECS_AUDIT)
	@Column(name = "attr_val_txt")
	private String textValue;

	@Column(name = "attr_cd_id")
	private Long attributeCodeId;

	@Column(name = "lst_updt_uid")
	private String changedBy;

	@Column(name = "LST_UPDT_TS",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
	private LocalDateTime lastUpdateTs;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="key_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false, nullable = true)
	@Where(clause = "itm_prod_key_cd = 'UPC'")
	private SellingUnit sellingUnit;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="key_id", referencedColumnName = "prod_id", insertable = false, updatable = false, nullable = true)
	@Where(clause = "itm_prod_key_cd = 'PROD'")
	private ProductMaster productMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="dta_src_sys", referencedColumnName = "src_system_id", insertable = false, updatable = false)
	private SourceSystem sourceSystem;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_id", referencedColumnName = "attr_id", insertable = false, updatable = false)
	private Attribute attribute;

	/**
	 * Returns the key for the dynamic attribtue.
	 *
	 * @return The key for the dynamic attribtue.
	 */
	public DynamicAttributeAuditKey getKey() {
		return key;
	}

	/**
	 * Sets the key for the dynamic attribtue.
	 *
	 * @param key The key for the dynamic attribtue.
	 */
	public void setKey(DynamicAttributeAuditKey key) {
		this.key = key;
	}

	/**
	 * If this attribute's value is just text, this property is set.
	 *
	 * @return The attribute's value if the type is text.
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * Sets the attribute's value if the attribute's type is text.
	 *
	 * @param textValue The attribute's value if the type is text.
	 */
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	/**
	 * Returns the SellingUnit this attribute is for if it is a UPC level attribute.
	 *
	 * @return The SellingUnit this attribute is for if it is a UPC level attribute.
	 */
	public SellingUnit getSellingUnit() {
		return sellingUnit;
	}

	/**
	 * Sets the SellingUnit this attribute is for if it is a UPC level attribute.
	 *
	 * @param sellingUnit The SellingUnit this attribute is for if it is a UPC level attribute.
	 */
	public void setSellingUnit(SellingUnit sellingUnit) {
		this.sellingUnit = sellingUnit;
	}

	/**
	 * Returns the system that is the source of this record.
	 *
	 * @return The system that is the source of this record.
	 */
	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * Sets the system that is the source of this record.
	 *
	 * @param sourceSystem The system that is the source of this record.
	 */
	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * When this record is at the product level, this will return the product it is associated to.
	 *
	 * @return The product this record is associated to.
	 */
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * Sets the product this record is associated to.
	 *
	 * @param productMaster The product this record is associated to.
	 */
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	public Long getAttributeCodeId() {
		return attributeCodeId;
	}

	/**
	 * Updates the attributeCodeId
	 * @param attributeCodeId
	 */
	public void setAttributeCodeId(Long attributeCodeId) {
		this.attributeCodeId = attributeCodeId;
	}

	/**
	 * Returns the attribute associated with the dynamic attribute
	 * @return
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * Updates the attribute
	 * @param attribute
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * Compares this object with another for equality. Equality is based on the key.
	 *
	 * @param o The other object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DynamicAttributeAudit)) return false;

		DynamicAttributeAudit that = (DynamicAttributeAudit) o;

		return key.equals(that.key);
	}

	/**
	 * Returns a hash code for the object. Equal objects return the same hash code. Unequal objects (probably) return
	 * different hash codes.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {
		return key.hashCode();
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "DynamicAttribute{" +
				"key=" + key +
				", textValue='" + textValue + '\'' +
				", attributeCodeId=" + attributeCodeId +
				", sellingUnit=" + sellingUnit +
				", productMaster=" + productMaster +
				", sourceSystem=" + sourceSystem +
				", attribute=" + attribute +
				'}';
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
}
