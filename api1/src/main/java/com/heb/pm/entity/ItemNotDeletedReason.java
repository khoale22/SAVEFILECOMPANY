/*
 * com.heb.pm.entity.ItemNotDeletedReason
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the types of Reason(s) for why a particular item was not deleted when the item went through
 * the Delete process.
 *
 * @author vn40486
 * @since 2.0.4
 */
@Entity
@Table(name = "itm_nt_deld_rsn")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ItemNotDeletedReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="itm_nt_deld_rsn_cd")
    //db2o changes  vn00907
    @Type(type="fixedLengthCharPK") 
    private String code;

    @Column(name="itm_nt_deld_rsn_abb")
    //db2o changes  vn00907
    @Type(type="fixedLengthChar")    
    private String abbreviation;

    @Column(name="itm_nt_deld_rsn_des")
    //db2o changes  vn00907
    @Type(type="fixedLengthChar")    
    private String description;

	@Column(name="usr_instn_txt")
	private String userInstructions;


	/**
	 * Gets user instructions.
	 *
	 * @return the user instructions
	 */
	public String getUserInstructions() {
		return userInstructions;
	}

	/**
	 * Sets user instructions.
	 *
	 * @param userInstructions the user instructions
	 */
	public void setUserInstructions(String userInstructions) {
		this.userInstructions = userInstructions;
	}

	/**
     * Returns item not delete reason code.
     *
     * @return item not delete reason code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets item not delete reason code.
     *
     * @param code item not delete reason code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the abbreviation of the item not deleted reason.
     *
     * @return item not deleted reason abbreviation or short description
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Returns abbreviation or short description of item not deleted reason.
     *
     * @param abbreviation item not deleted reason abbreviation.
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Returns description of item not deleted reason.
     *
     * @return item not deleted reason description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the item not deleted reason.
     *
     * @return item not deleted reason description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Compares another object to this one. If that object is an ItemNotDeletedReason, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemNotDeletedReason that = (ItemNotDeletedReason) o;

        return !(code != null ? !code.equals(that.code) : that.code != null);

    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "ItemNotDeletedReason{" +
                "code='" + code + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
