
/*
 * com.heb.util.list.ListFormatter
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.util.list;

import java.util.List;

/**
 * Class holding functions related to formatting lists for output.
 *
 * @author d116773
 */
public final class ListFormatter {

	private static final char DEFAULT_DELIMITER_CHAR = ',';

	/**
	 * Private constructor so you cannot instantiate the class.
	 */
	private ListFormatter(){};

	/**
	 * Returns a String with the list formatted as a series of elements delimited by
	 * the delimiter. For example, if delimiter is a pipe and list list is the first five
	 * integers, the method will return 1|2|3|4|5.
	 *
	 * @param list The list to format.
	 * @param delimiter The character to use to delimit the list.
	 * @return The formatted String.
	 */
	public static String formatAsString(List<? extends Object> list, char delimiter) {
		if (list == null || list.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		list.forEach(item->sb.append(item.toString()).append(delimiter));
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	/**
	 * Returns a String with the list formatted as a series of elements delimited by
	 * a comma (the default delimiter). For example, if the list is the first five
	 * integers, the method will return 1,2,3,4,5.
	 *
	 * @param list The list to format.
	 * @return The formatted String.
	 */
	public static String formatAsString(List<? extends Object> list) {

		return ListFormatter.formatAsString(list, ListFormatter.DEFAULT_DELIMITER_CHAR);
	}
}
