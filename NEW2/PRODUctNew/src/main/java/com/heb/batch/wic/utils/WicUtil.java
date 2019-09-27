/*
 * WicUtil
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.utils;

import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.*;
import com.heb.batch.wic.webservice.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class WicUtil {
    private static final Logger LOGGER = LogManager.getLogger(WicUtil.class);
    public static final String DEFAULT_FORMAT_DATE ="yyyyMMdd";
    public static final String FORMAT_DATE_SEND_EMAIL ="MMM dd, yyyy";
    public static final String DEFAULT_TXTSTATE_DATE ="00000000";
    public static final String DEFAULT_SQL_DATE ="16000101";
    public static final String DEFAULT_SQL_ENDDATE ="99991231";
    /**
     * Create WicUtil
     */
    private WicUtil() {
    }
    /**
     * Calculate check digit.
     *
     * @param unitUpc
     *            the unit upc
     * @return the int
     */
    public static int calculateCheckDigit(String unitUpc) {
        if (null != unitUpc) {
            unitUpc = removeMinus(unitUpc);
            unitUpc = getPadding(getWithoutDecimals(unitUpc));
            int checkDigit = 0;
            StringBuilder unitUpcBuffer = new StringBuilder(unitUpc);
            int finalValue = 0;
            for (int i = 0; i < unitUpcBuffer.length(); i++) {
                if (i % 2 != 0)
                    finalValue = finalValue + Integer.parseInt(unitUpcBuffer.charAt(i) + StringUtils.EMPTY);
                else {
                    int temp = Integer.parseInt(unitUpcBuffer.charAt(i) + StringUtils.EMPTY);
                    finalValue = finalValue + (temp * 3);
                }
            }
            checkDigit = finalValue % 10;
            if (checkDigit % 10 != 0) {
                checkDigit = java.lang.Math.abs(10 - checkDigit);
            }
            return checkDigit;
        }
        return 0;
    }

    /**
     * Removes the minus.
     *
     * @param in
     *            the in
     * @return the string
     */
    private static String removeMinus(String in) {
        if (StringUtils.isNotEmpty(in)) {
            in = in.replaceFirst("-", "");
        }
        return in;
    }

    /**
     * Gets the without decimals.
     *
     * @param in
     *            the in
     * @return the without decimals
     */
    public static String getWithoutDecimals(String in) {
        if (StringUtils.isNotEmpty(in)) {
            try {
                Double double1 = new Double(in);
                long l = double1.longValue();
                return l + StringUtils.EMPTY;
            } catch (NumberFormatException e) {
                return in;
            }
        } else {
            return in;
        }
    }

    /**
     * Gets the padding.
     *
     * @param unitUpc
     *            the unit upc
     * @return the padding
     */
    public static String getPadding(String unitUpc) {
        if (StringUtils.isNotEmpty(unitUpc)) {
            unitUpc = unitUpc.trim();
            int unitUPCLength = unitUpc.length();
            int padSize = 13 - unitUPCLength;
            int i = 0;
            while (i < padSize) {
                unitUpc = "0".concat(unitUpc);
                i++;
            }
        }
        return unitUpc;
    }

    /**
     * Date conversion from String
     *
     * @param sDate the String of Date
     * @return SQL Date
     */
    public static java.sql.Date convertDateFromString(String sDate) {
        try {
            String convertDate = WicUtil.getDateOrDefault(sDate);
            java.util.Date uDate = new SimpleDateFormat(DEFAULT_FORMAT_DATE).parse(convertDate);
            return new java.sql.Date(uDate.getTime());
        } catch (ParseException e) { return null; }
    }

    /**
     * convert Date To String
     *
     * @param uDate the Date
     * @return String
     */
    public static String convertDateToString(Date uDate, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(uDate);

    }

    /**
     * Parse String to Double
     *
     * @param number the number to parse
     * @param decimalPoint the decimal point
     * @return number(Double type)
     */
    public static Double parseStringToDouble(String number, Integer decimalPoint) {
        Integer defaultPoint = 100;
        if(decimalPoint!=null && decimalPoint==4){
            defaultPoint = 1000;
        }
        Double str = NumberUtils.toDouble(number);
        if(str>0){
          str = str/defaultPoint;
        }
        return str;
    }
    /**
     * get Current day
     * @return String format MM dd, yyyy
     */
    public static String getCurrentDay(String format){
        DateFormat df = new SimpleDateFormat(format);
        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
    /**
     * create report sent to POS system.
     * @param productScanCodeWicDocuments
     *            List<ProductScanCodeWicDocument>
     * @return The workbook of POI library have byte array of excel file
     * @throws WicException
     *             If cannot get excel template
     * @author vn55306
     */
    public static XSSFWorkbook createExcelDocumentPos(List<ProductScanCodeWicDocument> productScanCodeWicDocuments) throws WicException {
        XSSFWorkbook workbook = null;
        try {
            int rowNum = NumberUtils.INTEGER_ONE;
            int column;
            InputStream is = WicUtil.class.getResourceAsStream("/template/pos_template.xlsx");
            workbook = new XSSFWorkbook(is);
            XSSFSheet excelSheet = workbook.getSheetAt(NumberUtils.INTEGER_ZERO);
          //  excelSheet.set
            excelSheet.showInPane(NumberUtils.SHORT_ZERO,NumberUtils.SHORT_ZERO);
            XSSFRow excelRow;
            XSSFCellStyle normalCellStyle = WicUtil.getCellStyle(workbook, HSSFCellStyle.ALIGN_LEFT);
            for(ProductScanCodeWicDocument productScanCodeWicDocument : productScanCodeWicDocuments){
                excelRow = excelSheet.createRow(rowNum);
                column = NumberUtils.INTEGER_ZERO;
                // UPC
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getUpc()),excelRow,column,normalCellStyle);
                column = column + 1;
                // PROD DESC
                WicUtil.createCell(productScanCodeWicDocument.getWicDescription(),excelRow,column,normalCellStyle);
                column = column + 1;
                // CATEGORY CODE
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getWicCategoryId()),excelRow,column,normalCellStyle);
                column = column + 1;
                // CATEGORY DESC
                WicUtil.createCell(productScanCodeWicDocument.getWicCategoryDesc(),excelRow,column,normalCellStyle);
                column = column + 1;
                // SUBCATEGORY CODE
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getWicSubCategoryId()),excelRow,column,normalCellStyle);
                column = column + 1;
                // SUBCATEGORY DESC
                WicUtil.createCell(productScanCodeWicDocument.getWicSubCategoryDesc(),excelRow,column,normalCellStyle);
                column = column + 1;
                // UNIT OF MEASURE
                WicUtil.createCell(productScanCodeWicDocument.getWicUntTxt(),excelRow,column,normalCellStyle);
                column = column + 1;
                // PACKAGE SIZE
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getWicPackageSize()),excelRow,column,normalCellStyle);
                column = column + 1;
                // BENEFIT QTY
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getWicBnFtQty()),excelRow,column,normalCellStyle);
                column = column + 1;
                // BENEFIT UNIT
                WicUtil.createCell(productScanCodeWicDocument.getWicBnftUntTxt(),excelRow,column,normalCellStyle);
                column = column + 1;
                // ITEM PRICE
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getWicPrcAmt()),excelRow,column,normalCellStyle);
                column = column + 1;
                // PRICE TYPE
                WicUtil.createCell(productScanCodeWicDocument.getWicPrcCd(),excelRow,column,normalCellStyle);
                column = column + 1;
                // CARD ID
                WicUtil.createCell(productScanCodeWicDocument.getWicCrdAcptId(),excelRow,column,normalCellStyle);
                column = column + 1;
                // EFFECTIVE DATE
                WicUtil.createCell(convertDateToString(productScanCodeWicDocument.getEffDt(),DEFAULT_FORMAT_DATE),excelRow,column,normalCellStyle);
                column = column + 1;
                // END-DATE
                WicUtil.createCell(convertDateToString(productScanCodeWicDocument.getEndDt(),DEFAULT_FORMAT_DATE),excelRow,column,normalCellStyle);
                column = column + 1;
                // UPC_PLU_LENGTH
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getUpcPluLength()),excelRow,column,normalCellStyle);
                column = column + 1;
                // PURCHASE-INDICATOR
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getPurchaseIndicator()),excelRow,column,normalCellStyle);
                column = column + 1;
                // MANUAL_VOUCHER_INDICATOR
                WicUtil.createCell(productScanCodeWicDocument.getManualVoucherIndicator(),excelRow,column,normalCellStyle);
                column = column + 1;
                // WIC SW
                WicUtil.createCell(productScanCodeWicDocument.getWicSw(),excelRow,column,normalCellStyle);
                column = column + 1;
                // LEB SW
                WicUtil.createCell(productScanCodeWicDocument.getLebSwitch(),excelRow,column,normalCellStyle);
                column = column + 1;
                // WIC APPLICATION ID
                WicUtil.createCell(String.valueOf(productScanCodeWicDocument.getWicAplId()),excelRow,column,normalCellStyle);
                column = column + 1;
                // ACTION
                WicUtil.createCell(productScanCodeWicDocument.getAction(),excelRow,column,normalCellStyle);
                rowNum = rowNum + 1;
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new WicException(e);
        }
        return workbook;
    }
    /**
     * Create string cell.
     * @param cellValue
     *            The string was added into cell
     * @param row
     *            The row will be create cell
     * @param column
     *            The column will be create cell
     * @param cellStyle
     *            The cell style
     * @author vn55306
     */
    private static void createCell(String cellValue, XSSFRow row, int column,XSSFCellStyle cellStyle) {
        XSSFCell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    }
    /**
     * Get cell style.
     * @param workbook
     *            The workbook of POI library
     * @param align
     *            The value define cell align
     * @return The cell style of POI library
     * @author vn55306
     */
    private static XSSFCellStyle getCellStyle(XSSFWorkbook workbook, short align) {
        XSSFFont fontCell = workbook.createFont();
        fontCell.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        fontCell.setFontName("Calibri");
        XSSFCellStyle cellStyle = null;
        cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(false);
        cellStyle.setFont(fontCell);
        cellStyle.setAlignment(align);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }
    
	/** 
	 * Return the current Time stamp.
	 * @return current Time stamp.
	 */
	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

    /**
     * create file text contain invalid data to sent Production support team.
     * @param texasStateDocumentInCorrects
     *            List<TexasStateDocument>
     * @throws WicException
     *             If cannot get excel template
     * @author vn55306
     */
	public static File createInvalidFile(List<TexasStateDocument> texasStateDocumentInCorrects,String fileName) throws IOException{
        StringBuilder content = new StringBuilder();
        for(TexasStateDocument texasStateDocument:texasStateDocumentInCorrects){
            content = content.append(texasStateDocument.getDataRaw()).append("\n");
        }
        File file = new File(fileName);
        FileUtils.writeStringToFile(file, content.toString());
        return file;
    }
    /**
     * create file text contain invalid data to sent Production support team.
     * @param productScanCodeWicDocuments
     *            List<ProductScanCodeWicDocument>
     * @throws WicException
     *             If cannot get excel template
     * @author vn55306
     */
    public static File createPosFile(List<ProductScanCodeWicDocument> productScanCodeWicDocuments,String fileName) throws WicException{
        XSSFWorkbook workbook = WicUtil.createExcelDocumentPos(productScanCodeWicDocuments);
        File file = new File(fileName);
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
        }catch (FileNotFoundException e){
            LOGGER.error(e.getMessage(),e);
            throw new WicException(WicException.FILE_NOT_FOUND);
        }catch (IOException e){
            LOGGER.error(e.getMessage(),e);
            throw new WicException(e.getMessage());
        }
        return file;
    }

    /**
     * Check whether Wic Switch is Yes or No
     *
     * @param item
     * @return True Meaning Wic Switch is Yes
     * 			No Meaning Wic Switch is No
     * @author vn55306
     */
    public static boolean checkWicSwitch(ProductScanCodeWicDocument item) {
        boolean flag = false;
        Date effectDate =item.getEffDt();
        Date endDate =item.getEndDt();
        Date curDt = Calendar.getInstance().getTime();
        Date defaultDate = WicUtil.convertDateFromString(WicUtil.getDateOrDefault(DEFAULT_SQL_ENDDATE));
        if(endDate == null || endDate.compareTo(defaultDate) == 0) {
            if(effectDate != null && curDt.compareTo(effectDate) >= 0)
                flag = true;
        } else if(effectDate!=null && curDt.compareTo(effectDate) >= 0 && endDate.compareTo(curDt) > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * convert WicCategory To WicCategoryDocument
     *
     * @param wicCategories List<BaseVO>
     * @return List<WicCategoryDocument>
     * @author vn55306
     */
    public static List<WicCategoryDocument> convertWicCategoryVoToWicCategoryDocument(List<BaseVO> wicCategories){
        List<WicCategoryDocument> wicCategoryDocuments = new ArrayList<>();
        WicCategoryDocument wicCategoryDocument;
        WicCategoryVO wicCategoryVO = null;
        if(wicCategories!=null){
            for(BaseVO baseVO:wicCategories){
                wicCategoryVO = (WicCategoryVO)baseVO;
                wicCategoryDocument = new WicCategoryDocument();
                wicCategoryDocument.setWicCatId(String.valueOf(wicCategoryVO.getWicCatId()));
                wicCategoryDocument.setDescription(wicCategoryVO.getWicCatDes());
                wicCategoryDocuments.add(wicCategoryDocument);
            }
        }
        return wicCategoryDocuments;
    }
    /**
     * convert WicCategory To WicCategoryDocument
     *
     * @param wicSubCategories List<BaseVO>
     * @return List<WicCategoryDocument>
     * @author vn55306
     */
    public static List<WicSubCategoryDocument> convertWicSubCategoryVoToWicSubCategoryDocument(List<BaseVO> wicSubCategories){
        List<WicSubCategoryDocument> wicSubCategoryDocuments = new ArrayList<>();
        WicSubCategoryDocument wicSubCategoryDocument;
        WicSubCategoryVO wicSubCategoryVO = null;
        if(wicSubCategories!=null){
            for(BaseVO baseVO:wicSubCategories){
                wicSubCategoryVO = (WicSubCategoryVO)baseVO;
                wicSubCategoryDocument = new WicSubCategoryDocument();
                wicSubCategoryDocument.setWicCategoryId(Long.valueOf(String.valueOf(wicSubCategoryVO.getWicCatId())));
                wicSubCategoryDocument.setWicSubCategoryId(Long.valueOf(String.valueOf(wicSubCategoryVO.getWicSubCatId())));
                wicSubCategoryDocument.setDescription(wicSubCategoryVO.getWicSubCatDes());
                wicSubCategoryDocuments.add(wicSubCategoryDocument);
            }
        }
        return wicSubCategoryDocuments;
    }

    /**
     * set Default Value ProductScanCodeWic when insert
     *
     * @param productScanCodeWic List<ProductScanCodeWic>
     * @author vn55306
     */
    public static void setDefaultValueProductScanCodeWic(ProductScanCodeWicVO productScanCodeWic){
        productScanCodeWic.setWicPrcCd(getValueOrDefault(productScanCodeWic.getWicPrcCd()));
        productScanCodeWic.setWicCrdAcptId(getValueOrDefault(productScanCodeWic.getWicCrdAcptId()));
        productScanCodeWic.setWicUntTxt(getValueOrDefault(productScanCodeWic.getWicUntTxt()));
        productScanCodeWic.setWicPkgSzQty(getValueOrDefault(productScanCodeWic.getWicPkgSzQty()));
        productScanCodeWic.setWicBnftQty(getValueOrDefault(productScanCodeWic.getWicBnftQty()));
        productScanCodeWic.setWicPrcAmt(getValueOrDefault(productScanCodeWic.getWicPrcAmt()));
        productScanCodeWic.setWicBnftUntTxt(getValueOrDefault(productScanCodeWic.getWicBnftUntTxt()));
        productScanCodeWic.setLebSw(getYesOrNoOrDefault(productScanCodeWic.getLebSw()));
    }
    /**
     * set Default Value ProductScanCodeWic when insert
     *
     * @param texasStateDocument List<ProductScanCodeWic>
     * @author vn55306
     */
    public static void setDefaultValueTexasStateDocument(TexasStateDocument texasStateDocument){
        texasStateDocument.setWicPrcCd(getValueOrDefault(texasStateDocument.getWicPrcCd()));
        texasStateDocument.setWicCrdAcptId(getValueOrDefault(texasStateDocument.getWicCrdAcptId()));
        texasStateDocument.setWicUntTxt(getValueOrDefault(texasStateDocument.getWicUntTxt()));
        texasStateDocument.setWicPkgSzQty(getValueOrDefaultStringDouble(texasStateDocument.getWicPkgSzQty()));
        texasStateDocument.setWicBnFtQty(getValueOrDefaultStringDouble(texasStateDocument.getWicBnFtQty()));
        texasStateDocument.setWicPrcAmt(getValueOrDefaultStringDouble((texasStateDocument.getWicPrcAmt())));
        texasStateDocument.setWicBnftUntTxt(getValueOrDefault(texasStateDocument.getWicBnftUntTxt()));
    }

    /**
     * Get default value for string type if value is blank
     *
     * @author vn03503
     * @param value String value
     * @return the result
     */
    public static String getValueOrDefault(String value) {
        return StringUtils.isEmpty(value) ? StringUtils.SPACE : value;
    }

    /**
     * Get default value for date if value is blank or value is 0000000
     *
     * @author vn03503
     * @param value String value
     * @return the result
     */
    public static String getDateOrDefault(String value) {
        String defaultValue = value;
        if(value == null || StringUtils.isEmpty(value.trim())){
            defaultValue = DEFAULT_SQL_DATE;
        } else if(value.equals(DEFAULT_TXTSTATE_DATE)){
            defaultValue = DEFAULT_SQL_ENDDATE;
        }
        return defaultValue;
    }

    /**
     * Get default value for double type if value is null
     *
     * @author vn03503
     * @param value Double value
     * @return the result
     */
    public static Double getValueOrDefault(Double value) {
        return value == null ? NumberUtils.DOUBLE_ZERO : value;
    }

    /**
     * Get default value for double type if value is null
     *
     * @author vn03503
     * @param value Double value
     * @return the result
     */
    public static String getValueOrDefaultStringDouble(String value) {
        return StringUtils.isEmpty(value) ? String.valueOf(NumberUtils.DOUBLE_ZERO) : value;
    }

    /**
     * Get default value for Yes/No type if value is blank
     *
     * @author vn03503
     * @param value the value
     * @return the result
     */
    public static String getYesOrNoOrDefault(String value) {
        return StringUtils.isEmpty(value) ? WicConstants.NO : value;
    }

    /**
     * correct number value
     *
     * @param texasStateDocument TexasStateDocument
     * @author vn55306
     */
    public static void correctTexasStateDocumentKey(TexasStateDocument texasStateDocument){
        texasStateDocument.setScnCdId(String.valueOf(Long.valueOf(texasStateDocument.getScnCdId())));
        texasStateDocument.setWicCatId(String.valueOf(Long.valueOf(texasStateDocument.getWicCatId())));
        texasStateDocument.setWicSubCatId(String.valueOf(Long.valueOf(texasStateDocument.getWicSubCatId())));
        texasStateDocument.setWicAplId(String.valueOf(Long.valueOf(texasStateDocument.getWicAplId())));
        texasStateDocument.setId(TexasStateDocument.generateId(texasStateDocument.getWicAplId(),texasStateDocument.getScnCdId(),texasStateDocument.getWicCatId(),texasStateDocument.getWicSubCatId()));
    }
    /**
     * correct number value
     *
     * @param texasStateDocument TexasStateDocument
     * @author vn55306
     */
    public static void correctTexasStateDocumentKeyForD6(TexasStateDocument texasStateDocument){
        texasStateDocument.setWicCatId(String.valueOf(Long.valueOf(texasStateDocument.getWicCatId())));
        texasStateDocument.setWicSubCatId(String.valueOf(Long.valueOf(texasStateDocument.getWicSubCatId())));
        texasStateDocument.setId(TexasStateDocument.generateId(texasStateDocument.getIdCode(),texasStateDocument.getSequenceNumber(),texasStateDocument.getWicCatId(),texasStateDocument.getWicSubCatId()));
    }
    /**
     * convert WicCategory To WicCategoryDocument
     *
     * @param productScanCodeWics List<BaseVO>
     * @return List<ProductScanCodeWicDocument>
     * @author vn55306
     */
    public static List<BaseVO> convertProductScanCodeWicToProductScanCodeWicVOtoDelete(List<ProductScanCodeWicDocument> productScanCodeWics){
        List<BaseVO> productScanCodeWicVOs = null;
        ProductScanCodeWicVO productScanCodeWicVO;
       if(productScanCodeWics!=null){
           productScanCodeWicVOs = new ArrayList<>();
            for(ProductScanCodeWicDocument productScanCodeWic:productScanCodeWics){
                productScanCodeWicVO = new ProductScanCodeWicVO();
                productScanCodeWicVO.setScnCdId(productScanCodeWic.getUpc());
                productScanCodeWicVO.setWicAplId(productScanCodeWic.getWicAplId());
                productScanCodeWicVO.setWicCatId(productScanCodeWic.getWicCategoryId());
                productScanCodeWicVO.setWicSubCatId(productScanCodeWic.getWicSubCategoryId());
                productScanCodeWicVO.setLstUpdtUid(WicConstants.TXSTATE_USER);
                productScanCodeWicVO.setLstUpdtTs(WicUtil.getCurrentTimeStamp());
                productScanCodeWicVO.setActionCode(productScanCodeWic.getAction());
                productScanCodeWicVO.setSystemEnvironment(WicConstants.BATCH_ENVIRONMENT);
                productScanCodeWicVOs.add(productScanCodeWicVO);
            }
        }
        return productScanCodeWicVOs;
    }
    /**
     * Write TexasState file into csv file.
     *
     * @param texasStateDocuments List<TexasStateDocument>
     * @param path String
     * @author vn55306
     */
    public static void createCsvTexasStateDocument(List<TexasStateDocument> texasStateDocuments,String path) {
        if(texasStateDocuments!=null && !texasStateDocuments.isEmpty()) {
            try (FileWriter pw = new FileWriter(path, true)) {
                StringBuilder content = new StringBuilder();
                for (TexasStateDocument texasStateDocument : texasStateDocuments) {
                    content = content.append(texasStateDocument.getId()).append(WicConstants.DELIMITER)
                             .append(texasStateDocument.getAplPreFix()).append(WicConstants.DELIMITER)
                             .append(texasStateDocument.getWicAplId()).append(WicConstants.DELIMITER)
                             .append(texasStateDocument.getScnCdId()).append(WicConstants.DELIMITER)
                             .append(texasStateDocument.getUpcCheckDigit()).append(WicConstants.DELIMITER)
                             .append(texasStateDocument.getWicCatId()).append(WicConstants.DELIMITER)
                             .append(texasStateDocument.getWicSubCatId())
                             .append("\n");
                }
                pw.write(content.toString());
                pw.flush();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * Write TexasState file into csv file.
     *
     * @param productScanCodeVOs List<BaseVO>
     * @param productScanCodeWicDocuments List<ProductScanCodeWicDocument>
     * @author vn55306
     */
    public static List<ProductScanCodeWicDocument> getProductScanCodeUpdates(List<BaseVO> productScanCodeVOs,List<ProductScanCodeWicDocument> productScanCodeWicDocuments){
        List<ProductScanCodeWicDocument> productScanCodeUpdates = new ArrayList<>();
        ProductScanCodesVO productScanCodesVO;
        for(BaseVO baseVO:productScanCodeVOs){
            productScanCodesVO =(ProductScanCodesVO)baseVO;
            for(ProductScanCodeWicDocument productScanCodeWicDocument:productScanCodeWicDocuments){
                if(productScanCodesVO.getScnCdId() == productScanCodeWicDocument.getUpc().longValue()){
                    productScanCodeUpdates.add(productScanCodeWicDocument);
                    break;
                }
            }
        }
        return productScanCodeUpdates;
    }
    /**
     * Write TexasState file into csv file.
     *
     * @param productScanCodeWicDocument List<ProductScanCodeWicDocument> productScanCodeWicDocuments
     * @param productScanCodeWicDocuments List<ProductScanCodeWicDocument>
     * @author vn55306
     */
    public static boolean checkProductScanCodeUpdate(Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments, ProductScanCodeWicDocument productScanCodeWicDocument){
        boolean flag = true;
        if(WicConstants.YES.equals(productScanCodeWicDocument.getWicSw()) && !productScanCodeWicDocuments.isEmpty()
                    && productScanCodeWicDocuments.containsKey(productScanCodeWicDocument.getId())){
            flag = false;
        }
        return flag;
    }
    public static boolean checkStageEventTrigger(TexasStateDocument item){
        boolean flag = false;
        Date currentDate = Calendar.getInstance().getTime();
        Date tomorrowDate = DateUtils.addDays(currentDate,1);
        Date effectDate = WicUtil.convertToDate(WicUtil.getDateOrDefault(item.getEffDt()));
        Date endDate = WicUtil.convertToDate(WicUtil.getDateOrDefault(item.getEndDt()));
        if(DateUtils.isSameDay(currentDate,effectDate) || DateUtils.isSameDay(currentDate,endDate)
                || DateUtils.isSameDay(tomorrowDate,effectDate)  || DateUtils.isSameDay(tomorrowDate,endDate)){
            flag = true;
        }
        return flag;
    }
    /**
     * Date conversion from String
     *
     * @param sDate the String of Date
     * @return SQL Date
     */
    public static Date convertToDate(String sDate) {
        try {
            String convertDate = WicUtil.getDateOrDefault(sDate);
            return new SimpleDateFormat(DEFAULT_FORMAT_DATE).parse(convertDate);
        } catch (ParseException e) {
            return null;
        }
    }
}
