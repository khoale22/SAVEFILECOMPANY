/*
 *   AuthorizeItemComponent.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * The controller for the AuthorizeItem.
 *
 * @author vn70529
 * @since 2.22.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('authorizeItemComponent', {
		templateUrl: 'src/authorization/authorizeItem/authorizeItem.html',
		bindings: {
			selectedStore:'<',
			selectedScreen: '=',
	 		selectedItem:'=',
			flexWeightSwitch:'<',
			upc:'<',
			success:'=',
			error:'=',
			handleError:'&',
			onAuthorizeItemSuccess:'&'
	},
		controller: AuthorizeItemController
	});
	AuthorizeItemController.$inject = ['AuthorizationApi'];
	/**
	 * AuthorizeItem controller definition.
	 * @param authorizationApi
	 * @constructor
	 */
	function AuthorizeItemController(authorizationApi) {
		var self = this;
		const RETAIL_REQUIRED_MSG = "Please enter retail.";
		const FOR_REQUIRED_MSG = 'Please enter "For $"';
		const FOR_CAN_NOT_LARGER_THAN_MSG = '"For $" cannot be larger than 9999.99';
		const ITEM_SEARCH = {title:'Item Search', id:'itemSearch'};
		const DSD = "DSD";
		self.confirmTitle = 'Confirmation';
		self.confirmMessage = '';
		self.isWaiting = false;
		self.itemSizeUOMs = [];
		self.selectedAuthorizeItem;
		/**
		 * call when initial.
		 */
		this.$onInit = function () {
			// Load authorize item info to show on authorize item screen
			self.initAuthorizeItem();
		};
		/**
		 * Show authorize item info on authorize item screen.
		 */
		self.initAuthorizeItem = function(){
			self.error = '';
			self.selectedAuthorizeItem = angular.copy(self.selectedItem);
			self.selectedAuthorizeItem.upc = self.upc;
			var unitCost = 0;
			var caseListCost = 0;
			//Calculate the unit cost & list cost (vend_list_cst) in case: pack quantity (VEND_PK_QTY) != 0
			if (self.selectedAuthorizeItem.packQuantity != 0) {
				// unit cost = list cost (vend_list_cst) / pack quantity (VEND_PK_QTY).
				if(self.selectedAuthorizeItem.packQuantity > 0) {
					unitCost = parseFloat(self.selectedAuthorizeItem.listCost) / parseFloat(self.selectedAuthorizeItem.packQuantity);
					unitCost = parseFloat(unitCost).toFixed(2);
				}
				// case list cost = pack quantity * unit cost.
				caseListCost = parseFloat(self.selectedAuthorizeItem.packQuantity) * unitCost;
				unitCost = self.parseDecimal(unitCost, 2);
				caseListCost = self.parseDecimal(caseListCost, 2);
			}
			self.selectedAuthorizeItem.unitCost = unitCost==0?parseFloat(unitCost).toFixed(1):unitCost;
			self.selectedAuthorizeItem.masterPack = self.selectedAuthorizeItem.packQuantity;
			self.selectedAuthorizeItem.caseListCost = self.getDisplayCaseListCost(caseListCost);
			self.selectedAuthorizeItem.retail = 0;
			self.selectedAuthorizeItem.retailFor = parseFloat(0).toFixed(1);
		};
		/**
		 * Validate authorize item info before submit authorize item.
		 * @return {boolean} true authorize item is valid.
		 */
		self.isValidAuthorizeItem = function(){
			self.error = '';
			var retailFor = self.parseDecimal(self.selectedAuthorizeItem.retailFor, 2);
			if (self.parseDecimal(self.selectedAuthorizeItem.retail, 0) <= 0) {
				self.error = RETAIL_REQUIRED_MSG;
				return false;
			}
			if (retailFor <= 0) {
				self.error = FOR_REQUIRED_MSG;
				return false;
			}
			if(retailFor > 9999.99){
				self.error = FOR_CAN_NOT_LARGER_THAN_MSG;
				return false;
			}
			return true;
		};
		/**
		 * This method will be called when uer click on submit button on screen.
		 */
		self.submitAuthorizeItem = function(){
			if(!self.isValidAuthorizeItem()){
				return;
			}
			// do submit
			self.showAuthorizeItemConfirmation('The retail for the item is '+ self.selectedAuthorizeItem.retail +' for $'+self.selectedAuthorizeItem.retailFor +'.  Is this ok?');
		};
		/**
		 * This method will be called when submit button has been clicked that authorize item info is valid.
		 */
		self.doAuthorizeItem = function(){
			self.isWaiting = true;
			var authorizeItem = angular.copy(self.selectedAuthorizeItem);
			if(self.isEmpty(authorizeItem.itemId)){
				authorizeItem.itemId = authorizeItem.upc;
			}
			authorizeItem.store = angular.copy(self.selectedStore.id);
			// item size = itemMaster.ITM_SZ_QTY + ITM_SZ_UOM_CD
			authorizeItem.itemSize = self.selectedAuthorizeItem.itemSizeQuantity + '' + self.selectedAuthorizeItem.itemSizeUom;
			authorizationApi.submitAuthorizeItem(authorizeItem,
				function (result) {
					self.onAuthorizeItemSuccess({result:result});
					self.isWaiting = false;
				},
				function (error) {
					self.handleError({error:error});
					self.isWaiting = false;
				}
			);
			$('#confirmModal').modal("hide");
		};
		/**
		 * Show authorize item confirmation popup.
		 * @param message the message to show.
		 */
		self.showAuthorizeItemConfirmation = function(message){
			self.confirmTitle = 'Confirmation';
			self.confirmMessage = message;
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
		};
		/**
		 * This method will be called when user clicked on yes button of authorize item confirmation popup.
		 */
		self.confirmYesActionHandle = function(){
			self.doAuthorizeItem();
		};
		/**
		 * Get unit of measure for displaying on text size.
		 * @returns {string} unit of measure.
		 */
		self.getDisplayUOMForSize = function(){
			var itemSizeUomCd = '';
			if(!self.isEmpty(self.selectedAuthorizeItem.unitOfMeasure) && !self.isEmpty(self.selectedAuthorizeItem.unitOfMeasure.itemSizeUomCd.trim())) {
				itemSizeUomCd = self.selectedAuthorizeItem.unitOfMeasure.itemSizeUomCd;
			}
			return itemSizeUomCd;
		};
		/**
		 * Parse string to decimal with decimal digits.
		 * If parse to float is Nan, then use parseInt for it.
		 * @param val the value to parse
		 * @param decimalDigits the number of decimal digits.
		 * @returns {*}
		 */
		self.parseDecimal = function(val, decimalDigits){
			if(self.isEmpty(val)){
				return 0;
			}
			if(isNaN(parseFloat(val))){
				return parseFloat(parseInt(val)).toFixed(1);
			}
			return parseFloat(val).toFixed(decimalDigits);
		};
		/**
		 * Show item search screen.
		 */
		self.showItemSearchScreen = function(){
			self.selectedScreen = ITEM_SEARCH;
			self.selectedItem = null;
			self.error = '';
			self.success = '';
		};
		/**
		 * Check the type of item.
		 * @returns {boolean} return true If itemType  == 'DSD', or false.
		 */
		self.isDSDItem = function(){
			return (!self.isEmpty(self.selectedAuthorizeItem.itemType) && DSD == self.selectedAuthorizeItem.itemType.trim());
		};
		/**
		 * Check empty value.
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
		 * Correct retail value when retail text box out focus.
		 */
		self.correctRetail = function(){
			if(self.selectedAuthorizeItem.retail.length > 0)
				self.selectedAuthorizeItem.retail= parseInt(self.selectedAuthorizeItem.retail)
		};

		/**
		 * Returns case list cost for show on case list cost text box.
		 * @param caseListCost the case list cost value.
		 */
		self.getDisplayCaseListCost = function(caseListCost){
			if(caseListCost == 0){
				// Case: show case list cost 0.0
				return parseFloat(caseListCost).toFixed(1);
			}
			var caseListCostSplit = caseListCost.split('.');

			if(caseListCostSplit.length > 1 && _.endsWith(caseListCostSplit[1], '0')){
				// Case: if length of decimal places = 2 and ends with = 0, then remove that zero.
				// EX: 12.80 -> display: 12.8
				return parseFloat(caseListCost).toFixed(1);
			}
			return caseListCost;
		}
	}
})();
