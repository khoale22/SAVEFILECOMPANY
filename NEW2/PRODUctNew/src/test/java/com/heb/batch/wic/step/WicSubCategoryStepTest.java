/*
 * WicSubCategoryStepTest
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.batch.wic.step;

import com.heb.batch.wic.entity.WicSubCategory;
import com.heb.batch.wic.entity.WicSubCategoryKey;
import com.heb.batch.wic.index.WicSubCategoryDocument;
import com.heb.batch.wic.index.repository.WicSubCategoryIndexRepository;
import com.heb.batch.wic.processor.WicSubCategoryProcessor;
import com.heb.batch.wic.reader.WicSubCategoryReader;
import com.heb.batch.wic.repository.WicSubCategoryRepository;
import com.heb.batch.wic.writer.WicSubCategoryWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * Unit tests for WicSubCategory step (step 5: Caching WicSubCategory).
 *
 * @author vn03512
 * @since 1.0.0
 */
@Transactional(transactionManager = "jpaTransactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wic-sub-category-config-test.xml"})
public class WicSubCategoryStepTest {
    private static final Logger LOGGER = LogManager.getLogger(WicSubCategoryStepTest.class);

    @Autowired
    private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;
	@Autowired
	private WicSubCategoryRepository wicSubCategoryRepository;
    @Autowired
    private WicSubCategoryProcessor wicSubCategoryProcessor;
    @Autowired
    private WicSubCategoryReader wicSubCategoryReader;
    @Autowired
    private WicSubCategoryWriter wicSubCategoryWriter;

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
        wicSubCategoryIndexRepository.deleteAll();

        wicSubCategoryReader.beforeStep(createStepExecution());
        wicSubCategoryReader.setPageSize(50);
    }

    @After
    public void after() {
        wicSubCategoryReader.afterStep(createStepExecution());
    }
    
    @Test
	public void testReadData() throws Exception {
		long numberOfRecords = wicSubCategoryRepository.count();
		WicSubCategory wicSubCategory;
		for (int i = 1; i <= numberOfRecords; i++) {
			wicSubCategory = wicSubCategoryReader.read();
			Assert.assertNotNull(wicSubCategory);
		}
		wicSubCategory = wicSubCategoryReader.read();
		Assert.assertNull(wicSubCategory);
	}
    
    @Test
	public void testProcessData() throws Exception {
    	
    	// Given
		long numberOfRecords = wicSubCategoryRepository.count();
		WicSubCategory wicSubCategory;
		WicSubCategoryKey key;
		WicSubCategoryDocument wicSubCategoryDocument;
		for (int i = 1; i <= numberOfRecords; i++) {
			wicSubCategory = wicSubCategoryReader.read();
			
			// When
			wicSubCategoryDocument = wicSubCategoryProcessor.process(wicSubCategory);
			
			// Then
			key = wicSubCategory.getKey();
			Assert.assertEquals(WicSubCategoryDocument.generateId(key.getWicCategoryid(),
					key.getWicSubCategoryId()), wicSubCategoryDocument.getId());
			Assert.assertEquals(key.getWicCategoryid(), wicSubCategoryDocument.getWicCategoryId());
			Assert.assertEquals(key.getWicSubCategoryId(), wicSubCategoryDocument.getWicSubCategoryId());
			Assert.assertEquals(wicSubCategory.getDescription(), wicSubCategoryDocument.getDescription());
			Assert.assertEquals(true, key.equals(key));
			LOGGER.info(key.hashCode());
		}
	}

    @Test
	public void testWriteData() throws Exception {
    	
    	// Given
		long numberOfRecords = wicSubCategoryRepository.count();
		WicSubCategory wicSubCategory;
		WicSubCategoryKey key = null;
		Map<String, WicSubCategory> inputWicSubCategoryMap = new HashMap<>();
		WicSubCategory inputWicSubCategory;
		WicSubCategoryDocument wicSubCategoryDocument;
		List<WicSubCategoryDocument> wicSubCategoryDocuments = new ArrayList<>();
		Iterator<WicSubCategoryDocument> outputWicSubCategoryDocumentList;
		WicSubCategoryDocument outputWicSubCategoryDocument;
		for (int i = 1; i <= numberOfRecords; i++) {
			wicSubCategory = wicSubCategoryReader.read();
			LOGGER.info(wicSubCategory.getKey().equals(key));
			LOGGER.info(wicSubCategory.toString());
			key = wicSubCategory.getKey();
			inputWicSubCategoryMap.put(WicSubCategoryDocument.generateId(key.getWicCategoryid(), key.getWicSubCategoryId()), wicSubCategory);
			wicSubCategoryDocument = wicSubCategoryProcessor.process(wicSubCategory);
			wicSubCategoryDocuments.add(wicSubCategoryDocument);
		}
		
		// When
		wicSubCategoryWriter.write(wicSubCategoryDocuments);
		
		// Then
		Assert.assertEquals(numberOfRecords, wicSubCategoryIndexRepository.count());
		outputWicSubCategoryDocumentList = wicSubCategoryIndexRepository.findAll().iterator();
		
		while (outputWicSubCategoryDocumentList.hasNext()) {
			outputWicSubCategoryDocument = outputWicSubCategoryDocumentList.next();
			inputWicSubCategory = inputWicSubCategoryMap.get(outputWicSubCategoryDocument.getId());
			key = inputWicSubCategory.getKey();

			Assert.assertEquals(key.getWicCategoryid(), outputWicSubCategoryDocument.getWicCategoryId());
			Assert.assertEquals(key.getWicSubCategoryId(), outputWicSubCategoryDocument.getWicSubCategoryId());
			Assert.assertEquals(inputWicSubCategory.getDescription(), outputWicSubCategoryDocument.getDescription());
			Assert.assertEquals(inputWicSubCategory.getLebSwitch(), outputWicSubCategoryDocument.getLebSwitch());
		}
	}
}