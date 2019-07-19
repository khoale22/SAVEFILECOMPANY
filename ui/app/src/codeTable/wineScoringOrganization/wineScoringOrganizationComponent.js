/*
 * wineScoringOrganizationComponent.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * Component to support the page that allows users to show wine scoring organization.
 *
 * @author vn70529
 * @since 2.12
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('wineScoringOrganizationComponent', {
		templateUrl: 'src/codeTable/wineScoringOrganization/wineScoringOrganization.html',
		bindings: {
			seleted: '<'
		},
		controller: wineScoringOrganizationController
	});
	wineScoringOrganizationController.$inject = ['$scope', 'ngTableParams', 'WineScoringOrganizationApi', '$rootScope'];

	/**
	 * Constructs for wine scoring organization Controller.
	 *
	 * @param $scope
	 * @param ngTableParams The API to set up the report table.
	 * @param wineScoringOrganizationApi
	 */
	function wineScoringOrganizationController($scope, ngTableParams, wineScoringOrganizationApi, $rootScope) {

		var self = this;

		/**
		 * Messages
		 * @type {string}
		 */
		self.SCORING_ORG_LIMIT_DELETE_CONFIRM_MESSAGE_STRING = 'Deleting Scoring Organizations is limited to 15 records at a time.';
		self.DELETE_ITEM_MESSAGE_STRING = 'Are you sure you want to delete the selected Scoring Organization?';
		self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
		self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING = 'Unsaved data will be lost. Do you want to save the changes before continuing?';

		self.UNKNOWN_ERROR = 'An unknown error occurred.';
		self.SCORING_ORG_NAME_MANDATORY_FIELD_ERROR = 'Scoring Organization Name is a mandatory field.';
		self.SCORING_ORG_DESCRIPTION_MANDATORY_FIELD_ERROR = 'Scoring Organization Description is a mandatory field.';
		self.CONFIRM_TITLE = "Confirmation";
		/**
		 * Holds the title for add endi dialog.
		 * @type {string}
		 */
		self.ADD_NEW_TITLE_HEADER = 'Add New Scoring Organization';
		self.EDIT_TITLE_HEADER = 'Edit Scoring Organization';

		/**
		 * Action for add edit button.
		 * @type {string}
		 */
		self.ADD_ACTION = 'ADD';

		self.EDIT_ACTION = 'EDIT';

		/**
		 * Start position of page that want to show on WineScoringOrganization table
		 *
		 * @type {number}
		 */
		self.PAGE = 1;

		/**
		 * The number of records to show on the  WineScoringOrganization table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;

		/**
		 * The number of records to show on the  WineScoringOrganization table on edit mode.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE_ON_EDIT_MODEL = 15;

		/**
		 * Holds the total of rows to show scroll on add/edit
		 * @type {number}
		 */
		self.TOTAL_ROWS_TO_SHOW_SCROLL = 15;

		/**
		 * Holds the list of wineScoringOrganizations.
		 *
		 * @type {Array} the list of ScoringOrganizations.
		 */
		self.wineScoringOrganizations = [];

		self.isConfirmSaveScoringOrganization = false;

		self.wineScoringOrganizationsPre = null;

		self.wineScoringOrganizationsPreOrigin = null;

		self.isAddingActionCode = false;

		self.wineScoringOrganizationsAddData = [];

		self.RETURN_TAB = "returnTab";

		self.isReturnToTab = false;

		self.selectedRow = false;

		self.VALIDATE_WINE_SCORING_ORGANIZATION = "validateWineScoringOrganization";

		/**
		 * Initialize the controller.
		 */
		self.init = function () {
			if ($rootScope.isEditedOnPreviousTab) {
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
			self.loadWineScoringOrganization();
		}

		/**
		 * Initial the table to show list of Wine Scoring Organizations.
		 */
		self.loadWineScoringOrganization = function () {

			self.isWaitingForResponse = true;
			wineScoringOrganizationApi.getAllWineScoringOrganizationsOrderById().$promise.then(function (response) {
				self.setWineScoringOrganizations(response);
				self.isWaitingForResponse = false;
			});
		};

		/**
		 * Initial wine Scoring Organizations table.
		 */
		self.initWineScoringOrganizationsTable = function () {
			$scope.tableParams = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.PAGE_SIZE, /* count per page */
			}, {
				counts: [],
				debugMode: false,
				data: self.wineScoringOrganizationsHandle
			});
		}

		/**
		 * Initial country code table for add edit mode.
		 */
		self.initWineScoringOrganizationsTableForAddEditMode = function () {
			$scope.tableParamsForAddEditMode = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.action == self.EDIT_ACTION ? self.PAGE_SIZE_ON_EDIT_MODEL : 100000000, /* count per page */
			}, {
				counts: [],
				debugMode: false,
				data: self.wineScoringOrganizationsAddData
			});
		}

		/**
		 * Sets wine scoring Organizations into table modal.
		 */
		self.setWineScoringOrganizations = function (result) {
			self.wineScoringOrganizations = angular.copy(result);
			self.wineScoringOrganizationsHandle = angular.copy(result);
			angular.forEach(self.wineScoringOrganizationsHandle, function (value) {
				value.isEditing = false;
				value['scoringOrganizationName'] = value['scoringOrganizationName'].trim();
				value['scoringOrganizationDescription'] = value['scoringOrganizationDescription'].trim();
			});
			self.initWineScoringOrganizationsTable();
		}

		/**
		 * Add new wine Scoring Organization handle when add new button click.
		 */
		self.addNewWineScoringOrganization = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.clearMessages();
			$('#addEditContainer').attr('style', '');
			self.resetValidation();
			var wineScoringOrganization = self.createWineScoringOrganizationEmpty();
			self.wineScoringOrganizationsAddData.push(wineScoringOrganization);
			self.titleModel = self.ADD_NEW_TITLE_HEADER;
			self.action = self.ADD_ACTION;
			self.initWineScoringOrganizationsTableForAddEditMode();
			$('#wineScoringOrganizationModal').modal({backdrop: 'static', keyboard: true});
		}

		/**
		 * Edit Wine Scoring Organization handle. This method is called when click on edit button.
		 */
		self.editWineScoringOrganization = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.wineScoringOrganizationsHandle = [];
			self.resetValidation();
			angular.forEach(self.wineScoringOrganizations, function (value) {
				var scoringOrganizationEdit = angular.copy(value);
				if (scoringOrganizationEdit.isSelected == true) {
					self.wineScoringOrganizationsHandle.push(scoringOrganizationEdit);
				}
			});
			self.wineScoringOrganizationsHandleOrig = angular.copy(self.wineScoringOrganizationsHandle);
			self.titleModel = self.EDIT_TITLE_HEADER;
			self.action = self.EDIT_ACTION;
			self.initWineScoringOrganizationsTableForAddEditMode();
			$('#wineScoringOrganizationModal').modal({backdrop: 'static', keyboard: true});
		}

		/**
		 * Create a empty wine Scoring Organization object.
		 * @returns {{}}
		 */
		self.createWineScoringOrganizationEmpty = function () {
			var wineScoringOrganization = {};
			wineScoringOrganization['scoringOrganizationId'] = 0;
			wineScoringOrganization['scoringOrganizationName'] = '';
			wineScoringOrganization['scoringOrganizationDescription'] = '';
			return wineScoringOrganization;
		}

		/**
		 * Gets the list of WineScoringOrganizations have been changed.
		 *
		 * @returns {Array}
		 */
		self.getChangedWineScoringOrganizations = function () {
			var changedWineScoringOrganizations = [];
			angular.forEach(self.wineScoringOrganizationsHandle, function (scoringOrganization) {
				if (self.isWineScoringOrganizationChanged(scoringOrganization)) {
					changedWineScoringOrganizations.push(scoringOrganization);
				}
			});

			return changedWineScoringOrganizations;
		}

		/**
		 * Checks the scoringOrganization is changed or not.
		 *
		 * @param scoringOrganization
		 * @returns {boolean}
		 */
		self.isWineScoringOrganizationChanged = function (scoringOrganization) {
			var isChanged = false;

			var scoringOrganizationTemp = angular.copy(scoringOrganization);

			if (scoringOrganizationTemp.scoringOrganizationName == null) {
				scoringOrganizationTemp.scoringOrganizationName = '';
			}
			if (scoringOrganizationTemp.scoringOrganizationDescription == null) {
				scoringOrganizationTemp.scoringOrganizationDescription = '';
			}

			for (var i = 0; i < self.wineScoringOrganizationsHandleOrig.length; i++) {
				if (self.wineScoringOrganizationsHandleOrig[i].scoringOrganizationId == scoringOrganizationTemp.scoringOrganizationId) {

					if (JSON.stringify(self.wineScoringOrganizationsHandleOrig[i]) !== JSON.stringify(scoringOrganizationTemp)) {
						isChanged = true;
						break;
					}
				}
			}
			return isChanged;
		}

		/**
		 * Add one more wine Scoring Organization row.
		 */
		self.addMoreRowWineScoringOrganization = function () {
			if (self.isValidDataModal(self.wineScoringOrganizationsAddData)) {
				var wineScoringOrganization = self.createWineScoringOrganizationEmpty();
				self.wineScoringOrganizationsAddData.push(wineScoringOrganization);
				self.initWineScoringOrganizationsTableForAddEditMode();
			}
		}

		/**
		 * Add scroll to add form when the total of rows are greater than 15 rows.
		 */
		self.showScrollViewOnAddCountryCodeForm = function () {
			if (self.wineScoringOrganizationsHandle.length > self.TOTAL_ROWS_TO_SHOW_SCROLL) {
				$('#addEditContainer').attr('style', 'overflow-y: auto;height:500px');
				setTimeout(function () {
					var element = document.getElementById('addEditContainer');
					element.scrollTop = element.scrollHeight - element.clientHeight;
				}, 200);
			} else {
				$('#addEditContainer').attr('style', '');
			}
		}

		/**
		 * Do add new or update the list of Wine Scoring Organizations. Call to back end to insert to database.
		 */
		self.updateWineScoringOrganization = function (item) {
			self.closeConfirmPopup();
			var updateData = [{
				"scoringOrganizationId": null,
				"scoringOrganizationName": null,
				"scoringOrganizationDescription": null
			}];
			if (self.isDataChanged(item)) {
				if (self.isValidDataModal(self.wineScoringOrganizationsHandle)) {
					self.isWaitingForResponse = true;
					updateData[0]["scoringOrganizationId"] = item["scoringOrganizationId"];
					updateData[0]["scoringOrganizationName"] = item["scoringOrganizationName"];
					updateData[0]["scoringOrganizationDescription"] = item["scoringOrganizationDescription"];
					wineScoringOrganizationApi.updateWineScoringOrganizations(updateData, function (results) {
							self.setWineScoringOrganizations(results.data);
							self.success = results.message;
							if (self.isReturnToTab) {
								$rootScope.success = self.success;
								$rootScope.isEditedOnPreviousTab = true;
							}
							self.returnToTab(true);
							self.isWaitingForResponse = false;
							self.selectedRow = null;
							self.isEditing = false;
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
		 * Delete WineScoringOrganization handle when click on delete button.
		 */
		self.delete = function (item) {
			self.isDelete = true;
			self.wineScoringOrganizationsPreOrigin = angular.copy(item);
			self.clearMessages();
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			self.showConfirmDelete = true;
			self.messageConfirm = self.DELETE_ITEM_MESSAGE_STRING;
			self.titleConfirm = self.CONFIRM_TITLE;
		};

		/**
		 * Validate Scoring Organization before delete.
		 * @returns {Array}
		 */
		self.validateWineScoringOrganizationBeforeDelete = function () {

			var isValid = true;
			self.wineScoringOrganizationsHandle = [];
			for (var i = 0; i < self.wineScoringOrganizations.length; i++) {
				if (self.wineScoringOrganizations[i] && self.wineScoringOrganizations[i].isSelected == true) {
					self.wineScoringOrganizationsHandle.push(self.wineScoringOrganizations[i]);
				}
			}

			if (self.wineScoringOrganizationsHandle.length > 15) {
				self.messageConfirm = self.SCORING_ORG_LIMIT_DELETE_CONFIRM_MESSAGE_STRING;
				self.labelClose = 'Close';
				self.allowDeleteWineScoringOrganization = false;
				self.allowCloseButton = true;
				isValid = false;
			}

			return isValid;
		}

		/**
		 * Do delete Scoring Organization, call to back end to delete.
		 */
		self.doDeleteWineScoringOrganization = function () {
			self.isWaitingForResponse = true;
			var deleteData = [];
			self.clearMessages();
			deleteData.push(self.wineScoringOrganizationsPreOrigin);
			self.isWaiting = true;
			wineScoringOrganizationApi.deleteWineScoringOrganizations(deleteData, function (results) {
					self.setWineScoringOrganizations(results.data);
					self.success = results.message;
					self.stateWarningPreOrigin = null;
					self.isWaitingForResponse = false;
				}, self.fetchError
			);
			self.closeConfirmPopup();
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
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
			self.isWaitingForResponse = false;
			if (self.isReturnToTab) $rootScope.error = self.error;
		};

		/**
		 * This method is used to close the add/edit popup or show confirm data changes, if there are the data changes.
		 */
		self.close = function () {
			if (self.isDataChanged() === true) {
				self.isConfirmSaveScoringOrganization = true;
				self.allowDeleteWineScoringOrganization = false;
				self.allowCloseButton = false;
				// show popup
				self.confirmHeaderTitle = 'Confirmation';
				self.error = '';
				self.success = '';
				self.errorPopup = '';
				self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING;
				self.labelClose = 'No';
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			} else {
				$('#wineScoringOrganizationModal').modal('hide');
				self.isConfirmSaveScoringOrganization = false;
			}
		};

		/**
		 * This method is used to save the data changes when user click on
		 * Yes button of confirm popup for data changes.
		 */
		self.saveDataChanged = function () {
			if (self.validateWineScoringOrganizationBeforeUpdate()) {
				self.updateWineScoringOrganization();
			}
			$('#confirmModal').modal('hide');
			$('.modal-backdrop').attr('style', '');
			self.isConfirmSaveScoringOrganization = false;
		};

		/**
		 * This method is used to hide add/edit popup and confirm popup when user click on
		 */
		self.noSaveDataChanged = function () {
			$('#wineScoringOrganizationModal').modal('hide');
			$('#confirmModal').modal('hide');
			$('.modal-backdrop').attr('style', '');
			self.isConfirmSaveScoringOrganization = false;
		};

		/**
		 * Hides save confirm dialog.
		 */
		self.cancelConfirmDialog = function () {
			$('#confirmModal').modal('hide');
			$('.modal-backdrop').attr('style', '');
			self.isConfirmSaveScoringOrganization = false;
		};
		/**
		 * Reset validation.
		 */
		self.resetValidation = function () {
			$scope.addEditForm.$setPristine();
			$scope.addEditForm.$setUntouched();
			$scope.addEditForm.$rollbackViewValue();
		};

		/**
		 * Show red border on input text.
		 *
		 * @param id if of input text.
		 */
		self.showErrorOnTextBox = function (id) {
			$('#' + id).addClass('ng-invalid ng-touched');
		};

		/**
		 * Handle when click edit button.
		 *
		 * @param item
		 */
		self.edit = function (item) {
			self.clearMessages();
			self.selectedRow = true;
			self.isEditing = true;
			if (self.wineScoringOrganizationsPre !== null) {
				self.wineScoringOrganizationsPre.isEditing = false;
				item.isEditing = true;
				self.wineScoringOrganizationsPre = item;
				self.wineScoringOrganizationsPreOrigin = angular.copy(item);
			}
			else {
				if (self.wineScoringOrganizationsPre === null) {
					item.isEditing = true;
					self.wineScoringOrganizationsPre = item;
					self.wineScoringOrganizationsPreOrigin = angular.copy(item);
				}
			}
		};

		/**
		 * Reset data edited.
		 *
		 * @param item
		 */
		self.cancel = function (item) {
			self.clearMessages();
			self.selectedRow = null;
			item.isEditing = false;
			self.isEditing = false;
			item.scoringOrganizationId = self.wineScoringOrganizationsPreOrigin.scoringOrganizationId
			item.scoringOrganizationName = self.wineScoringOrganizationsPreOrigin.scoringOrganizationName;
			item.scoringOrganizationDescription = self.wineScoringOrganizationsPreOrigin.scoringOrganizationDescription;
			$scope.tableParams.reload();
			self.stateWarningPreOrigin = null;
		};

		/**
		 * Check data changed.
		 *
		 * @param item
		 * @returns {boolean}
		 */
		self.isDataChanged = function (item) {
			if ($.trim(item['scoringOrganizationId']) !== $.trim(self.wineScoringOrganizationsPreOrigin['scoringOrganizationId']) ||
				$.trim(item['scoringOrganizationName']) !== $.trim(self.wineScoringOrganizationsPreOrigin['scoringOrganizationName']) ||
				$.trim(item['scoringOrganizationDescription']) !== $.trim(self.wineScoringOrganizationsPreOrigin['scoringOrganizationDescription'])) {
				return true;
			}
			return false;
		}

		/**
		 * Valid data
		 *
		 * @param wineScoringOrganizationsList
		 * @returns {boolean}
		 */
		self.isValidDataModal = function (wineScoringOrganizationsList) {
			var errorMessages = [];
			var errorMessage = '';
			for (var i = 0; i < wineScoringOrganizationsList.length; i++) {
				if (self.isNullOrEmpty(wineScoringOrganizationsList[i]["scoringOrganizationName"])) {
					errorMessage = "<li>" + self.SCORING_ORG_NAME_MANDATORY_FIELD_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) == -1) {
						errorMessages.push(errorMessage);
					}
					wineScoringOrganizationsList[i].addClass = 'active-tooltip ng-invalid ng-touched';
				}
				if (self.isNullOrEmpty(wineScoringOrganizationsList[i]['scoringOrganizationDescription'])) {
					errorMessage = "<li>" + self.SCORING_ORG_DESCRIPTION_MANDATORY_FIELD_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) == -1) {
						errorMessages.push(errorMessage);
					}
					wineScoringOrganizationsList[i].addClass = 'active-tooltip ng-invalid ng-touched';
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
			self.wineScoringOrganizationsAddData = [];
		};

		/**
		 * Handle when swith to another tab.
		 */
		$scope.$on(self.VALIDATE_WINE_SCORING_ORGANIZATION, function () {
			if (self.selectedRow !== null) {
				if (self.wineScoringOrganizationsPre !== null) {
					if (self.isDataChanged(self.wineScoringOrganizationsPre)) {
						self.isReturnToTab = true;
						// show popup
						self.titleConfirm = 'Confirmation';
						self.error = '';
						self.success = '';
						self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING;
						$('#confirmModal').modal({backdrop: 'static', keyboard: true});
					}
					else $rootScope.$broadcast(self.RETURN_TAB);
				}
				else $rootScope.$broadcast(self.RETURN_TAB);
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
				$rootScope.$broadcast(self.RETURN_TAB);
			}
			else {
				$('#confirmModal').on('hidden.bs.modal', function () {
					$rootScope.$broadcast(self.RETURN_TAB);
					$scope.$apply();
				});
			}
		};

		/**
		 * Handle to save data when swith tab.
		 */
		self.updateWineScoringOrganizationTabchange = function () {
			self.updateWineScoringOrganization(self.wineScoringOrganizationsPre);
		}

		/**
		 * Close confirm poup.
		 */
		self.closeConfirmPopup = function () {
			self.isDelete = false;
			$('#confirmModal').modal("hide");
			$('.modal-backdrop').attr('style', ' ');
		}

		/**
		 * Close add popup.
		 */
		self.doCloseModal = function () {
			$('#confirmModal').modal("hide");
			$('#wineScoringOrganizationModal').modal("hide");
			self.clearMessages();
			self.isReturnToTab = false;
		};

		/**
		 * Handle when click close on add popup to show confirm popup when has data add
		 * or hide add popup when have not data add.
		 */
		self.closeModalUnsavedData = function () {
			if (self.wineScoringOrganizationsAddData[0].scoringOrganizationName.length !== 0 || self.wineScoringOrganizationsAddData[0].scoringOrganizationDescription.length !== 0) {
				self.titleConfirm = self.CONFIRM_TITLE;
				self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING;
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			}
			else {
				$('#wineScoringOrganizationModal').modal("hide");
				self.stateWarningsAddData = [];
			}
		}

		/**
		 * Handle to call ws to add new wine scoring organizations
		 */
		self.doAddwineScoringOrganizations = function () {
			if (self.isValidDataModal(self.wineScoringOrganizationsAddData)) {
				$('#confirmModal').modal("hide");
				$('#wineScoringOrganizationModal').modal("hide");
				self.isWaitingForResponse = true;
				wineScoringOrganizationApi.addNewWineScoringOrganizations(self.wineScoringOrganizationsAddData, function (results) {
						self.setWineScoringOrganizations(results.data);
						self.success = results.message;
						self.isWaitingForResponse = false;
						self.isAddingActionCode = false;
						self.isEditing = false;
						self.wineScoringOrganizationsPreOrigin = null;
						self.wineScoringOrganizationsAddData = [];
					}, self.fetchError
				);
			}
		};

		/**
		 * Scroll to bottom.
		 */
		self.scrollToBottom = function () {
			if (self.wineScoringOrganizationsAddData.length > self.TOTAL_ROWS_TO_SHOW_SCROLL) {
				setTimeout(function () {
					var element = document.getElementById('wineScoringOrganizationModal');
					element.scrollTop = element.scrollHeight - element.clientHeight;
				}, 200);
			}
		}

		/**
		 * Add scroll to addedit form when the total of rows are greater than 15 rows.
		 */
		self.showScrollToAddEditForm = function () {
			if (self.wineScoringOrganizationsAddData.length > self.TOTAL_ROWS_TO_SHOW_SCROLL) {
				$('#wineScoringOrganizationModal').attr('style', 'overflow-y: auto;height:500px');
			} else {
				$('#wineScoringOrganizationModal').attr('style', '');
			}
		};

	}
})();
