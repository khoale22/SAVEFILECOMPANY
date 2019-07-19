/*
 *
 * attributeMaintenanceDetailsController.js
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 * @author a786878
 * @since 2.16.0
 */
'use strict';

/**
 * The controller for the Attribute Maintenance Details Screen
 */
(function() {
	angular.module('productMaintenanceUiApp').directive('attributeMaintenanceDetailsDirective', attributeMaintenanceDetailsController);

	attributeMaintenanceDetailsController.$inject = ['attributeMaintenanceApi', 'ngTableParams', '$log', '$filter'];

	/**
	 * Constructs the controller.
	 */
	function attributeMaintenanceDetailsController(attributeMaintenanceApi, ngTableParams, $log, $filter) {

		return {

			scope: {
				attributeId: '<',
				onReturn: '&'
			},
			restrict: 'E',
			// Inline template which is bound to message variable in the component controller
			templateUrl: 'src/eCommerce/attributeMaintenanceDetails.html',
			controllerAs : 'attributeMaintenanceDetailsDirective',
			// The controller that handles our component logic

			controller: function ($scope) {
				const PM_SYSTEM = 4;

				var self = this;

				/**
				 * The original attribute values list in attribute values table
				 * @type {Array}
				 */
				self.originalAttributeValues = [];

				/**
				 * The changed attribute values list in attribute values table
				 * @type {Array}
				 */
				self.changedAttributeValues = [];

				/**
				 * The current attribute values list in attribute values table
				 * @type {Array}
				 */
				self.currentAttributeValues = [];

				/**
				 * Is the all checked flag set in attribute values table
				 * @type {boolean}
				 */
				self.allChecked = false;

				/**
				 * Whether or not the controller is waiting for data
				 * @type {boolean}
				 */
				self.isWaiting = false;

				/**
				 * Original attribute
				 * @type {null}
				 */
				self.originalAttribute = null;

				/**
				 * Changes to original attribute reflected here
				 * @type {null}
				 */
				self.changedAttribute = null;

				/**
				 * Determines field enabled or not
				 * @type {boolean}
				 */
				self.isMaximumLengthFieldEnabled = false;

				/**
				 * Determines field enabled or not
				 * @type {boolean}
				 */
				self.isPrecisionFieldEnabled = false;

				/**
				 * Attribute domain types (e.g. String, Decimal)
				 */
				self.attributeDomains = [{
					"attributeDomainCode": "DEC",
					"attributeDomainDescription": "DECIMAL"
				}, {
					"attributeDomainCode": "DT",
					"attributeDomainDescription": "DATE"
				}, {
					"attributeDomainCode": "I",
					"attributeDomainDescription": "INTEGER"
				}, {
					"attributeDomainCode": "IMG",
					"attributeDomainDescription": "IMAGE"
				}, {
					"attributeDomainCode": "S",
					"attributeDomainDescription": "STRING"
				}, {
					"attributeDomainCode": "TS",
					"attributeDomainDescription": "TIMESTAMP"
				}];

				/**
				 * Sets the two way binded value to null so this directive will disappear due to the ng-show & ng-if
				 */
				self.returnToList = function (){
					$scope.onReturn();
				};

				/**
				 * Initiates the construction of the attribute maintenance controller
				 */
				self.$onInit = function () {
					self.touchedInit = true;

					self.id = $scope.attributeId;

					if (self.id !== null) {
						self.isWaiting = true;
						self.getAttributeMaintenanceDetails();
						self.getAttributeMaintenanceValues();
					} else {
						// if no id then it is an empty attribute screen for create
						self.id = 0;
						self.originalAttribute = {
							attributeId: 0,
							attributeName: '',
							maximumLength: 0,
							precision: '',
							attributeDescription: '',
							tag: false,
							multipleValueSwitch: false,
							attributeDomainCode: '',
							dynamicAttributeSwitch: false,
							attributeDomainDescription: '',
							specification: false,
							attributeValueList: false,
							operation: 'A'
						};
						self.changedAttribute = angular.copy(self.originalAttribute);
					}
				};

				/**
				 * Loads the attribute domain values from the back end
				 */
				self.getAttributeDomains = function () {
					attributeMaintenanceApi.getAttributeDomains(self.loadAttributeDomains, self.fetchError);
				};

				/**
				 * Loads the attribute data from the back end
				 */
				self.getAttributeMaintenanceDetails = function () {
					attributeMaintenanceApi.getAttributeDetails({id: self.id}, self.loadAttributeDetails, self.fetchError);
				};

				/**
				 * Loads the attribute values from the back end
				 */
				self.getAttributeMaintenanceValues = function () {
					attributeMaintenanceApi.getAttributeValues({id: self.id}, self.loadAttributeValues, self.fetchError);
				};

				self.initDomain = function () {
					self.attributeDomains.forEach(function (element) {
						if (angular.equals(self.changedAttribute.attributeDomainCode, element.attributeDomainCode)) {
							self.changedAttribute.selectedDomain = element;
							self.originalAttribute.selectedDomain = element;
							self.initSelectedDomain();
						}
					});
				};

				/**
				 * load the attribute results
				 * @param results
				 */
				self.loadAttributeDetails = function (results) {
					self.originalAttribute = results;
					self.changedAttribute = angular.copy(self.originalAttribute);

					self.initDomain();

					self.isWaiting = false;
				};

				/**
				 * load the attribute domain results
				 * @param results
				 */
				self.loadAttributeDomains = function (results) {
					self.attributeDomains = results;
					self.attributeDomains.forEach(function (element) {
						element.attributeDomainCode = element.attributeDomainCode.trim();
					});
					self.isWaiting = false;
				};

				/**
				 * load the attribute values results
				 * @param results
				 */
				self.loadAttributeValues = function (results) {
					self.originalAttributeValues = results;
					self.changedAttributeValues = angular.copy(self.originalAttributeValues);
					self.currentAttributeValues = angular.copy(self.originalAttributeValues);

					for (var index = 0; index < self.currentAttributeValues.length; index++) {
						self.currentAttributeValues[index].toDelete = false;
						self.currentAttributeValues[index].isChecked = false;
						self.currentAttributeValues[index].toAdd = false;
					}

					self.buildTable();
					self.isWaiting = false;
				};

				/**
				 * If there is an error this will display the error
				 * @param error
				 */
				self.fetchError = function (error) {
					self.isWaiting = false;
					self.data = null;
					if (error && error.data) {
						if (error.data.message != null && error.data.message != "") {
							self.setError([error.data.message]);
						} else {
							self.setError([error.data.error]);
						}
					}
					else {
						self.setError("An unknown error occurred.");
					}
				};

				/**
				 * Sets the error
				 * @param error
				 */
				self.setError = function (error) {
					self.error = error;
				};

				/**
				 * Builds the attribute maintenance values table
				 */
				self.buildTable = function () {
					if (self.changedAttributeValues.length > 0) {
						self.attributeMaintenanceValuesTableParams = new ngTableParams(
							{
								page: 1,
								count: 20
							},
							{
								counts: [],
								total: self.changedAttributeValues.length,
								getData: function ($defer, params) {
									self.currentAttributeValues = self.changedAttributeValues.slice((params.page() - 1) * params.count(), params.page() * params.count());

									// need to make sure new page shows all checked
									if (self.allChecked) {
										self.checkAllAttributesRows();
									}

									$defer.resolve(self.currentAttributeValues);
									self.defer = $defer;
									self.dataResolvingParams = params;
								}
							}
						)
					} else {
						self.currentAttributeValues = null;
					}
				};

				/**
				 * Check if the attribute has changed
				 * @return {boolean}
				 */
				self.isDifference = function () {
					var result = angular.toJson(self.originalAttribute) !== angular.toJson(self.changedAttribute) || angular.toJson(self.originalAttributeValues) !== angular.toJson(self.changedAttributeValues);

					return result;
				};

				/**
				 * Reset the attribute back to the original value
				 */
				self.reset = function () {
					self.error = null;
					self.success = null;
					self.changedAttribute = angular.copy(self.originalAttribute);
					self.changedAttributeValues = angular.copy(self.originalAttributeValues);
					self.initDomain();
					self.allChecked = false;
					self.attributeMaintenanceValuesTableParams.reload();
				};

				/**
				 * Selected domain was changed, so update defaults for precision and scale based on type
				 */
				self.changeSelectedDomain = function () {
					if (self.changedAttribute.selectedDomain === null) {
						self.changedAttribute.maximumLength = '';
						self.isMaximumLengthFieldEnabled = false;

						self.changedAttribute.precision = '';
						self.isPrecisionFieldEnabled = false;

					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'DEC') {
						self.changedAttribute.maximumLength = 13;
						self.isMaximumLengthFieldEnabled = true;

						self.changedAttribute.precision = 4;
						self.isPrecisionFieldEnabled = true;

					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'DT') {
						self.changedAttribute.maximumLength = 10;
						self.isMaximumLengthFieldEnabled = true;

						self.changedAttribute.precision = '';
						self.isPrecisionFieldEnabled = false;

					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'I') {
						self.changedAttribute.maximumLength = '13';
						self.isMaximumLengthFieldEnabled = true;

						self.changedAttribute.precision = '';
						self.isPrecisionFieldEnabled = false;

					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'IMG') {
						self.changedAttribute.maximumLength = '';
						self.isMaximumLengthFieldEnabled = false;

						self.changedAttribute.precision = '';
						self.isPrecisionFieldEnabled = false;

					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'S') {
						self.changedAttribute.maximumLength = 10000;
						self.isMaximumLengthFieldEnabled = true;

						self.changedAttribute.precision = '';
						self.isPrecisionFieldEnabled = false;

					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'TS') {
						self.changedAttribute.maximumLength = 26;
						self.isMaximumLengthFieldEnabled = true;

						self.changedAttribute.precision = '';
						self.isPrecisionFieldEnabled = false;
					}

					if (self.changedAttribute.selectedDomain === null) {
						self.changedAttribute.attributeDomainCode = '';
						self.changedAttribute.attributeDomainDescription = '';
					} else {
						self.changedAttribute.attributeDomainCode = self.changedAttribute.selectedDomain.attributeDomainCode;
						self.changedAttribute.attributeDomainDescription = self.changedAttribute.selectedDomain.attributeDomainDescription;
					}
				};

				/**
				 * Init defaults for enabled based on actual value
				 */
				self.initSelectedDomain = function () {
					if (self.changedAttribute.selectedDomain === null) {
						self.isMaximumLengthFieldEnabled = false;
						self.isPrecisionFieldEnabled = false;
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'DEC') {
						self.isMaximumLengthFieldEnabled = true;
						self.isPrecisionFieldEnabled = true;
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'DT') {
						self.isMaximumLengthFieldEnabled = true;
						self.isPrecisionFieldEnabled = false;
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'I') {
						self.isMaximumLengthFieldEnabled = true;
						self.isPrecisionFieldEnabled = false;
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'IMG') {
						self.isMaximumLengthFieldEnabled = false;
						self.isPrecisionFieldEnabled = false;
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'S') {
						self.isMaximumLengthFieldEnabled = true;
						self.isPrecisionFieldEnabled = false;
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'TS') {
						self.isMaximumLengthFieldEnabled = true;
						self.isPrecisionFieldEnabled = false;
					}
				};

				/**
				 * Validate changes to attribute details
				 */
				self.validate = function() {
					self.error = [];

					if (!self.validateAddValue()) {
						self.error.push('Attribute Value Description must be specified');
					}
					if (angular.equals(self.changedAttribute.attributeName.trim(), '')) {
						self.error.push('Attribute Name must be specified');
					}

					if (angular.equals(self.changedAttribute.attributeDescription.trim(), '')) {
						self.error.push('Attribute Description must be specified');
					}

					if (self.changedAttribute.selectedDomain === undefined || self.changedAttribute.selectedDomain === null || self.changedAttribute.selectedDomain.attributeDomainCode === null) {
						self.error.push('Attribute Domain Code must be selected');
					}

					if (self.changedAttribute.tag === true && self.changedAttribute.specification === true) {
						self.error.push('Tag and Specification cannot both be selected');
					}

					if (self.changedAttribute.selectedDomain.attributeDomainCode === 'DEC') {
						if (self.changedAttribute.maximumLength === '' | self.changedAttribute.maximumLength > 13) {
							self.error.push('Maximum Length must be less than or equal to 13');
						} else if (self.changedAttribute.maximumLength === '0') {
							self.error.push('Maximum Length must be greater than 0');
						}
						if (self.changedAttribute.precision === '' | self.changedAttribute.precision > 4) {
							self.error.push('Precision must be less than or equal to 4');
						} else if (self.changedAttribute.precision === '0') {
							self.error.push('Precision must be greater than 0');
						}
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'DT') {
						if (self.changedAttribute.maximumLength === '' | self.changedAttribute.maximumLength > 10) {
							self.error.push('Maximum Length must be less than or equal to 10');
						} else if (self.changedAttribute.maximumLength === '0') {
							self.error.push('Maximum Length must be greater than 0');
						}
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'I') {
						if (self.changedAttribute.maximumLength === '' | self.changedAttribute.maximumLength > 13) {
							self.error.push('Maximum Length must be less than or equal to 13');
						} else if (self.changedAttribute.maximumLength === '0') {
							self.error.push('Maximum Length must be greater than 0');
						}
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'IMG') {
						// no validation
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'S') {
						if (self.changedAttribute.maximumLength === '' | self.changedAttribute.maximumLength > 10000) {
							self.error.push('Maximum Length must be less than or equal to 10000');
						} else if (self.changedAttribute.maximumLength === '0') {
							self.error.push('Maximum Length must be greater than 0');
						}
					} else if (self.changedAttribute.selectedDomain.attributeDomainCode === 'TS') {
						if (self.changedAttribute.maximumLength === '' | self.changedAttribute.maximumLength > 26) {
							self.error.push('Maximum Length must be less than or equal to 26');
						} else if (self.changedAttribute.maximumLength === '0') {
							self.error.push('Maximum Length must be greater than 0');
						}
					}
				};

				/**
				 * Save the changes to the back-end
				 *
				 */
				self.save = function () {
					self.validate();

					if (self.error.length === 0) {
						self.error = null;
						self.success = null;

						self.confirmTitle = 'Save Confirmation';
						self.confirmMessage = 'Are you sure you want to save the changes?';

						$('#confirmSaveModal').modal({backdrop: 'static', keyboard: true});
					}
				};

				/**
				 * Toggle the all rows checked flag
				 */
				self.toggleAllChecked = function () {
					if (self.allChecked === true) {
						self.checkAllAttributesRows();
					} else if (self.allChecked === false) {
						self.uncheckAllAttributesRows();
					}
				};

				/**
				 * This method will check all rows in attribute values table
				 */
				self.checkAllAttributesRows = function () {
					self.allChecked = true;

					if (self.changedAttributeValues !== null) {
						for (var index = 0; index < self.changedAttributeValues.length; index++) {
							self.changedAttributeValues[index].isChecked = true;
						}
					}
				};

				/**
				 * This method will uncheck all rows in attribute values table
				 */
				self.uncheckAllAttributesRows = function () {
					self.allChecked = false;

					if (self.changedAttributeValues !== null) {
						for (var index = 0; index < self.changedAttributeValues.length; index++) {
							self.changedAttributeValues[index].isChecked = false;
						}
					}
				};

				/**
				 * Is at least one row checked in attribute values table?
				 * @return {boolean}
				 */
				self.atLeastOneChecked = function () {
					if (self.allChecked || self.changedAttributeValues === null) {
						return true;
					}

					for (var index = 0; index < self.changedAttributeValues.length; index++) {
						if (self.changedAttributeValues[index].isChecked === true) {
							return true;
						}
					}

					return false;
				};

				/**
				 * Add an attribute value to the table
				 */
				self.add = function () {
					self.uncheckAllAttributesRows();
					self.error = null;
					if (self.validateAddValue() === true) {
						self.changedAttribute.attributeValueList = true;

						self.changedAttributeValues.push(
							{
								'key': {
									'attributeId': ''
								},
								'attributeCode': {
									'attributeValueText': ''
								},
								toBeAdded: true
							});
						if (self.attributeMaintenanceValuesTableParams === undefined) {
							self.buildTable();
						}
						var lastPage = Math.ceil(self.changedAttributeValues.length/self.attributeMaintenanceValuesTableParams.count());

						self.dataResolvingParams.total(self.changedAttributeValues.length);
						self.defer.resolve(self.attributeMaintenanceValuesTableParams.data);

						self.attributeMaintenanceValuesTableParams.page(lastPage);
						self.attributeMaintenanceValuesTableParams.reload();
					} else {
						self.error = ['Attribute Value Description must be specified'];
					}
				};

				/**
				 * Validate that the value to be added has a description
				 */
				self.validateAddValue = function () {
					var result = true;

					self.changedAttributeValues.forEach(function (attr) {
						if (attr.toBeAdded === true) {
							if (attr.attributeCode.attributeValueText.length > 0) {
								attr.toBeAdded = false; // no longer editable by user
								attr.toAdd = true;
							} else {
								result = false;
							}
						}
					});

					return result;
				};

				/**
				 * Delete an attribute value from the table
				 */
				self.delete = function () {
					self.error = null;

					self.removeDanglingAddValues();

					self.allChecked = false;

					for (var index = 0; index < self.currentAttributeValues.length; index++) {
						if (self.currentAttributeValues[index].isChecked === true) {
							self.currentAttributeValues[index].toDelete = true;
							self.currentAttributeValues[index].isChecked = false;
						}
					}
					self.attributeMaintenanceValuesTableParams.reload();
				};

				/**
				 * Remove dangling add values
				 */
				self.removeDanglingAddValues = function () {
					var removeValFromIndex = [];
					for (var index = 0; index < self.changedAttributeValues.length; index++) {
						if (self.changedAttributeValues[index].toBeAdded === true) {
							removeValFromIndex.push(index);
						}
					}

					for (var i = removeValFromIndex.length - 1; i >= 0; i--) {
						self.changedAttributeValues.splice(removeValFromIndex[i], 1);
					}
				};

				/**
				 * If un-checking Code field then popup to delete all values
				 */
				self.clickCodeCheckbox = function() {
					if (self.changedAttribute.attributeValueList == false && self.hasAttributeValue()) {
						$('#deleteAttributeValuesTypeModal').modal("show");
					}
				};

				/**
				 * check if there is at least one real attribute value
				 */
				self.hasAttributeValue = function() {
					var result = false;

					var len = self.changedAttributeValues.length;
					if (len > 0 ) {
						if (len === 1 && angular.equals(self.changedAttributeValues[0].attributeCode.attributeValueText,"") || self.changedAttributeValues[0].toDelete === true) {
							result = false;
						} else {
							result = true;
						}
					}

					return result;
				};

				/**
				 * This method will be called when click on continue button of delete attribute values.
				 */
				self.continueDelete = function () {
					$('#deleteAttributeValuesTypeModal').modal("hide");

					// delete the attribute values
					self.deleteAttributeValues();
				};

				/**
				 * This method will be called when click on continue button of delete attribute values.
				 */
				self.cancelDelete = function () {
					$('#deleteAttributeValuesTypeModal').modal("hide");

					// restore original value
					self.changedAttribute.attributeValueList = true;
				};

				/**
				 * Delete all the attribute values
				 */
				self.deleteAttributeValues = function() {
					for (var index = 0; index < self.changedAttributeValues.length; index++) {
						self.changedAttributeValues[index].toDelete = true;
					}
				};

				/**
				 * This method will be called when click on yes button of save confirmation modal to call the save method.
				 */
				self.agreeSave = function () {
					$('#confirmSaveModal').modal("hide");

					var attributeChanges = self.getAttributeDifference();

					self.isWaiting = true;

					attributeMaintenanceApi.updateAttributeDetails(attributeChanges, self.loadUpdatedAttributeDetails, self.fetchError);
				};

				/**
				 * This method will be called when click on no button of save confirmation modal to call the cancel method.
				 */
				self.cancelSave = function () {
					$('#confirmSaveModal').modal("hide");

					self.reset();
				};

				/**
				 * Check if an attribute value was added and add to the attributeValues list
				 * @param attributeValues
				 */
				self.addAddedAttributeValues = function (attributeValues) {
					for (var index = 0; index < self.changedAttributeValues.length; index++) {
						if (self.changedAttributeValues[index].toAdd === true) {
							var newAttributeValue =	{
								'attributeCode': {
									'attributeValueText': self.changedAttributeValues[index].attributeCode.attributeValueText
								},
								operation : 'A'
							};

							attributeValues.push(newAttributeValue);
						}
					}
				};

				/**
				 * Get the changed attribute value for given attribute id
				 * @param attributeCodeId
				 */
				self.getAttributeValueForId = function (attributeCodeId) {
					var result = null;

					for (var index = 0; index < self.changedAttributeValues.length; index++) {
						if (self.changedAttributeValues[index].key.attributeCodeId === attributeCodeId) {
							result = self.changedAttributeValues[index];
							break;
						}
					}

					return result;
				};

				/**
				 * Check if an attribute value was deleted
				 * @param attributeValue
				 */
				self.isAttributeValueDeleted = function (attributeValue) {
					var result = false;

					var changedAttributeValue = self.getAttributeValueForId(attributeValue.key.attributeCodeId);

					if (changedAttributeValue.toDelete) {
						result = true;
					}

					return result;
				};

				/**
				 * Get the differences made to the attribute and attribute values
				 */
				self.getAttributeDifference = function() {

					var attributeDetailsDifferent = angular.toJson(self.originalAttribute) !== angular.toJson(self.changedAttribute);

					var attributeDetails = angular.copy(self.changedAttribute);

					if (attributeDetails.operation !== 'A') {
						if (attributeDetailsDifferent === true) {
							attributeDetails.operation = 'U';
						} else {
							attributeDetails.operation = '';
						}
					}

					if (self.changedAttribute.tag === self.originalAttribute.tag) {
						attributeDetails.tagOperation = 'N';
					} else if (self.changedAttribute.tag === true, self.originalAttribute.tag === false) {
						attributeDetails.tagOperation = 'A';
					} else if (self.changedAttribute.tag === false, self.originalAttribute.tag === true) {
						attributeDetails.tagOperation = 'D';
					}

					if (self.changedAttribute.specification === self.originalAttribute.specification) {
						attributeDetails.specificationOperation = 'N';
					} else if (self.changedAttribute.specification === true, self.originalAttribute.specification === false) {
						attributeDetails.specificationOperation = 'A';
					} else if (self.changedAttribute.specification === false, self.originalAttribute.specification === true) {
						attributeDetails.specificationOperation = 'D';
					}

					var attributeValuesDifferent = angular.toJson(self.originalAttributeValues) !== angular.toJson(self.changedAttributeValues);

					// Go through original attribute values and determine if they were deleted
					if (attributeValuesDifferent === true) {
						var attributeValues = angular.copy(self.originalAttributeValues);

						for (var index = 0; index < attributeValues.length; index++) {
							if (self.isAttributeValueDeleted(attributeValues[index])) {
								attributeValues[index].operation = 'D';
							} else {
								attributeValues[index].operation = '';
							}
						}

						self.addAddedAttributeValues(attributeValues);

						attributeDetails.attributeValues = attributeValues;
					}

					return attributeDetails;
				};

				/**
				 * Refresh the attribute details from the backend
				 */
				self.loadUpdatedAttributeDetails = function() {
					self.success = 'Changes were successfully applied.';

					if (self.id !== 0) {
						self.getAttributeMaintenanceDetails();
						self.getAttributeMaintenanceValues();
					} else {
						self.isWaiting = false;
					}
				};
			}
		}
	}
})();

