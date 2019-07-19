/*
 * ProductDiscontinueRepositoryDelegateCommon
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

/**
 * Common constants for the count and non-count prod_del JPA repository implementations.
 *
 * @author m314029
 * @since 2.0.8
 */
public interface ProductDiscontinueRepositoryDelegateCommon {

	/**
	 * The base query to use when searching without count.
	 */
	String BASE_NON_COUNT_SEARCH = "select pd ";

	/**
	 * The base query to use when searching without count.
	 */
	String BASE_NON_COUNT_DISTINCT_SEARCH = "select distinct pd ";

	/**
	 * The base query to use when searching with count.
	 */
	String BASE_COUNT_SEARCH = "select count(pd.key.upc) ";

	/**
	 * The base query to use when searching by class.
	 */
	//DB2Oracle Changes vn00907  added trim
	String BASE_DEPARTMENT_SEARCH = "from ProductDiscontinue pd join pd.productMaster pm " +
			"where trim(pm.departmentCode) = '%s' ";

	/**
	 * The base query to use when searching by class.
	 */
	//DB2Oracle Changes vn00907  added trim
	String BASE_DEPARTMENT_AND_SUB_DEPARTMENT_SEARCH = "from ProductDiscontinue pd join pd.productMaster pm " +
			"where trim(pm.departmentCode) = '%s' and trim(pm.subDepartmentCode) = '%s' ";

	/**
	 * The base query to use when searching by item codes.
	 */
	String BASE_ITEM_CODE_SEARCH =  "select pd from ProductDiscontinue pd where pd.key.itemCode in :itemCodes and pd.key.itemType = 'ITMCD' ";

	/**
	 * The base query to use when searching by UPCs.
	 */
	String BASE_UPC_SEARCH = "select pd from ProductDiscontinue pd where pd.key.upc in :upcs ";

	/**
	 * The base query to use when searching by product ID.
	 */
	String BASE_PRODUCT_ID_SEARCH = "select pd from ProductDiscontinue pd where pd.key.productId in :productIds ";

	/**
	 * The base query to use when searching by class.
	 */
	String BASE_CLASS_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm " +
			"where pm.classCode = :classCode ";

	/**
	 * The base query to use when searching by class.
	 */
	String BASE_CLASS_AND_COMMODITY_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm " +
			"where pm.classCode = :classCode and pm.commodityCode = :commodityCode ";

	/**
	 * The base query to use when searching by class.
	 */
	String BASE_SUB_COMMODITY_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm " +
			"where pm.classCode = :classCode and pm.commodityCode = :commodityCode " +
			"and pm.subCommodityCode = :subCommodityCode ";

	/**
	 * The base query to use when searching by class.
	 */
	//DB2Oracle Changes vn00907 ,added trim
	String BASE_BDM_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm " +
			"join pm.classCommodity cc where trim(cc.bdmCode) = :bdmCode ";

	/**
	 * The base query to use when searching by department, class, commodity and sub commodity.
	 */
	String BASE_DEPARTMENT_CLASS_COMMODITY_SUBCOMMONDITY_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm "+
			"where trim(pm.departmentCode) = :department and trim(pm.subDepartmentCode) = :subDepartment and pm.classCode = :classCode and pm.commodityCode = :commodityCode and pm.subCommodityCode = :subCommodityCode ";

	/**
	 * The base query to use when searching by department, class, commodity.
	 */
	String BASE_DEPARTMENT_CLASS_COMMODITY_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm "+
			"where trim(pm.departmentCode) = :department and trim(pm.subDepartmentCode) = :subDepartment and pm.classCode = :classCode and pm.commodityCode = :commodityCode ";

	/**
	 * The base query to use when searching by commodity and sub commodity.
	 */
	String BASE_COMMODITY_AND_SUB_COMMODITY_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm " +
			"where pm.commodityCode = :commodityCode and pm.subCommodityCode = :subCommodityCode ";

	/**
	 * The base query to use when searching by department, class, commodity and sub commodity.
	 */
	String BASE_DEPARTMENT_COMMODITY_SUBCOMMONDITY_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm "+
			"where trim(pm.departmentCode) = :department and trim(pm.subDepartmentCode) = :subDepartment and pm.commodityCode = :commodityCode and pm.subCommodityCode = :subCommodityCode ";

	/**
	 * The base query to use when searching by department, class.
	 */
	String BASE_DEPARTMENT_CLASS_SEARCH = "select pd from ProductDiscontinue pd join pd.productMaster pm "+
			"where trim(pm.departmentCode) = :department and trim(pm.subDepartmentCode) = :subDepartment and pm.classCode = :classCode ";

	/**
	 * Add this as a predicate when looking for active items.
	 */
	//DB2Oracle Changes vn00907 ,added trim
	String ACTIVE_ITEMS_PREDICATE = "and ( " +
			"	( pd.key.itemType =  'DSD  ' and " +
			"		exists ( " +
			"			select 1 from pd.itemMaster im " +
			"			where im.discontinueDate = to_date('1600-01-01','yyyy-mm-dd') " +
			"		)) " +
			"	or ( pd.key.itemType = 'ITMCD' and " +
			"		exists ( " +
			"			select 1 from pd.itemMaster.warehouseLocationItems im " +
			"			where trim(im.purchasingStatus) <> 'D    ' " +
			"		)) " +
			"	) ";


	/**
	 * Add this as a predicate when looking for discontinued items.
	 */
	//DB2Oracle Changes vn00907 ,added trim
	String DISCONTINUED_ITEMS_PREDICATE = "and ( " +
			"	( pd.key.itemType =  'DSD  ' and " +
			"		exists ( " +
			"			select 1 from pd.itemMaster im " +
			"			where im.discontinueDate <> to_date('1600-01-01','yyyy-mm-dd') " +
			"		)) " +
			"	or ( pd.key.itemType = 'ITMCD' and " +
			"		not exists ( " +
			"			select 1 from pd.itemMaster.warehouseLocationItems im " +
			"			where trim(im.purchasingStatus) <> 'D    ' " +
			"		)) " +
			"	) ";
	//DB2Oracle Changes vn00907 ,added trim
	String VENDOR_SEARCH = " from ProductDiscontinue pd join " +
			"pd.itemMaster im join " +
			"pd.productMaster pm join " +
			"im.vendorLocationItems ven join " +
			"ven.location loc where loc.apVendorNumber=:vendorNumber and " +
			"(trim(loc.apTypeCode) = 'AP' or trim(loc.apTypeCode) = 'DS') ";
}
