/*
 * AuthorizationConstants.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.authorization.util;

import java.math.BigInteger;

/**
 * Holds constant values for authorization.
 *
 * @author vn70529
 * @since 2.23.0
 */
public class AuthorizationConstants {
    /**
     * Date time format
     */
    public static final String DATE_FORMAT_MMDDYYYYHHMMSS_Z = "MM/dd/yyyy HH:mm:ss z";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_HHMMSS = "HH:mm:ss";

    public static final String DASH = "-";
    public static final String COMMA = ",";

    /**
     * Default date
     */
    public static final String DEFAULT_DATE = "1600-01-01";
    /**
     * Constant for item type
     */
    public static final String ITEM_TYPE_DSD = "DSD";
    public static final String ITEM_TYPE_PLU = "PLU";
    public static final String PREFIX_PLU = "002";
    /**
     * constants for Project name in XML
     */
    public static final String PROJECT_NAME_AUTHORIZE_ITEM = "ItemAuthorization_ExpressSetup";
    public static final String APPLICATION = "Application";
    /**
     * Constants for Transaction types sent in XML
     */
    public static final String AUTHORIZATION_TRANSACTION_TYPE = "R7-ITEMAUTH";
    /**
     * Constants for Transaction types posted in the Queue
     */
    public static final String TYPE_AUTHORIZE = "ITEMAUTH";
    public static final String TYPE_ITEM_SETUP = "ITEMSETUP";
    //Constants for translating sub department id to number
    public static final String SUB_DEPARTMENT_ID = "0,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
    /**
     * Decimal format
     */
    public static final String DECIMAL_00000_FORMAT = "00000";
    public static final String SUB_DEPARTMENT_NUMBER_FORMAT = "00";
    public static final String ONE_VALUE = "1";
    public static final String ZERO_VALUE = "0";
    /**
     * Default value for G3HDRREC/G3_HDR_REC Tag
     */
    public static final String G3_HDR_REC_CODE = "HD";
    public static final BigInteger G3_HDR_APPLY_TYPE_CODE = BigInteger.valueOf(1);
    public static final BigInteger G3_HDR_RESULT_PTR_CODE = BigInteger.valueOf(0);
    /**
     * Default value for G3VNDRREC/G3_VNDR_REC Tag
     */
    public static final String G3_VNDR_REC_CODE = "SU";
    public static final Integer G3_VNDR_REC_CRUD_CODE = 4;
    public static final String G3_VNDR_RSULT_CODE = "00";
    public static final BigInteger G3_VNDR_REC_FLAG = BigInteger.valueOf(1);
    public static final String G3_VNDR_TOT = "0";
    public static final String G3_VNDR_DTL = "1";
    public static final String G3_VNDR_STYLE = "0";
    public static final String DSD_PROCESS_MODE = "M";
    public static final String DSD_BNK_CODE = "1B";
    /**
     * Default value for G3DEPTREC/G3_DEPT_REC Tag
     */
    public static final String G3_DEPT_REC_CD = "DP";
    public static final Integer G3_DEPT_REC_CRUD_CODE = 4;
    public static final String G3_DEPT_REC_RSULT_CODE = "00";
    /**
     * Default value for G3PRODREC/G3_PROD_REC Tag
     */
    public static final String G3_PROD_REC_CODE = "SK";
    public static final Integer G3_PROD_REC_CRUD_CODE = 4;
    public static final String G3_PROD_RSULT_CODE = "00";
    public static final String G3_PROD_SKU_TYP_CD = "M";
    public static final BigInteger G3_PROD_AUTH_SW = BigInteger.valueOf(1);
    public static final BigInteger G3_PROD_ACTV_SW = BigInteger.valueOf(1);
    public static final BigInteger G3_PROD_DISC_SW = BigInteger.ZERO;
    public static final String G3_PROD_STAT_CD = "00";
    public static final BigInteger G3_PROD_REPT_CD = BigInteger.valueOf(1);
    /**
     * Default value for G3CONSMUNTREC/G3_CONSM_UNT_REC Tag
     */
    public static final String G3_CONSM_REC_CD = "IT";
    public static final Integer G3_CONSM_UNT_REC_CRUD_CODE = 4;
    public static final String G3_CONSM_UNT_REC_RESULT_CODE = "00";
    public static final BigInteger G3_CONSM_CU_TAX_SW_ACTIVE = BigInteger.valueOf(1);
    public static final BigInteger G3_CONSM_CU_FD_STMP_ACTIVE = BigInteger.valueOf(1);
    public static final BigInteger G3_CONSM_CU_SCL_ITM_SW_ACTIVE = BigInteger.valueOf(1);
    public static final String G3_CCU_COUP_CD = "N";
    public static final BigInteger G3_CONSM_CU_POS_ITM = BigInteger.valueOf(1);
    public static final BigInteger G3_CONSM_CU_DSD_ITM_SW = BigInteger.valueOf(1);
    /**
     * Default value for G3PKREC/G3_PK_REC Tag
     */
    public static final String G3_PK_REC_CODE = "IT";
    public static final Integer G3_PK_REC_CRUD_CODE = 4;
    public static final String G3_PK_REC_RSULT_CODE = "00";
    public static final String G3_PK_REC_PKG_PK_TYP_CODE = "I";
    /**
     * Default value for G3SPLRPACKREC/G3_SPLR_PACK_REC Tag
     */
    public static final String G3_SPLR_PACK_REC_CODE = "SP";
    public static final Integer G3_SPLR_PACK_REC_CRUD_CODE = 4;
    public static final String G3_SPLR_PACK_REC_RSULT_CODE = "00";
    public static final String G3_SPLR_PK_TYP_CD = "I";
    public static final String G3_SPLR_PACK_ITM_VNDR_CODE = "1";
    public static final BigInteger SPLR_PK_RECV_AUTH_SW_ACTIVE = BigInteger.valueOf(1);
    /**
     * Default value for SCALEREC/SCALE_REC Tag
     */
    public static final String SCALE_REC_PK_REC_CODE = "SC";
    public static final Integer SCALE_REC_CRUD_CODE = 4;
    public static final String SCALE_REC_COMMAND_CODE = "SPIC";
    public static final String SCALE_REC_SHELF_LIFE = "1";

}
