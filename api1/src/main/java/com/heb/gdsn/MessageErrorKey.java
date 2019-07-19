/*
 * MessageErrorKey
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.gdsn;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Key for the message_error table.
 *
 * @author d116773
 * @since 2.3.0
 */
@Embeddable
public class MessageErrorKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "message_id")
	private String messageId;

	@Column(name = "document_id")
	private String documentId;

	@Column(name = "sequence_number")
	private Integer sequenceNumber;

	/**
	 * Returns the message ID of this record.
	 *
	 * @return The message ID of this record.
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Sets the message ID of this record.
	 *
	 * @param messageId The message ID of this record.
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * Returns the document ID of this record.
	 *
	 * @return The document ID of this record.
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * Sets the document ID of this record.
	 *
	 * @param documentId The document ID of this record.
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Compares this object with another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MessageErrorKey)) return false;

		MessageErrorKey that = (MessageErrorKey) o;

		if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) return false;
		if (documentId != null ? !documentId.equals(that.documentId) : that.documentId != null) return false;
		return !(sequenceNumber != null ? !sequenceNumber.equals(that.sequenceNumber) : that.sequenceNumber != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = messageId != null ? messageId.hashCode() : 0;
		result = 31 * result + (documentId != null ? documentId.hashCode() : 0);
		result = 31 * result + (sequenceNumber != null ? sequenceNumber.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "MessageErrorKey{" +
				"messageId='" + messageId + '\'' +
				", documentId='" + documentId + '\'' +
				", sequenceNumber=" + sequenceNumber +
				'}';
	}
}
