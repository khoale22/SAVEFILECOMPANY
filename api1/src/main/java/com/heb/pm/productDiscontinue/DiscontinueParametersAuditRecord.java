/*
 *
 *  discontinueParametersAudit
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.productDiscontinue;

import com.heb.util.audit.Audit;

import java.time.LocalDateTime;

/**
 * The type Discontinue parameters audit record.
 *
 * @author s573181
 * @since 2.0.3
 */
public class DiscontinueParametersAuditRecord implements Audit {

	private static final long serialVersionUID = 1L;

	private Boolean previouslyActive;
	private Boolean newActive;
	private Boolean previouslyNeverDiscontinue;
	private Boolean newNeverDiscontinue;
	private String exceptionType;
	private String exceptionDescription;
	private String attributeName;
	private String action;
	private LocalDateTime changedOn;
	private String changedBy;
	private String previousAttributeValue;
	private String newAttributeValue;

	/**
	 * Returns Boolean isPreviouslyActive for Audit object if original object was active.
	 *
	 * @return Boolean isPreviouslyActive for Audit object.
	 */
	public Boolean isPreviouslyActive() {
		return previouslyActive;
	}

	/**
	 * Sets the previouslyActive flag for the Audit.
	 *
	 * @param previouslyActive the previouslyActive flag for the Audit.
	 */
	public void setPreviouslyActive(Boolean previouslyActive) {
		this.previouslyActive = previouslyActive;
	}

	/**
	 * Returns Boolean isNewActive for Audit object if new object was active.
	 *
	 * @return Boolean isNewActive for Audit object.
	 */
	public Boolean isNewActive() {
		return newActive;
	}

	/**
	 * Sets the isNewActive flag for Audit object.
	 *
	 * @param newActive isNewActive flag for Audit object.
	 */
	public void setNewActive(Boolean newActive) {
		this.newActive = newActive;
	}

	/**
	 * Returns Boolean isPreviouslyNeverDiscontinue for Audit object if the original object is never discontinue-able.
	 *
	 * @return Boolean isPreviouslyNeverDiscontinue for Audit object.
	 */
	public Boolean isPreviouslyNeverDiscontinue() {
		return previouslyNeverDiscontinue;
	}

	/**
	 * Sets the OriginalNeverDiscontinue switch for Audit object.
	 *
	 * @param previouslyNeverDiscontinue the OriginalNeverDiscontinue switch for Audit object.
	 */
	public void setPreviouslyNeverDiscontinue(Boolean previouslyNeverDiscontinue) {
		this.previouslyNeverDiscontinue = previouslyNeverDiscontinue;
	}

	/**
	 * Returns Boolean isNewNeverDiscontinue for Audit object if the new object is never discontinue-able.
	 *
	 * @return Boolean isNewNeverDiscontinue for Audit object.
	 */
	public Boolean isNewNeverDiscontinue() {
		return newNeverDiscontinue;
	}

	/**
	 * Sets the newNeverDiscontinue switch for Audit object.
	 *
	 * @param newNeverDiscontinue the newNeverDiscontinue switch for Audit object.
	 */
	public void setNewNeverDiscontinue(Boolean newNeverDiscontinue) {
		this.newNeverDiscontinue = newNeverDiscontinue;
	}

	/**
	 * Returns the exceptionType for Audit object.
	 *
	 * @return exceptionType the exceptionType for Audit object.
	 */
	public String getExceptionType() {
		return exceptionType;
	}

	/**
	 * Sets the exceptionType  for Audit object.
	 *
	 * @param exceptionType the exceptionType switch for Audit object.
	 */
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	/**
	 * Returns the exceptionDescription for Audit object.
	 *
	 * @return exceptionDescription the exceptionType for Audit object.
	 */
	public String getExceptionDescription() {
		return exceptionDescription;
	}

	/**
	 * Sets the exceptionDescription for Audit object.
	 *
	 * @param exceptionDescription the exceptionDescription  for Audit object.
	 */
	public void setExceptionDescription(String exceptionDescription) {
		this.exceptionDescription = exceptionDescription;
	}

	/**
	 * Returns the AttributeName
	 *
	 * @return AttributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Sets the AttributeName
	 *
	 * @param attributeName The AttributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Returns the Action
	 *
	 * @return Action
	 */
	@Override
	public String getAction() {
		return action;
	}

	/**
	 * Sets the Action
	 *
	 * @param action The Action
	 */
	@Override
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Sets the ChangedOn
	 *
	 * @param changedOn The ChangedOn
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
	}

	/**
	 * Returns the ChangedOn
	 *
	 * @return ChangedOn
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Returns the ChangedBy
	 *
	 * @return ChangedBy
	 */
	@Override
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Sets the ChangedBy
	 *
	 * @param changedBy The ChangedBy
	 */
	@Override
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Returns the PreviousAttributeValue
	 *
	 * @return PreviousAttributeValue
	 */
	public String getPreviousAttributeValue() {
		return previousAttributeValue;
	}

	/**
	 * Sets the PreviousAttributeValue
	 *
	 * @param previousAttributeValue The PreviousAttributeValue
	 */
	public void setPreviousAttributeValue(String previousAttributeValue) {
		this.previousAttributeValue = previousAttributeValue;
	}

	/**
	 * Returns the NewAttributeValue
	 *
	 * @return NewAttributeValue
	 */
	public String getNewAttributeValue() {
		return newAttributeValue;
	}

	/**
	 * Sets the NewAttributeValue
	 *
	 * @param newAttributeValue The NewAttributeValue
	 */
	public void setNewAttributeValue(String newAttributeValue) {
		this.newAttributeValue = newAttributeValue;
	}
}
