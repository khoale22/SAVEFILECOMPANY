/*
 *  WineValidator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.eCommerceAttribute;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.entity.Attribute;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.AttributeCodeRepository;
import com.heb.pm.repository.AttributeRepository;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all validate of Wine Batch Upload.
 *
 * @author vn55306
 * @since 2.7.0
 */
@Component
public class EcommerceAttributeValidator extends AbstractBatchUploadValidator {
	private static final Logger logger = LoggerFactory.getLogger(EcommerceAttributeValidator.class);
	public static final String ERROR_MULTIATTRIBUTE = "Please only use one of the listed Attributes on the template file.";
	private static final String ERROR_NO_MATCHES_FOUND_FOR_ENTERED_UPC = "No matches found for entered UPC ";
	private static final String ERROR_LIMIT_STRING_LENGTH =" length must be less than or equal 10000 characters.";
	private static final String ERROR_IMAGE_SOURCE_COMBINE_IMAGE_URL_BLANK= "Image Source should be blank when Image Url is blank.";
	private static final String ERROR_IMAGE_SOURCE_COMBINE_IMAGE_URL_NOT_BLANK="Image Source is required when Image Url is entered.";
	private static final String ERROR_ATRIBUTE_ID="Error getting Attribute ID for value: ";
	public static final String ERROR_INVALID_UPC = "Invalid Upc ";
	public static final String ERROR_INVALID_PRODUCT_ID = "Invalid ProductId ";
	/** The Constants int DYNAMIC_COLUMN_START. */
	public static final int BIG_FIVE_DYNAMIC_COLUMN_START = 10;
	/** The Constants int BIG_FIVE_MAX_ALLOWABLE_COLUMNS. */
	public static final int  BIG_FIVE_MAX_ALLOWABLE_COLUMNS = 26;
	@Autowired
	private AttributeCodeRepository attributeCodeRepository;
	@Autowired
	private SellingUnitRepository sellingUnitRepository;
	@Autowired
	public AttributeRepository attributeRepository;

	private  EcomBigFiveBatchUpload ecomBigFiveBatchUpload;
	private SellingUnit sellingUnit;

	private List<ProductAttribute> productAttributes;
	@Override
	public void validateRow(BatchUpload batchUpload) {
		 ecomBigFiveBatchUpload =(EcomBigFiveBatchUpload)batchUpload;
		sellingUnit = null;
		this.runValidation();

	}
	/**
	 * Validate data from file upload.
	 * @author vn55306
	 */
	private void runValidation() {
		this.validateUpc();
		this.validateProductScanExtendLimit();
		this.validateImageUrl();
		this.validateImageUrlImageSourceCombine();
		this.validateAtributeCode();
	}
	/**
	 * Validate Upc.
	 * @author vn55306
	 */
	private void validateUpc() {
		if(StringUtils.isBlank(this.ecomBigFiveBatchUpload.getUpcPlu())){
			this.ecomBigFiveBatchUpload.getErrors().add(EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_UPC) + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD);
			throw new UnexpectedInputException(EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_UPC) + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD);
		} else {
			if (!this.isUpc(this.ecomBigFiveBatchUpload.getUpcPlu())) {
				this.ecomBigFiveBatchUpload.getErrors().add(ERROR_INVALID_UPC.concat(this.ecomBigFiveBatchUpload.getUpcPlu()));
				throw new UnexpectedInputException(ERROR_INVALID_UPC.concat(this.ecomBigFiveBatchUpload.getUpcPlu()));
			} else if(getSellingUnit()==null){
				this.ecomBigFiveBatchUpload.getErrors().add(EcommerceAttributeValidator.ERROR_NO_MATCHES_FOUND_FOR_ENTERED_UPC+this.ecomBigFiveBatchUpload.getUpcPlu());
				throw new UnexpectedInputException(EcommerceAttributeValidator.ERROR_NO_MATCHES_FOUND_FOR_ENTERED_UPC+this.ecomBigFiveBatchUpload.getUpcPlu());
			}
		}
	}
	/**
	 * Validate Product Scan Extend description Limit.
	 * @author vn55306
	 */
	private void validateProductScanExtendLimit(){
		if(StringUtils.isNotBlank(ecomBigFiveBatchUpload.getDisplayName()) && !isStringLimit(ecomBigFiveBatchUpload.getDisplayName().trim(),10000)){
			ecomBigFiveBatchUpload.getErrors().add(EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_DISPLAY_NAME).concat(EcommerceAttributeValidator.ERROR_LIMIT_STRING_LENGTH));
		}
		if(StringUtils.isNotBlank(ecomBigFiveBatchUpload.getRomanceCopy()) && !isStringLimit(ecomBigFiveBatchUpload.getRomanceCopy().trim(),10000)){
			ecomBigFiveBatchUpload.getErrors().add(EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_ROMANCE_COPY).concat(EcommerceAttributeValidator.ERROR_LIMIT_STRING_LENGTH));
		}
	}
	/**
	 * Validate Image Url.
	 * @author vn55306
	 */
	private void validateImageUrl(){
		if(StringUtils.isNotBlank(ecomBigFiveBatchUpload.getImageUrl()) && !this.isValidURL(ecomBigFiveBatchUpload.getImageUrl().trim())){
			ecomBigFiveBatchUpload.getErrors().add(EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_IMAGE_URL).concat(" for UPC ").concat(ecomBigFiveBatchUpload.getUpcPlu()).concat(" is not valid."));
		}
	}
	/**
	 * Validate Image Url combine Image Source.
	 * @author vn55306
	 */
	private void validateImageUrlImageSourceCombine(){
		if(StringUtils.isBlank(ecomBigFiveBatchUpload.getImageUrl()) && StringUtils.isNotBlank(ecomBigFiveBatchUpload.getImageSource())){
			ecomBigFiveBatchUpload.getErrors().add(EcommerceAttributeValidator.ERROR_IMAGE_SOURCE_COMBINE_IMAGE_URL_BLANK);
		} else if(StringUtils.isNotBlank(ecomBigFiveBatchUpload.getImageUrl()) && StringUtils.isBlank(ecomBigFiveBatchUpload.getImageSource())){
			ecomBigFiveBatchUpload.getErrors().add(EcommerceAttributeValidator.ERROR_IMAGE_SOURCE_COMBINE_IMAGE_URL_NOT_BLANK);
		}
	}
	/**
	 * Validate Atribute Name.
	 * @author vn55306
	 */
	private void validateAtributeName(ProductAttribute productAttribute){
		List<Attribute> attributes = null;
		attributes = this.attributeRepository.findByAttributeNameEquals(productAttribute.getAttrNm());
		if(attributes!=null && !attributes.isEmpty()){
			productAttribute.setAttrId(String.valueOf(attributes.get(0).getAttributeId()));
			productAttribute.setAttrDomainCode(attributes.get(0).getAttributeDomainCode());
			productAttribute.setAttrValLstSw(attributes.get(0).getAttributeValueList());
			productAttribute.setAttrNm(attributes.get(0).getAttributeName());
		} else {
			throw new UnexpectedInputException(ERROR_MULTIATTRIBUTE);
		}
	}
	/**
	 * Validate Atribute Code.
	 * @author vn55306
	 */
	private void validateAtributeCode(){
		if(!this.ecomBigFiveBatchUpload.getAttributeList().isEmpty()){
			for(ProductAttribute productAttribute:this.ecomBigFiveBatchUpload.getAttributeList()){
				if(productAttribute.isAttrValLstSw()){
					try{
						productAttribute.setAttrCdId(attributeCodeRepository.findAttributeCodeByAttributeCodeValue(productAttribute.getTextValue()).getAttributeCodeId());
					} catch (Exception e){
						this.ecomBigFiveBatchUpload.getErrors().add(ERROR_ATRIBUTE_ID.concat(productAttribute.getTextValue()));
					}
				}else if(EcomBigFiveBatchUpload.DOMAIN_CODE_I.equals(this.getTrimmedValue(productAttribute.getAttrDomainCode()))
						|| EcomBigFiveBatchUpload.DOMAIN_CODE_DEC.equals(this.getTrimmedValue(productAttribute.getAttrDomainCode()))){
					try {
						productAttribute.setNumberValue(Double.valueOf(productAttribute.getTextValue()));
					} catch (NumberFormatException e){
						this.ecomBigFiveBatchUpload.getErrors().add("Error parsing ".concat(productAttribute.getTextValue()).concat(" to number."));

					}
				}
			}
		}
	}
	/**
	 * Return The SellingUnit.
	 * @return The SellingUnit.
	 * @author vn55306
	 */
	private SellingUnit getSellingUnit() {
		if(sellingUnit==null){
			sellingUnit = sellingUnitRepository.findOne(Long.valueOf(this.ecomBigFiveBatchUpload.getUpcPlu()));
		}
		return sellingUnit;
	}

	/**
	 * validate Template file upload.
	 * @param data
	 * 				byte[]
	 * @return boolean
	 * @author vn55306
	 */
	public void validateTemplate(byte[] data) throws UnexpectedInputException{
		try {
		InputStream inputStream = new ByteArrayInputStream(data);
		Workbook workBook = new XSSFWorkbook(inputStream);
		int numberOfSheets = workBook.getNumberOfSheets();
		if (numberOfSheets > 0) {
			Row rowHeader = workBook.getSheetAt(0).getRow(0);
			productAttributes = new ArrayList<ProductAttribute>();
			String header;
			for (int columnCounter = 0; columnCounter < rowHeader.getLastCellNum(); columnCounter++) {
				if(columnCounter == BIG_FIVE_MAX_ALLOWABLE_COLUMNS){
					break;
				}
				header = this.getTrimmedValue(this.getValueOfCell(rowHeader.getCell(columnCounter)));
				switch (columnCounter) {
					case EcomBigFiveBatchUpload.COL_POS_UPC: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_UPC) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_UPC).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_BRAND: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_BRAND) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_BRAND).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_SIZE: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_SIZE) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_SIZE).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_DISPLAY_NAME: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_DISPLAY_NAME) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_DISPLAY_NAME).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_ROMANCE_COPY: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_ROMANCE_COPY) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_ROMANCE_COPY).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_DIRECTIONS: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_DIRECTIONS) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_DIRECTIONS).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_INGREDIENTS: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_INGREDIENTS) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_INGREDIENTS).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_WARNINGS: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_WARNINGS) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_WARNINGS).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_IMAGE_URL: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_IMAGE_URL) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_IMAGE_URL).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					case EcomBigFiveBatchUpload.COL_POS_IMAGE_SOURCE: {
						if(EcomBigFiveBatchUpload.uploadFileHeader.containsKey(EcomBigFiveBatchUpload.COL_POS_IMAGE_SOURCE) && !EcomBigFiveBatchUpload.uploadFileHeader.get(EcomBigFiveBatchUpload.COL_POS_IMAGE_SOURCE).equalsIgnoreCase(header))
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						break;
					}
					default: {//it is a dynamic column
						//check if attrList isn't empty and large enough to have another value
						if(StringUtils.isNotBlank(header) && columnCounter>=BIG_FIVE_DYNAMIC_COLUMN_START){
							ProductAttribute productAttribute = new ProductAttribute();
							productAttribute.setAttrNm(header);
							this.validateAtributeName(productAttribute);
							productAttributes.add(productAttribute);
						}
						break;
					}
				}
			}
		}
		}catch (Exception e) {
				throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
		}
	}

	/**
	 * get Prodcut Attribute list
	 * @return list of ProductAttribute
	 * @author vn55306
	 */
	public List<ProductAttribute> getProductAttributes() {
		return productAttributes;
	}
}
