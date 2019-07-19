/*
 *  ScoringOrganization
 *
 *  Copyright (c) 2017 HEB
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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents the Scoring Organization.
 *
 * @author vn70529
 * @since 2.12
 */

@Entity
@Table(name = "scoring_org")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ScoringOrganization {

	@Id
	@Column(name = "SCORING_ORG_ID")
	private Integer scoringOrganizationId;

	@Column(name = "SCORING_ORG_NM")
	@Type(type="fixedLengthChar")
	private String scoringOrganizationName;

	@Column(name = "SCORING_ORG_DES")
	private String scoringOrganizationDescription;

	/**
	 * Get the id of scoring organization.
	 *
	 * @return the id of scoring organization
	 */
	public Integer getScoringOrganizationId() {
		return scoringOrganizationId;
	}

	/**
	 * Set the id of scoring organization.
	 *
	 * @param scoringOrganizationId the id needs to set.
	 */
	public void setScoringOrganizationId(Integer scoringOrganizationId) {
		this.scoringOrganizationId = scoringOrganizationId;
	}

	/**
	 * Get the name of of scoring organization.
	 *
	 * @return the name of of scoring organization.
	 */
	public String getScoringOrganizationName() {
		return scoringOrganizationName;
	}

	/**
	 * Set the name of scoring organization.
	 *
	 * @param scoringOrganizationName the name needs to set.
	 */
	public void setScoringOrganizationName(String scoringOrganizationName) {
		this.scoringOrganizationName = scoringOrganizationName;
	}

	/**
	 * Get the description of of scoring organization.
	 *
	 * @return the description of scoring organization.
	 */
	public String getScoringOrganizationDescription() {
		return scoringOrganizationDescription;
	}

	/**
	 * Set the description of scoring organization.
	 *
	 * @param scoringOrganizationDescription the description needs to set.
	 */
	public void setScoringOrganizationDescription(String scoringOrganizationDescription) {
		this.scoringOrganizationDescription = scoringOrganizationDescription;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ScoringOrganization{" +
				"scoringOrganizationId=" + scoringOrganizationId +
				", scoringOrganizationName='" + scoringOrganizationName + '\'' +
				", scoringOrganizationDescription=" + scoringOrganizationDescription +
				'}';
	}

}
