/*
 *   variantsComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * variants -> variants Info page component.
 *
 * @author vn87351
 * @since 2.15.0
 */
(function () {

    angular.module('productMaintenanceUiApp').component('variants', {
        // isolated scope binding
        require: {
            productDetail: '^productDetail'
        },
        bindings: {
            listOfProducts: '=',
            productMaster: '<',
            itemMaster: '='

        },
        // Inline template which is binded to message variable in the component controller
        templateUrl: 'src/productDetails/product/variants/variants.html',
        // The controller that handles our component logic
        controller: VariantsController
    });

    VariantsController.$inject = ['productApi', '$scope', '$timeout', 'ProductFactory', 'variantsApi',
        'ngTableParams', '$rootScope', 'ProductSearchService', 'imageInfoApi', '$filter', 'DownloadImageService', 'appConstants','productDetailApi', 'ECommerceViewApi'];


    function VariantsController(productApi, $scope, $timeout, productFactory, variantsApi, ngTableParams,
                                $rootScope, productSearchService, imageInfoApi, $filter, downloadImageService, appConstants,productDetailApi, eCommerceViewApi) {

        var self = this;
        /**
         * Whether or not the controller is waiting for data
         * @type {boolean}
         */
        self.isWaiting = false;

        /**
         * message susscess from api
         */
        self.success = '';
        /**
         * message error form api
         */
        self.error = '';
        self.customerFriendlyDescription1 = 'TAG1';
        self.customerFriendlyDescription2 = 'TAG2';
        self.SEARCH_PRODUCT_EVENT = "searchProduct";
        self.variantQuantityUnknownCode = 'KNOWN';
        self.variantQuantityUndefinedCode = 'UNDEF';
        self.variantItemTableParams = {};
        self.variantsTableParams = {};
        self.lstPrimaryImages = [];
        self.variantData = [];
        self.parentProductId;
        self.sumQuantity = 0;
        self.productItemVariantTemp = {
            key: {
                itemKeyTypeCode: '',
                itemId: 0,
                productId: 0
            },
            retailPackQuantity: 0,
        }
        self.isShowAddForm = false;
        self.isErrorTabItem = false;
        self.tabSelected = 1;

        self.NUMBERIC_MUST_BE_GREATER_THAN_0_ERROR = 'Quantity for selected Variant Products should be greater than 0.';
        self.ASSIGN_IMAGE_SUCCESS_MSG = "Assign Image(s) to Variant(s) successfully.";
        self.IMG_ALREADY_ASSIGNED_MSG = '<li>Image at row #index is already assigned to Variant Product #productId.</li>'
        const IMAGE_ENCODE = 'data:image/jpg;base64,';
        self.disabledVariantQuantityUnknown = false;
        /**
         * Messages
         * @type {string}
         */
        self.IMAGE_NOT_FOUND = 'image-not-found';


        self.imagesInfo = [];
        /**
         * Current image loading.
         * @type {string}
         */
        self.imageBytes = '';
        self.cacheImages = null;
        self.checkAllVariant = false;
        /**
         * Variants sub tab
         * @type {String}
         */
        const VARIANTS_SUB_TAB = "variantsSubTab";
        /**
         * Variants item sub tab
         * @type {String}
         */
        const VARIANTS_ITEM_SUB_TAB = "variantsItemSubTab";
        /**
         * Variants
         * @type {String}
         */
        const VARIANTS = "variants";
        /**
         * Variants item
         * @type {String}
         */
        const VARIANTS_ITEM = "variantsItem";
        /**
         * The default error message.
         * @type {string}
         */
        self.UNKNOWN_ERROR = "An unknown error occurred.";
        /**
         * Sale channel hebcom
         * @type {String}
         */
        const SALE_CHANNEL_HEB_COM = "HebCom";

        /**
         * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
         * (or re-initialized).
         */
        this.$onInit = function () {
            self.disableReturnToList = productSearchService.getDisableReturnToList();
        }
        /**
         * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
         * (or re-initialized).
         */
        this.$onChanges = function () {
            self.success = '';
            self.error = '';
            self.variantQuantityUnknown = false;
            self.variantQuantityUnknownOrg = false;
            self.getProductMaster();
            //Custom to active the tab and panel.
            //In the case return to list to variant page.
            if (productSearchService.getToggledTab() !== null
                && productSearchService.getReturnToListButtonClicked()) {
                if (productSearchService.getToggledTab() === VARIANTS_SUB_TAB) {
                    self.chooseTab(VARIANTS);
                } else if (productSearchService.getToggledTab() === VARIANTS_ITEM_SUB_TAB) {
                    self.chooseTab(VARIANTS_ITEM);
                }
                productSearchService.setReturnToListButtonClicked(false);
                productSearchService.setToggledTab(null);
            } else {
                self.chooseTab(VARIANTS);
            }

            if (self.itemMaster !== undefined
                && self.itemMaster !== null
                && self.itemMaster.variantCode != self.variantQuantityUnknownCode) {
                self.variantQuantityUnknown = true;
                self.variantQuantityUnknownOrg = true;
            }
            self.isWaiting = true;
            variantsApi.findVariantsData({
                    productId: self.productMaster.prodId
                },
                self.callApiSuccess, self.callApiError);
            if (self.variantData.length > 0) {
                self.disabledVariantQuantityUnknown = false;
            } else {
                self.disabledVariantQuantityUnknown = true;
            }
        }

        /**
         * Get product master data.
         */
        self.getProductMaster = function () {
            productDetailApi.getUpdatedProduct(
                {
                    prodId: self.productMaster.prodId
                },
                function (result) {
                    self.productMaster = angular.copy(result);
                }, self.handleError)
        }
        /**
         * reset form
         */
        self.resetData = function (hasSaveData) {
            if (!hasSaveData) {
                self.success = '';
                self.error = '';
            }
            self.checkAllVariant = false;
            self.init();
        }
        /**
         * Initiates the construction.
         */
        self.init = function () {

            self.isWaiting = true;
            self.getAllImagesOfProduct();
            self.onLive = true;
            self.variantQuantityUnknown = false;
            if (self.itemMaster !== undefined
                && self.itemMaster !== null
                && self.itemMaster.variantCode != self.variantQuantityUnknownCode) {
                self.variantQuantityUnknown = true;
            }
            variantsApi.findVariantsData({
                    productId: self.productMaster.prodId
                },
                self.callApiSuccess, self.callApiError);

        }
        /**
         * Processing if call api success
         * @param results
         */
        self.callApiSuccess = function (response) {
            self.isWaiting = false;
            self.variantData = response;
            if (self.variantData.length != 0) {
                self.parentProductId = self.variantData[0].parentProduct.prodId;
                self.parentProductDecription = self.variantData[0].parentProduct.description;
                self.disabledVariantQuantityUnknown = false;
                self.changeVariantUnknownQuantity();
            } else {
                self.disabledVariantQuantityUnknown = true;
                self.parentProductId = self.productMaster.prodId;
                self.parentProductDecription = self.productMaster.description;
            }
            self.loadDataVariantsTab();

        };
        /**
         * call api error and throw message
         * @param error
         */
        self.callApiError = function (error) {
            self.isWaiting = false;
            if (error && error.data) {
                if (error.data.message) {
                    self.error = error.data.message;
                } else {
                    self.error = error.data.error;
                }
            }
            else {
                self.error = "An unknown error occurred.";
            }
        };
        self.checkQuantityZero = function () {
            var rs = false;
            angular.forEach(self.variantData, function (product) {
                if (product.relatedProduct && product.relatedProduct.checked && product.relatedProduct.productItemVariant
                    && product.relatedProduct.productItemVariant.retailPackQuantity <= 0) {
                    rs = true;
                }
            });
            return rs;
        }
        /**
         * calculate sum quantity
         */
        self.reCalculateQuantity = function (variants) {
            if (variants && variants.relatedProduct)
                variants.relatedProduct.isChange = true;
            self.reCalculateQuantitySum();
        }
        /**
         * recalculate quantity sum
         */
        self.reCalculateQuantitySum = function () {
            self.sumQuantity = 0;
            angular.forEach(self.variantData, function (product) {
                if (product.relatedProduct && product.relatedProduct.productItemVariant && product.relatedProduct.productItemVariant.retailPackQuantity) {
                    self.sumQuantity += parseInt(product.relatedProduct.productItemVariant.retailPackQuantity);
                }
            });
        }
        /**
         * checked any item on variant item tab
         * @returns {boolean}
         */
        self.anyCheckedItem = function () {
            var rs = false;
            angular.forEach(self.variantData, function (product) {
                if (product.relatedProduct && product.relatedProduct.checked) {
                    rs = true;
                }
            });
            return rs;
        }
        /**
         * check valid quantity
         * @returns {boolean}
         */
        self.checkValidQuantity = function () {
            self.reCalculateQuantitySum();

            if (!self.variantQuantityUnknown && self.anyCheckedItem() && self.sumQuantity !== self.productMaster.prodItems[0].itemMaster.pack) {
                self.error = 'Total Quantity should be equal to Case Pack Quantity.';
                self.isErrorTabItem = true;
                return false;
            }
            if (!self.variantQuantityUnknown && self.checkedItemQuantityZero()) {
                self.error = 'Quantity for selected Variant Products should be greater than 0';
                self.isErrorTabItem = true;
                return false;
            }
            if (self.anyCheckedItem() && self.sumQuantity === self.productMaster.prodItems[0].itemMaster.pack) {
                self.error = '';
                self.isErrorTabItem = false;
                return true;
            }
            return true;
        }
        self.checkedItemQuantityZero = function () {
            var rs = false;
            angular.forEach(self.variantData, function (product) {
                if (product.relatedProduct && product.relatedProduct.checked && product.relatedProduct.productItemVariant.retailPackQuantity === 0) {
                    rs = true;
                }
            });
            return rs;
        }
        /**
         * Load data and show on ui
         */
        self.loadDataVariantsTab = function () {
            if (self.variantData != null && self.variantData.length > 0) {
                self.checkAllValue = true;
                self.loadCustomerFriendlyDescription(self.variantData);
                self.loadPrimaryImage(self.variantData);
            }
            $scope.variantsTableParams = new ngTableParams({
                page: 1,
                count: 20
            }, {
                counts: [],
                data: self.variantData//self.initDataForNgTable()
            });
            $scope.variantsTableParams.reload();
            self.reCalculateQuantity();
        };

        /**
         * load primary image
         * @param data
         */
        self.loadPrimaryImage = function (data) {
            self.upcs = [];
            angular.forEach(data, function (product) {
                if (product && product.relatedProduct) {
                    self.upcs.push(product.relatedProduct.productPrimaryScanCodeId);
                }
            });
            variantsApi.findPrimaryImage(self.upcs, function (response) {
                console.log(response)
                self.lstPrimaryImages = response.data;
                self.findPrimaryImageByUri(0);
            }, self.callApiError);
        }
        /**
         * load Customer Friendly Description
         * @param data
         */
        self.loadCustomerFriendlyDescription = function (data) {
            angular.forEach(data, function (product) {
                if (product && product.relatedProduct && product.relatedProduct.productDescriptions) {
                    angular.forEach(product.relatedProduct.productDescriptions, function (desc) {
                        if (self.customerFriendlyDescription1 == desc.key.descriptionType) {
                            product.relatedProduct.customerFriendlyDescription1 = desc.description;
                        } else if (self.customerFriendlyDescription2 == desc.key.descriptionType) {
                            product.relatedProduct.customerFriendlyDescription2 = desc.description;
                        }
                    });
                    product.relatedProduct.productItemVariant = [];
                    product.relatedProduct.productItemVariantOrg = []
                    product.relatedProduct.checked = self.validateCheckedRelatedProduct(product.relatedProduct);
                    product.relatedProduct.checkedOrg = angular.copy(product.relatedProduct.checked);

                    product.relatedProduct.productItemVariantOrg = angular.copy(product.relatedProduct.productItemVariant);
                    if (!product.relatedProduct.checked) {
                        self.checkAllValue = false;
                    }
                }
            });
        }
        self.validateCheckedRelatedProduct = function (product) {
            var rs = false;
            if (product.lstProductItemVariant != null) {
                angular.forEach(product.lstProductItemVariant, function (value) {
                    if (self.itemMaster !== undefined
                        && self.itemMaster !== null
                        && self.itemMaster.key.itemCode == value.key.itemId) {
                        product.productItemVariant = angular.copy(value);
                        product.productItemVariant.parentItemMaster = self.itemMaster;
                        rs = true;
                    }
                });
            }
            return rs;
        }
        /**
         * Show full primary image, and allow user download
         */
        self.showFullImage = function (img) {
            self.imageBytes = img;
            // self.imageFormat =''
            $('#imageModal').modal({backdrop: 'static', keyboard: true});
            if (self.imageBytes == null) {
                self.imageBytes = ' ';
                $('#imageModal').modal("hide");
            }
        };
        self.addVariants = function () {
            self.success = '';
            self.error = '';
            self.getSizeInformation();
            self.loadProductDescription();
            self.getDisplayNameInformation();
            self.getBrandInformation();
            $('#addVariants').modal({backdrop: 'static', keyboard: true});
            self.isShowAddForm = true;
        };
        /**
         * watcher scope event for change associated table selected
         */
        $scope.$on('closeAddNewPopup', function (event, mess) {
            self.isShowAddForm = false;
            $('#addVariants').modal("hide");
            if (mess !== undefined && mess != null) {
                self.success = mess;
                self.resetData(true);
            }
        });
        /**
         * find primary image
         * @param index
         */
        self.findPrimaryImageByUri = function (index) {
            if (self.lstPrimaryImages !== null && self.lstPrimaryImages.length > index) {
                variantsApi.getImages({
                        imageUri: self.lstPrimaryImages[index].imageURI
                    },
                    function (results) {
                        var upcs = [];

                        angular.forEach(self.lstPrimaryImages, function (primary) {
                            if (primary.imageURI === results.message) {
                                upcs.push(primary.key.id);
                            }
                        });
                        angular.forEach(self.variantData, function (product) {
                            if (upcs.indexOf(product.relatedProduct.productPrimaryScanCodeId) >= 0) {
                                product.relatedProduct.image = IMAGE_ENCODE + results.data;
                            }
                        });
                        if (self.onLive) {
                            self.findPrimaryImageByUri(index + 1);
                        }
                    },
                    self.callApiError
                );
            }
        };
        /**
         * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
         */
        this.$onDestroy = function () {
            self.onLive = false;
        };

        /**
         * go to search product with id product
         * @param productId
         */
        self.goToSearchProduct = function (productId) {
            self.success = '';
            self.error = '';
            //Backup searching criteria data for navigate to
            //Only one time set temporary value even though the Product Id link clicked many times
            if (productSearchService.getListOfProductsTempBackup().length == 0) {
                productSearchService.setListOfProductsTempBackup(productSearchService.getListOfProductsBackup());
            }
            productSearchService.setSearchSelectionBackup(self.productMaster.prodId);
            productSearchService.setSearchSelection(productId);
            productSearchService.setSearchType(productSearchService.BASIC_SEARCH);
            productSearchService.setSelectionType(productSearchService.PRODUCT_ID)
            if (self.tabSelected == 1) {
                productSearchService.setToggledTab(VARIANTS_SUB_TAB);
            } else {
                productSearchService.setToggledTab(VARIANTS_ITEM_SUB_TAB);
            }
            productSearchService.setListOfProductsBackup(self.listOfProducts);
            productSearchService.setListOfProducts(self.variantData);
            productSearchService.setFromPage(appConstants.HOME);
            productSearchService.setDisableReturnToList(false);
            $rootScope.$broadcast('startAutomaticSearch');
        }
        /**
         * check all function
         */
        self.checkAll = function () {
            self.checkAllValue = !self.checkAllValue;
            angular.forEach(self.variantData, function (value, key) {
                value.relatedProduct.checked = self.checkAllValue;
                value.relatedProduct.isChange = true;
                if (!self.checkAllValue && value.relatedProduct && value.relatedProduct.productItemVariant) {
                    value.relatedProduct.productItemVariant.delete = true;
                }

            });

        }
        /**
         * change VariantUnknownQuantity will reset quantity
         */
        self.changeVariantUnknownQuantity = function () {
            if (self.variantQuantityUnknown) {
                angular.forEach(self.variantData, function (value, key) {
                    if (value.relatedProduct && value.relatedProduct.productItemVariant) {
                        value.relatedProduct.productItemVariant.retailPackQuantity = null;
                    }
                });
                self.sumQuantity = 0;
            }

        }
        /**
         * event check variant item
         * @param variants
         */
        self.checkVariantItem = function (variants) {
            if (variants.relatedProduct.checked) {
                var item = angular.copy(self.productItemVariantTemp);
                item.key.itemId = self.itemMaster.key.itemCode;
                item.key.itemKeyTypeCode = self.itemMaster.key.itemType;
                item.key.productId = variants.key.relatedProductId;
                variants.relatedProduct.productItemVariant = item;
            } else {
                variants.relatedProduct.productItemVariant.delete = true;
                variants.relatedProduct.productItemVariant.retailPackQuantity = 0;
            }
            variants.relatedProduct.isChange = true;
            self.refreshCheckAllFlag();
            self.reCalculateQuantitySum();

        }

        /**
         * Refresh flag check all in header grid. This flag show to user know, all row in current page has been
         * selected.
         */
        self.refreshCheckAllFlag = function () {
            self.checkAllValue = true;
            if (self.variantData.length == 0) {
                self.checkAllValue = false;
            }
            else {
                for (var i = 0; i < self.variantData.length; i++) {
                    if (!self.variantData[i].relatedProduct.checked) {
                        self.checkAllValue = false;
                        break;
                    }
                }
            }
        };
        /**
         * save data
         */
        self.saveData = function () {
            self.success = '';
            self.error = '';
            if (self.checkChangeData() && self.checkValidQuantity()) {
                self.isWaiting = true;
                variantsApi.saveVariantItem(self.getListProductItemVariantSave(), function () {
                    console.log('save done!!');
                    self.success = 'Changes were successfully applied.';
                    self.isWaiting = false;
                    self.init();

                }, self.callApiError);
            }

        }
        /**
         * get list product item variant to save
         * @returns {Array}
         */
        self.getListProductItemVariantSave = function () {
            var lst = [];
            var code = self.variantQuantityUnknownCode;
            if (self.variantQuantityUnknown) {
                code = self.variantQuantityUndefinedCode;
            }
            self.itemMaster.variantCode = code;
            for (var i = 0; i < self.variantData.length; i++) {
                if (self.variantQuantityUnknown && self.variantData[i].relatedProduct.checked) {
                    self.variantData[i].relatedProduct.productItemVariant.retailPackQuantity = 0;
                    self.variantData[i].relatedProduct.isChange = true;
                }
                if (self.variantData[i].relatedProduct.isChange) {
                    self.variantData[i].relatedProduct.productItemVariant.parentItemMaster = self.itemMaster;
                    lst.push(self.variantData[i].relatedProduct.productItemVariant);
                }
            }
            return lst;
        }
        /**
         * check change data
         * @returns {boolean}
         */
        self.checkChangeData = function () {
            for (var i = 0; i < self.variantData.length; i++) {
                if (self.variantData[i].relatedProduct.isChange) {
                    return true;
                }
            }
            return self.checkChangeVariantUnknownQuantity();
        }
        /**
         * check change variant unknown quantity
         * @returns {boolean}
         */
        self.checkChangeVariantUnknownQuantity = function () {
            var code = '';
            if (self.variantQuantityUnknown) {
                code = self.variantQuantityUndefinedCode;
            } else {
                code = self.variantQuantityUnknownCode;
            }
            return (code !== self.itemMaster.variantCode);
        }
        /**
         * delete variant product
         */
        self.deleteVariantProduct = function () {
            var lstDetele = [];

            angular.forEach(self.variantData, function (value, key) {
                if (value.selectedItem) {
                    value.actionCode = 'D';
                    lstDetele.push(value);
                }
            });
            if (lstDetele.length == 0) {
                self.error = 'Please select at least one Variant Product to delete';
            } else {
                self.isWaiting = true;
                self.success = '';
                self.error = '';
                variantsApi.saveRelatedProducts(lstDetele, function () {
                    console.log('delete done!!')
                    self.success = 'Changes were successfully applied.';
                    self.isWaiting = false;
                    self.resetData(true);

                }, self.callApiError);
            }

        };

        /**
         * Assign Image for Variant Products.
         */
        self.isCheckAllImage = false;
        self.styleDisplay2Modal = {'z-index': 1050};
        const IMAGE_ORDER_BY = '-createdDate';

        self.getAllImagesOfProduct = function () {
            self.cacheImages = new Map();
            imageInfoApi.getImageInformation(self.getUpcsOfProduct(), self.loadImageOfProduct, self.callApiError);
        };

        /**
         * After updating the database get the latest ItemMaster
         * @param results
         */
        self.loadImageOfProduct = function (results) {
            self.countImage = results.data.length;
            self.imagesInfo = angular.copy(results.data);
            self.imagesInfo = $filter('orderBy')(self.imagesInfo, IMAGE_ORDER_BY);
            self.addEmptyDataRow(results.data.length);
            self.findAllImageData();
        };

        self.getUpcsOfProduct = function () {
            var upcs = [];
            for (var i = 0, length = self.productMaster.sellingUnits.length; i < length; i++) {
                upcs.push(self.productMaster.sellingUnits[i].upc)
            }
            return upcs;
        };

        /**
         * call api to get image.
         */
        self.findAllImageData = function () {
            angular.forEach(self.imagesInfo, function (image) {
                if (!image.isEmptyRow && !self.cacheImages.has(image.imageURI)) {
                    imageInfoApi.getImages(image.imageURI,
                        function (results) {
                            if (results.data !== null && results.data !== '') {
                                self.cacheImages.set(image.imageURI, IMAGE_ENCODE + results.data);
                            } else {
                                self.cacheImages.set(image.imageURI, self.IMAGE_NOT_FOUND);
                            }
                        }
                    );
                }
            });
        };

        /**
         * Add empty row to view.
         * @param lengthOfData total image returned by api.
         */
        self.addEmptyDataRow = function (lengthOfData) {
            if (lengthOfData < 3) {
                var countEmptyRow = 3 - lengthOfData;
                for (var i = 0; i < countEmptyRow; i++) {
                    var emptyRow = {
                        'id': i,
                        'isEmptyRow': true,
                    };
                    self.imagesInfo.push(emptyRow);
                }
            }
        };

        /**
         * Build ng table for show image on view.
         */
        self.buildAssignImageTable = function () {
            self.assignImageTable = new ngTableParams({
                page: 1,
                count: 5
            }, {
                counts: [],
                data: self.imagesInfo
            });
        };

        /**
         * get data of image by uri text.
         *
         * @param uriText of image.
         * @returns {*} data of image. If image not found return empty string.
         */
        self.getImageByImageURI = function (uriText) {
            if (self.cacheImages.has(uriText)) {
                return self.cacheImages.get(uriText);
            } else {
                return '';
            }
        };

        self.showPopupAssignImage = function (productId, productPrimaryScanCodeId) {
            self.errorAssignImage = '';
            self.success = '';
            self.isCheckAllImage = false;
            self.selectedVariantProductId = productId;
            self.variantScanCodeIds = [];
            self.variantScanCodeIds.push(productPrimaryScanCodeId);
            angular.forEach(self.imagesInfo, function (image) {
                image.isChecked = false;
            });
            self.buildAssignImageTable();
            self.styleDisplay2Modal = {'z-index': 1050};
            $('#assignImageModal').modal({backdrop: 'static', keyboard: true});
        };

        /**
         * Handle assign image.
         */
        self.saveAssignImage = function () {
            self.isWaiting = true;
            self.success = '';
            $('#assignImageModal').modal("hide");
            self.errorAssignImage = '';
            self.assignImagesHandle = [];
            angular.forEach(self.imagesInfo, function (image) {
                if (!image.isEmptyRow && image.isChecked) {
                    var imageHandle = angular.copy(image);
                    self.assignImagesHandle.push(imageHandle);
                }
            });
            angular.forEach(self.assignImagesHandle, function (image) {
                delete image.isChecked;
                delete image.dimensions;
                image.createdDate = null;
                image.lastModifiedOn = null;
                image.revisedTimeStamp = null;
            });

            var data = self.createDataForRequestApi();
            variantsApi.assignImages(data, self.assignImageSuccess, self.handleError);
        };

        /**
         * Handle when call api assign image successfully.
         *
         * @param results
         */
        self.assignImageSuccess = function (results) {
            self.isWaiting = false;
            var errors = [];
            if (results.data) {
                self.styleDisplay2Modal = {'z-index': 1050};
                $('#assignImageModal').modal({backdrop: 'static', keyboard: true});
                self.assignImageResults = results.data;
                angular.forEach(self.assignImageResults, function (result) {
                    var error = {
                        'index': self.getImageIndexByImageUri(result.uri),
                        'productId': self.getVariantProductIdByScanCodeId(result.upc)
                    };
                    errors.push(error);
                });
                errors = $filter('orderBy')(errors, 'productId');
                angular.forEach(errors, function (error) {
                    self.errorAssignImage += self.IMG_ALREADY_ASSIGNED_MSG.replace('#index', error.index).replace('#productId', error.productId);
                });
            } else {
                self.loadPrimaryImage(self.variantData);
                self.success = self.ASSIGN_IMAGE_SUCCESS_MSG;
            }
        };

        /**
         * Show confirmation popup when click Assign All Variants.
         */
        self.assignAllImage = function () {
            $('#confirm-assign-all').modal("hide");
            self.errorAssignImage = '';
            self.variantScanCodeIds = [];
            angular.forEach(self.variantData, function (variant) {
                self.variantScanCodeIds.push(variant.relatedProduct.productPrimaryScanCodeId)
            });
            self.saveAssignImage();
        };

        /**
         * Create data for request api assign image.
         *
         * @returns {{}}
         */
        self.createDataForRequestApi = function () {
            var returnValue = {};
            returnValue.images = self.assignImagesHandle;
            returnValue.upcs = self.variantScanCodeIds;
            return returnValue;
        };

        /**
         * Get product id by scan code id.
         *
         * @param scanCodeId
         * @returns {*}
         */
        self.getVariantProductIdByScanCodeId = function (scanCodeId) {
            for (var i = 0; i < self.variantData.length; i++) {
                if (self.variantData[i].relatedProduct.productPrimaryScanCodeId === scanCodeId) {
                    return self.variantData[i].key.relatedProductId;
                }
            }
        };

        /**
         * Find index of image in list of images by image uri.
         *
         * @param imageUri Uri text
         * @returns {number} index of image in list.
         */
        self.getImageIndexByImageUri = function (imageUri) {
            for (var i = 0; i < self.imagesInfo.length; i++) {
                if (self.imagesInfo[i].imageURI === imageUri) {
                    return i + 1;
                }
            }
        };

        /**
         * Check if any image on Assign Image Popup has selected.
         *
         * @returns {boolean}
         */
        self.hasSelectedAssignImage = function () {
            for (var i = 0; i < self.imagesInfo.length; i++) {
                if (!self.imagesInfo[i].isEmptyRow && self.imagesInfo[i].isChecked) {
                    return true;
                }
            }
            return false;
        };

        /**
         * Handle event click check/uncheck a image on Assign Image Popup.
         *
         * @param image object
         */
        self.handleCheckImage = function (image) {
            image.isChecked = !image.isChecked;
            var countChecked = 0;
            angular.forEach(self.imagesInfo, function (image) {
                if (!image.isEmptyRow && image.isChecked) {
                    countChecked++;
                }
            });
            if (countChecked === self.countImage) {
                self.isCheckAllImage = true;
            } else {
                self.isCheckAllImage = false;
            }
        };

        /**
         * Handle event click check/uncheck all image on Assign Image Popup.
         */
        self.handleCheckAllImage = function () {
            self.isCheckAllImage = !self.isCheckAllImage;
            angular.forEach(self.imagesInfo, function (image) {
                if (!image.isEmptyRow) {
                    image.isChecked = self.isCheckAllImage;
                }
            });
        };

        /**
         * Handle button Assign Image(s) for all Variants Product.
         */
        self.handleAssignAll = function () {
            self.styleDisplay2Modal = {'z-index': 1040};
            $('#confirm-assign-all').modal({backdrop: 'static', keyboard: true});
        }
        /**
         * check all function
         */
        self.checkAllVariantAction = function () {
            angular.forEach(self.variantData, function (value, key) {
                value.selectedItem = self.checkAllVariant;
            });
        }
        /**
         * vlaidate check all function
         */
        self.checkAllVariantValidation = function () {
            self.checkAllVariant = true;
            angular.forEach(self.variantData, function (value, key) {
                if (!value.selectedItem) {
                    self.checkAllVariant = false;
                }
            });
        }
        /**
         * handle when click on return to list button
         */
        self.returnToList = function () {
            $rootScope.$broadcast('returnToListEvent');
        };

        /**
         * Determines which tab you are currently on.
         * @param tab
         */
        self.chooseTab = function (tab) {
            //Set the tab and panel of tab will be active
            switch (tab) {
                case VARIANTS:
                    //The variants tab and variants panel will be active
                    self.activeVariantsTabClass = 'ng-scope active';
                    self.variantsTabPaneClass = 'tab-pane active';
                    self.variantsItemTabPaneClass = 'tab-pane';
                    self.activeVariantsItemTabClass = '';
                    self.tabSelected = 1;
                    break;
                case VARIANTS_ITEM:
                    //The variants item tab and variants item panel will be active
                    self.activeVariantsItemTabClass = 'ng-scope active';
                    self.variantsItemTabPaneClass = 'tab-pane active';
                    self.variantsTabPaneClass = 'tab-pane';
                    self.activeVariantsTabClass = '';
                    self.tabSelected = 2;
                    break;
                default:
            }
        };
        /**
         * Download image.
         */
        self.downloadImage = function () {
            if (self.imageBytes != null) {
                downloadImageService.download(self.imageBytes, 'jpg');
            }
        }

        /**
         * Callback for when the backend returns an error.
         *
         * @param error The error from the back end.
         */
        self.handleError = function (error) {
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

        /**
         * Get brand information.
         */
        self.getBrandInformation = function () {
            eCommerceViewApi.getBrandInformation(
                {
                    productId: self.productMaster.prodId,
                    upc:  self.productMaster.productPrimaryScanCodeId,
                    saleChannel:  SALE_CHANNEL_HEB_COM
                },
                function (result) {
                    if(result != null){
                        self.brand = angular.copy(result.brand.content);
                    }

                }, self.callApiError
            );
        };
        /**
         * Get display name information.
         */
        self.getDisplayNameInformation = function () {
            eCommerceViewApi.getDisplayNameInformation(
                {
                    productId: self.productMaster.prodId,
                    upc:  self.productMaster.productPrimaryScanCodeId,
                    saleChannel:  SALE_CHANNEL_HEB_COM
                },
                function (result) {
                    if(result != null){
                        self.displayName = angular.copy(result.displayName.content);
                    }
                }, self.callApiError
            );
        };

        /**
         * Get product description.
         */
        self.loadProductDescription = function () {
            eCommerceViewApi.findProductDescription(
                {
                    productId: self.productMaster.prodId,
                    upc:  self.productMaster.productPrimaryScanCodeId,
                },
                //success case
                function (result) {
                    if(result != null && result.content != null) {
                        self.romanceCopy = he.decode(result.content);
                    }
                }, self.callApiError);
        };
        /**
         * Get size information.
         */
        self.getSizeInformation = function () {
                eCommerceViewApi.getSizeInformation(
                    {
                        productId: self.productMaster.prodId,
                        upc:  self.productMaster.productPrimaryScanCodeId,
                        saleChannel:  SALE_CHANNEL_HEB_COM
                    },
                     function (result) {
                         if(result != null){
                             self.size = angular.copy(result.size.content);
                         }
                }, self.callApiError
            );
        };
    }

})();