/*
 * ItemController.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.endpoint.itemInfo;

import com.heb.pm.ApiConstants;
import com.heb.pm.entity.Item;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController()
@RequestMapping(ApiConstants.BASE_WSAG_APPLICATION_URL + ItemController.ITEM_CODE_BASE_URL)
public class ItemController {

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	protected static final String ITEM_CODE_BASE_URL = "/item";

	private static final String FIND_ITEM_INFO_BY_ITEM_CD =
			"User %s from IP %s has requested item information by item code: %d";
	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ItemService itemService;

	/**
	 * Get item info.
	 *
	 * @param request the request
	 * @return the requested item info.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{itemCode}")
	public Item getItemInfo(@PathVariable(value="itemCode") Long itemCode, HttpServletRequest request, HttpServletResponse response){
		this.logFindItemInfoByItemCode(request.getRemoteAddr(), itemCode);
		Item item = this.itemService.getItemInfoByItemCode(itemCode);
		if(itemCode == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return item;
	}
	/**
	 * Logs a user's request to get all item info by item code.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindItemInfoByItemCode(String ip, long itemCode){
		ItemController.logger.info(String.format(ItemController.FIND_ITEM_INFO_BY_ITEM_CD,
				this.userInfo.getUserId(), ip, itemCode));
	}
}
