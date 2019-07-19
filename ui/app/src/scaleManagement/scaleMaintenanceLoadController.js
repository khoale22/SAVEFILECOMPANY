/*
 * scaleMaintenanceLoadController.js
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the scale maintenance load.
 *
 * @author m314029
 * @since 2.18.3
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('ScaleMaintenanceLoadController', scaleMaintenanceLoadController);

	scaleMaintenanceLoadController.$inject = ['ScaleManagementApi', 'ngTableParams','$rootScope', '$scope', 'ScaleMaintenanceApi', '$state', 'appConstants'];

	/**
	 * Creates the controller for the scale maintenance loads.
	 */
	function scaleMaintenanceLoadController(scaleManagementApi, ngTableParams,$rootScope, $scope, scaleMaintenanceApi, $state, appConstants) {

		var self = this;
		/**
		 * The current error message.
		 * @type {String}
		 */
		self.error = null;

		/**
		 * The current success message.
		 * @type {Boolean}
		 */
		
		self.success = null;
		/**
		 * The current Load by Dept success message.
		 * @type {Boolean}
		 */
		self.loadSuccess = null;

		/**
		 * Whether the front end is waiting for api return.
		 * @type {Boolean}
		 */
		self.waitingForUpdate = null;

		/**
		 * Whether or not the search panel is collapsed or open.
		 * @type {boolean}
		 */
		self.dataSearchToggle = true;

		/**
		 * The current search text field.
		 * @type {String}
		 */
		self.searchText = null;

		/**
		 * The current view.
		 * @type {String}
		 */
		self.currentView = null;

		/**
		 * Whether or not page is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Controller data.
		 * @type {Array}
		 */
		self.data = null;

		/**
		 * Array with available stores to send scale maintenance loads to.
		 *
		 * @type {Array}
		 */
		self.loadStores = null;
		/**
		 * Wheter or not to ask the backed for the number of records and pages are available.
		 * @type {boolean}
		 */
		self.includeCounts = true;

		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		var firstSearch = true;

		/**
		 * Whether or not user is searching by PLU
		 * @type {boolean}
		 */
		self.searchingByPlu = true;
		/**
		 * Whether or not the table has been built. We don't want to build the table until there is something
		 * to search for.
		 * @type {boolean} True if it has and false otherwise.
		 */
		var tableBuilt = false;

		/**
		 * The paramaters that define the table showing the report.
		 * @type {ngTableParams}
		 */
		self.tableParams = null;

		/**
		 * The paramaters passed from the ngTable when it is asking for data.
		 * @type {?}
		 */
		self.dataResolvingParams = null;

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 * @type {?}
		 */
		self.defer = null;

		/**
		 * The total number of records in the report.
		 * @type {int}
		 */
		self.totalRecordCount = null;

		/**
		 * The total number of pages in the report.
		 * @type {null}
		 */
		self.totalPages = null;

		/**
		 * The message to display about the nubmer of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;

		/**
		 * Total amount of scale upcs that will be submitted for scale maintenance load.
		 * @type {number}
		 */
		self.loadCount = 0;
		self.selectedStores = null;

		self.TRANSACTION_CREATED_PRE_TEXT = "Transaction id: ";
		self.TRANSACTION_CREATED_POST_TEXT = " created successfully.";
		self.NAVIGATE_TO_CHECK_STATUS_TEXT = "Navigate to check status for this transaction id.";
		self.UPDATING_MESSAGE = "Updating...please wait.";
		const FIRST_PAGE = 1;
		const PAGE_SIZE = 100;
		const COMMA_SEPARATOR = ",";
		/**
		 * Variables added for Send Load By Department Functionality
		 */
		self.datePickerOptions = {
				minDate: new Date()
		};
		self.responseDate = new Date();
		self.effectiveDatePickerOpened = false;
		self.resultDiv = false;
		self.inputError = null;
		$scope.DeptList = [213,301,401,403,601,603,701,901,141,121,131,151,161,191];
		$scope.DepartmentList =  [{"deptKey": "213", "deptValue": "02"}, { "deptKey": "301", "deptValue": "03" }, { "deptKey": "401", "deptValue": "04" }, { "deptKey": "403", "deptValue": "04" }, { "deptKey": "601", "deptValue": "06" }, { "deptKey": "603", "deptValue": "06" }, { "deptKey": "701", "deptValue": "07" }, { "deptKey": "901", "deptValue": "09" }, { "deptKey": "141", "deptValue": "14" }, { "deptKey": "121", "deptValue": "12" }, { "deptKey": "131", "deptValue": "13" }, { "deptKey": "151", "deptValue": "15" }, { "deptKey": "161", "deptValue": "16" }, { "deptKey": "191", "deptValue": "19" } ];
		
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			getAvailableLoadStores();
		};

		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(){
			firstSearch = true;
			self.selectAll = true;
			// The first time through, build the table. The rest of the time, just tell it to fetch new data.
			if (tableBuilt) {
				self.tableParams.reload();
			} else {
				tableBuilt = true;
				self.buildTable();
			}

		};

		/**
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function () {
			self.tableParams = new ngTableParams(
				{
					// set defaults for ng-table
					page: FIRST_PAGE,
					count: PAGE_SIZE
				}, {
					// hide page size
					counts: [],
					/**
					 * Called by ngTable to load data.
					 *
					 * @param $defer The object that will be waiting for data.
					 * @param params The parameters from the table helping the function determine what data to get.
					 */
					getData: function ($defer, params) {
						self.isWaiting = true;
						self.data = null;
						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.defer = $defer;
						self.dataResolvingParams = params;
						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if (firstSearch) {
							self.includeCounts = true;
							params.page(1);
							firstSearch = false;
						} else {
							self.includeCounts = false;
						}
						// Issue calls to the backend to get the data.
						self.searchingByPlu ?
							self.newItemSearch(params.page() - 1) : self.newDescriptionSearch(params.page() - 1);
					}
				}
			);
		};

		/**
		 * Initializes the item search.
		 */
		self.initItemSearch = function(){
			self.resetValues();
			self.searchingByPlu = true;
			self.resultDiv = false;
		};

		/**
		 * Initializes the description search.
		 */
		self.initDescriptionSearch = function(){
			self.resetValues();
			self.searchingByPlu = false;
			self.resultDiv = false;
		};
		
		/**
		 * Initializes the description search.
		 */
		self.initDepartmentSearch = function(){
			self.resetValues();
			self.searchingByPlu = false;
			
		};
		
		/**
		 * Reset necessary variables to default values.
		 */
		self.resetValues = function(){
			self.searchText = null;
			self.error = null;
			self.success = null;
			self.waitingForUpdate = null;
			self.data = null;
			firstSearch = true;
			self.strNumber = null;
			self.targetStrNumber=null;
			self.deptNumber = null;
			self.responseDate = new Date();
		};

		/**
		 * Sets new Search Values.
		 * @param searchByDescription
		 * @returns {boolean}
		 */
		function setNewSearchValues(searchByDescription) {
			if (self.searchText == null) {
				return false;
			}
			self.searchedByDescription = searchByDescription;
			self.isWaiting = true;
			return true;
		}

		/**
		 * Search for products by one or more PLUs.
		 */
		self.newItemSearch = function(page) {
			if (setNewSearchValues(false)) {
				scaleManagementApi.queryByPluList({
						pluList: self.searchText,
						page: page,
						includeCounts: self.includeCounts
					},
					loadData,
					fetchError);
				scaleManagementApi.queryForMissingPLUs({
						pluList: self.searchText
					},
					loadMissingData,
					fetchError);
			}
			else {
				self.error = "Please enter a PLU to search for.";
			}
		};

		/**
		 * Search for products by description.
		 */
		self.newDescriptionSearch = function(page) {
			if (setNewSearchValues(true)) {
				scaleManagementApi.queryByDescription({
						description: self.searchText,
						page: page,
						includeCounts: self.includeCounts
					},
					loadData,
					fetchError);
			}
			else {
				self.error = "Please enter a description to search for.";
			}
		};

		/**
		 * Callback for when the backend returns a success.
		 *
		 * @param results The results from the backend.
		 */
		function loadData(results) {
			self.success = null;
			self.isWaiting = false;
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.totalPages = results.pageCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.error = "No records found.";
			} else {
				self.error = null;
				self.dataSearchToggle = false;

				self.modifyMessage = null;
				self.data = results.data;
				self.resultMessage = self.getResultMessage(results.data.length, self.tableParams.page() - 1);

				for(var index = 0; index < self.data.length; index++) {
					self.data[index].isChecked = true;
				}
				self.loadCount = results.data.length;
				self.selectAll = true;

				if (self.searchedByDescription) {
					loadMissingData({matchCount: self.totalRecordCount, noMatchCount: 0, noMatchList: []});
				}
				self.defer.resolve(self.data);
			}
		}
		/**
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		self.getResultMessage = function(dataLength, currentPage){
			return (PAGE_SIZE * currentPage + 1) +
				" - " + (PAGE_SIZE * currentPage + dataLength) + " of " + self.totalRecordCount;
		};

		/**
		 * Callback for the request for the number of items found and not found.
		 *
		 * @param results The object returned from the backend with a list of found and not found items.
		 */
		function loadMissingData(results){
			self.missingValues = results;
		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		function fetchError(error) {
			self.isWaiting = false;
			self.waitingForUpdate = false;
			if (error && error.data) {
				if(error.data.message) {
					setError(error.data.message);
				} else {
					setError(error.data.error);
				}
			}
			else {
				setError("An unknown error occurred.");
			}
		}

		/**
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		function setError(error) {
			self.error = error;
		}

		/**
		 * Gets the stores available to send scale maintenance loads to.
		 */
		function getAvailableLoadStores() {
			scaleMaintenanceApi.getAvailableLoadStores(
				{},
				function(results){
					self.loadStores = results;
				},
				fetchError)
		}
		
		/**
		 * Added for Send Load by Department
		 * Process the data for send Load by Department.
		 */
		self.sendLoadByDept = function(){
			self.dataSearchToggle = false;
			self.resultDivStrNbr = self.strNumber;
			self.resultDivDeptNbr = self.deptNumber;
			self.resultDivTargetStrNbr = self.targetStrNumber;
			self.submitLoadByDept();
			
			
		};
		
		/**
		 * Added for Send Load by Department
		 * Reset the values to null in Department search tab.
		 */
		self.clearValues = function(){
			self.resultDivStrNbr = null;
			self.resultDivDeptNbr = null;
			self.resultDivTargetStrNbr = null;
			self.resultDiv = false;
			self.strNumber = null;
			self.targetStrNumber=null;
			self.deptNumber = null;
			self.responseDate = new Date();
			self.inputError = null;
		}

		/**
		 * Added for Send Load by Department
		 * Converts the Three digit department number to two digit number.
		 */
		self.getDepartment = function() {
			var	twoDigitDptNbr = "";
			for (var i in $scope.DepartmentList) {
				if (self.deptNumber == $scope.DepartmentList[i].deptKey) {
					twoDigitDptNbr = $scope.DepartmentList[i].deptValue;
				} 
			}
			return twoDigitDptNbr;
		}
		
		/**
		 * Added for Send Load by Department
		 * Compares the input source store and Target store and throws error if both are same.
		 */
		self.validateTargetStore = function() {/*
			if(self.targetStrNumber==self.strNumber){
				self.inputError = true;
				self.targetStrNumber = null;
				
			}else{
				self.inputError = false;
			}
			
		*/}

		/**
		 * Toggles the open status of the effective date picker.
		 */
		self.openEffectiveDatePicker = function() {
			self.effectiveDatePickerOpened = !self.effectiveDatePickerOpened;
		};
		

		/**
		 * Submits a scale maintenance load for the selected upcs and stores.
		 */
		self.submitLoad = function(){
			var stores = "";
			angular.forEach(self.selectedStores, function(store){
				stores += store + COMMA_SEPARATOR;
			});
			var upcs = "";
			angular.forEach(self.data, function(scaleUpc){
				if(scaleUpc.isChecked) {
					upcs += scaleUpc.upc + COMMA_SEPARATOR;
				}
			});
			var parameters = {
				upcs: upcs,
				stores: stores
			};
			self.success = null;
			self.waitingForUpdate = true;
			self.error = null;
			scaleMaintenanceApi.submitLoadToStores(
				parameters,
				function(results){
					self.waitingForUpdate = false;
					self.success = true;
					self.transactionIdCreated = results.transactionId;
				},
				fetchError)
		};
		
		
		/**
		 * Submits a scale maintenance load for the selected Department and stores.
		 */
		self.submitLoadByDept = function(){
			var stores = "";
			var departmentNbr = "";
			var effectiveDate = "";
			var targetStore = "";
			var effDate =  $rootScope.convertDateWithFullYear(self.responseDate);
			stores=  self.strNumber;
			departmentNbr = self.getDepartment();
			effectiveDate = effDate;
			targetStore = self.targetStrNumber;
			var parameters = {
				stores: stores,
				departmentNbr:departmentNbr,
				effectiveDate:effectiveDate,
				targetStore:targetStore
			};
			self.loadSuccess = null;
			self.isWaiting = true;
			self.error = null;
			scaleMaintenanceApi.submitLoadToStores(
				parameters,
				function(results){
					self.isWaiting = false;
					self.loadSuccess = true;
					self.transactionIdCreated = results.transactionId;
					self.resultDiv = true;
					self.resetValues();
					
				},
				fetchError)
				
				
		};
		
		/**
		 * When the select all box is clicked on.
		 * If select all is checked, all of the checkboxes will then be checked.
		 * If select all is unchecked, all of the checkboxes will then be unchecked.
		 */
		self.updateAllSelected = function() {
			if(self.selectAll) {
				for(var index = 0; index < self.data.length; index++) {
					self.data[index].isChecked = true;
					self.loadCount = self.data.length;
				}
			} else {
				for(index = 0; index < self.data.length; index++) {
					self.data[index].isChecked = false;
					self.loadCount = 0;
				}
			}
		};

		/**
		 * Changes single update all checkbox. Also checks whether one is unchecked and will uncheck the select
		 * all checkbox.
		 * @param value value true or false depending on whether it was checked or unchecked
		 */
		self.updateSelectAllSwitch = function(value) {
			if(!value) {
				self.selectAll = false;
				self.loadCount--;
			} else {
				var found = false;
				for(var index = 0; index < self.data.length; index++) {
					if(!self.data[index].isChecked){
						found = true;
						break;
					}
				}
				if(!found){
					self.selectAll = true;
					self.loadCount = self.data.length;
				} else {
					self.loadCount++;
				}
			}
		};

		/**
		 * Verifies a load is valid.
		 * A load is valid if and only if:
		 * 1. At least 1 scale upc is selected, AND
		 * 2. At least 1 store is selected
		 */
		self.isLoadValid = function() {
			return self.loadCount > 0 && self.selectedStores && self.selectedStores.length > 0;
		};
		
		/**
		 * Go to scale maintenance check status page and send the new transaction id.
		 */
		self.navigateToCheckStatus = function(){
			$state.go(appConstants.SCALE_MAINTENANCE_CHECK_STATUS, {transactionId: self.transactionIdCreated})
		}
	}
})();
