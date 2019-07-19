/*
 * productHierarchyDefaultsController.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the product hierarchy defaults.
 *
 * @author m314029
 * @since 2.4.0
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('ProductHierarchyDefaultsController', productHierarchyDefaultsController);

	productHierarchyDefaultsController.$inject = ['ProductHierarchyApi', '$scope', 'PermissionsService', '$timeout', 'codeTableApi', 'UserApi', 'ngTableParams', '$filter'];

	function productHierarchyDefaultsController(productHierarchyApi, $scope, permissionsService, $timeout, codeTableApi, userApi, ngTableParams, $filter) {
		var self = this;

		self.success = null;
		self.error = null;
		self.data = [];
		self.departmentSelected = null;
		self.originalDepartmentSelected = null;
		self.departmentIsCollapsed = true;
		self.subDepartmentSelected = null;
		self.originalSubDepartmentSelected = null;
		self.subDepartmentIsCollapsed = true;
		self.itemClassSelected = null;
		self.originalItemClassSelected = null;
		self.itemClassIsCollapsed = true;
		self.commoditySelected = null;
		self.originalCommoditySelected = null;
		self.commodityIsCollapsed = true;
		self.subCommoditySelected = null;
		self.originalSubCommoditySelected = null;
		self.subCommodityIsCollapsed = true;
		self.isWaitingForResponse = false;
		self.selectedHierarchyLevel = null;
		self.allSellingRestrictionsOriginal = [];
		self.sellingRestrictionChanges = [];
		self.allSellingRestrictionsChecked = false;
		self.allShippingRestrictionsOriginal = [];
		self.shippingRestrictionChanges = [];
		self.allShippingRestrictionsChecked = false;
		self.lowerLevelRestricionsOverrideCallback = null;
		self.waitingForUpdate = false;
		self.productHierarchySearchText = null;
		self.searchingForProductHierarchy = false;
		self.preferredUnitOfMeasureSequencesTemplate = [1,2];
		self.searchOption = "Product Hierarchy";

		var updateItemClassTemplate = null;
		var updateCommodityTemplate = null;
		var updateSubCommodityTemplate = null;
		var allProductStateWarnings = null;
		var subCommodityStateWarningTemplate = null;
		var allPreferredUnitsOfMeasure = null;
		var preferredUnitOfMeasureTemplate = null;

		self.DEPARTMENT = "DEPARTMENT";
		self.SUB_DEPARTMENT = "SUB_DEPARTMENT";
		self.ITEM_CLASS = "ITEM_CLASS";
		self.COMMODITY = "COMMODITY";
		self.SUB_COMMODITY = "SUB_COMMODITY";
		self.ADD_ACTION_CODE = "A";
		self.DELETE_ACTION_CODE = "D";
		self.UPDATE_ACTION_CODE = "U";
		self.ERROR_NO_CHANGES_DETECTED = "No changes detected.";
		self.UPDATE_SUCCESS_MESSAGE = "Successfully updated.";
		self.GUI_INHERITED_SELLING_RESTRICTIONS_NOTE = "Inherited selling restrictions can only be removed at the original level.";
		self.NEW_LINE = "\n";
		self.updateMessage = "Updating...please wait.";
		self.productHierarchySearchTitle = "Please enter name or id of product hierarchy level to search for.";
		self.productHierarchySearchButtonTitle = "Search for entered text within product hierarchy.";
		self.productHierarchySearchClearButtonTitle = "Clear search for product hierarchy.";
		self.searchingForProductHierarchyTextTemplate = "Searching product hierarchy for: $searchText. Please wait...";
		self.searchingForProductHierarchyText = null;
		self.REQUIRED_FIELD_ERROR_TEXT = "Field is required.";
		self.FIVE_NUMBER_NON_ZERO_REGEX_ERROR_TEXT = "Must match the format (#####)(up to five numbers) and not be zero equivalent.";
		self.THREE_NUMBER_NON_ZERO_REGEX_ERROR_TEXT = "Must match the format (###)(up to three numbers) and not be zero equivalent.";
		self.NINE_NUMBER_REGEX_ERROR_TEXT = "Must match the format (#########)(up to nine numbers) and not be zero equivalent.";
        self.ALPHABET_AND_NUMBER_REGEX_ERROR_TEXT = "Sub-Commodity must be alphabet and number."
		self.GENERAL_TAB_NAME = "General";
		self.DEFAULTS_TAB_NAME = "Defaults";
		self.previousSelectedLevel = null;
        self.resetFullHierarchy = false;
        self.waittingForResetFullHierarchy = "Data is Loading... Please wait..."
		var HIERARCHY_ACTIVE_CODE = 'A';
		var HIERARCHY_INACTIVE_CODE = 'I';
		var currentSelectedLevel =  null;
		/**
		 * Constant order by asc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_ASC = "asc";
		/**
		 * Constant order by desc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_DESC = "desc";
		/**
		 * List of available tax Category
		 * @type {Array}
		 */
		self.allAvailableTaxCategories =  [];
		/**
		 * The selected vertex tax category
		 * @type {Object}
		 */
		self.selectedVertexTaxCategory = {};
		/**
		 * List of non tax Category
		 * @type {Array}
		 */
		self.allNonTaxCategories =  [];
		/**
		 * List of tax Category
		 * @type {Array}
		 */
		self.allTaxCategories =  [];
		/**
		 * The selected vertex non tax category
		 * @type {Object}
		 */
		self.selectedVertexNonTaxCategory = {};
		/**
		 * BDM display value.
		 * @type {String}
		 */
		self.bdmUserDisplayValue = null;
		/**
		 * EBM display value.
		 * @type {String}
		 */
		self.ebmUserDisplayValue = null;
		/**
		 * BDA display value.
		 * @type {String}
		 */
		self.bdaUserDisplayValue = null;
		/**
		 * BDA display name.
		 * @type {String}
		 */
		self.bdaUserDisplayName = null;
		/**
		 * EBM display name.
		 * @type {String}
		 */
		self.ebmUserDisplayName = null;
		/**
		 * List of Sub-Commodity defaults Audit.
		 * @type {Array}
		 */
		self.subCommodityDefaultsAudit = [];
		/**
		 * Flag show SubCommodityDefaults.
		 * @type {Boolean}
		 */
		self.isShowSubCommodityDefaults = false;
		/**
		 * Product category display name.
		 * @type {String}
		 */
		self.productCategoryDisplayValue = null;
		/**
		 * Product category display name.
		 * @type {String}
		 */
		self.originalProductCategoryDisplayValue = null;
        /**
         * Regrular expression for numbers, use to matching numbers[0-9].
         * @type {String}
         */
        const NUMBERS_ONLY_REGRULAR_EXPRESSION = new RegExp('^[0-9]*$');
        /**
         * Regrular expression for numbers and alphabet, use to matching numbers[0-9] and alphabet[a-zA-Z].
         * @type {String}
         */
        const NUMBERS_ALPHABET_REGULAR_EXPRESSION = new RegExp('^[a-zA-Z0-9]*$');
        /**
         * Product category id invalid flag.
         * @type {Boolean}
         */
        self.prodCategoryIdInvalid = false;
        /**
         * IMS Sub-comm Code invalid flag.
         * @type {Boolean}
         */
        self.iMSSubCommCodeInvalid = false;
		/**
		 * Initialize necessary components on page load.
		 */
		self.init = function(){
			self.getAllVertexTaxCategories();
			self.isWaitingForResponse = true;
			self.setHierarchyLevelTabsHeight();
			self.setHierarchyLevelTabsHeightOnBrowserResize();
			productHierarchyApi.getFullHierarchy({}, self.loadData, fetchError);

			// if user has edit access for shipping restrictions, get all shipping restrictions
			if(permissionsService.getPermissions('PH_DFLT_01', 'EDIT')) {
				productHierarchyApi.findAllShippingRestrictions({}, self.loadAllShippingRestrictions, fetchError);
			}

			// if user has edit access for selling restrictions, get all selling restrictions
			if(permissionsService.getPermissions('PH_DFLT_02', 'EDIT')) {
				productHierarchyApi.findAllSellingRestrictions({}, self.loadAllSellingRestrictions, fetchError);
			}

			// if user has edit access for merchant types, get all merchant types
			if(permissionsService.getPermissions('PH_DFLT_02', 'EDIT')) {
				productHierarchyApi.getAllMerchantTypes({}, loadMerchantTypes, fetchError);
			}
		};

		/**
		 * Callback for getting all selling restrictions.
		 *
		 * @param results List of all selling restrictions.
		 */
		self.loadAllSellingRestrictions = function (results) {
			self.allSellingRestrictionsOriginal = results;
		};

		/**
		 * Callback for getting all shipping restrictions.
		 *
		 * @param results List of all shipping restrictions.
		 */
		self.loadAllShippingRestrictions = function (results) {
			self.allShippingRestrictionsOriginal = results;
		};

		/**
		 * Sets hierarchy level's default tab's height equal to values of their respective general tab's height.
		 */
		self.setHierarchyLevelTabsHeight = function () {
			self.resizeDepartmentDefaultTab();
			self.resizeSubDepartmentDefaultTab();
			self.resizeItemClassDefaultTab();
		};

		/**
		 * Resets hierarchy level's default tab's height equal to current values of their respective general
		 * tab's height.
		 */
		self.setHierarchyLevelTabsHeightOnBrowserResize = function () {
			$scope.onBrowserResize(self.setHierarchyLevelTabsHeight);
		};

		/**
		 * Callback for a successful call to get product hierarchy data from the back end.
		 *
		 * @param results The data returned by the back end.
		 */
		self.loadData = function (results) {
			self.data = results;
			self.isWaitingForResponse = false;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		function fetchError (error) {
			self.waitingForUpdate = false;
			self.success = null;
			self.isWaitingForResponse = false;
			self.resetFullHierarchy = false;
			self.error = self.getErrorMessage(error);
			resetProductHierarchySearchValues();
		}

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		function handleErrorUpdateSubCommodity (error) {
			self.prodCategoryIdInvalid = false;
			self.iMSSubCommCodeInvalid = false;
			self.waitingForUpdate = false;
			self.success = null;
			self.isWaitingForResponse = false;
			self.error = self.getErrorMessage(error);
			self.productCategoryDisplayValue = angular.copy(self.originalProductCategoryDisplayValue);
			self.subCommoditySelected.imsCommodityCode = self.originalSubCommoditySelected.imsCommodityCode;
			self.subCommoditySelected.imsSubCommodityCode  = self.originalSubCommoditySelected.imsSubCommodityCode;
			resetProductHierarchySearchValues();
		}

		/**
		 * Callback for when call update commodity returns an error.
		 *
		 * @param error The error from the back end.
		 */
		function fetchErrorUpdateCommodity (error) {
			self.waitingForUpdate = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
			resetCommodityEditableFields();
		}

		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			if(error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return "An unknown error occurred.";
			}
		};

		/**
		 * Method called when user selects a department level hierarchy from the full list of product hierarchy.
		 * This method sets the department view to the current selected department, and collapses or opens
		 * its sub-department list depending on its previous state.
		 *
		 * @param department The department selected by the user.
		 */
		self.selectDepartment = function(department){
			deSelectPreviousSelectedLevel();
			self.setDepartment(department, false);
			self.setSubDepartment(null, false);
			self.setItemClass(department, null, null, false);
			self.setCommodity(department, null, null, null, false);
			self.setSubCommodity(department, null, null, null, null, false);

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
			getCurrentShippingRestrictions(department, null, null, null, null);
			getCurrentSellingRestrictions(department, null, null, null, null);
		};

		/**
		 * Method called when user selects a sub-department level hierarchy from the full list of product hierarchy.
		 * This method sets the sub-department view to the current selected sub-department, the department view to
		 * the sub-department's parent, and collapses or opens its item-class list depending on its previous state.
		 *
		 * @param department The selected sub-department's parent department.
		 * @param subDepartment The sub-department selected by the user.
		 */
		self.selectSubDepartment = function (department, subDepartment) {
			deSelectPreviousSelectedLevel();
			self.setDepartment(department, true);
			self.setSubDepartment(subDepartment, false);
			self.setItemClass(department, subDepartment, null, false);
			self.setCommodity(department, subDepartment, null, null, false);
			self.setSubCommodity(department, subDepartment, null, null, null, false);
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
			getCurrentShippingRestrictions(department, subDepartment, null, null, null);
			getCurrentSellingRestrictions(department, subDepartment, null, null, null);
			setSelectedLevel(subDepartment);
		};

		/**
		 * Method called when user selects an item-class level hierarchy from the full list of product hierarchy.
		 * This method sets the item-class view to the current selected item-class, the sub-department view
		 * to the item-class's parent, the department view to the item-class's parent, and collapses or opens the
		 * item-class list depending on its previous state.
		 *
		 * @param department The selected item-class's parent department.
		 * @param subDepartment The selected item-class's parent sub-department.
		 * @param itemClass The item-class selected by the user.
		 */
		self.selectItemClass =
			function (department, subDepartment, itemClass) {
				deSelectPreviousSelectedLevel();
				self.setDepartment(department, true);
				self.setSubDepartment(subDepartment, true);
				self.setItemClass(department, subDepartment, itemClass, false);
				self.setCommodity(department, subDepartment, itemClass, null, false);
				self.setSubCommodity(department, subDepartment, itemClass, null, null, false);

				currentSelectedLevel = itemClass;
				// if original item-class equals selected level
				if(self.selectedHierarchyLevel === self.ITEM_CLASS &&
					self.originalItemClassSelected && self.originalItemClassSelected.itemClassCode === itemClass.itemClassCode){
					// set isCollapsed to opposite of previous value
					itemClass.isCollapsed = !itemClass.isCollapsed;
				} else {
					// else set isCollapsed to false
					itemClass.isCollapsed = false;
				}
				self.selectedHierarchyLevel = self.ITEM_CLASS;

				setSelectedLevel(itemClass);
			};

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
		 */
		self.selectCommodity =
			function (department, subDepartment, itemClass, commodity) {
				deSelectPreviousSelectedLevel();
				self.setDepartment(department, true);
				self.setSubDepartment(subDepartment, true);
				self.setCommodity(department, subDepartment, itemClass, commodity, false);
				self.setSubCommodity(department, subDepartment, itemClass, commodity, null, false);

				currentSelectedLevel = commodity;
				// if original commodity equals selected level
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
				self.selectedHierarchyLevel = self.COMMODITY;

				setSelectedLevel(commodity);
			};

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
		 */
		self.selectSubCommodity =
			function (department, subDepartment, itemClass, commodity, subCommodity) {
				deSelectPreviousSelectedLevel();
				self.setDepartment(department, true);
				self.setSubDepartment(subDepartment, true);
				self.setSubCommodity(department, subDepartment, itemClass, commodity, subCommodity, false);

				currentSelectedLevel = subCommodity;
				self.selectedHierarchyLevel = self.SUB_COMMODITY;

				setSelectedLevel(subCommodity);
			};
		/**
		 * Auto filter tax categories by search text
		 * @param search
		 */
		self.autoFilterForTaxCategories = function(search){
			var output=[];
			angular.forEach(self.allAvailableTaxCategories, function (element) {
				if(element.categoryName.toLowerCase().indexOf(search.toLowerCase())>=0){
					output.push(element);
				}
			});
			self.allTaxCategories = output;

		}
		/**
		 * Auto filter non tax categories by search text
		 * @param search
		 */
		self.autoFilterForNonTaxCategories = function(search){
			var output=[];
			angular.forEach(self.allAvailableTaxCategories, function (element) {
				if(element.categoryName.toLowerCase().indexOf(search.toLowerCase())>=0){
					output.push(element);
				}
			});
			self.allNonTaxCategories = output;

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
		};

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
		};

		/**
		 * This function changes the height of the department default tab to department general tab height. This is
		 * done so the view doesn't 'jump' to different height when switching tabs which makes better user experience.
		 */
		self.resizeDepartmentDefaultTab = function(){
			var generalTabWellHeight = $('#departmentGeneralTabWell').prop('offsetHeight');
			if(generalTabWellHeight !== 0) {
				$('#departmentDefaultsTabWell').css({'height': generalTabWellHeight + 'px'});
			}
		};

		/**
		 * This function changes the height of the subDepartment default tab to subDepartment general tab height.
		 * This is done so the view doesn't 'jump' to different height when switching tabs which makes better user
		 * experience.
		 */
		self.resizeSubDepartmentDefaultTab = function(){
			var generalTabWellHeight = $('#subDepartmentGeneralTabWell').prop('offsetHeight');
			if(generalTabWellHeight !== 0) {
				$('#subDepartmentDefaultsTabWell').css({'height': generalTabWellHeight + 'px'});
			}
		};

		/**
		 * This function changes the height of the itemClass default tab to itemClass general tab height. This is
		 * done so the view doesn't 'jump' to different height when switching tabs which makes better user experience.
		 */
		self.resizeItemClassDefaultTab = function () {
			self.setTabSelectedForSelectedHierarchyLevel(self.itemClassSelected, self.DEFAULTS_TAB_NAME);
			var generalTabWellHeight = $('#itemClassGeneralTabWell').prop('offsetHeight');
			if(generalTabWellHeight !== 0) {
				$('#itemClassDefaultsTabWell').css({'height': generalTabWellHeight + 'px'});
			}
		};

		/**
		 * This function changes the height of the commodity general tab to commodity default tab height. This is
		 * done so the view doesn't 'jump' to different height when switching tabs which makes better user experience.
		 */
		self.resizeCommodityDefaultTab = function () {
			self.setTabSelectedForSelectedHierarchyLevel(self.commoditySelected, self.DEFAULTS_TAB_NAME);
			var generalTabWellHeight = $('#commodityGeneralTabWell').prop('offsetHeight');
			if(generalTabWellHeight !== 0) {
				$('#commodityDefaultsTabWell').css({'height': generalTabWellHeight + 'px'});
			}
		};

		/**
		 * This function changes the height of the subCommodity general tab to subCommodity default tab height. This is
		 * done so the view doesn't 'jump' to different height when switching tabs which makes better user experience.
		 */
		self.resizeSubCommodityGeneralTab = function () {
			var defaultTabWellHeight = $('#subCommodityDefaultsTabWell').prop('offsetHeight');
			if(defaultTabWellHeight !== 0) {
				$('#subCommodityGeneralTabWell').css({'height': defaultTabWellHeight + 'px'});
			}
		};

		/**
		 * Gets the selling restrictions to be used for updating.
		 */
		self.getSellingRestrictionsForUpdate = function(){
			self.getCurrentSellingRestrictionTemplate();
			self.loadSellingRestrictionsForUpdate();
		};

		/**
		 * Gets the shipping restrictions to be used for updating.
		 */
		self.getShippingRestrictionsForUpdate = function(){
			self.getCurrentShippingRestrictionTemplate();
			self.loadShippingRestrictionsForUpdate();
		};

		/**
		 * Function to call when user has selected save from editing selling restrictions. This method takes in
		 * a boolean overrideLowerLevelRestrictions to determine if lower level restrictions should be overridden by
		 * this change or not.
		 *
		 * @param overrideLowerLevelRestrictions Whether or not to override lower level restrictions.
		 */
		self.updateSellingRestrictions = function(overrideLowerLevelRestrictions){
			var originalHierarchySelected = self.getCurrentSelectedHierarchy();
			var currentRestrictions = self.sellingRestrictionsCopy;

			self.sellingRestrictionChanges = self.getChangedSellingRestrictions(currentRestrictions, originalHierarchySelected, overrideLowerLevelRestrictions);
			if(self.sellingRestrictionChanges.length > 0){
				self.error = null;
				self.success = null;
				self.waitingForUpdate = true;
				productHierarchyApi.updateSellingRestrictionHierarchyLevel(
					self.sellingRestrictionChanges, self.setUpdatedSellingRestrictions, fetchError);
			} else {
				fetchError({data: {message: self.ERROR_NO_CHANGES_DETECTED}});
			}
		};

		/**
		 * Function to call when user has selected save from editing shipping restrictions. This method takes in
		 * a boolean overrideLowerLevelRestrictions to determine if lower level restrictions should be overridden by
		 * this change or not.
		 *
		 * @param overrideLowerLevelRestrictions Whether or not to override lower level restrictions.
		 */
		self.updateShippingRestrictions = function(overrideLowerLevelRestrictions){
			var originalHierarchySelected = self.getCurrentSelectedHierarchy();
			var currentRestrictions = self.shippingRestrictionsCopy;

			self.shippingRestrictionChanges = self.getChangedShippingRestrictions(currentRestrictions, originalHierarchySelected, overrideLowerLevelRestrictions);
			if(self.shippingRestrictionChanges.length > 0){
				self.error = null;
				self.success = null;
				self.waitingForUpdate = true;
				productHierarchyApi.updateShippingRestrictionHierarchyLevel(
					self.shippingRestrictionChanges, self.setUpdatedShippingRestrictions, fetchError);
			} else {
				fetchError({data: {message: self.ERROR_NO_CHANGES_DETECTED}});
			}
			self.cancelShippingRestrictions();
		};

		/**
		 * Updates selling restrictions to restrictions updated by user.
		 */
		self.setUpdatedSellingRestrictions = function () {
			self.waitingForUpdate = false;
			self.updateOriginalViewableSellingRestrictions(self.getCurrentSelectedHierarchy());
			self.success = self.UPDATE_SUCCESS_MESSAGE;
		};

		/**
		 * Updates shipping restrictions to restrictions updated by user.
		 */
		self.setUpdatedShippingRestrictions = function () {
			self.waitingForUpdate = false;
			self.updateOriginalViewableShippingRestrictions(self.getCurrentSelectedHierarchy());
			self.success = self.UPDATE_SUCCESS_MESSAGE;
		};

		/**
		 * This method updates the original data viewable selling restrictions to match the change in
		 * selling restrictions. Otherwise, when user selects another hierarchy and returns, the data will not match.
		 *
		 * @param originalSelectedHierarchy The original data to update selling restrictions for.
		 */
		self.updateOriginalViewableSellingRestrictions = function (originalSelectedHierarchy) {
			var originalRestriction;
			var changeRestriction;
			var foundRestrictionIndex;
			for(var changeIndex = 0; changeIndex < self.sellingRestrictionChanges.length; changeIndex++){
				changeRestriction = self.sellingRestrictionChanges[changeIndex];
				foundRestrictionIndex = -1;
				for (var originalIndex = 0; originalIndex < originalSelectedHierarchy.viewableSellingRestrictions.length; originalIndex++) {
					originalRestriction = originalSelectedHierarchy.viewableSellingRestrictions[originalIndex];
					if (changeRestriction.key.restrictionGroupCode === originalRestriction.key.restrictionGroupCode) {
						foundRestrictionIndex = originalIndex;
						break;
					}
				}
				// not found and add action -> add to original list
				if(foundRestrictionIndex === -1 && changeRestriction.actionCode === self.ADD_ACTION_CODE){
					originalSelectedHierarchy.viewableSellingRestrictions.push(changeRestriction);
				}
				// found and delete action -> remove from original list
				else if (foundRestrictionIndex !== -1 && changeRestriction.actionCode === self.DELETE_ACTION_CODE) {
					originalSelectedHierarchy.viewableSellingRestrictions.splice(foundRestrictionIndex, 1);
				}
			}

			// sort the viewable selling restrictions based on description
			originalSelectedHierarchy.viewableSellingRestrictions.sort(
				function(a, b){
					return compareTwoValuesForAscendingSort(
						a.sellingRestriction.restrictionDescription, b.sellingRestriction.restrictionDescription);
				});
		};

		/**
		 * This method updates the original data viewable shipping restrictions to match the change in
		 * shipping restrictions. Otherwise, when user selects another hierarchy and returns, the data will not match.
		 *
		 * @param originalSelectedHierarchy The original data to update shipping restrictions for.
		 */
		self.updateOriginalViewableShippingRestrictions = function (originalSelectedHierarchy) {
			var originalRestriction;
			var changeRestriction;
			var foundRestrictionIndex;
			for(var changeIndex = 0; changeIndex < self.shippingRestrictionChanges.length; changeIndex++){
				changeRestriction = self.shippingRestrictionChanges[changeIndex];
				foundRestrictionIndex = -1;
				for (var originalIndex = 0; originalIndex < originalSelectedHierarchy.viewableShippingRestrictions.length; originalIndex++) {
					originalRestriction = originalSelectedHierarchy.viewableShippingRestrictions[originalIndex];
					if (changeRestriction.key.restrictionCode === originalRestriction.key.restrictionCode) {
						foundRestrictionIndex = originalIndex;
						break;
					}
				}
				// not found and add action -> add to original list
				if(foundRestrictionIndex === -1 && changeRestriction.actionCode === self.ADD_ACTION_CODE){
					originalSelectedHierarchy.viewableShippingRestrictions.push(changeRestriction);
				}
				// found and update action -> change applicable at this level from original list
				else if (foundRestrictionIndex !== -1 && changeRestriction.actionCode === self.UPDATE_ACTION_CODE) {
					originalSelectedHierarchy.viewableShippingRestrictions[foundRestrictionIndex].applicableAtThisLevel =
						changeRestriction.applicableAtThisLevel;
				}
				// found and delete action -> remove from original list
				else if (foundRestrictionIndex !== -1 && changeRestriction.actionCode === self.DELETE_ACTION_CODE) {
					originalSelectedHierarchy.viewableShippingRestrictions.splice(foundRestrictionIndex, 1);
				}
			}

			// sort the viewable shipping restrictions based on description
			originalSelectedHierarchy.viewableShippingRestrictions.sort(
				function(a, b){
					return compareTwoValuesForAscendingSort(
						a.sellingRestrictionCode.restrictionDescription, b.sellingRestrictionCode.restrictionDescription);
				});
		};

		/**
		 * Returns the comparison between two objects as a number for an ascending sort. If the first value should
		 * come before the second, return -1. If the first value should come after the second, return 1. If they are
		 * the same, return 0;
		 *
		 * @param firstToCompare First element to compare.
		 * @param secondToCompare Second element to compare.
		 * @returns {number} 1 if first element comes after second, -1 if first element comes before second, and 0 if same.
		 */
		function compareTwoValuesForAscendingSort(firstToCompare, secondToCompare){
			return ((firstToCompare < secondToCompare) ?
				-1 : (firstToCompare > secondToCompare) ? 1 : 0)
		}

		/**
		 * This method calls the back end to get an empty selling restriction with pre-filled values matching the
		 * current selected hierarchy.
		 */
		self.getCurrentSellingRestrictionTemplate = function () {
			var parameters;

			switch(self.selectedHierarchyLevel){
				case self.DEPARTMENT: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, null, null, null, null);
					break;
				}
				case self.SUB_DEPARTMENT: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, self.subDepartmentSelected, null, null, null);
					break;
				}
				case self.ITEM_CLASS: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, self.subDepartmentSelected, self.itemClassSelected, null, null);
					break;
				}
				case self.COMMODITY: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, self.subDepartmentSelected, self.itemClassSelected,
						self.commoditySelected, null);
					break;
				}
				case self.SUB_COMMODITY: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, self.subDepartmentSelected, self.itemClassSelected,
						self.commoditySelected, self.subCommoditySelected);
					break;
				}
			}

			// get an empty selling restriction template with information matching current selected hierarchy
			productHierarchyApi.getSellingRestrictionHierarchyLevelTemplate(
				parameters,
				function (result) {
					self.sellingRestrictionTemplate = result;
				},
				fetchError
			);
		};

		/**
		 * This method calls the back end to get an empty shipping restriction with pre-filled values matching the
		 * current selected hierarchy.
		 */
		self.getCurrentShippingRestrictionTemplate = function () {
			var parameters;

			switch(self.selectedHierarchyLevel){
				case self.DEPARTMENT: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, null, null, null, null);
					break;
				}
				case self.SUB_DEPARTMENT: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, self.subDepartmentSelected, null, null, null);
					break;
				}
				case self.ITEM_CLASS: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, self.subDepartmentSelected, self.itemClassSelected, null, null);
					break;
				}
				case self.COMMODITY: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, self.subDepartmentSelected, self.itemClassSelected,
						self.commoditySelected, null);
					break;
				}
				case self.SUB_COMMODITY: {
					parameters = getProductHierarchyParameters(
						self.departmentSelected, self.subDepartmentSelected, self.itemClassSelected,
						self.commoditySelected, self.subCommoditySelected);
					break;
				}
			}

			// get an empty selling restriction template with information matching current selected hierarchy
			productHierarchyApi.getShippingRestrictionHierarchyLevelTemplate(
				parameters,
				function (result) {
					self.shippingRestrictionTemplate = result;
				},
				fetchError
			);
		};

		/**
		 * This method sets all selling restriction's 'checked' value as true or false, depending on if the user has
		 * selected to check all or un-check all.
		 */
		self.setAllSellingRestrictions = function () {
			for(var index = 0; index < self.sellingRestrictionsCopy.length; index++){
				self.sellingRestrictionsCopy[index].checked = self.allSellingRestrictionsChecked;
			}
		};

		/**
		 * This method sets all shipping restriction's 'checked' value as true or false, depending on if the user has
		 * selected to check all or un-check all.
		 */
		self.setAllShippingRestrictions = function () {
			// Get filter criteria.
			var filter = self.getShippingRestrictionFilter();
			// Get filtered data
			var filteredData = filter !=null? $filter('filter')(self.shippingRestrictionsCopy, filter) : self.shippingRestrictionsCopy;
			for(var index = 0; index < filteredData.length; index++){
				filteredData[index].applicableAtThisLevel = self.allShippingRestrictionsChecked;
			}
		};

		/**
		 * Sets whether viewed department should be collapsed or not
		 * --if department is lowest level, do not collapse, else do collapse
		 * Sets department selected to received department value, and sets selling restrictions at the department level.
		 *
		 * @param department The department selected by the user.
		 * @param collapse Whether department view should be collapsed or not.
		 */
		self.setDepartment = function (department, collapse) {
			self.clearSuccessOrErrorMessaage();
			self.departmentIsCollapsed = collapse;
			self.departmentSelected = angular.copy(department);
		};

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
		};

		/**
		 * Sets whether viewed itemClass should be collapsed or not
		 * --if itemClass is lowest level, do not collapse, else do collapse
		 * Sets itemClass selected to received itemClass value, and sets selling restrictions at the
		 * itemClass level.
		 *
		 * @param itemClass The itemClass selected by the user.
		 * @param collapse Whether itemClass view should be collapsed or not.
		 */
		self.setItemClass = function (department, subDepartment, itemClass, collapse) {
			self.itemClassIsCollapsed = collapse;
			if(itemClass !== null){
				var parameters = {
					itemClassCode: itemClass.itemClassCode
				};
				productHierarchyApi.getCurrentItemClassInfo(
					parameters,
					function (result) {
						setActualItemClass(result);

						self.setTabSelectedForSelectedHierarchyLevel(self.itemClassSelected, self.GENERAL_TAB_NAME);
						getItemClassTemplate();
						getCurrentShippingRestrictions(department, subDepartment, itemClass, null, null);
						getCurrentSellingRestrictions(department, subDepartment, itemClass, null, null);
					},
					fetchError);
			} else {
				self.itemClassSelected = null;
			}
		};

		/**
		 * Sets whether viewed commodity should be collapsed or not
		 * --if commodity is lowest level, do not collapse, else do collapse
		 * Sets commodity selected to received commodity value, and sets selling restrictions at the
		 * commodity level.
		 *
		 * @param commodity The commodity selected by the user.
		 * @param collapse Whether commodity view should be collapsed or not.
		 */
		self.setCommodity = function (department, subDepartment, itemClass, commodity, collapse) {
			self.commodityIsCollapsed = collapse;
			if(commodity !== null){
				var key = commodity.key;
				productHierarchyApi.getCurrentCommodityClassInfo(
					key,
					function (result) {
						setActualCommodity(result);

						self.setTabSelectedForSelectedHierarchyLevel(self.commoditySelected, self.GENERAL_TAB_NAME);
						getCommodityTemplate();
						getCurrentShippingRestrictions(department, subDepartment, itemClass, commodity, null);
						getCurrentSellingRestrictions(department, subDepartment, itemClass, commodity, null);
					},
					fetchError);
			} else {
				self.commoditySelected = null;
			}
		};

		/**
		 * Sets whether viewed sub-commodity should be collapsed or not
		 * --if sub-commodity is lowest level, do not collapse, else do collapse
		 * Sets sub-commodity selected to received sub-commodity value, and sets selling restrictions at the
		 * sub-commodity level.
		 *
		 * @param subCommodity The sub-commodity selected by the user.
		 * @param collapse Whether sub-commodity view should be collapsed or not.
		 */
		self.setSubCommodity = function (department, subDepartment, itemClass, commodity, subCommodity, collapse) {
            self.prodCategoryIdInvalid = false;
            self.iMSSubCommCodeInvalid = false;
			self.subCommodityIsCollapsed = collapse;
			if(subCommodity !== null){
				var key = subCommodity.key;
				productHierarchyApi.getCurrentSubCommodityClassInfo(
					key,
					function (result) {
						self.originalSubCommoditySelected = result;
						self.originalSubCommoditySelected.active = isSubCommodityActive(subCommodity);

						self.subCommoditySelected = angular.copy(self.originalSubCommoditySelected);
						self.productCategoryDisplayValue = self.subCommoditySelected.productCategory.productCategoryId + " - " +
														   self.subCommoditySelected.productCategory.productCategoryName.trim();
						self.originalProductCategoryDisplayValue = angular.copy(self.productCategoryDisplayValue);
						self.initDataForTaxAndNonTaxCategoryDropDown();
						setActualCommodity(result.commodityMaster);

						getSubCommodityTemplate();
						getCurrentShippingRestrictions(department, subDepartment, itemClass, commodity, subCommodity);
						getCurrentSellingRestrictions(department, subDepartment, itemClass, commodity, subCommodity);

					},
					fetchError);
			} else {
				self.subCommoditySelected = null;
			}
		};

		/**
		 * This method takes the selling restrictions from the current selected hierarchy and compares it with the all
		 * selling restrictions list. For each restriction that was in the selling restrictions, set that value in the
		 * copy of all selling restrictions list to be checked, then set that list copy.
		 */
		self.loadSellingRestrictionsForUpdate = function () {
			var sellingRestrictions = this.getCurrentSelectedHierarchy().viewableSellingRestrictions;
			var currentRestrictionIndexFound;
			var inherited;

			// get a copy of the full selling restrictions list
			self.sellingRestrictionsCopy = angular.copy(self.allSellingRestrictionsOriginal);

			// for each selling restriction in the selected hierarchy
			for (var currentRestrictionIndex = 0; currentRestrictionIndex < sellingRestrictions.length; currentRestrictionIndex++) {
				currentRestrictionIndexFound = null;

				// for each restriction in the full selling restriction copy list
				for (var index = 0; index < self.sellingRestrictionsCopy.length; index++) {

					// if the restriction group codes match
					if (sellingRestrictions[currentRestrictionIndex].key.restrictionGroupCode ===
						self.sellingRestrictionsCopy[index].restrictionGroupCode) {
						currentRestrictionIndexFound = index;
						inherited = sellingRestrictions[currentRestrictionIndex].inherited;
						break;
					}
				}

				// if the restriction was in the selected hierarchy list, update the selling restriction copy list
				// value to be checked
				if (currentRestrictionIndexFound !== null) {
					self.sellingRestrictionsCopy[currentRestrictionIndexFound].checked = true;
					self.sellingRestrictionsCopy[currentRestrictionIndexFound].inherited = inherited;
				}
			}
		};

		/**
		 * This method takes the shipping restrictions from the current selected hierarchy and compares it with the all
		 * shipping restrictions list. For each restriction that was in the shipping restrictions, set that value in
		 * the copy of all shipping restrictions list to be checked, then set that list copy.
		 */
		self.loadShippingRestrictionsForUpdate = function () {
			var shippingRestrictions = self.getCurrentSelectedHierarchy().viewableShippingRestrictions;
			var currentRestrictionIndexFound;
			var currentRestriction;
			var inherited;

			// get a copy of the full shipping restrictions list
			self.shippingRestrictionsCopy = angular.copy(self.allShippingRestrictionsOriginal);

			// for each shipping restriction in the selected hierarchy
			for (var currentRestrictionIndex = 0; currentRestrictionIndex < shippingRestrictions.length; currentRestrictionIndex++) {

				currentRestriction = shippingRestrictions[currentRestrictionIndex];

				currentRestrictionIndexFound = null;

				// for each restriction in the full shipping restriction copy list
				for (var index = 0; index < self.shippingRestrictionsCopy.length; index++) {

					// if the restriction codes match
					if (currentRestriction.key.restrictionCode === self.shippingRestrictionsCopy[index].restrictionCode) {
						currentRestrictionIndexFound = index;
						inherited = shippingRestrictions[currentRestrictionIndex].inherited;
						break;
					}
				}

				// if the restriction was in the selected hierarchy list, update the shipping restriction copy list
				// value to be the same as current restriction's applicableAtThisLevel
				if (currentRestrictionIndexFound !== null) {
					self.shippingRestrictionsCopy[currentRestrictionIndexFound].applicableAtThisLevel = currentRestriction.applicableAtThisLevel;
					self.shippingRestrictionsCopy[currentRestrictionIndexFound].inherited = inherited;
				}
			}
		};

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
		};

		/**
		 * This method returns the current state of the data from the selected hierarchy based on the selected hierarchy
		 * level.
		 *
		 * @returns {Object}
		 */
		self.getCurrentSelectedHierarchy = function(){
			switch(self.selectedHierarchyLevel){
				case self.DEPARTMENT:{
					return self.departmentSelected;
				}
				case self.SUB_DEPARTMENT:{
					return self.subDepartmentSelected;
				}
				case self.ITEM_CLASS:{
					return self.itemClassSelected;
				}
				case self.COMMODITY:{
					return self.commoditySelected;
				}
				case self.SUB_COMMODITY:{
					return self.subCommoditySelected;
				}
			}
		};

		/**
		 * Gets the changed selling restrictions by comparing the selected hierarchy with the original selected
		 * hierarchy. This method adds action codes to the selling restrictions ("A" for add, and "D" for delete).
		 *
		 * @param currentRestrictions The current state of the selling restrictions.
		 * @param originalSelectedHierarchy The original state of the selected hierarchy.
		 * @param overrideLowerLevelRestrictions Whether any lower level restrictions should be overridden by this change.
		 * @returns {Array} List of changed selling restrictions.
		 */
		self.getChangedSellingRestrictions = function(currentRestrictions, originalSelectedHierarchy, overrideLowerLevelRestrictions){
			var sellingRestrictionChanges = [];
			var currentUpdatedSellingRestriction;
			var existingSellingRestriction;
			var sellingRestrictionTemplate;
			var foundRestriction;
			var newIndex;
			var oldIndex;

			// get added selling restrictions
			for(newIndex = 0; newIndex < currentRestrictions.length; newIndex++) {
				currentUpdatedSellingRestriction = currentRestrictions[newIndex];
				foundRestriction = false;
				for(oldIndex = 0; oldIndex < originalSelectedHierarchy.viewableSellingRestrictions.length; oldIndex++) {
					existingSellingRestriction = originalSelectedHierarchy.viewableSellingRestrictions[oldIndex];
					if(currentUpdatedSellingRestriction.restrictionGroupCode === existingSellingRestriction.key.restrictionGroupCode){
						foundRestriction = true;
						break;
					}
				}
				if(!foundRestriction && currentUpdatedSellingRestriction.checked){
					sellingRestrictionTemplate = angular.copy(self.sellingRestrictionTemplate);
					sellingRestrictionTemplate.key.restrictionGroupCode = currentUpdatedSellingRestriction.restrictionGroupCode;
					sellingRestrictionTemplate.actionCode = self.ADD_ACTION_CODE;
					sellingRestrictionTemplate.overrideLowerLevels = overrideLowerLevelRestrictions;
					sellingRestrictionTemplate.sellingRestriction = currentUpdatedSellingRestriction;
					sellingRestrictionChanges.push(sellingRestrictionTemplate);
				}
			}

			// get removed selling restrictions
			for(oldIndex = 0; oldIndex < originalSelectedHierarchy.viewableSellingRestrictions.length; oldIndex++) {
				existingSellingRestriction = originalSelectedHierarchy.viewableSellingRestrictions[oldIndex];
				foundRestriction = false;
				for(newIndex = 0; newIndex < currentRestrictions.length; newIndex++) {
					currentUpdatedSellingRestriction = currentRestrictions[newIndex];
					if(currentUpdatedSellingRestriction.restrictionGroupCode === existingSellingRestriction.key.restrictionGroupCode){
						foundRestriction = true;
						break;
					}
				}
				if(foundRestriction && !currentUpdatedSellingRestriction.checked){
					sellingRestrictionTemplate = angular.copy(self.sellingRestrictionTemplate);
					sellingRestrictionTemplate.key.restrictionGroupCode = currentUpdatedSellingRestriction.restrictionGroupCode;
					sellingRestrictionTemplate.actionCode = self.DELETE_ACTION_CODE;
					sellingRestrictionTemplate.overrideLowerLevels = overrideLowerLevelRestrictions;
					sellingRestrictionChanges.push(sellingRestrictionTemplate);
				}
			}
			return sellingRestrictionChanges;
		};

		/**
		 * Gets the changed shipping restrictions by comparing the selected hierarchy with the original selected
		 * hierarchy. This method adds action codes to the shipping restrictions ("A" for add, and "D" for delete).
		 *
		 * @param currentRestrictions The current state of the shipping restrictions.
		 * @param originalSelectedHierarchy The original state of the selected hierarchy.
		 * @param overrideLowerLevelRestrictions Whether any lower level restrictions should be overridden by this change.
		 * @returns {Array} List of changed shipping restrictions.
		 */
		self.getChangedShippingRestrictions = function(currentRestrictions, originalSelectedHierarchy, overrideLowerLevelRestrictions){
			var shippingRestrictionChanges = [];
			var currentUpdatedShippingRestriction;
			var existingShippingRestriction;
			var shippingRestrictionTemplate;
			var foundRestriction;
			var newIndex;
			var oldIndex;
			var changedRestriction;

			// get added and updated shipping restrictions
			for(newIndex = 0; newIndex < currentRestrictions.length; newIndex++) {
				currentUpdatedShippingRestriction = currentRestrictions[newIndex];
				foundRestriction = false;
				changedRestriction = false;
				for(oldIndex = 0; oldIndex < originalSelectedHierarchy.viewableShippingRestrictions.length; oldIndex++) {
					existingShippingRestriction = originalSelectedHierarchy.viewableShippingRestrictions[oldIndex];
					if(currentUpdatedShippingRestriction.restrictionCode === existingShippingRestriction.key.restrictionCode){
						foundRestriction = true;

						// set changed restriction to true if applicable at this level changed
						changedRestriction = currentUpdatedShippingRestriction.applicableAtThisLevel !== existingShippingRestriction.applicableAtThisLevel;
						break;
					}
				}

				// if not found and applicable at this level, restriction is an add
				if(!foundRestriction && currentUpdatedShippingRestriction.applicableAtThisLevel){
					shippingRestrictionTemplate = angular.copy(self.shippingRestrictionTemplate);
					shippingRestrictionTemplate.key.restrictionCode = currentUpdatedShippingRestriction.restrictionCode;
					shippingRestrictionTemplate.actionCode = self.ADD_ACTION_CODE;
					shippingRestrictionTemplate.overrideLowerLevels = overrideLowerLevelRestrictions;
					shippingRestrictionTemplate.applicableAtThisLevel = currentUpdatedShippingRestriction.applicableAtThisLevel;
					shippingRestrictionTemplate.sellingRestrictionCode = currentUpdatedShippingRestriction;
					shippingRestrictionChanges.push(shippingRestrictionTemplate);
				}

				// if found and applicable at this level changed...
				else if(foundRestriction && changedRestriction){
					shippingRestrictionTemplate = angular.copy(self.shippingRestrictionTemplate);
					shippingRestrictionTemplate.key.restrictionCode = currentUpdatedShippingRestriction.restrictionCode;
					shippingRestrictionTemplate.overrideLowerLevels = overrideLowerLevelRestrictions;
					shippingRestrictionTemplate.applicableAtThisLevel = currentUpdatedShippingRestriction.applicableAtThisLevel;

					// if restriction was inherited and there was a change, restriction is an add
					if(currentUpdatedShippingRestriction.applicableAtThisLevel === true){
						// if restriction wasn't inherited, but is applicable at this level, restriction is an update
						shippingRestrictionTemplate.actionCode = self.UPDATE_ACTION_CODE;
						shippingRestrictionTemplate.sellingRestrictionCode = currentUpdatedShippingRestriction;
					}
					// if restriction wasn't inherited and is not applicable at this level, restriction is a delete
					else {
						shippingRestrictionTemplate.actionCode = self.UPDATE_ACTION_CODE;
						if(existingShippingRestriction!= null && existingShippingRestriction!= undefined) {
							existingShippingRestriction.inherited = false;
						}
					}
					shippingRestrictionChanges.push(shippingRestrictionTemplate);
				}
			}
			return shippingRestrictionChanges;
		};

		/**
		 * Set the callback function for the lower level restrictions override modal.
		 *
		 * @param callback
		 */
		self.setLowerLevelRestrictionsOverrideModalCallback = function (callback) {
			self.lowerLevelRestricionsOverrideCallback = callback;
		};

		/**
		 * Calls back end to get the current shipping restrictions for the specified product hierarchy.
		 *
		 * @param department Department to search for shipping restrictions.
		 * @param subDepartment Sub-department to search for shipping restrictions.
		 * @param itemClass Item-class to search for shipping restrictions.
		 * @param commodity Commodity to search for shipping restrictions.
		 * @param subCommodity Sub-commodity to search for shipping restrictions.
		 */
		function getCurrentShippingRestrictions(department, subDepartment, itemClass, commodity, subCommodity){
			productHierarchyApi.getViewableShippingRestrictions(
				getProductHierarchyParameters(department, subDepartment, itemClass, commodity, subCommodity),
				loadCurrentShippingRestrictions, fetchError);
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
		function getCurrentSellingRestrictions(department, subDepartment, itemClass, commodity, subCommodity){
			productHierarchyApi.getViewableSellingRestrictions(
				getProductHierarchyParameters(department, subDepartment, itemClass, commodity, subCommodity),
				loadCurrentSellingRestrictions, fetchError);
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
		function getProductHierarchyParameters(department, subDepartment, itemClass, commodity, subCommodity){
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
		function loadCurrentShippingRestrictions(restrictions) {
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
		function loadCurrentSellingRestrictions(restrictions) {
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
		 * This function clears the product hierarchy search, and resets the product hierarchy to the default
		 * non-filtered search.
		 *
		 */
		self.clearProductHierarchySearch = function () {
			self.productHierarchySearchText = null;
            self.resetFullHierarchy = true;
			productHierarchyApi.getFullHierarchy({}, expandAndHighlightPreviousSelectedLevel, fetchError);
		};

		/**
		 * This function expands to the previously selected level and highlights it.
		 */
		function expandAndHighlightPreviousSelectedLevel(results){
			self.data = results;

			deSelectPreviousSelectedLevel();
			if(self.departmentSelected) {
				findSelectedDepartmentInData(self.data);
			}
            self.resetFullHierarchy = false;
		}

		/**
		 * This method finds the department that was selected within a set of data.
		 *
		 * @param data
		 */
		function findSelectedDepartmentInData(data){
			var department;
			for (var departmentIndex = 0; departmentIndex < data.length; departmentIndex++) {
				department = data[departmentIndex];
				if(self.departmentSelected.key.department === department.key.department) {
					department.isCollapsed = false;
					if(self.subDepartmentSelected) {
						findSelectedSubDepartmentInDepartment(department);
					}
					break;
				}
			}
		}

		/**
		 * This method finds the sub department that was selected within a given department.
		 *
		 * @param department
		 */
		function findSelectedSubDepartmentInDepartment(department){
			var subDepartment;
			for (var subDepartmentIndex = 0; subDepartmentIndex < department.subDepartmentList.length; subDepartmentIndex++) {
				subDepartment = department.subDepartmentList[subDepartmentIndex];
				if (self.subDepartmentSelected.key.subDepartment === subDepartment.key.subDepartment) {
					subDepartment.isCollapsed = false;
					if(self.itemClassSelected) {
						findSelectedItemClassInSubDepartment(subDepartment);
					} else {
						setSelectedLevel(subDepartment);
					}
					break;
				}
			}
		}

		/**
		 * This method finds the item class that was selected within a given sub department.
		 *
		 * @param subDepartment
		 */
		function findSelectedItemClassInSubDepartment(subDepartment){
			var itemClass;
			for (var itemClassIndex = 0; itemClassIndex < subDepartment.itemClasses.length; itemClassIndex++) {
				itemClass = subDepartment.itemClasses[itemClassIndex];
				if (self.itemClassSelected.itemClassCode === itemClass.itemClassCode) {
					itemClass.isCollapsed = false;
					if(self.commoditySelected) {
						findSelectedCommodityInItemClass(itemClass);
					} else {
						setSelectedLevel(itemClass);
					}
					break;
				}
			}
		}

		/**
		 * This method finds the commodity that was selected within a given item class.
		 *
		 * @param itemClass
		 */
		function findSelectedCommodityInItemClass(itemClass){
			var commodity;
			for (var commodityIndex = 0; commodityIndex < itemClass.commodityList.length; commodityIndex++) {
				commodity = itemClass.commodityList[commodityIndex];
				if (self.commoditySelected.key.commodityCode === commodity.key.commodityCode) {
					commodity.isCollapsed = false;
					if(self.subCommoditySelected){
						findSelectedSubCommodityInCommodity(commodity);
					} else {
						setSelectedLevel(commodity);
					}
					break;
				}
			}
		}

		/**
		 * This method finds the sub commodity that was selected within a given commodity.
		 *
		 * @param commodity
		 */
		function findSelectedSubCommodityInCommodity(commodity){
			var subCommodity;
			for (var subCommodityIndex = 0; subCommodityIndex < commodity.subCommodityList.length; subCommodityIndex++) {
				subCommodity = commodity.subCommodityList[subCommodityIndex];
				if (self.subCommoditySelected.key.subCommodityCode === subCommodity.key.subCommodityCode) {
					setSelectedLevel(subCommodity);
					break;
				}
			}
		}

		/**
		 * For the given level to select, this function sets the level to selected, and sets the previous selected
		 * value to the given level to select (so that it can be de-selected later).
		 *
		 * @param levelToSelect
		 */
		function setSelectedLevel(levelToSelect){
			levelToSelect.isSelected = true;
			self.previousSelectedLevel = levelToSelect;
		}

		/**
		 * This function de-selects the previous selected level.
		 */
		function deSelectPreviousSelectedLevel(){
			if(self.previousSelectedLevel){
				self.previousSelectedLevel.isSelected = false
			}
		}

		/**
		 * This function sets the current search text based on the text the user has searched for.
		 */
		function setCurrentProductHierarchySearchText(){
			self.searchingForProductHierarchyText =
				self.searchingForProductHierarchyTextTemplate.replace("$searchText", self.productHierarchySearchText);
		}

		/**
		 * This function initializes the product hierarchy search values by setting the search text, and setting
		 * searching for product hierarchy to true.
		 */
		function initializeProductHierarchySearchValues(){
			setCurrentProductHierarchySearchText();
			self.searchingForProductHierarchy = true;
		}

		/**
		 * This function sets the collapsed variable to false for all current levels of the product hierarchy.
		 */
		function expandAllProductHierarchyValues(){
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
		function loadProductHierarchySearch(results){
			self.data = results;

			expandAllProductHierarchyValues();
			resetProductHierarchySearchValues();
		}

		/**
		 * This function resets variables used for the product hierarchy search to their default values.
		 */
		function resetProductHierarchySearchValues(){
			self.searchingForProductHierarchy = false;
			self.searchingForProductHierarchyText = null;
		}

		/**
		 * This function updates the view of the product hierarchy when a user clicks on the 'search' button, based
		 * on what the user has typed in the input by calling the api to get the searched values.
		 */
		self.updateProductHierarchyViewOnSearch = function () {

			initializeProductHierarchySearchValues();

			// this search for product hierarchy levels had to be moved to the api because some browsers were taking
			// approximately 15 seconds in worst case searches, and other browsers were taking less than 5 seconds.
			productHierarchyApi.getProductHierarchyBySearch(
				{searchString: self.productHierarchySearchText, searchType: self.searchOption},
				loadProductHierarchySearch,
				fetchError
			);
		};

		/**
		 * This function returns whether or not bill cost eligible on item-class is disabled or not.
		 * This is done by calling the permissions service getPermissions method passing in the resource for
		 * item-class bill cost eligible: 'PH_CLSS_02', for edit access. If the user has the appropriate permissions,
		 * this function returns false; else return true.
		 *
		 * @returns {boolean} False if user does have edit permissions on bill cost eligible, true otherwise.
		 */
		self.isBillCostEligibleDisabled = function(){
			if(permissionsService.getPermissions('PH_CLSS_02', 'EDIT')){
				return self.isSelectedHierarchyAtLevelDisabled(self.ITEM_CLASS);
			} else {
				return true;
			}
		};

		/**
		 * Callback for a successful call to get merchandise types.
		 *
		 * @param results The data returned by the back end.
		 */
		function loadMerchantTypes (results) {
			self.itemClassMerchantTypeList = results;
		}

		/**
		 * This function returns whether the save and reset buttons are visible in the selected product hierarchy view.
		 * If the lowest selected hierarchy level is item-class, and the user has at least one type of editable
		 * permission on item-class, and the item-class tab is general tab, return true.
		 * Else return false.
		 * @returns {boolean} True if save and reset buttons should be visible, false otherwise.
		 */
		self.areSaveAndResetButtonsVisible = function(){
			switch(self.selectedHierarchyLevel){
				case self.ITEM_CLASS:{
					return doesUserHaveEditAccessOnItemClass() && self.itemClassSelected !== null && self.itemClassSelected.tabSelected === self.GENERAL_TAB_NAME;
				}
				case self.COMMODITY:{
					if(self.commoditySelected !== null) {
						return doesUserHaveEditAccessOnCommodity() && self.commoditySelected !== null && self.commoditySelected.tabSelected === self.GENERAL_TAB_NAME;
					}
				}
				case self.SUB_COMMODITY:{
					return doesUserHaveEditAccessOnSubCommodity();
				}
			}
			return false;
		};

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
		};

		/**
		 * This function returns whether or not the save or reset buttons are disabled or not. This is based on the
		 * current state of the current selected hierarchy level. If that level has a save/ reset button,
		 * For save button: If form is pristine or invalid, return true. Else if there is at least one modifiable
		 * 		attribute that has changed, return false, else return true.
		 * For reset button: If form is pristine return true. Else if there is at least one modifiable
		 * 		attribute that has changed, return false, else return true.
		 * @param checkForInvalid Whether or not to check for validity of form (true for
		 * save, false for reset);
		 * @returns {boolean} True if save or reset buttons should be disabled, false otherwise.
		 */
		self.isSaveOrResetDisabled = function(checkForInvalid){
			switch(self.selectedHierarchyLevel) {
				case self.ITEM_CLASS: {
					if (self.itemClassGeneralForm.$pristine){
						return true;
					} else if(checkForInvalid && self.itemClassGeneralForm.$invalid) {
						return true;
					} else if(getItemClassChanges() === null) {
						return true;
					} else {
						return false;
					}
				}
				case self.COMMODITY: {
					if (self.commodityGeneralForm.$pristine){
						return true;
					} else if(checkForInvalid && self.commodityGeneralForm.$invalid) {
						return true;
					} else if(getCommodityChanges() === null) {
						return true;
					} else {
						return false;
					}
				}
				case self.SUB_COMMODITY: {
					if (self.subCommodityGeneralForm.$pristine && self.subCommodityDefaultForm.$pristine){
						return true;
					} else if(checkForInvalid && self.subCommodityGeneralForm.$invalid && self.subCommodityDefaultForm.$invalid) {
						return true;
					} else if(getSubCommodityChanges() === null) {
						return true;
					} else {
						return false;
					}
				}
			}
			return true;
		};

		/**
		 * This function is called when a user selects the 'Save' button on the selected product hierarchy view.
		 * If the lowest selected hierarchy level does not have any changes detected, show an error for not changes
		 * detected. Else call the appropriate update method for a particular hierarchy level.
		 */
		self.updateSelectedHierarchy = function(){
			self.error = null;
			self.success = null;
			self.waitingForUpdate = true;
			switch(self.selectedHierarchyLevel) {
				case self.ITEM_CLASS: {
					var itemClassToUpdate = getItemClassChanges();
					if(itemClassToUpdate === null) {
						fetchError(self.ERROR_NO_CHANGES_DETECTED);
					} else {
						productHierarchyApi.updateItemClass(itemClassToUpdate, setItemClassAfterUpdate, fetchError);
					}
					break;
				}
				case self.COMMODITY: {
					var commodityForUpdate = getCommodityChanges();
					if(commodityForUpdate === null) {
						fetchError(self.ERROR_NO_CHANGES_DETECTED);
					} else {
						productHierarchyApi.updateCommodity(commodityForUpdate, setCommodityAfterUpdate, fetchErrorUpdateCommodity);
					}
					break;
				}
				case self.SUB_COMMODITY: {
					var subCommodityForUpdate = getSubCommodityChanges();
					if(subCommodityForUpdate === null) {
						fetchError(self.ERROR_NO_CHANGES_DETECTED);
					}
					else {
						productHierarchyApi.updateSubCommodity(subCommodityForUpdate, setSubCommodityAfterUpdate, handleErrorUpdateSubCommodity);
					}
					break;
				}
			}
		};

		/**
		 * Callback method for updating an item class. This method sets the fields which could have been modified to
		 * the updated item-class's fields, including the displayName which would have been changed if the user
		 * modified the description, then displays the update successful message,
		 * letting the user know these changes may not be reflected until the product hierarchy cache is refreshed.
		 *
		 * @param itemClassAfterUpdate The item-class retrieved from the database after an update is completed.
		 */
		function setItemClassAfterUpdate(itemClassAfterUpdate){
			currentSelectedLevel.itemClassDescription = itemClassAfterUpdate.itemClassDescription;
			currentSelectedLevel.scanDepartment = itemClassAfterUpdate.scanDepartment;
			currentSelectedLevel.genericClass = itemClassAfterUpdate.genericClass;
			currentSelectedLevel.billCostEligible = itemClassAfterUpdate.billCostEligible;
			currentSelectedLevel.merchantTypeCode = itemClassAfterUpdate.merchantTypeCode;
			self.originalItemClassSelected.displayName = itemClassAfterUpdate.displayName;
			self.itemClassSelected.displayName = itemClassAfterUpdate.displayName;
			self.waitingForUpdate = false;
			self.success = self.UPDATE_SUCCESS_MESSAGE;
		}

		/**
		 * This method gets an item-class containing only the item class code (key) and any changes the user
		 * has made. If no changes were detected, return null.
		 *
		 * @returns {object} Item-class with changes, or null if no changes were found.
		 */
		function getItemClassChanges(){
			var itemClassToUpdate = null;
			if(self.itemClassSelected.itemClassDescription !== self.originalItemClassSelected.itemClassDescription) {
				if(itemClassToUpdate === null){
					itemClassToUpdate = angular.copy(updateItemClassTemplate);
					itemClassToUpdate.itemClassCode = self.itemClassSelected.itemClassCode;
				}
				itemClassToUpdate.itemClassDescription = self.itemClassSelected.itemClassDescription;
			}
			if(self.itemClassSelected.scanDepartment != self.originalItemClassSelected.scanDepartment) {
				if(itemClassToUpdate === null){
					itemClassToUpdate = angular.copy(updateItemClassTemplate);
					itemClassToUpdate.itemClassCode = self.itemClassSelected.itemClassCode;
				}
				itemClassToUpdate.scanDepartment = self.itemClassSelected.scanDepartment;
			}
			if(self.itemClassSelected.genericClass !== self.originalItemClassSelected.genericClass){
				if(itemClassToUpdate === null){
					itemClassToUpdate = angular.copy(updateItemClassTemplate);
					itemClassToUpdate.itemClassCode = self.itemClassSelected.itemClassCode;
				}
				itemClassToUpdate.genericClass = self.itemClassSelected.genericClass;
			}
			if(self.itemClassSelected.billCostEligible != self.originalItemClassSelected.billCostEligible){
				if(itemClassToUpdate === null){
					itemClassToUpdate = angular.copy(updateItemClassTemplate);
					itemClassToUpdate.itemClassCode = self.itemClassSelected.itemClassCode;
				}
				itemClassToUpdate.billCostEligible = self.itemClassSelected.billCostEligible;
			}
			if(self.itemClassSelected.merchantTypeCode !== self.originalItemClassSelected.merchantTypeCode){
				if(itemClassToUpdate === null){
					itemClassToUpdate = angular.copy(updateItemClassTemplate);
					itemClassToUpdate.itemClassCode = self.itemClassSelected.itemClassCode;
				}
				itemClassToUpdate.merchantTypeCode = self.itemClassSelected.merchantTypeCode;
			}
			return itemClassToUpdate;
		}

		/**
		 * Callback method for updating a commodity. This method sets the fields which could have been modified to
		 * the updated commodity's fields, including the displayName which would have been changed if the user
		 * modified the description, then displays the update successful message,
		 * letting the user know these changes may not be reflected until the product hierarchy cache is refreshed.
		 *
		 * @param commodityAfterUpdate The commodity retrieved from the database after an update is completed.
		 */
		function setCommodityAfterUpdate(commodityAfterUpdate){
			self.originalCommoditySelected.name = commodityAfterUpdate.name;
			self.originalCommoditySelected.bdmCode = commodityAfterUpdate.bdmCode;
			self.originalCommoditySelected.bdm.displayName = commodityAfterUpdate.bdm.displayName;
			self.bdmUserDisplayValue = commodityAfterUpdate.bdm.displayName;
			self.commoditySelected.bdmCode = commodityAfterUpdate.bdmCode;
			self.commoditySelected.bdm.displayName = commodityAfterUpdate.bdm.displayName;

			commodityAfterUpdate.eBMid = (commodityAfterUpdate.eBMid === null) ? '' : commodityAfterUpdate.eBMid;
			self.originalCommoditySelected.eBMid = commodityAfterUpdate.eBMid;
			self.commoditySelected.eBMid = commodityAfterUpdate.eBMid;

			self.originalCommoditySelected.bDAid = commodityAfterUpdate.bDAid;
			self.commoditySelected.bDAid = commodityAfterUpdate.bDAid;

			if (commodityAfterUpdate.bDAid) {
				userApi.getUserById({userId : commodityAfterUpdate.bDAid},
					function (results){
						self.bdaUserDisplayName = results.displayName;
						self.bdaUserDisplayValue = self.bdaUserDisplayName;
					},
					fetchError
				);
			} else {
				self.bdaUserDisplayName = '';
				self.bdaUserDisplayValue = ''
			}

			if(self.bdaUserDisplayName === null || self.bdaUserDisplayName === 'undefined') {
				self.bdaUserDisplayValue = angular.copy(commodityAfterUpdate.bDAid);
			}

			if (commodityAfterUpdate.eBMid) {
				userApi.getUserById({userId : commodityAfterUpdate.eBMid},
					function (results){
						self.ebmUserDisplayName = results.displayName;
						self.ebmUserDisplayValue = self.ebmUserDisplayName;
					},
					fetchError
				);
			} else {
				self.ebmUserDisplayName = '';
				self.ebmUserDisplayValue = '';
			}

			if(self.ebmUserDisplayName === null || self.ebmUserDisplayName === 'undefined'){
				self.ebmUserDisplayValue = angular.copy(commodityAfterUpdate.eBMid);
			}
			self.originalCommoditySelected.pssDepartment = commodityAfterUpdate.pssDepartment;
			self.originalCommoditySelected.classCommodityActive = commodityAfterUpdate.classCommodityActive;
			self.originalCommoditySelected.displayName = commodityAfterUpdate.displayName;
			self.commoditySelected.displayName = commodityAfterUpdate.displayName;
			self.success = self.UPDATE_SUCCESS_MESSAGE;
			self.waitingForUpdate = false;
		}

		/**
		 * This method gets a commodity containing only the commodity key attributes and any changes the user
		 * has made. If no changes were detected, return null.
		 *
		 * @returns {object} Commodity with changes, or null if no changes were found.
		 */
		function getCommodityChanges(){
			var commodityToUpdate = null;
			if(self.commoditySelected.name !== self.originalCommoditySelected.name) {
				if(commodityToUpdate === null){
					commodityToUpdate = angular.copy(updateCommodityTemplate);
					commodityToUpdate.key = self.commoditySelected.key;
				}
				commodityToUpdate.name = self.commoditySelected.name;
			}
			if(self.bdmUserDisplayValue !== self.originalCommoditySelected.bdmCode &&
				self.bdmUserDisplayValue !== self.originalCommoditySelected.bdm.displayName) {
				if(commodityToUpdate === null){
					commodityToUpdate = angular.copy(updateCommodityTemplate);
					commodityToUpdate.key = self.commoditySelected.key;
				}
				commodityToUpdate.bdmCode = self.bdmUserDisplayValue;
			}
			if(self.ebmUserDisplayValue !== self.originalCommoditySelected.eBMid &&
				self.ebmUserDisplayValue !== self.ebmUserDisplayName){
				if(commodityToUpdate === null){
					commodityToUpdate = angular.copy(updateCommodityTemplate);
					commodityToUpdate.key = self.commoditySelected.key;
				};
				commodityToUpdate.eBMid = self.ebmUserDisplayValue;
			}
			if(self.bdaUserDisplayValue !== self.originalCommoditySelected.bDAid &&
				self.bdaUserDisplayValue !== self.bdaUserDisplayName){
				if(commodityToUpdate === null){
					commodityToUpdate = angular.copy(updateCommodityTemplate);
					commodityToUpdate.key = self.commoditySelected.key;
				}
				commodityToUpdate.bDAid = self.bdaUserDisplayValue;
			}
			if(self.commoditySelected.pssDepartment !== self.originalCommoditySelected.pssDepartment){
				if(commodityToUpdate === null){
					commodityToUpdate = angular.copy(updateCommodityTemplate);
					commodityToUpdate.key = self.commoditySelected.key;
				}
				commodityToUpdate.pssDepartment = self.commoditySelected.pssDepartment;
			}
			if(isCommodityActive(self.commoditySelected) !== isCommodityActive(self.originalCommoditySelected)){
				if(commodityToUpdate === null){
					commodityToUpdate = angular.copy(updateCommodityTemplate);
					commodityToUpdate.key = self.commoditySelected.key;
				}
				commodityToUpdate.classCommodityActive = getCommodityActiveValue(self.commoditySelected);
			}
			return commodityToUpdate;
		}

		/**
		 * This function is called when a user selects the 'Reset' button on the selected product hierarchy view.
		 * This method calls helper functions to reset the lowest level selected hierarchy to the original values.
		 */
		self.resetSelectedHierarchy = function(){
			self.success = null;
			self.error = null;
			switch(self.selectedHierarchyLevel) {
				case self.ITEM_CLASS: {
					resetItemClassEditableFields();
					break;
				}
				case self.COMMODITY: {
					resetCommodityEditableFields();
					break;
				}
				case self.SUB_COMMODITY: {
					resetSubCommodityEditableFields();
					break;
				}
			}
		};

		/**
		 * This function resets item-class editable fields to their original values.
		 */
		function resetItemClassEditableFields(){
			self.itemClassSelected.itemClassDescription = self.originalItemClassSelected.itemClassDescription;
			self.itemClassSelected.scanDepartment = self.originalItemClassSelected.scanDepartment;
			self.itemClassSelected.genericClass = self.originalItemClassSelected.genericClass;
			self.itemClassSelected.billCostEligible = self.originalItemClassSelected.billCostEligible;
			self.itemClassSelected.merchantTypeCode = self.originalItemClassSelected.merchantTypeCode;
		}

		/**
		 * This function will retrieve an empty item class used for editing.
		 */
		function getItemClassTemplate(){
			if(updateItemClassTemplate === null && doesUserHaveEditAccessOnItemClass()) {
				productHierarchyApi.getEmptyItemClassForUpdate({}, loadItemClassTemplate, fetchError);
			}
		}

		/**
		 * This method returns whether a user has at least one edit access association for any of the item class
		 * editable fields.
		 *
		 * @returns {boolean} True if a user has at least one editable item-class association, false otherwise.
		 */
		function doesUserHaveEditAccessOnItemClass(){
			return permissionsService.getPermissions('PH_CLSS_01', 'EDIT') ||
				permissionsService.getPermissions('PH_CLSS_02', 'EDIT') ||
				permissionsService.getPermissions('PH_CLSS_03', 'EDIT') ||
				permissionsService.getPermissions('PH_CLSS_04', 'EDIT') ||
				permissionsService.getPermissions('PH_CLSS_05', 'EDIT');
		}

		/**
		 * Callback method for retrieving an empty item class used for editing.
		 *
		 * @param result An empty item class.
		 */
		function loadItemClassTemplate(result){
			updateItemClassTemplate = result;
		}

		/**
		 * This function resets commodity editable fields to their original values.
		 */
		function resetCommodityEditableFields(){
			self.commoditySelected.name = self.originalCommoditySelected.name;
			self.commoditySelected.bdmCode = self.originalCommoditySelected.bdmCode;
			self.commoditySelected.eBMid = self.originalCommoditySelected.eBMid;
			self.commoditySelected.bDAid = self.originalCommoditySelected.bDAid;
			self.commoditySelected.pssDepartment = self.originalCommoditySelected.pssDepartment;
			self.commoditySelected.active = isCommodityActive(self.originalCommoditySelected);
			self.bdmUserDisplayValue = self.originalCommoditySelected.bdm.displayName;
			self.ebmUserDisplayValue = self.ebmUserDisplayName;
			self.bdaUserDisplayValue = self.bdaUserDisplayName;
		}

		/**
		 * This function will retrieve an empty commodity used for editing.
		 */
		function getCommodityTemplate(){
			if(updateCommodityTemplate === null && doesUserHaveEditAccessOnCommodity()) {
				productHierarchyApi.getEmptyCommodityForUpdate({}, loadCommodityTemplate, fetchError);
			}
		}

		/**
		 * This method returns whether a user has at least one edit access association for any of the commodity
		 * editable fields.
		 *
		 * @returns {boolean} True if a user has at least one editable commodity association, false otherwise.
		 */
		function doesUserHaveEditAccessOnCommodity(){
			return permissionsService.getPermissions('PH_COMM_01', 'EDIT') ||
				permissionsService.getPermissions('PH_COMM_02', 'EDIT') ||
				permissionsService.getPermissions('PH_COMM_03', 'EDIT') ||
				permissionsService.getPermissions('PH_COMM_04', 'EDIT') ||
				permissionsService.getPermissions('PH_COMM_05', 'EDIT') ||
				permissionsService.getPermissions('PH_COMM_06', 'EDIT');
		}

		/**
		 * Callback method for retrieving an empty commodity used for editing.
		 *
		 * @param result An empty commodity.
		 */
		function loadCommodityTemplate(result){
			updateCommodityTemplate = result;
		}

		/**
		 * This function resets sub-commodity editable fields to their original values.
		 */
		function resetSubCommodityEditableFields(){
            self.prodCategoryIdInvalid = false;
            self.iMSSubCommCodeInvalid = false;
			self.subCommoditySelected.name = self.originalSubCommoditySelected.name;
			self.subCommoditySelected.taxCategoryCode = self.originalSubCommoditySelected.taxCategoryCode;
			self.subCommoditySelected.nonTaxCategoryCode = self.originalSubCommoditySelected.nonTaxCategoryCode;
			self.subCommoditySelected.productCategoryId = self.originalSubCommoditySelected.productCategoryId;
			self.productCategoryDisplayValue = angular.copy(self.originalProductCategoryDisplayValue);
			self.subCommoditySelected.imsCommodityCode = self.originalSubCommoditySelected.imsCommodityCode;
			self.subCommoditySelected.imsSubCommodityCode  = self.originalSubCommoditySelected.imsSubCommodityCode;
			self.subCommoditySelected.foodStampEligible = self.originalSubCommoditySelected.foodStampEligible;
			self.subCommoditySelected.taxEligible = self.originalSubCommoditySelected.taxEligible;
			self.subCommoditySelected.active = isSubCommodityActive(self.originalSubCommoditySelected);
			self.initDataForTaxAndNonTaxCategoryDropDown();
		}

		/**
		 * This function will retrieve an empty sub-commodity used for editing.
		 */
		function getSubCommodityTemplate(){
			if(updateSubCommodityTemplate === null && doesUserHaveEditAccessOnSubCommodity()) {
				productHierarchyApi.getEmptySubCommodityForUpdate({}, loadSubCommodityTemplate, fetchError);
			}
		}

		/**
		 * This method returns whether a user has at least one edit access association for any of the sub-commodity
		 * editable fields that do not use a save modal.
		 *
		 * @returns {boolean} True if a user has at least one editable sub-commodity association, false otherwise.
		 */
		function doesUserHaveEditAccessOnSubCommodity(){
			return permissionsService.getPermissions('PH_SUBC_02', 'EDIT') ||
				permissionsService.getPermissions('PH_SUBC_03', 'EDIT') ||
				permissionsService.getPermissions('PH_SUBC_04', 'EDIT') ||
				permissionsService.getPermissions('PH_SUBC_05', 'EDIT') ||
				permissionsService.getPermissions('PH_SUBC_06', 'EDIT') ||
				permissionsService.getPermissions('PH_SUBC_07', 'EDIT') ||
				permissionsService.getPermissions('PH_SUBC_08', 'EDIT') ||
				permissionsService.getPermissions('PH_SUBC_09', 'EDIT');
		}

		/**
		 * Callback method for retrieving an empty sub-commodity used for editing.
		 *
		 * @param result An empty sub-commodity.
		 */
		function loadSubCommodityTemplate(result){
			updateSubCommodityTemplate = result;
		}

		/**
		 * Callback method for updating a sub-commodity. This method sets the fields which could have been modified to
		 * the updated sub-commodity's fields, including the displayName which would have been changed if the user
		 * modified the description, then displays the update successful message,
		 * letting the user know these changes may not be reflected until the product hierarchy cache is refreshed.
		 *
		 * @param subCommodityAfterUpdate The sub-commodity retrieved from the database after an update is completed.
		 */
		function setSubCommodityAfterUpdate(subCommodityAfterUpdate){
            self.prodCategoryIdInvalid = false;
            self.iMSSubCommCodeInvalid = false;
			subCommodityAfterUpdate.stateWarningList = self.subCommoditySelected.stateWarningList;
			subCommodityAfterUpdate.viewableSellingRestrictions = self.subCommoditySelected.viewableSellingRestrictions;
			subCommodityAfterUpdate.viewableShippingRestrictions = self.subCommoditySelected.viewableShippingRestrictions;
			self.originalSubCommoditySelected = subCommodityAfterUpdate;
			self.subCommoditySelected = angular.copy(subCommodityAfterUpdate);
			self.productCategoryDisplayValue = self.subCommoditySelected.productCategory.productCategoryId + " - " + self.subCommoditySelected.productCategory.productCategoryName.trim();
			self.originalProductCategoryDisplayValue = angular.copy(self.productCategoryDisplayValue);
			self.success = self.UPDATE_SUCCESS_MESSAGE;
			self.waitingForUpdate = false;
		}

		/**
		 * This method gets a sub-commodity containing only the sub-commodity key attributes and any changes the user
		 * has made. If no changes were detected, return null.
		 *
		 * @returns {object} Sub-commodity with changes, or null if no changes were found.
		 */
		function getSubCommodityChanges(){
			var subCommodityToUpdate = null;
			if(self.subCommoditySelected != null && self.originalSubCommoditySelected != null){
			if(self.subCommoditySelected.name !== self.originalSubCommoditySelected.name) {
				if(subCommodityToUpdate === null){
					subCommodityToUpdate = angular.copy(updateSubCommodityTemplate);
					subCommodityToUpdate.key = self.subCommoditySelected.key;
				}
				subCommodityToUpdate.name = self.subCommoditySelected.name;
			}
			if(self.selectedVertexTaxCategory.dvrCode !== self.originalSubCommoditySelected.taxCategoryCode) {
				if(subCommodityToUpdate === null){
					subCommodityToUpdate = angular.copy(updateSubCommodityTemplate);
					subCommodityToUpdate.key = self.subCommoditySelected.key;
				}
				subCommodityToUpdate.taxCategoryCode = self.selectedVertexTaxCategory.dvrCode;
			}
			if(self.selectedVertexNonTaxCategory.dvrCode !== self.originalSubCommoditySelected.nonTaxCategoryCode) {
				if(subCommodityToUpdate === null){
					subCommodityToUpdate = angular.copy(updateSubCommodityTemplate);
					subCommodityToUpdate.key = self.subCommoditySelected.key;
				}
				subCommodityToUpdate.nonTaxCategoryCode = self.selectedVertexNonTaxCategory.dvrCode;
			}
			if(self.productCategoryDisplayValue != self.originalProductCategoryDisplayValue) {
					if(subCommodityToUpdate === null){
						subCommodityToUpdate = angular.copy(updateSubCommodityTemplate);
						subCommodityToUpdate.key = self.subCommoditySelected.key;
					}
					subCommodityToUpdate.productCategoryId = parseInt(self.productCategoryDisplayValue);
			}
			if(self.subCommoditySelected.imsCommodityCode != self.originalSubCommoditySelected.imsCommodityCode) {
				if(subCommodityToUpdate === null){
					subCommodityToUpdate = angular.copy(updateSubCommodityTemplate);
					subCommodityToUpdate.key = self.subCommoditySelected.key;
				}
				subCommodityToUpdate.imsCommodityCode = self.subCommoditySelected.imsCommodityCode;
			}
				if(self.subCommoditySelected.imsSubCommodityCode == undefined ||
						self.subCommoditySelected.imsSubCommodityCode.toUpperCase() != self.originalSubCommoditySelected.imsSubCommodityCode.toUpperCase()) {
					if(subCommodityToUpdate === null){
						subCommodityToUpdate = angular.copy(updateSubCommodityTemplate);
						subCommodityToUpdate.key = self.subCommoditySelected.key;
					}
					subCommodityToUpdate.imsSubCommodityCode = self.subCommoditySelected.imsSubCommodityCode == undefined?'':self.subCommoditySelected.imsSubCommodityCode.toUpperCase();
				}
			if(self.subCommoditySelected.foodStampEligible !== self.originalSubCommoditySelected.foodStampEligible) {
				if(subCommodityToUpdate === null){
					subCommodityToUpdate = angular.copy(updateSubCommodityTemplate);
					subCommodityToUpdate.key = self.subCommoditySelected.key;
				}
				subCommodityToUpdate.foodStampEligible = self.subCommoditySelected.foodStampEligible;
			}
			if(self.subCommoditySelected.taxEligible !== self.originalSubCommoditySelected.taxEligible) {
				if(subCommodityToUpdate === null){
					subCommodityToUpdate = angular.copy(updateSubCommodityTemplate);
					subCommodityToUpdate.key = self.subCommoditySelected.key;
				}
				subCommodityToUpdate.taxEligible = self.subCommoditySelected.taxEligible;
			}
			if(isSubCommodityActive(self.subCommoditySelected) !== isSubCommodityActive(self.originalSubCommoditySelected)){
				if(subCommodityToUpdate === null){
					subCommodityToUpdate = angular.copy(updateCommodityTemplate);
					subCommodityToUpdate.key = self.subCommoditySelected.key;
				}
				subCommodityToUpdate.subCommodityActive = getSubCommodityActiveValue(self.subCommoditySelected);
			}
			if(subCommodityToUpdate != null){
				if (subCommodityToUpdate.taxEligible == null || subCommodityToUpdate.taxEligible === undefined) {
					if(self.subCommoditySelected.taxEligible != null) {
						subCommodityToUpdate.taxEligible = self.subCommoditySelected.taxEligible;
					}else{
						subCommodityToUpdate.taxEligible = false;
					}
				}
				if (subCommodityToUpdate.foodStampEligible == null || subCommodityToUpdate.foodStampEligible === undefined) {
					if(self.subCommoditySelected.foodStampEligible != null) {
						subCommodityToUpdate.foodStampEligible = self.subCommoditySelected.foodStampEligible;
					}else{
						subCommodityToUpdate.foodStampEligible = false;
					}
				}
			}
			}
			return subCommodityToUpdate;
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
		};

		/**
		 * This function gets whether or not a commodity is active. If the commodity has an attribute 'active', use
		 * that value. Else if the commodity's classCommodityActive equals
		 * the HIERARCHY_ACTIVE_CODE ('A'), return true. Else return false.
		 *
		 * @param commodity Commodity to set active switch on.
		 * @returns {boolean} True if commodity is active, false otherwise.
		 */
		function isCommodityActive(commodity) {
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
		function getCommodityActiveValue(commodity) {
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
		};

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
		};

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
		};

		/**
		 * This function gets whether or not a sub-commodity is active. If the sub-commodity has an attribute
		 * 'active', use that value. Else if the sub-commodity's subCommodityActive equals
		 * the HIERARCHY_ACTIVE_CODE ('A'), return true. Else return false.
		 *
		 * @param subCommodity SubCommodity to set active switch on.
		 * @returns {boolean} True if sub-commodity is active, false otherwise.
		 */
		function isSubCommodityActive(subCommodity) {
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
		function getSubCommodityActiveValue(subCommodity) {
			return subCommodity.active ? HIERARCHY_ACTIVE_CODE : HIERARCHY_INACTIVE_CODE;
		}

		/**
		 * Gets the product state warnings to be used for updating.
		 */
		self.getProductStateWarningsForUpdate = function(){
			if(subCommodityStateWarningTemplate === null){
				getSubCommodityStateWarningTemplate();
			}

			if(allProductStateWarnings === null){
				getAllProductStateWarnings();
			} else {
				loadProductStateWarningsForUpdate(allProductStateWarnings);
			}
		};

		/**
		 * Method to retrieve an empty sub-commodity state warning used for update.
		 */
		function getSubCommodityStateWarningTemplate(){
			productHierarchyApi.getEmptySubCommodityStateWarning({},
				function(result){
					subCommodityStateWarningTemplate = result;
				},
				function(error){
					fetchError(error);
				});
		}

		/**
		 * This function gets all the product state warnings, then returns a boolean on whether the product state
		 * warnings were successfully retrieved or not. If they have already been retrieved, return true.
		 * If they have not been retrieved, call the back end to get them. If they are retrieved successfully,
		 * set the all product state warnings variable, and return true. If the retrieval threw an error, return false.
		 *
		 * @returns {boolean} Used for the promise to load the product state warnings after this method is complete.
		 */
		function getAllProductStateWarnings(){
			codeTableApi.getAllProductStateWarnings({},
				function (results)
				{
					allProductStateWarnings = results;
					loadProductStateWarningsForUpdate(allProductStateWarnings);
				},
				function(error){
					fetchError(error);
				});
		}

		/**
		 * This method takes the product state warnings from the current selected sub-commodity, compares it with
		 * the all product state warnings list, and creates a list to be used for editing product state warnings.
		 * For each warning that was in the sub-commodity, set that value in the copy of all product state warnings
		 * list to be checked.
		 */
		function loadProductStateWarningsForUpdate(allProductStateWarningsList) {
			var currentWarnings = self.subCommoditySelected.stateWarningList;
			var currentWarningIndexFound;

			// get a copy of the full product state warning list
			self.productStateWarningsForEdit = angular.copy(allProductStateWarningsList);

			// for each product state warning in the current selected sub-commodity
			for (var currentWarningIndex = 0; currentWarningIndex < currentWarnings.length; currentWarningIndex++) {
				currentWarningIndexFound = null;

				// for each warning in the full product state warning copy list
				for (var index = 0; index < self.productStateWarningsForEdit.length; index++) {

					// if the warning codes match
					if (currentWarnings[currentWarningIndex].key.stateProductWarningCode ===
						self.productStateWarningsForEdit[index].key.warningCode &&
						currentWarnings[currentWarningIndex].key.stateCode ===
						self.productStateWarningsForEdit[index].key.stateCode) {
						currentWarningIndexFound = index;
						break;
					}
				}

				// if the warning was in the current selected sub-commodity, update the full warning copy list
				// value to be checked
				if (currentWarningIndexFound !== null) {
					self.productStateWarningsForEdit[currentWarningIndexFound].checked = true;
				}
			}
		}

		/**
		 * Gets the changed product state warnings by comparing the selected sub-commodity's product state warnings
		 * with the product state warnings in the update modal. This method adds action codes to the product state
		 * warning changes:
		 * ("A" for add, and "D" for delete).
		 *
		 * @returns {Array} List of changed product state warnings.
		 */
		function getChangedProductStateWarnings(){
			var productStateWarningChanges = [];
			var currentUpdatedProductStateWarning;
			var existingProductStateWarning;
			var subCommodityStateWarning;
			var foundProductStateWarning;
			var newIndex;
			var oldIndex;
			var currentWarnings = self.subCommoditySelected.stateWarningList;

			// get added product state warnings
			for(newIndex = 0; newIndex < self.productStateWarningsForEdit.length; newIndex++) {
				currentUpdatedProductStateWarning = self.productStateWarningsForEdit[newIndex];
				foundProductStateWarning = false;
				for(oldIndex = 0; oldIndex < currentWarnings.length; oldIndex++) {
					existingProductStateWarning = currentWarnings[oldIndex];
					if(existingProductStateWarning.key.stateProductWarningCode ===
						currentUpdatedProductStateWarning.key.warningCode &&
						existingProductStateWarning.key.stateCode ===
						currentUpdatedProductStateWarning.key.stateCode){
						foundProductStateWarning = true;
						break;
					}
				}
				if(!foundProductStateWarning && currentUpdatedProductStateWarning.checked){
					subCommodityStateWarning = angular.copy(subCommodityStateWarningTemplate);
					subCommodityStateWarning.key.stateCode = currentUpdatedProductStateWarning.key.stateCode;
					subCommodityStateWarning.key.stateProductWarningCode = currentUpdatedProductStateWarning.key.warningCode;
					subCommodityStateWarning.key.subCommodityCode = self.subCommoditySelected.key.subCommodityCode;
					subCommodityStateWarning.action = self.ADD_ACTION_CODE;
					productStateWarningChanges.push(subCommodityStateWarning);
				}
			}

			// get removed product state warnings
			for(oldIndex = 0; oldIndex < currentWarnings.length; oldIndex++) {
				existingProductStateWarning = currentWarnings[oldIndex];
				foundProductStateWarning = false;
				for(newIndex = 0; newIndex < self.productStateWarningsForEdit.length; newIndex++) {
					currentUpdatedProductStateWarning = self.productStateWarningsForEdit[newIndex];
					if(existingProductStateWarning.key.stateProductWarningCode ===
						currentUpdatedProductStateWarning.key.warningCode &&
						existingProductStateWarning.key.stateCode ===
						currentUpdatedProductStateWarning.key.stateCode){
						foundProductStateWarning = true;
						break;
					}
				}
				if(foundProductStateWarning && !currentUpdatedProductStateWarning.checked){
					subCommodityStateWarning = angular.copy(subCommodityStateWarningTemplate);
					subCommodityStateWarning.key.stateCode = currentUpdatedProductStateWarning.key.stateCode;
					subCommodityStateWarning.key.stateProductWarningCode = currentUpdatedProductStateWarning.key.warningCode;
					subCommodityStateWarning.key.subCommodityCode = self.subCommoditySelected.key.subCommodityCode;
					subCommodityStateWarning.action = self.DELETE_ACTION_CODE;
					productStateWarningChanges.push(subCommodityStateWarning);
				}
			}
			return productStateWarningChanges;
		}

		/**
		 * Method called when a user hits the save button from the product state warning modal.
		 */
		self.saveProductStateWarnings = function () {

			var productStateWarningChanges = getChangedProductStateWarnings();
			if(productStateWarningChanges.length > 0){
				self.error = null;
				self.success = null;
				self.waitingForUpdate = true;
				productHierarchyApi.updateSubCommodityStateWarnings(
					productStateWarningChanges, self.setUpdatedProductStateWarnings, fetchError);
			} else {
				fetchError({data: {message: self.ERROR_NO_CHANGES_DETECTED}});
			}
			self.cancelProductStateWarnings();
		};

		/**
		 * Callback for updating product state warnings. Updates original and selected product state warnings to list
		 * of warnings retrieved after the update.
		 */
		self.setUpdatedProductStateWarnings = function (results) {
			self.originalSubCommoditySelected.stateWarningList = results;
			self.subCommoditySelected.stateWarningList = results;
			self.waitingForUpdate = false;
			self.success = self.UPDATE_SUCCESS_MESSAGE;
		};

		/**
		 * Gets the product preferred units of measure to be used for updating.
		 */
		self.getProductPreferredUnitsOfMeasureForUpdate = function(){
			if(preferredUnitOfMeasureTemplate === null){
				getPreferredUnitOfMeasureTemplate();
			}

			if(allPreferredUnitsOfMeasure === null){
				getAllPreferredUnitsOfMeasure();
			} else {
				loadPreferredUnitsOfMeasureForUpdate(allPreferredUnitsOfMeasure);
			}
		};

		/**
		 * Method to retrieve an empty sub-commodity preferred unit of measure used for update.
		 */
		function getPreferredUnitOfMeasureTemplate(){
			productHierarchyApi.getEmptySubCommodityPreferredUnitOfMeasure({},
				function(result){
					preferredUnitOfMeasureTemplate = result;
				},
				function(error){
					fetchError(error);
				});
		}

		/**
		 * This function gets all the preferred units of measure, then calls the method to load the list on success,
		 * or fetchError on return.
		 */
		function getAllPreferredUnitsOfMeasure(){
			codeTableApi.getAllRetailUnitsOfMeasure({},
				function (results)
				{
					allPreferredUnitsOfMeasure = results;
					loadPreferredUnitsOfMeasureForUpdate(allPreferredUnitsOfMeasure);
				},
				function(error){
					fetchError(error);
				});
		}

		/**
		 * This method takes the preferred units of measure from the current selected sub-commodity, compares it with
		 * the all preferred units of measure list, and creates a list to be used for editing preferred units of measure.
		 * For each unit of measure that was in the sub-commodity, set checked value to true, and set the sequence
		 * number to the current value.
		 */
		function loadPreferredUnitsOfMeasureForUpdate(allPreferredUnitsOfMeasure) {
			var currentUnitsOfMeasure = self.subCommoditySelected.productPreferredUnitOfMeasureList;
			var currentUnitsOfMeasureFound;

			// get a copy of the full unit of measure list
			self.preferredUnitsOfMeasureForEdit = angular.copy(allPreferredUnitsOfMeasure);
			self.preferredUnitOfMeasureSequences = angular.copy(self.preferredUnitOfMeasureSequencesTemplate);

			// for each unit of measure in the current selected sub-commodity
			for (var currentUnitOfMeasureIndex = 0; currentUnitOfMeasureIndex < currentUnitsOfMeasure.length; currentUnitOfMeasureIndex++) {
				currentUnitsOfMeasureFound = null;

				// for each unit of measure in the full unit of measure copy list
				for (var index = 0; index < self.preferredUnitsOfMeasureForEdit.length; index++) {

					// if the unit of measure codes match
					if (currentUnitsOfMeasure[currentUnitOfMeasureIndex].key.retailUnitOfMeasureCode ===
						self.preferredUnitsOfMeasureForEdit[index].id) {
						currentUnitsOfMeasureFound = index;
						break;
					}
				}

				// if the unit of measure was in the current selected sub-commodity, update the full unit of measure
				// copy list value to be checked, and the sequence number to current sequence number value
				if (currentUnitsOfMeasureFound !== null) {
					self.preferredUnitsOfMeasureForEdit[currentUnitsOfMeasureFound].checked = true;
					self.preferredUnitsOfMeasureForEdit[currentUnitsOfMeasureFound].sequenceNumber =
						currentUnitsOfMeasure[currentUnitOfMeasureIndex].sequenceNumber;
				}
			}
			self.isWaitingForPreferredUnitOfMeasureResponse = false;
		}

		/**
		 * Gets the changed preferred units of measure by comparing the selected sub-commodity's preferred units of
		 * measure with the preferred units of measure in the update modal. This method adds action codes to the
		 * preferred units of measure changes:
		 * ("A" for add, "U" for update, and "D" for delete).
		 *
		 * @returns {Array} List of changed preferred units of measure.
		 */
		function getChangedPreferredUnitsOfMeasure(){
			var preferredUnitsOfMeasureChanges = [];
			var currentUpdatedPreferredUnitsOfMeasure;
			var existingPreferredUnitsOfMeasure;
			var subCommodityPreferredUnitOfMeasure;
			var foundPreferredUnitsOfMeasure;
			var newIndex;
			var oldIndex;
			var currentPreferredUnitsOfMeasure = self.subCommoditySelected.productPreferredUnitOfMeasureList;

			// get added preferred units of measure
			for(newIndex = 0; newIndex < self.preferredUnitsOfMeasureForEdit.length; newIndex++) {
				currentUpdatedPreferredUnitsOfMeasure = self.preferredUnitsOfMeasureForEdit[newIndex];
				foundPreferredUnitsOfMeasure = false;
				for(oldIndex = 0; oldIndex < currentPreferredUnitsOfMeasure.length; oldIndex++) {
					existingPreferredUnitsOfMeasure = currentPreferredUnitsOfMeasure[oldIndex];
					if(existingPreferredUnitsOfMeasure.key.retailUnitOfMeasureCode ===
						currentUpdatedPreferredUnitsOfMeasure.id){
						foundPreferredUnitsOfMeasure = true;
						break;
					}
				}
				if(!foundPreferredUnitsOfMeasure && currentUpdatedPreferredUnitsOfMeasure.checked){
					subCommodityPreferredUnitOfMeasure = angular.copy(preferredUnitOfMeasureTemplate);
					subCommodityPreferredUnitOfMeasure.key.retailUnitOfMeasureCode = currentUpdatedPreferredUnitsOfMeasure.id;
					subCommodityPreferredUnitOfMeasure.key.subCommodityCode = self.subCommoditySelected.key.subCommodityCode;
					subCommodityPreferredUnitOfMeasure.sequenceNumber = currentUpdatedPreferredUnitsOfMeasure.sequenceNumber;
					subCommodityPreferredUnitOfMeasure.action = self.ADD_ACTION_CODE;
					preferredUnitsOfMeasureChanges.push(subCommodityPreferredUnitOfMeasure);
				}
			}

			// get removed and updated preferred units of measure
			for(oldIndex = 0; oldIndex < currentPreferredUnitsOfMeasure.length; oldIndex++) {
				existingPreferredUnitsOfMeasure = currentPreferredUnitsOfMeasure[oldIndex];
				foundPreferredUnitsOfMeasure = false;
				for(newIndex = 0; newIndex < self.preferredUnitsOfMeasureForEdit.length; newIndex++) {
					currentUpdatedPreferredUnitsOfMeasure = self.preferredUnitsOfMeasureForEdit[newIndex];
					if(existingPreferredUnitsOfMeasure.key.retailUnitOfMeasureCode ===
						currentUpdatedPreferredUnitsOfMeasure.id){
						foundPreferredUnitsOfMeasure = true;
						break;
					}
				}
				// removed
				if(foundPreferredUnitsOfMeasure && !currentUpdatedPreferredUnitsOfMeasure.checked){
					subCommodityPreferredUnitOfMeasure = angular.copy(preferredUnitOfMeasureTemplate);
					subCommodityPreferredUnitOfMeasure.key.retailUnitOfMeasureCode = currentUpdatedPreferredUnitsOfMeasure.id;
					subCommodityPreferredUnitOfMeasure.key.subCommodityCode = self.subCommoditySelected.key.subCommodityCode;
					subCommodityPreferredUnitOfMeasure.sequenceNumber = existingPreferredUnitsOfMeasure.sequenceNumber;
					subCommodityPreferredUnitOfMeasure.action = self.DELETE_ACTION_CODE;
					preferredUnitsOfMeasureChanges.push(subCommodityPreferredUnitOfMeasure);
				}
				// updated
				else if(foundPreferredUnitsOfMeasure && currentUpdatedPreferredUnitsOfMeasure.checked &&
					currentUpdatedPreferredUnitsOfMeasure.sequenceNumber !== existingPreferredUnitsOfMeasure.sequenceNumber){
					subCommodityPreferredUnitOfMeasure = angular.copy(preferredUnitOfMeasureTemplate);
					subCommodityPreferredUnitOfMeasure.key.retailUnitOfMeasureCode = currentUpdatedPreferredUnitsOfMeasure.id;
					subCommodityPreferredUnitOfMeasure.key.subCommodityCode = self.subCommoditySelected.key.subCommodityCode;
					subCommodityPreferredUnitOfMeasure.sequenceNumber = currentUpdatedPreferredUnitsOfMeasure.sequenceNumber;
					subCommodityPreferredUnitOfMeasure.action = self.UPDATE_ACTION_CODE;
					preferredUnitsOfMeasureChanges.push(subCommodityPreferredUnitOfMeasure);
				}
			}
			return preferredUnitsOfMeasureChanges;
		}

		/**
		 * Method called when a user hits the save button from the preferred units of measure modal.
		 */
		self.savePreferredUnitsOfMeasure = function () {

			var preferredUnitsOfMeasureChanges = getChangedPreferredUnitsOfMeasure();
			if(preferredUnitsOfMeasureChanges.length > 0){
				self.error = null;
				self.success = null;
				self.waitingForUpdate = true;
				productHierarchyApi.updateSubCommodityPreferredUnitsOfMeasure(
					preferredUnitsOfMeasureChanges, self.setUpdatedPreferredUnitsOfMeasure, fetchError);
			} else {
				fetchError({data: {message: self.ERROR_NO_CHANGES_DETECTED}});
			}
			self.canceldefaultUOMModal();
		};

		/**
		 * Updates original and selected preferred units of measure to full list of units of measure retrieved
		 * after the update.
		 */
		self.setUpdatedPreferredUnitsOfMeasure = function (results) {
			self.originalSubCommoditySelected.productPreferredUnitOfMeasureList = results;
			self.subCommoditySelected.productPreferredUnitOfMeasureList = results;
			self.waitingForUpdate = false;
			self.success = self.UPDATE_SUCCESS_MESSAGE;
		};

		/**
		 * Sets all product state warnings 'checked' value to the 'check all' boolean in productStateWarningModal.
		 */
		self.setAllProductStateWarnings = function(){
			var filter = self.getStateWarningFilter();
			var filteredData = filter !=null? $filter('filter')(self.productStateWarningsForEdit, filter) : self.productStateWarningsForEdit;
			angular.forEach(filteredData, function (value, index) {
				value.checked = self.allProductStateWarningsChecked;
			});
		};

		/**
		 * Return whether or not preferred unit of measure check box is disabled. If the preferred unit of measure
		 * has been selected (checked attribute == true), return false. Else, if there are already two preferred
		 * units of measure that have been checked, return true. Else return false.
		 *
		 * @returns {boolean} True if disabled, false if not disabled.
		 */
		self.isPreferredUnitOfMeasureCheckBoxDisabled = function(preferredUnitOfMeasure){
			if(self.isPreferredUnitOfMeasureChecked(preferredUnitOfMeasure)){
				return false;
			}
			var count = 0;
			for(var index = 0; index < self.preferredUnitsOfMeasureForEdit.length; index++){
				if(self.isPreferredUnitOfMeasureChecked(self.preferredUnitsOfMeasureForEdit[index])){
					count++;
					if(count === 2){
						return true;
					}
				}
			}
			return false;
		};

		/**
		 * Return whether or not preferred unit of measure is checked or not.
		 *
		 * @returns {boolean} True if checked, false if not not checked.
		 */
		self.isPreferredUnitOfMeasureChecked = function(preferredUnitOfMeasure){
			if(preferredUnitOfMeasure.hasOwnProperty('checked')){
				return preferredUnitOfMeasure.checked;
			}
			return false;
		};

		/**
		 * Return whether or not a preferred unit of measure's sequence number is disabled. It will be disabled if
		 * any of the preferred units of measure has that sequence number selected. This is so a user cannot select
		 * the same sequence number twice.
		 *
		 * @param sequenceNumber The sequence number in question.
		 * @returns {boolean} True if the sequence number should be disabled, false otherwise.
		 */
		self.isPreferredUnitOfMeasureSequenceDisabled = function (sequenceNumber) {
			for(var index = 0; index < self.preferredUnitsOfMeasureForEdit.length; index++) {
				if(self.preferredUnitsOfMeasureForEdit[index].sequenceNumber === sequenceNumber){
					return true;
				}
			}
			return false;
		};

		/**
		 * This function removes a sequence number from a preferred unit of measure when a user de-selects a unit of
		 * measure. This is to allow another preferred unit of measure to select that sequence number (otherwise the
		 * sequence number would be disabled).
		 *
		 * @param preferredUnitOfMeasure Preferred unit of measure to remove the sequence number property from.
		 */
		self.removeSequenceNumberOnPreferredUnitOfMeasureChangeIfNotChecked = function (preferredUnitOfMeasure) {
			if(!preferredUnitOfMeasure.checked && preferredUnitOfMeasure.hasOwnProperty('sequenceNumber')){
				delete preferredUnitOfMeasure['sequenceNumber'];
			}
		};

		/**
		 * Call api to get all vertex tax category codes used in a sub commodity.
		 */
		self.getAllVertexTaxCategories = function(){
			self.selectVertexTaxCategoryError = null;
			productHierarchyApi.getAllVertexTaxCategories(
				loadAllVertexTaxCategories,
				fetchError
			);
		};

		/**
		 * This is the callback function for getting all vertex tax categories.
		 */
		function loadAllVertexTaxCategories(results){
			self.allAvailableTaxCategories = results;
			angular.forEach(self.allAvailableTaxCategories, function (element) {
				if(element.dvrCode !== ""){
					element.categoryName = element.categoryName + ' [' + element.dvrCode + ']';
				}
				self.allNonTaxCategories.push(element);
				self.allTaxCategories.push(element);

			});
		}

		/**
		 * Init data for tax and non tax category drop down
		 */
		self.initDataForTaxAndNonTaxCategoryDropDown = function () {
			angular.forEach(self.allAvailableTaxCategories, function (element) {
				if(element.dvrCode == self.subCommoditySelected.taxCategoryCode)
				{
					self.selectedVertexTaxCategory =  angular.copy(element);
				}
				if(element.dvrCode == self.subCommoditySelected.nonTaxCategoryCode)
				{
					self.selectedVertexNonTaxCategory =  angular.copy(element);
				}

			});
		};

		/**
		 * Auto filter tax categories by search text
		 * @param search
		 */
		self.autoFilterForTaxCategories = function(search){
			var output=[];
			angular.forEach(self.allAvailableTaxCategories, function (element) {
				if(element.categoryName.toLowerCase().indexOf(search.toLowerCase())>=0){
					output.push(element);
				}
			});
			self.allTaxCategories = output;

		}
		/**
		 * Auto filter non tax categories by search text
		 * @param search
		 */
		self.autoFilterForNonTaxCategories = function(search){
			var output=[];
			angular.forEach(self.allAvailableTaxCategories, function (element) {
				if(element.categoryName.toLowerCase().indexOf(search.toLowerCase())>=0){
					output.push(element);
				}
			});
			self.allNonTaxCategories = output;

		}

		/**
		 * modifies the item class with any changes retrieved from the back end
		 * @param itemClass The item class selected by the user.
		 */
		function setActualItemClass(itemClass){
			self.originalItemClassSelected = itemClass;
			self.itemClassSelected = angular.copy(self.originalItemClassSelected);
			if(itemClass.itemClassDescription !== null){
				self.itemClassSelected.itemClassDescription = angular.copy(itemClass.itemClassDescription);
			}
		}

		/**
		 * modifies the commodity with any changes retrieved from the back end
		 * @param commodity The commodity selected by the user.
		 */
		function setActualCommodity(commodity){
			self.originalCommoditySelected = commodity;

			if(commodity.name !== null){
				self.originalCommoditySelected.name = commodity.name;
			}
			if(commodity.bdmCode !== null){
				self.originalCommoditySelected.bdmCode = commodity.bdmCode;
			}else{
				self.originalCommoditySelected.bdmCode = '';
			}

			if(commodity.eBMid !== null){
				self.originalCommoditySelected.eBMid = commodity.eBMid;
				//self.commoditySelected.eBMid = commodity.eBMid;
				userApi.getUserById({userId : commodity.eBMid},
					function (results){
						self.ebmUserDisplayName = results.displayName;
						self.ebmUserDisplayValue = self.ebmUserDisplayName;
					},
					fetchError
				);
				if(self.ebmUserDisplayName === null || self.ebmUserDisplayName === 'undefined'){
					self.ebmUserDisplayValue = angular.copy(commodity.eBMid);
				}
			}else{
				self.originalCommoditySelected.eBMid = '';
				self.ebmUserDisplayName = '';
				self.ebmUserDisplayValue = '';
			}
			if(commodity.bDAid !== null){
				self.originalCommoditySelected.bDAid = commodity.bDAid;
				//self.commoditySelected.bDAid = commodity.bDAid;
				userApi.getUserById({userId : commodity.bDAid},
					function (results){
						self.bdaUserDisplayName = results.displayName;
						self.bdaUserDisplayValue = self.bdaUserDisplayName;
					},
					fetchError
				);
				if(self.bdaUserDisplayName === null || self.bdaUserDisplayName === 'undefined') {
					self.bdaUserDisplayValue = angular.copy(commodity.bDAid);
				}
			}else{
				self.originalCommoditySelected.bDAid = '';
				self.bdaUserDisplayName = '';
				self.bdaUserDisplayValue = '';
			}
			if(commodity.bdm.displayName !== null){
				self.bdmUserDisplayValue = angular.copy(commodity.bdm.displayName);
			} else{
				self.bdmUserDisplayValue = '';
			}

			self.originalCommoditySelected.active = isCommodityActive(self.originalCommoditySelected);
			self.commoditySelected = angular.copy(self.originalCommoditySelected);
			setActualItemClass(commodity.itemClassMaster);
		}

		self.setBdmUserDisplayValueOnBlur = function () {
			if (self.bdmUserDisplayValue === self.commoditySelected.bdmCode){
				self.bdmUserDisplayValue = self.commoditySelected.bdm.displayName;
			}
		};
		self.setBdmUserDisplayValueOnFocus = function () {
			if (self.bdmUserDisplayValue === self.commoditySelected.bdm.displayName){
				self.bdmUserDisplayValue = self.commoditySelected.bdmCode;
			}
		};
		self.setEbmUserDisplayValueOnBlur = function () {
			if (self.ebmUserDisplayValue === self.commoditySelected.eBMid){
				self.ebmUserDisplayValue = self.ebmUserDisplayName;
			}
		};
		self.setEbmUserDisplayValueOnFocus = function () {
			if (self.ebmUserDisplayValue === self.ebmUserDisplayName){
				self.ebmUserDisplayValue = self.commoditySelected.eBMid;
			}
		};
		self.setBdaUserDisplayValueOnBlur = function () {
			if (self.bdaUserDisplayValue === self.commoditySelected.bDAid){
				self.bdaUserDisplayValue = self.bdaUserDisplayName;
			}
		};
		self.setBdaUserDisplayValueOnFocus = function () {
			if (self.bdaUserDisplayValue === self.bdaUserDisplayName){
				self.bdaUserDisplayValue = self.commoditySelected.bDAid;
			}
		};

		/**
		 * clear out the success or error message
		 */
		self.clearSuccessOrErrorMessaage = function(){
			self.error = null;
			self.success = null;
		}

		/**
		 * Clears filter after search cancellation
		 * @param search
		 */
		self.canceldefaultUOMModal = function(){
			self.defaultUnitOfMeasureFilter = "";
		};


		/**
		 * Clears search filter after search cancellation
		 * @param search
		 */
		self.cancelProductStateWarnings = function(){
			self.stateWarningsAbbreviationFilter = "";
			self.stateFilter = "";
		};

		/**
		 * Clears search filter after search cancellation
		 * @param search
		 */
		self.cancelShippingRestrictions = function(){
			self.shippingRestrictionModalSearch = "";
		};

		/**
		 * Show sub commodity audit info.
		 */
		self.showSubCommodityDefaultsAuditInfo = function(){
			self.isWaitingGetSubCommodityDefaultsAudit = true;
			productHierarchyApi.getSubCommodityDefaultsAudits(
				{
					"subCommodityCode":self.subCommoditySelected.key.subCommodityCode
				},
				//success case
				function (results) {
					self.subCommodityDefaultsAudit = results;
					self.initSubCommodityDefaultsAuditTable();
					self.isWaitingGetSubCommodityDefaultsAudit = false;
				}, fetchError);
			$('#sub-commodity-defaults-log-modal').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * Init data eCommerce View Audit.
		 */
		self.initSubCommodityDefaultsAuditTable = function () {
			$scope.filter = {
				attributeName: '',
				attributeValue: '',
				extendableAttValue: ''
			};
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			$scope.subCommodityDefaultsAuditTableParams = new ngTableParams({
				page: 1,
				count: 10,
				filter: $scope.filter,
				sorting: $scope.sorting

			}, {
				counts: [],
				data: self.subCommodityDefaultsAudit
			});
		}
		/**
		 * Change sort.
		 */
		self.changeSort = function (){
			if($scope.sorting.changedOn === ORDER_BY_DESC){
				$scope.sorting.changedOn = ORDER_BY_ASC;
			}else {
				$scope.sorting.changedOn = ORDER_BY_DESC;
			}
		}
		/**
		 * This method will be called when the filter or check on state warning has been changed to
		 * process check or un-check for check all on state warning table.
		 */
		self.onStateWarningCheck = function(){
			var backupProductStateWarningsForEdit = angular.copy(self.productStateWarningsForEdit);
			// Get filter criteria.
			var filter = self.getStateWarningFilter();
			// Get filtered data
			var filteredData = filter !=null? $filter('filter')(self.productStateWarningsForEdit, filter) : self.productStateWarningsForEdit;
			var isAllChecked =true;
			for(var index = 0; index < filteredData.length; index++){
				if(filteredData[index].checked != true){
					isAllChecked = false;
					break;
				}
			}
			self.allProductStateWarningsChecked = isAllChecked;
		}
		/**
		 * Get state warning filter criteria.
		 * @return {*}
		 */
		self.getStateWarningFilter = function(){
			var filter = null;
			if((self.stateWarningsAbbreviationFilter !== undefined && self.stateWarningsAbbreviationFilter !== null && self.stateWarningsAbbreviationFilter.length>0) ||
				(self.stateFilter !== undefined && self.stateFilter !== null && self.stateFilter.length>0)){
				filter =  {abbreviation: self.stateWarningsAbbreviationFilter, key: {stateCode: self.stateFilter}};
			}
			return filter;
		}
		/**
		 * Get shipping Restriction filter criteria.
		 * @return {*}
		 */
		self.getShippingRestrictionFilter = function(){
			var filter = null;
			if(self.shippingRestrictionModalSearch !== undefined && self.shippingRestrictionModalSearch !== null && self.shippingRestrictionModalSearch.length>0){
				filter =  {restrictionDescription: self.shippingRestrictionModalSearch};
			}
			return filter;
		}
		/**
		 * This method will be called when the filter or check on Shipping Restriction has been changed to
		 * process check or un-check for check all on ShippingRestriction table.
		 */
		self.onShippingRestrictionCheck = function(){
			// Get filter criteria.
			var filter = self.getShippingRestrictionFilter();
			// Get filtered data
			var filteredData = filter !=null? $filter('filter')(self.shippingRestrictionsCopy, filter) : self.shippingRestrictionsCopy;
			var isCheckAll = true;
			for(var index = 0; index < filteredData.length; index++){
				if(filteredData[index].applicableAtThisLevel != true){
					isCheckAll = false;
					break;
				}
			}
			self.allShippingRestrictionsChecked = isCheckAll;
		}
		/**
		 * This method will be called when the filter or check on selling Restriction has been changed to
		 * process check or un-check for check all on selling Restriction table.
		 */
		self.onSellingRestrictionCheck = function(){
			var isCheckAll = true;
			for(var index = 0; index < self.sellingRestrictionsCopy.length; index++){
				if(self.sellingRestrictionsCopy[index].checked != true){
					isCheckAll = false;
					break;
				}
			}
			self.allSellingRestrictionsChecked = isCheckAll;
		}


		/**
		 * validate don't allow user type space, special character, alphabet when user change the prod category id
		 */
		self.validateForProdCategoryId = function(){
            self.prodCategoryIdInvalid = false;
			self.productCategoryDisplayValue = self.productCategoryDisplayValue.trim();
            if(!(NUMBERS_ONLY_REGRULAR_EXPRESSION.exec(self.productCategoryDisplayValue)) || self.productCategoryDisplayValue.length >=10) {
                self.productCategoryDisplayValue = self.productCategoryDisplayValue.substr(0, self.productCategoryDisplayValue.length - 1);
                self.prodCategoryIdInvalid = true;

            }
		}

		/**
		 * validate don't allow user type space, special character when user change the IMS sub comm code
		 */
		self.validateForSubCommCode = function(){
            self.iMSSubCommCodeInvalid = false;
			self.subCommoditySelected.imsSubCommodityCode = self.subCommoditySelected.imsSubCommodityCode.trim();
			if(!(NUMBERS_ALPHABET_REGULAR_EXPRESSION.exec(self.subCommoditySelected.imsSubCommodityCode))) {
				self.subCommoditySelected.imsSubCommodityCode = '';
                self.iMSSubCommCodeInvalid = true;
			}
		}
	}
})();
