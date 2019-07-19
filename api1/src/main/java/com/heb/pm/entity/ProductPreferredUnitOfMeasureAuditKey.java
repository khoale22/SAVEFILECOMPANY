/*
 * ProductPreferredUnitOfMeasureAuditKey
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * An embeddable key for a product preferred UOM audit.
 *
 * @author vn70529
 * @since 2.18.4
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductPreferredUnitOfMeasureAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

    @Column(name="pd_omi_sub_com_cd")
    private Integer subCommodityCode;

    /**
     * Returns SubCommodityCode that the product is labeled under.  Which is the lowest organization of the specific product.
     *
     * @return The SubCommodityCode that the product is labeled under. Which is the lowest organization of the specific product.
     **/
    public Integer getSubCommodityCode() {
        return subCommodityCode;
    }

    /**
     * Sets the SubCommodityCode that the product is labeled under. Which is the lowest organization of the specific product.
     *
     * @param subCommodityCode The SubCommodityCode that the product is labeled under. Which is the lowest organization of the specific product.
     **/
    public void setSubCommodityCode(Integer subCommodityCode) {
        this.subCommodityCode = subCommodityCode;
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
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "ProductPreferredUOMKey{" +
                "subCommodityCode=" + subCommodityCode +
                ", changedOn='" + changedOn + '\'' +
                '}';
    }
}
