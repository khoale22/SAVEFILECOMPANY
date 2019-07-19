/*
 * ProductScanImageURIAudit
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.ldap.repository.Query;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Holds information about a product's image information
 *
 * @author vn70529
 * @version 2.39.0
 */
@Entity
@Table(name = "prod_scn_img_uri_a")
public class ProductScanImageURIAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProductScanImageURIAuditKey key;

    @Column(name = "ACTV_ONLIN_SW")
    private Boolean activeOnline;

    @Column(name = "IMG_TYP_CD")
    private String imageTypeCode;

    @Column(name = "ACTV_SW")
    private Boolean activeSwitch;

    @Column(name= "IMG_STAT_CD")
    private String imageStatusCode;

    /**
     * Returns glag indicator if image is active on heb.com.
     *
     * @return activeOnline
     */
    public Boolean isActiveOnline() {
        return activeOnline;
    }

    /**
     * Updates activeOnline
     *
     * @param activeOnline the new activeOnline
     */
    public void setActiveOnline(Boolean activeOnline) {
        this.activeOnline = activeOnline;
    }

    /**
     * Return the unique identifier that ties an image type to the productScanImageURI
     *
     * @return imageTypeCode
     */
    public String getImageTypeCode() {
        return imageTypeCode;
    }

    /**
     * Set the imageTypeCode
     *
     * @param imageTypeCode the new imageTypeCode
     */
    public void setImageTypeCode(String imageTypeCode) {
        this.imageTypeCode = imageTypeCode;
    }

    /**
     * Gets the active status of an image
     *
     * @return
     */
    public Boolean getActiveSwitch() {
        return activeSwitch;
    }

    /**
     * Updates activeSwitch
     *
     * @param activeSwitch
     */
    public void setActiveSwitch(Boolean activeSwitch) {
        this.activeSwitch = activeSwitch;
    }

    /**
     * Returns the unique identifier that ties image status to this object
     *
     * @return imageStatusCode
     */
    public String getImageStatusCode() {
        return imageStatusCode;
    }

    /**
     * Updates the imageStatusCode
     *
     * @param imagStatusCode the new imageStatusCode
     */
    public void setImageStatusCode(String imagStatusCode) {
        this.imageStatusCode = imagStatusCode;
    }

    /**
     * Returns the key to uniquely identify the product scan image audit
     *
     * @return the key
     */
    public ProductScanImageURIAuditKey getKey() {
        return key;
    }

    /**
     * Sets the key to uniquely identify the product scan image audit
     *
     * @param key the new key
     */
    public void setKey(ProductScanImageURIAuditKey key) {
        this.key = key;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "ProductScanImageURIAudit{" +
                "key=" + key +
                ", activeOnline=" + activeOnline +
                ", imageStatusCode='" + imageStatusCode + '\'' +
                ", imageTypeCode='" + imageTypeCode + '\'' +
                ", activeSwitch=" + activeSwitch +
                '}';
    }

    /**
     * Compares another object to this one. If that object is a ProductScanImageURIAudit, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductScanImageURIAudit)) {
            return false;
        }

        ProductScanImageURIAudit that = (ProductScanImageURIAudit) o;
        if (this.key != null ? !this.key.equals(that.key) : that.key != null) return false;

        return true;
    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return this.key == null ? 0 : this.key.hashCode();
    }
}
