/*
 *  AssortmentValidator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.assortment;


import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.entity.GoodsProduct;
import com.heb.pm.repository.GoodsProductRepository;
import com.heb.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Holds all validate of assortment Batch Upload.
 *
 * @author vn55306
 * @since 2.7.0
 */
@Component
public class AssortmentValidator extends AbstractBatchUploadValidator {
	private static final Logger logger = LoggerFactory.getLogger(AssortmentValidator.class);
	private static final String STRING_ERROR_END_DATE_BEFORE_TOMOROW = "End Date must be Greater than the Current Date.";
	private static final String STRING_ERROR_END_DATE_BEFORE_START_DATE = "EndDate must be greater than StartDate.";
	private static final String STRING_ERROR_INVALID_START_DATE = "Invalid Start Date.";
	private static final String STRING_ERROR_INVALID_END_DATE = "Invalid End Date.";
	private static final String STRING_ERROR_INVALID_MIN_CUSTOMER_ORDER_QTY_NUMBER = "Invalid Min Customer Order Qty Number.";
	private static final String STRING_ERROR_MIN_LESS_THAN_MAX_CUSTOMER_ORDER_QTY_NUMBER = "Min Customer Order Quantity must be < Max Customer Order Quantity.";
	private static final String STRING_ERROR_INVALID_MIN_ORDER_QTY_DISOUNT_COMBINE = "Invalid Min Order Qty Discount combination.";
	private static final String STRING_ERROR_OUNT_OF_RANGE_MIN_ORDER_QTY_DISCONT = "Out of range Min Order Qty Discount";
	private static final String STRING_ERROR_INVALID_MIN_ORDER_QTY_DISCOUNT = "Invalid Min Order Qty Discount";
	private static final String STRING_ERROR_INVALID_MIN_ORDER_QTY_DISCOUNT_VALUE = "Invalid Min Order Qty Discount Value";
	private static final String STRING_ERROR_START_DATE_BEFORE_TOMOROW="Start Date must be Greater than the Current Date.";

	@Autowired
	private GoodsProductRepository goodsProductRepository;
	AssortmentBatchUpload assortment;
	private GoodsProduct goodsProduct;
	private Double maxCustomerOrderQty;
	private Double minCustomerOrderQty;
	@Override
	public void validateRow(BatchUpload batchUpload){
		assortment = (AssortmentBatchUpload) batchUpload;
		goodsProduct = null;
		maxCustomerOrderQty= null;
		minCustomerOrderQty = null;
		this.runValidation();
	}
	/**
	 * Validate data from file upload.
	 * @author vn55306
	 */
	private void runValidation() {
		this.validateProductId();
		this.validateFulfillmentChannel();
		this.validateMinCustomerOrderQty();
		this.validateDiscountThreshold();
	}
	/**
	 * Validate Product Id.
	 * @exception UnexpectedInputException
	 * @author vn55306
	 */
	private void validateProductId() {
		if(StringUtils.isBlank(this.assortment.getProductId())){
			this.assortment.getErrors().add(AssortmentBatchUpload.STRING_PRODUCT_ID+ERROR_MANDATORY_FIELD);
			throw new UnexpectedInputException(AssortmentBatchUpload.STRING_PRODUCT_ID+ERROR_MANDATORY_FIELD);
		} else {
			if (!this.isInteger(this.assortment.getProductId())) {
				this.assortment.getErrors().add(ERROR_INVALID_PRODUCT_ID.concat(this.assortment.getProductId()));
				throw new UnexpectedInputException(ERROR_INVALID_PRODUCT_ID.concat(this.assortment.getProductId()));
			} else if(this.getGoodsProduct()==null){
				this.assortment.getErrors().add(ERROR_INVALID_PRODUCT_ID.concat(this.assortment.getProductId()));
				throw new UnexpectedInputException(ERROR_INVALID_PRODUCT_ID.concat(this.assortment.getProductId()));
			}
		}
	}
	/**
	 * validate Fulfillment Channel.
	 * @author vn55306
	 */
	private void validateFulfillmentChannel() {
		if (StringUtils.isBlank(this.assortment.getFulfillmentProgram())) {
			return;
	}
		this.validateEffectiveDates();
	}
	/**
	 * validate Effective Date.
	 * @author vn55306
	 */
	private void validateEffectiveDates() {
		this.validateEffectiveStartDate();
		this.validateEffectiveEndDate();
		this.validateEffectiveStartEndDate();
	}
	/**
	 * Return The minCustomerOrderQty.
	 * @return The minCustomerOrderQty.
	 * @author vn55306
	 */
	private void validateEffectiveStartDate() {
		String effectiveDate = this.assortment.getEffectiveStartDate();
		if (StringUtils.isNotBlank(effectiveDate) && DateUtils.getLocalDate(effectiveDate,DFLT_DATE_FORMAT)==null) {
			this.assortment.getErrors().add(STRING_ERROR_INVALID_START_DATE);
			return;
		}
		if (!this.isValidEffectiveStartDate(effectiveDate)) {
			this.assortment.getErrors().add(STRING_ERROR_START_DATE_BEFORE_TOMOROW);
		}
	}
	/**
	 * validate Effective End Date.
	 * @author vn55306
	 */
	private void validateEffectiveEndDate() {
		String effectiveDate = this.assortment.getEffectiveEndDate();
		if (StringUtils.isNotBlank(effectiveDate)  && DateUtils.getLocalDate(effectiveDate,DFLT_DATE_FORMAT)==null) {
			this.assortment.getErrors().add(STRING_ERROR_INVALID_END_DATE);
			return;
		}
		if (!this.isValidEffectiveEndDate(effectiveDate)) {
			this.assortment.getErrors().add(STRING_ERROR_END_DATE_BEFORE_TOMOROW);
		}
	}
	/**
	 * validate Effective Start and End Date.
	 * @author vn55306
	 */
	private void validateEffectiveStartEndDate() {
		if (!this.isValidStartEndDate(this.assortment.getEffectiveStartDate(), this.assortment.getEffectiveEndDate())) {
			this.assortment.getErrors().add(STRING_ERROR_END_DATE_BEFORE_START_DATE);
		}
	}
	/**
	 * validate Min Customer Order Qty.
	 * @author vn55306
	 */
	private void validateMinCustomerOrderQty() {
		String minCustomerOrderQty = this.assortment.getMinCustomerOrderQuantity();
		if (StringUtils.isBlank(minCustomerOrderQty)) {
			return;
		}
		if (!this.isInteger(minCustomerOrderQty)) {
			this.assortment.getErrors().add(STRING_ERROR_INVALID_MIN_CUSTOMER_ORDER_QTY_NUMBER);
			return;
		}
		if (!(Integer.parseInt(minCustomerOrderQty) < this.getMaxCustomerOrderQty())) {
			this.assortment.getErrors().add(STRING_ERROR_MIN_LESS_THAN_MAX_CUSTOMER_ORDER_QTY_NUMBER);
		}
	}
	/**
	 * validate Discount Threshold.
	 * @author vn55306
	 */
	private void validateDiscountThreshold() {
		if(!this.isDiscountThresholdCombine(this.assortment)) {
			this.assortment.getErrors().add(STRING_ERROR_INVALID_MIN_ORDER_QTY_DISOUNT_COMBINE);
			return;
		}
		this.validateMinCustomerOrderQtyFor();
		this.validateMinCustomerOrderQtyValue();
	}
	/**
	 * validate Min Customer Order Qty For.
	 * @author vn55306
	 */
	private void validateMinCustomerOrderQtyFor() {
		if(StringUtils.isNotBlank(assortment.getMinOrderQuantityForDiscount())){
			Integer  minOrderQtyDiscountFor = parseNumber(assortment.getMinOrderQuantityForDiscount(),Integer.class);
			if (minOrderQtyDiscountFor != null && minOrderQtyDiscountFor > 0) {
				if (minOrderQtyDiscountFor > this.getMaxCustomerOrderQty() || minOrderQtyDiscountFor < this.getMinCustomerOrderQty()) {
					assortment.getErrors().add(STRING_ERROR_OUNT_OF_RANGE_MIN_ORDER_QTY_DISCONT);
				}
			} else {
				assortment.getErrors().add(STRING_ERROR_INVALID_MIN_ORDER_QTY_DISCOUNT);
			}
		}
	}
	/**
	 * validate Min Customer Order Qty Value.
	 * @author vn55306
	 */
	private void validateMinCustomerOrderQtyValue() {
		if(StringUtils.isNotBlank(assortment.getMinOrderQuantityDiscountValue())) {
			Integer minOrderQtyDiscountValue = this.parseNumber(assortment.getMinOrderQuantityDiscountValue(), Integer.class);
			if (minOrderQtyDiscountValue == null || minOrderQtyDiscountValue <= 0) {
				assortment.getErrors().add(STRING_ERROR_INVALID_MIN_ORDER_QTY_DISCOUNT_VALUE);
			}
		}
	}
	/**
	 * Return The GoodsProduct.
	 * @return The GoodsProduct.
	 * @author vn55306
	 */
	private GoodsProduct getGoodsProduct() {
		if (this.goodsProduct == null) {
			this.goodsProduct = this.goodsProductRepository.findOne(parseNumber(this.assortment.getProductId(), Long.class));
		}
		return this.goodsProduct;
	}
	/**
	 * Return The maxCustomerOrderQty.
	 * @return The maxCustomerOrderQty.
	 * @author vn55306
	 */
	private Double getMaxCustomerOrderQty() {
		if (this.maxCustomerOrderQty == null && this.getGoodsProduct() != null) {
			this.maxCustomerOrderQty = this.getGoodsProduct().getMaxCustomerOrderQuantity();
		}
		return this.maxCustomerOrderQty;
	}
	/**
	 * Return The minCustomerOrderQty.
	 * @return The minCustomerOrderQty.
	 * @author vn55306
	 */
	private Double getMinCustomerOrderQty() {
		if (this.minCustomerOrderQty == null && getGoodsProduct() != null) {
			Double oldMinCustomerOrderQty = getGoodsProduct().getMinCustomerOrderQuantity();
			Double newMinCustomerOrderQty = parseNumber(this.assortment.getMinCustomerOrderQuantity(), Double.class);
			this.minCustomerOrderQty = newMinCustomerOrderQty != null ? newMinCustomerOrderQty : oldMinCustomerOrderQty;
		}
		return this.minCustomerOrderQty;
	}


	/**
	 * Check business rule of Candidate Discount Threshold.
	 * @param assortmentBatchUpload
	 *            the AssortmentBatchUpload
	 * @return true, if successful
	 * @author vn55306
	 */
	private boolean isDiscountThresholdCombine(AssortmentBatchUpload assortmentBatchUpload) {
		boolean flagCheck = false;
		if (StringUtils.isBlank(assortmentBatchUpload.getMinOrderQuantityForDiscount()) && StringUtils.isBlank(assortmentBatchUpload.getMinOrderQuantityDiscountType())
				&& StringUtils.isBlank(assortmentBatchUpload.getMinOrderQuantityDiscountValue())) {
			flagCheck = true;
		} else if (StringUtils.isNotBlank(assortmentBatchUpload.getMinOrderQuantityForDiscount()) && StringUtils.isNotBlank(assortmentBatchUpload.getMinOrderQuantityDiscountType())
				&& StringUtils.isNotBlank(assortmentBatchUpload.getMinOrderQuantityDiscountValue())) {
			flagCheck = true;
		}
		return flagCheck;
	}
	/**
	 * Validate File Upload.
	 * @param data
	 *            the byte[]
	 * @throws UnexpectedInputException
	 *        The UnexpectedInputException
	 * @author vn55306
	 */
	public void validateTemplate(byte[] data) throws UnexpectedInputException{
			try {
				InputStream inputStream = new ByteArrayInputStream(data);
				Workbook workBook = new XSSFWorkbook(inputStream);
				int numberOfSheets = workBook.getNumberOfSheets();
				if (numberOfSheets > 0) {
					Row row = workBook.getSheetAt(0).getRow(0);
					String header;
					for (short columnCounter = 0; columnCounter < row.getLastCellNum(); columnCounter++) {
						header = getValueOfCell(row.getCell(columnCounter));
						header= header !=null ? header.trim():StringUtils.EMPTY;
						switch (columnCounter) {
							case AssortmentBatchUpload.POSITION_COLUMN_PRODUCT_ID: {
								if(!AssortmentBatchUpload.STRING_PRODUCT_ID.equals(header))
									throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
								break;
							}
							case AssortmentBatchUpload.POSITION_COLUMN_FULFILLMENT_PROGRAM: {
								if(!AssortmentBatchUpload.STRING_FULFILLMENT_PROGRAM.equals(header))
									throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
								break;
							}
							case AssortmentBatchUpload.POSITION_COLUMN_EFFECTIVE_START_DATE: {
								if(!AssortmentBatchUpload.STRING_EFFECTIVE_START_DATE.equals(header))
									throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
								break;
							}
							case AssortmentBatchUpload.POSITION_COLUMN_EFFECTIVE_END_DATE: {
								if(!AssortmentBatchUpload.STRING_EFFECTIVE_END_DATE.equals(header))
									throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
								break;
							}
							case AssortmentBatchUpload.POSITION_COLUMN_MIN_CUSTOMER_ORDER_QUANTITY: {
								if(!AssortmentBatchUpload.STRING_MIN_CUSTOMER_ORDER_QUANTITY.equals(header))
									throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
								break;
							}
							case AssortmentBatchUpload.POSITION_COLUMN_MIN_ORDER_QUANTITY_FOR_DISCOUNT: {
								if(!AssortmentBatchUpload.STRING_MIN_ORDER_QUANTITY_FOR_DISCOUNT.equals(header))
									throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
								break;
							}
							case AssortmentBatchUpload.POSITION_COLUMN_MIN_ORDER_QUANTITY_DISCOUNT_TYPE: {
								if(!AssortmentBatchUpload.STRING_MIN_ORDER_QUANTITY_DISCOUNT_TYPE.equals(header))
									throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
								break;
							}
							case AssortmentBatchUpload.POSITION_COLUMN_MIN_ORDER_QUANTITY_DISCOUNT_VALUE: {
								if(!AssortmentBatchUpload.STRING_MIN_ORDER_QUANTITY_DISCOUNT_VALUE.equals(header))
									throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
								break;
							}
						}
					}

				}
			}catch (Exception e) {
				logger.error("error:",e);
				throw new UnexpectedInputException(AssortmentValidator.ERROR_FILE_WRONG_FORMAT);
			}

	}

}
