/*
 *  CodeTablesWineMakerController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.wineMaker;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.CodeTablesService;
import com.heb.pm.entity.WineMaker;
import com.heb.pm.entity.WineRegion;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * batch upload file.
 *
 * @author vn87351
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CodeTablesWineMakerController.CODE_TABLE)
@AuthorizedResource(ResourceConstants.CODE_TABLE_WINE_MAKER)
public class CodeTablesWineMakerController {

	private static final Logger logger = LoggerFactory.getLogger(CodeTablesWineMakerController.class);
	protected static final String URL_GET_ALL_WINE_MAKER = "/getAllWineMaker";
	protected static final String URL_ADD_NEW_WINE_MAKER = "/addNewlWineMaker";
	protected static final String URL_UPDATE_WINE_MAKER = "/updateWineMaker";
	protected static final String URL_DELETE_WINE_MAKER = "/deleteWineMaker";
	public static final String CODE_TABLE = "/code-table";
	/**
	 * add new message
	 */
	private static final String ADD_WINE_MAKER_MESSAGE = "User %s from IP %s requested to add new wine maker. ";
	private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";

	/**
	 * update message
	 */
	private static final String UPDATE_WINE_MAKER_MESSAGE = "User %s from IP %s requested to add update wine maker. ";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	/**
	 * delete MESSAGE
	 */
	private static final String DELETE_WINE_MAKER_MESSAGE = "User %s from IP %s requested to delete wine maker. ";
	private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";
	// Log	messages.
	private static final String FIND_ALL_WINE_MAKER_MESSAGE = "User %s from IP %s requested to find all wine maker. ";
	 
	@Autowired 
	private UserInfo userInfo;
	@Autowired
	private CodeTablesService codeTablesService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * get list wine maker from database.
	 *
	 * @throws Exception
	 * @return List value of process.
	 * @author vn87351
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CodeTablesWineMakerController.URL_GET_ALL_WINE_MAKER)
	public List<WineMaker> findAllWineMaker(HttpServletRequest request) throws Exception{

		CodeTablesWineMakerController.logger.info(String.format(CodeTablesWineMakerController.FIND_ALL_WINE_MAKER_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr()));
		return codeTablesService.findAllWineMaker();
	}
	/**
	 * Add new the list of wine maker.
	 *
	 * @param items The list of wine maker to add new.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of wine maker after add new and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_ADD_NEW_WINE_MAKER)
	public ModifiedEntity<List<WineMaker>> addWineMaker(@RequestBody List<WineMaker> items,
														 HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(ADD_WINE_MAKER_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.addNewWineMaker(items);
		/**
		 * research data after delete successfully and return to ui
		 */
		return new ModifiedEntity(codeTablesService.findAllWineMaker(), ADD_SUCCESS_MESSAGE);
	}
	/**
	 * update info for list of wine maker.
	 *
	 * @param items The list of wine maker to update.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of wine maker after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_UPDATE_WINE_MAKER)
	public ModifiedEntity<List<WineMaker>> updateWineMaker(@RequestBody List<WineMaker> items,
															HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(UPDATE_WINE_MAKER_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.updateWineMaker(items);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllWineMaker(), UPDATE_SUCCESS_MESSAGE);
	}
	/**
	 * delete list of wine maker.
	 *
	 * @param items The list of wine maker to delete.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of wine maker after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_DELETE_WINE_MAKER)
	public ModifiedEntity<List<WineMaker>> deleteWineMaker(@RequestBody List<WineMaker> items,
															HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(DELETE_WINE_MAKER_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.deleteWineMaker(items);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllWineMaker(), DELETE_SUCCESS_MESSAGE);
	}
}
