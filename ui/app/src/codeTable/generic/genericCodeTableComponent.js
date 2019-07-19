/*
 *   productCategoryComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Code Table -> generic code table component.
 *
 * @author m314029
 * @since 2.21.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('genericCodeTable', {
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/codeTable/generic/genericCodeTable.html',
		bindings: {
			tableSelected: '<'
		},
		// The controller that handles our component logic
		controller: GenericCodeTableController
	});

	GenericCodeTableController.$inject = ['GenericCodeTableApi', 'ngTableParams'];

	/**
	 * Generic code table's controller definition.
	 * @constructor
	 */
	function GenericCodeTableController(genericCodeTableApi, ngTableParams) {
		/** All CRUD operation controls of product category page goes here */

		var self = this;

		self.codeTableParams = null;
		self.newCodes = [];
		self.isAddingCode = false;

		const DEFAULT_PAGE = 1;
		const DEFAULT_PAGE_SIZE = 20;

		const CODE_TABLE_BUILD_ERROR_PRETEXT = "Error getting code table data: ";

		/**
		 * Standard AngularJS function ($onChanges) implementation. Used to catch change events of the objects that
		 * bound to this component (bindings can be found listed in the component definition above).
		 */
		self.$onChanges = function () {
			resetDefaults();
			fetchDataForCodeTable();
		};

		/**
		 * Resets defaults for controller.
		 */
		function resetDefaults(){
			self.error = null;
		}

		/**
		 * Gets all data for code table by calling back end.
		 */
		function fetchDataForCodeTable(){
			var parameters = {};
			parameters.tableName = self.tableSelected.codeTableName;
			self.isWaitingForResponse = true;
			genericCodeTableApi.findAllByTable(
				parameters,
				loadData,
				function(error){
					loadData([]);
					buildError(CODE_TABLE_BUILD_ERROR_PRETEXT, error);
				}
			)
		}

		/**
		 * Create the ngTable with the given results.
		 *
		 * @param results Code table records.
		 */
		function loadData(results){
			self.isWaitingForResponse = false;
			self.codeTableParams = new ngTableParams(
				{
					page: DEFAULT_PAGE, /* show first page */
					count: DEFAULT_PAGE_SIZE /* count per page */
				}, {
					counts: [],
					debugMode: false,
					data: results
				}
			)
		}

		/**
		 * Builds the error given a pre-text string, and api error message.
		 *
		 * @param errorPreText Pre-text for an error message.
		 * @param error Error message from api.
		 */
		function buildError(errorPreText, error){
			self.error = errorPreText + getError(error);
			self.isWaitingForResponse = false;
		}

		/**
		 * Gets the error given an api error message.
		 *
		 * @param error Error message from api.
		 * @returns {string}
		 */
		function getError(error){
			if (error && error.data) {
				if(error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return "An unknown error occurred.";
			}
		}


		/**
		 * Edit current row of code data.
		 * @param code
		 * @param index
		 */
		self.editCode = function(code, index){
			self.previousEditingIndex = index;
			self.currentEditingObject = angular.copy(code);
			self.isEditing = true;
		};

		/**
		 * Set codeToRemove.
		 */
		self.setRemoveCode = function(code){
			self.codeToRemove = code;
		};

		/**
		 * Reset codeToRemove.
		 */
		self.cancelRemove = function(){
			self.codeToRemove = null;
		};

		/**
		 * Call back end to remove a code.
		 */
		self.removeCode = function () {

			if(self.codeToRemove) {
				genericCodeTableApi.deleteCodeByTableNameAndId(
					{tableName: self.tableSelected.codeTableName, id: self.codeToRemove.id },
					self.onSuccess,
					self.onError
				);
			}
		};

		/**
		 * Call back end to save a code.
		 */
		self.saveCode = function (item) {
			self.isWaitingForResponse = true;
			var genericCodeTableUpdate = {tableName :  self.tableSelected.codeTableName, codeTables: item};
			genericCodeTableApi.saveCodeByTableNameAndCodeTable(genericCodeTableUpdate,
				self.onSuccess,
				self.onError
			);
		};

		/**
		 * Sets up parameters for the new code modal.
		 */
		self.addCodeModal = function () {
			self.isAddingCode = true;
			self.newCodes = [];
			self.newCodes.push({id: '', description: ''});
		};
		/**
		 * Adds a new empty row into the add new code table.
		 */
		self.addNewCodeRow = function () {
			self.newCodes.push({id: '', description: ''});
		};

		/**
		 * Returns true if the currently editing index is the same as previous editing index.
		 *
		 * @param index
		 * @returns {boolean}
		 */
		self.compareEditingIndexToCurrentIndex = function(index) {
			return self.previousEditingIndex != null && self.previousEditingIndex === index;
		};

		/**
		 * Returns true is there are changes in the current editing object  and the description isn't empty.
		 * @param code
		 * @returns {boolean}
		 */
		self.isCodeChangesAndNotEmpty = function(code) {
			return JSON.stringify(self.currentEditingObject) !== JSON.stringify(code) && code.description;
		};

		/**
		 * Cancel edit of row for a code
		 * @param index
		 */
		self.cancelEdit = function(item, index) {
			self.previousEditingIndex = null;
			item.description = self.currentEditingObject.description;
		};

		/**
		 * Resets parameters when closing the add new code modal.
		 */
		self.confirmAddNewCodeModalClose = function() {
			var isData = false;
			if(self.newCodes.length >0) {
				for(var x=0; x<self.newCodes.length; x++){
					if(self.newCodes[x].id || self.newCodes.description){
						isData = true;
						break;
					}
				}
			}
			if(isData) {
				$('#cancelAddCodeModal').modal("show");
			} else {
				self.newCodesError = "";
				self.isAddingCode = false;
				$('#addCodeModal').modal("hide");
			}
		};


		/**
		 * Validates codes and makes a backend call to save the codes to add.
		 */
		self.saveNewCodes = function () {
			$('#cancelAddCodeModal').modal("hide");
			var codes = self.validateNewCodes();
			if(!self.newCodesError) {
				self.isWaitingAddingCodes = true;
				var genericCodeTableUpdate = {tableName :  self.tableSelected.codeTableName, codeTables: codes};
				genericCodeTableApi.addAllByTableNameAndCodeTables(genericCodeTableUpdate,
					self.saveNewCodesSuccess,
					self.saveNewCodesError
				);
			}
		};

		/**
		 * Success callback function that hides the spinner and add code modal.
		 * @param result
		 */
		self.saveNewCodesSuccess = function(result) {
			self.isWaitingAddingCodes = false;
			self.isAddingCode = false;
			self.success = result.message;
			$('#addCodeModal').modal("hide");
			fetchDataForCodeTable();
		};

		/**
		 * Error callback when adding a new code.
		 *
		 * @param error
		 */
		self.saveNewCodesError = function(error) {
			self.isWaitingAddingCodes = false;
			self.newCodesError = error;
		};

		/**
		 * Validates whether or not a code is available.
		 *
		 * @param index
		 */
		self.isCodeAvailable = function(index) {
			if(self.newCodes[index].id) {
				genericCodeTableApi.findCodeByTableNameAndId(
					{tableName: self.tableSelected.codeTableName, id: self.newCodes[index].id },
					function(results) {
						self.newCodes[index].isCodeAvailable = !results.id;
					},
					function(error){
						buildError(CODE_TABLE_BUILD_ERROR_PRETEXT, error);
					}
				);
			} else {
				self.newCodes[index].isCodeAvailable = undefined;
			}
		};

		/**
		 * Validates the new codes to see if list is empty, there are duplicates, missing values, etc, and returns the list
		 * if valid.
		 * @returns {*}
		 */
		self.validateNewCodes = function () {
			self.newCodesError = "";
			var duplicateCodes = false;
			var unavailableCodes = false;
			var missingCode = false;
			var missingDescription = false;
			var codes = [];

			for(var x=0; x< self.newCodes.length; x++) {
				if(!self.newCodes[x].isCodeAvailable && self.newCodes[x].id) {
					unavailableCodes = true;
				}
				if(self.newCodes[x].id) {
					if(!self.newCodes[x].description) {
						missingDescription = true;
					} else {
						codes.push(self.newCodes[x]);
					}
					for (var y = 0; y < self.newCodes.length; y++) {
						if (x !== y) {
							if (self.newCodes[x].id === self.newCodes[y].id) {
								duplicateCodes = true;
							}
						}
					}
				} else if (self.newCodes[x].description) {
					missingCode = true;
					codes.push(self.newCodes[x]);
				}
			}

			if(unavailableCodes) {
				self.newCodesError += "ERROR: Some of the chosen codes were unavailable. Please select an available code.  ";
			}
			if(duplicateCodes) {
				self.newCodesError += "ERROR: Duplicate codes chosen. Please remove the duplicates and try again.  ";
			}
			if(missingCode) {
				self.newCodesError += "ERROR: Some of the codes contain a description that is missing a code ID.   ";
			}
			if(missingDescription) {
				self.newCodesError += "ERROR: Some of the codes contain an ID that is missing a description.  ";
			}
			if(codes.length === 0) {
				self.newCodesError += "ERROR: Please enter a code and description to save.   ";
				return undefined;
			} else {
				return codes;
			}
		};

		/**
		 * Closes add new code confirmation modal.
		 */
		self.closeAddNewCodeConfirmModal = function() {
			$('#cancelAddCodeModal').modal("hide");
		};

		/**
		 * Closes add new codes modal.
		 */
		self.closeAddNewCodeModals = function() {
			$('#cancelAddCodeModal').modal("hide");
			$('#addCodeModal').modal("hide");
		};

		/**
		 * Success callback that fetches new data.
		 * @param results
		 */
		self.onSuccess = function(results) {
			self.success = results.message;
			self.isAddingCode = false;
			self.isEditing = false;
			self.previousEditingIndex = null;

			fetchDataForCodeTable();
		};

		/**
		 * Error callback.
		 * @param results
		 */
		self.onError = function(results) {
			self.error = results.message;
			self.isWaitingForResponse = false;
		};
	}
})();
