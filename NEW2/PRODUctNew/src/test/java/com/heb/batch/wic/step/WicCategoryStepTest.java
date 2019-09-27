/*
 * WicCategoryStepTest
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.batch.wic.step;

import com.heb.batch.wic.entity.WicCategory;
import com.heb.batch.wic.index.WicCategoryDocument;
import com.heb.batch.wic.index.repository.WicCategoryIndexRepository;
import com.heb.batch.wic.index.repository.WicSubCategoryIndexRepository;
import com.heb.batch.wic.processor.WicCategoryProcessor;
import com.heb.batch.wic.reader.WicCategoryReader;
import com.heb.batch.wic.repository.WicCategoryRepository;
import com.heb.batch.wic.writer.WicCategoryWriter;
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
 * Unit tests for WicCategory step (step 4: Caching WicCategory).
 *
 * @author vn03512
 * @since 1.0.0
 */
@Transactional(transactionManager = "jpaTransactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wic-category-config-test.xml"})
public class WicCategoryStepTest {
    private static final Logger LOGGER = LogManager.getLogger(WicCategoryStepTest.class);

    @Autowired
    private WicCategoryIndexRepository wicCategoryIndexRepository;
    @Autowired
    private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;

	@Autowired
	private WicCategoryRepository wicCategoryRepository;
    @Autowired
    private WicCategoryProcessor wicCategoryProcessor;
    @Autowired
    private WicCategoryReader wicCategoryReader;
    @Autowired
    private WicCategoryWriter wicCategoryWriter;

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
        wicCategoryIndexRepository.deleteAll();
        wicSubCategoryIndexRepository.deleteAll();

        wicCategoryReader.beforeStep(createStepExecution());
        wicCategoryReader.setPageSize(50);
    }

    @After
    public void after() {
        wicCategoryReader.afterStep(createStepExecution());
    }
    
    @Test
	public void testReadData() throws Exception {
		long numberOfRecords = wicCategoryRepository.count();
		WicCategory wicCategory;
		for (int i = 1; i <= numberOfRecords; i++) {
			wicCategory = wicCategoryReader.read();
			Assert.assertNotNull(wicCategory);
		}
		wicCategory = wicCategoryReader.read();
		Assert.assertNull(wicCategory);
		WicCategory wicCategory1 = new WicCategory();
		wicCategory1.setId(1L);
		wicCategory1.setDescription("");
		WicCategory wicCategory2 = new WicCategory();
		wicCategory2.setId(1L);
		wicCategory2.setDescription("");
		wicCategory2.equals(wicCategory);
		wicCategory2.hashCode();
	}
    
    @Test
	public void testProcessData() throws Exception {
    	
    	// Given
		long numberOfRecords = wicCategoryRepository.count();
		WicCategory wicCategory;
		WicCategoryDocument wicCategoryDocument;
		for (int i = 1; i <= numberOfRecords; i++) {
			wicCategory = wicCategoryReader.read();
			
			// When
			wicCategoryDocument = wicCategoryProcessor.process(wicCategory);
			
			// Then
			Assert.assertEquals(wicCategoryDocument.getWicCatId(), String.valueOf(wicCategory.getId()));
			Assert.assertEquals(wicCategoryDocument.getDescription(), wicCategory.getDescription());
		}
	}

    @Test
	public void testWriteData() throws Exception {
    	
    	// Given
		long numberOfRecords = wicCategoryRepository.count();
		WicCategory wicCategory;
		Map<String, WicCategory> inputWicCategoryMap = new HashMap<>();
		WicCategoryDocument pscDocument;
		List<WicCategoryDocument> productScanCodeDocuments = new ArrayList<>();
		Iterator<WicCategoryDocument> outputWicCategoryDocumentList;
		WicCategoryDocument outputWicCategoryDocument;
		for (int i = 1; i <= numberOfRecords; i++) {
			wicCategory = wicCategoryReader.read();
			inputWicCategoryMap.put(String.valueOf(wicCategory.getId()), wicCategory);
			pscDocument = wicCategoryProcessor.process(wicCategory);
			productScanCodeDocuments.add(pscDocument);
		}
		
		// When
		wicCategoryWriter.write(productScanCodeDocuments);
		
		// Then
		Assert.assertEquals(numberOfRecords, wicCategoryIndexRepository.count());
		outputWicCategoryDocumentList = wicCategoryIndexRepository.findAll().iterator();
		
		while (outputWicCategoryDocumentList.hasNext()) {
			outputWicCategoryDocument = outputWicCategoryDocumentList.next();
			Assert.assertEquals(
					String.valueOf(inputWicCategoryMap.get(outputWicCategoryDocument.getWicCatId()).getId()),
					outputWicCategoryDocument.getWicCatId());
			Assert.assertEquals(inputWicCategoryMap.get(outputWicCategoryDocument.getWicCatId()).getDescription(),
					outputWicCategoryDocument.getDescription());
		}
	}
}