/*
 *   taxonomyComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * taxonomy -> Taxonomy Info page component.
 *
 * @author vn87351
 * @since 2.15.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('taxonomy', {
		// isolated scope binding
		require: {
			productDetail: '^productDetail'
		},
		bindings: {
			productMaster: '<'
		},
		// Inline template which is bound to message variable in the component controller
		templateUrl: 'src/productDetails/product/taxonomy/taxonomy.html',
		// The controller that handles our component logic
		controller: TaxonomyController
	});

	TaxonomyController.$inject = ['ProductTaxonomyApi', 'taxonomyService', '$rootScope'];

	function TaxonomyController(productTaxonomyApi, taxonomyService, $rootScope) {

		var self = this;

		var hasProductChange = false;
		var hasCasePackChange = false;
		var hasUPCChange = false;

		self.loadingLevels = false;

		var UNKNOWN_ERROR_TEXT = "An unknown error occurred.";

		self.matHierarchy = {
			displayName: "Master Attribute Taxonomy",
			hierCntxtCd: "MAT"};

		self.hierarchyPathLimit = 1;

		self.$onChanges = function(){
			self.getLevels();
		};

		/**
		 * Gets all paths to the selected product.
		 */
		self.getLevels = function(){
			self.loadingLevels = true;
			var parameters = {};
			parameters.productId = self.productMaster.prodId;
			productTaxonomyApi.findAll(
				parameters,
				function(results){
					self.levels = results;
					self.loadingLevels = false;
				},
				function(error){
					fetchError('Error getting taxonomy levels', error);
					self.loadingLevels = false;
				}
			)
		}

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param message The pretext message to display to user.
		 * @param error The error from the back end.
		 */
		function fetchError (message, error) {
			self.success = null;
			self.waitingForUpdate = false;
			self.error = message + ':' + getErrorMessage(error);
		}

		/**
		 * Returns error message.
		 *
		 * @param error Error object.
		 * @returns {string} String to set as error text.
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
				return UNKNOWN_ERROR_TEXT;
			}
		}

		/**
		 * Helper function to determine if the current level has hierarchy level children.
		 *
		 * @param currentLevel Hierarchy level to compare.
		 * @returns {boolean}
		 */
		self.hasHierarchyLevelChildren = function(currentLevel) {
			return currentLevel.childRelationships != null && currentLevel.childRelationships[0].childRelationships != null
		};

		/**
		 * Callback for when the product attributes are modified.
		 *
		 * @param change Whether or not there are changes within product attributes.
		 */
		self.hasProductChanges = function(change){
			hasProductChange = change;
		};

		/**
		 * Callback for when the case pack attributes are modified.
		 *
		 * @param change Whether or not there are changes within case pack attributes.
		 */
		self.hasCasePackChanges = function(change){
			hasCasePackChange = change;
		};

		/**
		 * Callback for when the UPC attributes are modified.
		 *
		 * @param change Whether or not there are changes within UPC attributes.
		 */
		self.hasUPCChanges = function(change){
			hasUPCChange = change;
		};

		/**
		 * Whether or not this component has a change or not.
		 *
		 * @returns {boolean} True if change detected, false otherwise.
		 */
		self.hasChanges = function(){
			var hasChanges = hasProductChange || hasCasePackChange || hasUPCChange;
			$rootScope.contentChangedFlag = hasChanges;
			return hasChanges;
		};

		/**
		 * Issue call to save to children components.
		 */
		self.saveChanges = function(){
			taxonomyService.save();
		};

		/**
		 * Issue call to reset to children components.
		 */
		self.reset = function(){
			taxonomyService.reset();
		};
	}

})();
