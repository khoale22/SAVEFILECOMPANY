/*
 *  ExcelReader
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload;
import com.heb.pm.batchUpload.parser.ExcelParser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * This is ExcelReader class.
 *
 * @author vn55306
 * @since 2.8.0
 */
public class ExcelReader implements ItemReader<List<String>>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);
	private static final int DATA_ROW_START_INDEX_NUTRITION = 1;
	private static final int DATA_ROW_START_INDEX_ASSORTMENT = 1;
	private static final int DATA_ROW_START_INDEX_WINE = 2;
	private static final int DATA_ROW_START_INDEX_PRIMO_PICK = 4;
	private static final int DATA_ROW_START_INDEX_SERVICE_CASE_SIGNS = 4;
	private static final int DATA_ROW_START_INDEX_EBM_BDA = 1;
	private static final int DATA_ROW_START_INDEX_BIG_FIVE = 1;
	private static final int DATA_ROW_START_INDEX_RELATED_PRODUCTS = 1;
	private static final int DATA_ROW_START_INDEX_UNDEFINED = 0;
	private static final String LOG_ERR_UNDEFINED_EXCEL_FILE = "Batch processing aborted. Encountered " +
			"invalid/undefined Data Row Start Index for tracking-id:%d";

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;
	@Autowired
	private BatchUploadParameterMap batchUploadParameterMap;
	@Autowired
	private ExcelParser excelParser;
	Sheet sheet;
	int currentRow;
	int numberRows=0;
	BatchUploadRequest batchUploadRequest;

	@Override
	public List<String> read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
		List<String> values = null;
		if (this.currentRow == DATA_ROW_START_INDEX_UNDEFINED) {
			logger.error(String.format(LOG_ERR_UNDEFINED_EXCEL_FILE, this.transactionId));
			return null;
		}
		if(this.currentRow <= this.numberRows){
			Row row = this.sheet.getRow(this.currentRow);
			if(this.excelParser.doesRowLookBlank(row)){
				return null;
			}
			values = new LinkedList<String>();
			for (short i = 0; i < row.getLastCellNum(); i++) {
				values.add(this.excelParser.getValueOfCell(row.getCell(i)));
			}
			this.currentRow++;
		}
		return values;

	}

	@Override
	public void beforeStep(StepExecution stepExecution){
		batchUploadRequest = this.batchUploadParameterMap.get(this.transactionId);
		byte[] data = batchUploadRequest.getData();
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			Workbook workBook = new XSSFWorkbook(inputStream);
			int numberOfSheets = workBook.getNumberOfSheets();
			if (numberOfSheets > 0) {
				this.sheet = workBook.getSheetAt(0);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		switch (batchUploadRequest.getBatchUploadType()){
			case ASSORTMENT:
				this.currentRow = DATA_ROW_START_INDEX_ASSORTMENT;
				break;
			case WINE:
				this.currentRow = DATA_ROW_START_INDEX_WINE;
				break;
			case PRIMO_PICK:
				this.currentRow = DATA_ROW_START_INDEX_PRIMO_PICK;
				break;
			case SERVICE_CASE_SIGNS:
				this.currentRow = DATA_ROW_START_INDEX_SERVICE_CASE_SIGNS;
				break;
			case NUTRITION:
				this.currentRow = DATA_ROW_START_INDEX_NUTRITION;
				break;
			case BIG_5:
				this.currentRow = DATA_ROW_START_INDEX_BIG_FIVE;
				break;	
			case RELATED_PRODUCTS:
				this.currentRow = DATA_ROW_START_INDEX_RELATED_PRODUCTS;
				break;
			case EBM_BDA:
				this.currentRow = DATA_ROW_START_INDEX_EBM_BDA;
				break;
			default:
				this.currentRow = DATA_ROW_START_INDEX_UNDEFINED;
		}
		this.numberRows = this.sheet.getLastRowNum();
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}


}
