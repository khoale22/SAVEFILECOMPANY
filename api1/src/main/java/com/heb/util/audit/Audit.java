/*
 *
 *  Audit
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.util.audit;

import java.time.LocalDateTime;

/**
 * This generic audit interface is used to set or retrieve each value in an audit to track what and who is updating the
 * products and to be shown in an audit table inside of the application.
 *
 * @author l730832
 * @since 2.6.0
 */
public interface Audit extends Comparable<Audit> {

	/**
	 * This constant is used to display a code table display name when the code table record is not found for an id.
	 */
	String DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND = "NOT FOUND[%s]";

	/**
	 * Returns an action code. An action code describes what the audit is doing. I.E. UPDAT, PURGE, ADD
	 *
	 * @return the action code.
	 */
	String getAction();

	/**
	 * Sets the action code received from the database.
	 *
	 * @param action the action
	 */
	void setAction(String action);

	/**
	 * Returns changed by. The changed by shows who was doing the action that is being audited. This is the uid(login) that a
	 * user has.
	 *
	 * @return the changed by
	 */
	String getChangedBy();

	/**
	 * Sets the changed by uid received from the database.
	 *
	 * @param changedBy the changed by
	 */
	void setChangedBy(String changedBy);

	/**
	 * Gets the changed on time. This is when the modification was done.
	 *
	 * @return The time the modification was done.
	 */
	LocalDateTime getChangedOn();
	/**
	 * Sets the changed on time.
	 *
	 * @param changedOn The time the modification was done.
	 */
	void setChangedOn(LocalDateTime changedOn);
	/**
	 * Auditable framework needs to sort records before analyzing for changes
	 * @param o - other Audit object to compare to
	 * @return - -1,0,1 based on comparison
	 */
	default int compareTo(Audit o) {
		return this.getChangedOn().compareTo(o.getChangedOn());
	}
}
