/*
 * WineCandidateWorkRequestCreator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.categorySpecific;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreator;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.batchUpload.parser.WorkRequestCreatorUtils;
import com.heb.pm.entity.*;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * This is template for WineCandidateWorkRequestCreator.
 *
 * @author vn55306
 * @since 2.8.0
 */
@Service
public class WineCandidateWorkRequestCreator extends CandidateWorkRequestCreator {
    private static final Logger logger = LoggerFactory.getLogger(WineCandidateWorkRequestCreator.class);
    public static final String DATE_MM_DD_YYYY = "MM/dd/yyyy";
    public  static final int CODE_LENGTH = 1;
    @Autowired
    private AbstractBatchUploadValidator wineValidator;
    @Autowired
    private SellingUnitRepository sellingUnitRepository;

    @Override
    public CandidateWorkRequest createRequest(List<String> cellValues, List<ProductAttribute> productAttributes,long transactionId, String userId) {
        WineBatchUpload wineBatchUpload = this.createWineBatchUpload(cellValues);
        this.formatData(wineBatchUpload);
        try{
            this.wineValidator.validateRow(wineBatchUpload);
        }catch (UnexpectedInputException e){
            wineBatchUpload.setUpc(null);
        }

        CandidateWorkRequest candidateWorkRequest =
                WorkRequestCreatorUtils.getEmptyWorkRequest(
                        wineBatchUpload.getProdId(),StringUtils.isBlank(wineBatchUpload.getUpc())?null:Long.valueOf(wineBatchUpload.getUpc()),userId,transactionId,
                        CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT,this.getBatchStatus(wineBatchUpload).getName());
        
        //set product id for ps work request
        if(null!=wineBatchUpload.getUpc()){
            SellingUnit sellingUnit = sellingUnitRepository.findOne(NumberUtils.toLong(wineBatchUpload.getUpc(), 0L));
            if(null!=sellingUnit){
                candidateWorkRequest.setProductId(sellingUnit.getProdId());
                wineBatchUpload.setProdId(String.valueOf(sellingUnit.getProdId()));
            }
        }
        
        if(wineBatchUpload.hasErrors()){
            this.setDataCandidateStat(candidateWorkRequest,wineBatchUpload,this.getBatchStatus(wineBatchUpload).getName());
        } else {
            this.setDataProductMaster(candidateWorkRequest,wineBatchUpload);
            this.setDataProductScore(candidateWorkRequest.getCandidateProductMaster().get(0),wineBatchUpload);
            this.setDataCandidateStat(candidateWorkRequest,wineBatchUpload,this.getBatchStatus(wineBatchUpload).getName());
        }
        return candidateWorkRequest;
    }
    /**
     * set Data To CandidateStat.
     * @param batchUploadWine
     *            WineBatchUpload
     * @author vn55306
     */
    private void setDataCandidateStat(CandidateWorkRequest candidateWorkRequest, WineBatchUpload batchUploadWine, String status){
        String errorMessage = batchUploadWine.hasErrors() ? batchUploadWine.getErrors().get(0) : StringUtils.EMPTY;
        candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
        CandidateStatus candidateStatus = new CandidateStatus();
        CandidateStatusKey candidateStatKey =new CandidateStatusKey();
        candidateStatKey.setStatus(status);
        candidateStatKey.setLastUpdateDate(LocalDateTime.now());
        candidateStatus.setKey(candidateStatKey);
        candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
        candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        candidateStatus.setCommentText(errorMessage);
        candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
        candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
    }
    /**
     * set Data To ProductMaster.
     * @param batchUploadWine
     *            WineBatchUpload
     * @author vn55306
     */
    private void setDataProductMaster(CandidateWorkRequest candidateWorkRequest, WineBatchUpload batchUploadWine){
        if(!batchUploadWine.hasErrors()) {
            CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
            candidateProductMaster.setProductId(Long.valueOf(batchUploadWine.getProdId()));
            candidateProductMaster.setNewDataSw(true);
            candidateProductMaster.setLstUpdtUsrId(candidateWorkRequest.getUserId());
            candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
            candidateProductMaster.setCriticalItem(CandidateProductMaster.STRING_DEFAULT_BLANK);
            candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
            candidateProductMaster.setPrprcForNbr(0);
            candidateProductMaster.setWineProductSwitch(WineBatchUpload.SW_Y);
            if (StringUtils.isNotBlank(batchUploadWine.getVintage())) {
                candidateProductMaster.setWineVintageYy(Integer.valueOf(batchUploadWine.getVintage()));
            }
            if (StringUtils.isNotBlank(batchUploadWine.getWineMaker())) {
                candidateProductMaster.setWineMakerId(Integer.valueOf(batchUploadWine.getWineMaker()));
            }
            if (StringUtils.isNotBlank(batchUploadWine.getWineRegion())) {
                candidateProductMaster.setWineRegionId(Integer.valueOf(batchUploadWine.getWineRegion()));
            }
            if (StringUtils.isNotBlank(batchUploadWine.getVarietal())) {
                candidateProductMaster.setVarTlId(Integer.valueOf(batchUploadWine.getVarietal()));
            }
            if (StringUtils.isNotBlank(batchUploadWine.getAlcoholPercent())) {
                candidateProductMaster.setAlcoholPercentAmount(BigDecimal.valueOf(Double.valueOf(batchUploadWine.getAlcoholPercent())));
                candidateProductMaster.setAlcoholProductSwitch(WineBatchUpload.SW_Y);
            }
            if (StringUtils.isNotBlank(batchUploadWine.getMinInventoryThresholdCount())) {
                candidateProductMaster.setMinInventoryThresholdCount(Integer.parseInt(batchUploadWine.getMinInventoryThresholdCount()));
            }
            candidateProductMaster.setPackagingText(CandidateProductMaster.STRING_DEFAULT_BLANK);
            candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
            candidateWorkRequest.setCandidateProductMaster(new ArrayList<CandidateProductMaster>());
            candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);
        }
    }
    /**
     * set Data to ProductScore.
     * @param wineBatchUpload WineBatchUpload
     * @param candidateProductMaster CandidateProductMaster
     * @author vn55306
     */
    private void setDataProductScore(CandidateProductMaster candidateProductMaster, WineBatchUpload wineBatchUpload){
        List<Integer> scoringOrgIds = new ArrayList<Integer>();
        Integer scoringOrgId;
        List<CandidateProductScore> candidateProductScores = new ArrayList<CandidateProductScore>();
        CandidateProductScore candidateProductScore = new CandidateProductScore();
        CandidateProductScoreKey key = new CandidateProductScoreKey();
        key.setScoringOrgId(Integer.valueOf(wineBatchUpload.getScoredBy1()));
        candidateProductScore.setKey(key);
        candidateProductScore.setCandidateProductMaster(candidateProductMaster);
        candidateProductScore.setProductScoreDate(DateUtils.getLocalDate(wineBatchUpload.getUpdatedDate1(),DATE_MM_DD_YYYY));
        candidateProductScore.setProductScoreNumber(NumberUtils.createBigDecimal(wineBatchUpload.getCriticalScore1()));
        candidateProductScore.setProdScoreTstText(StringUtils.trimToEmpty(wineBatchUpload.getTastingNotes1()));
        candidateProductScore.setWineVintageText(StringUtils.trimToEmpty(wineBatchUpload.getScore1Vintage()));
        candidateProductScore.setNewDataSw(true);
        candidateProductScores.add(candidateProductScore);
        if (StringUtils.isNotBlank(wineBatchUpload.getScoredBy2()) && StringUtils.isNotBlank(wineBatchUpload.getUpdatedDate2())) {
            scoringOrgId = Integer.valueOf(wineBatchUpload.getScoredBy2());
            if(!scoringOrgIds.contains(scoringOrgId)){
                scoringOrgIds.add(scoringOrgId);
                candidateProductScore = new CandidateProductScore();
                // Set key
                key = new CandidateProductScoreKey();
                key.setScoringOrgId(scoringOrgId);
                candidateProductScore.setKey(key);
                candidateProductScore.setCandidateProductMaster(candidateProductMaster);
                candidateProductScore.setProductScoreDate(DateUtils.getLocalDate(wineBatchUpload.getUpdatedDate2(),DATE_MM_DD_YYYY));
                candidateProductScore.setProductScoreNumber(NumberUtils.createBigDecimal(wineBatchUpload.getCriticalScore2()));
                candidateProductScore.setProdScoreTstText(StringUtils.trimToEmpty(wineBatchUpload.getTastingNotes2()));
                candidateProductScore.setWineVintageText(StringUtils.trimToEmpty(wineBatchUpload.getScore2Vintage()));
                candidateProductScore.setNewDataSw(true);
                candidateProductScores.add(candidateProductScore);
            }

        }
        if (StringUtils.isNotBlank(wineBatchUpload.getScoredBy3()) && StringUtils.isNotBlank(wineBatchUpload.getUpdatedDate3())) {
            scoringOrgId = Integer.valueOf((wineBatchUpload.getScoredBy3()));
            if (!scoringOrgIds.contains(scoringOrgId)) {
                scoringOrgIds.add(scoringOrgId);
                candidateProductScore = new CandidateProductScore();
                // Set key
                key = new CandidateProductScoreKey();
                key.setScoringOrgId(scoringOrgId);
                candidateProductScore.setKey(key);
                candidateProductScore.setCandidateProductMaster(candidateProductMaster);
                candidateProductScore.setProductScoreDate(DateUtils.getLocalDate(wineBatchUpload.getUpdatedDate3(),DATE_MM_DD_YYYY));
                candidateProductScore.setProductScoreNumber(NumberUtils.createBigDecimal(wineBatchUpload.getCriticalScore3()));
                candidateProductScore.setProdScoreTstText(StringUtils.trimToEmpty(wineBatchUpload.getTastingNotes3()));
                candidateProductScore.setWineVintageText(StringUtils.trimToEmpty(wineBatchUpload.getScore3Vintage()));
                candidateProductScore.setNewDataSw(true);
                candidateProductScores.add(candidateProductScore);
            }
        }
        candidateProductMaster.setCandidateProductScores(new ArrayList<CandidateProductScore>());
        candidateProductMaster.getCandidateProductScores().addAll(candidateProductScores);

    }

    /**
     * format data.
     * @param wineBatchUpload WineBatchUpload
     * @author vn55306
     */
    private void formatData(WineBatchUpload wineBatchUpload) {
        if (StringUtils.isNotBlank(wineBatchUpload.getVarietal())) {
            wineBatchUpload.setVarietal(this.getCode(wineBatchUpload.getVarietal(), CODE_LENGTH));
        }
        if (StringUtils.isNotBlank(wineBatchUpload.getWineMaker())) {
            wineBatchUpload.setWineMaker(this.getCode(wineBatchUpload.getWineMaker(), CODE_LENGTH));
        }
        if (StringUtils.isNotBlank(wineBatchUpload.getWineRegion())) {
            wineBatchUpload.setWineRegion(this.getCode(wineBatchUpload.getWineRegion(), CODE_LENGTH));
        }
        if (StringUtils.isNotBlank(wineBatchUpload.getScoredBy1())) {
            wineBatchUpload.setScoredBy1(this.getCode(wineBatchUpload.getScoredBy1(), CODE_LENGTH));
        }
        if (StringUtils.isNotBlank(wineBatchUpload.getScoredBy2())) {
            wineBatchUpload.setScoredBy2(this.getCode(wineBatchUpload.getScoredBy2(), CODE_LENGTH));
        }
        if (StringUtils.isNotBlank(wineBatchUpload.getScoredBy3())) {
            wineBatchUpload.setScoredBy3(this.getCode(wineBatchUpload.getScoredBy3(), CODE_LENGTH));
        }
        logger.info("formatData "+wineBatchUpload.getVarietal()+"--"+wineBatchUpload.getWineMaker()+"--"+wineBatchUpload.getWineMaker()+"--"+wineBatchUpload.getWineRegion()+"--"+wineBatchUpload.getScoredBy1()+"--"+wineBatchUpload.getScoredBy2()+"--"+wineBatchUpload.getScoredBy3());
    }


    /**
     * get Code.
     * @param codeValue String
     * @param endIndex int
     * @author vn55306
     */
    private String getCode(String codeValue, int endIndex) {
        return codeValue.substring(0, endIndex);
    }
    /**
     * convert data from row to WineBatchUpload.
     * @param cellValues
     *            List<String>
     * @return AssortmentBatchUpload
     * @author vn55306
     */
    private WineBatchUpload createWineBatchUpload(List<String> cellValues) {
        WineBatchUpload ret = new WineBatchUpload();
        String value;
        for (int columnCounter = 0; columnCounter < cellValues.size(); columnCounter++) {
            value = cellValues.get(columnCounter);
            switch (columnCounter) {
                case WineBatchUpload.COL_POS_UPC: {
                    ret.setUpc(value);
                    break;
                }
                case WineBatchUpload.COL_POS_MIN_INVENTORY_THRESHOLD_COUNT: {
                    ret.setMinInventoryThresholdCount(value);
                    break;
                }
                case WineBatchUpload.COL_POS_VINTAGE: {
                    ret.setVintage(value);
                    break;
                }
                case WineBatchUpload.COL_POS_WINE_MAKER: {
                    ret.setWineMaker(value);
                    break;
                }
                case WineBatchUpload.COL_POS_VARIETAL: {
                    ret.setVarietal(value);
                    break;
                }
                case WineBatchUpload.COL_POS_ALCOHOL_PERCENTAGE: {
                    ret.setAlcoholPercent(value);
                    break;
                }
                case WineBatchUpload.COL_POS_CRITICAL_SCORE_1: {
                    ret.setCriticalScore1(value);
                    break;
                }
                case WineBatchUpload.COL_POS_SCORE_BY_1: {
                    ret.setScoredBy1(value);
                    break;
                }
                case WineBatchUpload.COL_POS_TASTING_NOTES_1: {
                    ret.setTastingNotes1(value);
                    break;
                }
                case WineBatchUpload.COL_POS_UPDATE_DATE_1: {
                    ret.setUpdatedDate1(value);
                    break;
                }
                case WineBatchUpload.COL_POS_SCORE_1_VINTAGE: {
                    ret.setScore1Vintage(value);
                    break;
                }
                case WineBatchUpload.COL_POS_CRITICAL_SCORE_2: {
                    ret.setCriticalScore2(value);
                    break;
                }
                case WineBatchUpload.COL_POS_SCORE_BY_2: {
                    ret.setScoredBy2(value);
                    break;
                }
                case WineBatchUpload.COL_POS_TASTING_NOTES_2: {
                    ret.setTastingNotes2(value);
                    break;
                }
                case WineBatchUpload.COL_POS_UPDATE_DATE_2: {
                    ret.setUpdatedDate2(value);
                    break;
                }
                case WineBatchUpload.COL_POS_SCORE_2_VINTAGE: {
                    ret.setScore2Vintage(value);
                    break;
                }
                case WineBatchUpload.COL_POS_CRITICAL_SCORE_3: {
                    ret.setCriticalScore3(value);
                    break;
                }
                case WineBatchUpload.COL_POS_SCORE_BY_3: {
                    ret.setScoredBy3(value);
                    break;
                }
                case WineBatchUpload.COL_POS_TASTING_NOTES_3: {
                    ret.setTastingNotes3(value);
                    break;
                }
                case WineBatchUpload.COL_POS_UPDATE_DATE_3: {
                    ret.setUpdatedDate3(value);
                    break;
                }
                case WineBatchUpload.COL_POS_SCORE_3_VINTAGE: {
                    ret.setScore3Vintage(value);
                    break;
                }
            }
        }

        return ret;
    }
}
