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
 * Code Table -> product brand option page component.
 *
 * @author s769046
 * @since 2.12.0
 */
(function () {

    var app = angular.module('productMaintenanceUiApp');
    app.component('productLineBrandComponent', {
        templateUrl: 'src/codeTable/productLineBrand/productLineBrand.html',
        controller: productLineBrandController
    });

    productLineBrandController.$inject = ['$rootScope', '$scope', 'productLineBrandApi', 'ngTableParams', 'productLineApi', 'ProductBrandApi', 'urlBase', 'DownloadService'];
    /**
     * Product Line Brand component's controller definition.
     *
     * @param $scope scope of the case pack info component.
     * @param productLineBrandApi the api of product brands.
     * @param ngTableParams the table display product brands.
     * @constructor
     */
    function productLineBrandController($rootScope, $scope, productLineBrandApi, ngTableParams, productLineApi, productBrandApi, urlBase, downloadService) {
        /** All CRUD operation controls of choice option page goes here */
        var self = this;

        self.selectedProductLineBrand = null;
        self.PRODUCT_LINE_BRAND_DELETE_MESSAGE_HEADER = 'Delete Product Line Brand';
        self.PRODUCT_LINE_BRAND_DELETE_CONFIRM_MESSAGE_STRING = 'Are you sure you want to delete the selected Product Line Brand?';

        self.PRODUCT_LINE_BRAND_ADD_MESSAGE_HEADER = 'Add Product Line Brand';
        self.PRODUCT_LINE_BRAND_ADD_CONFIRM_MESSAGE_STRING = 'Are you sure you want to add the selected Product Line Brand?';

        self.productLinesList= [];
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
         * The list of product sub brands information.
         *
         * @type {Array}
         */
        self.productLineBrands = [];

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
         *  Holds the product sub brand id when user type on product sub brand id text box.
         *
         * @type {String}
         */
        self.prodLineBrandId = '';

        /**
         * Holds the selected product sub brand name when user type on product sub brand name text box.
         *
         * @type {String}
         */
        self.prodLineBrandName = '';

        /**
         *  Holds the product sub brand id when user type on product sub brand id text box.
         *
         * @type {String}
         */
        self.addProdLineBrandId = '';

        /**
         * Holds the selected product sub brand name when user type on product sub brand name text box.
         *
         * @type {String}
         */
        self.addProdLineBrandName = '';

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
            self.getProductLinesList();
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
                        self.getProductLineBrandsPage(params.page() - 1);
                    }
                }
            );
        };

        self.getProductLinesList = function(query) {
            if (query) {
                productLineApi.filterProductLines({
                        page: 0,
                        pageSize:20,
                        searched: query
                    },
                    function(results) {
                        self.productLinesList=results;
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
            }}

        /**
         * Find all the list of product sub brands information.
         *
         * @param page the page number.
         */
        self.getProductLineBrandsPage = function (page) {
            productLineBrandApi.getProductLineBrandsPage({
                    page: page,
                    pageSize: self.PAGE_SIZE,
                    includeCount: self.includeCount,
                    id: self.prodLineBrandId,
                    description: self.prodLineBrandName
                },
                self.productLineBrandResponseSuccess,
                self.fetchError);
        };

        /**
         * Handle event when click show own brand only checkbox, or filter by product sub brand id or name.
         */
        self.refreshProductLineBrandTable = function () {
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
            return self.prodLineBrandId || self.prodLineBrandName;
        };

        /**
         * Export product sub brand to csv file.
         */
        self.exportProductLineBrand = function () {
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
            var exportUrl = urlBase + '/pm/codeTable/productLineBrand/exportLineBrandToCSV?';
            exportUrl += 'prodLineBrandId=' + self.prodLineBrandId;
            exportUrl += '&prodLineBrandName=' + self.prodLineBrandName;
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
        self.productLineBrandResponseSuccess = function (results) {
            self.isWaitingForResponse = false;
            if (results.complete) {
                self.totalRecordCount = results.recordCount;
                self.totalPages = results.pageCount;
                self.dataResolvingParams.total(self.totalRecordCount);
            }
            if (results.data.length === 0) {
                self.dataResolvingParams.data = [];
                self.productLineBrands = [];
                self.error = self.NO_RECORDS_FOUND;
            } else {
                self.error = null;
                self.productLineBrands = results.data;
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
         * @param productLine the product line to delete.
         */
        self.deleteProductLineBrand = function(productLineBrand) {
            self.selectedDeletedProductLineBrand = productLineBrand;
            self.error = '';
            self.success = '';
            self.titleConfirm = self.PRODUCT_LINE_BRAND_DELETE_MESSAGE_HEADER;
            self.messageConfirm = self.PRODUCT_LINE_BRAND_DELETE_CONFIRM_MESSAGE_STRING;
            self.labelClose = 'No';
            self.allowDeleteProductLineBrand = true;
            $('#confirmModal').modal({backdrop: 'static', keyboard: true});
        };

        /**
         * Calls confirmation modal to confirm delete action.
         * @param productLine the product line to delete.
         */
        self.addProductLineBrand = function(productLineBrand) {
            self.selectedAddedProductLineBrand = productLineBrand;
            self.error = '';
            self.success = '';
            self.titleConfirm = self.PRODUCT_LINE_BRAND_ADD_MESSAGE_HEADER;
            self.messageConfirm = self.PRODUCT_LINE_BRAND_ADD_CONFIRM_MESSAGE_STRING;
            self.labelClose = 'No';
            self.allowAddProductLineBrand = true;
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
            self.allowDeleteProductLineBrand = false;
            self.allowAddProductLineBrand = false;
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
                self.prodLineBrandName = self.NO_FILTER_BY_PARAMETER;
                self.prodLineBrandId = self.NO_FILTER_BY_PARAMETER;
                self.refreshProductLineBrandTable();
            }
        };

        /**
         * Checks if the product line is changed or not.
         *
         * @param productLine the product line.
         * @param origData the list of original product line.
         * @returns {boolean}
         */
        self.isProductLineBrandChanged = function () {
            return false;

        };

        /**
         * Calls api to delete the product line.
         */
        self.doDeleteProductLineBrand = function () {
            self.doCloseModal();
            self.isWaitingForResponse = true;
            var productLineBrandKey=this.selectedDeletedProductLineBrand.key;
            productLineBrandApi.deleteProductLineBrand(productLineBrandKey,
                function (results) {
                    self.isWaitingForResponse = false;
                    self.success = results.message;
                    self.selectedDeletedProductLineBrand = null;
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
        self.doAddProductLineBrand = function () {
            self.doCloseModal();
            self.isWaitingForResponse = true;
            productLineBrandApi.addProductLineBrand({
                    id: self.addProdLineBrandId,
                    description: self.addProdLineBrandName
                },
                function (results) {
                    self.isWaitingForResponse = false;
                    self.success = results.message;
                    self.selectedAddedProductLineBrand = null;
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
        self.doNotAddProductLineBrand = function () {
            self.doCloseModal();
            self.selectedAddedProductLineBrand = null;
        };

        self.createProductLineBrands=function(productLineBrand){
            var productLineBrands=[];
            productLineBrands.push(productLineBrand);
            return productLineBrands;
        }
        /**
         * Clear message listener.
         */
        $scope.$on(self.VALIDATE_PRODUCT_LINE_BRAND, function () {
            if (self.selectedProductLineBrand != null && self.isProductLineBrandChanged()) {
                self.isReturnToTab = true;
                self.allowDeleteProductLineBrand = false;
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
