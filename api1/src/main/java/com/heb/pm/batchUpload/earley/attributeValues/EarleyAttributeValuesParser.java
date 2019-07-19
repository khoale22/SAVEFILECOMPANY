package com.heb.pm.batchUpload.earley.attributeValues;

import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.util.file.CsvParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Parses the Earley attribute/value file.
 *
 * @author d116773
 * @since 2.16.0
 */
public class EarleyAttributeValuesParser {

	private static final int HIERARCHY_ID_COLUMN = 0;
	private static final int ATTRIBUTE_ID_COLUMN = 2;
	private static final int ATTRIBUTE_NAME_COLUMN = 3;
	private static final int ATTRIBUTE_VALUE_COLUMN = 4;
	private static final int ATTRIBUTE_VALUE_ID_COLUMN = 5;

	private static final String EARLEY_HIERARCHY_ID_HEADER = "EMH ID";
	private static final String EARLEY_ATTRIBUTE_ID_HEADER = "Attribute ID";
	private static final String ATTRIBUTE_VALUE_HEADER = "Attribute Value";
	private static final String ATTRIBUTE_VALUE_ID_HEADER = "Value ID";

	private CsvParser csvParser = new CsvParser();

	/**
	 * Validates the header of the file.
	 *
	 * @param file The file to validate.
	 * @throws FileProcessingException
	 */
	public void validateHeaderRecord(MultipartFile file) throws FileProcessingException {

		List<String> csvHeader;

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			csvHeader = this.csvParser.parseLine(bufferedReader.readLine());
		} catch (IOException e) {
			throw new FileProcessingException(e.getMessage());
		}
		if (!csvHeader.get(EarleyAttributeValuesParser.HIERARCHY_ID_COLUMN).equals(EarleyAttributeValuesParser.EARLEY_HIERARCHY_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain Earley Hierarchy IDs in column %d", EarleyAttributeValuesParser.HIERARCHY_ID_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeValuesParser.ATTRIBUTE_ID_COLUMN).equals(EarleyAttributeValuesParser.EARLEY_ATTRIBUTE_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain Earley Attribute IDs in column %d", EarleyAttributeValuesParser.ATTRIBUTE_ID_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeValuesParser.ATTRIBUTE_VALUE_COLUMN).equals(EarleyAttributeValuesParser.ATTRIBUTE_VALUE_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain Earley Attribute IDs in column %d", EarleyAttributeValuesParser.ATTRIBUTE_VALUE_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeValuesParser.ATTRIBUTE_VALUE_ID_COLUMN).equals(EarleyAttributeValuesParser.ATTRIBUTE_VALUE_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain Earley Attribute IDs in column %d", EarleyAttributeValuesParser.ATTRIBUTE_VALUE_ID_COLUMN));
		}
	}

	/**
	 * Returns the attribute ID from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the attribute ID from.
	 * @return The attribute ID.
	 * @throws FileProcessingException
	 */
	public String parseAttributeId(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeValuesParser.ATTRIBUTE_ID_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The attribute ID field is missing");
		}
		return csvRecord.get(EarleyAttributeValuesParser.ATTRIBUTE_ID_COLUMN);
	}

	/**
	 * Returns the parent hierarchy ID from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the parent hierarchy ID from.
	 * @return The parent hierarchy ID.
	 * @throws FileProcessingException
	 */
	public String parseParentHierarchyId(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeValuesParser.HIERARCHY_ID_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The parent hierarchy ID field is missing");
		}
		return csvRecord.get(EarleyAttributeValuesParser.HIERARCHY_ID_COLUMN);
	}

	/**
	 * Returns the attribute value from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the attribute value from.
	 * @return The attribute value.
	 * @throws FileProcessingException
	 */
	public String parseAttributeValue(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeValuesParser.ATTRIBUTE_VALUE_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The attribute value field is missing");
		}
		return csvRecord.get(EarleyAttributeValuesParser.ATTRIBUTE_VALUE_COLUMN);
	}

	/**
	 * Returns the attribute value ID from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the attribute value ID from.
	 * @return The attribute value ID.
	 * @throws FileProcessingException
	 */
	public String parseAttributeValueId(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeValuesParser.ATTRIBUTE_VALUE_ID_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The attribute value ID field is missing");
		}
		return csvRecord.get(EarleyAttributeValuesParser.ATTRIBUTE_VALUE_ID_COLUMN);
	}
}
