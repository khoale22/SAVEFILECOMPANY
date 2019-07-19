/*
 *   casePackInfoComponent.js
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

	angular.module('productMaintenanceUiApp').component('casePackInfoCandidate', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			sellingUnits: '<',
			psItemMasters: '<',
			onCandidateTabChange : '&'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacksCandidate/casePackInfoCandidate.html',
		// The controller that handles our component logic
		controller: CasePackInfoCandidateController
	});

	CasePackInfoCandidateController.$inject = ['ProductCasePackCandidateApi', 'ProductDetailAuditModal', '$scope', '$rootScope', 'ProductSearchService', 'HomeSharedService'];

	/**
	 * Case Pack Info component's controller definition.
	 * @constructor
	 * @param productCasePackApi
	 */
	function CasePackInfoCandidateController(productCasePackCandidateApi, ProductDetailAuditModal, $scope, $rootScope, productSearchService, homeSharedService) {
		/** All CRUD operation controls of Case pack Info page goes here */
		var self = this;

		/**
		 * String constants.
		 */
		self.SUCCESS = "success";
		self.YES = "Yes";
		self.NO = "No";
		self.CASE_PACK_INFO_TITLE = "Case Pack Info";
		self.CONFIRMATION_TITLE = "Confirmation";
		self.REJECT_CANDIDATE_CONFIRMATION_TITLE = "Reject Confirmation";
		self.DISCONTINUE_TITLE = "Discontinue";
		self.REACTIVATE_ACTION = "RA";
		self.DISCONTINUEDATE_ACTION = "DD";
		self.REJECT_CANDIDATE_ACTION = "RC";
		self.REACTIVATE_MESSAGE = "Are you sure to re-activate this item ?";
		self.REJECT_CANDIDATE_MESSAGE = "Are you sure you want to reject the newly created Item ?";
		self.DISCONTINUE_MESSAGE = "Do you really want to discontinue this item ?";
		self.REACTIVATE_SUCCESS_MESSAGE = "Saved successfully. Use DSDS for authorizing stores to this item.";
		self.NO_CHANGE_UPDATE_MESSAGE = "There are no changes on this page to be saved. Please make any changes to update.";
		self.UNKNOWN_ERROR_MESSAGE = "An unknown error occurred.";
		self.ERROR_MESSAGE = "Error.";
		self.INCORRECT_MESSAGE = "Incorrect.";
		self.EDIT_PRIMARY_UPC_TITLE = "Edit Product Primary UPC";
		self.EDIT_PRIMARY_UPC_MESSAGE = "Are you sure you want to set \"{0}\" UPC as Product Primary UPC?";
		self.EDIT_PRIMARY_UPC_ACTION = "ED_PRIMARY_UPC";
		self.WAREHOUSE = "ITMCD";
		self.SEARCH_PRODUCT_EVENT = "searchProduct";

		/**
		 * The check digit is correct on load because it comes back as a calculation when the page is loaded.
		 *
		 * @type {string}
		 */
		self.checkDigitMessage = 'Correct';

		/**
		 * The check digit is intially correct on load. This is used to determine whether the page can be saved or not.
		 *
		 * @type {boolean}
		 */
		self.correctCheckDigit = true;

		/**
		 * Returns convertDate(date) function from higher scope.
		 * @type {function}
		 */
		self.convertDate = $scope.$parent.$parent.$parent.$parent.convertDate;

		/**
		 * If the discontinueDatePicker is opened.
		 * @type {boolean}
		 */
		self.discontinueDatePickerOpened = false;

		/**
		 * Current Discontinue Date.
		 * @type {date}
		 */
		self.currentDiscontinueDate = null;

		/**
		 * Boolean for whether or not dsd casepack is reactivate
		 * @type {boolean}
		 */
		self.isReActivate = false;

		/**
		 * This is product primary upc number.
		 * @type {string}
		 */
		self.productPrimaryUPC;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onChanges = function () {
			self.isLoading = true;
			self.error = null;
			self.success = null;
			productCasePackCandidateApi.getCandidateInformation({psItmId: self.psItemMasters[0].candidateItemId}, self.getData, self.fetchError);
		};

		/**
		 * This will grab the latest ItemMaster.
		 * @param results
		 */
		self.getData = function (results) {
			self.isLoading = false;
			self.psItemMasters = results;
			var psItemMaster = angular.copy(self.psItemMasters[0]);

			self.itemMaster.caseUpc = psItemMaster.caseUpc;
			self.itemMaster.key.itemCode = psItemMaster.itemCode;
			self.itemMaster.key.itemType = psItemMaster.itemKeyType;
			self.itemMaster.description = psItemMaster.itemDescription;

			self.itemMaster.discontinueDate = psItemMaster.discontinueDate;
			self.itemMaster.discontinuedByUID = psItemMaster.discontinueUserId;

			self.itemMaster.itemTypeCode = psItemMaster.candidateWorkRequest.candidateProductMaster[0].productTypeCode;

			self.itemMaster.oneTouchTypeCode = psItemMaster.oneTouchTypCode;
			if (psItemMaster.oneTouchType !== null) {
				self.itemMaster.oneTouchType.displayName = psItemMaster.oneTouchType.displayName;
			}

			self.itemMaster.warehouseLocationItems = [];
			angular.forEach(psItemMaster.candidateWarehouseLocationItems, function(candidateWarehouseLocationItem){//fix
				var item = {};
				item.key = {};
				item.key.warehouseNumber = candidateWarehouseLocationItem.key.warehouseNumber;
				item.purchasingStatusCode = {};
				item.purchasingStatusCode.description = candidateWarehouseLocationItem.purchasingStatusCode.description;
				item.purchaseStatusUpdateTime = candidateWarehouseLocationItem.lastUpdatedOn;
				item.purchaseStatusUserId = candidateWarehouseLocationItem.lastUpdatedId;
				item.supplierStatusCode = null;
				item.supplierStatusUpdateTime = candidateWarehouseLocationItem.lastUpdatedOn;
				item.warehouseLocationItemExtendedAttributes = {};
				item.warehouseLocationItemExtendedAttributes.firstReceivedDate = 0;

				self.itemMaster.warehouseLocationItems.push(item);
			});
			self.newDataSw = psItemMaster.newData;
			self.isWarehouse = (psItemMaster.itemKeyType.trim() === self.WAREHOUSE);

			self.setData();
		};

		/**
		 * Convert data from ItemMaster to PsItemMaster.
		 *
		 * @return the PsItemMaster.
		 */
		self.getPsItemMasterData = function(){
			var itemMaster = angular.copy(self.itemMaster);
			var psItemMaster = angular.copy(self.psItemMasters[0]);
			psItemMaster.caseUpc = itemMaster.caseUpc;
			psItemMaster.itemDescription = itemMaster.description;
			return psItemMaster;
		};

		/**
		 * Initialize component data.
		 */
		self.setData = function () {
			self.loadedItemMaster = angular.copy(self.itemMaster);
			// Calculates the check digit according to the case upc.
			productCasePackCandidateApi.calculateCheckDigit({'upc': self.itemMaster.caseUpc},
				self.getCheckDigit, self.fetchError);
			self.setDataDiscontinueDate();
		};

		/**
		 * Returns true if newDataSw is N.
		 * @returns {boolean}
		 */
		self.isDisabledMode = function(){
			return !self.newDataSw;
		};

		/**
		 * Returns a list of Item types.
		 */
		self.getAllItemTypes = function () {
			productCasePackCandidateApi.getAllCandidateProductTypes({}, self.loadItemTypes, self.fetchError);
		};

		/**
		 * Returns a list of one touch types.
		 */
		self.getAllOneTouchTypes = function () {
			productCasePackCandidateApi.getAllOneTouchTypes({},
				self.loadOneTouchTypes, self.fetchError);
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			console.log('CasePackInfoComp - Destroyed');
			/** Execute component destroy events if any. */
		};

		/**
		 * Callback for a successful call to get item types.
		 *
		 * @param results The data returned by the back end(list of item Types).
		 */
		self.loadItemTypes = function (results) {
			self.itemTypeList = results;
		};

		/**
		 * Callback for a successful call to calculate the check digit.
		 *
		 * @param results The data returned by the back end(check digit).
		 */
		self.getCheckDigit = function (results) {
			self.checkDigit = results.data;
			self.loadedCheckDigit = self.checkDigit;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isLoading = false;
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
			}
			else {
				return self.UNKNOWN_ERROR_MESSAGE;
			}
		};

		/**
		 * This determines whether the yellow question mark has been clicked and the check digit information
		 * modal box pops up.
		 */
		self.showCheckDigitInformation = function () {
			self.showingCheckDigitInformation = true;
		};

		/**
		 * The successful method when retrieving one touch type data.
		 *
		 * @param results the one touch type data.
		 */
		self.loadOneTouchTypes = function (results) {
			self.oneTouchTypeList = results;
		};

		/**
		 * Confirms whether or not the check digit is correct or not. Returns from the back end "Correct" if correct
		 * or "Not Correct" if it is incorrect.
		 */
		self.confirmCheckDigit = function () {
			if (self.checkDigit !== null) {
				productCasePackCandidateApi.confirmCheckDigit({
						checkDigit: self.checkDigit,
						upc: self.itemMaster.caseUpc
					},
					//success
					function (results) {
						self.checkDigitMessage = results.message;
						self.correctCheckDigit = results.data;
					},
					// error
					function () {
						self.correctCheckDigit = false;
						self.checkDigitMessage = self.ERROR_MESSAGE;
					});
			}
			else {
				self.correctCheckDigit = false;
				self.checkDigitMessage = self.INCORRECT_MESSAGE;
			}
		};

		/**
		 * Saves any changes to the item from the case pack info changes.
		 */
		self.saveCasePackInfoChanges = function () {
			self.isLoading = true;
			self.error = null;
			self.success = null;
			if(self.isDifference()){
				var psItemMaster = self.getPsItemMasterData();
				productCasePackCandidateApi.updateCasePackInfoCandidate(psItemMaster, self.reloadSavedData, self.fetchError);
			} else {
				self.error = self.NO_CHANGE_UPDATE_MESSAGE;
			}
		};

		/**
		 * Returns true if there's been a change made.
		 * @returns {boolean}
		 */
		self.isDifference = function(){
			self.convertAllDates();
			return angular.toJson(self.loadedItemMaster) !== angular.toJson(self.itemMaster)
				|| angular.toJson(self.loadedCheckDigit) !== angular.toJson(self.checkDigit);
		};

		/**
		 * After a save this reloads the succesful saved data.
		 * @param results
		 */
		self.reloadSavedData = function (results) {
			self.isLoading = false;
			//self.itemMaster = results.data;
			//self.setData();
			if (results.message !== null) {
				self.success = results.message;
			} else {
				results.message = null;
			}
			self.getData(results.data);
		};

		/**
		 * Whether or not you can save the page. If you have made changes on the page and whether or not the check digit
		 * is correct.
		 *
		 * @returns {boolean|*}
		 */
		self.canSave = function () {
			if(angular.toJson(self.loadedItemMaster) !== angular.toJson(self.itemMaster) && self.correctCheckDigit){
				$rootScope.contentChangedFlag = true;
				return true;
			}
			else{
				return false;
			}
		};

		/**
		 * When they have made some changes but they want to reset, on clicking the reset button this will reset them
		 * back to the original values.
		 */
		self.reset = function () {
			self.error = null;
			self.success = null;
			self.itemMaster = angular.copy(self.loadedItemMaster);
			self.checkDigit = angular.copy(self.loadedCheckDigit);
			self.confirmCheckDigit();
			self.setDataDiscontinueDate();
		};

		/**
		 * The user can reset because they have made changes.
		 * @returns {boolean}
		 */
		self.canReset = function () {
			self.convertAllDates();
			return angular.toJson(self.loadedItemMaster) !== angular.toJson(self.itemMaster)
				|| angular.toJson(self.loadedCheckDigit) !== angular.toJson(self.checkDigit);
		};

		/**
		 * Makes a call to the API to get the Audit information for the Case Pack info view
		 */
		self.showCasePackAuditInfo = function(){
			self.casePackAuditInfo = productCasePackCandidateApi.getCasePackAudits;
			var title = self.CASE_PACK_INFO_TITLE;
			ProductDetailAuditModal.open(self.casePackAuditInfo, self.itemMaster.key, title);
		};

		/**
		 * Open the DiscontinueDate picker to select a new date.
		 */
		self.openDiscontinueDatePicker = function () {
			self.discontinueDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Sets the current Discontinue Date.
		 */
		self.setDateForDiscontinueDatePicker = function () {
			self.discontinueDatePickerOpened = false;
			if (self.itemMaster.discontinueDate !== null) {
				self.currentDiscontinueDate =
					new Date(self.itemMaster.discontinueDate.replace(/-/g, '\/'));
			} else {
				self.currentDiscontinueDate = null;
			}
		};

		/**
		 * Sets the Discontinue Date data.
		 */
		self.setDataDiscontinueDate = function () {
			self.showReActivateCheckbox();
			self.showDiscontinueReason();
			self.setDateForDiscontinueDatePicker();
		};

		/**
		 * Converts datepicker dates to local date format, and assigns to current Discontinue Date.
		 */
		self.convertAllDates = function(){
			if(self.currentDiscontinueDate != null) {
				self.itemMaster.discontinueDate = self.convertDate(self.currentDiscontinueDate);
			}
		};

		/**
		 * Returns a list of Discontinue Reasons.
		 */
		self.getAllDiscontinueReasons = function () {
			productCasePackCandidateApi.getAllDiscontinueReasons({}, self.loadDiscontinueReasons, self.fetchError);
		};

		/**
		 * Callback for a successful call to get Discontinue Reasons.
		 *
		 * @param results The data returned by the back end(list of Discontinue Reasons).
		 */
		self.loadDiscontinueReasons = function (results) {
			self.discontinueReasonList = results;
		};

		/**
		 * Return if a passed in string is null or empty.
		 *
		 * @param string
		 */
		self.isEmptyString = function(string){
			return (string == null || string.trim().length == 0);
		};

		/**
		 * This determines whether the ReActivate checkbox is visible.
		 */
		self.showReActivateCheckbox = function () {
			self.isReActivate = false;
			self.isReActivateShow = false;
			if(!self.isEmptyString(self.itemMaster.discontinuedByUID)){
				self.isReActivateShow = true;
			}
		};

		/**
		 * This determines whether the DiscontinueReason combobox is visible.
		 */
		self.showDiscontinueReason = function () {
			self.isDiscontinueReasonDisabled = true;
			if(!self.isEmptyString(self.itemMaster.discontinuedByUID)){
				self.isDiscontinueReasonDisabled = false;
			}
		};

		/**
		 * ReActivate the DSD Discontinue data.
		 */
		self.reActivate = function () {
			if(self.isReActivate){
				self.isLoading = true;
				var data = angular.copy(self.loadedItemMaster);
				data.discontinueDate = null;
				data.discontinueReason = null;
				productCasePackCandidateApi.saveCasePackInfoChanges(data, self.reloadSavedDataAfterReactivate, self.fetchError);
			}
		};

		/**
		 * After reactivate this reloads the successful saved data.
		 * @param results
		 */
		self.reloadSavedDataAfterReactivate = function (results) {
			self.isLoading = false;
			self.itemMaster = results.data;
			self.setData();
			if (results.message !== null) {
				if (results.message.indexOf(self.SUCCESS) != -1) {
					self.success = self.REACTIVATE_SUCCESS_MESSAGE;
				}else{
					self.success = results.message;
				}
			} else {
				results.message = null;
			}
		};

		/**
		 * Show ReActivate popup.
		 */
		self.confirmReActivate = function () {
			if(self.isReActivate){
				self.error = null;
				self.success = null;
				self.headerTitleConfirm = self.CONFIRMATION_TITLE;
				self.messageConfirm = self.REACTIVATE_MESSAGE;
				self.action = self.REACTIVATE_ACTION;
				self.yesBtnLabel = self.YES;
				self.closeBtnLabel = self.NO;
				self.yesBtnEnable = true;
				$('#confirmReActivateModal').modal({backdrop: 'static', keyboard: true});
			}
		};

		/**
		 * User click yes on confirm message popup.
		 */
		self.yesConfirmAction = function () {
			$('#confirmReActivateModal').modal('hide');
			if(self.action == self.REACTIVATE_ACTION){
				self.reActivate();
			}
			if(self.action == self.DISCONTINUEDATE_ACTION){
				self.isDiscontinueReasonDisabled = false;
			}
			if(self.action == self.REJECT_CANDIDATE_ACTION){
				self.rejectCandidate();
			}
			self.action = null;
		};

		/**
		 * User click no on confirm message popup.
		 */
		self.noConfirmAction = function () {
			if(self.action == self.REACTIVATE_ACTION){
				self.isReActivate = false;
			}
			if(self.action == self.DISCONTINUEDATE_ACTION){
				var tempDate = angular.copy(self.loadedItemMaster.discontinueDate);
				if (tempDate !== null) {
					self.currentDiscontinueDate =
						new Date(tempDate.replace(/-/g, '\/'));
				} else {
					self.currentDiscontinueDate = null;
				}
			}
			if(self.action == self.REJECT_CANDIDATE_ACTION){

			}
			self.action = null;
		};

		/**
		 * Show DiscontinueDate popup.
		 */
		self.confirmDiscontinueDate = function () {
			if(self.isDiscontinueReasonDisabled == true){
				var tempDate = angular.copy(self.loadedItemMaster.discontinueDate);
				var orgDiscontinueDate = null;
				if (tempDate !== null) {
					orgDiscontinueDate = new Date(tempDate.replace(/-/g, '\/'));
				}
				if(angular.toJson(self.currentDiscontinueDate) !== angular.toJson(orgDiscontinueDate)){
					self.error = null;
					self.success = null;
					self.headerTitleConfirm = self.DISCONTINUE_TITLE;
					self.messageConfirm = self.DISCONTINUE_MESSAGE;
					self.action = self.DISCONTINUEDATE_ACTION;
					self.yesBtnLabel = self.YES;
					self.closeBtnLabel = self.NO;
					self.yesBtnEnable = true;
					$('#confirmReActivateModal').modal({backdrop: 'static', keyboard: true});
				}
			}
		};

		/**
		 * Show value for radio button Primary UPC Switch.
		 * @param primaryUPCSw
		 * @returns {*}
		 */
		self.valuePrimaryRadioButtion = function (primaryUPCSw) {
			if(primaryUPCSw){
				return primaryUPCSw;
			}
			return !primaryUPCSw;
		};

		/**
		 *
		 * @param primaryUPC
		 */
		self.primaryUPCChangedHandle = function (primaryUPC) {
			self.productPrimaryUPC = primaryUPC;
			//Stop event change to show confrimation message.
			angular.forEach(self.sellingUnits, function(value) {
				 if(value.upc == primaryUPC){
					 value.productPrimary = false;
				 }
			});
			//Confirmation handle change Product Primary UPC before do it.
			self.error = null;
			self.success = null;
			self.headerTitleConfirm = self.EDIT_PRIMARY_UPC_TITLE;
			self.messageConfirm = self.EDIT_PRIMARY_UPC_MESSAGE.replace("{0}",primaryUPC);
			self.action = self.EDIT_PRIMARY_UPC_ACTION;
			self.yesBtnLabel = self.YES;
			self.closeBtnLabel = self.NO;
			self.yesBtnEnable = true;
			$('#confirmReActivateModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Do Change Product Primary UPC. Call method update to back end.
		 */
		self.doChangeProductPrimaryUPC = function () {
			self.isLoading = true;
			var sellingUnitPrimaryUPC;
			angular.forEach(self.sellingUnits, function(value) {
				if(value.upc == self.productPrimaryUPC){
					value.productPrimary = true;
					sellingUnitPrimaryUPC = value;
				}else{
					value.productPrimary = false;
				}
			});
			//Call method update to back end.
			productCasePackApi.changeProductPrimaryUPC(sellingUnitPrimaryUPC
				,//successfully handle
				function (result) {
					self.isLoading = false;
					self.success = result.message;
				},//error handle
				self.fetchError);
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
			$('#confirmReActivateModal').modal({backdrop: 'static', keyboard: true});
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
		 * Change to next case pack tab in candidate mode.
		 */
		self.nextCandidate = function () {
			self.isLoading = true;
			self.error = null;
			self.success = null;
			if(self.isDifference()){
				var psItemMaster = self.getPsItemMasterData();
				productCasePackCandidateApi.updateCasePackInfoCandidate(psItemMaster,
						//success case
						function (results) {
							self.isLoading = false;
							self.success = results.message;
							self.onCandidateTabChange({tabId: "warehouseTab"});
						},
						//error case
						self.fetchError);
			} else {
				self.onCandidateTabChange({tabId: "warehouseTab"});
			}
		};
	}
})();
