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

	angular.module('productMaintenanceUiApp').component('casePackInfo', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			productMaster: '<',
			onCasePackChange: '&'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/casePackInfo.html',
		// The controller that handles our component logic
		controller: CasePackInfoController
	});

	CasePackInfoController.$inject = ['ProductCasePackApi', 'UserApi', 'ProductDetailAuditModal', '$scope', '$rootScope',
		'ProductSearchService', '$filter'];

	/**
	 * Case Pack Info component's controller definition.
	 * @constructor
	 * @param productCasePackApi
	 */
	function CasePackInfoController(productCasePackApi, userApi, ProductDetailAuditModal, $scope, $rootScope, productSearchService, $filter) {
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
		self.DISCONTINUE_TITLE = "Discontinue";
		self.REACTIVATE_ACTION = "RA";
		self.DISCONTINUEDATE_ACTION = "DD";
		self.REACTIVATE_MESSAGE = "Are you sure to re-activate this item ?";
		self.DISCONTINUE_MESSAGE = "Do you really want to discontinue this item ?";
		self.REACTIVATE_SUCCESS_MESSAGE = "Saved successfully. Use DSDS for authorizing stores to this item.";
		self.NO_CHANGE_UPDATE_MESSAGE = "There are no changes on this page to be saved. Please make any changes to update.";
		self.UNKNOWN_ERROR_MESSAGE = "An unknown error occurred.";
		self.ERROR_MESSAGE = "Error.";
		self.INCORRECT_MESSAGE = "Incorrect.";

		self.EDIT_ITEM_PRIMARY_UPC_TITLE = "Edit Item Primary UPC";
		self.EDIT_ITEM_PRIMARY_UPC_MESSAGE = "Are you sure you want to set \"{0}\" UPC as Item Primary UPC?";
		self.EDIT_ITEM_PRIMARY_UPC_ACTION = "ED_ITEM_PRIMARY_UPC";

		self.EDIT_ITEM_PRIMARY_UPC_RESULTING_NEW_PROD_UPC_MESSAGE = "Changes to Item Primary UPC resulting in new product Primary UPC \"{0}\" ";
		self.EDIT_ITEM_PRIMARY_UPC_RESULTING_NEW_PROD_UPC_ACTION = "ED_ITEM_PRIMARY_UPC_RESULTING_NEW_PROD_UPC";

		/**Returns discontinue action from ui. This flag will be define discontinue first action.
		 * If an item is not discontinued, then user discontinue the item. this flag will turn true.
		 * If an item is discontinued, then update date or reason discontinue. this flag will turn false.
		 * */
		self.isDiscontinueAction = false;
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
		 * The list of associate Upc basing on item id;
		 * @type {Array}
		 */
		self.associateUpcs = [];

		/**
		 * This is item primary upc number.
		 * @type {string}
		 */
		self.itemPrimaryUPC;
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
            self.isLoading = true;
		}
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onChanges = function () {
			self.isDiscontinueAction = false;
			self.error = null;
			if(self.loadedItemMaster !=undefined && self.itemMaster.key.itemCode != self.loadedItemMaster.key.itemCode){
				self.success = null;
			}
			var key = {
				itemCode: self.itemMaster.key.itemCode,
				itemType: self.itemMaster.key.itemType
			};
			if(!self.loadedItemMaster || self.itemMaster.key.itemCode!=self.loadedItemMaster.key.itemCode){
			    productCasePackApi.getCasePackInformation(key, self.getData, self.fetchError);
			}
		};

		/**
		 * This will grab the latest ItemMaster.
		 * @param results
		 */
		self.getData = function (results) {
			self.isLoading = false;
			self.itemMaster = results;
			self.setData();
			self.setAssociateUpcs();
		};

		/**
		 * Initialize component data.
		 */
		self.setData = function () {
			self.isDiscontinueAction = false;
			if(self.itemMaster.discontinueDate === "1600-01-01"){
				self.itemMaster.discontinueDate = null;
			}
			self.loadedItemMaster = angular.copy(self.itemMaster);
			// Calculates the check digit according to the case upc.
			productCasePackApi.calculateCheckDigit({'upc': self.itemMaster.caseUpc},
				self.getCheckDigit, self.fetchError);
			// If it is a warehouse, show warehouse table.. Else show the dsd table.
			self.isWarehouse = !!self.itemMaster.key.warehouse;
			self.setDataDiscontinueDate();
		};

		/**
		 * Set list of Associate Upc basing on Item Id.
		 */
		self.setAssociateUpcs = function () {
			self.associateUpcs = [];
			if(self.isWarehouse){
				self.associateUpcs = self.itemMaster.primaryUpc?self.itemMaster.primaryUpc.associateUpcs:[];
			}else{
				var associatedUpc = {};
				associatedUpc["upc"] = self.itemMaster.key.itemCode;
				var sellingUnit = {};
				sellingUnit["primaryUpc"] = true;
				sellingUnit["tagSize"] = self.itemMaster.itemSize;
				associatedUpc["sellingUnit"] = sellingUnit;
				self.associateUpcs.push(associatedUpc);
			}
		}

		/**
		 * Returns a list of Item types.
		 */
		self.getAllItemTypes = function () {
			productCasePackApi.getAllItemTypes({}, self.loadItemTypes, self.fetchError);
		};

		/**
		 * Returns a list of one touch types.
		 */
		self.getAllOneTouchTypes = function () {
			productCasePackApi.getAllOneTouchTypes({},
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
			self.confirmCheckDigit();
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
				productCasePackApi.confirmCheckDigit({
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
				if(angular.toJson(self.loadedItemMaster.discontinueReason) === angular.toJson(self.itemMaster.discontinueReason)){
					self.itemMaster.discontinueReason = null;
				}
				if(self.loadedItemMaster.discontinueDate === self.itemMaster.discontinueDate){
					self.itemMaster.discontinueDate = null;
				}
				self.itemMaster.discontinueAction = self.isDiscontinueAction;
				productCasePackApi.saveCasePackInfoChanges(self.itemMaster, self.reloadSavedData, self.fetchError);
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
			self.itemMaster.discontinueAction=null;
			self.loadedItemMaster.discontinueAction=null;
			return angular.toJson(self.loadedItemMaster) !== angular.toJson(self.itemMaster)
				|| angular.toJson(self.loadedCheckDigit) !== angular.toJson(self.checkDigit);
		};

		/**
		 * After a save this reloads the succesful saved data.
		 * @param results
		 */
		self.reloadSavedData = function (results) {
			self.isLoading = false;
			self.itemMaster = results.data;
            self.itemMaster.warehouseLocationItems = self.loadedItemMaster.warehouseLocationItems;
			var casePack = {casePack: results.data};
			self.setData();
			if (results.message !== null) {
				self.success = results.message;
				self.onCasePackChange(casePack);
			} else {
				results.message = null;
			}
		};

		/**
		 * Whether or not you can save the page. If you have made changes on the page and whether or not the check digit
		 * is correct.
		 *
		 * @returns {boolean|*}
		 */
		self.canSave = function () {
			self.itemMaster.discontinueAction=null;
			if(self.loadedItemMaster != undefined){
                self.loadedItemMaster.discontinueAction=null;
			}
			if(angular.toJson(self.loadedItemMaster) !== angular.toJson(self.itemMaster) && self.correctCheckDigit){
				$rootScope.contentChangedFlag = true;
				return true;
			} else {
				$rootScope.contentChangedFlag = false;
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
			self.setDataDiscontinueDate();
			self.confirmCheckDigit();
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
			self.casePackAuditInfo = productCasePackApi.getCasePackAudits;
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
			productCasePackApi.getAllDiscontinueReasons({}, self.loadDiscontinueReasons, self.fetchError);
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
			if(!(self.isEmptyString(self.itemMaster.discontinuedByUID)
					&& self.isEmptyString(self.itemMaster.discontinueReason.id)
					&& (self.itemMaster.discontinueDate === null))){
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
				data.discontinueDate = " ";
				data.discontinueReason = {};
				data.discontinueReason["id"] = " ";
				data.reActive = true;
				productCasePackApi.saveCasePackInfoChanges(data, self.reloadSavedDataAfterReactivate, self.fetchError);
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
			else if(self.action == self.DISCONTINUEDATE_ACTION){
				self.isDiscontinueReasonDisabled = false;
				self.isDiscontinueAction = true;
			}
			else if(self.action == self.EDIT_ITEM_PRIMARY_UPC_ACTION){
				self.allowChangeItemPrimaryUPC();
			}
			else if(self.action == self.EDIT_ITEM_PRIMARY_UPC_RESULTING_NEW_PROD_UPC_ACTION){
				self.doChangeItemPrimaryUPC();
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
				self.isDiscontinueAction=false;
				var tempDate = angular.copy(self.loadedItemMaster.discontinueDate);
				if (tempDate !== null) {
					self.currentDiscontinueDate =
						new Date(tempDate.replace(/-/g, '\/'));
				} else {
					self.currentDiscontinueDate = null;
				}
				self.itemMaster.discontinueDate = angular.copy(self.loadedItemMaster.discontinueDate)
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
		self.valuePrimaryRadioButton = function (primaryUPCSw) {
			if(primaryUPCSw){
				return primaryUPCSw;
			}
			return !primaryUPCSw;
		}

		/**
		 *
		 * @param primaryUPC
		 */
		self.itemPrimaryUPCChangedHandle = function (primaryUPC) {
			self.itemPrimaryUPC = primaryUPC;
			//Stop event change to show confirmation message.
			angular.forEach(self.associateUpcs, function(value) {
				if(value.upc == primaryUPC && value.sellingUnit){
					value.sellingUnit.primaryUpc = false;
				}
			});
			//Confirmation handle change Item Primary UPC before do it.
			self.error = null;
			self.success = null;
			self.headerTitleConfirm = self.EDIT_ITEM_PRIMARY_UPC_TITLE;
			self.messageConfirm = self.EDIT_ITEM_PRIMARY_UPC_MESSAGE.replace("{0}",primaryUPC);
			self.action = self.EDIT_ITEM_PRIMARY_UPC_ACTION;
			self.yesBtnLabel = self.YES;
			self.closeBtnLabel = self.NO;
			self.yesBtnEnable = true;
			$('#confirmReActivateModal').modal({backdrop: 'static', keyboard: true});
		}

		/**
		 * Do Allow Change Product Primary UPC. Will Call method update to back end.
		 */
		self.allowChangeItemPrimaryUPC = function () {
			//Find current selling primary UPC
			var sellingPrimaryUPC = '';
			angular.forEach(self.productMaster.sellingUnits, function(value) {
				if(value.productPrimary){
					sellingPrimaryUPC = value.upc;
				}
			});
			//Check new item primary Upc is current selling primary upc during selling primary upc not be product
			// upc primary.
			if(self.itemPrimaryUPC == sellingPrimaryUPC && sellingPrimaryUPC != self.productMaster.productPrimaryScanCodeId){
				//Confirmation handle change Item Primary UPC will Changes to Item Primary UPC resulting in new product
				// Primary UPC
				self.error = null;
				self.success = null;
				self.headerTitleConfirm = self.EDIT_ITEM_PRIMARY_UPC_TITLE;
				self.messageConfirm = self.EDIT_ITEM_PRIMARY_UPC_RESULTING_NEW_PROD_UPC_MESSAGE.replace("{0}",sellingPrimaryUPC);
				self.action = self.EDIT_ITEM_PRIMARY_UPC_RESULTING_NEW_PROD_UPC_ACTION;
				self.yesBtnLabel = self.YES;
				self.closeBtnLabel = self.NO;
				self.yesBtnEnable = true;
				$('#confirmReActivateModal').modal({backdrop: 'static', keyboard: true});
			}else{
				self.doChangeItemPrimaryUPC();
			}
		}

		/**
		 * Do Change Product Primary UPC. Call method update to back end.
		 */
		self.doChangeItemPrimaryUPC = function () {
			self.isLoading = true;
			//Set data for object UPC SWAP to sent back end.
			var upcSwap = {};

			var source = {};
			source["productId"] = self.productMaster.prodId;
			source["itemCode"] =  self.itemMaster.key.itemCode;
			source["itemType"] =  self.itemMaster.key.itemType;

			var destination = {};
			destination["productId"] = self.productMaster.prodId;
			destination["itemCode"] =  self.itemMaster.key.itemCode;
			destination["itemType"] =  self.itemMaster.key.itemType;
			destination["primaryUpc"] =  self.itemPrimaryUPC;

			//Set new primary switch
			angular.forEach(self.associateUpcs, function(value) {
				if(value.upc == self.itemPrimaryUPC){
					value.sellingUnit.primaryUpc = true;
				}else if(value.sellingUnit.primaryUpc == true){
					source["primaryUpc"] =  value.upc;
					value.sellingUnit.primaryUpc = false;
				}
			});
			//Set source, destination for upc swap
			upcSwap["source"] = source;
			upcSwap["destination"] = destination;
			//Call method update to back end.
			productCasePackApi.changeItemPrimaryUPC(upcSwap
				,//successfully handle
				function (result) {
					self.isLoading = false;
					self.success = result.message;
					//refresh data
					if(result.data){
						self.itemMaster.orderingUpc = angular.copy(result.data.orderingUpc);
						self.itemMaster.primaryUpc = angular.copy(result.data.primaryUpc);
						self.loadedItemMaster.orderingUpc = angular.copy(result.data.orderingUpc);
						self.loadedItemMaster.primaryUpc = angular.copy(result.data.primaryUpc);
						self.setAssociateUpcs();
					}
				},//error handle
				self.fetchError);
		}
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
