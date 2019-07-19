/*
 *   AuthorizationLayoutController.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Authorization page's main or top most parent component.
 *
 * @author vn70529
 * @since 2.22.0
 */
(function() {
	var myApp =angular.module('productMaintenanceUiApp');

	myApp.component('authorizationLayoutComponent', {
		// isolated scope binding
		bindings: {
		},
		// Inline template which is bound to message variable in the component controller
		templateUrl: 'src/authorization/authorizationLayout.html',
		// The controller that handles our component logic
		controller: AuthorizationController

	});
	/**
	 * Authorization Controller definition.
	 * @constructor
	 */

	function AuthorizationController() {
		var self = this;
		self.screenType ={itemSearch:{title:'Item Search', id:'itemSearch'}, authorizeItem:{title:'Authorize Item', id:'authorizeItem'}};
		/**
		 * Selected Screen
		 */
		self.selectedScreen;
		/**
		 * Selected item on result search.
		 */
		self.selectedItem;
		/**
		 * Selected Store
		 */
		self.selectedStore;
		/**
		 * Holds Weight switch status
		 * @type {boolean}
		 */
		self.flexWeightSwitch = false;
		/**
		 * List of results search.
		 * @type {null}
		 */
		self.authorizeItems = null;
		/**
		 * The list of stores.
		 * @type {null}
		 */
		self.stores = null;
		/**
		 * Current upc.
		 * @type {null}
		 */
		self.upc = null;
		/**
		 * This upc is used for authorize.
		 * @type {null}
		 */
		self.authorizeUpc = null;

		this.$onInit = function () {
			self.selectedScreen = self.screenType.itemSearch;
		};
		/**
		 * Show message if occur error.
		 * @param error contain message returned from api.
		 */
		self.handleError = function (error) {
			self.isWaiting = false;
			if (error) {
				if(error.data){
					if (error.data.message !== null && error.data.message !== "") {
						self.error = error.data.message;
					} else {
						self.error = error.data.error;
					}
				}else{
					if (error.message !== null && error.message !== "") {
						self.error = error.message;
					} else {
						self.error = error.error;
					}
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
		};
		/**
		 * Authorize item success.
		 */
		self.onAuthorizeItemSuccess = function(result){
			self.authorizeItems = null;
			self.selectedScreen = self.screenType.itemSearch;
			self.selectedItem = null;
			self.error = '';
			self.upc = null;
			self.authorizeUpc = null;
			self.success = result.message;
		};
	}
}());
