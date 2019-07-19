/*
 *
 *  DiscontinueExceptionParametersAudit
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

/**
 * Represents the exception rules audit for product discontinue.
 *
 * @Author s573181
 * @Since  2.0.3
 */

import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Discontinue exception parameters audit.
 */
@Entity
@Table(name = "prod_del_excp_aud")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
@TypeDef(name = "fixedChar", typeClass = com.heb.pm.util.oracle.OracleCharType.class)
})
// dB2Oracle changes vn00907
public class DiscontinueExceptionParametersAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TIMESTAMP = "key.parametersAuditKey.timestamp";
	/**
	 * Returns the default sort for the class (by timestamp ascending).
	 *
	 * @return The default sort for this class.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, DiscontinueExceptionParametersAudit.TIMESTAMP)
		);
	}


	@EmbeddedId
	private DiscontinueExceptionParametersAuditKey key = new DiscontinueExceptionParametersAuditKey();

	@Embedded
	private DiscontinueParametersCommonAudit audit = new DiscontinueParametersCommonAudit();

	@Column(name = "excp_attr_val_txt")
	//db2o changes  vn00907
	@Type(type="fixedChar")  	
	private String exceptionTypeId;

	@Column(name = "excp_attr_nm")
	//db2o changes  vn00907
	@Type(type="fixedChar")  	
	private String exceptionType;

	@Column(name = "nev_del_sw") 	
	private boolean neverDelete;

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public DiscontinueExceptionParametersAuditKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key They key for this object.
	 */
	public void setKey(DiscontinueExceptionParametersAuditKey key) {
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
	 * Returns the exception number for this object. An exception rule is grouped together by these numbers. For
	 * example, if there is a rule set up for a particular vendor, all the components of the rule share this ID.
	 * The ID and sequence number make the components of the rule unique.
	 *
	 * @return The exception number for this object.
	 */
	public int getExceptionNumber() {
		return this.key.getExceptionNumber();
	}

	/**
	 * Sets the exception number for this object.
	 *
	 * @param exceptionNumber The exception number for this object.
	 */
	public void setExceptionNumber(int exceptionNumber) {
		this.key.setExceptionNumber(exceptionNumber);
	}

	/**
	 * Returns the identifier for who this rule applies to. For example, if this is a vendor rule, it is the vendor
	 * number.
	 *
	 * @return The identifier for who this rule applies to.
	 */
	public String getExceptionTypeId() {
		return exceptionTypeId;
	}

	/**
	 * Sets the identifier for who this rule applies to. For example, if this is a vendor rule, it is the vendor
	 * number.
	 *
	 * @param exceptionTypeId The identifier for who this rule applies to.
	 */
	public void setExceptionTypeId(String exceptionTypeId) {
		this.exceptionTypeId = exceptionTypeId;
	}

	/**
	 * Returns the type of exception this is. For example, if this rule is about a vendor, it returns 'Vendor'.
	 * These are defined in the ProductDiscontinueExceptionType class.
	 *
	 * @return The exception type.
	 */
	public String getExceptionType() {
		return exceptionType;
	}

	/**
	 * Sets the exception type.
	 *
	 * @param exceptionType The exception type.
	 */
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	/**
	 * Returns whether or not items matching this type and value should ever be deleted.
	 *
	 * @return True if items matching this type and value should never be deleted and false otherwise.
	 */
	public boolean isNeverDelete() {
		return neverDelete;
	}


	/**
	 * Sets whether or not items matching this type and value should ever be deleted.
	 *
	 * @param neverDelete True if items matching this type and value should never be deleted and false otherwise.
	 */
	public void setNeverDelete(boolean neverDelete) {
		this.neverDelete = neverDelete;
	}

	/**
	 * Getters and Setters fir the DiscontinueParametersCommonAudit information
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
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DiscontinueExceptionParametersAudit)) return false;

		DiscontinueExceptionParametersAudit audit = (DiscontinueExceptionParametersAudit) o;

		return !(key != null ? !key.equals(audit.key) : audit.key != null);
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueExceptionParametersAudit{" +
				"key=" + key +
				", audit=" + audit +
				", exceptionTypeId='" + exceptionTypeId + '\'' +
				", exceptionType='" + exceptionType + '\'' +
				", neverDelete=" + neverDelete +
				'}';
	}
}
