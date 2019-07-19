/*
 *   productAttributesComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 *
 */
'use strict';

/**
 * Component of product Attributes screen
 *
 * @author s573181
 * @since 2.18.4
 */
(function () {
	angular.module('productMaintenanceUiApp').component('productAttributesComponent', {
		// isolated scope binding
		bindings: {
			productType: '<',
			productMaster: '<',
			displayName: '<',
			hasChanges: '&'
		},
		// Inline template which is bound to message variable in the component controller
		templateUrl: 'src/productDetails/product/taxonomy/productAttributesComponent.html',
		// The controller that handles our component logic
		controller: productAttributesController
	});

	productAttributesController.$inject = ['ProductTaxonomyApi', 'taxonomyService', '$filter', '$rootScope'];


	/**
	 * Product attributes component's controller definition.
	 *
	 * @constructor
	 */
	function productAttributesController(productTaxonomyApi, taxonomyService, $filter, $rootScope) {

		var self = this;
		self.waitingForAttributes = false;
		self.DECIMAL_TYPE = "DEC";
		self.DATE_TYPE = "DT";
		self.INTEGER_TYPE = "I";
		self.STRING_TYPE = "S";
		self.TIMESTAMP_TYPE = "TS";
		self.data = [];
		self.originalData = [];
		self.MORE_THAN_ONE_VALUE_WARNING_MESSAGE =
			"Attribute is defined as non-multiple select, but has more than 1 value. Click to view.";
        self.SAVE_DATA_ECOMMERCE = "saveDataEcommerce";

		var UNKNOWN_ERROR_TEXT = "An unknown error occurred.";
		var INTEGER_REGEX_TEMPLATE = "[0-9]{0,$maxLength}";
		var DECIMAL_REGEX_TEMPLATE = "[0-9]{0,$maxLength}([.][0-9]{1,$precision})?";

		// Constant for Case Pack.
		var CASEPACK = 'CASEPACK';
		// Constant for UPC Scan Code.
		var UPC = 'UPC';
		var PRODUCT = 'PROD';
		var SEND_NEW_ATTRIBUTE_VALUE_PRETEXT = 'confirmSendModal';

		/**
		 * Title for confirmSendModal pop up.
		 * @type {string}
		 */
		self.confirmSendTitle = '';

		/**
		 * Message for confirmSendModal pop up.
		 * @type {string}
		 */
		self.confirmSendMessage = '';

		/**
		 * Attribute selected during confirmSendModal popup.
		 * @type {string}
		 */
		self.selectedAttribute = '';

		self.newAttributesModalId = '';
        /**
         * Reload data when change Current Hierarch from eCommerceViewHierarchyAssignmentComponent
         */
        $rootScope.$on(self.SAVE_DATA_ECOMMERCE, function() {
            getAttributes();
        });

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.newAttributesModalId = SEND_NEW_ATTRIBUTE_VALUE_PRETEXT.concat(self.productType);
		};

		/**
		 * Component ngOnChanges lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onChanges = function () {
			getAttributes();
		};

		/**
		 * Get attributes for the product id and product type.
		 */
		function getAttributes() {
			self.waitingForAttributes = true;
			self.data = [];
			var parameters = {};
			parameters.productId = self.productMaster.prodId;
			parameters.productType = self.productType;
			productTaxonomyApi.getByProductIdAndProductType(
				parameters,
				setAttributes,
				function(error){
					fetchError('Error getting attributes by product ID and product type', error);
					self.waitingForAttributes = false;
				}
			)
		}

		/**
		 * Setter for attributes.
		 *
		 * @param results
		 */
		function setAttributes(results){
			self.originalData = results;
			self.data = angular.copy(self.originalData);
			updateDateValues();
			self.updateHasChanges();
			self.waitingForAttributes = false;
		}

		/**
		 * Cause a change.
		 *
		 */

		function updateDateValues() {
			for(var index = 0; index < self.data.length; index++) {
				if(self.data[index].domain === 'DT'
					&& !self.data[index].multipleAllowed && self.data[index].values &&
					self.data[index].values.length > 0) {
					self.data[index].value = getDateOffset(new Date(self.data[index].values[0].date), 1);
				}
				if(self.data[index].domain === 'TS'
					&& !self.data[index].multipleAllowed && self.data[index].values &&
					self.data[index].values.length > 0) {
					self.data[index].value = getDateOffset(new Date(self.data[index].values[0].timestamp), 1);
				}
			}
		}

		/**
		 * Gets a date offset by a certain number of days. This can be used to return 'dayOffset' days from a given
		 * date.
		 *
		 * @param date Date to offset.
		 * @param dayOffset Number of days to offset the given date.
		 * @returns {Date}
		 */
		function getDateOffset(date, dayOffset){
			if(!date || date === null){
				return null;
			}
			if(!dayOffset || dayOffset === null){
				dayOffset = 0;
			}
			var returnDate = date.setDate(date.getDate() + dayOffset);
			return new Date(returnDate);
		}

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param message The pretext message to display to user.
		 * @param error The error from the back end.
		 */
		function fetchError (message, error) {
			self.success = null;
			self.waitingForUpdate = false;
			self.error = message + ':' + getErrorMessage(error);
		}

		/**
		 * Returns error message.
		 *
		 * @param error Error object.
		 * @returns {string} String to set as error text.
		 */
		function getErrorMessage(error) {
			if(error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return UNKNOWN_ERROR_TEXT;
			}
		}

		/**
		 * Helper function to determine if the current level has hierarchy level children.
		 *
		 * @param currentLevel Hierarchy level to compare.
		 * @returns {boolean}
		 */
		self.hasHierarchyLevelChildren = function(currentLevel) {
			return currentLevel.childRelationships != null && currentLevel.childRelationships.length !== 0;
		};

		/**
		 * This method gets an array of display names for the given list of values. If values is null, return null. Else
		 * add display name of each value, then return the list of display names.
		 *
		 * @param values List of values.
		 * @returns {*} List of display names, or null if no values exist.
		 */
		self.getDisplayNamesForValues = function(values){
			if(!values){
				return null;
			}
			var displayNameArray = [];
			for(var index = 0; index < values.length; index++){
				displayNameArray.push(values[index].displayName);
			}
			return displayNameArray.toString();
		};

		/**
		 * Returns true if the product type is CASEPACK or Upc Scan Code.
		 * @returns {boolean}
		 */
		self.isCasePackOrScanCode = function() {
			return self.productType === CASEPACK || self.productType === UPC;
		};
		self.isProductType = function() {
			return self.productType === PRODUCT;
		};

		/**
		 * Returns the table column name based on the product type is CASEPACK or Upc Scan Code.
		 * @returns {string}
		 */
		self.getDisplayName = function () {
			if(self.productType === PRODUCT){
				return "Product Id"
			} else if(self.productType === UPC){
				return "UPC"
			} else {
				return "Item Id";
			}
		};

		/**
		 * Gets all attribute values for an attribute that has listOfValues switch == 'true', which allows users to
		 * only select from a given list of values.
		 *
		 * @param attribute Attribute to get values for.
		 */
		self.getAttributeValues = function(attribute){
			if(!attribute.hasCodes){
				productTaxonomyApi.getProductAttributeValues(
					{attributeId: attribute.attributeId},
					function(result) {
						attribute.codes = result;
						attribute.hasCodes = true;
						if(result.length === 0){
							attribute.error = "No codes available.";
						}
					},
					fetchError);
			}
		};

		/**
		 * Sends a request for new attribute values for a selected attribute
		 *
		 * @param attribute Attribute to request values for
		 */
		self.sendRequestForAttributeValues = function(attribute){
			self.selectedAttribute = attribute;

			self.styleDisplay2Modal = { 'z-index': 1040 };
			$('#'.concat(self.newAttributesModalId)).modal({backdrop: 'static', keyboard: false});
			self.confirmSendTitle = "Request new attribute values";
			self.confirmSendMessage = "Enter one or more new values:";
		};

		/**
		 * Send request for new attribute values.
		 */
		self.agreeSend = function () {
			$('#'.concat(self.newAttributesModalId)).modal("hide");

			self.success = '';
			self.error = '';
			self.waitingForAttributes = true;
			productTaxonomyApi.requestProductAttributeValues(self.selectedAttribute,
			function(result){
				self.waitingForAttributes = false;
				self.success = result.message;
				setTimeout(function(){
					self.success = '';
				}, 3000);
			},
			function(error){
				fetchError('Error sending request for attribute values ', error);
				self.waitingForAttributes = false;
			});
		};

		/**
		 * Cancel request for new attribute values.
		 */
		self.cancelSend = function () {
			$('#'.concat(self.newAttributesModalId)).modal("hide");
		};

		/**
		 * On an update to the data, call the callback function with whether or not the data matches the original data.
		 *
		 * @returns {boolean}
		 */
		self.updateHasChanges = function(){
			parentCallBack(hasUpdates());
		};

		/**
		 * Callback to the parent taxonomy component.
		 *
		 * @param hasChanges
		 */
		function parentCallBack(hasChanges){
			self.hasChanges({changes: hasChanges})
		}

		/**
		 * Checks if this component has changes, by looking at the selected value(s) for each attribute. If the list of
		 * values length is different for the current and original, return true. If one difference is found (value is in
		 * original and not in current, or vice versa), return true. Else return false.
		 *
		 * @returns {boolean} Whether a change was detected.
		 */
		function hasUpdates(){
			var hasChanges = false;
			var currentAttribute;
			var originalAttribute;
			var currentValue;
			for(var index = 0; index < self.data.length; index++){
				currentAttribute = self.data[index];
				originalAttribute = self.originalData[index];
				if(currentAttribute.values.length !== originalAttribute.values.length){
					hasChanges = true;
					break;
				}
				for(var valueIndex = 0; valueIndex < currentAttribute.values.length; valueIndex++){
					currentValue = currentAttribute.values[valueIndex];
					if(!findOriginalValue(currentValue, originalAttribute)){
						hasChanges = true;
						break;
					}
				}
				if(hasChanges){
					break;
				}
			}
			return hasChanges;
		}

		/**
		 * Finds the original value matching the current value, or null if not found.
		 *
		 * @param valueToFind Value in the current list.
		 * @param originalAttribute Attribute in the original list with a list of values.
		 * @returns {null|Object} Original value found, or null if not found.
		 */
		function findOriginalValue(valueToFind, originalAttribute){
			for(var valueIndex = 0; valueIndex < originalAttribute.values.length; valueIndex++){
				if(JSON.stringify(valueToFind) === JSON.stringify(originalAttribute.values[valueIndex])){
					return originalAttribute.values[valueIndex];
				}
			}
			return null;
		}

		/**
		 * Respond to a 'save' broadcast from the taxonomy service.
		 */
		taxonomyService.getServiceScope().$on(
			taxonomyService.getSaveMessage(),
			function(){
				if(hasUpdates()){
					self.waitingForAttributes = true;
					var parameters = {};
					parameters.productId = self.productMaster.prodId;
					parameters.productType = self.productType;
					parameters.productAttributes = self.data;
					productTaxonomyApi.updateProductAttributes(
						parameters,
						getAttributes,
						function(error){
							fetchError('Error getting attributes by product ID and product type', error);
							self.waitingForAttributes = false;
						}
					)
				}
			}
		);

		/**
		 * Respond to a 'reset' broadcast from the taxonomy service.
		 */
		taxonomyService.getServiceScope().$on(
			taxonomyService.getResetMessage(),
			function(){
				self.data = angular.copy(self.originalData);
				parentCallBack(false);
			}
		);

		/**
		 * Toggles the date picker to be
		 *
		 */
		self.toggleDatePicker = function(attribute){
			attribute.attributeValueDateOpened = !attribute.atributeValueDateOpened;
		};

		/**
		 * Add the selected date to the attribute's values.
		 *
		 * @param attribute Attribute to add the date to.
		 */
		self.addTimeStampToValues = function(attribute){
			if(!attribute.values){
				attribute.values = [];
			}
			attribute.values.push(self.attributeValueTimeStamp)
		};

		/**
		 * Setter for selected attribute.
		 *
		 * @param attribute Attribute to set as selected.
		 */
		self.setSelectedAttribute = function(attribute){
			self.selectedAttribute = attribute;
		};

		/**
		 * Transforms the input of the multi-select for text and number types into product attribute variables.
		 *
		 * @param new_value Value entered by user.
		 * @returns {{}}
		 */
		self.transformValueFunction = function(new_value){
			if(!new_value || !self.selectedAttribute){
				return {};
			}
			self.selectedAttribute.error = false;

			var newValue = {};
			var number;
			switch(self.selectedAttribute.domain){
				case self.DECIMAL_TYPE: {
					if(!Number.isNaN(Number(new_value))){
						var stringSplit = new_value.split('.');
						var greaterThanZero = Number(new_value) > 0;
						var hasError = false;
						if(greaterThanZero){
							if(stringSplit[0].length > (self.selectedAttribute.maximumLength - self.selectedAttribute.precision)){
								hasError = true;
							} else if(stringSplit.length > 1 && stringSplit[1].length > self.selectedAttribute.precision){
								hasError = true;
							}
						} else {
							if(stringSplit.length === 1){
								if(stringSplit[0].length > self.selectedAttribute.precision) {
									hasError = true;
								}
							} else if(stringSplit[0].length > (self.selectedAttribute.maximumLength - self.selectedAttribute.precision)){
								hasError = true;
							} else if(stringSplit.length > 1 && stringSplit[1].length > self.selectedAttribute.precision){
								hasError = true;
							}
						}
						if(!hasError){
							number = Number(Number(new_value));
							newValue.number = number;
							newValue.displayName = number;
						} else {
							self.selectedAttribute.error = "Input does not match precision: " +
								self.selectedAttribute.precision + " and scale: " +
								self.selectedAttribute.maximumLength + ".";
							return;
						}
					} else {
						self.selectedAttribute.error = "Input must be a number.";
						return;
					}
					break;
				}
				case self.INTEGER_TYPE: {
					number = Math.floor(Number(new_value));
					if(!Number.isNaN(number)){
						number = number.toString();
						if(self.selectedAttribute.maximumLength){
							number = number.substring(0, self.selectedAttribute.maximumLength);
						}
						newValue.number = number;
						newValue.displayName = number;
					} else {
						self.selectedAttribute.error = "Input must be a number.";
						return;
					}
					break;
				}
				case self.STRING_TYPE: {
					if(self.selectedAttribute.maximumLength && self.selectedAttribute.maximumLength > 0){
						new_value = new_value.substring(0, self.selectedAttribute.maximumLength);
					}
					newValue.text = new_value;
					newValue.displayName = new_value;
					break;
				}
			}
			return newValue;
		};

		/**
		 * Returns whether or not this attribute is of a date.
		 *
		 * @param attribute Attribute to look at for domain type.
		 * @returns {boolean} True if attribute is date, false otherwise.
		 */
		self.isAttributeDateType = function(attribute){
			return attribute.domain === self.DATE_TYPE;
		};

		/**
		 * Returns whether or not this attribute is of a timestamp type.
		 *
		 * @param attribute Attribute to look at for domain type.
		 * @returns {boolean} True if attribute is timestamp type, false otherwise.
		 */
		self.isAttributeTimeStampType = function(attribute){
			return attribute.domain === self.TIMESTAMP_TYPE;
		};

		/**
		 * Adds a selected date from the user to an attribute's list of values.
		 *
		 * @param attribute Attribute with the date selected.
		 */
		self.addDateToValues = function(attribute){
			if(!attribute.values){
				attribute.values = [];
			}
			var newDateValue = {};
			if(attribute.domain === self.DATE_TYPE) {
				// is date
				newDateValue.date = convertDateToYYYYMMDD(attribute.value);
			} else {
				// is time
				newDateValue.timestamp = convertDateToTime(attribute.value);
			}
			newDateValue.displayName = $filter('date_standard')(attribute.value);
			attribute.values.push(newDateValue);
			parentCallBack(hasUpdates());
		};

		/**
		 * Adds a selected date from the user to an attribute's list of values.
		 *
		 * @param attribute Attribute with the date selected.
		 */
		self.addSingleDateToValues = function(attribute){
			if(!attribute.values){
				attribute.values = [];
			}
			var newDateValue = {};
			if(attribute.domain === self.DATE_TYPE) {
				// is date
				newDateValue.date = convertDateToYYYYMMDD(attribute.value);
				newDateValue.displayName = $filter('date_standard')(attribute.value);
			} else {
				// is time
				newDateValue.timestamp = convertDateToTime(attribute.value);
				newDateValue.displayName = $filter('date_standard')(attribute.value);
			}
			var values = [];
			values.push(newDateValue);
			attribute.values = values;
			parentCallBack(hasUpdates());
		};

		/**
		 * Adds a selected date from the user to an attribute's list of values.
		 *
		 * @param attribute Attribute with the date selected.
		 */
		self.setUIDate = function(attribute){
			if(!attribute.values){
				return;
			} else {
				attribute.value = null;
				attribute.value = attribute.values[0].date;
			}
		};

		/**
		 * Convert a date (full day/ date/ time/ timezone) into hyphen-separated yyyy-MM-dd format.
		 *
		 * @param date Full date to convert.
		 * @returns {String}
		 */
		function convertDateToYYYYMMDD(date){
			if(date != null) {
				var d = new Date(date);
				var month = '' + (d.getMonth() + 1);
				var day = '' + d.getDate();
				var year = d.getFullYear();

				// If the month is 1-9, format has to be 0#.
				if (month.length < 2) {
					month = '0' + month;
				}

				// If the date is 1-9, format has to be 0#.
				if (day.length < 2) {
					day = '0' + day;
				}

				return [year, month, day].join('-');
			} else {
				return null;
			}
		}

		/**
		 * Convert a date (full day/ date/ time/ timezone) into hyphen-separated yyyy-MM-dd format.
		 *
		 * @param date Full date to convert.
		 * @returns {String}
		 */
		function convertDateToTime(date){
			if(date != null) {
				var d = new Date(date);
				var month = '' + (d.getMonth() + 1);
				var day = '' + d.getDate();
				var year = d.getFullYear();

				// If the month is 1-9, format has to be 0#.
				if (month.length < 2) {
					month = '0' + month;
				}

				// If the date is 1-9, format has to be 0#.
				if (day.length < 2) {
					day = '0' + day;
				}

				return [year, month, day].join('-').concat('T00:00');
			} else {
				return null;
			}
		}

		/**
		 * Gets the number pattern for an attribute. If the attribute is defined as an integer, only look at max length.
		 * Else The number is a decimal so it also needs precision.
		 *
		 * @param attribute Attribute to get number pattern for.
		 * @param isInteger Whether this attribute is an integer type or decimal type.
		 * @returns {string} Regex for the input.
		 */
		self.getNumberPatternForAttribute = function(attribute, isInteger){
			var scale = attribute.maximumLength - attribute.precision;
			var precision = attribute.precision;
			if(isInteger){
				return INTEGER_REGEX_TEMPLATE.replace("$maxLength", scale);
			} else {
				return DECIMAL_REGEX_TEMPLATE.replace("$maxLength", scale).replace("$precision", precision);
			}
		};

		/**
		 * Adds a selected number from the user to an attribute's list of values.
		 *
		 * @param attribute Attribute with the number selected.
		 */
		self.addNumberToValues = function(attribute){
			if(!attribute.values){
				return;
			}
			var numberValue = attribute.values[0];
			if(typeof numberValue.number === 'undefined' || numberValue.number === ''){
				attribute.values = [];
				parentCallBack(hasUpdates());
				return;
			}
			numberValue.displayName = numberValue.number;
			parentCallBack(hasUpdates());
		};

		/**
		 * Adds a selected text from the user to an attribute's list of values.
		 *
		 * @param attribute Attribute with the text selected.
		 */
		self.addStringToValues = function(attribute){
			if(!attribute.values){
				return;
			}
			var stringValue = attribute.values[0];
			if(typeof stringValue.text === 'undefined' || stringValue.text === ''){
				attribute.values = [];
				parentCallBack(hasUpdates());
				return;
			}
			stringValue.displayName = stringValue.text;
			parentCallBack(hasUpdates());
		};

		/**
		 * Getter for attribute type. This will include the attribute domain and, if the attribute allows multiple
		 * selection, will provide that text.
		 *
		 * @param attribute Attribute to get attribute type for.
		 */
		self.getAttributeType = function(attribute){
			var attributeType;
			if(attribute.listOfValues){
				attributeType = "Select";
			} else {
				switch(attribute.domain){
					case self.DECIMAL_TYPE: {
						attributeType = "Number";
						break;
					}
					case self.INTEGER_TYPE: {
						attributeType = "Number";
						break;
					}
					case self.STRING_TYPE: {
						attributeType = "Text";
						break;
					}
					case self.TIMESTAMP_TYPE: {
						attributeType = "TimeStamp";
						break;
					}
					default: {
						attributeType = "Date";
						break;
					}
				}
			}
			if(attribute.multipleAllowed){
				attributeType = attributeType.concat("(multiple)")
			} else {
				attributeType = attributeType.concat("(single)")
			}
			return attributeType;
		};

		self.clearSingleDate = function(attribute) {
			attribute.value = null;
			attribute.values = [];
			parentCallBack(hasUpdates());
		}
	}
})();
