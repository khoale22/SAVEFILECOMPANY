/*
 *  MasterDataExtensionAttributeKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This is the key for master data extension attribute.
 *
 * @author l730832
 * @since 2.13.0
 */
@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
@Embeddable
public class MasterDataExtensionAttributeKey implements Serializable {

	public static final String PRODUCT_KEY_TYPE = "PROD";

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "attr_id")
	private Long attributeId;

	@Column(name = "key_id")
	private long id;

	@Column(name = "itm_prod_key_cd")
	@Type(type="fixedLengthCharPK")
	private String itemProdIdCode;

	@Column(name = "dta_src_sys")
	private Long dataSourceSystem;

	@Column(name = "seq_nbr")
	private long sequenceNumber;

	/**
	 * Returns the AttributeId. The id of the corresponding attribute
	 *
	 * @return AttributeId
	 */
	public Long getAttributeId() {
		return attributeId;
	}

	/**
	 * Sets the AttributeId
	 *
	 * @param attributeId The AttributeId
	 */
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * Returns the Id. This is either the prodId or the upc according to the ItemProdIdCode.
	 *
	 * @return Id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the Id
	 *
	 * @param id The Id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the ItemProdIdCode. This determines whether or not the id is a product id or a upc.
	 *
	 * @return ItemProdIdCode
	 */
	public String getItemProdIdCode() {
		return itemProdIdCode;
	}

	/**
	 * Sets the ItemProdIdCode
	 *
	 * @param itemProdIdCode The ItemProdIdCode
	 */
	public void setItemProdIdCode(String itemProdIdCode) {
		this.itemProdIdCode = itemProdIdCode;
	}

	/**
	 * Returns the DataSourceSystem. The source system of the data.
	 *
	 * @return DataSourceSystem
	 */
	public Long getDataSourceSystem() {
		return dataSourceSystem;
	}

	/**
	 * Sets the DataSourceSystem
	 *
	 * @param dataSourceSystem The DataSourceSystem
	 */
	public void setDataSourceSystem(Long dataSourceSystem) {
		this.dataSourceSystem = dataSourceSystem;
	}


	public long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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

		MasterDataExtensionAttributeKey that = (MasterDataExtensionAttributeKey) o;

		if (id != that.id) return false;
		if (attributeId != null ? !attributeId.equals(that.attributeId) : that.attributeId != null) return false;
		if (itemProdIdCode != null ? !itemProdIdCode.equals(that.itemProdIdCode) : that.itemProdIdCode != null)
			return false;
		return dataSourceSystem != null ? dataSourceSystem.equals(that.dataSourceSystem) : that.dataSourceSystem == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = attributeId != null ? attributeId.hashCode() : 0;
		result = PRIME_NUMBER * result + (int) (id ^ (id >>> 32));
		result = PRIME_NUMBER * result + (itemProdIdCode != null ? itemProdIdCode.hashCode() : 0);
		result = PRIME_NUMBER * result + (dataSourceSystem != null ? dataSourceSystem.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "MasterDataExtensionAttributeKey{" +
				"attributeId=" + attributeId +
				", id=" + id +
				", itemProdIdCode='" + itemProdIdCode + '\'' +
				", dataSourceSystem=" + dataSourceSystem +
				'}';
	}
}
