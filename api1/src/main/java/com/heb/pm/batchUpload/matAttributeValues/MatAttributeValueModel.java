package com.heb.pm.batchUpload.matAttributeValues;

import java.util.Date;
/**
 * Model representation of a mat attribute value row in excel sheet.
 *
 * @author s573181
 * @since 2.29.0
 */
public class MatAttributeValueModel {

	private Long keyId;
	private String keyType;
	private Long attributeId;
	private String attributeValue;
	private Date attributeDateValue;
	private String errorMessage;

	/**
	 * Returns the key id.
	 *
	 * @return the key id.
	 */
	public Long getKeyId() {
		return keyId;
	}

	/**
	 * Sets the key id.
	 *
	 * @param keyId the key id.
	 * @return the key id.
	 */
	public MatAttributeValueModel setKeyId(Long keyId) {
		this.keyId = keyId;
		return this;
	}

	/**
	 * Returns the key type.
	 *
	 * @return the key type.
	 */
	public String getKeyType() {
		return keyType;
	}

	/**
	 * Sets the key type.
	 *
	 * @param keyType the key type.
	 * @return the key type.
	 */
	public MatAttributeValueModel setKeyType(String keyType) {
		this.keyType = keyType;
		return this;
	}

	/**
	 * Returns the attribute id.
	 *
	 * @return the attribute id.
	 */
	public Long getAttributeId() {
		return attributeId;
	}

	/**
	 * Sets  the attribute id.
	 *
	 * @param attributeId the attribute id.
	 * @return the attribute id.
	 */
	public MatAttributeValueModel setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
		return this;
	}

	/**
	 * Returns the attribute value.
	 *
	 * @return the attribute value.
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * Sets the attribute value.
	 *
	 * @param attributeValue the attribute value.
	 * @return the attribute value.
	 */
	public MatAttributeValueModel setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
		return this;
	}

	/**
	 * Returns the attribute date value.
	 *
	 * @return the attribute date value.
	 */
	public Date getAttributeDateValue() {
		return attributeDateValue;
	}

	/**
	 * Sets the attribute date value.
	 *
	 * @param attributeDateValue the attribute date value.
	 * @return the attribute date value.
	 */
	public MatAttributeValueModel setAttributeDateValue(Date attributeDateValue) {
		this.attributeDateValue = attributeDateValue;
		return this;
	}

	/**
	 * Returns the error message.
	 *
	 * @return the error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 *
	 * @param errorMessage the error message.
	 * @return the error message.
	 */
	public MatAttributeValueModel setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}
}
