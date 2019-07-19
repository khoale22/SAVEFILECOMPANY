/*
 * customHierarchyController.js
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
	angular.module('productMaintenanceUiApp').controller('CustomHierarchyController', customHierarchyController);

	customHierarchyController.$inject = ['CustomHierarchyApi', 'customHierarchyService','$timeout', 'ProductSearchService','$http', '$stateParams', '$scope', '$rootScope', '$state', 'appConstants', 'ProductGroupService'];

	function customHierarchyController(customHierarchyApi, customHierarchyService, $timeout, productSearchService,$http, $stateParams, $scope, $rootScope, $state, appConstants, productGroupService) {
		var self = this;
		var UNDEFINED_TYPE = 'undefined';
		self.data = null;
		self.originalData = null;
		self.success = null;
		self.error = null;
		self.hierarchyContexts = null;
		self.newHierarchyName = null;
		self.newHierarchyContext = null;
		self.hierarchyContextSelected = null;
		self.singleHierarchyContextSelected = null;
		self.firstLevelSelected = null;
		self.secondLevelSelected = null;
		self.thirdLevelSelected = null;
		self.fourthLevelSelected = null;
		self.fifthLevelSelected = null;
		self.sixthLevelSelected = null;
		self.originalFirstLevelSelected = null;
		self.originalSecondLevelSelected = null;
		self.originalThirdLevelSelected = null;
		self.originalFourthLevelSelected = null;
		self.originalFifthLevelSelected = null;
		self.originalSixthLevelSelected = null;
		self.searchingForCustomHierarchyText = null;
		self.customHierarchySearchText = null;
		self.rootLevelView = false;
		self.searchingForCustomHierarchy = false;
		self.transactionMessage = null;
		/**
		 * The currently selected hierarchy context
		 * @type {null}
		 */
		self.hierarchyLevelSelected = null;
		/**
		 * When trying to rebuild the hiearchy after making changes this is the entity id of the previous state
		 * @type {null}
		 */
		self.entityIdReceived = null;
		/**
		 * date picker for the effective date field
		 * @type {boolean}
		 */
		self.effectiveDatePickerOpened = false;
		/**
		 * Date picker for the end date field
		 * @type {boolean}
		 */
		self.endDatePickerOpened = false;
		/**
		 * This will hold the current level attributes and any changes that were made
		 * @type {null}
		 */
		self.currentLevelAttributes= null;
		/**
		 * This hold the original current level attributes used as a reference for detecting changes
		 * @type {{}}
		 */
		self.originalCurrentLevelAttributes={};
		/**
		 * This holds the options
		 * @type {null}
		 */
		self.options = null;
		/**
		 * The current state of the current level images this will hold any changes that were made
		 * @type {Array}
		 */
		self.currentLevelImages = [];
		/**
		 * original currentLevel images used as a reference
		 * @type {Array}
		 */
		self.originalCurrentLevelImages = [];
		/**
		 * Flag for when attempting to load a path choice
		 * @type {boolean}
		 */
		self.isLoadingPathChoice = false;
		/**
		 * The currently selected level
		 * @type {null}
		 */
		self.currentLevel = null;
		/**
		 * holds the original state of the current level it is used as a reference to observe changes
		 * @type {null}
		 */
		self.originalCurrentLevel=null;
		self.checkedForProductsAndGroups = false;

		/**
		 * holds the level to be removed
		 * @type {null}
		 */
		self.levelToBeRemoved = null;
		/**
		 * Tells the user when they attempt an illegal change to the hierarchy
		 * @type {null}
		 */
		self.removeConfirmationMessage = null;
		/**
		 * The current selected attribute's id
		 * @type {null}
		 */
		self.selectedAttributeId = null;
		/**
		 * This values hold the basic information for creating a new hierarchy level
		 * @type {null}
		 */
		self.addLevelDescription = null;
		self.currentEffectiveDate = new Date(Date.now());
		self.currentEndDate = new Date('December 31, 9999');
		self.addLevelActiveSwitch = true;
		self.updateMessage = "Updating...please wait.";
		var UNKNOWN_ERROR = "An unknown error occurred.";
		var UPDATE_SUCCESS_MESSAGE = "UPDATE_SUCCESS_MESSAGE";
		var UPDATE_ERROR_MESSAGE = "UPDATE_ERROR_MESSAGE";
		var DEFAULT_SUCCESS_MESSAGE = "Successfully updated.";
		var WAITING_FOR_UPDATE_MESSAGE = "WAITING_FOR_UPDATE_MESSAGE";
		var currentRequest = 0;
		self.fromPage = null;
		self.isHierarchyLoaded = false;
		const FROM_PRODUCT_GROUP_SEARCH = "FROM_PRODUCT_GROUP_SEARCH";
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the controller is initialized
		 * (or re-initialized).
		 */
		self.$onInit = function () {
			if(customHierarchyService.getFromPage() !== null){
			customHierarchyService.setFromPage(null);
			}

		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			$rootScope.contentChangedFlag = false;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		function fetchError(error) {
			customHierarchyService.setErrorMessage(getErrorMessage(error), true);
		}

		/**
		 * Sets the controller's error message.
		 * @param error The error message.
		 */
		function getErrorMessage(error) {
			if(error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return UNKNOWN_ERROR;
			}
		}

		/**
		 * Sets the controller's success message.
		 * @param success The success message.
		 */
		function getSuccessMessage(success) {
			if(success && success.message) {
				return success.message;
			}
			else {
				return DEFAULT_SUCCESS_MESSAGE;
			}
		}

		self.showProductPanel = function() {
			return self.currentLevel;
		};

		/**
		 * This method is used to sync the parent scope with all of its children and determine if the pannel are the
		 * right should be shown
		 * @returns {boolean|*}
		 */
		self.showPanels = function () {
			return self.showProductPanel();
		};

		/**
		 * Sets the current duty confirmation date.
		 */
		self.setDateForEffectiveDatePicker = function () {
			self.effectiveDatePickerOpened = false;
			if(self.currentLevelAttributes !== null){
				if (self.currentLevelAttributes.effectiveDate !== null) {
					self.currentEffectiveDate =
						new Date(self.currentLevelAttributes.effectiveDate);
				} else {
					self.currentEffectiveDate = new Date(Date.now());
				}
			}
		};

		/**
		 * Sets the current duty confirmation date.
		 */
		self.setDateForEndDatePicker = function () {
			self.endDatePickerOpened = false;
			if(self.currentLevelAttributes !== null){
				if (self.currentLevelAttributes.endDate !== null) {
					self.currentEndDate =
						new Date(self.currentLevelAttributes.endDate);
				} else {
					self.currentEndDate = null;
				}
			}
		};

		/**
		 * This method handles successful additional levels to a hierarchy
		 * @param results
		 */
		self.successfulAddHierarchyChange= function(results){
			customHierarchyService.setSuccessMessage(results.message, true);
			customHierarchyService.updateSelectedHierarchy();
		};

		/**
		 * Checks if there is a difference in any component
		 * @returns {*}
		 */
		self.isDifferent =function () {
			var isDifferent = customHierarchyService.getIsCurrentDifferent();
			if(isDifferent){
				$rootScope.contentChangedFlag = true;
			} else {
				$rootScope.contentChangedFlag = false;
			}
			return !isDifferent;
		};
		/**
		 * This method well reset the current level attributes and the current level image data back to its original state
		 */
		self.reset = function () {
			customHierarchyService.setErrorMessage(null);
			customHierarchyService.setSuccessMessage(null);
			self.currentLevel = angular.copy(self.originalCurrentLevel);
		};

		/**
		 * This method is used to communicate changes for the current level when save is pressed
		 */
		self.save = function () {
			customHierarchyService.setErrorMessage(null);
			customHierarchyService.setSuccessMessage(null);
			if(customHierarchyService.getValidateCurrentLevelImagesMessage() != null && customHierarchyService.getValidateCurrentLevelImagesMessage() != '' ){
				self.error = customHierarchyService.getValidateCurrentLevelImagesMessage();
				customHierarchyService.setErrorMessage(customHierarchyService.getValidateCurrentLevelImagesMessage());
			} else{
				resetDefaults();
				customHierarchyService.setWaitingForUpdate(true, true);

				var currentLevelChanges = customHierarchyService.getCurrentLevelAttributeChanges();
				var currentImagesChanges = customHierarchyService.getCurrentLevelImageChanges();
				if(currentLevelChanges !== null){
					customHierarchyApi.updateCurrentLevel(currentLevelChanges,
						function(results){
							self.waitingForUpdate = false;
							customHierarchyService.setSuccessMessage(getSuccessMessage(results), true);
							customHierarchyService.updateSelectedHierarchy();
						},
						function (error) {
							self.waitingForUpdate = false;
							fetchError(error);
						});
				}
				if(currentImagesChanges != null){
					if(currentImagesChanges.length > 0) {
						customHierarchyApi.updateImageMetadata(currentImagesChanges,
							function(results){
								customHierarchyService.setSuccessMessage(getSuccessMessage(results), true);
								customHierarchyService.updateSelectedHierarchy();
							},
							function (error) {
								self.waitingForUpdate = false;
								fetchError(error);
							});
					}
				}
			}
			
		};

		/**
		 * Callback for when a hierarchy level is selected from the custom hierarchy search component.
		 *
		 * @param selectedLevel Level selected.
		 * @param requestNumber A number attached to this request to ensure this is latest request.
		 * @param keepCurrentMessages Whether or not to keep the current messages.
		 */
		self.setCurrentLevel = function(selectedLevel, requestNumber, keepCurrentMessages){
			self.isHierarchyLoaded = true;
			if(!keepCurrentMessages) {
				resetDefaults();
			}
			if(requestNumber > currentRequest) {
				currentRequest = requestNumber;
				var tempSelectedLevel = angular.copy(selectedLevel);
				// relationship selected
				if (typeof selectedLevel['id'] === 'undefined') {
					self.intermediateView = true;
					formatDatesForDatePicker(tempSelectedLevel);
					self.originalCurrentLevel = tempSelectedLevel;
					self.currentLevel = angular.copy(tempSelectedLevel);
				}
				// hierarchy context selected
				else {
					self.intermediateView = false;
					formatDatesForDatePicker(tempSelectedLevel);
					tempSelectedLevel.key = {
						childEntityId: selectedLevel.parentEntityId
					};
					tempSelectedLevel.childDescription = {
						shortDescription: selectedLevel.description
					};
					self.originalCurrentLevel = tempSelectedLevel;
					self.currentLevel = angular.copy(tempSelectedLevel);
				}
			}
		};

		/**
		 * Formats dates for date pickers.
		 */
		function formatDatesForDatePicker(levelToFormat){
			if(levelToFormat.effectiveDate && levelToFormat.effectiveDate !== null){
				levelToFormat.effectiveDate = new Date(levelToFormat.effectiveDate.replace(/-/g, '\/'))
			}
			if(levelToFormat.expirationDate && levelToFormat.expirationDate !== null){
				levelToFormat.expirationDate = new Date(levelToFormat.expirationDate.replace(/-/g, '\/'))
			}
		}

		/**
		 * This method watches for a broadcast of the UPDATE_SUCCESS_MESSAGE to notify this component that
		 * there was an update to the update success message.
		 */
		$scope.$on(UPDATE_SUCCESS_MESSAGE, function(){
			self.success = customHierarchyService.getSuccessMessage();
		});

		/**
		 * This method watches for a broadcast of the UPDATE_ERROR_MESSAGE to notify this component that
		 * there was an update to the error message.
		 */
		$scope.$on(UPDATE_ERROR_MESSAGE, function(){
			self.error = customHierarchyService.getErrorMessage();
		});

		/**
		 * This method watches for a broadcast of the UPDATE_ERROR_MESSAGE to notify this component that
		 * there was an update to the error message.
		 */
		$scope.$on(WAITING_FOR_UPDATE_MESSAGE, function(){
			self.waitingForUpdate = customHierarchyService.getWaitingForUpdateMessage();
		});

		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param hierarchyLevel
		 * @returns {boolean}
		 */
		self.isHierarchyLevelCollapsed = function(hierarchyLevel){
			if(typeof hierarchyLevel.isCollapsed === UNDEFINED_TYPE){
				return true;
			}else {
				return hierarchyLevel.isCollapsed;
			}
		};

		/**
		 * Helper method to determine if the current state is an intermediate hierarchy level view.
		 *
		 * @returns {boolean}
		 */
		self.isIntermediateLevel = function(){
			return self.currentLevel && self.intermediateView
		};

		/**
		 * Helper method to determine if the current state is an intermediate hierarchy level view without product
		 * children.
		 *
		 * @returns {boolean}
		 */
		self.isNonProductChildrenLevel = function(){
			return !self.isProductLevel()
		};

		/**
		 * Helper method to determine if the current level is defined.
		 *
		 * @returns {boolean}
		 */
		self.isCurrentLevelDefined = function(){
			return self.currentLevel !== null;
		};

		/**
		 * This method checks to see if the current selected level is a product level(has products or product groups
		 * as children).
		 * @returns {boolean}
		 */
		self.isProductLevel = function(){
			return self.currentLevel !== null && self.currentLevel.countOfProductChildren > 0;
		};

		/**
		 * Resets the default values for this controller.
		 */
		function resetDefaults(){
			customHierarchyService.setSuccessMessage(null, true);
		}
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			if(customHierarchyService.getNavigateFromProdGroupTypePage()){
				$state.go(appConstants.CODE_TABLE);
			}else{
			customHierarchyService.setFromPage(FROM_PRODUCT_GROUP_SEARCH);
			customHierarchyService.setDisableReturnToList(true);
				productGroupService.setReturnToListFlag(true)
			//productGroupService.setNavigateFromCustomerHierPage(true);
				$state.go(appConstants.PRODUCT_GROUP);
			}
		};
		/**
		 * Return status to show return to list button.
		 * @return {boolean}
		 */
		self.isShowingReturnToListButton = function(){
			return !customHierarchyService.getDisableReturnToList() && self.isHierarchyLoaded;
		}

		/**
		 * resets selectedAttributeId to default value
		 */
		self.returnFromDirective = function(){
			self.selectedAttributeId = null;
		}
	}
})();
