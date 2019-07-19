/*
 * top2TopComponent.js
 *
 * Copyright (c) 2019 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * Component to support the page that allows users to manage top 2 top.
 *
 * @author vn73545
 * @since 2.41.0
 */
(function () {

    var app = angular.module('productMaintenanceUiApp');
    app.component('top2TopComponent', {
        templateUrl: 'src/codeTable/top2Top/top2Top.html',
        bindings: {
            selected: '<'
        },
        controller: top2TopController
    });

    top2TopController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'top2TopApi'];
    /**
     * Constructs for top 2 top Controller.
     */
    function top2TopController($rootScope, $scope, ngTableParams, top2TopApi) {

        var self = this;
        /**
         * Constant for EMPTY
         * @type {string}
         */
        const EMPTY = "";
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
        const UNSAVED_DATA_CONFIRM_MESSAGE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
        /**
         * Constant for TOP_2_TOP_DELETE_MESSAGE_HEADER
         * @type {string}
         */
        const TOP_2_TOP_DELETE_MESSAGE_HEADER = 'Delete Top 2 Top';
        /**
         * Constant for TOP_2_TOP_DELETE_CONFIRM_MESSAGE_STRING
         * @type {string}
         */
        const TOP_2_TOP_DELETE_CONFIRM_MESSAGE_STRING = 'Are you sure you want to delete the selected Top 2 Top?';
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
         * Constant for DUPLICATE_TOP_2_TOPS
         * @type {string}
         */
        const DUPLICATE_TOP_2_TOPS = "Duplicate Top 2 Tops.";
        /**
         * Constant for ALREADY_EXISTS_TOP_2_TOPS
         * @type {string}
         */
        const ALREADY_EXISTS_TOP_2_TOPS = "Top 2 Top already exists.";
        /**
         * Start position of page that want to show on top 2 top table
         *
         * @type {number}
         */
        self.PAGE = 1;
        /**
         * The number of records to show on the  top 2 top table.
         *
         * @type {number}
         */
        self.PAGE_SIZE = 20;
        /**
         * Start position of current page that want to show on top 2 top table
         *
         * @type {number}
         */
        self.currentPage = 1;
        self.data = [];
        self.firstSearch = false;
        self.tableParams = null;
        var previousNameFilter = null;
        var previousIdFilter = null;

        /**
         * String for TOP_2_TOP_NAME_MANDATORY_FIELD_ERROR
         * @type {string}
         */
        self.TOP_2_TOP_NAME_MANDATORY_FIELD_ERROR = 'Top 2 Top name is a mandatory field.';
        /**
         * Empty model.
         */
        self.EMPTY_MODEL = {
            "topToTopId": "",
            "topToTopName": ""
        };
        /**
         * Selected edit top 2 top.
         * @type {null}
         */
        self.selectedTop2Top = null;
        /**
         * The original, unedited top 2 top.
         * @type {null}
         */
        self.originalTop2Top = null;
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
         * Validation top 2 top key.
         * @type {string}
         */
        self.VALIDATE_TOP_2_TOP = 'validateTop2Top';
        /**
         * Return tab key.
         * @type {string}
         */
        self.RETURN_TAB = 'returnTab';
        self.isAddingTop2Top = false;
        self.newTop2Tops = [];
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
            self.currentPage = 1;
            if (self.tableParams == null) {
            	self.createTop2TopTable();
            } else {
                self.tableParams.reload();
            }
        };

        /**
         * Create top 2 top table.
         */
        self.createTop2TopTable = function () {
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

                    var topToTopId = params.filter()["topToTopId"];
                    var topToTopName = params.filter()["topToTopName"];

                    if (typeof topToTopId === "undefined") {
                        topToTopId = EMPTY;
                    }
                    if (typeof topToTopName === "undefined") {
                        topToTopName = EMPTY;
                    }

                    if (topToTopId !== previousIdFilter || topToTopName !== previousNameFilter) {
                        self.firstSearch = true;
                    }

                    if (self.firstSearch) {
                        includeCounts = true;
                        params.page(self.currentPage);
                        self.firstSearch = false;
                        self.startRecord = 0;
                    }

                    self.resetSelectedTop2Top();
                    previousNameFilter = topToTopName;
                    previousIdFilter = topToTopId;
                    self.fetchData(includeCounts, params.page() - 1, topToTopId, topToTopName);
                }
            });
        };

        /**
         * Initiates a call to get the list of attribute maintenance records.
         *
         * @param includeCounts Whether or not to include getting record counts.
         * @param page The page of data to ask for.
         * @param topToTopId ID for attribute filtering.
         * @param topToTopName the topToTopName for attribute filtering.
         */
        self.fetchData = function (includeCounts, page, topToTopId, topToTopName) {
            top2TopApi.findAll({
                topToTopId: topToTopId,
                topToTopName: topToTopName,
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
        	}
        	return UNKNOWN_ERROR;
        };

        /**
         * Clear filter.
         */
        self.clearFilter = function () {
            self.dataResolvingParams.filter()["topToTopId"] = null;
            self.dataResolvingParams.filter()["topToTopName"] = null;
            self.resetSelectedTop2Top();
            self.tableParams.reload();
            self.error = EMPTY;
            self.success = EMPTY;
        };

        /**
         * Determines if the filter has been cleared or not.
         * 
         * @returns {boolean}
         */
        self.isFilterCleared = function () {
            if (!self.dataResolvingParams) {
                return true;
            }
            if (!self.dataResolvingParams.filter()["topToTopId"] &&
                !self.dataResolvingParams.filter()["topToTopName"]) {
                return true
            }
            return false;
        };

        /**
         * Callback for when data is successfully returned from the backend.
         *
         * @param results The data returned from the backend.
         */
        self.loadData = function (results) {
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
         * Add one more row to top 2 top.
         */
        self.addRow = function () {
            if (self.isValidNewTop2Top()) {
                var newTop2Top = self.initEmptyNewTop2Top();
                self.newTop2Tops.push(newTop2Top);
                if (self.newTop2Tops.length > self.PAGE_SIZE 
                		&& self.newTop2Tops.length % self.PAGE_SIZE === 1) {
					self.tableModalParams.page(self.tableModalParams.page() + 1);
					$('#addTop2TopModal').find('input:text:visible:first').focus();
				}
                self.tableModalParams.reload();
            }
        };

        /**
         * Validates new top 2 top.
         * 
         * @returns {boolean}
         */
        self.isValidNewTop2Top = function () {
            var errorMessages = [];
            var errorMessage = EMPTY;
            self.errorPopup = EMPTY;
            for (var i = 0; i < self.newTop2Tops.length; i++) {
            	self.newTop2Tops[i].addClass = EMPTY;
            	self.newTop2Tops[i].addTooltip = EMPTY;
                if (self.isNullOrEmpty(self.newTop2Tops[i].topToTopName)) {
                    errorMessage = "<li>" + self.TOP_2_TOP_NAME_MANDATORY_FIELD_ERROR + "</li>";
                    if (errorMessages.indexOf(errorMessage) == -1) {
                        errorMessages.push(errorMessage);
                    }
                    self.newTop2Tops[i].addClass = 'active-tooltip ng-invalid ng-touched';
                    self.newTop2Tops[i].addTooltip = self.TOP_2_TOP_NAME_MANDATORY_FIELD_ERROR;
                }else if(self.isDuplicatedTopToTopName(self.newTop2Tops, self.newTop2Tops[i].topToTopName)){
                	errorMessage = "<li>" + DUPLICATE_TOP_2_TOPS + "</li>";
                    if (errorMessages.indexOf(errorMessage) == -1) {
                        errorMessages.push(errorMessage);
                    }
                    self.newTop2Tops[i].addClass = 'active-tooltip ng-invalid ng-touched';
                    self.newTop2Tops[i].addTooltip = DUPLICATE_TOP_2_TOPS;
                }
            }
            if (errorMessages.length > 0) {
                var errorMessagesAsString = EMPTY;
                angular.forEach(errorMessages, function (errorMessage) {
                    errorMessagesAsString += errorMessage;
                });
                self.errorPopup = errorMessagesAsString;
                return false;
            }
            return true;
        };
        
        /**
         * Check duplicated TopToTop name in array.
         * 
         * @returns {boolean}
         */
        self.isDuplicatedTopToTopName = function (array, value) {
        	var count = array.filter(function(obj){return obj.topToTopName.toUpperCase() === value.toUpperCase()}).length;
        	if(count >= 2) {
        		return true;
        	}
        	return false;
        };

        /**
         * Check object null or empty
         *
         * @param object
         * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
         */
        self.isNullOrEmpty = function (object) {
            return object === null || !object || object === EMPTY;
        };

        /**
         * Handle when click Add button to display the modal.
         */
        self.addNewTop2Top = function () {
        	self.clearMessages();
        	self.resetSelectedTop2Top();
        	self.tableParams.reload();
            self.isAddingTop2Top = true;
            var top2Top = self.initEmptyNewTop2Top();
            self.newTop2Tops.push(top2Top);
            self.tableModalParams = new ngTableParams({
                page: self.PAGE,
                count: self.PAGE_SIZE
            }, {
                counts: [],
                debugMode: true,
                data: self.newTop2Tops
            });
            $('#addTop2TopModal').modal({backdrop: 'static', keyboard: true});
            $('#addTop2TopModal').on('shown.bs.modal', function () {
            	$(this).find('input:text:visible:first').attr('title', EMPTY);
            	$(this).find('input:text:visible:first').removeClass('ng-invalid ng-touched');
            	$(this).find('input:text:visible:first').focus();
            });
        };

        /**
         * Initiate empty top 2 top to display in the modal.
         *
         * @returns {Object}
         */
        self.initEmptyNewTop2Top = function () {
            return angular.copy(self.EMPTY_MODEL);
        };
        
        /**
         * Clear all the messages when click buttons.
         */
        self.clearMessages = function () {
            self.error = EMPTY;
            self.success = EMPTY;
            self.errorPopup = EMPTY;
            self.newTop2Tops = [];
        };

        /**
         * Handle when click close in add popup but have data changed to show popup confirm.
         */
        self.closeModalUnsavedData = function () {
            if (self.newTop2Tops.length !== 0 && self.newTop2Tops[0].topToTopName.length !== 0) {
                self.titleConfirm = UNSAVED_DATA_CONFIRM_TITLE;
                self.messageConfirm = UNSAVED_DATA_CONFIRM_MESSAGE;
                $('#confirmModal').modal({backdrop: 'static', keyboard: true});
                $('.modal-backdrop').attr('style', ' z-index: 100000; ');
            } else {
                $('#addTop2TopModal').modal("hide");
                self.newTop2Tops = [];
            }
        };

        /**
         * Close confirm popup.
         */
        self.closeConfirmPopup = function () {
            $('#confirmModal').modal("hide");
            $('.modal-backdrop').removeAttr("style");
        };
        
        /**
         * Close add popup and confirm popup.
         */
        self.closeAllPopups = function () {
            self.allowDeleteTop2Top = false;
            self.isAddingTop2Top = false;
            if (self.isReturnToTab) {
                $('#addTop2TopModal').modal("hide");
                $('#confirmModal').on('hidden.bs.modal', function () {
                    self.returnToTab();
                    $scope.$apply();
                });
            } else {
                $('#confirmModal').modal("hide");
                $('#addTop2TopModal').modal("hide");
            }
        };

        /**
         * Removes selected row from modal table.
         *
         * @param index the index to remove.
         */
        self.deleteRow = function (index) {
        	var removedIndex = index + ((self.tableModalParams.page()-1)*self.PAGE_SIZE);
            self.newTop2Tops.splice(removedIndex, 1);
            self.tableModalParams.reload().then(function(data) {
            	if (data.length === 0 && self.tableModalParams.total() > 0) {
            		self.tableModalParams.page(self.tableModalParams.page() - 1);
            		self.tableModalParams.reload();
            	}
            });
            self.isValidNewTop2Top();
        };

        /**
         * Saves new top 2 tops.
         */
        self.saveNewTop2Tops = function () {
        	if (self.isValidNewTop2Top()
        			&& (self.newTop2Tops != null && self.newTop2Tops.length > 0)) {
        		top2TopApi.addTop2Tops(self.newTop2Tops,
        				function (response) {
        			if (response.message.indexOf("Successfully") !== -1) {
        				self.success = response.message;
        				self.closeAllPopups();
        				self.isAddingTop2Top = false;
        				self.tableParams.page(1);
        				self.reloadTableAfterSave();
        			}else{
        				$('#confirmModal').modal("hide");
            			$('.modal-backdrop').removeAttr("style");
        				self.handleAddErrorMessage(response);
        			}
        		}, function (error) {
        			$('#confirmModal').modal("hide");
        			$('.modal-backdrop').removeAttr("style");
        			self.errorPopup = self.getErrorMessage(error);
        		});
        	}else{
        		$('#confirmModal').modal("hide");
        		$('.modal-backdrop').removeAttr("style");
        	}
        };
        
        /**
         * Reload main table after add new or delete top 2 tops.
         */
        self.reloadTableAfterSave = function () {
        	self.isWaitingForResponse = true;
        	self.firstSearch = true;
        	self.currentPage = self.tableParams.page();
        	self.tableParams.reload();
        };
        
        /**
		 * Add error border on exist fields.
		 */
		self.handleAddErrorMessage = function (response) {
			var tempMsgs = response.data;
			self.errorPopup = "<li>" + response.message + "</li>";
			for (var i = 0; i < tempMsgs.length; i++) {
				for (var j = 0; j < self.newTop2Tops.length; j++) {
					if (self.newTop2Tops[j].topToTopName.toUpperCase() === tempMsgs[i].topToTopName.toUpperCase()) {
						self.newTop2Tops[j].addClass = 'active-tooltip ng-invalid ng-touched';
	                    self.newTop2Tops[j].addTooltip = ALREADY_EXISTS_TOP_2_TOPS;
					}
				}
			}
		};

        /**
         * Edit top 2 top handle. This method is called when click on edit button.
         * 
         * @param top2Top the top 2 top to handle.
         */
        self.editTop2Top = function (top2Top) {
            if (self.selectedRowIndex === -1) {
                self.originalTop2Top = JSON.stringify(top2Top);
                self.error = EMPTY;
                self.success = EMPTY;
                top2Top.isEditing = true;
                self.validationModel = angular.copy(top2Top);
                self.selectedTop2Top = top2Top;
                self.selectedRowIndex = self.getRowIndex();
            }
        };

        /**
         * Calls confirmation modal to confirm delete action.
         * 
         * @param top2Top the top 2 top to delete.
         */
        self.deleteTop2Top = function (top2Top) {
            self.selectedDeletedTop2Top = top2Top;
            self.error = EMPTY;
            self.success = EMPTY;
            self.titleConfirm = TOP_2_TOP_DELETE_MESSAGE_HEADER;
            self.messageConfirm = TOP_2_TOP_DELETE_CONFIRM_MESSAGE_STRING;
            self.labelClose = 'No';
            self.allowDeleteTop2Top = true;
            $('#confirmModal').modal({backdrop: 'static', keyboard: true});
        };

        /**
         * Calls the api to update the top 2 top.
         */
        self.updateTop2Top = function () {
            self.error = EMPTY;
            self.success = EMPTY;
            if (self.selectedRowIndex > -1) {
                // editing mode.
                if (self.isTop2TopChanged()) {
                    if (self.validateTop2TopBeforeUpdate()) {
                        self.isWaitingForResponse = true;
                        var top2TopTemp = angular.copy(self.selectedTop2Top);
                        delete top2TopTemp['isEditing'];
                        top2TopApi.updateTop2Top(top2TopTemp,
                            function (results) {
                                self.data[self.selectedRowIndex] = angular.copy(results.data);
                                self.resetSelectedTop2Top();
                                self.isWaitingForResponse = false;
                                self.checkAllFlag = false;
                                self.success = results.message;
                                if (self.isReturnToTab) {
                                    $rootScope.success = self.success;
                                    $rootScope.isEditedOnPreviousTab = true;
                                }
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
         * Resets top 2 top back to original state.
         * @param index
         */
        self.resetTop2Top = function (index) {
            self.error = EMPTY;
            self.success = EMPTY;
            self.data[index] = JSON.parse(self.originalTop2Top);
            self.resetSelectedTop2Top();
        };

        /**
         * Returns the disabled status of button by  top 2 top id.
         *
         * @param topToTopId the top 2 top id.
         * @returns {boolean} the disable status.
         */
        self.isDisabledButton = function (topToTopId) {
            return !(self.selectedRowIndex === -1 || self.selectedTop2Top.topToTopId === topToTopId);
        };

        /**
         * Returns the style for icon button.
         *
         * @param topToTopId the id of top 2 top.
         * @returns {*} the style.
         */
        self.getDisabledButtonStyle = function (topToTopId) {
            if (self.isDisabledButton(topToTopId)) {
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
            if (self.selectedTop2Top == null) {
                return -1;
            }
            if (self.selectedTop2Top.topToTopId === 0) {
                return 0;
            }
            for (var i = 0; i < self.data.length; i++) {
                if (self.data[i].topToTopId === self.selectedTop2Top.topToTopId) {
                    return i;
                }
            }
        };

        /**
         * Validates the top 2 top name before updates.
         * 
         * @returns {boolean}
         */
        self.validateTop2TopBeforeUpdate = function () {
            var errorMessages = [];
            var message = EMPTY;
            if (!self.validationModel || self.validationModel.topToTopName.trim().length === 0) {
                message = '<li>' + self.TOP_2_TOP_NAME_MANDATORY_FIELD_ERROR + '</li>';
                errorMessages.push(message);
                self.showErrorOnTextBox('topToTopName', self.TOP_2_TOP_NAME_MANDATORY_FIELD_ERROR);
                return false;
            }
            return true;
        };

        /**
         * Show red border on input text.
         *
         * @param topToTopId if of input text.
         */
        self.showErrorOnTextBox = function (topToTopId, message) {
            if ($('#' + topToTopId).length > 0) {
                $('#' + topToTopId).addClass('ng-invalid ng-touched');
                $('#' + topToTopId).attr('title', message);
            }
        };

        /**
         * Reset the status add or edit top 2 top.
         */
        self.resetSelectedTop2Top = function () {
            self.selectedRowIndex = -1;
            self.selectedTop2Top = null;
        };

        /**
         * Checks if the top 2 top is changed or not.
         *
         * @returns {boolean}
         */
        self.isTop2TopChanged = function () {
            var top2TopTemp = angular.copy(self.selectedTop2Top);
            delete top2TopTemp['isEditing'];
            return JSON.stringify(top2TopTemp) !== self.originalTop2Top;
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
         * Handles errors when name changes.
         */
        self.onNameChange = function () {
            var value = $('#topToTopName').val();
            if (value == null || value === undefined ||
                value.trim().length === 0) {
                self.showErrorOnTextBox('topToTopName', self.TOP_2_TOP_NAME_MANDATORY_FIELD_ERROR);
            }
        };

        /**
         * Calls api to delete the top 2 top.
         */
        self.doDeleteTop2Top = function () {
            self.closeAllPopups();
            self.isWaitingForResponse = true;
            top2TopApi.deleteTop2Top({topToTopId: self.selectedDeletedTop2Top.topToTopId},
                function (results) {
                    self.isWaitingForResponse = false;
                    self.success = results.message;
                    self.selectedDeletedTop2Top = null;
                    if (self.tableParams.data.length === 1 && self.tableParams.total() > 0) {
                		self.tableParams.page(self.tableParams.page() - 1);
                	}
                    self.reloadTableAfterSave();
                },
                function (error) {
                    self.handleError(error);
                }
            );
        };

        /**
         * Clear message listener.
         */
        $scope.$on(self.VALIDATE_TOP_2_TOP, function () {
            if (self.selectedTop2Top != null && self.isTop2TopChanged()) {
                self.isReturnToTab = true;
                self.allowDeleteTop2Top = false;
                self.titleConfirm = UNSAVED_DATA_CONFIRM_TITLE;
                self.error = EMPTY;
                self.success = EMPTY;
                self.messageConfirm = UNSAVED_DATA_CONFIRM_MESSAGE_STRING;
                self.labelClose = 'No';
                $('#confirmModal').modal({backdrop: 'static', keyboard: true});
            } else {
                $rootScope.$broadcast(self.RETURN_TAB);
            }
        });
    }
})();
