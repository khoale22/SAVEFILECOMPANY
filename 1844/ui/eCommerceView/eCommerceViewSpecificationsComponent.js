/*
 *   eCommerceViewSpecificationsComponent.js
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

	angular.module('productMaintenanceUiApp').component('eCommerceViewSpecifications', {
		bindings: {
			currentTab: '<',
			productMaster:'<',
			eCommerceViewDetails: '='
		},
		require:{
			parent:'^^eCommerceView'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewSpecifications.html',
		// The controller that handles our component logic
		controller: ECommerceViewSpecificationsController
	});

	ECommerceViewSpecificationsController.$inject = ['ECommerceViewApi', '$scope', '$timeout', '$sce'];

	function ECommerceViewSpecificationsController(eCommerceViewApi, $scope, $timeout, $sce) {

		var self = this;
		/**
		 * Reload eCommerce view key.
		 * @type {string}
		 */
		self.RELOAD_ECOMMERCE_VIEW = 'reloadECommerceView';
		/**
		 * Max length of textarea input.
		 *
		 * @type {number}
		 */
		self.MAX_LENGTH = 4000;
		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;
		/**
		 * Define trust as HTML copy from $sce to use in ui.
		 */
		self.trustAsHtml = $sce.trustAsHtml;

		self.selectedSource = null;
		/**
		 * Initialize the controller.
		 */
		this.$onInit = function () {
		};

		/**
		 * Component will reload the kits data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
			self.validateChangedPublishedSource();
			self.getSpecificationInformation();
		};
		/**
		 * Destroy component.
		 */
		this.$onDestroy = function () {
			 self.parent = null;
		};
		/**
		 * Check different between current value with default value.
		 */
		self.validateChangedPublishedSource = function () {
			self.differentWithDefaultValue = false;
			eCommerceViewApi.validateChangedPublishedSource(
					{
						productId: self.productMaster.prodId,
						logicAttributeCode: 1728
					},
					//success case
					function (result) {
						if(result != null){
							self.differentWithDefaultValue = result.data;
						}
					},self.fetchError);
		};
		/**
		 * Get specification information.
		 */
		self.getSpecificationInformation = function () {
			self.isWaitingForResponse = true;
			self.specificationList = null;
			eCommerceViewApi.getSpecificationInformation(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId
				},
				//success case
				function (results) {
					self.isWaitingForResponse = false;
					if(results != null && results.length > 0){
						self.specificationList = results;
					}
				},self.fetchError);
		};
		/**
		 * Show Edit source modal.
		 */
		self.showEditSourcePopup = function(){
			self.isWaitingForResponsePopup = true;
			$('#dataSourceModalTypeForSpecification').modal({backdrop: 'static', keyboard: true});
			eCommerceViewApi.findAllSpecificationAttributePriorities({
					productId: self.productMaster.prodId,
					scanCodeId: self.productMaster.productPrimaryScanCodeId
				},
				self.dataSourceResponseSuccess,
				self.fetchErrorPopup
			);
		}
		/**
		 * Response success. Store and handle show data source.
		 * @param result
		 */
		self.dataSourceResponseSuccess = function (result) {
			self.isWaitingForResponsePopup = false;
			self.eCommerceViewAttributePriority = angular.copy(result);
			self.eCommerceViewAttributePriorityOrg = angular.copy(result);
			self.showSpecificationsOnPopup();
		}
		/**
		 * Show the list of specifications by selected source.
		 */
		self.showSpecificationsOnPopup = function(){
			if(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails != null) {
				for (var i = 0; i < self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails.length; i++) {
					if (self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].selected) {
						self.specificationListPopup = angular.copy(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].content.content);
						self.selectedSource = self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i];
						break;
					}
				}
			}
		}
		/**
		 * Get nutrition data when change source system on nutrition popup.
		 */
		self.changeSourceSystemData = function (index) {
			self.specificationListPopup=[];
			if(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails != null){
				for (var i =0; i<self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails.length; i++){
					if(i != index){
						self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].selected = false;
					}else{
						self.specificationListPopup = angular.copy(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].content.content);
						self.selectedSource = self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i];
					}
				}
			}
		};
		/**
		 * Handle update information data source
		 */
		self.saveDataAttributePriorities = function () {
			self.errorPopup = '';
			self.parent.error = '';
			self.parent.success = '';
			if(angular.toJson(self.eCommerceViewAttributePriority) != angular.toJson(self.eCommerceViewAttributePriorityOrg)){
				self.isWaitingForResponsePopup = true;
				var param = angular.copy(self.eCommerceViewAttributePriority);
				param.productId = self.productMaster.prodId;
				param.attributeId = 1728;
				param.primaryScanCode=self.productMaster.productPrimaryScanCodeId;
				eCommerceViewApi.updateECommerceViewDataSource(
					param,
					self.dataSourceUpdateResponseSuccess,
					self.fetchErrorPopup
				);
			}
			$('#dataSourceModalTypeForSpecification').modal("hide");
		}
		/**
		 * Reset edit source modal.
		 */
		self.resetEditSourcePopup = function(){
			self.eCommerceViewAttributePriority = angular.copy(self.eCommerceViewAttributePriorityOrg);
			self.showSpecificationsOnPopup();
		}
		/**
		 * Response success. Store and handle show data source.
		 * @param result
		 */
		self.dataSourceUpdateResponseSuccess = function (result) {
			self.isWaitingForResponsePopup = false;
			self.parent.success = "Specification: " + result.message;
			self.$onChanges();
		}
		self.findAttributeMappingByLogicalAttribute = function(){
			self.parent.findAttributeMappingByLogicalAttribute(1728);
		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchErrorPopup= function (error) {
			self.isWaitingForResponsePopup = false;
			if (error && error.data) {
				if (error.data.message) {
					self.errorPopup = (error.data.message);
				} else if (error.data.error) {
					self.errorPopup = (error.data.error);
				} else {
					self.errorPopup = error;
				}
			}
			else {
				self.errorPopup = "An unknown error occurred.";
			}
		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
		};
		/**
		 * Show value for radio button data source selected.
		 * @param selected
		 * @returns {*}
		 */
		self.valueSourceSelectedRadioButton = function (item) {
			if(item.selected){
				return item.selected;
			}

			return !item.selected;
		}
		/**
		 * Reload ECommerceView.
		 */
		$scope.$on(self.RELOAD_ECOMMERCE_VIEW, function() {
			self.$onChanges();
		});
	}
})();
