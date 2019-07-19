/*
 *
 *  productInfoController.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

'use strict';

/**
 * Controller for the product info panel.
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('ProductInfoController', productInfoController);

	productInfoController.$inject = ['ProductInfoService', 'ProductFactory', 'InventoryFactory', 'PermissionsService'];

	/**
	 * Constructs the productInfoController.
	 *
	 * @param productInfoService The service that passes data between this controller and it's hosting controller.
	 * @param productFactory The API to fetch product data.
	 * @param inventoryFactory The API to fetch product inventory data.
	 */
	function productInfoController(productInfoService, productFactory, inventoryFactory, permissionsService) {

		var self = this;

		/**
		 * Whether or not the controller is waiting for data.
		 *
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Called from the HTML to close the panel.
		 */
		self.closePage = function(){
			productInfoService.hide();
		};

		/**
		 * Initializes the data in the controller.
		 */
		self.init = function() {
			self.isWaiting = true;

			// Fetch information about the product.
			self.prodId = productInfoService.getProductId();
			productFactory.get(
				{
					productId: self.prodId
				}, self.setProductData, self.fetchError);

			// Fetch inventory information.
			if (permissionsService.getPermissions("IN_STOR_01", "VIEW")) {
				inventoryFactory.queryStoreInventory({
					productId: self.prodId
				}, self.setInventoryData, self.fetchError);
			}
			if (productInfoService.getComparisonProductId() != null) {
				self.comparisonProductId = productInfoService.getComparisonProductId();
				productFactory.get(
					{
						productId:self.comparisonProductId
					}, self.setProductData, self.fetchError);

				// Fetch inventory information.
				if (permissionsService.getPermissions("IN_STOR_01", "VIEW")) {
					inventoryFactory.queryStoreInventory({
						productId: self.comparisonProductId
					}, self.setInventoryData, self.fetchError);
				}
			}
		};

		/**
		 * Callback for when there was an error fetching data from the backend.
		 *
		 * @param errorResults The error object from the backend.
		 */
		self.fetchError = function(errorResults){
			self.isWaiting = false;
			productInfoService.setError(errorResults.data.message);
		};

		/**
		 * Callback for the API call to get data from the backend.
		 *
		 * @param result The results sent from the backend.
		 */
		self.setProductData = function(result){
			self.isWaiting = false;
			if(productInfoService.getProductItemCode() != null){
				result = self.removeExtraProdItems(result);
			}
			if (result.prodId == self.prodId) {
				self.description = result.description;
				self.data = self.getPrimaryFirst(result.prodItems);
			}
			if (result.prodId == self.comparisonProductId) {
				self.comparisonDescription = result.description;
				self.comparisonData = self.getPrimaryFirst(result.prodItems);
			}

		};

		/**
		 * This method will search through the list of prodItems and confirm they are part of the item code search
		 * @param result result of the query to the backend
		 * @returns result with a filted prodItem list.
		 */
		self.removeExtraProdItems= function(result){
			var filteredProdItems = [];
			var index =0;
			for(index = 0; index<result.prodItems.length; index++){
				var value = result.prodItems[index];
				if(productInfoService.getProductItemCode().includes(value.key.itemCode)){
					filteredProdItems.push(value);
				}
			}
			result.prodItems = filteredProdItems;
			return result;
		};

		/**
		 * Callback for the API call to get product level inventory.
		 *
		 * @param result The results sent from the backend.
		 */
		self.setInventoryData = function(result){
			if (self.prodId == result.productId) {
				self.inventoryData = result.inventory;
			}
			if (self.comparisonProductId == result.productId) {
				self.comparisonInventoryData = result.inventory;
			}
		};

		/**
		 * Reorganizes the information about the product to put the Primary UPC for each item
		 * first in the list of UPCs.
		 *
		 * @param resultList The list of item codes to process.
		 * @returns {*} The same list with the primary UPCs for each item first in the list of UPCs
		 * tied to that item.
		 */
		self.getPrimaryFirst = function(resultList) {

			var currentObject;

			for(var index=0; index < resultList.length; index++) {

				// For DSD product, don't go into the UPC details as there are no associates.
				if (resultList[index].itemMaster.key.dsd) {
					continue;
				}
				currentObject = resultList[index].itemMaster.primaryUpc.associateUpcs;

				for(var innerIndex = 0; innerIndex < currentObject.length; innerIndex++) {
					if (currentObject[innerIndex].sellingUnit.primaryUpc) {
						var primaryUpc = currentObject.splice(innerIndex, 1);
						currentObject.unshift(primaryUpc[0]);
						break;
					}
				}
			}
			return resultList;
		};
	}

})();
