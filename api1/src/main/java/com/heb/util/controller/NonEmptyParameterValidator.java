/*
 * NonEmptyParameterValidator
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;

/**
 * Service to validate parameters that should be not null and not empty.
 *
 * @author d116773
 * @since 2.0.2
 */
@Service
public class NonEmptyParameterValidator implements MessageSourceAware {

	private MessageSource messageSource;

	/**
	 * Validates Strings. If the string is null or empty, it will throw an IllegalArgumentException.
	 *
	 * @param toValidate The string to check.
	 * @param defaultMessage The default message to send if the string is invalid and a message cannot be found.
	 * @param messageKey The key to use to look up an error message in this objec't message source.
	 * @param locale The locale to use to look up the message.
	 */
	public void validate(String toValidate, String defaultMessage, String messageKey, Locale locale) {
		if (toValidate == null || toValidate.isEmpty()) {
			this.throwException(defaultMessage, messageKey, locale);
		}
	}

	/**
	 * Validates Numbers. If the object is null, it will throw an IllegalArgumentException.
	 *
	 * @param toValidate The Number to check.
	 * @param defaultMessage The default message to send if the string is invalid and a message cannot be found.
	 * @param messageKey The key to use to look up an error message in this objec't message source.
	 * @param locale The locale to use to look up the message.
	 */
	public void validate(Number toValidate, String defaultMessage, String messageKey, Locale locale) {
		if (toValidate == null) {
			this.throwException(defaultMessage, messageKey, locale);
		}
	}

	/**
	 * Validates collections. If the collection is null or empty, it will throw an IllegalArumentException.
	 *
	 * @param toValidate The collection to check.
	 * @param defaultMessage The default message to send if the string is invalid and a message cannot be found.
	 * @param messageKey The key to use to look up an error message in this objec't message source.
	 * @param locale The locale to use to look up the message.
	 */
	public void validate(Collection<?> toValidate, String defaultMessage, String messageKey,
						 Locale locale) {
		if (toValidate == null || toValidate.isEmpty()) {
			this.throwException(defaultMessage, messageKey, locale);
		}
	}

	public void validate(Object toValidate, String defaultMessage, String messageKey,
						 Locale locale) {
		if (toValidate == null) {
			this.throwException(defaultMessage, messageKey, locale);
		}
	}
	/**
	 * Sets the object's MessageSource.
	 *
	 * @param messageSource The MessageSource for this object too use.
	 */
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Throws an IllegalArgumentException.
	 *
	 * @param defaultMessage The default message to send if the string is invalid and a message cannot be found.
	 * @param messageKey The key to use to look up an error message in this objec't message source.
	 * @param locale The locale to use to look up the message.
	 */
	private void throwException(String defaultMessage, String messageKey, Locale locale) {

		String message = this.messageSource == null ? defaultMessage :
				this.messageSource.getMessage(messageKey, null, defaultMessage, locale);
		throw new IllegalArgumentException(message);
	}
}
