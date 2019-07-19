/*
 * EbmBdaBatchUpload
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.ebmBda;

import com.heb.pm.batchUpload.BatchUpload;

import java.util.HashMap;
import java.util.Map;

/**
 * The object include EBM_BDA file info.
 * datas is the value on row
 *
 * @author vn70529
 * @since 2.33.0
 */
public class EbmBdaBatchUpload extends BatchUpload {

    /*
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    public static final int COL_POS_CLASS_CODE=0;
    public static final String COL_NM_CLASS_CODE = "Class";
    public static final int COL_POS_COMMODITY_CODE=1;
    public static final String COL_NM_COMMODITY_CODE = "OMI Commodity ID";
    public static final int COL_POS_EBM_CODE =2;
    public static final String COL_NM_EBM_CODE = "eBM OnePass ID";
    public static final int COL_POS_BDA_CODE =3;
    public static final String COL_NM_BDA_CODE = "BDA OnePass ID";

    private String classCode;
    private String commodityCode;
    private String ebmId;
    private String bdaId;

    public static final Map<Integer,String> uploadFileHeader;
    static {
        uploadFileHeader = new HashMap<>();
        uploadFileHeader.put(COL_POS_CLASS_CODE, COL_NM_CLASS_CODE);
        uploadFileHeader.put(COL_POS_COMMODITY_CODE, COL_NM_COMMODITY_CODE);
        uploadFileHeader.put(COL_POS_EBM_CODE, COL_NM_EBM_CODE);
        uploadFileHeader.put(COL_POS_BDA_CODE, COL_NM_BDA_CODE);
    }

    /**
     * Returns the class code.
     *
     * @return The class code.
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     * Sets the class code.
     *
     * @param classCode The class code.
     **/
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    /**
     * Returns the commodity code.
     *
     * @return The commodity code.
     */
    public String getCommodityCode() {
        return commodityCode;
    }

    /**
     * Sets the commodity code.
     *
     * @param commodityCode The commodity code.
     **/
    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    /**
     * Returns eBM id.
     *
     * @return The ebmId.
     **/
    public String getEbmId() {
        return ebmId;
    }

    /**
     * Sets the eBM id.
     *
     * @param ebmId The ebmId.
     **/
    public void setEbmId(String ebmId) {
        this.ebmId = ebmId;
    }

    /**
     * Returns BDA id.
     *
     * @return The bdaId.
     **/
    public String getBdaId() {
        return bdaId;
    }

    /**
     * Sets the BDA id.
     *
     * @param bdaId The bdaId.
     **/
    public void setBdaId(String bdaId) {
        this.bdaId = bdaId;
    }
}
