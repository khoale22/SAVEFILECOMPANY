/*
 * TexasStateData
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */
package com.heb.batch.wic.service;

import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TexasStateData {

    private Map<String,ProductScanCodeWicDocument> productScanCodeWicDocuments ;

    private List<ProductScanCodeWicDocument> productScanCodeWicDocumentDeletes ;

    private List<ProductScanCodeWicDocument> productScanCodeChangeWicSwitchs ;

    private  List<String> keyDeleteChangeDatas;
    /**
     * Get the productScanCodeWicDocuments list
     *
     * @return the productScanCodeWicDocuments list
     */
    public Map<String, ProductScanCodeWicDocument> getProductScanCodeWicDocuments() {
        if(productScanCodeWicDocuments == null){
            productScanCodeWicDocuments = new HashMap<>();
        }
        return productScanCodeWicDocuments;
    }
    /**
     * Set the productScanCodeWicDocuments list
     *
     * @param productScanCodeWicDocuments the productScanCodeWicDocuments list
     */
    public void setProductScanCodeWicDocuments(Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments) {
        this.productScanCodeWicDocuments = productScanCodeWicDocuments;
    }
    public List<ProductScanCodeWicDocument> getProductScanCodeWicDocumentDeletes() {
        if(productScanCodeWicDocumentDeletes == null){
            productScanCodeWicDocumentDeletes = new ArrayList<>();
        }
        return productScanCodeWicDocumentDeletes;
    }

    public void setProductScanCodeWicDocumentDeletes(List<ProductScanCodeWicDocument> productScanCodeWicDocumentDeletes) {
        this.productScanCodeWicDocumentDeletes = productScanCodeWicDocumentDeletes;
    }

    public List<ProductScanCodeWicDocument> getProductScanCodeChangeWicSwitchs() {
        if(productScanCodeChangeWicSwitchs == null){
            productScanCodeChangeWicSwitchs = new ArrayList<>();
        }
        return productScanCodeChangeWicSwitchs;
    }

    public void setProductScanCodeChangeWicSwitchs(List<ProductScanCodeWicDocument> productScanCodeChangeWicSwitchs) {
        this.productScanCodeChangeWicSwitchs = productScanCodeChangeWicSwitchs;
    }

    public List<String> getKeyDeleteChangeDatas() {
        if(keyDeleteChangeDatas == null){
            keyDeleteChangeDatas = new ArrayList<>();
        }
        return keyDeleteChangeDatas;
    }

    public void setKeyDeleteChangeDatas(List<String> keyDeleteChangeDatas) {
        this.keyDeleteChangeDatas = keyDeleteChangeDatas;
    }
}
