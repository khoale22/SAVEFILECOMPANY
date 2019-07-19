package com.heb.pm.customHierarchy;

import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.HierarchyContext;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import com.heb.pm.repository.HierarchyContextRepository;
import com.heb.pm.ws.ProductHierarchyManagementServiceClient;
import com.heb.util.controller.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds all business logic related to hierarchy context.
 *
 * @author m314029
 * @since 2.12.0
 */
@Service
public class HierarchyContextService {

	private static final String GS1_CONTEXT_TO_NOT_INCLUDE = "GS1";
	private static final String PRODUCT_CONTEXT_TO_NOT_INCLUDE = "PROD";
	private static final String HIERARCHY_CONTEXT_ALREADY_EXISTS_ERROR =
			"Hierarchy Context: %s already exists. Please assign a different context.";

	private String ALL_HIERARCHY_CONTEXT_ID = "ALL";

	@Autowired
	private HierarchyContextRepository hierarchyContextRepository;

	@Autowired
	private GenericEntityRelationshipService genericEntityRelationshipService;

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	private ProductHierarchyManagementServiceClient productHierarchyManagementServiceClient;

	/**
	 * This method returns a list of all hierarchy contexts except "GS1" or "PROD" id.
	 *
	 * @return List of all parent generic entity relationships except "GS1".
	 */
	public List<HierarchyContext> findAll(){
		return this.hierarchyContextRepository.
				findByIdNotIn(Arrays.asList(GS1_CONTEXT_TO_NOT_INCLUDE,PRODUCT_CONTEXT_TO_NOT_INCLUDE));
	}

    /**
     * Find customer hierarchy context and child relationships.
     *
     * @return the hierarchy context with child relationships.
     */
	public HierarchyContext findCustomerHierarchyContext(){
        HierarchyContext hierarchyContext = this.hierarchyContextRepository.findOne(HierarchyContext.HierarchyContextCode.CUST.getName());
        if(hierarchyContext != null) {
			hierarchyContext.setChildRelationships(
					this.genericEntityRelationshipService.findByHierarchyContext(hierarchyContext));
		}
        return hierarchyContext;
	}

	/**
	 * Finds hierarchies based on thier hierarchy context's id
	 * @param hierarchyContextId the id for the hierarchy context
	 * @return a list of hierarchy contexts
	 */
	public List<HierarchyContext> findById(String hierarchyContextId){
		List<HierarchyContext> allHierarchyContexts;
		if(hierarchyContextId.trim().equals(ALL_HIERARCHY_CONTEXT_ID)){
			allHierarchyContexts = this.findAll();
		} else {
			allHierarchyContexts = new ArrayList<>();
			allHierarchyContexts.add(this.hierarchyContextRepository.findOne(hierarchyContextId));
		}
		allHierarchyContexts.forEach(
				hierarchyContext -> hierarchyContext.setChildRelationships(
						this.genericEntityRelationshipService.findByHierarchyContext(hierarchyContext)));
		return allHierarchyContexts;
	}

	/**
	 * Returns a single hierarchy context based on its id
	 * @param hierarchyContextId
	 * @return
	 */
	public HierarchyContext findOneById(String hierarchyContextId){
		HierarchyContext toReturn = this.hierarchyContextRepository.findOne(hierarchyContextId);
		toReturn.setChildRelationships(this.genericEntityRelationshipService.findByHierarchyContext(toReturn));
		return toReturn;
	}

	/**
	 * Finds all hierarchies where the regular expression matches a level in the hierarchy
	 * @param trimmedUpperCaseSearchString the regular express in the form of a string
	 * @param resolvedHierarchyContexts the list of resolved contexts
	 * @return
	 */
	public List<HierarchyContext> findHierarchyRelationshipsByRegularExpression(
			String trimmedUpperCaseSearchString, List<HierarchyContext> resolvedHierarchyContexts) {

		List<HierarchyContext> returnList = new ArrayList<>();
		HierarchyContext currentHierarchyContext;
		List<GenericEntityRelationship> firstLevelRelationships;
		for(HierarchyContext hierarchyContext : resolvedHierarchyContexts){
			currentHierarchyContext = new HierarchyContext(hierarchyContext);
			firstLevelRelationships =
					this.getChildRelationshipMatches(
							hierarchyContext.getChildRelationships(), trimmedUpperCaseSearchString);
			// set the list of child relationships to the new firstLevelRelationships list
			currentHierarchyContext.setChildRelationships(firstLevelRelationships);
			// add the current hierarchy context to the new return list of hierarchyContexts
			returnList.add(currentHierarchyContext);
		}
		return returnList;
	}

	/**
	 * Returns a chain of generic entity relationships that map from the bottom child back to the top most element
	 * @param childId the entity id of the bottom most child
	 * @param hierarchyTop the entity id for the head of the hierarchy context
	 * @param context the hierarchy that is being searached
	 * @return
	 */
	public List<GenericEntityRelationship> findAllParentsToChild(Long childId, Long hierarchyTop, String context){
		Long currentChildId = childId;
		ArrayList<GenericEntityRelationship> relationships= new ArrayList<>();
		while(currentChildId.longValue() != hierarchyTop.longValue()){
			GenericEntityRelationship relationship= this.genericEntityRelationshipRepository.findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(currentChildId, context, true);
			relationships.add(0,relationship);
			currentChildId=relationship.getKey().getParentEntityId();
		}
		return relationships;
	}

	/**
	 * Takes a hierarchy and finds the links in the hierarchy and sets their isCollaspsed flag to false
	 * @param hierarchyContext the hierarchy in question
	 * @param path the list of links that need to be set to open
	 * @return
	 */
	public HierarchyContext openPath(HierarchyContext hierarchyContext, List<GenericEntityRelationship> path){
		hierarchyContext.setIsCollapsed(false);
		GenericEntityRelationship topLink;
		List<GenericEntityRelationship> currentChildren=hierarchyContext.getChildRelationships();
		while(path.size()>0){
			topLink=path.get(0);
			currentChildren=openLink(currentChildren, topLink);
			path.remove(0);
		}
		return hierarchyContext;
	}

	/**
	 * Takes a single link and sets it to open
	 * @param children
	 * @param link
	 * @return
	 */
	private List<GenericEntityRelationship> openLink(List<GenericEntityRelationship> children, GenericEntityRelationship link){
		for (GenericEntityRelationship child: children) {
			if(child.equals(link)){
				child.setIsCollapsed(false);
				return child.getChildRelationships();
			}
		}
		return children;
	}

	/**
	 * Get all child relationships whose description matches the given search string, or have any children's
	 * description that matches the given search string.
	 *
	 * @param childRelationships Child relationships to search for a descriptions matching a search string.
	 * @return Child relationships whose description matches the given search string or containing children whose
	 * description matches the search string.
	 */
	private List<GenericEntityRelationship> getChildRelationshipMatches(
			List<GenericEntityRelationship> childRelationships, String trimmedUpperCaseSearchString) {
		List<GenericEntityRelationship> matchingChildRelationships;
		List<GenericEntityRelationship> toReturn = new ArrayList<>();
		GenericEntityRelationship currentRelationship;
		for(GenericEntityRelationship relationship : childRelationships){
			matchingChildRelationships = new ArrayList<>();
			if(!relationship.isChildRelationshipOfProductEntityType()){
				matchingChildRelationships =
						getChildRelationshipMatches(
								relationship.getChildRelationships(), trimmedUpperCaseSearchString);
			}
			if(!matchingChildRelationships.isEmpty() || relationship.getChildDescription() != null &&
					relationship.getChildDescription().getShortDescription().trim().toUpperCase()
							.contains(trimmedUpperCaseSearchString)){
				currentRelationship = new GenericEntityRelationship(relationship);
				// set the list of child relationships to the new secondLevelRelationships list
				currentRelationship.setChildRelationships(matchingChildRelationships);
				// add the second level relationship to the new list of secondLevelRelationships
				toReturn.add(currentRelationship);
			}
		}
		return toReturn;
	}

	/**
	 * Update custom hierarchy.  Get the new Hierarchy name and Context to update table.
	 *
	 * @param customHierarchy the custom hierarchy
	 * @param userInfo        the user info
	 */
	public void updateCustomHierarchy(HierarchyContextController.CustomHierarchyValues customHierarchy, UserInfo userInfo) {
		List<HierarchyContext> contextsWithSameId =
				this.hierarchyContextRepository.findByIdIgnoreCase(customHierarchy.getNewHierarchyContext());
		if(contextsWithSameId.isEmpty()) {
			this.productHierarchyManagementServiceClient.updateCustomHierarchy(customHierarchy, userInfo);
		} else {
			throw new IllegalArgumentException(String.format(HIERARCHY_CONTEXT_ALREADY_EXISTS_ERROR, customHierarchy.getNewHierarchyContext()));
		}

	}

	/**
	 * Sends a request to add a new level to a customer Hierarchy to the generic entity relationship service and the
	 * Generic Entity Relationship is of the added level is returned
	 * @param hierarchyValues the attributes for the new level
	 * @param user the user requesting to make the change
	 */
	public GenericEntityRelationship addCustomHierarchy(HierarchyContextController.HierarchyValues hierarchyValues, String user) {

		return this.genericEntityRelationshipService.addCustomHierarchy(hierarchyValues, user);
	}

	/**
	 * This method attempts to safely remove an level in the hierarchy
	 * @param genericEntityRelationship the relationship to be removed
	 * @param userId the user making the request
	 */
	public void saveRemoveLevel(GenericEntityRelationship genericEntityRelationship, String userId) {
		this.genericEntityRelationshipService.saveRemoveLevel(genericEntityRelationship, userId);
	}

}
