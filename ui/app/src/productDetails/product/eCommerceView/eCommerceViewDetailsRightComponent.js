/*
 *   eCommerceViewDetailsRightComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Product -> eCommerce View page component -> general details -> left panel.
 *
 * @author vn70516
 * @since 2.0.14
 */
(function () {

	angular.module('productMaintenanceUiApp').component('eCommerceViewDetailsRight', {
		bindings: {
			currentTab: '<',
			productMaster: '<',
			eCommerceViewDetails:'='
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewDetailsRight.html',
		// The controller that handles our component logic
		controller: ECommerceViewDetailsRightController
	});

	ECommerceViewDetailsRightController.$inject = ['ECommerceViewApi', '$scope', '$timeout', 'ngTableParams', '$filter','$rootScope'];


	function ECommerceViewDetailsRightController(eCommerceViewApi, $scope, $timeout, ngTableParams, $filter,$rootScope) {

		var self = this;
		/**
		 * Reload eCommerce view key.
		 * @type {string}
		 */
		self.RELOAD_ECOMMERCE_VIEW = 'reloadECommerceView';
		/**
		 * The list of pdp template.
		 * @type {Array}
		 */
		self.pdpTemplates = [];
		/**
		 * Constant order by asc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_ASC = "asc";
		/**
		 * Constant order by desc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_DESC = "desc";
		/**
		 * current date.
		 * @type Date
		 */
		self.currentDate = new Date();
		/**
		 * Constant when start date invalid.
		 *
		 * @type {String}
		 */
		const START_DATE_MANDATORY = 'Start Date is mandatory.';
		/**
		 * Constant when start date invalid.
		 *
		 * @type {String}
		 */
		const START_GREATER_OR_EQUAL_CUR = 'Start Date must be greater than or equal to Current Date.';
		/**
		 * Constant when start date invalid.
		 *
		 * @type {String}
		 */
		const START_LESS_END = 'Start Date must be less than End Date.';
		/**
		 * Constant for END_DATE_MANDATORY.
		 * @type {String}
		 */
		const END_DATE_MANDATORY = 'End Date is mandatory';
		/**
		 * Constant when end date invalid.
		 *
		 * @type {String}
		 */
		const END_GREATER_CUR_AND_LESS_MAX = 'End Date must be greater than Current Date and less than 12/31/9999.';
		/**
		 * Constant when end date invalid.
		 *
		 * @type {String}
		 */
		const END_GREATER_START_AND_LESS_MAX = 'End Date must be greater than Start Date and less than 12/31/9999.';
		/**
		 * Constant for empty string.
		 * @type {String}
		 */
		const EMPTY = '';
		/**
		 * Constant for EFFECTIVE_DATE_MANDATORY.
		 * @type {String}
		 */
		const EFFECTIVE_DATE_MANDATORY = 'Effective Date is mandatory';
		/**
		 * Constant for EFFECTIVE_DATE_GREATER_OR_EQUAL_CURRENT_DATE.
		 * @type {String}
		 */
		const EFFECTIVE_DATE_GREATER_OR_EQUAL_CURRENT_DATE = 'Effective Date must be greater than Current Date';
		/**
		 * Constant for EFFECTIVE_DATE_LESS_OR_EQUAL_END_DATE.
		 * @type {String}
		 */
		const EFFECTIVE_DATE_LESS_OR_EQUAL_END_DATE = 'Effective Date must be less than or equal to End Date';
		/**
		 * Constant for END_DATE_GREATER_CURRENT_DATE.
		 * @type {String}
		 */
		const END_DATE_GREATER_CURRENT_DATE = 'End Date must be greater than Current Date';
		/**
		 * Constant for RED.
		 * @type {String}
		 */
		const RED = 'red';
        /**
         * Constant for Heb.com tab.
         *
         * @type {String}
         */
        const HEB_DOT_COM_TAB = "HebCom";
        /**
         * update reason description.
         * @type {String}
         */
        self.updateReasonDescription = null;

        /**
         * Code table of update reasons filter option.
         * @type {[*]}
         */
        const UPDATE_REASONS = [
            {code:'1664', desc:'Display name'},
            {code:'1666', desc:'Romance copy'},
            {code:'1667', desc:'Size'},
            {code:'1784', desc:'Dimensions'},
            {code:'1672', desc:'Brand'},
            {code:'1674', desc:'Ingredients'},
            {code:'1676', desc:'Directions'},
            {code:'1677', desc:'Warnings'},
            {code:'1679', desc:'Nutrition'},
            {code:'1678', desc:'Image Update'},
            {code:'1727', desc:'Primary UPC Update'}
        ];
		/**
		 * Initialize the controller.
		 */

		self.hasChangeShowOnSite = false;

		self.hasChangeSubEligible = false;

		self.firstCheck = true;

		self.pdpTemplateIdOrg = null;

		this.$onInit = function () {
			self.loadInitData();
		};

		/**
		 * Component will reload the kits data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
			self.eCommerceViewDetails.currentProductFulfillment = null;
			self.getAlertStaging();
		};

		self.loadInitData = function () {
			eCommerceViewApi.findAllPDPTemplate(
				//success
				function (results) {
					self.pdpTemplates = angular.copy(results);
				}
				,self.handleError
			);
		}
		/**
		 * Get alert staging.
		 */
		self.getAlertStaging = function () {
			self.updateReasonDescription = null;
			if(self.currentTab && self.currentTab.id && self.currentTab.id == HEB_DOT_COM_TAB){
				eCommerceViewApi.findAlertStagingByProductId({
					productId: self.productMaster.prodId
					},
					function (results) {
						if (results != null && results.length > 0) {
							self.updateReasonDescription =  self.displayUpdateReasonAttributes(results[0].alertDataTxt);
						}
						if(self.updateReasonDescription != null){
							self.updateReasonDescription = self.updateReasonDescription +".";
						}
					},
					//error
					self.handleError
				);
			}
		};
		/**
		 * Get display name of Update Reason Attributes.
		 * @param attributesCSV
		 * @returns {string}
		 */
		self.displayUpdateReasonAttributes = function (attributesCSV) {
			var name = [];
			if(attributesCSV) {
				var attributesArray = attributesCSV.split(',');
				_.forEach(attributesArray, function (attr) {
					var reason = _.find(UPDATE_REASONS, function(o) { return o.code == parseInt(attr, 10);});
					if(reason) {
						var index = _.findIndex(name, function(o) { return o == reason.desc;});
						if (!(index >= 0)) {name.push(reason.desc);}
					}
				});
			}
			return name.join(", ");
		};
		/**
		 * When click check box for show on site
		 */
		self.changeCheckShowOnSite = function () {
			self.eCommerceViewDetails.startDateErrorMgs = EMPTY;
			self.eCommerceViewDetails.isStartDateErrorMgs = false;
			self.eCommerceViewDetails.startEndErrorMgs = EMPTY;
			self.eCommerceViewDetails.isEndDateErrorMgs = false;
			self.hasChangeShowOnSite= !self.hasChangeShowOnSite;
			if (self.hasChangeShowOnSite){
				$rootScope.contentChangedFlag = true;
			}
			else {
				$rootScope.contentChangedFlag = false;
			}
			if(self.eCommerceViewDetails.showOnSite) {
				if (self.eCommerceViewDetails.productOnline) {
					if(self.eCommerceViewDetails.showOnSiteEndDateOrg !== null && self.eCommerceViewDetails.showOnSiteEndDateOrg !== null){
					self.eCommerceViewDetails.showOnSiteEndDate = new Date(self.eCommerceViewDetails.showOnSiteEndDateOrg.replace(/-/g, '\/'));
					}
					if(self.eCommerceViewDetails.showOnSiteStartDateOrg !== null){
					self.eCommerceViewDetails.showOnSiteStartDate = new Date(self.eCommerceViewDetails.showOnSiteStartDateOrg.replace(/-/g, '\/'));
				}
				}
				else {
					var todayDate = new Date();
					//console.log(new Date().setDate(todayDate.getDate()+1));
					self.eCommerceViewDetails.showOnSiteStartDate = new Date().setDate(todayDate.getDate() + 1);
					self.eCommerceViewDetails.showOnSiteEndDate = new Date("9999-12-31");
				}
			}
			else{

				if (!self.eCommerceViewDetails.productOnline) {
					self.eCommerceViewDetails.showOnSiteStartDate = null;
					self.eCommerceViewDetails.showOnSiteEndDate = null;
				}
			}
		};

		/**
		 * When click open date picker for fulfillment, store current status for date picker.
		 */
		self.openDatePickerForFulfillment = function (productFullfilment, startDate) {
			angular.forEach(self.eCommerceViewDetails.productFullfilmentChanels, function (value) {
				if (angular.toJson(value) == angular.toJson(productFullfilment)) {
					if (startDate) {
						value.effectDateOpen = true;
					} else {
						value.expirationDateOpen = true;
					}
				}
			});
			self.options = {
				minDate: new Date(),
				maxDate: new Date("9999-12-31")
			};
		};

		/**
		 * When user enter invalid data, set effectDate is null .
		 */
		self.changeEffectDate = function (event, status) {
			if(status.$dirty){
				event.productFulfillment.effectDateErrorCss = null;
				event.productFulfillment.effectDateErrorMsg = null;
				if(status.$invalid){
					event.productFulfillment.effectDate = null;
					event.productFulfillment.effectDateErrorCss = RED;
					event.productFulfillment.effectDateErrorMsg = EFFECTIVE_DATE_MANDATORY;
				}else{
					if (event.productFulfillment.effectDate == null) {
						event.productFulfillment.effectDateErrorCss = RED;
						event.productFulfillment.effectDateErrorMsg = EFFECTIVE_DATE_MANDATORY;
					}else if (!self.isDate1GreaterThanDate2(event.productFulfillment.effectDate, new Date())) {
						event.productFulfillment.effectDateErrorCss = RED;
						event.productFulfillment.effectDateErrorMsg = EFFECTIVE_DATE_GREATER_OR_EQUAL_CURRENT_DATE;
					}else if (!self.isDate1GreaterThanOrEqualToDate2(event.productFulfillment.expirationDate, event.productFulfillment.effectDate)) {
						event.productFulfillment.effectDateErrorCss = RED;
						event.productFulfillment.effectDateErrorMsg = EFFECTIVE_DATE_LESS_OR_EQUAL_END_DATE;
					}
				}
			}
		};

		/**
		 * When user enter invalid data, set expirationDate is null .
		 */
		self.changeExpirationDate = function (event, status) {
			if(status.$dirty){
				event.productFulfillment.expirationDateErrorCss = null;
				event.productFulfillment.expirationDateErrorMsg = null;
				if(status.$invalid){
					event.productFulfillment.expirationDate = null;
					event.productFulfillment.expirationDateErrorCss = RED;
					event.productFulfillment.expirationDateErrorMsg = END_DATE_MANDATORY;
				}else{
					if (event.productFulfillment.expirationDate == null) {
						event.productFulfillment.expirationDateErrorCss = RED;
						event.productFulfillment.expirationDateErrorMsg = END_DATE_MANDATORY;
					}else if (!self.isDate1GreaterThanDate2(event.productFulfillment.expirationDate, new Date())) {
						event.productFulfillment.expirationDateErrorCss = RED;
						event.productFulfillment.expirationDateErrorMsg = END_DATE_GREATER_CURRENT_DATE;
					}else if (!self.isDate1GreaterThanOrEqualToDate2(event.productFulfillment.expirationDate, event.productFulfillment.effectDate)) {
						event.productFulfillment.expirationDateErrorCss = RED;
						event.productFulfillment.expirationDateErrorMsg = EFFECTIVE_DATE_LESS_OR_EQUAL_END_DATE;
					}
				}
			}
		};

		/**
		 * When click open date picker for anther attribute, store current status for date picker.
		 */
		self.openDatePicker = function (fieldName) {
			switch (fieldName){
				case "subscriptionEndDate":
					self.eCommerceViewDetails.subscriptionEndDateOpen = true;
					break;
				case "subscriptionStartDate":
					self.eCommerceViewDetails.subscriptionStartDateOpen = true;
					break;
				case "showOnSiteStartDate":
					self.eCommerceViewDetails.showOnSiteStartDateOpen = true;
					break;
				case "showOnSiteEndDate":
					self.eCommerceViewDetails.showOnSiteEndDateOpen = true;
					break;
				default:
					break;
			}
			self.options = {
				minDate: new Date(),
				maxDate: new Date("9999-12-31")
			};
		};

		/**
		 * When user enter invalid data, set start date show on site is null .
		 */
		self.changeShowOnSiteStartDate = function (event, invalid) {

			if(invalid){
				self.eCommerceViewDetails.showOnSiteStartDate = null;
			}
			if(self.eCommerceViewDetails.showOnSiteStartDate === null){
				self.eCommerceViewDetails.startDateErrorMgs = START_DATE_MANDATORY;
				self.eCommerceViewDetails.isStartDateErrorMgs = true;
			}else{
			var newStartDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDate);
			var oldStartDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDateOrg);
			var newEndDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDate);
			var oldEndDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDateOrg);
			if (oldStartDate !== newStartDate || newEndDate !== oldEndDate) {
				$rootScope.contentChangedFlag = true;
				self.validateShowOnSiteDate();
			}
			}
		};
		/**
		 * When user enter invalid data, set end date show on site is null.
		 */
		self.changeShowOnSiteEndDate = function (event, invalid) {

			if(invalid){
				self.eCommerceViewDetails.showOnSiteEndDate = null;
			}
			if(self.eCommerceViewDetails.showOnSiteEndDate === null){
				self.eCommerceViewDetails.endDateErrorMgs = END_DATE_MANDATORY;
				self.eCommerceViewDetails.isEndDateErrorMgs = true;
			}else{
			var newStartDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDate);
			var oldStartDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDateOrg);
			var newEndDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDate);
			var oldEndDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDateOrg);
			if (oldStartDate !== newStartDate || newEndDate !== oldEndDate) {
					$rootScope.contentChangedFlag = true;
					self.validateShowOnSiteDate();
			}
			}
		};

		/**
		 * validate when user change on show on site date
		 */
		self.validateShowOnSiteDate = function () {
			//Start date
				if (!self.isDate1GreaterThanOrEqualToDate2(self.eCommerceViewDetails.showOnSiteStartDate, new Date())) {
				self.eCommerceViewDetails.startDateErrorMgs = START_GREATER_OR_EQUAL_CUR;
				self.eCommerceViewDetails.isStartDateErrorMgs = true;
			}
			else if(!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, self.eCommerceViewDetails.showOnSiteStartDate)) {
				if(self.eCommerceViewDetails.showOnSiteEndDate !== null){
					self.eCommerceViewDetails.startDateErrorMgs = START_LESS_END;
					self.eCommerceViewDetails.isStartDateErrorMgs = true;
				}else{
					self.eCommerceViewDetails.startDateErrorMgs = EMPTY;
					self.eCommerceViewDetails.isStartDateErrorMgs = false;
				}
			}

			else{
				self.eCommerceViewDetails.startDateErrorMgs = EMPTY;
				self.eCommerceViewDetails.isStartDateErrorMgs = false;
			}
			//End date
			if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, new Date())) {
				self.eCommerceViewDetails.endDateErrorMgs = END_GREATER_CUR_AND_LESS_MAX;
				self.eCommerceViewDetails.isEndDateErrorMgs = true;
			}
			else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, self.eCommerceViewDetails.showOnSiteStartDate)) {
				self.eCommerceViewDetails.endDateErrorMgs = END_GREATER_START_AND_LESS_MAX;
				self.eCommerceViewDetails.isEndDateErrorMgs = true;
			}
			else{
				self.eCommerceViewDetails.endDateErrorMgs = EMPTY;
				self.eCommerceViewDetails.isEndDateErrorMgs = false;
			}

		};
		/**
		 * Compare date1 greate than or equal to date2.
		 *
		 * @param date1 the date.
		 * @param date2 the date.
		 * @returns {boolean} true if the date is greater than or equal to the date 2.
		 */
		self.isDate1GreaterThanOrEqualToDate2 = function (date1, date2) {
			if ((new Date(self.convertDateToStringWithYYYYMMDD(date1)).getTime() >= new Date(self.convertDateToStringWithYYYYMMDD(date2)).getTime())) {
				return true;
			}
			return false;
		};
		/**
		 * Compare the date is greater than date 2 or not.
		 *
		 * @param date1 the date.
		 * @param date2 the date.
		 * @returns {boolean} true if the date1 is greater than date 2 or false.
		 */
		self.isDate1GreaterThanDate2 = function (date1, date2) {
			if ((new Date(self.convertDateToStringWithYYYYMMDD(date1)).getTime() > new Date(self.convertDateToStringWithYYYYMMDD(date2)).getTime())) {
				return true;
			}
			return false;
		};

		/**
		 * Convert the date to string with format: yyyy-MM-dd.
		 * @param date the date object.
		 * @returns {*} string
		 */
		self.convertDateToStringWithYYYYMMDD = function (date) {
			return $filter('date')(date, 'yyyy-MM-dd');
		};


		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.handleError = function(error) {
			self.countRequest = 0;
			self.isWaitingForResponse = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = (error.data.message);
				} else if (error.data.error) {
					self.error = (error.data.error);
				} else {
					self.error = error;
				}
			}
			else {
				self.error = "An unknown error occurred.";

			}
		};
		/**
		 * Get sale channel code.
		 */
		self.getSalsChnlCd = function () {
			var salsChnlCd = null;
            if (self.currentTab.saleChannel != null && self.currentTab.saleChannel !== undefined) {
                salsChnlCd = self.currentTab.saleChannel.id;
            } else {
                salsChnlCd = '01 ';
            }
			return salsChnlCd;
		};
		/**
		 * Handle event of apply button.
		 */
		self.apply = function(){
			if(self.eCommerceViewDetails.currentProductFulfillment !== null && self.eCommerceViewDetails.currentProductFulfillment.key !== null){
				if(self.eCommerceViewDetails.currentProductFulfillment.key.fulfillmentChannelCode.trim() === "08"){//Do not display
					var item = {
							salesChanelCode:self.getSalsChnlCd(),
							effectDate:new Date((new Date()).getTime() + (24 * 60 * 60 * 1000)),
							expirationDate:new Date("9999-12-31"),
							actionCode:'A',
							key:{salesChanelCode:self.getSalsChnlCd(),
								fullfillmentChanelCode:self.eCommerceViewDetails.currentProductFulfillment.key.fulfillmentChannelCode.trim()
							},
							fulfillmentChannel:{
								description:self.eCommerceViewDetails.currentProductFulfillment.description
							}
					};
					self.eCommerceViewDetails.productFullfilmentChanels=[];
					self.eCommerceViewDetails.productFullfilmentChanels.push(item);
					self.removeItemFromCurrentProductFulfillment();
				}else{
					var orgItem = self.getProductFullfilmentChanelByFulfillmentChannelCode(self.eCommerceViewDetails.currentProductFulfillment.key.fulfillmentChannelCode);
					if(orgItem === null){
						var productFullfilmentChanel = {
								salesChanelCode:self.getSalsChnlCd(),
								effectDate:new Date((new Date()).getTime() + (24 * 60 * 60 * 1000)),
								expirationDate:new Date("9999-12-31"),
								actionCode:'A',
								key:{salesChanelCode:self.getSalsChnlCd(),
									fullfillmentChanelCode:self.eCommerceViewDetails.currentProductFulfillment.key.fulfillmentChannelCode
								},
								fulfillmentChannel:{
									description:self.eCommerceViewDetails.currentProductFulfillment.description
								}
						};
						if(self.eCommerceViewDetails.productFullfilmentChanels ==null){
							self.eCommerceViewDetails.productFullfilmentChanels=[];
						}
						self.eCommerceViewDetails.productFullfilmentChanels.push(productFullfilmentChanel);
						self.removeItemFromCurrentProductFulfillment();
					}else{
						if(self.eCommerceViewDetails.productFullfilmentChanels ==null){
							self.eCommerceViewDetails.productFullfilmentChanels=[];
						}
						self.eCommerceViewDetails.productFullfilmentChanels.push(orgItem);
						self.removeItemFromCurrentProductFulfillment();
					}
				}
			}
		};
		self.removeItemFromCurrentProductFulfillment = function(){
			$rootScope.contentChangedFlag = true;
			//Combobox
			//self.currentProductFulfillment
			//self.eCommerceViewDetails.fulfillmentChannels
			//Current item list
			//self.eCommerceViewDetails.productFullfilmentChanels

			if(self.eCommerceViewDetails.fulfillmentChannels != null && self.eCommerceViewDetails.fulfillmentChannels.length > 0){
				for(var i = self.eCommerceViewDetails.fulfillmentChannels.length - 1; i >= 0; i--) {
					if(self.eCommerceViewDetails.fulfillmentChannels[i].key.fulfillmentChannelCode === self.eCommerceViewDetails.currentProductFulfillment.key.fulfillmentChannelCode) {
						self.eCommerceViewDetails.fulfillmentChannels.splice(i, 1);
					}
				}
				self.eCommerceViewDetails.currentProductFulfillment = null;
			}
		};
		self.addItemToCurrentProductFulfillment = function(){
			$rootScope.contentChangedFlag = true;
			var fulfillmentChannels = angular.copy(self.eCommerceViewDetails.fulfillmentChannelsOrg);
			if(fulfillmentChannels != null && fulfillmentChannels.length > 0){
				for(var i = fulfillmentChannels.length - 1; i >= 0; i--) {
					angular.forEach(self.eCommerceViewDetails.productFullfilmentChanels, function(item){
						if(fulfillmentChannels[i].key.fulfillmentChannelCode === item.key.fullfillmentChanelCode ) {
							fulfillmentChannels.splice(i, 1);
						}
					});
				}
				self.eCommerceViewDetails.fulfillmentChannels = angular.copy(fulfillmentChannels);
				self.eCommerceViewDetails.currentProductFulfillment = null;
			}
		};
		self.reset = function(){
			$rootScope.contentChangedFlag = false;
			self.eCommerceViewDetails.productFullfilmentChanels = angular.copy(self.eCommerceViewDetails.productFullfilmentChanelsOrg);
			self.eCommerceViewDetails.productFullfilmentChanelsRemoveList=[];
			var fulfillmentChannels = angular.copy(self.eCommerceViewDetails.fulfillmentChannelsOrg);
			if(fulfillmentChannels != null && fulfillmentChannels.length > 0){
				for(var i = fulfillmentChannels.length - 1; i >= 0; i--) {
					angular.forEach(self.eCommerceViewDetails.productFullfilmentChanels, function(item){
						if(fulfillmentChannels[i].key.fulfillmentChannelCode === item.key.fullfillmentChanelCode ) {
							fulfillmentChannels.splice(i, 1);
						}
					});
				}
				self.eCommerceViewDetails.fulfillmentChannels = angular.copy(fulfillmentChannels);
				self.eCommerceViewDetails.currentProductFulfillment = null;
			}
		};
		self.removeProductFullfilmentChanel = function(productFulfillmentChanel){
			if(productFulfillmentChanel.key.fullfillmentChanelCode === "08"){//Do not display
				self.reset();
			}else{
				if(self.eCommerceViewDetails.productFullfilmentChanelsRemoveList == null){
					self.eCommerceViewDetails.productFullfilmentChanelsRemoveList = [];
				}

				var index = self.eCommerceViewDetails.productFullfilmentChanels.indexOf(productFulfillmentChanel);
				if(index !== -1){
					self.eCommerceViewDetails.productFullfilmentChanels.splice(index, 1);
					if(productFulfillmentChanel.actionCode !== 'A') {
						var removeItem = angular.copy(productFulfillmentChanel);
						removeItem.actionCode = 'D';
						self.eCommerceViewDetails.productFullfilmentChanelsRemoveList.push(removeItem);
					}
				}
				self.addItemToCurrentProductFulfillment();
			}
		};
		self.getProductFullfilmentChanelByFulfillmentChannelCode = function (fulfillmentChannelCode) {
			var returnItem = null;
			var productFullfilmentChanelsOrg = angular.copy(self.eCommerceViewDetails.productFullfilmentChanelsOrg);
			angular.forEach(productFullfilmentChanelsOrg, function(item){
				if (item.key.fullfillmentChanelCode !== null && item.key.fullfillmentChanelCode.trim() === fulfillmentChannelCode.trim()) {
					returnItem = item;
				}
			});
			return returnItem;
		};
		/**
		 * Reload ECommerceView.
		 */
		$scope.$on(self.RELOAD_ECOMMERCE_VIEW, function() {
			self.loadInitData();
			self.getAlertStaging();
		});
		/**
		 * Show fulfillment attributes audit information modal
		 */
		// self.showFulfillmentAttributesAuditInfo = function () {
		// 	self.fulfillmentAttributesAuditInfo = eCommerceViewApi.getFulfillmentAttributesAudits;
		// 	var title ="Fulfillment Attributes";
		// 	var parameters = {'prodId' :self.productMaster.prodId};
		// 	FulfillmentAttributeAuditModal.open(self.fulfillmentAttributesAuditInfo, parameters, title);
		// }

		self.showFulfillmentAttributeLog = function(){
			self.isWaitingGetFulfillmentAttributeAudit = true;
			eCommerceViewApi.getFulfillmentAttributesAudits(
				{
					prodId: self.productMaster.prodId
				},
				//success case
				function (results) {
					self.fulfillmentAttributeAudits = results;
					self.initFulfillmentAttributeAuditTable();
					self.isWaitingGetFulfillmentAttributeAudit = false;
				}, self.handleError);

			$('#fulfillment-attribute-log-modal').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * Init data fulfillment attribute Audit.
		 */
		self.initFulfillmentAttributeAuditTable = function () {
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			$scope.fulfillmentAttributeAuditTableParams = new ngTableParams({
				page: 1,
				count: 10,
				sorting: $scope.sorting

			}, {
				counts: [],
				data: self.fulfillmentAttributeAudits
			});
		}
		/**
		 * Change sort.
		 */
		self.changeSort = function (){
			if($scope.sorting.changedOn === ORDER_BY_DESC){
				$scope.sorting.changedOn = ORDER_BY_ASC;
			}else {
				$scope.sorting.changedOn = ORDER_BY_DESC;
			}
		}
		/**
		 * Show eCommerceView error messages.
		 */
		self.showECommerceViewErrorMessages = function(){
			var hasError = false;
			if((self.eCommerceViewDetails.errorMessages != null &&  self.eCommerceViewDetails.errorMessages.length > 0)
				|| (self.eCommerceViewDetails.romanceCopyErrorMessages != null &&  self.eCommerceViewDetails.romanceCopyErrorMessages.length > 0)){
				hasError = true;
			}
			return hasError
		}
		/**
		 * Check change pdp template
		 */
		self.checkChangePdpTemplate= function () {
				$rootScope.contentChangedFlag = true;
		}

		/**
		 * Check change Subscription Eligible
		 */
		self.checkChangeSubEligible = function () {
			self.hasChangeSubEligible = !self.hasChangeSubEligible;
			if(self.hasChangeSubEligible){
				$rootScope.contentChangedFlag = true;
			}
			else{
				$rootScope.contentChangedFlag = false;
			}
		}
	}
})();
