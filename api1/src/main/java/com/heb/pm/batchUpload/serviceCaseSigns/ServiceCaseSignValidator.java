/*
 *  ServiceCaseSignValidator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.serviceCaseSigns;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.entity.GoodsProduct;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.GoodsProductRepository;
import com.heb.pm.repository.SellingUnitRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.heb.pm.batchUpload.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Holds all validate of Wine Batch Upload.
 *
 * @author vn55306
 * @since 2.7.0
 */
@Component
public class ServiceCaseSignValidator extends AbstractBatchUploadValidator {
	private static final Logger logger = LoggerFactory.getLogger(ServiceCaseSignValidator.class);
	/**
	 * PRIMO_PRICK_STATUS_SUBMITTED.
	 */
	public static final String PRIMO_PRICK_STATUS_SUBMITTED = "SUBMITTED";

	/**
	 * PRIMO_PRICK_STATUS_REJECED.
	 */
	public static final String PRIMO_PRICK_STATUS_REJECED = "REJECTED";

	/**
	 * PRIMO_PRICK_STATUS_APPROVED.
	 */
	public static final String PRIMO_PRICK_STATUS_APPROVED = "APPROVED";
	private static final String ERROR_PROPOSED_SERVICE_CASE_SIGN_DESCRIPTION_MADATORY_STATUS_SUBMITTED ="Proposed Service Case Sign Description should be mandatory when Service Case Status is Submitted.";
	private static final String ERROR_APPROVED_SERVICE_CASE_SIGN_DESCRIPTION_MADATORY_STATUS_APPROVED ="Approved Service Case Sign Description should be mandatory when Service Case Status is Approved.";
	private static final String ERROR_APPROVED_SERVICE_CASE_SIGN_DESCRIPTION_BLANK_STATUS_REJECTED ="Approved Service Case Sign Description should be blank when Service Case Status is Rejected.";
	private static final String ERROR_SERVICE_CASE_SIGN_STATUS ="The Service Case Status should be in the file template.";
	public static final String ERROR_INVALID_UPC = "Invalid Upc ";

	private static final int DEFAULT_SHEET = 0;
	private static final int HEADER_ROW = 3;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	private ServiceCaseSignBatchUpload serviceCaseSignBatchUpload;
	private SellingUnit sellingUnit;
	@Override
	public void validateRow(BatchUpload batchUpload) {
		serviceCaseSignBatchUpload = (ServiceCaseSignBatchUpload)batchUpload;
		sellingUnit = null;
		this.runValidation();
	}
	/**
	 * Validate data from file upload.
	 * @author vn55306
	 */
	private void runValidation() {
		validateUpc();
		this.validateServiceCaseSignDescription();

	}


	/**
	 * Validate data from file upload.
	 * @author vn55306
	 */
	private void validateUpc() {
		if(!isValidUpc()){
			throw new UnexpectedInputException(ERROR_INVALID_UPC.concat(this.serviceCaseSignBatchUpload.getUpc()));
		}
	}

	/**
	 * check if the upc is valid or not.
	 * @return boolean
	 * @author vn55306
	 */
	private boolean isValidUpc() {
		if(StringUtils.isNotBlank(this.serviceCaseSignBatchUpload.getUpc()) &&  this.isUpc(this.serviceCaseSignBatchUpload.getUpc()) && this.getSellingUnit()
				!=null){
			 return true;
		}else{
			return false;
		}

	}
	/**
	 * Return The SellingUnit.
	 * @return The SellingUnit.
	 * @author vn55306
	 */
	protected SellingUnit getSellingUnit() {
		if(sellingUnit==null){
			sellingUnit = this.sellingUnitRepository.findOne(Long.valueOf(this.serviceCaseSignBatchUpload.getUpc()));
		}
		return sellingUnit;
	}

	/**
	 * Validate Service Case Sign Description.
	 * @author vn55306
	 */
	private void validateServiceCaseSignDescription(){
		if(StringUtils.isNotBlank(this.serviceCaseSignBatchUpload.getServiceCaseSignStatus())){
			if(this.serviceCaseSignBatchUpload.getServiceCaseSignStatus().trim().equalsIgnoreCase(PRIMO_PRICK_STATUS_SUBMITTED)){
				if(StringUtils.isBlank(this.serviceCaseSignBatchUpload.getProposedServiceCaseSign())) {
					this.serviceCaseSignBatchUpload.getErrors().add(ServiceCaseSignValidator.ERROR_PROPOSED_SERVICE_CASE_SIGN_DESCRIPTION_MADATORY_STATUS_SUBMITTED);
				}
			}else if(this.serviceCaseSignBatchUpload.getServiceCaseSignStatus().trim().equalsIgnoreCase(PRIMO_PRICK_STATUS_APPROVED)){
					if(StringUtils.isBlank(this.serviceCaseSignBatchUpload.getApprovedServiceCaseSign())) {
						this.serviceCaseSignBatchUpload.getErrors().add(ServiceCaseSignValidator.ERROR_APPROVED_SERVICE_CASE_SIGN_DESCRIPTION_MADATORY_STATUS_APPROVED);
					}
			}else if(this.serviceCaseSignBatchUpload.getServiceCaseSignStatus().trim().equalsIgnoreCase(PRIMO_PRICK_STATUS_REJECED)){
				if(StringUtils.isNotBlank(this.serviceCaseSignBatchUpload.getApprovedServiceCaseSign())){
					this.serviceCaseSignBatchUpload.getErrors().add(ServiceCaseSignValidator.ERROR_APPROVED_SERVICE_CASE_SIGN_DESCRIPTION_BLANK_STATUS_REJECTED);
				}
			} else {
				this.serviceCaseSignBatchUpload.getErrors().add(ServiceCaseSignValidator.ERROR_SERVICE_CASE_SIGN_STATUS);
			}
		}else{
			this.serviceCaseSignBatchUpload.getErrors().add(ServiceCaseSignBatchUpload.SERVICE_CASE_STATUS+AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD);
		}
	}
	public void validateTemplate(byte[] data) throws UnexpectedInputException{
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			Workbook workBook = new XSSFWorkbook(inputStream);
			int numberOfSheets = workBook.getNumberOfSheets();
			if (numberOfSheets > 0) {
				Row rowHeader = workBook.getSheetAt(DEFAULT_SHEET).getRow(HEADER_ROW);
				String header;
				for (int columnCounter = 0; columnCounter < rowHeader.getLastCellNum(); columnCounter++) {
					header = this.getValueOfCell(rowHeader.getCell(columnCounter));
					header= header !=null ? header.trim():StringUtils.EMPTY;
					if(ServiceCaseSignBatchUpload.uploadFileHeader.containsKey(columnCounter)){
						if(!ServiceCaseSignBatchUpload.uploadFileHeader.get(columnCounter).equalsIgnoreCase(header)){
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						}
					}
				}
			}else{
				throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
			}
		}catch (Exception e) {
			throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
		}

	}
}
