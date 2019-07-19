/*
 * com.heb.pm.repository.ProductDiscontinueRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.entity.ProductDiscontinueKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for data from the prod_del table.
 *
 * @author d116773
 * @since 2.0.0
 */
public interface ProductDiscontinueRepository extends
		JpaRepository<ProductDiscontinue, ProductDiscontinueKey>, ProductDiscontinueRepositoryDelegate {

	/**
	 * Search prod_del by multiple item codes looking only for discontinued items.
	 *
	 * @param itemCodes The item codes to search by.
	 * @param itemType the itemType.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findDistinctByKeyItemCodeInAndKeyItemType(List<Long> itemCodes, String itemType,
																	   Pageable pageRequest);

	/**
	 * Search prod_del by a list of upcs.
	 *
	 * @param upcs The list of upcs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findDistinctByKeyUpcIn(@Param("upcs") List<Long> upcs, Pageable pageRequest);

	/**
	 * Search prod_del by a list of product IDs.
	 *
	 * @param productIds The list of prodcut IDs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findDistinctByKeyProductIdIn(@Param("productIds") List<Long> productIds, Pageable pageRequest);

	/**
	 * Search prod_del by a class code.
	 *
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findDistinctByProductMasterClassCode(@Param("classCode") int classCode, Pageable pageRequest);

	/**
	 * Search prod_del by a class code and commodity code.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findDistinctByProductMasterClassCodeAndProductMasterCommodityCode(
			@Param("classCode") int classCode,
			@Param("commodityCode") int commodityCode,
			Pageable pageRequest);

	/**
	 * Search prod_del by a sub commodity.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findDistinctByProductMasterClassCodeAndProductMasterCommodityCodeAndProductMasterSubCommodityCode(
			@Param("classCode") int classCode,
			@Param("commodityCode") int commodityCode,
			@Param("subCommodityCode") int subCommodityCode,
			Pageable pageRequest);

	/**
	 * Search prod_del by a bdm.
	 *
	 * @param bdmCode A bdm code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findDistinctByProductMasterClassCommodityTrimmedBdmCode(String bdmCode, Pageable pageRequest);
	/**
	 * Search prod_del by multiple item codes.
	 *
	 * @param itemCodes The item codes to search by.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findAllDistinctByKeyItemCodeIn(@Param("itemCodes")List<Long> itemCodes);

	/**
	 * Search prod_del by a list of upcs.
	 *
	 * @param upcs The list of upcs to search by.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findAllDistinctByKeyUpcIn(@Param("upcs") List<Long> upcs);
	/**
	 * Search prod_del by a list of product IDs.
	 *
	 * @param productIds The list of prodcut IDs to search by.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findAllDistinctByKeyProductIdIn(@Param("productIds") List<Long> productIds);

	/**
	 * Search prod_del by multiple item codes looking only for discontinued items.
	 *
	 * @param itemCodes The item codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_ITEM_CODE_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByItemCodes(@Param("itemCodes") List<Long> itemCodes,
														 Pageable pageRequest);

	/**
	 * Search prod_del by multiple item codes looking only for active items.
	 *
	 * @param itemCodes The item codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_ITEM_CODE_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByItemCodes(@Param("itemCodes") List<Long> itemCodes,
												   Pageable pageRequest);

	/**
	 * Search prod_del by a list of upcs looking only for discontinued products.
	 *
	 * @param upcs The list of upcs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_UPC_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByUpcs(@Param("upcs") List<Long> upcs, Pageable pageRequest);

	/**
	 * Search prod_del by a list of upcs looking only for active products.
	 *
	 * @param upcs The list of upcs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_UPC_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByUpcs(@Param("upcs") List<Long> upcs, Pageable pageRequest);

	/**
	 * Search prod_del by a list of product IDs looking only for discontinued products.
	 *
	 * @param productIds The list of prodcut IDs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_PRODUCT_ID_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByProductIds(@Param("productIds") List<Long> productIds, Pageable pageRequest);

	/**
	 * Search prod_del by a list of product IDs looking only for active products.
	 *
	 * @param productIds The list of prodcut IDs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_PRODUCT_ID_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByProductIds(@Param("productIds") List<Long> productIds, Pageable pageRequest);

	/**
	 * Search prod_del by a class code looking only for discontinued products.
	 *
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_CLASS_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByClass(@Param("classCode") int classCode, Pageable pageRequest);

	/**
	 * Search prod_del by a class code looking only for active products.
	 *
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_CLASS_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByClass(@Param("classCode") int classCode, Pageable pageRequest);

	/**
	 * Search prod_del by a class code and commodity code looking only for discontinued products.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_CLASS_AND_COMMODITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByClassAndCommodity(@Param("classCode") int classCode,
																 @Param("commodityCode") int commodityCode,
																 Pageable pageRequest);

	/**
	 * Search prod_del by a class code and commodity code looking only for active products.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_CLASS_AND_COMMODITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByClassAndCommodity(@Param("classCode") int classCode,
														   @Param("commodityCode") int commodityCode,
														   Pageable pageRequest);

	/**
	 * Search prod_del by a sub commodity looking only for discontinued products.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_SUB_COMMODITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedBySubCommodity(@Param("classCode") int classCode,
															@Param("commodityCode") int commodityCode,
															@Param("subCommodityCode") int subCommodityCode,
															Pageable pageRequest);

	/**
	 * Search prod_del by a sub commodity looking only for active products.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_SUB_COMMODITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveBySubCommodity(@Param("classCode") int classCode,
													  @Param("commodityCode") int commodityCode,
													  @Param("subCommodityCode") int subCommodityCode,
													  Pageable pageRequest);

	/**
	 * Search prod_del by a bdm looking only for discontinued products.
	 *
	 * @param bdmCode A bdm code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_BDM_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByBdm(@Param("bdmCode") String bdmCode, Pageable pageRequest);

	/**
	 * Search prod_del by a bdm looking only for active products.
	 *
	 * @param bdmCode A bdm code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_BDM_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByBdm(@Param("bdmCode") String bdmCode, Pageable pageRequest);

	/**
	 * Find list by Location vendor number.
	 *
	 * @param vendorNumber the ap vendor number
	 * @param request the request
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_NON_COUNT_DISTINCT_SEARCH + VENDOR_SEARCH)
	List<ProductDiscontinue> findAllByVendor(@Param("vendorNumber")int vendorNumber , Pageable request);

	/**
	 * Find list by Location vendor number for active products.
	 *
	 * @param vendorNumber the ap vendor number
	 * @param request the request
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_NON_COUNT_DISTINCT_SEARCH + VENDOR_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByVendor(@Param("vendorNumber")int vendorNumber , Pageable request);

	/**
	 * Find list by Location vendor number for discontinued products.
	 *
	 * @param vendorNumber the ap vendor number
	 * @param request the request
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_NON_COUNT_DISTINCT_SEARCH + VENDOR_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByVendor(@Param("vendorNumber")int vendorNumber , Pageable request);

	/**
	 * Search prod_del by a department and class and commodity and sub commodity looking only for active products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_COMMODITY_SUBCOMMONDITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByDepartmentAndClassAndCommodityAndSubCommodity(@Param("department") String department,
																					   @Param("subDepartment") String subDepartment,
																					   @Param("classCode") int classCode,
																					   @Param("commodityCode") int commodityCode,
																					   @Param("subCommodityCode") int subCommodityCode,
																					   Pageable pageRequest);

	/**
	 * Search prod_del by a department and class and commodity and sub commodity looking only for discontinued products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_COMMODITY_SUBCOMMONDITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByDepartmentAndClassAndCommodityAndSubCommodity(@Param("department") String department,
																							 @Param("subDepartment") String subDepartment,
																							 @Param("classCode") int classCode,
																							 @Param("commodityCode") int commodityCode,
																							 @Param("subCommodityCode") int subCommodityCode,
																							 Pageable pageRequest);

	/**
	 * Search prod_del by a department and class and commodity and sub commodity looking all products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_COMMODITY_SUBCOMMONDITY_SEARCH)
	List<ProductDiscontinue> findByDepartmentAndClassAndCommodityAndSubCommodity(@Param("department") String department,
																				 @Param("subDepartment") String subDepartment,
																				 @Param("classCode") int classCode,
																				 @Param("commodityCode") int commodityCode,
																				 @Param("subCommodityCode") int subCommodityCode,
																				 Pageable pageRequest);

	/**
	 * Search prod_del by a commodity and sub commodity looking only for active products.
	 *
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_COMMODITY_AND_SUB_COMMODITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByCommodityAndSubCommodity(@Param("commodityCode") int commodityCode,
																  @Param("subCommodityCode") int subCommodityCode,
																  Pageable pageRequest);

	/**
	 * Search prod_del by a commodity and sub commodity looking only for discontinued products.
	 *
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_COMMODITY_AND_SUB_COMMODITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByCommodityAndSubCommodity(@Param("commodityCode") int commodityCode,
																		@Param("subCommodityCode") int subCommodityCode,
																		Pageable pageRequest);
	/**
	 * Search prod_del by a commodity and sub commodity.
	 *
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod_del.
	 */
	List<ProductDiscontinue> findDistinctByProductMasterCommodityCodeAndProductMasterSubCommodityCode(@Param("commodityCode") int commodityCode,
																									  @Param("subCommodityCode") int subCommodityCode,
																									  Pageable pageRequest);

	/**
	 * Search prod_del by a department and class and commodity looking only for active products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_COMMODITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByDepartmentAndClassAndCommodity(@Param("department") String department,
																		@Param("subDepartment") String subDepartment,
																		@Param("classCode") int classCode,
																		@Param("commodityCode") int commodityCode,
																		Pageable pageRequest);

	/**
	 * Search prod_del by a department and class and commodity looking only for discontinued products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_COMMODITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByDepartmentAndClassAndCommodity(@Param("department") String department,
																		@Param("subDepartment") String subDepartment,
																		@Param("classCode") int classCode,
																		@Param("commodityCode") int commodityCode,
																		Pageable pageRequest);

	/**
	 * Search prod_del by a department and class and commodity and sub commodity looking all products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_COMMODITY_SEARCH)
	List<ProductDiscontinue> findByDepartmentAndClassAndCommodity(@Param("department") String department,
																  @Param("subDepartment") String subDepartment,
																  @Param("classCode") int classCode,
																  @Param("commodityCode") int commodityCode,
																  Pageable pageRequest);
	/**
	 * Search prod_del by a department and commodity and sub commodity looking only for active products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_COMMODITY_SUBCOMMONDITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByDepartmentAndCommodityAndSubCommodity(@Param("department") String department,
																			   @Param("subDepartment") String subDepartment,
																			   @Param("commodityCode") int commodityCode,
																			   @Param("subCommodityCode") int subCommodityCode,
																			   Pageable pageRequest);
	/**
	 * Search prod_del by a department and commodity and sub commodity looking only for discontinued products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_COMMODITY_SUBCOMMONDITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByDepartmentAndCommodityAndSubCommodity(@Param("department") String department,
																					 @Param("subDepartment") String subDepartment,
																					 @Param("commodityCode") int commodityCode,
																					 @Param("subCommodityCode") int subCommodityCode,
																					 Pageable pageRequest);
	/**
	 * Search prod_del by a department and commodity and sub commodity looking all products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_COMMODITY_SUBCOMMONDITY_SEARCH)
	List<ProductDiscontinue> findByDepartmentAndCommodityAndSubCommodity(@Param("department") String department,
																		 @Param("subDepartment") String subDepartment,
																		 @Param("commodityCode") int commodityCode,
																		 @Param("subCommodityCode") int subCommodityCode,
																		 Pageable pageRequest);

	/**
	 * Search prod_del by a department and class looking only for active products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_SEARCH + ACTIVE_ITEMS_PREDICATE)
	List<ProductDiscontinue> findActiveByDepartmentAndClass(@Param("department") String department,
															@Param("subDepartment") String subDepartment,
															@Param("classCode") int classCode,
															Pageable pageRequest);

	/**
	 * Search prod_del by a department and class looking only for discontinued products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	List<ProductDiscontinue> findDiscontinuedByDepartmentAndClass(@Param("department") String department,
																  @Param("subDepartment") String subDepartment,
																  @Param("classCode") int classCode,
																  Pageable pageRequest);

	/**
	 * Search prod_del by a department and class looking all products.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_DEPARTMENT_CLASS_SEARCH)
	List<ProductDiscontinue> findByDepartmentAndClass(@Param("department") String department,
													  @Param("subDepartment") String subDepartment,
													  @Param("classCode") int classCode,
													  Pageable pageRequest);
}