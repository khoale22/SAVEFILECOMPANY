/*
 * RelatedItemKey
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * Represents a ps_related_items data key
 *
 * @author vn73545
 * @since 2.12.0
 */
@Embeddable
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class RelatedItemKey implements Serializable {

	@Column(name = "related_itm_id")
    private Long relatedItemCode;
    
    @Column(name="itm_id")
	private Long itemCode;
    
    @Column(name="itm_key_typ_cd")
	@Type(type="fixedLengthCharPK")
	private String itemType;
    
    /**
	 * Gets the value of itemCode.
	 * 
	 * @return the itemCode
	 */
	public Long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the value of itemCode.
	 * 
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the item type for the ProductDiscontinue object.
	 *
	 * @return The item type for the ProductDiscontinue object.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Returns the item type for the ProductDiscontinue object.
	 *
	 * @param itemType The item type for the ProductDiscontinue object.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Gets the value of relatedItemCode.
	 * 
	 * @return the relatedItemCode
	 */
	public Long getRelatedItemCode() {
		return relatedItemCode;
	}

	/**
	 * Sets the value of relatedItemCode.
	 * 
	 * @param relatedItemCode the relatedItemCode to set
	 */
	public void setRelatedItemCode(Long relatedItemCode) {
		this.relatedItemCode = relatedItemCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RelatedItemKey)) return false;

		RelatedItemKey that = (RelatedItemKey) o;

		if (getItemCode() != null ? !getItemCode().equals(that.getItemCode()) : that.getItemCode() != null)
			return false;
		return getRelatedItemCode() != null ? getRelatedItemCode().equals(that.getRelatedItemCode()) : that.getRelatedItemCode() == null;
	}

	@Override
	public int hashCode() {
		int result = getItemCode() != null ? getItemCode().hashCode() : 0;
		result = 31 * result + (getRelatedItemCode() != null ? getRelatedItemCode().hashCode() : 0);
		return result;
	}
}
