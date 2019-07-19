/*
 *  CodeTablesVarietalTypeController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.wineVarietal;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.CodeTablesService;
import com.heb.pm.entity.Varietal;
import com.heb.pm.entity.VarietalType;
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
 * varietal type controller.
 *
 * @author vn87351
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CodeTablesVarietalTypeController.CODE_TABLE)
@AuthorizedResource(ResourceConstants.CODE_TABLE_WINE_VARIETAL_TYPE)
public class CodeTablesVarietalTypeController {

	private static final Logger logger = LoggerFactory.getLogger(CodeTablesVarietalTypeController.class);
	protected static final String URL_GET_ALL_VARIETAL_TYPE = "/getAllVarietalType";
	protected static final String URL_ADD_NEW_VARIETAL_TYPE = "/addNewlVarietalType";
	protected static final String URL_UPDATE_VARIETAL_TYPE = "/updateVarietalType";
	protected static final String URL_DELETE_VARIETAL_TYPE = "/deleteVarietalType";
	public static final String CODE_TABLE = "/code-table";

	// Log	messages.
	private static final String FIND_ALL_VARIETAL_TYPE_MESSAGE = "User %s from IP %s requested to find all varietal type. ";
	/**
	 * add new message
	 */
	private static final String ADD_VARIETAL_TYPE_MESSAGE = "User %s from IP %s requested to add new varietal type. ";
	private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";

	/**
	 * update message
	 */
	private static final String UPDATE_VARIETAL_TYPE_MESSAGE = "User %s from IP %s requested to add update varietal type. ";
	private static final String UPDATE_SUCCESS_MESSAGE = "Updated successfully.";
	/**
	 * delete MESSAGE
	 */
	private static final String DELETE_VARIETAL_TYPE_MESSAGE = "User %s from IP %s requested to delete varietal type. ";
	private static final String DELETE_SUCCESS_MESSAGE = "Deleted successfully.";

	@Autowired
	private UserInfo userInfo;
	@Autowired
	private CodeTablesService codeTablesService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * get list varietal Type from database.
	 *
	 * @throws Exception
	 * @return List value of process.
	 * @author vn87351
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CodeTablesVarietalTypeController.URL_GET_ALL_VARIETAL_TYPE)
	public List<VarietalType> getAllVarietalType(HttpServletRequest request) throws Exception{

		CodeTablesVarietalTypeController.logger.info(String.format(
				CodeTablesVarietalTypeController.FIND_ALL_VARIETAL_TYPE_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
		return codeTablesService.findAllVarietalType();
	}
	/**
	 * Add new the list of varietal type.
	 *
	 * @param items The list of varietals type to add new.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of varietals type after add new and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CodeTablesVarietalTypeController.URL_ADD_NEW_VARIETAL_TYPE)
	public ModifiedEntity<List<VarietalType>> addVarietalType(@RequestBody List<VarietalType> items,
														   HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		CodeTablesVarietalTypeController.logger.info(String.format(CodeTablesVarietalTypeController.ADD_VARIETAL_TYPE_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.addNewVarietalType(items);
		/**
		 * research data after delete successfully and return to ui
		 */
		return new ModifiedEntity(codeTablesService.findAllVarietalType(), ADD_SUCCESS_MESSAGE);
	}
	/**
	 * update info for list of varietal type.
	 *
	 * @param items The list of varietals type to update.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of varietals type after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CodeTablesVarietalTypeController.URL_UPDATE_VARIETAL_TYPE)
	public ModifiedEntity<List<VarietalType>> updateVarietalType(@RequestBody List<VarietalType> items,
													   HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		CodeTablesVarietalTypeController.logger.info(String.format(CodeTablesVarietalTypeController.UPDATE_VARIETAL_TYPE_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.updateVarietalType(items);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllVarietalType(), UPDATE_SUCCESS_MESSAGE);
	}
	/**
	 * delete list of varietal type.
	 *
	 * @param items The list of varietals type to delete.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of varietals type after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CodeTablesVarietalTypeController.URL_DELETE_VARIETAL_TYPE)
	public ModifiedEntity<List<VarietalType>> deleteVarietalType(@RequestBody List<VarietalType> items,
														  HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		CodeTablesVarietalTypeController.logger.info(String.format(CodeTablesVarietalTypeController.DELETE_VARIETAL_TYPE_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.deleteVarietalType(items);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllVarietalType(), DELETE_SUCCESS_MESSAGE);
	}
}
