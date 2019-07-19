package com.heb.pm.customHierarchy;

import com.heb.pm.entity.*;
import com.heb.pm.productGroup.ProductGroupSearchService;
import com.heb.pm.productSearch.ProductSearchService;
import com.heb.pm.repository.*;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.util.jpa.PageableResult;
import com.heb.util.ws.SoapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to generic entity relationship.
 *
 * @author m314029
 * @since 2.12.0
 */
@Service
public class GenericEntityRelationshipService {

	private static final String FAILED_UPDATE_MESSAGE = "Update Failed to complete";
	private static final String SUCCESS_MOVE_MESSAGE = "Hierarchy move of '%s' to '%s' was successful.";
	private static final String SUCCESS_LINK_MESSAGE = "Hierarchy link of '%s' to '%s' was successful.";
	private static final String FAILED_HIERARCHY_CHANGE_MESSAGE="The change that was requested was not able to be completed";
	private static final String FAILED_REMOVE_PRODUCT_CHILD_MESSAGE = "Please assign another primary path for %s PROD/PRGPs.";
	private static final String FAILED_REMOVE_PRODUCT_CHILD_AND_PARENT_MESSAGE = " Also, please assign another primary path for %s PROD/PRGPs.";
	private static final String FAILED_REMOVE_PARENT_MESSAGE = "The selected level has %s other parents; cannot remove.";
	private static final String FAILED_REMOVE_CHILDREN_MESSAGE = "The selected level has %s children; cannot remove";
	private static final String FAILED_REMOVE_CHILDREN_AND_PARENT_MESSAGE = " Also, the selected level has %s children; cannot remove";

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;

	@Autowired
	private ProductGroupSearchService productGroupSearchService;

	@Autowired
	private ProductSearchService productSearchService;

	@Autowired
	private MasterDataExtensionAttributeRepository masterDataExtensionAttributeRepository;

	@Autowired
	private GenericEntityRelationshipRepositoryWithCount genericEntityRelationshipRepositoryWithCount;

	@Autowired
	private SalesChannelRepository salesChannelRepository;

	@Autowired
	private PDPTemplateRepository pdpTemplateRepository;

	@Autowired
	private FulfillmentChannelRepository  fulfillmentChannelRepository;

	/**
	 * This method returns all generic entity relationships matching a given hierarchy context's context and parent id.
	 *
	 * @param hierarchyContext
	 * @return
	 */
	public List<GenericEntityRelationship> findByHierarchyContext(HierarchyContext hierarchyContext) {
		return this.genericEntityRelationshipRepository.
				findByKeyParentEntityIdAndHierarchyContext(hierarchyContext.getParentEntityId(), hierarchyContext.getId());
	}

	/**
	 * This method will attempt to update a generic entity relationship's attribute
	 * @param entityRelationship the changes requested to be made
	 * @param user the user making the change
	 * @throws Exception
	 */
	public void updateCurrentLevelAttributes(GenericEntityRelationship entityRelationship, String user) throws Exception{
		try{
			codeTableManagementServiceClient.updateCurrentLevelAttributes(entityRelationship, user);
			List<GenericEntityRelationship> entityList = this.genericEntityRelationshipRepository.findByKeyChildEntityIdAndHierarchyContextAndDefaultParent(entityRelationship.getKey().getChildEntityId(), entityRelationship.getKey().getHierarchyContext(), true);
			for(GenericEntityRelationship entity : entityList){
				if(!entityRelationship.getKey().equals(entity.getKey())){
					GenericEntityRelationship newEntity = new GenericEntityRelationship();
					newEntity.setDefaultParent(false);
					newEntity.setKey(entity.getKey());
					codeTableManagementServiceClient.updateCurrentLevelAttributes(newEntity, user);
				}
			}

		} catch (SoapException e){
			throw new Exception(FAILED_UPDATE_MESSAGE);
		}
	}

	/**
	 * Sends a request to add a new level to a customer Hierarchy to the Tibco webservice client
	 * creates Generic Entity Relationship of the added level to send back to the front end to be traced in the path
	 * @param hierarchyValues the attributes for the new level
	 * @param user the user requesting to make the change
	 */
	public GenericEntityRelationship addCustomHierarchy(HierarchyContextController.HierarchyValues hierarchyValues, String user) {

		String addActionCode = "A";

		Long childEntity =
				codeTableManagementServiceClient.addCustomHierarchy(hierarchyValues.getCurrentLevel(), hierarchyValues, addActionCode, user);

		GenericEntityRelationshipKey key = new GenericEntityRelationshipKey();
		if (hierarchyValues.getCurrentLevel().getKey() != null){
			key.setParentEntityId(hierarchyValues.getCurrentLevel().getKey().getChildEntityId());
		} else {
			key.setParentEntityId(hierarchyValues.getHierarchyContext().getParentEntityId());
		}
		key.setHierarchyContext(hierarchyValues.getHierarchyContext().getId());
		key.setChildEntityId(childEntity);

		GenericEntityRelationship toReturn = new GenericEntityRelationship();
		toReturn.setKey(key);
		return toReturn;
	}

	/**
	 * This method will take requested changes the custom hierarchies and send them to the webservice
	 * either adding a level or removing a level
	 * @param changes the list of proposed changes
	 * @return success or failure message
	 */
	public String moveLevelInCustomHierarchy(HierarchyChanges changes, String user) throws Exception{
		try{
			this.codeTableManagementServiceClient.customHierarchyGenericEntityRelationshipMoveLevel(changes, user);
			return String.format(
					SUCCESS_MOVE_MESSAGE,
					changes.getRelationshipsRemoved().get(0).getChildDescription().getShortDescription(),
					changes.getRelationshipsAdded().get(0).getParentDescription().getShortDescription());
		} catch (SoapException e){
			throw new Exception(FAILED_UPDATE_MESSAGE);
		}
	}

	/**
	 * This method will take a list of requested changes the custom hierarchies and send them to the webservice
	 * either adding a level or removing a level
	 * @param changes the list of proposed changes
	 * @return success or failure message
	 */
	public String linkLevelsInCustomHierarchy(HierarchyChanges changes, String user) throws Exception {
		boolean makeParent = false;
		try{
			GenericEntityRelationship defaultParentIfPresent = this.genericEntityRelationshipRepository.findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(
					changes.getRelationshipsAdded().get(0).getKey().getChildEntityId(),
					changes.getRelationshipsAdded().get(0).getHierarchyContext(), true);
			if(defaultParentIfPresent == null){
				makeParent = true;
			}
			this.codeTableManagementServiceClient.customHierarchyGenericEntityRelationshipAdditions(changes.getRelationshipsAdded(), user, makeParent);
			return String.format(
					SUCCESS_LINK_MESSAGE,
					changes.getRelationshipsAdded().get(0).getChildDescription().getShortDescription(),
					changes.getRelationshipsAdded().get(0).getParentDescription().getShortDescription());
		} catch (SoapException e){
			throw new Exception(FAILED_HIERARCHY_CHANGE_MESSAGE);
		}
	}

	/**
	 * This method will send the request to the TIBCO webservice client for updating the database
	 * @param genericEntityRelationship the relationship to be removed
	 * @param userId the user making the request
	 */
	public void saveRemoveLevel(GenericEntityRelationship genericEntityRelationship, String userId) {
		String removeActionCode = "D";
		List<GenericEntityRelationship> parents =
				this.genericEntityRelationshipRepository.findByKeyChildEntityIdAndHierarchyContext(genericEntityRelationship.getKey().getChildEntityId(), genericEntityRelationship.getKey().getHierarchyContext());
		List<GenericEntityRelationship> children =
				this.genericEntityRelationshipRepository.findByKeyParentEntityIdAndHierarchyContext(genericEntityRelationship.getKey().getChildEntityId(), genericEntityRelationship.getKey().getHierarchyContext());
		int defaultParentOfProductsCount = 0;

		for (GenericEntityRelationship child: children) {
			if(child.getDefaultParent()){
				if(isEntityOfProductOrProductGroupType(child.getGenericChildEntity().getType())){
					defaultParentOfProductsCount++;
				}
			}
		}

		if(!genericEntityRelationship.getDefaultParent()){ // remove !primary parent node
			// if node's children have other parent(s), so children data won't be lost {EX: linked node}
			if(parents.size() > 1) {
				codeTableManagementServiceClient.saveRemoveLevel(genericEntityRelationship, removeActionCode, userId);
			}else{
				throw new IllegalArgumentException(String.format(FAILED_REMOVE_CHILDREN_MESSAGE, children.size()));
			}
		}else if(parents.size() > 1 || children.size() > 0){ // is primary parent, now check children and other parents
			StringBuffer invalidRemoveLevelErrorMessage = new StringBuffer();
			Boolean hasParents = false;
			if(parents.size() > 1){ // if has other parents
				invalidRemoveLevelErrorMessage.append(String.format(FAILED_REMOVE_PARENT_MESSAGE, parents.size()-1));
				hasParents = true;
			}
			if (defaultParentOfProductsCount > 0){ // products and product groups
				invalidRemoveLevelErrorMessage =
						hasParents ? invalidRemoveLevelErrorMessage.append(FAILED_REMOVE_PRODUCT_CHILD_AND_PARENT_MESSAGE) : invalidRemoveLevelErrorMessage.append(FAILED_REMOVE_PRODUCT_CHILD_MESSAGE);
				throw new IllegalArgumentException(String.format(invalidRemoveLevelErrorMessage.toString(),defaultParentOfProductsCount));
			}else if (children.size() > 0){ // has just children
				invalidRemoveLevelErrorMessage =
						hasParents ? invalidRemoveLevelErrorMessage.append(FAILED_REMOVE_CHILDREN_AND_PARENT_MESSAGE) : invalidRemoveLevelErrorMessage.append(FAILED_REMOVE_CHILDREN_MESSAGE);
				throw new IllegalArgumentException(String.format(invalidRemoveLevelErrorMessage.toString(), children.size()));
			}else{ // just other parents message
				throw new IllegalArgumentException(String.format(invalidRemoveLevelErrorMessage.toString()));
			}
		}else{ // remove Primary parent w/o other parent and children
			codeTableManagementServiceClient.saveRemoveLevel(genericEntityRelationship, removeActionCode, userId);
		}
	}

	/**
	 * Determine if a entity type is a product or product group
	 * @param entityType
	 * @return Boolean value based on whether entity type is a product / product group or not
	 */
	private boolean isEntityOfProductOrProductGroupType(String entityType){
		String trimmedEntityType = entityType.trim();
		if(GenericEntity.EntyType.PROD.getName().equals(trimmedEntityType) ||
				GenericEntity.EntyType.PGRP.getName().equals(trimmedEntityType)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Finds all products sharing the same parent hierarchy level.
	 *
	 * @param hierarchyContext Hierarchy context of parent hierarchy level.
	 * @param hierarchyParentId Hierarchy parent id of parent hierarchy level.
	 * @param page Page to search for.
	 * @param pageSize Page size to look for.
	 * @param firstSearch Whether this is the first search or not.
	 * @return Pageable result of custom hierarchy product level information.
	 */
	public PageableResult<CustomHierarchyProductLevelInformation> findProductsByParent(
			String hierarchyContext, Long hierarchyParentId, int page, int pageSize, boolean firstSearch) {
		Pageable pageRequest = new PageRequest(page, pageSize);
		// get the list of children relationships matching the given parent hierarchy level information.
		PageableResult<GenericEntityRelationship> tempPage;
		if(firstSearch){
			tempPage = this.findGenericEntityRelationshipsByParentAndContextWithCount(
					hierarchyContext, hierarchyParentId, GenericEntity.EntyType.PROD, pageRequest);
		} else {
			tempPage = this.findGenericEntityRelationshipsByParentAndContextWithoutCount(
					hierarchyContext, hierarchyParentId, GenericEntity.EntyType.PROD, pageRequest);
		}
		List<CustomHierarchyProductLevelInformation> currentData = new ArrayList<>();
		tempPage.getData().forEach(genericEntityRelationship -> {
			CustomHierarchyProductLevelInformation temp = new CustomHierarchyProductLevelInformation();
			temp.setProductMaster(this.productSearchService.
					findByProdId(genericEntityRelationship.getGenericChildEntity().getDisplayNumber()));
			if(temp.getProductMaster() != null){
				temp.getProductMaster().setPdpTemplate(this.masterDataExtensionAttributeRepository.
						findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(
								Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_PDP_TEMPLATE.getValue(),
								temp.getProductMaster().getProdId(),
								GenericEntity.EntyType.PROD.getName(),
								SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_PRODUCT_MAINTENANCE.getValue()));
			}

			temp.setDefaultParentRelationships(
					this.findDefaultParentRelationshipsByChildIdAndHierarchyContextAndEntityType(
							genericEntityRelationship.getKey().getChildEntityId(),
							hierarchyContext,
							GenericEntity.EntyType.PROD));
			temp.setEntityId(genericEntityRelationship.getKey().getChildEntityId());
			currentData.add(temp);
		});
		if(firstSearch){
			return new PageableResult<>(
					tempPage.getPage(),
					tempPage.getPageCount(),
					tempPage.getRecordCount(),
					currentData);
		} else {
			return new PageableResult<>(
					tempPage.getPage(),
					currentData);
		}
	}

	/**
	 * Finds all customer product groups sharing the same parent hierarchy level.
	 *
	 * @param hierarchyContext Hierarchy context of parent hierarchy level.
	 * @param hierarchyParentId Hierarchy parent id of parent hierarchy level.
	 * @param page Page to search for.
	 * @param pageSize Page size to look for.
	 * @param firstSearch Whether this is the first search or not.
	 * @return Pageable result of custom hierarchy product level information.
	 */
	public PageableResult<CustomHierarchyProductLevelInformation> findAllCustomerProductGroupsByParent(
			String hierarchyContext, Long hierarchyParentId, int page, int pageSize, boolean firstSearch) {
		Pageable pageRequest = new PageRequest(page, pageSize);
		// get the list of children relationships matching the given parent hierarchy level information.
		PageableResult<GenericEntityRelationship> tempPage;
		if(firstSearch){
			tempPage = this.findGenericEntityRelationshipsByParentAndContextWithCount(
					hierarchyContext, hierarchyParentId, GenericEntity.EntyType.PGRP, pageRequest);
		} else {
			tempPage = this.findGenericEntityRelationshipsByParentAndContextWithoutCount(
					hierarchyContext, hierarchyParentId, GenericEntity.EntyType.PGRP, pageRequest);
		}

		List<CustomHierarchyProductLevelInformation> currentData = new ArrayList<>();
		tempPage.getData().forEach(genericEntityRelationship -> {
			CustomHierarchyProductLevelInformation temp = new CustomHierarchyProductLevelInformation();
			temp.setCustomerProductGroup(this.productGroupSearchService.
					findCustomerProductGroupById(
							genericEntityRelationship.getGenericChildEntity().getDisplayNumber()));

			temp.setDefaultParentRelationships(this.findDefaultParentRelationshipsByChildIdAndHierarchyContextAndEntityType(
					genericEntityRelationship.getKey().getChildEntityId(),
					hierarchyContext,
					GenericEntity.EntyType.PGRP));
			temp.setEntityId(genericEntityRelationship.getKey().getChildEntityId());
			currentData.add(temp);
		});

		if(firstSearch){
			return new PageableResult<>(
					tempPage.getPage(),
					tempPage.getPageCount(),
					tempPage.getRecordCount(),
					currentData);
		} else {
			return new PageableResult<>(
					tempPage.getPage(),
					currentData);
		}
	}

	/**
	 * Finds all customer product groups sharing the same relationship.
	 *
	 * @param hierarchyContext Hierarchy context of parent hierarchy level.
	 * @param hierarchyParentId Hierarchy parent id of parent hierarchy level.
	 * @param page Page to search for.
	 * @param pageSize Page size to look for.
	 * @param firstSearch Whether this is the first search or not.
	 * @return Pageable result of custom hierarchy product level information.
	 */
	public PageableResult<GenericEntityRelationship> findAllCustomerProductGroupsByRelationship(
			String hierarchyContext, Long hierarchyParentId, int page, int pageSize, boolean firstSearch) {
		Pageable pageRequest = new PageRequest(page, pageSize);
		if(firstSearch){
			return this.findGenericEntityRelationshipsByParentAndContextWithCount(
					hierarchyContext, hierarchyParentId, GenericEntity.EntyType.PGRP, pageRequest);
		} else {
			return this.findGenericEntityRelationshipsByParentAndContextWithoutCount(
					hierarchyContext, hierarchyParentId, GenericEntity.EntyType.PGRP, pageRequest);
		}
	}

	/**
	 * Find the parent relationships that have a given child entity id, hierarchy context, child entity's entity type,
	 * and default parent switch of true.
	 *
	 * @param childId The child entity id number to look for.
	 * @param hierarchyContext The hierarchy context to look for.
	 * @param entityType The type of entity to look for.
	 * @return List of relationships matching the search.
	 */
	private List<GenericEntityRelationship> findDefaultParentRelationshipsByChildIdAndHierarchyContextAndEntityType(
			Long childId, String hierarchyContext, GenericEntity.EntyType entityType) {
		return this.genericEntityRelationshipRepository.
				findByKeyChildEntityIdAndHierarchyContextAndDefaultParentAndGenericChildEntityType(
						childId, hierarchyContext, true, entityType.getName());
	}

	/**
	 * Returns a pageable result containing the requested relationships given a hierarchy context, parent entity id,
	 * and the page request. Include the counts query.
	 *
	 * @param hierarchyContext Hierarchy context of parent hierarchy level.
	 * @param hierarchyParentId Hierarchy parent id of parent hierarchy level.
	 * @param pageRequest Page request of this search.
	 * @return Pageable result of customer product groups matching the given criteria.
	 */
	private PageableResult<GenericEntityRelationship> findGenericEntityRelationshipsByParentAndContextWithCount(
			String hierarchyContext, Long hierarchyParentId, GenericEntity.EntyType entityType, Pageable pageRequest) {
		Page<GenericEntityRelationship> page = this.genericEntityRelationshipRepositoryWithCount.
				findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityTypeOrderByGenericChildEntityDisplayNumber(
						hierarchyParentId, hierarchyContext, entityType.getName(), pageRequest);
		return new PageableResult<>(pageRequest.getPageNumber(),
				page.getTotalPages(),
				page.getTotalElements(),
				page.getContent());
	}

	/**
	 * Returns a pageable result containing the requested relationships given a hierarchy context, parent entity id,
	 * and the page request. Does not include the counts query.
	 *
	 * @param hierarchyContext Hierarchy context of parent hierarchy level.
	 * @param hierarchyParentId Hierarchy parent id of parent hierarchy level.
	 * @param pageRequest Page request of this search.
	 * @return Pageable result of customer product groups matching the given criteria.
	 */
	private PageableResult<GenericEntityRelationship> findGenericEntityRelationshipsByParentAndContextWithoutCount(
			String hierarchyContext, Long hierarchyParentId, GenericEntity.EntyType entityType, Pageable pageRequest) {
		List<GenericEntityRelationship> data = this.genericEntityRelationshipRepository.
				findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityTypeOrderByGenericChildEntityDisplayNumber(
						hierarchyParentId, hierarchyContext, entityType.getName(), pageRequest);
		return new PageableResult<>(pageRequest.getPageNumber(), data);
	}

	/**
	 * Returns a list of all customer product groups not already tied to a given customer hierarchy level.
	 *
	 * @param parentEntityId The parent entity id to look for existing relationships.
	 * @param hierarchyContext Hierarchy context to look for existing relationships.
	 * @return List of customer product groups not already tied to a given hierarchy level.
	 */
	public List<CustomerProductGroup> findAllCustomerProductGroupsNotOnParentEntity(Long parentEntityId, String hierarchyContext) {
		List<GenericEntityRelationship> currentRelationships = this.genericEntityRelationshipRepository.
				findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityType(
						parentEntityId, hierarchyContext, GenericEntity.EntyType.PGRP.getName());
		List<Long> currentCustomerProductGroupIds = new ArrayList<>();
		currentRelationships.forEach(currentRelationship -> {
			currentCustomerProductGroupIds.add(currentRelationship.getGenericChildEntity().getDisplayNumber());
		});
		return productGroupSearchService.findCustomerProductGroupsNotIn(currentCustomerProductGroupIds);
	}

	/**
	 * This method returns all of the possible sale channels
	 * @return
	 */
	public List<SalesChannel> getAllSalesChannels() {
		return this.salesChannelRepository.findAll();
	}

	/**
	 * This method returns all of the possible pdp templates
	 * @return
	 */
	public List<PDPTemplate> getAllPDPTemplates() {
		return this.pdpTemplateRepository.findAll();
	}

	/**
	 * This method returns all of the possible sale channels the descriptions have excessive
	 * white space at the end so it is trimmed
	 * @return
	 */
	public List<FulfillmentChannel> getAllFulfillmentPrograms() {
		List<FulfillmentChannel> originalList=this.fulfillmentChannelRepository.findAll();
		for (FulfillmentChannel fulfillmentChannel: originalList) {
			fulfillmentChannel.setDescription(fulfillmentChannel.getDescription().trim());
		}
		return originalList;
	}

	/**
	 * This method returns generic entity relationship children that are not products or product groups given a parent
	 * entity id and hierarchy context.
	 *
	 * @param parentEntityId The parent entity id to look for.
	 * @param hierarchyContextId The hierarchy context to look for.
	 * @return Generic entity relationship matching the given key.
	 */
	public List<GenericEntityRelationship> findNonProductChildren(Long parentEntityId, String hierarchyContextId) {
		return this.genericEntityRelationshipRepository.
				findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityType(
						parentEntityId, hierarchyContextId, GenericEntity.EntyType.CUSTH.getName());
	}

	/**
	 * This method returns a generic entity relationship given a key.
	 *
	 * @param key The generic entity relationship key.
	 * @return Generic entity relationship with the given key.
	 */
	public GenericEntityRelationship findByKey(GenericEntityRelationshipKey key) {
		return this.genericEntityRelationshipRepository.findOne(key);
	}

	/**
	 * Find parent relationships by searching for entity relationships that have the given hierarchy context,
	 * child entity display number and child entity type.
	 *
	 * @param displayNumber Display number to search for.
	 * @param hierarchyContext Hierarchy context to search for.
	 * @param entityType Entity type to search for.
	 * @return List of parent entity ids matching the search.
	 */
	public List<GenericEntityRelationship> findParentsByChildDisplayNumberAndHierarchyContextAndEntityType(
			Long displayNumber, String hierarchyContext, GenericEntity.EntyType entityType){
		return this.genericEntityRelationshipRepository
				.findByHierarchyContextAndGenericChildEntityDisplayNumberAndGenericChildEntityType(
						hierarchyContext, displayNumber, entityType.getName());
	}
}
