/*
 * ItemSizeUomCode
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Holds the information relating to the ItemSizeUomCode.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Entity
@Table(name="ITM_SZ_UOM_CODE")
@TypeDefs({
	@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
public class ItemSizeUomCode implements Serializable{

    @Id
    @Column(name="ITM_SZ_UOM_CD")
    private String itemSizeUomCd;
    @Column(name="ITM_SZ_UOM_ABB")
    private String itemSizeUomAbb;
    @Column(name = "ITM_SZ_UOM_DES")
    private String itemSizeUomDescription;

	/**
	 * Returns item size UOM Code.
	 * @return item size UOM Code.
	 */
	public String getItemSizeUomCd() {
		return itemSizeUomCd;
	}

	/**
	 * Sets item size UOM Code.
	 * @param itemSizeUomCd item size UOM Code.
	 */
	public void setItemSizeUomCd(String itemSizeUomCd) {
		this.itemSizeUomCd = itemSizeUomCd;
	}

	/**
	 * Returns item size UOM Abb.
	 * @return item size UOM Abb.
	 */
	public String getItemSizeUomAbb() {
		return itemSizeUomAbb;
	}

	/**
	 * Sets item size UOM Abb.
	 * @param itemSizeUomAbb item size UOM Abb.
	 */
	public void setItemSizeUomAbb(String itemSizeUomAbb) {
		this.itemSizeUomAbb = itemSizeUomAbb;
	}

	/**
	 * Returns item size UOM description.
	 * @return item size UOM description.
	 */
	public String getItemSizeUomDescription() {
		return itemSizeUomDescription;
	}

	/**
	 * Sets item size UOM description.
	 * @param itemSizeUomDescription item size UOM description.
	 */
	public void setItemSizeUomDescription(String itemSizeUomDescription) {
		this.itemSizeUomDescription = itemSizeUomDescription;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ItemSizeUomCode{" +
				"itemSizeUomCd=" + itemSizeUomCd +
				", itemSizeUomAbb='" + itemSizeUomAbb + '\'' +
				", itemSizeUomDescription=" + itemSizeUomDescription +
				'}';
	}
}
