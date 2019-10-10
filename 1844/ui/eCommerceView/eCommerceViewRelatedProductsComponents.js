/*
 *   eCommerceViewRelatedProductsComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * product details -> product -> eCommerce View -> related products component.
 *
 * @author vn00602
 * @since 2.14.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('eCommerceViewRelatedProduct', {
		scope: '=',
		bindings: {
			productMaster: '<'
		},
		// Inline template which is bind to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewRelatedProducts.html',
		// The controller that handles our component logic
		controller: RelatedProductController
	});

	RelatedProductController.$inject = ['$scope', 'ECommerceViewApi', 'ngTableParams'];

	/**
	 * Product Relationships component's controller definition.
	 *
	 * @param $scope scope of the eCommerceView component.
	 * @param eCommerceViewApi the api of eCommerceView.
	 * @param ngTableParams the table display product relationships.
	 * @constructor
	 */
	function RelatedProductController($scope, eCommerceViewApi, ngTableParams) {

		/**
		 * All CRUD operation controls of choice option page goes here.
		 */
		var self = this;

		/**
		 * The default error message.
		 *
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * The default no records found message.
		 *
		 * @type {string}
		 */
		self.NO_RECORDS_FOUND = "No records found.";

		/**
		 * The ADDON product relationship code.
		 *
		 * @type {string}
		 */
		self.ADDON_PRODUCT_RELATIONSHIP_CODE = "ADDON";

		/**
		 * Flag for waiting response from back end.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * The list of product relationships information.
		 *
		 * @type {Array}
		 */
		self.relatedProducts = [];

		/**
		 * The flag check whether data exist or not to show table.
		 *
		 * @type {boolean}
		 */
		self.showTable = false;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized.
		 */
		this.$onInit = function () {
			self.isWaitingForResponse = true;
			self.findProductRelationships();
			self.tableParams = self.buildTable();
		};

		/**
		 * Component will reload the data whenever the item is changed in.
		 */
		this.$onChanges = function () {
			self.isWaitingForResponse = true;
			if (self.currentTab && self.currentTab.id && self.currentTab.id !== 'Google') {
				self.findProductRelationships();
			} else {
				self.isWaitingForResponse = false;
				// self.relatedProducts = [];
			}
		};

		/**
		 * Constructs the table that shows the product relationships.
		 */
		self.buildTable = function () {
			self.isWaitingForResponse = true;
			return new ngTableParams({},
				{
					counts: [],
					dataset: self.relatedProducts
				}
			);
		};

		/**
		 * Find all the list of product relationships information.
		 */
		self.findProductRelationships = function () {
			eCommerceViewApi.findProductRelationships({
					productId: self.productMaster.prodId
				},
				self.productRelationshipsResponseSuccess,
				self.fetchError);
		};

		/**
		 * Load product relationships data response success.
		 *
		 * @param results the results to load.
		 */
		self.productRelationshipsResponseSuccess = function (results) {
			self.isWaitingForResponse = false;
			if (results.length === 0) {
				self.relatedProducts = [];
				self.error = self.NO_RECORDS_FOUND;
				self.showTable = false;
			} else {
				self.error = null;
				self.relatedProducts = results;
				self.showTable = true;
				self.showAddOnSellable();
			}
		};

		/**
		 * Show Add on sellable checkbox if its description is Add on Product.
		 */
		self.showAddOnSellable = function () {
			angular.forEach(self.relatedProducts, function (relatedProduct) {
				if (relatedProduct.key.productRelationshipCode === self.ADDON_PRODUCT_RELATIONSHIP_CODE) {
					relatedProduct.isDisplayAddon = true;
				}
			});
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
		};

		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			} else {
				return self.UNKNOWN_ERROR;
			}
		};
	}
})();
