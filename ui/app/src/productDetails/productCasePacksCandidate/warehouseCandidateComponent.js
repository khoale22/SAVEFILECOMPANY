/*
 *   warehouseComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * CasePack ->warehouse page component.
 *
 * @author s753601
 * @since 2.8.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('warehouseCandidate', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			psItemMasters: '<',
			onCandidateTabChange : '&'
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacksCandidate/warehouseCandidate.html',
		// The controller that handles our component logic
		controller: WarehouseCandidateController
	});

	WarehouseCandidateController.$inject = ['ProductFactory', 'PMCommons', '$scope', 'ProductCasePackCandidateApi', '$timeout', 'ngTableParams', '$filter', '$rootScope', 'ProductSearchService', 'HomeSharedService'];

	/**
	 * Case Pack Warehouse component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @constructor
	 */
	function WarehouseCandidateController(productFactory, PMCommons, $scope, productCasePackCandidateApi, $timeout, ngTableParams, $filter, $rootScope, productSearchService, homeSharedService) {
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
		/**
		 * Flags for wether or not a panel is open or closed.
		 * @type {boolean}
		 */
		self.orderingInfoVisibility=true;
		self.weightVisibility = true;
		self.costVisibility = true;
		self.wHSTieTierSlotVisibility = true;
		self.changeLogVisibility = true;

		/**
		 *Token that is used to keep track of the current request so that old requests will be ignored.
		 * @type {number}
		 */
		self.latestRequest=0;

		/**
		 * Array that holds the warehouse item locations
		 * @type {Array}
		 */
		self.data=[];

		/**
		 * Array that to backup the warehouse item locations
		 * @type {Array}
		 */
		self.dataOrig=[];

		/**
		 * Array that hold the current warehouse item location's remarks.
		 * @type {Array}
		 */
		self.remarkData=[];
		/**
		 * Array that hold the original warehouse item location's remarks.
		 * @type {Array}
		 */
		self.originalRemarkData=[];

		/**
		 * Array that holds the orderQuantityTypes
		 * @type {Array}
		 */
		self.orderQuantityTypes=[];

		/**
		 * Array that holds the flow types
		 * @type {Array}
		 */
		self.flowTypes=[];

		/**
		 * flag to state if there is a request for additional data
		 * @type {boolean}
		 */
		self.isLoading= false;

		/**
		 * Flag used to state there is a request for comment data.
		 * @type {boolean}
		 */
		self.isWaitingForCommentResponse = false;

		/**
		 * Object warehouseLocationitem Selected.
		 * @type {{}}
		 */
		self.warehouseLocationItemSeleted={};

		/**
		 * RegExp check number is integer type, and max value is 999999999
		 * @type {{}}
		 */
		self.regexInt = new RegExp("^[0-9]{1,9}$");

		/**
		 * RegExp check number is decimal type (#####.####), and max value is 99999.9999
		 * @type {{}}
		 */
		self.regexDecimal = new RegExp("^[0-9]{1,5}\.?([0-9]{1,4})?$");

		/**
		 * Array contain Remark and Comment change.
		 * @type {Array}
		 */
		self.itemWarehouseCommentsUpdate = [];

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			productCasePackCandidateApi.getFlowTypes(self.loadFlowTypes, self.fetchError);
			productCasePackCandidateApi.getOrderQuantityTypes(self.loadOrderQuantityTypes, self.fetchError)
		};

		/**
		 * Component ngOnChanges lifecycle hook. Called whenever a binding variable is changed.
		 */
		this.$onChanges = function (){
			self.isLoading=true;
			var thisRequest = ++self.latestRequest;
			productCasePackCandidateApi.getCandidateInformation({psItmId: self.psItemMasters[0].candidateItemId}, self.loadData, self.fetchError);
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};

		/**
		 * This method will allow the panels to toggle from visible to hidden.
		 * @param segment which panel was clicked.
		 */
		self.toggleVisibility = function (panelNumber) {
			switch(panelNumber){
				case 1:
					self.orderingInfoVisibility = !self.orderingInfoVisibility;
					break;
				case 2:
					self.weightVisibility = !self.weightVisibility;
					break;
				case 3:
					self.costVisibility = !self.costVisibility;
					break;
				case 4:
					self.wHSTieTierSlotVisibility = !self.wHSTieTierSlotVisibility;
					break;
				case 5:
					self.changeLogVisibility = !self.changeLogVisibility;
				default:
					break;
			}
		};

		/**
		 * Fetches the error from the back end.
		 * @param error
		 */
		self.fetchError = function(error) {
			self.isLoading = false;
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
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.error = error;
		};

		/**
		 * After getting the data from the API this method will begin the view construction.
		 * @param results the warehouse location items from the API
		 */
		self.loadData = function (results) {
			self.isLoading = false;
			self.psItemMasters = results;
			//self.isWaitingForWarehouseLocationItems = false;
			self.data = self.handleResultsDataBeforeAssign(results);
			//self.data = results;
			self.dataOrig = angular.copy(self.data);
			//self.setPanelWidths();
			//self.checkLoadStatus();
		};

		/**
		 * Process data before use.
		 *
		 * @param results The data returned by the backend.
		 * @return the processed data.
		 */
		self.handleResultsDataBeforeAssign = function(psItemMasters){
			//OLD: List<WarehouseLocationItem>
			//NEW: List<CandidateWarehouseLocationItem>
			var psItemMaster = angular.copy(psItemMasters[0]);
			var candidateWarehouseLocationItems = angular.copy(psItemMasters[0].candidateWarehouseLocationItems);
			var candidateVendorLocationItems = angular.copy(psItemMasters[0].candidateVendorLocationItems);
			var whseList = [];
			angular.forEach(candidateWarehouseLocationItems, function(candidateWarehouseLocationItem){
				var item = {};

				item.location = candidateWarehouseLocationItem.location;
				item.orderQuantityTypeDisplayName = candidateWarehouseLocationItem.orderQuantityTypeDisplayName;
				item.orderQuantityTypeCode = candidateWarehouseLocationItem.orderQuantityTypeCode;
				if(candidateVendorLocationItems != null && candidateVendorLocationItems.length > 0){
					item.expectedWeeklyMovement = candidateVendorLocationItems[0].expctWklyMvt;
				}else{
					item.expectedWeeklyMovement = null;
				}
				item.whseMaxShipQuantityNumber = psItemMaster.maxShipQuantity;
				item.unitFactor1 = candidateWarehouseLocationItem.unitFactor1 ? candidateWarehouseLocationItem.unitFactor1 : 0;
				item.unitFactor2 = candidateWarehouseLocationItem.unitFactor2 ? candidateWarehouseLocationItem.unitFactor2 : 0;

				item.variableWeight = psItemMaster.variableWeight;
				item.catchWeight = psItemMaster.catchWeight;
				item.averageWeight = candidateWarehouseLocationItem.averageWeight;

				item.billCost = candidateWarehouseLocationItem.billCost ? candidateWarehouseLocationItem.billCost : 0;
				item.averageCost = candidateWarehouseLocationItem.averageCost ? candidateWarehouseLocationItem.averageCost : 0;
				item.warehouseLocationItemExtendedAttributes = {};
				item.warehouseLocationItemExtendedAttributes.lastCost = 0;
				item.warehouseLocationItemExtendedAttributes.priorLastCost = 0;

				item.whseTie = candidateWarehouseLocationItem.whseTie;
				item.whseTier = candidateWarehouseLocationItem.whseTier;
				item.currentSlotNumber = candidateWarehouseLocationItem.currentSlotNumber;
				item.flowTypeCode = candidateWarehouseLocationItem.flowTypeCode;
				item.flowType = candidateWarehouseLocationItem.flowType;

				item.createdOn = candidateWarehouseLocationItem.createdOn;
				item.createdBy = candidateWarehouseLocationItem.createdBy;
				item.lastUpdatedOn = candidateWarehouseLocationItem.lastUpdatedOn;
				item.lastUpdatedId = candidateWarehouseLocationItem.lastUpdatedId;

				item.comment = candidateWarehouseLocationItem.comment;
				item.key = {};
				item.key.itemCode = psItemMaster.itemCode;
				item.key.itemType = psItemMaster.itemKeyType;
				item.key.warehouseNumber = candidateWarehouseLocationItem.key.warehouseNumber;
				item.key.warehouseType = candidateWarehouseLocationItem.key.warehouseType;

				item.newDataSw = candidateWarehouseLocationItem.newData;
				whseList.push(item);
			});
			return whseList;
		};

		/**
		 * Convert data from ItemMaster to PsItemMaster.
		 *
		 * @return the PsItemMaster.
		 */
		self.getPsItemMasterData = function(warehouseLocationItems){
			//OLD: WarehouseLocationItem
			//NEW: CandidateWarehouseLocationItem
			var psItemMaster = angular.copy(self.psItemMasters[0]);
			var candidateWarehouseLocationItems = angular.copy(self.psItemMasters[0].candidateWarehouseLocationItems);
			psItemMaster.candidateWarehouseLocationItems = [];
			angular.forEach(warehouseLocationItems, function(item){
				psItemMaster.maxShipQuantity = item.whseMaxShipQuantityNumber;
				psItemMaster.variableWeight = item.variableWeight;
				angular.forEach(candidateWarehouseLocationItems, function(candidateWarehouseLocationItem){
					if(item.key.warehouseNumber === candidateWarehouseLocationItem.key.warehouseNumber && item.key.warehouseType === candidateWarehouseLocationItem.key.warehouseType){
						candidateWarehouseLocationItem.orderQuantityTypeCode = item.orderQuantityTypeCode;
						candidateWarehouseLocationItem.unitFactor1 = item.unitFactor1;
						candidateWarehouseLocationItem.unitFactor2 = item.unitFactor2;
						candidateWarehouseLocationItem.whseTie = item.whseTie;
						candidateWarehouseLocationItem.whseTier = item.whseTier;
						candidateWarehouseLocationItem.flowTypeCode = item.flowTypeCode;
						psItemMaster.candidateWarehouseLocationItems.push(candidateWarehouseLocationItem);
					}
				});
			});
			return psItemMaster;
		};

		/**
		 * Returns true if newDataSw is N.
		 * @returns {boolean}
		 */
		self.isDisabledMode = function(warehouseItemLocation){
			var check = false;
			if(warehouseItemLocation != null && !warehouseItemLocation.newDataSw){
				check = true;
			}
			return check;
		};

		/**
		 * Change WhseMaxShipQty data of other items.
		 */
		self.changeWhseMaxShipQtyData = function(warehouseItemLocation){
			angular.forEach(self.data, function(item){
				if(item.key.warehouseNumber != warehouseItemLocation.key.warehouseNumber && item.key.warehouseType != warehouseItemLocation.key.warehouseNumber){
					item.whseMaxShipQuantityNumber = angular.copy(warehouseItemLocation.whseMaxShipQuantityNumber);
				}
			});
		};

		/**
		 * Change VariableWeight data of other items.
		 */
		self.changeVariableWeightData = function(warehouseItemLocation){
			angular.forEach(self.data, function(item){
				if(item.key.warehouseNumber != warehouseItemLocation.key.warehouseNumber && item.key.warehouseType != warehouseItemLocation.key.warehouseNumber){
					item.variableWeight = angular.copy(warehouseItemLocation.variableWeight);
				}
			});
		};

		/**
		 * This method takes the order quantity types taken from the database and attaches them to the UI dropdown variable
		 * @param results form requesting for the order quantity types from the api
		 */
		self.loadOrderQuantityTypes = function (results) {
			//self.isWaitingForOrderQuantity=false;
			self.orderQuantityTypes=results;
			//self.checkLoadStatus();
		};

		/**
		 * This method takes the flow types taken from the database and attaches them to the UI dropdown variable
		 * @param results form requesting for the flow types from the api
		 */
		self.loadFlowTypes= function (results) {
			//self.isWaitingForFlows = false;
			self.flowTypes=results;
			//self.checkLoadStatus();
		};

		/**
		 * This method insures that all the data is present before displaying the results
		 */
		self.checkLoadStatus=function () {
			//self.isLoading=self.isWaitingForFlows||self.isWaitingForWarehouseLocationItems||self.isWaitingForOrderQuantity;
		};

		/**
		 * This method will see if the number of items will fit on the screen and if not it will extend the widths of
		 * all the panels to fit all of the contents.
		 */
		self.setPanelWidths= function () {
			var panelWidth="";
			var minWidth=200;
			if(self.data.length>8){
				panelWidth = ((self.data.length +1)*minWidth) + "px"
			} else{
				panelWidth="inherit"
			}
			document.getElementById("warehouseList").style.width=panelWidth;
			document.getElementById("orderingInfoPanel").style.width=panelWidth;
			document.getElementById("weightPanel").style.width=panelWidth;
			document.getElementById("costPanel").style.width=panelWidth;
			document.getElementById("wHSTieTierSlotPanel").style.width=panelWidth;
			document.getElementById("changeLogPanel").style.width=panelWidth;
		};

		/**
		 * This method will make a call to the API to get the remarks for the item at the warehouse
		 * @param warehouseLocationItem warehouseLocationItem the item at the warehouse whose remarks your requesting.
		 */
		self.getCommentsAndRemarks=function(warehouseLocationItem){
			self.isSaveRemarkSuccess = false;
			self.commentAndRemarkError = "";
			self.commentAndRemarkSuccess = "";
			self.warehouseLocationItemSeleted = angular.copy(warehouseLocationItem);
			self.currentLocation=warehouseLocationItem.location.displayName;
			self.currentComment=angular.copy(warehouseLocationItem.comment);
			self.originalComment=angular.copy(warehouseLocationItem.comment);
			self.currentWarehouseLocationNbr=warehouseLocationItem.key.warehouseNumber;
			self.isWaitingForCommentResponse = true;
			self.remarkData=[];
			productCasePackCandidateApi.getCandidateInformation({psItmId: self.psItemMasters[0].candidateItemId}, self.loadRemark, self.errorRemark);
		};

		/**
		 * This method will make a call to the API to get the remarks for the item at the warehouse after save Remark
		 * @param warehouseLocationItem warehouseLocationItem the item at the warehouse whose remarks your requesting.
		 */
		self.getCommentsAndRemarksAfterSave=function(warehouseLocationItem){
			self.commentAndRemarkError = "";
			self.commentAndRemarkSuccess = "";
			self.remarkData=[];
			productCasePackCandidateApi.getCandidateInformation({psItmId: self.psItemMasters[0].candidateItemId}, self.loadRemark, self.errorRemark);
		};

		/**
		 * When successfully getting the remarks this method sets up the data to be displayed.
		 * @param result the remarks for an item at a location.
		 */
		self.loadRemark = function (psItemMasters) {
			//OLD: List<ItemWarehouseComment>
			//NEW: List<CandidateItemWarehouseComment>
			var psItemMaster = angular.copy(psItemMasters[0]);
			var candidateWarehouseLocationItems = angular.copy(psItemMasters[0].candidateWarehouseLocationItems);
			var candidateItemWarehouseComments = null;
			angular.forEach(candidateWarehouseLocationItems, function(candidateWarehouseLocationItem){
				if(self.warehouseLocationItemSeleted.key.warehouseNumber === candidateWarehouseLocationItem.key.warehouseNumber
						&& self.warehouseLocationItemSeleted.key.warehouseType === candidateWarehouseLocationItem.key.warehouseType){
					candidateItemWarehouseComments = candidateWarehouseLocationItem.candidateItemWarehouseComments;
					self.currentComment = candidateWarehouseLocationItem.comment;
				}
			});
			self.originalComment = angular.copy(self.currentComment);
			var result = [];
			angular.forEach(candidateItemWarehouseComments, function(candidateItemWarehouseComment){
				var item = {};
				item.itemCommentContents = candidateItemWarehouseComment.comments;
				item.key = {};
				item.key.itemId = psItemMaster.itemCode;
				item.key.itemType = psItemMaster.itemKeyType;
				item.key.warehouseNumber = candidateItemWarehouseComment.key.warehouseNumber;
				item.key.warehouseType = candidateItemWarehouseComment.key.warehouseType;
				item.key.itemCommentType = candidateItemWarehouseComment.key.itemCommentType;
				item.key.itemCommentNumber = candidateItemWarehouseComment.key.itemCommentNumber;

				result.push(item);
			});


			self.itemWarehouseCommentsUpdate = [];
			self.remarkData=angular.copy(result);
			self.originalRemarkData = angular.copy(result);
			//document.getElementById("remarksRow").rowSpan=(""+3*self.remarkData.length) ;
			self.isWaitingForCommentResponse = false;
			if(self.isSaveRemarkSuccess){
				self.commentAndRemarkSuccess = "Updated successfully";
				//self.originalComment = angular.copy(self.currentComment);
			}
		};

		/**
		 * Fetches the error from the back end.
		 * @param error
		 */
		self.errorRemark = function (error) {
			self.isWaitingForCommentResponse = false;
			if(error && error.data) {
				if (error.data.message) {
					self.setCommentError(error.data.message);
				} else {
					self.setCommentError(error.data.error);
				}
			}
			else {
				self.setCommentError("An unknown error occurred.");
			}
		};

		/**
		 * Sets the controller's error message.
		 * @param error The error message.
		 */
		self.setCommentError = function(error) {
			self.isWaitingForCommentResponse = false;
			self.commentAndRemarkError = error;
			self.commentAndRemarkSuccess = "";
		};

		/**
		 * Add new row in Remark Comment.
		 */
		self.addNewRemarkComment = function () {
			if(self.remarkData != null && self.remarkData.length > 0){
				var itemWarehouseComments = angular.copy(self.remarkData[self.remarkData.length - 1]);
				if(itemWarehouseComments.itemCommentContents != ""){
					itemWarehouseComments.itemCommentContents = "";
					itemWarehouseComments.key.itemCommentNumber += 1;
					self.remarkData.push(itemWarehouseComments);
					self.itemWarehouseCommentsUpdate.push(itemWarehouseComments);
				}
			}else{
				var itemWarehouseCommentsNew = {};
				itemWarehouseCommentsNew["key"] = {};
				itemWarehouseCommentsNew["key"]["itemId"] = self.warehouseLocationItemSeleted.key.itemCode;
				itemWarehouseCommentsNew["key"]["itemType"] = self.warehouseLocationItemSeleted.key.itemType;
				itemWarehouseCommentsNew["key"]["warehouseNumber"] = self.warehouseLocationItemSeleted.key.warehouseNumber;
				itemWarehouseCommentsNew["key"]["warehouseType"] = self.warehouseLocationItemSeleted.key.warehouseType;
				itemWarehouseCommentsNew["key"]["itemCommentNumber"] = 1;
				itemWarehouseCommentsNew["key"]["itemCommentType"] = "REMRK";
				itemWarehouseCommentsNew["itemCommentContents"] = "";
				self.remarkData.push(itemWarehouseCommentsNew);
				self.itemWarehouseCommentsUpdate.push(itemWarehouseCommentsNew);
				//document.getElementById("remarksRow").rowSpan=(""+3*self.remarkData.length) ;
			}
		};

		/**
		 * Save data change in Remark Comment.
		 */
		self.saveRemarkComment=function(){
			self.commentAndRemarkError = "";
			self.commentAndRemarkSuccess = "";
			if(self.checkChange()){
				var errorMessage = self.validationCommentAndRemark();
				if(errorMessage){
					self.commentAndRemarkError = "Warehouse comment is a mandatory field";
				}else{
					self.getItemWarehouseUpdate();
					self.isWaitingForCommentResponse = true;
					var warehouseLocationItem = angular.copy(self.warehouseLocationItemSeleted);
					warehouseLocationItem["comment"] = self.currentComment;
					warehouseLocationItem["itemWarehouseCommentsList"] = self.itemWarehouseCommentsUpdate;

					var candidateWarehouseLocationItems = angular.copy(self.psItemMasters[0].candidateWarehouseLocationItems);
					var tempCandidateWarehouseLocationItem = null;
					angular.forEach(candidateWarehouseLocationItems, function(candidateWarehouseLocationItem){
						if(warehouseLocationItem.key.warehouseNumber === candidateWarehouseLocationItem.key.warehouseNumber
								&& warehouseLocationItem.key.warehouseType === candidateWarehouseLocationItem.key.warehouseType){
							tempCandidateWarehouseLocationItem = angular.copy(candidateWarehouseLocationItem);
						}
					});
					tempCandidateWarehouseLocationItem.comment = warehouseLocationItem.comment;
					var candidateItemWarehouseComments = [];
					angular.forEach(warehouseLocationItem.itemWarehouseCommentsList, function(item){
						var candidateItemWarehouseComment = {};
						candidateItemWarehouseComment.comments = item.itemCommentContents;
						candidateItemWarehouseComment.key = {};
						candidateItemWarehouseComment.key.warehouseNumber = item.key.warehouseNumber;
						candidateItemWarehouseComment.key.warehouseType = item.key.warehouseType;
						candidateItemWarehouseComment.key.itemCommentType = item.key.itemCommentType;
						candidateItemWarehouseComment.key.itemCommentNumber = item.key.itemCommentNumber;
						candidateItemWarehouseComment.key.candidateItemId = self.psItemMasters[0].candidateItemId;

						candidateItemWarehouseComments.push(candidateItemWarehouseComment);
					});
					tempCandidateWarehouseLocationItem.candidateItemWarehouseComments = candidateItemWarehouseComments;

					productCasePackCandidateApi.updateCasePackWarehouseCommentsCandidate(tempCandidateWarehouseLocationItem, self.updateRemarksSuccess, self.setCommentError);
				}
			}else{
				self.commentAndRemarkError = "There are no change.";
			}
		};

		/**
		 * Save data change success.
		 * @param results
		 */
		self.updateRemarksSuccess = function (results) {
			self.isWaitingForCommentResponse = false;
			//self.getCommentsAndRemarksAfterSave(self.warehouseLocationItemSeleted);
			self.isSaveRemarkSuccess = true;
			self.commentAndRemarkError = "";
			self.commentAndRemarkSuccess = "";
			self.remarkData=[];
			self.loadRemark(results.data);
		};

		/**
		 * Check different data before save.
		 * @returns {boolean}
		 */
		self.checkChange = function () {
			var flagChangeData = false;
			if(self.originalComment){
				if(self.currentComment){
					flagChangeData = self.currentComment.trim() != self.originalComment.trim();
				}else{
					flagChangeData =  true;
				}
			}else{
				if(self.currentComment){
					flagChangeData = true;
				}
			}
			if(!flagChangeData){
				if(self.remarkData.length == self.originalRemarkData.length){
					for (var i = 0; i < self.remarkData.length; i++) {
						if(angular.toJson(self.remarkData[i]) != angular.toJson(self.originalRemarkData[i])){
							flagChangeData = true;
							break;
						}
					}
				}else{
					flagChangeData =  true;
				}
			}
			return flagChangeData;
		};

		/**
		 * Validate Comment and Remark data before save.
		 * @returns {boolean}
		 */
		self.validationCommentAndRemark = function () {
			var check = false;
			angular.forEach(self.remarkData, function(item){
				if(item.itemCommentContents == null || item.itemCommentContents.trim() == ""){
					check = true;
				}
			});
			return check;
		};

		/**
		 * Get data change after save.
		 */
		self.getItemWarehouseUpdate = function () {
			for (var i = 0; i < self.remarkData.length; i++) {
				var itemWarehouseCommentNew = angular.copy(self.remarkData[i]);
				for (var j = 0; j < self.originalRemarkData.length; j++) {
					var itemWarehouseCommentOrig = angular.copy(self.originalRemarkData[j]);
					if(itemWarehouseCommentNew.key.itemCommentNumber == itemWarehouseCommentOrig.key.itemCommentNumber){
						if(angular.toJson(itemWarehouseCommentNew) != angular.toJson(itemWarehouseCommentOrig))
							self.itemWarehouseCommentsUpdate.push(itemWarehouseCommentNew);
					}
				}
			}
		};

		/**
		 * Reset data.
		 */
		self.resetRemarkComment=function(){
			self.commentAndRemarkError = "";
			self.commentAndRemarkSuccess = "";
			self.remarkData = angular.copy(self.originalRemarkData);
			self.currentComment = angular.copy(self.originalComment);
		};

		/**
		 * This method checks to see if any of the information of warehouse item locations have not been changed.
		 *
		 * @returns {boolean} Return true if any of the information of warehouse item locations have been changed.
		 */
		self.dataIsChanged = function () {
			if(angular.toJson(self.data) !== angular.toJson(self.dataOrig)){
				$rootScope.contentChangedFlag = true;
				return true;
			}
			return false;
		};

		/**
		 * This method to return back the value have been changed to old value.
		 */
		self.resetWarehouseItemLocation = function () {
			self.error = '';
			self.success = '';
			self.data = angular.copy(self.dataOrig);
		};

		/**
		 * This method to return a list of warehouse location item, that have been changed.
		 */
		self.getDataChangedToSave = function () {
			var dataChanged = [];
			var lengthData = self.data.length;
			for(var i = 0; i< lengthData; i++){
				if(angular.toJson(self.data[i]) != angular.toJson(self.dataOrig[i])){
					dataChanged.push(angular.copy(self.data[i]));
				}
			}
			return dataChanged;
		};

		/**
		 * Saves new information for the warehouse location item, that have been changed. After save successful
		 * reload data.
		 */
		self.saveWarehouseItemLocation = function () {
			self.error = null;
			self.success = null;
			var dataChanged = self.getDataChangedToSave();
			if(dataChanged){
				var errorMessage = self.validation(dataChanged);
				if(errorMessage == null || errorMessage == ''){
					self.isLoading=true;
					var psItemMaster = self.getPsItemMasterData(dataChanged);
					productCasePackCandidateApi.updateCasePackWarehouseCandidate(psItemMaster,
						//success case
						function (results) {
							self.isLoading=false;
							self.success = results.message;
							self.loadData(results.data);
						},
						//error case
						function(results){
							self.fetchError(results)
						}
					);
				}else{
					self.error = 'Case Pack - Warehouse:';
					angular.forEach(errorMessage, function (value) {
						self.error += "<li>" +value + "</li>";
					})
				}
			}
		};

		/**
		 * This method to validation data before call save.
		 */
		self.validation = function (dataChanged) {
			var errorMessage = [];
			for(var i = 0; i< dataChanged.length; i++){
				var whsItemLocation = dataChanged[i];
				if(whsItemLocation.whseMaxShipQuantityNumber && !self.regexInt.test(whsItemLocation.whseMaxShipQuantityNumber)){
					if(errorMessage.indexOf("Whse Max Ship Qty must be greater or equal 0 and less than or equal to 999999999") == -1){
						errorMessage.push("Whse Max Ship Qty must be greater or equal 0 and less than or equal to 999999999");
					}
				}
				if(whsItemLocation.unitFactor1 && !self.regexDecimal.test(whsItemLocation.unitFactor1)){
					if(errorMessage.indexOf("Unit Factor 1 must be greater or equal 0 and less than or equal to 99999.9999") == -1){
						errorMessage.push("Unit Factor 1 must be greater or equal 0 and less than or equal to 99999.9999");
					}
				}
				if(whsItemLocation.unitFactor2 && !self.regexDecimal.test(whsItemLocation.unitFactor2)){
					if(errorMessage.indexOf("Unit Factor 2 must be greater or equal 0 and less than or equal to 99999.9999") == -1){
						errorMessage.push("Unit Factor 2 must be greater or equal 0 and less than or equal to 99999.9999");
					}
				}
				if(whsItemLocation.whseTie && !self.regexInt.test(whsItemLocation.whseTie)){
					if(errorMessage.indexOf("Whse Tie must be greater or equal 0 and less than or equal to 999999999") == -1){
						errorMessage.push("Whse Tie must be greater or equal 0 and less than or equal to 999999999");
					}
				}
				if(whsItemLocation.whseTier && !self.regexInt.test(whsItemLocation.whseTier)){
					if(errorMessage.indexOf("Whse Tier must be greater or equal 0 and less than or equal to 999999999") == -1){
						errorMessage.push("Whse Tier must be greater or equal 0 and less than or equal to 999999999");
					}
				}
			}
			return errorMessage;
		};

		/**
		 * User click yes on confirm message popup.
		 */
		self.yesConfirmAction = function () {
			$('#warehouseRejectCandidateModal').modal('hide');
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
			$('#warehouseRejectCandidateModal').modal({backdrop: 'static', keyboard: true});
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
			var dataChanged = self.getDataChangedToSave();
			if(dataChanged){
				var errorMessage = self.validation(dataChanged);
				if(errorMessage == null || errorMessage == ''){
					self.isLoading=true;
					var psItemMaster = self.getPsItemMasterData(dataChanged);
					productCasePackCandidateApi.updateCasePackWarehouseCandidate(psItemMaster,
						//success case
						function (results) {
							self.isLoading=false;
							self.success = results.message;
							self.onCandidateTabChange({tabId: "vendorInfoTab"});
						},
						//error case
						self.fetchError
					);
				}else{
					self.error = 'Case Pack - Warehouse:';
					angular.forEach(errorMessage, function (value) {
						self.error += "<li>" +value + "</li>";
					})
				}
			}else{
				self.onCandidateTabChange({tabId: "vendorInfoTab"});
			}
		};

		/**
		 * Change to back case pack tab in candidate mode.
		 */
		self.backCandidate = function () {
			self.error = null;
			self.success = null;
			var dataChanged = self.getDataChangedToSave();
			if(dataChanged){
				var errorMessage = self.validation(dataChanged);
				if(errorMessage == null || errorMessage == ''){
					self.isLoading=true;
					var psItemMaster = self.getPsItemMasterData(dataChanged);
					productCasePackCandidateApi.updateCasePackWarehouseCandidate(psItemMaster,
						//success case
						function (results) {
							self.isLoading=false;
							self.success = results.message;
							self.onCandidateTabChange({tabId: "casePackInfoTab"});
						},
						//error case
						self.fetchError
					);
				}else{
					self.error = 'Case Pack - Warehouse:';
					angular.forEach(errorMessage, function (value) {
						self.error += "<li>" +value + "</li>";
					})
				}
			}else{
				self.onCandidateTabChange({tabId: "casePackInfoTab"});
			}
		};
	}
})();
