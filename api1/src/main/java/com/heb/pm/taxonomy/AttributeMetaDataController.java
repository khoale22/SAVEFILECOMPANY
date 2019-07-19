package com.heb.pm.taxonomy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.AttributeMetaData;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Rest endpoint for meta data.
 *
 * @author m314029
 * @since 2.21.0
 */
@RestController()
@RequestMapping(AttributeMetaDataController.ROOT_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_METADATA)
public class AttributeMetaDataController {

	private static final Logger logger = LoggerFactory.getLogger(AttributeMetaDataController.class);

	// urls
	static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/attribute/metaData";
	// Keys to user facing messages in the message resource bundle.
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="AttributeMetaDataController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="AttributeMetaData updated successfully.";

	// logs
	private static final String FIND_ALL_BY_NAME_MESSAGE = "User %s from IP %s requested to find attribute meta data " +
			"with name containing: %s, is standard: %s, includeCounts:%B, page:%d, pageSize:%d.";
	private static final String SAVE_METADATA_MESSAGE = "User %s from IP %s requested to save attribute meta data with" +
			" table:%s and field:%s.";
	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AttributeMetaDataService service;

	/**
	 * Finds all generic code tables matching the given query, with
	 * Optional parameters:
	 *
	 * @param name Name to search for containing text.
	 * @param customerFacing Customer facing to search for.
	 * @param global Global to search for.
	 * @param attributeStateCode Attribute state code to search for.
	 * @param hasStandardCodeTable Standard code table to search for.
	 * @param firstSearch Identifies first fetch where count query will also be ran for pagination.
	 * @param page Page requested.
	 * @param pageSize Number of records per page.
	 *
	 * Required parameters:
	 * @param request The HTTP request that initiated this call.
	 * @return All generic code tables matching the request.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public PageableResult<AttributeMetaData> findAll(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "customerFacing", required = false) Boolean customerFacing,
			@RequestParam(value = "global", required = false) Boolean global,
			@RequestParam(value = "attributeStateCode", required = false) String attributeStateCode,
			@RequestParam(value = "hasStandardCodeTable", required = false) Boolean hasStandardCodeTable,
			@RequestParam(value = "firstSearch", required = false) Boolean firstSearch,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		AttributeMetaDataController.logger.info(String.format(
				AttributeMetaDataController.FIND_ALL_BY_NAME_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), name, hasStandardCodeTable, firstSearch, page, pageSize));
		return service.findAll(
				name, customerFacing, global, attributeStateCode, hasStandardCodeTable, firstSearch, page, pageSize);
	}

	/**
	 * Saves Attribute Metadata.
	 *
	 * @param attributeMetaData the attributeMetaData to be saved.
	 * @param request The HTTP request that initiated this call.
	 * @return the saved Attribute Metadata.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value="/saveAttributeMetadata")
	public ModifiedEntity<AttributeMetaData> saveAttributeMetadata(@RequestBody AttributeMetaData attributeMetaData, HttpServletRequest request) {
		AttributeMetaDataController.logger.info(String.format(
				AttributeMetaDataController.SAVE_METADATA_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), attributeMetaData.getKey().getTable(), attributeMetaData.getKey().getField()));
		AttributeMetaData savedAttribute =  this.service.saveAttributeMetadata(attributeMetaData);
		String updateMessage = this.messageSource.getMessage(AttributeMetaDataController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{},
				AttributeMetaDataController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(savedAttribute, updateMessage);
	}
}
