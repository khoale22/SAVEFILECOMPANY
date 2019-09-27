/*
 * MasterDataWrapperReader
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.reader;


import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.utils.WicConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ReaderNotOpenException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;

/**
 * Reads TexasStateDocument from the TexasStateIndexRepository.
 *
 * @author vn03512
 * @since 1.0.0
 */
public class MasterDataWrapperReader extends FlatFileItemReader<TexasStateDocument>  {
	private static final Logger LOGGER = LogManager.getLogger(MasterDataWrapperReader.class);
	@Value("${texasState.dataparse}")
	String fileDataParse;
	@PostConstruct
	private void initialize() {
		LOGGER.info("Start J50X100D-SYS-BNFT-UNT-DES-TXSTATE-STEP-4/J50X100D-SYS-MASTERDATA-TXSTATE-STEP-7");
		this.setLineMapper(this.createTexasStateDocumentLineMapper());
	}
	@Override
	protected void doOpen() throws Exception {
		try {
			Resource resource = new FileSystemResource(fileDataParse);
			if (resource.exists() && resource.contentLength() > 0) {
				this.setResource(resource);
				super.doOpen();
			}
		}catch(Exception e){
			LOGGER.error("MasterDataWrapperReader Exception ="+e.getMessage());
		}
	}
	@Override
	protected TexasStateDocument doRead() throws Exception {
		TexasStateDocument texasStateDocument = null;
		try {
			texasStateDocument = super.doRead();
		}catch (ReaderNotOpenException e){
			LOGGER.error("MasterDataWrapperReader ReaderNotOpenException="+e.getMessage());
		}catch (Exception e1){
			LOGGER.error("MasterDataWrapperReader Exception="+e1.getMessage());
		}
		return texasStateDocument;
	}
	private LineMapper<TexasStateDocument> createTexasStateDocumentLineMapper() {
		DefaultLineMapper<TexasStateDocument> texasStateDocumentLineMapper = new DefaultLineMapper<>();
		LineTokenizer texasStateDocumentTokenizer = createTexasStateDocumentLineTokenizer();
		texasStateDocumentLineMapper.setLineTokenizer(texasStateDocumentTokenizer);
		FieldSetMapper<TexasStateDocument> texasStateDocumentMapper = createTexasStateDocumentMapper();
		texasStateDocumentLineMapper.setFieldSetMapper(texasStateDocumentMapper);
		return texasStateDocumentLineMapper;
	}

	private LineTokenizer createTexasStateDocumentLineTokenizer() {
		DelimitedLineTokenizer studentLineTokenizer = new DelimitedLineTokenizer();
		studentLineTokenizer.setDelimiter(WicConstants.DELIMITER);
		studentLineTokenizer.setNames("id","aplPreFix", "wicAplId", "scnCdId","upcCheckDigit","wicCatId",
				                      "wicSubCatId");
		return studentLineTokenizer;
	}

	private FieldSetMapper<TexasStateDocument> createTexasStateDocumentMapper() {
		BeanWrapperFieldSetMapper<TexasStateDocument> texasStateDocumentMapper = new BeanWrapperFieldSetMapper<>();
		texasStateDocumentMapper.setTargetType(TexasStateDocument.class);
		return texasStateDocumentMapper;
	}
}

