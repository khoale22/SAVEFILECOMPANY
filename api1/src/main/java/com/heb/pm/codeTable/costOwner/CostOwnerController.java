/*
 *  CostOwnerController
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.costOwner;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.CostOwner;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Rest endpoint for cost owner.
 *
 * @author vn70529
 * @since 2.41.0
 **/
@RestController
@RequestMapping(CostOwnerController.ROOT_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_COST_OWNER)
public class CostOwnerController {

	private static final Logger logger = LoggerFactory.getLogger(CostOwnerController.class);

	protected static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/codeTable/costOwner";

	private static final String GET_COST_OWNER_PAGE_URL = "/findCostOwners";
	private static final String ADD_COST_OWNER_URL = "/addCostOwners";
	private static final String UPDATE_COST_OWNER_URL = "/updateCostOwner";
	private static final String DELETE_COST_OWNER_URL = "/deleteCostOwner";

	// Log messages.
	private static final String FIND_COST_OWNER_MESSAGE = "User %s from IP %s has requested to find cost " +
			"owners by page: %d, page size: %d and include count: %s.";
	private static final String ADD_COST_OWNER_MESSAGE = "User %s from IP %s has requested to add cost owner";
	private static final String UPDATE_COST_OWNER_MESSAGE = "User %s from IP %s has requested to update cost " +
			"owner with the id: %s and name: %s.";
	private static final String DELETE_COST_OWNER_MESSAGE = "User %s from IP %s has requested to delete cost " +
			"owner with the id: %s.";

	// Keys to user facing messages in the message resource bundle.
	private static final String ADD_SUCCESS_MESSAGE = "Successfully added.";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Successfully updated.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY = "CostOwnerController.updateSuccessfully";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE = "Successfully deleted.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY = "CostOwnerController.deleteSuccessfully";

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;

	@Autowired
	private CostOwnerService costOwnerService;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Get all cost owners records (filter will be applied if exist).
	 *
	 * @param id           The id to search.
	 * @param name         The name to search.
	 * @param page         The page number.
	 * @param pageSize     The page size.
	 * @param includeCount Whether count of total records needs to be done.
	 * @param request      The http servlet request.
	 * @return the page of cost owners.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CostOwnerController.GET_COST_OWNER_PAGE_URL)
	public PageableResult<CostOwner> findCostOwners(@RequestParam(value = "id", required = false) String id,
	                                                @RequestParam(value = "name", required = false) String name,
	                                                @RequestParam(value = "page", required = false) Integer page,
	                                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
	                                                @RequestParam(value = "includeCount", required = false) Boolean includeCount,
	                                                HttpServletRequest request) {

		int pageNo = page == null ? CostOwnerController.DEFAULT_PAGE : page;
		int size = pageSize == null ? CostOwnerController.DEFAULT_PAGE_SIZE : pageSize;
		boolean count = includeCount == null ? Boolean.FALSE : includeCount;

		logger.info(String.format(CostOwnerController.FIND_COST_OWNER_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr(), pageNo, size, count));

		return this.costOwnerService.findCostOwners(id, name, pageNo, size, count);
	}

	/**
	 * Adds new cost owners.
	 *
	 * @param costOwners the cost owner descriptions
	 * @param request    The HTTP request that initiate this call.
	 * @return the added cost owners.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CostOwnerController.ADD_COST_OWNER_URL)
	public ModifiedEntity<List<CostOwner>> addCostOwners(@RequestBody List<CostOwner> costOwners,
	                                                     HttpServletRequest request) {

		logger.info(String.format(CostOwnerController.ADD_COST_OWNER_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr()));
		List<CostOwner> validateList = this.costOwnerService.validateCostOwners(costOwners);
		if(CollectionUtils.isNotEmpty(validateList)){
			return new ModifiedEntity<>(validateList, CostOwnerService.COST_OWNER_EXIST_ERROR_MESSAGE);
		}
		List<CostOwner> addedCostOwners = this.costOwnerService.addCostOwners(costOwners);
		return new ModifiedEntity<>(addedCostOwners, ADD_SUCCESS_MESSAGE);
	}

	/**
	 * Update a CostOwner's name.
	 *
	 * @param costOwner The cost owner to be updated.
	 * @param request   The HTTP request that initiate this call.
	 * @return the updated CostOwner and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = CostOwnerController.UPDATE_COST_OWNER_URL)
	public ModifiedEntity<CostOwner> updateCostOwner(@RequestBody CostOwner costOwner,
	                                                 HttpServletRequest request) {

		logger.info(String.format(CostOwnerController.UPDATE_COST_OWNER_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr(), costOwner.getCostOwnerId(), costOwner.getCostOwnerName()));

		CostOwner updatedCostOwner = this.costOwnerService.updateCostOwner(costOwner);
		String updateMessage = this.messageSource.getMessage(CostOwnerController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{costOwner.getCostOwnerId()}, CostOwnerController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				request.getLocale());

		return new ModifiedEntity<>(updatedCostOwner, updateMessage);
	}

	/**
	 * Deletes a CostOwner.
	 *
	 * @param costOwnerId The CostOwner's costOwnerId to be deleted.
	 * @param request     The HTTP request that initiated this call.
	 * @return the deleted CostOwner ID and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE, value = CostOwnerController.DELETE_COST_OWNER_URL)
	public ModifiedEntity<Integer> deleteCostOwner(@RequestParam Integer costOwnerId,
	                                               HttpServletRequest request) {

		logger.info(String.format(CostOwnerController.DELETE_COST_OWNER_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr(), costOwnerId));

		this.costOwnerService.deleteCostOwner(costOwnerId);
		String updateMessage = this.messageSource.getMessage(CostOwnerController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{costOwnerId}, CostOwnerController.DEFAULT_DELETE_SUCCESS_MESSAGE, request.getLocale());

		return new ModifiedEntity<>(costOwnerId, updateMessage);
	}
}
