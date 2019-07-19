package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SellingRestrictionHierarchyLevel;
import com.heb.pm.entity.SellingRestrictionHierarchyLevelKey;
import com.heb.pm.repository.SellingRestrictionHierarchyLevelRepository;
import com.heb.pm.ws.ProductHierarchyManagementServiceClient;
import com.heb.util.ws.SoapException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds all business functions related to selling restrictions.
 *
 * @author m314029
 * @since 2.0.6
 */
@Service
public class SellingRestrictionHierarchyLevelService {

	@Autowired
	private SellingRestrictionHierarchyLevelRepository repository;

	@Autowired
	private ProductHierarchyManagementServiceClient productHierarchyManagementServiceClient;

	/**
	 * Get all the selling restriction for a specified product hierarchy level.
	 *
	 * @param department department of the selling restriction
	 * @param subDepartment subDepartment of the selling restriction
	 * @param itemClass itemClass of the selling restriction
	 * @param commodity commodity of the selling restriction
	 * @param subCommodity subCommodity of the selling restriction
	 * @return List of selling restrictions matching the parameters.
	 */
	public List<SellingRestrictionHierarchyLevel> getSellingRestrictionsForHierarchyLevel(
			String department, String subDepartment, Integer itemClass, Integer commodity, Integer subCommodity) {
		return this.repository.findByKeyDepartmentAndKeySubDepartmentAndKeyItemClassAndKeyCommodityAndKeySubCommodity(
				department, subDepartment, itemClass, commodity, subCommodity);
	}

	/**
	 * Calls the product hierarchy management service to update the selling restrictions given.
	 *
	 * @param sellingRestrictions Selling restrictions to update.
	 */
	public void update(List<SellingRestrictionHierarchyLevel> sellingRestrictions) {
		List<SellingRestrictionHierarchyLevel> prunedSellingRestrictions =
				this.removeDuplicatesByComparingExistingRecords(sellingRestrictions);

		// if there are any new updates to submit, send them to webservice client to update
		if(!prunedSellingRestrictions.isEmpty()){
			try {
				this.productHierarchyManagementServiceClient.updateSellingRestrictions(prunedSellingRestrictions);
			} catch (Exception e){
				throw  new SoapException(e.getMessage());
			}
		}
	}

	/**
	 * This method removes duplicate records from the sent list of selling restrictions from the front end by comparing
	 * the list to the current records in the database. This needs to happen because the product hierarchy data is
	 * cached, so the user may not be seeing up to date information (what they are trying to do may have already
	 * been done). This method removes records marked as ADD if the restriction already exists in the database, and
	 * removes records marked as DELETE if they already do not exist in the database.
	 *
	 * @param sellingRestrictions List of selling restrictions sent from the user to update.
	 * @return List of selling restrictions to update after removing update records that have already been done.
	 */
	private List<SellingRestrictionHierarchyLevel> removeDuplicatesByComparingExistingRecords(
			List<SellingRestrictionHierarchyLevel> sellingRestrictions) {

		// use the first element's key to find all restrictions that exist in database for a particular product
		// hierarchy level. The first element can be used because all of these selling restrictions have the same
		// department, subDepartment, itemClass, commodity, and sub-commodity codes. They should only differ in
		// restriction group code.
		SellingRestrictionHierarchyLevelKey key = sellingRestrictions.get(0).getKey();
		List<SellingRestrictionHierarchyLevel> currentList =
				this.getSellingRestrictionsForHierarchyLevel(
						key.getDepartment(), key.getSubDepartment(), key.getItemClass(), key.getCommodity(),
						key.getSubCommodity());
		SellingRestrictionHierarchyLevel foundRestriction;
		List<SellingRestrictionHierarchyLevel> restrictionsToRemove = new ArrayList<>();

		for(SellingRestrictionHierarchyLevel requestedRestriction : sellingRestrictions){
			foundRestriction = null;
			for(SellingRestrictionHierarchyLevel currentRestriction : currentList){
				if(currentRestriction.getKey().getRestrictionGroupCode().equalsIgnoreCase(
						requestedRestriction.getKey().getRestrictionGroupCode())){
					foundRestriction = requestedRestriction;
					break;
				}
			}

			// remove add action codes if they already exist
			if(foundRestriction != null && foundRestriction.getActionCode().equals(
					SellingRestrictionHierarchyLevel.ADD_ACTION_CODE)){
				restrictionsToRemove.add(foundRestriction);
			}

			// remove delete action codes if they already do not exist
			else if (foundRestriction == null && requestedRestriction.getActionCode().equals(
					SellingRestrictionHierarchyLevel.DELETE_ACTION_CODE)) {
				restrictionsToRemove.add(requestedRestriction);
			}
		}

		sellingRestrictions.removeAll(restrictionsToRemove);
		return sellingRestrictions;
	}

	/**
	 * Gets all viewable selling restrictions for up to all five levels of product hierarchy. This includes getting
	 * restrictions inherited from higher levels.
	 *
	 * @param department department of the selling restriction
	 * @param subDepartment subDepartment of the selling restriction
	 * @param itemClass itemClass of the selling restriction
	 * @param commodity commodity of the selling restriction
	 * @param subCommodity subCommodity of the selling restriction
	 * @return List of Selling restriction hierarchy levels matching the requested information.
	 */
	public List<SellingRestrictionHierarchyLevel> getAllViewableSellingRestrictions(
			String department, String subDepartment, Integer itemClass, Integer commodity, Integer subCommodity) {
		List<SellingRestrictionHierarchyLevel> allRestrictions = new ArrayList<>();
		List<SellingRestrictionHierarchyLevel> previousLevelRestrictions = new ArrayList<>();

		// get department level restrictions
		List<SellingRestrictionHierarchyLevel> currentRestrictions = this.getSellingRestrictionsForHierarchyLevel(
				department, ProductHierarchyController.NO_SUB_DEPARTMENT,
				ProductHierarchyController.NO_ITEM_CLASS,
				ProductHierarchyController.NO_COMMODITY,
				ProductHierarchyController.NO_SUB_COMMODITY);
		previousLevelRestrictions = this.buildCurrentSellingRestrictionsForHierarchyLevel(
				currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.DEPARTMENT);
		allRestrictions.addAll(previousLevelRestrictions);

		// if sub-department is not empty, get sub-department level restrictions
		if(!subDepartment.trim().equals(StringUtils.EMPTY)) {
			currentRestrictions = this.getSellingRestrictionsForHierarchyLevel(
					department, subDepartment, ProductHierarchyController.NO_ITEM_CLASS,
					ProductHierarchyController.NO_COMMODITY,
					ProductHierarchyController.NO_SUB_COMMODITY);
			previousLevelRestrictions = this.buildCurrentSellingRestrictionsForHierarchyLevel(
					currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.SUB_DEPARTMENT);
			allRestrictions.addAll(previousLevelRestrictions);
		}

		// if item-class is not zero, get item-class level restrictions
		if(!itemClass.equals(ProductHierarchyController.NO_ITEM_CLASS)) {
			currentRestrictions = this.getSellingRestrictionsForHierarchyLevel(
					department, subDepartment, itemClass, ProductHierarchyController.NO_COMMODITY,
					ProductHierarchyController.NO_SUB_COMMODITY);
			previousLevelRestrictions = this.buildCurrentSellingRestrictionsForHierarchyLevel(
					currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.ITEM_CLASS);
			allRestrictions.addAll(previousLevelRestrictions);
		}

		// if commodity is not zero, get commodity level restrictions
		if(!commodity.equals(ProductHierarchyController.NO_COMMODITY)) {
			currentRestrictions = this.getSellingRestrictionsForHierarchyLevel(
					department, subDepartment, itemClass, commodity,
					ProductHierarchyController.NO_SUB_COMMODITY);
			previousLevelRestrictions = this.buildCurrentSellingRestrictionsForHierarchyLevel(
					currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.COMMODITY);
			allRestrictions.addAll(previousLevelRestrictions);
		}

		// if sub-commodity is not zero, get sub-commodity level restrictions
		if(!subCommodity.equals(ProductHierarchyController.NO_SUB_COMMODITY)) {
			currentRestrictions = this.getSellingRestrictionsForHierarchyLevel(
					department, subDepartment, itemClass, commodity, subCommodity);
			previousLevelRestrictions = this.buildCurrentSellingRestrictionsForHierarchyLevel(
					currentRestrictions, previousLevelRestrictions, ProductHierarchyController.ProductHierarchyLevel.SUB_COMMODITY);
			allRestrictions.addAll(previousLevelRestrictions);
		}
		return allRestrictions;
	}

	/**
	 * This method creates the selling restrictions for a product hierarchy level given the list of current level
	 * restrictions, any one level higher restrictions, and the current level of product hierarchy being observed.
	 *
	 * @param currentRestrictions List of selling restrictions for current level hierarchy.
	 * @param previousLevelRestrictions List of selling restrictions from one higher level in the product hierarchy.
	 * @param hierarchyLevel The hierarchy level being observed.
	 * @return The list of selling restrictions (including any that are inherited) for the currently observed level.
	 */
	private List<SellingRestrictionHierarchyLevel> buildCurrentSellingRestrictionsForHierarchyLevel(
			List<SellingRestrictionHierarchyLevel> currentRestrictions,
			List<SellingRestrictionHierarchyLevel> previousLevelRestrictions,
			ProductHierarchyController.ProductHierarchyLevel hierarchyLevel) {
		List<SellingRestrictionHierarchyLevel> toReturn = new ArrayList<>();
		SellingRestrictionHierarchyLevel foundRestriction;
		SellingRestrictionHierarchyLevel newRestriction;

		// look for restrictions that will be inherited from one higher product hierarchy level
		for (SellingRestrictionHierarchyLevel higherLevelRestriction : previousLevelRestrictions) {
			foundRestriction = null;
			for (SellingRestrictionHierarchyLevel currentRestriction : currentRestrictions) {

				// if higher level restriction is one level above current level, and restriction codes match
				if (currentRestriction.getKey().getRestrictionGroupCode().equals(higherLevelRestriction.getKey().getRestrictionGroupCode())) {

					// found restriction
					foundRestriction = currentRestriction;
					break;
				}
			}

			// if the current list does not contain the restriction code, add the inherited restriction to return list
			if (foundRestriction == null) {
				newRestriction = new SellingRestrictionHierarchyLevel(higherLevelRestriction);
				newRestriction.setInherited(true);
				newRestriction.setProductHierarchyLevel(hierarchyLevel);
				toReturn.add(newRestriction);
			}
		}

		// add all current level restrictions to return list
		for(SellingRestrictionHierarchyLevel currentRestriction : currentRestrictions){
			newRestriction = new SellingRestrictionHierarchyLevel(currentRestriction);
			newRestriction.setProductHierarchyLevel(hierarchyLevel);
			toReturn.add(newRestriction);
		}
		Collections.sort(toReturn);
		return toReturn;
	}
}
