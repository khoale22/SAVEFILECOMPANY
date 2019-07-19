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
 * Custom Hierarchy -> All Parent Info page component.
 *
 * @author l730832
 * @since 2.16.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('allParentPanel', {
		// isolated scope binding
		bindings: {
			currentLevel: '<',
			changePath: '&'
		},
		scope: {},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/customHierarchy/allParentPanel.html',
		// The controller that handles our component logic
		controller: allParentPanelController
	});


	allParentPanelController.$inject = ['customHierarchyService', 'CustomHierarchyApi', 'PermissionsService'];

	/**
	 * Case Pack Import component's controller definition.
	 * @constructor
	 * @param customHierarchyService
	 * @param customHierarchyApi
	 * @param permissionsService
	 */
	function allParentPanelController(customHierarchyService, customHierarchyApi, permissionsService) {
		var self = this;

		self.pathList = null;

		// Mass update for unassign products.
		self.massUpdateType = "UNASSIGN_PRODUCTS";

		self.unassignProductsDescriptionBooleanValue = "true";
		self.unassignProductsDescriptionStringValue = "";
		self.unassignProductsDescription = null;
		self.selectedProducts = [];
		self.hasPathChangeCompleted = false;
		self.changingContents = false;
		var pathArray;

		/**
		 * Search criteria used during Add Products to Task.
		 */
		self.searchCriteria = "";


		self.$onChanges = function () {
			self.changingContents = true;
			if(typeof self.currentLevel['key'] !== 'undefined') {
				self.selectedProducts = [];
				customHierarchyApi.findAllParentsByChild(
					{
						hierarchyContext: self.currentLevel.key.hierarchyContext,
						childId: self.currentLevel.key.childEntityId
					},
					self.loadPath,
					fetchError)
			}
		};

		function fetchError(error){
			customHierarchyService.setErrorMessage(error, true);
			self.changingContents = false;
		}

		/**
		 * Clears out the modal.
		 */
		self.clearModal = function() {
			self.searchCriteria = null;
			self.unassignProductsDescription = null;
			self.selectedProducts = [];
			self.selectedCriteria = null;
		};

		/**
		 * Loads the path list for each path.
		 * @param results
		 */
		self.loadPath = function (results) {
			self.pathList = results;
			self.changingContents = false;
		};

		/**
		 * Determines whether or not the all parent is minimized.
		 */
		self.allParentMinimize = function () {
			self.allParentVisible = !self.allParentVisible;
		};

		/**
		 * called by product-search-criteria when search button pressed
		 * @param searchCriteria
		 */
		self.updateSearchCriteria = function(searchCriteria) {
			self.searchCriteria = searchCriteria;
			self.selectedCriteria = null;
		};

		/**
		 * called by product-search-selection when select button pressed
		 * @param selectedCriteria
		 */
		self.updateSelectedCriteria = function(selectedCriteria) {
			self.selectedCriteria = selectedCriteria;
		};

		/**
		 * This unassigns all of the selected products that are checked.
		 */
		self.unassignProducts = function() {
			var massUpdateParameters = {
				attribute: self.massUpdateType,
				booleanValue: self.unassignProductsDescriptionBooleanValue,
				stringValue: self.unassignProductsDescriptionStringValue,
				description: self.unassignProductsDescription,
				rootId: customHierarchyService.getSelectedHierarchyContextRoot().parentEntityId,
				hierarchyContextId: self.currentLevel.key.hierarchyContext
			};

			var productSearchCriteria = self.selectedCriteria;

			var massUpdateRequest = {
				parameters: massUpdateParameters,
				productSearchCriteria: productSearchCriteria
			};
			customHierarchyApi.massUpdate(massUpdateRequest, self.unassignProductsSuccess, self.fetchError);
		};

		/**
		 * This clears the selected product list whenever the modal is closed.
		 */
		self.clearProductList = function() {
			self.selectedProducts = [];
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
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
		};

		/**
		 * Callback for a successful start of a mass update job.
		 *
		 * @param data The data sent from the back end.
		 */
		self.unassignProductsSuccess = function(data) {
			customHierarchyService.setSuccessMessage(data.message, true);
		};

		/**
		 * Sets the controller's error message.
		 * @param error The error message.
		 */
		self.setError = function (error) {
			customHierarchyService.setErrorMessage(error, true);
		};

		/**
		 * Whenever a path is clicked on, this will change the location of the current level.
		 * @param pathToSelectedLevel
		 */
		self.changeToThePathSpecified = function(pathToSelectedLevel) {
			customHierarchyService.setHierarchyToSelectedLevel(pathToSelectedLevel, false);
		};

		/**
		 * This creates a path array for the hierarchy level, to keep track of how to get back to this location. It will
		 * include all parent nodes, as well as the current level.
		 *
		 * @param previousPath Path array containing all parent nodes.
		 * @param currentLevel The current level getting its path array.
		 * @returns {array} Path array to get back to the current level.
		 */
		self.getPathToCurrentLevel = function(previousPath, currentLevel){
			var pathArray;
			// if this is the top level in the hierarchy, the previous path will be undefined; initialize as []
			if(!previousPath){
				pathArray = [];
			}
			// else set the path array to a copy of the previous path; if a copy was not done, children nodes would
			// sometimes get additional nodes that were not part of the path
			else {
				pathArray = angular.copy(previousPath);
			}
			// push the current level onto the path array
			pathArray.push(currentLevel);
			return pathArray;
		};

		/**
		 * Returns whether there are records to update.
		 *
		 * @returns {boolean}
		 */
		self.hasRecordsToUpdate = function(){
			return self.selectedCriteria && self.selectedCriteria.totalRecordCount > 0;
		};

		/**
		 * Returns whether a user can submit a mass update or not. There are two requirements for this:
		 * 1. At least 1 record to update.
		 * 2. The mass update description needs to be at least 1 character long.
		 *
		 * If both of these are true, return false (not disabled). Else return true (disabled).
		 *
		 * @returns {boolean} True if mass update is disabled, false otherwise.
		 */
		self.isUnAssignProductsDisabled = function(){
			if(self.hasRecordsToUpdate()){
				if(self.isUnAssignProductDescriptionValid()){
					return false;
				}
			}
			return true;
		};

		/**
		 * Returns if the description used for a mass update is valid. Return true if the description is not null and
		 * has a length greater than 0. Else return false.
		 *
		 * @returns {boolean}
		 */
		self.isUnAssignProductDescriptionValid = function(){
			return self.unassignProductsDescription && self.unassignProductsDescription.length > 0;
		};
	}
}());
