'use strict';

/*
 *
 *  productInfoService.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

/**
 * Constructs a service to pass data between a hosting controller and the kits controller.
 */
(function(){
	angular.module('productMaintenanceUiApp').service('kitsService', kitsService);

	/**
	 * Construct the service. This controller manages the saved information that is currently displayed when a user selects a kit upc.
	 * The hosting controller needs to have the following methods defined:
	 *
	 * @returns {{getOriginalSelectedProduct: getOriginalSelectedProduct, setOriginalSelectedProduct: setOriginalSelectedProduct, getOriginalSelectedCasePack: getOriginalSelectedCasePack, setOriginalSelectedCasePack: setOriginalSelectedCasePack, getOriginalSelectedSellingUnit: getOriginalSelectedSellingUnit, setOriginalSelectedSellingUnit: setOriginalSelectedSellingUnit, getOriginalSelectedKits: getOriginalSelectedKits, setOriginalSelectedKits: setOriginalSelectedKits, isReturnEnabled: isReturnEnabled, setReturnEnabled: setReturnEnabled}}
	 */
	function kitsService() {

		//Variables used to save the current data in the view.  As well as set the boolean that determines if the return button is dosplayed or not.
		var originalSelectedProduct = null;
		var originalSelectedCasePack = null;
		var originalSelectedSellingUnit = null;
		var originalSelectedKits = null;
		var returnEnabled = false;
		var isKit=false;

		return{

			getOriginalSelectedProduct:function () {
				return originalSelectedProduct;
			},
			setOriginalSelectedProduct:function (selectedProduct) {
				originalSelectedProduct = selectedProduct;
			},
			getOriginalSelectedCasePack:function () {
				return originalSelectedCasePack;
			},
			setOriginalSelectedCasePack:function (selectedCasePack) {
				originalSelectedCasePack = selectedCasePack;
			},
			getOriginalSelectedSellingUnit:function () {
				return originalSelectedSellingUnit;
			},
			setOriginalSelectedSellingUnit:function (selectedSellingUnit) {
				originalSelectedSellingUnit = selectedSellingUnit;
			},
			getOriginalSelectedKits:function () {
				return originalSelectedKits;
			},
			setOriginalSelectedKits:function (selectedKits) {
				originalSelectedKits = selectedKits;
			},
			isReturnEnabled:function () {
				return returnEnabled;
			},
			setReturnEnabled:function (enabled) {
				returnEnabled = enabled;
			},
			isKit:function () {
				return isKit;
			},
			setIsKit:function (value) {
				isKit = value;
			}


		}
	}
})();
