package com.heb.batch.wic.step;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.heb.batch.wic.dao.JobParamDAO;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.index.repository.TexasStateIndexRepository;
import com.heb.batch.wic.processor.TexasStateProcessor;
import com.heb.batch.wic.reader.TexasStateReader;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.service.impl.TexasFieldValidatorImpl;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.writer.TexasStateWriter;

@Transactional(transactionManager = "jpaTransactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:parse-texas-state-file-config-test.xml" })
public class ParseTexasStateFileStepTest {
	@Mock
	private MessageSource messageSource;
	@Mock
	private JobParamDAO jobParamDAO;
	@Mock
    private EmailService emailService;
	@Autowired
	private TexasStateIndexRepository texasStateIndexRepository;
	@Autowired
	@InjectMocks
	private TexasFieldValidatorImpl texasFieldValidatorImpl;
	@Autowired
	@InjectMocks
	private TexasStateReader reader;
	@Autowired
	@InjectMocks
	private TexasStateProcessor processor;
	@Autowired
	private TexasStateWriter writer;
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

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
		texasStateIndexRepository.deleteAll();
		MockitoAnnotations.initMocks(this);
		Mockito.when(this.jobParamDAO.getConfigurationInfor(Mockito.anyString())).thenReturn("any");
		Mockito.when(this.messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn("any");
	}
	
	@After
	public void after() {
		this.reader.close();
	}

	public TexasStateDocument runReader() throws Exception {
		this.reader.beforeStep(createStepExecution());
		ExecutionContext executionContext = new ExecutionContext();
		this.reader.open(executionContext);
		TexasStateDocument readTexasStateDocument = this.reader.read();
		this.reader.afterStep(createStepExecution());
		return readTexasStateDocument;
	}

	public TexasStateDocument runProcessor(TexasStateDocument readTexasStateDocument) throws Exception {
		this.processor.beforeStep(createStepExecution());
		TexasStateDocument processedTexasStateDocument = this.processor.process(readTexasStateDocument);
		this.processor.afterStep(createStepExecution());
		return processedTexasStateDocument;
	}

	public void runWriter(TexasStateDocument processedTexasStateDocument) throws Exception {
		List<TexasStateDocument> texasStateDocuments = new ArrayList<TexasStateDocument>();
		texasStateDocuments.add(processedTexasStateDocument);
		this.writer.write(texasStateDocuments);
	}

	@Test
	public void testReadDataValid() throws Exception {
		TexasStateDocument texasStateDocument = this.createTexasStateDocument();
		String wicFilePath = this.createTemporaryTexasStateFile(texasStateDocument.getDataRaw());
		Mockito.when(this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INPUT_NAS)).thenReturn(wicFilePath);
	    TexasStateDocument readTexasStateDocument = runReader();
		Assert.assertEquals(texasStateDocument.toString(), readTexasStateDocument.toString());
	}

	@Test
	public void testReadDataFileNotFound() throws Exception {
		Mockito.when(this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INPUT_NAS)).thenReturn(StringUtils.EMPTY);
		this.expectedException.expect(ItemStreamException.class);
		runReader();
	}

	@Test
	public void testReadDataFileEmpty() throws Exception {
		String wicFilePath = this.createTemporaryTexasStateFile(StringUtils.EMPTY);
		Mockito.when(this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INPUT_NAS)).thenReturn(wicFilePath);
		this.expectedException.expect(ItemStreamException.class);
		runReader();
	}

	@Test
	public void testReadDataFileCorrupt() throws Exception {
		TexasStateDocument texasStateDocument = this.createTexasStateDocument();
		String wicFilePath = this.createTemporaryTexasStateFile("ERR" + texasStateDocument.getDataRaw());
		Mockito.when(this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INPUT_NAS)).thenReturn(wicFilePath);
		Mockito.when(this.messageSource.getMessage(Mockito.eq("wic.email.production.file.corrupt"), Mockito.any(), Mockito.any())).thenReturn("File corrupt");
		this.expectedException.expect(WicException.class);
		this.expectedException.expectMessage("File corrupt");
		runReader();
	}

	@Test
	public void testProcessDataValid() throws Exception {
		TexasStateDocument readTexasStateDocument = this.createTexasStateDocument();
		TexasStateDocument processedTexasStateDocument = runProcessor(readTexasStateDocument);
		Assert.assertEquals(String.valueOf(Long.valueOf(readTexasStateDocument.getScnCdId())), processedTexasStateDocument.getScnCdId());
	}

	@Test
	public void testProcessDataInvalid() throws Exception {
		ReflectionTestUtils.setField(this.processor, "fileDataInvalid", new File(System.getProperty("java.io.tmpdir"), WicConstants.TEXAS_STATE_NAME).getAbsolutePath());
		TexasStateDocument readTexasStateDocument = this.createTexasStateDocument();
		readTexasStateDocument.setWicProdDes(StringUtils.EMPTY);
		TexasStateDocument processedTexasStateDocument = runProcessor(readTexasStateDocument);
		Assert.assertNull(processedTexasStateDocument);
	}

	@Test
	public void testWriteDataValid() throws Exception {
		TexasStateDocument processedTexasStateDocument = this.createTexasStateDocument();
		WicUtil.correctTexasStateDocumentKey(processedTexasStateDocument);
		WicUtil.setDefaultValueTexasStateDocument(processedTexasStateDocument);
		runWriter(processedTexasStateDocument);
		Assert.assertEquals(processedTexasStateDocument.toString(), this.texasStateIndexRepository.findAll().iterator().next().toString());
	}
	
	private String createTemporaryTexasStateFile(String fileContent) throws IOException {
		File file = new File(System.getProperty("java.io.tmpdir"), WicConstants.TEXAS_STATE_NAME);
		FileUtils.writeStringToFile(file, fileContent);
		FileUtils.forceDeleteOnExit(file);
		return file.getAbsolutePath();
	}

	private TexasStateDocument createTexasStateDocument() {
		TexasStateDocument texasStateDocument = new TexasStateDocument();
		texasStateDocument.setId(null);
		texasStateDocument.setWicAplId("10000000000942133");
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
		texasStateDocument.setWicCrdAcptId(StringUtils.EMPTY);
		texasStateDocument.setEffDt("20091001");
		texasStateDocument.setEndDt("00000000");
		texasStateDocument.setUpcCheckDigit("3");
		texasStateDocument.setIdCode("DA");
		texasStateDocument.setSequenceNumber("013580");
		texasStateDocument.setMessageId("0344");
		texasStateDocument.setErrorMessage(StringUtils.EMPTY);
		texasStateDocument.setDataRaw("DA013580034410000000000942133APPLES - RETAILER ASSIGNED                        19FRUIT/VEGETABLES                                  000FRUITS &/OR VEGETABLES                            value     0010000100value                                             00010003               2009100100000000");
		return texasStateDocument;
	}

}
