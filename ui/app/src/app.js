'use strict';

/**
 * @ngdoc overview
 * @name productMaintenanceUiApp
 * @description
 * # batchMonitorUiApp
 *
 * Main module of the application.
 */
(function () {
    var app = angular
        .module('productMaintenanceUiApp', [
            'ngCookies',
            'ngResource',
            'ui.router',
            'angularUtils.directives.dirPagination',
            'ngTable',
            'selectize',
            'ngIdle',
            'ngAnimate',
            'ui.bootstrap',
            'ui.select',
            'ngSanitize',
            'ngCsv',
            'ui.bootstrap.contextMenu',
            'treeGrid',
            'angularjs-dropdown-multiselect'
        ]);
    angular.module('productMaintenanceUiApp')
        .controller('appController', ApplicationController)
        .filter('secondsToDateTime', secondsToDateTime);
    /**
     * AngularJs's default Number formatter function "$filter('number')(number, fractionSize)" returns data with comma
     * (thousand seperator). Due to which this default function cannot be used to display productId or upc etc. Hence
     * this new filter is created and used to convert prod-id/upc like Strings with leading zeros to simple numbers.
     */
    angular.module('productMaintenanceUiApp')
        .controller('appController', ApplicationController)
        .filter('Integer', parseInteger);

    ApplicationController.$inject= ['$scope', '$rootScope', '$uibModal','$state', '$window', 'Idle', 'StatusApi', 'appVersion'];

    function ApplicationController($scope, $rootScope, $uibModal,$state, $window, Idle, statusApi, appVersion) {

        $scope.getHeight = getHeight;
        $scope.convertDate = convertDate;
        $rootScope.convertDateWithFullYear = convertDateWithFullYear;
        $scope.convertDateWithSlash = convertDateWithSlash;
        $rootScope.convertStringToDateWithSlashFormat = convertStringToDateWithSlashFormat;
        $scope.dateToJson = dateToJson;
        $scope.onBrowserResize = onBrowserResize;
        $scope.isEmptyString = isEmptyString;
        $rootScope.appVersion = appVersion;
        /**
         * The below scope variables are use for the confirmation modal.
         *  waitingState - holds the state the user is trying to go to
         *  waitingParams - holds any extra params for the state in waiting state
         *  stateChangeConfirmed - flag set to confirm the approval of a state change
         *  contentChangedFlag - global flag that is used by all components utilizing the modal to signify an element has changed
         */
        $scope.waitingState = null;
        $scope.waitingParams = null;
        $scope.stateChangeConfirmed = false;
        $rootScope.contentChangedFlag = false;
        $rootScope.logOutClicked = false;




        /**
         * This watches for any state change, determines if the user is leaving the home screen, opens a confirmation modal,
         * and checks if the state change has been confirmed.
         */
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            if((fromState.name === "home" || fromState.name === "customHierarchyAdmin" || fromState.name === "productGroup")
                && !$scope.stateChangeConfirmed && $rootScope.contentChangedFlag){
                event.preventDefault();
                $scope.waitingState = toState;
                $scope.waitingParams = toParams;
                var result = document.getElementById("confirmationModal");
                var wrappedResult = angular.element(result);
                wrappedResult.modal("show");
            }
            else{
                //Resets flag
                $scope.stateChangeConfirmed = false;
                $rootScope.contentChangedFlag = false;
            }

        });

        /**
         * Handles the results of the modal to either confirm or deny a state change
         */
        $rootScope.$on('modalResults', function(event, args){
            if($scope.waitingState !== null && args.result){
                $scope.stateChangeConfirmed = true;
                $rootScope.contentChangedFlag = false;
                $state.go($scope.waitingState, $scope.waitingParams);
                $state.go($scope.waitingState, $scope.waitingParams, {reload: true});
                $scope.waitingState = null;
                $scope.waitingParams = null;
            }else {
                $scope.waitingState = null;
                $scope.waitingParams = null;
            }
        });

        // this function allows for setting a height of an element, taking footer, navbar, other padding into account
        function getHeight(percent, padding) {
            var navbarHeight = 121;
            var footerHeight = 22;
            var bottomPadding = 20;
            var innerPort = navbarHeight + footerHeight + bottomPadding;
            var pixels = ($window.innerHeight - innerPort) * (percent/100);
            if (padding) {
                pixels -= padding;
            }
            return pixels + 'px';
        }

        /**
         * Converts a date to a readable format (2014-03-16)
         * @param date Date to be formatted.
         * @returns {string}
         */
        function convertDate(date) {
            if(date != null) {
                var d = new Date(date);
                var month = '' + (d.getMonth() + 1);
                var day = '' + d.getDate();
                var year = d.getFullYear();

                // If the month is 1-9, format has to be 0#.
                if (month.length < 2) {
                    month = '0' + month;
                }

                // If the date is 1-9, format has to be 0#.
                if (day.length < 2) {
                    day = '0' + day;
                }

                return [year, month, day].join('-');
            } else {
                return null;
            }
        };

        /**
         * Converts a date to a readable format (2014-03-16)
         * @param date Date to be formatted.
         * @returns {string}
         */
        function convertDateWithFullYear(date) {
            if(date != null) {
                var d;
                if (typeof date === 'string') {
                    d = convertStringToDateWithSlashFormat(date);
                }else {
                    d = new Date(date);
                }
                var month = '' + (d.getMonth() + 1);
                var day = '' + d.getDate();
                var year = d.getFullYear();
                if(year < 10){
                    year = '000' + year;
                }
                else if(year < 100){
                    year = '00' + year;
                }else if(year < 1000){
                    year = '0' + year;
                }

                // If the month is 1-9, format has to be 0#.
                if (month.length < 2) {
                    month = '0' + month;
                }

                // If the date is 1-9, format has to be 0#.
                if (day.length < 2) {
                    day = '0' + day;
                }

                return [year, month, day].join('-');
            } else {
                return null;
            }
        };

        /**
         * Convert string date to date.
         * @param stringDate the date type of string.
         * @returns {Date}
         */
        function convertStringToDateWithSlashFormat(stringDate) {
            if (stringDate != undefined && stringDate != null && typeof stringDate === 'string') {
                return new Date(stringDate.replace(/-/g, '\/'));
            }
            return null;
        }

        /**
         * Converts a date to a readable format (2014-03-16)
         * @param date Date to be formatted.
         * @returns {string}
         */
        function convertDateWithSlash(date) {
            if(date != null) {
                var d = new Date(date);
                var month = '' + (d.getMonth() + 1);
                var day = '' + d.getDate();
                var year = d.getFullYear();

                // If the month is 1-9, format has to be 0#.
                if (month.length < 2) {
                    month = '0' + month;
                }

                // If the date is 1-9, format has to be 0#.
                if (day.length < 2) {
                    day = '0' + day;
                }

                return [month, day, year].join('/');
            } else {
                return null;
            }
        }

        /**
         * Converts a date to a format we can send through a JSON message.
         *
         * @param date The date to convert.
         * @returns {*} The date in JSON format.
         */
        function dateToJson(date) {
            if(date != null) {
                var d = new Date(date);
                var month = '' + (d.getMonth() + 1);
                var day = '' + d.getDate();
                var year = d.getFullYear();

                // If the month is 1-9, format has to be 0#.
                if (month.length < 2) {
                    month = '0' + month;
                }

                // If the date is 1-9, format has to be 0#.
                if (day.length < 2) {
                    day = '0' + day;
                }

                return [year, month, day].join('-');
            } else {
                return null;
            }
        }

        /**
         * ng-idle functions for Keepalive and Idle
         *
         */

        $scope.started = false;

        function closeModals() {
            if ($scope.warning) {
                $scope.warning.close();
                $scope.warning = null;
            }

            if ($scope.timedout) {
                $scope.timedout.close();
                $scope.timedout = null;
            }
        }

        $scope.$on('IdleStart', function() {
            //If open modal, close with inactivity
            $(".modal").modal('hide');

            closeModals();

            $scope.warning = $uibModal.open({
                templateUrl: 'warningModal.html',
                controller: 'WarningModalController',
                controllerAs: 'WarningModalController',
                windowClass: 'modal-danger',
                backdrop: 'static'
            });
        });

        $scope.$on('IdleEnd', function() {
            Idle.watch();
            closeModals();
        });

        $scope.$on('IdleTimeout', function() {
            closeModals();

            $scope.stateChangeConfirmed = true;
            $rootScope.$broadcast('LogoutEvent', true);
        });

        $scope.start = function() {
            closeModals();
            Idle.watch();
            $scope.started = true;
        };

        $scope.stop = function() {
            closeModals();
            Idle.unwatch();
            $scope.started = false;

        };

        // when the browser is resized, call the specified callback function
        function onBrowserResize(callback) {
            $window.onresize = function(event) {
                callback();
            };
        }


        /**
         * Return whether a passed in string is null or empty.
         *
         * @param string String to check if null or empty
         */
        function isEmptyString(string){
            return (typeof string === 'undefined' || string === null || string.length === 0);
        }
    }

    function secondsToDateTime() {
        return function(seconds) {
            return new Date(1970, 0, 1).setSeconds(seconds);
        };
    }

    /**
     * Used to convert numbers of type string to type number. Removes leading zeros if any.
     * @returns {Function} a number.
     */
    function parseInteger() {
        return function(val) {
            return parseInt(val,10); // 10 represents the radix (base - decimal).
        };
    }

    var constList = {
        PRODUCT_DISCONTINUE_STATE: 'productDiscontinue',
        EXECUTION_STATE: 'execution',
        EXECUTION_DETAIL_STATE: 'executionDetail',
        LOGIN_STATE: 'login',
        HOME_STATE: 'home',
        DISCONTINUE_ADMIN_RULES: 'discontinueAdminRules',
        DISCONTINUE_EXCEPTION_RULES: 'discontinueExceptionRules',
        PRODUCTION_SUPPORT_STATE: 'productionSupport',
        UPC_SWAP_STATE: 'upcSwap',
        WAREHOUSE_TO_WAREHOUSE_STATE: 'warehouseToWarehouse',
        WAREHOUSE_TO_WAREHOUSE_SWAP_STATE: 'warehouseToWarehouseSwap',
        DSD_TO_BOTH_STATE: 'dsdToBoth',
        ADD_ASSOCIATE_STATE: 'addAssociate',
        BOTH_TO_DSD: 'bothToDsd',
        SCALE_MANAGEMENT_UPC_MAINTENANCE: 'scaleManagementUpcMaintenance',
        SCALE_MANAGEMENT_INGREDIENTS: 'scaleManagementIngredients',
        SCALE_MANAGEMENT_INGREDIENT_STATEMENTS: 'scaleManagementIngredientStatements',
        SCALE_MANAGEMENT_NUTRIENTS: 'scaleManagementNutrients',
        SCALE_MANAGEMENT_NUTRIENT_STATEMENT: 'scaleManagementNutrientStatement',
        SCALE_MANAGEMENT_NLEA16_NUTRIENT_STATEMENT: 'scaleManagementNLEA16NutrientStatement',
        SCALE_MANAGEMENT_CODE_AUDIT: 'scaleManagementCodeAudit',
        SCALE_MANAGEMENT_LABEL_FORMATS: 'scaleManagementLabelFormats',
        SCALE_MANAGEMENT_ACTION_CODES: 'scaleManagementActionCodes',
        SCALE_MANAGEMENT_GRAPHIC_CODES: 'scaleManagementGraphicCodes',
        NUTRIENT_UOM_CODE: 'NutrientUomCode',
        SCALE_MANAGEMENT_INGREDIENT_CATEGORY_CODES: 'scaleManagementIngredientCategoryCodes',
        SCALE_MANAGEMENT_REPORTS: 'scaleManagementReports',
        REPORT_INGREDIENTS: 'reportIngredients',
        GDSN_VENDOR_SUBSCRIPTIONS: 'gdsnVendorSubscription',
        METADATA_ATTRIBUTES: 'metadataAttributes',
        PRODUCT_DETAILS: 'productDetails',
        PRODUCT_HIERARCHY_ADMIN: 'productHierarchyAdmin',
        PRODUCT_HIERARCHY_DEFAULTS: 'productHierarchyDefaults',
        MASTER_ATTRIBUTE_TAXONOMY_HIERARCHY: 'masterAttributeTaxonomyHierarchy',
        CUSTOM_HIERARCHY_ADMIN: 'customHierarchyAdmin',
        SOURCE_PRIORITY: 'sourcePriority',
        ATTRIBUTE_MAINTENANCE: 'attributeMaintenance',
        UTILITIES_DICTIONARY: 'dictionary',
        UTILITIES_CHECK_DIGIT: 'checkDigitCalculator',
        BATCH_UPLOAD_BY_CATEGORY_SPECIFIC_ATTRIBUTES: 'batchUploadByCategorySpecificAttributes',
        BATCH_UPLOAD_EXCEL_UPLOAD_BY_ASSORTMENT : 'batchUploadByAssortment',
        BATCH_UPLOAD_EARLEY : 'batchUploadEarley',
        BATCH_UPLOAD_RELATED_PRODUCTS : 'batchUploadRelatedProducts',
        BATCH_UPLOAD_MAT_ATTRIBUTE_VALUES : 'batchUploadMatAttributeValues',
        ECOMMERCE_ATTRIBUTE : 'eCommerceAttribute',
        CODE_TABLE : 'code-table',
        CHECK_STATUS : 'checkstatus',
        BATCH_UPLOAD_BY_NUTRITION_AND_INGREDIENTS: 'batchUploadByNutritionAndIngredients',
        BATCH_UPLOAD_BY_PRIMO_PICK: 'batchUploadByPrimoPick',
        BATCH_UPLOAD_BY_SERVICE_CASE_SIGNS:'serviceCaseSigns',
        PRODUCT_UPDATES_TASK: 'productUpdatesTask',
        NUTRITION_UPDATES_TASK: 'nutritionUpdatesTask',
        ECOMMERCE_TASK: 'ecommerceTask',
        IMAGE_MANAGEMENT: 'image-upload',
        PRODUCT_GROUP: 'productGroup',
        CASE_UPC_GENERATOR:'caseUPCGenerator',
        FIND_MY_ATTRIBUTE:'findMyAttribute',
        SCALE_MAINTENANCE_LOAD:'scaleMaintenanceLoad',
        SCALE_MAINTENANCE_CHECK_STATUS:'scaleMaintenanceCheckStatus',
		SCALE_MAINTENANCE_STORE_DEPT:'scaleMaintenanceStoreDept',
		SCALE_MAINTENANCE_PANTRY_STORES:'scaleMaintenancePantryStores',
        AUTHORIZATION:'authorization',
        BATCH_UPLOAD_EBM_BDA : 'batchUploadEBMBDA',
        NOT_FOUND:'notFound'
    };
    /**
     * This array holds the list of url page that does not the app check permission when user enter from address bar.
     * @type {string[]} the list of url paths.
     */
    var ignorePermissionCheckPages = ['/notFound', '/login'];
    app.constant('appName', 'Product Maintenance');
    app.constant('appConstants', constList);
    app.constant('urlBase', 'product-maintenance_api');
    app.constant('ignorePermissionCheckPages', ignorePermissionCheckPages);
    app.constant('appVersion', '@@UIversion');

    /*
     * Add a filter to convert true and false to Y and N. This is based on code found here:
     * http://stackoverflow.com/questions/24611455/how-to-display-yes-no-instead-of-true-false-in-angularjs
     * by http://stackoverflow.com/users/2819741/mainguy
     */
    app.filter('boolean_yn', function() {
        return function(text, lenght, end) {
            if (typeof(text) == 'undefined' || text == null) {
                return '';
            }
            if (text) {
                return 'Y';
            }
            return 'N';
        }
    });

    /**
     * Add a filter to only return rows in an array that are not to be deleted.
     * Assumes the items have an attribute called toDelete if they are to be filtered.
     */
    app.filter('notDeleted', function() {
        return function(values) {
            var result = [];

            angular.forEach(values, function(value, key) {
                if (typeof(value.toDelete) === 'undefined' || value.toDelete === null || value.toDelete === false) {
                    result.push(value);
                }
            });

            return result;
        }
    });

    /*
     * Add a filter to convert date to "MM/dd/yyyy" format (the date standard for this application).
     */
    app.filter('date_standard', function($filter) {
        var angularDateFilter = $filter('date');
        return function(date) {
            if (typeof date === 'undefined' || date === null) {
                return '';
            }
            return angularDateFilter(date, 'MM/dd/yyyy');
        }
    });
})();

/**
 * Disable ctrl + o not show default window dialog.
 */
$(window).bind('keydown', function(e) {
    var keyCode = e.keyCode || e.which;
    if (e.keyCode === 79 && e.ctrlKey)
    {
        window.onhelp = function () { return false; }
        e.cancelable = true;
        e.stopPropagation();
        e.preventDefault();
        e.returnValue = false;
    }
});
