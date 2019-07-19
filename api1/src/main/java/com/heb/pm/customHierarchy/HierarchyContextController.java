package com.heb.pm.customHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.HierarchyContext;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * REST controller that returns all information related to hierarchy context.
 *
 * @author m314029
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + HierarchyContextController.CUSTOM_HIERARCHY_HIERARCHY_CONTEXT_URL)
@AuthorizedResource(ResourceConstants.CUSTOM_HIERARCHY_VIEW)
public class HierarchyContextController {

	private static final Logger logger = LoggerFactory.getLogger(HierarchyContextController.class);

	// url's used
	protected static final String CUSTOM_HIERARCHY_HIERARCHY_CONTEXT_URL = "/customHierarchy/hierarchyContext";
	private static final String FIND_ALL = "findAll";
	private static final String FIND_CUSTOMER_HIERARCHY_CONTEXT = "customerHierarchyContext";
	private static final String GET_HIERARCHY_BY_STRING_SEARCH = "getCustomHierarchyBySearch";
	private static final String GET_HIERARCHY_BY_CHILD_ID = "getCustomHierarchyByChild";
	private static final String UPDATE_CUSTOM_HIERARCHY = "updateCustomHierarchy";

	// logs
	private static final String FIND_ALL_MESSAGE =
			"User %s from IP %s has requested all hierarchy context data.";
	private static final String HIERARCHY_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for custom hierarchy relationships with the pattern '%s'";
	private static final String HIERARCHY_CHILD_ID_LOG_MESSAGE =
			"User %s from IP %s searched for custom hierarchy relationships with the child id '%s'";
	private static final String CUSTOMER_HIERARCHY_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for custom hierarchy and child relationships";
	// error property file keys
	private static final String NO_SEARCH_STRING_ERROR_KEY = "HierarchyContextController.missingSearchString";
	private static final String NO_CHILD_ID_ERROR_KEY = "HierarchyContextController.missingChildId";
	// error messages
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";
	private static final String NO_CHILD_ID_DEFAULT_ERROR_MESSAGE = "Must have a child Id to base on.";
	// Success message
	private static final String SUCCESS_REMOVE_LEVEL = "Level successfully removed";
	private static final String SUCCESS_ADD_LEVEL = "Level successfully added";

	@Autowired
	private HierarchyContextService hierarchyContextService;

	@Autowired private
	UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	private LazyObjectResolver<List<GenericEntityRelationship>> objectResolver = new GenericEntityRelationshipResolver();

	/**
	 * This class holds all of the basic information for changing a level in the custom hierarchy
	 */
	public static class CustomHierarchyValues {
		public String newHierarchyName;

		public String newHierarchyContext;

		/**
		 * Get the new name for a hierarchy level
		 * @return
		 */
		public String getNewHierarchyName() {
			return newHierarchyName;
		}

		/**
		 * updates the name
		 * @param newHierarchyName
		 */
		public void setNewHierarchyName(String newHierarchyName) {
			this.newHierarchyName = newHierarchyName;
		}

		/**
		 * gets the hierarchy that is to be changed
		 * @return
		 */
		public String getNewHierarchyContext() {
			return newHierarchyContext;
		}

		/**
		 * updates the hierarchy
		 * @param newHierarchyContext
		 */
		public void setNewHierarchyContext(String newHierarchyContext) {
			this.newHierarchyContext = newHierarchyContext;
		}

		/**
		 * This class holds all of the basic information for a level in the hierarchy
		 */
		public CustomHierarchyValues(){}
	}

	/**
	 * This class holds all of the basic information for creating a new level in the custom hierarchy
	 */
	public static class HierarchyValues {
		public String newDescription;

		public Boolean newActiveSwitch;

		public LocalDate newEffectiveDate;

		public LocalDate newEndDate;

		public String parentEntityId;

		public GenericEntityRelationship currentLevel;

		public HierarchyContext hierarchyContext;

		/**
		 * Returns the hierarchy that the level belongs to
		 * @return
		 */
		public HierarchyContext getHierarchyContext() {
			return hierarchyContext;
		}

		/**
		 * updates the hierarchy
		 * @param hierarchyContext
		 */
		public void setHierarchyContext(HierarchyContext hierarchyContext) {
			this.hierarchyContext = hierarchyContext;
		}

		/**
		 * The upper node(parent) for the relationship in the hierarchy
		 * @return
		 */
		public String getParentEntityId() {
			return parentEntityId;
		}

		/**
		 * updates the parentId
		 * @param parentEntityId
		 */
		public void setParentEntityId(String parentEntityId) {
			this.parentEntityId = parentEntityId;
		}

		/**
		 * The database representation of an edge in a hierarchy
		 * @return
		 */
		public GenericEntityRelationship getCurrentLevel() {
			return currentLevel;
		}

		/**
		 * updates the currentLevel
		 * @param currentLevel
		 */
		public void setCurrentLevel(GenericEntityRelationship currentLevel) {
			this.currentLevel = currentLevel;
		}

		/**
		 * The descripiton of the level in the hierarchy
		 * @return
		 */
		public String getNewDescription() {
			return newDescription;
		}

		/**
		 * updates the description
		 * @param newDescription
		 */
		public void setNewDescription(String newDescription) {
			this.newDescription = newDescription;
		}

		/**
		 * This switch states if this current level is active online
		 * @return
		 */
		public Boolean getNewActiveSwitch() {
			return newActiveSwitch;
		}

		/**
		 * updates the active switch
		 * @param newActiveSwitch
		 */
		public void setNewActiveSwitch(Boolean newActiveSwitch) {
			this.newActiveSwitch = newActiveSwitch;
		}

		/**
		 * Sets the date that the hierarchy level is active
		 * @return
		 */
		public LocalDate getNewEffectiveDate() {
			return newEffectiveDate;
		}

		/**
		 * updates the effective date
		 * @param newEffectiveDate
		 */
		public void setNewEffectiveDate(LocalDate newEffectiveDate) {
			this.newEffectiveDate = newEffectiveDate;
		}

		/**
		 * Sets the date that the new level is no longer active
		 * @return
		 */
		public LocalDate getNewEndDate() {
			return newEndDate;
		}

		/**
		 * updates teh end date
		 * @param newEndDate
		 */
		public void setNewEndDate(LocalDate newEndDate) {
			this.newEndDate = newEndDate;
		}
	}

	/**
	 * Get all custom hierarchy list.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL)
	public List<HierarchyContext> getFullHierarchy(HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr());
		List<HierarchyContext> toReturn = this.hierarchyContextService.findAll();
		return toReturn;
	}
	/**
	 * Find customer hierarchy context.
	 *
	 * @param request the request
	 * @return HierarchyContext object
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = FIND_CUSTOMER_HIERARCHY_CONTEXT)
	public HierarchyContext findCustomerHierarchyContext(HttpServletRequest request){
		HierarchyContextController.logger.info(
				String.format(HierarchyContextController.CUSTOMER_HIERARCHY_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr()));
		HierarchyContext hierarchyContext = this.hierarchyContextService.findCustomerHierarchyContext();
		this.objectResolver.fetch(hierarchyContext.getChildRelationships());
		return hierarchyContext;
	}
	/**
	 * Update custom hierarchy.  Get the new Hierarchy name and Context to update table.
	 *
	 * @param customHierarchy the custom hierarchy
	 * @param request         the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = UPDATE_CUSTOM_HIERARCHY)
	public List<HierarchyContext> updateCustomHierarchy(@RequestBody CustomHierarchyValues customHierarchy, HttpServletRequest request) {

		this.hierarchyContextService.updateCustomHierarchy(customHierarchy, userInfo);

		return this.hierarchyContextService.findAll();
	}

	/**
	 *
	 * Adds a new level to a customer Hierarchy
	 * @param hierarchyValues the new attributes for the level
	 * @param request a simply http request
	 * @return if successfull, Generic Entity Relationship of the added level and a success message otherwise the cause of the failure
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "addCustomHierarchy")
	public  ModifiedEntity<GenericEntityRelationship> addCustomHierarchy(@RequestBody HierarchyValues hierarchyValues, HttpServletRequest request) {

		GenericEntityRelationship toReturn = this.hierarchyContextService.addCustomHierarchy(hierarchyValues, this.userInfo.getUserId());
		return new ModifiedEntity<>(toReturn, SUCCESS_ADD_LEVEL);

	}

	/**
	 * This method will attempt to remove a level from a custom hierarchy
	 * @param genericEntityRelationship the level to be removed
	 * @param request a simple http request
	 * @return if successfull a success message otherwise the cause of the failure
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "saveRemoveLevel")
	public ModifiedEntity<String> saveRemoveLevel(@RequestBody GenericEntityRelationship genericEntityRelationship, HttpServletRequest request){
		this.hierarchyContextService.saveRemoveLevel(genericEntityRelationship, this.userInfo.getUserId());
		return new ModifiedEntity<>(SUCCESS_REMOVE_LEVEL, SUCCESS_REMOVE_LEVEL);
	}

	/**
	 * Get all levels of a custom hierarchy by search string.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_HIERARCHY_BY_STRING_SEARCH)
	public List<HierarchyContext> getCustomHierarchyBySearch(
			@RequestParam("searchString") String searchString,
			@RequestParam("hierarchyContextId") String hierarchyContextId, HttpServletRequest request){

		// Search string is required.
		this.parameterValidator.validate(searchString, HierarchyContextController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				HierarchyContextController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());
		this.logGetCustomHierarchyBySearch(request.getRemoteAddr(), searchString);

		List<HierarchyContext> hierarchyContexts = this.hierarchyContextService.findById(hierarchyContextId);

		this.resolveResults(hierarchyContexts);

		// send the service the search string already trimmed and toUpperCase so it only needs to be done once
		return this.hierarchyContextService.
				findHierarchyRelationshipsByRegularExpression(searchString.trim().toUpperCase(), hierarchyContexts);
	}

	/**
	 * Get all levels of a custom hierarchy by the child's entity id.
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_HIERARCHY_BY_CHILD_ID)
	public HierarchyContext getCustomHierarchyByChild(
			@RequestParam("childId") Long childId,
			@RequestParam("hierarchyContextId") String hierarchyContextId, HttpServletRequest request){

		// Search string is required.
		this.parameterValidator.validate(childId, HierarchyContextController.NO_CHILD_ID_DEFAULT_ERROR_MESSAGE,
				HierarchyContextController.NO_CHILD_ID_ERROR_KEY, request.getLocale());
		this.logGetCustomHierarchyByChild(request.getRemoteAddr(), childId);
		HierarchyContext hierarchyContext = this.hierarchyContextService.findOneById(hierarchyContextId);
		this.objectResolver.fetch(hierarchyContext.getChildRelationships());
		//Finds links from child to parent
		List<GenericEntityRelationship> pathToTop = this.hierarchyContextService.findAllParentsToChild(childId, hierarchyContext.getParentEntityId(), hierarchyContextId);
		//This method will set all the isCollapse
		hierarchyContext = this.hierarchyContextService.openPath(hierarchyContext, pathToTop);
		return hierarchyContext;
	}


	/**
	 * Logs a user's request to get all custom hierarchy records.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logFindAll(String ip) {
		HierarchyContextController.logger.info(
				String.format(HierarchyContextController.FIND_ALL_MESSAGE,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs a user's request to get custom hierarchy records by search string.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetCustomHierarchyBySearch(String ip, String searchString) {
		HierarchyContextController.logger.info(
				String.format(HierarchyContextController.HIERARCHY_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString));
	}

	/**
	 * Logs a user's request to get custom hierarchy records by the child's id.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetCustomHierarchyByChild(String ip, Long childId) {
		HierarchyContextController.logger.info(
				String.format(HierarchyContextController.HIERARCHY_CHILD_ID_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, childId));
	}

	/**
	 * Resolves list passed in and loads the lazily loaded objects needed by the generic entity relationships of the
	 * hierarchy contexts of the REST endpoint.
	 *
	 * @param results The list to load data for.
	 */
	private void resolveResults(List<HierarchyContext> results) {
		results.forEach(hierarchyContext -> this.objectResolver.fetch(hierarchyContext.getChildRelationships()));
	}
}
