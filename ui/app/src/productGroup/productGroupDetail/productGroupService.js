/*
 * productGroupService.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Creates the service used to interact with the product group detail.
 */
(function() {
	angular.module('productMaintenanceUiApp').service('ProductGroupService', productGroupService);

	function productGroupService() {

		var self = this;

		/**
		 * Selected product Id
		 * @type {Long}
		 */
		self.productGroupTypeCode = null;
		/**
		 * Selected product Id
		 * @type {Long}
		 */
		self.selectedProductId = null;
		/**
		 * Selected product group Id
		 * @type {Long}
		 */
		self.productGroupId = null;
		/**
		 * Navigate from ecommerce view page to another page or not
		 * @type {Boolean}
		 */
		self.returnToListFlag = false;
		/**
		 * Flag display page when navigate from product group type page.
		 */
		self.navFromProdGrpTypePageFlag = false;
		/**
		 * Flag display page when navigate from product group type page from customer hierarchy.
		 */
		self.navigateFromCustomerHierPage = false;
		/**
		 * Product group type code
		 */
		self.productGroupTypeCodeNav = null;
		/**
		 * Data product group name input.
		 * @type {string}
		 */
		self.productGroupName = "";
		/**
		 * Data customer hierachy id.
		 * @type {string}
		 */
		self.customerHierarchy = "";
		/**
		 * The browser all status.
		 * @type {boolean}
		 */
		self.isBrowserAll = false;
		/**
		 * The search text.
		 * @type {string}
		 */
		self.customHierarchySearchText='';
		self.customerHierarchyId = null;
		/**
		 * List Product received from detail task page.
		 */
		self.listOfProducts = [];
		return {
			/**
			 * Initializes the controller.
			 *
			 * @param showFullPanel Whether or not to show the full panel (excludes item status filter). This will add
			 * things like the MRT tab, the add button for complex search, product description search, etc.
			 * @param showStatusFilter Whether or not to show the item status filter options.
			 * @param basicSearchCallback The callback for when the user clicks on a basic search button. Basic
			 * searches only have one set of criteria.
			 * @param complexSearchCallback The callback for when the user clicks on the complex search button. Complex
			 * searches can contain multiple sets of criteria.
			 */
			init:function() {

			},
			/**
			 * Returns ProductGroupTypeCode
			 *
			 * @returns {null|*}
			 */
			getProductGroupTypeCode:function() {
				return self.productGroupTypeCode;
			},

			/**
			 * Sets the values the product group type code.
			 *
			 * @param selectedProductId
			 */
			setProductGroupTypeCode:function(productGroupTypeCode) {
				self.productGroupTypeCode = productGroupTypeCode;
			},

			/**
			 * Returns selectedProductId
			 *
			 * @returns {null|*}
			 */
			getSelectedProductId:function() {
				return self.selectedProductId;
			},

			/**
			 * Sets the values the selected productId
			 *
			 * @param selectedProductId
			 */
			setSelectedProductId:function(selectedProductId) {
				self.selectedProductId = selectedProductId;
			},
			/**
			 * Returns listOfProducts
			 *
			 * @returns {null|*}
			 */
			getListOfProducts:function() {
				return self.listOfProducts;
			},

			/**
			 * Sets the values the listOfProducts.
			 *
			 * @param listOfProducts
			 */
			setListOfProducts:function(listOfProducts) {
				self.listOfProducts = listOfProducts;
			},
			/**
			 * Returns the values productGroupId
			 *
			 * @returns {null|*}
			 */
			getProductGroupId:function() {
				return self.productGroupId;
			},
			/**
			 * Sets the values the product group Id
			 *
			 * @param productGroupId
			 */
			setProductGroupId:function(productGroupId) {
				self.productGroupId = productGroupId;
			},

			/**
			 * Returns return to list flag
			 *
			 * @returns {true|false}
			 */
			getReturnToListFlag:function() {
				return self.returnToListFlag;
			},

			/**
			 * Sets return to list flag.
			 *
			 * @param returnToListFlag
			 */
			setReturnToListFlag:function(returnToListFlag) {
				self.returnToListFlag = returnToListFlag;
			},
			/**
			 * Returns custom hierarchy level the user has selected for navigation.
			 *
			 * @returns selectedCustomHierarchyLevel
			 */
			getCustomerHierarchyId:function(){
				return self.customerHierarchyId;
			},

			/**
			 * Sets custom hierarchy level the user has selected for navigation.
			 *
			 * @param selectedCustomHierarchyLevel
			 */
			setCustomerHierarchyId:function(customerHierarchyId){
				self.customerHierarchyId = customerHierarchyId;
			},

			/**
			 * Return flag display page when navigate from product group type page.
			 *
			 * @returns {true|false}
			 */
			getNavFromProdGrpTypePageFlag:function() {
				return self.navFromProdGrpTypePageFlag;
			},

			/**
			 * Set flag display page when navigate from product group type page.
			 *
			 * @param returnToListFlag
			 */
			setNavFromProdGrpTypePageFlag:function(navFromProdGrpTypePageFlag) {
				self.navFromProdGrpTypePageFlag = navFromProdGrpTypePageFlag;
			},
			/**
			 * Return flag display page when navigate from product group type page.
			 *
			 * @returns {true|false}
			 */
			getNavigateFromCustomerHierPage:function() {
				return self.navigateFromCustomerHierPage;
			},

			/**
			 * Set flag display page when navigate from product group type page.
			 *
			 * @param navigateFromCustomerHierPage
			 */
			setNavigateFromCustomerHierPage:function(navigateFromCustomerHierPage) {
				self.navigateFromCustomerHierPage = navigateFromCustomerHierPage;
			},


			/**
			 * Return product group type code of product group type.
			 *
			 * @returns {true|false}
			 */
			getProductGroupTypeCodeNav:function() {
				return self.productGroupTypeCodeNav;
			},

			/**
			 * Set product group type code of product group type.
			 *
			 * @param returnToListFlag
			 */
			setProductGroupTypeCodeNav:function(productGroupTypeCodeNav) {
				self.productGroupTypeCodeNav = productGroupTypeCodeNav;
			},
			/**
			 * Return product Group Name.
			 *
			 * @returns productGroupName
			 */
			getProductGroupName:function() {
				return self.productGroupName;
			},

			/**
			 * Set  product group name.
			 *
			 * @param productGroupName
			 */
			setProductGroupName:function(productGroupName) {
				self.productGroupName = productGroupName;
			},
			/**
			 * Return the customer Hierarchy.
			 *
			 * @returns customerHierarchy
			 */
			getCustomerHierarchy:function() {
				return self.customerHierarchy;
			},

			/**
			 * Set the customer Hierarchy.
			 *
			 * @param customerHierarchy
			 */
			setCustomerHierarchy:function(customerHierarchy) {
				self.customerHierarchy = customerHierarchy;
			},
			/**
			 * Return the status of browser All.
			 *
			 * @returns isBrowserAll
			 */
			isBrowserAll:function() {
				return self.isBrowserAll;
			},

			/**
			 * Set thestatus of browser All.
			 *
			 * @param isBrowserAll
			 */
			setBrowserAll:function(isBrowserAll) {
				self.isBrowserAll = isBrowserAll;
			},
			/**
			 * Return custom Hierarchy Search Text.
			 *
			 * @returns customHierarchySearchText
			 */
			getCustomHierarchySearchText:function() {
				return self.customHierarchySearchText;
			},
			/**
			 * Set custom Hierarchy Search Text.
			 *
			 * @param customHierarchySearchText
			 */
			setCustomHierarchySearchText:function(customHierarchySearchText) {
				self.customHierarchySearchText = customHierarchySearchText;
			},
		}
	}
})();
