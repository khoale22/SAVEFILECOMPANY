/*
 *
 *  DiscontinueParametersAudit
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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Discontinue parameters audit.
 *
 * @author s573181
 * @since 2.0.3
 */
@Entity
@Table(name = "prod_del_parm_aud")
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class DiscontinueParametersAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TIMESTAMP = "key.timestamp";

	/**
	 * Returns the default sort for the class (by timestamp ascending).
	 *
	 * @return The default sort for this class.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, DiscontinueParametersAudit.TIMESTAMP)
		);
	}

	@EmbeddedId
	DiscontinueParametersAuditKey key = new DiscontinueParametersAuditKey();


	@Column(name = "parm_nm")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  	
	private String parameterName;

	@Embedded
	private DiscontinueParametersCommonAudit audit = new DiscontinueParametersCommonAudit();

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public DiscontinueParametersAuditKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key They key for this object.
	 */
	public void setKey(DiscontinueParametersAuditKey key) {
		this.key = key;
	}

	/**
	 * Returns the audit that is common between default and exception parameters as an object. This should
	 * generally not be used and rather the getters and setters of the audit themselves, but this is available
	 * where needed.
	 *
	 * @return The audit that is common between default and exception parameters audit as an object.
	 */
	public DiscontinueParametersCommonAudit getAudit() {
		return audit;
	}

	/**
	 * Returns the parameterName  - a rule name that corresponds to the sys_gend_id (id). For example an Id of 1
	 * would correspond to Lst_scn_dt (the last scan date with the stores).
	 *
	 * @return parameterName  - a rule name that corresponds to the sys_gend_id (id).
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * Sets parameter name.
	 *
	 * @param parameterName the parameter name
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * Getters and Setters fir the DiscontinueParametersCommonAudit information .
	 *
	 */

	/**
	 * Returns the value for the discontinue job to use for this rule. For example, if this record represents
	 * a last scan date for a vendor, it is the number of days since the last scan for a UPC attached to an item before
	 * the item can be discontinued.
	 *
	 * @return The value for the discontinue job to use for this rule.
	 */
	public String getParameterValue() {
		return this.audit.parameterValue;
	}

	/**
	 * Sets value for the discontinue job to use for this rule.
	 *
	 * @param parameterValue The value for the discontinue job to use for this rule.
	 */
	public void setParameterValue(String parameterValue) {
		this.audit.parameterValue = parameterValue;
	}

	/**
	 * Returns the priority of this rule.
	 *
	 * @return The priority of this rule.
	 */
	public int getPriority() {
		return this.audit.priority;
	}

	/**
	 * Sets the priority of this rule.
	 *
	 * @param priority The priority of this rule.
	 */
	public void setPriority(int priority) {
		this.audit.priority = priority;
	}

	/**
	 * Returns whether or not this rule is active.
	 *
	 * @return True if this rule is active and false otherwise.
	 */
	public boolean isActive() {
		return this.audit.active;
	}

	/**
	 * Sets whether or not this rule is active.
	 *
	 * @param active True if this rule is active and false otherwise.
	 */
	public void setActive(boolean active) {
		this.audit.active = active;
	}

	/**
	 * Returns the ID for this object. This combined with the sequence number defines what type of
	 * rule this exception is.
	 *
	 * @return The ID.
	 */
	public int getId() {
		return this.key.getId();
	}

	/**
	 * Sets the ID for this object.
	 *
	 * @param id The ID for this object.
	 */
	public void setId(int id) {
		this.key.setId(id);
	}

	/**
	 * Returns the sequence number for this object. This combined with the ID defines what type of
	 * rule this exception is.
	 *
	 * @return The sequence number for this object
	 */
	public int getSequenceNumber() {
		return this.key.getSequenceNumber();
	}

	/**
	 * Sets the sequence number for this object
	 *
	 * @param sequenceNumber The sequence number for this object.
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.key.setSequenceNumber(sequenceNumber);
	}

	/**
	 * Returns the action made by the user that caused the audit. For example, if a person added a new record, the Action
	 * would be ADD. Other values can be DEL or MOD.
	 *
	 * @return the action made by the user that caused the audit.
	 */
	public String getAction(){
		return this.audit.action;
	}

	/**
	 * Sets the action made by the user that caused the audit. For example, if a person added a new record, the Action
	 * would be ADD. Other values can be DEL or MOD.
	 *
	 * @param action the action made by the user that caused the audit.
	 */
	public void setAction(String action){
		this.audit.action = action;
	}

	/**
	 * Gets the userId of the person/entity that made the change in DiscontinueParameters.
	 *
	 * @return the userId of the person/entity that made the change in DiscontinueParameters.
	 */
	public String getUserId(){
		return this.audit.userId;
	}

	/**
	 * Sets the userId of the person/entity that made the change in DiscontinueParameters.
	 *
	 * @param userId the userId of the person/entity that made the change in DiscontinueParameters.
	 */
	public void setUserId(String userId){
		this.audit.userId = userId;
	}

	/**
	 * Returns the timestamp of when the audit was created.
	 *
	 * @return The timestamp of when the audit was created.
	 */
	public LocalDateTime getTimestamp() {
		return this.key.getTimestamp();
	}

	/**
	 * Sets the timestamp of when the audit was created.
	 * @param timestamp The timestamp of when the audit was created.
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.key.setTimestamp(timestamp);
	}

	/**
	 * Compares another object to this one. If that object is an DiscontinueExceptionParameters, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DiscontinueParametersAudit)) return false;

		DiscontinueParametersAudit that = (DiscontinueParametersAudit) o;

		if (this.key != null ? !this.key.equals(that.key) : that.key != null) return false;

		return true;

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they (probably) have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.key == null ? 0 : this.key.hashCode();
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueParametersAudit{" +
				"key=" + key +
				", parameterName='" + parameterName + '\'' +
				", audit=" + audit +
				'}';
	}
}
