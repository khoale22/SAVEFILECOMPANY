/*
 *   allParentPanelComponent.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Custom Hierarchy -> Children panel component.
 *
 * @author l730832
 * @since 2.16.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('childrenPanel', {
		// isolated scope binding
		bindings: {
			currentLevel: '<'
		},
		scope: {},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/customHierarchy/childrenPanel.html',
		// The controller that handles our component logic
		controller: childrenPanelController
	});


	childrenPanelController.$inject = ['customHierarchyService', 'CustomHierarchyApi', 'PermissionsService', '$scope'];

	/**
	 * Case Pack Import component's controller definition.
	 * @constructor
	 * @param customHierarchyService
	 * @param customHierarchyApi
	 * @param permissionsService
	 * @param $scope
	 */
	function childrenPanelController(customHierarchyService, customHierarchyApi, permissionsService, $scope) {
		var self = this;

		self.pathList = null;

		// Mass update for unassign products.
		self.massUpdateType = "UNASSIGN_PRODUCTS";

		self.unassignProductsDescriptionBooleanValue = "true";
		self.unassignProductsDescriptionStringValue = "";
		self.unassignProductsDescription = null;
		self.selectedProducts = [];
		self.unassignProductsTransactionMessage = null;
		self.hasPathChangeCompleted = false;
		self.levelToBeRemoved = null;

		/**
		 * Search criteria used during Add Products to Task.
		 */
		self.searchCriteria = "";
		/**
		 * This method converts a string to a date
		 * @type {*|function}
		 */
		var convertDate = $scope.$parent.convertDate;
		var PRODUCT_GROUP_ENTITY_TYPE = "PGRP";
		var ACTION_CODE_ADDING = 'Y';
		var ADD_HIERARCHY_PRODUCT_GROUP = "ADD_HIERARCHY_PRODUCT_GROUP";
		var REMOVE_LEVEL_CONFIRMATION_MESSAGE = "Do you want to remove all paths to the children, or only the current path to the children?";

		self.$onChanges = function () {
			self.originalCurrentLevel = self.currentLevel;
			getImmediateNonProductChildren();
			// reset the remove button when clicking on another level
			self.levelToBeRemoved = null;
		};

		function getImmediateNonProductChildren(){
			// relationship selected
			if(typeof self.originalCurrentLevel['id'] === 'undefined'){
				customHierarchyApi.getImmediateNonProductChildren(
					{
						parentEntityId: self.originalCurrentLevel.key.childEntityId,
						hierarchyContextId: self.originalCurrentLevel.key.hierarchyContext
					},
					function(results){
						self.originalCurrentLevel.childRelationships = results;
						self.currentLeve = angular.copy(self.originalCurrentLevel);
					},
					fetchError
				);
			}
			// hierarchy context selected
			else {
				customHierarchyApi.getImmediateNonProductChildren(
					{
						parentEntityId: self.originalCurrentLevel.parentEntityId,
						hierarchyContextId: self.originalCurrentLevel.id
					},
					function(results){
						self.originalCurrentLevel.childRelationships = results;
						self.currentLeve = angular.copy(self.originalCurrentLevel);
					},
					fetchError
				);
			}
		}

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		function fetchError(error) {
			self.isWaiting = false;
			self.isWaitingForData = false;
			self.data = null;
			customHierarchyService.setErrorMessage(getError(error), true);
		}

		/**
		 * Gets the error given an api error message.
		 *
		 * @param error Error message from api.
		 * @returns {string}
		 */
		function getError(error){
			if (error && error.data) {
				if(error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return "An unknown error occurred.";
			}
		}

		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param relationship
		 * @returns {boolean}
		 */
		self.isLowestBranchOrHasNoRelationships = function(relationship){
			if(relationship.lowestBranch){
				return true;
			}else if(relationship.childRelationships === null || relationship.childRelationships.length === 0) {
				return true;
			}
			return false;
		};

		self.selectChildRelationship = function(childRelationship){
			self.childData = [];
			customHierarchyApi.getCurrentLevel(
				childRelationship.key,
				loadChildData,
				fetchError);
		};

		function loadChildData(results){
			results.isCollapsed = false;
			if(typeof results['childRelationships'] !== 'undefined'){
				expandAllCustomHierarchyRelationships(results.childRelationships);
			}
			self.childData.push(results);
		}

		/**
		 * This function sets the collapsed variable to false for all current levels of the product hierarchy.
		 */
		function expandAllCustomHierarchyRelationships(relationships){
			angular.forEach(relationships, function (relationship) {
				relationship.isCollapsed = false;
				if(typeof relationship['childRelationships'] !== 'undefined'){
					expandAllCustomHierarchyRelationships(relationship.childRelationships);
				}
			});
		}

		/**
		 * This method clears our all of the previous values for the add level modal
		 */
		self.clearAddLevelModal = function () {
			self.addLevelDescription = null;
			self.currentEffectiveDate = new Date(Date.now());
			self.currentEndDate = new Date('December 31, 9999');
			self.addLevelActiveSwitch = true;
		};

		/**
		 * Method that calls api to add a new level to selected Level and Hierarchy context.
		 *
		 * @param effectiveDate
		 * @param endDate
		 * @param description
		 * @param activeSwitch
		 */
		self.addCustomHierarchy = function (effectiveDate, endDate, description, activeSwitch) {

			customHierarchyService.setWaitingForUpdate(true, true);
			// self.currentLevel.key = {parentEntityId: self.currentLevel.parentEntityId};
			if(angular.isUndefined(activeSwitch)) {
				activeSwitch = false;
			}

			var currentLevel = angular.copy(self.currentLevel);
			currentLevel.effectiveDate = convertDate(currentLevel.effectiveDate);
			currentLevel.expirationDate = convertDate(currentLevel.expirationDate);
			var hierarchyValues = {
				newEffectiveDate : convertDate(effectiveDate),
				newEndDate : convertDate(endDate),
				newDescription: description,
				newActiveSwitch: activeSwitch,
				currentLevel: currentLevel,
				parentEntityId: self.currentLevel.parentEntityId,
				hierarchyContext: customHierarchyService.getHierarchyContextSelected()

			};
			customHierarchyApi.addCustomHierarchy(hierarchyValues,
				function(results){
					customHierarchyService.setSuccessMessage(results.message, true);
					var tempSelectedArray = customHierarchyService.getHierarchyToSelectedLevel();
					tempSelectedArray.push(results.data);
					customHierarchyService.setHierarchyToSelectedLevelAndUpdate(tempSelectedArray);
				},
				fetchError)
		};

		/**
		 * Open the FreightConfirmed picker to select a new date.
		 */
		self.openEffectiveDatePicker = function () {
			self.effectiveDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Open the FreightConfirmed picker to select a new date.
		 */
		self.openEndDatePicker = function () {
			self.endDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};
		/**
		 * This method will attempt to remove a level from the hierarchy
		 */
		self.removeHierarchyLevel = function () {
			customHierarchyService.setWaitingForUpdate(true, true);
			customHierarchyApi.saveRemoveLevel(
				self.levelToBeRemoved,
				function(results){
					customHierarchyService.setSuccessMessage(results.message, true);
					customHierarchyService.updateSelectedHierarchy();
				},
				fetchError)
		};

		/**
		 * This method will update the level to remove and remove the previously selected level
		 * @param childRelationships the new level to remove
		 */
		self.setToRemove = function (childRelationships) {
			if(self.levelToBeRemoved !==null){
				self.levelToBeRemoved.removeSelected=false;
			}
			childRelationships.removeSelected = true;
			self.levelToBeRemoved = childRelationships;
		};

		/**
		 * This method will verify there a a level to remove
		 * @returns {boolean}
		 */
		self.isRemoveValid = function () {
			return self.levelToBeRemoved === null;
		};

		/**
		 * Call api to mass update add product groups.
		 */
		self.addCustomerProductGroups = function(){
			var addedProductGroupIds = [];
			angular.forEach(self.allCustomerProductGroups, function(customerProductGroup){
				if(customerProductGroup.isChecked){
					addedProductGroupIds.push(customerProductGroup.custProductGroupId);
				}
			});
			self.productIds = addedProductGroupIds;
			self.massUpdateType = ADD_HIERARCHY_PRODUCT_GROUP;
			self.totalUpdates = addedProductGroupIds.length;
		};

		/**
		 * Gets all customer product groups to be able to add them to a hierarchy relationship. This call sends the
		 * current hierarchy level information, so the currently attached product groups are not included in the
		 * return.
		 */
		self.getAllCustomerProductGroups = function(){
			self.isWaitingForAllProductGroups = true;
			self.allCustomerProductGroups = [];
			self.customerProductGroupsError = null;
			self.allCustomProductGroupsChecked = false;
			customHierarchyApi.findAllCustomerProductGroupsNotOnParentEntity(
				{
					hierarchyContext: self.currentLevel.key.hierarchyContext,
					parentEntityId: self.currentLevel.key.childEntityId
				},
				loadCustomerProductGroups,
				function(error){
					self.isWaitingForAllProductGroups = false;
					customHierarchyService.setErrorMessage(getError(error), true);
				}
			)
		};

		/**
		 * Callback for load customer product groups.
		 *
		 * @param results
		 */
		function loadCustomerProductGroups(results){
			self.allCustomerProductGroups = results;
			self.isWaitingForAllProductGroups = false;
		}

		/**
		 * Mass updates product groups by assigning the product search criteria and mass update parameters, then
		 * calling the api to mass update the selected body of product groups.
		 */
		self.massUpdateProductGroups = function(){
			customHierarchyService.setWaitingForUpdate(true, true);

			var productSearchCriteria = {
				lowestCustomerHierarchyNode: self.currentLevel.key,
				entityType: PRODUCT_GROUP_ENTITY_TYPE
			};
			var massUpdateParameters = {
				attribute: self.massUpdateType,
				stringValue: self.descriptionStringValue,
				description: self.description,
				entityRelationship: {
					key: self.currentLevel.key
				}
			};
			productSearchCriteria.productGroupIds = self.productIds.toString();
			massUpdateParameters.entityRelationship.actionCode = ACTION_CODE_ADDING;

			var massUpdateRequest = {
				parameters: massUpdateParameters,
				productSearchCriteria: productSearchCriteria
			};
			customHierarchyApi.massUpdate(
				massUpdateRequest,
				function(results){
					customHierarchyService.setSuccessMessage(results.message, true);
				},
				fetchError);
		};

		/**
		 * Set all product groups selected value to 'all selected' value.
		 */
		self.setAllCustomProductGroups = function(){
			angular.forEach(self.allCustomerProductGroups, function(customerProductGroup){
				customerProductGroup.isChecked = self.allCustomProductGroupsChecked;
			});
		};

		/**
		 * Callback for a successful start of a mass update job.
		 *
		 * @param data The data sent from the back end.
		 */
		self.massUpdateSuccess = function(data) {
			customHierarchyService.setSuccessMessage(data.message, true);
		};

		/**
		 * This method will tell the api to only delete a single layer of children after failing to remove a level from the hierarchy
		 */
		self.deleteSingleRemoveLevel = function () {
			customHierarchyService.setWaitingForUpdate(true, true);
			customHierarchyApi.deleteSingleRemoveLevel(self.levelToBeRemoved, self.successfulHierarchyChange, fetchError)
		};

		/**
		 * This method will tell the api to delete all children after failing to remove a level from the hierarchy
		 */
		self.deleteAllRemoveLevel = function () {
			customHierarchyService.setWaitingForUpdate(true, true);
			customHierarchyApi.deleteAllRemoveLevel(self.levelToBeRemoved, self.successfulHierarchyChange, fetchError)
		};

		/**
		 * Upon successful removing a level from the hierarchy this method will reset the view
		 * @param results
		 */
		self.successfulHierarchyChange = function(results){
			customHierarchyService.setSuccessMessage(results.message, true);
			customHierarchyService.updateSelectedHierarchy();
		};
	}
}());
