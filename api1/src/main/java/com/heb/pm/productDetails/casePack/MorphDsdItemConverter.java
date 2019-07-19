/*
 *  MorphDsdItemConverter
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.*;
import com.heb.pm.repository.ImportItemRepository;
import com.heb.pm.repository.ScaleUpcRepository;
import com.heb.pm.vendor.VendorServiceClient;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.CostServiceClient;
import com.heb.pm.ws.PriceServiceClient;
import com.heb.util.controller.UserInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to convert PRODUCT to PS_PRODUCT entity.
 *
 * @author vn55306
 * @since 2.8.0
 */
@Service
public class MorphDsdItemConverter {
    private static final Logger logger = LoggerFactory.getLogger(MorphDsdItemConverter.class);
    protected  static Character CHAR_WHITE_SPACE = ' ';
    private static final String COST_LOOKUP_ERROR= "Unable to lookup cost item %s";
    private static final String RETAIL_LOOKUP_ERROR= "Unable to lookup retail for upc %s";
    @Value("${app.defaultRetailZone}")
    private int defaultRetailZone;
    @Autowired
    private VendorServiceClient vendorServiceClient;
    @Autowired
    private ImportItemRepository importItemRepository;
    @Autowired
    private ScaleUpcRepository scaleUpcRepository;
    @Autowired
    private CostServiceClient costServiceClient;
    @Autowired
    private PriceServiceClient priceServiceClient;
    /**
     * Convert product data to ps work rqst.
     * @param productMaster
     *            the ProductMaster
     * @return the ps work rqst
     * @author vn55306
     */
    public  CandidateWorkRequest convertProductDataToPsWorkRqst(ProductMaster productMaster,List<BicepVendor> bicepVendors,UserInfo userInfo) {
        CandidateWorkRequest candidateWorkRequest = new CandidateWorkRequest();
        candidateWorkRequest.setIntent(CandidateWorkRequest.INTNT_ID_CPS);
        candidateWorkRequest.setCreateDate(LocalDateTime.now());
        candidateWorkRequest.setStatus(String.valueOf(CandidateWorkRequest.REQUEST_STATUS_WORKING));
        candidateWorkRequest.setSourceSystem(CandidateWorkRequest.SRC_SYSTEM_ID_CPS);
        candidateWorkRequest.setReadyToActivate(CandidateWorkRequest.RDY_TO_ACTVD_SW_DEFAULT);
        candidateWorkRequest.setStatusChangeReason(CandidateWorkRequest.STAT_CHG_RSN_ID_WRKG);
        // need to pass current user login infor
        candidateWorkRequest.setProductId(productMaster.getProdId());
        candidateWorkRequest.setUserId(userInfo.getUserId());
        candidateWorkRequest.setLastUpdateDate(LocalDateTime.now());
        candidateWorkRequest.setLastUpdateUserId(userInfo.getUserId());
        this.convertProductDataToCandidateStatus(candidateWorkRequest,userInfo);
        CandidateProductMaster CandidateProductMaster = this.convertProductMasterToCandidateProductMaster(productMaster,candidateWorkRequest,userInfo);
        candidateWorkRequest.setCandidateItemMasters(this.convertItemMasterPsItemMaster(CandidateProductMaster,productMaster.getProdItems(),candidateWorkRequest,bicepVendors,userInfo));
        return candidateWorkRequest;
    }
    /**
     * convert data from Product Master to CandidateStatus.
     *
     * @param candidateWorkRequest CandidateWorkRequest
     * @author vn55306
     */
    public void convertProductDataToCandidateStatus(CandidateWorkRequest candidateWorkRequest,UserInfo userInfo) {
        candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
        CandidateStatusKey candidateStatusKey = new CandidateStatusKey();
//        psCandidateStatKey.setPsWorkId(candidateWorkRequest.getWorkRequestId());
        candidateStatusKey.setStatus(CandidateStatus.REQUEST_STATUS_WORKING);
        candidateStatusKey.setLastUpdateDate(LocalDateTime.now());
        CandidateStatus candidateStatus = new CandidateStatus();
        candidateStatus.setKey(candidateStatusKey);
        candidateStatus.setUpdateUserId(userInfo.getUserId());
        candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
//        return psCandidateStat;
    }
    /**
     * convertProductMasterToCandidateProductMaster.
     *
     * @param productMaster The ProductMaster.
     * @return CandidateProductMaster.
     * @author vn55306
     * @author vn55306
     */
    private CandidateProductMaster convertProductMasterToCandidateProductMaster(ProductMaster productMaster,CandidateWorkRequest candidateWorkRequest,UserInfo userInfo){
        candidateWorkRequest.setCandidateProductMaster(new ArrayList<CandidateProductMaster>());
        CandidateProductMaster candidateProductMaster = this.genCandidateProductMaster();
        candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
        candidateProductMaster.setProductId(productMaster.getProdId());
        // set product_type getMerchandizeType
        candidateProductMaster.setCommodityCode(productMaster.getCommodityCode());
        candidateProductMaster.setSubCommodityCode(productMaster.getSubCommodityCode());
        candidateProductMaster.setClassCode(productMaster.getClassCode());
//        candidateProductMaster.setBdm(new Bdm());
        candidateProductMaster.setBdmCode(productMaster.getBdmCode());
        candidateProductMaster.setProdBrandId(productMaster.getProdBrandId());
        candidateProductMaster.setPssDepartment(productMaster.getPssDepartmentOne());
        candidateProductMaster.setTaxQualityCode(productMaster.getTaxQualifyingCode());
        candidateProductMaster.setSalesRestrictCode(productMaster.getSalesRestrictCode());
        candidateProductMaster.setDescription(productMaster.getDescription());
        candidateProductMaster.setLstUpdtUsrId(userInfo.getUserId());
        candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
        candidateProductMaster.setScaleSwitch(CandidateProductMaster.SCALE_SW_DEFAULT);
        if (candidateProductMaster.getDescription()!=null && !candidateProductMaster.getDescription().isEmpty()) {
            StringBuffer buffer = new StringBuffer(candidateProductMaster.getDescription());
            String scanDesc = (buffer.length() > CandidateProductMaster.SCAN_DESCRIPTION_MAX_LENGTH) ? buffer.substring(0, CandidateProductMaster.SCAN_DESCRIPTION_MAX_LENGTH) : buffer.toString();
            candidateProductMaster.setScanDescription(scanDesc);
        }
        if(productMaster.getProductPrimaryScanCodeId()!=null){
            candidateProductMaster.setPrimaryScanCode(productMaster.getProductPrimaryScanCodeId());
        }
        GoodsProduct goodsProduct = productMaster.getGoodsProduct();
        if(goodsProduct!=null){
            this.convertGoodsProductToCandidateProductMaster(goodsProduct,candidateProductMaster);
            if(goodsProduct.getRxProduct()!=null){
                this.convertRxProductToCandidateProductMaster(goodsProduct.getRxProduct(),candidateProductMaster);
            }
            if(goodsProduct.getTobaccoProduct()!=null){
                this.convertTbcoProdToCandidateProductMaster(goodsProduct.getTobaccoProduct(),candidateProductMaster);
            }
        }
        // SeT Product Scale
        List<SellingUnit> sellingUnit = productMaster.getSellingUnits();
        long upcProductScale = this.getUpcProductScaleScan(sellingUnit);
        if(upcProductScale >0){
            ScaleUpc scaleUpc = scaleUpcRepository.findOne(upcProductScale);
            this.convertScaleUpcToCandidateProductMaster(scaleUpc,candidateProductMaster);
        }
        candidateProductMaster.setCandidateDescriptions(this.convertProductDescTxtToCandidateDescription(productMaster.getProductDescriptions(),candidateProductMaster,userInfo));
        // set data to ps_prod_scn_codes
        candidateProductMaster.setCandidateSellingUnits(this.convertSellingUnitToCandidateSellingUnits(sellingUnit,candidateProductMaster,userInfo));
        candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);
        return candidateProductMaster;
    }
    /**
     * convertGoodsProductToCandidateProductMaster.
     *
     * @param goodsProduct The goodsProduct.
     * @param candidateProductMaster The CandidateProductMaster
     * @return CandidateProductMaster.
     * @author vn55306
     */
    private  CandidateProductMaster convertGoodsProductToCandidateProductMaster(GoodsProduct goodsProduct,CandidateProductMaster candidateProductMaster){
        candidateProductMaster.setColorCode(goodsProduct.getColorCode());
        candidateProductMaster.setPackagingText(goodsProduct.getPackagingText());
        candidateProductMaster.setFamily1Code(goodsProduct.getFamilyCode1());
        candidateProductMaster.setFamily2Code(goodsProduct.getFamilyCode2());
        candidateProductMaster.setDrugFactPanSwitch(goodsProduct.getDrugFactPanelSwitch() ? "Y" : "N");
        candidateProductMaster.setFoodStampSwitch(goodsProduct.getFoodStampSwitch() ? "Y" : "N");
        candidateProductMaster.setRetailTaxSwitch(goodsProduct.getRetailTaxSwitch()? "Y" : "N");
        candidateProductMaster.setFsaCode(goodsProduct.getFsaCode() ? "Y" : "N");
        candidateProductMaster.setVertexTaxCategoryCode(goodsProduct.getVertexTaxCategoryCode());
        candidateProductMaster.setPremarkPriceAmount(goodsProduct.getPrePrice());
        candidateProductMaster.setMarkPriceInStr(goodsProduct.getMrkPrcInStrSwitch()? "Y" : "N");
        candidateProductMaster.setWtSwitch(goodsProduct.getWicSwitch()? "Y" : "N");
        candidateProductMaster.setTobaccoProductSwitch(goodsProduct.getTobaccoProductSwitch()? "Y" : "N");
        candidateProductMaster.setPseTypeCode(goodsProduct.getPseTypeCode());
        candidateProductMaster.setCriticalItem(goodsProduct.getCriticalProductSwitch());
        candidateProductMaster.setMaxShelfLifeDays(goodsProduct.getMaxShelfLifeDays());
        candidateProductMaster.setSesnlyId(goodsProduct.getSeasonality().getSeasonalityId());
        candidateProductMaster.setSeasonalityYear(goodsProduct.getSeasonalityYear());
        candidateProductMaster.setProductModelText(goodsProduct.getProductModelText());
        candidateProductMaster.setT2tId(goodsProduct.getT2tId());
        return candidateProductMaster;
    }
    /**
     * convertRxProductToCandidateProductMaster.
     *
     * @param rxProduct The ProductMaster.
     * @param candidateProductMaster The RxProduct
     * @return CandidateProductMaster.
     * @author vn55306
     */
    private  CandidateProductMaster convertRxProductToCandidateProductMaster(RxProduct rxProduct,CandidateProductMaster candidateProductMaster){
        candidateProductMaster.setNdc(rxProduct.getNdc());
        candidateProductMaster.setDrugScheduleTypeCode(rxProduct.getDrugScheduleTypeCode());
        candidateProductMaster.setDrugNameCode(StringUtils.trimToEmpty(rxProduct.getDrugNmAbb()));
        candidateProductMaster.setAvgWhlslRxCst(rxProduct.getAvgWhlslRxCst());
        candidateProductMaster.setDirectRxCst(rxProduct.getDirRxCst());
        return candidateProductMaster;
    }
    /**
     * convert data TibcoProduct To CandidateProductMaster.
     *
     * @param tobaccoProduct The TibcoProduct.
     * @param candidateProductMaster The CandidateProductMaster
     * @return CandidateProductMaster.
     * @author vn55306
     */
    private CandidateProductMaster convertTbcoProdToCandidateProductMaster(TobaccoProduct tobaccoProduct, CandidateProductMaster candidateProductMaster){
        candidateProductMaster.setTobaccoProductTypeCode(tobaccoProduct.getTobaccoProductTypeCode());
        candidateProductMaster.setUnstampedSwitch(tobaccoProduct.isUnstampedSw()? "Y" : "N");
        candidateProductMaster.setCigTaxAmt(tobaccoProduct.getCigTaxAmt());
        return candidateProductMaster;
    }


    /**
     * convert data ProductDescription To CandidateDescription.
     *
     * @param productDescriptions The List<ProductDescription>.
     * @param CandidateProductMaster CandidateProductMaster
     * @param userInfo UserInfo
     * @return CandidateProductMaster.
     * @author vn55306
     */
    private List<CandidateDescription> convertProductDescTxtToCandidateDescription(List<ProductDescription> productDescriptions,CandidateProductMaster CandidateProductMaster,UserInfo userInfo){
        List<CandidateDescription> candidateDescriptions = new ArrayList<CandidateDescription>();
        CandidateDescription candidateDescription;
        CandidateDescriptionKey candidateDescriptionKey;
        for(ProductDescription productDescription:productDescriptions){
            candidateDescription = new CandidateDescription();
            candidateDescriptionKey = new CandidateDescriptionKey();
            candidateDescriptionKey.setDescriptionType(productDescription.getKey().getDescriptionType());
            candidateDescriptionKey.setLanguageType(productDescription.getKey().getLanguageType());
            candidateDescription.setKey(candidateDescriptionKey);
            candidateDescription.setLastUpdateDate(LocalDateTime.now());
            candidateDescription.setLastUpdateUserId(userInfo.getUserId());
            candidateDescription.setDescription(productDescription.getDescription());
            candidateDescription.setCandidateProductMaster(CandidateProductMaster);
            candidateDescriptions.add(candidateDescription);
        }
        return candidateDescriptions;
    }
    /**
     * convert data from SellingUnit to CandidateSellingUnit.
     *
     * @param sellingUnits The List<SellingUnit>.
     * @param candidateProductMaster The CandidateProductMaster
     * @param userInfo The UserInfo
     * @return List<PsProdScnCodes>.
     * @author vn55306
     */
    private  List<CandidateSellingUnit> convertSellingUnitToCandidateSellingUnits(List<SellingUnit> sellingUnits,CandidateProductMaster candidateProductMaster,UserInfo userInfo){
        List<CandidateSellingUnit> candidateSellingUnits = new ArrayList<CandidateSellingUnit>();
        CandidateSellingUnit candidateSellingUnit;
        CandidateSellingUnitKey candidateSellingUnitKey;
        long scnCdSeqNbr = 1;
        for(SellingUnit sellingUnit:sellingUnits){
            candidateSellingUnit = new CandidateSellingUnit();
            candidateSellingUnit.setCandidateProductMaster(candidateProductMaster);
            candidateSellingUnit.setNewDataSwitch(true);
            candidateSellingUnitKey = new CandidateSellingUnitKey();
//            psProdScnCodesKey.setPsProdId(CandidateProductMaster.getPsProdId());
            candidateSellingUnitKey.setSequenceNumber(scnCdSeqNbr);
            candidateSellingUnit.setKey(candidateSellingUnitKey);
            scnCdSeqNbr = scnCdSeqNbr +1;
            candidateSellingUnit.setUpc(sellingUnit.getUpc());
            candidateSellingUnit.setUnitUpc(getPadding(String.valueOf(sellingUnit.getUpc())));
            if (sellingUnit.getScanTypeCode()!=null && CandidateSellingUnit.SCN_TYPE_CD_EAN.equalsIgnoreCase(sellingUnit.getScanTypeCode().trim())) {
                candidateSellingUnit.setScanTypeCode(CandidateSellingUnit.SCN_TYPE_CD_UPC);
            } else {
                candidateSellingUnit.setScanTypeCode(sellingUnit.getScanTypeCode());
            }

            candidateSellingUnit.setQuantity(sellingUnit.getQuantity());
            candidateSellingUnit.setRetailUnitOfMeasureCode(sellingUnit.getRetailUnitOfMeasure().getId());
            candidateSellingUnit.setQuantity2(sellingUnit.getQuantity2());
            candidateSellingUnit.setRetailUnitOfMeasureCode2(sellingUnit.getRetailUnitOfMeasure2().getId());
            candidateSellingUnit.setUpc(sellingUnit.getUpc());
            candidateSellingUnit.setRetailHeight(sellingUnit.getRetailHeight());
            candidateSellingUnit.setRetailLength(sellingUnit.getRetailLength());
            candidateSellingUnit.setRetailWidth(sellingUnit.getRetailWidth());
            candidateSellingUnit.setRetailWeight(sellingUnit.getRetailWeight());
            candidateSellingUnit.setTagSize(sellingUnit.getTagSizeDescription());
            candidateSellingUnit.setBonusScanCode(sellingUnit.getBonusSwitch());
            if(sellingUnit.getSubBrand()!=null) {
                candidateSellingUnit.setProductSubBrandId(sellingUnit.getSubBrand().getProdSubBrandId());
            }
            if(sellingUnit.getWicApprovedProductListId()!=null){
                candidateSellingUnit.setWicApprovedProductListId(sellingUnit.getWicApprovedProductListId());
            }
            if(sellingUnit.getPseGramWeight() !=null){
                candidateSellingUnit.setPseGramWeight(sellingUnit.getPseGramWeight());
            }
            candidateSellingUnit.setLastUpdatedOn(sellingUnit.getLastUpdatedOn());
            candidateSellingUnit.setLastUpdatedBy(sellingUnit.getLastUpdatedBy());
            candidateSellingUnit.setTestScanned(false);
            candidateSellingUnit.setSampleProvdSwitch(false);
            candidateSellingUnits.add(candidateSellingUnit);
        }
        return candidateSellingUnits;
    }
    /**
     * convertScaleUpcToCandidateProductMaster.
     *
     * @param scaleUpc The ScaleUpc.
     * @return CandidateProductMaster.
     * @param candidateProductMaster The CandidateProductMaster
     * @author vn55306
     */
    private  CandidateProductMaster convertScaleUpcToCandidateProductMaster(ScaleUpc scaleUpc,CandidateProductMaster candidateProductMaster){
        candidateProductMaster.setActionCode(scaleUpc.getActionCode());
        candidateProductMaster.setEnglishDescriptionOne(scaleUpc.getEnglishDescriptionOne());
        candidateProductMaster.setEnglishDescriptionTwo(scaleUpc.getEnglishDescriptionTwo());
        candidateProductMaster.setSpanishDescriptionOne(scaleUpc.getSpanishDescriptionOne());
        candidateProductMaster.setSpanishDescriptionTwo(scaleUpc.getSpanishDescriptionTwo());
        candidateProductMaster.setGradeNumber(scaleUpc.getGrade());
        candidateProductMaster.setNetWeight(scaleUpc.getNetWeight());
        candidateProductMaster.setForceTare(scaleUpc.isForceTare()? "Y" : "N");
        candidateProductMaster.setGraphicsCode(scaleUpc.getGraphicsCode());
        candidateProductMaster.setIngredientStatement(scaleUpc.getIngredientStatement());
        if(scaleUpc.getFirstLabelFormat() != null){
            candidateProductMaster.setFirstLabelFormat(scaleUpc.getFirstLabelFormat().getFormatCode());
        }
        if(scaleUpc.getSecondLabelFormat() != null){
            candidateProductMaster.setSecondLabelFormat(scaleUpc.getSecondLabelFormat().getFormatCode());
        }
        candidateProductMaster.setNutrientStatement(scaleUpc.getNutrientStatement());
        candidateProductMaster.setPrePackTare(scaleUpc.getPrePackTare());
        candidateProductMaster.setServiceCounterTare(scaleUpc.getServiceCounterTare());
        candidateProductMaster.setShelfLifeDays(scaleUpc.getShelfLifeDays());
        return candidateProductMaster;
    }
    /**
     * get UPC type Scale.
     * @param sellingUnits The List<SellingUnit>.
     * @return upc.
     * @author vn55306
     */
    private long getUpcProductScaleScan(List<SellingUnit> sellingUnits){
        long upc = 0;
        for(SellingUnit sellingUnit:sellingUnits){
            String unitUpc = getPadding(String.valueOf(sellingUnit.getUpc()));
            if (unitUpc.startsWith("002")) {
                upc = sellingUnit.getUpc();
                break;
            }
        }
        return upc;
    }
    /**
     * convertdata from ItemMaster to CandidateItemMaster.
     * @param candidateProductMaster The CandidateProductMaster.
     * @param prodItems The List<ProdItem>.
     * @param candidateWorkRequest The Work Request Id.
     * @param bicepVendors The List<BicepVendor>.
     * @param userInfo The UserInfo.
     * @return CandidateItemMaster.
     * @author vn55306
     */
    public  List<CandidateItemMaster> convertItemMasterPsItemMaster(CandidateProductMaster candidateProductMaster,List<ProdItem> prodItems,CandidateWorkRequest candidateWorkRequest,List<BicepVendor> bicepVendors,UserInfo userInfo){
        List<CandidateItemMaster> candidateItemMasters = new ArrayList<CandidateItemMaster>();
        List<WarehouseLocationItem> warehouseLocationItems;
        CandidateItemMaster candidateItemMaster;
        ItemMaster itemMaster;
        for(ProdItem prodItem : prodItems){
            itemMaster = prodItem.getItemMaster();
            candidateItemMaster = genCandidateItemMaster();
            candidateItemMaster.setItemCode(itemMaster.getKey().getItemCode());
            candidateItemMaster.setItemDescription(itemMaster.getDescription());
            candidateItemMaster.setItemSizeText(itemMaster.getItemSize());
            candidateItemMaster.setMrt(itemMaster.isMrt());
            candidateItemMaster.setItemKeyType(itemMaster.getKey().getItemType());
            if (CandidateItemMaster.ITEM_KEY_TYPE_DSD.equalsIgnoreCase(candidateItemMaster.getItemKeyType())) {
                candidateItemMaster.setDcChannelType(candidateItemMaster.getItemKeyType());
            } else{
                candidateItemMaster.setDcChannelType(CandidateItemMaster.DC_CHNL_TYP_CD_WHS);
            }
            candidateItemMaster.setCandidateWorkRequest(candidateWorkRequest);
            if(itemMaster.getOrderingUpc()!=0){
                candidateItemMaster.setOrderingUpc(itemMaster.getOrderingUpc());
            }
            candidateItemMaster.setCaseUpc(itemMaster.getCaseUpc());
            candidateItemMaster.setDiscontinueDate(itemMaster.getDiscontinueDate());
            if (candidateItemMaster.getDiscontinueDate() != null) {
                if (CandidateItemMaster.ITEM_KEY_TYPE_DSD.equalsIgnoreCase(candidateItemMaster.getItemKeyType())) {
                    if (candidateItemMaster.getDiscontinueDate().toString().startsWith("1600-01-01")) {
                        candidateItemMaster.setPurchaseStatus(CandidateItemMaster.PURCHASE_STATUS_A);
                    } else {
                        candidateItemMaster.setPurchaseStatus(CandidateItemMaster.PURCHASE_STATUS_D);
                    }
                }
            }
            if(candidateProductMaster.getPrimaryScanCode() !=null){
                candidateItemMaster.setDeposit(true);
            } else {
                candidateItemMaster.setDeposit(false);
            }
            candidateItemMaster.setDiscontinueUserId(itemMaster.getDiscontinuedByUID());
            if(itemMaster.getAbcItemCategoryNo()!=null){
                candidateItemMaster.setAbcItemCategoryNo(itemMaster.getAbcItemCategoryNo());
            }
            candidateItemMaster.setAbcAuthorizationCode(itemMaster.getAbcAuthorizationCode());
            if(itemMaster.getUsdaNumber()!=null){
                candidateItemMaster.setUsdaNumber(itemMaster.getUsdaNumber());
            }
            candidateItemMaster.setCriticalItemIndicator(itemMaster.getCriticalItemIndicator());
            if(itemMaster.getMaxShipQty()!=null){
                candidateItemMaster.setMaxShipQuantity(itemMaster.getMaxShipQty());
            }
            candidateItemMaster.setOneTouchTypCode(itemMaster.getOneTouchTypeCode());
            candidateItemMaster.setVariableWeight(itemMaster.getVariableWeight());
            candidateItemMaster.setCattle(itemMaster.isCattle());
            candidateItemMaster.setCatchWeight(itemMaster.istCatchWeight());
            candidateItemMaster.setMexicoAuthorizationCode(itemMaster.getMexicoAuthorizationCode());
            candidateItemMaster.setMexicanBorderAuthorizationCode(itemMaster.getMexicanBorderAuthorizationCode());
            candidateItemMaster.setNewItem(itemMaster.isNewItem());
            candidateItemMaster.setAddedDate(itemMaster.getAddedDate());
            candidateItemMaster.setAddedUserId(itemMaster.getAddedUsrId());
            candidateItemMaster.setLastUpdateDate(itemMaster.getLastUpdateDate());
            candidateItemMaster.setLastUpdateUserId(itemMaster.getLastUpdateUserId());
            candidateItemMaster.setItemSizeQuantity(itemMaster.getItemSizeQuantity());
            candidateItemMaster.setItemSizeUom(itemMaster.getItemSizeUom());
            if(itemMaster.getCommodityCode() !=null){
                candidateItemMaster.setCommodityCode(itemMaster.getCommodityCode());
            }
            if(itemMaster.getSubCommodityCode() !=null){
                candidateItemMaster.setSubCommodityCode(itemMaster.getSubCommodityCode());
            }
            if(itemMaster.getClassCode() !=null) {
                candidateItemMaster.setClassCode(itemMaster.getClassCode());
            }
            candidateItemMaster.setDepartmentIdOne(itemMaster.getDepartmentIdOne());
            candidateItemMaster.setDepartmentIdTwo(itemMaster.getDepartmentIdTwo());
            candidateItemMaster.setDepartmentIdThree(itemMaster.getDepartmentIdThree());
            candidateItemMaster.setDepartmentIdFour(itemMaster.getDepartmentIdFour());
            candidateItemMaster.setSubDepartmentIdOne(itemMaster.getSubDepartmentIdOne());
            candidateItemMaster.setSubDepartmentIdTwo(itemMaster.getSubDepartmentIdTwo());
            candidateItemMaster.setSubDepartmentIdThree(itemMaster.getSubDepartmentIdThree());
            candidateItemMaster.setSubDepartmentIdFour(itemMaster.getSubDepartmentIdFour());
            candidateItemMaster.setShipInrPack(itemMaster.getPack());
            if(itemMaster.getGuaranteeToStoreDays() !=null){
                candidateItemMaster.setGuaranteeToStoreDays(itemMaster.getGuaranteeToStoreDays());
            }
            if(itemMaster.getWarehouseReactionDays() !=null){
                candidateItemMaster.setWarehouseReactionDays(itemMaster.getWarehouseReactionDays());
            }
            if(itemMaster.getOnReceiptLifeDays() !=null) {
                candidateItemMaster.setOnReceiptLifeDays(itemMaster.getOnReceiptLifeDays());
            }
            if (candidateItemMaster.getWarehouseReactionDays()!=null && candidateItemMaster.getInboundSpecDays()!=null && candidateItemMaster.getGuaranteeToStoreDays() !=null
                    && candidateItemMaster.getWarehouseReactionDays() !=0 && candidateItemMaster.getInboundSpecDays()!=0 && candidateItemMaster.getGuaranteeToStoreDays()!=0
                    && candidateProductMaster.getMaxShelfLifeDays()!= null) {
                candidateItemMaster.setMaxShelfLifeDays(candidateProductMaster.getMaxShelfLifeDays().intValue());
            }
            candidateItemMaster.setRowsFacing(itemMaster.getRowsFacing());
            candidateItemMaster.setRowsDeep(itemMaster.getRowsDeep());
            candidateItemMaster.setRowsHigh(itemMaster.getRowsHigh());
            candidateItemMaster.setOrientation(itemMaster.getOrientation());
            candidateItemMaster.setDisplayReadyUnit(itemMaster.getDisplayReadyUnit());
            candidateItemMaster.setPack(itemMaster.getPack());
            if(StringUtils.isEmpty(candidateProductMaster.getProductTypeCode()) && itemMaster.getItemType()!=null){
                candidateProductMaster.setProductTypeCode(this.getMerchandizeType(itemMaster.getItemType().getId().trim(),String.valueOf(itemMaster.getMerchandiseTypeCodeOne())));
            }
            //set Data to Candidate Vendor Location item
            candidateItemMaster.setCandidateVendorLocationItems(this.convertVendorLocationItemToPsVendorLocationItem(itemMaster,candidateItemMaster,candidateProductMaster,userInfo));
            // set data into  candidate warehouse location Item table
            if(CandidateItemMaster.ITEM_KEY_TYPE_ITMCD.equalsIgnoreCase(candidateItemMaster.getItemKeyType())){
                warehouseLocationItems = itemMaster.getWarehouseLocationItems();
                if(warehouseLocationItems != null && !warehouseLocationItems.isEmpty()){
                    // update informtion candidate Item
                    this.convertWarehouseLocationItemShipToCandidateItemMaster(warehouseLocationItems.get(0),candidateItemMaster);
                    candidateItemMaster.setCandidateWarehouseLocationItems(
                            this.convertWarehouseLocationItemToCandidateWarehouseLocationItem(warehouseLocationItems,candidateItemMaster,userInfo));
                }
                this.setDataExpectedWeeklyMovement(candidateItemMaster.getCandidateVendorLocationItems(),candidateItemMaster.getCandidateWarehouseLocationItems());
            }
            // set data ps_itm_scn_cd table
            candidateItemMaster.setCandidateItemScanCodes(this.setLinkedPrimaryCandidateItemScanCode(itemMaster,candidateItemMaster,candidateProductMaster.getCandidateSellingUnits()));
            candidateItemMasters.add(candidateItemMaster);
        }
        // create new item if item type is DSD
        this.createNewItemDsdMorph(candidateProductMaster,candidateItemMasters,bicepVendors,candidateProductMaster.getClassCode(),userInfo);
        return candidateItemMasters;
    }
    /**
     * setDataExpectedWeeklyMovement.
     *
     * @param candidateVendorLocationItems The List<CandidateVendorLocationItem>.
     * @param candidateWarehouseLocationItems The List<CandidateVendorLocationItem>.
     * @author vn55306
     */
    private void setDataExpectedWeeklyMovement(List<CandidateVendorLocationItem> candidateVendorLocationItems,List<CandidateWarehouseLocationItem> candidateWarehouseLocationItems){
        for(CandidateWarehouseLocationItem candidateWarehouseLocationItem:candidateWarehouseLocationItems){
            for(CandidateItemWarehouseVendor psItmWhseVend:candidateWarehouseLocationItem.getCandidateItemWarehouseVendors()){
                for(CandidateVendorLocationItem psVendLocItm:candidateVendorLocationItems){
                    if(psVendLocItm.getKey().getVendorNumber()!=null && psVendLocItm.getKey().getVendorNumber().equals(psItmWhseVend.getKey().getVendorNumber())){
                        psVendLocItm.setExpctWklyMvt(candidateWarehouseLocationItem.getExpctWklyMvt());
                    }
                }
            }
        }
    }
    /**
     * convert CandidateSellingUnit T oCandidateItemScanCode.
     *
     * @param candidateItemMaster The CandidateItemMaster.
     * @param candidateSellingUnits The List<CandidateSellingUnit>.
     * @return List<CandidateItemScanCode>.
     * @author vn55306
     */
    private List<CandidateItemScanCode> convertCandidateSellingUnitToCandidateItemScanCode(CandidateItemMaster candidateItemMaster,List<CandidateSellingUnit> candidateSellingUnits){
        List<CandidateItemScanCode> psItmScnCds = new ArrayList<CandidateItemScanCode>();
        CandidateItemScanCode candidateItemScanCode;
        CandidateItemScanCodeKey candidateItemScanCodeKey;
        for(CandidateSellingUnit candidateSellingUnit:candidateSellingUnits){
            candidateItemScanCode = new CandidateItemScanCode();
            candidateItemScanCodeKey = new CandidateItemScanCodeKey();
            candidateItemScanCodeKey.setCandidateItemId(candidateItemMaster.getCandidateItemId());
            candidateItemScanCodeKey.setUpc(candidateSellingUnit.getUpc());
            candidateItemScanCode.setKey(candidateItemScanCodeKey);
            candidateItemScanCode.setScanType(candidateSellingUnit.getScanTypeCode());
            candidateItemScanCode.setNewData(false);
            candidateItemScanCode.setRetailPackQuantity(0);
            candidateItemScanCode.setPrimarySwitch(false);
            candidateItemScanCode.setCandidateItemMaster(candidateItemMaster);
            psItmScnCds.add(candidateItemScanCode);
        }
        return psItmScnCds;
    }
    /**
     * setLinked PrimaryCandidate ItemScanCode.
     * @param itemMaster the itemMaster.
     * @param candidateItemMaster The CandidateItemMaster.
     * @param candidateSellingUnits The List<CandidateSellingUnit>.
     * @return List<CandidateItemScanCode>.
     * @author vn55306
     */
    private List<CandidateItemScanCode> setLinkedPrimaryCandidateItemScanCode(ItemMaster itemMaster, CandidateItemMaster candidateItemMaster,List<CandidateSellingUnit> candidateSellingUnits) {
        List<CandidateItemScanCode> candidateItemScanCodes = new ArrayList<CandidateItemScanCode>();
        List<CandidateItemScanCode> candidateSellingUnitConverts = this.convertCandidateSellingUnitToCandidateItemScanCode(candidateItemMaster, candidateSellingUnits);
        if (CandidateItemMaster.ITEM_KEY_TYPE_DSD.equalsIgnoreCase(candidateItemMaster.getItemKeyType())) {
            for (CandidateItemScanCode candidateItemScanCode : candidateSellingUnitConverts) {
                if (candidateItemScanCode.getKey().getUpc().equals(candidateItemMaster.getItemCode())) {
                    candidateItemScanCode.setPrimarySwitch(true);
                    candidateItemScanCode.setLinked(true);
                    candidateItemScanCodes.add(candidateItemScanCode);
                    break;
                }
            }
        } else {
            PrimaryUpc primaryUpc = itemMaster.getPrimaryUpc();
            if(primaryUpc !=null && primaryUpc.getAssociateUpcs()!=null){
                for(AssociatedUpc associatedUpc:primaryUpc.getAssociateUpcs()){
                    for (CandidateItemScanCode candidateItemScanCode : candidateSellingUnitConverts) {
                        if (candidateItemScanCode.getKey().getUpc() == associatedUpc.getPrimaryUpc().getUpc()) {
                            candidateItemScanCode.setPrimarySwitch(true);
                        }
                        if (candidateItemScanCode.getKey().getUpc() == associatedUpc.getSellingUnit().getUpc()) {
                            candidateItemScanCode.setLinked(true);
                            candidateItemScanCodes.add(candidateItemScanCode);
                        }
                    }
                }
            }
        }
        return candidateItemScanCodes;
    }
    /**
     * convert WarehouseLocationItemShip To CandidateItemMaster.
     *
     * @param warehouseLocationItem The WarehouseLocationItem.
     * @param candidateItemMaster the CandidateItemMaster
     * @author vn55306
     */
    private void convertWarehouseLocationItemShipToCandidateItemMaster(WarehouseLocationItem warehouseLocationItem,CandidateItemMaster candidateItemMaster){
        candidateItemMaster.setShipHeight(warehouseLocationItem.getShipHeight());
        candidateItemMaster.setShipLength(warehouseLocationItem.getShipLength());
        candidateItemMaster.setShipWidth(warehouseLocationItem.getShipWidth());
//        candidateItemMaster.setShipCube(warehouseLocationItem.getShipCube());
        candidateItemMaster.setShipInrPack(warehouseLocationItem.getShipPack());
        candidateItemMaster.setShipWeight(warehouseLocationItem.getShipWeight());
        if(candidateItemMaster.getShipHeight()!=null && candidateItemMaster.getShipWidth()!=null && candidateItemMaster.getShipLength()!=null){
            candidateItemMaster.setShipCube((candidateItemMaster.getShipHeight()*candidateItemMaster.getShipWidth()*candidateItemMaster.getShipLength())/1732);
        }
        if(warehouseLocationItem.getWhseMaxShipQuantityNumber()!=null){
            candidateItemMaster.setMaxShipQuantity(warehouseLocationItem.getWhseMaxShipQuantityNumber().intValue());
        }
    }
    /**
     * convert VendorLocationItem To CandidateVendorLocationItem.
     *
     * @param itemMaster The ItemMaster.
     * @param  candidateItemMaster CandidateProductMaster.
     * @param candidateItemMaster The CandidateItemMaster.
     * @return PsVendLocItm.
     * @author vn55306
     */
    private List<CandidateVendorLocationItem> convertVendorLocationItemToPsVendorLocationItem(ItemMaster itemMaster,CandidateItemMaster candidateItemMaster,CandidateProductMaster candidateProductMaster,UserInfo userInfo){
        logger.info("convertVendorLocationItemToPsVendorLocationItem");
        List<CandidateVendorLocationItem> candidateVendorLocationItems = new ArrayList<CandidateVendorLocationItem>();
        CandidateVendorLocationItem candidateVendorLocationItem;
        CandidateVendorLocationItemKey key;
        for(VendorLocationItem vendorLocationItem:itemMaster.getVendorLocationItems()){
            candidateVendorLocationItem = genCandidateVendorLocationItem();
            key = new CandidateVendorLocationItemKey();
//            key.setPsItmId(psItemMaster.getPsItmId());
            key.setVendorNumber(vendorLocationItem.getKey().getVendorNumber());
            key.setVendorType(vendorLocationItem.getKey().getVendorType());
            candidateVendorLocationItem.setKey(key);
            candidateVendorLocationItem.setTop2topId(candidateProductMaster.getT2tId());
            candidateVendorLocationItem.setVendItemId(vendorLocationItem.getVendItemId());
            candidateItemMaster.setMasterPackQuantity(vendorLocationItem.getPackQuantity());
            candidateItemMaster.setCube(vendorLocationItem.getCube());
            candidateVendorLocationItem.setCube(vendorLocationItem.getCube());
            candidateItemMaster.setHeight(vendorLocationItem.getHeight());
            candidateVendorLocationItem.setHeight(vendorLocationItem.getHeight());
            candidateItemMaster.setLength(vendorLocationItem.getLength());
            candidateVendorLocationItem.setLength(vendorLocationItem.getLength());
            candidateItemMaster.setWeight(vendorLocationItem.getWeight());
            candidateVendorLocationItem.setWeight(vendorLocationItem.getWeight());
            candidateItemMaster.setWidth(vendorLocationItem.getWidth());
            candidateVendorLocationItem.setWidth(vendorLocationItem.getWidth());
            candidateVendorLocationItem.setWidth(vendorLocationItem.getWidth());
            candidateItemMaster.setShipNestCube(vendorLocationItem.getNestCube());
            candidateVendorLocationItem.setNestCube(vendorLocationItem.getNestCube());
            candidateItemMaster.setShipNestMaxQuantity(vendorLocationItem.getNestMax());
            candidateVendorLocationItem.setNestMax(vendorLocationItem.getNestMax());
            candidateVendorLocationItem.setCandidateItemMaster(candidateItemMaster);
            candidateVendorLocationItem.setSesnlyId(candidateProductMaster.getSesnlyId());
            candidateVendorLocationItem.setSeasonalityYear(candidateProductMaster.getSeasonalityYear());
//            candidateVendorLocationItem.setListCost(vendorLocationItem.getListCost());
            candidateVendorLocationItem.setCostOwnerId(vendorLocationItem.getCostOwnerId());
            candidateVendorLocationItem.setCountryOfOriginId(vendorLocationItem.getCountryOfOriginId());
            candidateVendorLocationItem.setTie(vendorLocationItem.getTie());
            candidateVendorLocationItem.setTier(vendorLocationItem.getTier());
            candidateVendorLocationItem.setOrderQuantityRestrictionCode(vendorLocationItem.getOrderQuantityRestrictionCode());
            candidateVendorLocationItem.setPalletQuantity(vendorLocationItem.getPalletQuantity());
            candidateVendorLocationItem.setPalletSize(vendorLocationItem.getPalletSize());
            candidateVendorLocationItem.setScaCode(vendorLocationItem.getScaCode());
            candidateVendorLocationItem.setCostLinkId(vendorLocationItem.getCostLinkId());
            if(CandidateVendorLocationItem.VEND_LOC_TYP_CD_DSD.equals(vendorLocationItem.getKey().getVendorType())){
                List<VendorItemStore> vendorItemStores = vendorLocationItem.getVendorItemStores();
                if(vendorItemStores!=null){
                    candidateVendorLocationItem.setDeptNbr(vendorItemStores.get(0).getStrDeptNbr());
                    candidateVendorLocationItem.setSubDeptId(vendorItemStores.get(0).getStrSubDeptId());
                }
                candidateVendorLocationItem.setCandidateVendorItemStores(this.convertVendorItemStoreToCandidateVendorItemStore(vendorItemStores,candidateItemMaster,candidateVendorLocationItem));
            } else {
                //set Import Item
                ImportItemKey importItemKey = new ImportItemKey();
                importItemKey.setItemCode(vendorLocationItem.getKey().getItemCode());
                importItemKey.setItemType(vendorLocationItem.getKey().getItemType());
                importItemKey.setVendorNumber(vendorLocationItem.getKey().getVendorNumber());
                importItemKey.setVendorType(vendorLocationItem.getKey().getVendorType());
                ImportItem importItem = importItemRepository.findOne(importItemKey);
                if(importItem!=null){
                    this.convertImportItemToPsPsVendLocItm(importItem,candidateVendorLocationItem,candidateItemMaster);
                }
            }
            candidateVendorLocationItem.setLastUpdatedate(LocalDateTime.now());
            if(this.isNotEmpy(vendorLocationItem.getLstUpdtUid())){
                candidateVendorLocationItem.setLastUpdateUserId(vendorLocationItem.getLstUpdtUid().trim());
            } else {
                candidateVendorLocationItem.setLastUpdateUserId(userInfo.getUserId());
            }
            Cost cost = this.getCostDetailsForVendorLocationItem(vendorLocationItem);
            if(cost!=null){
                candidateVendorLocationItem.setListCost(cost.getCost());
            }
            candidateVendorLocationItems.add(candidateVendorLocationItem);
        }
        return candidateVendorLocationItems;
    }
    /**
     * convert ImportItem To CandidateVendorLocationItem.
     *
     * @param importItem The ImportItem.
     * @param  candidateVendorLocationItem the CandidateVendorLocationItem.
     * @param  candidateItemMaster the CandidateItemMaster.
     * @author vn55306
     */
    private void convertImportItemToPsPsVendLocItm(ImportItem importItem,CandidateVendorLocationItem candidateVendorLocationItem,CandidateItemMaster candidateItemMaster){
        if(importItem.getAgentCommissionPercent() !=null) {
            candidateVendorLocationItem.setAgentCommissionPercent(importItem.getAgentCommissionPercent());
        }
        candidateVendorLocationItem.setColor(importItem.getColor());
        candidateVendorLocationItem.setContainerSizeCode(importItem.getContainerSizeCode());
        candidateVendorLocationItem.setImportSwitch(true);
//            psVendLocItm.setCnimportItem.getCountryOfOrigin());
        candidateVendorLocationItem.setCartonMarking(importItem.getCartonMarking());
        candidateVendorLocationItem.setDutyConfirmationText(importItem.getDutyConfirmationDate());
        candidateVendorLocationItem.setDutyInfoText(importItem.getDutyInformation());
        candidateVendorLocationItem.setDutyPercent(importItem.getDutyPercent());
        candidateVendorLocationItem.setFreightConfirmationText(importItem.getFreightConfirmationDate());
        candidateVendorLocationItem.setHtsNumber(importItem.getHts1());
        candidateVendorLocationItem.setHts2Number(importItem.getHts2());
        candidateVendorLocationItem.setHts3Number(importItem.getHts3());
        candidateVendorLocationItem.setIncoTermCode(importItem.getIncoTermCode());
        candidateVendorLocationItem.setInStoreDate(importItem.getInStoreDate());
        candidateVendorLocationItem.setMinTypeText(importItem.getMinOrderDescription());
        if(importItem.getMinOrderQuantity() != null)
            candidateVendorLocationItem.setMinOrderQuantity(importItem.getMinOrderQuantity());
        candidateVendorLocationItem.setPickupPoint(importItem.getPickupPoint());
        candidateVendorLocationItem.setProrationDate(importItem.getProrationDate());
        candidateVendorLocationItem.setSeason(importItem.getSeason());
        candidateVendorLocationItem.setSellByYear(importItem.getSellByYear());
        candidateVendorLocationItem.setWarehouseFlushDate(importItem.getWarehouseFlushDate());
        candidateVendorLocationItem.setLastUpdateUserId(importItem.getImportUpdtUsr());
        candidateVendorLocationItem.setLastUpdatedate(importItem.getImportUpdtTs());
        List<VendorItemFactory> vendorItemFactories = importItem.getVendorItemFactory();
        if(vendorItemFactories!=null && !vendorItemFactories.isEmpty()){
            candidateVendorLocationItem.setCandidateVendorItemFactorys(this.convertVendorItemFactoryToCandidateVendorItemFactory(vendorItemFactories,candidateItemMaster,candidateVendorLocationItem));
        }

    }
    /**
     * convert VendorItemFactory To CandidateVendorItemFactory.
     *
     * @param vendorItemFactories The vendorItemFactories.
     * @param candidateItemMaster The CandidateItemMaster.
     * @param  candidateVendorLocationItem The CandidateVendorLocationItem.
     * @return List<CandidateVendorItemFactory>.
     * @author vn55306
     */
    private List<CandidateVendorItemFactory> convertVendorItemFactoryToCandidateVendorItemFactory(List<VendorItemFactory> vendorItemFactories,CandidateItemMaster candidateItemMaster,CandidateVendorLocationItem candidateVendorLocationItem){
        List<CandidateVendorItemFactory> candidateVendorItemFactories = new ArrayList<CandidateVendorItemFactory>();
        CandidateVendorItemFactory candidateVendorItemFactory;
        CandidateVendorItemFactoryKey key;
        for(VendorItemFactory vendorItemFactory:vendorItemFactories){
            candidateVendorItemFactory = new CandidateVendorItemFactory();
            key = new CandidateVendorItemFactoryKey();
            key.setVendorNumber(vendorItemFactory.getKey().getVendorNumber());
            key.setVendorType(vendorItemFactory.getKey().getVendorType());
            key.setFactoryId(vendorItemFactory.getKey().getFactoryId());
            candidateVendorItemFactory.setKey(key);
            candidateVendorItemFactory.setCandidateVendorLocationItem(candidateVendorLocationItem);
            candidateVendorItemFactory.setLastUpdateTs(vendorItemFactory.getLastUpdatedTimeStamp());
            candidateVendorItemFactory.setLastUpdateUserId(vendorItemFactory.getLastUpdatedUserID());
            candidateVendorItemFactories.add(candidateVendorItemFactory);
        }
        return candidateVendorItemFactories;
    }
    /**
     * genCandidateVendorLocationItem.
     * @return PsVendLocItm.
     * @author vn55306
     */
    private CandidateVendorLocationItem genCandidateVendorLocationItem(){
        CandidateVendorLocationItem candidateVendorLocationItem = new CandidateVendorLocationItem();
        candidateVendorLocationItem.setNewData(false);
        candidateVendorLocationItem.setCostLinkId(0);
        candidateVendorLocationItem.setLastUpdateUserId(CandidateVendorLocationItem.DEFAULT_EMPTY_STRING);
        candidateVendorLocationItem.setHtsNumber(0L);
        candidateVendorLocationItem.setHts2Number(0L);
        candidateVendorLocationItem.setHts3Number(0L);
        candidateVendorLocationItem.setSellByYear(0);
        candidateVendorLocationItem.setAgentCommissionPercent(0D);
        candidateVendorLocationItem.setDutyPercent(0D);
        candidateVendorLocationItem.setIncoTermCode(CandidateVendorLocationItem.INCO_TRM_CD_FOB);
        candidateVendorLocationItem.setAuthdSwitch(true);
        candidateVendorLocationItem.setSampProvideSwitch(CandidateVendorLocationItem.SW_Y);
        return candidateVendorLocationItem;
    }
    /**
     * convert VendorItemStore To CandidateVendorItemStore.
     *
     * @param vendorItemStores The List<VendorItemStore>.
     * @param  candidateItemMaster the CandidateItemMaster.
     * @param candidateVendorLocationItem The CandidateVendorLocationItem.
     * @return List<CandidateVendorItemStore>.
     * @author vn55306
     */
    private List<CandidateVendorItemStore> convertVendorItemStoreToCandidateVendorItemStore(List<VendorItemStore> vendorItemStores,CandidateItemMaster candidateItemMaster,CandidateVendorLocationItem candidateVendorLocationItem){
        List<CandidateVendorItemStore> candidateVendorItemStores = new ArrayList<CandidateVendorItemStore>();
        CandidateVendorItemStore candidateVendorItemStore;
        CandidateVendorItemStoreKey key;
        for(VendorItemStore vendorItemStore:vendorItemStores){
            key = new CandidateVendorItemStoreKey();
            key.setCandidateItemId(candidateItemMaster.getCandidateItemId());
            key.setVendorNumber(candidateVendorLocationItem.getKey().getVendorNumber());
            key.setVendorType(vendorItemStore.getId().getVendLocTypCd());
            key.setLocationNumber(Integer.valueOf(String.valueOf(vendorItemStore.getId().getLocNbr())));
            key.setLocationType(vendorItemStore.getId().getLocTypCd());
            candidateVendorItemStore = genCandidateVendorItemStore();
            candidateVendorItemStore.setKey(key);
            candidateVendorItemStore.setAuthorizeDate(vendorItemStore.getAuthDt());
            candidateVendorItemStore.setUnAuthorizeDate(vendorItemStore.getUnathDt());
            candidateVendorItemStore.setLastUpdateUserId(vendorItemStore.getLstUpdtUsrId());
            candidateVendorItemStore.setLastUpdate(vendorItemStore.getLstUpdtTs());
            candidateVendorItemStore.setAuthorized(vendorItemStore.getAuthnSw());
            if(vendorItemStore.getInvSeqNbr() !=null){
                candidateVendorItemStore.setInvSeqNbr(vendorItemStore.getInvSeqNbr().intValue());
            }
            candidateVendorItemStore.setDirCstAmt(candidateVendorLocationItem.getListCost());
            candidateVendorItemStore.setCandidateVendorLocationItem(candidateVendorLocationItem);
            candidateVendorItemStores.add(candidateVendorItemStore);
        }
        return candidateVendorItemStores;
    }
    /**
     * gen default CandidateVendorItemStore.
     * @return CandidateVendorItemStore.
     * @author vn55306
     */
    private CandidateVendorItemStore genCandidateVendorItemStore(){
        CandidateVendorItemStore candidateVendorItemStore = new CandidateVendorItemStore();
        candidateVendorItemStore.setLastUpdateUserId(CandidateVendorItemStore.DEFAULT_EMPTY_STRING);
        candidateVendorItemStore.setOmitStrSWitch(false);
        candidateVendorItemStore.setLastUpdate(LocalDateTime.now());
        candidateVendorItemStore.setAuthorized(true);
        candidateVendorItemStore.setCostGroupId(0);
        return candidateVendorItemStore;
    }
    /**
     * convert WarehouseLocationItemTo CandidateWarehouseLocationItem.
     *
     * @param warehouseLocationItems The List<WarehouseLocationItem>.
     * @param candidateItemMaster the CandidateItemMaster
     * @param userInfo The UserInfo
     * @return PsWhseLocItm.
     * @author vn55306
     */
    public  List<CandidateWarehouseLocationItem> convertWarehouseLocationItemToCandidateWarehouseLocationItem(List<WarehouseLocationItem> warehouseLocationItems,CandidateItemMaster candidateItemMaster,UserInfo userInfo){
        List<CandidateWarehouseLocationItem> candidateWarehouseLocationItems = new ArrayList<CandidateWarehouseLocationItem>();
        CandidateWarehouseLocationItem candidateWarehouseLocationItem;
        CandidateWarehouseLocationItemKey candidateWarehouseLocationItemKey;
        for(WarehouseLocationItem warehouseLocationItem:warehouseLocationItems){
            candidateWarehouseLocationItem = this.genCandidateWarehouseLocationItem();
            candidateWarehouseLocationItemKey = new CandidateWarehouseLocationItemKey();
            candidateWarehouseLocationItemKey.setWarehouseNumber(warehouseLocationItem.getKey().getWarehouseNumber());
            candidateWarehouseLocationItemKey.setWarehouseType(warehouseLocationItem.getKey().getWarehouseType());
            candidateWarehouseLocationItem.setKey(candidateWarehouseLocationItemKey);
            candidateWarehouseLocationItem.setCandidateItemMaster(candidateItemMaster);
            candidateWarehouseLocationItem.setOrderQuantityTypeCode(warehouseLocationItem.getOrderQuantityTypeCode());
            candidateWarehouseLocationItem.setCreatedOn(warehouseLocationItem.getCreatedOn());
            if(isNotEmpy(warehouseLocationItem.getCreatedBy())){
                candidateWarehouseLocationItem.setCreatedBy(warehouseLocationItem.getCreatedBy());
            } else {
                candidateWarehouseLocationItem.setCreatedBy(userInfo.getUserId());
            }
            candidateWarehouseLocationItem.setLastUpdatedOn(warehouseLocationItem.getPurchaseStatusUpdateTime());
            if(isNotEmpy(warehouseLocationItem.getPurchaseStatusUserId())){
                candidateWarehouseLocationItem.setLastUpdatedId(warehouseLocationItem.getPurchaseStatusUserId());
            } else {
                candidateWarehouseLocationItem.setLastUpdatedId(userInfo.getUserId());
            }
            candidateWarehouseLocationItem.setPurchaseStatusUpdateTime(warehouseLocationItem.getPurchaseStatusUpdateTime());
            candidateWarehouseLocationItem.setSupplierStatusUpdateTime(warehouseLocationItem.getSupplierStatusUpdateTime());
            candidateWarehouseLocationItem.setPurchasingStatus(warehouseLocationItem.getPurchasingStatus());
            candidateWarehouseLocationItem.setPurchaseStatusUserId(warehouseLocationItem.getPurchaseStatusUserId());
            candidateWarehouseLocationItem.setVariableWeight(warehouseLocationItem.isVariableWeight());
            candidateWarehouseLocationItem.setCatchWeight(warehouseLocationItem.isCatchWeight());
            candidateWarehouseLocationItem.setBillCost(warehouseLocationItem.getBillCost());
            candidateWarehouseLocationItem.setBillableInventory(warehouseLocationItem.getBillableInventory());
            candidateWarehouseLocationItem.setAverageCost(warehouseLocationItem.getAverageCost());
            candidateWarehouseLocationItem.setAverageWeight(warehouseLocationItem.getAverageWeight());
            candidateWarehouseLocationItem.setCurrentSlotNumber(warehouseLocationItem.getCurrentSlotNumber());
            candidateWarehouseLocationItem.setUnitFactor1(warehouseLocationItem.getUnitFactor1());
            candidateWarehouseLocationItem.setUnitFactor2(warehouseLocationItem.getUnitFactor2());
            candidateWarehouseLocationItem.setFlowTypeCode(warehouseLocationItem.getFlowTypeCode());
            candidateWarehouseLocationItem.setComment(warehouseLocationItem.getComment());
            candidateWarehouseLocationItem.setWhseTier(warehouseLocationItem.getWhseTier());
            candidateWarehouseLocationItem.setWhseTie(warehouseLocationItem.getWhseTie());
            candidateWarehouseLocationItem.setExpctWklyMvt(warehouseLocationItem.getExpectedWeeklyMovement());
            candidateWarehouseLocationItem.setCandidateItemWarehouseVendors(this.convertItemWarehouseVendorToCandidateItemWarehouseVendor(warehouseLocationItem.getItemWarehouseVendorList(),candidateItemMaster,candidateWarehouseLocationItem,userInfo));
//            candidateWarehouseLocationItem.setCandidateItemWarehouseCommentssetCandidateItemWarehouseComments(this.convertItemWarehouseCommentsToCandidateItemWarehouseComments(warehouseLocationItem.getItemWarehouseCommentsList(),candidateItemMaster,candidateWarehouseLocationItem));
            candidateWarehouseLocationItems.add(candidateWarehouseLocationItem);
        }

        return candidateWarehouseLocationItems;
    }
    /**
     * convert ItemWarehouseVendor To CandidateItemWarehouseVendor.
     *
     * @param itemWarehouseVendors The List<ItemWarehouseVendor>.
     * @param candidateItemMaster CandidateItemMaster
     * @param userInfo UserInfo
     * @param candidateWarehouseLocationItem CandidateWarehouseLocationItem
     * @return PsItmWhseVend.
     * @author vn55306
     */
    private List<CandidateItemWarehouseVendor> convertItemWarehouseVendorToCandidateItemWarehouseVendor(List<ItemWarehouseVendor> itemWarehouseVendors,CandidateItemMaster candidateItemMaster,CandidateWarehouseLocationItem candidateWarehouseLocationItem,UserInfo userInfo){
        List<CandidateItemWarehouseVendor> candidateItemWarehouseVendors = new ArrayList<CandidateItemWarehouseVendor>();
        CandidateItemWarehouseVendor candidateItemWarehouseVendor;
        CandidateItemWarehouseVendorKey key;
        for(ItemWarehouseVendor itemWarehouseVendor : itemWarehouseVendors){
            candidateItemWarehouseVendor = new CandidateItemWarehouseVendor();
            key = new CandidateItemWarehouseVendorKey();
            key.setVendorNumber(itemWarehouseVendor.getKey().getVendorNumber());
            key.setVendorType(itemWarehouseVendor.getKey().getVendorType());
            key.setWarehouseNumber(itemWarehouseVendor.getKey().getWarehouseNumber());
            key.setWarehouseType(itemWarehouseVendor.getKey().getWarehouseType());
            candidateItemWarehouseVendor.setKey(key);
            candidateItemWarehouseVendor.setLastUpdateDate(LocalDateTime.now());
            candidateItemWarehouseVendor.setLastUpdateUserId(userInfo.getUserId());
            candidateItemWarehouseVendor.setCandidateWarehouseLocationItem(candidateWarehouseLocationItem);
            candidateItemWarehouseVendors.add(candidateItemWarehouseVendor);
        }
        return candidateItemWarehouseVendors;
    }
    /**
     * convert ItemWarehouseComments To CandidateItemWarehouseComments.
     *
     * @param lstItemWarehouseComments The List<ItemWarehouseComments>.
     * @param candidateItemMaster CandidateItemMaster
     * @param  candidateWarehouseLocationItem The CandidateWarehouseLocationItem.
     * @return List<CandidateItemWarehouseComments>.
     * @author vn55306
     */
    private List<CandidateItemWarehouseComments> convertItemWarehouseCommentsToCandidateItemWarehouseComments(List<ItemWarehouseComments> lstItemWarehouseComments,CandidateItemMaster candidateItemMaster,CandidateWarehouseLocationItem candidateWarehouseLocationItem){
        List<CandidateItemWarehouseComments> lstCandidateItemWarehouseComments = new ArrayList<CandidateItemWarehouseComments>();
        CandidateItemWarehouseComments candidateItemWarehouseComments;
        CandidateItemWarehouseCommentsKey candidateItemWarehouseCommentsKey;
        for(ItemWarehouseComments itemWarehouseComments:lstItemWarehouseComments){
            candidateItemWarehouseComments = new CandidateItemWarehouseComments();
            candidateItemWarehouseCommentsKey = new CandidateItemWarehouseCommentsKey();
            candidateItemWarehouseCommentsKey.setCandidateItemId(candidateItemMaster.getCandidateItemId());
            candidateItemWarehouseCommentsKey.setItemCommentType(itemWarehouseComments.getKey().getItemCommentType());
            candidateItemWarehouseCommentsKey.setWarehouseNumber(itemWarehouseComments.getKey().getWarehouseNumber());
            candidateItemWarehouseCommentsKey.setWarehouseType(itemWarehouseComments.getKey().getWarehouseType());
            candidateItemWarehouseCommentsKey.setItemCommentNumber(itemWarehouseComments.getKey().getItemCommentNumber());
            candidateItemWarehouseComments.setKey(candidateItemWarehouseCommentsKey);
            candidateItemWarehouseComments.setComments(itemWarehouseComments.getItemCommentContents());
            candidateItemWarehouseComments.setCandidateWarehouseLocationItem(candidateWarehouseLocationItem);
            lstCandidateItemWarehouseComments.add(candidateItemWarehouseComments);
        }
        return lstCandidateItemWarehouseComments;
    }

    /**
     * genCandidateProductMaster.
     * @return CandidateProductMaster
     * @author vn55306
     */
    public  CandidateProductMaster genCandidateProductMaster(){
        CandidateProductMaster CandidateProductMaster=new CandidateProductMaster();
        CandidateProductMaster.setPrprcForNbr(1);
        CandidateProductMaster.setLastUpdateTs(LocalDateTime.now());
        CandidateProductMaster.setLstUpdtUsrId(CandidateProductMaster.STRING_DEFAULT_BLANK);
        CandidateProductMaster.setPackagingText(CandidateProductMaster.STRING_DEFAULT_BLANK);
        return CandidateProductMaster;
    }
    /**
     * gen CandidateWarehouseLocationItem.
     * @return CandidateWarehouseLocationItem
     * @author vn55306
     */
    public  CandidateWarehouseLocationItem genCandidateWarehouseLocationItem(){
        CandidateWarehouseLocationItem candidateWarehouseLocationItem = new CandidateWarehouseLocationItem();
        candidateWarehouseLocationItem.setMfgId(CandidateWarehouseLocationItem.DEFAULT_EMPTY_STRING);
        candidateWarehouseLocationItem.setVariableWeight(false);
        candidateWarehouseLocationItem.setCatchWeight(false);
        candidateWarehouseLocationItem.setAverageWeight(0D);
        candidateWarehouseLocationItem.setWhseTie(1L);
        candidateWarehouseLocationItem.setWhseTier(1L);
        candidateWarehouseLocationItem.setOrderQuantityTypeCode(CandidateWarehouseLocationItem.ORD_QTY_TYP_CD_DEFAULT);
        return candidateWarehouseLocationItem;
    }

    /**
     * PsItemMaster.
     * @return CandidateProductMaster
     * @author vn55306
     */
    private  CandidateItemMaster genCandidateItemMaster(){
        CandidateItemMaster candidateItemMaster = new CandidateItemMaster();
        candidateItemMaster.setAddedUserId(CandidateItemMaster.DEFAULT_EMPTY_STRING);
        candidateItemMaster.setDateControlledItem(false);
        candidateItemMaster.setNewItem(false);
        candidateItemMaster.setRepack(false);
        candidateItemMaster.setCriticalItemIndicator(CandidateItemMaster.DEFAULT_EMPTY_STRING);
        candidateItemMaster.setNevOut(false);
        candidateItemMaster.setCatchWeight(false);
        candidateItemMaster.setLowVel(false);
        candidateItemMaster.setCrossDockItem(false);
        candidateItemMaster.setReplaceOrderQuantity(false);
        candidateItemMaster.setLetterOfCredit(false);
        candidateItemMaster.setVariableWeight(false);
        candidateItemMaster.setForwardBuyApproved(false);
        candidateItemMaster.setCattle(false);
        candidateItemMaster.setDsdItem(false);
        candidateItemMaster.setUpcMap(false);
        candidateItemMaster.setDeposit(false);
        candidateItemMaster.setUnstampedTobacco(false);
        candidateItemMaster.setLastUpdateUserId(CandidateItemMaster.DEFAULT_EMPTY_STRING);
        candidateItemMaster.setMrt(false);
        candidateItemMaster.setNewData(false);
        candidateItemMaster.setDisplayReadyUnit(false);
        candidateItemMaster.setSupplierItem(false);
        return candidateItemMaster;
    }
    /**
     * Gets the merchandize type.
     * @param itmTypCd
     *            the itm typ cd
     * @param deptMerchType
     *            the dept merch type
     * @return the merchandize type
     * @author vn55306
     */
    private  String getMerchandizeType(String itmTypCd, String deptMerchType) {
        String merchandizeType = null;
        if (ItemType.ITEM_TYPE_CODE_1.equals(itmTypCd)) {
            if (ItemType.ITEM_TYPE_CODE_1.equals(deptMerchType) || ItemType.ITEM_TYPE_CODE_DEFAULT.equals(deptMerchType) || ItemType.ITEM_TYPE_CODE_0.equals(deptMerchType) || null == deptMerchType) {
                merchandizeType = itmTypCd;
            } else if (ItemType.ITEM_TYPE_CODE_6.equals(deptMerchType) || ItemType.ITEM_TYPE_CODE_7.equals(deptMerchType) || ItemType.ITEM_TYPE_CODE_9.equals(deptMerchType)
                    || ItemType.ITEM_TYPE_CODE_F.equals(deptMerchType) || ItemType.ITEM_TYPE_CODE_T.equals(deptMerchType)) {
                merchandizeType = deptMerchType;
            }
        } else if (ItemType.ITEM_TYPE_CODE_4.equals(itmTypCd)) {
            merchandizeType = itmTypCd;
        } else if (ItemType.ITEM_TYPE_CODE_3.equals(itmTypCd)) {
            merchandizeType = ItemType.PRODUCT_TYPE_8;
        } else if (ItemType.ITEM_TYPE_CODE_2.equals(itmTypCd)) {
            merchandizeType = ItemType.PRODUCT_TYPE_PRMO;
        } else if (ItemType.ITEM_TYPE_CODE_5.equals(itmTypCd)) {
            merchandizeType = ItemType.PRODUCT_TYPE_SHPR;
        } else if (ItemType.ITEM_TYPE_CODE_6.equals(itmTypCd)) {
            merchandizeType = ItemType.PRODUCT_TYPE_XDK;
        } else if (ItemType.ITEM_TYPE_CODE_9.equals(itmTypCd)) {
            merchandizeType = ItemType.PRODUCT_TYPE_9;
        } else if (ItemType.ITEM_TYPE_CODE_7.equals(itmTypCd)) {
            merchandizeType = ItemType.ITEM_TYPE_CODE_7;
        } else if (ItemType.ITEM_TYPE_CODE_0.equals(itmTypCd) || ItemType.ITEM_TYPE_CODE_SELL.equalsIgnoreCase(itmTypCd)) {
            merchandizeType = ItemType.PRODUCT_TYPE_DEFAULT;
        }
        return merchandizeType;
    }
    /**
     * Gets the padding.
     * @param unitUpc
     *            the unit upc
     * @return the padding
     */
    private  String getPadding(String unitUpc) {
        if (unitUpc != null && !unitUpc.isEmpty()) {
            unitUpc = unitUpc.trim();
            int unitUPCLength = unitUpc.length();
            int padSize = 13 - unitUPCLength;
            int i = 0;
            while (i < padSize) {
                unitUpc = "0" + unitUpc;
                i++;
            }
        }
        return unitUpc;
    }
    /**
     * Creates the data for new item.
     *  @param candidateProductMaster
     *            the CandidateProductMaster
     * @param candidateItemMasters
     *            the List<CandidateItemMaster>
     * @param bicepVendors
     *            the List<BicepVendor>
     * @param classCode
     *           the Integer
     * @param userInfo
     *           the UserInfo
     * @author vn55306
     */
    private void createNewItemDsdMorph(CandidateProductMaster candidateProductMaster,List<CandidateItemMaster> candidateItemMasters, List<BicepVendor> bicepVendors,Integer classCode,UserInfo userInfo) {
        if (bicepVendors != null && !bicepVendors.isEmpty()) {
            CandidateItemMaster candidateItemMasterClone = null;
            CandidateItemMaster candidateItemMasterParentRelated = null;
            for (CandidateItemMaster candidateItemMaster : candidateItemMasters) {
                if (candidateItemMaster.getItemCode().equals(bicepVendors.get(0).getItemId())) {
                    boolean isEdc = false;
                    boolean isWareHouseEdc = false;
                    if (bicepVendors.get(0).getItemType()!=null && CandidateItemMaster.ITEM_KEY_TYPE_DSD.equals(bicepVendors.get(0).getItemType().trim())) {
                        if (bicepVendors.size() == 1 && bicepVendors.get(0).getWarehouseNumber().equals(CandidateItemMaster.STRING_EDC_WHS)) {
                            isEdc = true;
                        }
                        try {
                            candidateItemMasterClone = (CandidateItemMaster) BeanUtils.cloneBean(candidateItemMaster);
                            if(isEdc){
                                candidateItemMasterParentRelated = candidateItemMaster;
                            }
                          } catch (IllegalAccessException e) {
                            logger.error(e.getMessage());
                        } catch (InstantiationException e) {
                            logger.error(e.getMessage());
                        } catch (InvocationTargetException e) {
                            logger.error(e.getMessage());
                        } catch (NoSuchMethodException e) {
                            logger.error(e.getMessage());
                        }
                        this.setDafaultValueNewItem(candidateItemMasterClone,isEdc,userInfo);
                        List<CandidateItemScanCode> candidateItemScanCodes = candidateItemMasterClone.getCandidateItemScanCodes();
                        List<CandidateItemScanCode> candidateItemScanCodeClones = new ArrayList<CandidateItemScanCode>();
                        CandidateItemScanCode candidateItemScanCode;
                        CandidateItemScanCodeKey candidateItemScanCodeKey;
                        for(CandidateItemScanCode candidateItemScanCodeClone :candidateItemScanCodes){
                            candidateItemScanCode = new CandidateItemScanCode();
                            candidateItemScanCodeKey = new CandidateItemScanCodeKey();
                            candidateItemScanCodeKey.setUpc(candidateItemScanCodeClone.getKey().getUpc());
                            candidateItemScanCode.setKey(candidateItemScanCodeKey);
                            candidateItemScanCode.setCandidateItemMaster(candidateItemMasterClone);
                            candidateItemScanCode.setNewData(true);
                            candidateItemScanCode.setScanType(candidateItemScanCodeClone.getScanType());
                            candidateItemScanCode.setPrimarySwitch(candidateItemScanCodeClone.getPrimarySwitch());
                            candidateItemScanCode.setRetailPackQuantity(candidateItemScanCodeClone.getRetailPackQuantity());
                            candidateItemScanCodeClones.add(candidateItemScanCode);
                        }
                        candidateItemMasterClone.setCandidateItemScanCodes(new ArrayList<CandidateItemScanCode>());
                        candidateItemMasterClone.getCandidateItemScanCodes().addAll(candidateItemScanCodeClones);
                        candidateItemMasterClone.setLastUpdateUserId(userInfo.getUserId());
                        candidateItemMasterClone.setLastUpdateDate(LocalDateTime.now());
                        if(isEdc){
                            this.setDataRelatedItem(candidateItemMasterParentRelated, candidateItemMasterClone);
                        }
                        candidateItemMasters.add(candidateItemMasterClone);
                    } else {
                        if (bicepVendors.size() == 1 && bicepVendors.get(0).getWarehouseNumber().equals(CandidateItemMaster.STRING_EDC_WHS)) {
                            isWareHouseEdc =true;
                            candidateItemMaster.setPack(1L);
                            candidateItemMaster.setShipNestCube(1D);
                            candidateItemMaster.setShipNestMaxQuantity(1);
                        }
                        candidateItemMasterClone = candidateItemMaster;
                    }
                    if(candidateItemMasterClone.getOrderingUpc()!=null){
                        Retail retail = this.getRetail(candidateItemMasterClone.getOrderingUpc().longValue());
                        if(retail!=null){
                            candidateProductMaster.setRetailX4Qty(retail.getxFor());
                            candidateProductMaster.setRetailPriceAmount(BigDecimal.valueOf(retail.getRetail()));
                        }
                    }

                    this.createNewVendorDsdMorph(candidateItemMasterClone,bicepVendors,classCode,userInfo,isEdc,isWareHouseEdc);
                    break;
                }
            }
        }
    }

    private void setDataRelatedItem(CandidateItemMaster candidateItemMasterParent,CandidateItemMaster candidateItemMasterChild){
        candidateItemMasterChild.setCandidateRelatedItems(new ArrayList<CandidateRelatedItem>());
        CandidateRelatedItem candidateRelatedItem = new CandidateRelatedItem();
        CandidateRelatedItemKey candidateRelatedItemKey = new CandidateRelatedItemKey();
//        psRelatedItemsKey.setRelatedItmId(itemId.intValue());
        candidateRelatedItem.setKey(candidateRelatedItemKey);
        candidateRelatedItem.setCandidateItemMasterByPsItmId(candidateItemMasterParent);
        candidateRelatedItem.setCandidateItemMasterByPsRelatedItmId(candidateItemMasterChild);
        candidateRelatedItem.setQuantity(0);
        candidateRelatedItem.setSequenceNumber(0);
        candidateRelatedItem.setItemRelationshipType(CandidateRelatedItem.ITM_RLSHP_TYP_CD_MORPH);
        candidateItemMasterChild.getCandidateRelatedItems().add(candidateRelatedItem);
    }

    private void setDafaultValueNewItem(CandidateItemMaster candidateItemMaster,boolean isEdc,UserInfo useInfo){
        candidateItemMaster.setItemCode(null);
        candidateItemMaster.setDcChannelType(CandidateItemMaster.DC_CHNL_TYP_CD_WHS);
        candidateItemMaster.setAddedDate(LocalDate.now());
        candidateItemMaster.setAddedUserId(useInfo.getUserId());
        candidateItemMaster.setLastUpdateDate(LocalDateTime.now());
        candidateItemMaster.setLastUpdateUserId(useInfo.getUserId());
        candidateItemMaster.setNewData(true);
        candidateItemMaster.setNewItem(true);
        candidateItemMaster.setItemKeyType(CandidateItemMaster.ITEM_KEY_TYPE_ITMCD);
        // set Default value
        candidateItemMaster.setShipWeight(1D);
        candidateItemMaster.setShipInrPack(1L);
        candidateItemMaster.setShipWidth(1D);
        candidateItemMaster.setShipCube(1D);
        candidateItemMaster.setShipLength(1D);
        candidateItemMaster.setShipHeight(1D);
        candidateItemMaster.setShipNestCube(0D);
        candidateItemMaster.setShipNestMaxQuantity(0);
        candidateItemMaster.setMaxShipQuantity(99999);
        candidateItemMaster.setCube(1D);
        candidateItemMaster.setHeight(1D);
        candidateItemMaster.setLength(1D);
        candidateItemMaster.setWidth(1D);
        candidateItemMaster.setWeight(1D);
        if (!isEdc) {
            if(candidateItemMaster.getMasterPackQuantity()!=null){
                candidateItemMaster.setPack(candidateItemMaster.getMasterPackQuantity().longValue());
            }
            candidateItemMaster.setShipWeight(0D);
            candidateItemMaster.setShipWidth(0D);
            candidateItemMaster.setShipCube(0D);
            candidateItemMaster.setShipLength(0D);
            candidateItemMaster.setShipHeight(0D);
        } else {
            candidateItemMaster.setPack(1L);
        }
    }
    /**
     * Creates the data for new vendor.
     * @param candidateItemMaster
     *            the CandidateItemMaster
     * @param bicepVendorSelecteds
     *            the List<BicepVendor>
     * @param classCode
     *           the Integer
     * @param userInfo
     *           the UserInfo
     * @param isEdc
     *           the isEdc
     * * @param isWareHouseEdc
     *           the isWareHouseEdc
     * @author vn55306
     */
    private void createNewVendorDsdMorph(CandidateItemMaster candidateItemMaster,List<BicepVendor> bicepVendorSelecteds,Integer classCode,UserInfo userInfo,boolean isEdc,boolean isWareHouseEdc){
        try {
            List<BicepVendor> lstWareHouses = this.vendorServiceClient.getBicepsListByApVendorAndClass(bicepVendorSelecteds.get(0).getApVendor(), classCode);
            CandidateVendorLocationItem candidateVendorLocationItemCopy = null;
            CandidateWarehouseLocationItem candidateWarehouseLocationItemCopy = null;
            for (CandidateVendorLocationItem candidateVendorLocationItem : candidateItemMaster.getCandidateVendorLocationItems()) {
                if (candidateVendorLocationItem.getKey().getVendorNumber().equals(bicepVendorSelecteds.get(0).getBicepVendorNumber())){
                    candidateVendorLocationItemCopy = (CandidateVendorLocationItem) BeanUtils.cloneBean(candidateVendorLocationItem);
                    if(candidateVendorLocationItemCopy.getCandidateVendorItemStores() !=null) {
                        candidateVendorLocationItemCopy.getCandidateVendorItemStores().clear();
                    }
                    candidateVendorLocationItemCopy.setNewData(true);
                    candidateVendorLocationItemCopy.setLastUpdatedate(LocalDateTime.now());
                    candidateVendorLocationItemCopy.setLastUpdateUserId(userInfo.getUserId());
                    if(isEdc && candidateItemMaster.getMasterPackQuantity()!=null && candidateVendorLocationItemCopy.getListCost()!=null){
                        double newVendLstCost = 0;
                        newVendLstCost = candidateVendorLocationItemCopy.getListCost()/ candidateItemMaster.getMasterPackQuantity();
                        candidateVendorLocationItemCopy.setListCost(newVendLstCost);
                    }
                    break;
                }
            }
            if(CandidateItemMaster.ITEM_KEY_TYPE_ITMCD.equalsIgnoreCase(candidateItemMaster.getItemKeyType()) && candidateItemMaster.getCandidateWarehouseLocationItems()!=null){
                candidateWarehouseLocationItemCopy = (CandidateWarehouseLocationItem)BeanUtils.cloneBean(candidateItemMaster.getCandidateWarehouseLocationItems().get(0));
            }
            CandidateVendorLocationItem candidateVendorLocationItemClone;
            CandidateVendorLocationItemKey candidateVendorLocationItemKey;
            CandidateWarehouseLocationItem candidateWarehouseLocationItemClone;
            CandidateWarehouseLocationItemKey candidateWarehouseLocationItemKey;
            List<CandidateItemWarehouseVendor> candidateItemWarehouseVendors;
            CandidateItemWarehouseVendor candidateItemWarehouseVendor;
            CandidateItemWarehouseVendorKey candidateItemWarehouseVendorKey;
            if(candidateItemMaster.isNewData()){
                candidateItemMaster.setCandidateVendorLocationItems(null);
                candidateItemMaster.setCandidateWarehouseLocationItems(null);
            }
            if(candidateVendorLocationItemCopy!=null) {
                if(candidateItemMaster.isNewData()){
                    candidateVendorLocationItemCopy.setTie(1);
                    candidateVendorLocationItemCopy.setTier(1);
                } else {
                    candidateItemMaster.setHeight(candidateVendorLocationItemCopy.getHeight());
                    candidateItemMaster.setLength(candidateVendorLocationItemCopy.getLength());
                    candidateItemMaster.setWeight(candidateVendorLocationItemCopy.getWeight());
                    candidateItemMaster.setWidth(candidateVendorLocationItemCopy.getWidth());
                    if(candidateItemMaster.getHeight()!=null&&candidateItemMaster.getLength()!=null &&candidateItemMaster.getWidth()!=null)
                    candidateItemMaster.setCube((candidateItemMaster.getHeight()*candidateItemMaster.getLength()*candidateItemMaster.getWidth())/1732);
                    if(!isWareHouseEdc) {
                        if (candidateVendorLocationItemCopy.getNestCube() != null) {
                            candidateItemMaster.setShipNestCube(candidateVendorLocationItemCopy.getNestCube());
                        }
                        if (candidateVendorLocationItemCopy.getNestMax() != null) {
                            candidateItemMaster.setShipNestMaxQuantity(candidateVendorLocationItemCopy.getNestMax());
                        }
                    }
                }
                for (BicepVendor wareHouseSeleted : bicepVendorSelecteds) {
                    for (BicepVendor wareHouseWs : lstWareHouses) {
                         if (wareHouseWs.getWarehouseNumber().equals(wareHouseSeleted.getWarehouseNumber())) {
                             candidateVendorLocationItemClone = (CandidateVendorLocationItem) BeanUtils.cloneBean(candidateVendorLocationItemCopy);
                             candidateVendorLocationItemKey = new CandidateVendorLocationItemKey();
                             candidateVendorLocationItemKey.setVendorNumber(wareHouseWs.getVendorLocationNumber());
                             candidateVendorLocationItemKey.setVendorType(CandidateVendorLocationItem.VEND_LOC_TYP_CD_WHS+" ");
                             candidateVendorLocationItemKey.setCandidateItemId(candidateItemMaster.getCandidateItemId());
                             candidateVendorLocationItemClone.setKey(candidateVendorLocationItemKey);
                             candidateVendorLocationItemClone.setCandidateItemMaster(candidateItemMaster);
                             candidateVendorLocationItemClone.setNewData(true);
                             candidateVendorLocationItemClone.setAuthdSwitch(true);
                             candidateVendorLocationItemClone.setSampProvideSwitch(CandidateVendorLocationItem.SW_Y);
                             if(candidateVendorLocationItemClone.getCandidateVendorItemFactorys()!=null && !candidateVendorLocationItemClone.getCandidateVendorItemFactorys().isEmpty()){
                                List<CandidateVendorItemFactory> psCandidateVendorItemFactories = new ArrayList<CandidateVendorItemFactory>();
                                 CandidateVendorItemFactory candidateVendorItemFactory;
                                 CandidateVendorItemFactoryKey candidateVendorItemFactoryKey;
                                for(CandidateVendorItemFactory candidateVendorItemFactoryTmp : candidateVendorLocationItemClone.getCandidateVendorItemFactorys()){
                                    candidateVendorItemFactory = (CandidateVendorItemFactory) BeanUtils.cloneBean(candidateVendorItemFactoryTmp);
                                    candidateVendorItemFactoryKey = new CandidateVendorItemFactoryKey();
                                    candidateVendorItemFactoryKey.setVendorNumber(candidateVendorLocationItemKey.getVendorNumber());
                                    candidateVendorItemFactoryKey.setVendorType(candidateVendorLocationItemKey.getVendorType());
                                    candidateVendorItemFactoryKey.setFactoryId(candidateVendorItemFactory.getKey().getFactoryId());
                                    candidateVendorItemFactoryKey.setCandidateItemId(candidateVendorItemFactory.getKey().getCandidateItemId());
                                    candidateVendorItemFactory.setKey(candidateVendorItemFactoryKey);
                                    candidateVendorItemFactory.setLastUpdateTs(LocalDate.now());
                                    candidateVendorItemFactory.setLastUpdateUserId(userInfo.getUserId());
                                    candidateVendorItemFactory.setCandidateVendorLocationItem(candidateVendorLocationItemClone);
                                    psCandidateVendorItemFactories.add(candidateVendorItemFactory);
                                }
                                 candidateVendorLocationItemClone.setCandidateVendorItemFactorys(new ArrayList<CandidateVendorItemFactory>());
                                 candidateVendorLocationItemClone.getCandidateVendorItemFactorys().addAll(psCandidateVendorItemFactories);
                            }
                            if(candidateWarehouseLocationItemCopy!=null){
                                candidateWarehouseLocationItemClone = (CandidateWarehouseLocationItem) BeanUtils.cloneBean(candidateWarehouseLocationItemCopy);
                            } else {
                                candidateWarehouseLocationItemClone = this.genCandidateWarehouseLocationItem();
                                candidateWarehouseLocationItemClone.setPurchasingStatus(PurchasingStatusCode.PURCHASING_STATUS_CODE_SUSPENDED);
                            }
                             if(wareHouseWs.getWarehouseNumber().equals(CandidateItemMaster.STRING_EDC_WHS)){
                                 candidateWarehouseLocationItemClone.setCurrentSlotNumber(CandidateWarehouseLocationItem.CURENT_SLOT_EDC);
                             } else {
                                 candidateWarehouseLocationItemClone.setCurrentSlotNumber(CandidateWarehouseLocationItem.CURENT_SLOT_DEFAULT);
                             }
                             candidateWarehouseLocationItemKey = new CandidateWarehouseLocationItemKey();
                             candidateWarehouseLocationItemKey.setWarehouseType(CandidateWarehouseLocationItemKey.WHSE_LOC_TYP_CD);
                             candidateWarehouseLocationItemKey.setWarehouseNumber(Integer.valueOf(wareHouseWs.getWarehouseNumber()));
                             candidateWarehouseLocationItemKey.setCandidateItemId(candidateItemMaster.getCandidateItemId());
                             candidateWarehouseLocationItemClone.setKey(candidateWarehouseLocationItemKey);
                             candidateWarehouseLocationItemClone.setNewData(true);
                             candidateWarehouseLocationItemClone.setLastUpdatedId(userInfo.getUserId());
                             candidateWarehouseLocationItemClone.setLastUpdatedOn(LocalDateTime.now());
                             candidateWarehouseLocationItemClone.setLastUpdateDt(LocalDate.now());
                             candidateWarehouseLocationItemClone.setCreatedBy(userInfo.getUserId());
                             candidateWarehouseLocationItemClone.setCreatedOn(LocalDateTime.now());
                             candidateWarehouseLocationItemClone.setCandidateItemMaster(candidateItemMaster);
                             candidateItemWarehouseVendors = new ArrayList<CandidateItemWarehouseVendor>();
                             candidateItemWarehouseVendor = new CandidateItemWarehouseVendor();
                             candidateItemWarehouseVendorKey = new CandidateItemWarehouseVendorKey();
                             candidateItemWarehouseVendorKey.setWarehouseType(CandidateWarehouseLocationItemKey.WHSE_LOC_TYP_CD);
                             candidateItemWarehouseVendorKey.setWarehouseNumber(Integer.valueOf(wareHouseWs.getWarehouseNumber()));
                             candidateItemWarehouseVendorKey.setVendorType(CandidateItemWarehouseVendor.VEND_LOC_TYP_CD_WHS);
                             candidateItemWarehouseVendorKey.setVendorNumber(wareHouseWs.getVendorLocationNumber());
                             candidateItemWarehouseVendor.setKey(candidateItemWarehouseVendorKey);
                             candidateItemWarehouseVendor.setLastUpdateUserId(userInfo.getUserId());
                             candidateItemWarehouseVendor.setLastUpdateDate(LocalDateTime.now());
                             candidateItemWarehouseVendor.setCandidateWarehouseLocationItem(candidateWarehouseLocationItemClone);
                             candidateItemWarehouseVendors.add(candidateItemWarehouseVendor);
                             candidateWarehouseLocationItemClone.setCandidateItemWarehouseVendors(candidateItemWarehouseVendors);
                            if(candidateItemMaster.getCandidateWarehouseLocationItems()==null) {
                                candidateItemMaster.setCandidateWarehouseLocationItems(new ArrayList<CandidateWarehouseLocationItem>());
                            }
                             candidateItemMaster.getCandidateWarehouseLocationItems().add(candidateWarehouseLocationItemClone);
                            if(candidateItemMaster.getCandidateVendorLocationItems()==null){
                                candidateItemMaster.setCandidateVendorLocationItems(new ArrayList<CandidateVendorLocationItem>());
                            }
                             candidateItemMaster.getCandidateVendorLocationItems().add(candidateVendorLocationItemClone);
                            break;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        } catch (InstantiationException e) {
            logger.error(e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        }catch(CheckedSoapException e){
            logger.error(e.getMessage());
        }
    }
    /**
     * check Empty.
     *
     * @param str .
     * @author vn55306
     */
    private boolean isNotEmpy(String str){
        boolean flag = false;
        if(StringUtils.isNotEmpty(str) && !str.trim().equals("")){
            flag = true;
        }
        return flag;
    }

    /**
     * Receives the cost details for the specified vendor location item.
     *
     * @param vendorLocationItem the vendor location item to getting the details for.
     * @author vn55306
     */
    private Cost getCostDetailsForVendorLocationItem(VendorLocationItem vendorLocationItem) {
        try {
            return this.costServiceClient.getCostDetail(
                    vendorLocationItem.getItemMaster().getProdItems().get(0).getProductMaster().getProductPrimaryScanCodeId(),
                    vendorLocationItem.getKey().getVendorNumber(),
                    vendorLocationItem.getKey().getItemCode());

        } catch (CheckedSoapException e) {
            MorphDsdItemConverter.logger.error(String.format(MorphDsdItemConverter.COST_LOOKUP_ERROR,
                    vendorLocationItem.getKey()));
        }

        return null;
    }
    /**
     * Looks up the retail for the primary UPC associated to an item.
     *
     * @param upc The upc to lookup retail for.
     * @return The retail for the primary UPC associated to the item. Will return null if there is an error.
     */
    private Retail getRetail(Long upc) {
        try {
            Retail r = this.priceServiceClient.getRegularRetail(this.defaultRetailZone, upc);
            return r;
        } catch (CheckedSoapException e) {
            MorphDsdItemConverter.logger.error(String.format(MorphDsdItemConverter.RETAIL_LOOKUP_ERROR,
                    upc));
        }

        return null;
    }
}
