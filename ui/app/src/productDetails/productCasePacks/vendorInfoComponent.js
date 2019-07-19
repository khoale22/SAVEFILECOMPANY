/*
 *   vendorInfoComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * CasePack -> Vendor component.
 *
 * @author l730832
 * @since 2.5.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('vendorInfo', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			onCandidateStatusChange : '&',
			productMaster: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/vendorInfo.html',
		// The controller that handles our component logic
		controller: VendorInfoController
	});

	VendorInfoController.$inject = ['ProductCasePackApi', 'ProductDetailAuditModal', '$rootScope', 'ProductSearchService'];

	/**
	 * Vendor info component's controller definition.
	 * @constructor
	 * @param productCasePackApi
	 * @param ProductDetailAuditModal
	 */
	function VendorInfoController(productCasePackApi, ProductDetailAuditModal, $rootScope, productSearchService) {

		var self = this;

		self.orderQuantityRestrictionList = [
			{displayName: "NO-RESTRICTIONS[N]", id: "N"},
			{displayName: "VENDOR-PALLET[P]", id: "P"},
			{displayName: "VEN-TIE[T]", id: "T"},
			{displayName: "WAREHOUSE-PALLET[W]", id: "W"}
		];

		/**
		 * Constant for VENDOR_PRODUCT_CODE_MANDATORY_FIELD_MESSAGE
		 * @type {string}
		 */
		const VENDOR_PRODUCT_CODE_MANDATORY_FIELD_MESSAGE = "Vendor Product Code is mandatory field.";

		/**
		 * Constant for NET_CUBE_FORMAT_FIELD_MESSAGE
		 * @type {string}
		 */
		const NET_CUBE_FORMAT_FIELD_MESSAGE = "Net Cube value must be a decimal number with 3 places and must be greater than or equal to 0 and less than or equal to 999999.999. Example: 1234.567";

		/**
		 * Constant for MASTER_LENGTH_MANDATORY_FIELD_MESSAGE
		 * @type {string}
		 */
		const MASTER_LENGTH_MANDATORY_FIELD_MESSAGE = "Master Length is mandatory field.";

		/**
		 * Constant for MASTER_LENGTH_FORMAT_FIELD_MESSAGE
		 * @type {string}
		 */
		const MASTER_LENGTH_FORMAT_FIELD_MESSAGE = " Master Length value must be a decimal number with 2 places and must be greater than or equal to 0 and less than or equal to 99999.99. Example: 1234.56";

		/**
		 * Constant for MASTER_WIDTH_MANDATORY_FIELD_MESSAGE
		 * @type {string}
		 */
		const MASTER_WIDTH_MANDATORY_FIELD_MESSAGE = "Master Width is mandatory field.";

		/**
		 * Constant for MASTER_WIDTH_FORMAT_FIELD_MESSAGE
		 * @type {string}
		 */
		const MASTER_WIDTH_FORMAT_FIELD_MESSAGE = " Master Width value must be a decimal number with 2 places and must be greater than or equal to 0 and less than or equal to 99999.99. Example: 1234.56";

		/**
		 * Constant for MASTER_HEIGHT_MANDATORY_FIELD_MESSAGE
		 * @type {string}
		 */
		const MASTER_HEIGHT_MANDATORY_FIELD_MESSAGE = "Master Height is mandatory field.";

		/**
		 * Constant for MASTER_HEIGHT_FORMAT_FIELD_MESSAGE
		 * @type {string}
		 */
		const MASTER_HEIGHT_FORMAT_FIELD_MESSAGE = " Master Height value must be a decimal number with 2 places and must be greater than or equal to 0 and less than or equal to 99999.99. Example: 1234.56";

		/**
		 * Constant for MASTER_WEIGHT_MANDATORY_FIELD_MESSAGE
		 * @type {string}
		 */
		const MASTER_WEIGHT_MANDATORY_FIELD_MESSAGE = "Master Weight is mandatory field.";

		/**
		 * Constant for MASTER_WEIGHT_FORMAT_FIELD_MESSAGE
		 * @type {string}
		 */
		const MASTER_WEIGHT_FORMAT_FIELD_MESSAGE = " Master Weight value must be a decimal number with 2 places and must be greater than or equal to 0 and less than or equal to 99999.99. Example: 1234.56";

		/**
		 * Constant for ZERO_NUMBER
		 * @type {number}
		 */
		const ZERO_NUMBER = 0;
		/**
		 * Flag used to state if there is any vendor information available.
		 * @type {boolean}
		 */
		self.isVendorInfoAvailable = false;

		/**
		 * Current vendor list attached to that item code.
		 * @type {null}
		 */
		self.vendorList = null;

		/**
		 * Original vendor list attached to that item code.
		 * @type {null}
		 */
		self.vendorListOrig = null;
		/**
		 * Success message.
		 * @type {null}
		 */
		self.success = null;
		/**
		 * Error message.
		 * @type {null}
		 */
		self.error = null;

		/**
		 * If the item doesn't have a vendor. This is most likely caused by a data issue.
		 * @type {boolean}
		 */
		self.emtpyVendor = false;

		/**
		 *Token that is used to keep track of the current request so that old requests will be ignored.
		 */
		self.latestRequest =0;

		/**
		 * Page title
		 *
		 * @type {string}
		 */
		self.title = "Vendor";
		/**
		 * Whether or not the bicep can support multiple item classes.
		 * @type {boolean}
		 */
		self.isMixedClassVendor = false;

		/**
		 * Show loading waiting when open popup.
		 * @type {boolean}
		 */
		self.isPopupLoading = false;

		/**
		 * List of bicep vendor basing ap location number and class code. This is data provider for popoup add to
		 * warehouse.
		 */
		self.bicepVendors = [];

		/**
		 * Flag check all value in add to warehouse popup.
		 * @type {boolean}
		 */
		self.checkAllFlag = false;

		/**
		 * Error message show in popup.
		 * @type {string}
		 */
		self.errorPopup = '';
		/**
		 * Holds loading request.
		 * @type {{}}
		 */
		self.loadingRequest = {};

		/**
		 * Current number index.
		 * @type {number}
		 */
		self.currentIndex = 0;

		/**
		 * RegExp check number is decimal type (######.###), and max value is 999999.999
		 * @type {{}}
		 */
		self.regexDecimal = new RegExp("^[0-9]{1,6}\.?([0-9]{1,3})?$");

		/**
		 * RegExp check number is decimal type (#####.##), and max value is 99999.99
		 * @type {{}}
		 */
		self.regexDecimal2 = new RegExp("^[0-9]{1,5}\.?([0-9]{1,2})?$");

		/**
		 * Error message.
		 * @type {null}
		 */
		self.errorValidate = null;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		}
		/**
		 * Component will reload the vendor data whenever the item is changed in casepack.
		 */
		this.$onChanges=function(){
			self.success = null;
			self.error = null;
			self.errorValidate = null;
			self.isEditingVendor = true;
			self.currentItemCode = self.itemMaster.key.itemCode;
			if(self.itemMaster!= null  && self.itemMaster != undefined) {
				var loadingKey = self.itemMaster.key.itemCode + '_' + self.itemMaster.key.itemType;
				//Avoid double requests
				if (self.loadingRequest[loadingKey] == null || self.loadingRequest[loadingKey] == undefined || self.loadingRequest[loadingKey] == false) {
					self.loadingRequest = {};
					self.loadingRequest[loadingKey] = true;
					self.getData();
				}else{
					self.isVendorInfoAvailable = true;
				}
			}else{
				self.isVendorInfoAvailable = false;
			}
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};

		/**
		 * Retrieves all data for the vendor.
		 */
		self.getData = function() {
			self.isLoading = true;
			var thisRequest = ++self.latestRequest;
			productCasePackApi.findVendorInfoByItemId({
					itemId: self.itemMaster.key.itemCode,
					itemType: self.itemMaster.key.itemType
				},
				//success case
				function (results) {
					if (thisRequest === self.latestRequest) {
						self.loadData(results);
					}
				},
				//error case
				function(results){
					if(thisRequest === self.latestRequest){
						self.fetchError(results)
					}
					self.loadingRequest = {};
				});
		};

		/**
		 * Loads the data sent back from the back end.
		 * @param result
		 */
		self.loadData = function(result) {
			self.currentIndex = 0;
			if(result.length != 0) {
				self.isVendorInfoAvailable = true;
				self.error = null;
				self.errorValidate = null;
				if (result.length > 0) {
					self.vendorList = self.handleResultsDataBeforeAssign(result);
					self.vendorListOrig = angular.copy(self.vendorList);
					self.selectedVendor = self.vendorList[0];
				} else {
					self.emptyVendor = true;
					self.isLoading = false;
					return;
				}
				if (result.message !== null && result.message != undefined) {
					self.success = result.message;
				} else{
					result.message = null;
				}
				self.retail = self.selectedVendor.itemMaster.retail;

				self.shipPackQuantity = self.itemMaster.pack;
				if (self.selectedVendor.cost == null) {
					self.listCost = null;
				} else {
					self.listCost = self.selectedVendor.cost.cost;
				}

				if (self.itemMaster.key.dsd === false) {
					self.isDsd = false;
					self.isMixedClassVendor = self.selectedVendor.vendorLocation.mixedClassVendorSwitch;
					self.calculateUnitCost(self.shipPackQuantity);
					self.selectedWarehouseLocationItem = self.selectedVendor.itemWarehouseVendorList[0].warehouseLocationItem;
				} else {
					self.isDsd = true;
					self.calculateUnitCost(self.selectedVendor.packQuantity);
				}
				self.calculateGrossMargin();
				self.isReliable = self.isReliableInventory();
			}
			self.isLoading = false;
		};

		/**
		 * trim data
		 *
		 * @param results The data returned by the backend.
		 *
		 * @return the processed data.
		 */
		self.handleResultsDataBeforeAssign = function(results){
			var tmpResults = angular.copy(results);
			for(var i =0; i < tmpResults.length; i++){
				if(tmpResults[i].vendItemId!=null){
					tmpResults[i].vendItemId = tmpResults[i].vendItemId.trim();
				}

				if(tmpResults[i].orderQuantityRestrictionCode!=null){
					tmpResults[i].orderQuantityRestrictionCode = tmpResults[i].orderQuantityRestrictionCode.trim();
				}
				tmpResults[i]["index"] = i;
			}
			return tmpResults;
		};


		/**
		 * Fetches the error from the back end.
		 * @param error
		 */
		self.fetchError = function(error){
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
		 * Handles dropdown bicep vendor list change.
		 * @param vendor the vendor being changed to.
		 */
		self.handleVendorListChange = function() {
			if(self.validateVendorInfo(self.vendorList[self.currentIndex], self.vendorListOrig[self.currentIndex])){
				self.currentIndex = self.selectedVendor.index;
				self.retail = self.selectedVendor.itemMaster.retail;
				self.shipPackQuantity = self.itemMaster.pack;
				if (self.selectedVendor.cost == null) {
					self.listCost = null;
				} else {
					self.listCost = self.selectedVendor.cost.cost;
				}
				if(self.itemMaster.key.dsd === false) {
					self.isDsd = false;
					self.isMixedClassVendor = self.selectedVendor.vendorLocation.mixedClassVendorSwitch;
					console.log(self.isMixedClassVendor);
					self.calculateUnitCost(self.shipPackQuantity);
					self.selectedWarehouseLocationItem = self.selectedVendor.itemWarehouseVendorList[0].warehouseLocationItem;
				} else {
					self.isDsd = true;
					self.calculateUnitCost(self.selectedVendor.packQuantity);
				}
				self.calculateGrossMargin();
				self.isReliable = self.isReliableInventory();
			}else{
				self.selectedVendor = self.vendorList[self.currentIndex];
			}
		};

		/**
		 * Replace all vendor product code in the list of vendor when change vendor product code.
		 */
		self.vendorProductCodeChangeHandle = function () {
			angular.forEach(self.vendorList, function(vendor) {
				vendor.vendItemId = angular.copy(self.selectedVendor.vendItemId);
			});
		}

		/**
		 * Gross Margin =(Unit retail-Unit cost)/Unit retail *100

		 - Using [getRetailByUPC] method of PriceService to get Unit Retail value based on ordering UPC of selected Item
		 If Unit Retail >1 : Unit Retail = Retail For/Unit Retail
		 else, Unit Retail = Retail For
		 */
		self.calculateGrossMargin = function() {
			if (self.retail == null || self.unitCost == null) {
				self.grossMargin = "Gross Margin Unavailable";
			} else {
				var unitRetail = self.retail.retail / self.retail.xFor;
				self.grossMargin = ((unitRetail - self.unitCost) / unitRetail) * 100;
				self.grossMargin = self.grossMargin.toFixed(2) + '%';
			}
		};

		/**
		 * Calculates the unit Cost depending on whether its warehouse or vendor.
		 * @param packQuantity
		 */
		self.calculateUnitCost = function(packQuantity) {
			if (self.listCost == null) {
				self.unitCostDisplay = "";
				self.unitCost = null;
			} else {
				var x = self.listCost;
				self.unitCost = x / packQuantity;
				self.unitCostDisplay = '$' + self.unitCost.toFixed(3);
			}
		};

		/**
		 * Checks to see if it is a reliable inventory.
		 * @returns {boolean} whether it is reliable or not.
		 */
		self.isReliableInventory = function() {
			if (self.selectedVendor.vendorLocationItemMaster.trustedItemSwitch) {
				if (self.itemMaster.key.dsd === false) {
					return self.selectedVendor.vendorLocationItemMaster.dsdLocation.trustedVendSwitch;
				} else
					return self.selectedVendor.vendorLocationItemMaster.vendorLocation.trustedVendSwitch;
			} else {
				return false;
			}
		};

		/**
		 * Updates the selected Vendor.
		 */
		self.updateSelectedVendor = function() {
			self.success = null;
			self.error = null;
			self.errorValidate = null;
			var vendors = [];
			// When user hit save button, compare data from current list to original list. Only sent to back end the
			// list of element bicep vendor has been changed. Before sent data to save, validation each element. If
			// exits data invalid. Show error message and skip action save.
			if(self.vendorList !== null && self.vendorList !== undefined){
				for(var i =0; i < self.vendorList.length; i++){
					if(angular.toJson(self.vendorList[i]) !== angular.toJson(self.vendorListOrig[i])){
						if(self.validateVendorInfo(self.vendorList[i], self.vendorListOrig[i])){
							vendors.push(self.getVendorChanges(self.vendorList[i], self.vendorListOrig[i]));
						}else{
							return;
						}
					}
				}
			}
			//After get the list of vendor has been changed with data valid. Show progess bar and call to back end
			// save function.
			if(vendors !== null && vendors.length > 0){
				self.isLoading = true;
				productCasePackApi.updateVendorInfo(vendors, self.loadDataAfterSave, self.fetchError);
			}
		};

		/**
		 * Get value vendor changed.
		 * @param original
		 * @param current
		 * @returns {{}}
		 */
		self.getVendorChanges = function(current, original) {
			var temp = {};
			for (var key in current) {
				if (!current.hasOwnProperty(key)) continue;
				if(angular.toJson(current[key]) !== angular.toJson(original[key])) {
					temp[key] = current[key];
				}
			}
			temp.key = current.key;
			temp.itemMaster = current.itemMaster;
			if(original.palletQuantity !== current.palletQuantity && (current.palletQuantity === null || current.palletQuantity === "")){
				temp.palletQuantity = ZERO_NUMBER;
			}
			return temp;
		};

		/**
		 * Validate selected vendor info before save/change vendor.
		 */
		self.validateVendorInfo = function (selectedVendor, selectedVendorOrig) {
			var validationMessage = [];
			if (selectedVendor.vendItemId !== selectedVendorOrig.vendItemId && (selectedVendor.vendItemId === null || selectedVendor.vendItemId === '')) {
				validationMessage.push(VENDOR_PRODUCT_CODE_MANDATORY_FIELD_MESSAGE);
			}
			if (!self.isDsd && !self.emptyVendor) {
				if(selectedVendor.nestCube !==  selectedVendorOrig.nestCube && selectedVendor.nestCube && (!self.regexDecimal.test(selectedVendor.nestCube) || selectedVendor.nestCube > 999999.999)){
					validationMessage.push(NET_CUBE_FORMAT_FIELD_MESSAGE);
				}
				if (selectedVendor.length !== selectedVendorOrig.length && (selectedVendor.length === null || selectedVendor.length === '')) {
					validationMessage.push(MASTER_LENGTH_MANDATORY_FIELD_MESSAGE);
				} else if(selectedVendor.length !==  selectedVendorOrig.length && selectedVendor.length && (!self.regexDecimal2.test(selectedVendor.length) || selectedVendor.length > 99999.99)){
					validationMessage.push(MASTER_LENGTH_FORMAT_FIELD_MESSAGE);
				}
				if (selectedVendor.width !== selectedVendorOrig.width && (selectedVendor.width === null || selectedVendor.width === '')) {
					validationMessage.push(MASTER_WIDTH_MANDATORY_FIELD_MESSAGE);
				} else if(selectedVendor.width !==  selectedVendorOrig.width && selectedVendor.width && (!self.regexDecimal2.test(selectedVendor.width) || selectedVendor.width > 99999.99)){
					validationMessage.push(MASTER_WIDTH_FORMAT_FIELD_MESSAGE);
				}
				if (selectedVendor.height !== selectedVendorOrig.height && (selectedVendor.height === null || selectedVendor.height === '')) {
					validationMessage.push(MASTER_HEIGHT_MANDATORY_FIELD_MESSAGE);
				} else if(selectedVendor.height !==  selectedVendorOrig.height && selectedVendor.height && (!self.regexDecimal2.test(selectedVendor.height) || selectedVendor.height > 99999.99)){
					validationMessage.push(MASTER_HEIGHT_FORMAT_FIELD_MESSAGE);
				}
				if (selectedVendor.weight !== selectedVendorOrig.weight && (selectedVendor.weight === null || selectedVendor.weight === '')) {
					validationMessage.push(MASTER_WEIGHT_MANDATORY_FIELD_MESSAGE);
				} else if(selectedVendor.weight !==  selectedVendorOrig.weight && selectedVendor.weight && (!self.regexDecimal2.test(selectedVendor.weight) || selectedVendor.weight > 99999.99)){
					validationMessage.push(MASTER_WEIGHT_FORMAT_FIELD_MESSAGE);
				}
			}
			if (validationMessage !== null && validationMessage.length > 0) {
				self.errorValidate = validationMessage;
				return false;
			}
			return true;
		}

		/**
		 * handle after save vendor information successfully.
		 */
		self.loadDataAfterSave = function(result) {
			self.success = result.message;
			self.isLoading = false;
			self.getData();
		};

		/**
		 * Resets everything back to the way it was loaded in.
		 */
		self.resetSelectedVendor = function() {
			self.success = null;
			self.error = null;
			self.errorValidate = null;
			//If vendor product code has been changed. When reset an vendor, we will reset vendor product code value
			// for another vendor tie to item code.
			if(self.selectedVendor !== null && self.selectedVendor !== undefined && self.vendorListOrig !== null
				&& self.vendorListOrig[self.currentIndex] !== null && self.vendorListOrig[self.currentIndex] !== undefined){
				if(self.selectedVendor.vendItemId !== self.vendorListOrig[self.currentIndex].vendItemId){
					for(var i =0; i < self.vendorList.length; i++){
						self.vendorList[i].vendItemId = angular.copy(self.vendorListOrig[i].vendItemId);
					}
				}
			}
			//Reset all value has been changed for current vendor, that was selected.
			self.vendorList[self.currentIndex] = angular.copy(self.vendorListOrig[self.currentIndex]);
			self.selectedVendor = self.vendorList[self.currentIndex];
		};

		/**
		 * Checks to see whether it is editable or not.
		 * @returns {boolean}
		 */
		self.isEditable = function() {
			return self.isDsd && self.isEditingVendor;
		};

		/**
		 * Call ProductDetailAuditModal's open() method, passing in the function to retrieve vendor audit information,
		 * the selected vendor's key, and 'Vendor' for title.
		 *
		 */
		self.showVendorAuditInfo = function () {
			self.getVendorAuditInfo = productCasePackApi.getVendorAuditInformation;
			ProductDetailAuditModal.open(self.getVendorAuditInfo, self.selectedVendor.key, self.title);
		};

		/**
		 * Get Candidate Information.
		 */
		self.getCandidateInformation = function (psWorkId) {
			productCasePackApi.getCandidateInformation({
					psWorkId: psWorkId
				},
				//success case
				function (results) {
					if(results != null && results.length > 0){
						self.onCandidateStatusChange({psItemMasters: results});
					}
				},
				//error case
				function(errors){
					self.fetchError(errors)
				}
			);
		};

		/**
		 * Handle add warehouse when click on "Add to Warehouse" button. Check product exist candidate associated.
		 * If exist change mode to Morph screen allow user handle data. Else not exist get list of warehouse basing
		 * to class and bicep vendor, and show on pop up, allow user select.
		 */
		self.addToWarehouseHandle = function () {
			self.checkAllFlag = false;
			self.errorPopup = '';
			self.bicepVendors =  [];
			productCasePackApi.findAddToWarehouseCandidate({
					prodId: self.productMaster.prodId
				},
				//success case
				function (results) {
					if(results && results.data == 0){
						self.isPopupLoading = true;
						$("#addToWhsModal").modal({backdrop: 'static',keyboard: true});
						self.isDisabledAdd = false;
						var apVendor = 0;
						if(self.isDsd){
							apVendor = self.selectedVendor.key.vendorNumber;
						}else{
							apVendor = self.selectedVendor.location.apVendorNumber;
						}
						productCasePackApi.findBicepVendorsByApVendorAndClass({
								apVendor: apVendor,
								classCode: self.productMaster.classCode
							},
							//success case
							function (results) {
								self.isPopupLoading = false;
								angular.forEach(results, function(value) {
									if(!self.isDsd && self.findWarehouseInItemWhse(value.warehouseNumber)){
										value["selected"] = true;
										value["disable"] = true;
									}else {
										value["selected"] = false;
										value["disable"] = false;
									}
									self.bicepVendors.push(value);
								});
								self.refreshCheckAllFlagAfterLoadData();
							},
							//error case
							function(errors){
								self.isPopupLoading = false;
								self.isDisabledAdd = true;
								if(errors && errors.data) {
									if (errors.data.message) {
										self.errorPopup = errors.data.message;
									} else {
										self.errorPopup = errors.data.error;
									}
								}
								else {
									self.errorPopup = "An unknown error occurred.";
								}
							}
						);
					}else{
						self.getCandidateInformation(results.data);
					}
				},
				//error case
				function(errors){
					self.fetchError(errors)
				}
			);
		}

		/**
		 * Finding warehouse exist in list warehouse item location of current item. If exist ignore this warehouse.
		 * Disable check box, don't allow use add it again.
		 * @param warehouseNbr
		 * @returns {boolean}
		 */
		self.findWarehouseInItemWhse = function (warehouseNbr) {
			var flag = false;
			if(self.itemMaster && self.itemMaster.warehouseLocationItems){
				for(var i = 0; i< self.itemMaster.warehouseLocationItems.length; i++){
					if(self.itemMaster.warehouseLocationItems[i].key.warehouseNumber == Number(warehouseNbr)){
						flag = true;
						break;
					}
				}
			}
			return flag;
		}

		/**
		 * Refresh flag check all in header grid. This flag show to user know, all row in current page has been
		 * selected.
		 */
		self.refreshCheckAllFlag = function () {
			self.checkAllFlag = true;
			angular.forEach(self.bicepVendors, function(value) {
				if(value.selected == false){
					self.checkAllFlag = false;
					return;
				}
			});
		};

		/**
		 * Refresh flag check all in header grid. This flag show to user know, all row in current page has been
		 * selected.
		 */
		self.refreshCheckAllFlagAfterLoadData = function () {
			self.checkAllFlag = true;
			var check = true;
			angular.forEach(self.bicepVendors, function(value) {
				if(value.selected == false){
					self.checkAllFlag = false;
				}
				if(value.disable == false){
					check = false;
				}
			});
			self.isDisabledAdd = check;
		};

		/**
		 * User checked/unchecked in checkbox on header data tables.
		 */
		self.checkAllHandle = function () {
			angular.forEach(self.bicepVendors, function(value) {
				if(value.disable == false){
					value.selected = self.checkAllFlag;
				}
			});
		};

		/**
		 * Click on Add to Warehouse button handle.
		 */
		self.doAddToWarehouse = function () {
			self.errorPopup = '';
			var bicepVendorsSelected = [];
			var apVendor = 0;
			var bicepVendorNumber = 0;
			if(self.isDsd){
				apVendor = self.selectedVendor.key.vendorNumber;
				bicepVendorNumber = self.selectedVendor.key.vendorNumber;
			}else{
				apVendor = self.selectedVendor.location.apVendorNumber;
				bicepVendorNumber = self.selectedVendor.key.vendorNumber;
			}
			angular.forEach(self.bicepVendors, function(value) {
				if(value.selected && value.disable == false){
					value["apVendor"] = apVendor;
					value["bicepVendorNumber"] = bicepVendorNumber;
					value["productId"] = self.productMaster.prodId;
					value["itemId"]	= self.itemMaster.key.itemCode;
					value["itemType"] = self.itemMaster.key.itemType;
					bicepVendorsSelected.push(angular.copy(value));
				}
			});
			if(bicepVendorsSelected != null && bicepVendorsSelected.length > 0){
				self.doCopyItemDataToPsTable(bicepVendorsSelected);
			}else{
				self.errorPopup = "Please selects at least a Warehouse to add";
			}
		}

		/**
		 * Call to back end do copy item data to ps table.
		 * @param bicepVendorsSelected
		 */
		self.doCopyItemDataToPsTable = function (bicepVendorsSelected) {
			self.isPopupLoading = true;
			productCasePackApi.copyItemDataToPsTable(bicepVendorsSelected,
				//success case
				function (results) {
					self.isPopupLoading = false;
					self.closeAddToWhsModal();
					if(results && results.data == 0){
						self.setError(results.message);
					}else{
						self.getCandidateInformation(results.data);
					}
				},
				//error case
				function(errors){
					self.closeAddToWhsModal();
					self.fetchError(errors)
				}
			);
		};
        /**
		 * This method wil ensure that the move/link modal closes cleanly.
		 */
		self.closeAddToWhsModal = function(){
			$('#addToWhsModal').modal('hide');
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
		};
		/**
		 * This determines whether or not the save is disabled.
		 * @returns {boolean}
		 */
		self.isSaveDisabled = function() {
			if(angular.toJson(self.vendorList) !== angular.toJson(self.vendorListOrig)){
				$rootScope.contentChangedFlag = true;
				return true;
			} else {
				$rootScope.contentChangedFlag = false;
				return false;
			}
		}
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
	}
})();
