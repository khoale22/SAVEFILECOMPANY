/*
 * brandComponent.js
 *
 * Copyright (c) 2019 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */

'use strict';

/**
 * Component to support the page that allows users to show Brand.
 *
 * @author vn87351
 * @since 2.41.0
 */
(function () {

    var app = angular.module('productMaintenanceUiApp');
    app.component('brandComponent', {
        templateUrl: 'src/codeTable/brand/brand.html',
        controller: brandController
    });

    brandController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'brandApi'];

    /**
     * Constructs for brand Controller.
     */
    function brandController($rootScope, $scope, ngTableParams, brandApi) {

        var self = this;

        /**
         * Empty model.
         */
        self.EMPTY_MODEL = {
            productBrandId: '',
            productBrandDescription: ''
        };

        /**
         * Selected edit brand.
         * @type {null}
         */
        self.selectedBrand = null;

        /**
         * The original, unedited brand.
         * @type {null}
         */
        self.originalBrand = null;

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
         * Start position of page that want to show on brand table
         *
         * @type {number}
         */
        self.PAGE = 1;

        /**
         * The number of records to show on the brand table.
         *
         * @type {number}
         */
        self.PAGE_SIZE = 20;
        self.firstSearch = false;
        self.tableParams = null;
        var previousNameFilter = null;
        var previousIdFilter = null;
        self.currentPage = 1;

        /**
         * Validation brand key.
         * @type {string}
         */
        self.VALIDATE_BRAND = 'validateBrand';

        /**
         * Mandatory field error message.
         * @type {string}
         */
        self.BRAND_NAME_MANDATORY_FIELD_ERROR = 'Brand name is a mandatory field.';


        /**
         * Initialize some const.
         */
        const UNKNOWN_ERROR = "An unknown error occurred.";
        const THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
        const UNSAVED_DATA_CONFIRM_TITLE = "Confirmation";
        const UNSAVED_DATA_CONFIRM_MESSAGE = "Unsaved data will be lost. Do you want to save the changes before continuing ?";
        const BRAND_DELETE_MESSAGE_HEADER = 'Delete Brand';
        const BRAND_DELETE_CONFIRM_MESSAGE_STRING = 'Are you sure you want to delete the selected Brand?';
        const DUPLICATE_BRANDS = "Duplicate brands. ";
        const ALREADY_EXISTS_BRAND = "Brand already exists.";
        const EMPTY = "";

        /**
         * Return tab key.
         * @type {string}
         */
        self.RETURN_TAB = 'returnTab';
        self.isAddingBrand = false;
        self.newBrands = [];

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
                self.createBrandsTable();
            } else {
                self.tableParams.reload();
            }
        };

        /**
         * Create brand table.
         */
        self.createBrandsTable = function () {
            self.tableParams = new ngTableParams({
                page: self.PAGE, // show first page
                count: self.PAGE_SIZE // count per page
            }, {
                counts: [],
                getData: function ($defer, params) {
                    self.data = null;

                    self.defer = $defer;
                    self.dataResolvingParams = params;

                    var includeCount = false;

                    var productBrandId = params.filter()['productBrandId'];
                    var productBrandDescription = params.filter()['productBrandDescription'];

                    if (typeof productBrandId === 'undefined') {
                        productBrandId = EMPTY;
                    }
                    if (typeof productBrandDescription === 'undefined') {
                        productBrandDescription = EMPTY;
                    }

                    if (productBrandId !== previousIdFilter || productBrandDescription !== previousNameFilter) {
                        self.firstSearch = true;
                    }

                    if (self.firstSearch) {
                        includeCount = true;
                        params.page(self.currentPage);
                        self.firstSearch = false;
                    }
                    self.resetSelectedBrand();
                    previousIdFilter = productBrandId;
                    previousNameFilter = productBrandDescription;
                    self.loadData(includeCount, params.page() - 1, productBrandId, productBrandDescription);
                }
            });

        };

        /**
         * Reload main table after add new or delete brands.
         */
        self.reloadTableAfterSave = function () {
            self.isWaitingForResponse = true;
            self.firstSearch = true;
            self.currentPage = self.tableParams.page();
            self.tableParams.reload();
        };

        /**
         * Initiates a call to get the list of attribute maintenance records.
         *
         * @param includeCount Whether or not to include getting record counts.
         * @param page The page of data to ask for.
         * @param productBrandId The productBrandId for attribute filtering.
         * @param productBrandDescription The productBrandDescription for attribute filtering.
         */
        self.loadData = function (includeCount, page, productBrandId, productBrandDescription) {
            brandApi.findAll({
                productBrandId: productBrandId,
                productBrandDescription: productBrandDescription,
                page: page,
                pageSize: self.PAGE_SIZE,
                includeCount: includeCount
            }, self.handleSuccess, self.handleError);
        };

        /**
         * Clear filter.
         */
        self.clearFilter = function () {
            self.dataResolvingParams.filter()["productBrandId"] = null;
            self.dataResolvingParams.filter()["productBrandDescription"] = null;
            self.resetSelectedBrand();
            self.tableParams.reload();
            self.error = EMPTY;
            self.success = EMPTY;
        };

        /**
         * Edit brand. This method is called when click on edit button.
         * @param brand the brand to handle.
         */
        self.editBrand = function (brand) {
            if (self.selectedRowIndex === -1) {
                self.originalBrand = JSON.stringify(brand);
                self.error = EMPTY;
                self.success = EMPTY;
                brand.isEditing = true;
                self.validationModel = angular.copy(brand);
                self.selectedBrand = brand;
                self.selectedRowIndex = self.getRowIndex();
            }
        };

        /**
         * Calls confirmation modal to confirm delete action.
         * @param brand the brand to delete.
         */
        self.deleteBrand = function (brand) {
            self.selectedDeletedBrand = brand;
            self.error = EMPTY;
            self.success = EMPTY;
            self.titleConfirm = BRAND_DELETE_MESSAGE_HEADER;
            self.messageConfirm = BRAND_DELETE_CONFIRM_MESSAGE_STRING;
            self.labelClose = 'No';
            self.allowDeleteBrand = true;
            $('#confirmModal').modal({backdrop: 'static', keyboard: true});
        };

        /**
         * Calls the api to update the brand.
         */
        self.updateBrand = function () {
            self.error = EMPTY;
            self.success = EMPTY;
            if (self.selectedRowIndex > -1) {
                // editing mode.
                if (self.isBrandChanged()) {
                    if (self.isBrandValidBeforeUpdate()) {
                        self.isWaitingForResponse = true;
                        var tempBrand = angular.copy(self.selectedBrand);
                        delete tempBrand['isEditing'];
                        brandApi.updateBrand(tempBrand,
                            function (results) {
                                self.data[self.selectedRowIndex] = angular.copy(results.data);
                                self.resetSelectedBrand();
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
        self.addNewBrand = function () {
            self.resetBrand(self.data.indexOf(self.selectedBrand));
            self.isAddingBrand = true;
            var brand = self.initEmptyNewBrand();
            self.clearMessages();
            self.newBrands.push(brand);
            self.tableModalParams = new ngTableParams({
                page: self.PAGE,
                count: self.PAGE_SIZE
            }, {
                counts: [],
                debugMode: true,
                data: self.newBrands
            });
            $('#addBrandModal').modal({backdrop: 'static', keyboard: true});
            $('#productBrandDescription0').removeClass('ng-invalid');
        };

        /**
         * Reset brand back to original state.
         *
         * @param index The index of brand list displaying in current page.
         */
        self.resetBrand = function (index) {
            self.error = EMPTY;
            self.success = EMPTY;
            self.data[index] = JSON.parse(self.originalBrand);
            self.resetSelectedBrand();
        };

        /**
         * Removes selected row from modal table.
         *
         * @param index the index to remove.
         */
        self.deleteRow = function (index) {
            self.errorPopup = EMPTY;
            index = index + ((self.tableModalParams.page() - 1) * self.PAGE_SIZE);
            $('#productBrandDescription' + index).removeClass('ng-invalid');
            self.newBrands.splice(index, 1);
            if (self.tableModalParams.data.length === 1) {
                self.tableModalParams.page(1);
                self.tableModalParams.reload();
            } else {
                self.tableModalParams.reload();
            }
            self.isValidNewBrand();
        };


        /**
         * Add one more row to brand.
         */
        self.addRow = function () {
            if (self.isValidNewBrand()) {
                self.resetInvalidField();
                var newBrand = self.initEmptyNewBrand();
                self.newBrands.push(newBrand);
                if (self.newBrands.length > self.PAGE_SIZE && self.newBrands.length % self.PAGE_SIZE === 1) {
                    self.tableModalParams.page(self.tableModalParams.page() + 1);
                    $('#productBrandDescription0').focus();
                }
                self.tableModalParams.reload();
                self.errorPopup = EMPTY;
            }
        };

        /**
         * Remove invalid red border.
         */
        self.resetInvalidField = function () {
            for (var i = 0; i < self.newBrands.length; i++) {
                $('#productBrandDescription' + i).removeClass('ng-invalid');
            }
        };

        /**
         * Saves new brands.
         */
        self.saveNewBrands = function () {
            if (self.isValidNewBrand()) {
                self.resetInvalidField();
                brandApi.addBrands(self.newBrands,
                    function (response) {
                        if (response.message.indexOf("Successfully") !== -1) {
                            self.success = response.message;
                            self.closeAllModals();
                            self.isAddingBrand = false;
                            self.tableParams.page(1);
                            self.reloadTableAfterSave();
                        } else {
                            $('#confirmModal').modal("hide");
                            $('.modal-backdrop').removeAttr("style");
                            self.handleAddErrorMessage(response);
                        }

                    }, function (error) {
                        $('#confirmModal').modal("hide");
                        $('.modal-backdrop').attr('style', ' z-index: 0; ');
                        self.errorPopup = self.getErrorMessage(error);
                    });
            } else {
                $('.modal-backdrop').attr('style', ' z-index: 0; ');
            }
        };

        /**
         * Handle when click close in add popup but have data changed to show popup confirm.
         */
        self.closeModalUnsavedData = function () {
            if (self.newBrands.length !== 0 && self.newBrands[0].productBrandDescription.length !== 0) {
                self.titleConfirm = UNSAVED_DATA_CONFIRM_TITLE;
                self.messageConfirm = UNSAVED_DATA_CONFIRM_MESSAGE;
                $('#confirmModal').modal({backdrop: 'static', keyboard: true});
                $('.modal-backdrop').attr('style', ' z-index: 100000; ');
            } else {
                self.isValidNewBrand(self.newBrands);
                $('#addBrandModal').modal("hide");
                self.newBrands = [];
            }
        };

        /**
         * Close confirm popup
         */
        self.closeConfirmPopup = function () {
            $('#confirmModal').modal("hide");
            $('.modal-backdrop').attr('style', ' ');
        };

        /**
         * The function to reset all root message.
         */
        self.resetAllRootMessage = function () {
            $rootScope.error = null;
            $rootScope.success = null;
        };

        /**
         * Close add popup and confirm popup.
         */
        self.closeAllModals = function () {
            self.resetAllRootMessage();
            self.allowDeleteBrand = false;
            self.isAddingBrand = false;
            if (self.isReturnToTab) {
                $('#addBrandModal').modal("hide");
                $('#confirmModal').on('hidden.bs.modal', function () {
                    self.returnToTab();
                    $scope.$apply();
                });
            } else {
                $('#confirmModal').modal("hide");
                $('#addBrandModal').modal("hide");
            }
        };

        /**
         * Validates new brand./
         * @returns {boolean}
         */
        self.isValidNewBrand = function () {
            var errorMessages = [];
            var errorMessage = EMPTY;
            self.errorPopup = EMPTY;
            for (var i = 0; i < self.newBrands.length; i++) {
                self.newBrands[i].addClass = EMPTY;
                self.newBrands[i].addTooltip = EMPTY;
                if (self.isNullOrEmpty(self.newBrands[i].productBrandDescription)) {
                    errorMessage = "<li>" + self.BRAND_NAME_MANDATORY_FIELD_ERROR + "</li>";
                    if (errorMessages.indexOf(errorMessage) === -1) {
                        errorMessages.push(errorMessage);
                    }
                    self.newBrands[i].addClass = 'active-tooltip ng-invalid ng-touched';
                    self.newBrands[i].addTooltip = self.BRAND_NAME_MANDATORY_FIELD_ERROR;
                } else if (self.isValuesDuplicated(self.newBrands, self.newBrands[i].productBrandDescription)) {
                    errorMessage = "<li>" + DUPLICATE_BRANDS + "</li>";
                    if (errorMessages.indexOf(errorMessage) === -1) {
                        errorMessages.push(errorMessage);
                    }
                    self.newBrands[i].addClass = 'active-tooltip ng-invalid ng-touched';
                    self.newBrands[i].addTooltip = DUPLICATE_BRANDS;
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
         * Check duplicated value in array.
         *
         * @returns {boolean}
         */
        self.isValuesDuplicated = function (array, value) {
            var count = array.filter(function (obj) {
                return obj.productBrandDescription.toUpperCase() === value.toUpperCase()
            }).length;
            if (count >= 2) {
                return true;
            }
            return false;
        };

        /**
         * Validates the brand name before updates.
         * @returns {boolean}
         */
        self.isBrandValidBeforeUpdate = function () {
            if (!self.validationModel || self.validationModel.productBrandDescription.trim().length === 0) {
                self.error = self.BRAND_NAME_MANDATORY_FIELD_ERROR;
                self.showErrorOnTextBox('name', self.BRAND_NAME_MANDATORY_FIELD_ERROR);
                return false;
            }
            return true;
        };

        /**
         * Checks if the brand is changed or not.
         *
         * @returns {boolean}
         */
        self.isBrandChanged = function () {
            var brandTemp = angular.copy(self.selectedBrand);
            delete brandTemp['isEditing'];
            return JSON.stringify(brandTemp) !== self.originalBrand;
        };

        /**
         * Return edited row index.
         *
         * @returns {number}
         */
        self.getRowIndex = function () {
            if (self.selectedBrand == null) {
                return -1;
            }
            if (self.selectedBrand.productBrandId === 0) {
                return 0;
            }
            for (var i = 0; i < self.data.length; i++) {
                if (self.data[i].productBrandId === self.selectedBrand.productBrandId) {
                    return i;
                }
            }
        };

        /**
         * Calls api to delete the brand.
         */
        self.doDeleteBrand = function () {
            self.closeAllModals();
            self.isWaitingForResponse = true;
            brandApi.deleteBrand({productBrandId: self.selectedDeletedBrand.productBrandId},
                function (results) {
                    self.isWaitingForResponse = false;
                    self.success = results.message;
                    self.selectedDeletedBrand = null;
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
         * Initiate empty brand to display in the modal.
         *
         * @returns {{}}
         */
        self.initEmptyNewBrand = function () {
            return angular.copy(self.EMPTY_MODEL);
        };

        /**
         * Clear all the messages when click buttons.
         */
        self.clearMessages = function () {
            self.error = EMPTY;
            self.success = EMPTY;
            self.errorPopup = EMPTY;
            self.newBrands = [];
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
         * Add error border on exist fields
         */
        self.handleAddErrorMessage = function (response) {
            var tempMsgs = response.data;
            self.errorPopup = "<li>" + response.message + "</li>";
            for (var i = 0; i < tempMsgs.length; i++) {
                for (var j = 0; j < self.newBrands.length; j++) {
                    if (self.newBrands[j].productBrandDescription.toUpperCase() === tempMsgs[i].productBrandDescription.toUpperCase()) {
                        self.newBrands[j].addClass = 'active-tooltip ng-invalid ng-touched';
                        self.newBrands[j].addTooltip = ALREADY_EXISTS_BRAND;
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
                }
                return error.data.error;
            }
            return UNKNOWN_ERROR;
        };

        /**
         * Resets brand back to original state.
         * @param index
         */
        self.resetBrand = function (index) {
            self.error = EMPTY;
            self.success = EMPTY;
            self.data[index] = JSON.parse(self.originalBrand);
            self.resetSelectedBrand();
        };

        /**
         * Reset the status add or edit brand.
         */
        self.resetSelectedBrand = function () {
            self.selectedRowIndex = -1;
            self.selectedBrand = null;
        };

        /**
         * Determines if the filter has been cleared or not.
         */
        self.isFilterCleared = function () {
            if (!self.dataResolvingParams) {
                return true;
            }
            if (!self.dataResolvingParams.filter()["productBrandId"] &&
                !self.dataResolvingParams.filter()["productBrandDescription"]) {
                return true;
            }
            return false;
        };

        /**
         * The funtion is to check if newBrands is equals 0.
         * @returns {boolean}
         */
        self.checkIfNewBrandsIsEqualsZero = function () {
            var result = false;
            if (self.newBrands.length === 0) {
                result = true;
            }
            return result
        };

        /**
         * Returns the disabled status of button by productBrandId.
         *
         * @param productBrandId the productBrandId.
         * @returns {boolean} the disable status.
         */
        self.isDisabledButton = function (productBrandId) {
            return !(self.selectedRowIndex === -1 || self.selectedBrand.productBrandId === productBrandId);

        };

        /**
         * Returns the style for icon button.
         *
         * @param productBrandId the productBrandId of brand.
         * @returns {*} the style.
         */
        self.getDisabledButtonStyle = function (productBrandId) {
            if (self.isDisabledButton(productBrandId)) {
                return 'opacity: 0.5;'
            }
            return 'opacity: 1.0;';
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
         * Check object null or empty
         *
         * @param object
         * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
         */
        self.isNullOrEmpty = function (object) {
            return object === null || !object || object === "";
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
        self.onBrandNameChange = function () {
            var value = $('#name').val();
            if (value == null || value === undefined ||
                value.trim().length === 0) {
                self.showErrorOnTextBox('name', self.BRAND_NAME_MANDATORY_FIELD_ERROR);
            }
        };

        $scope.$on(self.VALIDATE_BRAND, function () {
            if (self.selectedBrand != null && self.isBrandChanged()) {
                self.isReturnToTab = true;
                self.allowDeleteBrand = false;
                self.titleConfirm = 'Confirmation';
                self.error = EMPTY;
                self.success = EMPTY;
                self.messageConfirm = UNSAVED_DATA_CONFIRM_MESSAGE;
                self.labelClose = 'No';
                $('#confirmModal').modal({backdrop: 'static', keyboard: true});
            } else {
                $rootScope.$broadcast(self.RETURN_TAB);
            }
        });

    }
})();