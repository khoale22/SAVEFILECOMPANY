package com.heb.util.file;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utility functions to parse CSV files.
 *
 * @author d116773
 * @since 2.15.0
 */
public class CsvParser {

	private static final char CSV_DELIMITER = ',';
	private static final char GROUP_CHARACTER = '"';


	/**
	 * Parses a String in a CSV format into an list of Strings. This is base on a function that can
	 * be found here: https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	 *
	 * @param csvLine A String in a CSV format.
	 * @return A list of Strings parsed from the line. Empty or null strings will return an empty list.
	 */
	public List<String> parseLine(String csvLine) {

		List<String> result = new ArrayList<>();

		if (csvLine == null || csvLine.isEmpty()) {
			return result;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;

		char[] chars = csvLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;

				if (ch == CsvParser.GROUP_CHARACTER) {
					inQuotes = false;
				} else {
					curVal.append(ch);
				}
			} else {
				switch (ch) {
					case CsvParser.GROUP_CHARACTER:
						inQuotes = true;

						//double quotes in column will hit this!
						if (startCollectChar) {
							curVal.append(CsvParser.GROUP_CHARACTER);
						}
						break;
					case CsvParser.CSV_DELIMITER:
						result.add(curVal.toString());

						curVal = new StringBuffer();
						startCollectChar = false;
						break;
					case '\r':
						continue;
					case '\n':
						break;
					default:
						curVal.append(ch);
				}
			}
		}

		if (inQuotes) {
			throw new IllegalArgumentException(String.format("'%s' has an unterminated quote", csvLine));
		}

		result.add(curVal.toString());

		return result;
	}
}
