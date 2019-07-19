package com.heb.util.audit;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Class to hold audit attributes.
 *
 * @author m314029
 * @since 2.7.0
 */
public class AuditRecord {

	private String action;
	private String changedBy;
	private String changedFrom;
	private String changedTo;
	private LocalDateTime changedOn;
	private LocalDateTime createdDate;
	private String attributeName;
	private String attributeValue;
	private String extendableAttValue;
	private Long attributeId;
	private String fulfillmentProgram;
	private LocalDate startDate;
	private LocalDate endDate;
	private String upcText;
	private String productIdText;
	/**
	 * Default constructor.
	 */
	public AuditRecord(){
	}

	/**
	 * Instantiates a new audit record given a changed on time, changed by user, action code, and attribute name.
	 *
	 * @param changedOn     the changed on
	 * @param changedBy     the changed by
	 * @param action        the action
	 * @param attributeName the attribute name
	 */
	public AuditRecord(LocalDateTime changedOn, String changedBy, String action, String attributeName) {
		this.setChangedOn(changedOn);
		this.setChangedBy(changedBy);
		this.setAction(action);
		this.setAttributeName(attributeName);
	}

	/**
	 * The enum Action codes. This represents possible action codes for an audit record.
	 */
	public enum ActionCodes {
		/**
		 * Add action codes.
		 */
		ADD("Add"),
		/**
		 * Purge action codes.
		 */
		PURGE("Delete"),
		/**
		 * Updat action codes.
		 */
		UPDAT("Update"),
		/**
		 * UPDT action codes.
		 */
		UPDT("Update"),
		/**
		 * Add action codes.
		 */
		DEL("Delete");

		private String displayName;

		ActionCodes(String displayName) {
			this.displayName = displayName;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getDisplayName() {
			return this.displayName;
		}
	}

	/**
	 * Gets action.
	 *
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets action.
	 *
	 * @param action the action
	 */
	public void setAction(String action) {
		try {
			this.action = ActionCodes.valueOf(action.trim()).getDisplayName();
		} catch (IllegalArgumentException e){
			this.action = action;
		}
	}

	/**
	 * Gets changed by.
	 *
	 * @return the changed by
	 */
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Sets changed by.
	 *
	 * @param changedBy the changed by
	 */
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Gets changed from.
	 *
	 * @return the changed from
	 */
	public String getChangedFrom() {
		return changedFrom;
	}

	/**
	 * Sets changed from.
	 *
	 * @param changedFrom the changed from
	 */
	public void setChangedFrom(String changedFrom) {
		this.changedFrom = changedFrom;
	}

	/**
	 * Gets changed to.
	 *
	 * @return the changed to
	 */
	public String getChangedTo() {
		return changedTo;
	}

	/**
	 * Sets changed to.
	 *
	 * @param changedTo the changed to
	 */
	public void setChangedTo(String changedTo) {
		this.changedTo = changedTo;
	}

	/**
	 * Gets changed on.
	 *
	 * @return the changed on
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Sets changed on.
	 *
	 * @param changedOn the changed on
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
	}

	/**
	 * Gets changed on.
	 *
	 * @return the changed on
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets changed on.
	 *
	 * @param createdDate the changed on
	 */
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets attribute name.
	 *
	 * @return the attribute name
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Sets attribute name.
	 *
	 * @param attributeName the attribute name
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	/**
	 * Gets attribute value.
	 *
	 * @return the attribute value
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * Sets attribute value.
	 *
	 * @param attributeValue the attribute value
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	/**
	 * Returns the ID of the attribute this key is for.
	 *
	 * @return The ID of the attribute this key is for.
	 */
	public Long getAttributeId() {
		return attributeId;
	}

	/**
	 * Sets the ID of the attribute this key is for.
	 *
	 * @param attributeId The ID of the attribute this key is for.
	 */
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}
	/**
	 * Gets attribute name.
	 *
	 * @return the attribute name
	 */
	public String getFulfillmentProgram() {
		return fulfillmentProgram;
	}

	/**
	 * Sets attribute name.
	 *
	 */
	public void setFulfillmentProgram(String fulfillmentProgram) {
		this.fulfillmentProgram = fulfillmentProgram;
	}
	/**
	 * Gets changed on.
	 *
	 * @return the changed on
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Sets changed on.
	 *
	 * @param startDate the changed on
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	/**
	 * Gets changed on.
	 *
	 * @return the changed on
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Sets changed on.
	 *
	 * @param endDate the changed on
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns the upc.
	 *
	 * @return upc.
	 */
	public String getUpcText() {
		return upcText;
	}

	/**
	 * Sets the ID of the attribute this key is for.
	 *
	 * @param upcText The product scan code.
	 */
	public void setUpcText(String upcText) {
		this.upcText = upcText;
	}
	/**
	 * Returns the product id.
	 *
	 * @return product id.
	 */
	public String getProductIdText() {
		return productIdText;
	}

	/**
	 * Sets the product id.
	 *
	 * @param productIdText The product id.
	 */
	public void setProductIdText(String productIdText) {
		this.productIdText = productIdText;
	}

	/**
	 * Returns the extendable attribute value.
	 *
	 * @return extendableAttValue.
	 */
	public String getExtendableAttValue() {
		return this.extendableAttValue;
	}

	/**
	 * Sets the extendableAttValue.
	 *
	 * @param extendableAttValue The extendable attribute value.
	 */
	public void setExtendableAttValue(String extendableAttValue) {
		this.extendableAttValue = extendableAttValue;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "AuditRecord{" +
				"changedBy='" + changedBy + '\'' +
				", changedFrom='" + changedFrom + '\'' +
				", changedTo='" + changedTo + '\'' +
				", changedOn='" + changedOn + '\'' +
				", attributeName='" + attributeName + '\'' +
				'}';
	}
}
