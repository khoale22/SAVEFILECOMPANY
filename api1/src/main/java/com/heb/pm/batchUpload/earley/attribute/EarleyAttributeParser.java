package com.heb.pm.batchUpload.earley.attribute;

import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.util.file.CsvParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Parses the Early attribute file.
 *
 * @author d116773
 * @since 2.16.0
 */
public class EarleyAttributeParser {

	private static final int HIERARCHY_ID_COLUMN = 0;
	private static final int ATTRIBUTE_ID_COLUMN = 2;
	private static final int ATTRIBUTE_NAME_COLUMN = 3;
	private static final int ATTRIBUTE_DESCRIPTION_COLUMN = 4;
	private static final int REQUIRED_COLUMN = 6;
	private static final int MULTIVALUE_COLUMN = 7;
	private static final int ATTRIBUTE_VALUES_COLUMN = 15;
	private static final int HELP_TEXT_COLUMN = 5;

	private static final String EARLEY_HIERARCHY_ID_HEADER = "EMH ID";
	private static final String EARLEY_ATTRIBUTE_ID_HEADER = "Attribute ID";
	private static final String ATTRIBUTE_NAME_HEADER = "Name";
	private static final String ATTRIBUTE_DESCRIPTION_HEADER = "Description";
	private static final String REQUIRED_HEADER = "Required";
	private static final String MULTIVALUE_HEADER = "Multivalued";
	private static final String ATTRIBUTE_VALUES_HEADER = "Sample Values";
	private static final String HELP_TEXT_HEADER = "Help Text";

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

		if (!csvHeader.get(EarleyAttributeParser.HIERARCHY_ID_COLUMN).equals(EarleyAttributeParser.EARLEY_HIERARCHY_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain Earley Hierarchy IDs in column %d", EarleyAttributeParser.HIERARCHY_ID_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeParser.ATTRIBUTE_ID_COLUMN).equals(EarleyAttributeParser.EARLEY_ATTRIBUTE_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain Earley Attribute IDs in column %d", EarleyAttributeParser.ATTRIBUTE_ID_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeParser.ATTRIBUTE_NAME_COLUMN).equals(EarleyAttributeParser.ATTRIBUTE_NAME_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the attribute names in column %d", EarleyAttributeParser.ATTRIBUTE_NAME_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeParser.ATTRIBUTE_DESCRIPTION_COLUMN).equals(EarleyAttributeParser.ATTRIBUTE_DESCRIPTION_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the attribute descriptions in column %d", EarleyAttributeParser.ATTRIBUTE_DESCRIPTION_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeParser.REQUIRED_COLUMN).equals(EarleyAttributeParser.REQUIRED_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain whether or not the attribute is required in column %d", EarleyAttributeParser.REQUIRED_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeParser.MULTIVALUE_COLUMN).equals(EarleyAttributeParser.MULTIVALUE_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain whether or not the attribute is multi-valued in column %d", EarleyAttributeParser.MULTIVALUE_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeParser.ATTRIBUTE_VALUES_COLUMN).equals(EarleyAttributeParser.ATTRIBUTE_VALUES_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the attribute values in column %d", EarleyAttributeParser.ATTRIBUTE_VALUES_COLUMN));
		}
		if (!csvHeader.get(EarleyAttributeParser.HELP_TEXT_COLUMN).equals(EarleyAttributeParser.HELP_TEXT_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the help text in column %d", EarleyAttributeParser.HELP_TEXT_COLUMN));
		}
	}

	/**
	 * Returns the help text from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the help text from.
	 * @return The help text.
	 * @throws FileProcessingException
	 */
	public String parseHelpText(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeParser.HELP_TEXT_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The help text field is missing");
		}
		return csvRecord.get(EarleyAttributeParser.HELP_TEXT_COLUMN);
	}

	/**
	 * Returns whether or not the attribute is required from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull whether or not the attribute is required from.
	 * @return Whether or not the attribute is required.
	 * @throws FileProcessingException
	 */
	public boolean parseRequired(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeParser.REQUIRED_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The required field is missing");
		}
		return Boolean.parseBoolean(csvRecord.get(EarleyAttributeParser.REQUIRED_COLUMN));
	}

	/**
	 * Returns whether or not the attribute allows multiple values from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull whether or not the attribute allows multiple values from.
	 * @return Whether or not the attribute allows multiple values.
	 * @throws FileProcessingException
	 */
	public boolean parseMultiValue(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeParser.MULTIVALUE_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The multi-value field is missing");
		}
		return Boolean.parseBoolean(csvRecord.get(EarleyAttributeParser.MULTIVALUE_COLUMN));
	}

	/**
	 * Returns the parent hierarchy ID from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the parent hierarchy ID from.
	 * @return The parent hierarchy ID.
	 * @throws FileProcessingException
	 */
	public String parseParentHierarchyId(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeParser.HIERARCHY_ID_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The parent hierarchy ID field is missing");
		}
		return csvRecord.get(EarleyAttributeParser.HIERARCHY_ID_COLUMN);
	}

	/**
	 * Returns the attribute ID from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the attribute ID from.
	 * @return The attribute ID .
	 * @throws FileProcessingException
	 */
	public String parseAttributeId(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeParser.ATTRIBUTE_ID_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The attribute ID field is missing");
		}
		return csvRecord.get(EarleyAttributeParser.ATTRIBUTE_ID_COLUMN);
	}

	/**
	 * Returns the attribute name from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the attribute name from.
	 * @return The attribute name .
	 * @throws FileProcessingException
	 */
	public String parseAttributeName(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeParser.ATTRIBUTE_NAME_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The attribute name field is missing");
		}
		return csvRecord.get(EarleyAttributeParser.ATTRIBUTE_NAME_COLUMN);
	}

	/**
	 * Returns the description from an Earley attribute record.
	 *
	 * @param csvRecord The record to pull the description from.
	 * @return The description.
	 * @throws FileProcessingException
	 */
	public String parseDescription(List<String> csvRecord) throws FileProcessingException {
		if (EarleyAttributeParser.ATTRIBUTE_DESCRIPTION_COLUMN > csvRecord.size() - 1) {
			throw new FileProcessingException("The description field is missing");
		}
		return csvRecord.get(EarleyAttributeParser.ATTRIBUTE_DESCRIPTION_COLUMN);
	}
}
