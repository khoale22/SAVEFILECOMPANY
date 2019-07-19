package com.heb.pm.batchUpload.earley.hierarchy;

import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.util.file.CsvParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Parses an Earley product record.
 *
 * @author d116773
 * @since 2.15.0
 */
public class EarleyProductParser {

	private static final int PRODUCT_ID_COLUMN = 0;
	private static final int SHOW_IN_HEB_COM_COLUMN = 2;
	private static final int EARLEY_HIERARCHY_ID_COLUMN = 8;
	private static final String PRODUCT_ID_HEADER = "Product ID";
	private static final String SHOW_IN_HEB_COM_HEADER = "HEB.com";
	private static final String EARLEY_HIERARCHY_ID_HEADER = "EMH ID";

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

		if (!csvHeader.get(EarleyProductParser.PRODUCT_ID_COLUMN).equals(EarleyProductParser.PRODUCT_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain product IDs in column %d",
					EarleyProductParser.PRODUCT_ID_COLUMN + 1));
		}
		if (!csvHeader.get(EarleyProductParser.SHOW_IN_HEB_COM_COLUMN).equals(EarleyProductParser.SHOW_IN_HEB_COM_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain whether or not this product belongs on heb.com in column %d",
					EarleyProductParser.SHOW_IN_HEB_COM_COLUMN + 1));
		}
		if (!csvHeader.get(EarleyProductParser.EARLEY_HIERARCHY_ID_COLUMN).equals(EarleyProductParser.EARLEY_HIERARCHY_ID_HEADER)) {
			throw new FileProcessingException(String.format("File does not contain the Earley hierarchy ID in column %d",
					EarleyProductParser.EARLEY_HIERARCHY_ID_COLUMN + 1));
		}
	}

	/**
	 * Returns whether or not to add this product to the heb.com hierarchy from the record.
	 *
	 * @param csvRecord The record to parse.
	 * @return Whether or not to add this product to the heb.com hierarchy.
	 * @throws FileProcessingException
	 */
	public boolean parseShowOnHebCom(List<String> csvRecord) throws FileProcessingException {
		if (EarleyProductParser.SHOW_IN_HEB_COM_COLUMN > csvRecord.size() -1) {
			throw new FileProcessingException("Show in heb.com column is missing");
		}
		return Boolean.parseBoolean(csvRecord.get(EarleyProductParser.SHOW_IN_HEB_COM_COLUMN));
	}

	/**
	 * Returns the product ID from the record.
	 *
	 * @param csvRecord The record to parse.
	 * @return The product ID.
	 * @throws FileProcessingException
	 */
	public long parseProductId(List<String> csvRecord) throws FileProcessingException {
		if (EarleyProductParser.PRODUCT_ID_COLUMN > csvRecord.size() -1) {
			throw new FileProcessingException("Product ID column is missing");
		}
		try {
			return Long.parseLong(csvRecord.get(EarleyProductParser.PRODUCT_ID_COLUMN));
		} catch (NumberFormatException e) {
			throw new FileProcessingException(String.format("Unable to convert %s to a product ID",
					csvRecord.get(EarleyProductParser.PRODUCT_ID_COLUMN)));
		}
	}

	/**
	 * Returns the parent hierarchy ID from the record.
	 *
	 * @param csvRecord The record to parse.
	 * @return The parent hierarchy ID.
	 * @throws FileProcessingException
	 */
	public String parseParentHierarchyId(List<String> csvRecord) throws FileProcessingException {
		if (EarleyProductParser.EARLEY_HIERARCHY_ID_COLUMN > csvRecord.size() -1) {
			throw new FileProcessingException("Hierarchy ID column is missing");
		}
		return csvRecord.get(EarleyProductParser.EARLEY_HIERARCHY_ID_COLUMN);
	}
}
