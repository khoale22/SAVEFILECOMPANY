/*
 * CandidateSellingUnit
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents a candidate retail selling unit.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_prod_scn_codes")
//dB2Oracle changes vn00907 starts
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateSellingUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int FOUR_BYTES = 32;
    public static final String SCN_TYPE_CD_EAN = "EAN";
    public static final String SCN_TYPE_CD_UPC = "UPC";
    @EmbeddedId
    private CandidateSellingUnitKey key;

    @Column(name = "scn_cd_id")
    private Long upc;

    @Column(name = "tag_sz_des")
    @Type(type = "fixedLengthChar")
    private String tagSize;

    @Column(name = "bns_scn_cd_sw")
    private Boolean bonusScanCode;

    @Column(name = "retl_unt_sell_sz_1")
    private Double quantity;

    @Column(name = "retl_unt_sell_sz_2")
    private Double quantity2;

    @Column(name = "retl_unt_wt")
    private Double retailWeight;

    @Column(name = "tst_scn_ovrd_sw")
    private Boolean testScanned;

    @Column(name = "retl_unt_ln")
    private Double retailLength;

    @Column(name = "retl_unt_wd")
    private Double retailWidth;

    @Column(name = "retl_unt_ht")
    private Double retailHeight;

    @Column(name = "lst_updt_ts")
    private Date lastUpdatedOn;

    @Column(name = "lst_updt_usr_id")
    private String lastUpdatedBy;

    @Column(name = "pse_grams_wt")
    private Double pseGramWeight;

    @Column(name = "wic_apl_id")
    private Long wicApprovedProductListId;

    /**
     * The scn typ cd.
     */
    @Column(name = "SCN_TYP_CD", length = 5)
    @Type(type="fixedLengthCharPK")
    private String scanTypeCode;

    @JsonIgnoreProperties
    @Transient
    private String unitUpc;

    /**
     * The retl sell sz cd1.
     */
    @Column(name = "RETL_SELL_SZ_CD1", length = 2)
    @Type(type="fixedLengthCharPK")
    private String retailUnitOfMeasureCode;

    /**
     * The new data sw.
     */
    @Column(name = "NEW_DATA_SW")
    private boolean newDataSwitch;

    /**
     * The prod sub brnd id.
     */
    @Column(name = "PROD_SUB_BRND_ID")
    private Long productSubBrandId;

    /**
     * The sample provd sw.
     */
    @Column(name = "SAMPLE_PROVD_SW")
    private boolean sampleProvdSwitch;
    /**
     * The retl sell sz cd1.
     */
    @Column(name = "RETL_SELL_SZ_CD2", length = 2)
    @Type(type="fixedLengthCharPK")
    private String retailUnitOfMeasureCode2;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retl_sell_sz_cd1", referencedColumnName = "retl_sell_sz_cd", insertable = false, updatable = false)
    private RetailUnitOfMeasure retailUnitOfMeasure;
    /**
     * The ps product master.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PS_PROD_ID", nullable = false, insertable = false, updatable = false)
    private CandidateProductMaster candidateProductMaster;
    /**
     * Gets the key composite key to represent the primary keys for the candidate selling unit table.
     *
     * @return the key composite key to represent the primary keys for the candidate selling unit table.
     */
    public CandidateSellingUnitKey getKey() {
        return key;
    }

    /**
     * Sets the key composite key to represent the primary keys for the candidate selling unit table.
     *
     * @param key the key composite key to represent the primary keys for the candidate selling unit table.
     */
    public void setKey(CandidateSellingUnitKey key) {
        this.key = key;
    }

    /**
     * Gets last updated on.
     *
     * @return the last updated on date that the CPS measuring data was modified.
     */
    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    /**
     * Sets last updated on.
     *
     * @param lastUpdatedOn the last updated on date that the CPS measuring data was modified.
     */
    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    /**
     * Gets last updated by user ID of the user that modified the CPS measuring data last.
     *
     * @return the last updated by user ID of the user that modified the CPS measuring data last.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets last updated by user ID of the user that modified the CPS measuring data last.
     *
     * @param lastUpdatedBy the last updated by user ID of the user that modified the CPS measuring data last.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }


    /**
     * Gets retail height for the created product in cps
     *
     * @return the retail height for the created product in cps
     */
    public Double getRetailHeight() {
        return retailHeight;
    }

    /**
     * Sets retail height for the created product in cps
     *
     * @param retailHeight the retail height for the created product in cps
     */
    public void setRetailHeight(Double retailHeight) {
        this.retailHeight = retailHeight;
    }

    /**
     * Gets retail width for the created product in cps
     *
     * @return the retail width for the created product in cps
     */
    public Double getRetailWidth() {
        return retailWidth;
    }

    /**
     * Sets retail width for the created product in cps
     *
     * @param retailWidth the retail width for the created product in cps
     */
    public void setRetailWidth(Double retailWidth) {
        this.retailWidth = retailWidth;
    }

    /**
     * Gets retail length for the created product in cps
     *
     * @return the retail length for the created product in cps.
     */
    public Double getRetailLength() {
        return retailLength;
    }

    /**
     * Sets retail length for the created product in cps
     *
     * @param retailLength the retail length for the created product in cps
     */
    public void setRetailLength(Double retailLength) {
        this.retailLength = retailLength;
    }

    /**
     * Is test scanned boolean.  Switch that keeps track if upc was scanned at the setup time to ensure the upc being setup correctly.
     *
     * @return the Boolean switch that keeps track if upc was scanned at the setup time to ensure the upc being setup correctly.
     */
    public Boolean isTestScanned() {
        return testScanned;
    }

    /**
     * Sets test scanned. Switch that keeps track if upc was scanned at the setup time to ensure the upc being setup correctly.
     *
     * @param testScanned the test scanned switch that keeps track if upc was scanned at the setup time to ensure the upc being setup correctly.
     */
    public void setTestScanned(Boolean testScanned) {
        this.testScanned = testScanned;
    }

    /**
     * Gets retail weight of the given upc.
     *
     * @return the retail weight  of the upc.
     */
    public Double getRetailWeight() {
        return retailWeight;
    }

    /**
     * Sets retail weight of the given upc.
     *
     * @param retailWeight the retail weight of the upc.
     */
    public void setRetailWeight(Double retailWeight) {
        this.retailWeight = retailWeight;
    }

    /**
     * Gets retail unit of measure which is used to quantify the inventory items and enables to track them.
     *
     * @return the retail unit of measure which is used to quantify the inventory items and enables to track them.
     */
    public RetailUnitOfMeasure getRetailUnitOfMeasure() {
        return retailUnitOfMeasure;
    }

    /**
     * Sets retail unit of measure which is used to quantify the inventory items and enables to track them.
     *
     * @param retailUnitOfMeasure the retail unit of measure which is used to quantify the inventory items and enables to track them.
     */
    public void setRetailUnitOfMeasure(RetailUnitOfMeasure retailUnitOfMeasure) {
        this.retailUnitOfMeasure = retailUnitOfMeasure;
    }

    /**
     * Gets quantity the selling size of the product
     *
     * @return the quantity is the selling size of the product
     */
    public Double getQuantity() {
        return quantity;
    }
    public Double getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(Double quantity2) {
        this.quantity2 = quantity2;
    }

    /**
     * Sets quantity the selling size of the product
     *
     * @param quantity the quantity selling size of the product
     */
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the selling unit's UPC. UPC is used generically here. This could be a UPC, PLU, EAN, etc.
     *
     * @return The selling unit's UPC. UPC is used generically here. This could be a UPC, PLU, EAN, etc.
     */
    public Long getUpc() {
        return upc;
    }

    /**
     * Sets the selling unit's UPC.
     *
     * @param upc The selling unit's UPC.
     */
    public void setUpc(Long upc) {
        this.upc = upc;
    }

    /**
     * Returns the size printed on the shelf tags.
     *
     * @return The size printed on the shelf tags.
     */
    public String getTagSize() {
        return tagSize;
    }

    /**
     * Sets the size printed on the shelf tags.
     *
     * @param tagSize The size printed on the shelf tags.
     */
    public void setTagSize(String tagSize) {
        this.tagSize = tagSize;
    }

    /**
     * Returns the bonusScanCode for an item.
     *
     * @return The bonusScanCode for an item.
     */
    public Boolean isBonusScanCode() {
        return bonusScanCode;
    }

    /**
     * Sets the bonusScanCode for an item.
     *
     * @param bonusScanCode the bonusScanCode for an item.
     */
    public void setBonusScanCode(Boolean bonusScanCode) {
        this.bonusScanCode = bonusScanCode;
    }

    /**
     * Gram weight of PSE in the item if flagged with PSE.
     *
     * @return pseGramWeight;
     */
    public Double getPseGramWeight() {
        return pseGramWeight;
    }

    /**
     * Updates pseGramWeight
     *
     * @param pseGramWeight the new pseGramWeight
     */
    public void setPseGramWeight(Double pseGramWeight) {
        this.pseGramWeight = pseGramWeight;
    }

    /**
     * Returns the WicApprovedProductListId. The approved product list id for the current product.
     *
     * @return WicApprovedProductListId
     */
    public Long getWicApprovedProductListId() {
        return wicApprovedProductListId;
    }

    /**
     * Sets the WicApprovedProductListId
     *
     * @param wicApprovedProductListId The WicApprovedProductListId
     */
    public void setWicApprovedProductListId(Long wicApprovedProductListId) {
        this.wicApprovedProductListId = wicApprovedProductListId;
    }
    public CandidateProductMaster getCandidateProductMaster() {
        return candidateProductMaster;
    }

    public void setCandidateProductMaster(CandidateProductMaster candidateProductMaster) {
        this.candidateProductMaster = candidateProductMaster;
    }

    public String getUnitUpc() {
        return unitUpc;
    }

    public void setUnitUpc(String unitUpc) {
        this.unitUpc = unitUpc;
    }
    public String getScanTypeCode() {
        return scanTypeCode;
    }

    public void setScanTypeCode(String scanTypeCode) {
        this.scanTypeCode = scanTypeCode;
    }
    public String getRetailUnitOfMeasureCode() {
        return retailUnitOfMeasureCode;
    }

    public void setRetailUnitOfMeasureCode(String retailUnitOfMeasureCode) {
        this.retailUnitOfMeasureCode = retailUnitOfMeasureCode;
    }

    public String getRetailUnitOfMeasureCode2() {
        return retailUnitOfMeasureCode2;
    }

    public void setRetailUnitOfMeasureCode2(String retailUnitOfMeasureCode2) {
        this.retailUnitOfMeasureCode2 = retailUnitOfMeasureCode2;
    }
    public boolean isNewDataSwitch() {
        return newDataSwitch;
    }

    public void setNewDataSwitch(boolean newDataSwitch) {
        this.newDataSwitch = newDataSwitch;
    }

    public Long getProductSubBrandId() {
        return productSubBrandId;
    }

    public void setProductSubBrandId(Long productSubBrandId) {
        this.productSubBrandId = productSubBrandId;
    }
    public boolean isSampleProvdSwitch() {
        return sampleProvdSwitch;
    }

    public void setSampleProvdSwitch(boolean sampleProvdSwitch) {
        this.sampleProvdSwitch = sampleProvdSwitch;
    }

    /**
     * Compares another object to this one. This only compares the keys.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateSellingUnit)) return false;

        CandidateSellingUnit that = (CandidateSellingUnit) o;

        return upc == that.upc;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return (int) (upc ^ (upc >>> CandidateSellingUnit.FOUR_BYTES));
    }

    @Override
    public String toString() {
        return "CandidateSellingUnit{" +
                "key=" + key +
                ", upc=" + upc +
                ", tagSize='" + tagSize + '\'' +
                ", bonusScanCode=" + bonusScanCode +
                ", quantity=" + quantity +
                ", retailWeight=" + retailWeight +
                ", testScanned=" + testScanned +
                ", retailLength=" + retailLength +
                ", retailWidth=" + retailWidth +
                ", retailHeight=" + retailHeight +
                ", lastUpdatedOn=" + lastUpdatedOn +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", pseGramWeight=" + pseGramWeight +
                ", wicApprovedProductListId=" + wicApprovedProductListId +
                ", retailUnitOfMeasure=" + retailUnitOfMeasure +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateProductId() {
        if (this.getKey().getCandidateProductId() == null) {
            this.getKey().setCandidateProductId(this.candidateProductMaster.getCandidateProductId());
        }
    }
}
