/*
 * com.heb.pm.repository.ProductDiscontinueRepository
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.entity.ProductDiscontinueKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for data from the prod_del table with count.
 *
 * @author d116773
 * @since 2.0.0
 */
public interface ProductDiscontinueRepositoryWithCount extends JpaRepository<ProductDiscontinue,
		ProductDiscontinueKey>, ProductDiscontinueRepositoryDelegateWithCount {

	/**
	 * Search prod_del by multiple item codes.
	 *
	 * @param itemCodes The item codes to search by.
	 * @param itemType the itemType.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ProductDiscontinue> findDistinctByKeyItemCodeInAndKeyItemType(List<Long> itemCodes, String itemType,
														 Pageable pageRequest);

	/**
	 * Search prod_del by multiple item codes looking only for discontinued items.
	 *
	 * @param itemCodes The item codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_ITEM_CODE_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedByItemCodesWithCount(@Param("itemCodes") List<Long> itemCodes,
																  Pageable pageRequest);

	/**
	 * Search prod_del by multiple item codes looking only for active items.
	 *
	 * @param itemCodes The item codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_ITEM_CODE_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveByItemCodesWithCount(@Param("itemCodes") List<Long> itemCodes,
															Pageable pageRequest);

	/**
	 * Search prod_del by a list of upcs.
	 *
	 * @param upcs The list of upcs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ProductDiscontinue> findDistinctByKeyUpcIn(@Param("upcs") List<Long> upcs, Pageable pageRequest);

	/**
	 * Search prod_del by a list of upcs looking only for discontinued products.
	 *
	 * @param upcs The list of upcs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_UPC_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedByUpcsWithCount(@Param("upcs") List<Long> upcs, Pageable pageRequest);

	/**
	 * Search prod_del by a list of upcs looking only for active products.
	 *
	 * @param upcs The list of upcs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_UPC_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveByUpcsWithCount(@Param("upcs") List<Long> upcs, Pageable pageRequest);

	/**
	 * Search prod_del by a list of product IDs.
	 *
	 * @param productIds The list of prodcut IDs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ProductDiscontinue> findDistinctByKeyProductIdIn(
			@Param("productIds") List<Long> productIds, Pageable pageRequest);


	/**
	 * Search prod_del by a list of product IDs looking only for discontinued products.
	 *
	 * @param productIds The list of prodcut IDs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_PRODUCT_ID_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedByProductIdsWithCount(@Param("productIds") List<Long> productIds, Pageable pageRequest);

	/**
	 * Search prod_del by a list of product IDs looking only for active products.
	 *
	 * @param productIds The list of prodcut IDs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_PRODUCT_ID_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveByProductIdsWithCount(@Param("productIds") List<Long> productIds, Pageable pageRequest);

	/**
	 * Search prod_del by a class code.
	 *
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ProductDiscontinue> findDistinctByProductMasterClassCode(
			@Param("classCode") int classCode, Pageable pageRequest);

	/**
	 * Search prod_del by a class code looking only for discontinued products.
	 *
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_CLASS_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedByClassWithCount(@Param("classCode") int classCode, Pageable pageRequest);

	/**
	 * Search prod_del by a class code looking only for active products.
	 *
	 * @param classCode The class code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_CLASS_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveByClassWithCount(@Param("classCode") int classCode, Pageable pageRequest);

	/**
	 * Search prod_del by a class code and commodity code.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ProductDiscontinue> findDistinctByProductMasterClassCodeAndProductMasterCommodityCode(
			@Param("classCode") int classCode,
			@Param("commodityCode") int commodityCode,
			Pageable pageRequest);

	/**
	 * Search prod_del by a class code and commodity code looking only for discontinued products.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_CLASS_AND_COMMODITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedByClassAndCommodityWithCount(@Param("classCode") int classCode,
																		  @Param("commodityCode") int commodityCode,
																		  Pageable pageRequest);

	/**
	 * Search prod_del by a class code and commodity code looking only for active products.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_CLASS_AND_COMMODITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveByClassAndCommodityWithCount(@Param("classCode") int classCode,
																	@Param("commodityCode") int commodityCode,
																	Pageable pageRequest);

	/**
	 * Search prod_del by a sub commodity.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ProductDiscontinue>
	findDistinctByProductMasterClassCodeAndProductMasterCommodityCodeAndProductMasterSubCommodityCode(
			@Param("classCode") int classCode,
			@Param("commodityCode") int commodityCode,
			@Param("subCommodityCode") int subCommodityCode,
			Pageable pageRequest);

	/**
	 * Search prod_del by a sub commodity looking only for discontinued products.
	 *
	 * @param classCode The class code to search by.
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_SUB_COMMODITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedBySubCommodityWithCount(@Param("classCode") int classCode,
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
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_SUB_COMMODITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveBySubCommodityWithCount(@Param("classCode") int classCode,
															   @Param("commodityCode") int commodityCode,
															   @Param("subCommodityCode") int subCommodityCode,
															   Pageable pageRequest);

	/**
	 * Search prod_del by a bdm.
	 *
	 * @param bdmCode A bdm code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ProductDiscontinue> findDistinctByProductMasterClassCommodityTrimmedBdmCode(String bdmCode, Pageable pageRequest);

	/**
	 * Search prod_del by a bdm looking only for discontinued products.
	 *
	 * @param bdmCode A bdm code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_BDM_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedByBdmWithCount(@Param("bdmCode") String bdmCode, Pageable pageRequest);

	/**
	 * Search prod_del by a bdm looking only for active products.
	 *
	 * @param bdmCode A bdm code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_BDM_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveByBdmWithCount(@Param("bdmCode") String bdmCode, Pageable pageRequest);

	/**
	 * Find page by Location vendor number.
	 *
	 * @param vendorNumber the ap vendor number
	 * @param request the request
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_NON_COUNT_DISTINCT_SEARCH + VENDOR_SEARCH, countQuery = BASE_COUNT_SEARCH + VENDOR_SEARCH)
	Page<ProductDiscontinue> findAllByVendor(@Param("vendorNumber")int vendorNumber , Pageable request);

	/**
	 * Find page by Location vendor number for active products.
	 *
	 * @param vendorNumber the ap vendor number
	 * @param request the request
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_NON_COUNT_DISTINCT_SEARCH + VENDOR_SEARCH + ACTIVE_ITEMS_PREDICATE,
			countQuery = BASE_COUNT_SEARCH + VENDOR_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveByVendor(@Param("vendorNumber")int vendorNumber , Pageable request);

	/**
	 * Find page by Location vendor number for discontinued products.
	 *
	 * @param vendorNumber the ap vendor number
	 * @param request the request
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_NON_COUNT_DISTINCT_SEARCH + VENDOR_SEARCH + DISCONTINUED_ITEMS_PREDICATE,
			countQuery = BASE_COUNT_SEARCH + VENDOR_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedByVendor(@Param("vendorNumber")int vendorNumber , Pageable request);

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
	Page<ProductDiscontinue> findActiveByDepartmentAndClassAndCommodityAndSubCommodityWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findDiscontinuedByDepartmentAndClassAndCommodityAndSubCommodityWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findByDepartmentAndClassAndCommodityAndSubCommodityWithCount(@Param("department") String department,
																						   @Param("subDepartment") String subDepartment,
																						   @Param("classCode") int classCode,
																						   @Param("commodityCode") int commodityCode,
																						   @Param("subCommodityCode") int subCommodityCode,
																						   Pageable pageRequest);

	/**
	 * Search prod_del by a commidity and sub commodity looking only for active products.
	 *
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_COMMODITY_AND_SUB_COMMODITY_SEARCH + ACTIVE_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findActiveByCommodityAndSubCommodityWithCount(@Param("commodityCode") int commodityCode,
																		   @Param("subCommodityCode") int subCommodityCode,
																		   Pageable pageRequest);

	/**
	 * Search prod_del by a commidity and sub commodity looking only for discontinued products.
	 *
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(value = BASE_COMMODITY_AND_SUB_COMMODITY_SEARCH + DISCONTINUED_ITEMS_PREDICATE)
	Page<ProductDiscontinue> findDiscontinuedByCommodityAndSubCommodityWithCount(@Param("commodityCode") int commodityCode,
																				 @Param("subCommodityCode") int subCommodityCode,
																				 Pageable pageRequest);

	/**
	 * Search prod_del by a commodity and sub commodity.
	 *
	 * @param commodityCode A commodity code to search by.
	 * @param subCommodityCode A sub commodity code to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ProductDiscontinue> findDistinctByProductMasterCommodityCodeAndProductMasterSubCommodityCode(@Param("commodityCode") int commodityCode,
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
	Page<ProductDiscontinue> findActiveByDepartmentAndClassAndCommodityWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findDiscontinuedByDepartmentAndClassAndCommodityWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findByDepartmentAndClassAndCommodityWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findActiveByDepartmentAndCommodityAndSubCommodityWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findDiscontinuedByDepartmentAndCommodityAndSubCommodityWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findByDepartmentAndCommodityAndSubCommodityWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findActiveByDepartmentAndClassWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findDiscontinuedByDepartmentAndClassWithCount(@Param("department") String department,
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
	Page<ProductDiscontinue> findByDepartmentAndClassWithCount(@Param("department") String department,
															   @Param("subDepartment") String subDepartment,
															   @Param("classCode") int classCode,
															   Pageable pageRequest);
}