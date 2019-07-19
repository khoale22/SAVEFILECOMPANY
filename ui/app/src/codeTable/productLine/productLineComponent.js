/*
 * productLineComponent.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author m314029
 * @since 2.26.0
 */

'use strict';

/**
 * Component to support the page that allows users to show brand Cost Owner T2T.
 *
 * @author vn70529
 * @since 2.12
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('productLineComponent', {
		templateUrl: 'src/codeTable/productLine/productLine.html',
		bindings: {
			selected: '<'
		},
		controller: productLineController
	});

	productLineController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'productLineApi'];
	/**
	 * Constructs for country code Controller.
	 */
	function productLineController($rootScope, $scope, ngTableParams, productLineApi) {

		var self = this;
		/**
		 * Messages
		 * @type {string}
		 */
		self.PRODUCT_LINE_NAME_MANDATORY_FIELD_ERROR = 'Product Line Name is a mandatory field.';

		/**
		 * Start position of page that want to show on country code table
		 *
		 * @type {number}
		 */
		self.PAGE = 1;
		/**
		 * The number of records to show on the  country code table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;
		self.data = [];
		self.firstSearch = false;
		self.tableParams = null;
		var previousDescriptionFilter = null;
		var previousIdFilter = null;

		/**
		 * Messages.
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";
		self.UNSAVED_DATA_CONFIRM_TITLE = "Confirmation";
		self.UNSAVED_DATA_CONFIRM_MESSAGE = "Unsaved data will be lost. Do you want to save the changes before continuing ?";
		self.PRODUCT_LINE_DELETE_MESSAGE_HEADER = 'Delete Product Line';
		self.PRODUCT_LINE_DELETE_CONFIRM_MESSAGE_STRING = 'Are you sure you want to delete the selected Product Line?';
		self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
		self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING = 'Unsaved data will be lost. Do you want to save the changes before continuing?';
		self.PRODUCT_LINE_DESCRIPTION_MANDATORY_FIELD_ERROR = 'Product Line Description is a mandatory field.';

		/**
		 * Empty model.
		 */
		self.EMPTY_MODEL = {
			id: '',
			description: '',
			productBrandId: ''
		};
		/**
		 * Selected edit product line.
		 * @type {null}
		 */
		self.selectedProductLine = null;
		/**
		 * The original, unedited product line.
		 * @type {null}
		 */
		self.originalProductLine = null;
		/**
		 * Selected edited row index.
		 * @type {null}
		 */
		self.selectedRowIndex = -1;
		/**
		 * Validation model.
		 */
		self.validationModel = angular.copy(self.EMPTY_MODEL);
		/**
		 * Validation product line key.
		 * @type {string}
		 */
		self.VALIDATE_PRODUCT_LINE = 'validateProductLine';
		/**
		 * Return tab key.
		 * @type {string}
		 */
		self.RETURN_TAB = 'returnTab';
		self.isAddingProductLine = false;
		self.newProductLines = [];
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.newSearch();
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
		};

		/**
		 * Resets the table with current filter. If the table has not been created, create the table. Else reload the
		 * table.
		 */
		self.newSearch = function() {
			self.isWaitingForResponse = true;
			self.firstSearch = true;
			if (self.tableParams == null) {
				createProductLinesTable();
			} else {
				self.tableParams.reload();
			}

		};

		/**
		 * Create product lines table.
		 */
		function createProductLinesTable() {
			self.tableParams = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.PAGE_SIZE /* count per page */
			}, {
				counts: [],

				getData: function ($defer, params) {
					self.recordsVisible = 0;
					self.data = null;

					self.defer = $defer;
					self.dataResolvingParams = params;

					var includeCounts = false;

					var id = params.filter()["id"];
					var description = params.filter()["description"];

					if (typeof id === "undefined") {
						id = "";
					}
					if (typeof description === "undefined") {
						description = "";
					}

					if(id !== previousIdFilter || description !== previousDescriptionFilter) {
						self.firstSearch = true;
					}

					if (self.firstSearch) {
						includeCounts = true;
						params.page(1);
						self.firstSearch = false;
						self.startRecord = 0;
					}

					previousDescriptionFilter = description;
					previousIdFilter = id;
					self.fetchData(includeCounts, params.page() - 1, id, description);
				}
			});
		}

		/**
		 * Initiates a call to get the list of attribute maintenance records.
		 *
		 * @param includeCounts Whether or not to include getting record counts.
		 * @param page The page of data to ask for.
		 * @param id ID for attribute filtering.
		 * @param description Description for attribute filtering.
		 */
		self.fetchData = function(includeCounts, page, id, description) {
			productLineApi.findAll({
				id: id, description: description, page: page, pageSize: self.PAGE_SIZE, includeCount: includeCounts
			}, loadData, self.fetchError);
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
			if(self.isReturnToTab){
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			}
			self.isReturnToTab = false;
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
			}
			else {
				return self.UNKNOWN_ERROR;
			}
		};

		/**
		 * Clear filter.
		 */
		self.clearFilter = function () {
			self.dataResolvingParams.filter()["id"] = null;
			self.dataResolvingParams.filter()["description"] = null;
			self.tableParams.reload();
			self.error = '';
			self.success = '';
		};

		/**
		 * Determines if the filter has been cleared or not.
		 */
		self.isFilterCleared = function () {
			if(!self.dataResolvingParams) {
				return true;
			}
			if(!self.dataResolvingParams.filter()["id"] &&
				!self.dataResolvingParams.filter()["description"]) {
				return true
			} else {
				return false;
			}
		};

		/**
		 * Callback for when data is successfully returned from the backend.
		 *
		 * @param results The data returned from the backend.
		 */
		function loadData(results) {
			self.isWaitingForResponse = false;
			self.data = results.data;
			self.defer.resolve(self.data);

			if (results.complete) {
				self.totalPages = results.pageCount;
				self.totalRecords = results.recordCount;
				self.dataResolvingParams.total(self.totalRecords);
			}
		}
		/**
		 * Add one more row to product line.
		 */
		self.addRow = function () {
			if(self.isValidNewProductLine()) {
				var newProductLine = self.initEmptyNewProductLine();
				self.newProductLines.push(newProductLine);
				self.tableModalParams.reload();
			}
		};

		/**
		 * Validates new product line./
		 * @returns {boolean}
		 */
		self.isValidNewProductLine = function(){
			var errorMessages = [];
			var errorMessage = '';
			for (var i = 0; i < self.newProductLines.length; i++) {
				if(self.isNullOrEmpty(self.newProductLines[i].description)) {
					errorMessage = "<li>" + self.PRODUCT_LINE_NAME_MANDATORY_FIELD_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) == -1) {
						errorMessages.push(errorMessage);
					}
					self.newProductLines[i].addClass = 'active-tooltip ng-invalid ng-touched';
				}
			}
			if (errorMessages.length > 0) {
				var errorMessagesAsString = 'Product Line:';
				angular.forEach(errorMessages, function (errorMessage) {
					errorMessagesAsString += errorMessage;
				});
				self.errorPopup = errorMessagesAsString;
				return false;
			}
			return true;
		}

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === null || !object || object === "";
		};

		/**
		 * Handle when click Add button to display the modal.
		 */
		self.addNewProductLine = function () {
			self.isAddingProductLine = true;
			var productLine = self.initEmptyNewProductLine();
			self.clearMessages();
			self.newProductLines.push(productLine);
			self.tableModalParams = new ngTableParams({
				page: self.PAGE,
				count: self.PAGE_SIZE
			}, {
				counts: [],
				debugMode: true,
				data: self.newProductLines
			});
			$('#addProductLineModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Initiate empty product line to display in the modal.
		 *
		 * @returns {{}}
		 */
		self.initEmptyNewProductLine = function () {
			var productLine = {
				"id": "",
				"description": "",
				"productBrandId": ""
			};
			return productLine;
		};
		/**
		 * Clear all the messages when click buttons.
		 */
		self.clearMessages = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.newProductLines = [];
		}

		/**
		 * Handle when click close in add popup but have data changed to show popup confirm.
		 */
		self.closeModalUnsavedData = function () {

			if (self.newProductLines.length !== 0 && self.newProductLines[0].description.length !== 0) {
				self.titleConfirm = self.UNSAVED_DATA_CONFIRM_TITLE;
				self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE;
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			} else {
				self.isValidNewProductLine(self.newProductLines);
				$('#addProductLineModal').modal("hide");
				self.newProductLines = [];
			}
		}

		/**
		 * Close confirm popup
		 */
		self.closeConfirmPopup = function () {
			$('#confirmModal').modal("hide");
			$('.modal-backdrop').attr('style', ' ');
		}
		/**
		 * Close add popup and confirm popup.
		 */
		self.doCloseModal = function () {
			self.allowDeleteProductLine = false;
			self.isAddingProductLine = false;
			if (self.isReturnToTab) {
				$('#addProductLineModal').modal("hide");
				$('#confirmModal').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			} else {
				$('#confirmModal').modal("hide");
				$('#addProductLineModal').modal("hide");
			}
		};

		/**
		 * Removes selected row from modal table.
		 *
		 * @param index the index to remove.
		 */
		self.deleteRow = function(index) {
			self.newProductLines.splice(index, 1);
			self.tableModalParams.reload();
		};

		/**
		 * Saves new product lines.
		 */
		self.saveNewProductLines = function () {
			productLineApi.addProductLines(self.newProductLines,
				function (response) {
					self.success = response.message;
					self.doCloseModal();
					self.isAddingProductLine = false;
				}, function (error) {
					$('#confirmModal').modal("hide");
					$('.modal-backdrop').attr('style', ' z-index: 0; ');
					self.errorPopup = self.getErrorMessage(error);
				});
		}

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
			}
			else {
				return self.UNKNOWN_ERROR;
			}
		};

		/**
		 * Edit Product Line handle. This method is called when click on edit button.
		 * @param productLine the product line to handle.
		 */
		self.editProductLine = function(productLine) {
			if (self.selectedRowIndex === -1) {
				self.originalProductLine = JSON.stringify(productLine);
				self.error = '';
				self.success = '';
				productLine.isEditing = true;
				self.validationModel = angular.copy(productLine);
				self.selectedProductLine = productLine;
				self.selectedRowIndex = self.getRowIndex();
			}
		};

		/**
		 * Calls confirmation modal to confirm delete action.
		 * @param productLine the product line to delete.
		 */
		self.deleteProductLine = function(productLine) {
			self.selectedDeletedProductLine = productLine;
			self.error = '';
			self.success = '';
			self.titleConfirm = self.PRODUCT_LINE_DELETE_MESSAGE_HEADER;
			self.messageConfirm = self.PRODUCT_LINE_DELETE_CONFIRM_MESSAGE_STRING;
			self.labelClose = 'No';
			self.allowDeleteProductLine = true;
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Calls the api to update the product line.
		 */
		self.updateProductLine = function() {
			self.error = '';
			self.success = '';
			if (self.selectedRowIndex > -1) {
				// editing mode.
				if (self.isProductLineChanged()) {
					if (self.validateProductLineBeforeUpdate()) {
						self.isWaitingForResponse = true;
						var tempProductLine = angular.copy(self.selectedProductLine);
						delete tempProductLine['isEditing'];
						productLineApi.updateProductLine(tempProductLine,
							function (results) {
								self.data[self.selectedRowIndex] = angular.copy(results.data);
								self.resetSelectedProductLine();
								self.isWaitingForResponse = false;
								self.checkAllFlag = false;
								self.success = results.message;
								if(self.isReturnToTab){
									$rootScope.success = self.success;
									$rootScope.isEditedOnPreviousTab = true;
								};
								self.returnToTab();
							},
							function (error) {
								self.fetchError(error);
							}
						);
					}
				} else {
					self.error = self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
				}
			} else {
				self.error = self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
			}

		};

		/**
		 * Resets product line back to original state.
		 * @param index
		 */
		self.resetProductLine = function(index) {
			self.error = '';
			self.success = '';
			self.data[index] = JSON.parse(self.originalProductLine);
			self.resetSelectedProductLine();
		};


		/**
		 * Returns the disabled status of button by  product line id.
		 *
		 * @param id the ProductLine id.
		 * @returns {boolean} the disable status.
		 */
		self.isDisabledButton = function (id) {
			return !(self.selectedRowIndex === -1 || self.selectedProductLine.id === id);

		};

		/**
		 * Returns the style for icon button.
		 *
		 * @param id the id of product line.
		 * @returns {*} the style.
		 */
		self.getDisabledButtonStyle = function (id) {
			if (self.isDisabledButton(id)) {
				return 'opacity: 0.5;'
			}
			return 'opacity: 1.0;';
		};

		/**
		 * Return edited row index.
		 *
		 * @returns {number}
		 */
		self.getRowIndex = function () {
			if (self.selectedProductLine == null) {
				return -1;
			}
			if (self.selectedProductLine.id === 0) {
				return 0;
			}
			for (var i = 0; i < self.data.length; i++) {
				if (self.data[i].id === self.selectedProductLine.id) {
					return i;
				}
			}
		};

		/**
		 * Validates the description before updates.
		 * @returns {boolean}
		 */
		self.validateProductLineBeforeUpdate = function() {
			var errorMessages = [];
			var message = '';
			if(!self.validationModel || self.validationModel.description.trim().length === 0) {
				message = '<li>' + self.PRODUCT_LINE_DESCRIPTION_MANDATORY_FIELD_ERROR + '</li>';
				errorMessages.push(message);
				self.showErrorOnTextBox('description', self.PRODUCT_LINE_DESCRIPTION_MANDATORY_FIELD_ERROR);
				return false;
			}
			return true;
		};

		/**
		 * Show red border on input text.
		 *
		 * @param id if of input text.
		 */
		self.showErrorOnTextBox = function (id, message) {
			if ($('#' + id).length > 0) {
				$('#' + id).addClass('ng-invalid ng-touched');
				$('#' + id).attr('title', message);
			}
		};

		/**
		 * Reset the status add or edit product line.
		 */
		self.resetSelectedProductLine = function () {
			self.selectedRowIndex = -1;
			self.selectedProductLine = null;
		};

		/**
		 * Checks if the product line is changed or not.
		 *
		 * @param productLine the product line.
		 * @param origData the list of original product line.
		 * @returns {boolean}
		 */
		self.isProductLineChanged = function () {
			var productLineTemp = angular.copy(self.selectedProductLine);
			delete productLineTemp['isEditing'];
			return JSON.stringify(productLineTemp) !== self.originalProductLine;

		};

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function () {
			if (self.isReturnToTab) {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		};

		/**
		 * Handles errors when description changes.
		 */
		self.onDescriptionChange = function () {
			var value = $('#description').val();
			if (value == null || value === undefined ||
				value.trim().length === 0) {
				self.showErrorOnTextBox('description', self.PRODUCT_LINE_DESCRIPTION_MANDATORY_FIELD_ERROR);
			}
		};

		/**
		 * Calls api to delete the product line.
		 */
		self.doDeleteProductLine = function () {
			self.doCloseModal();
			self.isWaitingForResponse = true;
			productLineApi.deleteProductLine( {productLineId: self.selectedDeletedProductLine.id},
				function (results) {
					self.isWaitingForResponse = false;
					self.success = results.message;
					self.selectedDeletedProductLine = null;
					self.tableParams.reload()
				},
				function (error) {
					self.fetchError(error);
				}
			);
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_PRODUCT_LINE, function () {
			if (self.selectedProductLine != null && self.isProductLineChanged()) {
				self.isReturnToTab = true;
				self.allowDeleteProductLine = false;
				self.titleConfirm = 'Confirmation';
				self.error = '';
				self.success = '';
				self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING;
				self.labelClose = 'No';
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});
	}
})();
