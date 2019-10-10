/*
 *   eCommerceViewNutritionFactsComponent.js
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

	angular.module('productMaintenanceUiApp').component('eCommerceViewNutritionFacts', {
		bindings: {
			currentTab: '<',
			productMaster:'<',
			eCommerceViewDetails: '=',
			success:'=',
			error:'='
		},
		require:{
			parent:'^^eCommerceView'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewNutritionFacts.html',
		// The controller that handles our component logic
		controller: ECommerceViewNutritionFactsController
	});

	ECommerceViewNutritionFactsController.$inject = ['ECommerceViewApi', '$scope', '$timeout','$rootScope'];


	function ECommerceViewNutritionFactsController(eCommerceViewApi, $scope, $timeout,$rootScope) {

		var self = this;
		/**
		 * Reload eCommerce view key.
		 * @type {string}
		 */
		self.RELOAD_ECOMMERCE_VIEW = 'reloadECommerceView';

		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;
		self.isWaitingForNutritionFactsModal = false;
		self.panelTypeList = [];

		/**
		 * The nutrition logical attribute id.
		 *
		 * @type {number}
		 */
		self.NUTRITION_ATTRIBUTE_ID = 1679;
        /**
		 * Holds the source system id of eCommerce.
         * @type {number}
         */
		const SOURCE_SYSTEM_ID = 13;
		/**
		 * Initialize the controller.
		 */
		this.$onInit = function () {
		};

		/**
		 * Component will reload the kits data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
			self.getData();
			self.findAllProductPanelType();
		};

		/**
		 * Get data of nutrition fact.
		 */
		self.getData = function () {
			self.eCommerceViewDetails.panelType = null;
			self.eCommerceViewDetails.panelTypeOrg = null;
			self.editSourceNutritionFactsError = null;
			self.editSourceNutritionFactsSuccess = null;
			self.getNutritionFactInformation();
		};

		/**
		 * Get nutrition fact information.
		 */
		self.getNutritionFactInformation = function () {
			self.isWaitingForResponse = true;
			self.differentWithDefaultValue = false;
			eCommerceViewApi.getNutritionFactInformation(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId,
					sourceSystem: SOURCE_SYSTEM_ID
				},
				//success case
				function (results) {
					self.isWaitingForResponse = false;
					if(results != null && results.length > 0){//List<ECommerceViewAttributePriorities>
						self.nutritionList = self.getNutritionList(results[0]);
						self.differentWithDefaultValue = results[0].differentWithDefaultValue;
					}
				},self.fetchError);
		};

		/**
		 * Get all nutrition facts information by attribute priorities.
		 */
		self.getAllNutritionFactsByAttributePriorities = function () {
			self.isWaitingForNutritionFactsModal = true;
			eCommerceViewApi.getAllNutritionFactsByAttributePriorities(
					{
						productId: self.productMaster.prodId,
						upc: self.productMaster.productPrimaryScanCodeId,
						sourceSystem: SOURCE_SYSTEM_ID
					},
					//success case
					function (results) {
						self.isWaitingForNutritionFactsModal = false;
						if(results != null && results.length > 0){//List<ECommerceViewAttributePriorities>
							if(results[0].sourceSystemId === SOURCE_SYSTEM_ID){
								self.rightNutritionItem = angular.copy(results[0]);
								self.rightNutritionList = self.getNutritionList(results[0]);
								self.postedDate = results[0].postedDate;
							}else{
								self.rightNutritionItem = null;
								self.rightNutritionList = null;
								self.postedDate = null;
							}
							var sourcelist = [];
							angular.forEach(results, function(item){
								if(item.sourceSystemId !== SOURCE_SYSTEM_ID){
									sourcelist.push(item);
									if(item.sourceSystemDefault === true){
										self.firstSourceSystemId = item.sourceSystemId;
									}
									if(item.selectedSourceSystem === true){
										self.selectedSourceSystemId = item.sourceSystemId;
										self.changeSourceSystemData(item);
									}
								}
							});
							self.selectedSourceSystemIdOrg = angular.copy(self.selectedSourceSystemId);
							self.updateSourceSystemList = angular.copy(sourcelist);
							self.sourceSystemList = angular.copy(sourcelist);
						}
					},self.fetchError);
		};

		/**
		 * Get nutrition data when change source system on nutrition popup.
		 */
		self.changeSourceSystemData = function (sourceSystem) {
			self.leftNutritionList = self.getNutritionList(sourceSystem);
			self.createdDate = sourceSystem.createdDate;
		};

		/**
		 * Get list of nutrition data to display.
		 */
		self.getNutritionList = function (result) {
			var returnList = null;
			self.currentSourceSystemId = result.sourceSystemId;
			if(result.sourceSystemId === 17){
				returnList = self.getNutritionFactByScaleSource(result);
			}else{
				var productNutrients = angular.copy(result.productNutrients);
				var productPkVariations = angular.copy(result.productPkVariations);
				var nutritionList = [];
				if(productPkVariations != null && productPkVariations.length > 0){
					angular.forEach(productPkVariations, function(productPkVariation){
						if(self.eCommerceViewDetails.panelType === null || self.eCommerceViewDetails.panelType === undefined){
							angular.forEach(self.panelTypeList, function(item){
								if(item.panelTypeCode === productPkVariation.panelTypeCode){
									self.eCommerceViewDetails.panelType = item;
									self.eCommerceViewDetails.panelTypeOrg = angular.copy(self.eCommerceViewDetails.panelType);
								}
							});
						}
						var nutritionItem = {};
						nutritionItem.isSource17 = false;
						nutritionItem.nutritionFactList = [];
						nutritionItem.servingSize = productPkVariation.houseHoldMeasurement;
						nutritionItem.servingPerContainer = productPkVariation.servingsPerContainerText;
						nutritionItem.amountPerServing = productPkVariation.productValueDescription;

						var nutritionFactList = [];
						angular.forEach(productNutrients, function(productNutrient){
							if(productNutrient.key.upc === productPkVariation.key.upc
									&& productNutrient.key.sourceSystem === productPkVariation.key.sourceSystem
									&& productNutrient.key.valPreprdTypCd === productPkVariation.key.sequence){
								nutritionFactList.push(productNutrient);
								if(productNutrient.dclrOnLblSw !== "1"
									&& productNutrient.key.masterId !== 20567
									&& productNutrient.key.masterId !== 27187
									&& productNutrient.key.masterId !== 20592){
									nutritionItem.nutritionFactList.push(productNutrient);
								}
							}
						});

						var isOrContains = "";
						var checkServingSize = false;
						var checkServingPerContainer = false;
						angular.forEach(nutritionFactList, function(item){
							if(item.dclrOnLblSw === "1" && item.nutrientMaster.nutrientName !== ""){
								if(isOrContains === ""){
									isOrContains = item.nutrientMaster.nutrientName;
								}else{
									isOrContains = isOrContains + "; " + item.nutrientMaster.nutrientName;
								}
							}
							if(!checkServingSize && (item.key.masterId === 20567 || item.key.masterId === 27187)){
								checkServingSize = true;
								nutritionItem.servingSize = nutritionItem.servingSize + " ( " + item.nutrientQuantity.toFixed(2) + " " + item.servingSizeUOM.servingSizeUomDescription + " )";
							}
							if(!checkServingPerContainer && item.key.masterId === 20592){
								checkServingPerContainer = true;
								nutritionItem.servingPerContainer = nutritionItem.servingPerContainer + " ( " + item.nutrientQuantity.toFixed(2) + " " + item.servingSizeUOM.servingSizeUomDescription + " )";
							}
						});
						nutritionItem.isOrContains = isOrContains;
						nutritionList.push(nutritionItem);
					});
				}else{
					if(productNutrients != null && productNutrients.length > 0){
						var nutritionItem = {};
						nutritionItem.isSource17 = false;
						nutritionItem.nutritionFactList = angular.copy(productNutrients);
						nutritionList.push(nutritionItem);
					}
				}
				returnList = angular.copy(nutritionList);
			}
			return returnList;
		};

		/**
		 * Get nutrition data of scale source.
		 */
		self.getNutritionFactByScaleSource = function (result) {
			var scaleUpc = angular.copy(result.scaleUpc);
			var nutrientStatementDetails = angular.copy(scaleUpc.nutrientStatementDetails);
			var nutritionList = [];
			var nutritionItem = {};
			nutritionItem.isSource17 = true;
			nutritionItem.nutritionFactList = [];
			nutritionItem.servingSize = scaleUpc.nutrientStatementHeader.measureQuantity + " "
										+ scaleUpc.nutrientStatementHeader.nutrientCommonUom.nutrientUomDescription + " ( "
										+ scaleUpc.nutrientStatementHeader.metricQuantity.toFixed(2) + " "
										+ scaleUpc.nutrientStatementHeader.nutrientMetricUom.nutrientUomDescription + " )";
			nutritionItem.servingPerContainer = scaleUpc.nutrientStatementHeader.servingsPerContainer;
			nutritionItem.nutritionFactList = nutrientStatementDetails;
			nutritionList.push(nutritionItem);
			return nutritionList;
		};

		/**
		 * Returns a list of Panel types.
		 */
		self.findAllProductPanelType = function () {
			eCommerceViewApi.findAllProductPanelType(
					//success
					function (results) {
						if(results != null && results.length > 0){
							self.panelTypeList = results;
						}
					},self.fetchError);
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.isWaitingForNutritionFactsModal = false;
			self.error = self.getErrorMessage(error);
		};

		/**
		 * Show edit source nutrition popup.
		 */
		self.editSource = function () {
			self.editSourceNutritionFactsError = null;
			self.editSourceNutritionFactsSuccess = null;
			$('#editSourceNutritionFactsModal').modal({backdrop: 'static', keyboard: true});
			self.getAllNutritionFactsByAttributePriorities();
		};

		/**
		 * Close edit source nutrition popup.
		 */
		self.closePopup = function () {
			self.editSourceNutritionFactsError = null;
			self.editSourceNutritionFactsSuccess = null;
			$('#editSourceNutritionFactsModal').modal('hide');
		};

		/**
		 * Reset edit source nutrition popup.
		 */
		self.resetPopup = function () {
			self.editSourceNutritionFactsError = null;
			self.editSourceNutritionFactsSuccess = null;
			self.selectedSourceSystemId = angular.copy(self.selectedSourceSystemIdOrg);
			angular.forEach(self.sourceSystemList, function(item){
				if(self.selectedSourceSystemId === item.sourceSystemId){
					self.changeSourceSystemData(item);
				}
			});
		};

		self.findAttributeMappingByLogicalAttribute = function(){
			self.parent.findAttributeMappingByLogicalAttribute(self.NUTRITION_ATTRIBUTE_ID);
		}


		/**
		 * Get quantity value.
		 */
		$scope.getQuantityValue = function(value){
			var message = "";
			if(value < 0){
				message = "< " + Math.abs(value).toFixed(1);
			}else{
				message = value.toFixed(1);
			}
			return message;
		};

		/**
		 * Get panel type data.
		 */
		$scope.getPanelTypeData = function(){
			var message = "";
			self.panelTypeDisabled = false;
			if(self.currentSourceSystemId === 17){//scale source
				message = "Nutrition Panel";
				self.panelTypeDisabled = true;
			}else{
				if(self.eCommerceViewDetails != null && self.eCommerceViewDetails.panelType != null){
					message = self.eCommerceViewDetails.panelType.panelDescription;
				}
				if(self.nutritionList == null || self.nutritionList.length == 0){
					self.panelTypeDisabled = true;
				}
			}
			return message;
		};

		/**
		 * Update nutrition fact information.
		 */
		self.updateNutritionFactInformation = function () {
			self.error = '';
			self.success = '';
			if(self.selectedSourceSystemId != self.selectedSourceSystemIdOrg){
				$('#editSourceNutritionFactsModal').modal('hide');
				self.isWaitingForNutritionFactsModal = true;
				self.isWaitingForResponse = true;

				var tempList = angular.copy(self.updateSourceSystemList);
				var tempItem = null;
				var updateSourceSystemList = [];
				angular.forEach(tempList, function(item){
					if(self.selectedSourceSystemId == item.sourceSystemId){
						if(!item.sourceSystemDefault){
							item.actionCode = null;
							tempItem = angular.copy(item);
						}
					}else{
						item.actionCode = "D";
						updateSourceSystemList.push(item);
					}
				});
				//add source 13
				var rightNutritionItem = angular.copy(self.rightNutritionItem);
				if(rightNutritionItem !== null){
					rightNutritionItem.actionCode = "D";
					rightNutritionItem.relationshipGroupTypeCode = null;
					rightNutritionItem.physicalAttributeId = 1679;//source 13
					updateSourceSystemList.push(rightNutritionItem);
				}
				if(tempItem !== null){
					updateSourceSystemList.push(tempItem);
				}

				var eCommerceView = {};
				eCommerceView.nutrientList = updateSourceSystemList;
				eCommerceView.productId = self.productMaster.prodId;

				eCommerceViewApi.updateNutritionFactInformation(eCommerceView,
						//success case
						function (result) {
					self.isWaitingForNutritionFactsModal = false;
					self.isWaitingForResponse = false;
					if(result && result.message) {
						//self.editSourceNutritionFactsSuccess = result.message;

						self.success ="Nutrition fact: " + result.message;
					}
					self.getData();


				},self.errorPopup);
			}else{
				self.closePopup();
			}
		};

		/**
		 * Fetches the error from the back end.
		 * @param error
		 */
		self.errorPopup = function (error) {
			self.isWaitingForNutritionFactsModal = false;
			self.editSourceNutritionFactsSuccess = null;
			if(error && error.data) {
				if (error.data.message) {
					self.editSourceNutritionFactsError = error.data.message;
				} else {
					self.editSourceNutritionFactsError = error.data.error;
				}
			}
			else {
				self.editSourceNutritionFactsError = "An unknown error occurred.";
			}
		};

		/**
		 * Reload ECommerceView.
		 */
		$scope.$on(self.RELOAD_ECOMMERCE_VIEW, function() {
			self.$onChanges();
		});

		self.findAttributeMappingByLogicalAttribute = function(){
			self.parent.findAttributeMappingByLogicalAttribute(1679);
		};

		/**
		 * Check change Panel type.
		 */
		self.checkChangePanelType = function () {
			if (self.eCommerceViewDetails.panelType !== self.eCommerceViewDetails.panelTypeOrg){
				$rootScope.contentChangedFlag = true;
			}
			else {
				$rootScope.contentChangedFlag = false;
			}
		}
	}
})();
