/*
 *  MasterDataService
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.service;

import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.index.WicSubCategoryDocument;
import com.heb.batch.wic.utils.MasterDataWrapper;
import com.heb.batch.wic.webservice.vo.BaseVO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * MasterDataService.
 * 
 * @author vn03512
 * @see com.heb.batch.wic.service.impl.MasterDataServiceImpl
 */
public interface MasterDataService {

    /**
     * Process TexasStateDocument and return MasterDataWrapper
     *
     * @param item The TexasStateDocument
     * @return MasterDataWrapper
     * @throws WicException
     */
    MasterDataWrapper processTexasStateDocument(TexasStateDocument item) throws WicException;


    /**
     * Set the WicCategoryNewKeys
     * @param wicCategoryNewKeys
     */
    void setWicCategoryNewKeys(Set<String> wicCategoryNewKeys);

    /**
     * Set the WicSubCategoryNewKeys.
     * @param wicSubCategoryNewKeys
     */
    void setWicSubCategoryNewKeys(Set<String> wicSubCategoryNewKeys);
    /**
     * Set the WicSubCategoryNewKeys.
     * @param productScanCodeWicDocument
     * @return List<ProductScanCodesVO>
     * @author vn55306
     */
    List<BaseVO>  getProductScanCodeUpdateWicSwitch(List<ProductScanCodeWicDocument> productScanCodeWicDocument);
    /**
     * Get Sub category by cat and sub cat.
     * @param wicCatId wic category id.
     * @param wicSubCatId wic sub category id.
     * @return WicSubCategoryDocument.
     */
    Optional<WicSubCategoryDocument> getSubCategory(String wicCatId, String wicSubCatId);

    /**
     * Process wic sub category and return MasterDataWrapper
     *
     * @param item the TexasStateDocument.
     * @return MasterDataWrapper
     * @throws WicException
     */
    MasterDataWrapper processTexasStateWicSubCatDocument(TexasStateDocument item) throws WicException;

}
