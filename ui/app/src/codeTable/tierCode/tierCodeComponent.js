/*
 * tierCodeComponent.js
 *
 * Copyright (c) 2019 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author m314029
 * @since 2.26.0
 */

'use strict';

/**
 * Component to support the page that allows users to show Tier Code.
 *
 * @author vn70529
 * @since 2.12
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('tierCodeComponent', {
		templateUrl: 'src/codeTable/tierCode/tierCode.html',
		bindings: {
			selected: '<'
		},
		controller: tierCodeController
	});

    tierCodeController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'tierCodeApi', '$timeout'];

	/**
	 * Constructs for Tier Code Controller.
	 */
	function tierCodeController($rootScope, $scope, ngTableParams, tierCodeApi, $timeout) {

		var self = this;
		/**
		 * Messages
		 * @type {string}
		 */
		self.TIER_CODE_NAME_MANDATORY_FIELD_ERROR = 'Tier Code name is a mandatory field.';

		/**
		 * Start position of page that want to show on tier code table
		 *
		 * @type {number}
		 */
		self.PAGE = 1;
		/**
		 * The number of records to show on the  tier code table.
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
         * Constant for UNKNOWN_ERROR
         * @type {string}
         */
		const UNKNOWN_ERROR = "An unknown error occurred.";

        /**
         * Constant for UNSAVED_DATA_CONFIRM_TITLE
         * @type {string}
         */
		const UNSAVED_DATA_CONFIRM_TITLE = "Confirmation";

        /**
         * Constant for UNSAVED_DATA_CONFIRM_MESSAGE
         * @type {string}
         */
		const UNSAVED_DATA_CONFIRM_MESSAGE = "Unsaved data will be lost. Do you want to save the changes before continuing ?";

        /**
         * Constant for TIER_CODE_DELETE_MESSAGE_HEADER
         * @type {string}
         */
		const TIER_CODE_DELETE_MESSAGE_HEADER = 'Delete Tier Code';

        /**
         * Constant for TIER_CODE_DELETE_CONFIRM_MESSAGE_STRING
         * @type {string}
         */
		const TIER_CODE_DELETE_CONFIRM_MESSAGE_STRING = 'Are you sure you want to delete the selected Tier Code?';

        /**
         * Constant for THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING
         * @type {string}
         */
		const THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';

        /**
         * Constant for UNSAVED_DATA_CONFIRM_MESSAGE_STRING
         * @type {string}
         */
		const UNSAVED_DATA_CONFIRM_MESSAGE_STRING = 'Unsaved data will be lost. Do you want to save the changes before continuing?';

        /**
         * Constant for TIER_CODE_DESCRIPTION_MANDATORY_FIELD_ERROR
         * @type {string}
		 */
		const TIER_CODE_DESCRIPTION_MANDATORY_FIELD_ERROR = 'Tier Code Description is a mandatory field.';

        /**
         * Constant for DUPLICATE_TIER_CODES
         * @type {string}
         */
		const DUPLICATE_TIER_CODES = 'Duplicate Tier Code. ';

        /**
         * Constant for EMPTY_STRING
         * @type {string}
         */
        const EMPTY_STRING = '';
        /**
         * Constant for ALREADY_EXISTS_TIER_CODE
         * @type {string}
         */
        const ALREADY_EXISTS_TIER_CODE = "Tier Code already exists.";

		/**
		 * Empty model.
		 */
		self.EMPTY_MODEL = {
            productBrandTierCode: EMPTY_STRING,
            productBrandName: EMPTY_STRING
		};
		/**
		 * Selected edit tier code.
		 * @type {null}
		 */
		self.selectedTierCode = null;
		/**
		 * The original, unedited tier code.
		 * @type {null}
		 */
		self.originalTierCode = null;
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
		 * Validation tier code key.
		 * @type {string}
		 */
		self.VALIDATE_TIER_CODE = 'validateTierCode';
		/**
		 * Return tab key.
		 * @type {string}
		 */
		self.RETURN_TAB = 'returnTab';
		self.isAddingTierCode = false;
		self.newTierCodes = [];
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
		 * Resets the table with current filter. If the table has not been created, create the table. Else reload the
		 * table.
		 */
        self.newSearch = function () {
			self.isWaitingForResponse = true;
			self.firstSearch = true;
			if (self.tableParams === null) {
                createTierCodesTable();
			} else {
				self.tableParams.reload();
			}

		};

		/**
		 * Create tier codes table.
		 */
		function createTierCodesTable() {
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

					var id = params.filter()["productBrandTierCode"];
					var description = params.filter()["productBrandName"];

					if (typeof id === "undefined") {
						id = EMPTY_STRING;
					}
					if (typeof description === "undefined") {
						description = EMPTY_STRING;
					}

                    if (id !== previousIdFilter || description !== previousDescriptionFilter) {
						self.firstSearch = true;
					}
                    if (self.deletingLastItemPage === true) {
                        includeCounts = true;
                        self.deletingLastItemPage = false;
                    } else {
                        includeCounts = false;
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
        self.fetchData = function (includeCounts, page, id, description) {
			tierCodeApi.findTierCodeList({
                productBrandTierCode: id,
                productBrandName: description,
                page: page,
                pageSize: self.PAGE_SIZE,
                includeCount: includeCounts
            }, self.loadData, self.handleError);
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
            } else {
				return UNKNOWN_ERROR;
			}
		};

		/**
		 * Clear filter.
		 */
		self.clearFilter = function () {
			self.dataResolvingParams.filter()["productBrandTierCode"] = null;
			self.dataResolvingParams.filter()["productBrandName"] = null;
			self.tableParams.reload();
			self.error = EMPTY_STRING;
			self.success = EMPTY_STRING;
		};

		/**
		 * Determines if the filter has been cleared or not.
		 */
		self.isFilterCleared = function () {
            if (!self.dataResolvingParams) {
				return true;
			}
            if (!self.dataResolvingParams.filter()["productBrandTierCode"] &&
				!self.dataResolvingParams.filter()["productBrandName"]) {
				return true;
			} else {
				return false;
			}
		};

		/**
		 * Callback for when data is successfully returned from the backend.
		 *
		 * @param results The data returned from the backend.
		 */
        self.loadData = function(results) {
            self.resetSelectedTierCode();
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
		 * Add one more row to tier code.
		 */
		self.addRow = function () {
            if (self.isValidNewTierCode()) {
                self.resetInvalidField();
                var newTierCode = self.initEmptyNewTierCode();
                self.newTierCodes.push(newTierCode);
                self.nextPage();
                self.tableModalParams.reload();
                self.errorPopup = '';
            }
		};

		/**
		 * Validates new tier code.
		 * @returns {boolean}
		 */
        self.isValidNewTierCode = function () {
            self.resetInvalidField();
			var errorMessages = [];
			var errorMessage = EMPTY_STRING;
			var tierCodeId = "productBrandName";
            var itemIndexInvalid = [];

            for (var i = 0; i < self.newTierCodes.length; i++) {
                // Check empty tier code.
                if (self.isNullOrEmpty(self.newTierCodes[i].productBrandName)) {
                    errorMessage = "<li>" + self.TIER_CODE_NAME_MANDATORY_FIELD_ERROR + "</li>";
                    if (errorMessages.indexOf(errorMessage) === -1) {
                        errorMessages.push(errorMessage);
                    }

                    self.newTierCodes[i].addClass = 'active-tooltip ng-invalid ng-touched';
                } else {
                    // Check duplicate tier code names.
                    for (var k = i + 1; k < self.newTierCodes.length; k++) {
                        if (itemIndexInvalid.indexOf(k) < 0
                            && self.newTierCodes[k].productBrandName.toUpperCase() === self.newTierCodes[i].productBrandName.toUpperCase()) {
                            itemIndexInvalid.push(k);
                            errorMessage = "<li>" + DUPLICATE_TIER_CODES + "</li>";
                            if (errorMessages.indexOf(errorMessage) === -1) {
                                errorMessages.push(errorMessage);
                            }
                            self.newTierCodes[i].addClass = 'active-tooltip ng-invalid ng-touched';
                            self.newTierCodes[k].addClass = 'active-tooltip ng-invalid ng-touched';
                            $('#' + tierCodeId + i).addClass('ng-invalid ng-touched');
                            $('#' + tierCodeId + k).addClass('ng-invalid ng-touched');
                        }
                    }
                }
            }

			if (errorMessages.length > 0) {
				var errorMessagesAsString = EMPTY_STRING;
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
			return object === null || !object || object === EMPTY_STRING;
		};

		/**
		 * Handle when click Add button to display the modal.
		 */
		self.addNewTierCode = function () {
            self.resetTierCode(self.data.indexOf(self.selectedTierCode));
			self.isAddingTierCode = true;
			var tierCode = self.initEmptyNewTierCode();
			self.clearMessages();
			self.newTierCodes.push(tierCode);
			self.tableModalParams = new ngTableParams({
				page: self.PAGE,
				count: self.PAGE_SIZE
			}, {
				counts: [],
				debugMode: true,
				data: self.newTierCodes
			});
			$('#addTierCodeModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Initiate empty tier code to display in the modal.
		 *
		 * @returns {{}}
		 */
		self.initEmptyNewTierCode = function () {
			var tierCode = {
				"productBrandTierCode": EMPTY_STRING,
				"productBrandName": EMPTY_STRING
			};
			return tierCode;
		};
		/**
		 * Clear all the messages when click buttons.
		 */
		self.clearMessages = function () {
			self.error = EMPTY_STRING;
			self.success = EMPTY_STRING;
			self.errorPopup = EMPTY_STRING;
			self.newTierCodes = [];
		}

		/**
		 * Handle when click close in add popup but have data changed to show popup confirm.
		 */
		self.closeModalUnsavedData = function () {
			self.resetInvalidField();
			if (self.newTierCodes.length !== 0 && self.newTierCodes[0].productBrandName.length !== 0) {
				self.titleConfirm = UNSAVED_DATA_CONFIRM_TITLE;
				self.messageConfirm = UNSAVED_DATA_CONFIRM_MESSAGE;
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			} else {
				self.isValidNewTierCode(self.newTierCodes);
				$('#addTierCodeModal').modal("hide");
				self.newTierCodes = [];
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
		self.closeConfirmModal = function () {
			self.allowDeleteTierCode = false;
			self.isAddingTierCode = false;
			if (self.isReturnToTab) {
				$('#addTierCodeModal').modal("hide");
				$('#confirmModal').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			} else {
				$('#confirmModal').modal("hide");
				$('#addTierCodeModal').modal("hide");
			}
		};

		/**
		 * Removes selected row from modal table.
		 *
		 * @param index the index to remove.
		 */
        self.deleteRow = function (index) {
            self.errorPopup = '';
            self.newTierCodes.splice(index, 1);
            if (index % self.PAGE_SIZE === 0 && ((self.tableModalParams.page() - 1) * self.PAGE_SIZE) === (self.newTierCodes.length) && self.tableModalParams.page() > 1) {
                self.tableModalParams.page(self.tableModalParams.page() - 1);
            }
            self.tableModalParams.reload();
            self.isValidNewTierCode();
		};

		/**
		 * Saves new tier codes.
		 */
		self.saveNewTierCodes = function () {
            if (self.newTierCodes.length > 0 && self.isValidNewTierCode()) {
                self.resetInvalidField();
                tierCodeApi.addTierCodes(self.newTierCodes,
                    function (response) {
                        if (response.message.indexOf("Successfully") !== -1) {
                        self.success = response.message;
                            self.closeConfirmModal();
                        self.isAddingTierCode = false;
                        self.deletingLastItemPage = true;
                        self.newSearch();
						} else {
                            $('#confirmModal').modal("hide");
                            $('.modal-backdrop').attr('style', ' z-index: 0; ');
                            self.handleAddErrorMessage(response);
						}

                    }, function (error) {
                        $('#confirmModal').modal("hide");
                        $('.modal-backdrop').removeAttr("style");
                        self.errorPopup = self.getErrorMessage(error);
                    });
            } else {
                $('.modal-backdrop').attr('style', ' ');
			}
		}

		/**
         * Add error border on exist fields.
         */
        self.handleAddErrorMessage = function (response) {
            var tempMsgs = response.data;
            self.errorPopup = "<li>" + response.message + "</li>";
            for (var i = 0; i < tempMsgs.length; i++) {
                for (var j = 0; j < self.newTierCodes.length; j++) {
                    if (self.newTierCodes[j].productBrandName.toUpperCase() === tempMsgs[i].productBrandName.toUpperCase()) {
                        self.newTierCodes[j].addClass = 'active-tooltip ng-invalid ng-touched';
                        self.newTierCodes[j].addTooltip = ALREADY_EXISTS_TIER_CODE;
                    }
                }
            }
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
            } else {
				return UNKNOWN_ERROR;
			}
		};

		/**
		 * Edit Tier Code handle. This method is called when click on edit button.
		 * @param tierCode the tier code to handle.
		 */
        self.editTierCode = function (tierCode) {
			if (self.selectedRowIndex === -1) {
				self.originalTierCode = JSON.stringify(tierCode);
				self.error = EMPTY_STRING;
				self.success = EMPTY_STRING;
                tierCode.isEditing = true;
				self.validationModel = angular.copy(tierCode);
				self.selectedTierCode = tierCode;
				self.selectedRowIndex = self.getRowIndex();
			}
		};

		/**
		 * Calls confirmation modal to confirm delete action.
		 * @param tierCode the tier code to delete.
		 */
        self.deleteTierCode = function (tierCode) {
			self.selectedDeletedTierCode = tierCode;
			self.error = EMPTY_STRING;
			self.success = EMPTY_STRING;
			self.titleConfirm = TIER_CODE_DELETE_MESSAGE_HEADER;
			self.messageConfirm = TIER_CODE_DELETE_CONFIRM_MESSAGE_STRING;
			self.labelClose = 'No';
			self.allowDeleteTierCode = true;
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Calls the api to update the tier code.
		 */
        self.updateTierCode = function () {
			self.error = EMPTY_STRING;
			self.success = EMPTY_STRING;
			if (self.selectedRowIndex > -1) {
				// editing mode.
				if (self.isTierCodeChanged()) {
                    if (self.isValidTierCodeBeforeUpdate()) {
						self.isWaitingForResponse = true;
						var tempTierCode = angular.copy(self.selectedTierCode);
						delete tempTierCode['isEditing'];
						tierCodeApi.updateTierCode(tempTierCode,
							function (results) {
								self.data[self.selectedRowIndex] = angular.copy(results.data);
								self.resetSelectedTierCode();
								self.isWaitingForResponse = false;
								self.checkAllFlag = false;
								self.success = results.message;
                                if (self.isReturnToTab) {
									$rootScope.success = self.success;
									$rootScope.isEditedOnPreviousTab = true;
                                }
                                ;
								self.returnToTab();
							},
							function (error) {
                                self.handleError(error);
							}
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
		 * Resets tier code back to original state.
		 * @param index
		 */
        self.resetTierCode = function (index) {
			self.error = EMPTY_STRING;
			self.success = EMPTY_STRING;
			self.data[index] = JSON.parse(self.originalTierCode);
			self.resetSelectedTierCode();
		};


		/**
		 * Returns the disabled status of button by tier code id.
		 *
		 * @param id the TierCode id.
		 * @returns {boolean} the disable status.
		 */
		self.isDisabledButton = function (id) {
			return !(self.selectedRowIndex === -1 || self.selectedTierCode.productBrandTierCode === id);

		};

		/**
         * Return the save new tier code status.
         *
         * @returns {boolean}
         */
        self.isPermitAdd = function () {
            return self.newTierCodes.length === 0;
        };

		/**
		 * Returns the style for icon button.
		 *
		 * @param id the id of tier code.
		 * @returns {*} the style.
		 */
		self.getDisabledButtonStyle = function (id) {
			if (self.isDisabledButton(id)) {
				return 'opacity: 0.5;';
			}
			return 'opacity: 1.0;';
		};

		/**
		 * Return edited row index.
		 *
		 * @returns {number}
		 */
		self.getRowIndex = function () {
			if (self.selectedTierCode === null) {
				return -1;
			}
			if (self.selectedTierCode.productBrandTierCode === 0) {
				return 0;
			}
			for (var i = 0; i < self.data.length; i++) {
				if (self.data[i].productBrandTierCode === self.selectedTierCode.productBrandTierCode) {
					return i;
				}
			}
		};

        /**
         * Remove invalid red border.
         */
        self.resetInvalidField = function () {
            for (var i = 0; i < self.newTierCodes.length; i++) {
                //$('#productBrandName' + i).removeClass('ng-invalid');
                self.newTierCodes[i].addClass = 'ng-valid';
            }
        };

		/**
		 * Validates the description before updates.
		 * @returns {boolean}
		 */
        self.isValidTierCodeBeforeUpdate = function () {
			var errorMessages = [];
			var message = EMPTY_STRING;
            if (!self.validationModel || self.validationModel.productBrandName.trim().length === 0) {
				message = '<li>' + TIER_CODE_DESCRIPTION_MANDATORY_FIELD_ERROR + '</li>';
				errorMessages.push(message);
                self.error = TIER_CODE_DESCRIPTION_MANDATORY_FIELD_ERROR;
				self.showErrorOnTextBox('productBrandName', TIER_CODE_DESCRIPTION_MANDATORY_FIELD_ERROR);
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
		 * Reset the status add or edit tier code.
		 */
		self.resetSelectedTierCode = function () {
			self.selectedRowIndex = -1;
			self.selectedTierCode = null;
		};

		/**
		 * Checks if the tier code is changed or not.
		 *
		 * @returns {boolean}
		 */
		self.isTierCodeChanged = function () {
			var tierCodeTemp = angular.copy(self.selectedTierCode);
			delete tierCodeTemp['isEditing'];
			return JSON.stringify(tierCodeTemp) !== self.originalTierCode;

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
			var value = $('#productBrandName').val();
			if (value === null || value === undefined ||
				value.trim().length === 0) {
				self.showErrorOnTextBox('productBrandName', TIER_CODE_DESCRIPTION_MANDATORY_FIELD_ERROR);
			}
		};

		/**
         * Update next page in add modal.
         */
        self.nextPage = function () {
            if (self.newTierCodes.length > self.PAGE_SIZE && self.newTierCodes.length % self.PAGE_SIZE === 1) {
                self.tableModalParams.page(self.tableModalParams.page() + 1);
                // Temporarily fix
                $timeout(function () {
                    $('#productBrandName' + (self.newTierCodes.length - 1)).focus();
                }, 800);
            }
        };

		/**
		 * Calls api to delete the tier code.
		 */
        self.doDeleteTierCode = function () {
			self.closeConfirmModal();
			self.isWaitingForResponse = true;
            tierCodeApi.deleteTierCode({productBrandTierCode: self.selectedDeletedTierCode.productBrandTierCode},
				function (results) {
                    if (self.data[0] === self.selectedDeletedTierCode && self.data.length === 1 && self.tableParams.page() > 1) {
                        self.tableParams.page(self.tableParams.page() - 1);
                    }
                    self.deletingLastItemPage = true;
					self.isWaitingForResponse = false;
					self.success = results.message;
					self.selectedDeletedTierCode = null;
					self.tableParams.reload();
				},
				function (error) {
                    self.handleError(error);
				}
			);
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_TIER_CODE, function () {
			if (self.selectedTierCode !== null && self.isTierCodeChanged()) {
				self.isReturnToTab = true;
				self.allowDeleteTierCode = false;
				self.titleConfirm = 'Confirmation';
				self.error = EMPTY_STRING;
				self.success = EMPTY_STRING;
				self.messageConfirm = UNSAVED_DATA_CONFIRM_MESSAGE_STRING;
				self.labelClose = 'No';
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});
	}
})();
