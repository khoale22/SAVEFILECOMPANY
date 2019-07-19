/*
 * RelatedItem
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a related_items
 *
 * @author vn73545
 * @since 2.12.0
 */
@Entity
@Table(name = "related_items")
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class RelatedItem implements Serializable {
    public static String ITM_RLSHP_TYP_CD_MORPH = "MORPH";

    @EmbeddedId
    private RelatedItemKey key;
    
	@Column(name = "itm_rlshp_typ_cd")
    @Type(type="fixedLengthCharPK")
    private String itemRelationshipType;

	@Column(name = "related_itm_key_ty")
	@Type(type="fixedLengthCharPK")
	private String relatedItemKeyType;

	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties({"vendorLocationItems", "primaryUpc", "warehouseLocationItemExtendedAttributes",
		"warehouseLocationItems", "itemNotDeleted", "vendorLocationItemMaster", "merchandiseTypeOne", "prodItems"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "related_itm_key_ty", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false),
			@JoinColumn(name = "related_itm_id", referencedColumnName = "itm_id", insertable = false,  updatable = false)
	})
	private ItemMaster itemMasterByRelatedItemCode;

	/**
	 * Gets the value of key.
	 * 
	 * @return the key
	 */
	public RelatedItemKey getKey() {
		return key;
	}

	/**
	 * Sets the value of key.
	 * 
	 * @param key the key to set
	 */
	public void setKey(RelatedItemKey key) {
		this.key = key;
	}

	/**
	 * Gets the value of itemMaster by relatedItemCode.
	 * 
	 * @return the itemMasterByRelatedItemCode
	 */
	public ItemMaster getItemMasterByRelatedItemCode() {
		return itemMasterByRelatedItemCode;
	}

	/**
	 * Sets the value of itemMaster by relatedItemCode.
	 * 
	 * @param itemMasterByRelatedItemCode the itemMasterByRelatedItemCode to set
	 */
	public void setItemMasterByRelatedItemCode(ItemMaster itemMasterByRelatedItemCode) {
		this.itemMasterByRelatedItemCode = itemMasterByRelatedItemCode;
	}

	/**
	 * Gets the value of itemRelationshipType.
	 * 
	 * @return the itemRelationshipType
	 */
	public String getItemRelationshipType() {
		return itemRelationshipType;
	}

	/**
	 * Sets the value of itemRelationshipType.
	 * 
	 * @param itemRelationshipType the itemRelationshipType to set
	 */
	public void setItemRelationshipType(String itemRelationshipType) {
		this.itemRelationshipType = itemRelationshipType;
	}

	/**
	 * Gets the value of relatedItemKeyType.
	 * 
	 * @return the relatedItemKeyType
	 */
	public String getRelatedItemKeyType() {
		return relatedItemKeyType;
	}

	/**
	 * Sets the value of relatedItemKeyType.
	 * 
	 * @param relatedItemKeyType the relatedItemKeyType to set
	 */
	public void setRelatedItemKeyType(String relatedItemKeyType) {
		this.relatedItemKeyType = relatedItemKeyType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemMasterByRelatedItemCode == null) ? 0 : itemMasterByRelatedItemCode.hashCode());
		result = prime * result + ((itemRelationshipType == null) ? 0 : itemRelationshipType.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((relatedItemKeyType == null) ? 0 : relatedItemKeyType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelatedItem other = (RelatedItem) obj;
		if (itemMasterByRelatedItemCode == null) {
			if (other.itemMasterByRelatedItemCode != null)
				return false;
		} else if (!itemMasterByRelatedItemCode.equals(other.itemMasterByRelatedItemCode))
			return false;
		if (itemRelationshipType == null) {
			if (other.itemRelationshipType != null)
				return false;
		} else if (!itemRelationshipType.equals(other.itemRelationshipType))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (relatedItemKeyType == null) {
			if (other.relatedItemKeyType != null)
				return false;
		} else if (!relatedItemKeyType.equals(other.relatedItemKeyType))
			return false;
		return true;
	}

}
