/*
 *  CodeTablesWineAreaController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.wineArea;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.CodeTablesService;
import com.heb.pm.entity.WineArea;
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
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CodeTablesWineAreaController.CODE_TABLE)
@AuthorizedResource(ResourceConstants.CODE_TABLE_WINE_AREA)
public class CodeTablesWineAreaController {

	private static final Logger logger = LoggerFactory.getLogger(CodeTablesWineAreaController.class);

	protected static final String URL_GET_ALL_WINE_AREA = "/getAllWineArea";
	protected static final String URL_ADD_NEW_WINE_AREA = "/addNewlWineArea";
	protected static final String URL_UPDATE_WINE_AREA = "/updateWineArea";
	protected static final String URL_DELETE_WINE_AREA = "/deleteWineArea";
	public static final String CODE_TABLE = "/code-table";
	/**
	 * add new message
	 */
	private static final String ADD_WINE_AREA_MESSAGE = "User %s from IP %s requested to add new wine area. ";
	private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";

	/**
	 * update message
	 */
	private static final String UPDATE_WINE_AREA_MESSAGE = "User %s from IP %s requested to add update wine area. ";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	/**
	 * delete MESSAGE
	 */
	private static final String DELETE_WINE_AREA_MESSAGE = "User %s from IP %s requested to delete wine area. ";
	private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";

	// Log	messages.
	private static final String FIND_ALL_WINE_AREA_MESSAGE = "User %s from IP %s requested to find all wine area. ";
	 
	@Autowired 
	private UserInfo userInfo;
	@Autowired
	private CodeTablesService codeTablesService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * get list wine area from database.
	 *
	 * @throws Exception
	 * @return List value of process.
	 * @author vn87351
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CodeTablesWineAreaController.URL_GET_ALL_WINE_AREA)
	public List<WineArea> findAllWineArea(HttpServletRequest request) throws Exception{

		CodeTablesWineAreaController.logger.info(String.format(CodeTablesWineAreaController.FIND_ALL_WINE_AREA_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr()));
		return codeTablesService.findAllWineArea();
	}
	/**
	 * Add new the list of wine area.
	 *
	 * @param items The list of wine area to add new.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of wine area after add new and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_ADD_NEW_WINE_AREA)
	public ModifiedEntity<List<WineArea>> addWineArea(@RequestBody List<WineArea> items,
														 HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(ADD_WINE_AREA_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.addNewWineArea(items);
		/**
		 * research data after delete successfully and return to ui
		 */
		return new ModifiedEntity(codeTablesService.findAllWineArea(), ADD_SUCCESS_MESSAGE);
	}
	/**
	 * update info for list of wine area.
	 *
	 * @param items The list of wine area to update.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of wine area after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_UPDATE_WINE_AREA)
	public ModifiedEntity<List<WineArea>> updateWineArea(@RequestBody List<WineArea> items,
															HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(UPDATE_WINE_AREA_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.updateWineArea(items);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllWineArea(), UPDATE_SUCCESS_MESSAGE);
	}
	/**
	 * delete list of wine area.
	 *
	 * @param items The list of wine area to delete.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of wine area after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_DELETE_WINE_AREA)
	public ModifiedEntity<List<WineArea>> deleteWineArea(@RequestBody List<WineArea> items,
															HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(DELETE_WINE_AREA_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.deleteWineArea(items);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllWineArea(), DELETE_SUCCESS_MESSAGE);
	}
}
