/*
 * MyServiceImplTest class.
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.batch.wic.service;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.entity.WicCategory;
import com.heb.batch.wic.entity.WicSubCategory;
import com.heb.batch.wic.repository.ProductScanCodeWicRepository;
import com.heb.batch.wic.repository.WicCategoryRepository;
import com.heb.batch.wic.repository.WicSubCategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * This is MyServiceImplTest class.
 *
 * @author vn55306
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:h2-jpa-config-test.xml"})
public class MyServiceImplTest {
    private static final Logger LOGGER = LogManager.getLogger(MyServiceImplTest.class);
    @Autowired
    WicCategoryRepository wicCategoryRepository;
    @Autowired
    WicSubCategoryRepository wicSubCategoryRepository;
    @Autowired
    ProductScanCodeWicRepository productScanCodeWicRepository;
    @Test
    public void testGetAllDataWic() {
        LOGGER.info("testGetAllDataWic WicCategory");
        List<WicCategory> wicCategories = wicCategoryRepository.findAll();
        LOGGER.info("testGetAllDataWic WicCategory = "+wicCategories.size());
        Assert.assertNotNull(wicCategories);
        LOGGER.info("testGetAllDataWic wicSubCategoryRepository");
        List<WicSubCategory> weicSubCategories = wicSubCategoryRepository.findAll();
        LOGGER.info("testGetAllDataWic wicSubCategoryRepository = "+weicSubCategories.size());
        Assert.assertNotNull(weicSubCategories);
        LOGGER.info("testGetAllDataWic ProductScanCodeWic");
        List<ProductScanCodeWic> productScanCodeWics = productScanCodeWicRepository.findAll();
        LOGGER.info("testGetAllDataWic ProductScanCodeWic = "+productScanCodeWics.size());
        Assert.assertNotNull(productScanCodeWics);
    }
}
