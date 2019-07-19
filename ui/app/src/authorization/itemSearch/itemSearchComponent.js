/*
 *   ItemSearchComponent.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * The controller for the ItemSearchController.
 *
 * @author vn70529
 * @since 2.22.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('itemSearchComponent', {
		templateUrl: 'src/authorization/itemSearch/itemSearch.html',
		bindings: {
			upc:'=',
			authorizeUpc:'=',
			authorizeItems:'=',
			stores:'=',
			selectedScreen: '=',
			selectedItem:'=',
			flexWeightSwitch:'=',
			selectedStore:'=',
			success:'=',
			error:'=',
			handleError:'&'},
		controller: ItemSearchController
	});
	ItemSearchController.$inject = ['AuthorizationApi','PermissionsService'];
	/**
	 * Item Search controller definition.
	 * @param codeTableApi
	 * @constructor
	 */
	function ItemSearchController(authorizationApi, permissionsService) {
		var self = this;
		const DSD = "DSD";
		const WHS = "WHS";
		const RESOURCE_AUTHORIZE_ITEM = "AT_JVSS_01";
		const PERMISSION_EDIT = "EDIT";
		const AUTHORIZE_ITEM = {title:'Authorize Item', id:'authorizeItem'};
		const STORE_REQUIRED_MSG = 'Please select a store.';
		const UPC_PLU_REQUIRED_MSG = 'Please enter UPC/PLU.';
		const SELECT_RECORD_MSG = 'Please select a record.';
		self.isWaiting = false;
		self.isUPCSupplied = false;
		/**
		 * call when initial.
		 */
		self.init = function(){
			self.loadStores();
		};
		/**
		 * Load the list of stores.
		 */
		self.loadStores = function(){
			if(self.stores == null) {
				self.isWaiting = true;
				authorizationApi.getStores(null,
					function (result) {
						self.stores = [];
						angular.forEach(result.stores, function (storeId) {
							self.stores[self.stores.length] = {id: storeId.trim(), label: storeId};
						});
						// Set default store key
						if (result.isDefaultStore) {
							self.selectedStore = angular.copy(self.stores[0]);
						} else {
							self.selectedStore = {id: 0, label: 'Select'};
						}
						self.isWaiting = false;
					},
					function (error) {
						self.handleError({error:error});
						self.isWaiting = false;
					}
				);
			}
		};
		/**
		 * Search authorize item.
		 */
		self.search = function () {
			self.error = '';
			self.success = '';
			if(!self.isValidSearchParams()){
				return;
			}
			// This upc is used to authorize.
			self.authorizeUpc = angular.copy(self.upc);
			self.isWaiting = true;
			authorizationApi.findItems(
				{ upc: self.upc, storeId:self.selectedStore.id},
				function (result) {
					self.isUPCSupplied = result.isUPCSupplied;
					self.flexWeightSwitch = result.flexWeightSwitch;
					self.authorizeItems = result.authorizeItems;
					self.isWaiting = false;
				},
				function (error) {
					self.handleError({error:error});
					self.isWaiting = false;
				}
			);
		};
		/**
		 * Validate search params.
		 * @return {boolean} true it is valid or false and display error message.
		 */
		self.isValidSearchParams = function(){
			if(self.isEmpty(self.selectedStore) || self.isEmpty(self.selectedStore.id) || self.selectedStore.id == 0){
				self.error = STORE_REQUIRED_MSG;
				return false;
			}
			if(self.upc == null || self.upc.length == 0){
				self.error = UPC_PLU_REQUIRED_MSG;
				return false;
			}
			return true;
		};
		/**
		 * Show item type
		 * @param itemType
		 * @return {string}
		 */
		self.getDisplayItemType = function(itemType){
			if(!self.isEmpty(itemType) && itemType.trim() == DSD){
				return DSD;
			}
			return WHS;
		};
		/**
		 * Show authorize item screen
		 * @return {boolean}
		 */
		self.showAuthorizeItemScreen = function () {
			self.success= '';
			if(self.isEmpty(self.selectedStore) || self.isEmpty(self.selectedStore.id) || self.selectedStore.id.length == 0 || self.selectedStore.id == 0){
				self.error = STORE_REQUIRED_MSG;
				return false;
			}
			if(self.isEmpty(self.selectedItem)){
				self.error = SELECT_RECORD_MSG;
				return false;
			}
			self.selectedScreen = AUTHORIZE_ITEM;
			self.error = '';
		};
		/**
		 * Check empty value.
		 *
		 * @param val the value to check.
		 * @returns {boolean} true it is empty, or not.
		 */
		self.isEmpty = function(val){
			if (val === undefined || val === null ||  val === "") {
				return true;
			}
			return false;
		};
		/**
		 * Helper function to determine if a user has edit access for authorize item.
		 *
		 * @returns {boolean}
		 */
		self.canEditAuthorizeItem = function(){
			return permissionsService.getPermissions(RESOURCE_AUTHORIZE_ITEM, PERMISSION_EDIT);
		};
	}
})();
