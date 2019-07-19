/*
 * SubDepartmentService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.index.DocumentWrapperUtil;
import com.heb.pm.repository.SubDepartmentIndexRepository;
import com.heb.pm.repository.SubDepartmentRepository;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business functions related to sub-departments.
 *
 * @author d116773
 * @since 2.0.2
 */
@Service
public class SubDepartmentService {

	private static final Logger logger = LoggerFactory.getLogger(SubDepartmentService.class);

	private static final String SUB_DEPARTMENT_REGULAR_EXPRESSION = "*%s*";
	private static final String SUB_DEPARTMENT_SEARCH_LOG_MESSAGE =
			"searching for sub-department by the regular expression '%s'";

	@Autowired
	private SubDepartmentIndexRepository subDepartmentIndexRepository;

	@Autowired
	private SubDepartmentRepository subDepartmentRepository;

	@Autowired
	private ProductHierarchyUtils productHierarchyUtils;

	private LazyObjectResolver<SubDepartment> subDepartmentResolver = new SubDepartmentResolver();

	/**
	 * Searches for a list of sub-departments by a regular expression.  This is a wildcard search, meaning that
	 * anything partially matching the text passed in will be returned.
	 *
	 * @param searchString The text to search for sub-departments by.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with sub-departments matching the search criteria.
	 */
	public PageableResult<SubDepartment> findByRegularExpression(String searchString, int page, int pageSize) {

		String regexString = String.format(SubDepartmentService.SUB_DEPARTMENT_REGULAR_EXPRESSION,
				searchString.toUpperCase());

		SubDepartmentService.logger.debug(String.format(SubDepartmentService.SUB_DEPARTMENT_SEARCH_LOG_MESSAGE,
				regexString));

		Page<SubDepartmentDocument> sdp = this.subDepartmentIndexRepository.findByRegularExpression(regexString,
				new PageRequest(page, pageSize));

		// Convert the SubDepartmentDocuments passed in to SubDepartments.
		List<SubDepartment> subDepartments = new ArrayList<>();
		DocumentWrapperUtil.toDataCollection(sdp, subDepartments);

		return new PageableResult<>(page, sdp.getTotalPages(), sdp.getTotalElements(), subDepartments);
	}

	/**
	 * Returns a list of all sub-departments.
	 *
	 * @return A list of all sub-departments.
	 */
	public Iterable<SubDepartment> findAll() {

		Iterable<SubDepartmentDocument> d = this.subDepartmentIndexRepository.findAll();

		// Convert the SubDepartmentDocuments passed in to SubDepartments.
		List<SubDepartment> subDepartments = new ArrayList<>();
		DocumentWrapperUtil.toDataCollection(d, subDepartments);
		return subDepartments;
	}

	/**
	 * Finds a particular sub-department in the index by its key. If not found, this method will return null.
	 *
	 * @param subDepartment The key to look for.
	 * @return A sub-department matching that key.
	 */
	public SubDepartment findSubDepartment(String subDepartment) {
		return DocumentWrapperUtil.toData(this.subDepartmentIndexRepository.findOne(subDepartment));
	}

	/**
	 * Sets the object's SubDepartmentIndexRepository. This is used for testing.
	 *
	 * @param subDepartmentIndexRepository The SubDepartmentIndexRepository for the object to use.
	 */
	public void setIndexRepository(SubDepartmentIndexRepository subDepartmentIndexRepository) {
		this.subDepartmentIndexRepository = subDepartmentIndexRepository;
	}

	/**
	 * Finds sub-departments by page.
	 *
	 * @param pageRequest the page request
	 * @return page of sub-departments defined by page request.
	 */
	public Page<SubDepartment> findAllByPage(PageRequest pageRequest) {
		Page<SubDepartment> subDepartments =
				this.subDepartmentRepository.findAll(pageRequest);
		this.productHierarchyUtils.extrapolateItemClassListOfSubDepartmentList(subDepartments.getContent());
		subDepartments.forEach(subDepartment -> this.subDepartmentResolver.fetch(subDepartment));
		return subDepartments;
	}
}
