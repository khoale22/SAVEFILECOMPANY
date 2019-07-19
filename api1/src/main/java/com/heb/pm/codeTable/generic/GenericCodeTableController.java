package com.heb.pm.codeTable.generic;

import com.heb.pm.ApiConstants;
import com.heb.pm.entity.CodeTable;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest endpoint for generic code tables.
 *
 * @author m314029
 * @since 2.21.0
 */
@RestController()
@RequestMapping(GenericCodeTableController.ROOT_URL)
public class GenericCodeTableController {

	private static final Logger logger = LoggerFactory.getLogger(GenericCodeTableController.class);

	// urls
	static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/codeTable/generic";
	private static final String FIND_ALL_BY_TABLE = "/findAllByTable";
	private static final String FIND_CODE_BY_TABLE_AND_ID = "/findCodeByTableNameAndId";
	private static final String ADD_ALL_BY_TABLE_AND_CODE_TABLES = "/addAllByTableNameAndCodeTables";
	private static final String SAVE_BY_TABLE_AND_CODE_TABLES = "/saveCodeByTableNameAndCodeTable";
	private static final String DELETE_BY_TABLE_AND_ID = "/deleteCodeByTableNameAndId";

	// logs
	private static final String FIND_ALL_BY_TABLE_MESSAGE = "User %s from IP %s requested to find all values for " +
			"code table: %s.";
	private static final String FIND_CODE_BY_TABLE_AND_ID_MESSAGE = "User %s from IP %s requested to find a code for " +
			"code table: %s and ID: %s.";
	private static final String ADD_CODES_BY_TABLE_MESSAGE = "User %s from IP %s requested to add new codes for " +
			"code table: %s and IDs: %s.";
	private static final String SAVE_CODE_BY_TABLE_MESSAGE = "User %s from IP %s requested to save the code for " +
			"code table: %s and ID: %s and description: %s.";

	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Generic codes added successfully.";
	private static final String DEFAULT_SAVE_SUCCESS_MESSAGE ="Generic codes saved successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY ="GenericCodeTableController.addSuccessful";
	private static final String SAVE_SUCCESS_MESSAGE_KEY ="GenericCodeTableController.saveSuccessful";
	private static final String DELETE_CODE_LOG_MESSAGE =
			"User %s from IP %s requested to delete codes by code table: '%s' and id:'%s'.";
	private static final String DELETE_SUCCESSFUL_MESSAGE = "Generic code successfully deleted.";


	@Autowired
	private UserInfo userInfo;

	@Autowired
	private GenericCodeTableService service;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Finds all records for a given code table by table name.
	 *
	 * @param tableName Name of table that contains code table values.
	 * @param request The HTTP request that initiated this call.
	 * @return All generic code tables matching the request.
	 */
	@RequestMapping(method = RequestMethod.GET, value = GenericCodeTableController.FIND_ALL_BY_TABLE)
	public List<? extends CodeTable> findAllByTable(@RequestParam(value = "tableName") String tableName,
													HttpServletRequest request) {
		GenericCodeTableController.logger.info(String.format(
				GenericCodeTableController.FIND_ALL_BY_TABLE_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), tableName));
		return service.findAllByTable(tableName);
	}

	/**
	 * Finds the requested code by table name and id.
	 *
	 * @param tableName the tableName.
	 * @param id the id.
	 * @param request The HTTP request that initiated this call.
	 * @return the requested code by table name and id.
	 */
	@RequestMapping(method = RequestMethod.GET, value = GenericCodeTableController.FIND_CODE_BY_TABLE_AND_ID)
	public CodeTable findCodeByTableNameAndId(@RequestParam(value = "tableName") String tableName,
											  @RequestParam(value = "id") String id, HttpServletRequest request) {
		GenericCodeTableController.logger.info(String.format(
				GenericCodeTableController.FIND_CODE_BY_TABLE_AND_ID_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), tableName, id));
		return service.findCodeByTableNameAndId(tableName, id);
	}

	/**
	 * Adds all generic code table updates by table name.
	 *
	 * @param genericCodeTableUpdate the genericCodeTableUpdate.
	 * @param request The HTTP request that initiated this call.
	 * @return a ModifiedEntity with the message and all the added  generic code table updates by table name .
	 */
	@RequestMapping(method = RequestMethod.POST, value = GenericCodeTableController.ADD_ALL_BY_TABLE_AND_CODE_TABLES)
	public ModifiedEntity<List<? extends CodeTable>> addAllByTableNameAndCodeTables(@RequestBody GenericCodeTableUpdate genericCodeTableUpdate,
																					HttpServletRequest request) {
		GenericCodeTableController.logger.info(String.format(
				GenericCodeTableController.ADD_CODES_BY_TABLE_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), genericCodeTableUpdate.getTableName(),
				genericCodeTableUpdate.getCodeTables().stream().map(CodeTable::getId).collect(Collectors.toList())));
		String updateMessage = this.messageSource.getMessage(GenericCodeTableController.ADD_SUCCESS_MESSAGE_KEY,
				null, GenericCodeTableController.DEFAULT_ADD_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(this.service.saveAll(genericCodeTableUpdate.getTableName(),
				genericCodeTableUpdate.getCodeTables()), updateMessage);
	}

	/**
	 * Deletes the code table value by table name and id.
	 *
	 * @param tableName the table name.
	 * @param id the id.
	 * @param request The HTTP request that initiated this call.
	 * @return ModifiedEntity<ProductGroupInfo> the result.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = GenericCodeTableController.DELETE_BY_TABLE_AND_ID)
	public ModifiedEntity<CodeTable> deleteCodeByTableNameAndId(
			@RequestParam(value = "tableName") String tableName, @RequestParam(value = "id") String id,
			HttpServletRequest request) {
		GenericCodeTableController.logger.info(String.format(
				GenericCodeTableController.DELETE_CODE_LOG_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), tableName, id));
		this.service.deleteByTableNameAndId(tableName, id);
		return new ModifiedEntity<>(null, DELETE_SUCCESSFUL_MESSAGE);
	}

	/**
	 * Saves codes by table name and code table.
	 *
	 * @param genericCodeTableUpdate the genericCodeTableUpdate.
	 * @param request The HTTP request that initiated this call.
	 * @return a ModifiedEntity with the message and the results.
	 */
	@RequestMapping(method = RequestMethod.POST, value = GenericCodeTableController.SAVE_BY_TABLE_AND_CODE_TABLES)
	public ModifiedEntity<List<? extends CodeTable>> saveCodeByTableNameAndCodeTable(@RequestBody GenericCodeTableUpdate genericCodeTableUpdate,
																					 HttpServletRequest request) {
		GenericCodeTableController.logger.info(String.format(
				GenericCodeTableController.SAVE_CODE_BY_TABLE_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), genericCodeTableUpdate.getTableName(),
				genericCodeTableUpdate.getCodeTables().stream().map(CodeTable::getId).collect(Collectors.toList()),
				genericCodeTableUpdate.getCodeTables().stream().map(CodeTable::getDescription).collect(Collectors.toList())));
		String updateMessage = this.messageSource.getMessage(GenericCodeTableController.SAVE_SUCCESS_MESSAGE_KEY,
				null, GenericCodeTableController.DEFAULT_SAVE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(this.service.saveAll(genericCodeTableUpdate.getTableName(),
				genericCodeTableUpdate.getCodeTables()), updateMessage);
	}
}
