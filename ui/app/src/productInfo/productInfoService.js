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

'use strict';

/**
 * Constructs a service to pass data between a hosting controller and the product information panel.
 */
(function(){
	angular.module('productMaintenanceUiApp').service('ProductInfoService', productInfoService);

	/**
	 * Construct the service. The hosting controller is responsible for showing and hiding the panel, this
	 * controller just manages state on whether or not it should he displayed. The hosting controller needs to have
	 * the following methods defined:
	 *
	 * setError - sets an error message to be displayed.
	 *
	 * @returns {{ProductInfoService}}
	 */
	function productInfoService(){

		var self = this;

		self.visible = false;
		self.productId = null;
		self.comparisonProductId = null;
		self.error = null;
		self.sourceController = null;
		self.itemId = null;
		return {
			/**
			 * The function to show the panel.
			 *
			 * @param sourceController The controller that is requesting the product information panel to show.
			 * @param productId The ID of the product whose information is being requested.
			 * @param comparisonProductId Optional Product ID to show in comparison to the main product ID.
			 * @param itemId option item code for when Item code search is done.
			 */
			show:function(sourceController, productId, comparisonProductId, itemId) {
				self.sourceController = sourceController;
				self.productId = productId;
				self.comparisonProductId = comparisonProductId;
				self.visible = true;
				self.itemId = itemId;
			},
			/**
			 * Hides the panel.
			 */
			hide:function() {
				self.sourceController = null;
				self.productId = null;
				self.comparisonProductId = null;
				self.visible = false;
			},
			/**
			 * Returns whether or not this panel should be visible.
			 *
			 * @returns {boolean}
			 */
			isVisible:function(){
				return self.visible;
			},
			/**
			 * Returns the ID of the product being shown.
			 * @returns {null|int}
			 */
			getProductId:function(){
				return self.productId;
			},
			/**
			 * Returns the item code of the product being shown.
			 * @returns {null|int}
			 */
			getProductItemCode:function(){
				return self.itemId;
			},

			getComparisonProductId:function() {
				return self.comparisonProductId;
			},

			/**
			 * Called to send an error back to the hosting panel.
			 *
			 * @param error The error message.
			 */
			setError: function(error) {
				self.sourceController.setError(error);
			}
		}
	}
})();
