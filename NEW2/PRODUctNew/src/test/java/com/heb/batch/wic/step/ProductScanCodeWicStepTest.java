/*
 * ProductScanCodeWicStepTest
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.batch.wic.step;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.entity.ProductScanCodeWicKey;
import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import com.heb.batch.wic.index.repository.ProductScanCodeWicIndexRepository;
import com.heb.batch.wic.index.repository.WicCategoryIndexRepository;
import com.heb.batch.wic.index.repository.WicSubCategoryIndexRepository;
import com.heb.batch.wic.processor.ProductScanCodeWicProcessor;
import com.heb.batch.wic.reader.ProductScanCodeWicReader;
import com.heb.batch.wic.repository.ProductScanCodeWicRepository;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.writer.ProductScanCodeWicWriter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Unit tests for ProductScanCodeWic step (step 2: Caching ProductScanCodeWic).
 *
 * @author vn03512
 * @since 1.0.0
 */
@Transactional(transactionManager = "jpaTransactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:product-scan-code-wic-config-test.xml"})
public class ProductScanCodeWicStepTest {
    @Autowired
    private ProductScanCodeWicIndexRepository productScanCodesIndexRepository;
    @Autowired
    private ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository;
    @Autowired
    private WicCategoryIndexRepository wicCategoryIndexRepository;
    @Autowired
    private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;

	@Autowired
	private ProductScanCodeWicRepository productScanCodeWicRepository;
    @Autowired
    private ProductScanCodeWicProcessor productScanCodeWicProcessor;
    @Autowired
    private ProductScanCodeWicReader productScanCodeWicReader;
    @Autowired
    private ProductScanCodeWicWriter productScanCodeWicWriter;

    public static StepExecution createStepExecution() {
        JobParameters jobParameters = new JobParameters();
        JobInstance jobInstance = new JobInstance(12L, "job");
        JobExecution jobExecution = new JobExecution(jobInstance, 123L, jobParameters, null);
        StepExecution stepExecution = jobExecution.createStepExecution("step");
        stepExecution.setId(1234L);
        return stepExecution;
    }

    @Before
    public void before() {
        productScanCodesIndexRepository.deleteAll();
        productScanCodeWicIndexRepository.deleteAll();
        wicCategoryIndexRepository.deleteAll();
        wicSubCategoryIndexRepository.deleteAll();

        productScanCodeWicReader.beforeStep(createStepExecution());
        productScanCodeWicReader.setPageSize(50);
    }

    @After
    public void after() {
        productScanCodeWicReader.afterStep(createStepExecution());
    }
    
    @Test
	public void testReadData() throws Exception {
		long numberOfRecords = productScanCodeWicRepository.count();
		ProductScanCodeWic productScanCodeWic;
		for (int i = 1; i <= numberOfRecords; i++) {
			productScanCodeWic = productScanCodeWicReader.read();
			Assert.assertNotNull(productScanCodeWic);
		}
		productScanCodeWic = productScanCodeWicReader.read();
		Assert.assertNull(productScanCodeWic);
	}
    
    @Test
	public void testProcessData() throws Exception {
    	
    	// Given
		long numberOfRecords = productScanCodeWicRepository.count();
		ProductScanCodeWic productScanCodeWic;
		ProductScanCodeWicDocument productScanCodeWicDocument;
		for (int i = 1; i <= numberOfRecords; i++) {
			productScanCodeWic = productScanCodeWicReader.read();
			
			// When
			productScanCodeWicDocument = productScanCodeWicProcessor.process(productScanCodeWic);
			
			// Then
			ProductScanCodeWicKey key = productScanCodeWic.getKey();
			Assert.assertEquals(productScanCodeWicDocument.getId(),
					ProductScanCodeWicDocument.generateId(key.getWicApprovedProductListId(), key.getUpc(),
							key.getWicCategoryId(), key.getWicSubCatId()));
			Assert.assertEquals(productScanCodeWicDocument.getWicAplId(), key.getWicApprovedProductListId());
			Assert.assertEquals(productScanCodeWicDocument.getUpc(), key.getUpc());
			Assert.assertEquals(productScanCodeWicDocument.getWicCategoryId(), key.getWicCategoryId());
			Assert.assertEquals(productScanCodeWicDocument.getWicSubCategoryId(), key.getWicSubCatId());
			Assert.assertEquals(productScanCodeWicDocument.getLebSwitch(), productScanCodeWic.getLebSwitch());
			Assert.assertEquals(productScanCodeWicDocument.getWicDescription(), productScanCodeWic.getWicDescription());
			Assert.assertEquals(productScanCodeWicDocument.getWicPackageSize(), productScanCodeWic.getWicPackageSize());
			Assert.assertEquals(productScanCodeWicDocument.getEffDt(), productScanCodeWic.getEffDt());
			Assert.assertEquals(productScanCodeWicDocument.getEndDt(), productScanCodeWic.getEndDt());
			Assert.assertEquals(productScanCodeWicDocument.getWicUntTxt(), productScanCodeWic.getWicUntTxt());
			Assert.assertEquals(productScanCodeWicDocument.getWicBnFtQty(), productScanCodeWic.getWicBnFtQty());
			Assert.assertEquals(productScanCodeWicDocument.getWicBnftUntTxt(), productScanCodeWic.getWicBnftUntTxt());
			Assert.assertEquals(productScanCodeWicDocument.getWicPrcAmt(), productScanCodeWic.getWicPrcAmt());
			Assert.assertEquals(productScanCodeWicDocument.getWicPrcCd(), productScanCodeWic.getWicPrcCd());
			Assert.assertEquals(productScanCodeWicDocument.getWicCrdAcptId(), productScanCodeWic.getWicCrdAcptId());
			Assert.assertEquals(productScanCodeWicDocument.getCre8UId(), productScanCodeWic.getCre8UId());
			Assert.assertEquals(productScanCodeWicDocument.getWicSw(), WicUtil.checkWicSwitch(productScanCodeWicDocument) ? WicConstants.YES : WicConstants.NO);
		}
	}

    @Test
	public void testWriteData() throws Exception {
    	
    	// Given
		long numberOfRecords = productScanCodeWicRepository.count();
		ProductScanCodeWic inputProductScanCodeWic;
		ProductScanCodeWicKey key;
		Map<String, ProductScanCodeWic> inputProductScanCodeWicMap = new HashMap<>();
		ProductScanCodeWicDocument pscwDocument;
		List<ProductScanCodeWicDocument> productScanCodeWicDocuments = new ArrayList<>();
		Iterator<ProductScanCodeWicDocument> outputProductScanCodeWicDocumentList;
		ProductScanCodeWicDocument outputProductScanCodeWicDocument;
		for (int i = 1; i <= numberOfRecords; i++) {
			inputProductScanCodeWic = productScanCodeWicReader.read();
			key = inputProductScanCodeWic.getKey();
			inputProductScanCodeWicMap.put(ProductScanCodeWicDocument.generateId(key.getWicApprovedProductListId(),
					key.getUpc(), key.getWicCategoryId(), key.getWicSubCatId()), inputProductScanCodeWic);
			pscwDocument = productScanCodeWicProcessor.process(inputProductScanCodeWic);
			productScanCodeWicDocuments.add(pscwDocument);
		}
		
		// When
		productScanCodeWicWriter.beforeStep(createStepExecution());
		productScanCodeWicWriter.write(productScanCodeWicDocuments);
		productScanCodeWicWriter.afterStep(createStepExecution());
		
		// Then
		Assert.assertEquals(numberOfRecords, productScanCodeWicIndexRepository.count());
		outputProductScanCodeWicDocumentList = productScanCodeWicIndexRepository.findAll().iterator();
		while (outputProductScanCodeWicDocumentList.hasNext()) {
			outputProductScanCodeWicDocument = outputProductScanCodeWicDocumentList.next();
			inputProductScanCodeWic = inputProductScanCodeWicMap.get(outputProductScanCodeWicDocument.getId());
			key = inputProductScanCodeWic.getKey();
			Assert.assertEquals(
					ProductScanCodeWicDocument.generateId(key.getWicApprovedProductListId(), key.getUpc(),
							key.getWicCategoryId(), key.getWicSubCatId()), outputProductScanCodeWicDocument.getId());
			Assert.assertEquals(key.getWicApprovedProductListId(), outputProductScanCodeWicDocument.getWicAplId());
			Assert.assertEquals(key.getUpc(), outputProductScanCodeWicDocument.getUpc());
			Assert.assertEquals(key.getWicCategoryId(), outputProductScanCodeWicDocument.getWicCategoryId());
			Assert.assertEquals(key.getWicSubCatId(), outputProductScanCodeWicDocument.getWicSubCategoryId());
			Assert.assertEquals(inputProductScanCodeWic.getLebSwitch(), outputProductScanCodeWicDocument.getLebSwitch());
			Assert.assertEquals(inputProductScanCodeWic.getWicDescription(), outputProductScanCodeWicDocument.getWicDescription());
			Assert.assertEquals(inputProductScanCodeWic.getWicPackageSize(), outputProductScanCodeWicDocument.getWicPackageSize());
			Assert.assertEquals(inputProductScanCodeWic.getEffDt(), outputProductScanCodeWicDocument.getEffDt());
			Assert.assertEquals(inputProductScanCodeWic.getEndDt(), outputProductScanCodeWicDocument.getEndDt());
			Assert.assertEquals(inputProductScanCodeWic.getWicUntTxt(), outputProductScanCodeWicDocument.getWicUntTxt());
			Assert.assertEquals(inputProductScanCodeWic.getWicBnFtQty(), outputProductScanCodeWicDocument.getWicBnFtQty());
			Assert.assertEquals(inputProductScanCodeWic.getWicBnftUntTxt(), outputProductScanCodeWicDocument.getWicBnftUntTxt());
			Assert.assertEquals(inputProductScanCodeWic.getWicPrcAmt(), outputProductScanCodeWicDocument.getWicPrcAmt());
			Assert.assertEquals(inputProductScanCodeWic.getWicPrcCd(), outputProductScanCodeWicDocument.getWicPrcCd());
			Assert.assertEquals(inputProductScanCodeWic.getWicCrdAcptId(), outputProductScanCodeWicDocument.getWicCrdAcptId());
			Assert.assertEquals(inputProductScanCodeWic.getCre8UId(), outputProductScanCodeWicDocument.getCre8UId());
			// Assert.assertEquals(WicUtil.checkWicSwitch(inputProductScanCodeWic) ? WicConstants.YES : WicConstants.NO, outputProductScanCodeWicDocument.getWicSw());
		}
	}
}