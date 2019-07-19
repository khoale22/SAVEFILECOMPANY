/*
 * Message
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.gdsn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a record in the message table. This could be a message we sent to or received from 1WorldSync.
 *
 * @author d116773
 * @since 2.3.0
 */
@Entity
@Table(name = "message")
public class Message {

	@EmbeddedId
	private MessageKey key;

	@JsonIgnoreProperties("message")
	@OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
	private List<MessageError> messageErrors;

	/**
	 * Returns the key of the record.
	 *
	 * @return The key of the record.
	 */
	public MessageKey getKey() {
		return key;
	}

	/**
	 * Sets the key of the record.
	 *
	 * @param key The key of the record.
	 */
	public void setKey(MessageKey key) {
		this.key = key;
	}

	/**
	 * Returns the list of errors for this message.
	 *
	 * @return The list of errors for this message.
	 */
	public List<MessageError> getMessageErrors() {
		return messageErrors;
	}

	/**
	 * Sets the list of errors for this message.
	 *
	 * @param messageErrors The list of errors for this message.
	 */
	public void setMessageErrors(List<MessageError> messageErrors) {
		this.messageErrors = messageErrors;
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
		if (!(o instanceof Message)) return false;

		Message message = (Message) o;

		return !(key != null ? !key.equals(message.key) : message.key != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "Message{" +
				"key=" + key +
				'}';
	}
}
