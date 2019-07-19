/*
 * BdmService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.Bdm;
import com.heb.pm.index.DocumentWrapperUtil;
import com.heb.pm.repository.BdmIndexRepository;
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
 * Holds all business functions related to bdms.
 *
 * @author m314029
 * @since 2.0.6
 */
@Service
public class BdmService {

	private static final Logger logger = LoggerFactory.getLogger(BdmService.class);

	private static final String BDM_REGULAR_EXPRESSION = "*%s*";
	private static final String BDM_SEARCH_LOG_MESSAGE =
			"searching for bdms by the regular expression '%s'";

	@Autowired
	BdmIndexRepository indexRepository;

	/**
	 * Searches for a list of bdms by a regular expression. This is a wildcard search, meaning that
	 * anything partially matching the text passed in will be returned.
	 *
	 * @param searchString The text to search for bdms by.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with classes matching the search criteria.
	 */
	public PageableResult<Bdm> findByRegularExpression(String searchString, int page, int pageSize) {

		String regex = String.format(BdmService.BDM_REGULAR_EXPRESSION, searchString.toUpperCase());

		BdmService.logger.debug(String.format(BdmService.BDM_SEARCH_LOG_MESSAGE, regex));

		Page<BdmDocument> bd = this.indexRepository.findByRegularExpression(regex,
				new PageRequest(page, pageSize));

		List<Bdm> bdmList = new ArrayList<>(bd.getSize());
		DocumentWrapperUtil.toDataCollection(bd, bdmList);

		return new PageableResult<>(page, bd.getTotalPages(), bd.getTotalElements(), bdmList);
	}

	/**
	 * Searches for a specific bdm.
	 *
	 * @param bdmCode The ID of the bdm to look for.
	 * @return A Bdm with the ID requested.
	 */
	public Bdm findBdm(String bdmCode) {
		BdmDocument bd = this.indexRepository.findOne(bdmCode);
		return DocumentWrapperUtil.toData(bd);
	}

	/**
	 * Sets the BdmIndexRepository for the object to use. This is mainly for testing.
	 *
	 * @param indexRepository The BdmIndexRepository for the object to use.
	 */
	public void setIndexRepository(BdmIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
