/*
 * DynamicAttribute
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a dynamic attribute of a product.
 *
 * @author d116773
 * @since 2.0.7
 */
@Entity
@Table(name = "mst_dta_extn_attr")
public class DynamicAttribute implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DynamicAttributeKey key;

	@Column(name = "attr_val_txt")
	private String textValue;

	@Column(name = "attr_cd_id")
	private Long attributeCodeId;

	@Column(name = "attr_val_nbr")
	private Double attributeValueNumber;

	@Column(name = "ATTR_VAL_TS")
	private LocalDateTime attributeValueTime;

	@Column(name = "ATTR_VAL_DT")
	private LocalDate attributeValueDate;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_cd_id", referencedColumnName = "attr_cd_id", insertable = false, updatable = false)
	private AttributeCode attributeCode;

	/**
	 * Returns the key for the dynamic attribtue.
	 *
	 * @return The key for the dynamic attribtue.
	 */
	public DynamicAttributeKey getKey() {
		return key;
	}

	/**
	 * Sets the key for the dynamic attribtue.
	 *
	 * @param key The key for the dynamic attribtue.
	 */
	public void setKey(DynamicAttributeKey key) {
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
	 * Returns AttributeCode.
	 *
	 * @return The AttributeCode.
	 **/
	public AttributeCode getAttributeCode() {
		return attributeCode;
	}

	/**
	 * Sets the AttributeCode.
	 *
	 * @param attributeCode The AttributeCode.
	 **/
	public DynamicAttribute setAttributeCode(AttributeCode attributeCode) {
		this.attributeCode = attributeCode;
		return this;
	}

	/**
	 * Returns AttributeValueNumber.
	 *
	 * @return The AttributeValueNumber.
	 **/
	public Double getAttributeValueNumber() {
		return attributeValueNumber;
	}

	/**
	 * Sets the AttributeValueNumber.
	 *
	 * @param attributeValueNumber The AttributeValueNumber.
	 **/
	public DynamicAttribute setAttributeValueNumber(Double attributeValueNumber) {
		this.attributeValueNumber = attributeValueNumber;
		return this;
	}

	/**
	 * Returns AttributeValueTime.
	 *
	 * @return The AttributeValueTime.
	 **/
	public LocalDateTime getAttributeValueTime() {
		return attributeValueTime;
	}

	/**
	 * Sets the AttributeValueTime.
	 *
	 * @param attributeValueTime The AttributeValueTime.
	 **/
	public DynamicAttribute setAttributeValueTime(LocalDateTime attributeValueTime) {
		this.attributeValueTime = attributeValueTime;
		return this;
	}

	/**
	 * Returns AttributeValueDate.
	 *
	 * @return The AttributeValueDate.
	 **/
	public LocalDate getAttributeValueDate() {
		return attributeValueDate;
	}

	/**
	 * Sets the AttributeValueDate.
	 *
	 * @param attributeValueDate The AttributeValueDate.
	 **/
	public DynamicAttribute setAttributeValueDate(LocalDate attributeValueDate) {
		this.attributeValueDate = attributeValueDate;
		return this;
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
		if (!(o instanceof DynamicAttribute)) return false;

		DynamicAttribute that = (DynamicAttribute) o;

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
}
