/*
 *   casePackImportComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * CasePack -> Case Pack Info page component.
 *
 * @author vn40486
 * @since 2.3.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('casePackImport', {
		// isolated scope binding
		bindings: {
			itemMaster: '<'
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/casePackImport.html',
		// The controller that handles our component logic
		controller: CasePackImportController
	});

	CasePackImportController.$inject = ['ProductFactory','PMCommons','$scope','$rootScope', 'ProductCasePackApi',
		'UserApi', '$timeout', 'ngTableParams', '$filter', 'ProductDetailAuditModal',
		'PermissionsService','ProductSearchService'];

	/**
	 * Case Pack Import component's controller definition.
	 * @param $scope	scope of the case pack info component.
	 * @constructor
	 */
	function CasePackImportController(productFactory, PMCommons, $scope,$rootScope, productCasePackApi, userApi,
									  $timeout, ngTableParams, $filter, ProductDetailAuditModal,
									  permissionsService, productSearchService) {
		/** All CRUD operation controls of Case pack Import page goes here */
		var self = this;
		/**
		 * Constant for NO_CHANGE_UPDATE_MESSAGE
		 * @type {string}
		 */
		const NO_CHANGE_UPDATE_MESSAGE = "There are no changes on this page to be saved. Please make any changes to update.";
		/**
		 * Constant for DSD_TYPE
		 * @type {string}
		 */
		const DSD_TYPE = "DSD";
		/**
		 * Constant for AGENT_VALIDATION
		 * @type {string}
		 */
		const AGENT_VALIDATION = "Agent % is greater than or equal to 0 and less than or equal to 99999.99";
		/**
		 * Constant for DUTY_VALIDATION
		 * @type {string}
		 */
		const DUTY_VALIDATION = "Duty % is greater than or equal to 0 and less than or equal to 99999.99";

		self.currentImport = null;

		self.originalImport = null;

		self.isLoadingFactory = false;
		/**
		 * The countries being shown.
		 * @type {Array}
		 */
		self.countryList = null;
		/**
		 * The container sizes.
		 * @type {Array}
		 */
		self.containerSizes = null;
		/**
		 * The inco terms.
		 * @type {Array}
		 */
		self.incoTerms = null;
		/**
		 * The index of the current bicep vendor.
		 * @type {number}
		 */
		self.index = 0;
		/**
		 * The the current country.
		 * @type {number}
		 */
		self.currentCountry = null;
		/**
		 * The index of the current container size.
		 * @type {number}
		 */
		self.currentContainerSize = null;
		/**
		 * The index of the current inco terms.
		 * @type {number}
		 */
		self.currentIncoTerms = null;
		/**
		 * Whether or not a user is editing the page.
		 * @type {boolean}
		 */
		self.isEditing = true;
		/**
		 * If prorationDatePicker is Opened.
		 * @type {boolean}
		 */
		self.prorationDatePickerOpened = false;
		/**
		 * The currentProrationDate.
		 * @type {date}
		 */
		self.currentProrationDate = null;
		/**
		 * If the inStoreDatePicker id Opened.
		 * @type {boolean}
		 */
		self.inStoreDatePickerOpened = false;
		/**
		 * The currentInStoreDate.
		 * @type {date}
		 */
		self.currentInStoreDate = null;
		/**
		 * If the warehouseFlushDatePicker is opened.
		 * @type {boolean}
		 */
		self.warehouseFlushDatePickerOpened = false;
		/**
		 * The currentWarehouseFlushDate.
		 * @type {date}
		 */
		self.currentWarehouseFlushDate = null;
		/**
		 * If the dutyConfirmationDatePicker is opened.
		 * @type {boolean}
		 */
		self.dutyConfirmationDatePickerOpened = false;
		/**
		 * The currentDutyConfirmationDate
		 * @type {date}
		 */
		self.currentDutyConfirmationDate = null;
		/**
		 * If freightConfirmationDatePicker is opened
		 * @type {boolean}
		 */
		self.freightConfirmationDatePickerOpened = false;
		/**
		 * The currentFreightConfirmationDate.
		 * @type {date}
		 */
		self.currentFreightConfirmationDate = null;
		/**
		 * Returns convertDate(date) function from higher scope.
		 * @type {function}
		 */
		self.convertDate = $scope.$parent.$parent.$parent.$parent.convertDate;

		/**
		 * List containing all of the factories.
		 * @type {null}
		 */
		self.factoryList = null;

		/**
		 * These flags represent individual api call loaded flags.  isLoading will only be true as long as any of these
		 * are false.
		 * @type {boolean}
		 */
		self.isDataLoaded = true;
		self.isCountryLoaded = true;
		self.isContainerSizeLoaded = true;
		self.isIncoTermsLoaded = true;

		/**
		 * Whether there is a bicep Vendor or not.
		 * @type {boolean}
		 */
		self.noBicepVendor = false;

		/**
		 * Whenever the page is "refreshed", includes switching between item codes.
		 */
		this.$onChanges= function () {
			self.initData();
			self.getData();
		};

		/**
		 * This resets the data back to regular.
		 */
		self.initData = function() {
			self.error = null;
			self.success = null;
			self.isDisabledImportFactory = false;
			self.noBicepVendor = false;
			self.isLoading = false;
			self.isDataLoaded = false;
			self.isCountryLoaded = false;
			self.isContainerSizeLoaded = false;
			self.isIncoTermsLoaded = false;
			self.index = 0;
			self.countryList = [];
			self.currentCountry = null;
			self.currentContainerSize = null;
			self.containerSizes = [];
			self.incoTerms = [];
			self.currentIncoTerms = null;
			self.currentDutyConfirmationDate = null;
			self.currentFreightConfirmationDate = null;
			self.currentInStoreDate = null;
			self.currentProrationDate = null;
			self.currentWarehouseFlushDate = null;
		};
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		}
		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};

		/**
		 * Gets the bicep vendor import data.
		 */
		self.getData = function() {
			self.isImportItem = false;
			if(self.itemMaster.key.itemType !== DSD_TYPE){
				self.isImportItem = true;
				self.isLoading = true;
				productCasePackApi.queryAllImportItemsByItemCode({
					itemCode: self.itemMaster.key.itemCode,
					itemType: self.itemMaster.key.itemType}, self.loadData, self.fetchError);
			}
		};

		/**
		 * Gets the list of countries.
		 */
		self.getCountries = function(){
			productCasePackApi.findAllCountriesOrderByName({}, self.loadCountryData, self.fetchError)
		};

		/**
		 * Gets the list of container sizes.
		 */
		self.getContainerSizes = function(){
			productCasePackApi.findAllImportContainerSizes({}, self.loadContainerSizes, self.fetchError)
		};

		/**
		 * Gets the list of inco terms.
		 */
		self.getIncoTerms = function(){
			productCasePackApi.findAllIncoTerms({}, self.loadIncoTerms, self.fetchError)
		};

		/**
		 * This method makes sure that while any of the api calls is incomplete the spinner will remain active.
		 */
		self.updateLoadingStatus = function () {
			//self.isLoading = !(self.isDataLoaded && self.isCountryLoaded && self.isContainerSizeLoaded && self.isIncoTermsLoaded);
		};

		/**
		 * Callback for a successful call to get vendor import data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			var dataResults = self.handleResultsDataBeforeAssign(results);
			if(dataResults && dataResults.length > 0){
				self.currentImport = angular.copy(dataResults[self.index]);
				self.originalImport = angular.copy(dataResults[self.index]);
				self.isDisabledImportFactory = angular.copy(self.currentImport.noImportData);
				self.vendorList = dataResults;
				self.setDatePickers();
				self.getCountries();
				self.getContainerSizes();
				self.getIncoTerms();
				self.findAllFactories();
				//self.updateLoadingStatus();
			} else {
				self.noBicepVendor = true;
			}
			self.isDataLoaded = true;
			self.isLoading = false;
		};

		/**
		 * trim data and convert the dates to local date.
		 *
		 * @param results The data returned by the backend.
		 *
		 * @return the processed data.
		 */
		self.handleResultsDataBeforeAssign = function(results){

			var tmpResults = angular.copy(results);
			var returnList = [];

			for(var i=0; i<tmpResults.length; i++){
				if(tmpResults[i].importItem){
					if(null!= tmpResults[i].importItem.color){
						tmpResults[i].importItem.color = tmpResults[i].importItem.color.trim();
					}
					if(null!= tmpResults[i].importItem.cartonMarking){
						tmpResults[i].importItem.cartonMarking = tmpResults[i].importItem.cartonMarking.trim();
					}
					if(null!= tmpResults[i].importItem.dutyInformation){
						tmpResults[i].importItem.dutyInformation = tmpResults[i].importItem.dutyInformation.trim();
					}
					tmpResults[i].importItem.inStoreDate = $rootScope.convertDateWithFullYear(tmpResults[i].importItem.inStoreDate);
					tmpResults[i].importItem.prorationDate = $rootScope.convertDateWithFullYear(tmpResults[i].importItem.prorationDate);
					tmpResults[i].importItem.warehouseFlushDate = $rootScope.convertDateWithFullYear(tmpResults[i].importItem.warehouseFlushDate);
					tmpResults[i].importItem.dutyConfirmationDate = $rootScope.convertDateWithFullYear(tmpResults[i].importItem.dutyConfirmationDate);
					tmpResults[i].importItem.freightConfirmationDate = $rootScope.convertDateWithFullYear(tmpResults[i].importItem.freightConfirmationDate);

					// Convert all fields that are integer type to string type for 
					// fixing invalid compare error between integer type and string type when check the data change before save or
					// navigate to other module.
					tmpResults[i].importItem.hts1 = tmpResults[i].importItem.hts1.toString();
					tmpResults[i].importItem.hts2 = tmpResults[i].importItem.hts2.toString();
					tmpResults[i].importItem.hts3 = tmpResults[i].importItem.hts3.toString();
					tmpResults[i].importItem.minOrderQuantity = tmpResults[i].importItem.minOrderQuantity.toString();
					tmpResults[i].importItem.sellByYear = tmpResults[i].importItem.sellByYear.toString();
					tmpResults[i].importItem.agentCommissionPercent = tmpResults[i].importItem.agentCommissionPercent.toString();
					tmpResults[i].importItem.dutyPercent = tmpResults[i].importItem.dutyPercent.toString();

					tmpResults[i].importItem.noImportData = false;

					returnList.push(tmpResults[i].importItem);
				}else{
					tmpResults[i].importItem = {};
					tmpResults[i].importItem.displayName = angular.copy(tmpResults[i].displayName);

					tmpResults[i].importItem.color = null;
					tmpResults[i].importItem.cartonMarking = null;
					tmpResults[i].importItem.dutyInformation = null;

					tmpResults[i].importItem.inStoreDate = null;
					tmpResults[i].importItem.prorationDate = null;
					tmpResults[i].importItem.warehouseFlushDate = null;
					tmpResults[i].importItem.dutyConfirmationDate = null;
					tmpResults[i].importItem.freightConfirmationDate = null;

					tmpResults[i].importItem.hts1 = null;
					tmpResults[i].importItem.hts2 = null;
					tmpResults[i].importItem.hts3 = null;
					tmpResults[i].importItem.minOrderDescription = null;
					tmpResults[i].importItem.minOrderQuantity = null;
					tmpResults[i].importItem.pickupPoint = null;

					tmpResults[i].importItem.countryOfOrigin = null;
					tmpResults[i].importItem.containerSizeCode = null;
					tmpResults[i].importItem.incoTermCode = null;

					tmpResults[i].importItem.season = null;
					tmpResults[i].importItem.sellByYear = null;
					tmpResults[i].importItem.agentCommissionPercent = null;
					tmpResults[i].importItem.dutyPercent = null;

					tmpResults[i].importItem.key = {};
					tmpResults[i].importItem.key.itemCode = tmpResults[i].key.itemCode;
					tmpResults[i].importItem.key.itemType = tmpResults[i].key.itemType;
					tmpResults[i].importItem.key.vendorType = tmpResults[i].key.locationType;
					tmpResults[i].importItem.key.vendorNumber = tmpResults[i].key.locationNumber;

					tmpResults[i].importItem.vendorItemFactory = null;

					tmpResults[i].importItem.noImportData = true;

					returnList.push(tmpResults[i].importItem);
				}
			}

			return returnList;
		};

		/**
		 * Callback for a successful call to get country data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadCountryData = function (results) {
			if (!(results.length === 0)) {
				self.countryList = results;
				self.findCountry();
				self.isCountryLoaded = true;
				//self.updateLoadingStatus();
			}
		};

		/**
		 * Callback for a successful call to get country data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadContainerSizes = function (results) {
			if (!(results.length === 0)) {
				self.containerSizes = results;
				self.findContainerSize();
				self.isContainerSizeLoaded = true;
				//self.updateLoadingStatus();
			}
		};

		/**
		 * Callback for a successful call to get country data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadIncoTerms = function (results) {
			if (!(results.length === 0)) {
				self.incoTerms = results;
				self.findIncoTerms();
				self.isIncoTermsLoaded = true;
				//self.updateLoadingStatus();
			}
		};

		/**
		 * Sets the current country;
		 */
		self.findCountry = function(){
			$timeout(function(){
				$scope.$evalAsync(function() {
					self.currentCountry = null;
					if(self.currentImport.countryOfOrigin){
						for(var i=0; i<self.countryList.length; i++){
							if(self.countryList[i].countryName.trim() === self.currentImport.countryOfOrigin.trim()){
								self.currentCountry = self.countryList[i];
								break;
							}
						}
					}
				});
			});
		};


		/**
		 * Sets the current container size;
		 */
		self.findContainerSize = function(){
			$timeout(function(){
				$scope.$evalAsync(function() {
					self.currentContainerSize = null;
					if(self.currentImport.containerSizeCode){
						for(var i=0; i<self.containerSizes.length; i++){
							if(self.containerSizes[i].trim() === self.currentImport.containerSizeCode.trim()){
								self.currentContainerSize = self.containerSizes[i];
								break;
							}
						}
					}
				});
			});
		};

		/**
		 * Sets the current container size;
		 */
		self.findIncoTerms = function(){
			$timeout(function(){
				$scope.$evalAsync(function() {
					self.currentIncoTerms = null;
					if(self.currentImport.incoTermCode){
						for(var i=0; i<self.incoTerms.length; i++){
							if(self.incoTerms[i].trim() === self.currentImport.incoTermCode.trim()){
								self.currentIncoTerms = self.incoTerms[i];
								break;
							}
						}
					}
				});
			});
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isLoading = false;
			self.isLoadingFactory = false;
			self.resetFactories();
			if(error && error.data) {
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
		 *
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.error = error;
		};

		/**
		 * Handles the drop downs for countries, container sizes, inco terms and the date pickers on
		 * changing of a vendor.
		 * @param vendor
		 */
		self.handleVendorListChange = function(index){
			self.error = null;
			self.success = null;
			self.currentImport = angular.copy(self.vendorList[index]);
			self.originalImport = angular.copy(self.vendorList[index]);
			self.index = index;
			self.findCountry();
			self.findContainerSize();
			self.findIncoTerms();
			self.setDatePickers();
			self.resetFactories();
			self.isDisabledImportFactory = angular.copy(self.currentImport.noImportData);
		};

		/**
		 * Handles changing the country on the drop down.
		 * @param country
		 */
		self.handleCountryListChange = function(country){
			self.currentCountry = angular.copy(country);
			self.currentImport.countryOfOrigin = (self.currentCountry !== null) ? angular.copy(self.currentCountry.countryName) : null;
		};

		/**
		 * Handles changing the container size on the drop down.
		 * @param size
		 */
		self.handleContainerSizeListChange = function(size){
			self.currentContainerSize = angular.copy(size);
			self.currentImport.containerSizeCode = angular.copy(self.currentContainerSize);
		};

		/**
		 * Handles changing the inco terms on the drop down.
		 * @param incoTerms
		 */
		self.handleIncoTermsListChange = function(incoTerms){
			self.currentIncoTerms = angular.copy(incoTerms);
			self.currentImport.incoTermCode = angular.copy(self.currentIncoTerms);
		};

		/**
		 * Open the proration picker to select a new date.
		 */
		self.openProrationDatePicker = function(){
			self.prorationDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Sets the current proration date.
		 */
		self.setDateForProrationDatePicker = function(){
			self.prorationDatePickerOpened = false;
			if(self.currentImport.prorationDate) {
				self.currentProrationDate = $rootScope.convertStringToDateWithSlashFormat(self.currentImport.prorationDate);
			} else {
				self.currentProrationDate = null;
			}
		};

		/**
		 * Open the inStore picker to select a new date.
		 */
		self.openInStoreDatePicker = function(){
			self.inStoreDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Sets the current in store date.
		 */
		self.setDateForInStoreDatePicker = function(){
			self.inStoreDatePickerOpened = false;
			if(self.currentImport.inStoreDate) {
				self.currentInStoreDate = $rootScope.convertStringToDateWithSlashFormat(self.currentImport.inStoreDate);
			} else {
				self.currentInStoreDate = null;
			}
		};

		/**
		 * Open the warehouseFlush picker to select a new date.
		 */
		self.openWarehouseFlushDatePicker = function(){
			self.warehouseFlushDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Sets the current warehouse flush date.
		 */
		self.setDateForWarehouseFlushDatePicker = function(){
			self.warehouseFlushDatePickerOpened = false;
			if(self.currentImport.warehouseFlushDate) {
				self.currentWarehouseFlushDate = $rootScope.convertStringToDateWithSlashFormat(self.currentImport.warehouseFlushDate);
			} else {
				self.currentWarehouseFlushDate = null;
			}
		};

		/**
		 * Open the dutyConfirmation picker to select a new date.
		 */
		self.openDutyConfirmationDatePicker = function(){
			self.dutyConfirmationDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Sets the current duty confirmation date.
		 */
		self.setDateForDutyConfirmationDatePicker = function(){
			self.dutyConfirmationDatePickerOpened = false;
			if(self.currentImport.dutyConfirmationDate) {
				self.currentDutyConfirmationDate = $rootScope.convertStringToDateWithSlashFormat(self.currentImport.dutyConfirmationDate);
			} else {
				self.currentDutyConfirmationDate = null;
			}
		};

		/**
		 * Open the FreightConfirmed picker to select a new date.
		 */
		self.openFreightConfirmationDatePicker = function(){
			self.freightConfirmationDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Sets the current freight confirmation date.
		 */
		self.setDateForFreightConfirmationDatePicker = function(){
			self.freightConfirmationDatePickerOpened = false;
			if(self.currentImport.freightConfirmationDate) {
				self.currentFreightConfirmationDate = $rootScope.convertStringToDateWithSlashFormat(self.currentImport.freightConfirmationDate);
			} else {
				self.currentFreightConfirmationDate = null;
			}
		};

		/**
		 * Calls all the set date picker functions.
		 */
		self.setDatePickers = function(){
			self.setDateForDutyConfirmationDatePicker();
			self.setDateForFreightConfirmationDatePicker();
			self.setDateForInStoreDatePicker();
			self.setDateForProrationDatePicker();
			self.setDateForWarehouseFlushDatePicker();
		};

		/**
		 * This method to validation data before call save.
		 */
		self.validation = function (item) {
			var errorMessage = [];
			var regexAgentCommissionPercent = new RegExp("^[0-9]{1,5}(\.[0-9]{0,2})?$");

			if(item.agentCommissionPercent === undefined){
				errorMessage.push(AGENT_VALIDATION);
			}else{
				if(item.agentCommissionPercent && !regexAgentCommissionPercent.test(item.agentCommissionPercent)){
					errorMessage.push(AGENT_VALIDATION);
				}
			}
			if(item.dutyPercent === undefined){
				errorMessage.push(DUTY_VALIDATION);
			}else{
				if(item.dutyPercent && !regexAgentCommissionPercent.test(item.dutyPercent)){
					errorMessage.push(DUTY_VALIDATION);
				}
			}
			return errorMessage;
		};

		/**
		 * Calls back end to update the import item if there's a difference.
		 */
		self.update = function () {
			self.error = null;
			self.success = null;
			if(self.isDifference()){
				var errorMessage = self.validation(self.currentImport);
				if(errorMessage == null || errorMessage == ''){
					self.isLoading = true;
					var importItem = angular.copy(self.currentImport);
					productCasePackApi.updateImportItem(importItem, self.loadNewData, self.fetchError);
				}else{
					self.error = 'Case Pack - Import:';
					angular.forEach(errorMessage, function (value) {
						self.error += "<li>" +value + "</li>";
					});
				}
			} else {
				self.error = NO_CHANGE_UPDATE_MESSAGE;
			}
		};

		/**
		 * Resets import item to original information.
		 */
		self.reset = function () {
			self.error = null;
			self.success = null;
			self.currentImport = angular.copy(self.originalImport);
			self.setDatePickers();
			self.findContainerSize();
			self.findCountry();
			self.findIncoTerms();
			self.casepackImportForm.$setPristine();
			self.casepackImportForm.$setUntouched();
		};

		/**
		 * Callback for a successful call to get vendor import data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadNewData = function (results) {
			self.success = results.message;
			self.getData()
		};

		/**
		 * Converts datepicker dates to local date format, and assigns to current import.
		 */
		self.convertAllDates = function(){
			if(self.currentInStoreDate != null) {
				self.currentImport.inStoreDate = $rootScope.convertDateWithFullYear(self.currentInStoreDate);
			}
			if(self.currentProrationDate != null){
				self.currentImport.prorationDate = $rootScope.convertDateWithFullYear(self.currentProrationDate);
			}
			if(self.currentWarehouseFlushDate != null){
				self.currentImport.warehouseFlushDate = $rootScope.convertDateWithFullYear(self.currentWarehouseFlushDate);
			}
			if(self.currentDutyConfirmationDate != null){
				self.currentImport.dutyConfirmationDate = $rootScope.convertDateWithFullYear(self.currentDutyConfirmationDate);
			}
			if(self.currentFreightConfirmationDate != null){
				self.currentImport.freightConfirmationDate = $rootScope.convertDateWithFullYear(self.currentFreightConfirmationDate);
			}
		};

		/**
		 * Returns true if there's been a change made.
		 * @returns {boolean}
		 */
		self.isDifference = function(){
			self.convertAllDates();
			var isDifference = (angular.toJson(self.currentImport) !== angular.toJson(self.originalImport));
			if (isDifference) {
				$rootScope.contentChangedFlag = true;
			} else {
				$rootScope.contentChangedFlag = false;
			}
			return isDifference;
		};

		/**
		 * This method will unlock the save button when changes are present and inform the user if they try to leave
		 * before leaving
		 * @returns {boolean}
		 */
		self.isSaveDisabled = function () {
			if(angular.toJson(self.currentImport) !== angular.toJson(self.originalImport)){
				$rootScope.contentChangedFlag = true;
				return false;
			} else {
				$rootScope.contentChangedFlag = false;
				return true;
			}
		};

		/**
		 * Returns all factories.
		 */
		self.findAllFactories = function(){
			self.isLoadingFactory = true;
			productCasePackApi.findAllFactories({}, self.loadFactoryData, self.fetchError);

		};

		/**
		 * handle after saving import factory successfully.
		 */
		self.loadVendorFactoryAfterSave = function(results){
			self.success = results.message;
			self.vendorList[self.index].vendorItemFactory = results.data.vendorItemFactory;
			self.currentImport.vendorItemFactory = angular.copy(self.vendorList[self.index].vendorItemFactory);
			self.originalImport.vendorItemFactory = angular.copy(self.vendorList[self.index].vendorItemFactory);
			self.resetFactories();
			self.isLoading = false;
		};

		/**
		 * Saves the factory data, updates switches to add checks to factories, sets the ng table up.
		 * @param results
		 */
		self.loadFactoryData = function(results){
			self.factoryList = angular.copy(results);
			//self.originalFactoryList = angular.copy(results);
			self.updateFactorySwitches(results);
			self.isLoadingFactory = false;
			self.factoryTableParams = new ngTableParams(
				{
					page: 1,
					count:10,
					sorting: {activeSwitch: "asc"}
				}, {
					counts: [],
					data: results
				}
			);
			self.updateFactoryList();

		};

		/**
		 * Loads the 1st pages.
		 */
		self.updateFactoryList = function(){
			if(null!=self.factoryTableParams){
				self.factoryTableParams.page(1);
				self.factoryTableParams.reload();
			}
		};

		/**
		 * Sets current factories in the vendor import item to true.
		 * @param factoryList
		 */
		self.updateFactorySwitches = function(factoryList){
			if(self.currentImport && self.currentImport.vendorItemFactory && factoryList) {
				for (var x = 0; x < self.currentImport.vendorItemFactory.length; x++) {
					for (var y = 0; y < factoryList.length; y++) {
						if (self.currentImport.vendorItemFactory[x].key.factoryId === factoryList[y].factoryId) {
							factoryList[y].activeSwitch = true;
							break;
						}
					}
				}
			}
		};

		/**
		 * resets the factory list to original data.
		 */
		self.resetFactories = function(){
			self.loadFactoryData(self.factoryList);
		};

		/**
		 * Gets new factories into import item, and updates the entity.
		 */
		self.saveFactories = function(){
			self.error = null;
			self.success = null;
			self.isLoading = true;
			var importItem =  self.getNewFactoriesList();
			productCasePackApi.updateVendorItemFactories(importItem, self.loadVendorFactoryAfterSave, self.fetchError)
		};

		/**
		 * check if there is any change on Import Item Factory.
		 * @returns {boolean}
		 */
		self.checkImportFactoryDifferent = function () {
			if(self.isDisabledImportFactory){
				return false;
			}
			if(self.factoryTableParams ==null || typeof self.currentImport === "undefined"){
				return false;
			}
			var factoryList = self.factoryTableParams.settings().data;
			var newImport = angular.copy(self.currentImport);
			var checkedFactoryList = [];
			if(factoryList!=null && factoryList.length > 0){
				for(var x = 0; x < factoryList.length; x++){
					if(factoryList[x].activeSwitch != null && factoryList[x].activeSwitch){
						checkedFactoryList.push(factoryList[x].factoryId);
					}
				}
			}

			var currentCheckedFactoryList = [];

			if(self.currentImport){
				if(self.currentImport.vendorItemFactory !=null && self.currentImport.vendorItemFactory.length > 0){
					for (var x = 0; x < self.currentImport.vendorItemFactory.length; x++) {
						currentCheckedFactoryList.push(self.currentImport.vendorItemFactory[x].factory.factoryId);
					}
				}
			}

			return checkedFactoryList.join(",") !== currentCheckedFactoryList.join(",");
		};

		/**
		 * Returns a new import item with the new factory list.
		 */
		self.getNewFactoriesList = function() {
			var factoryList = self.factoryTableParams.settings().data;
			var newImport = angular.copy(self.currentImport);
			var checkedFactoryList = [];

			for(var x = 0; x < factoryList.length; x++){
				if(factoryList[x].activeSwitch != null && factoryList[x].activeSwitch){
					checkedFactoryList.push(factoryList[x].factoryId);
				}
			}

			newImport.vendorItemFactory = [];
			var newVendorItemFactory ={};
			newVendorItemFactory.key = {};
			newVendorItemFactory.key.itemType = newImport.key.itemType;
			newVendorItemFactory.key.itemCode = newImport.key.itemCode;
			newVendorItemFactory.key.vendorType = newImport.key.vendorType;
			newVendorItemFactory.key.vendorNumber = newImport.key.vendorNumber;

			for(var x = 0; x < checkedFactoryList.length; x++){
				var tempItemFactory = angular.copy(newVendorItemFactory);
				tempItemFactory.key.factoryId = checkedFactoryList[x];
				newImport.vendorItemFactory.push(tempItemFactory);
			}

			return newImport;
		};

		self.showImportAuditInfo = function () {
			var title ="Case Pack Import";
			self.getImportAuditInfo=productCasePackApi.getImportAuditInformation;
			ProductDetailAuditModal.open(self.getImportAuditInfo, self.currentImport.key, title);
		};

		/**
		 * Returns whether or not the user is allowed to edit Import Factory Section.
		 *
		 * @returns {boolean} Whether or not the user is allowed to edit Import Factory Section.
		 */
		self.canEditImportFactory = function() {
			var isEdit = false;
			if(permissionsService.getPermissions("CP_IMPRT_02", "EDIT")){
				isEdit = true;
				if(self.isDisabledImportFactory){
					isEdit = false;
				}
			}
			return isEdit;
		};

		/**
		 * Close the import factory popup.
		 */
		self.closeImportFactoryPopup = function() {
			self.resetFactories();
			self.factoryTableParams.filter({});
		};
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};

		/**
		 * Returns the added date, or null value if date doesn't exist.
		 */
		self.getAddedDate = function() {
			if(self.itemMaster.createdDateTime === null || angular.isUndefined(self.itemMaster.createdDateTime)) {
				return '01/01/1901 00:00';
			} else if (parseInt(self.itemMaster.createdDateTime.substring(0, 4)) < 1900) {
				return '01/01/0001 00:00';
			} else {
				return $filter('date')(self.itemMaster.createdDateTime, 'MM/dd/yyyy HH:mm');
			}
		};

		/**
		 * Returns createUser or '' if not present.
		 */
		self.getCreateUser = function() {
			if(self.itemMaster.displayCreatedName === null || self.itemMaster.displayCreatedName.trim().length == 0) {
				return '';
			}
			return self.itemMaster.displayCreatedName;
		};
	}
})();
