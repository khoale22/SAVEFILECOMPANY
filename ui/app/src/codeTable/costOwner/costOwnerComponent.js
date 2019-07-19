/*
 * costOwnerComponent.js
 *
 * Copyright (c) 2019 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * Component to support the page that allows users to show Cost Owner.
 *
 * @author vn70529
 * @since 2.41.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('costOwnerComponent', {
		templateUrl: 'src/codeTable/costOwner/costOwner.html',
		controller: costOwnerController
	});

	costOwnerController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'costOwnerApi', '$timeout'];

	/**
	 * Constructs for cost owner Controller.
	 */
	function costOwnerController($rootScope, $scope, ngTableParams, costOwnerApi, $timeout) {

		var self = this;

		/**
		 * Empty model.
		 */
		self.EMPTY_MODEL = {
			costOwnerId: '',
			costOwnerName: ''
		};

		/**
		 * Selected edit cost owner.
		 * @type {null}
		 */
		self.selectedCostOwner = null;

		/**
		 * The original, unedited cost owner.
		 * @type {null}
		 */
		self.originalCostOwner = null;

		/**
		 * Selected row index
		 * @type {number}
		 */
		self.selectedRowIndex = -1;

		/**
		 * Validation model.
		 */
		self.validationModel = angular.copy(self.EMPTY_MODEL);

		/**
		 * Start position of page that want to show on cost owner table.
		 *
		 * @type {number}
		 */
		const PAGE = 1;

		/**
		 * The number of records to show on the cost owner table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;
		self.firstSearch = false;
		self.tableParams = null;
		var previousNameFilter = null;
		var previousIdFilter = null;

		/**
		 * Unknown error messages.
		 *
		 * @type {string}
		 */
		const UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * No data changes message.
		 *
		 * @type {string}
		 */
		const THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';

		/**
		 * Mandatory field error message.
		 *
		 * @type {string}
		 */
		self.COST_OWNER_NAME_MANDATORY_FIELD_ERROR = 'Cost Owner name is a mandatory field.';

		/**
		 * Unsaved data confirm title.
		 *
		 * @type {string}
		 */
		const UNSAVED_DATA_CONFIRM_TITLE = "Confirmation";

		/**
		 * Unsaved data confirm message.
		 *
		 * @type {string}
		 */
		const UNSAVED_DATA_CONFIRM_MESSAGE = "Unsaved data will be lost. Do you want to save the changes before continuing?";

		/**
		 * Delete message header.
		 *
		 * @type {string}
		 */
		const COST_OWNER_DELETE_MESSAGE_HEADER = 'Delete Cost Owner';

		/**
		 * Confirm delete message.
		 *
		 * @type {string}
		 */
		const COST_OWNER_DELETE_CONFIRM_MESSAGE_STRING = 'Are you sure you want to delete the selected Cost Owner?';

		/**
		 * Duplicate cost owner message.
		 *
		 * @type {string}
		 */
		const DUPLICATE_COST_OWNERS = "Duplicate Cost Owners. ";

		/**
		 * Return tab key.
		 * @type {string}
		 */
		self.RETURN_TAB = 'returnTab';
		self.isAddingCostOwner = false;
		self.newCostOwners = [];

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.newSearch();
			if ($rootScope.isEditedOnPreviousTab) {
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
		};

		/**
		 * Reset the table with current filter. If the table has not been created, create the table. Else reload the
		 * table.
		 */
		self.newSearch = function () {
			self.isWaitingForResponse = true;
			self.firstSearch = true;
			if (self.tableParams == null) {
				self.createCostOwnersTable();
			} else {
				self.tableParams.reload();
			}
		};

		/**
		 * Create cost owner table.
		 */
		self.createCostOwnersTable = function () {
			self.tableParams = new ngTableParams({
				page: PAGE, // Show first page
				count: self.PAGE_SIZE // Count per page
			}, {
				counts: [],
				getData: function ($defer, params) {
					self.data = null;

					self.defer = $defer;
					self.dataResolvingParams = params;

					var includeCount = false;

					var id = params.filter()['id'];
					var name = params.filter()['name'];

					if (typeof id === 'undefined') {
						id = '';
					}
					if (typeof name === 'undefined') {
						name = '';
					}

					if (id !== previousIdFilter || name !== previousNameFilter) {
						self.firstSearch = true;
					}

					if (self.reloadAfterDeleting === true) {
						includeCount = true;
						self.reloadAfterDeleting = false;
					}

					if (self.firstSearch) {
						includeCount = true;
						params.page(1);
						self.firstSearch = false;
					}

					self.resetSelectedCostOwner();
					previousIdFilter = id;
					previousNameFilter = name;
					self.loadData(includeCount, params.page() - 1, id, name);
				}
			});

		};

		/**
		 * Initiate a call to get the list of attribute maintenance records.
		 *
		 * @param includeCount Whether or not to include getting record counts.
		 * @param page The page of data to ask for.
		 * @param id ID for attribute filtering.
		 * @param name Cost owner name for attribute filtering.
		 */
		self.loadData = function (includeCount, page, id, name) {
			costOwnerApi.findCostOwners({
				id: id,
				name: name,
				page: page,
				pageSize: self.PAGE_SIZE,
				includeCount: includeCount
			}, self.handleSuccess, self.handleError);
		};

		/**
		 * Clear filter.
		 */
		self.clearFilter = function () {
			self.dataResolvingParams.filter()["id"] = null;
			self.dataResolvingParams.filter()["name"] = null;
			self.tableParams.reload();
			self.error = '';
			self.success = '';
		};

		/**
		 * Edit cost owner. This method is called when click on edit button.
		 *
		 * @param costOwner The cost owner to handle.
		 */
		self.editCostOwner = function (costOwner) {
			if (self.selectedRowIndex === -1) {
				self.originalCostOwner = JSON.stringify(costOwner);
				self.error = '';
				self.success = '';
				costOwner.isEditing = true;
				self.validationModel = angular.copy(costOwner);
				self.selectedCostOwner = costOwner;
				self.selectedRowIndex = self.getRowIndex();
			}
		};

		/**
		 * Call confirmation modal to confirm delete action.
		 *
		 * @param costOwner The cost owner to delete.
		 */
		self.deleteCostOwner = function (costOwner) {
			self.selectedDeletedCostOwner = costOwner;
			self.error = '';
			self.success = '';
			self.titleConfirm = COST_OWNER_DELETE_MESSAGE_HEADER;
			self.messageConfirm = COST_OWNER_DELETE_CONFIRM_MESSAGE_STRING;
			self.labelClose = 'No';
			self.allowDeleteCostOwner = true;
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Call the api to update the cost owner.
		 */
		self.updateCostOwner = function () {
			self.error = '';
			self.success = '';
			if (self.selectedRowIndex > -1) {
				// Editing mode.
				if (self.isCostOwnerChanged()) {
					if (self.isValidUpdateCostOwner()) {
						self.isWaitingForResponse = true;
						var tempCostOwner = angular.copy(self.selectedCostOwner);
						delete tempCostOwner['isEditing'];
						costOwnerApi.updateCostOwner(tempCostOwner,
							function (results) {
								self.data[self.selectedRowIndex] = angular.copy(results.data);
								self.resetSelectedCostOwner();
								self.isWaitingForResponse = false;
								self.checkAllFlag = false;
								self.success = results.message;
								if (self.isReturnToTab) {
									$rootScope.success = self.success;
									$rootScope.isEditedOnPreviousTab = true;
								}
								self.returnToTab();
							},
							self.handleError
						);
					}
				} else {
					self.error = THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
				}
			} else {
				self.error = THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
			}
		};

		/**
		 * Handle when click Add button to display the modal.
		 */
		self.addNewCostOwner = function () {
			self.resetCostOwner(self.data.indexOf(self.selectedCostOwner));
			self.isAddingCostOwner = true;
			var costOwner = self.initEmptyNewCostOwner();
			self.clearMessages();
			self.newCostOwners.push(costOwner);
			self.tableModalParams = new ngTableParams({
				page: PAGE,
				count: self.PAGE_SIZE
			}, {
				counts: [],
				data: self.newCostOwners
			});
			$('#addCostOwnerModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Remove selected row from modal table.
		 *
		 * @param index The index to remove.
		 */
		self.deleteRow = function (index) {
			self.errorPopup = '';
			self.newCostOwners.splice(index, 1);
			self.previousPage(index);
			self.tableModalParams.reload();
			self.isValidNewCostOwner();
		};

		/**
		 * Add one more row to add new cost owner modal table.
		 */
		self.addRow = function () {
			if (self.isValidNewCostOwner()) {
				$scope.addForm.$setUntouched();
				var newCostOwner = self.initEmptyNewCostOwner();
				self.newCostOwners.push(newCostOwner);
				self.nextPage();
				self.tableModalParams.reload();
				self.errorPopup = '';
			}
		};

		/**
		 * Save new cost owners.
		 */
		self.saveNewCostOwners = function () {
			if (self.isValidNewCostOwner()) {
				costOwnerApi.addCostOwners(self.newCostOwners,
					function (response) {
						if (response.message.indexOf("Successfully") !== -1) {
							self.success = response.message;
							self.closeAllModals();
							self.isAddingCostOwner = false;
							self.newSearch();
						} else {
							self.closeConfirmModal();
							self.handleAddErrorMessage(response);
						}
					}, function (error) {
						self.closeConfirmModal();
						self.errorPopup = self.getErrorMessage(error);
					});
			} else {
				self.closeConfirmModal();
			}
		};

		/**
		 * Handle when click close in add modal but have data changed to show modal confirm.
		 */
		self.closeModalUnsavedData = function () {
			if (self.newCostOwners.length !== 0 && self.newCostOwners[0].costOwnerName.length !== 0) {
				self.titleConfirm = UNSAVED_DATA_CONFIRM_TITLE;
				self.messageConfirm = UNSAVED_DATA_CONFIRM_MESSAGE;
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			} else {
				self.isValidNewCostOwner();
				$('#addCostOwnerModal').modal("hide");
				self.newCostOwners = [];
			}
		};

		/**
		 * Close confirm modal when click no.
		 */
		self.closeConfirmModal = function () {
			if ($('#confirmModal').is(':visible')) {
				$('#confirmModal').modal("hide");
				$('.modal-backdrop').attr('style', ' ');
				$('#confirmModal').on('hidden.bs.modal', function () {
					angular.element(document.body).addClass("modal-open");
				});
			}
		};

		/**
		 * Close add modal and confirm modal.
		 */
		self.closeAllModals = function () {
			self.allowDeleteCostOwner = false;
			self.isAddingCostOwner = false;
			if (self.isReturnToTab) {
				$('#addCostOwnerModal').modal("hide");
				$('#confirmModal').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			} else {
				$('#confirmModal').modal("hide");
				$('#addCostOwnerModal').modal("hide");
			}
		};

		/**
		 * Validate new cost owner.
		 *
		 * @returns {boolean} the validation status of new cost owner.
		 */
		self.isValidNewCostOwner = function () {

			self.clearInputInvalidClass();
			var errorMessages = [];
			var errorMessage = '';

			for (var i = 0; i < self.newCostOwners.length; i++) {
				// Check empty cost owners.
				if (self.isNullOrEmpty(self.newCostOwners[i].costOwnerName)) {
					errorMessage = "<li>" + self.COST_OWNER_NAME_MANDATORY_FIELD_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) === -1) {
						errorMessages.push(errorMessage);
					}
					self.addInvalidClass(i);
				} else {
					// Check duplicate cost owner names.
					for (var k = i + 1; k < self.newCostOwners.length; k++) {
						if (self.newCostOwners[k].costOwnerName.toUpperCase() === self.newCostOwners[i].costOwnerName.toUpperCase()) {
							errorMessage = "<li>" + DUPLICATE_COST_OWNERS + "</li>";
							if (errorMessages.indexOf(errorMessage) === -1) {
								errorMessages.push(errorMessage);
							}
							self.addInvalidClass(i);
							self.addInvalidClass(k);
						}
					}
				}
			}

			if (errorMessages.length > 0) {
				var errorMessagesAsString = '';
				angular.forEach(errorMessages, function (errorMessage) {
					errorMessagesAsString += errorMessage;
				});
				self.errorPopup = errorMessagesAsString;
				return false;
			}
			return true;
		};

		/**
		 * Validates the cost owner name before updates.
		 *
		 * @returns {boolean} the validation status of cost owner name.
		 */
		self.isValidUpdateCostOwner = function () {
			if (!self.validationModel || self.validationModel.costOwnerName.trim().length === 0) {
				self.error = self.COST_OWNER_NAME_MANDATORY_FIELD_ERROR;
				self.showErrorOnTextBox('name', self.COST_OWNER_NAME_MANDATORY_FIELD_ERROR);
				return false;
			}
			return true;
		};

		/**
		 * Checks if the cost owner is changed or not.
		 *
		 * @returns {boolean} the change status of cost owner.
		 */
		self.isCostOwnerChanged = function () {
			var costOwnerTemp = angular.copy(self.selectedCostOwner);
			delete costOwnerTemp['isEditing'];
			return JSON.stringify(costOwnerTemp) !== self.originalCostOwner;
		};

		/**
		 * Return edited row index.
		 *
		 * @returns {number} the row index of edited cost owner.
		 */
		self.getRowIndex = function () {
			if (self.selectedCostOwner == null) {
				return -1;
			}
			if (self.selectedCostOwner.costOwnerId === 0) {
				return 0;
			}
			for (var i = 0; i < self.data.length; i++) {
				if (self.data[i].costOwnerId === self.selectedCostOwner.costOwnerId) {
					return i;
				}
			}
		};

		/**
		 * Call api to delete the cost owner when click yes in confirm modal.
		 */
		self.doDeleteCostOwner = function () {
			self.closeAllModals();
			self.isWaitingForResponse = true;
			costOwnerApi.deleteCostOwner({costOwnerId: self.selectedDeletedCostOwner.costOwnerId},
				function (results) {
					if (self.data[0] === self.selectedDeletedCostOwner && self.data.length === 1 && self.tableParams.page() > 1) {
						self.tableParams.page(self.tableParams.page() - 1);
					}
					self.reloadAfterDeleting = true;
					self.isWaitingForResponse = false;
					self.success = results.message;
					self.selectedDeletedCostOwner = null;
					self.tableParams.reload();
				},
				self.handleError
			);
		};

		/**
		 * Update next page in add modal.
		 */
		self.nextPage = function () {
			if (self.newCostOwners.length > self.PAGE_SIZE && self.newCostOwners.length % self.PAGE_SIZE === 1) {
				self.tableModalParams.page(self.tableModalParams.page() + 1);
				$timeout(function () {
					$('#costOwnerName' + (self.newCostOwners.length - 1)).focus();
				}, 500);
			}
		};

		/**
		 * Update previous page in add modal.
		 */
		self.previousPage = function (index) {
			if (index % self.PAGE_SIZE === 0 && ((self.tableModalParams.page() - 1) * self.PAGE_SIZE) === (self.newCostOwners.length) && self.tableModalParams.page() > 1) {
				self.tableModalParams.page(self.tableModalParams.page() - 1);
			}
		};

		/**
		 * Initiate empty cost owner to display in the modal.
		 *
		 * @returns an empty cost owner.
		 */
		self.initEmptyNewCostOwner = function () {
			return angular.copy(self.EMPTY_MODEL);
		};

		/**
		 * Clear all the messages when click buttons.
		 */
		self.clearMessages = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.newCostOwners = [];
		};

		/**
		 * Callback for when data is successfully returned from the backend.
		 *
		 * @param results The data returned from the backend.
		 */
		self.handleSuccess = function (results) {
			self.isWaitingForResponse = false;
			self.data = results.data;
			self.defer.resolve(self.data);

			if (results.complete) {
				self.totalPages = results.pageCount;
				self.totalRecords = results.recordCount;
				self.dataResolvingParams.total(self.totalRecords);
			}
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.handleError = function (error) {
			self.isWaitingForResponse = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
			if (self.isReturnToTab) {
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			}
			self.isReturnToTab = false;
		};

		/**
		 * Handle error message. It will show error message and red border on text box.
		 */
		self.handleAddErrorMessage = function (response) {
			var tempMsgs = response.data;
			self.errorPopup = "<li>" + response.message + "</li>";
			for (var i = 0; i < tempMsgs.length; i++) {
				for (var j = 0; j < self.newCostOwners.length; j++) {
					if (self.newCostOwners[j].costOwnerName.toUpperCase() === tempMsgs[i].costOwnerName.toUpperCase()) {
						self.addInvalidClass(j);
					}
				}
			}
		};

		/**
		 * Return error message.
		 *
		 * @param error The error.
		 * @returns {string} the error message.
		 */
		self.getErrorMessage = function (error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				}
				return error.data.error;
			}
			return UNKNOWN_ERROR;
		};

		/**
		 * Reset cost owner back to original state.
		 *
		 * @param index The index of cost owners list displaying in current page.
		 */
		self.resetCostOwner = function (index) {
			self.error = '';
			self.success = '';
			self.data[index] = JSON.parse(self.originalCostOwner);
			self.resetSelectedCostOwner();
		};

		/**
		 * Reset the status of editing cost owner.
		 */
		self.resetSelectedCostOwner = function () {
			self.selectedRowIndex = -1;
			self.selectedCostOwner = null;
		};

		/**
		 * Add invalid red border.
		 *
		 * @param index The index of new cost owners list.
		 */
		self.addInvalidClass = function (index) {
			self.newCostOwners[index].addClass = 'active-tooltip ng-invalid ng-touched';
		};

		/**
		 * Remove invalid red border.
		 */
		self.clearInputInvalidClass = function () {
			for (var i = 0; i < self.newCostOwners.length; i++) {
				self.newCostOwners[i].addClass = 'ng-valid';
			}
		};

		/**
		 * Determine if the filter has been cleared or not.
		 */
		self.isFilterCleared = function () {
			if (!self.dataResolvingParams) {
				return true;
			}
			if (!self.dataResolvingParams.filter()["id"] &&
				!self.dataResolvingParams.filter()["name"]) {
				return true;
			}
			return false;
		};

		/**
		 * Return the disabled status of button by cost owner id.
		 *
		 * @param id The CostOwner id.
		 * @returns {boolean} the disabled status.
		 */
		self.isDisabledButton = function (id) {
			return !(self.selectedRowIndex === -1 || self.selectedCostOwner.costOwnerId === id);
		};

		/**
		 * Return the disabled status of save new cost owners button.
		 *
		 * @returns {boolean} the disabled status.
		 */
		self.isDisabledSaveButton = function () {
			return self.newCostOwners.length === 0;
		};

		/**
		 * Show red border on input text and tooltip message.
		 *
		 * @param id The id of input text.
		 * @param message The tooltip message.
		 */
		self.showErrorOnTextBox = function (id, message) {
			if ($('#' + id).length > 0) {
				$('#' + id).addClass('ng-invalid ng-touched');
				$('#' + id).attr('title', message);
			}
		};

		/**
		 * Check object null or empty.
		 *
		 * @param object The object to check.
		 * @returns {boolean} true if object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === null || !object || object === '';
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
		 * Handle errors when cost owner name changes.
		 */
		self.onCostOwnerNameChange = function () {
			var value = $('#name').val();
			if (value == null || value === undefined ||
				value.trim().length === 0) {
				self.showErrorOnTextBox('name', self.COST_OWNER_NAME_MANDATORY_FIELD_ERROR);
			}
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on('validateCostOwner', function () {
			if (self.selectedCostOwner != null && self.isCostOwnerChanged()) {
				self.isReturnToTab = true;
				self.allowDeleteCostOwner = false;
				self.titleConfirm = 'Confirmation';
				self.error = '';
				self.success = '';
				self.messageConfirm = UNSAVED_DATA_CONFIRM_MESSAGE;
				self.labelClose = 'No';
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});
	}
})();