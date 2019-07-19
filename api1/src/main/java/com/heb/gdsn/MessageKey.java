/*
 * MessageKey
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
 * Represents a key on the message table.
 *
 * @author d116773
 * @since 2.3.0
 */
@Embeddable
public class MessageKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "message_id")
	private String messageId;

	@Column(name = "document_id")
	private String documentId;

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

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MessageKey)) return false;

		MessageKey that = (MessageKey) o;

		if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) return false;
		return !(documentId != null ? !documentId.equals(that.documentId) : that.documentId != null);

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
		return result;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "MessageKey{" +
				"messageId='" + messageId + '\'' +
				", documentId='" + documentId + '\'' +
				'}';
	}
}
