/*
 *  BatchUploadController
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
import com.heb.pm.codeTable.choice.ChoiceTypeController;
import com.heb.pm.entity.ChoiceType;
import com.heb.pm.entity.Varietal;
import com.heb.pm.entity.VarietalType;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.list.ListFormatter;
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
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CodeTablesVarietalController.CODE_TABLE)
@AuthorizedResource(ResourceConstants.CODE_TABLE_WINE_VARIETAL)
public class CodeTablesVarietalController {

	private static final Logger logger = LoggerFactory.getLogger(CodeTablesVarietalController.class);

	protected static final String URL_GET_ALL_VARIETAL = "/getAllVarietal";
	protected static final String URL_GET_ALL_VARIETAL_TYPE = "/getAllVarietalType";
	protected static final String URL_ADD_NEW_VARIETAL = "/addNewlVarietal";
	protected static final String URL_UPDATE_VARIETAL = "/updateVarietal";
	protected static final String URL_DELETE_VARIETAL = "/deleteVarietal";
	public static final String CODE_TABLE = "/code-table";

	// Log	messages.
	private static final String FIND_ALL_VARIETAL_MESSAGE = "User %s from IP %s requested to find all varietal. ";
	private static final String FIND_ALL_VARIETAL_TYPE_MESSAGE = "User %s from IP %s requested to find all varietal type. ";
	/**
	 * add new message
	 */
	private static final String ADD_VARIETAL_MESSAGE = "User %s from IP %s requested to add new varietal. ";
	private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";

	/**
	 * update message
	 */
	private static final String UPDATE_VARIETAL_MESSAGE = "User %s from IP %s requested to add update varietal. ";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	/**
	 * delete MESSAGE
	 */
	private static final String DELETE_VARIETAL_MESSAGE = "User %s from IP %s requested to delete varietal. ";
	private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";

	@Autowired
	private UserInfo userInfo;
	@Autowired
	private CodeTablesService codeTablesService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * get list varietal from database.
	 *
	 * @throws Exception
	 * @return List value of process.
	 * @author vn87351
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CodeTablesVarietalController.URL_GET_ALL_VARIETAL)
	public List<Varietal> getAllVarietal(HttpServletRequest request) throws Exception{

		CodeTablesVarietalController.logger.info(String.format(CodeTablesVarietalController.FIND_ALL_VARIETAL_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr()));
		return codeTablesService.findAllVarietal();
	}

	/**
	 * Add new the list of varietal.
	 *
	 * @param varietals The list of varietals to add new.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of varietals after add new and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CodeTablesVarietalController.URL_ADD_NEW_VARIETAL)
	public ModifiedEntity<List<Varietal>> addVarietals(@RequestBody List<Varietal> varietals,
														   HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		CodeTablesVarietalController.logger.info(String.format(CodeTablesVarietalController.ADD_VARIETAL_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.addNewVarietal(varietals);
		/**
		 * research data after delete successfully and return to ui
		 */
		return new ModifiedEntity(codeTablesService.findAllVarietal(), ADD_SUCCESS_MESSAGE);
	}
	/**
	 * update info for list of varietal.
	 *
	 * @param varietals The list of varietals to update.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of varietals after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CodeTablesVarietalController.URL_UPDATE_VARIETAL)
	public ModifiedEntity<List<Varietal>> updateVarietals(@RequestBody List<Varietal> varietals,
													   HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		CodeTablesVarietalController.logger.info(String.format(CodeTablesVarietalController.UPDATE_VARIETAL_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.updateVarietal(varietals);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllVarietal(), UPDATE_SUCCESS_MESSAGE);
	}
	/**
	 * delete list of varietal.
	 *
	 * @param varietals The list of varietals to delete.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of varietals after save and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CodeTablesVarietalController.URL_DELETE_VARIETAL)
	public ModifiedEntity<List<Varietal>> deleteVarietals(@RequestBody List<Varietal> varietals,
														  HttpServletRequest request)throws Exception {
		/**
		 * show log message when init method
		 */
		CodeTablesVarietalController.logger.info(String.format(CodeTablesVarietalController.DELETE_VARIETAL_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr()));
		/**
		 * call service to save
		 */
		this.codeTablesService.deleteVarietal(varietals);
		//research data after delete successfully and return to ui
		return new ModifiedEntity(codeTablesService.findAllVarietal(), DELETE_SUCCESS_MESSAGE);
	}
}
