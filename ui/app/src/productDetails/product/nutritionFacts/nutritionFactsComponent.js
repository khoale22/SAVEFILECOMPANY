/*
 *   nutritionFactsComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Product -> nutrition facts component.
 *
 * @author vn73545
 * @since 2.15.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('nutritionFacts', {
		bindings: {
			productMaster: '<',
			isLastProduct: '&'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/nutritionFacts/nutritionFacts.html',
		// The controller that handles our component logic
		controller: NutritionFactsController
	});

	NutritionFactsController.$inject = ['NutritionFactsApi', '$scope', '$state', 'appConstants', 'ProductSearchService', 'taskService','ProductGroupService','$location', '$rootScope'];


	function NutritionFactsController(nutritionFactsApi, $scope, $state, appConstants, productSearchService, taskService, productGroupService ,$location, $rootScope) {

		var self = this;
		/**
		 * Constant for MEDIA_MASTER
		 * @type {string}
		 */
		const MEDIA_MASTER = "Media Master";
		/**
		 * Constant for HEB_COM
		 * @type {string}
		 */
		const HEB_COM = "Heb.com";
		/**
		 * Constant for EMPTY
		 * @type {string}
		 */
		const EMPTY = "";
		/**
		 * Constant for SPACE
		 * @type {string}
		 */
		const SPACE = " ";
		/**
		 * Constant for OPEN_PARENTHESES
		 * @type {string}
		 */
		const OPEN_PARENTHESES = " ( ";
		/**
		 * Constant for CLOSE_PARENTHESES
		 * @type {string}
		 */
		const CLOSE_PARENTHESES = " )";
		/**
		 * Constant for SEMICOLON
		 * @type {string}
		 */
		const SEMICOLON = "; ";
		/**
		 * Constant for ONE
		 * @type {string}
		 */
		const ONE = "1";
		/**
		 * Constant for UNKNOWN_ERROR
		 * @type {string}
		 */
		const UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * enable or disable return to list button
		 *
		 * @type {Boolean}
		 */
		self.disableReturnToList = true;

		/**
		 * Task Detail base url.
		 * @type {string}
		 */
		const TASK_DETAIL_URL = "/task/ecommerceTask/taskInfo/";

		/**
		 * Initialize the controller.
		 */
		this.$onInit = function () {
			//Check to enable or disable the return to list button
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		};

		/**
		 * Component will reload the kits data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
			self.error = null;
			self.success = null;
			self.getData();
		};

		/**
		 * Get data of nutrition fact.
		 */
		self.getData = function () {
			self.getAllNutritionFactsInformation();
		};

		/**
		 * Get nutrition fact information.
		 */
		self.getAllNutritionFactsInformation = function () {
			self.isWaitingForResponse = true;
			self.nutritionList = [];
			self.approvedNutritionList = [];
			self.unApprovedNutritionList = [];
			nutritionFactsApi.getAllNutritionFactsInformation(
				{
					productId: self.productMaster.prodId,
					primaryUpc: self.productMaster.productPrimaryScanCodeId
				},
				//success case
				function (results) {
					self.isWaitingForResponse = false;
					if(results != null && results.length > 0){
						angular.forEach(results, function(result){
							var dataArrays = self.getNutritionList(result);
							angular.forEach(dataArrays, function(item){
								self.nutritionList.push(item);
							});
						});
					}
				},self.fetchError);
		};

		/**
		 * Get list of nutrition data to display.
		 */
		self.getNutritionList = function (result) {
			var returnList = null;
			if(result.sourceSystemId === 17){
				returnList = self.getNutritionFactByScaleSource(result);
			}else if(result.sourceSystemId === 26 && result.sourceSystemDefault === true){
				self.getUnApprovedNutritionList(result);//S26 (UnApproved)
			}else if(result.sourceSystemId === 26 && result.sourceSystemDefault === false){
				self.getApprovedNutritionList(result);//S26 (Approved)
			}else{
				var productNutrients = angular.copy(result.productNutrients);
				var productPkVariations = angular.copy(result.productPkVariations);
				var nutritionList = [];
				if(productPkVariations != null && productPkVariations.length > 0){
					angular.forEach(productPkVariations, function(productPkVariation){
						var nutritionItem = {};
						nutritionItem.isSource17 = false;
						nutritionItem.upc = angular.copy(productPkVariation.key.upc);
						nutritionItem.sourceSystemDescription = angular.copy(result.sourceSystemDescription);
						nutritionItem.sourceSystemId = angular.copy(result.sourceSystemId);

						if(result.sourceSystemId === 13){
							nutritionItem.target = HEB_COM;
							nutritionItem.sourceSystemDescription = null;
						}

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
								if(productNutrient.dclrOnLblSw !== ONE
									&& productNutrient.key.masterId !== 20567
									&& productNutrient.key.masterId !== 27187
									&& productNutrient.key.masterId !== 20592){
									nutritionItem.nutritionFactList.push(productNutrient);
								}
							}
						});

						var isOrContains = EMPTY;
						var checkServingSize = false;
						var checkServingPerContainer = false;
						angular.forEach(nutritionFactList, function(item){
							if(item.dclrOnLblSw === ONE && item.nutrientMaster.nutrientName !== EMPTY){
								if(isOrContains === EMPTY){
									isOrContains = item.nutrientMaster.nutrientName;
								}else{
									isOrContains = isOrContains + SEMICOLON + item.nutrientMaster.nutrientName;
								}
							}
							if(!checkServingSize && (item.key.masterId === 20567 || item.key.masterId === 27187)){
								checkServingSize = true;
								nutritionItem.servingSize = nutritionItem.servingSize + OPEN_PARENTHESES + item.nutrientQuantity.toFixed(2) + SPACE + item.servingSizeUOM.servingSizeUomDescription + CLOSE_PARENTHESES;
							}
							if(!checkServingPerContainer && item.key.masterId === 20592){
								checkServingPerContainer = true;
								nutritionItem.servingPerContainer = nutritionItem.servingPerContainer + OPEN_PARENTHESES + item.nutrientQuantity.toFixed(2) + SPACE + item.servingSizeUOM.servingSizeUomDescription + CLOSE_PARENTHESES;
							}
						});
						nutritionItem.isOrContains = isOrContains;
						nutritionList.push(nutritionItem);
					});
				}else{
					if(productNutrients != null && productNutrients.length > 0){
						var nutritionItem = {};
						nutritionItem.isSource17 = false;
						nutritionItem.upc = angular.copy(productNutrients[0].key.upc);
						nutritionItem.sourceSystemDescription = angular.copy(result.sourceSystemDescription);
						nutritionItem.sourceSystemId = angular.copy(result.sourceSystemId);
						nutritionItem.nutritionFactList = angular.copy(productNutrients);
						nutritionList.push(nutritionItem);
					}
				}
				returnList = angular.copy(nutritionList);
			}
			return returnList;
		};

		/**
		 * Get list of approved nutrition data to display.
		 */
		self.getApprovedNutritionList = function (result) {//S26 (Approved)
			var productNutrients = angular.copy(result.productNutrients);
			var productPkVariations = angular.copy(result.productPkVariations);
			var nutritionList = [];
			if(productPkVariations != null && productPkVariations.length > 0){
				angular.forEach(productPkVariations, function(productPkVariation){
					var nutritionItem = {};
					nutritionItem.isSource17 = false;
					nutritionItem.upc = angular.copy(productPkVariation.key.upc);
					nutritionItem.sourceSystemDescription = angular.copy(result.sourceSystemDescription);
					nutritionItem.sourceSystemId = angular.copy(result.sourceSystemId);
					nutritionItem.ingredients = angular.copy(result.ingredients);
					nutritionItem.allergens = angular.copy(result.allergens);
					nutritionItem.createdDate = angular.copy(result.createdDate);
					nutritionItem.target = MEDIA_MASTER;
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
							if(productNutrient.dclrOnLblSw !== ONE
									&& productNutrient.key.masterId !== 20567
									&& productNutrient.key.masterId !== 27187
									&& productNutrient.key.masterId !== 20592){
								nutritionItem.nutritionFactList.push(productNutrient);
							}
						}
					});

					var isOrContains = EMPTY;
					var checkServingSize = false;
					var checkServingPerContainer = false;
					angular.forEach(nutritionFactList, function(item){
						if(item.dclrOnLblSw === ONE && item.nutrientMaster.nutrientName !== EMPTY){
							if(isOrContains === EMPTY){
								isOrContains = item.nutrientMaster.nutrientName;
							}else{
								isOrContains = isOrContains + SEMICOLON + item.nutrientMaster.nutrientName;
							}
						}
						if(!checkServingSize && (item.key.masterId === 20567 || item.key.masterId === 27187)){
							checkServingSize = true;
							nutritionItem.servingSize = nutritionItem.servingSize + OPEN_PARENTHESES + item.nutrientQuantity.toFixed(2) + SPACE + item.servingSizeUOM.servingSizeUomDescription + CLOSE_PARENTHESES;
						}
						if(!checkServingPerContainer && item.key.masterId === 20592){
							checkServingPerContainer = true;
							nutritionItem.servingPerContainer = nutritionItem.servingPerContainer + OPEN_PARENTHESES + item.nutrientQuantity.toFixed(2) + SPACE + item.servingSizeUOM.servingSizeUomDescription + CLOSE_PARENTHESES;
						}
					});
					nutritionItem.isOrContains = isOrContains;
					nutritionList.push(nutritionItem);
				});
			}else{
				if(productNutrients != null && productNutrients.length > 0){
					var nutritionItem = {};
					nutritionItem.isSource17 = false;
					nutritionItem.upc = angular.copy(productNutrients[0].key.upc);
					nutritionItem.sourceSystemDescription = angular.copy(result.sourceSystemDescription);
					nutritionItem.sourceSystemId = angular.copy(result.sourceSystemId);
					nutritionItem.ingredients = angular.copy(result.ingredients);
					nutritionItem.allergens = angular.copy(result.allergens);
					nutritionItem.nutritionFactList = angular.copy(productNutrients);
					nutritionList.push(nutritionItem);
				}
			}
			self.approvedNutritionList = angular.copy(nutritionList);
		};

		/**
		 * Get list of unapproved nutrition data to display.
		 */
		self.getUnApprovedNutritionList = function (result) {//S26 (UnApproved)
			var productNutrients = angular.copy(result.candidateNutrients);
			var productPkVariations = angular.copy(result.candidateProductPkVariations);
			var nutritionList = [];
			if(productPkVariations != null && productPkVariations.length > 0){
				angular.forEach(productPkVariations, function(productPkVariation){
					var nutritionItem = {};
					nutritionItem.isSource17 = false;
					nutritionItem.upc = angular.copy(productPkVariation.key.upc);
					nutritionItem.sourceSystemDescription = angular.copy(result.sourceSystemDescription);
					nutritionItem.sourceSystemId = angular.copy(result.sourceSystemId);
					nutritionItem.ingredients = angular.copy(result.ingredients);
					nutritionItem.allergens = angular.copy(result.allergens);
					nutritionItem.createdDate = angular.copy(result.createdDate);
					nutritionItem.psWorkId = angular.copy(productPkVariation.key.candidateWorkRequestId);
					nutritionItem.nutritionFactList = [];
					nutritionItem.servingSize = productPkVariation.houseHoldMeasurement;
					nutritionItem.servingPerContainer = productPkVariation.servingsPerContainerText;
					nutritionItem.amountPerServing = productPkVariation.prodValDes;

					var nutritionFactList = [];
					angular.forEach(productNutrients, function(productNutrient){
						if(productNutrient.key.upc === productPkVariation.key.upc
								&& productNutrient.key.candidateWorkId === productPkVariation.key.candidateWorkRequestId
								&& productNutrient.valPreprdTypCd === productPkVariation.key.sequenceNumber){
							nutritionFactList.push(productNutrient);
							if(productNutrient.dclrOnLblSw !== ONE
									&& productNutrient.masterId !== 20567
									&& productNutrient.masterId !== 27187
									&& productNutrient.masterId !== 20592){
								nutritionItem.nutritionFactList.push(productNutrient);
							}
						}
					});

					var isOrContains = EMPTY;
					var checkServingSize = false;
					var checkServingPerContainer = false;
					angular.forEach(nutritionFactList, function(item){
						if(item.dclrOnLblSw === ONE && item.nutrientMaster.nutrientName !== EMPTY){
							if(isOrContains === EMPTY){
								isOrContains = item.nutrientMaster.nutrientName;
							}else{
								isOrContains = isOrContains + SEMICOLON + item.nutrientMaster.nutrientName;
							}
						}
						if(!checkServingSize && (item.masterId === 20567 || item.masterId === 27187)){
							checkServingSize = true;
							nutritionItem.servingSize = nutritionItem.servingSize + OPEN_PARENTHESES + item.nutrientQuantity.toFixed(2) + SPACE + item.servingSizeUOM.servingSizeUomDescription + CLOSE_PARENTHESES;
						}
						if(!checkServingPerContainer && item.masterId === 20592){
							checkServingPerContainer = true;
							nutritionItem.servingPerContainer = nutritionItem.servingPerContainer + OPEN_PARENTHESES + item.nutrientQuantity.toFixed(2) + SPACE + item.servingSizeUOM.servingSizeUomDescription + CLOSE_PARENTHESES;
						}
					});
					nutritionItem.isOrContains = isOrContains;
					nutritionList.push(nutritionItem);
				});
			}else{
				if(productNutrients != null && productNutrients.length > 0){
					var nutritionItem = {};
					nutritionItem.isSource17 = false;
					nutritionItem.upc = angular.copy(productNutrients[0].key.upc);
					nutritionItem.sourceSystemDescription = angular.copy(result.sourceSystemDescription);
					nutritionItem.sourceSystemId = angular.copy(result.sourceSystemId);
					nutritionItem.nutritionFactList = angular.copy(productNutrients);
					nutritionItem.ingredients = angular.copy(result.ingredients);
					nutritionItem.allergens = angular.copy(result.allergens);
					nutritionList.push(nutritionItem);
				}
			}
			self.unApprovedNutritionList = angular.copy(nutritionList);
		};

		/**
		 * Get nutrition data of scale source.
		 */
		self.getNutritionFactByScaleSource = function (result) {//S17
			var scaleUpc = angular.copy(result.scaleUpc);
			var nutrientStatementDetails = angular.copy(scaleUpc.nutrientStatementDetails);
			var nutritionList = [];
			var nutritionItem = {};
			nutritionItem.isSource17 = true;
			nutritionItem.upc = angular.copy(scaleUpc.upc);
			nutritionItem.sourceSystemDescription = angular.copy(result.sourceSystemDescription);
			nutritionItem.sourceSystemId = angular.copy(result.sourceSystemId);
			nutritionItem.target = MEDIA_MASTER;
			nutritionItem.nutritionFactList = [];
			nutritionItem.servingSize = scaleUpc.nutrientStatementHeader.measureQuantity + SPACE
										+ scaleUpc.nutrientStatementHeader.nutrientCommonUom.nutrientUomDescription + OPEN_PARENTHESES
										+ scaleUpc.nutrientStatementHeader.metricQuantity.toFixed(2) + SPACE
										+ scaleUpc.nutrientStatementHeader.nutrientMetricUom.nutrientUomDescription + CLOSE_PARENTHESES;
			nutritionItem.servingPerContainer = scaleUpc.nutrientStatementHeader.servingsPerContainer;
			nutritionItem.nutritionFactList = nutrientStatementDetails;
			nutritionList.push(nutritionItem);
			return nutritionList;
		};

		/**
		 * Get quantity value.
		 */
		$scope.getQuantityValue = function(value){
			var message = EMPTY;
			if(value < 0){
				message = "< " + Math.abs(value).toFixed(1);
			}else{
				message = value.toFixed(1);
			}
			return message;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = (error.data.message);
				} else if (error.data.error) {
					self.error = (error.data.error);
				} else {
					self.error = error;
				}
			}
			else {
				self.error = UNKNOWN_ERROR;
			}
		};

		/**
		 * Approve nutrition fact information.
		 */
		self.approveNutritionFactInformation = function () {
			self.error = null;
			self.success = null;
			self.isWaitingForResponse = true;
			if(self.unApprovedNutritionList != null && self.unApprovedNutritionList.length > 0){
				var psWorkIds = [];
				angular.forEach(self.unApprovedNutritionList, function(item){
					psWorkIds.push(item.psWorkId);
				});
				nutritionFactsApi.approveNutritionFactInformation(psWorkIds,
					//success case
					function (result) {
						self.isWaitingForResponse = false;
						if(result && result.message) {
							self.success = result.message;
						}
						if (productSearchService.getFromPage() == appConstants.HOME){
							self.getData();
						}else{
							if(self.isLastProduct()){
								$rootScope.$broadcast("reloadPreviousPageAfterPublish");
							}else {
								$rootScope.$broadcast("reloadCurrentPageAfterPublish");
							}
						}
				},self.fetchError);
			}
		};

		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
		/**
		 * call the search component to search product automatically
		 */
		self.searchAutomaticCustomHierarchy = function(){
			$rootScope.$broadcast('searchAutomaticCustomHierarchy');
		}
	}
})();
