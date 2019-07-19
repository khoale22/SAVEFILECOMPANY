/*
 * ProductDescriptionAudit
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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Ties customer friendly descriptions to the product master
 *
 * Created by a786878
 * @since 2.16.0
 */
@Entity
@Table(name="prod_desc_txt_aud")
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductDescriptionAudit implements Audit, Serializable{

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProductDescriptionAuditKey key;

    @AuditableField(displayNameMethod = "getAuditDescription", filter = {FilterConstants.SHELF_ATTRIBUTES_AUDIT, FilterConstants.ONLINE_ATTRIBUTES_AUDIT})
    @Column(name="prod_des")
    private String description;

	@OneToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumns({
			@JoinColumn(name="des_typ_cd", referencedColumnName = "des_typ_cd", insertable = false, updatable = false),
	})
	private DescriptionType descriptionType;

	@Column(name = "lst_updt_uid")
	private String lastUpdatedBy;

	@Column(name = "ACT_CD")
	private String action;

	/**
     * Returns the key for the ProductDescription audit object
     *
     * @return the key for the ProductDescription audit object
     */
    public ProductDescriptionAuditKey getKey() {
        return key;
    }

    /**
     * Sets the key for the ProductDescription audit object
     *
     * @param key The key for the ProductDescription audit object
     */
    public void setKey(ProductDescriptionAuditKey key) {
        this.key = key;
    }

    /**
     * Returns the description of the product
     * @return the description of the product
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the product
     * @param description the new description for the product
     */
    public void setDescription(String description) {
        this.description = description;
    }

	/**
	 * Returns the DescriptionType. The description type holds all of the information for the current description type
	 * including the length. The code table for description type.
	 *
	 * @return DescriptionType
	 */
	public DescriptionType getDescriptionType() {
		return descriptionType;
	}

	/**
	 * Sets the DescriptionType
	 *
	 * @param descriptionType The DescriptionType
	 */
	public void setDescriptionType(DescriptionType descriptionType) {
		this.descriptionType = descriptionType;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductDescription{" +
				"key=" + key +
				", description='" + description + '\'' +
				'}';
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal an false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductDescriptionAudit)) return false;

		ProductDescriptionAudit that = (ProductDescriptionAudit) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}


	/**
	 * Getter for lastUpdatedBy property
	 * @return lastUpdatedBy property
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * Setter for lastUpdatedBy property
	 * @param lastUpdatedBy lastUpdatedBy property
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * Getter for action property
	 * @return action property
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Setter for action property
	 * @param action property
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Getter for lastUpdatedBy property
	 * @return lastUpdatedBy property
	 */
	@Override
	public String getChangedBy() {
		return lastUpdatedBy;
	}

	/**
	 * Setter for changedBy attribute
	 * @param changedBy the changed by
	 */
	@Override
	public void setChangedBy(String changedBy) {
		this.lastUpdatedBy = changedBy;
	}

	/**
	 * Getter for getChangedOn attribute
	 * @return getChangedOn attribute
	 */
	@Override
	public LocalDateTime getChangedOn() {
		return key.getChangedOn();
	}

	@Override
	public void setChangedOn(LocalDateTime changedOn) {

	}
	/**
	 * Return a user-friendly description for each type.
	 *
	 * @return A user-friendly description for each type.
	 */
	public String getAuditDescription() {

		if (this.key.getDescriptionType() == null) {
			return "Unknown";
		}
		if (this.key.getDescriptionType().equals(DescriptionType.Codes.STANDARD.getId().trim())) {
			if (this.key.getLanguageType().equals(ProductDescription.ENGLISH)) {
				return "English Description";
			}
			return "Spanish Description";
		}
		if (this.key.getDescriptionType().equals(DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_ONE.getId().trim())) {
			if (this.key.getLanguageType().equals(ProductDescription.ENGLISH)) {
				return "English CFD Line 1";
			}
			return "Spanish CFD Line 2";
		}

		if (this.key.getDescriptionType().equals(DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_TWO.getId().trim())) {
			if (this.key.getLanguageType().equals(ProductDescription.ENGLISH)) {
				return "English CFD Line 1";
			}
			return "Spanish CFD Line 2";
		}

		if (this.key.getDescriptionType().equals(DescriptionType.Codes.PROPOSED_SIGN_ROMANCE_COPY.getId().trim())) {
			return "Proposed Service Case Sign Copy";
		}

		if (this.key.getDescriptionType().equals(DescriptionType.Codes.SIGN_ROMANCE_COPY.getId().trim())) {
			return "Service Case Sign Copy";
		}

		if (this.key.getDescriptionType().equals(DescriptionType.Codes.PRIMO_PICK_LONG.getId().trim())) {
			return "Primo Pick Story";
		}

		if (this.key.getDescriptionType().equals(DescriptionType.Codes.PRIMO_PICK_SHORT.getId().trim())) {
			return "Primo Pick Sign";
		}

		if (this.descriptionType != null) {
			return this.descriptionType.getDescription();
		}

		return "Unknown";
	}
}
