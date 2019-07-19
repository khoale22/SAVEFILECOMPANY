/*
 *   productSubBrandComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */

'use strict';

/**
 * Code Table -> brandTierCode component.
 *
 * @author s769046
 * @since 2.12.0
 */
(function () {

    var app = angular.module('productMaintenanceUiApp');
    app.component('brandTierCodeComponent', {
        templateUrl: 'src/codeTable/brandTierCode/brandTierCodeComponent.html',
        controller: brandTierCodeController
    });

    brandTierCodeController.$inject = ['$rootScope', '$scope', 'brandTierCodeApi', 'ngTableParams', 'tierCodeApi', 'ProductBrandApi', 'urlBase', 'DownloadService'];
    /**
     * Product Line Brand component's controller definition.
     *
     * @param $scope scope of the case pack info component.
     * @param brandTierCodeApi the api of product brands.
     * @param ngTableParams the table display product brands.
     * @constructor
     */
    function brandTierCodeController($rootScope, $scope, brandTierCodeApi, ngTableParams, tierCodeApi, productBrandApi, urlBase, downloadService) {
        /** All CRUD operation controls of choice option page goes here */
        var self = this;

        self.selectedBrandTierCode = null;
        self.BRAND_TIER_CODE_DELETE_MESSAGE_HEADER = 'Delete Brand Tier Code';
        self.BRAND_TIER_CODE_DELETE_CONFIRM_MESSAGE_STRING = 'Are you sure you want to delete the selected Brand Tier Code?';

        self.BRAND_TIER_CODE_ADD_MESSAGE_HEADER = 'Add Brand Tier Code';
        self.BRAND_TIER_CODE_ADD_CONFIRM_MESSAGE_STRING = 'Are you sure you want to add the selected Brand Tier Code?';

        self.productBrandTiersList= [];
        self.productBrandsList= [];
        self.RETURN_TAB = 'returnTab';

        /**
         * The default error message.
         *
         * @type {string}
         */
        self.UNKNOWN_ERROR = "An unknown error occurred.";

        /**
         * The default no records found message.
         *
         * @type {string}
         */
        self.NO_RECORDS_FOUND = "No records found.";

        /**
         * The default page number.
         *
         * @type {number}
         */
        self.PAGE = 1;

        /**
         * The default page size.
         *
         * @type {number}
         */
        self.PAGE_SIZE = 20;

        /**
         * The param to indicate not filter.
         * @type {number}
         */
        self.NO_FILTER_BY_PARAMETER = '';

        /**
         * Max time to wait for export file.
         *
         * @type {number}
         */
        self.WAIT_TIME = 1200;

        /**
         * Flag for waiting response from back end.
         *
         * @type {boolean}
         */
        self.isWaitingForResponse = false;

        /**
         * Flag for waiting response from back end.
         *
         * @type {boolean}
         */
        self.isAdding = false;

        /**
         * The flag check whether downloading or not.
         *
         * @type {boolean}
         */
        self.isDownloading = false;

        /**
         * The list of brandTierCodes information.
         *
         * @type {Array}
         */
        self.brandTierCodes = [];

        /**
         * The ngTable object that will be waiting for data while the report is being refreshed.
         *
         * @type {object}
         */
        self.defer = null;

        /**
         * The parameters passed from the ngTable when it is asking for data.
         *
         * @type {object}
         */
        self.dataResolvingParams = null;

        /**
         * The total number of pages in the report.
         *
         * @type {null}
         */
        self.totalPages = null;

        /**
         * The total records in the report.
         *
         * @type {null}
         */
        self.totalRecordCount = null;

        /**
         *  Holds the brandTierCodeId on text box.
         *
         * @type {String}
         */
        self.brandTierCodeId = '';

        /**
         * Holds the selected brandTierCode nameo n text box.
         *
         * @type {String}
         */
        self.brandTierCodeName = '';

        /**
         *  Holds the addbrandTierCodeid on text box.
         *
         * @type {String}
         */
        self.addBrandTierCodeId = '';

        /**
         * Holds the selected brandTierCodeName when user type on brandTierCodeName text box.
         *
         * @type {String}
         */
        self.addBrandTierCodeName = '';

        /**
         * Check if it is the first time to filter or not.
         *
         * @type {boolean}
         */
        self.firstSearch = true;

        /**
         * Check if it is search with count or not.
         *
         * @type {boolean}
         */
        self.includeCount = false;

        /**
         * Selected edit product line brand.
         * @type {null}
         */

        /**
         * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
         * (or re-initialized).
         */
        this.$onInit = function () {
            self.isWaitingForResponse = true;
            self.firstSearch = true;
            self.tableParams = self.buildTable();
            self.getProductBrandTiersList();
            self.getProductBrandsList();
            if($rootScope.isEditedOnPreviousTab){
                self.error = $rootScope.error;
                self.success = $rootScope.success;
            }
            $rootScope.isEditedOnPreviousTab = false;
        };

        /**
         * Constructs the table that shows the product sub brands.
         */
        self.buildTable = function () {
            return new ngTableParams(
                {
                    page: self.PAGE,
                    count: self.PAGE_SIZE
                }, {
                    counts: [],

                    /**
                     * Called by ngTable to load data.
                     *
                     * @param $defer The object that will be waiting for data.
                     * @param params The parameters from the table helping the function determine what data to get.
                     */
                    getData: function ($defer, params) {

                        self.isWaitingForResponse = true;
                        // Save off these parameters as they are needed by the callback when data comes back from
                        // the back-end.
                        self.defer = $defer;
                        self.dataResolvingParams = params;

                        // If it is first time to search, then it search with count, otherwise it doesn't include count.
                        if (self.firstSearch) {
                            self.includeCount = true;
                            self.firstSearch = false;
                        } else {
                            self.includeCount = false;
                        }


                        // if (typeof id === "undefined") {
                        //     id = "";
                        // }
                        // if (typeof description === "undefined") {
                        //     description = "";
                        // }

                        // Issue calls to the backend to get the data.
                        self.getBrandTierCodesPage(params.page() - 1);
                    }
                }
            );
        };

        self.getProductBrandTiersList = function(query) {
            if (query) {
                tierCodeApi.filterProductLines({
                        page: 0,
                        pageSize:20,
                        searched: query
                    },
                    function(results) {
                        self.productBrandTiersList=results;
                    },
                    function (error) {
                        self.fetchError(error);
                    });
            }}


        self.getProductBrandsList = function(query) {
            if (query) {
                productBrandApi.filterProductBrands({
                        page: 0,
                        pageSize:20,
                        productBrand: query
                    },
                    function(results) {
                        self.productBrandsList=results.data;
                    },
                    function (error) {
                        self.fetchError(error);
                    });
            }};

        /**
         * Find all the list of brandTierCodes information.
         *
         * @param page the page number.
         */
        self.getBrandTierCodesPage = function (page) {
            brandTierCodeApi.getBrandTierCodesPage({
                    page: page,
                    pageSize: self.PAGE_SIZE,
                    includeCount: self.includeCount,
                    id: self.brandTierCodeId,
                    description: self.brandTierCodeName
                },
                self.brandTierCodeResponseSuccess,
                self.fetchError);
        };

        /**
         * Handle event when click show own brand only checkbox, or filter by product sub brand id or name.
         */
        self.refreshBrandTierCodeTable = function () {
            self.firstSearch = true;
            self.tableParams = self.buildTable();
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
         * Check whether input data filter or not.
         *
         * @returns {boolean}
         */
        self.isDataFiltered = function () {
            return self.brandTierCodeId || self.brandTierCodeName;
        };

        /**
         * Export product sub brand to csv file.
         */
        self.exportBrandTierCode = function () {
            if (!self.totalRecordCount) return;
            self.isDownloading = true;

            downloadService.export(self.generateExportUrl(), self.createExportFileName(), self.WAIT_TIME, self.exportResponse);
        };

        /**
         * Generate the export url.
         *
         * @type {string}
         */
        self.generateExportUrl = function () {
            var exportUrl = urlBase + '/pm/codeTable/brandTierCode/exportBrandTierCodeToCSV?';
            exportUrl += 'brandTierCodeId=' + self.brandTierCodeId;
            exportUrl += '&brandTierCodeName=' + self.brandTierCodeName;
            exportUrl += '&page=' + self.PAGE;
            return exportUrl;
        };

        /**
         * Generate the file name to export.
         *
         * @returns {string}
         */
        self.createExportFileName = function () {
            var fileName = 'Sub-Brand_';
            var d = new Date();
            fileName += d.getFullYear();
            fileName += ('0' + (d.getMonth() + 1)).slice(-2);
            fileName += ('0' + d.getDate()).slice(-2);
            fileName += ('0' + d.getHours()).slice(-2);
            fileName += ('0' + d.getMinutes()).slice(-2);
            fileName += ('0' + d.getSeconds()).slice(-2);
            fileName += '.csv';
            return fileName;
        };

        /**
         * Load product sub brand data response success.
         *
         * @param results the results to load.
         */
        self.brandTierCodeResponseSuccess = function (results) {
            self.isWaitingForResponse = false;
            if (results.complete) {
                self.totalRecordCount = results.recordCount;
                self.totalPages = results.pageCount;
                self.dataResolvingParams.total(self.totalRecordCount);
            }
            if (results.data.length === 0) {
                self.dataResolvingParams.data = [];
                self.brandTierCodes = [];
                self.error = self.NO_RECORDS_FOUND;
            } else {
                self.error = null;
                self.brandTierCodes = results.data;
                self.defer.resolve(results.data);
            }
        };

        /**
         * Handle when export to csv has error.
         */
        self.exportResponse = function () {
            self.isDownloading = false;
        };

        /**
         * Calls confirmation modal to confirm delete action.
         * @param brandTierCode the brandTierCode to delete.
         */
        self.deleteBrandTierCode = function(brandTierCode) {
            self.selectedDeletedBrandTierCode = brandTierCode;
            self.error = '';
            self.success = '';
            self.titleConfirm = self.BRAND_TIER_CODE_DELETE_MESSAGE_HEADER;
            self.messageConfirm = self.BRAND_TIER_CODE_DELETE_CONFIRM_MESSAGE_STRING;
            self.labelClose = 'No';
            self.allowDeleteBrandTierCode = true;
            $('#confirmModal').modal({backdrop: 'static', keyboard: true});
        };

        /**
         * Calls confirmation modal to confirm delete action.
         * @param brandTierCode the brandTierCode to delete.
         */
        self.addBrandTierCode = function(brandTierCode) {
            self.selectedAddedBrandTierCode = brandTierCode;
            self.error = '';
            self.success = '';
            self.titleConfirm = self.BRAND_TIER_CODE_ADD_MESSAGE_HEADER;
            self.messageConfirm = self.BRAND_TIER_CODE_ADD_CONFIRM_MESSAGE_STRING;
            self.labelClose = 'No';
            self.allowAddBrandTierCode = true;
        };

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
            self.allowDeleteBrandTierCode = false;
            self.allowAddBrandTierCode = false;
            if (self.isReturnToTab) {
                $('#confirmModal').on('hidden.bs.modal', function () {
                    self.returnToTab();
                    $scope.$apply();
                });
            } else {
                $('#confirmModal').modal("hide");
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
                return self.UNKNOWN_ERROR;
            }
        };

        self.doClearFilter = function () {
            if (self.isDataFiltered()) {
                self.BrandTierCodeName = self.NO_FILTER_BY_PARAMETER;
                self.BrandTierCodeId = self.NO_FILTER_BY_PARAMETER;
                self.refreshBrandTierCodeTable();
            }
        };

        /**
         * Checks if the product line is changed or not.
         *
         * @param productLine the product line.
         * @param origData the list of original product line.
         * @returns {boolean}
         */
        self.isBrandTierCodeChanged = function () {
            return false;

        };

        /**
         * Calls api to delete the product line.
         */
        self.doDeleteBrandTierCode = function () {
            self.doCloseModal();
            self.isWaitingForResponse = true;
            var brandTierCodeKey=this.selectedDeletedBrandTierCode.key;
            brandTierCodeApi.deleteBrandTierCode(brandTierCodeKey,
                function (results) {
                    self.isWaitingForResponse = false;
                    self.success = results.message;
                    self.selectedDeletedBrandTierCode = null;
                    self.tableParams.reload()
                },
                function (error) {
                    self.fetchError(error);
                }
            );
        };

        self.doAddModal = function() {
            $('#confirmModal').modal({backdrop: 'static', keyboard: true});
        }

        /**
         * Calls api to delete the product line.
         */
        self.doAddBrandTierCode = function () {
            self.doCloseModal();
            self.isWaitingForResponse = true;
            brandTierCodeApi.addBrandTierCode({
                    id: self.addBrandTierCodeId,
                    description: self.addBrandTierCodeName
                },
                function (results) {
                    self.isWaitingForResponse = false;
                    self.success = results.message;
                    self.selectedAddedBrandTierCode = null;
                    self.tableParams.reload()
                },
                function (error) {
                    self.fetchError(error);
                }
            );
        };

        /**
         * Calls api to delete the product line.
         */
        self.doNotAddBrandTierCode = function () {
            self.doCloseModal();
            self.selectedAddedBrandTierCode = null;
        };

        self.createBrandTierCodes=function(brandTierCode){
            var brandTierCodes=[];
            brandTierCodes.push(brandTierCode);
            return brandTierCodes;
        }
        /**
         * Clear message listener.
         */
        $scope.$on(self.VALIDATE_BRAND_TIER_CODE, function () {
            if (self.selecteBrandTierCode != null && self.isBrandTierCodeChanged()) {
                self.isReturnToTab = true;
                self.allowDeleteBrandTierCode = false;
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
