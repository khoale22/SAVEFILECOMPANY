/*
 * DeleteScanCodesWICAndUpdateScanCodesProcessor class.
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.processor;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.index.WicCategoryDocument;
import com.heb.batch.wic.index.WicSubCategoryDocument;
import com.heb.batch.wic.index.repository.ProductScanCodeWicIndexRepository;
import com.heb.batch.wic.index.repository.TexasStateIndexRepository;
import com.heb.batch.wic.index.repository.WicCategoryIndexRepository;
import com.heb.batch.wic.index.repository.WicSubCategoryIndexRepository;
import com.heb.batch.wic.service.TexasStateData;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Returns ProductScanCodes from ProductScanCodeDocument but not in TexasStateIndexRepository
 *
 * @author vn03500
 * @since 1.0.0
 */
public class DeleteScanCodesWICAndUpdateScanCodesProcessor implements ItemProcessor<ProductScanCodeWic, ProductScanCodeWicDocument> , StepExecutionListener{
	@Autowired
	private TexasStateIndexRepository texasStateIndexRepository;
	@Autowired
	private WicCategoryIndexRepository wicCategoryIndexRepository;
	@Autowired
	private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;
	@Autowired
	private ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository;
	@Autowired
	private TexasStateData texasStateData;
	private List<String> keyChecks;
	/**
	 * Called by the Spring Batch framework. It will wrap a ProductScanCodes in a
	 * ProductScanCodeDocument and return it.
	 *
	 * @param item The ProductScanCodeDocument to wrap.
	 * @return The wrapped ProductScanCodes.
	 * @throws Exception
	 */
	@Override
	public ProductScanCodeWicDocument process(ProductScanCodeWic item) throws Exception {
		ProductScanCodeWicDocument productScanCodeWicDocument = null;
		if(item != null) {
			String keyDelete = String.valueOf(item.getKey().getUpc()).concat(String.valueOf(item.getKey().getWicApprovedProductListId()))
					.concat(String.valueOf(item.getKey().getWicCategoryId())).concat(String.valueOf(item.getKey().getWicSubCatId()));
			if(!keyChecks.contains(keyDelete)) {
				List<TexasStateDocument> txDocument = texasStateIndexRepository.findByScnCdIdAndWicAplIdAndWicCatIdAndWicSubCatId(String.valueOf(item.getKey().getUpc()),
						String.valueOf(item.getKey().getWicApprovedProductListId()), String.valueOf(item.getKey().getWicCategoryId()), String.valueOf(item.getKey().getWicSubCatId()));
				String keyProductScanCodeWic = ProductScanCodeWicDocument.generateId(item.getKey().getWicApprovedProductListId(), item.getKey().getUpc(),
						item.getKey().getWicCategoryId(), item.getKey().getWicSubCatId());
				Optional<ProductScanCodeWicDocument> productScanCodeWicDocumentOptional = this.productScanCodeWicIndexRepository.findById(keyProductScanCodeWic);
				if(productScanCodeWicDocumentOptional.isPresent()){
					keyChecks.add(keyDelete);
					ProductScanCodeWicDocument productScanCodeWicDocumentCheck = productScanCodeWicDocumentOptional.get();
					this.setDescriptionCategory(productScanCodeWicDocumentCheck,item);
					// check existing on File or Not Wicable
					if (txDocument.isEmpty() || WicConstants.NO.equals(productScanCodeWicDocumentCheck.getWicSw())) {
						item.setAction(WicConstants.DELETE);
						productScanCodeWicDocumentCheck.setAction(WicConstants.DELETE);
						productScanCodeWicDocument = productScanCodeWicDocumentCheck;
						productScanCodeWicDocument.setWicSw(WicConstants.NO);
					} else if(WicUtil.checkProductScanCodeUpdate(this.texasStateData.getProductScanCodeWicDocuments(),productScanCodeWicDocumentCheck)){
						productScanCodeWicDocumentCheck.setAction(WicConstants.UPDATE);
						this.texasStateData.getProductScanCodeChangeWicSwitchs().add(productScanCodeWicDocumentCheck);
					}
				}
			}
		}
		return productScanCodeWicDocument;
	}
	/**
	 * Sets WicCategory , WicSubCategory Description.
	 * ProductScanCodeDocument and return it.
	 *
	 * @param productScanCodeWicDocument The ProductScanCodeWicDocument to wrap.
	 * @param item The ProductScanCodeWic.
	 * @author vn55306
	 */
	private void setDescriptionCategory(ProductScanCodeWicDocument productScanCodeWicDocument,ProductScanCodeWic item){
		String wicCategoryDescription = StringUtils.EMPTY;
		String wicSubCategoryDescription = StringUtils.EMPTY;
		Optional<WicCategoryDocument> wicCategoryDocuments = this.wicCategoryIndexRepository.findById(String.valueOf(item.getKey().getWicCategoryId()));
		if (wicCategoryDocuments.isPresent()) {
			wicCategoryDescription = wicCategoryDocuments.get().getDescription();
		}
		String subCategoryDocumentId = WicSubCategoryDocument.generateId(item.getKey().getWicCategoryId(), item.getKey().getWicSubCatId());
		Optional<WicSubCategoryDocument> wicSubCategoryDocuments = wicSubCategoryIndexRepository.findById(subCategoryDocumentId);
		if (wicSubCategoryDocuments.isPresent()) {
			wicSubCategoryDescription = wicSubCategoryDocuments.get().getDescription();
		}
		productScanCodeWicDocument.setWicCategoryDesc(wicCategoryDescription);
		productScanCodeWicDocument.setWicSubCategoryDesc(wicSubCategoryDescription);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		keyChecks = new ArrayList<>();
		keyChecks.addAll(this.texasStateData.getKeyDeleteChangeDatas());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
