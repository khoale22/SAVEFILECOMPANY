/*
 *   productCategoryComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Product Group Search Component.
 *
 * @author vn70529
 * @since 2.12.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('productGroupDetail', {
		// isolated scope binding
		bindings: {
			itemReceived: '<',
			selectedProduct: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productGroup/productGroupDetail/productGroupDetail.html',
		// The controller that handles our component logic
		controller: ProductGroupDetailController
	});

	ProductGroupDetailController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'codeTableApi', 'productGroupApi', 'ProductGroupService'];

	/**
	 * product group detail component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @param codeTableApi
	 * @param ngTableParams
	 * @constructor
	 */
	function ProductGroupDetailController($rootScope, $scope, ngTableParams, codeTableApi, productGroupApi, productGroupService) {
		/** All CRUD operation controls of product group detail page goes here */

		var self = this;

		self.isWaiting = false;

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 *
		 * @type {?}
		 */
		self.defer = null;
		/**
		 * Start position of page that want to show on Product Group Search results.
		 *
		 * @type {number}
		 */
		self.PAGE = 1;

		/**
		 * Tab product group info
		 * @type {String}
		 */
		const PRODUCT_GROUP_INFO = "productGroupInfo";
		/**
		 * Tab image info
		 * @type {String}
		 */
		const IMAGE_INFO = "imageInfo";
		/**
		 * Tab ecommerce view info
		 * @type {String}
		 */
		const ECOMMERCE_VIEW = "eCommerceView";
		/**
		 * Tab selected
		 * @type {String}
		 */
		self.selectedTab  = "productGroupInfo";
		/**
		 * List Product Group Ids received from list Product Group Page.
		 */
		self.listOfProducts = [];

		/**
		 * Product Group selected and received from list Product Group Page.
		 */
		self.selectedProduct;

		/**
		 * Add new Product Group.
		 */
		self.isCreateProductGroup = false;

		/**
		 * init form
		 */
		self.init = function(){
			if(self.itemReceived !== undefined){

				if (productGroupService.getListOfProducts() !== null && productGroupService.getListOfProducts().length > 0 ){
					self.listOfProductTemp = [];
					var listOfProductTempOrg = [];
					listOfProductTempOrg = angular.copy(productGroupService.getListOfProducts());
					for (var i = 0; i < listOfProductTempOrg.length; i++) {
						self.listOfProductTemp[i] = listOfProductTempOrg[i].customerProductGroup.custProductGroupId;
					}
					self.listOfProducts = angular.copy(self.listOfProductTemp);
				}
				else{
				self.listOfProducts = angular.copy(self.itemReceived.productGroupIds);
				}
				self.selectedProduct = angular.copy(self.itemReceived.productGroup.custProductGroupId);
			}else{
				self.isCreateProductGroup = true;
			}
		};
		/**
		 * prev product selected
		 */
		self.prevProductSelect = function(){
			var indexItem=self.listOfProducts.indexOf(self.selectedProduct);
			$rootScope.contentChangedFlag = false;
			self.handleProductChange(self.listOfProducts[indexItem-1]);
		}
		/**
		 * next product selected
		 */
		self.nextProductSelect = function(){
			var indexItem=self.listOfProducts.indexOf(self.selectedProduct);
			$rootScope.contentChangedFlag = false;
			self.handleProductChange(self.listOfProducts[indexItem+1]);
		}
		/**
		 * check enable button prev
		 * @returns {boolean}
		 */
		self.isEnablePrevProductSelect = function(){
			var indexItem=self.listOfProducts.indexOf(self.selectedProduct);
			if(indexItem>0)
				return true;
			else
				return false;
		}
		/**
		 * check enable for next button
		 * @returns {boolean}
		 */
		self.isEnableNextProductSelect = function(){
			var indexItem=self.listOfProducts.indexOf(self.selectedProduct);
			if(indexItem<self.listOfProducts.length-1)
				return true;
			else
				return false;
		}

		self.handleProductChange = function(productId) {
			self.selectedProduct =  productId;
			//$scope.broadcast('someEvent', productId);
			$rootScope.$broadcast('reloadProductInfo',productId);
		}

		/**
		 * Handles tab change click action.
		 * @param msg
		 * @param $event
		 */
		self.handleTabChange = function($event) {
			self.tabTarget = event.target;
			if($rootScope.contentChangedFlag && (self.selectedTab !== event.target.id)){
				var result = document.getElementById("confirmationModal");
				var wrappedResult = angular.element(result);
				wrappedResult.modal("show");
			}
			else{
				self.toggleTab(self.tabTarget.id);
			}

		};

		/**
		 * Receives the results of the confirmation modal and acts accordingly
		 */
		$rootScope.$on('modalResults', function(event, args){
			if(self.tabTarget !== null){
				self.handleModalResults(args.result)
			}
		});
		/**
		 * Handles the results of the confirmation modal
		 * @param result
		 */
		self.handleModalResults = function(result){
			if(result === true){
				$rootScope.contentChangedFlag = false;
				self.toggleTab(self.tabTarget.id);
			}
			else{
				self.toggleTab(self.selectedTab);
			}
			self.tabTarget = null;
		};

		/**
		 * Used to execute tab toggle events. Sets the selected tab and updates the selected Tab name  for Right Panel Header.
		 * @param tabId
		 */
		self.toggleTab = function (tabId) {
			var tabElementSelector = '#'+tabId;
			$(tabElementSelector).tab('show');
			self.selectedTab = tabId;
		};

		self.confirmNextProduct = function () {
			self.isNextProduct = true;
			self.isSelectedFromList = false;
			if($rootScope.contentChangedFlag) {
				$('#confirmationNextPG').modal("show");
			}
			else {
				self.nextProductSelect();
			}
		};

		self.confirmPreviousProduct = function () {
			self.isNextProduct = false;
			self.isSelectedFromList = false;
			if($rootScope.contentChangedFlag) {
				$('#confirmationNextPG').modal("show");
			}
			else {
				self.prevProductSelect();
			}
		};

		self.confirmChangeProduct = function (productId) {
			self.isSelectedFromList = true;
			self.currentProductId = productId;
			if($rootScope.contentChangedFlag) {
				$('#confirmationNextPG').modal("show");
			}
			else {
				self.handleProductChange(productId);
			}
		}

	}
})();
