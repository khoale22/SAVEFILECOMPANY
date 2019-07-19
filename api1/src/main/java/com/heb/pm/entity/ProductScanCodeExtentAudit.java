/*
 * ProductScanCodeExtentAudit
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents Product Scan Code Extent Audit Data.
 * @author vn70529
 * @since 2.15.0
 */
@Entity
@Table(name = "prod_scn_cd_ext_au")
public class ProductScanCodeExtentAudit implements Serializable, Audit {

    @EmbeddedId
    private ProductScanCodeExtentAuditKey key;

    @Column(name = "prod_ext_dta_cd")
    private String prodExtDataCode;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @AuditableField(displayName = "Product Description", filter = FilterConstants.PRODUCT_SCAN_CODE_EXTENT_AUDIT)
    @Column(name = "prod_des_txt")
    private String productDescription;

    /**
     * PROD_EXT_DTA_CD_FOR_ECOM_DES1.
     */
    public static final String PROD_EXT_DTA_CD_FOR_ECOM_DES1 = "ESHRT";
    /**
     * PROD_EXT_DTA_CD_FOR_ECOM_DES2.
     */
    public static final String PROD_EXT_DTA_CD_FOR_ECOM_DES2 = "ELONG";

    /** The Constant AUDIT_ATTR_FOR_ECOMMERCE_VIEW_DISP_NAME. */
    public static final String AUDIT_ATTR_FOR_ECOMMERCE_VIEW_DISP_NAME = "Display Name";

    /** The Constant AUDIT_ATTR_FOR_ECOMMERCE_VIEW_ROMANCE_COPY. */
    public static final String AUDIT_ATTR_FOR_ECOMMERCE_VIEW_ROMANCE_COPY = "Romance Copy";
    private static final String PROD_EXT_DATA_CODE_SORT_FIELD = "prodExtDataCode";
    private static final String AUD_REC_CRE8_TS_SORT_FIELD = "changedOn";

    /**
     * Gets prod ext data code.
     *
     * @return the prod ext data code that is tied to a description that then provides the measurement for that code.
     */
    public String getProdExtDataCode() {
        return prodExtDataCode;
    }

    /**
     * Sets prod ext data code.
     *
     * @param prodExtDataCode the prod ext data code that is tied to a description that then provides the measurement for that code.
     */
    public void setProdExtDataCode(String prodExtDataCode) {
        this.prodExtDataCode = prodExtDataCode;
    }

    /**
     * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
     * 	@return ActionCode
     */
    @Override
    public String getAction() {
        return action;
    }

    /**
     * Updates the action code
     * @param action the new action
     */
    @Override
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Returns changed by. The changed by shows who was doing the action that is being audited. This is the uid(login) that a
     * user has.
     *
     * @return the changed by
     */
    @Override
    public String getChangedBy() {
        return changedBy;
    }

    /**
     * Sets the changed by uid received from the database.
     *
     * @param changedBy the changed by
     */
    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    /**
     * Gets the changed on time. This is when the modification was done.
     *
     * @return The time the modification was done.
     */
    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    /**
     * Sets the changed on time.
     *
     * @param changedOn The time the modification was done.
     */
    @Override
    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public ProductScanCodeExtentAuditKey getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(ProductScanCodeExtentAuditKey key) {
        this.key = key;
    }

    /**
     * Gets prod description text. That holds values tied to the prod_ext_dta_cd within the key.
     *
     * @return the prod description text that holds values tied to the prod_ext_dta_cd within the key.
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Sets prod description text that holds values tied to the prod_ext_dta_cd within the key.
     *
     * @param productDescription the prod description text that holds values tied to the prod_ext_dta_cd within the key.
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Method to call to retrieve the display name for product scan code Extent audit.
     *
     * @return  ProductScanCodeExtentAudit display name.
     */
    public String getProductScanCodeExtentAuditDisplayName() {
        if(this.prodExtDataCode.equals(this.PROD_EXT_DTA_CD_FOR_ECOM_DES1)) {
            return this.AUDIT_ATTR_FOR_ECOMMERCE_VIEW_DISP_NAME;
        } else if(this.prodExtDataCode.equals(this.PROD_EXT_DTA_CD_FOR_ECOM_DES2)) {
            return this.AUDIT_ATTR_FOR_ECOMMERCE_VIEW_ROMANCE_COPY;
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Compares another object to this one. If that object is an ProductScanCodeExtentAudit, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductScanCodeExtentAudit that = (ProductScanCodeExtentAudit) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(action, that.action) &&
                Objects.equals(changedBy, that.changedBy) &&
                Objects.equals(prodExtDataCode, that.prodExtDataCode) &&
                Objects.equals(productDescription, that.productDescription) ;
    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(key, action, changedBy, prodExtDataCode, productDescription);
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "ProductScanCodeExtentAudit{" +
                "key=" + key +
                ", action='" + action +
                ", changedBy='" + changedBy +
                ", prodExtDataCode='" + prodExtDataCode +
                ", productDescription='" + productDescription +
                '}';
    }
}
