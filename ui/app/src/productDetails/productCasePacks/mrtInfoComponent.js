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
 * MrtInfo -> Mrt Info page component.
 *
 * @author m594201
 * @since 2.5.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('mrtInfo', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			productMaster: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/mrtInfo.html',
		// The controller that handles our component logic
		controller: MrtInfoController
	});

	MrtInfoController.$inject = ['ProductCasePackApi', 'ProductDetailAuditModal','ProductSearchService', '$rootScope'];


	function MrtInfoController(productCasePackApi, ProductDetailAuditModal,productSearchService, $rootScope) {

		/** All CRUD operation controls of Case pack Info page goes here */

		var self = this;

		/**
		 * Whether or not to show audit
		 *
		 * @type {boolean}
		 */
		self.showMrtAuditPanel = false;

		/**
		 * Page title
		 *
		 * @type {string}
		 */
		self.title = "MRT";

		/**
		 * Value that is the sum of all quantities in an MRT
		 *
		 * @type {number}
		 */
		self.totalQuantity = 0;

		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Error message.
		 * @type {null}
		 */
		self.error = null;

		/**
		 * Error message array.
		 * @type {null}
		 */
		self.errorValidate = null;

		/**
		 * The current data of itemMaster
		 * @type {null}
		 */
		self.itemMasterCurrent = null;

		/**
		 * The original data of item master.
		 * @type {null}
		 */
		self.original = null;

		/**
		 * The flag selected all element MRT.
		 * @type {boolean}
		 */
		self.checkAllMRT = false;

		/**
		 * Count number process when call multi process once time.
		 * @type {number}
		 */
		self.countProcess = 0;

		/**
		 * Item Master data will be sent to call back do update.
		 * @type {null}
		 */
		self.itemMasterToSave = null;

		/**
		 * Constant for QUANTITY_MANDATORY_FIELD_MESSAGE
		 * @type {string}
		 */
		const QUANTITY_MANDATORY_FIELD_MESSAGE = "Quantity value is mandatory field.";

		/**
		 * Constant for ELEMENT_UPC_MANDATORY_FIELD_MESSAGE
		 * @type {string}
		 */
		const ELEMENT_UPC_MANDATORY_FIELD_MESSAGE = "Element UPC value is mandatory field.";

		/**
		 * Constant for UPC_NOT_FOUND_MESSAGE
		 * @type {string}
		 */
		const UPC_NOT_FOUND_MESSAGE = "No matches found for the UPC[upc_number]. Please check and re-enter.";

		/**
		 * Constant for UPC_FIELD_EXIST_MESSAGE
		 * @type {string}
		 */
		const UPC_FIELD_EXIST_MESSAGE = "Entered UPC is existing.";
		/**
		 * Holds the alert message to show when save mrt info that enter empty size.
		 * @type {string}
		 */
		const SIZE_MANDATORY_FIELD_MESSAGE = "Size value is mandatory field.";
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		};

		/**
		 * On change handle on page.
		 */
		this.$onChanges = function () {
			self.success = null;
			self.itemMasterCurrent = angular.copy(self.itemMaster);
			self.original = angular.copy(self.itemMasterCurrent);
			if(self.itemMaster.mrt){
				self.isWaitingForResponse = true;
				self.getMRTInfo(self.itemMaster.orderingUpc, self.itemMaster.key);
			}
		};

		/**
		 * Init loading MRT information basing on item master ordering UPC(MRT UPC).
		 * @param upcNumber
		 */
		self.getMRTInfo = function (upcNumber, itemMasterKey) {
			self.countProcess = 2;
			productCasePackApi.getMrtInfo({upc: upcNumber}, self.loadMRTData, self.fetchError);
			productCasePackApi.getMrtItemInfo({itemCode:itemMasterKey.itemCode, itemType:itemMasterKey.itemType}, self.loadMRTItemData, self.fetchError);
		};

		/**
		 * Callback for a successful call to get mrt info from backend
		 * @param results
		 */
		self.loadMRTData = function (results) {
			self.itemMaster.primaryUpc.shipper = angular.copy(results);
			self.countProcess -= 1;
			if(self.countProcess === 0){
				self.isWaitingForResponse = false;
				self.error = null;
				self.errorValidate = null;
				self.checkAllMRT = false;
				self.itemMasterCurrent = angular.copy(self.itemMaster);
				self.calculateQuantityTotal(self.itemMasterCurrent.primaryUpc.shipper);
				self.selectedAllMRT();
				self.original = angular.copy(self.itemMasterCurrent);
			}
		};

		/**
		 * Callback for a successful call to get mrt item info from backend
		 * @param results
		 */
		self.loadMRTItemData = function (results) {
			self.itemMaster.prodItems = angular.copy(results);
			self.countProcess -= 1;
			if(self.countProcess === 0){
				self.isWaitingForResponse = false;
				self.error = null;
				self.errorValidate = null;
				self.checkAllMRT = false;
				self.itemMasterCurrent = angular.copy(self.itemMaster);
				self.calculateQuantityTotal(self.itemMasterCurrent.primaryUpc.shipper);
				self.selectedAllMRT();
				self.original = angular.copy(self.itemMasterCurrent);
			}
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};

		/**
		 * Callback for a successful call to get mrt info from backend
		 * @param results
		 */
		self.loadData = function (results) {
			self.success = results.message;
			// If there is any change on item size then we need to update current item size to original item size
			// of item master after save successfully.
			if(self.itemMasterCurrent.itemSize.trim() != self.itemMaster.itemSize.trim()){
				self.itemMaster.itemSize = self.itemMasterCurrent.itemSize;
			}
			self.getMRTInfo(self.itemMaster.orderingUpc, self.itemMaster.key);
		};

		/**
		 * Sends modified mrt to the backend for saving
		 */
		self.saveMrtInfo = function () {
			self.success = null;
			self.error = null;
			self.errorValidate = null;
			//get the list of pd_shipper has been changed
			var shipperDataToSave = self.validateMRTDataBeforeToBeSave();
			if(self.errorValidate === null || self.errorValidate.length == 0){
				// Assign itemSize = null when there is no any change on item size.
				// and api basic on itemSize <> null to update or not.
				var itemSize = null;
				if(self.itemMasterCurrent.itemSize.trim() != self.itemMaster.itemSize.trim()){
					// Set the new item size to update.
					itemSize = self.itemMasterCurrent.itemSize;
				}
				//Sent data to back end, to update new information MRT
				if(shipperDataToSave != null && shipperDataToSave.length > 0 || itemSize != null){
					self.isWaitingForResponse = true;
					//get the list of prod_item data has been changed
					var prodItemDataToSave = self.getProdItemData(self.itemMasterCurrent.primaryUpc.shipper);
					var itemMasterToSave = angular.copy(self.itemMasterCurrent);
					itemMasterToSave.primaryUpc.shipper = shipperDataToSave;
					itemMasterToSave.prodItems = prodItemDataToSave;
					itemMasterToSave.itemSize = itemSize;
					productCasePackApi.saveMrt(itemMasterToSave, self.loadData, self.fetchError)
				}else{
					self.error = "There are no changes on this page to be saved. Please make any changes to update.";
				}
			}
		};

		/**
		 *
		 * @returns {Array}
		 */
		self.getProdItemData = function (currentShippers) {
			var prodItemDataToSave = [];
			//find new product item data
			angular.forEach(currentShippers, function (newShipper) {
				var foundOldData = false;
				self.itemMasterCurrent.prodItems.find(function (prodItemOld) {
					if(prodItemOld.key.productId === newShipper.sellingUnit.prodId){
						foundOldData = true;
					}
				});
				if(foundOldData === false){
					var find = false;
					prodItemDataToSave.find(function(item) {
						if(item.key.productId === newShipper.sellingUnit.prodId){
							item.productCount += newShipper.shipperQuantity;
							item.actionCode = "A";
							find = true;
						}
					});
					if(find === false){
						var productItemNew = {};
						productItemNew["actionCode"] = "A";
						productItemNew["key"] = {itemType:self.itemMasterCurrent.key.itemType,itemCode:self.itemMasterCurrent.key.itemCode,productId:newShipper.sellingUnit.prodId};
						productItemNew["productCount"]=newShipper.shipperQuantity;
						prodItemDataToSave.push(productItemNew);
					}
				}
			});
			//find modify, delete product item data
			angular.forEach(self.itemMasterCurrent.prodItems, function (prodItem) {
				var foundElm = false;
				var totalRetail = 0;
				currentShippers.find(function (newShipper) {
					if(prodItem.key.productId === newShipper.sellingUnit.prodId){
						totalRetail += newShipper.shipperQuantity;
						foundElm = true;
					}
				});
				if(foundElm === false){
					prodItem.actionCode = "D";
					prodItemDataToSave.push(prodItem);
				}
				else if(totalRetail !== prodItem.productCount){
					prodItem.actionCode = "";
					prodItem.productCount = totalRetail;
					prodItemDataToSave.push(prodItem);
				}
			});
			return prodItemDataToSave;
		}

		/**
		 * Check data to changed, and validate all data has been changed is valid to call back end to update.
		 * @returns {Array}
		 */
		self.validateMRTDataBeforeToBeSave = function () {
			var shipperDataToSave = [];
			var errorMessageArray = [];
			var shipperUPCs = [];
			//Find all element shipper edited/add new
			angular.forEach(self.itemMasterCurrent.primaryUpc.shipper, function (newShipper) {
				var foundElm = false;
				var elementNeedValidate = false;
				self.original.primaryUpc.shipper.find(function (oldShipper) {
					if(newShipper.key.shipperUpc == oldShipper.key.shipperUpc){
						foundElm = true;
						if(newShipper.shipperQuantity !== oldShipper.shipperQuantity){
							elementNeedValidate = true;
							newShipper.actionCode = "";
							shipperDataToSave.push(newShipper);
						}
					}
				});
				if(foundElm === false){
					newShipper.actionCode = "A";
					shipperDataToSave.push(newShipper);
					elementNeedValidate = true;
				}
				if(elementNeedValidate){
					if(newShipper.key.shipperUpc === null || newShipper.key.shipperUpc === ""){
						errorMessageArray.push(ELEMENT_UPC_MANDATORY_FIELD_MESSAGE);
					}
					else if(shipperUPCs.indexOf(newShipper.key.shipperUpc+'') >= 0 && errorMessageArray.indexOf(UPC_FIELD_EXIST_MESSAGE) < 0){
						errorMessageArray.push(UPC_FIELD_EXIST_MESSAGE);
					}
					if((newShipper.shipperQuantity === null || newShipper.shipperQuantity === '') && errorMessageArray.indexOf(QUANTITY_MANDATORY_FIELD_MESSAGE) < 0){
						errorMessageArray.push(QUANTITY_MANDATORY_FIELD_MESSAGE);
					}
					if(newShipper.key.shipperUpc !== null && newShipper.key.shipperUpc !== '' && (newShipper.sellingUnit === undefined || newShipper.sellingUnit === null)){
						errorMessageArray.push(UPC_NOT_FOUND_MESSAGE.replace("upc_number",newShipper.key.shipperUpc));
					}
				}
				shipperUPCs.push(newShipper.key.shipperUpc+'');
			});
			//Find all element shipper deleted.
			angular.forEach(self.original.primaryUpc.shipper, function (oldShipper) {
				var foundElm = false;
				self.itemMasterCurrent.primaryUpc.shipper.find(function (newShipper) {
					if(newShipper.key.shipperUpc == oldShipper.key.shipperUpc){
						foundElm = true;
					}
				});
				if(foundElm === false){
					oldShipper.actionCode = "D";
					shipperDataToSave.push(oldShipper);
				}
			});
			// Validate item size:
			// 1. if there is any change on the item size then
			// we need to check item size that is not empty before save.
			// 2. If there is no any change on item size then we don't need to check empty on it,
			// because we just save the data when it changed.
			if(self.itemMasterCurrent.itemSize.trim() != self.itemMaster.itemSize.trim()) {
				if (self.itemMasterCurrent.itemSize.trim() == '') {
					// Show empty message
					errorMessageArray.push(SIZE_MANDATORY_FIELD_MESSAGE);
				}
			}
			if(errorMessageArray !== null && errorMessageArray.length > 0){
				self.errorValidate = errorMessageArray;
			}
			return shipperDataToSave;
		}

		/**
		 * Set values to original value
		 */
		self.reset = function () {
			self.error = null;
			self.errorValidate = null;
			self.success = false;
			self.checkAllMRT = false;
			self.itemMasterCurrent = angular.copy(self.original);
			self.calculateQuantityTotal(self.itemMasterCurrent.primaryUpc.shipper);
		};

		/**
		 * Add new row to Add UPC to an MRT
		 */
		self.addRow = function (){
			self.error = null;
			self.errorValidate = null;
			self.success = false;
			self.validateMRTDataBeforeToBeSave();
			if(self.errorValidate === null || self.errorValidate.length == 0){
				var newShipper = {};
				newShipper.key = {};
				newShipper.key.shipperUpc = "";
				newShipper.key.upc = self.itemMasterCurrent.orderingUpc;
				newShipper.shipperTypeCode = "M";
				newShipper.shipperQuantity = "";
				newShipper.actionCode = "A";
				self.itemMasterCurrent.primaryUpc.shipper.push(newShipper);
			}
		};

		/**
		 * Checks to see if necessary fields have been filled to enable the save button.
		 *
		 * @returns {boolean} disabled flag.  This determines if the save button is active.
		 */
		self.disableSave = function () {
			// Check data change on  mrt item
			if(self.itemMaster.mrt && (angular.toJson(self.original.primaryUpc.shipper) !== angular.toJson(self.itemMasterCurrent.primaryUpc.shipper) ||
				self.itemMasterCurrent.itemSize.trim() != self.itemMaster.itemSize.trim())
			){
				$rootScope.contentChangedFlag = true;
				return false;
			}
			$rootScope.contentChangedFlag = false;
			return true;
		};

		/**
		 * Creates a blank row to enter a new upc into an mrt
		 * @param shipper
		 */
		self.rowChecked = function () {
			var noneSelectedAll = false;
			angular.forEach(this.itemMasterCurrent.primaryUpc.shipper, function (shipper) {
				if(shipper.isChecked === false){
					noneSelectedAll = true;
				}
			});
			self.checkAllMRT = !noneSelectedAll;
		};

		/**
		 * Populates dynamic list of checked rows.  Then sent to back end for removal from MRT.
		 */
		self.deleteMrt = function () {
			self.error = null;
			self.errorValidate = null;
			self.success = false;
			if(self.checkAllMRT){
				self.error = "MRT's MUST have more than 1 element";
			}else{
				var shipperData = [];
				var shipperDataToSave = [];
				angular.forEach(this.itemMasterCurrent.primaryUpc.shipper, function (shipper) {
					var shipperOrig = null;
					self.original.primaryUpc.shipper.find(function (oldShipper) {
						if(shipper.key.shipperUpc == oldShipper.key.shipperUpc){
							shipperOrig = oldShipper;
						}
					});
					if(shipper.isChecked === true){
						shipper.actionCode = "D";
						shipperDataToSave.push(shipper);
					}else if(shipperOrig !== null){
						shipperData.push(shipperOrig)
					}
				});
				//Sent data to back end, to update new information MRT
				if(shipperDataToSave !== null && shipperDataToSave.length > 0){
					var prodItemDataToSave = self.getProdItemData(shipperData);
					self.itemMasterToSave = angular.copy(self.itemMasterCurrent);
					self.itemMasterToSave.primaryUpc.shipper = shipperDataToSave;
					self.itemMasterToSave.prodItems = prodItemDataToSave;
					//Show confirm pop up for user make sure handle delete MRT. If use click Yes, then call to back
					// end process delete MRT.
					$('#deleteConfirmModal').modal({backdrop: 'static', keyboard: true});
				}else{
					self.error = "There are no changes on this page to be saved. Please make any changes to update.";
				}
			}
		};

		/**
		 * After user make sure handle delete MRT, then call to back end process delete MRT.
		 */
		self.doDeleteMRT = function () {
			$('#deleteConfirmModal').modal("hide");
			self.isWaitingForResponse = true;
			productCasePackApi.saveMrt(self.itemMasterToSave, self.loadData, self.fetchError);
		};

		/**
		 * Handle selected all element MRT.
		 */
		self.selectedAllMRT = function () {
			angular.forEach(this.itemMasterCurrent.primaryUpc.shipper, function (shipper) {
				shipper["isChecked"] = self.checkAllMRT;
			});
		};

		/**
		 * When user add new once MRT element and type upc number then tab out. Call back end to find more detail
		 * basing on UPC number has been entered.
		 * @param shipper
		 */
		self.loadSellingUnit = function (shipper) {
			if(shipper.key.shipperUpc != undefined && shipper.key.shipperUpc != '' && shipper.key.shipperUpc != null
				&& (shipper.sellingUnit === undefined || shipper.sellingUnit == null ||
				(shipper.sellingUnit !== null && shipper.sellingUnit.upc !== shipper.key.shipperUpc))){
				self.error = null;
				self.errorValidate = null;
				self.success = false;
				self.isWaitingForResponse = true;
				productCasePackApi.getMRTElementInfo(
					{upc: shipper.key.shipperUpc},
					function(result){//successful method load
						self.isWaitingForResponse = false;
						if(result != undefined && result != null && result.upc != undefined && result.upc != null){
							shipper.sellingUnit = result;
						}else{
							self.error = "No matches found for the UPC[".concat(shipper.key.shipperUpc).concat("]. Please check and re-enter.");
						}
					},
					self.fetchError);
			}
		};

		/**
		 * Fetches the error from the back end.
		 *
		 * @param error
		 */
		self.fetchError = function(error) {
			self.isWaitingForResponse = false;
			if(error && error.data) {
				self.isLoading = false;
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
		 * Shows the MRT audit panel
		 */
		self.showMrtAuditInfo = function() {
			self.getMrtAuditInfo = productCasePackApi.getMrtAuditInformation;
			self.auditParams = {upc: self.itemMaster.primaryUpc.upc};
			ProductDetailAuditModal.open(self.getMrtAuditInfo, self.auditParams, self.title);
		};

		/**
		 * Takes in a list of shippers and calculates the sum of the quantities
		 * @param shipper list
		 */
		self.calculateQuantityTotal = function(shipper) {
			self.totalQuantity=0;
			angular.forEach(shipper, function (value, index) {
				self.totalQuantity = self.totalQuantity + shipper[index].shipperQuantity;
			});
		};
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
	}
})();
