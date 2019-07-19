/*
 *   eCommerceViewDimensionsComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Product -> eCommerce View page component.
 *
 * @author vn73545
 * @since 2.0.14
 */
(function () {

	angular.module('productMaintenanceUiApp').component('eCommerceViewDimensions', {
		bindings: {
			currentTab: '<',
			productMaster:'<',
			success:'=',
			error:'='
		},
		require:{
			parent:'^^eCommerceView'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewDimensions.html',
		// The controller that handles our component logic
		controller: ECommerceViewDimensionsController
	});

	ECommerceViewDimensionsController.$inject = ['ECommerceViewApi', '$scope', '$timeout'];


	function ECommerceViewDimensionsController(eCommerceViewApi, $scope, $timeout) {

		var self = this;
		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Initialize the controller.
		 */
		this.$onInit = function () {
		};

		/**
		 * The list of data source provider information.
		 * @type {Array}
		 */
		self.eCommerceViewAttributePriority = {};

		/**
		 * The list of data source provider information back up
		 * @type {Array}
		 */
		self.eCommerceViewAttributePriorityOrg = {};

		/**
		 * The dimension logical attribute id.
		 *
		 * @type {number}
		 */
		self.DIMENSION_ATTRIBUTE_ID = 1784;

		/**
		 * Reload eCommerce view key.
		 * @type {string}
		 */
		self.RELOAD_ECOMMERCE_VIEW = 'reloadECommerceView';

		/**
		 * Reset eCommerce view.
		 * @type {string}
		 */
		self.RESET_ECOMMERCE_VIEW = 'resetECommerceView';

		/**
		 * Component will reload the kits data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
			if (self.currentTab && self.currentTab.id && self.currentTab.id !== 'Google') {
				self.validateChangedPublishedSource();
				self.getDimensionInformation();
			}
		};

		/**
		 * Check different between current value with default value.
		 */
		self.validateChangedPublishedSource = function () {
			self.differentWithDefaultValue = false;
			eCommerceViewApi.validateChangedPublishedSource(
					{
						productId: self.productMaster.prodId,
						logicAttributeCode: 1784
					},
					//success case
					function (result) {
						if(result != null){
							self.differentWithDefaultValue = result.data;
						}
					},self.fetchError);
		};

		/**
		 * Show edit source dimension popup.
		 */
		self.editSourceDimension = function () {
			self.editSourceDimensionsError = null;
			self.editSourceDimensionsSuccess = null;
			$('#editSourceDimensionsModal').modal({backdrop: 'static', keyboard: true});
			self.getAllDimensionsByAttributePriorities();
		};

		/**
		 * Reset edit source nutrition popup.
		 */
		self.resetPopup = function () {
			self.editSourceDimensionsError = null;
			self.editSourceDimensionsSuccess = null;
			self.selectedSourceSystemId = angular.copy(self.selectedSourceSystemIdOrg);
			angular.forEach(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails, function(item){
				if(self.selectedSourceSystemId === item.targetSystemAttributePriority.key.dataSourceSystemId){
					self.changeSourceSystemData(item, null);
				}
			});
		};

		/**
		 * Get all dimensions information by attribute priorities.
		 */
		self.getAllDimensionsByAttributePriorities = function () {
			self.isWaitingForDimensionsModal = true;
			self.rightDimensionList = null;
			var sourceSystem =  13;
			eCommerceViewApi.getAllDimensionsByAttributePriorities(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId,
					sourceSystem: sourceSystem
				},
				//success case
				function (results) {
					var sourcelist = angular.copy(results);
					self.isWaitingForDimensionsModal = false;
					var tempData = [];
					if(results != null && results.eCommerceViewAttributePriorityDetails != null && results.eCommerceViewAttributePriorityDetails.length > 0){//List<ECommerceViewAttributePriorityDetails>
						angular.forEach(results.eCommerceViewAttributePriorityDetails, function(varItem){
							if(varItem.sourceDescription === "13"){
								self.rightDimensionList = angular.copy(varItem.content.content);
							}else{
								tempData.push(varItem);
							}
						});
						sourcelist.eCommerceViewAttributePriorityDetails = tempData;
						self.eCommerceViewAttributePriority = angular.copy(sourcelist);
						self.eCommerceViewAttributePriorityOrg = angular.copy(sourcelist);

						if(sourcelist.eCommerceViewAttributePriorityDetails != null && sourcelist.eCommerceViewAttributePriorityDetails.length > 0){
							angular.forEach(sourcelist.eCommerceViewAttributePriorityDetails, function(item){
								if(item.sourceDefault === true){
									self.firstSourceSystemId = item.targetSystemAttributePriority.key.dataSourceSystemId;
								}
								if(item.selected === true){
									self.selectedSourceSystemId = item.targetSystemAttributePriority.key.dataSourceSystemId;
									self.changeSourceSystemData(item, null);
								}
							});
						}
						self.selectedSourceSystemIdOrg = angular.copy(self.selectedSourceSystemId);
					}

				},self.fetchError);
		};

		/**
		 * Get dimension data when change source system on nutrition popup.
		 */
		self.changeSourceSystemData = function (sourceSystem, index) {
			if(index != null){
				if(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails != null){
					for (var i =0; i<self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails.length; i++){
						if(i != index){
							if(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].selected){
								self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].selected = false;
							}
						}
						else{
							self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].selected = true;
						}
					}
				}
			}

			if(sourceSystem.content === null) {
				self.leftDimensionList = null;
			}
			else {
				self.leftDimensionList = sourceSystem.content.content;
			}
		};

		/**
		 * Handle update information data source
		 */
		self.saveDataAttributePriorities = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			if(angular.toJson(self.eCommerceViewAttributePriority) != angular.toJson(self.eCommerceViewAttributePriorityOrg)){
				$('#editSourceDimensionsModal').modal("hide");
				self.isWaitingForDimensionsModal = true;
				var eComViewAttributePriority = angular.copy(self.eCommerceViewAttributePriority);
				eComViewAttributePriority.productId = self.productMaster.prodId;
				eComViewAttributePriority.primaryScanCode = self.productMaster.productPrimaryScanCodeId,
				eComViewAttributePriority.attributeId = self.attributeId,
				//eComViewAttributePriority.salesChannel = self.currentTab.saleChannel.id,
				eCommerceViewApi.updateECommerceViewDataSource(
						eComViewAttributePriority,
						self.dataSourceUpdateResponseSuccess,
						self.fetchError);
			}else{
				self.editSourceDimensionsError = null;
				self.editSourceDimensionsSuccess = null;
				$('#editSourceDimensionsModal').modal("hide");
			}
		};


		/**
		 * Show value for radio button data source selected.
		 * @param selected
		 * @returns {*}
		 */
		self.valueSourceSelectedRadioButton = function (selected) {
			if(selected){
				return selected;
			}
			return !selected;
		};

		/**
		 * Response success. Store and handle show data source.
		 * @param result
		 */
		self.dataSourceUpdateResponseSuccess = function (result) {
			self.isWaitingForDimensionsModal = false;
			if (result && result.message) {
				self.success = "Dimension: "+ result.message;
			}
			self.getDimensionInformation();
		};

		/**
		 * Get dimension information.
		 */
		self.getDimensionInformation = function () {
			self.isWaitingForResponse = true;
			self.dimensionList = null;
			eCommerceViewApi.getDimensionInformation(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId
				},
				//success case
				function (results) {
					self.isWaitingForResponse = false;
					if(results != null && results.length > 0){
						self.dimensionList = results;
					}
				},self.fetchError);
		};

		self.findAttributeMappingByLogicalAttribute = function(){
			self.parent.findAttributeMappingByLogicalAttribute(self.DIMENSION_ATTRIBUTE_ID);
		};

		self.showEditText = function(){
			self.editableDimension=true;
		};

		/**
		 * Reload ECommerceView.
		 */
		$scope.$on(self.RELOAD_ECOMMERCE_VIEW, function() {
			self.$onChanges();
		});

		/**
		 * Reset eCommerce view.
		 */
		$scope.$on(self.RESET_ECOMMERCE_VIEW, function() {
			self.editableDimension = false;
		});

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.isWaitingForDimensionsModal = false;
			self.editSourceDimensionsError = null;
			if(error && error.data) {
				if (error.data.message) {
					self.editSourceDimensionsError = error.data.message;
				} else {
					self.editSourceDimensionsError = error.data.error;
				}
			}else {
				self.editSourceDimensionsError = "An unknown error occurred.";
			}
		};
	}
})();
