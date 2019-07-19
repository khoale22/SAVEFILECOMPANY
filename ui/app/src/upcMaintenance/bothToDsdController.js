/*
 *   bothToDsdController.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */

/**
 * Controller to support bothToDsd.html page to all users to make a WHS/DSD UPC just DSD.
 *
 * @author s573181
 * @since 2.0.5
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('BothToDsdController', bothToDsdController);

	bothToDsdController.$inject = ['UpcSwapApi', 'ProductInfoService', '$scope', '$timeout'];

	function bothToDsdController(upcSwapApi, productInfoService, $scope, $timeout){

		var self = this;

		/**
		 * The list of both to DSDs to send to back end.
		 *
		 * @type {Array}
		 */
		self.bothToDsdList = [];

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
		 * Keeps track of whether a source has an error message.
		 *
		 * @type {boolean}
		 */
		self.hasSourceError = false;

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
            self.bothToDsdList = [];
            self.init();
            self.showAfterUpdate =false;
        };

        /**
         * This function is used to check datas are null or not.
         *
         * @returns {boolean}
         */
        self.isEmptyData = function(){
            for (var i = 0; i < self.bothToDsdList.length; i++) {
                if ((self.bothToDsdList[i].sourceUpc != null && self.bothToDsdList[i].sourceUpc != undefined)
                     || (self.isViewingProductInfo() == true)
                ){
                    return false;
                }else if(self.bothToDsdList[i].source != null && self.bothToDsdList[i].source != undefined){
                	if(self.bothToDsdList[i].source.productId != null && self.bothToDsdList[i].source.productId != undefined){
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

		/**
		 * Contains the list of error upc swaps so user can see errors and description.
		 *
		 * @type {Array}
		 */
		self.errorResultList = [];

		/*
		 * Functions related to setting upc or item code data dynamically from user input.
		 */

		/**
		 * Set the upc column in the table dynamically when the user has entered more than one upc in a text field.
		 *
		 * @param inputIndex index of input that is sending the upc.
		 * @param IEclipData Internet Explorer clipboard data.
		 */
		self.setUpcDataRows = function(inputIndex, IEclipData){
			self.resetDefaultValues();
			self.hasRecord = true;
			var swap = self.bothToDsdList[inputIndex];
			swap = self.resetRecord(swap);
			self.bothToDsdList[inputIndex] = swap;
			if(swap.sourceUpc == null && IEclipData == null){
				return;
			}

			var textSplit;
			// if internet explorer paste event
			if(IEclipData != null) {
				textSplit = IEclipData;
			} else {
				textSplit = swap.sourceUpc.split(/[,\s]+/);
			}

			var focusUpc = 0;
			if(textSplit.length > 1){
				swap.sourceUpc = textSplit[0];
				var firstData = null;
				if(IEclipData){
					firstData = textSplit[0];
				}
				self.bothToDsdList[inputIndex] = swap;
				for(var index = 1; index < textSplit.length; index++){
					focusUpc = index + inputIndex;
					// insert upc into existing data row
					if(self.bothToDsdList.length > (focusUpc)) {
						self.bothToDsdList[focusUpc].sourceUpc = textSplit[index];
						self.bothToDsdList[focusUpc] = self.resetRecord(self.bothToDsdList[focusUpc]);
					}
					// create a new data row
					else {
						self.bothToDsdList.push({sourceUpc: textSplit[index]});
					}
				}
				// sets focus on last upc updated after angular renders it (null pointer otherwise)
				$timeout(function(){
					$scope.$evalAsync(function() {
						if(firstData){
							self.bothToDsdList[inputIndex].sourceUpc = firstData;
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
		 * Add an empty row to the both to DSD array.
		 */
		self.addEmptyDataRow = function(){
			self.resetDefaultValues();
			self.bothToDsdList.push({});
		};

		/**
		 * Delete the selected index from the both to DSD array.
		 *
		 * @param index The index to delete.
		 */
		self.removeDataRow = function(index){
			self.resetDefaultValues();
			self.bothToDsdList.splice(index, 1);
		};

		/**
		 * Resets data row when source UPC changes.
		 *
		 * @param upcSwap The record to reset.
		 */
		self.resetRecord = function (upcSwap) {
			var resetUpcSwap = {};
			var sourceUpc = upcSwap.sourceUpc == "" ? null : upcSwap.sourceUpc;
			resetUpcSwap.sourceUpc = sourceUpc;
			return resetUpcSwap;
		};

		/*
		 * Functions related to both to DSD.
		 */

		/**
		 * Get the details of the both (WHS & DSD) UPC('s) selected.
		 */
		self.getBothToDsdDetails = function(){
			self.resetDefaultValues();
			var upcList = [];
			var foundData = false;
			var upcSwap;
			for(var index in self.bothToDsdList){
				upcSwap = self.bothToDsdList[index];
				if(!self.isEmptyRecord(upcSwap)){
					foundData = true;
					upcList.push(upcSwap.sourceUpc);
				}
			}
			if(!foundData){
				self.fetchError(({data: {message: "Please enter UPC before submitting request."}}));
				return;
			}
			if(upcList.length > 0) {
				upcSwapApi.getBothToDsdDetails({upcList: upcList},
					self.setBothToDsdDetails, self.fetchError);
			}
		};

		/**
		 * Submit a both (WHS & DSD) to DSD request.
		 */
		self.submitBothToDsd = function(){

			self.resetDefaultValues();
			var bothToDsdList = [];
			var upcSwap;
			for(var index in self.bothToDsdList){
				upcSwap = self.bothToDsdList[index];
				if(!self.isEmptyRecord(upcSwap)){

// User can select new primary, or keep Item codes current primary (if the DSD wasn't the current primary).
					// user has already fetched data
					if(self.alreadyFetchedDetails(upcSwap)) {
						bothToDsdList.push(upcSwap);
					}
					// user needs to fetch details first
					else {
						self.fetchError({data: {message: "You must first fetch details for upc " + upcSwap.sourceUpc}});
						return;
					}
				}
			}
			if(bothToDsdList.length > 0) {
				self.isWaitingForResponse = true;
				upcSwapApi.submitBothToDsd(bothToDsdList, self.setBothToDsdSuccess, self.fetchError);
			}
		};

		/*
		 * Functions related to retrieving product information.
		 */

		/**
		 * Shows the panel that contains product detail information.
		 *
		 * @param productId The product ID of the product to show.
		 */
		self.showProductInfo = function(productId){
			self.resetDefaultValues();
			productInfoService.show(self, productId);
		};

		/*
		 * State related functions.
		 */

		/**
		 * Returns whether or not the data row has fetched the details from the back end.
		 *
		 * @param bothToDsdRecord data row to look at
		 * @returns {boolean}
		 */
		self.alreadyFetchedDetails = function(bothToDsdRecord){
			return (bothToDsdRecord != null && bothToDsdRecord.source != null && bothToDsdRecord.source.productId != null);
		};

		/**
		 * Returns whether or not the data row has a list of associated UPCs.
		 *
		 * @param bothToDsdRecord data row to look at
		 * @returns {boolean}
		 */
		self.hasSelectPrimaryList = function(bothToDsdRecord){
			return (bothToDsdRecord != null && bothToDsdRecord.source.associatedUpcList != null
			&& bothToDsdRecord.source.associatedUpcList.length > 0);
		};

		/**
		 * Returns whether or not data row is empty of required information (source upc)
		 *
		 * @param bothToDsdRecord
		 * @returns {boolean}
		 */
		self.isEmptyRecord = function(bothToDsdRecord){
			return !(bothToDsdRecord != null && !$scope.isEmptyString(bothToDsdRecord.sourceUpc));
		};

		/**
		 * Closes the view for both to DSD submission details.
		 */
		self.closePage = function(){
			self.showAfterUpdate = false;
			self.bothToDsdList = self.errorResultList;
			if(self.bothToDsdList.length == 0) {
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
		 * Callback for when there is a successful return after requesting the details for a both to DSD.
		 *
		 * @param results The results from the server.
		 */
		self.setBothToDsdDetails = function(results){
			var bothToDsdList = [];
			var upcSwap;
			productInfoService.hide();
			var errorFound = false;
			for(var index = 0; index < results.data.length; index++){
				upcSwap = results.data[index];
				// if the upcSwap source or destination side has an error
				if(upcSwap.errorFound) {
					errorFound = true;
					self.resetErrorDataRecord(upcSwap);
				}
				bothToDsdList.push(upcSwap);
			}

			self.bothToDsdList = bothToDsdList;

			if (results.data.length == 1 && !errorFound) {
				self.showingSingleRow = true;
				self.showProductInfo(results.data[0].source.productId);
			} else {
				self.showingSingleRow = false;
			}
		};

		/**
		 * Callback for when there is a successful return after submitting a both to DSD.
		 *
		 * @param results The results from the server.
		 */
		self.setBothToDsdSuccess = function(results){
			self.isWaitingForResponse = false;
			self.afterUpdateList = results.data;
			self.showAfterUpdate = true;
			self.errorResultList = self.getErrorList(results.data);
			productInfoService.hide();
		};

		/**
		 * This method resets all information in a data record for a both to dsd with errors except:
		 * source: upc + error if there is one
		 * so that it is easy to see what went wrong and where.
		 *
		 * @param upcSwap Upc swap with errors.
		 */
		self.resetErrorDataRecord = function(upcSwap){
			// if the upcSwap source side had an error
			if (upcSwap.source != null && upcSwap.source.errorMessage != null) {
				self.hasSourceError = true;
			}
			// set fields to null so user has to refetch data before submitting
			upcSwap.sourcePrimaryUpc = null;
			upcSwap.selectSourcePrimaryUpc = null;
			if(upcSwap.source != null) {
				upcSwap.source.productId = null;
				upcSwap.source.itemCode = null;
				upcSwap.source.itemDescription = null;
				upcSwap.source.balanceOnHand = null;
				upcSwap.source.balanceOnOrder = null;
				upcSwap.source.onLiveOrPendingPog = null;
				upcSwap.source.productRetailLink = null;
				upcSwap.source.onUpcomingDelivery = null;
				upcSwap.source.purchaseOrderDisplayText = null;
			}
		};

		/**
		 * Resets errors to default state.
		 */
		self.resetDefaultValues = function(){
			self.error = null;
			self.hasSourceError = false;
		};

		/**
		 * This method gets the list to display upc swaps that had application caught errors after submitting a
		 * set of upc swaps. If there were no errors, the list will be empty. This is done because the
		 * application returns two types of upc swaps -- results and errors.
		 *
		 * @param results Complete list of upc swap results after submitting a set of upc swaps.
		 * @returns {Array}
		 */
		self.getErrorList = function(results){
			var upcSwapErrorList = [];
			var upcSwap;
			for(var index = 0; index < results.length; index++){
				upcSwap = angular.copy(results[index]);
				// if the upcSwap source has an error
				if(upcSwap.errorFound) {
					self.resetErrorDataRecord(upcSwap);
					upcSwapErrorList.push(upcSwap);
				}
			}
			return upcSwapErrorList;

		};
	}
})();
