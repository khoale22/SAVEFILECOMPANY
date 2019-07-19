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
	angular.module('productMaintenanceUiApp').component('warehouse', {
		// isolated scope binding
		bindings: {
			itemMaster: '<'
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/warehouse.html',
		// The controller that handles our component logic
		controller: WarehouseController
	});

	WarehouseController.$inject = ['ProductFactory', 'PMCommons', '$scope', '$rootScope', 'ProductCasePackApi',
		'$timeout', 'ngTableParams', '$filter', 'ProductDetailAuditModal', 'ProductSearchService'];

	/**
	 * Case Pack Warehouse component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @constructor
	 */
	function WarehouseController(productFactory, PMCommons, $scope, $rootScope, productCasePackApi, $timeout,
								 ngTableParams, $filter, ProductDetailAuditModal, productSearchService) {
		/** All CRUD operation controls of Case pack Import page goes here */
		var self = this;
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
		self.isWaitingForResponse= false;

		/**
		 * Because there are so many API calls on init and on changes these flags are set to insure everything is
		 * present before the view is displayed.
		 * @type {boolean}
		 */
		self.isWaitingForWarehouseLocationItems=true;
		self.isWaitingForFlows = true;
		self.isWaitingForOrderQuantity=true;

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
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
			self.isWaitingForFlows = true;
			self.isWaitingForOrderQuantity=true;
			productCasePackApi.getFlowTypes(self.loadFlowTypes, self.fetchError);
			productCasePackApi.getOrderQuantityTypes(self.loadOrderQuantityTypes, self.fetchError)
		};

		/**
		 * Component ngOnChanges lifecycle hook. Called whenever a binding variable is changed.
		 */
		this.$onChanges = function (){
			self.success = "";
			self.error = ""
			self.isWaitingForResponse=true;
			self.isWaitingForWarehouseLocationItems=true;
			var thisRequest = ++self.latestRequest;
			productCasePackApi.getWarehouseInfo({
					itemCode: self.itemMaster.key.itemCode,
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
				});
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
			self.isWaitingForWarehouseLocationItems = false;
			//Checking Whse Max Ship Qty#. if  WHSE_LOC_ITM.MAX_SHP_WHSE_QTY  = 99999 then the application will get the
			// value of ITEM_MASTER.MAX_SHIP_QTY.
			// It was confirmed by Girish as confirmed by Girish on PIM 1067 on 08/26/2015.
			angular.forEach(results, function(elm){
				if(elm.whseMaxShipQuantityNumber === 99999){
					elm.whseMaxShipQuantityNumber = angular.copy(self.itemMaster.maxShipQty);
				}
			});
			self.data = results;
			self.dataOrig = angular.copy(results);
			self.setPanelWidths();
			self.checkLoadStatus()
		};

		/**
		 * This method takes the order quantity types taken from the database and attaches them to the UI dropdown variable
		 * @param results form requesting for the order quantity types from the api
		 */
		self.loadOrderQuantityTypes = function (results) {
			self.isWaitingForOrderQuantity=false;
			self.orderQuantityTypes=results;
			self.checkLoadStatus();
		};

		/**
		 * This method takes the flow types taken from the database and attaches them to the UI dropdown variable
		 * @param results form requesting for the flow types from the api
		 */
		self.loadFlowTypes= function (results) {
			self.isWaitingForFlows = false;
			self.flowTypes=results;
			self.checkLoadStatus();
		};

		/**
		 * This method insures that all the data is present before displaying the results
		 */
		self.checkLoadStatus=function () {
			self.isWaitingForResponse=self.isWaitingForFlows||self.isWaitingForWarehouseLocationItems||self.isWaitingForOrderQuantity;
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
			self.currentComment=warehouseLocationItem.comment;
			self.originalComment=angular.copy(warehouseLocationItem.comment);
			self.currentWarehouseLocationNbr=warehouseLocationItem.key.warehouseNumber;
			self.isWaitingForCommentResponse = true;
			self.remarkData=[];
			var param = {
				itemId: warehouseLocationItem.key.itemCode,
				itemType: warehouseLocationItem.key.itemType,
				warehouseNumber: warehouseLocationItem.key.warehouseNumber,
				warehouseType: warehouseLocationItem.key.warehouseType
			};
			productCasePackApi.getRemarks(param, self.loadRemark, self.errorRemark);
		};

		/**
		 * This method will make a call to the API to get the remarks for the item at the warehouse after save Remark
		 * @param warehouseLocationItem warehouseLocationItem the item at the warehouse whose remarks your requesting.
		 */
		self.getCommentsAndRemarksAfterSave=function(warehouseLocationItem){
			self.commentAndRemarkError = "";
			self.commentAndRemarkSuccess = "";
			self.remarkData=[];
			var param = {
				itemId: warehouseLocationItem.key.itemCode,
				itemType: warehouseLocationItem.key.itemType,
				warehouseNumber: warehouseLocationItem.key.warehouseNumber,
				warehouseType: warehouseLocationItem.key.warehouseType
			};
			productCasePackApi.getRemarks(param, self.loadRemark, self.errorRemark);
		}
		/**
		 * When successfully getting the remarks this method sets up the data to be displayed.
		 * @param result the remarks for an item at a location.
		 */
		self.loadRemark = function (result) {
			self.itemWarehouseCommentsUpdate = [];
			self.remarkData=result;
			self.originalRemarkData = angular.copy(result);
			document.getElementById("remarksRow").rowSpan=(""+3*self.remarkData.length) ;
			self.isWaitingForCommentResponse = false;
			if(self.isSaveRemarkSuccess){
				self.commentAndRemarkSuccess = "Updated successfully";
				self.originalComment = self.currentComment;
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
		 * Array contain Remark and Comment change.
		 * @type {Array}
		 */
		self.itemWarehouseCommentsUpdate = [];

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
				itemWarehouseCommentsNew["itemCommentContents"] = "";
				self.remarkData.push(itemWarehouseCommentsNew);
				self.itemWarehouseCommentsUpdate.push(itemWarehouseCommentsNew);
				document.getElementById("remarksRow").rowSpan=(""+3*self.remarkData.length) ;
			}
		}

		/**
		 * Save data change in Remark Comment.
		 */
		self.saveRemarkComment=function(){
			self.commentAndRemarkError = "";
			self.commentAndRemarkSuccess = "";
			if(self.checkChange()){
				self.getItemWarehouseUpdate();
				self.isWaitingForCommentResponse = true;
				var warehouseLocationItem = angular.copy(self.warehouseLocationItemSeleted);
				warehouseLocationItem["comment"] = self.currentComment;
				warehouseLocationItem["itemWarehouseCommentsList"] = self.itemWarehouseCommentsUpdate;
				console.log(angular.toJson(self.itemWarehouseCommentsUpdate));
				productCasePackApi.updateRemarks(warehouseLocationItem, self.updateRemarksSuccess, self.setCommentError);
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
			self.getCommentsAndRemarksAfterSave(self.warehouseLocationItemSeleted);
			self.isSaveRemarkSuccess = true;
		}

		/**
		 * Check different data after save.
		 * @returns {boolean}
		 */
		self.checkChange = function () {
			var flagChangeData = false;
			if(self.currentComment.trim() != self.originalComment.trim()){
				flagChangeData =  true;
			}else{
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
		}

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
		}

		/**
		 * Reset data.
		 */
		self.resetRemarkComment=function(){
			self.commentAndRemarkError = "";
			self.commentAndRemarkSuccess = "";
			self.remarkData = angular.copy(self.originalRemarkData);
			self.currentComment = angular.copy(self.originalComment);
		}
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
			$rootScope.contentChangedFlag = false;
			return false;
		};

		/**
		 * This method to return back the value have been changed to old value.
		 */
		self.resetWarehouseItemLocation = function () {
			self.error = '';
			self.success = '';
			self.data = angular.copy(self.dataOrig);
		}

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
		}

		/**
		 * Saves new information for the warehouse location item, that have been changed. After save successful
		 * reload data.
		 */
		self.saveWarehouseItemLocation = function () {
			self.error = '';
			self.success = '';
			var dataChanged = self.getDataChangedToSave();
			if(dataChanged){
				var errorMessage = self.validation(dataChanged);
				if(errorMessage == null || errorMessage == ''){
					self.isWaitingForResponse=true;
					self.isWaitingForWarehouseLocationItems=true;
					productCasePackApi.saveWarehouseItemLocation(dataChanged,
						//success case
						function (results) {
							self.isWaitingForResponse=false;
							self.isWaitingForWarehouseLocationItems=false;
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
		}

		/**
		 * This method to validation data before call save.
		 */
		self.validation = function (dataChanged) {
			var errorMessage = [];
			for(var i = 0; i< dataChanged.length; i++){
				var whsItemLocation = dataChanged[i];
				if(whsItemLocation.whseMaxShipQuantityNumber && !self.regexInt.test(whsItemLocation.whseMaxShipQuantityNumber)){
					if(errorMessage.indexOf("Whse Max Ship Qty must be number, and less than or equal to 999999999") == -1){
						errorMessage.push("Whse Max Ship Qty must be number, and less than or equal to 999999999");
					}
				}
				if(whsItemLocation.unitFactor1 && !self.regexDecimal.test(whsItemLocation.unitFactor1)){
					if(errorMessage.indexOf("Unit Factor 1 must be number (#####.####), greater or equal 0 and less than or equal to 99999.9999") == -1){
						errorMessage.push("Unit Factor 1 must be number (#####.####), greater or equal 0 and less than or equal to 99999.9999");
					}
				}
				if(whsItemLocation.unitFactor2 && !self.regexDecimal.test(whsItemLocation.unitFactor2)){
					if(errorMessage.indexOf("Unit Factor 2 must be number (#####.####), greater or equal 0 and less than or equal to 99999.9999") == -1){
						errorMessage.push("Unit Factor 2 must be number (#####.####), greater or equal 0 and less than or equal to 99999.9999");
					}
				}
				if(whsItemLocation.whseTie && !self.regexInt.test(whsItemLocation.whseTie)){
					if(errorMessage.indexOf("Whse Tie must be number, and less than or equal to 999999999") == -1){
						errorMessage.push("Whse Tie must be number, and less than or equal to 999999999");
					}
				}
				if(whsItemLocation.whseTier && !self.regexInt.test(whsItemLocation.whseTier)){
					if(errorMessage.indexOf("Whse Tier must be number, and less than or equal to 999999999") == -1){
						errorMessage.push("Whse Tier must be number, and less than or equal to 999999999");
					}
				}
			}
			return errorMessage;
		}

		/**
		 * Show warehouse audit information modal
		 */
		self.showWarehouseAuditInfo = function () {
			self.warehouseAuditInfo = productCasePackApi.getWarehouseAudits;
			var title ="Warehouse";
			var parameters = {'itemCode' :self.itemMaster.key.itemCode, 'itemType' :self.itemMaster.key.itemType};

			ProductDetailAuditModal.open(self.warehouseAuditInfo, parameters, title);
		}
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
	}
})();
