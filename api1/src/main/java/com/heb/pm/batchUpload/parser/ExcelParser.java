/*
 *  ExcelParser
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.parser;

import org.apache.poi.ss.usermodel.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This is template for batch upload parse.
 *
 * @author vn55306
 * @since 2.8.0
 */
@Service
public class ExcelParser implements Serializable{
	private static final Logger logger = LoggerFactory.getLogger(ExcelParser.class);
	private static final long serialVersionUID = 1L;
	public static final String DATE_MM_DD_YYYY = "MM/dd/yyyy";
	/**
	 * STR_EMPTY.
	 */
	public static final String STR_EMPTY = "";
	/**
	 * TYPE.
	 */
	public static final String TYPE = ".type";




	/**
	 * getValueOfCell.
	 * 
	 * @param cell
	 *            Cell
	 * @return String
	 */
	public String getValueOfCell(Cell cell) {
		String cellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(ExcelParser.DATE_MM_DD_YYYY, Locale.getDefault());
					cellValue = dateFormat.format(cell.getDateCellValue());
				} else {
					double val = cell.getNumericCellValue();
					if (Math.round(val) == val) {
						cellValue = String.valueOf(Math.round(val));
					} else {
						cellValue = new DecimalFormat("#############.#####").format(val);
					}
				}
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = ExcelParser.STR_EMPTY;
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			default:
				break;
			}
		}
		return cellValue;
	}

	 /**
     * doesRowLookBlank.
     * @param row
     *            Row
     * @return boolean
     */
    public boolean doesRowLookBlank(Row row) {
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
