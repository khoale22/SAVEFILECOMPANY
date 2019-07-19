/*
* discontinueParametersService
*
* Copyright (c) 2016 HEB
* All rights reserved.
*
* This software is the confidential and proprietary information
* of HEB.
*
*/

'use strict';

/**
 * Manages communication between SinglePluPanel, the PluDirective and the Maintenance UPC component
 *
 * @author s753601
 * @since 2.13.0
 */
(function(){
	angular.module('productMaintenanceUiApp').service('customHierarchyService', customHierarchyService);

	customHierarchyService.$inject = ['$rootScope'];

	/**
	 * Constructs the singlePluPanelService.
	 *
	 * @param $rootScope The Angular scope representing this application.
	 * @returns
	 */
	function customHierarchyService($rootScope){
		var self = this;
		/**
		 * Entity id to set starting point for custom hierarchy
		 * @type {null}
		 */
		self.entityId=null;
		/**
		 * Entity id to set starting point for custom hierarchy
		 * @type {null}
		 */
		self.entityIdReceived = null;
		/**
		 * The currently selected hierarchy context
		 * @type {null}
		 */
		self.hierarchyContextSelected=null;
		/**
		 * Child relationships for a hierarchy
		 * @type {null}
		 */
		self.childData=null;
		self.disableReturnToList = true;
		/**
		/**
		 * The upc of an image used to link back to product details
		 * @type {null}
		 */
		self.referenceUPC = null;
		/**
		 * the current hierarchy selected
		 * @type {null}
		 */
		self.hierarchy = null;
		self.selectedTab = null;
		/**
		 * the current hierarchy selected for navigation
		 * @type {Object}
		 */
		self.selectedHierarchyContextForNavigation = null;
		/**
		 * the root hierarchy selected for navigation
		 * @type {Object}
		 */
		self.selectedHierarchyContextRoot = null;
		/**
		 * the current hierarchy level selected for navigation
		 * @type {Object}
		 */
		self.selectedCustomHierarchyLevel = null;
		/**
		 * flag for if there is any updates to grap
		 * @type {null}
		 */
		self.fetchingUpdate = null;
		self.hierarchyContextId = null;
		/**
		 * Navigate from product group info page to another page or not
		 * @type {Boolean}
		 */
		self.returnToListFlag = false;
		/**
		 * From page navigated to
		 * @type {String}
		 */
		self.fromPage = null;
		/**
		 * A flag for when a current level is different
		 * @type {boolean}
		 */
		self.isCurrentDifferent = false;
		/**
		 * Navigate from product group info page to another page or not
		 * @type {Boolean}
		 */
		self.returnToListFromCustomerHierPage = false;
		/**
		 * Navigate from product group info page to another page or not
		 * @type {Boolean}
		 */
		self.navigateFromCustomerHierPage = false;
		self.navigateFromProdGroupTypePage = false;
		self.navigatedForFirstSearch = false;
		self.notFacingHierarchyLink = false;
		/**
		 * Product group type code
		 */
		self.productGroupTypeCodeNav = null;
		/**
		 * Message when validate currentLevelImages.
		 * @type {String}
		 */
		self.validateCurrentLevelImagesMessage = null;
		/**
		 * Navigate from product group info page to another page or not
		 * @type {Boolean}
		 */
		self.navigatedFromOtherPage = false;
		var UPDATE_SELECTED_HIERARCHY = "UPDATE_SELECTED_HIERARCHY";
		var UPDATE_SUCCESS_MESSAGE = "UPDATE_SUCCESS_MESSAGE";
		var UPDATE_ERROR_MESSAGE = "UPDATE_ERROR_MESSAGE";
		var SET_HIERARCHY_TO_SELECTED_LEVEL = "SET_HIERARCHY_TO_SELECTED_LEVEL";
		var SET_HIERARCHY_TO_SELECTED_LEVEL_AND_UPDATE = "SET_HIERARCHY_TO_SELECTED_LEVEL_AND_UPDATE";
		var UPDATING_MESSAGE = "Updating...please wait.";
		var WAITING_FOR_UPDATE_MESSAGE = "WAITING_FOR_UPDATE_MESSAGE";
		self.successMessage = null;
		self.errorMessage = null;
		self.imageChanges = null;
		self.attributeChanges = null;
		self.currentLevelTab = "PROPERTIES_TAB";
		self.selectedLevelArray = null;

		return{
			/**
			 * updates the child data
			 * @param data
			 */
			setChildData:function (data) {
				self.childData = angular.copy(data);
			},
			/**
			 * returns the child data
			 * @returns {null}
			 */
			getChildData:function () {
				return self.childData
			},
			/**
			 * updates the entity id
			 * @param entityId
			 */
			setEntityId:function (entityId) {
				self.entityId=entityId;
			},
			/**
			 * updates the entity id
			 * @returns {null}
			 */
			getEntityId: function () {
				return self.entityId;
			},
			/**
			 * updates the entity id
			 * @param entityId
			 */
			setEntityIdReceived:function (entityIdReceived) {
				self.entityIdReceived=entityIdReceived;
			},
			/**
			 * updates the entity id
			 * @returns {null}
			 */
			getEntityIdReceived: function () {
				return self.entityIdReceived;
			},
			/**
			 * Returns selected tab after
			 *
			 * @returns {null|*} The type of search the user has chosen.
			 */
			getSelectedTab:function() {
				return self.selectedTab;
			},

			/**
			 * Sets the type of search the user has chosen.
			 *
			 * @param searchType The type of search the user has chosen.
			 */
			setSelectedTab:function(selectedTab) {
				self.selectedTab = selectedTab;
			},
			/**
			 * Returns disable return to list flag
			 *
			 * @returns {true|false}
			 */
			getDisableReturnToList:function() {
				return self.disableReturnToList;
			},

			/**
			 * Sets disable return to list flag
			 *
			 * @param returnToListFlag
			 */
			setDisableReturnToList:function(disableReturnToList) {
				self.disableReturnToList = disableReturnToList;
			},
			/**
			 * Returns hierarchy context the user has selected for navigation.
			 *
			 * @returns selectedHierarchyContextForNavigation
			 */
			getSelectedHierarchyContextForNavigation:function(){
				return self.selectedHierarchyContextForNavigation;
			},

			/**
			 * Sets hierarchy context the user has selected for navigation.
			 *
			 * @param selectedHierarchyContextForNavigation
			 */
			setSelectedHierarchyContextForNavigation:function(selectedHierarchyContextForNavigation){
				self.selectedHierarchyContextForNavigation = selectedHierarchyContextForNavigation;
			},

			/**
			 * Returns hierarchy context root the user has selected for navigation.
			 *
			 * @returns selectedHierarchyContextRoot
			 */
			getSelectedHierarchyContextRoot:function(){
				return self.selectedHierarchyContextRoot;
			},

			/**
			 * Sets hierarchy context root the user has selected for navigation.
			 *
			 * @param selectedHierarchyContextRoot
			 */
			setSelectedHierarchyContextRoot:function(selectedHierarchyContextRoot){
				self.selectedHierarchyContextRoot =selectedHierarchyContextRoot;
			},
			/**
			 * Returns custom hierarchy level the user has selected for navigation.
			 *
			 * @returns selectedCustomHierarchyLevel
			 */
			getSelectedCustomHierarchyLevel:function(){
				return self.selectedCustomHierarchyLevel;
			},

			/**
			 * Sets custom hierarchy level the user has selected for navigation.
			 *
			 * @param selectedCustomHierarchyLevel
			 */
			setSelectedCustomHierarchyLevel:function(selectedCustomHierarchyLevel){
				self.selectedCustomHierarchyLevel =selectedCustomHierarchyLevel;
			},
			/**
			 * updates the hierarchy context selected
			 * @param hierarchyContextSelected
			 */
			setHierarchyContextSelected: function (hierarchyContextSelected) {
				self.hierarchyContextSelected=hierarchyContextSelected;
			},
			/**
			 * returns the hierarhcy context selected
			 * @returns {null}
			 */
			getHierarchyContextSelected: function () {
				return self.hierarchyContextSelected;
			},
			/**
			 * flag for is the current level is different
			 * @returns {*}
			 */
			getIsCurrentDifferent: function () {
				return self.isCurrentDifferent;
			},
			/**
			 * updates the flag for if the current level is different
			 * @param isCurrentDifferent
			 */
			setIsCurrentDifferent: function (isCurrentDifferent) {
				self.isCurrentDifferent = isCurrentDifferent;
			},
			/**
			 * returns the reference upc
			 * @returns {null}
			 */
			getReferenceUPC: function () {
				return self.referenceUPC;
			},
			/**
			 * updates the reference upc
			 * @param referenceUPC
			 */
			setReferenceUPC: function (referenceUPC) {
				self.referenceUPC = referenceUPC;
			},
			/**
			 * update the fetch update flag
			 * @param fetchingUpdate
			 */
			setFetchingUpdate: function (fetchingUpdate) {
				self.fetchingUpdate = fetchingUpdate;
			},
			/**
			 * This method will broadcasts the message that the selected hierarchy needs to be updated.
			 */
			updateSelectedHierarchy:function() {
				$rootScope.$broadcast(UPDATE_SELECTED_HIERARCHY);
			},
			/**
			 * Gets the message for an success.
			 */
			getSuccessMessage:function() {
				return self.successMessage;
			},
			/**
			 * Sets the message for a success.
			 * @param successMessage
			 * @param setOthers
			 */
			setSuccessMessage:function(successMessage, setOthers) {
				if(setOthers) {
					this.setErrorMessage(null, false);
					this.setWaitingForUpdate(false, false);
				}
				self.successMessage = successMessage;
				$rootScope.$broadcast(UPDATE_SUCCESS_MESSAGE);
			},
			/**
			 * Gets the message for an error.
			 */
			getErrorMessage:function() {
				return self.errorMessage;
			},
			/**
			 * Sets the message for an error.
			 * @param errorMessage
			 * @param setOthers
			 */
			setErrorMessage:function(errorMessage, setOthers) {
				if(setOthers) {
					this.setSuccessMessage(null, false);
					this.setWaitingForUpdate(false, false);
				}
				self.errorMessage = errorMessage;
				$rootScope.$broadcast(UPDATE_ERROR_MESSAGE);
			},
			/**
			 * Gets the message for an error.
			 */
			getWaitingForUpdateMessage:function() {
				return self.updateMessage;
			},
			/**
			 * Sets the updating message.
			 * @param waitingForUpdate
			 * @param setOthers
			 */
			setWaitingForUpdate:function(waitingForUpdate, setOthers) {
				if(setOthers) {
					this.setErrorMessage(null, false);
					this.setSuccessMessage(null, false);
				}
				if(waitingForUpdate){
					self.updateMessage = UPDATING_MESSAGE;
				} else {
					self.updateMessage = null;
				}
				$rootScope.$broadcast(WAITING_FOR_UPDATE_MESSAGE);
			},
			/**
			 * Gets the current level image changes
			 * @returns {Object}
			 */
			getCurrentLevelImageChanges:function() {
				return self.imageChanges;
			},
			/**
			 * Sets the current level image changes
			 * @param imageChanges
			 */
			setCurrentLevelImageChanges:function(imageChanges) {
				self.imageChanges = imageChanges;
			},
			/**
			 * Gets the current level image changes
			 * @returns {Object}
			 */
			getHierarchyContextId:function() {
				return self.hierarchyContextId;
			},
			/**
			 * Sets the current level image changes
			 * @param imageChanges
			 */
			setHierarchyContextId:function(hierarchyContextId) {
				self.hierarchyContextId = hierarchyContextId;
			},

			/**
			 * Gets the current level attribute changes
			 * @returns {Object}
			 */
			getCurrentLevelAttributeChanges: function () {
				return self.attributeChanges;
			},
			/**
			 * Sets the current level attribute changes
			 * @param attributeChanges
			 */
			setCurrentLevelAttributeChanges: function (attributeChanges) {
				self.attributeChanges = attributeChanges;
			},
			/**
			 * Returns the current level tab
			 * @returns {String}
			 */
			getCurrentLevelTab: function () {
				return self.currentLevelTab;
			},
			/**
			 * sets the current level tab
			 * @param currentLevelTab
			 */
			setCurrentLevelTab: function (currentLevelTab) {
				self.currentLevelTab = currentLevelTab;
			},
			/**
			 * Returns the selected level array (array of relationships to selected level)
			 * @returns {array}
			 */
			getHierarchyToSelectedLevel: function () {
				return self.selectedLevelArray;
			},
			/**
			 * sets the selected level array (array of relationships to selected level)
			 * @param selectedLevelArray
			 */
			setHierarchyToSelectedLevel: function (selectedLevelArray, justSet) {
				self.selectedLevelArray = selectedLevelArray;
				if(!justSet) {
					$rootScope.$broadcast(SET_HIERARCHY_TO_SELECTED_LEVEL);
				}
			},
			/**
			 * Returns return to list flag
			 *
			 * @returns {true|false}
			 */
			getReturnToListFlag:function() {
				return self.returnToListFlag;
			},

			/**
			 * Sets return to list flag.
			 *
			 * @param returnToListFlag
			 */
			setReturnToListFlag:function(returnToListFlag) {
				self.returnToListFlag = returnToListFlag;
			},
			/**
			 * Returns return to list flag
			 *
			 * @returns {true|false}
			 */
			getReturnToListFromCustomerHierPage:function() {
				return self.returnToListFromCustomerHierPage;
			},

			/**
			 * Sets return to list flag.
			 *
			 * @param returnToListFromCustomerHierPage
			 */
			setReturnToListFromCustomerHierPage:function(returnToListFromCustomerHierPage) {
				self.returnToListFromCustomerHierPage= returnToListFromCustomerHierPage;
			},
			/**
			 * Returns return to list flag
			 *
			 * @returns {true|false}
			 */
			getNavigateFromCustomerHierPage:function() {
				return self.navigateFromCustomerHierPage;
			},

			/**
			 * Sets return to list flag.
			 *
			 * @param returnToListFromCustomerHierPage
			 */
			setNavigateFromCustomerHierPage:function(navigateFromCustomerHierPage) {
				self.navigateFromCustomerHierPage= navigateFromCustomerHierPage;
			},
			/**
			 * Returns navigatedForFirstSearch
			 *
			 * @returns {true|false}
			 */
			getNavigatedForFirstSearch:function() {
				return self.navigatedForFirstSearch;
			},

			/**
			 * Sets navigatedForFirstSearch.
			 *
			 * @param navigatedForFirstSearch
			 */
			setNavigatedForFirstSearch:function(navigatedForFirstSearch) {
				self.navigatedForFirstSearch= navigatedForFirstSearch;
			},
			/**
			 * Returns return to list flag
			 *
			 * @returns {true|false}
			 */
			getNotFacingHierarchyLink:function() {
				return self.notFacingHierarchyLink;
			},

			/**
			 * Sets return to list flag.
			 *
			 * @param returnToListFromCustomerHierPage
			 */
			setNotFacingHierarchyLink:function(notFacingHierarchyLink) {
				self.notFacingHierarchyLink= notFacingHierarchyLink;
			},

			/**
			 * Returns return to list flag
			 *
			 * @returns {true|false}
			 */
			getNavigatedFromOtherPage:function() {
				return self.navigatedFromOtherPage;
			},

			/**
			 * Sets return to list flag.
			 *
			 * @param returnToListFromCustomerHierPage
			 */
			setNavigatedFromOtherPage:function(navigatedFromOtherPage) {
				self.navigatedFromOtherPage = navigatedFromOtherPage;
			},
			/**
			 * Returns from page
			 *
			 * @returns {String}
			 */
			getFromPage:function() {
				return self.fromPage;
			},

			/**
			 * Sets from page.
			 *
			 * @param fromPage
			 */
			setFromPage:function(fromPage) {
				self.fromPage = fromPage;
			},
			/**
			 * sets the selected level array (array of relationships to selected level)
			 * @param selectedLevelArray
			 */
			setHierarchyToSelectedLevelAndUpdate: function (selectedLevelArray) {
				self.selectedLevelArray = selectedLevelArray;
				$rootScope.$broadcast(SET_HIERARCHY_TO_SELECTED_LEVEL_AND_UPDATE);
			},
			/**
			 * Return navigateFromProdGroupTypePage
			 *
			 * @returns {true|false}
			 */
			getNavigateFromProdGroupTypePage:function() {
				return self.navigateFromProdGroupTypePage;
			},

			/**
			 * Set navigateFromProdGroupTypePage.
			 *
			 * @param navigateFromProdGroupTypePage
			 */
			setNavigateFromProdGroupTypePage:function(navigateFromProdGroupTypePage) {
				self.navigateFromProdGroupTypePage= navigateFromProdGroupTypePage;
			},

			/**
			 * Return product group type code of product group type.
			 *
			 * @returns {true|false}
			 */
			getProductGroupTypeCodeNav:function() {
				return self.productGroupTypeCodeNav;
			},

			/**
			 * Set product group type code of product group type.
			 *
			 * @param returnToListFlag
			 */
			setProductGroupTypeCodeNav:function(productGroupTypeCodeNav) {
				self.productGroupTypeCodeNav = productGroupTypeCodeNav;
			},
			/**
			 * Returns the message
			 * @returns {String}
			 */
			getValidateCurrentLevelImagesMessage: function () {
				return self.validateCurrentLevelImagesMessage;
			},
			/**
			 * sets the message
			 * @param validateCurrentLevelImagesMessage
			 */
			setValidateCurrentLevelImagesMessage: function (validateCurrentLevelImagesMessage) {
				self.validateCurrentLevelImagesMessage = validateCurrentLevelImagesMessage;
			},

		}
	}
})();
