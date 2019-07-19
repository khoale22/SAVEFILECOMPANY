package com.heb.pm.customHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Attribute;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Rest Endpoint for Custom Hierarchy Attributes.
 * @author o792389
 * @since 2.18.4
 */

@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CustomHierarchyAttributeController.ROOT_URL)
@AuthorizedResource(ResourceConstants.HIERARCHY_ATTRIBUTE_MAINTENANCE)
public class CustomHierarchyAttributeController {

	private static final Logger logger = LoggerFactory.getLogger(CustomHierarchyAttributeController.class);
	protected static final String ROOT_URL = "/customHierarchy/attribute";
	private static final String FIND_BY_HIERARCHY_AND_ENTITY = "findAttributesByHierarchyContextAndEntity";
	// errors
	private static final String MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE_KEY = "CustomHierarchyAttributeController.missingHierarchyContext";
	private static final String MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE = "Must have a hierarchy context to search for.";
	private static final String MISSING_ENTITY_ID_ERROR_MESSAGE_KEY = "CustomHierarchyAttributeController.missingEntityId";
	private static final String MISSING_ENTITY_ID_ERROR_MESSAGE = "Must have a entity id to search for.";


	//Logs
	private static final String FIND_BY_HIERARCHY_AND_ENTITY_MESSAGE =
			"User %s from IP %s has requested all attributes where entity id: %d, and hierarchy context: %s.";

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private CustomerHierarchyService customerHierarchyService;

	/**
	 * Find all Attributes for a given Hierarchy context and Entity ID
	 *
	 * @param entityId The entity id being used to search for
	 * @param hierarchyContextId The heirarchyContextid to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of Attributes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CustomHierarchyAttributeController.FIND_BY_HIERARCHY_AND_ENTITY)
	public List<Attribute> findByHierarchyAndEntity(
			@RequestParam Long entityId, @RequestParam String hierarchyContextId, HttpServletRequest request) {
		// entity id is required.
		this.parameterValidator.validate(entityId, CustomHierarchyAttributeController.MISSING_ENTITY_ID_ERROR_MESSAGE,
				CustomHierarchyAttributeController.MISSING_ENTITY_ID_ERROR_MESSAGE_KEY, request.getLocale());
		// hierarchy context is required.
		this.parameterValidator.validate(hierarchyContextId, CustomHierarchyAttributeController.MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE,
				CustomHierarchyAttributeController.MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE_KEY, request.getLocale());
		this.logFindByHierarchyAndEntity(entityId, hierarchyContextId, request);
		return this.customerHierarchyService.findByHierarchyAndEntity(entityId,
				hierarchyContextId);
	}

	/**
	 * Log's a user's request to get a list of attributes tied to an entity id and hierarchy
	 * context ID.
	 * @param entityId The entity id to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindByHierarchyAndEntity(Long entityId, String hierarchyContext, HttpServletRequest ip) {
		CustomHierarchyAttributeController.logger.info(
				String.format(CustomHierarchyAttributeController.FIND_BY_HIERARCHY_AND_ENTITY_MESSAGE,
						this.userInfo.getUserId(), ip.getRemoteAddr(), entityId, hierarchyContext));
	}
}
