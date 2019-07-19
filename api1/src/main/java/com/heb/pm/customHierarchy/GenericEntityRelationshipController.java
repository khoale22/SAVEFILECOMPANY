package com.heb.pm.customHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller that returns all information related to generic entity relationship.
 *
 * @author m314029
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + GenericEntityRelationshipController.CUSTOM_HIERARCHY_ENTITY_RELATIONSHIP_URL)
@AuthorizedResource(ResourceConstants.CUSTOM_HIERARCHY_VIEW)
public class GenericEntityRelationshipController {

	private static final Logger logger = LoggerFactory.getLogger(GenericEntityRelationshipController.class);

	// url's used
	protected static final String CUSTOM_HIERARCHY_ENTITY_RELATIONSHIP_URL = "/customHierarchy/entityRelationship";
	private static final String FIND_BY_HIERARCHY_CONTEXT = "findByHierarchyContext";
	private static final String FIND_ALL_PARENTS_BY_CHILD = "findAllParentsByChild";
	private static final String FIND_ALL_CUSTOMER_PRODUCT_GROUPS_BY_PARENT = "findAllCustomerProductGroupsByParent";
	private static final String FIND_ALL_CUSTOMER_PRODUCT_GROUPS_NOT_ON_PARENT_ENTITY =
			"findAllCustomerProductGroupsNotOnParentEntity";
	private static final String GET_IMMEDIATE_NON_PRODUCT_CHILDREN =
			"getImmediateNonProductChildren";
	private static final String GET_RESOLVED_CURRENT_LEVEL_BY_KEY =
			"getResolvedCurrentLevelByKey";

	// logs
	private static final String FIND_BY_HIERARCHY_CONTEXT_AND_PARENT_ID_MESSAGE =
			"User %s from IP %s has requested entity relationship data for hierarchy context: %s with parent id: %d.";
	private static final String UPDATE_CURRENT_LEVEL_ATTRIBUTES =
			"User %s from IP %s has requested to update the entity relationship data for hierarchy context: %s, between parent id: %d, and child id: %d.";
	private static final String FIND_ALL_PARENTS_BY_CHILD_MESSAGE =
			"Finding all parents for the child: %d from hierarchy: %s";
	private static final String MOVE_REQUEST =
			"User %s from IP %s, has requested to move the entity relationship data for hierarchy context: %s, between parent id: %d, and child id: %d.";
	private static final String LINK_REQUEST =
			"User %s from IP %s, has requested to link the entity %d to %d other entities";
	private static final String GET_PRODUCTS =
			"User %s from IP %s, has requested all products linked to the entity %d in the %s hierarchy";
	private static final String FIND_ALL_CUSTOMER_PRODUCT_GROUPS_BY_PARENT_MESSAGE =
			"User %s from IP %s has requested all product groups attached to search criteria: %s";
	private static final String FIND_ALL_CUSTOMER_PRODUCT_GROUPS_NOT_ON_PARENT_MESSAGE =
			"User %s from IP %s has requested all product groups not already on an entity relationship where " +
					"parent entity id: %d, and hierarchy context: %s";
	private static final String GET_IMMEDIATE_NON_PRODUCT_CHILDREN_MESSAGE =
			"User %s from IP %s has requested all immediate non-product relationships from " +
					"entity relationship parent id: %d, and hierarchy context: %s";
	private static final String GET_RESOLVED_CURRENT_LEVEL_BY_KEY_MESSAGE =
			"User %s from IP %s has requested a fully resolved hierarchy level given generic entity relationship key: %s";

	// errors
	private static final String MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE_KEY = "GenericEntityRelationshipController.missingHierarchyContext";
	private static final String MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE = "Must have a hierarchy context to search for.";
	private static final String MISSING_PARENT_ENTITY_ID_ERROR_MESSAGE_KEY = "GenericEntityRelationshipController.missingParentEntityId";
	private static final String MISSING_PARENT_ENTITY_ID_ERROR_MESSAGE = "Must have a parent entity id to search for.";

	private static final String GET_SALES_CHANNELS=
			"User %s from IP %s, has requested all the sales channels for custom hierarchy";
	private static final String GET_PDP_TEMPLATES=
			"User %s from IP %s, has requested all the PDP templates for custom hierarchy";
	private static final String GET_FULFILLMENT_PROGRAMS=
			"User %s from IP %s, has requested all the fulfillment programs for custom hierarchy";

	@Autowired
	private GenericEntityRelationshipService genericEntityRelationshipService;

	@Autowired
	private CustomerHierarchyService customerHierarchyService;

	@Autowired private
	UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	private LazyObjectResolver<List<GenericEntityRelationship>> objectResolver = new GenericEntityRelationshipResolver();
	private LazyObjectResolver<Iterable<CustomHierarchyProductLevelInformation>> defaultParentRelationshipResolver = new DefaultParentRelationshipResolver();
	private LazyObjectResolver<Iterable<CustomerProductGroup>> customerProductGroupRelationshipResolver = new CustomerProductGroupRelationshipResolver();
	private LazyObjectResolver<Iterable<GenericEntityRelationship>> immediateNonProductChildrenResolver = new ImmediateNonProductChildrenResolver();

	/**
	 * Resolver fetches one level of child relationships of a given list of relationships.
	 */
	private class ImmediateNonProductChildrenResolver implements LazyObjectResolver<Iterable<GenericEntityRelationship>>{

		@Override
		public void fetch(Iterable<GenericEntityRelationship> relationships) {
			relationships.forEach(relationship -> {
				if (!relationship.isChildRelationshipOfProductEntityType()) {
					relationship.getChildRelationships();
					if(!relationship.getChildRelationships().isEmpty()){
						relationship.getChildRelationships().get(0).getKey().getChildEntityId();
					}
				}
			});
		}
	}

	/**
	 * Resolver fetches default parent relationship and product group type data for customer product groups or products.
	 */
	private class DefaultParentRelationshipResolver implements LazyObjectResolver<Iterable<CustomHierarchyProductLevelInformation>>{

		@Override
		public void fetch(Iterable<CustomHierarchyProductLevelInformation> productLevelInformation) {
			productLevelInformation.forEach(currentLevelInformation -> {
				if(currentLevelInformation.getDefaultParentRelationships() != null){
					currentLevelInformation.getDefaultParentRelationships().forEach(
							defaultParentRelationship -> {
								if(defaultParentRelationship.getParentDescription() != null){
									defaultParentRelationship.getParentDescription().getShortDescription();
								}
							});
				}
				if(currentLevelInformation.getCustomerProductGroup() != null){
					if(currentLevelInformation.getCustomerProductGroup().getProductGroupType() != null){
						currentLevelInformation.getCustomerProductGroup().getProductGroupType().getProductGroupTypeCode();
					}
				} else if(currentLevelInformation.getProductMaster() != null){
					if(currentLevelInformation.getProductMaster().getProductFullfilmentChanels() != null){
						currentLevelInformation.getProductMaster().
								getProductFullfilmentChanels().forEach(ProductFullfilmentChanel::getFulfillmentChannel);
					}
					if(currentLevelInformation.getProductMaster().getSubCommodity() != null){
						currentLevelInformation.getProductMaster().getSubCommodity().getName();
					}
				}
			});
		}
	}

	/**
	 * Resolver fetches customer product groups when attached to an entity relationship.
	 */
	private class CustomerProductGroupRelationshipResolver implements LazyObjectResolver<Iterable<CustomerProductGroup>>{

		@Override
		public void fetch(Iterable<CustomerProductGroup> customerProductGroups) {
			customerProductGroups.forEach(customerProductGroup -> {
				if(customerProductGroup.getProductGroupType() != null){
					customerProductGroup.getProductGroupType().getProductGroupType();
				}
			});
		}
	}

	/**
	 * Get all custom hierarchy list.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = FIND_BY_HIERARCHY_CONTEXT)
	public List<GenericEntityRelationship> findByHierarchyContext(
			@RequestBody HierarchyContext hierarchyContext, HttpServletRequest request){

		this.logFindByHierarchyContext(hierarchyContext, request.getRemoteAddr());
		List<GenericEntityRelationship> toReturn =
				this.genericEntityRelationshipService.findByHierarchyContext(hierarchyContext);
		return this.resolveResults(toReturn);
	}

	/**
	 * This method will update the current level attributes
	 * @param genericEntityRelationship
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "updateCurrentLevel")
	public void updateCurrentLevel(@RequestBody GenericEntityRelationship genericEntityRelationship, HttpServletRequest request) throws Exception{
		GenericEntityRelationshipController.logger.info(String.format(GenericEntityRelationshipController.UPDATE_CURRENT_LEVEL_ATTRIBUTES,
				this.userInfo.getUserId(), request.getRemoteAddr(), genericEntityRelationship.getKey().getHierarchyContext(),
				genericEntityRelationship.getKey().getParentEntityId(),	genericEntityRelationship.getKey().getChildEntityId()));
		genericEntityRelationshipService.updateCurrentLevelAttributes(genericEntityRelationship, this.userInfo.getUserId());
	}

	/**
	 * Finds all parents for a child.
	 * @param childId the child id being used to search for all parents
	 * @param hierarchyContext the hierarchy context in question
	 * @param request the http servlet request
	 * @return A list of relationships.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_PARENTS_BY_CHILD)
	public List<GenericEntityRelationship> findAllParentsByChild(
			@RequestParam Long childId,
			@RequestParam String hierarchyContext, HttpServletRequest request) {
		this.logFindAllParentsByChild(childId, hierarchyContext);
		return this.customerHierarchyService.getAllParentsOfChild(childId, hierarchyContext);
	}

	/**
	 * This method will take a list of requested changes the custom hierarchies and send them to the webservice for processing
	 * @param changes the list of proposed changes
	 * @param request the http servlet request
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "moveLevels")
	public ModifiedEntity<String> moveLevel(@RequestBody HierarchyChanges changes, HttpServletRequest request) throws Exception {
		GenericEntityRelationshipController.logger.info(String.format(MOVE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(),
				changes.getRelationshipsRemoved().get(0).getKey().getHierarchyContext(), changes.getRelationshipsRemoved().get(0).getKey().getParentEntityId(),
				changes.getRelationshipsRemoved().get(0).getKey().getChildEntityId()));
		String updateResult = this.genericEntityRelationshipService.moveLevelInCustomHierarchy(changes, this.userInfo.getUserId());
		return new ModifiedEntity<String>(updateResult, updateResult);
	}

	/**
	 * This method will take a list of requested changes the custom hierarchies and send them to the webservice for processing
	 * @param changes the list of proposed changes
	 * @param request the http servlet request
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "linkLevels")
	public ModifiedEntity<String> linkLevel(@RequestBody HierarchyChanges changes, HttpServletRequest request) throws Exception {
		GenericEntityRelationshipController.logger.info(String.format(LINK_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(),
				changes.getRelationshipsAdded().get(0).getKey().getChildEntityId(), changes.getRelationshipsAdded().size()));
		String updateResult = this.genericEntityRelationshipService.linkLevelsInCustomHierarchy(changes, this.userInfo.getUserId());
		return new ModifiedEntity<String>(updateResult, updateResult);
	}

	/**
	 * This api endpoint will grab a hierarchies products if they are present
	 * @param request the http servlet request
	 * @return list of products tied to the parent node.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = "findProductsByParent")
	public PageableResult<CustomHierarchyProductLevelInformation> findProductsByParent(@RequestBody CustomHierarchyParentSearchCriteria searchCriteria,
																					   HttpServletRequest request) throws Exception {
		GenericEntityRelationshipController.logger.info(
				String.format(GET_PRODUCTS, this.userInfo.getUserId(),
						request.getRemoteAddr(), searchCriteria.getHierarchyParentId(),
						searchCriteria.getHierarchyContext()));
		PageableResult<CustomHierarchyProductLevelInformation> toReturn =
				this.genericEntityRelationshipService.findProductsByParent(
						searchCriteria.getHierarchyContext(), searchCriteria.getHierarchyParentId(),
						searchCriteria.getPage(), searchCriteria.getPageSize(), searchCriteria.isFirstSearch());
		this.defaultParentRelationshipResolver.fetch(toReturn.getData());
		return toReturn;
	}

	/**
	 * Finds a page of product groups given a custom hierarchy parent search criteria.
	 *
	 * @param searchCriteria The search criteria containing page information and related customer product group information.
	 * @param request The http servlet request.
	 * @return A list of product groups.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = FIND_ALL_CUSTOMER_PRODUCT_GROUPS_BY_PARENT)
	public PageableResult<CustomHierarchyProductLevelInformation> findAllCustomerProductGroupsByParent(
			@RequestBody CustomHierarchyParentSearchCriteria searchCriteria, HttpServletRequest request) {
		this.logFindAllProductGroupsByParent(searchCriteria, request);
		PageableResult<CustomHierarchyProductLevelInformation> toReturn = this.genericEntityRelationshipService.findAllCustomerProductGroupsByParent(
				searchCriteria.getHierarchyContext(),
				searchCriteria.getHierarchyParentId(),
				searchCriteria.getPage(),
				searchCriteria.getPageSize(),
				searchCriteria.isFirstSearch());
		this.defaultParentRelationshipResolver.fetch(toReturn.getData());
		return toReturn;
	}

	/**
	 * Finds all customer product groups that are not already attached to the given hierarchy level parent id and
	 * hierarchy context.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param request The http servlet request.
	 * @return A list of product groups.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_CUSTOMER_PRODUCT_GROUPS_NOT_ON_PARENT_ENTITY)
	public List<CustomerProductGroup> findAllCustomerProductGroupsNotOnParentEntity(
			@RequestParam Long parentEntityId, @RequestParam String hierarchyContext, HttpServletRequest request) {
		// parent entity id is required.
		this.parameterValidator.validate(parentEntityId, GenericEntityRelationshipController.MISSING_PARENT_ENTITY_ID_ERROR_MESSAGE,
				GenericEntityRelationshipController.MISSING_PARENT_ENTITY_ID_ERROR_MESSAGE_KEY, request.getLocale());
		// hierarchy context is required.
		this.parameterValidator.validate(hierarchyContext, GenericEntityRelationshipController.MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE,
				GenericEntityRelationshipController.MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE_KEY, request.getLocale());
		this.logAllCustomerProductGroupsNotOnParentEntity(parentEntityId, hierarchyContext, request);

		List<CustomerProductGroup> toReturn = this.genericEntityRelationshipService.
				findAllCustomerProductGroupsNotOnParentEntity(
						parentEntityId, hierarchyContext);
		this.customerProductGroupRelationshipResolver.fetch(toReturn);
		return toReturn;
	}

	/**
	 * Log's a user's request to get a list of all product groups not already attached to the given hierarchy level
	 * parent id and hierarchy context.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param ip The IP address the user is logged in from.
	 */
	private void logAllCustomerProductGroupsNotOnParentEntity(Long parentEntityId, String hierarchyContext, HttpServletRequest ip) {
		GenericEntityRelationshipController.logger.info(
				String.format(GenericEntityRelationshipController.FIND_ALL_CUSTOMER_PRODUCT_GROUPS_NOT_ON_PARENT_MESSAGE,
						this.userInfo.getUserId(), ip.getRemoteAddr(), parentEntityId, hierarchyContext));
	}

	/**
	 * This method returns all of the possible sales channels used for creating product fulfillment programs
	 * @param request simple http request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllSalesChannels")
	public List<SalesChannel> getAllSalesChannels(HttpServletRequest request) {
		GenericEntityRelationshipController.logger.info(String.format(GET_SALES_CHANNELS, this.userInfo.getUserId(), request.getRemoteAddr()));
		return this.genericEntityRelationshipService.getAllSalesChannels();
	}

	/**
	 * This method returns all of the possible pdp templates that can be tied to a product as a dynamic attribute
	 * @param request simple http request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllPDPTemplates")
	public List<PDPTemplate> getAllPDPTemplates(HttpServletRequest request) {
		GenericEntityRelationshipController.logger.info(String.format(GET_PDP_TEMPLATES, this.userInfo.getUserId(), request.getRemoteAddr()));
		return this.genericEntityRelationshipService.getAllPDPTemplates();
	}

	/**
	 * This method returns all of the possible fulfillment programs possible to create a product fulfillment program
	 * @param request simple http request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllFulfillmentPrograms")
	public List<FulfillmentChannel> getAllFulfillmentPrograms(HttpServletRequest request) {
		GenericEntityRelationshipController.logger.info(String.format(GET_FULFILLMENT_PROGRAMS, this.userInfo.getUserId(), request.getRemoteAddr()));
		return this.genericEntityRelationshipService.getAllFulfillmentPrograms();
	}


	/**
	 * Log's a user's request to get a page of product groups given a parent entity id, and hierarchy context.
	 *
	 * @param searchCriteria The search criteria containing page information and related customer product group information.
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindAllProductGroupsByParent(CustomHierarchyParentSearchCriteria searchCriteria, HttpServletRequest ip) {
		GenericEntityRelationshipController.logger.info(
				String.format(GenericEntityRelationshipController.FIND_ALL_CUSTOMER_PRODUCT_GROUPS_BY_PARENT_MESSAGE,
						this.userInfo.getUserId(), ip.getRemoteAddr(), searchCriteria));
	}

	/**
	 * This method returns a generic entity relationship given a key. It will resolve the immediate non product
	 * children if there are any.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContextId The hierarchy context to look for.
	 * @param request The http request.
	 * @return Resolved generic entity relationship with the given key.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_IMMEDIATE_NON_PRODUCT_CHILDREN)
	public List<GenericEntityRelationship> getHierarchyRelationshipsOfHierarchyLevel(
			@RequestParam Long parentEntityId, @RequestParam String hierarchyContextId, HttpServletRequest request) {
		// parent entity id is required.
		this.parameterValidator.validate(parentEntityId,
				GenericEntityRelationshipController.MISSING_PARENT_ENTITY_ID_ERROR_MESSAGE,
				GenericEntityRelationshipController.MISSING_PARENT_ENTITY_ID_ERROR_MESSAGE_KEY, request.getLocale());
		// hierarchy context is required.
		this.parameterValidator.validate(hierarchyContextId,
				GenericEntityRelationshipController.MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE,
				GenericEntityRelationshipController.MISSING_HIERARCHY_CONTEXT_ERROR_MESSAGE_KEY, request.getLocale());
		GenericEntityRelationshipController.logger.info(
				String.format(
						GET_IMMEDIATE_NON_PRODUCT_CHILDREN_MESSAGE,
						this.userInfo.getUserId(),
						request.getRemoteAddr(),
						parentEntityId,
						hierarchyContextId));
		List<GenericEntityRelationship> nonProductChildren =
				this.genericEntityRelationshipService.findNonProductChildren(parentEntityId, hierarchyContextId);
		this.immediateNonProductChildrenResolver.fetch(nonProductChildren);
		nonProductChildren.sort(GenericEntityRelationship::compareTo);
		return nonProductChildren;
	}

	/**
	 * This method returns a generic entity relationship given a key. It will resolve the non-product children if any.
	 *
	 * @param key The generic entity relationship key.
	 * @param request The http request.
	 * @return Resolved generic entity relationship with the given key.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = GET_RESOLVED_CURRENT_LEVEL_BY_KEY)
	public GenericEntityRelationship getResolvedCurrentLevelByKey(
			@RequestBody GenericEntityRelationshipKey key, HttpServletRequest request) {
		GenericEntityRelationshipController.logger.info(
				String.format(
						GET_RESOLVED_CURRENT_LEVEL_BY_KEY_MESSAGE,
						this.userInfo.getUserId(),
						request.getRemoteAddr(),
						key));
		GenericEntityRelationship currentLevel =
				this.genericEntityRelationshipService.findByKey(key);
		if (!currentLevel.isChildRelationshipOfProductEntityType()) {
			this.objectResolver.fetch(currentLevel.getChildRelationships());
		}
		return currentLevel;
	}

	/**
	 * Logs the request to find all parents by child.
	 * @param childId the child id being used to search for all parents
	 * @param hierarchyContext the hierarchy context in question
	 */
	private void logFindAllParentsByChild(Long childId, String hierarchyContext) {
		GenericEntityRelationshipController.logger.info(
				String.format(GenericEntityRelationshipController.FIND_ALL_PARENTS_BY_CHILD_MESSAGE,
						childId, hierarchyContext));
	}


	/**
	 * Logs a user's request to get generic entity relationship records by hierarchy context and parent id.
	 *
	 * @param hierarchyContext The hierarchy context to search for.
	 * @param ip The IP address th user is logged in from.
	 */
	private void logFindByHierarchyContext(HierarchyContext hierarchyContext, String ip) {
		GenericEntityRelationshipController.logger.info(
				String.format(GenericEntityRelationshipController.FIND_BY_HIERARCHY_CONTEXT_AND_PARENT_ID_MESSAGE,
						this.userInfo.getUserId(), ip, hierarchyContext.getId(), hierarchyContext.getParentEntityId()));
	}

	/**
	 * Resolves list passed in and loads the lazily loaded objects needed by the generic entity relationship functions
	 * of the REST endpoint.
	 *
	 * @param results The list to load data for.
	 * @return The list with its data resolved.
	 */
	private List<GenericEntityRelationship> resolveResults(List<GenericEntityRelationship> results) {
		this.objectResolver.fetch(results);
		return results;
	}
}
