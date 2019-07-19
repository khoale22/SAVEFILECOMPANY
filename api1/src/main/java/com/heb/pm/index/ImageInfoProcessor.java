/*
 * ImageInfoProcessor
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.ProductScanImageURI;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;

/**
 * Processor for the ProductScanImageURI batch job.
 *
 * @author vn70529
 * @since 2.39.0
 */
public class ImageInfoProcessor implements ItemProcessor<ProductScanImageURI, ProductScanImageURI> {

    private static final String SINGLE_SPACE = " ";

    /**
     * Called by the Spring Batch framework. It will set data for ProductScanImageURI and return it.
     *
     * @param productScanImageURI The ProductScanImageURI.
     * @return The ProductScanImageURI.
     * @throws Exception
     */
    @Override
    @CoreTransactional
    public ProductScanImageURI process(ProductScanImageURI productScanImageURI) throws Exception {
        productScanImageURI.setImageStatusCode(ProductScanImageURI.IMAGE_REJECT);
        if(StringUtils.isEmpty(productScanImageURI.getApplicationSource())) productScanImageURI.setApplicationSource(SINGLE_SPACE);
        return productScanImageURI;
    }
}
