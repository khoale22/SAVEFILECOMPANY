/*
 * MasterDataWrapperWriter
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.writer;

import com.heb.batch.wic.dao.JobParamDAO;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import com.heb.batch.wic.index.repository.ProductScanCodeWicIndexRepository;
import com.heb.batch.wic.index.repository.WicCategoryIndexRepository;
import com.heb.batch.wic.index.repository.WicSubCategoryIndexRepository;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.service.MasterDataService;
import com.heb.batch.wic.service.TexasStateData;
import com.heb.batch.wic.utils.MasterDataWrapper;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.webservice.ProductScanCodeServiceClient;
import com.heb.batch.wic.webservice.ProductScanCodeWicServiceClient;
import com.heb.batch.wic.webservice.WicCategoryServiceClient;
import com.heb.batch.wic.webservice.WicSubCategoryServiceClient;
import com.heb.batch.wic.webservice.vo.BaseVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import java.util.*;

/**
 * Persists MasterDataWrapper to the db.
 *
 * @author vn03512
 * @since 1.0.0
 */
public class MasterDataWrapperWriter implements ItemWriter<MasterDataWrapper>, StepExecutionListener {
    private static final Logger LOGGER = LogManager.getLogger(MasterDataWrapperWriter.class);
    @Autowired
    private TexasStateData texasStateData;
    @Autowired
    private ProductScanCodeServiceClient productScanCodeServiceClient;
    @Autowired
    private ProductScanCodeWicServiceClient productScanCodeWicServiceClient;
    @Autowired
    private WicCategoryServiceClient wicCategoryServiceClient;
    @Autowired
    private WicSubCategoryServiceClient wicSubCategoryServiceClient;
    @Autowired
    private WicCategoryIndexRepository wicCategoryIndexRepository;
    @Autowired
    private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;
    @Autowired
    private ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository;
    @Autowired
    private MasterDataService masterDataService;
    @Autowired
    JobParamDAO jobParamDAO;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageSource messageSource;
    private String updateProductScanCodeDaoUrl;
    private String insertWicCatDaoUrl;
    private String insertWicSubCatDaoUrl;
    private String insertProductScanCodeWicDaoUrl;
    private String updateProductScanCodeWicDaoUrl;
    private String apiKey;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.apiKey = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_APIKEY);
        this.updateProductScanCodeDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_UPDATE_PRODUCT_SCAN_CODE_ORACLE_URL);
        this.insertWicCatDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INSERT_WIC_CAT_ORACLE_URL);
        this.insertWicSubCatDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INSERT_WIC_SUB_CAT_ORACLE_URL);
        this.insertProductScanCodeWicDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INSERT_PRODUCT_SCAN_CODE_WIC_ORACLE_URL);
        this.updateProductScanCodeWicDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_UPDATE_PRODUCT_SCAN_CODE_WIC_ORACLE_URL);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("MasterDataWrapperWriter afterStep ");
        List<BaseVO> productScanCodesList = this.masterDataService.getProductScanCodeUpdateWicSwitch(new ArrayList<ProductScanCodeWicDocument>(texasStateData.getProductScanCodeWicDocuments().values()));
        if(!productScanCodesList.isEmpty()) {
            LOGGER.info("CHANGE WIC_SW IN PROD_SCN_CODES = "+productScanCodesList.size());
            List<BaseVO> productScanCodeSends;
            try {
                for(BaseVO baseVO : productScanCodesList) {
                    productScanCodeSends = new ArrayList<>();
                    productScanCodeSends.add(baseVO);
                    this.productScanCodeServiceClient.submitRequest(productScanCodeSends, updateProductScanCodeDaoUrl, apiKey);
                }
            } catch (Exception e){
                LOGGER.error(e.getMessage(),e);
            }
        }
        return null;
    }

    /**
     * Called by the Spring Batch framework to save WIC_CAT, WIC_SUB_CAT, PROD_SCN_CD_WIC, PROD_SCN_CODES to the index.
     *
     * @param masterDataWrapper The list of MasterDataWrapper to save.
     * @throws Exception
     */
    @Override
    public void write(List<? extends MasterDataWrapper> masterDataWrapper) throws Exception {
        Map<String,BaseVO> insertedProductScanCodeWicMaps = new HashMap<>();
        Map<String,BaseVO> updatedProductScanCodeWicMaps = new HashMap<>();
        String keyProductScanCodeWic;
        List<BaseVO> wicCategoryList = new ArrayList<>();
        List<BaseVO> wicSubCategoryList = new ArrayList<>();
        // Get list of WIC_CAT, WIC_SUB_CAT, PROD_SCN_CODES_WIC
        for(MasterDataWrapper record : masterDataWrapper) {
            if(record.getWicCategory() != null)
                wicCategoryList.add(record.getWicCategory());
            if(record.getWicSubCategory() != null)
                wicSubCategoryList.add(record.getWicSubCategory());
            if(record.getProductScanCodeWic()!=null){
                keyProductScanCodeWic = ProductScanCodeWicDocument.generateId(record.getProductScanCodeWic().getWicAplId(), record.getProductScanCodeWic().getScnCdId(),
                        record.getProductScanCodeWic().getWicCatId(), record.getProductScanCodeWic().getWicSubCatId());
                if(WicConstants.ADD.equals(record.getProductScanCodeWic().getActionCode())){
                    insertedProductScanCodeWicMaps.put(keyProductScanCodeWic,record.getProductScanCodeWic());
                } else if(WicConstants.UPDATE.equals(record.getProductScanCodeWic().getActionCode())){
                    updatedProductScanCodeWicMaps.put(keyProductScanCodeWic,record.getProductScanCodeWic());
                }
            }
        }
        // insert Wic Category
        this.insertWicCategory(wicCategoryList);
        // insert Wic Sub Category
        this.insertWicSubCategory(wicSubCategoryList);
        // insert PROD_SCN_CODE_WIC
        this.insertProductScanCodeWics(insertedProductScanCodeWicMaps);
        // update PROD_SCN_CODE_WIC
        this.updateProductScanCodeWics(updatedProductScanCodeWicMaps);
        try{
            if(!this.texasStateData.getProductScanCodeWicDocuments().isEmpty()) {
                this.productScanCodeWicIndexRepository.saveAll(this.texasStateData.getProductScanCodeWicDocuments().values());
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
    }
    private void insertWicCategory(List<BaseVO> wicCategoryList) throws WicException{
        if(!wicCategoryList.isEmpty()){
            try {
            this.wicCategoryServiceClient.submitRequest(wicCategoryList,this.insertWicCatDaoUrl,this.apiKey);
                wicCategoryIndexRepository.saveAll(WicUtil.convertWicCategoryVoToWicCategoryDocument(wicCategoryList));
            }catch (WicException e){
                this.sendProductionSupportApiCorrupt(this.insertWicCatDaoUrl);
                throw new WicException(e.getMessage());
            }
        }
    }
    private void insertWicSubCategory(List<BaseVO> wicSubCategoryList) throws WicException{
        if(!wicSubCategoryList.isEmpty()){
            try {
            this.wicSubCategoryServiceClient.submitRequest(wicSubCategoryList,this.insertWicSubCatDaoUrl,this.apiKey);
                wicSubCategoryIndexRepository.saveAll(WicUtil.convertWicSubCategoryVoToWicSubCategoryDocument(wicSubCategoryList));
            }catch (WicException e){
                this.sendProductionSupportApiCorrupt(this.insertWicSubCatDaoUrl);
                throw new WicException(e.getMessage());
            }
        }
    }
    private void insertProductScanCodeWics(Map<String,BaseVO> insertedProductScanCodeWicMaps) throws WicException{
        if(insertedProductScanCodeWicMaps.size()>0){
            try{
            List<BaseVO> insertedProductScanCodeWicList = new ArrayList<>(insertedProductScanCodeWicMaps.values());
            this.productScanCodeWicServiceClient.submitRequest(insertedProductScanCodeWicList,this.insertProductScanCodeWicDaoUrl,this.apiKey);
            }catch (WicException e){
                this.sendProductionSupportApiCorrupt(this.insertProductScanCodeWicDaoUrl);
                throw new WicException(e.getMessage());
        }
    }
    }
    private void updateProductScanCodeWics(Map<String,BaseVO> updatedProductScanCodeWicMaps) throws WicException{
        if(updatedProductScanCodeWicMaps.size()>0){
            List<BaseVO> updatedProductScanCodeWicList = new ArrayList<>(updatedProductScanCodeWicMaps.values());
            List<BaseVO> updatedProductScanCdWicListApi;
            try{
            for(BaseVO baseVO:updatedProductScanCodeWicList) {
                updatedProductScanCdWicListApi = new ArrayList<>();
                updatedProductScanCdWicListApi.add(baseVO);
                this.productScanCodeWicServiceClient.submitRequest(updatedProductScanCdWicListApi,this.updateProductScanCodeWicDaoUrl,this.apiKey);
            }
            }catch (WicException e){
                this.sendProductionSupportApiCorrupt(this.updateProductScanCodeWicDaoUrl);
                throw new WicException(e.getMessage());
            }
        }
        }
    private void sendProductionSupportApiCorrupt(String url){
        String subject;
        String content;
        String fromAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_FROM);
        String toAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT);
        subject = messageSource.getMessage("wic.email.production.subject", null, Locale.US);
        content = messageSource.getMessage("wic.email.production.javadao.corrupt", new Object[] { url}, Locale.US);
        this.emailService.sendProductionSupportCorruptFile(subject,content,fromAddress,toAddress);
    }
}