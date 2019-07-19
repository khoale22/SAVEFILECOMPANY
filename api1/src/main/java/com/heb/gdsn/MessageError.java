/*
 * MessageError
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.gdsn;

import javax.persistence.*;

/**
 * Represents an error coming off a GDSN message.
 *
 * @author d116773
 * @since 2.3.0
 */
@Entity
@Table(name = "message_error")
public class MessageError {

	@EmbeddedId
	private MessageErrorKey key;

	@Column(name = "error_code")
	private String errorCode;

	@Column(name = "error_message")
	private String errorMessage;

	// Don't know why, but the order of these is significant and it doesn't work when message_id is first.
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "document_id", insertable = false, updatable = false),
			@JoinColumn(name = "message_id", insertable = false, updatable = false)

	})
	private Message message;

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public MessageErrorKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key The key for this object.
	 */
	public void setKey(MessageErrorKey key) {
		this.key = key;
	}

	/**
	 * Returns the code associated with this error.
	 *
	 * @return The code associated with this error.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Sets the code associated with this error.
	 *
	 * @param errorCode The code associated with this error.
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Returns the error message.
	 *
	 * @return The error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 *
	 * @param errorMessage The error message.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Returns the object that tracks the GDSN message this error is tied to.
	 *
	 * @return The object that tracks the GDSN message this error is tied to.
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * Sets the object that tracks the GDSN message this error is tied to.
	 *
	 * @param message The object that tracks the GDSN message this error is tied to.
	 */
	public void setMessage(Message message) {
		this.message = message;
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
		if (!(o instanceof MessageError)) return false;

		MessageError that = (MessageError) o;

		if (key != null ? !key.equals(that.key) : that.key != null) return false;
		if (errorCode != null ? !errorCode.equals(that.errorCode) : that.errorCode != null) return false;
		return !(errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (errorCode != null ? errorCode.hashCode() : 0);
		result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "MessageError{" +
				"key=" + key +
				", errorCode='" + errorCode + '\'' +
				", errorMessage='" + errorMessage + '\'' +
				'}';
	}
}
