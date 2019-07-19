/*
 *  dsdToBothController.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 */

'use strict';

/**
 * Controller to support dsdToBoth.html page to all users to make a DSD upc both DSD and WHS.
 *
 * @author s573181
 * @since 2.0.5
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('DsdToBothController', dsdToBothController);

	dsdToBothController.$inject = ['UpcSwapApi', 'ProductInfoService', '$scope', '$timeout'];

	function dsdToBothController(upcSwapApi, productInfoService, $scope, $timeout){

		var self = this;

		/**
		 * The list of DSDs to both to send to back end.
		 *
		 * @type {Array}
		 */
		self.dsdToBothList = [];

		/**
		 * The error message to display.
		 *
		 * @type {String}
		 */
		self.error = null;

		/**
		 * The variable to keep track of whether user has typed in any information or not.
		 *
		 * @type {Boolean}
		 */
		self.hasRecord = false;

		/**
		 * Keeps track of whether user is looking at the new warehouse information after an update.
		 *
		 * @type {boolean}
		 */
		self.showAfterUpdate = false;

		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Whether or not the user is only working on one row of swaps.
		 *
		 * @type {boolean}
		 */
		self.showingSingleRow = false;

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			self.addEmptyDataRow();
			productInfoService.hide();
		};

        /**
         * Clear all datas that user entered or the system generated.
         */
        self.clearData = function(){
            self.dsdToBothList = [] ;
            self.init();
            self.hasDestinationError = false;
            self.hasSourceError = false;
            self.showAfterUpdate =false;
        };

        /**
         * This function is used to check datas are null or not.
         *
         * @returns {boolean}
         */
        self.isEmptyData = function(){
            for (var i = 0; i < self.dsdToBothList.length; i++) {
                if ((self.dsdToBothList[i].sourceUpc != null && self.dsdToBothList[i].sourceUpc != undefined)
                     || (self.isViewingProductInfo() == true)
                ){
                    return false;
                }else if(self.dsdToBothList[i].source != null && self.dsdToBothList[i].source != undefined){
                    if(self.dsdToBothList[i].source.productId != null && self.dsdToBothList[i].source.productId != undefined){
                        return false;
                    }
                }else if(self.dsdToBothList[i].destination != null && self.dsdToBothList[i].destination != undefined){
                    if((self.dsdToBothList[i].destination.itemCode != null && self.dsdToBothList[i].destination.itemCode != undefined)
					    || (self.dsdToBothList[i].destination.productId != null && self.dsdToBothList[i].destination.itemCode != productId)
					){
                        return false;
					}
				}
            }
            return true;
        };

		/**
		 * Function that figures out whether or not to show the swap panel at the top.
		 *
		 * @returns {boolean} True to show the panel and false otherwise.
		 */
		self.showSwapPanel = function() {
			// If the update panel is showing, then don't show the swap panel.
			if (self.showAfterUpdate) {
				return false;
			}

			// If there is only one product showing, always show the panel.
			if (self.showingSingleRow) {
				return true;
			}

			// If there is more than one row of data, look to see if they've clicked on the product to
			// show the panel
			if (self.isViewingProductInfo()) {
				return false;
			}

			// Any other state, show the panel.
			return true;
		};

		/*
		 * Functions related to setting upc or item code data dynamically from user input.
		 */

		/**
		 * Set the upc column in the table dynamically when the user has entered more than one upc in a text field.
		 *
		 * @param inputIndex index of input that is sending the upc.
		 * @param IEclipData Internet Explorer clipboard data
		 */
		self.setUpcDataRows = function(inputIndex, IEclipData){
			self.error = null;
			self.hasRecord = true;
			var dsdToBoth = self.dsdToBothList[inputIndex];
			dsdToBoth = self.resetRecord(dsdToBoth);
			self.dsdToBothList[inputIndex] = dsdToBoth;
			if(dsdToBoth.sourceUpc == null && IEclipData == null){
				return;
			}
			var textSplit;
			// if internet explorer paste event
			if(IEclipData != null) {
				textSplit = IEclipData;
			} else {
				textSplit = dsdToBoth.sourceUpc.split(/[,\s]+/);
			}
			var focusUpc = 0;
			if(textSplit.length > 1){
				dsdToBoth.sourceUpc = textSplit[0];
				var firstData = null;
				if(IEclipData){
					firstData = textSplit[0];
				}
				self.dsdToBothList[inputIndex] = dsdToBoth;
				for(var index = 1; index < textSplit.length; index++){

					focusUpc = index + inputIndex;
					// insert upc into existing data row
					if(self.dsdToBothList.length > (focusUpc)) {
						self.dsdToBothList[focusUpc].sourceUpc = textSplit[index];
						self.dsdToBothList[focusUpc] = self.resetRecord(self.dsdToBothList[focusUpc]);
					}

					// create a new data row
					else {
						self.dsdToBothList.push({sourceUpc: textSplit[index]});
					}
				}

				// sets focus on last upc updated after angular renders it (null pointer otherwise)
				$timeout(function(){
					$scope.$evalAsync(function() {
						if(firstData){
							self.dsdToBothList[inputIndex].sourceUpc = firstData;
						}
						document.getElementById("upc" + focusUpc).focus();
					});
				});
			}
		};

		/**
		 * Gets the upc data in the correct format when pasted for IE.
		 * @param index
		 */
		self.setUpcDataRowsPaste = function(index) {
			if(window.clipboardData != null) {
				var clipData =  window.clipboardData.getData('Text');
				clipData.replace(/(\s)/g, " ");
				clipData = self.removeEmptyValuesFromArray(clipData.split(/[\s,]+/));
				self.setUpcDataRows(index, clipData);
			}
		};

		/**
		 * Set the newItemCode column in the table dynamically when the user has entered more than one item code in a text field.
		 *
		 * @param inputIndex index of input that is sending the item code.
		 * @param IEclipData Internet Explorer clipboard Data.
		 */
		self.setItemCodeDataRows = function(inputIndex, IEclipData){
			self.error = null;
			self.hasRecord = true;
			var dsdToBoth = self.dsdToBothList[inputIndex];
			dsdToBoth = self.resetRecord(dsdToBoth);
			self.dsdToBothList[inputIndex] = dsdToBoth;
			if(dsdToBoth.destination == null && IEclipData == null){
				return;
			}
			var textSplit;
			// if internet explorer paste event
			if(IEclipData != null) {
				textSplit = IEclipData;
			} else {
				textSplit = dsdToBoth.destination.itemCode.split(/[,\s]+/);
			}
			var focusItemCode = 0;
			if(textSplit.length > 1){
				dsdToBoth.destination.itemCode = textSplit[0];
				var firstData = null;
				if(IEclipData){
					firstData = textSplit[0];
				}
				self.dsdToBothList[inputIndex] = dsdToBoth;
				for(var index = 1; index < textSplit.length; index++){

					focusItemCode = index + inputIndex;
					// insert upc into existing data row
					if(self.dsdToBothList.length > (focusItemCode)) {
						if(self.dsdToBothList[focusItemCode].destination != null){
							self.dsdToBothList[focusItemCode].destination.itemCode = textSplit[index];
						} else {
							self.dsdToBothList[focusItemCode].destination = {itemCode: textSplit[index]};
						}
						self.dsdToBothList[focusItemCode] = self.resetRecord(self.dsdToBothList[focusItemCode]);
					}

					// create a new data row
					else {
						self.dsdToBothList.push({destination: { itemCode: textSplit[index]}});
					}
				}

				// sets focus on last item code updated after angular renders it (null pointer otherwise)
				$timeout(function() {
					$scope.$evalAsync(function() {
						if(firstData){
							self.dsdToBothList[inputIndex].destination.itemCode = firstData;
						}
						document.getElementById("newItemCode" + focusItemCode).focus();
					});
				});
			}
		};

		/**
		 * Gets the upc data in the correct format when pasted for IE.
		 * @param index
		 */
		self.setItemCodeDataRowsPaste = function(index) {
			if(window.clipboardData != null) {
				var clipData =  window.clipboardData.getData('Text');
				clipData.replace(/(\s)/g, " ");
				clipData = self.removeEmptyValuesFromArray(clipData.split(/[\s,]+/));
				self.setItemCodeDataRows(index, clipData);
			}
		};

		/**
		 * Remove any element from array that should be treated as empty. {This method needed to be added for
		 * 		internet explorer to show expected behavior}
		 *
		 * @param arrayToInspect array to inspect for empty elements
		 * @returns {Array}
		 */
		self.removeEmptyValuesFromArray = function (arrayToInspect) {
			var currentElement;
			var arrayToReturn = [];
			for(var index = 0; index < arrayToInspect.length; index++){
				currentElement = arrayToInspect[index];
				if(currentElement == null || currentElement == undefined || currentElement == ""){
					continue;
				}
				arrayToReturn.push(currentElement);
			}
			return arrayToReturn;
		};

		/*
		 * Functions related to adding, modifying, and deleting array rows.
		 */

		/**
		 * Add an empty row to the dsdToBoth array.
		 */
		self.addEmptyDataRow = function(){
			self.error = null;
			self.dsdToBothList.push({});
		};

		/**
		 * Delete the selected index from the dsdToBoth array.
		 *
		 * @param index The index to delete.
		 */
		self.removeDataRow = function(index){
			self.error = null;
			self.dsdToBothList.splice(index, 1);
		};

		/**
		 * Resets data row when source upc or destination item code changes.
		 *
		 * @param dsdToBoth The record to reset.
		 */
		self.resetRecord = function (dsdToBoth) {
			var resetDsdToBoth = {};
			var sourceUpc = dsdToBoth.sourceUpc == "" ? null : dsdToBoth.sourceUpc;
			var destinationItemCode = (dsdToBoth.destination != null && dsdToBoth.destination.itemCode != null) ?
				dsdToBoth.destination.itemCode == "" ? null : dsdToBoth.destination.itemCode : null;
			resetDsdToBoth.sourceUpc = sourceUpc;
			if(destinationItemCode != null) {
				resetDsdToBoth.destination ={itemCode: destinationItemCode};
			} else {
				resetDsdToBoth.destination = {};
			}
			return resetDsdToBoth;
		};
		/*
		 * Functions related to dsdToBoth.
		 */

		/**
		 * Get the details of the dsd upc('s) and item code('s) selected.
		 */
		self.getDsdToBothDetails = function(){
			self.error = null;
			productInfoService.hide();
			var upcList = [];
			var itemCodeList = [];
			var foundData = false;
			var dsdToBoth;
			for(var index in self.dsdToBothList){
				dsdToBoth = self.dsdToBothList[index];
				if(!self.isEmptyRecord(dsdToBoth)) {
					foundData = true;
					upcList.push(dsdToBoth.sourceUpc);
					itemCodeList.push(dsdToBoth.destination.itemCode);
				} else if(!$scope.isEmptyString(dsdToBoth.sourceUpc) && dsdToBoth.destination != null && $scope.isEmptyString(dsdToBoth.destination.itemCode)){
					self.fetchError({data: {message: "Please select an item code for upc " + dsdToBoth.sourceUpc + "."}});
					return;
				} else if(dsdToBoth.destination != null && !$scope.isEmptyString(dsdToBoth.destination.itemCode) && $scope.isEmptyString(dsdToBoth.sourceUpc)){
					self.fetchError({data: {message: "Please select a upc for item code " + dsdToBoth.destination.itemCode + "."}});
					return;
				}
			}

			if(!foundData){
				self.fetchError(({data: {message: "Please enter upc and item code before submitting request."}}));
				return;
			}

			if(upcList.length > 0) {
				upcSwapApi.getDsdToBothDetails({upcList: upcList, itemCodeList: itemCodeList},
					self.setDsdToBothDetails, self.fetchError);
			}
		};

		/**
		 * Submit a DsdToBoth request.
		 */
		self.submitDsdToBoth = function(){
			self.error = null;
			var dsdToBothList = [];
			var dsdToBoth;
			for(var index in self.dsdToBothList){
				dsdToBoth = self.dsdToBothList[index];
				if(!self.isEmptyRecord(dsdToBoth)){

					// user has already fetched data and
					if(self.alreadyFetchedDetails(dsdToBoth)) {
						dsdToBothList.push(dsdToBoth);
					}
					// user needs to fetch details first
					else {
						self.fetchError({data: {message: "You must first fetch details for upc " + dsdToBoth.sourceUpc +
								", and item code " + dsdToBoth.destination.itemCode + "."}});
						return;
					}
				} else if(!$scope.isEmptyString(dsdToBoth.sourceUpc) && dsdToBoth.destination != null && $scope.isEmptyString(dsdToBoth.destination.itemCode)){
					self.fetchError({data: {message: "Please select an item code for upc " + dsdToBoth.sourceUpc + "."}});
					return;
				} else if(dsdToBoth.destination != null && !$scope.isEmptyString(dsdToBoth.destination.itemCode) && $scope.isEmptyString(dsdToBoth.sourceUpc)){
					self.fetchError({data: {message: "Please select a upc for item code " + dsdToBoth.destination.itemCode + "."}});
					return;
				}
			}

			if(dsdToBothList.length > 0) {
				self.isWaitingForResponse = true;
				upcSwapApi.submitDsdToBoth(dsdToBothList, self.setDsdToBothSuccess, self.fetchError);
			}
		};

		/*
		 * Functions related to retrieving product information.
		 */

		/**
		 * Shows the panel that contains product detail information.
		 *
		 * @param productId The product ID of the product to show.
		 * @param comparisonProductId The product ID of the product to put into the comparison pane
		 * of the product info screen.
		 */
		self.showProductInfo = function(productId, comparisonProductId){

			productInfoService.show(self, productId, comparisonProductId);
		};
		/*
		 * State related functions.
		 */

		/**
		 * Returns whether or not the data row has fetched the details from the back end.
		 *
		 * @param dsdToBothRecord data row to look at
		 * @returns {boolean}
		 */
		self.alreadyFetchedDetails = function(dsdToBothRecord){
			return (dsdToBothRecord != null && dsdToBothRecord.source != null && dsdToBothRecord.source.productId != null);
		};

		/**
		 * Returns whether or not data row is empty of required information (source upc and destination item code)
		 *
		 * @param dsdToBothRecord
		 * @returns {boolean}
		 */
		self.isEmptyRecord = function(dsdToBothRecord){
			return !(dsdToBothRecord != null && !$scope.isEmptyString(dsdToBothRecord.sourceUpc)
				&& dsdToBothRecord.destination != null && !$scope.isEmptyString(dsdToBothRecord.destination.itemCode));

		};

		/**
		 * Closes the view for upc swap submission details.
		 */
		self.closePage = function(){
			self.showAfterUpdate = false;
			self.dsdToBothList = self.errorResultList;
			if(self.dsdToBothList.length == 0) {
				self.hasRecord = false;
				self.addEmptyDataRow();
			}
		};

		/**
		 * Returns whether or not the user is currently viewing product information.
		 *
		 * @returns {boolean} True if the user is viewing product information and false otherwise.
		 */
		self.isViewingProductInfo = function(){
			return productInfoService.isVisible();
		};

		/*
		 * Callbacks.
		 */

		/**
		 * Callback for when there is an error returned from the server.
		 *
		 * @param error The error from the server.
		 */
		self.fetchError = function(error){
			self.isWaitingForResponse = false;
			if (error && error.data) {
				if (error.data.message && error.data.message != "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			} else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Callback for when there is a successful return after requesting the details for a upc swap.
		 *
		 * @param results The results from the server.
		 */
		self.setDsdToBothDetails = function(results){
			var dsdToBothList = [];
			var dsdToBoth;
			var foundError = false;
			for(var index = 0; index < results.data.length; index++){
				dsdToBoth = results.data[index];

				// if the upcSwap source or destination side has an error
				if(dsdToBoth.errorFound) {
					foundError = true;
					self.resetErrorDataRecord(dsdToBoth);
				}else{
					dsdToBoth.destinationInformation = formatSwapInformation(dsdToBoth.destination);
				}
				dsdToBothList.push(dsdToBoth);
			}
			self.dsdToBothList = dsdToBothList;

			if (results.data.length == 1 && !foundError) {
				self.showingSingleRow = true;
				self.showProductInfo(results.data[0].source.productId, results.data[0].destination.productId);
			} else {
				self.showingSingleRow = false;
			}
		};

		/**
		 * Callback for when there is a successful return after submitting a dsdToBoth request.
		 *
		 * @param results The results from the server.
		 */
		self.setDsdToBothSuccess = function(results){
			self.isWaitingForResponse = false;
			self.afterUpdateList = results.data;
			self.showAfterUpdate = true;
			self.errorResultList = self.getErrorList(results.data);
			productInfoService.hide();
		};

		/**
		 * This method resets all information in a data record for a DSD to both with errors except:
		 * source: upc + error if there's one
		 * destination: item code + error if there's one
		 * so that it is easy to see what went wrong and where.
		 *
		 * @param dsdToBoth DSD to both with errors.
		 */
		self.resetErrorDataRecord = function(dsdToBoth){
			// if the dsdToBoth source side had an error
			if (dsdToBoth.source != null && dsdToBoth.source.errorMessage != null) {
				self.hasSourceError = true;
			}
			// if the dsdToBoth destination side had an error
			if (dsdToBoth.destination != null && dsdToBoth.destination.errorMessage != null) {
				self.hasDestinationError = true;
			}
			// set fields to null so user has to refetch data before submitting
			dsdToBoth.sourcePrimaryUpc = null;
			dsdToBoth.selectSourcePrimaryUpc = null;
			if(dsdToBoth.source != null) {
				dsdToBoth.source.productId = null;
				dsdToBoth.source.itemCode = null;
				dsdToBoth.source.itemDescription = null;
				dsdToBoth.source.balanceOnHand = null;
				dsdToBoth.source.balanceOnOrder = null;
				dsdToBoth.source.onLiveOrPendingPog = null;
				dsdToBoth.source.productRetailLink = null;
				dsdToBoth.source.onUpcomingDelivery = null;
				dsdToBoth.source.purchaseOrderDisplayText = null;
			}
			dsdToBoth.destinationPrimaryUpc = null;
			dsdToBoth.makeDestinationPrimaryUpc = null;
			if(dsdToBoth.destination != null) {
				dsdToBoth.destination.productId = null;
				dsdToBoth.destination.itemDescription = null;
				dsdToBoth.destination.balanceOnHand = null;
				dsdToBoth.destination.balanceOnOrder = null;
				dsdToBoth.destination.onLiveOrPendingPog = null;
				dsdToBoth.destination.productRetailLink = null;
				dsdToBoth.destination.onUpcomingDelivery = null;
				dsdToBoth.destination.purchaseOrderDisplayText = null;
			}
		};

		/**
		 * This method gets the list to display DSDs to boths that had application caught errors after submitting a
		 * set of DSDs to boths. If there were no errors, the list will be empty. This is done because the
		 * application returns two types of DSDs to both -- results and errors.
		 *
		 * @param results Complete list of DSDs to both results after submitting a set of DSDs to boths.
		 * @returns {Array}
		 */
		self.getErrorList = function(results){
			var upcSwapErrorList = [];
			var upcSwap;
			for(var index = 0; index < results.length; index++){
				upcSwap = angular.copy(results[index]);
				// if the upcSwap source or destination side has an error
				if(upcSwap.errorFound) {
					self.resetErrorDataRecord(upcSwap);
					upcSwapErrorList.push(upcSwap);
				}
			}
			return upcSwapErrorList;
		};
	}
})();
