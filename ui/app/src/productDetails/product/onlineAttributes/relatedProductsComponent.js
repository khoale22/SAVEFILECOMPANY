/*
 *   relatedProductsComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Related Products -> Related Products page component.
 *
 * @author vn73545
 * @since 2.31.0
 */
(function () {

    angular.module('productMaintenanceUiApp').component('relatedProducts', {
        // isolated scope binding
        require: {
            productDetail: '^productDetail'
        },
        bindings: {
            changedRelatedProductsList: '=',
            currentRelatedProductsList: '=',
            originalRelatedProductsList: '=',
            selectedProducts: '=',
            listOfProducts: '=',
            success: '=',
            error: '=',
            productMaster: '<'
        },
        // Inline template which is binded to message variable in the component controller
        templateUrl: 'src/productDetails/product/onlineAttributes/relatedProducts.html',
        // The controller that handles our component logic
        controller: RelatedProductsController
    });

    RelatedProductsController.$inject = ['productSellingUnitsApi', 'productApi', 'UserApi', 'ngTableParams', '$scope',
        '$rootScope', 'HomeSharedService', 'ProductDetailAuditModal', 'productDetailApi', 'ProductSearchService',
        '$filter', 'appConstants'];


    function RelatedProductsController(productSellingUnitsApi, productApi, userApi, ngTableParams, $scope, $rootScope,
                                       homeSharedService, ProductDetailAuditModal, productDetailApi,
                                       productSearchService, $filter, appConstants) {
        var self = this;

        /**
         * Visibility of search results
         */
        self.searchSelectionResultsVisible = false;

        /**
         * This flag states if all the rows in the related products table has been selected
         * @type {boolean}
         */
        self.allRelatedProductsChecked = false;

        self.init = "";

        self.searchCriteria = {id: 0, name: 'init name'};

        /**
         * Related product tab
         * @type {String}
         */
        const RELATED_PRODUCT_TAB = "relatedProductTab";

        /**
         * Drop down display values for relationship type of related products
         */
        self.relatedProductCodes = [
            {id: 'ADDON', displayName: 'Add On Product'},
            {id: 'FPROD', displayName: 'Fixed Related Product'},
            {id: 'USELL', displayName: 'Upsell'}
        ];

        /**
         * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
         * (or re-initialized).
         */
        this.$onInit = function () {
            self.allRelatedProductsChecked = false;
            var productId = {'prodId': self.productMaster.prodId};
            productApi.getRelatedProducts(productId, self.createRelatedProducts, self.fetchError);
        };

        /**
         * AJAX callback for getting related products from REST svc
         * @param results
         */
        self.createRelatedProducts = function (results) {
            if (results !== null && results.length > 0) {
                angular.forEach(results, function (item) {
                    item.toDelete = false;
                    item.isChecked = false;
                    item.toAdd = false;
                });
            }
            self.originalRelatedProductsList = angular.copy(results);
            self.currentRelatedProductsList = angular.copy(results);
            self.changedRelatedProductsList = angular.copy(results);

            self.buildRelatedProductsTableParams();
        };

        /**
         * Fetches the error from the back end.
         * @param error
         */
        self.fetchError = function (error) {
            if (error && error.data) {
                if (error.data.message) {
                    self.setError(error.data.message);
                } else {
                    self.setError(error.data.error);
                }
            }
            else {
                self.setError("An unknown error occurred.");
            }
        };

        /**
         * Sets the controller's error message.
         * @param error The error message.
         */
        self.setError = function (error) {
            self.error = error;
        };

        /**
         * Builds the related products table params.
         */
        self.buildRelatedProductsTableParams = function () {
            if (self.changedRelatedProductsList.length > 0) {

                self.relatedProductsTableParams = new ngTableParams(
                    {
                        page: 1,
                        count: 10,
                    },
                    {
                        counts: [],
                        total: self.changedRelatedProductsList.length,
                        getData: function ($defer, params) {

                            self.currentRelatedProductsList =
                                self.changedRelatedProductsList.slice((params.page() - 1) * params.count(), params.page() * params.count());

                            // need to make sure new page shows all checked
                            if (self.allRelatedProductsChecked) {
                                self.checkAllRelatedProductsRows();
                            }

                            $defer.resolve(self.currentRelatedProductsList);
                        }
                    }
                );
            } else {
                self.currentRelatedProductsList = null;
            }
        };

        /**
         * This method is called whenever the add button is pressed on the related products tab
         */
        self.addNewRelatedProduct = function () {
            self.error = '';
            self.success = '';

            self.selectedRelationshipType = '';
            self.relationshipTypeTitle = 'Product Relationship Type';
            self.selectRelationshipTypeMessage = 'Please select the product relationship type';

            $('#selectRelationshipTypeModal').modal({backdrop: 'static', keyboard: true});
            self.searchSelectionResultsVisible = false;
            self.init = self.init + "Y";
        };

        /**
         * This method will remove the selected related product
         */
        self.deleteRelatedProduct = function () {
            // set toDelete flag on selected related product
            for (var index = 0; index < self.currentRelatedProductsList.length; index++) {
                if (self.currentRelatedProductsList[index].isChecked === true) {
                    self.currentRelatedProductsList[index].toDelete = true;
                    self.currentRelatedProductsList[index].isChecked = false;
                }
            }
            self.relatedProductsTableParams.page(1);
            self.relatedProductsTableParams.reload();
        };

        /**
         * Called by product-search-criteria when search button pressed
         * @param searchCriteria
         */
        self.updateSearchCriteria = function (searchCriteria) {
            self.searchCriteria = searchCriteria;
            self.searchSelectionResultsVisible = true;
        };

        /**
         * Called by product-search-selection when select button pressed
         * @param selectedProducts
         */
        self.updateSearchSelection = function (selectedProducts) {
            var valid = self.validateProductsToAdd(selectedProducts);
            if (valid && selectedProducts.length > 0) {
                self.selectedProducts = selectedProducts;

                self.addSelectedProductsToRelatedProductsList(selectedProducts);
            }
            $('#searchRelationshipModal').modal("hide");
            self.searchSelectionResultsVisible = false;
        };

        /**
         * Validate that the product relationships can be added
         * @param selectedProducts - products the user selected to add
         */
        self.validateProductsToAdd = function (selectedProducts) {
            for (var i = 0; i < selectedProducts.length; i++) {
                if (selectedProducts[i].prodId === self.productMaster.prodId) {
                    var error = 'Product id ' + self.productMaster.prodId + ' cannot be added to itself as a related product';
                    self.error = error;
                    return false;
                }

                for (var j = 0; j < self.changedRelatedProductsList.length; j++) {
                    if (selectedProducts[i].prodId === self.changedRelatedProductsList[j].key.relatedProductId) {
                        var error = 'Related product id ' + selectedProducts[i].prodId
                            + ' already exists for product id ' + self.changedRelatedProductsList[j].key.productId;
                        self.error = error;
                        return false;
                    }
                }
            }
            return true;
        };

        /**
         * This method will be called when click on continue button of add relationship.
         */
        self.continueAdd = function () {
            $('#selectRelationshipTypeModal').modal("hide");
            var isContinueAdd = true;
            $('#selectRelationshipTypeModal').on('hidden.bs.modal', function () {
                if (isContinueAdd) {
                    $('#searchRelationshipModal').modal({backdrop: 'static', keyboard: true});
                    isContinueAdd = false;
                }
            });
        };

        /**
         * This method will be called when click on continue button of add relationship.
         */
        self.cancelAdd = function () {
            $('#selectRelationshipTypeModal').modal("hide");
        };

        /**
         * Add selected products to related products list.
         * @param selectedProducts
         */
        self.addSelectedProductsToRelatedProductsList = function (selectedProducts) {
            angular.forEach(selectedProducts, function (selectedProduct) {
                var newProduct = {
                    productQuantity: 0.0,
                    key: {productId: -1, productRelationshipCode: "", relatedProductId: -1},
                    relatedProduct: {
                        description: "",
                        goodsProduct: {sellableProduct: false},
                        productPrimaryScanCodeId: -1
                    },
                    toAdd: true
                };

                newProduct.key.productId = self.productMaster.prodId;
                newProduct.key.productRelationshipCode = self.selectedRelationshipType;
                newProduct.key.relatedProductId = selectedProduct.prodId;
                newProduct.relatedProduct.description = selectedProduct.description;
                newProduct.relatedProduct.goodsProduct.sellableProduct = selectedProduct.goodsProduct.sellableProduct;
                newProduct.relatedProduct.productPrimaryScanCodeId = selectedProduct.productPrimaryScanCodeId;

                self.changedRelatedProductsList.push(newProduct);
            });


            // build table if it wasn't built yet because it was empty
            if (typeof self.relatedProductsTableParams === "undefined" || self.relatedProductsTableParams === null) {
                self.buildRelatedProductsTableParams();
            }

            // show last page of table
            var lastPage = Math.ceil(self.relatedProductsTableParams.total() / self.relatedProductsTableParams.count());
            self.relatedProductsTableParams.page(lastPage);

            self.relatedProductsTableParams.reload();
        };

        /**
         * Build related products table.
         */
        $scope.$on("BuildRelatedProductsTableParams", function () {
            self.buildRelatedProductsTableParams();
        });

        /**
         * Reset related products tab.
         */
        $scope.$on("ResetRelatedProductsTab", function () {
            self.allRelatedProductsChecked = false;
            self.buildRelatedProductsTableParams();
        });

        /**
         * Enable delete button or not.
         * @returns {boolean}
         */
        self.enableDeleteButton = function () {
            if (self.allRelatedProductsChecked || self.changedRelatedProductsList === null) {
                return true;
            }
            for (var index = 0; index < self.changedRelatedProductsList.length; index++) {
                if (self.changedRelatedProductsList[index].isChecked === true) {
                    return true;
                }
            }
            return false;
        };

        /**
         * Toggle all check-box of table.
         */
        self.toggleAllCheckedRelatedProducts = function () {
            if (self.allRelatedProductsChecked === true) {
                self.checkAllRelatedProductsRows(true);
            } else {
                self.checkAllRelatedProductsRows(false);
            }
        };

        /**
         * This method will check or uncheck all rows in related products table.
         * @param status
         */
        self.checkAllRelatedProductsRows = function (status) {
            if (self.changedRelatedProductsList !== null && self.changedRelatedProductsList.length > 0) {
                angular.forEach(self.changedRelatedProductsList, function (item) {
                    item.isChecked = status;
                });
            }
        };

        /**
         * Validate status of all check-box.
         */
        self.validateAllCheckedRelatedProducts = function () {
            if (self.changedRelatedProductsList !== null && self.changedRelatedProductsList.length > 0) {
                var status = true;
                angular.forEach(self.changedRelatedProductsList, function (item) {
                    if (item.isChecked === false) {
                        status = false;
                    }
                });
                self.allRelatedProductsChecked = status;
            }
        };

        /**
         * Switch product and select product info tab
         * @param relatedProduct
         */
        self.switchToProductInfo = function (relatedProduct) {
            self.productDetail.selectedProduct = relatedProduct.relatedProduct;
            self.productDetail.tabToShowAfterUpdate = "productInfoTab";
            //Backup searching criteria data for navigate to
            //Only one time set temporary value even though the Product Id link clicked many times
            if (productSearchService.getListOfProductsTempBackup().length == 0) {
                productSearchService.setListOfProductsTempBackup(productSearchService.getListOfProductsBackup());
            }
            productSearchService.setSearchSelection(self.productMaster.prodId);
            productSearchService.setSearchType(productSearchService.BASIC_SEARCH);
            productSearchService.setSelectionType(productSearchService.PRODUCT_ID);
            productSearchService.setToggledTab(RELATED_PRODUCT_TAB);
            productSearchService.setListOfProductsBackup(self.listOfProducts);
            productSearchService.setListOfProducts(self.currentRelatedProductsList);
            //Set from page navigated to
            productSearchService.setFromPage(appConstants.HOME);
            productSearchService.setDisableReturnToList(false);
            $rootScope.$broadcast('CreateListOfProducts');
            homeSharedService.broadcastUpdateProduct();
        };

        /**
         * Switch product and select UPC info tab
         * @param relatedProduct
         */
        self.switchToUPCInfo = function (relatedProduct) {
            self.productDetail.selectedProduct = relatedProduct.relatedProduct;
            self.productDetail.tabToShowAfterUpdate = "upcInfoTab";
            //Backup searching criteria data for navigate to
            //Only one time set temporary value even though the Product Id link clicked many times
            if (productSearchService.getListOfProductsTempBackup().length == 0) {
                productSearchService.setListOfProductsTempBackup(productSearchService.getListOfProductsBackup());
            }
            productSearchService.setSearchSelection(self.productMaster.prodId);
            productSearchService.setSearchType(productSearchService.BASIC_SEARCH);
            productSearchService.setSelectionType(productSearchService.PRODUCT_ID);
            productSearchService.setToggledTab(RELATED_PRODUCT_TAB);
            productSearchService.setListOfProductsBackup(self.listOfProducts);
            productSearchService.setListOfProducts(self.currentRelatedProductsList);
            //Set from page navigated to
            productSearchService.setFromPage(appConstants.HOME);
            productSearchService.setDisableReturnToList(false);
            $rootScope.$broadcast('CreateListOfProducts');
            homeSharedService.broadcastUpdateProduct();
        };

        /**
         * Converts relationship code to a UI readable string
         * @param code
         * @returns {*}
         */
        self.productRelationshipCodeToString = function (code) {
            var relatedProductCodes = {
                'ADDON': 'Add On Product',
                'FPROD': 'Fixed Related Product',
                'USELL': 'Upsell'
            };

            return relatedProductCodes[code];
        };
    }
})();
