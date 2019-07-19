/*
 *
 *  DiscontinueParametersCommonAudit
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * Holds attributes that are common between product discontinue default rules and product discontinue exception
 * rules.
 *
 * @author s573181
 * @since 2.0.3
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class DiscontinueParametersCommonAudit {

	@Column(name = "parm_val_txt")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  	
	protected String parameterValue;

	@Column(name = "prty_nbr")
	protected int priority;

	@Column(name = "actv_sw")  	
	protected boolean active;

	@Column(name = "act_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  	
	protected String action;

	@Column(name = "cre8_id")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  	
	protected String userId;


	/**
	 * Gets the userId of the person/entity that made the change in DiscontinueParameters.
	 *
	 * @return the userId of the person/entity that made the change in DiscontinueParameters.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the userId of the person/entity that made the change in DiscontinueParameters.
	 *
	 * @param userId the userId of the person/entity that made the change in DiscontinueParameters.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the audit value for the audit discontinue job to use for this rule. For example, if this record represents
	 * a last scan date for a vendor, it is the number of days since the last scan for a UPC attached to an item before
	 * the item can be discontinued.
	 *
	 * @return The value for the audit discontinue job to use for this rule.
	 */
	public String getParameterValue() {
		return parameterValue;
	}

	/**
	 * Sets audit value for the audit discontinue job to use for this rule.
	 *
	 * @param parameterValue The value for the discontinue job to use for this rule.
	 */
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	/**
	 * Returns the priority of this rule.
	 *
	 * @return The priority of this rule.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Sets the priority of this rule.
	 *
	 * @param priority The priority of this rule.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Returns whether or not this rule is active.
	 *
	 * @return True if this rule is active and false otherwise.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets whether or not this rule is active.
	 *
	 * @param active True if this rule is active and false otherwise.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Returns the action made by the user that caused the audit. For example, if a person added a new record, the Action
	 * would be ADD. Other values can be DEL or MOD.
	 *
	 * @return the action made by the user that caused the audit.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action made by the user that caused the audit. For example, if a person added a new record, the Action
	 * would be ADD. Other values can be DEL or MOD.
	 *
	 * @param action the action made by the user that caused the audit.
	 */
	public void setAction(String action) {
		this.action = action;
	}


	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueParametersCommonAudit{" +
				"parameterValue='" + parameterValue + '\'' +
				", priority=" + priority +
				", active=" + active +
				", action='" + action + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}
}
