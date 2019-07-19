/*
 *   addMoveComponent.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Custom Hierarchy -> product panel component -> Add or Move component.
 *
 * @author l730832
 * @since 2.17.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('addMoveComponent', {
		// isolated scope binding
		scope: {},
		bindings: {
			callback: '&',
			currentLevel: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/customHierarchy/addMoveProductPanelComponents/addMoveProducts.html',
		// The controller that handles our component logic
		controller: addMoveComponent
	});


	addMoveComponent.$inject = ['customHierarchyService', 'CustomHierarchyApi', '$scope'];
	/**
	 * addMovePanel component definition.
	 * @constructor
	 * @param customHierarchyService
	 * @param customHierarchyApi
	 */
	function addMoveComponent(customHierarchyService, customHierarchyApi, $scope) {
		var self = this;
		self.actionCode = null;

		self.addingActionCode = 'Y';

		// Mass update to add a product.
		self.ADD_HIERARCHY_PRODUCT = "ADD_HIERARCHY_PRODUCT";

		self.MOVE_HIERARCHY_PRODUCT = "MOVE_HIERARCHY_PRODUCT";

		self.descriptionBooleanValue = "true";
		self.descriptionStringValue = "";
		self.description = null;
		self.transactionMessage = null;
		self.onAdding = false;
		self.onMoving = false;

		/**
		 * Every time a level gets changed.
		 */
		self.$onChanges = function () {
			var self = this;
			self.actionCode = null;
			self.onAdding = false;
			self.onMoving = false;
			self.selectedCriteria = null;
			self.massUpdateType = null;
			self.transactionMessage = null;
		};

		/**
		 * This adds products to the bottom level of a hierarchy.
		 */
		self.alterProducts = function() {
			self.fillActionCode();
			self.fillEntityRelationship();
			var massUpdateRequest = self.fillBatchUpdate();
			customHierarchyApi.massUpdate(massUpdateRequest, self.massUpdateSuccess, self.fetchError);
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
		 * Callback for a successful start of a mass update job.
		 *
		 * @param data The data sent from the back end.
		 */
		self.massUpdateSuccess = function(data) {
			self.callback({data: data});
			self.transactionMessage = data.message;
			self.submittedMassUpdate = false;
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
		 * Sets the controller's error message.
		 * @param error The error message.
		 */
		self.setError = function (error) {
			self.error = error;
		};

		/**
		 * This clears the product list to add.
		 */
		self.clearProductList = function() {
			self.searchCriteria = null;
			self.selectedCriteria = null;
			self.onAdding = false;
			self.onMoving = false;
			self.actionCode = null;
			self.massUpdateType = null;
			self.entityRelationship = null;

			self.clearDescription();
		};

		/**
		 * This determines whether or not it is on the adding modal.
		 * @param bool
		 */
		self.setOnAdding = function(bool) {
			self.onAdding = bool;
			$scope.$broadcast('resetBYOSCriteria');
		};

		/**
		 * Fills the entity relationship that will be placed into the ps_enty_rlshp.
		 */
		self.fillEntityRelationship = function() {
			self.entityRelationship = {
				key: {
					parentEntityId: customHierarchyService.getHierarchyContextSelected().parentEntityId,
					childEntityId: self.currentLevel.key.childEntityId,
					hierarchyContext: self.currentLevel.key.hierarchyContext
				},
				actionCode: self.actionCode
			};
		};

		/**
		 * This removes all of the selected products that are checked.
		 */
		self.fillBatchUpdate = function () {
			var massUpdateParameters = {
				attribute: self.massUpdateType,
				booleanValue: self.descriptionBooleanValue,
				stringValue: self.descriptionStringValue,
				description: self.description,
				rootId: customHierarchyService.getHierarchyContextSelected().parentEntityId,
				entityRelationship: self.entityRelationship
			};

			/**
			 * the search criteria for the request
			 */
			var productSearchCriteria = self.selectedCriteria;

			/**
			 * The mass update request.
			 * @type {{parameters: {attribute: null|*, booleanValue: string, stringValue: string, description: null, rootId: number}, productSearchCriteria: {productIds: string}}}
			 */
			var massUpdateRequest = {
				parameters: massUpdateParameters,
				productSearchCriteria: productSearchCriteria
			};

			self.transactionMessage = null;
			self.submittedMassUpdate = true;
			return massUpdateRequest;
		};

		/**
		 * This will clear the description value.
		 */
		self.clearDescription = function() {
			self.description = null;
		};

		/**
		 * This determines whether or not it is on the moving modal.
		 * @param bool
		 */
		self.setOnMoving = function (bool) {
			$scope.$broadcast('resetBYOSCriteria');
			self.onMoving = bool;
		};

		/**
		 * Fills necessary information according to the type of mass update. Add or Move.
		 */
		self.fillActionCode = function() {
			if(self.onAdding) {
				self.actionCode = self.addingActionCode;
				self.massUpdateType = self.ADD_HIERARCHY_PRODUCT;
			} else if(self.onMoving) {
				self.actionCode = null;
				self.massUpdateType = self.MOVE_HIERARCHY_PRODUCT
			}
		};

		/** Resets search criteria and selected products to empty values.
		 */
		self.resetSearchCriteriaAndSelectedProducts = function() {
			self.searchCriteria = null;
			self.selectedProducts = [];
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
		self.isSubmitMassUpdateDisabled = function(){
			if(self.hasRecordsToUpdate()){
				if(self.isMassUpdateDescriptionValid()){
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
		self.isMassUpdateDescriptionValid = function(){
			return self.description && self.description.length > 0;
		};
	}
}());
