package com.heb.pm.batchUpload.matAttributeValues;

import com.heb.pm.batchUpload.BatchUploadParameterMap;
import com.heb.pm.batchUpload.BatchUploadRequest;
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

/**
 * Reads an excel and parses into MatAttributeValueModels.
 *
 * @author s573181
 * @since 2.29.0
 */
public class MatAttributeValuesExcelReader  implements ItemReader<MatAttributeValueModel>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(MatAttributeValuesExcelReader.class);

	private static final int DATA_ROW_START = 1;
	private static final int KEY_ID_COLUMN = 0;
	private static final int KEY_TYPE_COLUMN = 1;
	private static final int ATTRIBUTE_ID_COLUMN = 2;
	private static final int ATTRIBUTE_VALUE_COLUMN = 3;


	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Autowired
	private BatchUploadParameterMap batchUploadParameterMap;

	private Sheet sheet;

	private int currentRow;

	private int numberRows=0;

	private BatchUploadRequest batchUploadRequest;

	/**
	 * Reads an excel sheet row and parses into a MatAttributeValueModel.
	 *
	 * @return a MatAttributeValueModel.
	 * @throws UnexpectedInputException UnexpectedInputException.
	 * @throws ParseException ParseException.
	 * @throws NonTransientResourceException NonTransientResourceException.
	 */
	@Override
	public MatAttributeValueModel read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
		MatAttributeValueModel matAttributeValueModel = null;
		if(this.currentRow <= this.numberRows){
			Row row = this.sheet.getRow(this.currentRow);
			if(this.doesRowLookBlank(row)){
				return null;
			}
			matAttributeValueModel = new MatAttributeValueModel();
			Cell cell;
			for (short i = 0; i < 4; i++) {
				cell = row.getCell(i);
				switch (i) {
					case KEY_ID_COLUMN: {
						try {
							if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
								matAttributeValueModel.setKeyId(Double.valueOf(cell.getRichStringCellValue().getString().trim()).longValue());
								break;
							} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								matAttributeValueModel.setKeyId(Double.valueOf(cell.getNumericCellValue()).longValue());
								break;
							} else {
								this.currentRow++;
								return matAttributeValueModel.setErrorMessage("Invalid input for Key ID");
							}
						} catch (NumberFormatException e) {
							this.currentRow++;
							return matAttributeValueModel.setErrorMessage("Invalid input for Key ID.");
						}
					}
					case KEY_TYPE_COLUMN: {
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							matAttributeValueModel.setKeyType(cell.getRichStringCellValue().getString());
							break;
						} else {
							this.currentRow++;
							return matAttributeValueModel.setErrorMessage("Invalid input for Key Type.");
						}
					}
					case ATTRIBUTE_ID_COLUMN: {
						try {

							if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
								matAttributeValueModel.setAttributeId(Double.valueOf(cell.getRichStringCellValue().getString().trim()).longValue());
								break;

							} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								matAttributeValueModel.setAttributeId(Double.valueOf(cell.getNumericCellValue()).longValue());
								break;
							} else {
								this.currentRow++;
								return matAttributeValueModel.setErrorMessage("Invalid input for Attribute ID.");
							}
						} catch (NumberFormatException e) {
							this.currentRow++;
							return matAttributeValueModel.setErrorMessage("Invalid input for Attribute ID.");
						}
					}
					case ATTRIBUTE_VALUE_COLUMN: {
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (DateUtil.isCellDateFormatted(cell)) {
								matAttributeValueModel.setAttributeDateValue(cell.getDateCellValue());
								break;
							} else {
								matAttributeValueModel.setAttributeValue(Double.valueOf(cell.getNumericCellValue()).toString());
							}
						} else if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							matAttributeValueModel.setAttributeValue(cell.getRichStringCellValue().getString());
							break;
						} else {
							this.currentRow++;
							return matAttributeValueModel.setErrorMessage("Invalid input for Attribute Value.");
						}
						break;
					}
				}
			}
			this.currentRow++;
		}
		return matAttributeValueModel;

	}

	/**
	 * Reads the request and gets the sheet from the batch request. Sets start row and number of rows.
	 *
	 * @param stepExecution the step.
	 */
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
		this.currentRow = DATA_ROW_START;
		this.numberRows = this.sheet.getLastRowNum();
	}

	/**
	 * Empty override.
	 *
	 * @param stepExecution the step.
	 * @return null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}


	/**
	 * doesRowLookBlank.
	 * @param row
	 *            Row
	 * @return boolean
	 */
	private boolean doesRowLookBlank(Row row) {
		boolean ret = true;
		if (row == null) {
			ret = true;
		} else {
			Cell cell;
			String str;
			for (int i = 0; i < row.getLastCellNum(); i++) {
				cell = row.getCell((short)i);
				if (cell != null) {
					if (cell.getCellType() == Cell.CELL_TYPE_BLANK || cell.getCellType() == Cell.CELL_TYPE_STRING) {
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							str = cell.getRichStringCellValue().getString();
							if (!str.trim().isEmpty()) {
								ret = false;
								break;
							}
						}
					} else {
						ret = false;
						break;
					}
				}
			}
		}
		return ret;
	}
}
