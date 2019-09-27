/*
 * DeleteScanCodesWICAndUpdateScanCodesStepTest
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.batch.wic.step;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.entity.ProductScanCodeWicKey;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.*;
import com.heb.batch.wic.index.repository.*;
import com.heb.batch.wic.service.TexasStateData;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.webservice.ProductScanCodeServiceClient;
import com.heb.batch.wic.webservice.ProductScanCodeWicServiceClient;
import com.heb.batch.wic.webservice.vo.BaseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.heb.batch.wic.processor.DeleteScanCodesWICAndUpdateScanCodesProcessor;
import com.heb.batch.wic.reader.DeleteScanCodesWICAndUpdateScanCodesReader;
import com.heb.batch.wic.repository.ProductScanCodeWicRepository;
import com.heb.batch.wic.writer.DeleteScanCodesWICAndUpdateScanCodesWriter;

/**
 * Unit tests for DeleteScanCodesWICAndUpdateScanCodes step.
 *
 * @author vn03512
 * @since 1.0.0
 */
@Transactional(transactionManager = "jpaTransactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:delete-and-update-master-data-config-test.xml"})
public class DeleteScanCodesWICAndUpdateScanCodesStepTest {
    private static final Logger LOGGER = LogManager.getLogger(DeleteScanCodesWICAndUpdateScanCodesStepTest.class);
    private static ProductScanCodeWic stepReaderWic;
    private static ProductScanCodeWicDocument stepProcessorWicDocument;

    @Autowired
    private ProductScanCodeWicRepository productScanCodeWicRepository;
    @Autowired
    private ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository;
    @Autowired
    private WicCategoryIndexRepository wicCategoryIndexRepository;
    @Autowired
    private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;
    @Autowired
    private TexasStateIndexRepository texasStateIndexRepository;
    @Autowired
    private TexasStateData texasStateData;
    @Autowired
    private DeleteScanCodesWICAndUpdateScanCodesProcessor processor;
    @Autowired
    private DeleteScanCodesWICAndUpdateScanCodesReader reader;
    @InjectMocks
    @Autowired
    private DeleteScanCodesWICAndUpdateScanCodesWriter writer;
    @Mock
    private MessageSource messageSource;
    @Mock
    private ProductScanCodeWicServiceClient productScanCodeWicServiceClient;
    @Mock
    private ProductScanCodeServiceClient productScanCodeServiceClient;

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
    	productScanCodeWicIndexRepository.deleteAll();
    	wicCategoryIndexRepository.deleteAll();
    	wicSubCategoryIndexRepository.deleteAll();
    	texasStateIndexRepository.deleteAll();
        productScanCodeWicRepository.deleteAll();
        reader.beforeStep(createStepExecution());
        reader.setPageSize(50);
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageSource.getMessage("wic.email.production.subject", null, Locale.US)).thenReturn("test");
    }

    @After
    public void after() {
        reader.afterStep(createStepExecution());
    }


    public ProductScanCodeWic runReader() throws Exception {
        reader.beforeStep(createStepExecution());
        stepReaderWic = reader.read();
        reader.afterStep(createStepExecution());

        return stepReaderWic;
    }

    public ProductScanCodeWicDocument runProcessor() throws Exception {
        if (stepReaderWic != null) {
            processor.beforeStep(createStepExecution());
            stepProcessorWicDocument = processor.process(stepReaderWic);
        } else {
            throw new Exception("The stepReaderWicDocument is null");
        }

        return stepProcessorWicDocument;
    }
    public ProductScanCodeWicDocument runWriter(List<? extends ProductScanCodeWicDocument> productScanCodeWicList) throws Exception {
        writer.beforeStep(createStepExecution());
        writer.write(productScanCodeWicList);
        writer.afterStep(createStepExecution());
        return stepProcessorWicDocument;
    }

    /**
     * Delete missed records in PROD_SCN_CD_WIC with UPC does not exist in WIC file.
     *
     * @unittestid Step7_Delete_01
     */
    @Test
    public void testDeleteMissedRecordByUPC() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        texasStateDocument.setScnCdId("000000000094214");
        ProductScanCodeWic productScanCodeWic = createProductScanCodeWic(texasStateDocument);
        productScanCodeWicRepository.save(productScanCodeWic);
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(productScanCodeWic));

        LOGGER.info("Running reader");
        ProductScanCodeWic readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicAplId(), String.valueOf(readResult.getKey().getWicApprovedProductListId()));

        LOGGER.info("Running processor");
        ProductScanCodeWicDocument processResult = runProcessor();
        productScanCodeWic.hashCode();
        LOGGER.info(productScanCodeWic.toString());
        Assert.assertNotNull(processResult);
        Assert.assertEquals(WicConstants.DELETE, processResult.getAction());
        Assert.assertEquals(StringUtils.EMPTY, processResult.getWicCategoryDesc());
        Assert.assertEquals(StringUtils.EMPTY, processResult.getWicSubCategoryDesc());
        Assert.assertEquals(true, productScanCodeWic.equals(productScanCodeWic));
        Assert.assertEquals(true, productScanCodeWic.getKey().equals(productScanCodeWic.getKey()));

//        Assert.assertEquals(wicDocumentsSize + 1, texasStateData.getProductScanCodeWicDocuments().size());
    }

    /**
     * Delete missed records in PROD_SCN_CD_WIC with WIC_SUB_CAT_ID doesn't exist in WIC file.
     *
     * @unittestid Step7_Delete_02
     */
    @Test
    public void testDeleteMissedRecordByWicSubCatID() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        texasStateDocument.setWicSubCatId("001");
        ProductScanCodeWic productScanCodeWic = createProductScanCodeWic(texasStateDocument);
        productScanCodeWicRepository.save(productScanCodeWic);
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(productScanCodeWic));

        LOGGER.info("Running reader");
        ProductScanCodeWic readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicAplId(), String.valueOf(readResult.getKey().getWicApprovedProductListId()));

        LOGGER.info("Running processor");
        ProductScanCodeWicDocument processResult = runProcessor();
        List<ProductScanCodeWicDocument> lstProScnDoc = new ArrayList<>();
        lstProScnDoc.add(processResult);
        runWriter(lstProScnDoc);


        Assert.assertNotNull(processResult);
        Assert.assertEquals(WicConstants.DELETE, processResult.getAction());
        Assert.assertEquals(StringUtils.EMPTY, processResult.getWicCategoryDesc());
        Assert.assertEquals(StringUtils.EMPTY, processResult.getWicSubCategoryDesc());
        //Assert.assertEquals(wicDocumentsSize + 1, texasStateData.getProductScanCodeWicDocuments().size());
    }

    /**
     * Delete missed records in PROD_SCN_CD_WIC with WIC_CAT_ID, WIC_SUB_CAT_ID don't exist in WIC file.
     *
     * @unittestid Step7_Delete_03
     */
    @Test
    public void testDeleteMissedRecordByWicCatIdAndWicSubCatId() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        texasStateDocument.setWicCatId("20");
        texasStateDocument.setWicSubCatId("001");
        ProductScanCodeWic productScanCodeWic = createProductScanCodeWic(texasStateDocument);
        productScanCodeWicRepository.save(productScanCodeWic);
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(productScanCodeWic));
        WicCategoryDocument wicCategoryDocument = new WicCategoryDocument();
        wicCategoryDocument.setWicCatId("20");
        wicCategoryDocument.setDescription("FRUIT/VEGETABLES1");
        wicCategoryIndexRepository.save(wicCategoryDocument);
        WicSubCategoryDocument wicSubCategoryDocument = new WicSubCategoryDocument();
        wicSubCategoryDocument.setId(20L, 1L);
        wicSubCategoryDocument.setWicCategoryId(20L);
        wicSubCategoryDocument.setWicSubCategoryId(Long.valueOf(1L));
        wicSubCategoryDocument.setDescription("FRUITS &/OR VEGETABLES1");
        wicSubCategoryIndexRepository.save(wicSubCategoryDocument);
        int wicDocumentsSize = texasStateData.getProductScanCodeWicDocuments().size();

        LOGGER.info("Running reader");
        ProductScanCodeWic readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicAplId(), String.valueOf(readResult.getKey().getWicApprovedProductListId()));

        LOGGER.info("Running processor");
        ProductScanCodeWicDocument processResult = runProcessor();

        Assert.assertNotNull(processResult);
        Assert.assertEquals(WicConstants.DELETE, processResult.getAction());
        Assert.assertEquals("FRUIT/VEGETABLES1", processResult.getWicCategoryDesc());
        Assert.assertEquals("FRUITS &/OR VEGETABLES1", processResult.getWicSubCategoryDesc());
        //Assert.assertEquals(wicDocumentsSize + 1, texasStateData.getProductScanCodeWicDocuments().size());
        wicCategoryIndexRepository.deleteAll();
        wicSubCategoryIndexRepository.deleteAll();
    }

    /**
     * Delete missed records in PROD_SCN_CD_WIC with WIC_APL_ID , WIC_CAT_ID, WIC_SUB_CAT_ID don't exist in WIC file.
     *
     * @unittestid Step7_Delete_04
     */
    @Test
    public void testDeleteMissedRecordByWicAPLAndWicCatIdAndWicSubCatId() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        texasStateDocument.setWicAplId("10000000000942134");
        texasStateDocument.setWicCatId("20");
        texasStateDocument.setWicSubCatId("001");
        ProductScanCodeWic productScanCodeWic = createProductScanCodeWic(texasStateDocument);
        productScanCodeWicRepository.save(productScanCodeWic);
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(productScanCodeWic));
        int wicDocumentsSize = texasStateData.getProductScanCodeWicDocuments().size();

        LOGGER.info("Running reader");
        ProductScanCodeWic readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicAplId(), String.valueOf(readResult.getKey().getWicApprovedProductListId()));

        LOGGER.info("Running processor");
        ProductScanCodeWicDocument processResult = runProcessor();

        Assert.assertNotNull(processResult);
        Assert.assertEquals(WicConstants.DELETE, processResult.getAction());
        Assert.assertEquals(StringUtils.EMPTY, processResult.getWicCategoryDesc());
        Assert.assertEquals(StringUtils.EMPTY, processResult.getWicSubCategoryDesc());
//        Assert.assertEquals(wicDocumentsSize + 1, texasStateData.getProductScanCodeWicDocuments().size());
    }

    @Test
    public void testWriterDeleteScanCodeWicTc1() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        texasStateDocument.setWicSubCatId("001");
        ProductScanCodeWic productScanCodeWic = createProductScanCodeWic(texasStateDocument);
        productScanCodeWicRepository.save(productScanCodeWic);
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(productScanCodeWic));
        LOGGER.info("Running reader");
        ProductScanCodeWic readResult = runReader();
        LOGGER.info("Running processor");
        ProductScanCodeWicDocument processResult = runProcessor();
        //run writer
        List<BaseVO> objects = new ArrayList<>();
        BaseVO b = new BaseVO();
        b.setActionCode("1");
        objects.add(b);
        Mockito.when(this.productScanCodeWicServiceClient.submitRequest(objects, "/product/update", "key")).thenReturn("test1");
        Mockito.when(this.productScanCodeWicServiceClient.submitRequest(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("test2");
        List<ProductScanCodeWicDocument> lstProScnDoc = new ArrayList<>();
        lstProScnDoc.add(processResult);
        runWriter(lstProScnDoc);
    }
    @Test
    public void testWriterDeleteScanCodeWicTc2() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        texasStateDocument.setWicSubCatId("001");
        ProductScanCodeWic productScanCodeWic = createProductScanCodeWic(texasStateDocument);
        productScanCodeWicRepository.save(productScanCodeWic);
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(productScanCodeWic));;
        LOGGER.info("Running reader");
        ProductScanCodeWic readResult = runReader();
        LOGGER.info("Running processor");
        ProductScanCodeWicDocument processResult = runProcessor();
        //run writer
        List<BaseVO> objects = new ArrayList<>();
        BaseVO b = new BaseVO();
        b.setActionCode("1");
        objects.add(b);
        Mockito.when(this.productScanCodeWicServiceClient.submitRequest(objects, "/product/update", "key")).thenReturn("test1");
        Mockito.when(this.productScanCodeWicServiceClient.submitRequest(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("test2");
        List<ProductScanCodeWicDocument> lstProScnDoc = new ArrayList<>();
        lstProScnDoc.add(processResult);
        runWriter(lstProScnDoc);
    }
    
    @Test
    public void testWriterDeleteScanCodeWicTc3() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        texasStateDocument.setWicSubCatId("001");
        ProductScanCodeWic productScanCodeWic = createProductScanCodeWic(texasStateDocument);
        productScanCodeWicRepository.save(productScanCodeWic);
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(productScanCodeWic));;
        LOGGER.info("Running reader");
        ProductScanCodeWic readResult = runReader();
        LOGGER.info("Running processor");
        ProductScanCodeWicDocument processResult = runProcessor();
        //run writer
        List<BaseVO> objects = new ArrayList<>();
        BaseVO b = new BaseVO();
        b.setActionCode("1");
        b.setSystemEnvironment("  ");
        b.getActionCode();
        b.getSystemEnvironment();
        objects.add(b);
        Mockito.when(this.productScanCodeWicServiceClient.submitRequest(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new WicException(new Exception()));
        List<ProductScanCodeWicDocument> lstProScnDoc = new ArrayList<>();
        lstProScnDoc.add(processResult);
        runWriter(lstProScnDoc);
    }
    
    @Test
    public void testEqualProductScanCodeWic() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        ProductScanCodeWic productScanCodeWic1 = createProductScanCodeWic(texasStateDocument);
        ProductScanCodeWic productScanCodeWic2 = createProductScanCodeWic(texasStateDocument);
        productScanCodeWic1.equals(productScanCodeWic2);
    }

    /**
     * Create ProductScanCodeWicDocument by TexasStateDocument
     *
     * @param productScanCodeWic the TexasStateDocument using to create ProductScanCodeWicDocument
     * @return the ProductScanCodeWicDocument
     */
    public ProductScanCodeWicDocument createProductScanCodeWicDocument(ProductScanCodeWic productScanCodeWic) {
        ProductScanCodeWicDocument productScanCodeWicDocument = new ProductScanCodeWicDocument();
        productScanCodeWicDocument.setFields(productScanCodeWic);
        return productScanCodeWicDocument;
    }

    /**
     * Create ProductScanCodeWicDocument by TexasStateDocument
     *
     * @param document the TexasStateDocument using to create ProductScanCodeWicDocument
     * @return the ProductScanCodeWicDocument
     */
    public ProductScanCodeWic createProductScanCodeWic(TexasStateDocument document) {
        ProductScanCodeWicKey key = new ProductScanCodeWicKey();
        key.setWicApprovedProductListId(Long.valueOf(document.getWicAplId()));
        key.setUpc(Long.valueOf(document.getScnCdId()));
        key.setWicCategoryId(Long.valueOf(document.getWicCatId()));
        key.setWicSubCatId(Long.valueOf(document.getWicSubCatId()));
        ProductScanCodeWic productScanCodeWic = new ProductScanCodeWic();
        productScanCodeWic.setKey(key);
        productScanCodeWic.setEffDt(WicUtil.convertDateFromString(document.getEffDt()));
        productScanCodeWic.setEndDt(WicUtil.convertDateFromString(WicUtil.getDateOrDefault(document.getEndDt())));
        productScanCodeWic.setWicUntTxt(document.getWicUntTxt());
        productScanCodeWic.setWicBnFtQty(WicUtil.parseStringToDouble(document.getWicBnFtQty(), 3));
        productScanCodeWic.setWicBnftUntTxt(document.getWicBnftUntTxt());
        productScanCodeWic.setWicPrcAmt(WicUtil.parseStringToDouble(document.getWicPrcAmt(), 4));
        productScanCodeWic.setWicPrcCd(document.getWicPrcCd());
        productScanCodeWic.setWicCrdAcptId(document.getWicCrdAcptId());
        productScanCodeWic.setLebSwitch(WicConstants.NO);
        productScanCodeWic.setWicDescription(document.getWicProdDes());
        productScanCodeWic.setWicPackageSize(WicUtil.parseStringToDouble(document.getWicPkgSzQty(), 3));
        Timestamp current = new Timestamp(System.currentTimeMillis());
        productScanCodeWic.setCre8Ts(current);
        productScanCodeWic.setCre8UId(WicConstants.TXSTATE_USER);
        productScanCodeWic.setLstUpdtTs(current);
        productScanCodeWic.setLstUpdtUId(WicConstants.TXSTATE_USER);

        return productScanCodeWic;
    }

    /**
     * Create data test for TexasStateDocument
     *
     * @return the TexasStateDocument test
     */
    public TexasStateDocument createTexasStateDocument() {
        TexasStateDocument texasStateDocument = new TexasStateDocument();
        texasStateDocument.setId("000000000094213");
        texasStateDocument.setAplPreFix("1");
        texasStateDocument.setScnCdId("000000000094213");
        texasStateDocument.setWicProdDes("APPLES - RETAILER ASSIGNED");
        texasStateDocument.setWicCatId("19");
        texasStateDocument.setWicCategoryDesc("FRUIT/VEGETABLES");
        texasStateDocument.setWicSubCatId("000");
        texasStateDocument.setWicSubCategoryDesc("FRUITS &/OR VEGETABLES");
        texasStateDocument.setWicUntTxt("value");
        texasStateDocument.setWicPkgSzQty("00100");
        texasStateDocument.setWicBnFtQty("00100");
        texasStateDocument.setWicBnftUntTxt("value");
        texasStateDocument.setWicPrcAmt("000100");
        texasStateDocument.setWicPrcCd("03");
        texasStateDocument.setWicCrdAcptId("");
        texasStateDocument.setEffDt("20091001");
        texasStateDocument.setEndDt("00000000");
        texasStateDocument.setUpcCheckDigit("3");
        texasStateDocument.setIdCode("DA");
        texasStateDocument.setSequenceNumber("013580");
        texasStateDocument.setMessageId("0344");
        texasStateDocument.setErrorMessage("null");
        texasStateDocument.setWicAplId("10000000000942133");
        texasStateDocument.setDataRaw("DA013580034410000000000942133APPLES - RETAILER ASSIGNED                        19FRUIT/VEGETABLES                                  000FRUITS &/OR VEGETABLES                            value     0010000100value                                             00010003               2009100100000000");
        return texasStateDocument;
    }
}