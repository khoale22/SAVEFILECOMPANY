/*
 * com.heb.pm.productDiscontinue.ProductDiscontinueService
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.Hits;
import com.heb.pm.entity.ItemNotDeletedReason;
import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.entity.ProductDiscontinueKey;
import com.heb.pm.repository.ProductDiscontinueRepository;
import com.heb.pm.repository.ProductDiscontinueRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Maintains all business logic related to the Product Discontinue module.
 *
 * @author d116773
 * @since 2.0.0
 */
@Service
public class ProductDiscontinueService {

	private static final Logger logger = LoggerFactory.getLogger(ProductDiscontinueService.class);

	// Delegates searching by item code to this service.
	@Autowired
	private ItemCodeSearchService itemCodeSearchService;

	// Delegates searching by UPC to this service.
	@Autowired
	private UpcSearchService upcSearchService;

	// Delegates searching by product ID to this service.
	@Autowired
	private ProductIdSearchService productIdSearchService;

	// Delegates findAll
	@Autowired
	private FindAllService findAllService;

	//app managed item not deleted reason types with their short description and instruction
	@Autowired @Qualifier("itemNotDelReasons")
	private Properties itemNotDelReasons;

	// Delegates searching by product hierarchy.
	@Autowired
	private ProductHierarchySearchService productHierarchySearchService;

	@Autowired
	private ProductDiscontinueRepository repository;

	@Autowired
	private ProductDiscontinueRepositoryWithCount repositoryWithCount;

	//Collection of all the app managed item not delted reason codes.
	private static final Map<String, ItemNotDeletedReason> ALL_DEL_REASONS = new HashMap<String, ItemNotDeletedReason>();

	/**
	 * Defines columns that are available to sort on.
	 */
	public enum SortColumn {
		UPC,
		ITEM_CODE,
		DISCONTINUE_ELIGIBLE
	}

	/**
	 * Defines directions to sort on.
	 */
	public enum SortDirection {
		ASC,
		DESC
	}

	/**
	 * Sets the ItemCodeSearchService for this object. This method is primarily used for testing.
	 *
	 * @param itemCodeSearchService The ItemCodeSearchService for this object to use.
	 */
	public void setItemCodeSearchService(ItemCodeSearchService itemCodeSearchService) {
		this.itemCodeSearchService = itemCodeSearchService;
	}

	/**
	 * Sets the UpcSearchService for this object. This method is primarily used for testing.
	 *
	 * @param upcSearchService The UpcSearchService for this object
	 */
	public void setUpcSearchService(UpcSearchService upcSearchService) {
		this.upcSearchService = upcSearchService;
	}

	/**
	 * Sets the FindAllService for this object. This method is primarily used for testing.
	 *
	 * @param findAllService The FindAllService for this object
	 */
	public void setFindAllService(FindAllService findAllService) {
		this.findAllService = findAllService;
	}

	/**
	 * Sets the ProductIdSearchService for this object. This method is primarily used for testing.
	 *
	 * @param productIdSearchService The ProductIdSearchService for this object.
	 */
	public void setProductIdSearchService(ProductIdSearchService productIdSearchService) {
		this.productIdSearchService = productIdSearchService;
	}

	/**
	 * Sets the item not delete reasons
	 * @param itemNotDelReasons item not delete reasons
	 */
	public void setItemNotDelReasons(Properties itemNotDelReasons) {
		this.itemNotDelReasons = itemNotDelReasons;
	}

	/**
	 * Fetches a window of all the product discontinue records.
	 *
	 * @param page Which page to fetch.
	 * @param pageSize How big of a page to fetch.
	 * @return An iterable list of product discontinue records.
	 */
	public PageableResult<ProductDiscontinue> findAll(int page, int pageSize) {

		return this.findAllService.findAll(page, pageSize);
	}

	/**
	 * Searches for product discontinue records based on multiple item codes.
	 *
	 * @param itemCodes A list of item codes to search for records on.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByItemCodes(List<Long> itemCodes, StatusFilter statusFilter,
															  boolean includeCount, int page, int pageSize,
															  SortColumn sortColumn, SortDirection direction) {

		Pageable itemCodeRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.itemCodeSearchService.findByItemCodes(itemCodes, itemCodeRequest, statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a list of upcs.
	 *
	 * @param upcs The list of upc to search for records on.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByUpcs(List<Long> upcs, StatusFilter statusFilter,
														 boolean includeCount, int page,
														 int pageSize, SortColumn sortColumn,
														 SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.upcSearchService.findByUpcs(upcs, upcRequest, statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a list of product IDs.
	 *
	 * @param productIds The list of product IDs to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByProductIds(List<Long> productIds, StatusFilter statusFilter,
															   boolean includeCount, int page,
															   int pageSize, SortColumn sortColumn,
															   SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productIdSearchService.findByProductIds(productIds, upcRequest, statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a sub department.
	 *
	 * @param department The department to search for.
	 * @param subDepartment The sub department to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findBySubDepartment(String department, String subDepartment,
																  StatusFilter statusFilter,
																  boolean includeCount, int page,
																  int pageSize, SortColumn sortColumn,
																  SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findBySubDepartment(department, subDepartment, upcRequest,
				statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a class and commodity.
	 *
	 * @param classCode The class to search for.
	 * @param commodityCode The commodity to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByClassAndCommodity(int classCode, int commodityCode,
																	  StatusFilter statusFilter,
																	  boolean includeCount, int page,
																	  int pageSize, SortColumn sortColumn,
																	  SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findByClassAndCommodity(classCode, commodityCode, upcRequest,
				statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a sub commodity.
	 *
	 * @param classCode The class to search for.
	 * @param commodityCode The commodity to search for.
	 * @param subCommodityCode The sub commodity to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findBySubCommodity(int classCode,
																 int commodityCode, int subCommodityCode,
																 StatusFilter statusFilter,
																 boolean includeCount, int page,
																 int pageSize, SortColumn sortColumn,
																 SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findBySubCommodity(classCode, commodityCode,
				subCommodityCode, upcRequest, statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a bdm.
	 *
	 * @param bdmCode The bdm code to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByBdm(String bdmCode, StatusFilter statusFilter,
														boolean includeCount, int page,
														int pageSize, SortColumn sortColumn,
														SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findByBdm(bdmCode, upcRequest, statusFilter, includeCount);
	}

	/**
	 * Returns the Hits count with match, non-match and non-match upcs in the input
	 * @param upcs the UPCs
	 * @return	Hits
	 */
	Hits findHitsByUPCs(List<Long> upcs) { return Hits.calculateHits(upcs, this.upcSearchService.findAllUPCs(upcs)); }

	/**
	 * Returns the Hits count with match, non-match and non-matche item codes in the input
	 * @param itemCodes the UPCs
	 * @return	Hits
	 */
	Hits findHitsByItemCodes(List<Long> itemCodes) {
		return Hits.calculateHits(itemCodes, this.itemCodeSearchService.findAllItemCodes(itemCodes));
	}

	/**
	 * Returns the Hits count with match, non-match and non-match products in the input
	 * @param productIds the products
	 * @return Hits
	 */
	Hits findHitsByProducts(List<Long> productIds) {
		return Hits.calculateHits(productIds, this.productIdSearchService.findAllProducts(productIds));
	}

	/**
	 * Utility function to take a column and direction as defined by the enums in this class and convert it into
	 * a Spring Sort.
	 *
	 * @param sortColumn The column to sort on. If null, the default sort of ProductDiscontinueKey is used.
	 * @param direction The direction to sort. If null, ascending is assumed.
	 * @return A Spring Sort object.
	 */
	private Sort toSort(SortColumn sortColumn, SortDirection direction) {
		Sort sort;
		if (sortColumn == null) {
			sort = ProductDiscontinueKey.getDefaultSort();
		} else {
			Sort.Direction  sortDirection = this.toDirection(direction);
			switch (sortColumn) {
				case UPC:
					sort = ProductDiscontinueKey.getSortByUpc(sortDirection);
					break;
				case ITEM_CODE:
					sort = ProductDiscontinueKey.getSortByItemCode(sortDirection);
					break;
				case DISCONTINUE_ELIGIBLE:
					sort = ProductDiscontinue.getSortByAllColumns(sortDirection);
					break;
				default:
					sort = ProductDiscontinueKey.getDefaultSort();
			}

		}
		return sort;
	}

	/**
	 * Utility function to convert a sort direction as defined in the enum in this class to the one defined in Spring's
	 * Sort class.
	 *
	 * @param direction The direction to sort in. If null, ascending is assumed.
	 * @return The Spring Sort Direction to use.
	 */
	private Sort.Direction toDirection(SortDirection direction) {
		if (direction == null) {
			return Sort.Direction.ASC;
		}
		if (direction.equals(SortDirection.DESC)) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}

	/**
	 * Used to get all the app managed item not deleted reason types that contains app managed short description and
	 * the instruction. The result is returned as map of reason types referenced by their respective code as key.
	 *
	 * @return all managed item not deleted reasons
	 */
	public Map<String, ItemNotDeletedReason> getAllItemNotDeleteReasons() {
		if (ALL_DEL_REASONS.isEmpty()) {
			logger.info("Loading ALL_DEL_REASONS ..  ");
			String keyShortDes = ".short.des";
			String keyInstructionMsg = ".instruction.msg";

			ItemNotDeletedReason itemNotDeletedReason;
			for (String code : itemNotDelReasons.getProperty("all.codes").split(",")) {
				itemNotDeletedReason = new ItemNotDeletedReason();
				itemNotDeletedReason.setCode(code.toUpperCase());
				itemNotDeletedReason.setAbbreviation(itemNotDelReasons.getProperty(code.concat(keyShortDes)));
				itemNotDeletedReason.setDescription(itemNotDelReasons.getProperty(code.concat(keyInstructionMsg)));
				ALL_DEL_REASONS.put(code.toUpperCase(),itemNotDeletedReason);
			}
		}
		return ALL_DEL_REASONS;

	}

	/**
	 * Returns a page of product discontinue data for a vendor.
	 * @param vendorNumber The vendor number to search for.
	 * @param sf
	 *@param includeCount Whether or not to include total record and page counts.
	 * @param pg The page of data to pull.
	 * @param ps The number of records being asked for.
	 * @param sc
	 * @param sd    @return A list of product discontinue data.
	 */
	public PageableResult<ProductDiscontinue> findByVendorNumber(
			int vendorNumber, StatusFilter sf, boolean includeCount, int pg, int ps, SortColumn sc, SortDirection sd) {
		Pageable request = new PageRequest(pg, ps, this.toSort(sc, sd));
		return includeCount ? this.searchByVendorWithCounts(vendorNumber, request, sf) :
				this.searchByVendorWithoutCounts(vendorNumber, request, sf);
	}

	/**
	 * Handles the search when the user does not want counts for a vendor.
	 *
	 * @param vendorNumber The vendor number to search for.
	 * @param request The request with page and page size.
	 * @param sf The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByVendorWithoutCounts(
			int vendorNumber, Pageable request, StatusFilter sf) {
		List<ProductDiscontinue> data;
		switch (sf) {
			case ACTIVE:
				data = this.repository.
						findActiveByVendor(
								vendorNumber, request);
				break;
			case DISCONTINUED:
				data = this.repository.
						findDiscontinuedByVendor(
								vendorNumber, request);
				break;
			default:
				data = this.repository.
						findAllByVendor(
								vendorNumber, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Handles the search when the user does wants counts for a vendor.
	 *
	 * @param vendorNumber The vendor number to search for.
	 * @param request The request with page and page size.
	 * @param sf The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByVendorWithCounts(
			int vendorNumber, Pageable request, StatusFilter sf) {
		Page<ProductDiscontinue> data;
		switch (sf) {
			case ACTIVE:
				data = this.repositoryWithCount.
						findActiveByVendor(
								vendorNumber, request);
				break;
			case DISCONTINUED:
				data = this.repositoryWithCount.
						findDiscontinuedByVendor(
								vendorNumber, request);
				break;
			default:
				data = this.repositoryWithCount.
						findAllByVendor(
								vendorNumber, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Searches for product discontinue records based on a department and class code and commodity and sub commodity.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode The class to search for.
	 * @param commodityCode The commodity to search for.
	 * @param subCommodityCode The sub commodity to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByDepartmentAndClassAndCommodityAndSubCommodity(String department, String subDepartment,
																								  int classCode,
																								  int commodityCode, int subCommodityCode,
																								  StatusFilter statusFilter,
																								  boolean includeCount, int page,
																								  int pageSize, SortColumn sortColumn,
																								  SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findByDepartmentAndClassAndCommodityAndSubCommodity(department, subDepartment,
				classCode, commodityCode, subCommodityCode, upcRequest, statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a commodity and sub commodity.
	 *
	 * @param commodityCode The commodity to search for.
	 * @param subCommodityCode The sub commodity to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByCommodityAndSubCommodity(int commodityCode, int subCommodityCode,
																			 StatusFilter statusFilter,
																			 boolean includeCount, int page,
																			 int pageSize, SortColumn sortColumn,
																			 SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findByCommodityAndSubCommodity(commodityCode, subCommodityCode,
				upcRequest, statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on department and class code and commodity.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode The class to search for.
	 * @param commodityCode The commodity to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByDepartmentAndClassAndCommodity(String department, String subDepartment,
																				   int classCode, int commodityCode,
																				   StatusFilter statusFilter,
																				   boolean includeCount, int page,
																				   int pageSize, SortColumn sortColumn,
																				   SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findByDepartmentAndClassAndCommodity(department, subDepartment,
				classCode, commodityCode, upcRequest, statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a department and commodity and sub commodity.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param commodityCode The commodity to search for.
	 * @param subCommodityCode The sub commodity to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByDepartmentAndCommodityAndSubCommodity(String department, String subDepartment,
																						  int commodityCode, int subCommodityCode,
																						  StatusFilter statusFilter,
																						  boolean includeCount, int page,
																						  int pageSize, SortColumn sortColumn,
																						  SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findByDepartmentAndCommodityAndSubCommodity(department, subDepartment,
				commodityCode, subCommodityCode, upcRequest, statusFilter, includeCount);
	}

	/**
	 * Searches for product discontinue records based on a department and class code.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode The class to search for.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @param page The page you are looking for.
	 * @param pageSize The maximum number of records to return.
	 * @param sortColumn The column to sort the  result set on.
	 * @param direction The direction the sort should be in.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByDepartmentAndClass(String department, String subDepartment,
																	   int classCode,
																	   StatusFilter statusFilter,
																	   boolean includeCount, int page,
																	   int pageSize, SortColumn sortColumn,
																	   SortDirection direction) {

		Pageable upcRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, direction));

		return this.productHierarchySearchService.findByDepartmentAndClass(department, subDepartment, classCode, upcRequest, statusFilter, includeCount);
	}
}