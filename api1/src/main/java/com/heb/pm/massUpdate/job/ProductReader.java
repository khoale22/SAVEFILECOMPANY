package com.heb.pm.massUpdate.job;

import com.heb.pm.customHierarchy.GenericEntityRelationshipService;
import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.productSearch.ProductSearchService;
import com.heb.util.jpa.PageableResult;
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
 * The reader for the mass update job. It will return a list of products to mass update.
 *
 * @author d116773
 * @since 2.12.0
 */
public class ProductReader implements ItemReader<Long>, StepExecutionListener {

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Autowired
	private MassUpdateProductMap massUpdateProductMap;

	@Autowired
	private ProductSearchService productSearchService;

	@Autowired
	private GenericEntityRelationshipService genericEntityRelationshipService;

	private int pageSize = 100;
	private int page = 0;

	private ProductSearchCriteria productSearchCriteria;

	private ProductFeeder productFeeder;

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
	public Long read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return this.productFeeder.read();
	}

	/**
	 * Called by the Spring framework when the job starts. This will pull the list of products
	 * from the parameters map so that they can be iterated through.
	 *
	 * @param stepExecution The context the job is executing in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.productSearchCriteria = this.massUpdateProductMap.get(this.transactionId);
		if (this.productSearchCriteria == null) {
			throw new IllegalArgumentException(
					String.format("No search criteria defined for transaction ID %d", this.transactionId));
		}
		this.productSearchCriteria.setSessionId(UUID.randomUUID().toString());
		this.page = 0;
		// If the entity typ is a product group then continue with either using group Id List or by page.
		if(GenericEntity.EntyType.PGRP.getName().equals(this.productSearchCriteria.getEntityType())) {
			// This is if the front end sends a product group List.
			if(this.productSearchCriteria.getProductGroupIds() != null &&
					!this.productSearchCriteria.getProductGroupIds().isEmpty()){
				// if this selected product group ids (1 list not page) have been sent
				this.productFeeder = new ProductGroupIdListFeeder(this.productSearchCriteria.getProductGroupIds().iterator());
			} else {
				// else this is a product group mass page batch job...get all current hierarchy relationships
				this.productFeeder = new ProductGroupIdPageFeeder(this.pageSize, this.productSearchCriteria, this.genericEntityRelationshipService);
			}
		} else {
			// Else it is a list of product Id's and we need to continue reading with product id's.
			this.productFeeder = new ProductIdFeeder(this.pageSize, this.productSearchCriteria, this.productSearchService);
		}
		this.productFeeder.init();
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
