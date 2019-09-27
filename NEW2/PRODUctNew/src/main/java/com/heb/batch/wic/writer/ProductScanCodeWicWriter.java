/*
 * ProductScanCodesWriter
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.writer;
import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import com.heb.batch.wic.index.repository.ProductScanCodeWicIndexRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Persists productScanCodeWicDocuments to an index.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class ProductScanCodeWicWriter implements ItemWriter<ProductScanCodeWicDocument>, StepExecutionListener {

	private static final Logger LOGGER = LogManager.getLogger(ProductScanCodeWicWriter.class);

	@Autowired
	private ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository;

	/**
	 * Called by the Spring Batch framework to save productScanCodeDocuments to the index.
	 *
	 * @param productScanCodeWicDocuments The list of productScanCodeWicDocuments to save.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends ProductScanCodeWicDocument> productScanCodeWicDocuments) throws Exception {
		if(productScanCodeWicDocuments !=null && !productScanCodeWicDocuments.isEmpty()) {
			try {
				this.productScanCodeWicIndexRepository.saveAll(productScanCodeWicDocuments);
			}catch (Exception e){
				LOGGER.error("ProductScanCodeWicWriter "+e.getMessage());
			}

		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.debug("ProductScanCodeWicWriter beforeStep");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
