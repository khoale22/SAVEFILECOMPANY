/*
 * $Id: EcomAttributesUploadVO.java,v 1.3 2016/01/12 07:51:33 vn47792 Exp $.
 *
 * Copyright (c) 2012 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.batchUpload.categorySpecific;

import com.heb.pm.batchUpload.BatchUpload;

import java.util.HashMap;
import java.util.Map;
/**
 * This is WineBatchUpload class.
 *
 * @author vn55306
 * @since 2.8.0
 */
public class WineBatchUpload extends BatchUpload {
    /** The column Constant in file upload. */
    public static final int COL_POS_UPC =0;
    public static final String COL_NM_UPC = "UPC";
    public static final int COL_POS_MIN_INVENTORY_THRESHOLD_COUNT =1;
    public static final String COL_NM_MIN_INVENTORY_THRESHOLD_COUNT = "Min Inventory Threshold Count";
    public static final int COL_POS_VINTAGE =2;
    public static final String COL_NM_VINTAGE = "Vintage";
    public static final int COL_POS_WINE_MAKER =3;
    public static final String COL_NM_WINE_MAKER = "Wine Maker";
    public static final int COL_POS_WINE_REGION =4;
    public static final String COL_NM_WINE_REGION = "Wine Region";
    public static final int COL_POS_VARIETAL =5;
    public static final String COL_NM_VARIETAL= "Varietal";
    public static final int COL_POS_ALCOHOL_PERCENTAGE =6;
    public static final String COL_NM_ALCOHOL_PERCENTAGE = "Alcohol Percentage";
    public static final int COL_POS_CRITICAL_SCORE_1 =7;
    public static final String COL_NM_CRITICAL_SCORE_1 = "Critical Score 1";
    public static final int COL_POS_SCORE_BY_1 =8;
    public static final String COL_NM_SCORE_BY_1 = "Scored By 1";
    public static final int COL_POS_TASTING_NOTES_1 =9;
    public static final String COL_NM_TASTING_NOTES_1 = "Tasting Notes 1";
    public static final int COL_POS_UPDATE_DATE_1 =10;
    public static final String COL_NM_UPDATE_DATE_1 = "Updated Date 1";
    public static final int COL_POS_SCORE_1_VINTAGE =11;
    public static final String COL_NM_SCORE_1_VINTAGE = "Score 1 Vintage";
    public static final int COL_POS_CRITICAL_SCORE_2 =12;
    public static final String COL_NM_CRITICAL_SCORE_2 = "Critical Score 2";
    public static final int COL_POS_SCORE_BY_2 =13;
    public static final String COL_NM_SCORE_BY_2 = "Scored By 2";
    public static final int COL_POS_TASTING_NOTES_2 =14;
    public static final String COL_NM_TASTING_NOTES_2 = "Tasting Notes 2";
    public static final int COL_POS_UPDATE_DATE_2 =15;
    public static final String COL_NM_UPDATE_DATE_2 = "Updated Date 2";
    public static final int COL_POS_SCORE_2_VINTAGE =16;
    public static final String COL_NM_SCORE_2_VINTAGE = "Score 2 Vintage";
    public static final int COL_POS_CRITICAL_SCORE_3 =17;
    public static final String COL_NM_CRITICAL_SCORE_3 = "Critical Score 3";
    public static final int COL_POS_SCORE_BY_3 =18;
    public static final String COL_NM_SCORE_BY_3 = "Scored By 3";
    public static final int COL_POS_TASTING_NOTES_3 =19;
    public static final String COL_NM_TASTING_NOTES_3 = "Tasting Notes 3";
    public static final int COL_POS_UPDATE_DATE_3 =20;
    public static final String COL_NM_UPDATE_DATE_3 = "Updated Date 3";
    public static final int COL_POS_SCORE_3_VINTAGE =21;

    public static final String COL_NM_SCORE_3_VINTAGE = "Score 3  Vintage";
    public static final String CRITICAL_SCORE = "Critical Score ";
    public static final String SCORE_BY = "Scored By ";
    public static final String TASTING_NOTES = "Tasting Notes ";
    public static final String UPDATE_DATE = "Updated Date ";
    public static final String SW_Y = "Y";
    public static final Map<Integer,String> uploadFileHeader;
    static {
        uploadFileHeader = new HashMap<>();
        uploadFileHeader.put(COL_POS_UPC, COL_NM_UPC);
        uploadFileHeader.put(COL_POS_MIN_INVENTORY_THRESHOLD_COUNT, COL_NM_MIN_INVENTORY_THRESHOLD_COUNT);
        uploadFileHeader.put(COL_POS_VINTAGE, COL_NM_VINTAGE);
        uploadFileHeader.put(COL_POS_WINE_MAKER, COL_NM_WINE_MAKER);
        uploadFileHeader.put(COL_POS_WINE_REGION, COL_NM_WINE_REGION);
        uploadFileHeader.put(COL_POS_VARIETAL, COL_NM_VARIETAL);
        uploadFileHeader.put(COL_POS_ALCOHOL_PERCENTAGE, COL_NM_ALCOHOL_PERCENTAGE);
        uploadFileHeader.put(COL_POS_CRITICAL_SCORE_1, COL_NM_CRITICAL_SCORE_1);
        uploadFileHeader.put(COL_POS_SCORE_BY_1, COL_NM_SCORE_BY_1);
        uploadFileHeader.put(COL_POS_TASTING_NOTES_1, COL_NM_TASTING_NOTES_1);
        uploadFileHeader.put(COL_POS_UPDATE_DATE_1, COL_NM_UPDATE_DATE_1);
        uploadFileHeader.put(COL_POS_SCORE_1_VINTAGE, COL_NM_SCORE_1_VINTAGE);
        uploadFileHeader.put(COL_POS_CRITICAL_SCORE_2, COL_NM_CRITICAL_SCORE_2);
        uploadFileHeader.put(COL_POS_SCORE_BY_2, COL_NM_SCORE_BY_2);
        uploadFileHeader.put(COL_POS_TASTING_NOTES_2, COL_NM_TASTING_NOTES_2);
        uploadFileHeader.put(COL_POS_UPDATE_DATE_2, COL_NM_UPDATE_DATE_2);
        uploadFileHeader.put(COL_POS_SCORE_2_VINTAGE, COL_NM_SCORE_2_VINTAGE);
        uploadFileHeader.put(COL_POS_CRITICAL_SCORE_3, COL_NM_CRITICAL_SCORE_3);
        uploadFileHeader.put(COL_POS_SCORE_BY_3, COL_NM_SCORE_BY_3);
        uploadFileHeader.put(COL_POS_TASTING_NOTES_3, COL_NM_TASTING_NOTES_3);
        uploadFileHeader.put(COL_POS_UPDATE_DATE_3, COL_NM_UPDATE_DATE_3);
        uploadFileHeader.put(COL_POS_SCORE_3_VINTAGE, COL_NM_SCORE_3_VINTAGE);
    }

    private String upc;
    private String minInventoryThresholdCount;
    private String vintage;
    private String wineMaker;
    private String wineRegion;
    private String varietal;
    private String alcoholPercent;
    private String criticalScore1;
    private String scoredBy1;
    private String tastingNotes1;
    private String updatedDate1;
    private String score1Vintage;
    private String criticalScore2;
    private String scoredBy2;
    private String tastingNotes2;
    private String updatedDate2;
    private String score2Vintage;
    private String criticalScore3;
    private String scoredBy3;
    private String tastingNotes3;
    private String updatedDate3;
    private String score3Vintage;
    private String prodId;

    /**
     * @return the upc
     */
    public String getUpc() {
        return this.upc;
    }

    /**
     * @param upcVal String
     */
    public void setUpc(String upcVal) {
        this.upc = upcVal;
    }


    /**
     * @return the minInventoryThresholdCount
     */
    public String getMinInventoryThresholdCount() {
        return this.minInventoryThresholdCount;
    }

    /**
     * @param minInventoryThresholdCount the minInventoryThresholdCount to set
     */
    public void setMinInventoryThresholdCount(String minInventoryThresholdCount) {
        this.minInventoryThresholdCount = minInventoryThresholdCount;
    }

    /**
     * @return the vintage
     */
    public String getVintage() {
        return this.vintage;
    }

    /**
     * @param vintageVal
     *            the vintage to set
     */
    public void setVintage(String vintageVal) {
        this.vintage = vintageVal;
    }

    /**
     * @return the wineMaker
     */
    public String getWineMaker() {
        return this.wineMaker;
    }

    /**
     * @param wineMakerVal
     *            the wineMaker to set
     */
    public void setWineMaker(String wineMakerVal) {
        this.wineMaker = wineMakerVal;
    }

    /**
     * @return the wineRegion
     */
    public String getWineRegion() {
        return this.wineRegion;
    }

    /**
     * @param wineRegionVal
     *            the wineRegion to set
     */
    public void setWineRegion(String wineRegionVal) {
        this.wineRegion = wineRegionVal;
    }

    /**
     * @return the varietal
     */
    public String getVarietal() {
        return this.varietal;
    }

    /**
     * @param varietalVal
     *            the varietal to set
     */
    public void setVarietal(String varietalVal) {
        this.varietal = varietalVal;
    }
    /**
     * @return the alcoholPercent
     */
    public String getAlcoholPercent() {
        return this.alcoholPercent;
    }

    /**
     * @param alcoholPercentVal the alcoholPercent to set
     */
    public void setAlcoholPercent(String alcoholPercentVal) {
        this.alcoholPercent = alcoholPercentVal;
    }

    /**
     * @return the criticalScore1
     */
    public String getCriticalScore1() {
        return this.criticalScore1;
    }

    /**
     * @param criticalScore1Val
     *            the criticalScore1 to set
     */
    public void setCriticalScore1(String criticalScore1Val) {
        this.criticalScore1 = criticalScore1Val;
    }

    /**
     * @return the scoredBy1
     */
    public String getScoredBy1() {
        return this.scoredBy1;
    }

    /**
     * @param scoredBy1Val
     *            the scoredBy1 to set
     */
    public void setScoredBy1(String scoredBy1Val) {
        this.scoredBy1 = scoredBy1Val;
    }

    /**
     * @return the tastingNotes1
     */
    public String getTastingNotes1() {
        return this.tastingNotes1;
    }

    /**
     * @param tastingNotes1Val
     *            the tastingNotes1 to set
     */
    public void setTastingNotes1(String tastingNotes1Val) {
        this.tastingNotes1 = tastingNotes1Val;
    }

    /**
     * @return the updatedDate1
     */
    public String getUpdatedDate1() {
        return this.updatedDate1;
    }

    /**
     * @param updatedDate1Val
     *            the updatedDate1 to set
     */
    public void setUpdatedDate1(String updatedDate1Val) {
        this.updatedDate1 = updatedDate1Val;
    }

    /**
     * @return the criticalScore2
     */
    public String getCriticalScore2() {
        return this.criticalScore2;
    }

    /**
     * @param criticalScore2Val
     *            the criticalScore2 to set
     */
    public void setCriticalScore2(String criticalScore2Val) {
        this.criticalScore2 = criticalScore2Val;
    }

    /**
     * @return the scoredBy2
     */
    public String getScoredBy2() {
        return this.scoredBy2;
    }

    /**
     * @param scoredBy2Val
     *            the scoredBy2 to set
     */
    public void setScoredBy2(String scoredBy2Val) {
        this.scoredBy2 = scoredBy2Val;
    }

    /**
     * @return the tastingNotes2
     */
    public String getTastingNotes2() {
        return this.tastingNotes2;
    }

    /**
     * @param tastingNotes2Val
     *            the tastingNotes2 to set
     */
    public void setTastingNotes2(String tastingNotes2Val) {
        this.tastingNotes2 = tastingNotes2Val;
    }

    /**
     * @return the updatedDate2
     */
    public String getUpdatedDate2() {
        return this.updatedDate2;
    }

    /**
     * @param updatedDate2Val
     *            the updatedDate2 to set
     */
    public void setUpdatedDate2(String updatedDate2Val) {
        this.updatedDate2 = updatedDate2Val;
    }

    /**
     * @return the criticalScore3
     */
    public String getCriticalScore3() {
        return this.criticalScore3;
    }

    /**
     * @param criticalScore3Val
     *            the criticalScore3 to set
     */
    public void setCriticalScore3(String criticalScore3Val) {
        this.criticalScore3 = criticalScore3Val;
    }

    /**
     * @return the scoredBy3
     */
    public String getScoredBy3() {
        return this.scoredBy3;
    }

    /**
     * @param scoredBy3Val
     *            the scoredBy3 to set
     */
    public void setScoredBy3(String scoredBy3Val) {
        this.scoredBy3 = scoredBy3Val;
    }

    /**
     * @return the tastingNotes3
     */
    public String getTastingNotes3() {
        return this.tastingNotes3;
    }

    /**
     * @param tastingNotes3Val
     *            the tastingNotes3 to set
     */
    public void setTastingNotes3(String tastingNotes3Val) {
        this.tastingNotes3 = tastingNotes3Val;
    }

    /**
     * @return the updatedDate3
     */
    public String getUpdatedDate3() {
        return this.updatedDate3;
    }

    /**
     * @param updatedDate3Val
     *            the updatedDate3 to set
     */
    public void setUpdatedDate3(String updatedDate3Val) {
        this.updatedDate3 = updatedDate3Val;
    }

    /**
     * @return the score1Vintage
     */
    public String getScore1Vintage() {
        return score1Vintage;
    }

    /**
     * @param score1Vintage the score1Vintage to set
     */
    public void setScore1Vintage(String score1Vintage) {
        this.score1Vintage = score1Vintage;
    }

    /**
     * @return the score2Vintage
     */
    public String getScore2Vintage() {
        return score2Vintage;
    }

    /**
     * @param score2Vintage the score2Vintage to set
     */
    public void setScore2Vintage(String score2Vintage) {
        this.score2Vintage = score2Vintage;
    }

    /**
     * @return the score3Vintage
     */
    public String getScore3Vintage() {
        return score3Vintage;
    }

    /**
     * @param score3Vintage the score3Vintage to set
     */
    public void setScore3Vintage(String score3Vintage) {
        this.score3Vintage = score3Vintage;
    }
    /**
     * @return the prodId
     */
    public String getProdId() {
        return prodId;
    }
    /**
     * @param prodId the prodId to set
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
}
