/*
 * BrandCostOwnerTop2TopService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.codeTable.brandCostOwnerT2T;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.pm.entity.CostOwner;
import com.heb.pm.entity.ProductBrandCostOwner;
import com.heb.pm.entity.TopToTop;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all of the business logic relative to Brand Cost Owner T2T.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Service
public class BrandCostOwnerTop2TopService {
	@CoreEntityManager
	@Autowired
	private EntityManager entityManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(BrandCostOwnerTop2TopService.class);
	/**
	 * Log error message.
	 */
	private static final String LOG_ERROR_MESSAGE_EXPORT_BRND_CST_OWNR_T2TS =
			"User %s from IP %s requested the brand cost owner top 2 top report by product-brand:%s, cost-owner:%s, T2T:%s: %s";
	/**
	 * The max number of records in batch to export csv.
	 */
	private static final int MAX_NUMBER_OF_RECORDS_IN_BATCH_TO_EXPORT = 50;
	/**
	 * Holds the format header to export csv.
	 */
	private static final String BRND_CST_OWNR_T2T_EXPORT_HEADER = "\"Brand\",\"Cost Owner\",\"Top 2 Top\"";
	/**
	 * Holds the format of content to export csv.
	 */
	private static final String BRND_CST_OWNR_T2T_EXPORT_FORMAT_CONTENT = "\"%s\",\"%s\",\"%s\"";
	private static final String JPA_CRITERIA_WILDCARD = "%";
	@Autowired
	private ProductBrandCostOwnerRepository productBrandCostOwnerRepository;
	@Autowired
	private ProductBrandCostOwnerRepositoryWithCount productBrandCostOwnerRepositoryWithCount;
	@Autowired
	private ProductBrandCostOwnerRepositoryImpl productBrandCostOwnerRepositoryImpl;
	@Autowired
	private ProductBrandCostOwnerRepositoryWithCountImpl productBrandCostOwnerRepositoryWithCountImpl;
	@Autowired
	private UserInfo userInfo;
	@Autowired
	private CostOwnerRepository costOwnerRepository;
	@Autowired
	private TopToTopRepository topToTopRepository;
	private LazyObjectResolver<Iterable<ProductBrand>> productBrandLazyObjectResolver = new BrandCostOwnerTop2TopService.ProductBrandResolver();


	/**
	 * Resolver for Product brand object.
	 */
	private class ProductBrandResolver implements LazyObjectResolver<Iterable<ProductBrand>> {

		/**
		 * Resolves all of the associate product brand tiers information.
		 *
		 * @param productBrands the product brand page to fetch the product brand tiers for.
		 */
		@Override
		public void fetch(Iterable<ProductBrand> productBrands) {
			productBrands.forEach((productBrand) -> {
				productBrand.getProductBrandCostOwners().size();
				if(productBrand.getProductBrandCostOwners()!=null){
					productBrand.getProductBrandCostOwners().forEach(costOwner ->{
						if(costOwner!=null) {
							costOwner.getCostOwner().getTopToTop();
							if (costOwner.getCostOwner().getTopToTop() != null)
								costOwner.getCostOwner().getTopToTop().getTopToTopId();
						}

					});
				}

			});
		}
	}
	/**
	 * Find all product Brand Cost Owners.
	 *
	 * @param page         the start position that want to get.
	 * @param pageSize     the number of rows that want to get per page.
	 * @param includeCount Set to true to include the record counts.
	 * @return the Page object with type ProductBrandCostOwner.
	 */
	public Page<ProductBrandCostOwner> findAll(int page, int pageSize, boolean includeCount) {
		long totalElements = 0;
		List<ProductBrandCostOwner> brandCostOwnerT2Ts;
		Pageable pageable = new PageRequest(page, pageSize, ProductBrandCostOwner.getDefaultSort());
		if (includeCount) {
			Page<ProductBrandCostOwner> pageObject = productBrandCostOwnerRepositoryWithCount.findAll(pageable);
			brandCostOwnerT2Ts = pageObject.getContent();
			totalElements = pageObject.getTotalElements();
		} else {
			brandCostOwnerT2Ts = productBrandCostOwnerRepository.findAllBy(pageable);
		}
		return new PageImpl<>(brandCostOwnerT2Ts, pageable, totalElements);
	}
	/**
	 * Find the list of productBrandCostOwners by paging and the params that you want to filter as product brand description, cost owner name and top 2 top name.
	 *
	 * @param productBrand the description or id that you want to filter.
	 * @param costOwner           the name or id of cost owner that you want to filter.
	 * @param top2Top             the name or id of top 2 top that you want to filter.
	 * @param page                    the start position that want to get.
	 * @param pageSize                the number of rows that want to get per page.
	 * @param includeCount            Set to true to include the record counts.
	 * @return the Page object with type ProductBrandCostOwner.
	 */
	public Page<ProductBrandCostOwner> findByCriteria(String productBrand, String costOwner, String top2Top, int page, int pageSize, boolean includeCount) {
		long totalElements = 0;
		List<ProductBrandCostOwner> brandCostOwnerT2Ts;
		Pageable pageable = new PageRequest(page, pageSize, ProductBrandCostOwner.getDefaultSort());
		if (includeCount) {
			Page<ProductBrandCostOwner> pageObject = productBrandCostOwnerRepositoryWithCountImpl.findBrndCstOwnrT2TByProductBrandAndCostOwnerAndTop2Top(
					productBrand,
					costOwner,
					top2Top,
					pageable);
			brandCostOwnerT2Ts = pageObject.getContent();
			totalElements = pageObject.getTotalElements();
		} else {
			brandCostOwnerT2Ts = productBrandCostOwnerRepositoryImpl.findBrndCstOwnrT2TByProductBrandAndCostOwnerAndTop2Top(
					productBrand,
					costOwner,
					top2Top,
					pageable);
		}
		return new PageImpl<>(brandCostOwnerT2Ts, pageable, totalElements);
	}
	/**
	 * Generates a CSV of all BrandCostOwnerT2Ts.
	 *
	 * @param outputStream            The output stream to write the CSV to.
	 */
	public void streamAll(ServletOutputStream outputStream) {
		// Print out the header
		try {
			outputStream.println(BRND_CST_OWNR_T2T_EXPORT_HEADER);
		} catch (IOException e) {
			LOGGER.error(String.format(LOG_ERROR_MESSAGE_EXPORT_BRND_CST_OWNR_T2TS, userInfo.getUserId(), null, null, null, e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		// Stream content
		// Get the list of ProductBrandCostOwners by productBrandDescription and costOwnerName and t2tName and first page.
		Page<ProductBrandCostOwner> pageResults = findAll(0, MAX_NUMBER_OF_RECORDS_IN_BATCH_TO_EXPORT, true);
		// Stream BrandCostOwnerTop2Top of first page
		this.streamBrndCstOwnrT2T(outputStream, pageResults.getContent());
		int totalPages = pageResults.getTotalPages();
		if (totalPages > 1) {
			// Stream next page
			for (int page = 1; page < totalPages; page++) {
				pageResults = findAll( page, MAX_NUMBER_OF_RECORDS_IN_BATCH_TO_EXPORT, false);
				// Stream next page.
				this.streamBrndCstOwnrT2T(outputStream, pageResults.getContent());
			}
		}
	}
	/**
	 * Generates a CSV of the list of BrandCostOwnerT2Ts. This includes all BrandCostOwnerT2Ts
	 * by the params that you want to filter as product brand description, cost owner name and top 2 top name.
	 *
	 * @param outputStream            The output stream to write the CSV to.
	 * @param productBrand the description or id to search brand for.
	 * @param costOwner           the name or id to search cost owner for.
	 * @param top2Top             the name or id to search top 2 top for.
	 */
	public void streamByCriteria(ServletOutputStream outputStream, String productBrand, String costOwner, String top2Top) {
		// Print out the header
		try {
			outputStream.println(BRND_CST_OWNR_T2T_EXPORT_HEADER);
		} catch (IOException e) {
			LOGGER.error(String.format(LOG_ERROR_MESSAGE_EXPORT_BRND_CST_OWNR_T2TS, userInfo.getUserId(), productBrand, costOwner, top2Top, e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		// Stream content
		// Get the list of ProductBrandCostOwners by productBrandDescription and costOwnerName and t2tName and first page.
		Page<ProductBrandCostOwner> pageResults = findByCriteria(productBrand, costOwner, top2Top,
				0, MAX_NUMBER_OF_RECORDS_IN_BATCH_TO_EXPORT, true);
		// Stream BrandCostOwnerTop2Top of first page
		this.streamBrndCstOwnrT2T(outputStream, pageResults.getContent());
		int totalPages = pageResults.getTotalPages();
		if (totalPages > 1) {
			// Stream next page
			for (int page = 1; page < totalPages; page++) {
				pageResults = findByCriteria(productBrand, costOwner, top2Top, page, MAX_NUMBER_OF_RECORDS_IN_BATCH_TO_EXPORT, false);
				// Stream next page.
				this.streamBrndCstOwnrT2T(outputStream, pageResults.getContent());
			}
		}
	}
	/**
	 * Generates a CSV of a list of productBrandCostOwners.
	 *
	 * @param outputStream           The output stream to write the CSV to.
	 * @param productBrandCostOwners the list of ProductBrandCostOwners to stream.
	 */
	private void streamBrndCstOwnrT2T(ServletOutputStream outputStream, List<ProductBrandCostOwner> productBrandCostOwners) {
		productBrandCostOwners.forEach((ProductBrandCostOwner) -> {
			// Write out the productBrandCostOwner to the stream.
			try {
				outputStream.println(
						String.format(BRND_CST_OWNR_T2T_EXPORT_FORMAT_CONTENT, ProductBrandCostOwner.getProductBrand().getProductBrandDescription(),
								ProductBrandCostOwner.getCostOwner().getCostOwnerName(),
								ProductBrandCostOwner.getCostOwner().getTopToTop().getTopToTopName())
				);
			} catch (IOException e) {
				LOGGER.error(String.format(LOG_ERROR_MESSAGE_EXPORT_BRND_CST_OWNR_T2TS, userInfo.getUserId(), null, null, null, e.getMessage()));
				throw new StreamingExportException(e.getMessage(), e.getCause());
			}
		});
	}

	/**
	 * find cost owner by id or name
	 * @param page page number
	 * @param pageSize page size number
	 * @param searchText the text to search id or name
	 * @return PageableResult
	 */
	public PageableResult<CostOwner> findCostOwnerByIdAndName(int page, int pageSize, String searchText){
		PageableResult<CostOwner> results;
		// Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		// Builds the criteria for the main query.
		CriteriaQuery<CostOwner> queryBuilder = criteriaBuilder.createQuery(CostOwner.class);
		// Select from product brand
		Root<CostOwner> pmRoot = queryBuilder.from(CostOwner.class);
		// Build query.
		queryBuilder.select(pmRoot);

		Expression<String> literal = criteriaBuilder.literal(JPA_CRITERIA_WILDCARD.concat(searchText.toUpperCase()).concat(JPA_CRITERIA_WILDCARD));
		queryBuilder.where(criteriaBuilder.or(
				criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(CostOwner_.costOwnerName)),literal),
				criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(CostOwner_.costOwnerId).as(String.class)),literal)
		));
		queryBuilder.orderBy(criteriaBuilder.desc(criteriaBuilder.selectCase()
				.when(criteriaBuilder.like(criteriaBuilder.trim(pmRoot.get(CostOwner_.costOwnerName)),
						searchText.toUpperCase().concat(JPA_CRITERIA_WILDCARD)),1)
				.when(criteriaBuilder.equal(criteriaBuilder.trim(pmRoot.get(CostOwner_.costOwnerId).as(String.class)), searchText.toUpperCase()),1)
				.otherwise(0)),criteriaBuilder.asc(pmRoot.get(CostOwner_.costOwnerName)));
		TypedQuery<CostOwner> pmTQuery = this.entityManager.createQuery(queryBuilder);
		pmTQuery.setFirstResult(page).setMaxResults(pageSize);
		List<CostOwner> productBrands = pmTQuery.getResultList();
		results = new PageableResult<>(page, productBrands);
		return results;
	}

	/**
	 * Find Top to Top data by Id or Name
	 * @param page page number
	 * @param pageSize page size
	 * @param searchText the text to search top to top
	 * @return PageableResult
	 */
	public PageableResult<TopToTop> findTopToTopByIdAndName(int page, int pageSize, String searchText){
		PageableResult<TopToTop> results;
		// Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		// Builds the criteria for the main query.
		CriteriaQuery<TopToTop> queryBuilder = criteriaBuilder.createQuery(TopToTop.class);
		// Select from product brand
		Root<TopToTop> pmRoot = queryBuilder.from(TopToTop.class);
		// Build query.
		queryBuilder.select(pmRoot);

		Expression<String> literal = criteriaBuilder.literal(JPA_CRITERIA_WILDCARD.concat(searchText.toUpperCase()).concat(JPA_CRITERIA_WILDCARD));
		queryBuilder.where(criteriaBuilder.or(
				criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(TopToTop_.topToTopName)),literal),
				criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(TopToTop_.topToTopId).as(String.class)),literal)
		));

		queryBuilder.orderBy(criteriaBuilder.desc(criteriaBuilder.selectCase()
				.when(criteriaBuilder.like(criteriaBuilder.upper(criteriaBuilder.trim(pmRoot.get(TopToTop_.topToTopName))),
						searchText.toUpperCase().concat(JPA_CRITERIA_WILDCARD)),1)
				.when(criteriaBuilder.equal(criteriaBuilder.trim(pmRoot.get(TopToTop_.topToTopId).as(String.class)), searchText.toUpperCase()),1)
				.otherwise(0)),criteriaBuilder.asc(pmRoot.get(TopToTop_.topToTopName)));
		TypedQuery<TopToTop> pmTQuery = this.entityManager.createQuery(queryBuilder);
		pmTQuery.setFirstResult(page).setMaxResults(pageSize);
		List<TopToTop> productBrands = pmTQuery.getResultList();
		results = new PageableResult<>(page, productBrands);
		return results;
	}

	/**
	 * Find data product brand by selected filter.
	 * @param productBrand product brand id
	 * @param costOwner the cost owner id
	 * @param topToTop the top to top id
	 * @return
	 */
	public PageableResult<ProductBrand> findProductBrandsBySelectedFilter(Long productBrand, Integer costOwner, Integer topToTop){
		PageableResult<ProductBrand> results;
		// Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		// Builds the criteria for the main query.
		CriteriaQuery<ProductBrand> queryBuilder = criteriaBuilder.createQuery(ProductBrand.class);
		// Select from product brand
		Root<ProductBrand> pmRoot = queryBuilder.from(ProductBrand.class);
		// Build query.
		queryBuilder.distinct(true);
		queryBuilder.select(pmRoot);
		Join<ProductBrand, ProductBrandCostOwner> productBrandCostOwnerJoin = pmRoot.join(ProductBrand_.productBrandCostOwners);
		Join<ProductBrandCostOwner, CostOwner> costOwnerJoin = productBrandCostOwnerJoin.join(ProductBrandCostOwner_.costOwner);
		Join<CostOwner, TopToTop> topToTopJoin = costOwnerJoin.join(CostOwner_.topToTop);

		List<Predicate> predicates=new ArrayList<>();
		if(productBrand!=null){
			predicates.add(criteriaBuilder.equal(pmRoot.get(ProductBrand_.productBrandId),productBrand));
		}
		if( costOwner!=null){
			predicates.add(criteriaBuilder.equal(costOwnerJoin.get(CostOwner_.costOwnerId),costOwner));
		}
		if( topToTop!=null){
			predicates.add(criteriaBuilder.equal(topToTopJoin.get(TopToTop_.topToTopId),topToTop));
		}
		Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
		queryBuilder.where(criteriaBuilder.and(predicatesArray));

		TypedQuery<ProductBrand> pmTQuery = this.entityManager.createQuery(queryBuilder);

		List<ProductBrand> productBrands = pmTQuery.getResultList();
		results = new PageableResult<>(1, productBrands);
		this.productBrandLazyObjectResolver.fetch(results.getData());
		return results;

	}
}
