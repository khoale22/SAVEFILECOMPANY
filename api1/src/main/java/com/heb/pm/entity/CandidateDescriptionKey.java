/*
 *  CandidateDescriptionKey
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
 * Represents the key for the customer friendly descriptions of candidate.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateDescriptionKey implements Serializable {

	/**
	 * LANGUAGE_ENGLISH.
	 */
	public static final String LANGUAGE_ENGLISH = "ENG";
	/**
	 * LANGUAGE_SPANISH.
	 */
	public static final String LANGUAGE_SPANISH = "SPN";
    /**
     * DESCRIPTION_TYPE_SGNRC.
     */
    public static final String DESCRIPTION_TYPE_SGNRC = "SGNRC";
    /**
     * DESCRIPTION_TYPE_SGNRP.
     */
    public static final String DESCRIPTION_TYPE_SGNRP = "SGNRP";
    /**
     * DESCRIPTION_TYPE_SRVCC.
     */
    public static final String DESCRIPTION_TYPE_SRVCC = "SRVCC";

    private static final long serialVersionUID = 1L;

    @Column(name = "PS_PROD_ID", nullable = false)
    private Long candidateProductId;

    @Column(name = "LANG_TYP_CD", nullable = false, length = 5)
    @Type(type = "fixedLengthCharPK")
    private String languageType;

    @Column(name = "DES_TYP_CD", nullable = false, length = 5)
    @Type(type = "fixedLengthCharPK")
    private String descriptionType;

    /**
     * Returns the product id.
     *
     * @return the product id.
     */
    public Long getCandidateProductId() {
        return this.candidateProductId;
    }

    /**
     * Sets the product id.
     *
     * @param candidateProductId the product id.
     */
    public void setCandidateProductId(Long candidateProductId) {
        this.candidateProductId = candidateProductId;
    }

    /**
     * Returns the langugage the description is in.
     *
     * @return the langugage the description is in.
     */
    public String getLanguageType() {
        return this.languageType;
    }

    /**
     * Sets the langugage the description is in.
     *
     * @param languageType the langugage the description is in.
     */
    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    /**
     * Returns the type of description.
     *
     * @return the type of description.
     */
    public String getDescriptionType() {
        return this.descriptionType;
    }

    /**
     * Sets the type of description.
     *
     * @param descriptionType the type of description.
     */
    public void setDescriptionType(String descriptionType) {
        this.descriptionType = descriptionType;
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
        if (!(o instanceof CandidateDescriptionKey)) return false;

        CandidateDescriptionKey that = (CandidateDescriptionKey) o;

        if (candidateProductId != null ? !candidateProductId.equals(that.candidateProductId) : that.candidateProductId != null) return false;
        if (languageType != null ? !languageType.equals(that.languageType) : that.languageType != null) return false;
        return descriptionType != null ? descriptionType.equals(that.descriptionType) : that.descriptionType == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = candidateProductId != null ? candidateProductId.hashCode() : 0;
        result = 31 * result + (languageType != null ? languageType.hashCode() : 0);
        result = 31 * result + (descriptionType != null ? descriptionType.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateDescriptionKey{" +
                "candidateProductId=" + candidateProductId +
                ", languageType='" + languageType + '\'' +
                ", descriptionType='" + descriptionType + '\'' +
                '}';
    }
}
