/*
 * ProductHierarchySearchService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.repository.ProductDiscontinueRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class can be delegated to for all searches based on product hierarchy.
 *
 * @author m314029
 * @since 2.0.6
 */
@Service
public class ProductHierarchySearchService {

	@Autowired
	private ProductDiscontinueRepositoryWithCount productDiscontinueRepositoryWithCount;

	@Autowired
	private com.heb.pm.repository.ProductDiscontinueRepository productDiscontinueRepository;

	/**
	 * Searches for product discontinue records based on sub department.
	 *
	 * @param department A department to search for records on.
	 * @param subDepartment A sub department to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findBySubDepartment(String department, String subDepartment,
																  Pageable request, StatusFilter statusFilter,
																  boolean includeCount) {
		if (StringUtils.isBlank(department)) {
			throw new IllegalArgumentException("Department cannot be null or empty.");
		}
		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		if(StringUtils.isBlank(subDepartment)){
			return includeCount ? this.searchByDepartmentWithCounts(department, request, statusFilter) :
					this.searchByDepartmentWithoutCounts(department, request, statusFilter);
		} else {
			return includeCount ?
					this.searchByDepartmentAndSubDepartmentWithCounts(department, subDepartment, request, statusFilter) :
					this.searchByDepartmentAndSubDepartmentWithoutCounts(department, subDepartment, request, statusFilter);
		}
	}

	/**
	 * Searches for product discontinue records based on class and commodity.
	 *
	 * @param classCode A class to search for records on.
	 * @param commodityCode A commodity to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByClassAndCommodity(int classCode, int commodityCode, Pageable request,
																	  StatusFilter statusFilter, boolean includeCount) {

		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		if(commodityCode == 0) {
			return includeCount ? this.searchByClassWithCounts(classCode, request, statusFilter) :
					this.searchByClassWithoutCounts(classCode, request, statusFilter);
		} else {
			return includeCount ?
					this.searchByClassAndCommodityWithCounts(classCode, commodityCode, request, statusFilter) :
					this.searchByClassAndCommodityWithoutCounts(classCode, commodityCode, request, statusFilter);
		}
	}

	/**
	 * Searches for product discontinue records based on sub commodity.
	 *
	 * @param classCode A class to search for records on.
	 * @param commodityCode A commodity to search for records on.
	 * @param subCommodityCode A sub commodity to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findBySubCommodity(int classCode,
																 int commodityCode, int subCommodityCode,
																 Pageable request, StatusFilter statusFilter,
																 boolean includeCount) {

		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		return includeCount ? this.searchBySubCommodityWithCounts(
				classCode, commodityCode, subCommodityCode, request, statusFilter) :
				this.searchBySubCommodityWithoutCounts(
						classCode, commodityCode, subCommodityCode,request, statusFilter);
	}

	/**
	 * Searches for product discontinue records based on bdm.
	 *
	 * @param bdmCode A bdm code to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByBdm(String bdmCode,
														Pageable request, StatusFilter statusFilter,
														boolean includeCount) {

		if (StringUtils.isBlank(bdmCode)) {
			throw new IllegalArgumentException("Bdm cannot be null or empty.");
		}
		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		return includeCount ? this.searchByBdmWithCounts(bdmCode, request, statusFilter) :
				this.searchByBdmWithoutCounts(bdmCode, request, statusFilter);
	}

	/**
	 * Handles the bdm search when the user wants counts.
	 *
	 * @param bdmCode A bdm code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByBdmWithCounts(String bdmCode, Pageable request,
																	 StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByBdmWithCount(bdmCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByBdmWithCount(bdmCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.findDistinctByProductMasterClassCommodityTrimmedBdmCode(bdmCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the bdm search when the user does not want counts.
	 *
	 * @param bdmCode A bdm code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByBdmWithoutCounts(String bdmCode, Pageable request,
																		StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByBdm(bdmCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByBdm(bdmCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.findDistinctByProductMasterClassCommodityTrimmedBdmCode(bdmCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Handles the sub commodity search when the user wants counts.
	 *
	 * @param classCode A class code to search for records on.
	 * @param commodityCode A commodity code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchBySubCommodityWithCounts(int classCode, int commodityCode,
																			  int subCommodityCode, Pageable request,
																			  StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveBySubCommodityWithCount(classCode, commodityCode,
						subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedBySubCommodityWithCount(classCode, commodityCode,
						subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.
						findDistinctByProductMasterClassCodeAndProductMasterCommodityCodeAndProductMasterSubCommodityCode(
								classCode, commodityCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the sub commodity search when the user does not want counts.
	 *
	 * @param classCode A class code to search for records on.
	 * @param commodityCode A commodity code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchBySubCommodityWithoutCounts(int classCode, int commodityCode,
																				 int subCommodityCode, Pageable request,
																				 StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveBySubCommodity(classCode, commodityCode,
						subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedBySubCommodity(classCode, commodityCode,
						subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.
						findDistinctByProductMasterClassCodeAndProductMasterCommodityCodeAndProductMasterSubCommodityCode(
								classCode, commodityCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Handles the class search when the user wants counts.
	 *
	 * @param classCode A class code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByClassWithCounts(int classCode, Pageable request,
																	   StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByClassWithCount(classCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByClassWithCount(classCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.findDistinctByProductMasterClassCode(classCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the class search when the user does not want counts.
	 *
	 * @param classCode A class code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByClassWithoutCounts(int classCode, Pageable request,
																		  StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByClass(classCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByClass(classCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.findDistinctByProductMasterClassCode(classCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Handles the class and commodity search when the user wants counts.
	 *
	 * @param classCode A class code to search for records on.
	 * @param commodityCode A commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByClassAndCommodityWithCounts(int classCode, int commodityCode,
																				   Pageable request,
																				   StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.
						findActiveByClassAndCommodityWithCount(classCode, commodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.
						findDiscontinuedByClassAndCommodityWithCount(classCode, commodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.
						findDistinctByProductMasterClassCodeAndProductMasterCommodityCode(classCode, commodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the class and commodity search when the user does not want counts.
	 *
	 * @param classCode A class code to search for records on.
	 * @param commodityCode A commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByClassAndCommodityWithoutCounts(int classCode, int commodityCode,
																					  Pageable request,
																					  StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.
						findActiveByClassAndCommodity(classCode, commodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.
						findDiscontinuedByClassAndCommodity(classCode, commodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.
						findDistinctByProductMasterClassCodeAndProductMasterCommodityCode(
								classCode, commodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Handles the department search when the user wants counts.
	 *
	 * @param department A department to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentWithCounts(String department, Pageable request,
																			StatusFilter statusFilter) {
		Page<ProductDiscontinue> data =
				this.productDiscontinueRepositoryWithCount.findByDepartment(department, statusFilter, request);
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the department search when the user does not want counts.
	 *
	 * @param department A department to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentWithoutCounts(String department, Pageable request,
																			   StatusFilter statusFilter) {
		List<ProductDiscontinue> data =
				this.productDiscontinueRepository.findByDepartment(department, statusFilter, request);
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Handles the department and subDepartment search when the user wants counts.
	 *
	 * @param department A department to search for records on.
	 * @param subDepartment A subDepartment to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndSubDepartmentWithCounts(String department,
																							String subDepartment,
																							Pageable request,
																							StatusFilter statusFilter) {
		Page<ProductDiscontinue> data = this.productDiscontinueRepositoryWithCount.
				findByDepartmentAndSubDepartment(department, subDepartment, statusFilter, request);
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the department and subDepartment search when the user does not want counts.
	 *
	 * @param department A department to search for records on.
	 * @param subDepartment A subDepartment to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndSubDepartmentWithoutCounts(String department,
																							   String subDepartment,
																							   Pageable request,
																							   StatusFilter statusFilter) {
		List<ProductDiscontinue> data = this.productDiscontinueRepository.
				findByDepartmentAndSubDepartment(department, subDepartment, statusFilter, request);
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Searches for product discontinue records based on department and class code and commodity and sub commodity.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class to search for records on.
	 * @param commodityCode A commodity to search for records on.
	 * @param subCommodityCode A sub commodity to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByDepartmentAndClassAndCommodityAndSubCommodity(String department, String subDepartment,
																												  int classCode,
																												  int commodityCode, int subCommodityCode,
																												  Pageable request, StatusFilter statusFilter,
																												  boolean includeCount) {

		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		return includeCount ? this.searchByDepartmentAndClassAndCommodityAndSubCommodityWithCounts(department, subDepartment, classCode, commodityCode, subCommodityCode, request, statusFilter) :
				this.searchByDepartmentAndClassAndCommodityAndSubCommodityWithoutCounts(department, subDepartment, classCode, commodityCode, subCommodityCode,request, statusFilter);
	}

	/**
	 * Handles the department and class code and commodity and sub commodity search when the user wants counts.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class code to search for records on.
	 * @param commodityCode A commodity code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndClassAndCommodityAndSubCommodityWithCounts(String department, String subDepartment,
																									 int classCode, int commodityCode,
																									 int subCommodityCode, Pageable request,
																									 StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByDepartmentAndClassAndCommodityAndSubCommodityWithCount(department, subDepartment,
						classCode, commodityCode,subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByDepartmentAndClassAndCommodityAndSubCommodityWithCount(department, subDepartment,
						classCode, commodityCode, subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.
						findByDepartmentAndClassAndCommodityAndSubCommodityWithCount(department, subDepartment, classCode, commodityCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the department and class code and commodity and sub commodity search when the user not wants counts.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class code to search for records on.
	 * @param commodityCode A commodity code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndClassAndCommodityAndSubCommodityWithoutCounts(String department, String subDepartment,
																											   int classCode, int commodityCode,
																											   int subCommodityCode, Pageable request,
																											   StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByDepartmentAndClassAndCommodityAndSubCommodity(department, subDepartment,
						classCode, commodityCode,subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByDepartmentAndClassAndCommodityAndSubCommodity(department, subDepartment,
						classCode, commodityCode, subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.
						findByDepartmentAndClassAndCommodityAndSubCommodity(department, subDepartment, classCode, commodityCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Searches for product discontinue records based on commodity and sub commodity.
	 *
	 * @param commodityCode A commodity to search for records on.
	 * @param subCommodityCode A sub commodity to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByCommodityAndSubCommodity(int commodityCode, int subCommodityCode,
																			 Pageable request, StatusFilter statusFilter,
																			 boolean includeCount) {

		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		return includeCount ? this.searchByCommodityAndSubCommodityWithCounts(commodityCode, subCommodityCode, request, statusFilter) :
				this.searchByCommodityAndSubCommodityWithoutCounts(commodityCode, subCommodityCode,request, statusFilter);
	}

	/**
	 * Handles the commodity and sub commodity search when the user wants counts.
	 *
	 * @param commodityCode A commodity code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByCommodityAndSubCommodityWithCounts(int commodityCode,
																						  int subCommodityCode, Pageable request,
																						  StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByCommodityAndSubCommodityWithCount(commodityCode,
						subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByCommodityAndSubCommodityWithCount(commodityCode,
						subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.findDistinctByProductMasterCommodityCodeAndProductMasterSubCommodityCode(commodityCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}
	/**
	 * Handles the commodity and sub commodity search when the user does not want counts.
	 *
	 * @param commodityCode A commodity code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByCommodityAndSubCommodityWithoutCounts(int commodityCode,
																							 int subCommodityCode, Pageable request,
																							 StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByCommodityAndSubCommodity(commodityCode, subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByCommodityAndSubCommodity(commodityCode, subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.findDistinctByProductMasterCommodityCodeAndProductMasterSubCommodityCode(commodityCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Searches for product discontinue records based on department and class code and commodity.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class to search for records on.
	 * @param commodityCode A commodity to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByDepartmentAndClassAndCommodity(String department, String subDepartment,
																				   int classCode, int commodityCode,
																				   Pageable request, StatusFilter statusFilter,
																				   boolean includeCount) {

		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		return includeCount ? this.searchByDepartmentAndClassAndCommodityWithCounts(department, subDepartment, classCode, commodityCode, request, statusFilter) :
				this.searchByDepartmentAndClassAndCommodityWithoutCounts(department, subDepartment, classCode, commodityCode, request, statusFilter);
	}

	/**
	 * Handles the sub commodity search when the user wants counts.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class code to search for records on.
	 * @param commodityCode A commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndClassAndCommodityWithCounts(String department, String subDepartment,
																								int classCode, int commodityCode,
																								Pageable request,
																								StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByDepartmentAndClassAndCommodityWithCount(department, subDepartment, classCode, commodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByDepartmentAndClassAndCommodityWithCount(department, subDepartment,
						classCode, commodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.findByDepartmentAndClassAndCommodityWithCount(department, subDepartment, classCode, commodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the sub commodity search when the user not wants counts.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndClassAndCommodityWithoutCounts(String department, String subDepartment,
																								   int classCode,
																								   int subCommodityCode, Pageable request,
																								   StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByDepartmentAndClassAndCommodity(department, subDepartment, classCode, subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByDepartmentAndClassAndCommodity(department, subDepartment, classCode, subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.findByDepartmentAndClassAndCommodity(department, subDepartment, classCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Searches for product discontinue records based on department and commodity and sub commodity.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param commodityCode A commodity to search for records on.
	 * @param subCommodityCode A sub commodity to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByDepartmentAndCommodityAndSubCommodity(String department, String subDepartment,
																						  int commodityCode, int subCommodityCode,
																						  Pageable request, StatusFilter statusFilter,
																						  boolean includeCount) {

		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		return includeCount ? this.searchByDepartmentAndCommodityAndSubCommodityWithCounts(department, subDepartment, commodityCode, subCommodityCode, request, statusFilter) :
				this.searchByDepartmentAndCommodityAndSubCommodityWithoutCounts(department, subDepartment, commodityCode, subCommodityCode,request, statusFilter);
	}

	/**
	 * Handles the department and commodity and sub commodity search when the user wants counts.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param commodityCode A commodity code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndCommodityAndSubCommodityWithCounts(String department, String subDepartment,
																									   int commodityCode,
																									   int subCommodityCode, Pageable request,
																									   StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByDepartmentAndCommodityAndSubCommodityWithCount(department, subDepartment,
						commodityCode,subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByDepartmentAndCommodityAndSubCommodityWithCount(department, subDepartment,
						commodityCode, subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.findByDepartmentAndCommodityAndSubCommodityWithCount(department, subDepartment, commodityCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the sub commodity search when the user not wants counts.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param commodityCode A commodity code to search for records on.
	 * @param subCommodityCode A sub commodity code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndCommodityAndSubCommodityWithoutCounts(String department, String subDepartment,
																										  int commodityCode,
																										  int subCommodityCode, Pageable request,
																										  StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByDepartmentAndCommodityAndSubCommodity(department, subDepartment,
						commodityCode,subCommodityCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByDepartmentAndCommodityAndSubCommodity(department, subDepartment,
						commodityCode, subCommodityCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.findByDepartmentAndCommodityAndSubCommodity(department, subDepartment, commodityCode, subCommodityCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Searches for product discontinue records based on department and class code.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByDepartmentAndClass(String department, String subDepartment,
																	   int classCode,
																	   Pageable request, StatusFilter statusFilter,
																	   boolean includeCount) {
		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		return includeCount ? this.searchByDepartmentAndClassWithCounts(department, subDepartment, classCode, request, statusFilter) :
				this.searchByDepartmentAndClassWithoutCounts(department, subDepartment, classCode, request, statusFilter);
	}

	/**
	 * Handles the department and class code search when the user wants counts.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndClassWithCounts(String department, String subDepartment,
																					int classCode, Pageable request,
																					StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByDepartmentAndClassWithCount(department, subDepartment, classCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByDepartmentAndClassWithCount(department, subDepartment, classCode, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.findByDepartmentAndClassWithCount(department, subDepartment, classCode, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the department and class code search when the user not wants counts.
	 *
	 * @param department The department to search for
	 * @param subDepartment The subDepartment to search for
	 * @param classCode A class code to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchByDepartmentAndClassWithoutCounts(String department, String subDepartment,
																					   int classCode, Pageable request,
																					   StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByDepartmentAndClass(department, subDepartment, classCode, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByDepartmentAndClass(department, subDepartment, classCode, request);
				break;
			default:
				data = this.productDiscontinueRepository.findByDepartmentAndClass(department, subDepartment, classCode, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}
}