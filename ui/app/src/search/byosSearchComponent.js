/*
 * byosSearchComponent.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

(function() {
	angular.module('productMaintenanceUiApp').component('byosSearch', {
		// isolated scope binding
		require: {

		},
		bindings: {
			init: '<',
			onUpdate: '&'
		},
		// Inline template which is bound to message variable in the component controller
		templateUrl: 'src/search/byosSearch.html',

		// The controller that handles our component logic
		controller: ByosSearchController
	});

	ByosSearchController.$inject = ['ProductSearchCriteriaService', '$rootScope', '$scope', 'ProductSearchApi', 'ProductInfoApi'];

	function ByosSearchController(productSearchCriteriaService, $rootScope, $scope, productSearchApi, productInfoApi) {

			var self = this;

			// init flag
			self.init="";

			// Operators
			self.NO_SELECTION = 4;
			self.EQUAL = 1;
			self.GT_LT = 2;
			self.GT_LT_EQUAL = 3;

			// Values for greater than/less than/equal
			self.GTL_EQUAL = 0;
			self.GREATER_THAN = 1;
			self.LESS_THAN = 2;
			self.ONE_WEEK = 3;
			self.BETWEEN = 4;

			// Values for different response types
			self.YES_NO = 0;
			self.DATE = 1;
			self.STRING = 3;
			self.INTEGER = 4;
			self.WORKFLOW = 5;
			self.TAX_CATEGORY = 6;
			self.TAG_TYPE = 9;
			self.TAX_QUALIFYING_CONDITION = 7;
			self.FULFILMENT_CHANNEL = 8;

			// Valid values for workflow.
			self.APPROVED = "A    ";
			self.PENDING = "S    ";
			self.REJECTED = "R    ";

			// An array for the workflow dropdown
			self.workflowOptions =[
				{id: self.PENDING, description: 'Submitted'},
				{id: self.APPROVED, description: 'Approved'},
				{id: self.REJECTED, description: 'Rejected'}
			];
			self.responseWorkflow = self.workflowOptions[0];


			// Valid Values for Yes and No
			self.YES = 1;
			self.NO = 0;

			// An array for Yes or No so it can be in a dropdown.
			self.yesNoOptions = [
				{id: self.YES, description: 'Yes'},
				{id: self.NO, description: 'No'}
			];

			// Automatically pick yes
			self.responseYesNo = self.yesNoOptions[0];

			// Stuff for date responses.
			self.datePickerOptions = {};
			self.effectiveDatePickerOpened = false;
			self.endDatePickerOpened = false;
			self.responseDate = new Date();
			self.responseEndDate = new Date();

			// Integer responses
			self.responseInteger = null;

			// String responses
			self.responseString = null;

			// Tax category responses
			self.taxCategories = null;
			self.responseTaxCategory = null;
			self.responseTagType = null;

			// Tax qualifying condition responses
			self.qualifyingConditions = null;
			self.responseQualifyingCondition = null;

			// Fulfilment channel responses
			self.fulfilmentChannels = null;
			self.responseFulfilmentChannel = null;

			// Valid operators for dates
			self.ltGtEqOperands = [
				{id: self.GTL_EQUAL, description: 'Equal (=)', shortDescription: '='},
				{id: self.GREATER_THAN, description: 'Later Than (>=)', shortDescription: '>='},
				{id: self.LESS_THAN, description: 'Earlier Than (<=)', shortDescription: '<='},
				{id: self.ONE_WEEK, description: 'Last Week', shortDescription: 'Last Week'},
				{id: self.BETWEEN, description: 'Between', shortDescription: 'Between'}
			];
			self.ltGtEqOperand = self.ltGtEqOperands[0];

			// The list of types for build your own search. The type values should match the constant defined in the
			//CustomSearchEntry class.
			self.searchTypes = [
				{type: -1, name: ''},
				{type: 9, name: '3rd Party Sellable', operator: self.EQUAL, responseType: self.YES_NO, responseLabel: 'Product is 3rd Party Sellable'},
				{type: 4, name: 'Distinctive', operator: self.NO_SELECTION, responseLabel: 'Product is Distinctive'},
				{type: 10, name: 'DSV', operator: self.EQUAL, responseType: self.YES_NO, responseLabel: 'Product is DSV'},
				{type: 15, name: 'eBM', operator: self.EQUAL, responseType: self.STRING, responseLabel: 'OnePass ID'},
				{type: 16, name: 'eCommerce Brand', operator: self.EQUAL, responseType: self.STRING, responseLabel: 'eCommerce Brand is'},
				{type: 19, name: 'Fulfilment Program', operator: self.EQUAL, responseType: self.FULFILMENT_CHANNEL},
				{type: 5, name: 'Go Local', operator: self.NO_SELECTION, responseLabel: 'Product is Go Local'},
				{type: 2, name: 'Item Add Date', operator: self.GT_LT_EQUAL, responseType: self.DATE},
				{type: 14, name: 'Last Published Date', operator: self.GT_LT_EQUAL, responseType: self.DATE},
				{type: 0, name: 'Primo Pick', operator: self.NO_SELECTION, responseLabel: 'Product is a Primo Pick'},
				{type: 1, name: 'Primo Pick Status', operator: self.EQUAL, responseType: self.WORKFLOW, responseLabel: 'Primo Pick Status is'},
				{type: 7, name: 'Product Create Date', operator: self.GT_LT_EQUAL, responseType: self.DATE},
				{type: 11, name: 'Published Status', operator: self.EQUAL, responseType: self.YES_NO, responseLabel: 'Product is Published'},
				{type: 8, name: 'Retail Link', operator: self.EQUAL, responseType: self.INTEGER, responseLabel: 'Retail Link Number'},
				{type: 12, name: 'Self Manufactured', operator: self.EQUAL, responseType: self.YES_NO, responseLabel: 'Product is Self Manufactured'},
				{type: 6, name: 'Select Ingredients', operator: self.NO_SELECTION, responseLabel: 'Product has Select Ingredients'},
				{type: 3, name: 'Service Case Status', operator: self.EQUAL, responseType: self.WORKFLOW, responseLabel: 'Service Case Status is'},
				{type: 17, name: 'Tax Category', operator: self.EQUAL, responseType: self.TAX_CATEGORY},
				{type: 20, name: 'Tag Type', operator: self.EQUAL, responseType: self.TAG_TYPE},
				{type: 18, name: 'Tax Qualifying Condition', operator: self.EQUAL, responseType: self.TAX_QUALIFYING_CONDITION},
				{type: 13, name: 'Totally Texas', operator: self.NO_SELECTION, responseLabel: 'Product is Totally Texas'}
			];

			/**
			 * The type of custom search the user has selected.
			 *
			 * @type {{type, name, operator}|*}
			 */
			self.selectedType = self.searchTypes[0];

		/**
		 * The orginal search Types
		 *
		 * @type Array
		 */
		self.searchTypesOrg = [];

			/**
			 * Initializes the controller.
			 */
			self.$onInit = function() {
				productSearchApi.queryVertexTaxCategories(self.loadVertexTaxCategories, self.fetchError);
				productSearchApi.queryVertexQualifyingConditions(self.loadVertexQualifyingConditions, self.fetchError);
				productSearchApi.queryFulfilmentChannels(self.loadFulfilmentChannels, self.fetchError);
				productInfoApi.queryTagTypes(self.loadTagTypes, self.fetchError);
				self.searchTypesOrg = self.searchTypes;
			};

			self.$onChanges = function() {
				// clear all fields for all possibilities
				self.selectedType = self.searchTypes[0];
				self.ltGtEqOperand = self.ltGtEqOperands[0];
				self.responseYesNo = self.yesNoOptions[0];
				self.datePickerOptions = {};
				self.effectiveDatePickerOpened = false;
				self.endDatePickerOpened = false;
				self.responseDate = new Date();
				self.responseEndDate = new Date();
				self.responseInteger = null;
				self.responseString = null;
				self.responseWorkflow = self.workflowOptions[0];
			};

			/**
			 * Callback for the request for the tax qualifying conditions.
			 *
			 * @param data The tax qualifying conditions.
			 */
			self.loadVertexQualifyingConditions = function(data) {
				self.qualifyingConditions = data;
				self.responseQualifyingCondition = self.qualifyingConditions[0];
			};

			/**
			 * Callback for the request for vertex tax categories.
			 *
			 * @param data The list of vertex tax categories.
			 */
			self.loadVertexTaxCategories = function(data) {
				self.taxCategories = data;
				self.responseTaxCategory = self.taxCategories[0];
			};

			/**
			 * Callback for the request for fulfilment channels.
			 *
			 * @param data The list of fulfilment channels.
			 */
			self.loadFulfilmentChannels = function(data) {
				self.fulfilmentChannels = data;
				self.responseFulfilmentChannel = self.fulfilmentChannels[0];
			};

			/**
			 * Callback for the request for vertex tax categories.
			 *
			 * @param data The list of vertex tax categories.
			 */
			self.loadTagTypes = function(data) {
				self.tagTypes = data;
				self.responseTagType = self.tagTypes[0];
			};

			/**
			 * Callback for when there is an error loading any of the above lists.
			 *
			 * @param error The error that happened.
			 */
			self.fetchError = function(error) {
				if (error && error.data) {
					if (error.data.message) {
						console.log(error.data.message);
					} else if(error.data.error){
						console.log(error.data.error);
					}else {
						console.log(error);
					}
				}
				else {
					console.log("An unknown error occurred.");
				}
			};

			/**
			 * Toggles the open status of the effective date picker.
			 */
			self.openEffectiveDatePicker = function() {
				self.effectiveDatePickerOpened = !self.effectiveDatePickerOpened;
			};

			/**
			 * Toggles the open status of the end date picker.
			 */
			self.openEndDatePicker = function() {
				self.endDatePickerOpened = !self.endDatePickerOpened;
			};

			/**
			 * Returns whether or not the user has selected enough information in order to perform the search.
			 *
			 * @returns {boolean}
			 */
			self.userCanSearch = function() {
				if (self.selectedType.operator === self.NO_SELECTION) {
					return true;
				}
				switch (self.selectedType.responseType) {
					case self.YES_NO:
						return self.responseYesNo != null;
					case self.STRING:
						return self.responseString != null && self.responseString.length > 0;
					case self.FULFILMENT_CHANNEL:
						return self.responseFulfilmentChannel != null;
					case self.DATE:
						if (self.ltGtEqOperand.id === self.BETWEEN) {
							return self.responseDate != null && self.responseEndDate != null;
						} else {
							return self.responseDate != null;
						}
					case self.WORKFLOW:
						return self.responseWorkflow != null;
					case self.INTEGER:
						return self.responseInteger != null;
					case self.TAX_CATEGORY:
						return self.responseTaxCategory != null;
					case self.TAG_TYPE:
						return self.responseTagType != null;
					case self.TAX_QUALIFYING_CONDITION:
						return self.responseQualifyingCondition != null;
				}
				return false;
			};

			/**
			 * Initiates the search. It delegates the work back to the productSearchCriteriaComponent.
			 */
			self.search = function() {
				productSearchCriteriaService.setSearchType(productSearchCriteriaService.BYOS_SEARCH);
				productSearchCriteriaService.setByosObject(self.getCustomSearchObject());

				var criteria = productSearchCriteriaService.getBasicSearchObject();

				// call parent component with changes
				self.onUpdate({searchCriteria: criteria});
			};

			/**
			 * Adds a BYOS object to the complex search.
			 */
			self.addComplexSearch = function() {
				var searchObject = self.getCustomSearchObject();
				productSearchCriteriaService.addByosObject(searchObject);
			};

			/**
			 * Builds the custom search object to pass to the backend.
			 * @returns {*}
			 */
			self.getCustomSearchObject = function() {

				if (self.selectedType.responseType == null) {
					return {type: self.selectedType.type,
						description: self.selectedType.name};
				}

				if (self.selectedType.responseType === self.WORKFLOW) {
					return {type: self.selectedType.type,
						stringComparator: self.responseWorkflow.id,
						description: self.selectedType.name + ': ' + self.responseWorkflow.description};
				}

				if (self.selectedType.responseType === self.YES_NO) {
					return {type: self.selectedType.type,
						booleanComparator: self.responseYesNo.id ,
						description: self.selectedType.name + ': '  + self.responseYesNo.description
					};
				}

				if (self.selectedType.responseType === self.INTEGER) {
					return {type: self.selectedType.type,
						integerComparator: self.responseInteger,
						description: self.selectedType.name + ': ' + self.responseInteger
					}
				}

				if (self.selectedType.responseType === self.DATE) {

					var startDate = $rootScope.convertDateWithFullYear(self.responseDate);
					var endDate = $rootScope.convertDateWithFullYear(self.responseEndDate);

					var message = "";
					switch (self.ltGtEqOperand.id) {
						case self.BETWEEN:
							message = self.selectedType.name + self.ltGtEqOperand.shortDescription + startDate + " and " + endDate;
							break;
						case self.ONE_WEEK:
							message = self.selectedType.name + " since last week";
							break;
						default:
							message = self.selectedType.name + self.ltGtEqOperand.shortDescription + startDate
					}

					return {type: self.selectedType.type,
						operator: self.ltGtEqOperand.id,
						dateComparator: startDate,
						endDateComparator: endDate,
						description: message};
				}

				if (self.selectedType.responseType === self.STRING) {
					return {type: self.selectedType.type,
						stringComparator: self.responseString,
						description: self.selectedType.name + ': ' + self.responseString
					}
				}

				if (self.selectedType.responseType === self.TAX_CATEGORY) {
					return {type: self.selectedType.type,
						taxCategory: self.responseTaxCategory,
						description: self.selectedType.name + ': ' + self.responseTaxCategory.displayName
					}
				}

				if (self.selectedType.responseType == self.TAG_TYPE) {
					return {type: self.selectedType.type,
						tagType: self.responseTagType,
						description: self.selectedType.name + ': ' + self.responseTagType.tagTypeDescription
					}
				}

				if (self.selectedType.responseType == self.TAX_QUALIFYING_CONDITION) {
					return {type: self.selectedType.type,
						taxCategory: self.responseQualifyingCondition,
						description: self.selectedType.name + ': ' + self.responseQualifyingCondition.displayName
					}
				}

				if (self.selectedType.responseType === self.FULFILMENT_CHANNEL) {
					return {type: self.selectedType.type,
						fulfillmentChannel: self.responseFulfilmentChannel,
						description: self.selectedType.name + ': ' + self.responseFulfilmentChannel.displayName
					}
				}
			}

		/**
		 * Auto filter for search field
		 * @param search
		 */
		self.autoFilterForSearchField = function(search){
			var output=[];
			angular.forEach(self.searchTypesOrg , function (element) {
				if(element.name.toLowerCase().indexOf(search.toLowerCase())>=0){
					output.push(element);
				}
			});
			self.searchTypes = output;

		}
			/**
		 	* Listen the reset product search event.
		 	*/
			$scope.$on('resetProductSearch', function () {
				self.$onChanges();
			});
			/**
			 * Listen the reset byos criteria.
			 */
			$scope.$on('resetBYOSCriteria', function() {
				self.selectedType = self.searchTypes[0];
			});
		}
	}
)();
