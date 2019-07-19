package com.heb.pm.batchUpload.earley;

import com.heb.pm.batchUpload.BatchUploadParameterMap;
import com.heb.pm.batchUpload.BatchUploadRequest;
import com.heb.pm.batchUpload.JobExecutionException;
import com.heb.util.file.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Spring Batch Reader that will pull an input stream out of the batch load parameter map. This input stream should
 * contain data in a CSV format.
 *
 * @author d116773
 * @since 2.15.0
 */
public class CsvReader implements ItemReader<List<String>>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(CsvReader.class);

	private static final int HEADER_LINE_COUNT = 1;

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Autowired
	private BatchUploadParameterMap batchUploadParameterMap;

	private BufferedReader bufferedReader;

	private CsvParser csvParser = new CsvParser();

	private long recordCount;

	/**
	 * Returns a list of strings parsed from a record in a CSV file. Will return null when there are no more records.
	 *
	 * @return A list of strings parsed from a record in a CSV file.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		// Get a line of data.
		String s = this.bufferedReader.readLine();
		CsvReader.logger.debug(String.format("Record %d: %s", this.recordCount++, s));

		// Return null once done
		if (s == null) {
			return null;
		}

		// Parse the line and return the results.
		return this.csvParser.parseLine(s);
	}

	/**
	 * Called by Spring Batch before the step this file is in is started. It prepares the data to be read from the
	 * CSV.
	 *
	 * @param stepExecution The context this step will execute in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {

		CsvReader.logger.info(String.format("Looking for parameters for transaction %d", this.transactionId));

		// Try and get the input stream from the batch upload parameters map.
		BatchUploadRequest batchUploadRequest = this.batchUploadParameterMap.get(this.transactionId);
		if (batchUploadRequest == null) {
			throw new IllegalArgumentException(
					String.format("Unable to find upload parameters for transaction %d", this.transactionId));
		}

		if (batchUploadRequest.getDataAsStream() == null) {
			throw new IllegalArgumentException(
					String.format("User input not passed to processing job for transaction %d", this.transactionId));
		}

		// Construct a buffered reader for the read process to  read from.
		this.bufferedReader = new BufferedReader(new InputStreamReader(batchUploadRequest.getDataAsStream()));

		// Skip header lines.
		try {
			for (int i = 0; i < CsvReader.HEADER_LINE_COUNT; i++) {
				this.bufferedReader.readLine();
			}
		} catch (IOException e) {
			CsvReader.logger.error(e.getMessage());
			throw new JobExecutionException(e.getMessage(), e.getCause());
		}

		this.recordCount = 0;
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
