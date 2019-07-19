package com.heb.util.controller;

/**
 * Converts a String to a List of Longs.
 *
 * @author s573181
 * @since 2.0.0
 */
public class LongListFromStringFormatter extends ListFromStringFormatter<Long> {

    /**
     * Creates a new LongListFromStringFormatter.
     */
    public LongListFromStringFormatter() {
		super();
    }

	/**
	 * Creates a Long from a String.
	 *
	 * @param parsedValue The String to convert to a Long.
	 * @return ParsedValue converted to a Long.
	 */
	@Override
	public Long convert(String parsedValue) {
		return Long.valueOf(parsedValue);
	}
}
