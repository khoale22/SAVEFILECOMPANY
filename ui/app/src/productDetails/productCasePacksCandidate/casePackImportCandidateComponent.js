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

	angular.module('productMaintenanceUiApp').component('casePackImportCandidate', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			psItemMasters: '<',
			onCandidateTabChange : '&'
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacksCandidate/casePackImportCandidate.html',
		// The controller that handles our component logic
		controller: CasePackImportCandidateController
	});

	CasePackImportCandidateController.$inject = ['ProductFactory','PMCommons','$scope', 'ProductCasePackCandidateApi', '$timeout', 'ngTableParams', '$filter', 'ProductDetailAuditModal', 'PermissionsService', '$rootScope', 'ProductSearchService', 'HomeSharedService'];

	/**
	 * Case Pack Import component's controller definition.
	 * @param $scope	scope of the case pack info component.
	 * @constructor
	 */
	function CasePackImportCandidateController(productFactory, PMCommons, $scope, productCasePackCandidateApi, $timeout, ngTableParams, $filter, ProductDetailAuditModal, permissionsService, $rootScope, productSearchService, homeSharedService) {
		/** All CRUD operation controls of Case pack Import page goes here */
		var self = this;

		/**
		 * String constants.
		 */
		self.YES = "Yes";
		self.NO = "No";
		self.REJECT_CANDIDATE_CONFIRMATION_TITLE = "Reject Confirmation";
		self.REJECT_CANDIDATE_ACTION = "RC";
		self.REJECT_CANDIDATE_MESSAGE = "Are you sure you want to reject the newly created Item ?";
		self.SEARCH_PRODUCT_EVENT = "searchProduct";
		self.NO_CHANGE_UPDATE_MESSAGE = "There are no changes on this page to be saved. Please make any changes to update.";

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
		 * This flag is set so that when the item isn't an MRT not of the fields will be editable.
		 * @type {boolean}
		 */
		self.isImportItem=false;

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
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
		};

		this.$onChanges= function (){
			//self.isLoading = true;
			self.isDataLoaded = false;
			self.isCountryLoaded = false;
			self.isContainerSizeLoaded = false;
			self.isIncoTermsLoaded = false;
			self.index = 0;
			$rootScope.contentChangedFlag = true;
			self.getData();
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};

		/**
		 * Gets the bicep vendor import data.
		 */
		self.getData = function(){
			self.isLoading = true;
			productCasePackCandidateApi.getCandidateInformation({psItmId: self.psItemMasters[0].candidateItemId}, self.loadData, self.fetchError);
		};

		/**
		 * Gets the list of countries.
		 */
		self.getCountries = function(){
			productCasePackCandidateApi.findAllCountriesOrderByName({}, self.loadCountryData, self.fetchError)
		};

		/**
		 * Gets the list of container sizes.
		 */
		self.getContainerSizes = function(){
			productCasePackCandidateApi.findAllImportContainerSizes({}, self.loadContainerSizes, self.fetchError)
		};

		/**
		 * Gets the list of inco terms.
		 */
		self.getIncoTerms = function(){
			productCasePackCandidateApi.findAllIncoTerms({}, self.loadIncoTerms, self.fetchError)
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
			self.isLoading = false;
			self.psItemMasters = results;
			var candidateVendorLocationItems = angular.copy(results[0].candidateVendorLocationItems);

			self.isImportItem = !(candidateVendorLocationItems.length === 0);
			if (self.isImportItem) {
				var dataResults = self.handleResultsDataBeforeAssign(results);//List<ImportItem>
				self.currentImport = angular.copy(dataResults[self.index]);
				self.originalImport = angular.copy(dataResults[self.index]);
				self.vendorList = dataResults;
				self.setDatePickers();
				self.getCountries();
				self.getContainerSizes();
				self.getIncoTerms();
				self.findAllFactories();
				//self.updateLoadingStatus();
			}else{
				self.isLoading = false;
			}
			self.isDataLoaded = true;

		};

		self.loadDataAfterSave = function (results) {
			self.isLoading = false;
			self.psItemMasters = results;
			var candidateVendorLocationItems = angular.copy(results[0].candidateVendorLocationItems);

			self.isImportItem = !(candidateVendorLocationItems.length === 0);
			if (self.isImportItem) {
				var dataResults = self.handleResultsDataBeforeAssign(results);//List<ImportItem>
				self.currentImport = angular.copy(dataResults[self.index]);
				self.originalImport = angular.copy(dataResults[self.index]);
				self.vendorList = dataResults;

				self.setDatePickers();
				self.findCountry();
				self.findContainerSize();
				self.findIncoTerms();
				self.resetFactories();
			}
		};

		/**
		 * Convert data from CandidateVendorLocationItem to ImportItem before use.
		 *
		 * @param results The data returned by the backend.
		 *
		 * @return the processed data.
		 */
		self.handleResultsDataBeforeAssign = function(psItemMasters){
			//Old: List<ImportItem>
			//New: List<CandidateVendorLocationItem>
			var candidateVendorLocationItems = angular.copy(psItemMasters[0].candidateVendorLocationItems);
			var importItems = [];

			angular.forEach(candidateVendorLocationItems, function(candidateVendorLocationItem){
				var item = {};

				if(null!= candidateVendorLocationItem.color){
					item.color = candidateVendorLocationItem.color.trim();
				}
				if(null!= candidateVendorLocationItem.cartonMarking){
					item.cartonMarking = candidateVendorLocationItem.cartonMarking.trim();
				}
				if(null!= candidateVendorLocationItem.dutyInfoText){
					item.dutyInformation = candidateVendorLocationItem.dutyInfoText.trim();
				}

				item.inStoreDate = candidateVendorLocationItem.inStoreDate;
				item.prorationDate = candidateVendorLocationItem.prorationDate;
				item.warehouseFlushDate = candidateVendorLocationItem.warehouseFlushDate;
				item.dutyConfirmationDate = candidateVendorLocationItem.dutyConfirmationText;
				item.freightConfirmationDate = candidateVendorLocationItem.freightConfirmationText;

				item.displayName = candidateVendorLocationItem.displayName;
				item.hts1 = candidateVendorLocationItem.htsNumber;
				item.hts2 = candidateVendorLocationItem.hts2Number;
				item.hts3 = candidateVendorLocationItem.hts3Number;
				item.minOrderDescription = candidateVendorLocationItem.minTypeText;
				item.minOrderQuantity = candidateVendorLocationItem.minOrderQuantity;
				item.pickupPoint = candidateVendorLocationItem.pickupPoint;

				item.countryOfOrigin = candidateVendorLocationItem.country.countryName;
				if(candidateVendorLocationItem.containerSizeCode != null){
					item.containerSizeCode = candidateVendorLocationItem.containerSizeCode;
				}else{
					item.containerSizeCode = "";
				}
				item.incoTermCode = candidateVendorLocationItem.incoTermCode;

				item.season = candidateVendorLocationItem.season;
				item.sellByYear = candidateVendorLocationItem.sellByYear;
				item.agentCommissionPercent = candidateVendorLocationItem.agentCommissionPercent;
				item.dutyPercent = candidateVendorLocationItem.dutyPercent;

				item.newDataSw = candidateVendorLocationItem.newData;

				item.key = {};
				item.key.itemCode = psItemMasters[0].itemCode;
				item.key.itemType = psItemMasters[0].itemKeyType;
				item.key.vendorType = candidateVendorLocationItem.key.vendorType;
				item.key.vendorNumber = candidateVendorLocationItem.key.vendorNumber;

				//Old: List<VendorItemFactory>
				//New: List<CandidateVendorItemFactory>
				var tempVendorItemFactory = [];
				angular.forEach(candidateVendorLocationItem.candidateVendorItemFactorys, function(candidateVendorItemFactory){
					var temp = {};
					temp.factory = {};
					if(candidateVendorItemFactory.factory != null){
						temp.factory = candidateVendorItemFactory.factory;
					}
					temp.lastUpdatedUserID = candidateVendorItemFactory.lastUpdateUserId;
					temp.lastUpdatedTimeStamp = candidateVendorItemFactory.lastUpdateTs;
					temp.key = {};
					temp.key.factoryId = candidateVendorItemFactory.key.factoryId;
					temp.key.vendorType = candidateVendorItemFactory.key.vendorType;
					temp.key.vendorNumber = candidateVendorItemFactory.key.vendorNumber;
					tempVendorItemFactory.push(temp);
				});
				item.vendorItemFactory = angular.copy(tempVendorItemFactory);

				importItems.push(item);
			});

			return importItems;
		}

		/**
		 * Convert data from ImportItem to CandidateVendorLocationItem before save.
		 *
		 * @return the CandidateVendorLocationItem.
		 */
		self.getCandidateVendorLocationItemData = function(){
			//Old: List<ImportItem>
			//New: List<CandidateVendorLocationItem>
			var tempImportItem = angular.copy(self.currentImport);
			var candidateVendorLocationItem = angular.copy(self.psItemMasters[0].candidateVendorLocationItems[self.index]);

			candidateVendorLocationItem.prorationDate = tempImportItem.prorationDate;
			candidateVendorLocationItem.inStoreDate = tempImportItem.inStoreDate;
			candidateVendorLocationItem.warehouseFlushDate = tempImportItem.warehouseFlushDate;
			candidateVendorLocationItem.dutyConfirmationText = tempImportItem.dutyConfirmationDate;
			candidateVendorLocationItem.freightConfirmationText = tempImportItem.freightConfirmationDate;

			candidateVendorLocationItem.htsNumber = tempImportItem.hts1;
			candidateVendorLocationItem.hts2Number = tempImportItem.hts2;
			candidateVendorLocationItem.hts3Number = tempImportItem.hts3;
			candidateVendorLocationItem.minTypeText = tempImportItem.minOrderDescription;
			candidateVendorLocationItem.minOrderQuantity = tempImportItem.minOrderQuantity;
			candidateVendorLocationItem.pickupPoint = tempImportItem.pickupPoint;

			candidateVendorLocationItem.season = tempImportItem.season;
			candidateVendorLocationItem.sellByYear = tempImportItem.sellByYear;
			candidateVendorLocationItem.color = tempImportItem.color;
			candidateVendorLocationItem.cartonMarking = tempImportItem.cartonMarking;
			candidateVendorLocationItem.agentCommissionPercent = tempImportItem.agentCommissionPercent;
			candidateVendorLocationItem.dutyPercent = tempImportItem.dutyPercent;
			candidateVendorLocationItem.dutyInfoText = tempImportItem.dutyInformation;

			candidateVendorLocationItem.containerSizeCode = tempImportItem.containerSizeCode;
			candidateVendorLocationItem.incoTermCode = tempImportItem.incoTermCode;
			if(self.currentCountry != null){
				candidateVendorLocationItem.countryOfOriginId = self.currentCountry.countryId;
			}
			return candidateVendorLocationItem;
		}

		/**
		 * Returns true if newDataSw is N.
		 * @returns {boolean}
		 */
		self.isDisabledMode = function(){
			var check = false;
			if(self.currentImport != null && !self.currentImport.newDataSw){
				check = true;
			}
			return check;
		};

		/**
		 * Callback for a successful call to get country data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadCountryData = function (results) {
			self.countryList = [];
			if (!(results.length === 0)) {
				self.countryList = results;
			}
			self.findCountry();
			self.isCountryLoaded = true;
			//self.updateLoadingStatus();
		};

		/**
		 * Callback for a successful call to get country data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadContainerSizes = function (results) {
			self.containerSizes = [];
			if (!(results.length === 0)) {
				self.containerSizes = results;
			}
			self.findContainerSize();
			self.isContainerSizeLoaded = true;
			//self.updateLoadingStatus();
		};

		/**
		 * Callback for a successful call to get country data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadIncoTerms = function (results) {
			self.incoTerms = [];
			if (!(results.length === 0)) {
				self.incoTerms = results;
			}
			self.findIncoTerms();
			self.isIncoTermsLoaded = true;
			//self.updateLoadingStatus();
		};

		/**
		 * Sets the current country;
		 */
		self.findCountry = function(){
			$timeout(function(){
				$scope.$evalAsync(function() {
					if(self.currentImport.countryOfOrigin != null){
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
					if(self.currentImport.containerSizeCode != null){
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
					if(self.currentImport.incoTermCode != null){
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
			self.currentImport = angular.copy(self.vendorList[index]);
			self.originalImport = angular.copy(self.vendorList[index]);
			self.index = index;

			self.setDatePickers();
			self.findCountry();
			self.findContainerSize();
			self.findIncoTerms();
			self.resetFactories();
		};

		/**
		 * Handles changing the country on the drop down.
		 * @param country
		 */
		self.handleCountryListChange = function(country){
			self.currentCountry = country;
		};

		/**
		 * Handles changing the container size on the drop down.
		 * @param size
		 */
		self.handleContainerSizeListChange = function(size){
			self.currentContainerSize = size;
		};

		/**
		 * Handles changing the inco terms on the drop down.
		 * @param incoTerms
		 */
		self.handleIncoTermsListChange = function(incoTerms){
			self.currentIncoTerms = incoTerms;
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
			if(self.currentImport.prorationDate != null) {
				self.currentProrationDate =
					new Date(self.currentImport.prorationDate.replace(/-/g, '\/'));
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
			if(self.currentImport.inStoreDate != null) {
				self.currentInStoreDate =
					new Date(self.currentImport.inStoreDate.replace(/-/g, '\/'));
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
			if(self.currentImport.warehouseFlushDate != null) {
				self.currentWarehouseFlushDate =
					new Date(self.currentImport.warehouseFlushDate.replace(/-/g, '\/'));
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
			if(self.currentImport.dutyConfirmationDate != null) {
				self.currentDutyConfirmationDate =
					new Date(self.currentImport.dutyConfirmationDate.replace(/-/g, '\/'));
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
			if(self.currentImport.freightConfirmationDate != null) {
				self.currentFreightConfirmationDate =
					new Date(self.currentImport.freightConfirmationDate.replace(/-/g, '\/'));
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
			var regexInt = new RegExp("^[0-9]{1,10}?$");
			var regexSellByYear = new RegExp("^[0-9]{1,10}?$");
			var regexMinOrderQuantity = new RegExp("^[0-9]{1,7}?$");
			var regexAgentCommissionPercent = new RegExp("^[0-9]{1,5}(\.[0-9]{0,2})?$");

			if(item.hts1 === undefined || item.hts1 === null){
				errorMessage.push("HTS1 is a mandatory field, greater than or equal to 0 and less than or equal to 9999999999");
			}else{
				if(item.hts1 && !regexInt.test(item.hts1)){
					errorMessage.push("HTS1 is a mandatory field, greater than or equal to 0 and less than or equal to 9999999999");
				}
			}
			if(item.hts2 === undefined){
				errorMessage.push("HTS2 value must be greater than or equal to 0 and less than or equal to 9999999999");
			}else{
				if(item.hts2 && !regexInt.test(item.hts2)){
					errorMessage.push("HTS2 value must be greater than or equal to 0 and less than or equal to 9999999999");
				}
			}
			if(item.hts3 === undefined){
				errorMessage.push("HTS3 value must be greater than or equal to 0 and less than or equal to 9999999999");
			}else{
				if(item.hts3 && !regexInt.test(item.hts3)){
					errorMessage.push("HTS3 value must be greater than or equal to 0 and less than or equal to 9999999999");
				}
			}
			if(item.minOrderDescription === undefined || item.minOrderDescription === null){
				errorMessage.push("Min. Order Description is a mandatory field");
			}
			if(item.minOrderQuantity === undefined || item.minOrderQuantity === null){
				errorMessage.push("Min. Order Quantity is a mandatory field, greater than or equal to 0 and less than or equal to 9999999");
			}else{
				if(item.minOrderQuantity && !regexMinOrderQuantity.test(item.minOrderQuantity)){
					errorMessage.push("Min. Order Quantity is a mandatory field, greater than or equal to 0 and less than or equal to 9999999");
				}
			}
			if(item.pickupPoint === undefined || item.pickupPoint === null){
				errorMessage.push("Pickup Point is a mandatory field");
			}
			if(item.sellByYear === undefined || item.sellByYear === null){
				errorMessage.push("Sell By is a mandatory field, greater than or equal to 0 and less than or equal to 999999999");
			}else{
				if(item.sellByYear && !regexSellByYear.test(item.sellByYear)){
					errorMessage.push("Sell By is a mandatory field, greater than or equal to 0 and less than or equal to 999999999");
				}
			}
			if(item.color === undefined || item.color === null){
				errorMessage.push("Color is a mandatory field");
			}
			if(item.cartonMarking === undefined || item.cartonMarking === null){
				errorMessage.push("Carton Marking is a mandatory field");
			}
			if(item.agentCommissionPercent === undefined || item.agentCommissionPercent === null){
				errorMessage.push("Agent % is a mandatory field, greater than or equal to 0 and less than or equal to 99999.99");
			}else{
				if(item.agentCommissionPercent && !regexAgentCommissionPercent.test(item.agentCommissionPercent)){
					errorMessage.push("Agent % is a mandatory field, greater than or equal to 0 and less than or equal to 99999.99");
				}
			}
			if(item.dutyPercent === undefined || item.dutyPercent === null){
				errorMessage.push("Duty % is a mandatory field, greater than or equal to 0 and less than or equal to 99999.99");
			}else{
				if(item.dutyPercent && !regexAgentCommissionPercent.test(item.dutyPercent)){
					errorMessage.push("Duty % is a mandatory field, greater than or equal to 0 and less than or equal to 99999.99");
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
					var candidateVendorLocationItem = self.getCandidateVendorLocationItemData();
					productCasePackCandidateApi.updateCasePackImportCandidate(candidateVendorLocationItem, self.loadNewData, self.fetchError);
				}else{
					self.error = 'Case Pack - Import:';
					angular.forEach(errorMessage, function (value) {
						self.error += "<li>" +value + "</li>";
					});
				}
			} else {
				self.error = self.NO_CHANGE_UPDATE_MESSAGE;
			}
		};

		/**
		 * Resets import item to original information.
		 */
		self.reset = function () {
			self.error = false;
			self.success = false;
			self.currentImport = angular.copy(self.originalImport);
			self.setDatePickers();
			self.findContainerSize();
			self.findCountry();
			self.findIncoTerms();
		};

		/**
		 * Callback for a successful call to get vendor import data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadNewData = function (results) {
			if (!(results.data.length === 0)) {
				self.success = results.message;
				self.loadDataAfterSave(results.data);
				//self.getData()
			}
		};

		/**
		 * Converts datepicker dates to local date format, and assigns to current import.
		 */
		self.convertAllDates = function(){
			if(self.currentInStoreDate != null) {
				self.currentImport.inStoreDate = self.convertDate(self.currentInStoreDate);
			}
			if(self.currentProrationDate != null){
				self.currentImport.prorationDate = self.convertDate(self.currentProrationDate);
			}
			if(self.currentWarehouseFlushDate != null){
				self.currentImport.warehouseFlushDate = self.convertDate(self.currentWarehouseFlushDate);
			}
			if(self.currentDutyConfirmationDate != null){
				self.currentImport.dutyConfirmationDate = self.convertDate(self.currentDutyConfirmationDate);
			}
			if(self.currentFreightConfirmationDate != null){
				self.currentImport.freightConfirmationDate = self.convertDate(self.currentFreightConfirmationDate);
			}
		};


		/**
		 * Returns true if there's been a change made.
		 * @returns {boolean}
		 */
		self.isDifference = function(){
			self.convertAllDates();
			if(self.currentImport.countryOfOrigin.trim() != self.currentCountry.countryName.trim())
			{
				self.currentImport.countryOfOrigin = self.currentCountry.countryName;
			}
			if(self.currentContainerSize != null && self.currentImport.containerSizeCode.trim() != self.currentContainerSize.trim()) {
				self.currentImport.containerSizeCode = self.currentContainerSize;
			}
			if(self.currentIncoTerms != null && self.currentImport.incoTermCode.trim() != self.currentIncoTerms.trim()) {
				self.currentImport.incoTermCode = self.currentIncoTerms;
			}
			return angular.toJson(self.currentImport) != angular.toJson(self.originalImport);
		};

		/**
		 * Returns all factories.
		 */
		self.findAllFactories = function(){
			self.isLoadingFactory = true;
			productCasePackCandidateApi.findAllFactories({}, self.loadFactoryData, self.fetchError);

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
			if(self.currentImport != null && factoryList != null) {
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
			self.isLoading = true;
			self.error = null;
			self.success = null;
			var candidateVendorLocationItem = self.getCandidateVendorLocationItemData();
			candidateVendorLocationItem.candidateVendorItemFactorys = self.getNewFactoriesList();
			productCasePackCandidateApi.updateCasePackImportFactoriesCandidate(candidateVendorLocationItem, self.loadNewData, self.fetchError)
		};

		/**
		 * check if there is any change on Import Item Factory.
		 * @returns {boolean}
		 */
		self.checkImportFactoryDifferent = function () {
			if(self.factoryTableParams ==null) return false;
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

			for (var x = 0; x < self.currentImport.vendorItemFactory.length; x++) {
				currentCheckedFactoryList.push(self.currentImport.vendorItemFactory[x].factory.factoryId);
			}

			return checkedFactoryList.join(",") != currentCheckedFactoryList.join(",");
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

			var candidateVendorItemFactorys = [];
			var candidateVendorItemFactory = {};
			candidateVendorItemFactory.key = {};
			candidateVendorItemFactory.key.psItmId = self.psItemMasters[0].candidateItemId;
			candidateVendorItemFactory.key.vendorType = newImport.key.vendorType;
			candidateVendorItemFactory.key.vendorNumber = newImport.key.vendorNumber;

			for(var x = 0; x < checkedFactoryList.length; x++){
				var tempCandidateVendorItemFactory = angular.copy(candidateVendorItemFactory);
				tempCandidateVendorItemFactory.key.factoryId = checkedFactoryList[x];
				candidateVendorItemFactorys.push(tempCandidateVendorItemFactory);
			}

			return candidateVendorItemFactorys;
		};

		self.showImportAuditInfo = function () {
			var title ="Case Pack Import";
			self.getImportAuditInfo=productCasePackCandidateApi.getImportAuditInformation;
			ProductDetailAuditModal.open(self.getImportAuditInfo, self.currentImport.key, title);
		}

		/**
		 * Returns whether or not the user is allowed to edit Import Factory Section.
		 *
		 * @returns {boolean} Whether or not the user is allowed to edit Import Factory Section.
		 */
		self.canEditImportFactory = function() {
			return permissionsService.getPermissions("CP_IMPRT_02", "EDIT");
		};

		/**
		 * User click yes on confirm message popup.
		 */
		self.yesConfirmAction = function () {
			$('#importRejectCandidateModal').modal('hide');
			if(self.action == self.REJECT_CANDIDATE_ACTION){
				self.rejectCandidate();
			}
			self.action = null;
		};

		/**
		 * User click no on confirm message popup.
		 */
		self.noConfirmAction = function () {
			if(self.action == self.REJECT_CANDIDATE_ACTION){

			}
			self.action = null;
		};

		/**
		 * Show Reject Candidate popup.
		 */
		self.confirmRejectCandidate = function () {
			self.error = null;
			self.success = null;
			self.headerTitleConfirm = self.REJECT_CANDIDATE_CONFIRMATION_TITLE;
			self.messageConfirm = self.REJECT_CANDIDATE_MESSAGE;
			self.action = self.REJECT_CANDIDATE_ACTION;
			self.yesBtnLabel = self.YES;
			self.closeBtnLabel = self.NO;
			self.yesBtnEnable = true;
			$('#importRejectCandidateModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Reject case pack in candidate mode.
		 */
		self.rejectCandidate = function () {
			self.isLoading = true;
			self.error = null;
			self.success = null;
			productCasePackCandidateApi.rejectCandidate({psWorkId: self.psItemMasters[0].candidateWorkRequestId},
			function (result) {
				self.isLoading = false;
				self.success = result.message;
				productSearchService.setActivateCandidateProductMessage(result.message);
				homeSharedService.broadcastNavigateTabAndUpdateProduct("productInfoTab");
			}, self.fetchError);
		};

		/**
		 * Activate case pack in candidate mode.
		 */
		self.activateCandidate = function () {
			self.isLoading = true;
			self.error = null;
			self.success = null;
			productCasePackCandidateApi.activateCandidate({psWorkId: self.psItemMasters[0].candidateWorkRequestId},
					function (result) {
				self.isLoading = false;
				self.success = result.message;
				productSearchService.setActivateCandidateProductMessage(result.message);
				homeSharedService.broadcastNavigateTabAndUpdateProduct("productInfoTab");
			}, self.fetchError);
		};

		/**
		 * Change to back case pack tab in candidate mode.
		 */
		self.backCandidate = function () {
			self.error = null;
			self.success = null;
			if(self.isDifference()){
				var errorMessage = self.validation(self.currentImport);
				if(errorMessage == null || errorMessage == ''){
					self.isLoading = true;
					var candidateVendorLocationItem = self.getCandidateVendorLocationItemData();
					productCasePackCandidateApi.updateCasePackImportCandidate(candidateVendorLocationItem,
							function (results) {
								self.isLoading = false;
								self.success = results.message;
								self.onCandidateTabChange({tabId: "vendorInfoTab"});
							},
							self.fetchError);
				}else{
					self.error = 'Case Pack - Import:';
					angular.forEach(errorMessage, function (value) {
						self.error += "<li>" +value + "</li>";
					});
				}
			} else {
				self.onCandidateTabChange({tabId: "vendorInfoTab"});
			}
		};
	}
})();
