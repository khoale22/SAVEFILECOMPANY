package com.heb.pm.batchUpload.earley.hierarchy;

import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.util.file.CsvParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Parses an Earley hierarhcy record.
 *
 * @author d116773
 * @since 2.15.0
 */
public class EarleyHierarchyParser {

	private static final int HIERARCHY_ID_COLUMN = 0;
	private static final int SHORT_NAME_COLUMN = 1;
	private static final int LONG_NAME_COLUMN = 2;
	private static final int STATUS_COLUMN = 11;
	private static final int PARENT_HIERARCHY_ID_COLUMN = 7;

	private static final String HIERARCHY_ID_HEADER = "EMH ID";
	private static final String SHORT_NAME_HEADER = "Short Name";
	private static final String LONG_NAME_HEADER = "Long Name";
	private static final String STATUS_HEADER = "Status";
	private static final String PARENT_HIERARCHY_ID_HEADER = "Primary Parent EMH ID";

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

		if (!csvHeader.get(EarleyHierarchyParser.HIERARCHY_ID_COLUMN).equals(EarleyHierarchyParser.HIERARCHY_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the Earley hierarchy ID in column %d",
					EarleyHierarchyParser.HIERARCHY_ID_COLUMN + 1));
		}

		if (!csvHeader.get(EarleyHierarchyParser.SHORT_NAME_COLUMN).equals(EarleyHierarchyParser.SHORT_NAME_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the Earley short name in column %d",
					EarleyHierarchyParser.SHORT_NAME_COLUMN + 1));
		}

		if (!csvHeader.get(EarleyHierarchyParser.LONG_NAME_COLUMN).equals(EarleyHierarchyParser.LONG_NAME_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the Earley long name in column %d",
					EarleyHierarchyParser.LONG_NAME_COLUMN + 1));
		}

		if (!csvHeader.get(EarleyHierarchyParser.PARENT_HIERARCHY_ID_COLUMN).equals(EarleyHierarchyParser.PARENT_HIERARCHY_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the parent Earley hierarchy ID in column %d",
					EarleyHierarchyParser.PARENT_HIERARCHY_ID_COLUMN + 1));
		}

		if (!csvHeader.get(EarleyHierarchyParser.STATUS_COLUMN).equals(EarleyHierarchyParser.STATUS_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain status in column %d",
					EarleyHierarchyParser.STATUS_COLUMN + 1));
		}
	}

	/**
	 * Returns the Earley hierarchy ID from the record.
	 *
	 * @param csvRecord The record to parse.
	 * @return The Earley hierarchy ID from the record.
	 * @throws FileProcessingException
	 */
	public String parseEarleyHierarchyId(List<String> csvRecord) throws FileProcessingException {
		if (csvRecord.size() < EarleyHierarchyParser.HIERARCHY_ID_COLUMN + 1) {
			throw new FileProcessingException(String.format("File does not contain the Earley hierarchy ID in column %d",
					EarleyHierarchyParser.HIERARCHY_ID_COLUMN + 1));
		}
		return csvRecord.get(EarleyHierarchyParser.HIERARCHY_ID_COLUMN);
	}

	/**
	 * Returns the short name from the record.
	 *
	 * @param csvRecord The record to parse.
	 * @return The short name from the record.
	 * @throws FileProcessingException
	 */
	public String parseShortName(List<String> csvRecord)  throws FileProcessingException  {
		if (csvRecord.size() < EarleyHierarchyParser.SHORT_NAME_COLUMN + 1) {
			throw new FileProcessingException(String.format("File does not contain the Earley short name in column %d",
					EarleyHierarchyParser.SHORT_NAME_COLUMN + 1));
		}
		return csvRecord.get(EarleyHierarchyParser.SHORT_NAME_COLUMN);
	}

	/**
	 * Returns the long name from the record.
	 *
	 * @param csvRecord The record to parse.
	 * @return The long name from the record.
	 * @throws FileProcessingException
	 */
	public String parseLongName(List<String> csvRecord) throws FileProcessingException  {
		if (csvRecord.size() < EarleyHierarchyParser.LONG_NAME_COLUMN + 1) {
			throw new FileProcessingException(String.format("File does not contain the Earley long name in column %d",
					EarleyHierarchyParser.LONG_NAME_COLUMN + 1));
		}
		return csvRecord.get(EarleyHierarchyParser.LONG_NAME_COLUMN);
	}

	/**
	 * Returns the parent hierarchy ID from the record.
	 *
	 * @param csvRecord The record to parse.
	 * @return The parent hierarchy ID.
	 * @throws FileProcessingException
	 */
	public String parseParentEarleyHierarchyId(List<String> csvRecord) throws FileProcessingException {
		if (csvRecord.size() < EarleyHierarchyParser.PARENT_HIERARCHY_ID_COLUMN + 1) {
			throw new FileProcessingException(String.format("File does not contain the parent Earley hierarchy ID in column %d",
					EarleyHierarchyParser.PARENT_HIERARCHY_ID_COLUMN + 1));
		}
		return csvRecord.get(EarleyHierarchyParser.PARENT_HIERARCHY_ID_COLUMN);
	}

	/**
	 * Returns whether or not this node is active from the record. It looks for the text 'Active'.
	 *
	 * @param csvRecord The record to parse.
	 * @return True if the node is active and false otherwise.
	 * @throws FileProcessingException
	 */
	public Boolean parseActive(List<String> csvRecord) throws FileProcessingException {
		if (csvRecord.size() < EarleyHierarchyParser.STATUS_COLUMN + 1) {
			throw new FileProcessingException(String.format("File does not contain status in column %d",
					EarleyHierarchyParser.STATUS_COLUMN + 1));
		}
		String active = csvRecord.get(EarleyHierarchyParser.STATUS_COLUMN);
		return active.equals("Active");
	}

}
