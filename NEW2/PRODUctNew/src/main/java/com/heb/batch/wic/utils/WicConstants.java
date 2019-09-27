/*
 *  com.heb.batch.wic.utils.WicConstants
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */

package com.heb.batch.wic.utils;

/**
 * This is WicConstants class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public final class WicConstants {
    /**
     * Private so the class cannot be instantiated.
     */
    private WicConstants() {}
    /**
     * The ID_CODE.
     */
    public static final String ID_CODE = "ID-CODE";
    /**
     * The SEQUENCE_NUMBER.
     */
    public static final String SEQUENCE_NUMBER = "SEQUENCE-NBR";
    /**
     * The MESSAGE_ID.
     */
    public static final String MESSAGE_ID = "MESSAGE-ID";
    /**
     * The UPC_KEY.
     */
    public static final String UPC_KEY = "UPC-KEY";
    /**
     * The APL_PREFIX.
     */
    public static final String APL_PREFIX = "APL-PREFIX";
    /**
     * The CHECK_DIGIT.
     */
    public static final String CHECK_DIGIT = "CHECK-DIGIT";
    /**
     * The DESCRIPTION.
     */
    public static final String DESCRIPTION = "DESC";
    /**
     * The CATEGORY_CODE.
     */
    public static final String CATEGORY_CODE = "CATEGORY-CD";
    /**
     * The CATEGORY_DESCRIPTION.
     */
    public static final String CATEGORY_DESCRIPTION = "CATEGORY-DESC";
    /**
     * The SUBCATEGORY_CODE.
     */
    public static final String SUBCATEGORY_CODE = "SUBCATEGORY-CD";
    /**
     * The SUBCATEGORY_DESCRIPTION.
     */
    public static final String SUBCATEGORY_DESCRIPTION = "SUBCATEGORY-DESC";
    /**
     * The UNIT_OF_MEASURE.
     */
    public static final String UNIT_OF_MEASURE = "UNIT-OF-MEASURE";
    /**
     * The PACKAGE_SIZE.
     */
    public static final String PACKAGE_SIZE = "PACKAGE-SIZE";
    /**
     * The BENEFIT_QUANTITY.
     */
    public static final String BENEFIT_QUANTITY = "BENEFIT-QTY";
    /**
     * The BENEFIT_UNIT.
     */
    public static final String BENEFIT_UNIT = "BENEFIT-UNIT";
    /**
     * The ITEM_PRICE.
     */
    public static final String ITEM_PRICE = "ITEM-PRICE";
    /**
     * The PRICE_TYPE.
     */
    public static final String PRICE_TYPE = "PRICE-TYPE";
    /**
     * The CARD_ID.
     */
    public static final String CARD_ID = "CARD-ID-CD";
    /**
     * The EFFECTIVE_DATE.
     */
    public static final String EFFECTIVE_DATE = "EFF-DATE";
    /**
     * The END_DATE.
     */
    public static final String END_DATE = "END-DATE";
    /**
     * PURCHASE-INDICATOR
     */
    public static final String PURCHASE_INDICATOR = "PURCHASE-INDICATOR";
    /**
     * UPC_PLU_LENGTH
     */
    public static final String UPC_PLU_LENGTH = "UPC-PLU-LENGTH";
    /**
     * MANUAL-VOUCHER-INDICATOR
     */
    public static final String MANUAL_VOUCHER_INDICATOR = "MANUAL-VOUCHER-INDICATOR";
    /**
     * BENEFIT_UNIT_DESCRIPTION
     */
    public static final String BENEFIT_UNIT_DESCRIPTION = "BENEFIT-UNIT-DESCRIPTION";
    /**
     * The User ID of WIC
     */
    public static final String WIC_USER = "J50X100D";
    /**
     * The User ID of TXSTATE
     */
    public static final String TXSTATE_USER = "TXSTATE";
    
	/**
	 * The Yes value of Wic Switch. 
	 */
    public static final String YES = "Y";
    
	/**
	 * The No value of Wic Switch. 
	 */
    public static final String NO = "N";
    
	/**
	 * The Action Code with Add value. 
	 */
    public static final String ADD = "A";
    
    /**
     * The Action Code with Update value. 
     */
    public static final String UPDATE = "U";
    
    /**
     * The Action Code with Delete value. 
     */
    public static final String DELETE = "D";
    /**
     * The DELIMITER value.
     */
    public static final String DELIMITER = "|%$|";
    /**
     * The FILE_DATA_REPORT value.
     */
    public static final String FILE_DATA_REPORT = "C:\\wic-data\\WIC_Report.xlsx";
    /**
     * The JOB_NAME value.
     */
    public static final String JOB_NAME = "J50X100D";
    /**
     * The TEXAS_STATE_NAME value.
     */
    public static final String TEXAS_STATE_NAME = "TRF.XCOM.WICTX.TXT";

    /**
     * Query in ProductScanCodeWicRepository.
     */
    public static final String FIND_BY_CREATE_USER_SQL ="select productScanCodeWic from ProductScanCodeWic productScanCodeWic where trim(productScanCodeWic.cre8UId)=:cre8UId";
    /**
     * The BATCH_ENVIRONMENT value.
     */
    public static final String BATCH_ENVIRONMENT = "BT";
}
