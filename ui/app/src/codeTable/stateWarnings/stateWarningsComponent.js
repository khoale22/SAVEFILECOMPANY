/*
 *
 * stateWarningComponent.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.12.0
 */
'use strict';

/**
 * The controller for the State Warnings Controller.
 *
 * @author vn87351
 * @updated vn86116
 * @since 2.12.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('stateWarningComponent', {
		templateUrl: 'src/codeTable/stateWarnings/stateWarnings.html',
		bindings: {seleted: '<'},
		controller: StateWarningsController
	});
	app.filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
	});

	StateWarningsController.$inject = ['$rootScope', 'StateWarningsApi', '$scope', 'ngTableParams', '$filter'];

	/**
	 * Constructs the controller.
	 */
	function StateWarningsController($rootScope, stateWarningsApi, $scope, ngTableParams, $filter) {

		var self = this;
		/**
		 * The default page number.
		 *
		 * @type {number}
		 */
		self.PAGE = 1;

		self.stateWarningsAddData = [];

		/**
		 * The default page size of main table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE_MAIN = 20;

		/**
		 * The default page size of modal table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE_MODAL = 15;
		/**
		 * Error Message
		 * @type {string}
		 */
		self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
		/**
		 * The unknown error message.
		 *
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * The error message when validate warning name text box on the modal.
		 *
		 * @type {string}
		 */
		self.REQUIRED_ABBREVIATION_ERROR = "State Warning Name is a mandatory field.";

		/**
		 * The error message when validate state code text box on the modal.
		 *
		 * @type {string}
		 */
		self.REQUIRED_STATE_CODE_ERROR = "State Warning Code is a mandatory field.";

		/**
		 * The error message when validate description text box on the modal.
		 *
		 * @type {string}
		 */
		self.REQUIRED_DESCRIPTION_ERROR = "State Warning Description is a mandatory field.";

		/**
		 * The title of confirmation dialog to delete State Warnings.
		 *
		 * @type {string}
		 */
		self.DELETE_STATE_WARNINGS_CONFIRM_TITLE = "Delete State Warnings";

		/**
		 * The title of confirmation dialog.
		 *
		 * @type {string}
		 */
		self.UNSAVED_DATA_CONFIRM_TITLE = "Confirmation";

		self.isReturnToTab = false;

		self.selectedRow = null;
		/**
		 * The message of confirmation dialog to delete State Warnings.
		 *
		 * @type {string}
		 */
		self.DELETE_STATE_WARNINGS_CONFIRM_MESSAGE = "Are you sure you want to delete the selected State Warnings ?";

		self.RETURN_TAB = 'returnTab';

		self.VALIDATE_STATE_WARNING = 'validateStateWarning'

		/**
		 * The message of confirmation dialog.
		 *
		 * @type {string}
		 */
		self.UNSAVED_DATA_CONFIRM_MESSAGE = "Unsaved data will be lost. Do you want to save the changes before continuing ?";

		/**
		 * Whether or not the controller is waiting for data.
		 *
		 * @type {boolean}
		 */
		self.isWaiting = false;

		self.stateWarningPre = null;

		/**
		 * check all flag.
		 *
		 * @type {boolean}
		 */
		self.checkAllValue = false;

		/**
		 * Success message from api.
		 *
		 * @type {string}
		 */
		self.success = null;

		/**
		 * Error message form api.
		 *
		 * @type {string}
		 */
		self.error = null;

		/**
		 * The error of modal.
		 *
		 * @type {string}
		 */
		self.errorPopup = null;

		/**
		 * The list of state warnings.
		 *
		 * @type {Array}
		 */
		self.stateWarnings = [];

		/**
		 * The list of state warnings to add/edit/delete.
		 *
		 * @type {Array}
		 */
		self.stateWarningsHandle = [];

		self.filteredDataOrigin = [];

		/**
		 * The list of state warnings for reset function.
		 *
		 * @type {Array}
		 */
		self.filteredData = [];

		/**
		 * The title of modal.
		 *
		 * @type {string}
		 */
		self.titleModal = "Add State Warnings";

		/**
		 * The flag to show Add button on the modal.
		 *
		 * @type {boolean}
		 */
		self.showAddButtonModal = false;

		/**
		 * The flag to show Reset button on the modal.
		 *
		 * @type {boolean}
		 */
		self.showResetButtonModal = false;

		/**
		 * The title of confirmation dialog.
		 *
		 * @type {string}
		 */
		self.titleConfirm = null;

		/**
		 * The message of confirmation dialog.
		 *
		 * @type {string}
		 */
		self.messageConfirm = null;

		/**
		 * The flag to check if it is confirming to delete or not.
		 *
		 * @type {boolean}
		 */
		self.isConfirmDelete = false;

		/**
		 * The flag to check if it is confirming unsaved data or not.
		 *
		 * @type {boolean}
		 */
		self.isConfirmUnsaved = false;

		self.isAddingActionCode = false;

		self.indexPre = null;

		self.defer = null;

		self.params = null;

		self.errorPopup = null;

		/**
		 * Initiates the construction of the State Warnings Controller.
		 */
		self.init = function () {
			self.isWaiting = true;
			if ($rootScope.isEditedOnPreviousTab) {
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
			self.findAllStateWarning();
		};

		/**
		 * get state warnings.
		 */
		self.findAllStateWarning = function () {
			stateWarningsApi.getAllStateWarnings().$promise.then(function (results) {
				self.callApiResponseSuccess(results);
			});
		};
		/**
		 * filter data warnings.
		 */
		self.filterDatas = function () {
			$scope.filter = {
				abbreviation: undefined,
				key: {stateCode: undefined},
				description: undefined
			};
			$scope.tableParams = new ngTableParams({
				page: self.PAGE,
				count: self.PAGE_SIZE_MAIN,
				filter: $scope.filter
			}, {
				counts: [],
				debugMode: true,
				data: self.stateWarningsHandle
			});
		};

		self.addMoreRowStateWarnings = function () {
			if (self.isValidDataModal(self.stateWarningsAddData))
				var stateWarning = self.initEmptyStateWarnings();
			self.stateWarningsAddData.push(stateWarning);
			self.tableModalParams.reload();
		}
		/**
		 * Handle when click Add button to display the modal.
		 */
		self.addNewStateWarnings = function () {
			var stateWarning = self.initEmptyStateWarnings();
			self.clearMessages();
			self.stateWarningsAddData.push(stateWarning);
			self.tableModalParams = new ngTableParams({
				page: self.PAGE,
				count: self.PAGE_SIZE_MAIN,
				filter: $scope.filter
			}, {
				counts: [],
				debugMode: true,
				data: self.stateWarningsAddData
			});
			$('#stateWarningsModal').modal({backdrop: 'static', keyboard: true});
		};

		self.clearFilter = function () {
			$scope.filter.key.stateCode = undefined;
			$scope.filter.abbreviation = undefined;
			$scope.filter.description = undefined;
		}

		self.editStateWarning = function (stateWarning) {
			self.clearMessages();
			self.selectedRow = true;
			self.isEditing = true;
			if (self.stateWarningPre !== null) {
				self.stateWarningPre.isEditing = false;
				stateWarning.isEditing = true;
				self.stateWarningPre = stateWarning;
				self.stateWarningPreOrigin = angular.copy(stateWarning);
			}
			else {
				if (self.stateWarningPre === null) {
					stateWarning.isEditing = true;
					self.stateWarningPre = stateWarning;
					self.stateWarningPreOrigin = angular.copy(stateWarning);
				}
			}
		};

		self.updateStateWarnings = function (stateWarning) {
			self.clearMessages();
			self.closeConfirmPopup();
			var updateData = [{
				"key": {
					"stateCode": null,
					"warningCode": null
				},
				"abbreviation": null,
				"description": null
			}];
			if (self.isDataChanged(stateWarning)) {
				if (self.isValidDataModal(self.stateWarningsHandle)) {
					self.isWaiting = true;
					updateData[0]["key"]["stateCode"] = stateWarning["key"]["stateCode"];
					updateData[0]["key"]["warningCode"] = stateWarning["key"]["warningCode"];
					updateData[0].abbreviation = stateWarning.abbreviation;
					updateData[0].description = stateWarning.description;
					stateWarningsApi.updateStateWarnings(updateData, function (results) {
							self.callApiResponseSuccess(results.data);
							self.success = results.message;
							if (self.isReturnToTab) {
								$rootScope.success = self.success;
								$rootScope.isEditedOnPreviousTab = true;
							}
							self.returnToTab(true);
							self.isAddingActionCode = false;
							self.isEditing = false;
							self.selectedRow = null;
							self.stateWarningPreOrigin = null;
						}, self.fetchError
					);
				}
			}
			else {
				self.error = "";
				self.error = self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
			}
		};

		/**
		 * Check data change
		 *
		 * @param stateWarning
		 * @returns {boolean}
		 */
		self.isDataChanged = function (stateWarning) {
			var lstChange = [];
			if ($.trim(stateWarning['abbreviation']) !== $.trim(self.stateWarningPreOrigin['abbreviation']) ||
				$.trim(stateWarning['key']) !== $.trim(self.stateWarningPreOrigin['key']) ||
				$.trim(stateWarning['key']['stateCode']) !== $.trim(self.stateWarningPreOrigin['key']['stateCode']) ||
				$.trim(stateWarning['description']) !== $.trim(self.stateWarningPreOrigin['description'])) {
				return true;
			}
			return false;
		}

		/**
		 * Call ws to add a state warning.
		 */
		self.doAddStateWarnings = function () {
			if (self.isValidDataModal(self.stateWarningsAddData)) {
				$('#confirmModal').modal("hide");
				$('#stateWarningsModal').modal("hide");
				self.isWaiting = true;
				stateWarningsApi.updateStateWarnings(self.stateWarningsAddData, function (results) {
						self.callApiResponseSuccess(results.data);
						self.success = results.message;
						self.isAddingActionCode = false;
						self.isEditing = false;
						self.stateWarningPreOrigin = null;
						self.stateWarningsAddData = [];
					}, self.fetchError
				);
			}
		};

		/**
		 * Handle when click Delete button to display the modal.
		 */
		self.deleteStateWarnings = function (stateWarning) {
			self.isDelete = true;
			self.stateWarningPreOrigin = angular.copy(stateWarning);
			self.clearMessages();
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			self.showConfirmDelete = true;
			self.messageConfirm = self.DELETE_STATE_WARNINGS_CONFIRM_MESSAGE;
			self.titleConfirm = self.DELETE_STATE_WARNINGS_CONFIRM_TITLE;
		};

		/**
		 * Handle call ws when confirm Delete action.
		 */
		self.doDeleteStateWarnings = function () {
			var deleteData = [];
			self.clearMessages();
			deleteData.push(self.stateWarningPreOrigin);
			self.isWaiting = true;
			stateWarningsApi.deleteStateWarnings(deleteData, function (results) {
					self.callApiResponseSuccess(results.data);
					self.success = results.message;
					self.isDelete = false;
					self.stateWarningPreOrigin = null;
				}, self.fetchError
			);
			self.closeConfirmPopup();
		};

		/**
		 * Reset data edited.
		 *
		 * @param stateWarning
		 */
		self.cancel = function (stateWarning) {
			self.clearMessages();
			self.selectedRow = null;
			stateWarning.isEditing = false;
			self.isEditing = false;
			stateWarning["key"]["stateCode"] = self.stateWarningPreOrigin["key"]["stateCode"];
			stateWarning["key"]["warningCode"] = self.stateWarningPreOrigin["key"]["warningCode"];
			stateWarning.abbreviation = self.stateWarningPreOrigin.abbreviation;
			stateWarning.description = self.stateWarningPreOrigin.description;
			$scope.tableParams.reload();
			self.stateWarningPreOrigin = null;

		}

		/**
		 * Close add popup and confirm popup.
		 */
		self.doCloseModal = function () {
			$('#confirmModal').modal("hide");
			$('#stateWarningsModal').modal("hide");
			self.clearMessages();
			self.isReturnToTab = false;
		};

		/**
		 * Close confirm popup
		 */
		self.closeConfirmPopup = function () {
			$('#confirmModal').modal("hide");
			$('.modal-backdrop').attr('style', ' ');
		}

		/**
		 * Handle when call api controller and response results successfully.
		 *
		 * @param data
		 */
		self.callApiResponseSuccess = function (data) {
			self.stateWarnings = angular.copy(data);
			self.stateWarningsHandle = angular.copy(data);
			angular.forEach(self.stateWarningsHandle, function (value) {
				value.isEditing = false;
			});
			self.filterDatas();
			self.checkAllValue = false;
			self.isWaiting = false;
		};

		/**
		 * Check mandatory fields on the modal.
		 *
		 * @returns {boolean}
		 */
		self.isValidDataModal = function (stateWarningsList) {
			var errorMessages = [];
			var errorMessage = '';
			for (var i = 0; i < stateWarningsList.length; i++) {
				if (self.isNullOrEmpty(stateWarningsList[i].abbreviation)) {
					errorMessage = "<li>" + self.REQUIRED_ABBREVIATION_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) == -1) {
						errorMessages.push(errorMessage);
					}
					stateWarningsList[i].addClass = 'active-tooltip ng-invalid ng-touched';
				}
				if (self.isNullOrEmpty(stateWarningsList[i].key) ||
					stateWarningsList[i]["key"]["stateCode"] === null) {
					errorMessage = "<li>" + self.REQUIRED_STATE_CODE_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) == -1) {
						errorMessages.push(errorMessage);
					}
					stateWarningsList[i].addClass = 'active-tooltip ng-invalid ng-touched';
				}
				if (self.isNullOrEmpty(stateWarningsList[i].description)) {
					errorMessage = "<li>" + self.REQUIRED_DESCRIPTION_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) == -1) {
						errorMessages.push(errorMessage);
					}
					stateWarningsList[i].addClass = 'active-tooltip ng-invalid ng-touched';
				}
				if (errorMessages.length == 3) {
					break;
				}
			}
			if (errorMessages.length > 0) {
				var errorMessagesAsString = 'State Warning:';
				angular.forEach(errorMessages, function (errorMessage) {
					errorMessagesAsString += errorMessage;
				});
				self.errorPopup = errorMessagesAsString;
				return false;
			}
			return true;
		};

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
		 * Clear all the messages when click buttons.
		 */
		self.clearMessages = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.stateWarningsAddData = [];
		};
		/**
		 * Initiate empty state warnings to display in the modal.
		 *
		 * @returns {{}}
		 */
		self.initEmptyStateWarnings = function () {
			var stateWarning = {
				"abbreviation": "",
				"description": "",
				"key": {
					"stateCode": null
				}
			};
			return stateWarning;
		};

		/**
		 * If there is an error this will display the error.
		 *
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message !== null && error.data.message !== "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			} else {
				self.error = self.UNKNOWN_ERROR;
			}
			if (self.isReturnToTab) $rootScope.error = self.error;
		};

		/**
		 * Handle when click close in add popup but have data changed to show popup confirm.
		 */
		self.closeModalUnsavedData = function () {
			if (self.stateWarningsAddData[0].abbreviation.length !== 0 || self.stateWarningsAddData[0].description.length === 0
				&& self.stateWarningsAddData[0].key.stateCode !== null) {
				self.titleConfirm = self.UNSAVED_DATA_CONFIRM_TITLE;
				self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE;
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			}
			else {
				self.isValidDataModal(self.stateWarningsAddData);
				$('#stateWarningsModal').modal("hide");
				self.stateWarningsAddData = [];
			}
		}

		/**
		 * Handle when switch to other tab.
		 */
		$scope.$on(self.VALIDATE_STATE_WARNING, function () {
			if (self.selectedRow !== null) {
				if (self.stateWarningPre !== null) {
					if (self.isDataChanged(self.stateWarningPre)) {
						self.isReturnToTab = true;
						// show popup
						self.titleConfirm = 'Confirmation';
						self.error = '';
						self.success = '';
						self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE;
						$('#confirmModal').modal({backdrop: 'static', keyboard: true});
					}
					else $rootScope.$broadcast(self.RETURN_TAB);
				}
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function (clickYes) {
			self.closeConfirmPopup();
			if (self.isReturnToTab && clickYes) {
				$rootScope.$broadcast(self.	RETURN_TAB);
			}
			else {
				$('#confirmModal').on('hidden.bs.modal', function () {
					$rootScope.$broadcast(self.RETURN_TAB);
					$scope.$apply();
				});
			}
		};

		/**
		 * Handle when click yes in confirm popup when switch tab and had data changed to save and switch tab.
		 */
		self.updateStateWarningsTabchange = function () {
			self.updateStateWarnings(self.stateWarningPre);
		}
	}
})();
