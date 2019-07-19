package com.heb.util.controller;

/**
 * Converts a string to a list of strings.
 *
 * @author d116773
 * @since 2.13.0
 */
public class StringListFromStringFormatter extends ListFromStringFormatter<String> {

	/**
	 * Just returns the original string.
	 *
	 * @param parsedValue The String to convert to T.
	 * @return The original string.
	 */
	@Override
	public String convert(String parsedValue) {
		return parsedValue;
	}
}
