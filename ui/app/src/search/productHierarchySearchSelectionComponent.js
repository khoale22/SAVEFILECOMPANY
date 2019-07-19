/*
 * productHierarchySearchSelectionComponent.js
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

(function() {
	angular.module('productMaintenanceUiApp').component('productHierarchySearchSelection', {

		require: {

		},
		bindings: {
			onUpdate: '&'
		},

		templateUrl: 'src/search/productHierarchySearchSelection.html',

		controller: ProductHierarchySearchSelectionController
	});

	ProductHierarchySearchSelectionController.$inject = ['ProductHierarchyApi', '$scope'];

	function ProductHierarchySearchSelectionController(productHierarchyApi, $scope) {

		var self = this;

		/**
		 * Error message
		 * @type {null}
		 */
		self.error = null;

		/**
		 * Data referred by html page
		 * @type {Array}
		 */
		self.data = [];

		/**
		 * Original data received from back end
		 * @type {Array}
		 */
		self.originalData = [];

		/**
		 * Text in search box
		 * @type {null}
		 */
		self.productHierarchySearchText = null;

		/**
		 * If user is filtering the hierarchy
		 * @type {boolean}
		 */
		self.searchingForProductHierarchy = false;

		var STRING_TAG = "#";
		var STRING_HYPHEN = "-";

		var ADD_CLICK_BACKGROUND_CLASS = "add-click-background";
		var ADD_CLICK_BACKGROUND_CLASS_REFERENCE = ".add-click-background";

		var HIERARCHY_ACTIVE_CODE = 'A';
		var HIERARCHY_INACTIVE_CODE = 'I';

		var SUB_DEPARTMENT_LEVEL_ELEMENT_ID_PREFIX = "subDepartment";
		var ITEM_CLASS_LEVEL_ELEMENT_ID_PREFIX = "itemClass";
		var COMMODITY_LEVEL_ELEMENT_ID_PREFIX = "commodity";
		var SUB_COMMODITY_LEVEL_ELEMENT_ID_PREFIX = "subCommodity";

		var updateItemClassTemplate = null;

		self.productHierarchySearchClearButtonTitle = "Clear search for product hierarchy.";
		self.searchingForProductHierarchyTextTemplate = "Searching product hierarchy for: $searchText. Please wait...";
		self.searchingForProductHierarchyText = null;

		/**
		 * Whether or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

        /**
		 * Search type default for hierarchy tab
         * @type {string}
         */
        self.searchType = "Product Hierarchy";

		/**
		 * Initialize the controller.
		 */
		self.$onInit = function(){
			self.isWaitingForResponse = true;
			productHierarchyApi.getFullHierarchy({}, self.loadData, self.fetchError);
		}

		/**
		 * Handle changes
		 */
		self.$onChanges = function() {
		}

		/**
		 * Callback for a successful call to get product hierarchy data from the back end.
		 *
		 * @param results The data returned by the back end.
		 */
		self.loadData = function (results) {
			self.originalData = results;
			self.data = results;
			self.isWaitingForResponse = false;
		}


		/**
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.error = error;
		}


		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaitingForResponse = false;
			self.data = null;
			if (error && error.data) {
				if(error.data.message) {
					self.setError(error.data.message);
				} else {
					self.setError(error.data.error);
				}
			}
			else {
				self.setError("An unknown error occurred.");
			}
		}

		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param hierarchyLevel
		 * @returns {boolean}
		 */
		self.isHierarchyLevelCollapsed = function(hierarchyLevel){
			if(typeof hierarchyLevel.isCollapsed === 'undefined'){
				return true;
			}else {
				return hierarchyLevel.isCollapsed;
			}
		}

		/**
		 * Method called when user selects a department level hierarchy from the full list of product hierarchy.
		 * This method sets the department view to the current selected department, and collapses or opens
		 * its sub-department list depending on its previous state.
		 *
		 * @param department The department selected by the user.
		 */
		self.selectDepartment = function(department){
			self.setDepartment(department, false);
			self.setSubDepartment(null, false);
			self.setItemClass(null, false);
			self.setCommodity(null, false);
			self.setSubCommodity(null, false);

			// if original department equals selected department
			if(self.selectedHierarchyLevel === self.DEPARTMENT &&
				self.originalDepartmentSelected && self.originalDepartmentSelected.key.department === department.key.department){
				// set isCollapsed to opposite of previous value
				department.isCollapsed = !department.isCollapsed;
			} else {
				// else set isCollapsed to false
				department.isCollapsed = false;
			}
			self.originalDepartmentSelected = department;
			self.selectedHierarchyLevel = self.DEPARTMENT;
			self.getCurrentShippingRestrictions(department, null, null, null, null);
			self.getCurrentSellingRestrictions(department, null, null, null, null);
		}

		/**
		 * Method called when user selects a sub-department level hierarchy from the full list of product hierarchy.
		 * This method sets the sub-department view to the current selected sub-department, the department view to
		 * the sub-department's parent, and collapses or opens its item-class list depending on its previous state.
		 *
		 * @param department The selected sub-department's parent department.
		 * @param subDepartment The sub-department selected by the user.
		 * @param departmentIndex The selected sub-department's parent department's index.
		 * @param subDepartmentIndex The selected sub-department's index.
		 */
		self.selectSubDepartment = function (department, subDepartment, departmentIndex, subDepartmentIndex) {
			self.setDepartment(department, true);
			self.setSubDepartment(subDepartment, false);
			self.setItemClass(null, false);
			self.setCommodity(null, false);
			self.setSubCommodity(null, false);
			// if original sub-department equals selected department
			if(self.selectedHierarchyLevel === self.SUB_DEPARTMENT &&
				self.originalSubDepartmentSelected &&
				self.originalSubDepartmentSelected.key.department === subDepartment.key.department &&
				self.originalSubDepartmentSelected.key.subDepartment === subDepartment.key.subDepartment){
				// set isCollapsed to opposite of previous value
				subDepartment.isCollapsed = !subDepartment.isCollapsed;
			} else {
				// else set isCollapsed to false
				subDepartment.isCollapsed = false;
			}
			self.originalSubDepartmentSelected = subDepartment;
			self.selectedHierarchyLevel = self.SUB_DEPARTMENT;
			self.getCurrentShippingRestrictions(department, subDepartment, null, null, null);
			self.getCurrentSellingRestrictions(department, subDepartment, null, null, null);
			self.highlightCurrentSelectedHierarchyLevel(departmentIndex, subDepartmentIndex, null, null, null);
		}

		/**
		 * Method called when user selects an item-class level hierarchy from the full list of product hierarchy.
		 * This method sets the item-class view to the current selected item-class, the sub-department view
		 * to the item-class's parent, the department view to the item-class's parent, and collapses or opens the
		 * item-class list depending on its previous state.
		 *
		 * @param department The selected item-class's parent department.
		 * @param subDepartment The selected item-class's parent sub-department.
		 * @param itemClass The item-class selected by the user.
		 * @param departmentIndex The selected item-class's parent department's index.
		 * @param subDepartmentIndex The selected item-class's parent sub-department's index.
		 * @param itemClassIndex The selected item-class's index.
		 */
		self.selectItemClass =
			function (department, subDepartment, itemClass, departmentIndex, subDepartmentIndex, itemClassIndex) {
				self.setDepartment(department, true);
				self.setSubDepartment(subDepartment, true);
				self.setItemClass(itemClass, false);
				self.setCommodity(null, false);
				self.setSubCommodity(null, false);
				// if original item-class equals selected department
				if(self.selectedHierarchyLevel === self.ITEM_CLASS &&
					self.originalItemClassSelected && self.originalItemClassSelected.itemClassCode === itemClass.itemClassCode){
					// set isCollapsed to opposite of previous value
					itemClass.isCollapsed = !itemClass.isCollapsed;
				} else {
					// else set isCollapsed to false
					itemClass.isCollapsed = false;
				}
				self.originalItemClassSelected = itemClass;
				self.selectedHierarchyLevel = self.ITEM_CLASS;
				self.setTabSelectedForSelectedHierarchyLevel(self.itemClassSelected, self.GENERAL_TAB_NAME);
				self.getItemClassTemplate();
				self.getCurrentShippingRestrictions(department, subDepartment, itemClass, null, null);
				self.getCurrentSellingRestrictions(department, subDepartment, itemClass, null, null);
				self.highlightCurrentSelectedHierarchyLevel(departmentIndex, subDepartmentIndex, itemClassIndex, null, null);
			}

		/**
		 * Method called when user selects an commodity level hierarchy from the full list of product hierarchy.
		 * This method sets the commodity view to the current selected commodity, the item-class view to the
		 * commodity's parent, the sub-department view to the commodity's parent, the department view to the
		 * commodity's parent, and collapses or opens the sub-commodity list depending on its previous state.
		 *
		 * @param department The selected commodity's parent department.
		 * @param subDepartment The selected commodity's parent sub-department.
		 * @param itemClass The selected commodity's parent item-class.
		 * @param commodity The commodity selected by the user.
		 * @param departmentIndex The selected commodity's parent department's index.
		 * @param subDepartmentIndex The selected commodity's parent sub-department's index.
		 * @param itemClassIndex The selected commodity's parent item-class's index.
		 * @param commodityIndex The selected commodity's index.
		 */
		self.selectCommodity =
			function (department, subDepartment, itemClass, commodity,
					  departmentIndex, subDepartmentIndex, itemClassIndex, commodityIndex) {
				self.setDepartment(department, true);
				self.setSubDepartment(subDepartment, true);
				self.setItemClass(itemClass, true);
				self.setCommodity(commodity, false);
				self.setSubCommodity(null, false);
				// if original commodity equals selected department
				if(self.selectedHierarchyLevel === self.COMMODITY &&
					self.originalCommoditySelected &&
					self.originalCommoditySelected.key.classCode === commodity.key.classCode &&
					self.originalCommoditySelected.key.commodityCode === commodity.key.commodityCode){
					// set isCollapsed to opposite of previous value
					commodity.isCollapsed = !commodity.isCollapsed;
				} else {
					// else set isCollapsed to false
					commodity.isCollapsed = false;
				}
				self.originalCommoditySelected = commodity;
				self.selectedHierarchyLevel = self.COMMODITY;
				self.setTabSelectedForSelectedHierarchyLevel(self.commoditySelected, self.GENERAL_TAB_NAME);
				self.getCommodityTemplate();
				self.getCurrentShippingRestrictions(department, subDepartment, itemClass, commodity, null);
				self.getCurrentSellingRestrictions(department, subDepartment, itemClass, commodity, null);
				self.highlightCurrentSelectedHierarchyLevel(
					departmentIndex, subDepartmentIndex, itemClassIndex, commodityIndex, null);
			}

		/**
		 * Method called when user selects an sub-commodity level hierarchy from the full list of product hierarchy.
		 * This method sets the sub-commodity view to the current selected sub-commodity, the commodity view to the
		 * sub-commodity's parent, the item-class view to the sub-commodity's parent, the sub-department view to
		 * the sub-commodity's parent, and the department view to the sub-commodity's parent.
		 *
		 * @param department The selected sub-commodity's parent department.
		 * @param subDepartment The selected sub-commodity's parent sub-department.
		 * @param itemClass The selected sub-commodity's parent item-class.
		 * @param commodity The selected sub-commodity's parent commodity.
		 * @param subCommodity The sub-commodity selected by the user.
		 * @param departmentIndex The selected commodity's parent department's index.
		 * @param subDepartmentIndex The selected commodity's parent sub-department's index.
		 * @param itemClassIndex The selected commodity's parent item-class's index.
		 * @param commodityIndex The selected commodity's index.
		 * @param subCommodityIndex The selected sub-commodity's index.
		 */
		self.selectSubCommodity =
			function (department, subDepartment, itemClass, commodity, subCommodity,
					  departmentIndex, subDepartmentIndex, itemClassIndex, commodityIndex, subCommodityIndex) {
				self.setDepartment(department, true);
				self.setSubDepartment(subDepartment, true);
				self.setItemClass(itemClass, true);
				self.setCommodity(commodity, true);
				self.setSubCommodity(subCommodity, false);
				self.originalSubCommoditySelected = subCommodity;
				self.selectedHierarchyLevel = self.SUB_COMMODITY;
				self.getSubCommodityTemplate();
				self.getCurrentShippingRestrictions(department, subDepartment, itemClass, commodity, subCommodity);
				self.getCurrentSellingRestrictions(department, subDepartment, itemClass, commodity, subCommodity);
				self.highlightCurrentSelectedHierarchyLevel(
					departmentIndex, subDepartmentIndex, itemClassIndex, commodityIndex, subCommodityIndex);
			}

		/**
		 * This is a helper method to determine if a particular hierarchy is at least at a hierarchyLevel or above.
		 * (The user should not see sub commodity details if the user has just selected a department)
		 *
		 * @param hierarchyLevel The level the user has selected (i.e. SUB_DEPARTMENT)
		 * @returns {boolean}
		 */
		self.isSelectedHierarchyAtLeastAtLevel = function (hierarchyLevel) {
			if(self.selectedHierarchyLevel === hierarchyLevel){
				return true;
			}
			switch(hierarchyLevel){
				case self.DEPARTMENT:{
					if(self.selectedHierarchyLevel === self.SUB_DEPARTMENT){
						return true;
					}
				}
				case self.SUB_DEPARTMENT:{
					if(self.selectedHierarchyLevel === self.ITEM_CLASS){
						return true;
					}
				}
				case self.ITEM_CLASS:{
					if(self.selectedHierarchyLevel === self.COMMODITY){
						return true;
					}
				}
				case self.COMMODITY:{
					if(self.selectedHierarchyLevel === self.SUB_COMMODITY){
						return true;
					}
				}
			}
			return false;
		}

		/**
		 * Helper method to determine if the inputs should be disabled at the current level. This will happen when a
		 * user selects a lower than department level for every level above the selected level. (If the user selected a
		 * sub-department, all information in the department level will be read only)
		 *
		 * @param hierarchyLevel The hierarchyLevel of the information being shown.
		 * @returns {boolean}
		 */
		self.isSelectedHierarchyAtLevelDisabled = function (hierarchyLevel) {
			return self.selectedHierarchyLevel !== hierarchyLevel;
		}

		/**
		 * Sets whether viewed department should be collapsed or not
		 * --if department is lowest level, do not collapse, else do collapse
		 * Sets department selected to received department value, and sets selling restrictions at the department level.
		 *
		 * @param department The department selected by the user.
		 * @param collapse Whether department view should be collapsed or not.
		 */
		self.setDepartment = function (department, collapse) {
			self.departmentIsCollapsed = collapse;
			self.departmentSelected = angular.copy(department);
		}

		/**
		 * Sets whether viewed sub-department should be collapsed or not
		 * --if sub-department is lowest level, do not collapse, else do collapse
		 * Sets sub-department selected to received sub-department value, and sets selling restrictions at the
		 * sub-department level.
		 *
		 * @param subDepartment The sub-department selected by the user.
		 * @param collapse Whether sub-department view should be collapsed or not.
		 */
		self.setSubDepartment = function (subDepartment, collapse) {
			self.subDepartmentIsCollapsed = collapse;
			self.subDepartmentSelected = subDepartment !== null ? angular.copy(subDepartment) : null;
		}

		/**
		 * Sets whether viewed itemClass should be collapsed or not
		 * --if itemClass is lowest level, do not collapse, else do collapse
		 * Sets itemClass selected to received itemClass value, and sets selling restrictions at the
		 * itemClass level.
		 *
		 * @param itemClass The itemClass selected by the user.
		 * @param collapse Whether itemClass view should be collapsed or not.
		 */
		self.setItemClass = function (itemClass, collapse) {
			self.itemClassIsCollapsed = collapse;
			if(itemClass !== null){
				self.itemClassSelected = angular.copy(itemClass);
				if(itemClass.itemClassDescription !== null){
					self.itemClassSelected.itemClassDescription = angular.copy(itemClass.itemClassDescription);
				}
			} else {
				self.itemClassSelected = null;
			}
		}

		/**
		 * Sets whether viewed commodity should be collapsed or not
		 * --if commodity is lowest level, do not collapse, else do collapse
		 * Sets commodity selected to received commodity value, and sets selling restrictions at the
		 * commodity level.
		 *
		 * @param commodity The commodity selected by the user.
		 * @param collapse Whether commodity view should be collapsed or not.
		 */
		self.setCommodity = function (commodity, collapse) {
			self.commodityIsCollapsed = collapse;
			if(commodity !== null){
				self.commoditySelected = angular.copy(commodity);
				if(commodity.name !== null){
					self.commoditySelected.name = angular.copy(commodity.name);
				}
				if(commodity.bdmCode !== null){
					self.commoditySelected.bdmCode = angular.copy(commodity.bdmCode);
				}
				if(commodity.eBMid !== null){
					self.commoditySelected.eBMid = angular.copy(commodity.eBMid);
				}
				if(commodity.bDAid !== null){
					self.commoditySelected.bDAid = angular.copy(commodity.bDAid);
				}
				self.commoditySelected.active = self.isCommodityActive(self.commoditySelected);
			} else {
				self.commoditySelected = null;
			}
		}

		/**
		 * Sets whether viewed sub-commodity should be collapsed or not
		 * --if sub-commodity is lowest level, do not collapse, else do collapse
		 * Sets sub-commodity selected to received sub-commodity value, and sets selling restrictions at the
		 * sub-commodity level.
		 *
		 * @param subCommodity The sub-commodity selected by the user.
		 * @param collapse Whether sub-commodity view should be collapsed or not.
		 */
		self.setSubCommodity = function (subCommodity, collapse) {
			self.subCommodityIsCollapsed = collapse;
			if(subCommodity !== null){
				self.subCommoditySelected = angular.copy(subCommodity);
				if(subCommodity.name !== null){
					self.subCommoditySelected.name = angular.copy(subCommodity.name);
				}
				if(subCommodity.taxCategoryCode !== null){
					self.subCommoditySelected.taxCategoryCode = angular.copy(subCommodity.taxCategoryCode);
				}
				if(subCommodity.nonTaxCategoryCode !== null){
					self.subCommoditySelected.nonTaxCategoryCode = angular.copy(subCommodity.nonTaxCategoryCode);
				}
				self.subCommoditySelected.active = self.isSubCommodityActive(self.subCommoditySelected);
			} else {
				self.subCommoditySelected = null;
			}
		}

		/**
		 * This method removes the previous selected highlighted hierarchy level(if there is one), and then sets the
		 * current selected level in a product hierarchy to be highlighted. This is done by using the string
		 * representation of the id tag (i.e. '#department') and the index of the current level and all indices of
		 * parent levels.
		 *
		 * @param departmentIndex The index of the department in the product hierarchy.
		 * @param subDepartmentIndex The index of the sub-department in the product hierarchy.
		 * @param itemClassIndex The index of the item-class in the product hierarchy.
		 * @param commodityIndex The index of the commodity in the product hierarchy.
		 * @param subCommodityIndex The index of the sub-commodity in the product hierarchy.
		 */
		self.highlightCurrentSelectedHierarchyLevel = function (departmentIndex, subDepartmentIndex, itemClassIndex, commodityIndex, subCommodityIndex) {

			// remove any html elements with 'add-click-background'
			$(ADD_CLICK_BACKGROUND_CLASS_REFERENCE).removeClass(ADD_CLICK_BACKGROUND_CLASS);

			// add a '#' tag reference to the unique element id
			var elementIdTag = STRING_TAG + self.getUniqueElementIdFromIndices(
				departmentIndex, subDepartmentIndex, itemClassIndex, commodityIndex, subCommodityIndex);

			// add 'add-click-background' class to current selected hierarchy level
			$(elementIdTag).addClass(ADD_CLICK_BACKGROUND_CLASS);
		}

		/**
		 * This function returns whether or not active checkbox on commodity is disabled or not.
		 * This is done by calling the permissions service getPermissions method passing in the resource for
		 * commodity active: 'PH_COMM_06', for edit access. If the user has the appropriate permissions, and the
		 * commodity level is not disabled, this function returns false; else return true.
		 *
		 * @returns {boolean} True if checkbox should be disabled, false otherwise.
		 */
		self.isCommodityActiveDisabled = function(){
			if(permissionsService.getPermissions('PH_COMM_06', 'EDIT')){
				return self.isSelectedHierarchyAtLevelDisabled(self.COMMODITY);
			} else {
				return true;
			}
		}

		/**
		 * This function gets whether or not a commodity is active. If the commodity has an attribute 'active', use
		 * that value. Else if the commodity's classCommodityActive equals
		 * the HIERARCHY_ACTIVE_CODE ('A'), return true. Else return false.
		 *
		 * @param commodity Commodity to set active switch on.
		 * @returns {boolean} True if commodity is active, false otherwise.
		 */
		self.isCommodityActive = function (commodity) {
			if(commodity.hasOwnProperty('active')){
				return commodity.active;
			}
			return commodity.classCommodityActive === HIERARCHY_ACTIVE_CODE;
		}

		/**
		 * This function returns the value for class commodity active. If the commodity is active,
		 * return HIERARCHY_ACTIVE_CODE('A'), else return HIERARCHY_INACTIVE_CODE('I').
		 *
		 * @param commodity Commodity to set active switch on.
		 * @returns {String} ('A') if commodity is active, ('I') otherwise.
		 */
		self.getCommodityActiveValue = function (commodity) {
			return commodity.active ? HIERARCHY_ACTIVE_CODE : HIERARCHY_INACTIVE_CODE;
		}

		/**
		 * This function returns whether or not active checkbox on sub-commodity is disabled or not.
		 * This is done by calling the permissions service getPermissions method passing in the resource for
		 * sub-commodity active: 'PH_SUBC_04', for edit access. If the user has the appropriate permissions,
		 * this function returns false; else return true.
		 *
		 * @returns {boolean} True if checkbox should be disabled, false otherwise.
		 */
		self.isSubCommodityActiveDisabled = function(){
			return !permissionsService.getPermissions('PH_SUBC_04', 'EDIT');
		}

		/**
		 * This function returns whether or not food stamp checkbox on sub-commodity is disabled or not.
		 * This is done by calling the permissions service getPermissions method passing in the resource for
		 * sub-commodity food stamp eligible: 'PH_SUBC_06', for edit access. If the user has the appropriate permissions,
		 * this function returns false; else return true.
		 *
		 * @returns {boolean} True if checkbox should be disabled, false otherwise.
		 */
		self.isSubCommodityFoodStampDisabled = function(){
			return !permissionsService.getPermissions('PH_SUBC_06', 'EDIT');
		}

		/**
		 * This function returns whether or not tax eligible checkbox on sub-commodity is disabled or not.
		 * This is done by calling the permissions service getPermissions method passing in the resource for
		 * sub-commodity tax eligible: 'PH_SUBC_07', for edit access. If the user has the appropriate permissions,
		 * this function returns false; else return true.
		 *
		 * @returns {boolean} True if checkbox should be disabled, false otherwise.
		 */
		self.isSubCommodityTaxEligibleDisabled = function(){
			return !permissionsService.getPermissions('PH_SUBC_07', 'EDIT');
		}

		/**
		 * This function gets whether or not a sub-commodity is active. If the sub-commodity has an attribute
		 * 'active', use that value. Else if the sub-commodity's subCommodityActive equals
		 * the HIERARCHY_ACTIVE_CODE ('A'), return true. Else return false.
		 *
		 * @param subCommodity SubCommodity to set active switch on.
		 * @returns {boolean} True if sub-commodity is active, false otherwise.
		 */
		self.isSubCommodityActive = function (subCommodity) {
			if(subCommodity.hasOwnProperty('active')){
				return subCommodity.active;
			}
			return subCommodity.subCommodityActive === HIERARCHY_ACTIVE_CODE;
		}

		/**
		 * This function returns the value for sub commodity active. If the sub-commodity is active,
		 * return HIERARCHY_ACTIVE_CODE('A'), else return HIERARCHY_INACTIVE_CODE('I').
		 *
		 * @param subCommodity SubCommodity to set active switch on.
		 * @returns {String} ('A') if sub-commodity is active, ('I') otherwise.
		 */
		self.getSubCommodityActiveValue = function (subCommodity) {
			return subCommodity.active ? HIERARCHY_ACTIVE_CODE : HIERARCHY_INACTIVE_CODE;
		}

		/**
		 * This function sets the tab selected for a hierarchy level given the tab name that is selected
		 * (self.GENERAL_TAB_NAME or self.DEFAULTS_TAB_NAME).
		 *
		 * @param selectedHierarchyLevel Hierarchy level to set tab selected on.
		 * @param tabSelectedName Tab name to set for the hierarchy level's tab.
		 */
		self.setTabSelectedForSelectedHierarchyLevel = function(selectedHierarchyLevel, tabSelectedName){
			if(selectedHierarchyLevel !== null) {
				selectedHierarchyLevel.tabSelected = tabSelectedName;
			}
		}

		/**
		 * Calls back end to get the current shipping restrictions for the specified product hierarchy.
		 *
		 * @param department Department to search for shipping restrictions.
		 * @param subDepartment Sub-department to search for shipping restrictions.
		 * @param itemClass Item-class to search for shipping restrictions.
		 * @param commodity Commodity to search for shipping restrictions.
		 * @param subCommodity Sub-commodity to search for shipping restrictions.
		 */
		self.getCurrentShippingRestrictions = function (department, subDepartment, itemClass, commodity, subCommodity){
			productHierarchyApi.getViewableShippingRestrictions(
				self.getProductHierarchyParameters(department, subDepartment, itemClass, commodity, subCommodity),
				self.loadCurrentShippingRestrictions, self.fetchError);
		}

		/**
		 * Calls back end to get the current selling restrictions for the specified product hierarchy.
		 *
		 * @param department Department to search for selling restrictions.
		 * @param subDepartment Sub-department to search for selling restrictions.
		 * @param itemClass Item-class to search for selling restrictions.
		 * @param commodity Commodity to search for selling restrictions.
		 * @param subCommodity Sub-commodity to search for selling restrictions.
		 */
		self.getCurrentSellingRestrictions = function (department, subDepartment, itemClass, commodity, subCommodity){
			productHierarchyApi.getViewableSellingRestrictions(
				self.getProductHierarchyParameters(department, subDepartment, itemClass, commodity, subCommodity),
				self.loadCurrentSellingRestrictions, self.fetchError);
		}

		/**
		 * Helper function to retrieve the product hierarchy parameters for a given product hierarchy level. If the
		 * level is null, use the default value. Otherwise use that level's key value representing that level.
		 *
		 * @param department Department to search for.
		 * @param subDepartment Sub-department to search for..
		 * @param itemClass Item-class to search for..
		 * @param commodity Commodity to search for..
		 * @param subCommodity Sub-commodity to search for..
		 * @returns {{department: String, subDepartment: String, itemClass: number, commodity: number, subCommodity: number}}
		 */
		self.getProductHierarchyParameters = function (department, subDepartment, itemClass, commodity, subCommodity){
			return {
				department: department.key.department,
				subDepartment: subDepartment !== null ? subDepartment.key.subDepartment : department.key.subDepartment,
				itemClass: itemClass !== null ? itemClass.itemClassCode : 0,
				commodity: commodity !== null ? commodity.key.commodityCode : 0,
				subCommodity: subCommodity !== null ? subCommodity.key.subCommodityCode : 0
			};
		}

		/**
		 * Loads the shipping restrictions into their respective objects
		 *
		 * @param restrictions
		 */
		self.loadCurrentShippingRestrictions = function (restrictions) {
			self.departmentSelected.viewableShippingRestrictions = [];
			self.subDepartmentSelected !== null ? self.subDepartmentSelected.viewableShippingRestrictions = [] : null;
			self.itemClassSelected !== null ? self.itemClassSelected.viewableShippingRestrictions = [] : null;
			self.commoditySelected !== null ? self.commoditySelected.viewableShippingRestrictions = [] : null;
			self.subCommoditySelected !== null ? self.subCommoditySelected.viewableShippingRestrictions = [] : null;
			var restriction;
			for(var restrictionIndex = 0; restrictionIndex < restrictions.length; restrictionIndex++) {
				restriction = restrictions[restrictionIndex];
				switch (restriction.productHierarchyLevel) {
					case self.DEPARTMENT:{
						self.departmentSelected.viewableShippingRestrictions.push(restriction);
						break;
					}
					case self.SUB_DEPARTMENT:{
						self.subDepartmentSelected.viewableShippingRestrictions.push(restriction);
						break;
					}
					case self.ITEM_CLASS:{
						self.itemClassSelected.viewableShippingRestrictions.push(restriction);
						break;
					}
					case self.COMMODITY:{
						self.commoditySelected.viewableShippingRestrictions.push(restriction);
						break;
					}
					case self.SUB_COMMODITY:{
						self.subCommoditySelected.viewableShippingRestrictions.push(restriction);
						break;
					}
				}
			}
		}

		/**
		 * Loads the selling restrictions into their respective objects
		 *
		 * @param restrictions
		 */
		self.loadCurrentSellingRestrictions = function (restrictions) {
			self.departmentSelected.viewableSellingRestrictions = [];
			self.subDepartmentSelected !== null ? self.subDepartmentSelected.viewableSellingRestrictions = [] : null;
			self.itemClassSelected !== null ? self.itemClassSelected.viewableSellingRestrictions = [] : null;
			self.commoditySelected !== null ? self.commoditySelected.viewableSellingRestrictions = [] : null;
			self.subCommoditySelected !== null ? self.subCommoditySelected.viewableSellingRestrictions = [] : null;
			var restriction;
			for(var restrictionIndex = 0; restrictionIndex < restrictions.length; restrictionIndex++) {
				restriction = restrictions[restrictionIndex];
				switch (restriction.productHierarchyLevel) {
					case self.DEPARTMENT:{
						self.departmentSelected.viewableSellingRestrictions.push(restriction);
						break;
					}
					case self.SUB_DEPARTMENT:{
						self.subDepartmentSelected.viewableSellingRestrictions.push(restriction);
						break;
					}
					case self.ITEM_CLASS:{
						self.itemClassSelected.viewableSellingRestrictions.push(restriction);
						break;
					}
					case self.COMMODITY:{
						self.commoditySelected.viewableSellingRestrictions.push(restriction);
						break;
					}
					case self.SUB_COMMODITY:{
						self.subCommoditySelected.viewableSellingRestrictions.push(restriction);
						break;
					}
				}
			}
		}

		/**
		 * This function gets whether or not a commodity is active. If the commodity has an attribute 'active', use
		 * that value. Else if the commodity's classCommodityActive equals
		 * the HIERARCHY_ACTIVE_CODE ('A'), return true. Else return false.
		 *
		 * @param commodity Commodity to set active switch on.
		 * @returns {boolean} True if commodity is active, false otherwise.
		 */
		self.isCommodityActive = function (commodity) {
			if(commodity.hasOwnProperty('active')){
				return commodity.active;
			}
			return commodity.classCommodityActive === HIERARCHY_ACTIVE_CODE;
		}

		/**
		 * This function returns the value for class commodity active. If the commodity is active,
		 * return HIERARCHY_ACTIVE_CODE('A'), else return HIERARCHY_INACTIVE_CODE('I').
		 *
		 * @param commodity Commodity to set active switch on.
		 * @returns {String} ('A') if commodity is active, ('I') otherwise.
		 */
		self.getCommodityActiveValue = function (commodity) {
			return commodity.active ? HIERARCHY_ACTIVE_CODE : HIERARCHY_INACTIVE_CODE;
		}

		/**
		 * This function returns whether or not active checkbox on sub-commodity is disabled or not.
		 * This is done by calling the permissions service getPermissions method passing in the resource for
		 * sub-commodity active: 'PH_SUBC_04', for edit access. If the user has the appropriate permissions,
		 * this function returns false; else return true.
		 *
		 * @returns {boolean} True if checkbox should be disabled, false otherwise.
		 */
		self.isSubCommodityActiveDisabled = function(){
			return !permissionsService.getPermissions('PH_SUBC_04', 'EDIT');
		}

		/**
		 * This function returns whether or not food stamp checkbox on sub-commodity is disabled or not.
		 * This is done by calling the permissions service getPermissions method passing in the resource for
		 * sub-commodity food stamp eligible: 'PH_SUBC_06', for edit access. If the user has the appropriate permissions,
		 * this function returns false; else return true.
		 *
		 * @returns {boolean} True if checkbox should be disabled, false otherwise.
		 */
		self.isSubCommodityFoodStampDisabled = function(){
			return !permissionsService.getPermissions('PH_SUBC_06', 'EDIT');
		}

		/**
		 * This function returns whether or not tax eligible checkbox on sub-commodity is disabled or not.
		 * This is done by calling the permissions service getPermissions method passing in the resource for
		 * sub-commodity tax eligible: 'PH_SUBC_07', for edit access. If the user has the appropriate permissions,
		 * this function returns false; else return true.
		 *
		 * @returns {boolean} True if checkbox should be disabled, false otherwise.
		 */
		self.isSubCommodityTaxEligibleDisabled = function(){
			return !permissionsService.getPermissions('PH_SUBC_07', 'EDIT');
		}

		/**
		 * This function gets whether or not a sub-commodity is active. If the sub-commodity has an attribute
		 * 'active', use that value. Else if the sub-commodity's subCommodityActive equals
		 * the HIERARCHY_ACTIVE_CODE ('A'), return true. Else return false.
		 *
		 * @param subCommodity SubCommodity to set active switch on.
		 * @returns {boolean} True if sub-commodity is active, false otherwise.
		 */
		self.isSubCommodityActive = function (subCommodity) {
			if(subCommodity.hasOwnProperty('active')){
				return subCommodity.active;
			}
			return subCommodity.subCommodityActive === HIERARCHY_ACTIVE_CODE;
		}

		/**
		 * This function returns the value for sub commodity active. If the sub-commodity is active,
		 * return HIERARCHY_ACTIVE_CODE('A'), else return HIERARCHY_INACTIVE_CODE('I').
		 *
		 * @param subCommodity SubCommodity to set active switch on.
		 * @returns {String} ('A') if sub-commodity is active, ('I') otherwise.
		 */
		self.getSubCommodityActiveValue = function (subCommodity) {
			return subCommodity.active ? HIERARCHY_ACTIVE_CODE : HIERARCHY_INACTIVE_CODE;
		}

		/**
		 * This method returns a unique element id for a given product hierarchy level based on the current selected
		 * lowest level, and the indices from any higher levels. Since department is inside of a panel-heading,
		 * department was not required to call this function. This function is used for sub-department, item-class,
		 * commodity, and sub-commodity.
		 *
		 * @param departmentIndex The index of the department.
		 * @param subDepartmentIndex The index of the sub-department.
		 * @param itemClassIndex The index of the item-class.
		 * @param commodityIndex The index of the commodity.
		 * @param subCommodityIndex The index of the sub-commodity.
		 * @returns {String} A unique string used for id generation (i.e. 'subDepartment1-4' or 'commodity2-5-12-8).
		 */
		self.getUniqueElementIdFromIndices = function (departmentIndex, subDepartmentIndex, itemClassIndex, commodityIndex, subCommodityIndex) {
			var uniqueId;
			// sub department level
			if(itemClassIndex === null){
				uniqueId = SUB_DEPARTMENT_LEVEL_ELEMENT_ID_PREFIX + departmentIndex + STRING_HYPHEN + subDepartmentIndex;
			}
			// item class level
			else if(commodityIndex === null){
				uniqueId = ITEM_CLASS_LEVEL_ELEMENT_ID_PREFIX + departmentIndex + STRING_HYPHEN + subDepartmentIndex
					+ STRING_HYPHEN + itemClassIndex;
			}
			// commodity level
			else if(subCommodityIndex === null){
				uniqueId = COMMODITY_LEVEL_ELEMENT_ID_PREFIX + departmentIndex + STRING_HYPHEN + subDepartmentIndex
					+ STRING_HYPHEN + itemClassIndex + STRING_HYPHEN + commodityIndex;
			}
			// sub commodity level
			else {
				uniqueId = SUB_COMMODITY_LEVEL_ELEMENT_ID_PREFIX + departmentIndex + STRING_HYPHEN + subDepartmentIndex
					+ STRING_HYPHEN + itemClassIndex + STRING_HYPHEN + commodityIndex + STRING_HYPHEN + subCommodityIndex;
			}
			return uniqueId;
		}

		/**
		 * This function will retrieve an empty item class used for editing.
		 */
		self.getItemClassTemplate = function (){
			if(updateItemClassTemplate === null && self.doesUserHaveEditAccessOnItemClass()) {
				productHierarchyApi.getEmptyItemClassForUpdate({}, self.loadItemClassTemplate, self.fetchError);
			}
		}

		/**
		 * This method returns whether a user has at least one edit access association for any of the item class
		 * editable fields.
		 *
		 * @returns {boolean} True if a user has at least one editable item-class association, false otherwise.
		 */
		self.doesUserHaveEditAccessOnItemClass = function (){
			return true;
		}

		/**
		 * Callback method for retrieving an empty item class used for editing.
		 *
		 * @param result An empty item class.
		 */
		self.loadItemClassTemplate = function (result){
			updateItemClassTemplate = result;
		}

		/**
		 * This function gets whether or not a commodity is active. If the commodity has an attribute 'active', use
		 * that value. Else if the commodity's classCommodityActive equals
		 * the HIERARCHY_ACTIVE_CODE ('A'), return true. Else return false.
		 *
		 * @param commodity Commodity to set active switch on.
		 * @returns {boolean} True if commodity is active, false otherwise.
		 */
		self.isCommodityActive = function (commodity) {
			if(commodity.hasOwnProperty('active')){
				return commodity.active;
			}
			return commodity.classCommodityActive === HIERARCHY_ACTIVE_CODE;
		}

		/**
		 * This function will retrieve an empty commodity used for editing.
		 */
		self.getCommodityTemplate = function (){
			if(self.updateCommodityTemplate === null && self.doesUserHaveEditAccessOnCommodity()) {
				productHierarchyApi.getEmptyCommodityForUpdate({}, self.loadCommodityTemplate, self.fetchError);
			}
		}

		/**
		 * This method returns whether a user has at least one edit access association for any of the commodity
		 * editable fields.
		 *
		 * @returns {boolean} True if a user has at least one editable commodity association, false otherwise.
		 */
		self.doesUserHaveEditAccessOnCommodity = function (){
			return true;
		}

		/**
		 * Callback method for retrieving an empty commodity used for editing.
		 *
		 * @param result An empty commodity.
		 */
		self.loadCommodityTemplate = function (result){
			self.updateCommodityTemplate = result;
		}

		/**
		 * This function resets sub-commodity editable fields to their original values.
		 */
		self.resetSubCommodityEditableFields = function (){
			self.subCommoditySelected.name = self.originalSubCommoditySelected.name;
			self.subCommoditySelected.taxCategoryCode = self.originalSubCommoditySelected.taxCategoryCode;
			self.subCommoditySelected.nonTaxCategoryCode = self.originalSubCommoditySelected.nonTaxCategoryCode;
			self.subCommoditySelected.productCategoryId = self.originalSubCommoditySelected.productCategoryId;
			self.subCommoditySelected.imsCommodityCode = self.originalSubCommoditySelected.imsCommodityCode;
			self.subCommoditySelected.foodStampEligible = self.originalSubCommoditySelected.foodStampEligible;
			self.subCommoditySelected.taxEligible = self.originalSubCommoditySelected.taxEligible;
			self.subCommoditySelected.active = self.isSubCommodityActive(self.originalSubCommoditySelected);
		}

		/**
		 * This method returns whether a user has at least one edit access association for any of the sub-commodity
		 * editable fields that do not use a save modal.
		 *
		 * @returns {boolean} True if a user has at least one editable sub-commodity association, false otherwise.
		 */
		self.doesUserHaveEditAccessOnSubCommodity = function (){
			return true;
		}

		/**
		 * This function will retrieve an empty sub-commodity used for editing.
		 */
		self.getSubCommodityTemplate = function (){
			if(self.updateSubCommodityTemplate === null && self.doesUserHaveEditAccessOnSubCommodity()) {
				productHierarchyApi.getEmptySubCommodityForUpdate({}, self.loadSubCommodityTemplate, self.fetchError);
			}
		}

		/**
		 * This method returns whether a user has at least one edit access association for any of the sub-commodity
		 * editable fields that do not use a save modal.
		 *
		 * @returns {boolean} True if a user has at least one editable sub-commodity association, false otherwise.
		 */
		self.doesUserHaveEditAccessOnSubCommodity = function (){
			return true;
		}

		/**
		 * Callback method for retrieving an empty sub-commodity used for editing.
		 *
		 * @param result An empty sub-commodity.
		 */
		self.loadSubCommodityTemplate = function (result){
			self.updateSubCommodityTemplate = result;
		}

		/**
		 * This function clears the product hierarchy search, and resets the product hierarchy to the default
		 * non-filtered search.
		 *
		 */
		self.clearProductHierarchySearch = function () {
			self.productHierarchySearchText = null;
            self.data = JSON.parse(JSON.stringify(self.originalData));
			self.error = null;
		}

		/**
		 * This function updates the view of the product hierarchy when a user clicks on the 'search' button, based
		 * on what the user has typed in the input by calling the api to get the searched values.
		 */
		self.updateProductHierarchyViewOnSearch = function () {

			if (self.productHierarchySearchText === undefined || self.productHierarchySearchText == null) {
				return;
			}

			self.initializeProductHierarchySearchValues();

			// this search for product hierarchy levels had to be moved to the api because some browsers were taking
			// approximately 15 seconds in worst case searches, and other browsers were taking less than 5 seconds.
			productHierarchyApi.getProductHierarchyBySearch(
				{searchString: self.productHierarchySearchText, searchType:self.searchType},
				self.loadProductHierarchySearch,
				self.fetchError
			);
		}

		/**
		 * This is the callback function for getting the product hierarchy based on a search string.
		 */
		self.loadProductHierarchySearch = function (results){
			self.data = results;

			self.expandAllProductHierarchyValues();
			self.resetProductHierarchySearchValues();
		}

		/**
		 * This function sets the collapsed variable to false for all current levels of the product hierarchy.
		 */
		self.expandAllProductHierarchyValues = function (){
			angular.forEach(self.data, function (department) {
				department.isCollapsed = false;

				// for each sub department in department's subDepartmentList
				angular.forEach(department.subDepartmentList, function (subDepartment) {
					subDepartment.isCollapsed = false;

					// for each item class in sub department's itemClasses
					angular.forEach(subDepartment.itemClasses, function (itemClass) {
						itemClass.isCollapsed = false;

						// for each commodity in item class's commodityList
						angular.forEach(itemClass.commodityList, function (commodity) {
							commodity.isCollapsed = false;
						});
					});
				});
			});
		}

		/**
		 * This is the callback function for getting the product hierarchy based on a search string.
		 */
		self.loadProductHierarchySearch = function (results){
			self.data = results;

			self.expandAllProductHierarchyValues();
			self.resetProductHierarchySearchValues();
		}

		/**
		 * This function resets variables used for the product hierarchy search to their default values.
		 */
		self.resetProductHierarchySearchValues = function (){
			self.searchingForProductHierarchy = false;
			self.searchingForProductHierarchyText = null;
		}

		/**
		 * This function sets the current search text based on the text the user has searched for.
		 */
		self.setCurrentProductHierarchySearchText = function (){
			self.searchingForProductHierarchyText =
				self.searchingForProductHierarchyTextTemplate.replace("$searchText", self.productHierarchySearchText);
		}

		/**
		 * This function initializes the product hierarchy search values by setting the search text, and setting
		 * searching for product hierarchy to true.
		 */
		self.initializeProductHierarchySearchValues = function (){
			self.setCurrentProductHierarchySearchText();
			self.searchingForProductHierarchy = true;
		}

		/**
		 * Add the selected item to the search box
		 */
		self.addSelectedItemToSearch = function (){
			var department = null;
			var subDepartment = null;
			var itemClass = null;
			var commodity = null;
			var subCommodity = null;

			if (self.subCommoditySelected !== null) {
				subCommodity = self.subCommoditySelected;
			} else if (self.commoditySelected !== null) {
				commodity = self.commoditySelected;
			} else if (self.itemClassSelected !== null) {
				itemClass = self.itemClassSelected;
			} else if (self.subDepartmentSelected !== null) {
				subDepartment = self.subDepartmentSelected;
			} else if (self.departmentSelected !== null) {
				department = self.departmentSelected;
			}

			// call parent component with selected item
			self.onUpdate(
				{	selectedItem:
					{
						department:department,
						subDepartment:subDepartment,
						class: itemClass,
						commodity: commodity,
						subCommodity: subCommodity
					}
				}
			);
		}

		/**
		 * watcher scope event for default value search when back search page.
		 */
		$scope.$on('initProductHierarchySearchSelection', function(event) {
			self.clearProductHierarchySearch();
		});
		
	}
})();
