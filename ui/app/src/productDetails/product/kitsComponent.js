/*
 *   kitsComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Kits -> Kits Info page component.
 *
 * @author m594201
 * @since 2.8.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('kits', {
		// isolated scope binding
		require: {
			productDetail: '^productDetail'
		},
		bindings: {
			listOfProducts: '=',
			productMaster: '<'

		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/kits.html',
		// The controller that handles our component logic
		controller: KitsController
	});

	KitsController.$inject = ['productApi', '$scope', '$timeout', 'kitsService', 'ProductFactory'];


	function KitsController(productApi, $scope, $timeout, kitsService, productFactory) {

		var self = this;

		/**
		 * The current error message.
		 * @type {String}
		 */
		self.error = null;

		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;
		/**
		 * This object holds a list of all the kits that are currently being viewed if the product itself is a kit it will
		 * display a single kit if the product is involved in several kits it will display those kits
		 * @type {Array}
		 */
		self.listOfKits=[];
		/**
		 * A list of kit components that a product is part of a kit
		 * @type {Array}
		 */
		self.listOfInvolvedKits=[];
		/**
		 * flag for the is kit status of a product
		 * @type {boolean}
		 */
		self.isKit=false;
		/**
		 * Is used to state if the component should try a second attempt at finding a product relationship.
		 * @type {boolean}
		 */
		self.keepTrying=true;
		/**
		 * Initialize the controller.
		 */
		this.$onInit = function () {
		};

		/**
		 * Component will reload the kits data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
			self.isWaitingForResponse = true;
			self.listOfKits=[];
			var selectedKits = null;
			self.isKit=kitsService.isKit();
			if(kitsService.getOriginalSelectedKits() !== null) {
				selectedKits = kitsService.getOriginalSelectedKits();
			}
			if(kitsService.getOriginalSelectedProduct() !== null){
				if(!angular.equals(kitsService.getOriginalSelectedProduct(), self.productMaster)){
					selectedKits = null;
				}
			}
			//On selection of kits tab check if there is a previous saved kit, if so load that kits data.
			if(selectedKits !== null){
				self.listOfKits = selectedKits;
				//After kit is loaded reset saved values to null for next save.
				kitsService.setOriginalSelectedKits(null);
				kitsService.setReturnEnabled(false);
				self.isWaitingForResponse=false;
			} else {
				self.getKitsData(self.isKit);
			}
		};

		self.getKitsData = function (isKitFlag) {
			if(isKitFlag){
				//When there is no saved kit data pull kit data based on the prodId.
				productApi.getKitsData({'productId': self.productMaster.prodId}, self.loadData, self.fetchError)
			} else {
				var upcs=[];
				angular.forEach(self.productMaster.sellingUnits, function(sellingUnit){
					upcs.push(sellingUnit.upc)
				});
				productApi.getKitsDataByElements(upcs,
					function(results){
						self.listOfInvolvedKits=results;
						angular.forEach(self.listOfInvolvedKits, function(kit){
							productApi.getKitsData({'productId': kit.key.productId}, self.loadData, self.fetchError)
						});
					},
					self.fetchError);
			}
		};

		/**
		 * Loads the data sent back from the back end.
		 * @param results
		 */
		self.loadData = function (results) {
			var kit = angular.copy(results);
			if(results.length === 0){
				if(self.keepTrying){
					self.keepTrying = false;
					self.isKit = !self.isKit;
					self.getKitsData(self.isKit);
				} else {
					self.fetchError("No kits founds")
				}
			} else {
				productApi.getKitsProduct({prodId: kit[0].key.productId},
					function (results) {
						self.isWaitingForResponse = false;
						kit.productMaster = results;
					},
					self.fetchError);
				self.listOfKits.push(kit);
			}
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};

		/**
		 * Function that handles the tab change that happens when an element UPC is click on the Kits table.
		 *
		 * @param kit
		 */
		self.changeTab = function(kit) {
			var productFound = null;
			for(var productIndex = 0; productIndex < self.listOfProducts.length; productIndex++){
				//Check to see if the prodId clicked is in the list current list of productIds.
				if(self.listOfProducts[productIndex].prodId === kit.relatedProduct.prodId){
					productFound = self.listOfProducts[productIndex];
					break;
				}
			}
			//When the productId is not found in the current productList then add it to that listOfProducts.
			if(productFound === null){
				productFound = kit.relatedProduct;
				self.listOfProducts.push(productFound);
			}

			//Used to determine the displayed selectedUnit.
			var sellingUnitIndexFound = -1;
			for(var sellingUnitIndex = 0; sellingUnitIndex < productFound.sellingUnits.length; sellingUnitIndex++){
				if(productFound.sellingUnits[sellingUnitIndex].upc === kit.elementUpc){
					sellingUnitIndexFound = sellingUnitIndex;
					break;
				}
			}

			//Call the kitsService to save the current search results values.
			kitsService.setOriginalSelectedCasePack(self.productDetail.selectedCasePack);
			kitsService.setOriginalSelectedProduct(self.productMaster);
			kitsService.setOriginalSelectedSellingUnit(productFound.sellingUnits[sellingUnitIndexFound]);
			kitsService.setReturnEnabled(true);
			kitsService.setIsKit(self.isKit);
			kitsService.setOriginalSelectedKits(self.listOfKits);
			//Call overloaded function in productDetail to handles changing the correct sellingUnit.
			self.productDetail.handleProductChange(productFound, null, sellingUnitIndexFound);
			//function that selects the correct Selling Unit UPC when tab changes from kits to UPC info.
			$timeout(function(){
				$scope.$evalAsync(function() {
					var element = document.getElementById('sellingUnit' + sellingUnitIndexFound);
					document.getElementById('sellingUnitTable').scrollTop = element.offsetTop;
				});
			});

			//Call function in productDetail to set the selected tab and update the selected tab name
			self.productDetail.toggleTab('upcInfoTab');

		};
	}

})();
