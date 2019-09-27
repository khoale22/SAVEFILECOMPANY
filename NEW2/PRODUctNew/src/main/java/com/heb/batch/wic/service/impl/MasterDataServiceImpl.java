/*
 * $Id: MasterDataServiceImpl.java,v 1.9.2.5 2015/10/30 09:58:59 vn03512 Exp $
 *
 * Copyright (c) 2013 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.service.impl;

import com.heb.batch.wic.entity.ProductScanCodes;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.*;
import com.heb.batch.wic.index.repository.*;
import com.heb.batch.wic.repository.ProductScanCodesRepository;
import com.heb.batch.wic.service.MasterDataService;
import com.heb.batch.wic.service.TexasStateData;
import com.heb.batch.wic.utils.MasterDataWrapper;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.webservice.vo.BaseVO;
import com.heb.batch.wic.webservice.vo.ProductScanCodesVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * MasterDataServiceImpl class.
 *
 * @author vn03512
 */
@Service
public class MasterDataServiceImpl implements MasterDataService {
	private static final Logger LOGGER = LogManager.getLogger(MasterDataServiceImpl.class);
    @Autowired
    private TexasStateIndexRepository texasStateIndexRepository;
    @Autowired
    private ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository;
    @Autowired
    private WicCategoryIndexRepository wicCategoryIndexRepository;
    @Autowired
    private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;
    @Autowired
    private TexasStateData texasStateData;
    @Autowired
    private ProductScanCodesRepository productScanCodesRepository;
     private Set<String> wicCategoryNewKeys;
     private Set<String> wicSubCategoryNewKeys;


	/**
	 * Process TexasStateDocument and return MasterDataWrapper
	 *
	 * @param item The TexasStateDocument
	 * @return MasterDataWrapper
	 */
	@Override
	public MasterDataWrapper processTexasStateDocument(TexasStateDocument item) throws WicException {
        MasterDataWrapper masterDataWrapper = new MasterDataWrapper();
        Optional<TexasStateDocument> texasStateDocumentOptional = this.texasStateIndexRepository.findById(item.getId());
        if(texasStateDocumentOptional.isPresent()) {
            TexasStateDocument texasStateDocument = texasStateDocumentOptional.get();
            // check wic category existed
            if (!wicCategoryNewKeys.contains(texasStateDocument.getWicCatId())) {
                Optional<WicCategoryDocument> wicCategoryDocuments = wicCategoryIndexRepository
                        .findById(Long.valueOf(texasStateDocument.getWicCatId()).toString());
                if (!wicCategoryDocuments.isPresent()) {
                    // Insert new WicCategory
                    masterDataWrapper.newWicCategory(texasStateDocument);
                    wicCategoryNewKeys.add(texasStateDocument.getWicCatId());
                }
            }
            // check Wic Subcategory existed
            String keyWicSubWic = texasStateDocument.getWicCatId().concat("_").concat(texasStateDocument.getWicSubCatId());
            if (!wicSubCategoryNewKeys.contains(keyWicSubWic)) {
                String subCategoryDocumentId = WicSubCategoryDocument.generateId(texasStateDocument.getWicCatId(), texasStateDocument.getWicSubCatId());
                Optional<WicSubCategoryDocument> wicSubCategoryDocuments = wicSubCategoryIndexRepository.findById(subCategoryDocumentId);
                if (!wicSubCategoryDocuments.isPresent()) {
                    // Insert new WicSubCategory
                    masterDataWrapper.newWicSubCategory(texasStateDocument, WicConstants.ADD);
                    wicSubCategoryNewKeys.add(keyWicSubWic);
                }
            }
            this.processTexasStateDocumentIfProScnExist(texasStateDocument, masterDataWrapper);
        }
		return masterDataWrapper;
	}

	private void processTexasStateDocumentIfProScnExist(TexasStateDocument texasStateDocument, MasterDataWrapper masterDataWrapper){
        String wicCategoryDesciption = "";
        String wicSubCategoryDescription = "";
        String keyProductScanCodeWic = "";
        wicCategoryDesciption = texasStateDocument.getWicCategoryDesc();
        wicSubCategoryDescription = texasStateDocument.getWicSubCategoryDesc();
            // check existing in productScanCodeWic
        keyProductScanCodeWic = ProductScanCodeWicDocument.generateId(texasStateDocument.getWicAplId(), texasStateDocument.getScnCdId(), texasStateDocument.getWicCatId(), texasStateDocument.getWicSubCatId());
        Optional<ProductScanCodeWicDocument> productScanCodeWicDocuments = productScanCodeWicIndexRepository
                .findById(keyProductScanCodeWic);
        ProductScanCodeWicDocument productScanCodeWicDocument = null;
        if (productScanCodeWicDocuments.isPresent()) {
            // check create user by TXSTATE
            if (productScanCodeWicDocuments.get().getCre8UId() != null && WicConstants.TXSTATE_USER.equals(productScanCodeWicDocuments.get().getCre8UId().trim())) {
                productScanCodeWicDocument = productScanCodeWicDocuments.get();
                this.processProductScanCodeWicChange(texasStateDocument, masterDataWrapper, productScanCodeWicDocument, wicCategoryDesciption, wicSubCategoryDescription, keyProductScanCodeWic);
            }
        } else {
            // Set new ProductScanCodeWic
            masterDataWrapper.setProductScanCodeWicVOTexasStateDocument(texasStateDocument, WicConstants.ADD);
            // New document of productScanCodeWic repository
            productScanCodeWicDocument = new ProductScanCodeWicDocument();
            productScanCodeWicDocument.setAction(WicConstants.ADD);
            productScanCodeWicDocument.setFieldFromProductScanCodeWicVOs(masterDataWrapper.getProductScanCodeWic());
            productScanCodeWicDocument.setWicCategoryDesc(wicCategoryDesciption);
            productScanCodeWicDocument.setWicSubCategoryDesc(wicSubCategoryDescription);
            // check wicable
            if (WicConstants.YES.equals(productScanCodeWicDocument.getWicSw())) {
                // Append productScanCodeWicDocument to texasStateData
                texasStateData.getProductScanCodeWicDocuments().put(keyProductScanCodeWic, productScanCodeWicDocument);
            } else {
                masterDataWrapper.setProductScanCodeWic(null);
            }
        }
    }
    private void processProductScanCodeWicChange(TexasStateDocument texasStateDocument, MasterDataWrapper masterDataWrapper,ProductScanCodeWicDocument productScanCodeWicDocument,String wicCategoryDesciption,String wicSubCategoryDescription,String keyProductScanCodeWic){
	    if (masterDataWrapper.setProductScanCodeWicChange(texasStateDocument, productScanCodeWicDocument)) {
            productScanCodeWicDocument.setAction(WicConstants.UPDATE);
            productScanCodeWicDocument.setWicCategoryDesc(wicCategoryDesciption);
            productScanCodeWicDocument.setWicSubCategoryDesc(wicSubCategoryDescription);
            ProductScanCodeWicDocument productScanCodeWicDocumentOrigin = null;
            try {
                productScanCodeWicDocumentOrigin = (ProductScanCodeWicDocument) BeanUtils.cloneBean(productScanCodeWicDocument);
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (InstantiationException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (InvocationTargetException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (NoSuchMethodException e) {
                LOGGER.error(e.getMessage(),e);
            }
            // update data change
            productScanCodeWicDocument.setFieldFromProductScanCodeWicVOs(masterDataWrapper.getProductScanCodeWic());
            // check wicable
            if(WicConstants.YES.equals(productScanCodeWicDocument.getWicSw())) {
                // Append productScanCodeWicDocument to texasStateData
                texasStateData.getProductScanCodeWicDocuments().put(keyProductScanCodeWic, productScanCodeWicDocument);
            } else {
                if(productScanCodeWicDocumentOrigin == null){
                    productScanCodeWicDocumentOrigin = productScanCodeWicDocument;
                }
                productScanCodeWicDocumentOrigin.setAction(WicConstants.DELETE);
                productScanCodeWicDocumentOrigin.setWicSw(WicConstants.NO);
                masterDataWrapper.setProductScanCodeWic(null);
                String keyDelete = String.valueOf(productScanCodeWicDocumentOrigin.getUpc()).concat(String.valueOf(productScanCodeWicDocumentOrigin.getWicAplId()))
                            .concat(String.valueOf(productScanCodeWicDocumentOrigin.getWicCategoryId())).concat(String.valueOf(productScanCodeWicDocumentOrigin.getWicSubCategoryId()));
                if(!this.texasStateData.getKeyDeleteChangeDatas().contains(keyDelete)) {
                    this.texasStateData.getKeyDeleteChangeDatas().add(keyDelete);
                    this.texasStateData.getProductScanCodeWicDocumentDeletes().add(productScanCodeWicDocumentOrigin);
                }
            }
                // check condition sent WICE
	    } else if(masterDataWrapper.getProductScanCodeWic()!=null &&  WicConstants.NO.equals(productScanCodeWicDocument.getWicSw())){
	        masterDataWrapper.setProductScanCodeWic(null);
	    }

    }

    /**
     * Get the ProductScanCodeWicIndexRepository.
     * @return ProductScanCodeWicIndexRepository
     */
    public ProductScanCodeWicIndexRepository getProductScanCodeWicIndexRepository() {
        return productScanCodeWicIndexRepository;
    }

    /**
     * Set the ProductScanCodeWicIndexRepository.
     * @param productScanCodeWicIndexRepository
     */
    public void setProductScanCodeWicIndexRepository(ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository) {
        this.productScanCodeWicIndexRepository = productScanCodeWicIndexRepository;
    }

    /**
     * Get the WicCategoryIndexRepository.
     * @return WicCategoryIndexRepository
     */
    public WicCategoryIndexRepository getWicCategoryIndexRepository() {
        return wicCategoryIndexRepository;
    }

    /**
     * Set the WicCategoryIndexRepository.
     * @param wicCategoryIndexRepository
     */
    public void setWicCategoryIndexRepository(WicCategoryIndexRepository wicCategoryIndexRepository) {
        this.wicCategoryIndexRepository = wicCategoryIndexRepository;
    }

    /**
     * Get the WicSubCategoryIndexRepository.
     * @return WicSubCategoryIndexRepository
     */
    public WicSubCategoryIndexRepository getWicSubCategoryIndexRepository() {
        return wicSubCategoryIndexRepository;
    }

    /**
     * Set the WicSubCategoryIndexRepository.
     * @param wicSubCategoryIndexRepository
     */
    public void setWicSubCategoryIndexRepository(WicSubCategoryIndexRepository wicSubCategoryIndexRepository) {
        this.wicSubCategoryIndexRepository = wicSubCategoryIndexRepository;
    }

    /**
     * Get the WicCategoryNewKeys.
     * @return wicCategoryNewKeys
     */
    public Set<String> getWicCategoryNewKeys() {
        return wicCategoryNewKeys;
    }

    /**
     * Set the WicCategoryNewKeys
     * @param wicCategoryNewKeys
     */
    public void setWicCategoryNewKeys(Set<String> wicCategoryNewKeys) {
        this.wicCategoryNewKeys = wicCategoryNewKeys;
    }

    /**
     * Get the WicSubCategoryNewKeys.
     * @return wicSubCategoryNewKeys
     */
    public Set<String> getWicSubCategoryNewKeys() {
        return wicSubCategoryNewKeys;
    }

    /**
     * Set the WicSubCategoryNewKeys.
     * @param wicSubCategoryNewKeys
     */
    public void setWicSubCategoryNewKeys(Set<String> wicSubCategoryNewKeys) {
        this.wicSubCategoryNewKeys = wicSubCategoryNewKeys;
    }

    /**
     * Set the WicSubCategoryNewKeys.
     * @param productScanCodeWicDocuments
     * @return List<BaseVO>
     * @author vn55306
     */
    @Override
    public List<BaseVO> getProductScanCodeUpdateWicSwitch(List<ProductScanCodeWicDocument> productScanCodeWicDocuments){
        List<BaseVO> productScanCodesVOs = new ArrayList<>();
        List<Long> upcChecks = new ArrayList<>();
        List<ProductScanCodeWicDocument> productScanCodeWicDocumentWicSwitch;
        String wicSwitch;
        boolean flagWicExisted;
        if(productScanCodeWicDocuments!=null && !productScanCodeWicDocuments.isEmpty()) {
            try {
                for (ProductScanCodeWicDocument productScanCodeWicDocument : productScanCodeWicDocuments) {
                    flagWicExisted = true;
                    if (!upcChecks.contains(productScanCodeWicDocument.getUpc())) {
                        upcChecks.add(productScanCodeWicDocument.getUpc());
                        // check Wic Effect
                        productScanCodeWicDocumentWicSwitch =  this.productScanCodeWicIndexRepository.findByUpc(productScanCodeWicDocument.getUpc());
                        if(productScanCodeWicDocumentWicSwitch!=null && !productScanCodeWicDocumentWicSwitch.isEmpty()) {
                            productScanCodeWicDocumentWicSwitch = this.productScanCodeWicIndexRepository.findByUpcAndWicSw(productScanCodeWicDocument.getUpc(), WicConstants.YES);
                            wicSwitch = this.checkWicEff(productScanCodeWicDocumentWicSwitch);
                        } else {
                            wicSwitch = WicConstants.NO;
                            flagWicExisted = false;
                        }
                        Optional<ProductScanCodes> productScanCodesOptional = this.productScanCodesRepository.findById(productScanCodeWicDocument.getUpc());
                        // check have change WIC_SW in PROD_SCAN_CD
                        this.checkWicSw(productScanCodesOptional, flagWicExisted, wicSwitch, productScanCodeWicDocument, productScanCodesVOs);
                    }
                }
            }catch (Exception e){
                LOGGER.info("getProductScanCodeUpdateWicSwitch = "+e.getMessage());
            }
        }
        return productScanCodesVOs;
    }

    private String checkWicEff(List<ProductScanCodeWicDocument> productScanCodeWicDocumentWicSwitch){
        String wicSwitch = WicConstants.NO;
        if (productScanCodeWicDocumentWicSwitch != null && !productScanCodeWicDocumentWicSwitch.isEmpty()) {
            wicSwitch = WicConstants.YES;
        }
        return wicSwitch;
    }

    private void checkWicSw(Optional<ProductScanCodes> productScanCodesOptional,
                            boolean flagWicExisted, String wicSwitch, ProductScanCodeWicDocument productScanCodeWicDocument, List<BaseVO> productScanCodesVOs){
        if (productScanCodesOptional.isPresent() && (!flagWicExisted || !wicSwitch.equalsIgnoreCase(productScanCodesOptional.get().getWicSwitch()))) {
            ProductScanCodesVO productScanCodesVO = new ProductScanCodesVO();
            productScanCodesVO.setScnCdId(productScanCodeWicDocument.getUpc());
            productScanCodesVO.setWicSw(wicSwitch);
            productScanCodesVO.setLstUpdtTs(WicUtil.getCurrentTimeStamp());
            productScanCodesVO.setLstUpdtUid(WicConstants.WIC_USER);
            productScanCodesVO.setWicLstUpdtTs(WicUtil.getCurrentTimeStamp());
            productScanCodesVO.setWicUpdtUsrId(WicConstants.WIC_USER);
            productScanCodesVO.setActionCode(WicConstants.UPDATE);
            productScanCodesVO.setVcUpdtUsrId(WicConstants.WIC_USER);
            productScanCodesVO.setSystemEnvironment(WicConstants.BATCH_ENVIRONMENT);
            if (!flagWicExisted) {
                productScanCodesVO.setWicAplId(NumberUtils.LONG_ZERO);
            } else {
                productScanCodesVO.setWicAplId(productScanCodeWicDocument.getWicAplId());
            }
            productScanCodesVOs.add(productScanCodesVO);
        }
    }

    /**
     * Get Sub category by cat and sub cat.
     * @param wicCatId wic category id.
     * @param wicSubCatId wic sub category id.
     * @return WicSubCategoryDocument.
     */
    public Optional<WicSubCategoryDocument> getSubCategory(String wicCatId, String wicSubCatId){
        String subCategoryDocumentId = WicSubCategoryDocument.generateId(wicCatId, wicSubCatId);
        Optional<WicSubCategoryDocument> wicSubCategoryDocuments = wicSubCategoryIndexRepository.findById(subCategoryDocumentId);
        return wicSubCategoryDocuments;
    }

    /**
     * Process wic sub category and return MasterDataWrapper
     *
     * @param item the TexasStateDocument.
     * @return MasterDataWrapper
     */
    @Override
    public MasterDataWrapper processTexasStateWicSubCatDocument(TexasStateDocument item) {
        MasterDataWrapper masterDataWrapper = new MasterDataWrapper();
        Optional<TexasStateDocument> texasStateDocumentOptional = this.texasStateIndexRepository.findById(item.getId());
        boolean isChangeData = false;
        if(texasStateDocumentOptional.isPresent()) {
            TexasStateDocument texasStateDocument = texasStateDocumentOptional.get();
            // check wic category existed
            if (!wicCategoryNewKeys.contains(texasStateDocument.getWicCatId())) {
                Optional<WicCategoryDocument> wicCategoryDocuments = wicCategoryIndexRepository
                        .findById(Long.valueOf(texasStateDocument.getWicCatId()).toString());
                if (!wicCategoryDocuments.isPresent()) {
                    // Insert new WicCategory
                    masterDataWrapper.newWicCategory(texasStateDocument);
                    wicCategoryNewKeys.add(texasStateDocument.getWicCatId());
                    isChangeData = true;
                }
            }
            // check Wic Subcategory existed
            String keyWicSubWic = texasStateDocument.getWicCatId().concat("_").concat(texasStateDocument.getWicSubCatId());
            if (!wicSubCategoryNewKeys.contains(keyWicSubWic)) {
                String subCategoryDocumentId = WicSubCategoryDocument.generateId(texasStateDocument.getWicCatId(), texasStateDocument.getWicSubCatId());
                Optional<WicSubCategoryDocument> wicSubCategoryDocuments = wicSubCategoryIndexRepository.findById(subCategoryDocumentId);
                if (!wicSubCategoryDocuments.isPresent()) {
                    // Insert new WicSubCategory
                    masterDataWrapper.newWicSubCategory(texasStateDocument, WicConstants.ADD);
                    wicSubCategoryNewKeys.add(keyWicSubWic);
                    isChangeData = true;
                }else if(!StringUtils.trimToEmpty(wicSubCategoryDocuments.get().getDescription()).equals(StringUtils.trimToEmpty(texasStateDocument.getWicSubCategoryDesc()))){
                    masterDataWrapper.newWicSubCategory(texasStateDocument, WicConstants.UPDATE);
                    isChangeData = true;
                }
            }
        }
        return isChangeData ? masterDataWrapper : null;
    }
}