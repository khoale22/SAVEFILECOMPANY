/*
 *  CodeTablesWineMakerController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.wineRegion;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.CodeTablesService;
import com.heb.pm.codeTable.wineVarietal.CodeTablesVarietalTypeController;
import com.heb.pm.entity.VarietalType;
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
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CodeTablesWineRegionController.CODE_TABLE)
@AuthorizedResource(ResourceConstants.CODE_TABLE_WINE_REGION)
public class CodeTablesWineRegionController {

	private static final Logger logger = LoggerFactory.getLogger(CodeTablesWineRegionController.class);
	protected static final String URL_GET_ALL_WINE_REGION= "/getAllWineRegion";
	protected static final String URL_ADD_NEW_WINE_REGION = "/addNewlWineRegion";
	protected static final String URL_UPDATE_WINE_REGION = "/updateWineRegion";
	protected static final String URL_DELETE_WINE_REGION = "/deleteWineRegion";
	public static final String CODE_TABLE = "/code-table";

	// Log	messages.
	private static final String FIND_ALL_WINE_REGION_MESSAGE = "User %s from IP %s requested to find all wine region. ";
	/**
	 * add new message
	 */
	private static final String ADD_WINE_REGION_MESSAGE = "User %s from IP %s requested to add new wine region. ";
	private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";

	/**
	 * update message
	 */
	private static final String UPDATE_WINE_REGION_MESSAGE = "User %s from IP %s requested to add update wine region. ";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	/**
	 * delete MESSAGE
	 */
	private static final String DELETE_WINE_REGION_MESSAGE = "User %s from IP %s requested to delete wine region. ";
	private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";
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
	@RequestMapping(method = RequestMethod.GET, value = CodeTablesWineRegionController.URL_GET_ALL_WINE_REGION)
	public List<WineRegion> findAllWineRegion(HttpServletRequest request) throws Exception{

		CodeTablesWineRegionController.logger.info(String.format(CodeTablesWineRegionController.FIND_ALL_WINE_REGION_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr()));
		return codeTablesService.findAllWineRegion();
	}
	/**
	 * Add new the list of wine region.
	 *
	 * @param items The list of wine region to add new.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of wine region after add new and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_ADD_NEW_WINE_REGION)
	public ModifiedEntity<List<WineRegion>> addWineRegion(@RequestBody List<WineRegion> items,
														   HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(ADD_WINE_REGION_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.addNewWineRegion(items);
		/**
		 * research data after delete successfully and return to ui
		 */
		return new ModifiedEntity(codeTablesService.findAllWineRegion(), ADD_SUCCESS_MESSAGE);
	}
	/**
	 * update info for list of varietal type.
	 *
	 * @param items The list of varietals type to update.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of varietals type after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_UPDATE_WINE_REGION)
	public ModifiedEntity<List<WineRegion>> updateWineRegion(@RequestBody List<WineRegion> items,
															  HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(UPDATE_WINE_REGION_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.updateWineRegion(items);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllWineRegion(), UPDATE_SUCCESS_MESSAGE);
	}
	/**
	 * delete list of varietal type.
	 *
	 * @param items The list of varietals type to delete.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of varietals type after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = URL_DELETE_WINE_REGION)
	public ModifiedEntity<List<WineRegion>> deleteWineRegion(@RequestBody List<WineRegion> items,
															  HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		logger.info(String.format(DELETE_WINE_REGION_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.deleteWineRegion(items);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllWineRegion(), DELETE_SUCCESS_MESSAGE);
	}
}
