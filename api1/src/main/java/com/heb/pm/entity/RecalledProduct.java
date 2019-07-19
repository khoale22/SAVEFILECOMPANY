package com.heb.pm.entity;

/**
 * Holds the parameters that hold recalled product data.
 *
 * @author m594201
 * @since 2.13.0
 */
public class RecalledProduct {

	private String qaNumber;

	private String issueDate;

	private String cutOffDate;

	private String classification;

	private String posLock;

	private String details;

	/**
	 * Gets details.  Details explaining the reasoning for the recall.
	 *
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * Sets details.  Details explaining the reasoning for the recall.
	 *
	 * @param details the details
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * Gets qa number.  The recalled number given to the recall.
	 *
	 * @return the qa number
	 */
	public String getQaNumber() {
		return qaNumber;
	}

	/**
	 * Sets qa number.  The recalled number given to the recall.
	 *
	 * @param qaNumber the qa number
	 */
	public void setQaNumber(String qaNumber) {
		this.qaNumber = qaNumber;
	}

	/**
	 * Gets issue date.  The date the recalled was issued.
	 *
	 * @return the issue date
	 */
	public String getIssueDate() {
		return issueDate;
	}

	/**
	 * Sets issue date. The date the recalled was issued.
	 *
	 * @param issueDate the issue date
	 */
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * Gets cut off date.  Date the product needs to be recalled by.
	 *
	 * @return the cut off date
	 */
	public String getCutOffDate() {
		return cutOffDate;
	}

	/**
	 * Sets cut off date.  Date the product needs to be recalled by.
	 *
	 * @param cutOffDate the cut off date
	 */
	public void setCutOffDate(String cutOffDate) {
		this.cutOffDate = cutOffDate;
	}

	/**
	 * Gets classification.  The type of recalled product.
	 *
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * Sets classification. The type of recalled product.
	 *
	 * @param classification the classification
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * Gets pos lock.  Locked at POS flag.
	 *
	 * @return the pos lock
	 */
	public String getPosLock() {
		return posLock;
	}

	/**
	 * Sets pos lock. Locked at POS flag.
	 *
	 * @param posLock the pos lock
	 */
	public void setPosLock(String posLock) {
		this.posLock = posLock;
	}
}
