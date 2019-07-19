/*
 * moveWarehouseUpcSwapController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Controller to support the page that allows users to move a warehouse upc to a new item code.
 *
 * @author m314029
 * @since 2.0.4
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('MoveWarehouseUpcSwapController', moveWarehouseUpcSwapController);

	moveWarehouseUpcSwapController.$inject = ['UpcSwapApi', 'ProductInfoService', '$scope', '$timeout'];

	function moveWarehouseUpcSwapController(upcSwapApi, productInfoService, $scope, $timeout){

		var self = this;

		/**
		 * The list of upcSwaps to send to back end.
		 *
		 * @type {Array}
		 */
		self.upcSwapList = [];

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
		self.hasUpcSwapRecord = false;

		/**
		 * Keeps track of whether user is looking at upc swap information after and update.
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
		 * Keeps track of whether a destination has an error message.
		 *
		 * @type {boolean}
		 */
		self.hasDestinationError = false;

		/**
		 * Contains the list of error upc swaps so user can see errors and description.
		 *
		 * @type {Array}
		 */
		self.errorResultList = [];

		/**
		 * Contains options for what to send when source upc is the primary UPC.
		 *
		 * @type {[*]}
		 */
		self.whatToSendList = [
			{name: "Just primary", id: "JUST_PRIMARY"},
			{name: "Primary and associates", id: "PRIMARY_AND_ASSOCIATES"},
			{name: "Just associates", id: "JUST_ASSOCIATES"}];

		/**
		 * Wheter or not the screen is only showing one row of swaps.
		 *
		 * @type {boolean}
		 */
		self.showingSingleRow = false;

		/**
		 * Select all option for choose Associates.
		 * @type {string}
		 */
		self.selectAllOption = "Select All";

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
            self.upcSwapList = [];
            self.init();
            self.showAfterUpdate =false;
        };

        /**
         * This function is used to check datas are null or not.
         *
         * @returns {boolean}
         */
        self.isEmptyData = function(){
            for (var i = 0; i < self.upcSwapList.length; i++) {
                if ((self.upcSwapList[i].sourceUpc != null && self.upcSwapList[i].sourceUpc != undefined)
                    || (self.upcSwapList[i].source != null && self.upcSwapList[i].source != undefined)
                    || (self.isViewingProductInfo() == true)
                ){
                    return false;
                }else if(self.upcSwapList[i].destination != null && self.upcSwapList[i].destination != undefined){
                    if(self.upcSwapList[i].destination.itemCode != null && self.upcSwapList[i].destination.itemCode != undefined){
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
		 * @param IEclipData Internet Explorer clipboard data.
		 */
		self.setUpcDataRows = function(inputIndex, IEclipData){
			self.resetDefaultValues();
			self.hasUpcSwapRecord = true;
			var swap = self.upcSwapList[inputIndex];
			swap = self.resetRecord(swap);
			self.upcSwapList[inputIndex] = swap;
			if(swap.sourceUpc == null && IEclipData == null){
				return;
			}
			var textSplit;
			// if internet explorer paste event
			if(IEclipData != null) {
				textSplit = IEclipData;
			} else {
				textSplit = swap.sourceUpc.split(/[\s,]+/);
			}

			var focusUpc = 0;
			if(textSplit.length > 1){
				swap.sourceUpc = textSplit[0];
				var firstData = null;
				if(IEclipData){
					firstData = textSplit[0];
				}
				self.upcSwapList[inputIndex] = swap;
				for(var index = 1; index < textSplit.length; index++){

					focusUpc = index + inputIndex;
					// insert upc into existing data row
					if(self.upcSwapList.length > (focusUpc)) {
						self.upcSwapList[focusUpc].sourceUpc = textSplit[index];
						self.upcSwapList[focusUpc] = self.resetRecord(self.upcSwapList[focusUpc]);
					}

					// create a new data row
					else {
						self.upcSwapList.push({sourceUpc: textSplit[index]});
					}
				}

				// sets focus on last upc updated after angular renders it (null pointer otherwise)
				$timeout(function(){
					$scope.$evalAsync(function() {
						if(firstData){
							self.upcSwapList[inputIndex].sourceUpc = firstData;
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
		 * @param IEclipData Internet Explorer clipboard data.
		 */
		self.setItemCodeDataRows = function(inputIndex, IEclipData){
			self.resetDefaultValues();
			self.hasUpcSwapRecord = true;
			var swap = self.upcSwapList[inputIndex];
			swap = self.resetRecord(swap);
			self.upcSwapList[inputIndex] = swap;
			if(swap.destination == null && IEclipData == null){
				return;
			}
			var textSplit;
			// if internet explorer paste event
			if(IEclipData != null) {
				textSplit = IEclipData;
			} else {
				textSplit = swap.destination.itemCode.split(/[\s,]+/);
			}
			var focusItemCode = 0;
			if(textSplit.length > 1){
				swap.destination.itemCode = textSplit[0];
				var firstData = null;
				if(IEclipData){
					firstData = textSplit[0];
				}
				self.upcSwapList[inputIndex] = swap;
				for(var index = 1; index < textSplit.length; index++){

					focusItemCode = index + inputIndex;
					// insert upc into existing data row
					if(self.upcSwapList.length > (focusItemCode)) {
						if(self.upcSwapList[focusItemCode].destination != null){
							self.upcSwapList[focusItemCode].destination.itemCode = textSplit[index];
						} else {
							self.upcSwapList[focusItemCode].destination = {itemCode: textSplit[index]};
						}

						self.upcSwapList[focusItemCode] = self.resetRecord(self.upcSwapList[focusItemCode]);
					}

					// create a new data row
					else {
						self.upcSwapList.push({destination: { itemCode: textSplit[index]}});
					}
				}

				// sets focus on last item code updated after angular renders it (null pointer otherwise)
				$timeout(function() {
					$scope.$evalAsync(function() {
						if(firstData){
							self.upcSwapList[inputIndex].destination.itemCode = firstData;
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
		 * Add an empty row to the upc swap array.
		 */
		self.addEmptyDataRow = function(){
			self.resetDefaultValues();
			self.upcSwapList.push({});
		};

		/**
		 * Delete the selected index from the upc swap array.
		 *
		 * @param index The index to delete.
		 */
		self.removeDataRow = function(index){
			self.resetDefaultValues();
			self.upcSwapList.splice(index, 1);
		};

		/**
		 * Resets data row when source upc or destination item code changes.
		 *
		 * @param upcSwap The record to reset.
		 */
		self.resetRecord = function (upcSwap) {
			var resetUpcSwap = {};
			var sourceUpc = upcSwap.sourceUpc == "" ? null : upcSwap.sourceUpc;
			var destinationItemCode = (upcSwap.destination != null && upcSwap.destination.itemCode != null) ?
				upcSwap.destination.itemCode == "" ? null : upcSwap.destination.itemCode : null;
			resetUpcSwap.sourceUpc = sourceUpc;
			if(destinationItemCode != null) {
				resetUpcSwap.destination ={itemCode: destinationItemCode};
			} else {
				resetUpcSwap.destination = {};
			}
			return resetUpcSwap;

		};

		/**
		 * This method resets all information in a data record for a upc swap with errors except:
		 * source: upc + error if there is one
		 * destination: item code + error if there is one
		 * so that it is easy to see what went wrong and where.
		 *
		 * @param upcSwap Upc swap with errors.
		 */
		self.resetErrorDataRecord = function(upcSwap){

			// if the upcSwap source side had an error
			if (upcSwap.source != null && upcSwap.source.errorMessage != null) {
				self.hasSourceError = true;
			}

			// if the upcSwap destination side had an error
			if (upcSwap.destination != null && upcSwap.destination.errorMessage != null) {
				self.hasDestinationError = true;
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
			upcSwap.destinationPrimaryUpc = null;
			upcSwap.makeDestinationPrimaryUpc = null;
			if(upcSwap.destination != null) {
				upcSwap.destination.productId = null;
				upcSwap.destination.itemDescription = null;
				upcSwap.destination.balanceOnHand = null;
				upcSwap.destination.balanceOnOrder = null;
				upcSwap.destination.onLiveOrPendingPog = null;
				upcSwap.destination.productRetailLink = null;
				upcSwap.destination.onUpcomingDelivery = null;
				upcSwap.destination.purchaseOrderDisplayText = null;
			}
		};

		/*
		 * Functions related to upc swap.
		 */

		/**
		 * Get the details of the upc('s) and item code('s) selected.
		 */
		self.getWarehouseUpcSwapDetails = function(){
			self.resetDefaultValues();
			productInfoService.hide();
			var upcList = [];
			var itemCodeList = [];
			var foundData = false;
			var upcSwap;
			for(var index in self.upcSwapList){
				upcSwap = self.upcSwapList[index];
				if(!self.isEmptyRecord(upcSwap)){
					foundData = true;
					upcList.push(upcSwap.sourceUpc);
					itemCodeList.push(upcSwap.destination.itemCode);
				}

				else if(!$scope.isEmptyString(upcSwap.sourceUpc) && upcSwap.destination != null && $scope.isEmptyString(upcSwap.destination.itemCode)){
					self.fetchError({data: {message: "Please select an item code for upc " + upcSwap.sourceUpc + "."}});
					return;
				}

				else if(upcSwap.destination != null && !$scope.isEmptyString(upcSwap.destination.itemCode) && $scope.isEmptyString(upcSwap.sourceUpc)){
					self.fetchError({data: {message: "Please select a upc for item code " + upcSwap.destination.itemCode + "."}});
					return;
				}
			}

			if(!foundData){
				self.fetchError(({data: {message: "Please enter upc and item code before submitting request."}}));
				return;

			}

			if(upcList.length > 0) {
				upcSwapApi.getWarehouseMoveUpcSwapsDetails({upcList: upcList, itemCodeList: itemCodeList},
					self.setUpcSwapDetails, self.fetchError);
			}
		};

		/**
		 * Submit a warehouse upc swap.
		 */
		self.submitWarehouseUpcSwap = function(){
			self.resetDefaultValues();
			var upcSwapList = [];
			var upcSwap;
			for(var index in self.upcSwapList){
				upcSwap = angular.copy(self.upcSwapList[index]);
				if(!self.isEmptyRecord(upcSwap)){

					// user has already fetched data and
					if(self.alreadyFetchedDetails(upcSwap) &&

						// upc is not primary
						(!upcSwap.sourcePrimaryUpc ||

						// upc is primary, and is only associated upc of current item code
						(upcSwap.sourcePrimaryUpc && !self.hasSelectPrimaryList(upcSwap)) ||

						// upc is primary, and user has selected a replacement primary
						(upcSwap.sourcePrimaryUpc && (upcSwap.selectSourcePrimaryUpc != null || upcSwap.primarySelectOption == 'JUST_ASSOCIATES')
						&& (upcSwap.primarySelectOption == 'JUST_PRIMARY' || (upcSwap.additionalUpcList != undefined && additionalUpcList.additionalUpcList != null))))) {
						//remove select all option of associated upc list before update
						self.handleSelectAllOption(true, upcSwap.source.associatedUpcList);
						upcSwapList.push(upcSwap);
					}

					//a selected upc is a primary, and the user has not selected a associate upc
					else if(self.alreadyFetchedDetails(upcSwap) && upcSwap.sourcePrimaryUpc && (upcSwap.selectSourcePrimaryUpc != null || upcSwap.primarySelectOption == 'JUST_ASSOCIATES') &&
						(upcSwap.primarySelectOption != 'JUST_PRIMARY' && upcSwap.additionalUpcList === undefined || upcSwap.additionalUpcList === null)){
						self.fetchError({data: {message: "Please select the list of associate upc for item code: " +
						upcSwap.source.itemCode + "."}});
						return;
					}

					// a selected upc is a primary, and the user has not selected a replacement primary upc
					else if(self.alreadyFetchedDetails(upcSwap) && upcSwap.sourcePrimaryUpc && upcSwap.selectSourcePrimaryUpc == null){
						self.fetchError({data: {message: "You must first select a new primary upc for item code: " +
						upcSwap.source.itemCode + "."}});
						return;
					}

					// user needs to fetch details first
					else {
						self.fetchError({data: {message: "You must first fetch details for upc " + upcSwap.sourceUpc +
						", and item code " + upcSwap.destination.itemCode + "."}});
						return;
					}
				}

				else if(!$scope.isEmptyString(upcSwap.sourceUpc) && upcSwap.destination != null && $scope.isEmptyString(upcSwap.destination.itemCode)){
					self.fetchError({data: {message: "Please select an item code for upc " + upcSwap.sourceUpc + "."}});
					return;
				}

				else if(upcSwap.destination != null && !$scope.isEmptyString(upcSwap.destination.itemCode) && $scope.isEmptyString(upcSwap.sourceUpc)){
					self.fetchError({data: {message: "Please select a upc for item code " + upcSwap.destination.itemCode + "."}});
					return;
				}
			}

			if(upcSwapList.length > 0) {
				self.isWaitingForResponse = true;
				upcSwapApi.submitWarehouseMoveUpcSwaps(upcSwapList, self.setUpcSwapSuccess, self.fetchError);
			}
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

				// if the upcSwap source or destination side has an error
				if(upcSwap.errorFound) {
					self.resetErrorDataRecord(upcSwap);
					upcSwapErrorList.push(upcSwap);
				}
			}
			return upcSwapErrorList;

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
			self.resetDefaultValues();
			productInfoService.show(self, productId, comparisonProductId);
		};

		/*
		 * State related functions.
		 */

		/**
		 * Returns whether or not the data row has fetched the details from the back end.
		 *
		 * @param upcSwapRecord data row to look at
		 * @returns {boolean}
		 */
		self.alreadyFetchedDetails = function(upcSwapRecord){
			return (upcSwapRecord != null && upcSwapRecord.errorFound == false);
		};

		/**
		 * Returns whether or not the data row has a list of associated UPCs.
		 *
		 * @param upcSwapRecord data row to look at
		 * @returns {boolean}
		 */
		self.hasSelectPrimaryList = function(upcSwapRecord){
			return (upcSwapRecord != null && upcSwapRecord.source != null && upcSwapRecord.source.associatedUpcList != null
			&& upcSwapRecord.source.associatedUpcList.length > 0);
		};

		/**
		 * Returns whether or not data row is empty of required information (source upc and destination item code)
		 *
		 * @param upcSwapRecord
		 * @returns {boolean}
		 */
		self.isEmptyRecord = function(upcSwapRecord){
			return !(upcSwapRecord != null && !$scope.isEmptyString(upcSwapRecord.sourceUpc)
			&& upcSwapRecord.destination != null && !$scope.isEmptyString(upcSwapRecord.destination.itemCode));

		};

		/**
		 * Closes the view for upc swap submission details.
		 */
		self.closePage = function(){
			self.showAfterUpdate = false;
			self.upcSwapList = self.errorResultList;
			if(self.upcSwapList.length == 0) {
				self.hasUpcSwapRecord = false;
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

		/**
		 * Resets default values for this controller
		 */
		self.resetDefaultValues = function(){
			self.error = null;
			self.hasSourceError = false;
			self.hasDestinationError = false;
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
		self.setUpcSwapDetails = function(results){
			var upcSwapList = [];
			var upcSwap;
			var errorFound = false;
			for(var index = 0; index < results.data.length; index++){
				upcSwap = results.data[index];

				// if the upcSwap source or destination side has an error
				if(upcSwap.errorFound) {
					errorFound = true;
					self.resetErrorDataRecord(upcSwap);
				} else {
					upcSwap.sourceInformation = formatSwapInformation(upcSwap.source);
					upcSwap.destinationInformation = formatSwapInformation(upcSwap.destination);
					//add select all option to the first list associated UPC
					if(upcSwap.source != undefined && upcSwap.source.associatedUpcList != undefined && upcSwap.source.associatedUpcList.length >0){
						upcSwap.source.associatedUpcList.splice(0, 0, self.selectAllOption);
					}
				}

				upcSwapList.push(upcSwap);

			}
			self.upcSwapList = upcSwapList;

			// If the user is only looking at one row, go ahead and show the product details page.
			if (results.data.length == 1 && !errorFound) {
				self.showingSingleRow = true;
				self.showProductInfo(results.data[0].source.productId, results.data[0].destination.productId);
			} else {
				self.showingSingleRow = false;
			}
		};

		/**
		 * Callback for when there is a successful return after submitting a upc swap.
		 *
		 * @param results The results from the server.
		 */
		self.setUpcSwapSuccess = function(results){
			self.isWaitingForResponse = false;
			self.afterUpdateList = results.data;
			self.errorResultList = self.getErrorList(results.data);
			self.showAfterUpdate = true;
			productInfoService.hide();
		};

		/**
		 * Set the associated upc list to what is left to pick from in the selectize dropdown.
		 *
		 * @param upcSwap upc swap object you are modifying
		 * @param select selectize dropdown being used
		 * @param item item selected
		 * @param addToList whether item needs to be added to the list or not
		 */
		self.setAssociateListToSelectize = function(upcSwap, select, item, addToList){
			if(item === self.selectAllOption){
				self.handleSelectAllOption(true, upcSwap.additionalUpcList);
				upcSwap.source.associatedUpcList.forEach(function (value) {
					if(value !== self.selectAllOption){
						upcSwap.additionalUpcList.push(value);
					}
				});
				upcSwap.source.associatedUpcList = [];
			}else{
				if(addToList){
					if(item == upcSwap.makeDestinationPrimaryUpcSelected){
						upcSwap.makeDestinationPrimaryUpcSelected = null;
					}
					if(select.$select.items === [] || select.$select.items.length === 0){
						select.$select.items.push(self.selectAllOption);
					}
					select.$select.items.push(item);
				}
				select.$select.items.sort(
					function(firstNumber, secondNumber){return parseInt(firstNumber) - parseInt(secondNumber)}
				);
				upcSwap.source.associatedUpcList = select.$select.items;
				if(upcSwap.source.associatedUpcList !== null && upcSwap.source.associatedUpcList.length === 1){
					self.handleSelectAllOption(true, upcSwap.source.associatedUpcList);
				}
			}
		};

		/**
		 * Set available options in make primary list.
		 *
		 * @param upcSwap current upc swap
		 * @param select select dropdown that shows options
		 * @param item upc to add/ remove from primary list
		 */
		self.setAvailablePrimaryList = function(upcSwap, select, item){
			if(upcSwap.previousSelectedSourcePrimaryUpc){
				select.$select.items.push(upcSwap.previousSelectedSourcePrimaryUpc);
			}
			upcSwap.previousSelectedSourcePrimaryUpc = item;
			if(item) {
				select.$select.items.splice(select.$select.items.indexOf(item), 1);
			}
			select.$select.items.sort(
				function(firstNumber, secondNumber){return parseInt(firstNumber) - parseInt(secondNumber)}
			);
			upcSwap.source.associatedUpcList = select.$select.items;
			self.handleSelectAllOption(false, upcSwap.source.associatedUpcList);
		};

		/**
		 * Helper method to return if already fetched details and is source primary upc.
		 *
		 * @param upcSwap current upc swap
		 * @returns {boolean}
		 */
		self.isDetailsFetchedAndPrimary = function (upcSwap) {
			return upcSwap.sourcePrimaryUpc && self.alreadyFetchedDetails(upcSwap);
		};

		/**
		 * Refresh drop-downs after user changes their primary select option choice for a upc swap that is primary.
		 *
		 * @param upcSwap current upc swap
		 */
		self.refreshSelectize = function(upcSwap){
			upcSwap.makeDestinationPrimaryUpcSelected = null;
			if(upcSwap.primarySelectOption == "JUST_PRIMARY"){
				if(upcSwap.additionalUpcList) {
					upcSwap.additionalUpcList.forEach(function (currentValue) {
						upcSwap.source.associatedUpcList.splice(0, 0, currentValue);
					});
					upcSwap.source.associatedUpcList.sort(
						function(firstNumber, secondNumber){return parseInt(firstNumber) - parseInt(secondNumber)}
					);
				}
				upcSwap.additionalUpcList = [];
			} else if(upcSwap.primarySelectOption == "JUST_ASSOCIATES"){
				upcSwap.previousSelectedSourcePrimaryUpc = null;
				if(upcSwap.selectSourcePrimaryUpc){
					upcSwap.source.associatedUpcList.splice(0, 0, upcSwap.selectSourcePrimaryUpc);
					upcSwap.selectSourcePrimaryUpc = null;
				}
			} else if(upcSwap.primarySelectOption == "PRIMARY_AND_ASSOCIATES"){
				if(upcSwap.selectSourcePrimaryUpc && upcSwap.source.associatedUpcList !== undefined && upcSwap.source.associatedUpcList.length > 0
					&& upcSwap.source.associatedUpcList.indexOf(upcSwap.selectSourcePrimaryUpc) >= 0){
					upcSwap.source.associatedUpcList.splice(upcSwap.source.associatedUpcList.indexOf(upcSwap.selectSourcePrimaryUpc),1);
				}
			}
			self.handleSelectAllOption(true,upcSwap.source.associatedUpcList);
			self.handleSelectAllOption(false,upcSwap.source.associatedUpcList);
		};

		/**
		 * Returns true if user should see 'Pre-digit 4' in 'Future Primary' column; false otherwise.
		 *
		 * @param upcSwap
		 * @returns {boolean}
		 */
		self.showPreDigitFour = function (upcSwap) {
			return self.isDetailsFetchedAndPrimary(upcSwap) &&
				(upcSwap.primarySelectOption == 'PRIMARY_AND_ASSOCIATES' &&
				upcSwap.source.associatedUpcList.length == 0 &&
				!upcSwap.selectSourcePrimaryUpc) ||
				upcSwap.primarySelectOption == 'JUST_PRIMARY' &&
				(!upcSwap.selectSourcePrimaryUpc &&
				upcSwap.source.associatedUpcList.length == 0);
		};

		/**
		 * Returns true if user should select a source primary upc in 'Future Primary' column, false otherwise.
		 *
		 * @param upcSwap current upc swap
		 * @returns {boolean}
		 */
		self.showSelectSourcePrimary = function (upcSwap) {
			return self.isDetailsFetchedAndPrimary(upcSwap) &&
				(upcSwap.primarySelectOption == 'PRIMARY_AND_ASSOCIATES' &&
				(upcSwap.selectSourcePrimaryUpc ||
				upcSwap.source.associatedUpcList.length != 0))||
				(upcSwap.primarySelectOption == 'JUST_PRIMARY' &&
				(upcSwap.selectSourcePrimaryUpc ||
				upcSwap.source.associatedUpcList.length != 0));
		};

		/**
		 * Returns available future primary list.
		 *
		 * @param upcSwap current upc swap
		 * @returns {Array}
		 */
		self.getAvailableFuturePrimaryList = function (upcSwap) {
			var returnArray = [];
			if(upcSwap.sourcePrimaryUpc){
				var index;
				switch (upcSwap.primarySelectOption){
					case 'JUST_PRIMARY': {
						returnArray.push(upcSwap.sourceUpc);
						break;
					}
					case 'PRIMARY_AND_ASSOCIATES': {
						returnArray.push(upcSwap.sourceUpc);
						if(upcSwap.additionalUpcList) {
							for (index = 0; index < upcSwap.additionalUpcList.length; index++) {
								returnArray.push(upcSwap.additionalUpcList[index]);
							}
						}
						break;
					}
					case 'JUST_ASSOCIATES': {
						if(upcSwap.additionalUpcList) {
							for (index = 0; index < upcSwap.additionalUpcList.length; index++) {
								returnArray.push(upcSwap.additionalUpcList[index]);
							}
						}
						break;
					}
				}
			} else {
				returnArray.push(upcSwap.sourceUpc);
			}
			returnArray.sort(
				function(firstNumber, secondNumber){return parseInt(firstNumber) - parseInt(secondNumber)}
			);
			return returnArray;
		};

		/**
		 * Returns true if all upc swap records have already fetched details.
		 *
		 * @returns {boolean}
		 */
		self.alreadyFetchedAllDetails = function () {
			for(var index = 0; index < self.upcSwapList.length; index++){
				if(!self.alreadyFetchedDetails(self.upcSwapList[index])){
					return false;
				}
			}
			return true;
		};

		/**
		 * Handle select all option in associated upc list. If remove switch is true, it mean remove select all
		 * option out of the list of upc associated. Else add select all option in the list of upc associated.
		 * @param remove
		 * @param associatedUpcList
		 */
		self.handleSelectAllOption = function (remove, associatedUpcList) {
			if(associatedUpcList !== undefined && associatedUpcList !== null){
				if(remove){
					if(associatedUpcList.indexOf(self.selectAllOption) >= 0){
						associatedUpcList.splice(associatedUpcList.indexOf(self.selectAllOption),1);
					}
				}else if(associatedUpcList.length > 0){
					associatedUpcList.splice(0,0,self.selectAllOption);
				}
			}
		}

		/**
		 * Return the list of associated upc list. The list has been remove select all option.
		 * @param associatedUpcList
		 */
		self.associatedUPCsNoneSelectAllOption = function (associatedUpcList) {
			var associatedUpcListRet = angular.copy(associatedUpcList);
			if(associatedUpcListRet !== undefined && associatedUpcListRet !== null && associatedUpcListRet.indexOf(self.selectAllOption) >= 0){
				associatedUpcListRet.splice(associatedUpcListRet.indexOf(self.selectAllOption),1);
			}
			return associatedUpcListRet;
		}
	}
})();
