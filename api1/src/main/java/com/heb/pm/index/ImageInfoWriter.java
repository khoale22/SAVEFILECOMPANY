/*
 * ImageInfoWriter
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
import com.heb.pm.repository.ImageInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writer for the ProductScanImageURI batch job.
 *
 * @author vn70529
 * @since 2.39.0
 */
public class ImageInfoWriter implements ItemWriter<ProductScanImageURI> {
    public static final Logger logger = LoggerFactory.getLogger(ImageInfoWriter.class);

    private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called write with null or empty list.";

    @Autowired
    private ImageInfoRepository imageInfoRepository;

    /**
     * Called by the Spring Batch framework to save ProductScanImageURI.
     *
     * @param items The list of ProductScanImageURI to save.
     * @throws Exception
     */
    @Override
    @CoreTransactional
    public void write(List<? extends ProductScanImageURI> items) throws Exception {
        if (items == null || items.isEmpty()) {
            ImageInfoWriter.logger.debug(EMPTY_LIST_LOGGER_MESSAGE);
            return;
        }
        this.imageInfoRepository.save(items);
    }

}
