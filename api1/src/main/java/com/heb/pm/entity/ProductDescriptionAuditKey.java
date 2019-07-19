/*
 * ProductDescriptionAuditKey
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The key for the customer friendly descriptions audit
 * that will be tied to the product master
 * Created by a786878
 * @since 2.16.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductDescriptionAuditKey implements Serializable{

    private static final long serialVersionUID = 1L;

    @Column(name="prod_id")
    private long productId;

    @Column(name = "lang_typ_cd")
    @Type(type="fixedLengthCharPK")    
    private String languageType;

    @Column(name = "des_typ_cd")
	@Type(type="fixedLengthCharPK")
	private String descriptionType;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

    /**
     * Returns the product ID
     *
     * @return product ID
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Sets a new product id
     *
     * @param productId the new value for the product id
     */
    public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * Returns the langugage the description is in
     *
     * @return the description's language
     */
    public String getLanguageType() {
        return languageType;
    }

    /**
     * Updates the language for a description
     *
     * @param languageType the new descriptioin language
     */
    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

	/**
	 * Returns the type of description
	 * @return description type
	 */
	public String getDescriptionType() {
		return descriptionType;
	}

	/**
	 * Updates the description type
	 * @param descriptionType new description*/
	public void setDescriptionType(String descriptionType) {
		this.descriptionType = descriptionType;
	}

	/**
     * Compares another object to this one. The key is the only thing used to determine equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDescriptionAuditKey that = (ProductDescriptionAuditKey) o;
        return productId == that.productId &&
                Objects.equals(languageType, that.languageType) &&
                Objects.equals(descriptionType, that.descriptionType) &&
                Objects.equals(changedOn, that.changedOn);
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {

        return Objects.hash(productId, languageType, descriptionType, changedOn);
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "ProductDescriptionAuditKey{" +
                "productId=" + productId +
                ", languageType='" + languageType + '\'' +
                ", descriptionType='" + descriptionType + '\'' +
                ", changedOn=" + changedOn +
                '}';
    }

    /**
     * Returns the timestamp for when the record was made
     * @return changedOn
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Sets the timestamp for when the record was made
     * @param changedOn
     */
    public void setChangedOn(LocalDateTime changedOn) {
        this.changedOn = changedOn;
    }
}
