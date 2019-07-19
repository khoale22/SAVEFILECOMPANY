/*
 * EcommerceTaskRemoveProductsReader.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.job.MassUpdateProductMap;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.UUID;

/**
 * The reader for the mass update job. It will return a list of products to mass update based on the ecommerce task's
 * or alert referenced transaction id.
 *
 * @author vn40486
 * @since 2.16.0
 */
public class EcommerceTaskRemoveProductsReader implements ItemReader<CandidateWorkRequest>, StepExecutionListener {
	private static final Logger logger = LoggerFactory.getLogger(EcommerceTaskRemoveProductsReader.class);

	private static final String ERROR_SEARCH_CRITERIA_NOT_AVAILABLE = "No search criteria defined for transaction ID: %d";

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Value("#{jobParameters['assigneeId']}")
	private String assignee;

	@Value("#{jobParameters['showOnSite']}")
	private String showOnSite;

	@Value("#{jobParameters['salesChannels']}")
	private String salesChannels;

	@Autowired
	private MassUpdateProductMap massUpdateProductMap;
	@Autowired
	private EcommerceTaskService ecommerceTaskService;

	private int pageSize = 100;
	/**
	 * The number of records in a batch
	 */
	@Value("${app.commitInterval100}")
	private int commitInterval;
	/**
	 * Total of reading pages in a batch
	 */
	private int currentPages = 0;
	/**
	 * Total of pages exist in DB
	 */
	private int totalPages = 0;
	/**
	 * The page number, which query in DB
	 */
	private int page = 0;
	/**
	 * The number of eligible products in a batch
	 */
	private int countEligibleProducts = 0;
	/**
	 * The number of reading products in the current page
	 */
	private int countReadingProducts = 0;
	/**
	 * The batch finish flag
	 */
	private boolean isFinishBatch = false;
	private ProductSearchCriteria productSearchCriteria;
	private Iterator<CandidateWorkRequest> products;
	/**
	 * Set to TRUE if at least one eligible product (not excluded) found for processing.
	 */
	private boolean isBatchContainsEligibleProducts = false;

	/**
	 * Called by the Spring Batch framework. It will return the next product to add to the mass update
	 *
	 * @return The ID of a product to process.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	public CandidateWorkRequest read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		// SetupNextBatch will set the iterator to null once the search service
		// stops returning data.
		if (this.products == null) {
			logger.info("EcommerceTaskRemoveProductsReader with transaction id %s -- Ends", this.transactionId);
			return null;
		}
		// There's may be a list of products to exclude. Loop through the list of products
		// until we find a product ID that's not part of that list.
		while (this.products.hasNext()) {
			this.countReadingProducts ++;
			/* In the case the number of eligible products is equals the number of records in a batch then stop read this page
            because in the case un check on the UI then in one batch may be read many pages until enough a batch.*/
			if( this.countEligibleProducts == this.commitInterval){
				break;
			}
			CandidateWorkRequest product = this.products.next();
			if (this.productSearchCriteria.getExcludedProducts() == null ||
					!this.productSearchCriteria.getExcludedProducts().contains(product.getProductId())) {
				this.isBatchContainsEligibleProducts = true;
				this.countEligibleProducts++;
				return product;
			}
		}
		/* In the case the number of eligible products is equals the number of records in a batch then finish this batch*/
		if( this.countEligibleProducts == this.commitInterval){
			this.isFinishBatch = true;
		}
		if (!this.isBatchContainsEligibleProducts || !this.isFinishBatch)  {
			/* If no eligible products are found in the current page(batch) or finish a batch, fetch next page(batch)*/
			this.page++;
		}

		/* In the case have not read all pages yet or number of reading products in this page < page size
		 then call query DB and read continue*/
		if(this.currentPages < this.totalPages || this.countReadingProducts < this.pageSize){
			this.setupNextBatch(false);
			return this.read();
		}
		return null;
	}

	/**
	 * Called by the Spring framework when the job starts. This will pull the list of products
	 * from the parameters map so that they can be iterated through.
	 *
	 * @param stepExecution The context the job is executing in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("EcommerceTaskRemoveProductsReader with transaction id %s -- Begins", this.transactionId);
		this.productSearchCriteria = this.massUpdateProductMap.get(this.transactionId);
		if (this.productSearchCriteria == null) {
			throw new IllegalArgumentException(
					String.format(ERROR_SEARCH_CRITERIA_NOT_AVAILABLE, this.transactionId));
		}
		this.productSearchCriteria.setSessionId(UUID.randomUUID().toString());
		this.page = 0;
		this.countEligibleProducts = 0;
		this.isFinishBatch = false;
		this.countReadingProducts = 0;
		this.setupNextBatch(true);
	}

	/**
	 * Allows for configuring the page size when searching for products. The default is 100.
	 *
	 * @param pageSize The maximum number of records to request from the product search service
	 *                 for each call to the service.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * To be called after a page of data is exhausted. This will load the next batch of products.
	 * It will set the product master list to null once there is no more data available.
	 */
	private void setupNextBatch(boolean includeCounts) {
		if(this.isFinishBatch){
            /*In the case finish batch then reset to read from first page and start next batch */
			this.isFinishBatch = false;
			this.countEligibleProducts = 0;
			this.page = 0;
			this.currentPages = 0;
			this.totalPages --;
		}
		PageableResult<CandidateWorkRequest> productData =
				this.ecommerceTaskService.getTaskProducts(this.transactionId, assignee, showOnSite, salesChannels, includeCounts, this.page, this.pageSize);
		if(includeCounts && productData.getData().iterator().hasNext()){
			this.totalPages = productData.getPageCount();
		}
		if (!productData.getData().iterator().hasNext()) {
			this.products = null;
		} else {
			this.products = productData.getData().iterator();
			this.currentPages ++;
		}
		this.isBatchContainsEligibleProducts = false;
		/*Reset the number of reading products in the page*/
		this.countReadingProducts = 0;
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return null
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}