/*
 *   favorItemDescriptionComponent.js
 *
 *   Copyright (c) 2019 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';
/**
 * Component to support the page that allows users to show product description.
 *
 * @author vn70529
 * @since 2.31.0
 */
(function () {

    angular.module('productMaintenanceUiApp').component('favorItemDescriptionComponent', {
        bindings: {
            currentTab: '<',
            productMaster: '<',
            eCommerceViewDetails: '=',
            changeSpellCheckStatus: '&',
            isEditText: '=',
            callbackfunctionAfterSpellCheck: '='
        },
        require: {
            parent: '^^eCommerceView'
        },
        // Inline template which is binded to message variable in the component controller
        templateUrl: 'src/productDetails/product/eCommerceView/favorItemDescription.html',
        // The controller that handles our component logic
        controller: favorItemDescriptionController
    });
    favorItemDescriptionController.$inject = ['$timeout', 'ECommerceViewApi', '$scope', '$sce', 'VocabularyService', 'VocabularyApi', 'ngTableParams'];

    function favorItemDescriptionController($timeout, eCommerceViewApi, $scope, $sce, vocabularyService, vocabularyApi, ngTableParams) {
        var self = this;
        /**
         * The default error message.
         *
         * @type {string}
         */
        self.UNKNOWN_ERROR = "An unknown error occurred.";
        /**
         * Favor item attribute attribute id.
         *
         * @type {number}
         */
        const FAVOR_ITEM_DESCRIPTION_LOG_ATTR_ID = 3989;
        /**
         * Edit favor item description physical attribute id.
         *
         * @type {number}
         */
        const EDIT_FAVOR_ITEM_DESCRIPTION_PHY_ATTR_ID = 3989;
        /**
         * System source id for edit mode.
         *
         * @type {number}
         */
        const SRC_SYS_ID_EDIT = 4;

        /**
         * Reload eCommerce view key.
         * @type {string}
         */
        const RELOAD_ECOMMERCE_VIEW = 'reloadECommerceView';
        /**
         * Reset eCommerce view.
         * @type {string}
         */
        const RESET_ECOMMERCE_VIEW = 'resetECommerceView';
        /**
         * Reload after save popup.
         * @type {string}
         */
        const RELOAD_AFTER_SAVE_POPUP = 'reloadAfterSavePopup';
        /**
         * Validate warning eCommerce view key.
         * @type {string}
         */
        const VALIDATE_WARNING = 'validateWarning';
        /**
         * Holds the Editable status for favor item description.
         *
         * @type {boolean}
         */
        self.editableFavorItemDescription = false;

        /**
         * Define trust as HTML copy from $sce to use in ui.
         */
        self.trustAsHtml = $sce.trustAsHtml;
        /**
         * Flag to show type of format favor item description field.
         * @type {boolean}
         */
        self.isShowingHtml = false;
        /**
         * HTML Tab Pressing flag.
         * @type {boolean}
         */
        self.isPressingHtmlTab = false;
        /**
         * Constant order by asc.
         *
         * @type {String}
         */
        const ORDER_BY_ASC = "asc";
        /**
         * Constant order by desc.
         *
         * @type {String}
         */
        const ORDER_BY_DESC = "desc";
        /**
         * Component will reload the data whenever the item is changed in.
         */
        this.$onChanges = function () {
            self.isWaitingForResponse = true;
            self.loadFavorItemDescription();
        };
        /**
         * Destroy component.
         */
        this.$onDestroy = function () {
            self.parent = null;
        };

        /**
         * Get product description.
         */
        self.loadFavorItemDescription = function () {
            self.editableFavorItemDescription = false;
            self.editableFavorItemDescriptionOrg = false;
            eCommerceViewApi.findFavorItemDescription(
                {
                    productId: self.productMaster.prodId,
                    upc: self.productMaster.productPrimaryScanCodeId,
                },
                //success case
                function (results) {
                    if(results.content === null){
                        results.content = "";
                    }
                    self.eCommerceViewDetails.favorItemDescription = results;
                    self.eCommerceViewDetails.favorItemDescriptionOrg = angular.copy(self.eCommerceViewDetails.favorItemDescription);
                    if(self.eCommerceViewDetails.favorItemDescription != null) {
                        self.editableFavorItemDescription = self.isEditMode();
                        self.editableFavorItemDescriptionOrg = angular.copy(self.editableFavorItemDescription);
                        //validate spell check of favor Item Description.
                        self.validateFavorItemDescriptionTextForView();
                    }
                    self.isWaitingForResponse = false;
                }, self.handleError);
        };

        /**
         * Callback for when the backend returns an error.
         *
         * @param error The error from the back end.
         */
        self.handleError = function (error) {
            self.isWaitingForResponse = false;
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
                }
                return error.data.error;
            }
            return self.UNKNOWN_ERROR;
        };
        /**
         * Check edit status or not.
         *
         * @returns {boolean}
         */
        self.isEditMode = function () {
            return (self.eCommerceViewDetails.favorItemDescription.sourceSystemId === SRC_SYS_ID_EDIT &&
                self.eCommerceViewDetails.favorItemDescription.physicalAttributeId === EDIT_FAVOR_ITEM_DESCRIPTION_PHY_ATTR_ID);

        };
        /**
         * Show edit mode of favor item description.
         */
        self.showEditText = function () {
            if (!self.editableFavorItemDescription) {
                self.editableFavorItemDescription = true;
                vocabularyService.createSuggestionMenuContext($scope, $scope.suggestionsOrg);
            }
        };
        /**
         * Show popup to edit description source.
         */
        self.showDescriptionEditSource = function () {
            self.isEditText = self.editableFavorItemDescription;
            self.parent.getECommerceViewDataSource(self.productMaster.prodId, self.productMaster.productPrimaryScanCodeId, FAVOR_ITEM_DESCRIPTION_LOG_ATTR_ID, 0, 'Favor Item Description', null);
        };

        /**
         * Disable validate spell check of Romance Copy when open suggestions popup.
         */
        $scope.$on('context-menu-opened', function (event, args) {
            self.openSuggestionsPopup = true;
        });
        /**
         * Enable validate spell check of Romance Copy when close suggestions popup.
         */
        $scope.$on('context-menu-closed', function (event, args) {
            self.openSuggestionsPopup = false;
        });
        /**
         * Validate text for view mode. It will call after loaded favor item description.
         */
        self.validateFavorItemDescriptionTextForView = function () {
            if (!self.isEmpty(self.eCommerceViewDetails.favorItemDescription.content)) {
                self.isWaitingForCheckSpellingResponse = true;
                vocabularyService.validateRomancyTextForView(self.eCommerceViewDetails.favorItemDescription.content, function (newText, suggestions) {
                    if (self.editableFavorItemDescription) {//load data of edit mode
                        // Use $timeout and space char to clear cache on layout.
                        self.eCommerceViewDetails.favorItemDescription.content = self.eCommerceViewDetails.favorItemDescription.content + ' ';
                        $timeout(function () {
                            self.eCommerceViewDetails.favorItemDescription.content = angular.copy(newText);//color text
                            self.favorItemDescriptionContent = angular.copy(newText);
                            self.favorItemDescriptionContentOrg = angular.copy(newText);
                            $scope.suggestionsOrg = angular.copy(suggestions);
                            $timeout(function () {
                                vocabularyService.createSuggestionMenuContext($scope, suggestions);
                                self.isWaitingForCheckSpellingResponse = false;
                            }, 1000);
                        }, 100);
                    } else {//load data of view mode
                        self.favorItemDescriptionContent = angular.copy(newText);
                        self.favorItemDescriptionContentOrg = angular.copy(newText);
                        self.eCommerceViewDetails.favorItemDescription.content = angular.copy(newText);//color text
                        $scope.suggestionsOrg = angular.copy(suggestions);
                        self.isWaitingForCheckSpellingResponse = false;
                    }
                });
            } else {
                self.favorItemDescriptionContent = '';
                self.favorItemDescriptionContentOrg = '';
            }
        };
        /**
         * Validate text for edit mode of favor item description.
         */
        self.validateFavorItemDescriptionTextForEdit = function () {
            if (self.isPressingHtmlTab) {
                self.isPressingHtmlTab = false;
                return;
            }
            if (!self.openSuggestionsPopup) {//avoid conflict with suggestion popup
                if (!self.isEmpty(self.eCommerceViewDetails.favorItemDescription.content)) {
                    var callbackFunction = self.getAfterSpellCheckCallback();
                    self.isWaitingForCheckSpellingResponse = true;
                    self.changeSpellCheckStatus({status: true});
                    var contentHtml = $('#favorItemDescriptionDiv').html();
                    contentHtml = contentHtml.split("<br>").join("<div>");
                    contentHtml = contentHtml.split("</p>").join("<div>");
                    var contentHtmlArray = contentHtml.split("<div>");
                    var contentNoneHtml = '';
                    if (contentHtmlArray.length > 1) {
                        for (var i = 0; i < contentHtmlArray.length; i++) {
                            if (contentHtmlArray[i] != null && contentHtmlArray[i] != '') {
                                contentNoneHtml = contentNoneHtml.concat(self.convertHtmlToPlaintext(contentHtmlArray[i]));
                                if (i < (contentHtmlArray.length - 1)) {
                                    contentNoneHtml = contentNoneHtml.concat(" <br> ");
                                }
                            }
                        }
                        self.eCommerceViewDetails.favorItemDescription.content = contentNoneHtml.split("&nbsp;").join(" ");
                    }
                    var response = vocabularyApi.validateRomanceCopySpellCheck({textToValidate: self.eCommerceViewDetails.favorItemDescription.content});
                    response.$promise.then(
                        function (result) {
                            // Use $timeout and space char to clear cache on layout.
                            self.eCommerceViewDetails.favorItemDescription.content = self.eCommerceViewDetails.favorItemDescription.content + ' ';
                            $timeout(function () {
                                self.eCommerceViewDetails.favorItemDescription.content = angular.copy(result.validatedText);//normal text
                                vocabularyService.validateRomancyTextForView(self.eCommerceViewDetails.favorItemDescription.content, function (newText, suggestions) {
                                    self.favorItemDescriptionContent = angular.copy(newText);
                                    self.eCommerceViewDetails.favorItemDescription.content = angular.copy(newText);//color text
                                    $scope.suggestionsOrg = angular.copy(suggestions);
                                    $timeout(function () {
                                        vocabularyService.createSuggestionMenuContext($scope, suggestions);
                                    }, 1000);
                                    self.isWaitingForCheckSpellingResponse = false;
                                    self.changeSpellCheckStatus({status: false});
                                    $timeout(function () {
                                        // Save or publish if event occurs from save or publish button.
                                        self.processAfterSpellCheck(callbackFunction);
                                    }, 500);
                                });
                            }, 100);
                        }, function (error) {
                        }
                    );
                }
            }
        };
        /**
         * Check empty value.
         *
         * @param val
         * @returns {boolean}
         */
        self.isEmpty = function (val) {
            if (val == null || val == undefined || val.trim().length == 0) {
                return true;
            }
            return false;
        };
        /**
         * Reload ECommerceView.
         */
        $scope.$on(RELOAD_ECOMMERCE_VIEW, function () {
            self.$onChanges();
        });
        /**
         * Reset eCommerce view.
         */
        $scope.$on(RESET_ECOMMERCE_VIEW, function () {
            self.isShowingHtml = false;
            self.editableFavorItemDescription = angular.copy(self.editableFavorItemDescriptionOrg);
            self.favorItemDescriptionContent = angular.copy(self.favorItemDescriptionContentOrg);
            self.eCommerceViewDetails.favorItemDescription.content = angular.copy(self.favorItemDescriptionContentOrg);
            if (self.editableFavorItemDescription) {//edit mode
                $timeout(function () {
                    vocabularyService.createSuggestionMenuContext($scope, $scope.suggestionsOrg);
                }, 1000);
            }
        });
        /**
         * Reload after save popup.
         */
        $scope.$on(RELOAD_AFTER_SAVE_POPUP, function (event, attributeId, status) {
            if (attributeId === FAVOR_ITEM_DESCRIPTION_LOG_ATTR_ID) {
                self.editableFavorItemDescription = false;
                if (status === true) {
                    self.$onChanges();
                } else {
                    self.isWaitingForResponse = true;
                }
            }
        });
        /**
         * Init data eCommerce View Audit.
         */
        self.initECommerceViewAuditTable = function () {
            $scope.filter = {
                attributeName: '',
            };
            $scope.sorting = {
                changedOn: ORDER_BY_DESC
            };
            $scope.eCommerceViewAuditTableParams = new ngTableParams({
                page: 1,
                count: 10,
                filter: $scope.filter,
                sorting: $scope.sorting
            }, {
                counts: [],
                data: self.eCommerceViewAudits
            });
        };
        /**
         * Change sort orientation.
         */
        self.changeSortOrientation = function () {
            if ($scope.sorting.changedOn === ORDER_BY_DESC) {
                $scope.sorting.changedOn = ORDER_BY_ASC;
            } else {
                $scope.sorting.changedOn = ORDER_BY_DESC;
            }
        };

        /**
         * Remove all tag html in text
         * @param text the text to convert.
         * @returns the string after converted.
         */
        self.convertHtmlToPlaintext = function (text) {
            return text ? String(text).replace(/<[^>]+>/gm, '') : '';
        };
        /**
         * Returns the function to do (save or publish) after spell check success.
         * @return callback function
         */
        self.getAfterSpellCheckCallback = function () {
            var callbackFunction = self.callbackfunctionAfterSpellCheck;
            self.callbackfunctionAfterSpellCheck = null;
            return callbackFunction;
        };
        /**
         * Call the function for save or publish after spell check is success.
         * @param callback
         */
        self.processAfterSpellCheck = function (callback) {
            if (callback != null && callback !== undefined) {
                callback();
            }
        };

        /**
         * Set type of text for favor item description field
         * @param isShowingHtml true show it or hide it.
         */
        self.showFormatDescriptionField = function (isShowingHtml) {
            self.isShowingHtml = isShowingHtml;
            self.eCommerceViewDetails.favorItemDescription.htmlSave = isShowingHtml;
            self.isPressingHtmlTab = false;
        };

        /**
         * Prepare callback function for  after spell check is success.
         */
        self.onHtmlTabClicked = function () {
            self.isPressingHtmlTab = true;
        };
    }
})();
