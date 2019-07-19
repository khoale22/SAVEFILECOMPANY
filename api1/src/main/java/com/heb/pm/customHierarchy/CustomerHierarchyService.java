/*
 *  CustomerHierarchyService
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 */
package com.heb.pm.customHierarchy;

import com.heb.pm.attribute.EntityAttributeService;
import com.heb.pm.entity.Attribute;
import com.heb.pm.entity.EntityAttribute;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.GenericEntityRelationshipKey;
import com.heb.pm.entity.HierarchyContext;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CustomerHierarchyService serves queries related to customer hierarchy by product, context and sales channel.
 *
 * @author vn40486
 * @since 2.15.0
 */
@Service
public class CustomerHierarchyService {

	@Autowired
	private EntityAttributeService entityAttributeService;

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	private HierarchyContextService hierarchyContextService;

	/**
	 * This function retrieves an entityRelationship based on its
	 * hierarchy context and product-Id and returns the
	 * entityRelationship along with its hierarchy display path
	 *
	 * @param productId
	 * @param hierarchyContext
	 * @return entityRelationship
	 */
	public GenericEntityRelationship getPrimaryCustomerHierarchy(long productId, String hierarchyContext) {
		GenericEntityRelationship entityRelationship = this.genericEntityRelationshipRepository.findByProduct(productId, hierarchyContext);
		this.resolvePrimaryCustomerHierarchy(entityRelationship);
		return entityRelationship;
	}

	/**
	 *This private function takes in an entityRelationship and
	 * sets its primary Custom Hierarchy display path
	 *
	 * @param entityRelationship
	 */
	private void resolvePrimaryCustomerHierarchy(GenericEntityRelationship entityRelationship) {
		if(entityRelationship != null) {
			entityRelationship.getGenericParentEntity().getId();
			if(entityRelationship.getGenericParentEntity().getDisplayNumber()>0){
				entityRelationship.getGenericParentEntity().setHierarchyPathDisplay(entityRelationship.getParentDescription().getLongDescription());
			}else{
				entityRelationship.getGenericParentEntity().setHierarchyPathDisplay(entityRelationship.getGenericParentEntity().getDisplayText());
			}
		}
	}

	/**
	 * This retrieves all parents of a child id.
	 * @param childId
	 * @param hierarchyContext
	 * @return all the parents of a child.
	 */
	public List<GenericEntityRelationship> getAllParentsOfChild(Long childId, String hierarchyContext) {
		List<GenericEntityRelationship> pathList = new ArrayList<>();
		HierarchyContext selectedHierarchyContext = this.hierarchyContextService.findOneById(hierarchyContext);
		List<GenericEntityRelationship> allParentsOfChild =
				genericEntityRelationshipRepository.findByKeyChildEntityIdAndHierarchyContext(childId, hierarchyContext);
		for (GenericEntityRelationship ger : allParentsOfChild) {
			traverseThroughToFindAllParents(selectedHierarchyContext.getParentEntityId(), ger, null, hierarchyContext, pathList);
		}
		return pathList;
	}

	/**
	 * This is a recursive call to figure out all of the parents of a child id.
	 * @param parentEntityId
	 * @param parent
	 * @param path
	 * @param hierarchyContext
	 * @param pathList
	 */
	private void traverseThroughToFindAllParents(
			Long parentEntityId, GenericEntityRelationship parent, GenericEntityRelationship path, String hierarchyContext, List<GenericEntityRelationship> pathList) {
		GenericEntityRelationship pu = new GenericEntityRelationship();
		GenericEntityRelationshipKey key = new GenericEntityRelationshipKey();
		pu.setKey(key);
		pu.getKey().setHierarchyContext(hierarchyContext);
		pu.getKey().setParentEntityId(parent.getKey().getParentEntityId());
		pu.getKey().setChildEntityId(parent.getKey().getChildEntityId());
		pu.setChildDescription(parent.getChildDescription());
		pu.setDefaultParent(parent.getDefaultParent());
		pu.setParentDescription(parent.getParentDescription());
		if(path != null) {
			List<GenericEntityRelationship> pathToPreviousNode = new ArrayList<>();
			pathToPreviousNode.add(path);
			pu.setChildRelationships(pathToPreviousNode);
		}
		if(parent.getKey().getParentEntityId().equals(parentEntityId)) {
			pathList.add(pu);
		} else {
			for (GenericEntityRelationship grandParent : parent.getParentRelationships()) {
				traverseThroughToFindAllParents(parentEntityId, grandParent, pu, hierarchyContext, pathList);
			}
		}
	}

	/**
	 * This function finds attributes by hierarchy context and entity ID
	 * @param entityId to search for
	 * @param hierarchyContextId to search for
	 * @return list of attributes
	 */
	public List<Attribute> findByHierarchyAndEntity(Long entityId, String hierarchyContextId) {
		HierarchyContext hierarchyContext = this.hierarchyContextService.findOneById(hierarchyContextId);
		Set<Long> entityIds;
		// if the entity id is the root node, just create a list and add the entity id
		if(entityId.equals(hierarchyContext.getParentEntityId())){
			entityIds = new HashSet<>();
			entityIds.add(entityId);
		}
		// else get all entity ids from all paths to the given entity id
		else {
			//Get all paths for given entity ID and hierarchy context
			List<GenericEntityRelationship> allPaths = this.getAllParentsOfChild(entityId, hierarchyContextId);
			//Get all entityIds from all path
			entityIds = this.getEntityIdsFromAllPaths(allPaths);
		}
		//Gets all entity attributes tied to the entity ids
		List<EntityAttribute> entityAttributes = this.entityAttributeService.getByEntityIds(new ArrayList<>(entityIds));
		//Gets attributes tied to the entity attributes
		return this.getAttributeFromEntityAttributes(entityAttributes, entityId);
	}
	/**
	 * This function is used to loop through a list of entity attributes to        * find all attributes tied to a given entity. After-which
	 * a 2nd loop is performed on the list to find inherited attributes
	 * @param entityAttributes
	 * @param entityId
	 * @return
	 */
	private List<Attribute> getAttributeFromEntityAttributes(List<EntityAttribute> entityAttributes, Long entityId) {
		List<Attribute> toReturn = new ArrayList<>();
		Attribute currentAttribute;
		Set<Long> attributeIds = new HashSet<>();
		//Get attributes tied to given entity
		for(EntityAttribute entityAttribute : entityAttributes){
			currentAttribute = entityAttribute.getAttribute();
			if(entityId.equals(entityAttribute.getKey().getEntityId())){
				toReturn.add(currentAttribute);
				attributeIds.add(currentAttribute.getAttributeId());
			}
		}
		//Get inherited attributes from the list
		for(EntityAttribute entityAttribute : entityAttributes){
			currentAttribute = entityAttribute.getAttribute();
			if(attributeIds.contains(currentAttribute.getAttributeId())){
				continue;
			}
			currentAttribute.setInherited(true);
			toReturn.add(currentAttribute);
			attributeIds.add(currentAttribute.getAttributeId());
		}
		return toReturn;
	}

	/**
	 * This gets unique entity ids from all relationships to a given entity
	 * @param allPaths paths to an entity id
	 * @return entityids
	 */
	private Set<Long> getEntityIdsFromAllPaths(List<GenericEntityRelationship> allPaths) {
		Set<Long> entityIds =  new HashSet<>();
		boolean addedRootId = false;
		for(GenericEntityRelationship currentRelationship : allPaths){
			if(!addedRootId){
				entityIds.add(currentRelationship.getKey().getParentEntityId());
				addedRootId = true;
			}
			this.addEntityIdsFromRelationship(entityIds, currentRelationship);
		}
		return entityIds;
	}

	/**
	 * Adds a child id from a given relationship to a set of entityids.
	 * If the relationship has children, recursively call this
	 * function passing each child.
	 *
	 * @param entityIds to store entity IDs
	 * @param currentRelationship shows the entity relationship being looked at
	 */
	private void addEntityIdsFromRelationship(Set<Long> entityIds, GenericEntityRelationship currentRelationship) {
		entityIds.add(currentRelationship.getKey().getChildEntityId());
		if (currentRelationship.getChildRelationships() != null &&
				!currentRelationship.getChildRelationships().isEmpty()){
			for(GenericEntityRelationship relationship : currentRelationship.getChildRelationships()){
				this.addEntityIdsFromRelationship(entityIds, relationship);
			}        }
	}
}
