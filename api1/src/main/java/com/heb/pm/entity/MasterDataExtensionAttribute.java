/*
 *  MasterDataExtensionAttribute
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This represents a master data extension attribute entity.
 *
 * @author l730832
 * @since 2.13.0
 */
@Entity
@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
@Table(name = "mst_dta_extn_attr")
public class MasterDataExtensionAttribute implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String STRING_ATTRIBUTE_VALUE = "S";
	public static final String TIME_ATTRIBUTE_VALUE = "TS";
	public static final String DATE_ATTRIBUTE_VALUE = "DT";
	public static final String DECIMAL_ATTRIBUTE_VALUE = "DEC";
	public static final String INTEGER_ATTRIBUTE_VALUE = "I";

	public static final int NOOP = 0;
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE= 3;

	@EmbeddedId
	private MasterDataExtensionAttributeKey key;

	@Column(name = "attr_cd_id")
	private Long attributeCodeId;

	@Column(name = "attr_val_txt")
	@Type(type="fixedLengthCharPK")
	private String attributeValueText;


	@Column(name = "attr_val_nbr")
    private Double attributeValueNumber;

	@Column(name = "ATTR_VAL_TS")
    private LocalDateTime attributeValueTime;

	@Column(name = "ATTR_VAL_DT")
	private LocalDate attributeValueDate;

	@Column(name = "LST_UPDT_TS")
	private LocalDate lstUpdtTs;

	@Column(name = "CRE8_UID")
	private String createUserId;

	@Column(name = "CRE8_TS")
	private LocalDateTime createTime;

	@Column(name = "LST_UPDT_UID")
	private String lastUdateUserId;

	@Where(clause = "enty_id=16")
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_id", referencedColumnName = "attr_id", insertable = false, updatable = false)
	private List<EntityAttribute> entityAttribute;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_id", referencedColumnName = "attr_id", insertable = false, updatable = false)
	private List<EcommerUserGroupAttribute> ecommerUserGroupAttributes;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({
        @JoinColumn(name = "attr_id", referencedColumnName = "attr_id", insertable = false, updatable = false)
	})
	private List<GroupAttribute> groupAttributes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_id", referencedColumnName = "attr_id", insertable = false, updatable = false)
	private Attribute attribute;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_cd_id", referencedColumnName = "attr_cd_id", insertable = false, updatable = false)
	private AttributeCode attributeCode;

	@Transient
	private int action;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public MasterDataExtensionAttributeKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(MasterDataExtensionAttributeKey key) {
		this.key = key;
	}

	/**
	 * @return the groupAttributes
	 */
	public List<GroupAttribute> getGroupAttributes() {
		return groupAttributes;
	}

	/**
	 * @param groupAttributes the groupAttributes to set
	 */
	public void setGroupAttributes(List<GroupAttribute> groupAttributes) {
		this.groupAttributes = groupAttributes;
	}

	/**
	 * @return the attributeValueDate
	 */
	public LocalDate getAttributeValueDate() {
		return attributeValueDate;
	}

	/**
	 * @param attributeValueDate the attributeValueDate to set
	 */
	public void setAttributeValueDate(LocalDate attributeValueDate) {
		this.attributeValueDate = attributeValueDate;
	}

	/**
	 * @return the attributeValueTime
	 */
	public LocalDateTime getAttributeValueTime() {
		return attributeValueTime;
	}

	/**
	 * @param attributeValueTime the attributeValueTime to set
	 */
	public void setAttributeValueTime(LocalDateTime attributeValueTime) {
		this.attributeValueTime = attributeValueTime;
	}

	/**
	 * @return the attributeValueNumber
	 */
	public Double getAttributeValueNumber() {
		return attributeValueNumber;
	}

	/**
	 * @param attributeValueNumber the attributeValueNumber to set
	 */
	public void setAttributeValueNumber(Double attributeValueNumber) {
		this.attributeValueNumber = attributeValueNumber;
	}

	/**
	 * Returns the AttributeCodeId. This is the code id for the attribute that is on the product.
	 *
	 * @return AttributeCodeId
	 */
	public Long getAttributeCodeId() {
		return attributeCodeId;
	}

	/**
	 * Sets the AttributeCodeId
	 *
	 * @param attributeCodeId The AttributeCodeId
	 */
	public void setAttributeCodeId(Long attributeCodeId) {
		this.attributeCodeId = attributeCodeId;
	}

	/**
	 * Returns the AttributeValueText
	 *
	 * @return AttributeValueText
	 */
	public String getAttributeValueText() {
		return attributeValueText;
	}

	/**
	 * Sets the AttributeValueText. The attribute value text is what the attribute is called. "GOLD, BLUE"
	 *
	 * @param attributeValueText The AttributeValueText
	 */
	public void setAttributeValueText(String attributeValueText) {
		this.attributeValueText = attributeValueText;
	}



	/**
	 * Sets the lstUpdtTs
	 */
	public LocalDate getLstUpdtTs() {
		return lstUpdtTs;
	}

	/**
	 * @return Gets the value of lstUpdtTs and returns lstUpdtTs
	 */
	public void setLstUpdtTs(LocalDate lstUpdtTs) {
		this.lstUpdtTs = lstUpdtTs;
	}

	/**
	 * Returns the attribute.
	 * @return the attribute.
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * Sets the attribute.
	 *
	 * @param attribute the attribute.
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * Returns the attributeCode.
	 *
	 * @return the attributeCode.
	 */
	public AttributeCode getAttributeCode() {
		return attributeCode;
	}

	/**
	 * Sets the attributeCode.
	 *
	 * @param attributeCode the attributeCode.
	 */
	public void setAttributeCode(AttributeCode attributeCode) {
		this.attributeCode = attributeCode;
	}
	/**
	 * Returns the EntityAttribute
	 *
	 * @return EntityAttribute
	 */
	public List<EntityAttribute> getEntityAttribute() {
		return entityAttribute;
	}


	/**
	 * Sets the EntityAttribute
	 *
	 * @param entityAttribute The EntityAttribute
	 */
	public void setEntityAttribute(List<EntityAttribute> entityAttribute) {
		this.entityAttribute = entityAttribute;
	}

	/**
	 * Return the list of EcommerUserGroupAttributes tied to this entity.
	 *
	 * @return The list of EcommerUserGroupAttributes tied to this entity.
	 */
	public List<EcommerUserGroupAttribute> getEcommerUserGroupAttributes() {
		return ecommerUserGroupAttributes;
	}

	/**
	 * Sets the list of EcommerUserGroupAttributes tied to this entity.
	 *
	 * @param ecommerUserGroupAttributes The list of EcommerUserGroupAttributes tied to this entity.
	 */
	public void setEcommerUserGroupAttributes(List<EcommerUserGroupAttribute> ecommerUserGroupAttributes) {
		this.ecommerUserGroupAttributes = ecommerUserGroupAttributes;
	}

	/**
	 * Returns the ID of the user who created this record.
	 *
	 * @return The ID of the user who created this record.
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Sets the ID of the user who created this record.
	 *
	 * @param createUserId The ID of the user who created this record.
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Returns the time this record was created.
	 *
	 * @return The time this record was created.
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the time this record was created.
	 *
	 * @param createTime The time this record was created.
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * Returns the ID of the user who last updated this record.
	 *
	 * @return The ID of the user who last updated this record.
	 */
	public String getLastUdateUserId() {
		return lastUdateUserId;
	}

	/**
	 * Sets the ID of the user who last updated this record.
	 *
	 * @param lastUdateUserId The ID of the user who last updated this record.
	 */
	public void setLastUdateUserId(String lastUdateUserId) {
		this.lastUdateUserId = lastUdateUserId;
	}

	/**
	 * Sets the action to perform with this record.
	 *
	 * @return The action to perform with this record.
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Sets the action to perform with this record.
	 *
	 * @param action The action to perform with this record.
	 */
	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MasterDataExtensionAttribute that = (MasterDataExtensionAttribute) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "MasterDataExtensionAttribute{" +
				"key=" + key +
				", attributeCodeId=" + attributeCodeId +
				", attributeValueText='" + attributeValueText + '\'' +
				", attributeValueNumber=" + attributeValueNumber +
				", attributeValueTime=" + attributeValueTime +
				", attributeValueDate=" + attributeValueDate +
				", lstUpdtTs=" + lstUpdtTs +
				", createUserId='" + createUserId + '\'' +
				", createTime=" + createTime +
				", lastUdateUserId='" + lastUdateUserId + '\'' +
				", attribute=" + attribute +
				", attributeCode=" + attributeCode +
				", action=" + action +
				'}';
	}
}
