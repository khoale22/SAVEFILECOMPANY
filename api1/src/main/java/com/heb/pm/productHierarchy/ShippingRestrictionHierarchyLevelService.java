package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ShippingRestrictionHierarchyLevel;
import com.heb.pm.repository.ShippingRestrictionHierarchyLevelRepository;
import com.heb.pm.ws.ProductHierarchyManagementServiceClient;
import com.heb.util.ws.SoapException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds all business functions related to shipping restrictions.
 *
 * @author m314029
 * @since 2.0.6
 */
@Service
public class ShippingRestrictionHierarchyLevelService {

	@Autowired
	private ShippingRestrictionHierarchyLevelRepository repository;

	@Autowired
	private ProductHierarchyManagementServiceClient productHierarchyManagementServiceClient;

	/**
	 * Get the shipping restrictions for a specified product hierarchy level.
	 *
	 * @param department department of the shipping restriction
	 * @param subDepartment subDepartment of the shipping restriction
	 * @param itemClass itemClass of the shipping restriction
	 * @param commodity commodity of the shipping restriction
	 * @param subCommodity subCommodity of the shipping restriction
	 * @return List of Shipping restriction hierarchy levels matching the requested information.
	 */
	public List<ShippingRestrictionHierarchyLevel> getShippingRestrictionsForHierarchyLevel(
			String department, String subDepartment, Integer itemClass, Integer commodity, Integer subCommodity) {
		return this.repository.
				findByKeyDepartmentAndKeySubDepartmentAndKeyItemClassAndKeyCommodityAndKeySubCommodity(
						department, subDepartment, itemClass, commodity, subCommodity);
	}

	/**
	 * Calls the product hierarchy management service to update the shipping restrictions given.
	 *
	 * @param shippingRestrictions Shipping restrictions to update.
	 */
	public void update(List<ShippingRestrictionHierarchyLevel> shippingRestrictions) {
		List<ShippingRestrictionHierarchyLevel> prunedShippingRestrictions =
				this.removeDuplicatesByComparingExistingRecords(shippingRestrictions);

		// if there are any new updates to submit, send them to webservice client to update
		if(!prunedShippingRestrictions.isEmpty()){
			try {
				this.productHierarchyManagementServiceClient.updateShippingRestrictions(prunedShippingRestrictions);
			} catch (Exception e){
				throw  new SoapException(e.getMessage());
			}
		}
	}

	/**
	 * This method removes duplicate records from the sent list of shipping restrictions from the front end by comparing
	 * the list to the current records in the database. This needs to happen because the product hierarchy data is
	 * cached, so the user may not be seeing up to date information (what they are trying to do may have already
	 * been done). This method removes records marked as ADD if the restriction already exists in the database, and
	 * removes records marked as DELETE if they already do not exist in the database.
	 *
	 * @param shippingRestrictions List of shipping restrictions sent from the user to update.
	 * @return List of shipping restrictions to update after removing update records that have already been done.
	 */
	private List<ShippingRestrictionHierarchyLevel> removeDuplicatesByComparingExistingRecords(
			List<ShippingRestrictionHierarchyLevel> shippingRestrictions) {

		// use the first element to find all restrictions that exist in database for a particular product
		// hierarchy level. The first element can be used because all of these shipping restrictions have the same
		// department, subDepartment, itemClass, commodity, and sub-commodity codes. They should only differ in
		// restriction group code.
		ShippingRestrictionHierarchyLevel restriction = shippingRestrictions.get(0);
		List<ShippingRestrictionHierarchyLevel> currentList =
				this.getShippingRestrictionsForHierarchyLevel(
						restriction.getKey().getDepartment(), restriction.getKey().getSubDepartment(),
						restriction.getKey().getItemClass(), restriction.getKey().getCommodity(),
						restriction.getKey().getSubCommodity());
		ShippingRestrictionHierarchyLevel foundRestriction;
		List<ShippingRestrictionHierarchyLevel> restrictionsToRemove = new ArrayList<>();

		// look for add or update records that already exist (applicable at this level matters)
		for(ShippingRestrictionHierarchyLevel requestedRestriction : shippingRestrictions){
			foundRestriction = null;
			for(ShippingRestrictionHierarchyLevel currentRestriction : currentList){
				if(currentRestriction.getKey().getRestrictionCode().equalsIgnoreCase(
						requestedRestriction.getKey().getRestrictionCode()) &&
						currentRestriction.getApplicableAtThisLevel() ==
								requestedRestriction.getApplicableAtThisLevel()){
					foundRestriction = requestedRestriction;
					break;
				}
			}

			// remove add action codes if they already exist
			if(foundRestriction != null && foundRestriction.getActionCode().equals(
					ShippingRestrictionHierarchyLevel.ADD_ACTION_CODE)){
				restrictionsToRemove.add(foundRestriction);
			}

			// remove update action codes if the update has already been done
			else if(foundRestriction != null && foundRestriction.getActionCode().equals(
					ShippingRestrictionHierarchyLevel.UPDATE_ACTION_CODE) &&
					foundRestriction.getApplicableAtThisLevel() == requestedRestriction.getApplicableAtThisLevel()){
				restrictionsToRemove.add(foundRestriction);
			}

			// remove delete action codes if they already do not exist
			else if (foundRestriction == null && requestedRestriction.getActionCode().equals(
					ShippingRestrictionHierarchyLevel.DELETE_ACTION_CODE)) {
				restrictionsToRemove.add(requestedRestriction);
			}
		}

		shippingRestrictions.removeAll(restrictionsToRemove);
		return shippingRestrictions;
	}

	/**
	 * Gets all viewable shipping restrictions for up to all five levels of product hierarchy. This includes getting
	 * restrictions inherited from higher levels.
	 *
	 * @param department department of the shipping restriction
	 * @param subDepartment subDepartment of the shipping restriction
	 * @param itemClass itemClass of the shipping restriction
	 * @param commodity commodity of the shipping restriction
	 * @param subCommodity subCommodity of the shipping restriction
	 * @return List of Shipping restriction hierarchy levels matching the requested information.
	 */
	public List<ShippingRestrictionHierarchyLevel> getAllViewableShippingRestrictions(
			String department, String subDepartment, Integer itemClass, Integer commodity, Integer subCommodity) {
		List<ShippingRestrictionHierarchyLevel> allRestrictions = new ArrayList<>();
		List<ShippingRestrictionHierarchyLevel> previousLevelRestrictions = new ArrayList<>();

		// get department level restrictions
		List<ShippingRestrictionHierarchyLevel> currentRestrictions = this.getShippingRestrictionsForHierarchyLevel(
				department, ProductHierarchyController.NO_SUB_DEPARTMENT,
				ProductHierarchyController.NO_ITEM_CLASS,
				ProductHierarchyController.NO_COMMODITY,
				ProductHierarchyController.NO_SUB_COMMODITY);
		previousLevelRestrictions = this.buildCurrentShippingRestrictionsForHierarchyLevel(
				currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.DEPARTMENT);
		allRestrictions.addAll(previousLevelRestrictions);

		// if sub-department is not empty, get sub-department level restrictions
		if(!subDepartment.trim().equals(StringUtils.EMPTY)) {
			currentRestrictions = this.getShippingRestrictionsForHierarchyLevel(
					department, subDepartment, ProductHierarchyController.NO_ITEM_CLASS,
					ProductHierarchyController.NO_COMMODITY,
					ProductHierarchyController.NO_SUB_COMMODITY);
			previousLevelRestrictions = this.buildCurrentShippingRestrictionsForHierarchyLevel(
					currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.SUB_DEPARTMENT);
			allRestrictions.addAll(previousLevelRestrictions);
		}

		// if item-class is not zero, get item-class level restrictions
		if(!itemClass.equals(ProductHierarchyController.NO_ITEM_CLASS)) {
			currentRestrictions = this.getShippingRestrictionsForHierarchyLevel(
					department, subDepartment, itemClass, ProductHierarchyController.NO_COMMODITY,
					ProductHierarchyController.NO_SUB_COMMODITY);
			previousLevelRestrictions = this.buildCurrentShippingRestrictionsForHierarchyLevel(
					currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.ITEM_CLASS);
			allRestrictions.addAll(previousLevelRestrictions);
		}

		// if commodity is not zero, get commodity level restrictions
		if(!commodity.equals(ProductHierarchyController.NO_COMMODITY)) {
			currentRestrictions = this.getShippingRestrictionsForHierarchyLevel(
					department, subDepartment, itemClass, commodity,
					ProductHierarchyController.NO_SUB_COMMODITY);
			previousLevelRestrictions = this.buildCurrentShippingRestrictionsForHierarchyLevel(
					currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.COMMODITY);
			allRestrictions.addAll(previousLevelRestrictions);
		}

		// if sub-commodity is not zero, get sub-commodity level restrictions
		if(!subCommodity.equals(ProductHierarchyController.NO_SUB_COMMODITY)) {
			currentRestrictions = this.getShippingRestrictionsForHierarchyLevel(
					department, subDepartment, itemClass, commodity, subCommodity);
			previousLevelRestrictions = this.buildCurrentShippingRestrictionsForHierarchyLevel(
					currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.SUB_COMMODITY);
			allRestrictions.addAll(previousLevelRestrictions);
		}
		return allRestrictions;
	}

	/**
	 * This method creates the shipping restrictions for a product hierarchy level given the list of current level
	 * restrictions, any one level higher restrictions, and the current level of product hierarchy being observed.
	 *
	 * @param currentRestrictions List of shipping restrictions for current level hierarchy.
	 * @param previousLevelRestrictions List of shipping restrictions from one higher level in the product hierarchy.
	 * @param hierarchyLevel The hierarchy level being observed.
	 * @return The list of shipping restrictions (including any that are inherited) for the currently observed level.
	 */
	private List<ShippingRestrictionHierarchyLevel> buildCurrentShippingRestrictionsForHierarchyLevel(
			List<ShippingRestrictionHierarchyLevel> currentRestrictions,
			List<ShippingRestrictionHierarchyLevel> previousLevelRestrictions,
			ProductHierarchyController.ProductHierarchyLevel hierarchyLevel) {
		List<ShippingRestrictionHierarchyLevel> toReturn = new ArrayList<>();
		ShippingRestrictionHierarchyLevel foundRestriction;
		ShippingRestrictionHierarchyLevel newRestriction;

		// look for restrictions that will be inherited from one higher product hierarchy level
		for (ShippingRestrictionHierarchyLevel higherLevelRestriction : previousLevelRestrictions) {
			foundRestriction = null;
			for (ShippingRestrictionHierarchyLevel currentRestriction : currentRestrictions) {

				// if higher level restriction is one level above current level, and restriction codes match
				if (currentRestriction.getKey().getRestrictionCode().equals(higherLevelRestriction.getKey().getRestrictionCode())) {

					// found restriction
					foundRestriction = currentRestriction;
					break;
				}
			}

			// if the current list does not contain the restriction code, add the inherited restriction to return list
			if (foundRestriction == null) {
				newRestriction = new ShippingRestrictionHierarchyLevel(higherLevelRestriction);
				newRestriction.setInherited(true);
				newRestriction.setProductHierarchyLevel(hierarchyLevel);
				toReturn.add(newRestriction);
			}
		}

		// add all current level restrictions to return list
		for(ShippingRestrictionHierarchyLevel currentRestriction : currentRestrictions){
				newRestriction = new ShippingRestrictionHierarchyLevel(currentRestriction);
				newRestriction.setProductHierarchyLevel(hierarchyLevel);
				toReturn.add(newRestriction);
		}
		Collections.sort(toReturn);
		return toReturn;
	}
}
