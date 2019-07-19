/*
 * WineCandidateWorkRequestCreator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.eCommerceAttribute;

import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreator;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.batchUpload.parser.WorkRequestCreatorUtils;
import com.heb.pm.entity.*;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ContentManagementServiceClient;
import com.heb.pm.repository.SellingUnitRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.math.NumberUtils;

/**
 * This is template for WineCandidateWorkRequestCreator.
 *
 * @author vn55306
 * @since 2.8.0
 */
@Service
public class EcommerceAttributeCandidateWorkRequestCreator extends CandidateWorkRequestCreator {
    private static final Logger logger = LoggerFactory.getLogger(EcommerceAttributeCandidateWorkRequestCreator.class);
    /** The Constants int DYNAMIC_COLUMN_START. */
    public static final int BIG_FIVE_DYNAMIC_COLUMN_START = 10;
    /** The Constants int BIG_FIVE_MAX_ALLOWABLE_COLUMNS. */
    public static final int  BIG_FIVE_MAX_ALLOWABLE_COLUMNS = 26;
    public static final long BOTH_ATTR_BRAND = 1642;
    public static final long BOTH_ATTR_SIZE = 1636;
    public static final long BOTH_ATTR_DIRECTIONS = 1654;
    public static final long BOTH_ATTR_INGREDIENTS = 1643;
    public static final long BOTH_ATTR_WARNINGS = 1682;
    public static final String EMPTY_S = "Empty";
    public static final String PROD = "PROD";
    public static final String I = "I";
    public static final String DEC = "DEC";
    public static final String UPC = "UPC";
    public static final String BREAK_LINE_TAG = "</br>";
    public static final String BREAK_LINE = "\n";

    @Value("${app.sourceSystemId}")
    private int sourceSystemId;
    @Autowired
    private EcommerceAttributeValidator ecommerceAttributeValidator;
    @Autowired
    private ContentManagementServiceClient contentManagementServiceClient;
    @Autowired
    private SellingUnitRepository sellingUnitRepository;
    @Override
    public CandidateWorkRequest createRequest(List<String> cellValues, List<ProductAttribute> productAttributes,long transactionId, String userId) {
        EcomBigFiveBatchUpload ecomBigFiveBatchUpload = this.createEcomBigFiveBatchUpload(cellValues,productAttributes);
        try {
            this.ecommerceAttributeValidator.validateRow(ecomBigFiveBatchUpload);
        }catch (UnexpectedInputException e){
            //set UPC id null in case user input invalid value for upc
            ecomBigFiveBatchUpload.setUpcPlu(null);
        }

        CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(null,StringUtils.isNotBlank(ecomBigFiveBatchUpload.getUpcPlu())?Long.valueOf(ecomBigFiveBatchUpload.getUpcPlu()):null,userId,transactionId, CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT,this.getBatchStatus(ecomBigFiveBatchUpload).getName());
        
        //set product id for view detail product information of tracking
        if(null!=ecomBigFiveBatchUpload.getUpcPlu()){
            SellingUnit sellingUnit = sellingUnitRepository.findOne(NumberUtils.toLong(ecomBigFiveBatchUpload.getUpcPlu(), 0L));
            if(null!=sellingUnit){
                candidateWorkRequest.setProductId(sellingUnit.getProdId());
            }
        }
        if(ecomBigFiveBatchUpload.hasErrors()) {
            this.setDataToCandidateStat(candidateWorkRequest,ecomBigFiveBatchUpload);
        } else {
            this.prepareForImageUpload(ecomBigFiveBatchUpload,userId);
            this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest,ecomBigFiveBatchUpload);
            this.setDataToCandidateProductMaster(candidateWorkRequest,ecomBigFiveBatchUpload);
            this.setDataToCandidateScanCodeExtent(candidateWorkRequest,ecomBigFiveBatchUpload);
            this.setDataToCandidateStat(candidateWorkRequest,ecomBigFiveBatchUpload);
            this.setDataToCandidateMasterDataExtensionAttributeDynamic(candidateWorkRequest,ecomBigFiveBatchUpload);

        }
        return candidateWorkRequest;
    }
    /**
     * set Data To CandidateStat.
     * @param batchUploadEcomBigFive
     *            EcomBigFiveBatchUpload
     * @param candidateWorkRequest
     *           CandidateWorkRequest
     * @author vn55306
     */
    private void setDataToCandidateStat(CandidateWorkRequest candidateWorkRequest, EcomBigFiveBatchUpload batchUploadEcomBigFive){
        String errorMessage = batchUploadEcomBigFive.hasErrors() ? batchUploadEcomBigFive.getErrors().get(0) : StringUtils.EMPTY;
        candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
        CandidateStatus candidateStatus = new CandidateStatus();
        CandidateStatusKey candidateStatKey =new CandidateStatusKey();
        candidateStatKey.setStatus(this.getBatchStatus(batchUploadEcomBigFive).getName());
        candidateStatKey.setLastUpdateDate(LocalDateTime.now());
        candidateStatus.setKey(candidateStatKey);
        candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
        candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        candidateStatus.setCommentText(errorMessage);
        candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
    }
    /**
     * set Data To CandidateProductMaster.
     * @param batchUploadEcomBigFive
     *            EcomBigFiveBatchUpload
     * @param candidateWorkRequest
     *           CandidateWorkRequest
     * @author vn55306
     */
    private void setDataToCandidateProductMaster(CandidateWorkRequest candidateWorkRequest, EcomBigFiveBatchUpload batchUploadEcomBigFive) {
        if (!StringUtils.isBlank(batchUploadEcomBigFive.getDisplayName())
                || !StringUtils.isBlank(batchUploadEcomBigFive.getRomanceCopy())) {
            CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
            candidateProductMaster.setNewDataSw(true);
            candidateProductMaster.setPackagingText(CandidateProductMaster.STRING_DEFAULT_BLANK);
            candidateProductMaster.setPrprcForNbr(1);
            candidateProductMaster.setLstUpdtUsrId(candidateWorkRequest.getUserId());
            candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
            candidateProductMaster.setCriticalItem(CandidateProductMaster.STRING_DEFAULT_BLANK);
            candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
            candidateWorkRequest.setCandidateProductMaster(new ArrayList<CandidateProductMaster>());
            candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);
        }
    }

    /**
     * set Data To CandidateScanCodeExtent.
     * @param batchUploadEcomBigFive
     *         The EcomBigFiveBatchUpload
     * @param candidateWorkRequest
     *          The CandidateWorkRequest
     * @author vn55306
     */
    private void setDataToCandidateScanCodeExtent(CandidateWorkRequest candidateWorkRequest, EcomBigFiveBatchUpload batchUploadEcomBigFive){
        if(candidateWorkRequest.getCandidateProductMaster()!=null && !candidateWorkRequest.getCandidateProductMaster().isEmpty()) {
            CandidateScanCodeExtent candidateProductScanCodeExt;
            CandidateScanCodeExtentKey candidateScanCodeExtentKey;
            candidateWorkRequest.getCandidateProductMaster().get(0).setCandidateScanCodeExtents(new ArrayList<CandidateScanCodeExtent>());
            if (StringUtils.isNotBlank(batchUploadEcomBigFive.getDisplayName())) {
                candidateProductScanCodeExt = new CandidateScanCodeExtent();
                candidateScanCodeExtentKey = new CandidateScanCodeExtentKey();
                candidateScanCodeExtentKey.setProductExtentDataCode(CandidateScanCodeExtent.STRING_ESHRT);
                candidateScanCodeExtentKey.setScanCodeId(Long.valueOf(batchUploadEcomBigFive.getUpcPlu()));
                candidateProductScanCodeExt.setKey(candidateScanCodeExtentKey);
                candidateProductScanCodeExt.setCandidateProductMaster(candidateWorkRequest.getCandidateProductMaster().get(0));
                candidateProductScanCodeExt.setCandidateWorkRequest(candidateWorkRequest);
                candidateProductScanCodeExt.setProductDescription(this.getTrimmedValue(batchUploadEcomBigFive.getDisplayName()));
                candidateWorkRequest.getCandidateProductMaster().get(0).getCandidateScanCodeExtents().add(candidateProductScanCodeExt);
            }
            if (StringUtils.isNotBlank(batchUploadEcomBigFive.getRomanceCopy())) {
                candidateProductScanCodeExt = new CandidateScanCodeExtent();
                candidateScanCodeExtentKey = new CandidateScanCodeExtentKey();
                candidateScanCodeExtentKey.setProductExtentDataCode(CandidateScanCodeExtent.STRING_ELONG);
                candidateScanCodeExtentKey.setScanCodeId(Long.valueOf(batchUploadEcomBigFive.getUpcPlu()));
                candidateProductScanCodeExt.setKey(candidateScanCodeExtentKey);
                candidateProductScanCodeExt.setCandidateProductMaster( candidateWorkRequest.getCandidateProductMaster().get(0));
                candidateProductScanCodeExt.setCandidateWorkRequest(candidateWorkRequest);
                candidateProductScanCodeExt.setProductDescription(this.convertNewLineToBreakLineTag(this.getTrimmedValue(batchUploadEcomBigFive.getRomanceCopy())));
                candidateWorkRequest.getCandidateProductMaster().get(0).getCandidateScanCodeExtents().add(candidateProductScanCodeExt);
            }
        }
    }
    /**
     * set Data To CandidateMasterDataExtensionAttribute.
     * @param batchUploadEcomBigFive
     *            EcomBigFiveBatchUpload
     * @author vn55306
     */
    private void setDataToCandidateMasterDataExtensionAttribute(CandidateWorkRequest candidateWorkRequest, EcomBigFiveBatchUpload batchUploadEcomBigFive) {
        candidateWorkRequest.setCandidateMasterDataExtensionAttributes(new ArrayList<CandidateMasterDataExtensionAttribute>());
        if (StringUtils.isNotBlank(batchUploadEcomBigFive.getBrand())) {
            candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest, Long.valueOf(batchUploadEcomBigFive.getUpcPlu()), BOTH_ATTR_BRAND, batchUploadEcomBigFive.getBrand(), "Y"));
        }
        if (StringUtils.isNotBlank(batchUploadEcomBigFive.getSize())) {
            candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest, Long.valueOf(batchUploadEcomBigFive.getUpcPlu()), BOTH_ATTR_SIZE, batchUploadEcomBigFive.getSize(), "Y"));
        }
        if (StringUtils.isNotBlank(batchUploadEcomBigFive.getDirections())) {
            if (!EMPTY_S.equalsIgnoreCase(this.getTrimmedValue(batchUploadEcomBigFive.getDirections()))) {
                candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest,Long.valueOf(batchUploadEcomBigFive.getUpcPlu()), BOTH_ATTR_DIRECTIONS, batchUploadEcomBigFive.getDirections(), "Y"));
            } else {
                candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest, Long.valueOf(batchUploadEcomBigFive.getUpcPlu()), BOTH_ATTR_DIRECTIONS, batchUploadEcomBigFive.getDirections(), "N"));
            }
        }
        if (StringUtils.isNotBlank(batchUploadEcomBigFive.getIngredients())) {
            if (!EMPTY_S.equalsIgnoreCase(this.getTrimmedValue(batchUploadEcomBigFive.getIngredients()))) {
                candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest,Long.valueOf(batchUploadEcomBigFive.getUpcPlu()), BOTH_ATTR_INGREDIENTS, batchUploadEcomBigFive.getIngredients(), "Y"));
            } else {
                candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest, Long.valueOf(batchUploadEcomBigFive.getUpcPlu()), BOTH_ATTR_INGREDIENTS, batchUploadEcomBigFive.getIngredients(), "N"));
            }
        }
        if (StringUtils.isNotBlank(batchUploadEcomBigFive.getWarnings())) {
            if (!EMPTY_S.equalsIgnoreCase(this.getTrimmedValue(batchUploadEcomBigFive.getWarnings()))) {
                candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest,Long.valueOf(batchUploadEcomBigFive.getUpcPlu()), BOTH_ATTR_WARNINGS, batchUploadEcomBigFive.getWarnings(), "Y"));
            } else {
                candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(this.setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest, Long.valueOf(batchUploadEcomBigFive.getUpcPlu()), BOTH_ATTR_WARNINGS, batchUploadEcomBigFive.getWarnings(), "N"));
            }
        }

    }
    /**
     * set Data ToandidateMasterDataExtensionAttribute Dynamic.
     * @param candidateWorkRequest
     *            CandidateWorkRequest
     * @author vn55306
     */
    private void setDataToCandidateMasterDataExtensionAttributeDynamic(CandidateWorkRequest candidateWorkRequest, EcomBigFiveBatchUpload batchUploadEcomBigFive) {
        CandidateMasterDataExtensionAttribute candidateMasterDataExtensionAttribute;
        CandidateMasterDataExtensionAttributeKey candidateMasterDataExtensionAttributeKey;
        for (ProductAttribute productAttribute : batchUploadEcomBigFive.getAttributeList()) {
            candidateMasterDataExtensionAttribute = new CandidateMasterDataExtensionAttribute();
            candidateMasterDataExtensionAttributeKey = new CandidateMasterDataExtensionAttributeKey();
            candidateMasterDataExtensionAttributeKey.setSequenceNumber(0L);
            candidateMasterDataExtensionAttributeKey.setDataSourceSystem(Long.valueOf(sourceSystemId));
            candidateMasterDataExtensionAttributeKey.setItemProductKey(UPC);
            candidateMasterDataExtensionAttributeKey.setKeyId(Long.valueOf(batchUploadEcomBigFive.getUpcPlu()));
            candidateMasterDataExtensionAttributeKey.setAttributeId(Long.valueOf(productAttribute.getAttrId()));
            candidateMasterDataExtensionAttribute.setKey(candidateMasterDataExtensionAttributeKey);
            candidateMasterDataExtensionAttribute.setCandidateWorkRequest(candidateWorkRequest);
            if (productAttribute.isAttrValLstSw()) {
                candidateMasterDataExtensionAttribute.setAttributeCode(Integer.valueOf(String.valueOf(productAttribute.getAttrCdId())));
            } else {
                candidateMasterDataExtensionAttribute.setAttributeCode(null);
            }
            if (!productAttribute.isAttrValLstSw() && (I.equals(productAttribute.getAttrDomainCode())
                    || DEC.equals(productAttribute.getAttrDomainCode()))) {
                candidateMasterDataExtensionAttribute.setAttributeValueNumber(productAttribute.getNumberValue());
            } else {
                candidateMasterDataExtensionAttribute.setAttributeValueNumber(null);
            }
            candidateMasterDataExtensionAttribute.setAttributeValueText(productAttribute.getTextValue());
            candidateMasterDataExtensionAttribute.setNewData("Y");
            candidateMasterDataExtensionAttribute.setCreateDate(LocalDateTime.now());
            candidateMasterDataExtensionAttribute.setLastUpdateUserId(candidateWorkRequest.getLastUpdateUserId());
            candidateMasterDataExtensionAttribute.setCreateUserId(candidateWorkRequest.getLastUpdateUserId());
            candidateMasterDataExtensionAttribute.setLastUpdateDate(LocalDateTime.now());
            if(candidateWorkRequest.getCandidateMasterDataExtensionAttributes()==null || candidateWorkRequest.getCandidateMasterDataExtensionAttributes().isEmpty()){
                candidateWorkRequest.setCandidateMasterDataExtensionAttributes(new ArrayList<CandidateMasterDataExtensionAttribute>());
            }
            candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(candidateMasterDataExtensionAttribute);
        }
    }

    /**
     * convert EcomBigFiveBatchUpload To CandidateScanCodeExtent.
     * @param candidateWorkRequest
     *            CandidateWorkRequest
     * @author vn55306
     */
    private CandidateMasterDataExtensionAttribute setDataToCandidateMasterDataExtensionAttribute(CandidateWorkRequest candidateWorkRequest, long upc,long attributeID,String value, String dataSwitch) {
        CandidateMasterDataExtensionAttribute candidateMasterDataExtensionAttribute = new CandidateMasterDataExtensionAttribute();
        CandidateMasterDataExtensionAttributeKey candidateMasterDataExtensionAttributeKey = new CandidateMasterDataExtensionAttributeKey();
        candidateMasterDataExtensionAttributeKey.setAttributeId(attributeID);
        candidateMasterDataExtensionAttributeKey.setSequenceNumber(0L);
        candidateMasterDataExtensionAttributeKey.setDataSourceSystem(Long.valueOf(sourceSystemId));
        candidateMasterDataExtensionAttributeKey.setItemProductKey(UPC);
        candidateMasterDataExtensionAttributeKey.setKeyId(upc);
        candidateMasterDataExtensionAttribute.setKey(candidateMasterDataExtensionAttributeKey);
        candidateMasterDataExtensionAttribute.setAttributeValueText(value);
        candidateMasterDataExtensionAttribute.setNewData(dataSwitch);
        candidateMasterDataExtensionAttribute.setCreateDate(LocalDateTime.now());
        candidateMasterDataExtensionAttribute.setLastUpdateUserId(candidateWorkRequest.getLastUpdateUserId());
        candidateMasterDataExtensionAttribute.setCreateUserId(candidateWorkRequest.getLastUpdateUserId());
        candidateMasterDataExtensionAttribute.setLastUpdateDate(LocalDateTime.now());
        candidateMasterDataExtensionAttribute.setCandidateWorkRequest(candidateWorkRequest);
        return candidateMasterDataExtensionAttribute;
    }
    /**
     * This method calls the webservice to retrieve the image from a URL, do an antivirus check, the call the
     * existing webservice uploadImageOnline to insert to Centera
     * @param ecomBigFiveBatchUpload EcomBigFiveBatchUpload
     * @param userId String
     * @throws Exception
     * @author vn87351
     */
    private void prepareForImageUpload(EcomBigFiveBatchUpload ecomBigFiveBatchUpload, String userId) {
        if(StringUtils.isNotBlank(ecomBigFiveBatchUpload.getImageUrl())){
            ImageToUpload imageUpload = new ImageToUpload();
            imageUpload.setUpc(Long.valueOf(ecomBigFiveBatchUpload.getUpcPlu()));
            imageUpload.setImageSourceCode(UPC);
            String[] stringSplit = ecomBigFiveBatchUpload.getImageUrl().split("\\.");
            String imageExtension = StringUtils.EMPTY;
            if(stringSplit.length > 1){
                imageExtension = ".".concat(stringSplit[stringSplit.length - 1]);
            }
            imageUpload.setImageName(ecomBigFiveBatchUpload.getUpcPlu().concat(imageExtension));
            imageUpload.setImageCategoryCode(PROD);
            imageUpload.setImageURL(ecomBigFiveBatchUpload.getImageUrl());
            imageUpload.setUserId(userId);
            imageUpload.setImageSource(ecomBigFiveBatchUpload.getImageSource());
            try{
                this.contentManagementServiceClient.uploadImageFromURL(imageUpload);
            }catch (CheckedSoapException e){
                ecomBigFiveBatchUpload.getErrors().add(e.getMessage());
            }
        }
    }
    /**
     * getTrimmedValue.
     *
     * @param value
     *            String value
     * @return String value
     */
    private  String getTrimmedValue(final String value) {
        if (StringUtils.isNotBlank(value)) {
            return value.trim();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Convert new line to break line tag.
     * @param dataStr Data column.
     * @return String
     */
    private String convertNewLineToBreakLineTag(String dataStr) {
        List<String> result = new ArrayList<>();
        if(!StringUtils.isBlank(dataStr)) {
            String[] dataStrArr = dataStr.split(BREAK_LINE);
            for(int i = 0; i < dataStrArr.length; i++) {
                String str = dataStrArr[i].trim();
                if(!StringUtils.isBlank(str)) {
                    result.add(str);
                }
            }
            dataStr = String.join(BREAK_LINE_TAG, result);
        }
        return dataStr;
    }

    /**
     * this method creates a BatchUpload EcomBig Five from a row of data
     * @param cellValues
     * 			List<String>
     * @return ExcelUploadEcomBigFiveVO
     * @author vn55306
     */
    private EcomBigFiveBatchUpload createEcomBigFiveBatchUpload(List<String> cellValues,List<ProductAttribute> productAttributes) {
        EcomBigFiveBatchUpload ret = new EcomBigFiveBatchUpload();
        ProductAttribute productAttribute;
        String value;
        for (int columnCounter = 0; columnCounter < cellValues.size(); columnCounter++) {
            if(columnCounter == BIG_FIVE_MAX_ALLOWABLE_COLUMNS){
                break;
            }
            value = cellValues.get(columnCounter);
            switch (columnCounter) {
                case EcomBigFiveBatchUpload.COL_POS_UPC: {
                    ret.setUpcPlu(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_BRAND: {
                    ret.setBrand(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_SIZE: {
                    ret.setSize(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_DISPLAY_NAME: {
                    ret.setDisplayName(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_ROMANCE_COPY: {
                    ret.setRomanceCopy(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_DIRECTIONS: {
                    ret.setDirections(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_INGREDIENTS: {
                    ret.setIngredients(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_WARNINGS: {
                    ret.setWarnings(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_IMAGE_URL: {
                    ret.setImageUrl(value);
                    break;
                }
                case EcomBigFiveBatchUpload.COL_POS_IMAGE_SOURCE: {
                    ret.setImageSource(value);
                    break;
                }
                default: {
                    //it is a dynamic column
                    //check if attrList isn't empty and large enough to have another value
                    if(StringUtils.isNotBlank(value) && productAttributes!=null
                            && !productAttributes.isEmpty() && productAttributes.size()>(columnCounter-BIG_FIVE_DYNAMIC_COLUMN_START)){
                        productAttribute = productAttributes.get(columnCounter-BIG_FIVE_DYNAMIC_COLUMN_START);
                        productAttribute.setTextValue(value);
                        ret.getAttributeList().add(productAttribute);
                    }
                    break;
                }
            }
        }
        return ret;
    }
   }
