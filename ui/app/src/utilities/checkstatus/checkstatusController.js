/*
 *
 * checkstatusController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.14.0
 */
'use strict';

/**
 * The controller for the check status Controller.
 */
(function() {

    angular.module('productMaintenanceUiApp').controller('checkstatusController', checkstatusController);

    checkstatusController.$inject = ['$sce','urlBase','$scope','$http','checkstatusApi', 'ngTableParams', '$log','DownloadService','$stateParams','$interval', '$filter'];

    /**
     * Constructs the controller.
     */
    function checkstatusController($sce,urlBase,$scope,$http,checkstatusApi, ngTableParams, $log,downloadService,$stateParams,$interval, $filter) {

        var self = this;
        self.PAGE_SIZE = 10;
        self.WAIT_TIME = 120;
        self.downloading = false;
        $scope.actionUrl =urlBase+'/pm/batchUpload/assortment/download-template';
        const MESSAGE_WARNING_EXPORT = "We found more than 30K records so please use Date option to export rest of the records.";
        const MAX_RECORD_EXPORT = 30000;
        /**
         * Whether or not the controller is waiting for data
         * @type {boolean}
         */
        self.isWaiting = false;

        /**
         * Attributes for each row in the table
         * @type {Array}
         */
        self.attributes = [];

        /**
         * Used to keep track of the number of columns in the table
         * @type {Array}
         */
        self.columns = [];
        /**
         * The maximum number of priorities an attribute has.
         * @type {number}
         */
        self.maxNumberofPriorities = 0;
        /**
         * Used to keep track of the attribute name
         * @type {Array}
         */
        self.attributeNames = [];
        /**
         * data source of tracking table
         * @type {Array}
         */
        self.data = null;
        /**
         * the tracking selected
         * @type {null}
         */
        self.selectedItem=null;
        /**
         * show deteail tracking
         * @type {boolean}
         */
        self.showDetail=false;
        /**
         * tracking id on param
         * @type {number}
         */
        self.trackingId = -1;
        // store the interval promise in this variable
        self.promise;
        self.firstSearch=true;
        /**
         * Request ID for filter.
         */
        self.requestIdFilter = null;

        /**
         * Data origin of check status function.
         * @type {Array}
         */
        self.originData = [];

        /**
         * First load page check stutus.
         * @type {boolean}
         */
        self.firstLoad = true;
        /**
         * Start date filter
         * @type {string}
         */
        self.startDateFilter = null;
        /**
         * End date filter
         * @type {string}
         */
        self.endDateFilter = null;
        /**
         * description filter
         * @type {string}
         */
        self.descriptionFilter = null;
        /**
         * attribute filter
         * @type {string}
         */
        self.attributeFilter = null;
        /**
         * user filter template.
         * @type {object}
         */
        self.userFilterTemp = {
            id : null,
            description : "--Select--"
        };
        /**
         * user filter
         * @type {string}
         */
        self.userFilter = angular.copy(self.userFilterTemp);
        /**
         * user list create tracking.
         * @type {string}
         */
        self.userList = [];
        /**
         * request id currently filter
         * @type {string}
         */
        self.requestIdFilterOrg = "";
        /**
         * Start date currently filter
         * @type {string}
         */
        self.startDateFilterOrg = "";
        /**
         * end date currently filter
         * @type {string}
         */
        self.endDateFilterOrg = "";
        /**
         * description currently filter
         * @type {string}
         */
        self.descriptionFilterOrg =  "";
        /**
         * attribute currently filter
         * @type {string}
         */
        self.attributeFilterOrg =  "";
        /**
         * user currently filter
         * @type {string}
         */
        self.userIdFilterOrg = "";
        /**
         * flag open date picker
         * @type {boolean}
         */
        self.startDatePickerOpened = false;

        /**
         * Initiates the construction of the Check statsus
         */
        self.init = function () {
            self.isWaiting=true;
            self.buildColumns();
            /**
             * Ng-table params variable for maintaining data.
             */
            if($stateParams && $stateParams.trackingId){
                self.trackingId = $stateParams.trackingId;
            }
            self.tableParams = self.buildTable();
            //$scope.start();
            self.getUserList();
        };
        self.checkAutoRefresh = function(){
            var auto = document.getElementById("auto-refresh");
            if(auto.checked){
                $scope.start();
            }else{
                $scope.stop();
            }
        };
        // stops the interval when the scope is destroyed,
        // this usually happens when a route is changed and
        // the Controller $scope gets destroyed. The
        // destruction of the Controller scope does not
        // guarantee the stopping of any intervals, you must
        // be responsible for stopping it when the scope is
        // is destroyed.
        $scope.$on('$destroy', function() {
            $scope.stop();
        });
        self.autoRefresh = function(){
            var auto = document.getElementById("auto-refresh");
            if(auto.checked){
                self.tableParams.page(1);
                self.tableParams.reload()
            }
        };
        // stops the interval
        $scope.stop = function() {
            $interval.cancel(self.promise);
        };
        // starts the interval
        $scope.start = function() {
            // stops any running interval to avoid two intervals running at the same time
            $scope.stop();

            // store the interval promise
            self.promise=$interval(self.autoRefresh,10000);
        };

        /**
         * Constant when start date invalid.
         *
         * @type {String}
         */
        const START_LESS_OR_EQUALS_END = 'Start Date must be less than or equals End Date.';

        /**
         * refresh page function
         */
        self.clearResult = function(){
            self.requestIdFilter = null;
            self.startDateFilter = null;
            self.endDateFilter = null;
            self.descriptionFilter = null;
            self.attributeFilter = null;
            self.userFilter = angular.copy(self.userFilterTemp);
            self.trackingId = -1;
            self.setFilterCriteria();
            self.error = null;
            self.data=[];
            self.selectedItem=null;
            self.firstSearch = true;
            self.tableParams = self.buildTable();
        };
        self.manualRefresh = function(){
            self.error = null;
            self.firstSearch = true;
            self.tableParams = self.buildTable();
        };
        /**
         *If there is an error this will display the error
         * @param error
         */
        self.fetchError = function (error) {
            self.isWaiting = false;
            self.data = null;
            if (error && error.data) {
                if (error.data.message != null && error.data.message != "") {
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
         * Constructs the ng-table.
         */
        self.buildTable = function () {
            return new ngTableParams(
                {
                    page: 1,
                    count: self.PAGE_SIZE
                }, {
                    counts: [],
                    getData: function ($defer, params) {
                        self.data = null;
                        self.defer = $defer;
                        self.dataResolvingParams = params;
                        self.includeCounts = false;
                        if (self.firstSearch) {
                            self.includeCounts = true;
                            self.firstSearch = false;
                        }
                        self.getTrackingData(params.page()-1);
                    }
                }
            )
        };
        /**
         * get list tracking info from database with page number
         * @param page
         */
        self.getTrackingData = function (page) {
            self.isWaiting = true;
            self.selectedItem = null;
            if(self.trackingId < 0){
                checkstatusApi.getListTracking({
                    includeCounts: self.includeCounts,
                    page: page,
                    pageSize: self.PAGE_SIZE,
                    requestId: self.requestIdFilterOrg,
                    attribute: self.attributeFilterOrg,
                    description: self.descriptionFilterOrg,
                    startDate: self.startDateFilterOrg,
                    endDate : self.endDateFilterOrg,
                    userId: self.userIdFilterOrg
                }).$promise.then(self.loadData).catch(function(error) {
                    self.handleError(error);
                }).finally(function(){
                    PMCommons.displayLoading(self, false);
                });
            }else{
                self.requestIdFilter = angular.copy(self.trackingId);
                self.requestIdFilterOrg = angular.copy(self.trackingId);
                checkstatusApi.getTrackingDetailById({
                    trackingId: self.trackingId
                }).$promise.then(self.loadData).catch(function(error) {
                    self.handleError(error);
                }).finally(function(){
                    PMCommons.displayLoading(self, false);
                });
            }
        };
        /**
         * Callback for a successful call to get list tracking data from the backend.
         *
         * @param results The data returned by the backend.
         */
        self.loadData = function (results) {
            self.isWaiting = false;
            self.error = null;
            if (self.isNullOrEmpty(self.requestIdFilter) && self.firstLoad){
                self.originData  = angular.copy(results);
                self.firstLoad = false;
            }
            self.firstSearch=false;
            if(!self.searchAfterHandle){
                self.success = null;
            } else {
                self.success = self.messageWaiting;
            }

            // If this was the fist page, it includes record count and total pages .
            if (results.complete) {
                self.totalRecordCount = results.recordCount;
                self.dataResolvingParams.total(self.totalRecordCount);
            }
            if (results.data.length === 0) {
                self.isWaiting = false;
                self.data = null;
                self.dataResolvingParams.data = [];
                if(!self.searchAfterHandle){
                    self.error = "No records found.";
                }
            } else {
                self.error = null;
                self.data = results.data;
                self.defer.resolve(results.data);
            }
            if(self.searchAfterHandle){
                self.searchAfterHandle = false;
            }
        };

        /**
         * selected tracking and show detail
         * @author vn87351
         **/
        self.selectTrackingId = function(data){
            self.selectedItem=data;
            self.showDetail=true;
        };
        /**
         * Creates a list with all of the column names based on the priorities
         * @param maxNumber
         */
        self.buildColumns = function(){
            self.columns.push(
                {	title: 'Request ID',
                    field: 'requestId',
                    visible: true},
                {
                    title: 'Attribute Selected',
                    field: 'attrSelected',
                    visible: true},
                {	title: 'Description',
                    field: 'updtDescription',
                    visible: true},
                {title: 'Date & Time of Request',
                    field: 'dateTime',
                    visible: true},
                {title: 'Status',
                    field: 'status',
                    visible: true},
                {title: 'Result',
                    field: 'result',
                    visible: true},
                {title: 'User',
                    field: 'userId',
                    visible: true});

        };


        /**
         * This function is to check if User change the user value
         */
        self.checkIfUserChangeValue = function(){
            if(self.endDateFilter === null && self.startDateFilter === null && self.userFilter.id != null){
                var today = new Date();
                self.endDateFilter = today;
                self.startDateFilter = new Date().setDate(today.getDate()-6);
            }
        };


        /**
         * Convert date string to datetime.
         */
        self.convertStringToDateTime = function(dateString){
            return new Date(dateString.split(' ')[0] + ' ' + dateString.split(' ')[1].replace(/-/g,":"));
        };

        /**
         * Check object null or empty.
         *
         * @param object
         * @returns {boolean}
         */
        self.isNullOrEmpty = function (object) {
            return object === undefined || object === null || !object || object === "";
        };

        /**
         * Compare the endDate is greater than startDate or not.
         *
         * @param endDate the date.
         * @param startDate the date.
         * @returns {boolean} true if the endDate is greater than or equals endDate or false.
         */
        self.isEndDateGreaterThanOrEqualsStartDate = function (endDate, startDate) {
            if ((new Date(self.convertDateToStringWithYYYYMMDD(endDate)).getTime() >= new Date(self.convertDateToStringWithYYYYMMDD(startDate)).getTime())) {
                return true;
            }
            return false;
        };

        /**
         * Convert the date to string with format: yyyy-MM-dd.
         * @param date the date object.
         * @returns {*} string
         */
        self.convertDateToStringWithYYYYMMDD = function (date) {
            return $filter('date')(date, 'yyyy-MM-dd');
        };

        /**
         * Handle event when filter by request id.
         */
        self.searchTracking = function () {
            if(self.endDateFilter !== null && self.startDateFilter !== null && self.isEndDateGreaterThanOrEqualsStartDate( self.endDateFilter ,self.startDateFilter)
                || (self.endDateFilter !== null &&  self.startDateFilter === null)
                || (self.endDateFilter === null &&  self.startDateFilter !== null)
                || (self.endDateFilter === null &&  self.startDateFilter === null)){
                self.trackingId = -1;
                self.setFilterCriteria();
                self.firstSearch = true;
                self.tableParams = self.buildTable();
            }else if(self.endDateFilter !== null && self.startDateFilter !== null && !self.isEndDateGreaterThanOrEqualsStartDate(self.endDateFilter ,self.startDateFilter)){
                self.error = START_LESS_OR_EQUALS_END;
            }
        };

        /**
         * This function to set filter criteria for all attributes.
         */
        self.setFilterCriteria = function(){
            self.requestIdFilterOrg = self.isNullOrEmpty(self.requestIdFilter) ? '' : self.requestIdFilter;
            self.attributeFilterOrg =  self.isNullOrEmpty(self.attributeFilter) ? '' : self.attributeFilter;
            self.descriptionFilterOrg =  self.isNullOrEmpty(self.descriptionFilter) ? '' : self.descriptionFilter;
            self.startDateFilterOrg = self.startDateFilter == null? '': self.convertDateToStringWithYYYYMMDD(self.startDateFilter);
            self.endDateFilterOrg = self.endDateFilter == null? '': self.convertDateToStringWithYYYYMMDD(self.endDateFilter);
            self.userIdFilterOrg =  self.isNullOrEmpty(self.userFilter.id) ? '' : self.userFilter.id;
        };


        /**
         * When click open date picker , store current status for date picker.
         */
        self.openDatePicker = function (fieldName) {
            switch (fieldName){
                case "startDateFilter":
                    self.startDatePickerOpened = true;
                    break;
                case "endDateFilter":
                    self.endDatePickerOpened = true;
                    break;
                default:
                    break;
            }
            self.options = {
                maxDate: new Date()
            };
        };

        /**
         * Get list of user created tracking.
         */
        self.getUserList = function () {
            checkstatusApi.getUserList({},
                function (results) {
                    self.userFilter = angular.copy(self.userFilterTemp);
                    self.userList.push(angular.copy(self.userFilterTemp));
                    if (results != null) {
                        results.forEach(function (item) {
                            var user = {
                                id: item,
                                description: item
                            };
                            self.userList.push(user);
                        });
                    }
                },
                function (error) {
                    self.handleError(error);
                }
            );
        };

        /**
         * Callback that will respond to errors sent from the backend.
         *
         * @param error The object with error information.
         */
        self.handleError = function (error) {
            if (error && error.data) {
                self.isError = error.data.error;
            } else {
                self.isError = "An unknown error occurred.";
            }
            self.isWaiting = false;
        };

        /**
         * Max time to wait for csv download.
         * @type {number}
         */
        self.WAIT_TIME = 1200;

        /**
         * Initiates a download of all the records.
         */
        self.export = function () {
            self.checkSizeDataExport();
            self.downloading = true;
            downloadService.export(self.generateExportUrl(), 'Tracking.csv', self.WAIT_TIME,
                function () { self.downloading = false; });
        };

        /**
         * Generates the URL to ask for the export.
         * @returns {string} The URL to ask for the export.
         */
        self.generateExportUrl = function () {
            var requestParam = "requestId="+self.requestIdFilterOrg+"&attribute="+self.attributeFilterOrg
                +"&description="+self.descriptionFilterOrg +"&startDate="+self.startDateFilterOrg
                +"&endDate="+self.endDateFilterOrg+"&userId="+self.userIdFilterOrg ;
            return urlBase + '/pm/batchUpload/exportTrackingToCsv?' + requestParam;
        };

        /**
         * Check data export csv.
         */
        self.checkSizeDataExport = function () {
            self.success = null;
            self.error = null;
            checkstatusApi.countItemExport(
                {
                    requestId: self.requestIdFilterOrg,
                    attribute: self.attributeFilterOrg,
                    description: self.descriptionFilterOrg,
                    startDate: self.startDateFilterOrg,
                    endDate: self.endDateFilterOrg,
                    userId: self.userIdFilterOrg
                },
                //success case
                function (results) {
                    //if total record export > 30k, show warning message
                    if(results != null && results.totalItemExport > MAX_RECORD_EXPORT){
                        self.success = MESSAGE_WARNING_EXPORT;
                    }
                },
                function (error) {
                    self.handleError(error);
                }
            );
        }
    }
})();
