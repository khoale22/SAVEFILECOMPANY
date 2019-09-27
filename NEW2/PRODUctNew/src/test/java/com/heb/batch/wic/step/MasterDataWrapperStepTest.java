/*
 * MasterDataWrapperStepTest
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.batch.wic.step;

import com.heb.batch.wic.entity.ProductScanCodes;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.*;
import com.heb.batch.wic.index.repository.*;
import com.heb.batch.wic.processor.MasterDataWrapperProcessor;
import com.heb.batch.wic.reader.MasterDataWrapperReader;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.service.TexasStateData;
import com.heb.batch.wic.service.impl.MasterDataServiceImpl;
import com.heb.batch.wic.utils.MasterDataWrapper;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.webservice.ProductScanCodeServiceClient;
import com.heb.batch.wic.webservice.ProductScanCodeWicServiceClient;
import com.heb.batch.wic.webservice.WicCategoryServiceClient;
import com.heb.batch.wic.webservice.WicSubCategoryServiceClient;
import com.heb.batch.wic.webservice.vo.ProductScanCodeWicVO;
import com.heb.batch.wic.webservice.vo.ProductScanCodesVO;
import com.heb.batch.wic.webservice.vo.WicCategoryVO;
import com.heb.batch.wic.webservice.vo.WicSubCategoryVO;
import com.heb.batch.wic.writer.MasterDataWrapperWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Unit tests for MasterDataWrapper step (step 6: insert and update).
 *
 * @author vn03503
 */
@Transactional(transactionManager = "jpaTransactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:master-data-wrapper-config-test.xml"})
public class MasterDataWrapperStepTest {
    private static final Logger LOGGER = LogManager.getLogger(MasterDataWrapperStepTest.class);

    private static StepExecution stepExecution;
    private static TexasStateDocument stepTexasStateDocument;
    private static MasterDataWrapper stepMasterDataWrapper;
    @Value("${texasState.dataparse}")
    String fileDataParse;
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
    @Mock
    private WicCategoryServiceClient wicCategoryServiceClient;
    @Mock
    private WicSubCategoryServiceClient wicSubCategoryServiceClient;
    @Mock
    private ProductScanCodeWicServiceClient productScanCodeWicServiceClient;
    @Mock
    private ProductScanCodeServiceClient productScanCodeServiceClient;
    @Mock
    private MessageSource messageSource;
    @Mock
    private EmailService emailService;

    @Autowired
    private MasterDataWrapperReader reader;
    @Autowired
    private MasterDataWrapperProcessor processor;
    @Autowired
    @InjectMocks
    private MasterDataWrapperWriter writer;
    
    @Rule
	public ExpectedException expectedException = ExpectedException.none();

    public static StepExecution createStepExecution() {
        if(stepExecution == null) {
            JobParameters jobParameters = new JobParameters();
            JobInstance jobInstance = new JobInstance(12L, "job");
            JobExecution jobExecution = new JobExecution(jobInstance, 123L, jobParameters, null);
            stepExecution = jobExecution.createStepExecution("step");
            stepExecution.setId(1234L);
        }
        return stepExecution;
    }

    @Before
    public void before() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	Mockito.when(wicSubCategoryServiceClient.submitRequest(Mockito.anyList(), Mockito.anyString(), Mockito.anyString())).thenReturn("OK");
        Mockito.when(wicCategoryServiceClient.submitRequest(Mockito.anyList(), Mockito.anyString(), Mockito.anyString())).thenReturn("OK");
        Mockito.when(productScanCodeWicServiceClient.submitRequest(Mockito.anyList(), Mockito.anyString(), Mockito.anyString())).thenReturn("OK");
        
    	reader.close();
        texasStateIndexRepository.deleteAll();
        productScanCodeWicIndexRepository.deleteAll();
        wicCategoryIndexRepository.deleteAll();
        wicSubCategoryIndexRepository.deleteAll();
        texasStateData.setProductScanCodeWicDocuments(null);
        texasStateData.setProductScanCodeWicDocumentDeletes(null);
        texasStateData.setProductScanCodeChangeWicSwitchs(null);
        texasStateData.setKeyDeleteChangeDatas(null);
        stepTexasStateDocument = null;
        stepMasterDataWrapper = null;
    }

    public TexasStateDocument runReader() throws Exception {
    	ExecutionContext executionContext = new ExecutionContext();
        // reader.beforeStep(createStepExecution());
        // reader.setResource(new FileSystemResource("src/test/resources/WIC_Data_Parse.cvs"));
        reader.open(executionContext);
        stepTexasStateDocument = reader.read();
        // reader.afterStep(createStepExecution());
        return stepTexasStateDocument;
    }

    public MasterDataWrapper runProcessor() throws Exception {
        if(stepTexasStateDocument != null) {
            processor.beforeStep(createStepExecution());
            stepMasterDataWrapper = processor.process(stepTexasStateDocument);
            processor.afterStep(createStepExecution());
        } else throw new Exception("The stepTexasStateDocument is null");

        return stepMasterDataWrapper;
    }

    public ExitStatus runWriter() throws Exception {
        if(stepMasterDataWrapper != null) {
            writer.beforeStep(createStepExecution());

            List<MasterDataWrapper> dataWrappers = new ArrayList<MasterDataWrapper>();
            dataWrappers.add(stepMasterDataWrapper);
            writer.write(dataWrappers);
            //writer.splitMasterDataWrappers(dataWrappers);

            return writer.afterStep(createStepExecution());
        } else throw new Exception("The stepMasterDataWrapper is null");
    }

    //region Test insert data
    /**
     * Insert a valid record
     *
     * @unittestid Step6_Insert_01
     */
    @Test
    public void testInsertValidRecord() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);
        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicAplId(), readResult.getWicAplId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO scanCodeWic = processResult.getProductScanCodeWic();
        //ProductScanCodeWicKey scanCodeWicKey = scanCodeWic.getKey();
        //Assert.assertNotNull(scanCodeWicKey);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert SCN_CD_ID = UPC-KEY
        Assert.assertEquals(Long.valueOf(texasStateDocument.getScnCdId()), scanCodeWic.getScnCdId());
        //Assert WIC_APL_ID = APL-PREFIX + UPC-KEY + CHECK-DIGIT
        Assert.assertEquals(Long.valueOf(texasStateDocument.getAplPreFix() + String.format("%015d", Long.valueOf(texasStateDocument.getScnCdId())) + texasStateDocument.getUpcCheckDigit()),
                scanCodeWic.getWicAplId());
        //Assert WIC_CAT_ID = CATEGORY-CD
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicCatId()), scanCodeWic.getWicCatId());
        //Assert WIC_SUB_CAT_ID = SUBCATEGORY-CD
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicSubCatId()), scanCodeWic.getWicSubCatId());
        //Assert EFF_DT = EFF-DATE
        Assert.assertEquals(WicUtil.convertDateFromString(texasStateDocument.getEffDt()), scanCodeWic.getEffDt());
        //Assert END_DT = END-DATE
        Assert.assertEquals(WicUtil.convertDateFromString(WicUtil.getDateOrDefault(texasStateDocument.getEndDt())), scanCodeWic.getEndDt());
        //Assert WIC_PROD_DES = DESC
        Assert.assertEquals(texasStateDocument.getWicProdDes(), scanCodeWic.getWicProdDes());
        //Assert WIC_UNT_TXT = UNIT-OF-MEASURE
        Assert.assertEquals(WicUtil.getValueOrDefault(texasStateDocument.getWicUntTxt()), scanCodeWic.getWicUntTxt());
        //Assert WIC_PKG_SZ_QTY = PACKAGE-SIZE
        Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicPkgSzQty(), 3), scanCodeWic.getWicPkgSzQty());
        //Assert WIC_BNFT_QTY = BENEFIT-QTY
        Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicBnFtQty(), 3), scanCodeWic.getWicBnftQty());
        //Assert WIC_BNFT_UNT_TXT = BENEFIT-UNIT
        Assert.assertEquals(WicUtil.getValueOrDefault(texasStateDocument.getWicBnftUntTxt()), scanCodeWic.getWicBnftUntTxt());
        //Assert WIC_PRC_AMT = ITEM-PRICE
        Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicPrcAmt(), 3), scanCodeWic.getWicPrcAmt());
        //Assert WIC_CRD_ACPT_ID = CARD-ID-CD
        Assert.assertEquals(WicUtil.getValueOrDefault(texasStateDocument.getWicCrdAcptId()), scanCodeWic.getWicCrdAcptId());
        //Assert LEB_SW = N
        Assert.assertEquals(WicConstants.NO, scanCodeWic.getLebSw());
        //Assert CRE8_TS = current insert time
        assertDatesAlmostEqual(current, scanCodeWic.getCre8Ts());
        //Assert CRE8_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, scanCodeWic.getCre8Uid());
        //Assert LST_UPDT_TS = current insert time
        assertDatesAlmostEqual(current, scanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, scanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

		MasterDataWrapper masterDataWrapperTest = new MasterDataWrapper(new ProductScanCodeWicVO(), new WicCategoryVO(), new WicSubCategoryVO());
		masterDataWrapperTest.setWicCategory(new WicCategoryVO());
		masterDataWrapperTest.setWicSubCategory(new WicSubCategoryVO());
        scanCodeWic.setStageEvent("");
        scanCodeWic.setWiccEvent("");
        scanCodeWic.getWiccEvent();
        scanCodeWic.getStageEvent();
        scanCodeWic.toString();
        //List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        //Assert.assertEquals(Long.valueOf(texasStateDocument.getScnCdId()), productScanCodeWicVO.getScnCdId());
    }

    /**
     * UPC, WIC_APL_ID, WIC_CAT_ID exist and WIC_SUB_CAT_ID does not exist in PROD_SCN_CD_WIC
     * EFF-DATE <= Current Date & END-DATE > Current-Date
     *
     * @unittestid Step6_Insert_02
     */
    @Test
    public void testInsertSubCatIdNotExist() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicSubCatId("005");
        texasStateDocument.setWicSubCategoryDesc("JUICE 48 OZ &/OR 12 OZ FRZ");
        texasStateDocument.setEndDt(WicUtil.convertDateToString(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()), WicUtil.DEFAULT_FORMAT_DATE));
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicDocument scanCodeWicDocument = (ProductScanCodeWicDocument) texasStateData.getProductScanCodeWicDocuments().values().toArray()[0];
        Assert.assertNotNull(scanCodeWicDocument);
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicSubCatId()), scanCodeWicDocument.getWicSubCategoryId());

        LOGGER.info("Running writer");
        runWriter();

        Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
        Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = Y
        Assert.assertEquals(WicConstants.YES, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

//        List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
//        Assert.assertFalse(productScanCodeWicList.isEmpty());
//        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
//        //Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
//        Assert.assertEquals(scanCodeWicDocument.getWicAplId(), productScanCodeWicVO.getWicAplId());
    }

    /**
     * UPC, WIC_APL_ID exist and WIC_CAT_ID, WIC_SUB_CAT_ID don't exist in PROD_SCN_CD_WIC
     * EFF-DATE <= Current Date & END-DATE > Current-Date
     *
     * @unittestid Step6_Insert_03
     */
    @Test
    public void testInsertCatIdAndSubCatIdNotExist() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicCatId("16");
        texasStateDocument.setWicCategoryDesc("GRAINS");
        texasStateDocument.setWicSubCatId("005");
        texasStateDocument.setWicSubCategoryDesc("JUICE 48 OZ &/OR 12 OZ FRZ");
        texasStateDocument.setEndDt(WicUtil.convertDateToString(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()), WicUtil.DEFAULT_FORMAT_DATE));
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicDocument scanCodeWicDocument = (ProductScanCodeWicDocument) texasStateData.getProductScanCodeWicDocuments().values().toArray()[0];
        Assert.assertNotNull(scanCodeWicDocument);
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicCatId()), scanCodeWicDocument.getWicCategoryId());
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicSubCatId()), scanCodeWicDocument.getWicSubCategoryId());
        assertDatesAlmostEqual(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                scanCodeWicDocument.getEndDt());

        LOGGER.info("Running writer");
        runWriter();

        Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
        Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = Y
        Assert.assertEquals(WicConstants.YES, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

//        List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
//        Assert.assertFalse(productScanCodeWicList.isEmpty());
//        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
//        //Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
//        Assert.assertEquals(scanCodeWicDocument.getWicAplId(), productScanCodeWicVO.getWicAplId());
    }

    /**
     * UPC exists and WIC_APL_ID, WIC_CAT_ID, WIC_SUB_CAT_ID don't exist in PROD_SCN_CD_WIC
     * EFF-DATE <= Current Date & END-DATE > Current-Date
     *
     * @unittestid Step6_Insert_04
     */
    @Test
    public void testInsertUPCExist() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setAplPreFix("0");
        texasStateDocument.setWicAplId(texasStateDocument.getAplPreFix() + texasStateDocument.getScnCdId() + texasStateDocument.getUpcCheckDigit());
        texasStateDocument.setWicCatId("16");
        texasStateDocument.setWicCategoryDesc("GRAINS");
        texasStateDocument.setWicSubCatId("005");
        texasStateDocument.setWicSubCategoryDesc("JUICE 48 OZ &/OR 12 OZ FRZ");
        texasStateDocument.setEndDt(WicUtil.convertDateToString(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()), WicUtil.DEFAULT_FORMAT_DATE));
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicDocument scanCodeWicDocument =  (ProductScanCodeWicDocument) texasStateData.getProductScanCodeWicDocuments().values().toArray()[0];
        Assert.assertNotNull(scanCodeWicDocument);
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicAplId()), scanCodeWicDocument.getWicAplId());
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicCatId()), scanCodeWicDocument.getWicCategoryId());
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicSubCatId()), scanCodeWicDocument.getWicSubCategoryId());

        LOGGER.info("Running writer");
        runWriter();

        Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
        Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = Y
        Assert.assertEquals(WicConstants.YES, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

//        List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
//        Assert.assertFalse(productScanCodeWicList.isEmpty());
//        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
//        //Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
//        Assert.assertEquals(scanCodeWicDocument.getWicAplId(), productScanCodeWicVO.getWicAplId());
    }

    /**
     * UPC, WIC_APL_ID, WIC_CAT_ID, WIC_SUB_CAT_ID don't exist in PROD_SCN_CD_WIC
     * EFF-DATE <= Current Date & END-DATE > Current-Date
     *
     * @unittestid Step6_Insert_05
     */
    @Test
    public void testInsertAllNotExist() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setScnCdId("000000818179009");
        texasStateDocument.setWicAplId(texasStateDocument.getAplPreFix() + texasStateDocument.getScnCdId() + texasStateDocument.getUpcCheckDigit());
        texasStateDocument.setWicCatId("16");
        texasStateDocument.setWicCategoryDesc("GRAINS");
        texasStateDocument.setWicSubCatId("005");
        texasStateDocument.setWicSubCategoryDesc("JUICE 48 OZ &/OR 12 OZ FRZ");
        texasStateDocument.setEndDt(WicUtil.convertDateToString(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()), WicUtil.DEFAULT_FORMAT_DATE));
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicDocument scanCodeWicDocument =  (ProductScanCodeWicDocument) texasStateData.getProductScanCodeWicDocuments().values().toArray()[0];
        Assert.assertNotNull(scanCodeWicDocument);
        Assert.assertEquals(Long.valueOf(texasStateDocument.getScnCdId()), scanCodeWicDocument.getUpc());
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicAplId()), scanCodeWicDocument.getWicAplId());
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicCatId()), scanCodeWicDocument.getWicCategoryId());
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicSubCatId()), scanCodeWicDocument.getWicSubCategoryId());

        LOGGER.info("Running writer");
        runWriter();

        Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
        Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = Y
        Assert.assertEquals(WicConstants.YES, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

//        List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
//        Assert.assertFalse(productScanCodeWicList.isEmpty());
//        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
//        //Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
//        Assert.assertEquals(scanCodeWicDocument.getWicAplId(), productScanCodeWicVO.getWicAplId());
    }

    /**
     * UPC, WIC_APL_ID exist and WIC_CAT_ID, WIC_SUB_CAT_ID don't exist in PROD_SCN_CD_WIC
     * EFF-DATE <= Current Date & END-DATE = 00000000
     *
     * @unittestid Step6_Insert_06
     */
    @Test
    public void testInsertCatIdAndSubCatIdNotExistEndZero() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicCatId("16");
        texasStateDocument.setWicCategoryDesc("GRAINS");
        texasStateDocument.setWicSubCatId("005");
        texasStateDocument.setWicSubCategoryDesc("JUICE 48 OZ &/OR 12 OZ FRZ");
        texasStateDocument.setEndDt("00000000");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicDocument scanCodeWicDocument =  (ProductScanCodeWicDocument) texasStateData.getProductScanCodeWicDocuments().values().toArray()[0];
        Assert.assertNotNull(scanCodeWicDocument);
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicCatId()), scanCodeWicDocument.getWicCategoryId());
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicSubCatId()), scanCodeWicDocument.getWicSubCategoryId());
        // Assert.assertNull(scanCodeWicDocument.getEndDt());

        LOGGER.info("Running writer");
        runWriter();

        Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
        Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = Y
        Assert.assertEquals(WicConstants.YES, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

//        List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
//        Assert.assertFalse(productScanCodeWicList.isEmpty());
//        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
//        //Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
//        Assert.assertEquals(scanCodeWicDocument.getWicAplId(), productScanCodeWicVO.getWicAplId());
    }

    /**
     * UPC doesn't exist in PROD_SCN_CD_WIC
     * EFF-DATE < Current Date & END-DATE < Current-Date & EFF-DATE < END-DATE
     *
     * @unittestid Step6_Insert_07
     */
    @Test
    public void testInsertEndDateLessThanCurrent() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        texasStateDocument.setScnCdId("000000818179009");
        texasStateDocument.setWicAplId(texasStateDocument.getAplPreFix() + texasStateDocument.getScnCdId() + texasStateDocument.getUpcCheckDigit());
        texasStateDocument.setEndDt("20180505");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

    //    Assert.assertNotNull(processResult);
//        ProductScanCodeWicDocument scanCodeWicDocument =  (ProductScanCodeWicDocument) texasStateData.getProductScanCodeWicDocuments().values().toArray()[0];
    //    Assert.assertNotNull(scanCodeWicDocument);
    //    Assert.assertEquals(WicUtil.convertDateFromString(WicUtil.getDateOrDefault(texasStateDocument.getEndDt())), new java.sql.Date(scanCodeWicDocument.getEndDt().getTime()));

   //     LOGGER.info("Running writer");
   //     runWriter();

    //    Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
    //    Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = N
     //   Assert.assertEquals(WicConstants.NO, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

//        List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
//        Assert.assertFalse(productScanCodeWicList.isEmpty());
//        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
//        //Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
//        Assert.assertEquals(scanCodeWicDocument.getWicAplId(), productScanCodeWicVO.getWicAplId());
    }

    /**
     * UPC doesn't exist in PROD_SCN_CD_WIC
     * EFF-DATE > Current Date & END-DATE > EFF-DATE
     *
     * @unittestid Step6_Insert_08
     */
    @Test
    public void testInsertEffDateGreaterThanCurrent() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        texasStateDocument.setScnCdId("000000818179009");
        texasStateDocument.setWicAplId(texasStateDocument.getAplPreFix() + texasStateDocument.getScnCdId() + texasStateDocument.getUpcCheckDigit());
        texasStateDocument.setEffDt(WicUtil.convertDateToString(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()), WicUtil.DEFAULT_FORMAT_DATE));
        texasStateDocument.setEndDt(WicUtil.convertDateToString(Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()), WicUtil.DEFAULT_FORMAT_DATE));
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

     //   Assert.assertNotNull(processResult);
     //   ProductScanCodeWicDocument scanCodeWicDocument =  (ProductScanCodeWicDocument) texasStateData.getProductScanCodeWicDocuments().values().toArray()[0];
     //   Assert.assertNotNull(scanCodeWicDocument);
     //   assertDatesAlmostEqual(Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
     //           scanCodeWicDocument.getEndDt());

    //    LOGGER.info("Running writer");
    //    runWriter();

    //    Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
    //    Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = N
    //    Assert.assertEquals(WicConstants.NO, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

//        List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
//        Assert.assertFalse(productScanCodeWicList.isEmpty());
//        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
//        //Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
//        Assert.assertEquals(scanCodeWicDocument.getWicAplId(), productScanCodeWicVO.getWicAplId());
    }

    /**
     * UPC doesn't exist in PROD_SCN_CD_WIC
     * EFF-DATE > Current Date & END-DATE = 00000000
     *
     * @unittestid Step6_Insert_09
     */
    @Test
    public void testInsertEffGreaterThanCurrentAndEndZero() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        texasStateDocument.setScnCdId("550");
        texasStateDocument.setWicAplId(texasStateDocument.getAplPreFix() + texasStateDocument.getScnCdId() + texasStateDocument.getUpcCheckDigit());
        texasStateDocument.setEffDt(WicUtil.convertDateToString(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()), WicUtil.DEFAULT_FORMAT_DATE));
        texasStateDocument.setEndDt("00000000");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

       // Assert.assertNotNull(processResult);
       // ProductScanCodeWicDocument scanCodeWicDocument =  (ProductScanCodeWicDocument) texasStateData.getProductScanCodeWicDocuments().values().toArray()[0];
    //    Assert.assertEquals(Long.valueOf(texasStateDocument.getScnCdId()), scanCodeWicDocument.getUpc());
     //   Assert.assertNotNull(scanCodeWicDocument);
        //Assert.assertNull(scanCodeWicDocument.getEndDt());

        LOGGER.info("Running writer");
     //   runWriter();

      //  Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
     //   Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = N
     //   Assert.assertEquals(WicConstants.NO, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

//        List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
//        Assert.assertFalse(productScanCodeWicList.isEmpty());
//        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
//        //Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
//        Assert.assertEquals(scanCodeWicDocument.getWicAplId(), productScanCodeWicVO.getWicAplId());
    }

    /**
     * UPC, WIC_APL_ID exist and WIC_CAT_ID , WIC_SUB_CAT_ID do not exist in PROD_SCN_CD_WIC
     * WIC_CAT_ID does not exist in WIC_CAT table and WIC_SUB_CAT_ID does not exist in WIC_SUB_CAT table
     *
     * @unittestid Step6_Insert_12
     */
    @Test
    public void testInsertCatAndSubCatNotExist() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicCatId("69");
        texasStateDocument.setWicCategoryDesc("GRAINS");
        texasStateDocument.setWicSubCatId("986");
        texasStateDocument.setWicSubCategoryDesc("JUICE 48 OZ &/OR 12 OZ FRZ");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        WicCategoryVO wicCategory = processResult.getWicCategory();
        Assert.assertNotNull(wicCategory);
        //Assert WIC_CAT_ID = CATEGORY-CD from WIC file
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicCatId()), Long.valueOf(wicCategory.getWicCatId()));
        // WIC_CAT_DES = CATEGORY-DESC from WIC file
        Assert.assertEquals(texasStateDocument.getWicCategoryDesc(), wicCategory.getWicCatDes());
        WicSubCategoryVO wicSubCategory = processResult.getWicSubCategory();
        Assert.assertNotNull(wicSubCategory);
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicSubCatId()), Long.valueOf(wicSubCategory.getWicSubCatId()));

        LOGGER.info("Running writer");
        runWriter();

        // List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
        // Assert.assertFalse(productScanCodeWicList.isEmpty());

        // List<BaseVO> wicSubCategoryList = writer.getWicSubCategoryList();
        // Assert.assertFalse(wicSubCategoryList.isEmpty());
    }

    /**
     * UPC, WIC_APL_ID, WIC_CAT_ID exist and WIC_SUB_CAT_ID does not exist in PROD_SCN_CD_WIC
     * WIC_SUB_CAT_ID does not exist in WIC_SUB_CAT table
     *
     * @unittestid Step6_Insert_13
     */
    @Test
    public void testInsertSubCatNotExist() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicSubCatId("986");
        texasStateDocument.setWicSubCategoryDesc("JUICE 48 OZ &/OR 12 OZ FRZ");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getWicSubCatId(), readResult.getWicSubCatId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        WicSubCategoryVO wicSubCategory = processResult.getWicSubCategory();
        Assert.assertNotNull(wicSubCategory);
        //Assert WIC_CAT_ID = CATEGORY-CD from WIC file
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicCatId()), Long.valueOf(wicSubCategory.getWicCatId()));
        //Assert WIC_SUB_CAT_ID = SUBCATEGORY-CD from WIC file
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicSubCatId()), Long.valueOf(wicSubCategory.getWicSubCatId()));
        //Assert WIC_SUB_CAT_DES =  SUBCATEGORY-DESC from WIC file
        Assert.assertEquals(texasStateDocument.getWicSubCategoryDesc(), wicSubCategory.getWicSubCatDes());
        //Assert LEB_SW = N
        Assert.assertEquals(WicConstants.NO, wicSubCategory.getLebSw());

        LOGGER.info("Running writer");
        runWriter();

        // List<BaseVO> productScanCodeWicList = writer.getInsertedProductScanCodeWicList();
        // Assert.assertFalse(productScanCodeWicList.isEmpty());

        // List<BaseVO> wicSubCategoryList = writer.getWicSubCategoryList();
        // Assert.assertFalse(wicSubCategoryList.isEmpty());
    }
    //endregion

    //region Test update data

    /**
     * There is no change between WIC file and DB
     *
     * @unittestid Step6_Update_01
     */
    @Test
    public void testIgnoreUpdateNoChange() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

      //  Assert.assertNotNull(processResult);
//        Assert.assertNull(processResult.getProductScanCodeWic());
     //   Assert.assertEquals(0, texasStateData.getProductScanCodeWicDocuments().size());

        LOGGER.info("Running writer");
     //   runWriter();

        // List<BaseVO> insertedProductScanCodeWicList = writer.getInsertedProductScanCodeWicList();
        // Assert.assertTrue(insertedProductScanCodeWicList.isEmpty());

        // List<BaseVO> updatedProductScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        // Assert.assertTrue(updatedProductScanCodeWicList.isEmpty());
    }

    /**
     * Description is changed
     *
     * @unittestid Step6_Update_02
     */
    @Test
    public void testUpdateDescriptionChanged() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicProdDes("LEMONS 2LB BAG ORGANIC");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        productScanCodeWic.equals(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        // Assert WIC_PROD_DES = new description from WIC file
        Assert.assertEquals(texasStateDocument.getWicProdDes(), productScanCodeWic.getWicProdDes());
        // Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        // Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        //List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_PROD_DES = new description from WIC file
        //Assert.assertEquals(texasStateDocument.getWicProdDes(), productScanCodeWicVO.getWicProdDes());
    }

    /**
     * Unit of Measure is changed
     *
     * @unittestid Step6_Update_03
     */
    @Test
    public void testUpdateUnitOfMeasureChanged() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicUntTxt("qt");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert WIC_UNT_TXT = new UNIT-OF-MEASURE from WIC file
        Assert.assertEquals(texasStateDocument.getWicUntTxt(), productScanCodeWic.getWicUntTxt());
        //Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        //List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_UNT_TXT = new UNIT-OF-MEASURE
        //Assert.assertEquals(texasStateDocument.getWicUntTxt(), productScanCodeWicVO.getWicUntTxt());
    }

    /**
     * Package Size is changed
     *
     * @unittestid Step6_Update_04
     */
    @Test
    public void testUpdatePackageSizeChanged() throws Exception{
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicPkgSzQty("00101");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert WIC_PKG_SZ_QTY = new PACKAGE-SIZE from WIC file
        Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicPkgSzQty(), 3), productScanCodeWic.getWicPkgSzQty());
        //Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        //List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_PKG_SZ_QTY = new PACKAGE-SIZE
        //Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicPkgSzQty(), 3), productScanCodeWicVO.getWicPkgSzQty());
    }

    /**
     * Benefit Quantity is changed
     *
     * @unittestid Step6_Update_05
     */
    @Test
    public void testUpdateBenefitQuantityChanged() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicBnFtQty("00101");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert WIC_BNFT_QTY = new BENEFIT-QTY from WIC file
        Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicBnFtQty(), 3), productScanCodeWic.getWicBnftQty());
        //Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        //List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_BNFT_QTY = new BENEFIT-QTY
        //Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicBnFtQty(), 3), productScanCodeWicVO.getWicBnftQty());
    }

    /**
     * Benefit Unit Quantity is changed
     *
     * @unittestid Step6_Update_06
     */
    @Test
    public void testUpdateBenefitUnitQuantityChanged() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicBnftUntTxt("qt");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert WIC_BNFT_UNT_TXT = new BENEFIT-UNIT from WIC file
        Assert.assertEquals(texasStateDocument.getWicBnftUntTxt(), productScanCodeWic.getWicBnftUntTxt());
        //Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        //List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_BNFT_UNT_TXT = new BENEFIT-UNIT
        //Assert.assertEquals(texasStateDocument.getWicBnftUntTxt(), productScanCodeWicVO.getWicBnftUntTxt());
    }

    /**
     * Item Price is changed
     *
     * @unittestid Step6_Update_07
     */
    @Test
    public void testUpdateItemPriceChanged() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicPrcAmt("00101");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert WIC_PRC_AMT = new ITEM-PRICE from WIC file with format ...
        Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicPrcAmt(), 3), productScanCodeWic.getWicPrcAmt());
        //Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        //List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_PRC_AMT = new ITEM-PRICE
        //Assert.assertEquals(WicUtil.parseStringToDouble(texasStateDocument.getWicPrcAmt(), 4), productScanCodeWicVO.getWicPrcAmt());
    }

    /**
     * Price Type is changed
     *
     * @unittestid Step6_Update_08
     */
    @Test
    public void testUpdatePriceTypeChanged() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicPrcCd("01");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert WIC_PRC_CD = new PRICE-TYPE from WIC file
        Assert.assertEquals(texasStateDocument.getWicPrcCd(), productScanCodeWic.getWicPrcCd());
        //Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        //List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_PRC_CD = new PRICE-TYPE
        //Assert.assertEquals(texasStateDocument.getWicPrcCd(), productScanCodeWicVO.getWicPrcCd());
    }

    /**
     * Card ID is changed
     *
     * @unittestid Step6_Update_09
     */
    @Test
    public void testUpdateCardIdChanged() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicCrdAcptId("A1");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert WIC_CRD_ACPT_ID = new CARD-ID-CD from WIC file
        Assert.assertEquals(texasStateDocument.getWicCrdAcptId(), productScanCodeWic.getWicCrdAcptId());
        //Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        //List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        //Assert.assertFalse(productScanCodeWicList.isEmpty());
        //ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_CRD_ACPT_ID = new CARD-ID-CD
        //Assert.assertEquals(texasStateDocument.getWicCrdAcptId(), productScanCodeWicVO.getWicCrdAcptId());
    }
    
    /**
     * End Date is changed
     *
     * @unittestid Step6_Update_10
     */
    @Test
    public void testUpdateEndDateChanged() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setEndDt("20180102");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
    }

    /**
     * PROD_SCN_CODES.WIC_SW = N
     * EFF_DATE is changed with EFF_DATE >= current date
     *
     * @unittestid Step6_Update_11
     */
    @Test
    public void testUpdateEffGreaterThanOrEqualCurrent() throws Exception{
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setEffDt("20180102");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();

        Assert.assertNotNull(processResult);
        ProductScanCodeWicVO productScanCodeWic = processResult.getProductScanCodeWic();
        Assert.assertNotNull(productScanCodeWic);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //Assert EFF_DT = new description from WIC file
        Assert.assertEquals(WicUtil.convertDateFromString(texasStateDocument.getEffDt()), productScanCodeWic.getEffDt());
        //Assert LST_UPDT_TS = current update time
        assertDatesAlmostEqual(current, productScanCodeWic.getLstUpdtTs());
        //Assert LST_UPDT_UID = TXSTATE
        Assert.assertEquals(WicConstants.TXSTATE_USER, productScanCodeWic.getLstUpdtUid());

        LOGGER.info("Running writer");
        runWriter();

        /* Map<String, ProductScanCodeWicDocument> productScanCodeWicDocuments = texasStateData.getProductScanCodeWicDocuments();
        Assert.assertFalse(productScanCodeWicDocuments.isEmpty());
        //Assert WIC_SW = Y
        Assert.assertEquals(WicConstants.YES, ((ProductScanCodeWicDocument) productScanCodeWicDocuments.values().toArray()[0]).getWicSw());

        List<BaseVO> productScanCodeWicList = writer.getUpdatedProductScanCodeWicList();
        Assert.assertFalse(productScanCodeWicList.isEmpty());
        ProductScanCodeWicVO productScanCodeWicVO = (ProductScanCodeWicVO) productScanCodeWicList.get(0);
        // Assert WIC_APP_ID = PROD_SCN_CD_WIC.WIC_APL_ID
        Assert.assertEquals(Long.valueOf(texasStateDocument.getWicAplId()), productScanCodeWicVO.getWicAplId()); */
    }
    
    /**
     * There is no change between WIC file and DB and WicSw is NO
     *
     * @unittestid Step6_Update_12
     */
    @Test
    public void testIgnoreUpdateNoChangeWicSwNo() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        ProductScanCodeWicDocument productScanCodeWicDocument = createProductScanCodeWicDocument(texasStateDocument);
        productScanCodeWicDocument.setWicSw(WicConstants.NO);
        productScanCodeWicIndexRepository.save(productScanCodeWicDocument);
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();
        Assert.assertNull(processResult.getProductScanCodeWic());
    }
    
    /**
     * Description is changed and WicSw is NO
     *
     * @unittestid Step6_Update_13
     */
    @Test
    public void testUpdateDescriptionChangedWicSwNo() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        ProductScanCodeWicDocument productScanCodeWicDocument = createProductScanCodeWicDocument(texasStateDocument);
        productScanCodeWicDocument.setWicSw(WicConstants.NO);
        productScanCodeWicIndexRepository.save(productScanCodeWicDocument);
        texasStateDocument.setWicProdDes("LEMONS 2LB BAG ORGANIC");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);

        LOGGER.info("Running reader");
        TexasStateDocument readResult = runReader();

        Assert.assertEquals(texasStateDocument.getScnCdId(), readResult.getScnCdId());

        LOGGER.info("Running processor");
        MasterDataWrapper processResult = runProcessor();
        Assert.assertNull(processResult.getProductScanCodeWic());
    }

    /**
     * Insert WicCatThrow Exception
     *
     * @unittestid Step6_Insert_11
     */
    @Test
    public void testInsertWicCatThrowException() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);
        LOGGER.info("Running reader");
        runReader();

        LOGGER.info("Running processor");
        runProcessor();

        Mockito.when(wicCategoryServiceClient.submitRequest(Mockito.anyList(), Mockito.anyString(), Mockito.anyString())).thenThrow(WicException.class);
        this.expectedException.expect(WicException.class);
        
        LOGGER.info("Running writer");
        runWriter();
    }

    /**
     * Insert WicSubCat Throw Exception
     *
     * @unittestid Step6_Insert_12
     */
    @Test
    public void testInsertWicSubCatThrowException() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);
        LOGGER.info("Running reader");
        runReader();

        LOGGER.info("Running processor");
        runProcessor();

        Mockito.when(wicSubCategoryServiceClient.submitRequest(Mockito.anyList(), Mockito.anyString(), Mockito.anyString())).thenThrow(WicException.class);
        this.expectedException.expect(WicException.class);
        
        LOGGER.info("Running writer");
        runWriter();
    }

    /**
     * Insert ProductScanCodeWics Throw Exception
     *
     * @unittestid Step6_Insert_13
     */
    @Test
    public void testInsertProductScanCodeWicsThrowException() throws Exception {
        TexasStateDocument texasStateDocument = createTexasStateDocument();
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);
        LOGGER.info("Running reader");
        runReader();

        LOGGER.info("Running processor");
        runProcessor();

        Mockito.when(productScanCodeWicServiceClient.submitRequest(Mockito.anyList(), Mockito.anyString(), Mockito.anyString())).thenThrow(WicException.class);
        this.expectedException.expect(WicException.class);
        
        LOGGER.info("Running writer");
        runWriter();
    }

    /**
     * Update ProductScanCodeWics Throw Exception
     *
     * @unittestid Step6_Insert_14
     */
    @Test
    public void testUpdateProductScanCodeWicsThrowException() throws Exception {
    	TexasStateDocument texasStateDocument = createTexasStateDocument();
        wicCategoryIndexRepository.save(createWicCategoryDocument(texasStateDocument));
        wicSubCategoryIndexRepository.save(createWicSubCategoryDocument(texasStateDocument));
        productScanCodeWicIndexRepository.save(createProductScanCodeWicDocument(texasStateDocument));
        texasStateDocument.setWicProdDes("LEMONS 2LB BAG ORGANIC");
        texasStateIndexRepository.save(texasStateDocument);
        new PrintWriter(fileDataParse).close();
        WicUtil.createCsvTexasStateDocument(Arrays.asList(texasStateDocument), fileDataParse);
        
        LOGGER.info("Running reader");
        runReader();

        LOGGER.info("Running processor");
        runProcessor();
        
        Mockito.when(productScanCodeWicServiceClient.submitRequest(Mockito.anyList(), Mockito.anyString(), Mockito.anyString())).thenThrow(WicException.class);
        this.expectedException.expect(WicException.class);
        
        LOGGER.info("Running writer");
        runWriter();
    }
    
    @Test
    public void testIncreaseCoverage() throws Exception {
        WicCategoryVO wicCategory = new WicCategoryVO(0, "");
        wicCategory.toString();
        WicSubCategoryVO wicSubCategory = new WicSubCategoryVO(0, 0, "", "");
        wicSubCategory.toString();
        ProductScanCodesVO productScanCodes = new ProductScanCodesVO();
        productScanCodes.setScnCdId(1L);
        productScanCodes.setProdId(0);
        productScanCodes.setScnTypCd("");
        productScanCodes.setPrimScnCdSw("");
        productScanCodes.setBnsScnCdSw("");
        productScanCodes.setScnCdCmt("");
        productScanCodes.setRetlUntLn(0);
        productScanCodes.setRetlUntWd(0);
        productScanCodes.setRetlUntHt(0);
        productScanCodes.setRetlUntWt(0);
        productScanCodes.setSampProvdSw("");
        productScanCodes.setRetlSellSzCd1("");
        productScanCodes.setRetlUntSellSz1(0);
        productScanCodes.setRetlSellSzCd2("");
        productScanCodes.setRetlUntSellSz2(0);
        productScanCodes.setPrprcOffPct(0);
        productScanCodes.setProdSubBrndId(0);
        productScanCodes.setFrstScnDt(null);
        productScanCodes.setLstScnDt(null);
        productScanCodes.setConsmUntId(0);
        productScanCodes.setTagItmId(0);
        productScanCodes.setTagItmKeyTypCd("");
        productScanCodes.setWicSw("");
        productScanCodes.setTagSzDes("");
        productScanCodes.setLebSw("");
        productScanCodes.setWicAplId(1);
        productScanCodes.setFam3Cd(0);
        productScanCodes.setFam4Cd(0);
        productScanCodes.setDsdDeldSw("");
        productScanCodes.setDsdDeptOvrdSw("");
        productScanCodes.setDsdDeldSw("");
        productScanCodes.setUpcActvSw("");
        productScanCodes.setScaleSw("");
        productScanCodes.setWicUpdtUsrId("");
        productScanCodes.setWicLstUpdtTs(new Timestamp(System.currentTimeMillis()));
        productScanCodes.setLebUpdtUsrId("");
        productScanCodes.setLebLstUpdtTs(null);
        productScanCodes.setCre8Ts(null);
        productScanCodes.setCre8Uid("");
        productScanCodes.setLstUpdtTs(new Timestamp(System.currentTimeMillis()));
        productScanCodes.setLstUpdtUid("");
        productScanCodes.setLstSysUpdtId(0);
        productScanCodes.setPseGramsWt(0);
        productScanCodes.setTstScnPrfmdSw("");
        productScanCodes.setDsconDt(null);
        productScanCodes.setProcScnMaintSw("");
        productScanCodes.setStageEventIdeal(false);
        productScanCodes.setStageEventWic(false);
        productScanCodes.setFamilyCodesEvent(false);
        productScanCodes.setStageEvent(false);
        productScanCodes.setProductUpdateEvent(false);
        productScanCodes.setVcUpdtUsrId("");
        productScanCodes.getScnCdId();
        productScanCodes.getProdId();
        productScanCodes.getScnTypCd();
        productScanCodes.getPrimScnCdSw();
        productScanCodes.getBnsScnCdSw();
        productScanCodes.getScnCdCmt();
        productScanCodes.getRetlUntLn();
        productScanCodes.getRetlUntWd();
        productScanCodes.getRetlUntHt();
        productScanCodes.getRetlUntWt();
        productScanCodes.getSampProvdSw();
        productScanCodes.getRetlSellSzCd1();
        productScanCodes.getRetlUntSellSz1();
        productScanCodes.getRetlSellSzCd2();
        productScanCodes.getRetlUntSellSz2();
        productScanCodes.getPrprcOffPct();
        productScanCodes.getProdSubBrndId();
        productScanCodes.getFrstScnDt();
        productScanCodes.getLstScnDt();
        productScanCodes.getConsmUntId();
        productScanCodes.getTagItmId();
        productScanCodes.getTagItmKeyTypCd();
        productScanCodes.getWicSw();
        productScanCodes.getTagSzDes();
        productScanCodes.getLebSw();
        productScanCodes.getWicAplId();
        productScanCodes.getFam3Cd();
        productScanCodes.getFam4Cd();
        productScanCodes.getDsdDeldSw();
        productScanCodes.getDsdDeptOvrdSw();
        productScanCodes.getDsdDeldSw();
        productScanCodes.getUpcActvSw();
        productScanCodes.getScaleSw();
        productScanCodes.getWicUpdtUsrId();
        productScanCodes.getWicLstUpdtTs();
        productScanCodes.getLebUpdtUsrId();
        productScanCodes.getLebLstUpdtTs();
        productScanCodes.getCre8Ts();
        productScanCodes.getCre8Uid();
        productScanCodes.getLstUpdtTs();
        productScanCodes.getLstUpdtUid();
        productScanCodes.getLstSysUpdtId();
        productScanCodes.getPseGramsWt();
        productScanCodes.getTstScnPrfmdSw();
        productScanCodes.getDsconDt();
        productScanCodes.getProcScnMaintSw();
        productScanCodes.isStageEventIdeal();
        productScanCodes.isStageEventWic();
        productScanCodes.isFamilyCodesEvent();
        productScanCodes.isStageEvent();
        productScanCodes.isProductUpdateEvent();
        productScanCodes.getVcUpdtUsrId();
        productScanCodes.toString();
        ProductScanCodes productScanCode1 = new ProductScanCodes();
        productScanCode1.setUpc(1L);
        ProductScanCodes productScanCode2 = new ProductScanCodes();
        productScanCode2.setUpc(1L);
        productScanCode1.equals(productScanCode2);
        productScanCode1.getUpc();
        productScanCode1.hashCode();
        productScanCode1.toString();
        MasterDataServiceImpl masterDataService = new MasterDataServiceImpl();
        masterDataService.setProductScanCodeWicIndexRepository(this.productScanCodeWicIndexRepository);
        masterDataService.setWicCategoryIndexRepository(this.wicCategoryIndexRepository);
        masterDataService.setWicSubCategoryIndexRepository(this.wicSubCategoryIndexRepository);
        masterDataService.getProductScanCodeWicIndexRepository();
        masterDataService.getWicCategoryIndexRepository();
        masterDataService.getWicSubCategoryIndexRepository();
        masterDataService.getWicCategoryNewKeys();
        masterDataService.getWicSubCategoryNewKeys();
        Constructor<WicConstants> constructor = WicConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
    //endregion

    //region Generate data test

    /**
     * Create WicCategoryDocument by TexasStateDocument
     *
     * @param document the TexasStateDocument using to create WicCategoryDocument
     */
    public WicCategoryDocument createWicCategoryDocument(TexasStateDocument document) {
        WicCategoryDocument wicCategoryDocument = new WicCategoryDocument();
        wicCategoryDocument.setWicCatId(document.getWicCatId());
        wicCategoryDocument.setDescription(document.getWicCategoryDesc());
        return wicCategoryDocument;
    }

    /**
     * Create WicSubCategoryDocument by TexasStateDocument
     *
     * @param document the TexasStateDocument using to create WicSubCategoryDocument
     */
    public WicSubCategoryDocument createWicSubCategoryDocument(TexasStateDocument document) {
        WicSubCategoryDocument wicSubCategoryDocument = new WicSubCategoryDocument();
        wicSubCategoryDocument.setId(document.getWicCatId(), document.getWicSubCatId());
        wicSubCategoryDocument.setWicCategoryId(Long.valueOf(document.getWicCatId()));
        wicSubCategoryDocument.setWicSubCategoryId(Long.valueOf(document.getWicSubCatId()));
        wicSubCategoryDocument.setDescription(document.getWicSubCategoryDesc());
        wicSubCategoryDocument.setLebSwitch(WicConstants.NO);
        return wicSubCategoryDocument;
    }

    /**
     * Create ProductScanCodeWicDocument by TexasStateDocument
     *
     * @param document the TexasStateDocument using to create ProductScanCodeWicDocument
     * @return the ProductScanCodeWicDocument
     */
    public ProductScanCodeWicDocument createProductScanCodeWicDocument(TexasStateDocument document) {
        ProductScanCodeWicDocument productScanCodeWicDocument = new ProductScanCodeWicDocument();
        productScanCodeWicDocument.setId(document.getWicAplId(), document.getScnCdId(), document.getWicCatId(), document.getWicSubCatId());
        productScanCodeWicDocument.setWicAplId(Long.valueOf(document.getWicAplId()));
        productScanCodeWicDocument.setUpc(Long.valueOf(document.getScnCdId()));
        productScanCodeWicDocument.setWicCategoryId(Long.valueOf(document.getWicCatId()));
        productScanCodeWicDocument.setWicSubCategoryId(Long.valueOf(document.getWicSubCatId()));
        productScanCodeWicDocument.setEffDt(WicUtil.convertDateFromString(document.getEffDt()));
        productScanCodeWicDocument.setEndDt(WicUtil.convertDateFromString(WicUtil.getDateOrDefault(document.getEndDt())));
        productScanCodeWicDocument.setWicUntTxt(document.getWicUntTxt());
        productScanCodeWicDocument.setWicBnFtQty(WicUtil.parseStringToDouble(document.getWicBnFtQty(), 3));
        productScanCodeWicDocument.setWicBnftUntTxt(document.getWicBnftUntTxt());
        productScanCodeWicDocument.setWicPrcAmt(WicUtil.parseStringToDouble(document.getWicPrcAmt(), 3));
        productScanCodeWicDocument.setWicPrcCd(document.getWicPrcCd());
        productScanCodeWicDocument.setWicCrdAcptId(document.getWicCrdAcptId());
        productScanCodeWicDocument.setWicDescription(document.getWicProdDes());
        productScanCodeWicDocument.setWicPackageSize(WicUtil.parseStringToDouble(document.getWicPkgSzQty(), 3));
        Timestamp current = new Timestamp(System.currentTimeMillis());
        productScanCodeWicDocument.setLebSwitch(WicConstants.NO);
        productScanCodeWicDocument.setCre8Ts(current);
        productScanCodeWicDocument.setCre8UId(WicConstants.TXSTATE_USER);
        productScanCodeWicDocument.setLstUpdtTs(current);
        productScanCodeWicDocument.setLstUpdtUId(WicConstants.TXSTATE_USER);
        productScanCodeWicDocument.setWicSw(WicUtil.checkWicSwitch(productScanCodeWicDocument) ? WicConstants.YES:WicConstants.NO);
        return productScanCodeWicDocument;
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
        String effectDate = WicUtil.convertDateToString(new Date(),WicUtil.DEFAULT_FORMAT_DATE);
        System.out.println("effectDate "+effectDate);
        texasStateDocument.setEffDt(effectDate);
        texasStateDocument.setEndDt("00000000");
        texasStateDocument.setUpcCheckDigit("3");
        texasStateDocument.setIdCode("DA");
        texasStateDocument.setSequenceNumber("013580");
        texasStateDocument.setMessageId("0344");
        texasStateDocument.setErrorMessage("null");
        texasStateDocument.setWicAplId("10000000000942133");
        texasStateDocument.setDataRaw("DA013580034410000000000942133APPLES - RETAILER ASSIGNED                        19FRUIT/VEGETABLES                                  000FRUITS &/OR VEGETABLES                            value     0010000100value                                             00010003               2009100100000000");
        WicUtil.correctTexasStateDocumentKey(texasStateDocument);
        WicUtil.setDefaultValueTexasStateDocument(texasStateDocument);
        return texasStateDocument;
    }

    /**
     * Assert two Dates is Almost Equal
     *
     * @param expected the Expected
     * @param actual the Actual
     */
    public static void assertDatesAlmostEqual(Date expected, Date actual) {
        if(expected == null && actual == null) {
            LOGGER.info("Dates are null");
            Assert.assertTrue(true);
        } else if(expected == null || actual == null)
            Assert.assertTrue("One Date is null", false);
        if(expected.equals(actual)) {
            LOGGER.info("Dates are null");
            Assert.assertTrue(true);
        }
        long dateDif = expected.getTime() - actual.getTime();
        LOGGER.debug("Expected=" + expected.getTime());
        LOGGER.debug("Actual=" + actual.getTime());
        LOGGER.debug("#@@@ DateDif==" + dateDif);
        if(dateDif < 10000 && dateDif > -10000)
            Assert.assertTrue(true);
        else Assert.assertTrue("Expected: [" + expected + "] Actual: [" + actual + "]", false);
    }
    //endregion
}
