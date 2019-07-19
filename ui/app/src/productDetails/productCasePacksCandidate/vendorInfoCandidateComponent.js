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

	angular.module('productMaintenanceUiApp').component('vendorInfoCandidate', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			onCandidateStatusChange : '&',
			onCandidateTabChange : '&',
			productMaster: '<',
			psItemMasters: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacksCandidate/vendorInfoCandidate.html',
		// The controller that handles our component logic
		controller: VendorInfoCandidateController
	});

	VendorInfoCandidateController.$inject = ['ProductCasePackCandidateApi', 'ProductDetailAuditModal', '$rootScope', 'ProductSearchService', 'HomeSharedService'];

	/**
	 * Vendor info component's controller definition.
	 * @constructor
	 * @param productCasePackApi
	 * @param ProductDetailAuditModal
	 */
	function VendorInfoCandidateController(productCasePackCandidateApi, ProductDetailAuditModal, $rootScope, productSearchService, homeSharedService) {

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

		self.orderQuantityRestrictionList = [
			{displayName: "NO-RESTRICTIONS[N]", id: "N"},
			{displayName: "VENDOR-PALLET[P]", id: "P"},
			{displayName: "VEN-TIE[T]", id: "T"},
			{displayName: "WAREHOUSE-PALLET[W]", id: "W"}
		];

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
		 * The index of the current bicep vendor.
		 * @type {number}
		 */
		self.index = 0;

		/**
		 * Component will reload the vendor data whenever the item is changed in casepack.
		 */
		this.$onChanges=function(){
			self.isEditingVendor = true;
			self.index = 0;
			$rootScope.contentChangedFlag = true;
			//self.currentItemCode = self.itemMaster.key.itemCode;
			self.getData();
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
			productCasePackCandidateApi.getCandidateInformation({psItmId: self.psItemMasters[0].candidateItemId}, self.loadData, self.fetchError);
		};

		/**
		 * Loads the data sent back from the back end.
		 * @param result
		 */
		self.loadData = function(results) {
			self.isLoading = false;
			self.psItemMasters = results;
			var candidateVendorLocationItems = angular.copy(results[0].candidateVendorLocationItems);

			if(candidateVendorLocationItems.length != 0) {
				self.isVendorInfoAvailable = true;
				self.error = null;
				if (candidateVendorLocationItems.length > 0) {
					self.vendorList = self.handleResultsDataBeforeAssign(results);
					self.selectedVendor = self.vendorList[self.index];
					//self.selectedVendor = self.vendorList[0];
				} else {
					self.emptyVendor = true;
					self.isLoading = false;
					return;
				}
				self.resettableVendor = angular.copy(self.selectedVendor);
				self.retail = self.selectedVendor.retail;
				//self.retail = self.selectedVendor.itemMaster.retail;

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
					self.selectedWarehouseLocationItem = self.selectedVendor.selectedWarehouseLocationItem;
					//self.selectedWarehouseLocationItem = self.selectedVendor.itemWarehouseVendorList[0].warehouseLocationItem;
				} else {
					self.isDsd = true;
					self.calculateUnitCost(self.selectedVendor.packQuantity);
				}
				self.calculateGrossMargin();
				self.isReliable = false;
				//self.isReliable = self.isReliableInventory();
			}

		};

		/**
		 * Convert data from CandidateVendorLocationItem to VendorLocationItem before use.
		 *
		 * @param results The data returned by the backend.
		 *
		 * @return the processed data.
		 */
		self.handleResultsDataBeforeAssign = function(psItemMasters){
			//OLD: List<VendorLocationItem>
			//NEW: List<CandidateVendorLocationItem>
			var psItemMaster = angular.copy(psItemMasters[0]);
			var candidateVendorLocationItems = angular.copy(psItemMasters[0].candidateVendorLocationItems);
			var vendorList = [];
			angular.forEach(candidateVendorLocationItems, function(candidateVendorLocationItem){
				var item = {};
				item.displayBicepVendorName = candidateVendorLocationItem.displayName;
				item.location = candidateVendorLocationItem.location;
				item.vendItemId = candidateVendorLocationItem.vendItemId;
				item.costOwner = candidateVendorLocationItem.costOwner;
				item.palletQuantity = candidateVendorLocationItem.palletQuantity;
				item.country = candidateVendorLocationItem.country;
				item.palletSize = candidateVendorLocationItem.palletSize;
				item.orderQuantityRestrictionCode = candidateVendorLocationItem.orderQuantityRestrictionCode;
				item.costLinkId = candidateVendorLocationItem.costLinkId;
				item.sca = candidateVendorLocationItem.sca;
				item.tie = candidateVendorLocationItem.tie;
				item.tier = candidateVendorLocationItem.tier;
				item.packQuantity = psItemMaster.masterPackQuantity;
				item.nestCube = psItemMaster.shipNestCube;
				item.length = psItemMaster.length;
				item.nestMax = psItemMaster.shipNestMaxQuantity;
				item.width = psItemMaster.width;
				item.height = psItemMaster.height;
				item.weight = psItemMaster.weight;
				item.cube = psItemMaster.cube;

				if(psItemMaster.candidateWorkRequest.candidateProductMaster[0].retailX4Qty === null &&
						psItemMaster.candidateWorkRequest.candidateProductMaster[0].retailPriceAmount === null){
					item.retail = null;
				}else{
					item.retail = {};
					item.retail.xFor = psItemMaster.candidateWorkRequest.candidateProductMaster[0].retailX4Qty;
					item.retail.retail = psItemMaster.candidateWorkRequest.candidateProductMaster[0].retailPriceAmount;
				}
				item.cost = {};
				item.cost.cost = candidateVendorLocationItem.listCost;
				item.vendorLocation = candidateVendorLocationItem.vendorLocation;

				item.key = {};
				item.key.itemCode = psItemMaster.itemCode;
				item.key.itemType = psItemMaster.itemKeyType;
				item.key.vendorType = candidateVendorLocationItem.key.vendorType;
				item.key.vendorNumber = candidateVendorLocationItem.key.vendorNumber;

				item.selectedWarehouseLocationItem = {};
				item.selectedWarehouseLocationItem.shipLength = psItemMaster.shipLength;
				item.selectedWarehouseLocationItem.shipWidth = psItemMaster.shipWidth;
				item.selectedWarehouseLocationItem.shipHeight = psItemMaster.shipHeight;
				item.selectedWarehouseLocationItem.shipWeight = psItemMaster.shipWeight;
				item.selectedWarehouseLocationItem.shipCube = psItemMaster.shipCube;

				item.newDataSw = candidateVendorLocationItem.newData;

				vendorList.push(item);
			});
			self.itemMaster.key.dsd = (psItemMaster.itemKeyType === "DSD");

			return vendorList;
		};

		/**
		 * Convert data from ItemMaster to PsItemMaster.
		 *
		 * @return the PsItemMaster.
		 */
		self.getPsItemMasterData = function(){
			//OLD: VendorLocationItem
			//NEW: CandidateVendorLocationItem
			var tempVendor = angular.copy(self.selectedVendor);
			var psItemMaster = angular.copy(self.psItemMasters[0]);
			var candidateVendorLocationItem = angular.copy(self.psItemMasters[0].candidateVendorLocationItems[self.index]);

			psItemMaster.shipNestCube = tempVendor.nestCube;
			psItemMaster.shipNestMaxQuantity = tempVendor.nestMax;
			psItemMaster.length = tempVendor.length;
			psItemMaster.width = tempVendor.width;
			psItemMaster.height = tempVendor.height;
			psItemMaster.weight = tempVendor.weight;

			psItemMaster.shipLength = tempVendor.selectedWarehouseLocationItem.shipLength;
			psItemMaster.shipWidth = tempVendor.selectedWarehouseLocationItem.shipWidth;
			psItemMaster.shipHeight = tempVendor.selectedWarehouseLocationItem.shipHeight;
			psItemMaster.shipWeight = tempVendor.selectedWarehouseLocationItem.shipWeight;

			psItemMaster.candidateVendorLocationItems = [];
			candidateVendorLocationItem.vendItemId = tempVendor.vendItemId;
			candidateVendorLocationItem.palletQuantity = tempVendor.palletQuantity;
			candidateVendorLocationItem.orderQuantityRestrictionCode = tempVendor.orderQuantityRestrictionCode;
			candidateVendorLocationItem.costLinkId = tempVendor.costLinkId;
			candidateVendorLocationItem.tie = tempVendor.tie;
			candidateVendorLocationItem.tier = tempVendor.tier;
			psItemMaster.candidateVendorLocationItems.push(candidateVendorLocationItem);

			return psItemMaster;
		};

		/**
		 * Returns true if newDataSw is N.
		 * @returns {boolean}
		 */
		self.isDisabledMode = function(){
			var check = false;
			if(self.selectedVendor != null && !self.selectedVendor.newDataSw){
				check = true;
			}
			return check;
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
		self.handleVendorListChange = function(vendor, index) {
			self.error = null;
			self.success = null;
			self.index = index;
			self.selectedVendor = vendor;
			self.resettableVendor = angular.copy(self.selectedVendor);
			self.retail = self.selectedVendor.retail;
			self.shipPackQuantity = self.itemMaster.pack;
			if (self.selectedVendor.cost == null) {
				self.listCost = null;
			} else {
				self.listCost = self.selectedVendor.cost.cost;
			}
			if(self.itemMaster.key.dsd === false) {
				self.isDsd = false;
				self.isMixedClassVendor = self.selectedVendor.vendorLocation.mixedClassVendorSwitch;
				//console.log(self.isMixedClassVendor);
				self.calculateUnitCost(self.shipPackQuantity);
				self.selectedWarehouseLocationItem = self.selectedVendor.selectedWarehouseLocationItem;
				//self.selectedWarehouseLocationItem = self.selectedVendor.itemWarehouseVendorList[0].warehouseLocationItem;
			} else {
				self.isDsd = true;
				self.calculateUnitCost(self.selectedVendor.packQuantity);
			}
			self.calculateGrossMargin();
			self.isReliable = false;
			//self.isReliable = self.isReliableInventory();
		};

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
		 * This method to validation data before call save.
		 */
		self.validation = function (item) {
			var errorMessage = [];
			var regexInt = new RegExp("^[0-9]{1,9}?$");
			var regexNetCube = new RegExp("^[0-9]{1,6}(\.[0-9]{0,3})?$");
			var regexMaster = new RegExp("^[0-9]{1,2}(\.[0-9]{0,1})?$");
			var regexMasterWeight = new RegExp("^[0-9]{1,5}(\.[0-9]{0,2})?$");

			if(item.palletQuantity === undefined){
				errorMessage.push("Vendor Pallet Quantity must be a numeric value");
			}else{
				if(item.palletQuantity && !regexInt.test(item.palletQuantity)){
					errorMessage.push("Vendor Pallet Quantity must be a numeric value");
				}
			}
			if(item.costLinkId === undefined){
				errorMessage.push("Cost Link ID must be a numeric value");
			}else{
				if(item.costLinkId && !regexInt.test(item.costLinkId)){
					errorMessage.push("Cost Link ID must be a numeric value");
				}
			}
			if(item.nestMax === undefined){
				errorMessage.push("Net Max Qty must be a numeric value");
			}else{
				if(item.nestMax && !regexInt.test(item.nestMax)){
					errorMessage.push("Net Max Qty must be a numeric value");
				}
			}
			if(item.tie === undefined){
				errorMessage.push("Vendor Tie must be a numeric value");
			}else{
				if(item.tie && !regexInt.test(item.tie)){
					errorMessage.push("Vendor Tie must be a numeric value");
				}
			}
			if(item.tier === undefined){
				errorMessage.push("Vendor Tier must be a numeric value");
			}else{
				if(item.tier && !regexInt.test(item.tier)){
					errorMessage.push("Vendor Tier must be a numeric value");
				}
			}

			if(item.nestCube === undefined){
				errorMessage.push("Net Cube value must be greater or equal 0 and less than or equal to 999999.999");
			}else{
				if(item.nestCube && !regexNetCube.test(item.nestCube)){
					errorMessage.push("Net Cube value must be greater or equal 0 and less than or equal to 999999.999");
				}
			}

			if(item.length === undefined || item.length == 0){
				errorMessage.push("Master Length is a mandatory field, greater than 0 and less than or equal to 99.9");
			}else{
				if(!regexMaster.test(item.length)){
					errorMessage.push("Master Length is a mandatory field, greater than 0 and less than or equal to 99.9");
				}
			}
			if(item.width === undefined || item.width == 0){
				errorMessage.push("Master Width is a mandatory field, greater than 0 and less than or equal to 99.9");
			}else{
				if(!regexMaster.test(item.width)){
					errorMessage.push("Master Width is a mandatory field, greater than 0 and less than or equal to 99.9");
				}
			}
			if(item.height === undefined || item.height == 0){
				errorMessage.push("Master Height is a mandatory field, greater than 0 and less than or equal to 99.9");
			}else{
				if(!regexMaster.test(item.height)){
					errorMessage.push("Master Height is a mandatory field, greater than 0 and less than or equal to 99.9");
				}
			}

			if(item.selectedWarehouseLocationItem.shipLength === undefined || item.selectedWarehouseLocationItem.shipLength == 0){
				errorMessage.push("Ship Length is a mandatory field, greater than 0 and less than or equal to 99.9");
			}else{
				if(!regexMaster.test(item.selectedWarehouseLocationItem.shipLength)){
					errorMessage.push("Ship Length is a mandatory field, greater than 0 and less than or equal to 99.9");
				}
			}
			if(item.selectedWarehouseLocationItem.shipWidth === undefined || item.selectedWarehouseLocationItem.shipWidth == 0){
				errorMessage.push("Ship Width is a mandatory field, greater than 0 and less than or equal to 99.9");
			}else{
				if(!regexMaster.test(item.selectedWarehouseLocationItem.shipWidth)){
					errorMessage.push("Ship Width is a mandatory field, greater than 0 and less than or equal to 99.9");
				}
			}
			if(item.selectedWarehouseLocationItem.shipHeight === undefined || item.selectedWarehouseLocationItem.shipHeight == 0){
				errorMessage.push("Ship Height is a mandatory field, greater than 0 and less than or equal to 99.9");
			}else{
				if(!regexMaster.test(item.selectedWarehouseLocationItem.shipHeight)){
					errorMessage.push("Ship Height is a mandatory field, greater than 0 and less than or equal to 99.9");
				}
			}

			if(item.weight === undefined || item.weight == 0){
				errorMessage.push("Master Weight is a mandatory field, greater than 0 and less than or equal to 99999.99");
			}else{
				if(!regexMasterWeight.test(item.weight)){
					errorMessage.push("Master Weight is a mandatory field, greater than 0 and less than or equal to 99999.99");
				}
			}
			if(item.selectedWarehouseLocationItem.shipWeight === undefined || item.selectedWarehouseLocationItem.shipWeight == 0){
				errorMessage.push("Ship Weight is a mandatory field, greater than 0 and less than or equal to 99999.99");
			}else{
				if(!regexMasterWeight.test(item.selectedWarehouseLocationItem.shipWeight)){
					errorMessage.push("Ship Weight is a mandatory field, greater than 0 and less than or equal to 99999.99");
				}
			}
			return errorMessage;
		};

		/**
		 * Updates the selected Vendor.
		 */
		self.updateSelectedVendor = function() {
			self.error = null;
			self.success = null;
			var errorMessage = self.validation(self.selectedVendor);
			if(errorMessage == null || errorMessage == ''){
				self.isLoading = true;
				var psItemMaster = self.getPsItemMasterData();
				productCasePackCandidateApi.updateCasePackVendorCandidate(psItemMaster, self.loadDataAfterSave, self.fetchError);
			}else{
				self.error = 'Case Pack - Vendor:';
				angular.forEach(errorMessage, function (value) {
					self.error += "<li>" +value + "</li>";
				})
			}
		};

		/**
		 * handle after save vendor information successfully.
		 */
		self.loadDataAfterSave = function(result) {
			self.success = result.message;
			self.loadData(result.data);
			//self.getData();
		};

		/**
		 * Resets everything back to the way it was loaded in.
		 */
		self.resetSelectedVendor = function() {
			self.selectedVendor = angular.copy(self.resettableVendor);
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
			self.getVendorAuditInfo = productCasePackCandidateApi.getVendorAuditInformation;
			ProductDetailAuditModal.open(self.getVendorAuditInfo, self.selectedVendor.key, self.title);
		};

		/**
		 * Get Candidate Information.
		 */
		self.getCandidateInformation = function (psWorkId) {
			productCasePackCandidateApi.getCandidateInformation({
				psWorkId: psWorkId
				},
				//success case
				function (results) {
					if(results != null){
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
		};

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
			var bicepVendorLocNbr = 0;
			if(self.isDsd){
				apVendor = self.selectedVendor.key.vendorNumber;
				bicepVendorLocNbr = self.selectedVendor.key.vendorNumber;
			}else{
				apVendor = self.selectedVendor.location.apVendorNumber;
				bicepVendorLocNbr = self.selectedVendor.key.vendorNumber;
			}
			angular.forEach(self.bicepVendors, function(value) {
				if(value.selected && value.disable == false){
					value["apVendor"] = apVendor;
					value["bicepVendorLocNbr"] = bicepVendorLocNbr;
					bicepVendorsSelected.push(angular.copy(value));
				}
			});
			if(bicepVendorsSelected){
				self.doCopyItemDataToPsTable(bicepVendorsSelected);
			}else{
				self.errorPopup = "Please selects at least a Warehouse to add";
			}
		};

		/**
		 * Call to back end do copy item data to ps table.
		 * @param bicepVendorsSelected
		 */
		self.doCopyItemDataToPsTable = function (bicepVendorsSelected) {
			self.isPopupLoading = true;
			productCasePackCandidateApi.copyItemDataToPsTable(bicepVendorsSelected,
				//success case
				function (results) {
					self.isPopupLoading = false;
					//call show morph (Hanh' method)
				},
				//error case
				function(errors){
					self.fetchError(errors)
				}
			);
		};

		/**
		 * User click yes on confirm message popup.
		 */
		self.yesConfirmAction = function () {
			$('#vendorRejectCandidateModal').modal('hide');
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
			$('#vendorRejectCandidateModal').modal({backdrop: 'static', keyboard: true});
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
			self.error = null;
			self.success = null;
			if(self.isDifference()){
				var errorMessage = self.validation(self.selectedVendor);
				if(errorMessage == null || errorMessage == ''){
					self.isLoading = true;
					var psItemMaster = self.getPsItemMasterData();
					productCasePackCandidateApi.updateCasePackVendorCandidate(psItemMaster,
							function (results) {
						self.isLoading = false;
						self.success = results.message;
						self.onCandidateTabChange({tabId: "casePackImportTab"});
					},
					self.fetchError);
				}else{
					self.error = 'Case Pack - Vendor:';
					angular.forEach(errorMessage, function (value) {
						self.error += "<li>" +value + "</li>";
					});
				}
			}else{
				self.onCandidateTabChange({tabId: "casePackImportTab"});
			}
		};

		/**
		 * Change to back case pack tab in candidate mode.
		 */
		self.backCandidate = function () {
			self.error = null;
			self.success = null;
			if(self.isDifference()){
				var errorMessage = self.validation(self.selectedVendor);
				if(errorMessage == null || errorMessage == ''){
					self.isLoading = true;
					var psItemMaster = self.getPsItemMasterData();
					productCasePackCandidateApi.updateCasePackVendorCandidate(psItemMaster,
							function (results) {
						self.isLoading = false;
						self.success = results.message;
						self.onCandidateTabChange({tabId: "warehouseTab"});
					},
					self.fetchError);
				}else{
					self.error = 'Case Pack - Vendor:';
					angular.forEach(errorMessage, function (value) {
						self.error += "<li>" +value + "</li>";
					})
				}
			}else{
				self.onCandidateTabChange({tabId: "warehouseTab"});
			}
		};

		/**
		 * Returns true if there's been a change made.
		 * @returns {boolean}
		 */
		self.isDifference = function(){
			if(angular.toJson(self.selectedVendor) !== angular.toJson(self.resettableVendor)) {
				$rootScope.contentChangedFlag = true;
				return true;
			} else{
				$rootScope.contentChangedFlag = false;
				return false;
			}
			return angular.toJson(self.selectedVendor) !== angular.toJson(self.resettableVendor);
		};
	}
})();
