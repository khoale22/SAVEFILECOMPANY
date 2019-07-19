/*
 *  addAssociateController.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */


(function() {
	angular.module('productMaintenanceUiApp').controller('AddAssociateController', addAssociateController);

	addAssociateController.$inject = ['UpcSwapApi', 'ProductInfoService'];

	/**
	 * Constructs the controller to support the addAssociate page.
	 *
	 * @param upcSwapApi The API that will all the backend for most of the functions of this page.
	 * @param productInfoService The service that supports showing product information.
	 */
	function addAssociateController(upcSwapApi, productInfoService) {

		var self = this;

		/**
		 * Error messages to show the user.
		 *
		 * @type {String}
		 */
		self.error = null;

		/**
		 * Success messages to show the user.
		 *
		 * @type {String}
		 */
		self.success = null;

		/**
		 * Whether or not the front end is waiting on a response from the back end.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Whether or not the front end has the data for what's on the page.
		 *
		 * @type {boolean}
		 */

		self.hasData = false;

		/**
		 * Whether or not to  show the error column on the source side.
		 *
		 * @type {boolean}
		 */
		self.hasSourceError = false;

		/**
		 * Whether or not to  show the error column on the destination side.
		 *
		 * @type {boolean}
		 */
		self.hasDestinationError = false;

		/**
		 * The list of records the user wants to add associates to.
		 *
		 * @type {Array}
		 */
		self.addAssociateList = [];

		/**
		 * List of updated associate upcs
		 * @type {Array}
		 */
		self.addAssociateUpdateList=[];

		/**
		 * Flag set for when to show the results html
		 * @type {boolean}
		 */
		self.showAfterUpdate= false;

		/**
		 * Whether or not to  show the error column on the source side.
		 *
		 * @type {boolean}
		 */
		self.hasSourceUpdateError = false;

		/**
		 * Whether or not to  show the error column on the destination side.
		 *
		 * @type {boolean}
		 */
		self.hasDestinationUpdateError = false;

		/**
		 * Initializes the controller.
		 */
		self.init = function() {
			productInfoService.hide();
			self.addAssociateList = [];
			self.addEmptyRow();
		};

        /**
         * Clear all datas that user entered or the system generated.
         */
		self.clearData = function(){
			self.init();
            self.success = null;
			self.error = null;
			self.showAfterUpdate =false;
			self.hasDestinationError = false;
			self.hasSourceError = false;
		};

        /**
         * This function is used to check datas are null or not.
		 *
		 * @returns {boolean}
         */
		self.isEmptyData = function(){
            for (var i = 0; i < self.addAssociateList.length; i++) {
                if ((self.addAssociateList[i].source.upc != null && self.addAssociateList[i].source.upc != "")
                    || (self.addAssociateList[i].source.productId != null && self.addAssociateList[i].source.productId != "")
					|| (self.addAssociateList[i].source.itemCode != null && self.addAssociateList[i].source.itemCode != "")
                    || (self.addAssociateList[i].destination.upc != null && self.addAssociateList[i].destination.upc != "")
                    || (self.addAssociateList[i].destination.checkDigit != null && self.addAssociateList[i].destination.checkDigit != "")
				){
                    return false;
                }
            }
            return true;
		};

		/**
		 * Returns wheter or not the product info panel should be showing.
		 *
		 * @returns {boolean}
		 */
		self.isViewingProductInfo = function() {
			return productInfoService.isVisible();
		};

		/**
		 * Shows the panel that contains product detail information.
		 *
		 * @param productId The product ID of the product to show.
		 */
		self.showProductInfo = function(productId){
			productInfoService.hide();
			productInfoService.show(self, productId);
		};

		/**
		 * Adds an empty row to the table.
		 */
		self.addEmptyRow = function() {
			self.hasData = false;
			upcSwapApi.getEmptySwap(self.addEmptyRowCallback, self.setError);
		};

		/**
		 * The callback from the request from the backend for a new, empty request.
		 *
		 * @param results The empty requst sent from the backend.
		 */
		self.addEmptyRowCallback = function(results) {
			self.addAssociateList.push(results);
		};

		/**
		 * Retrieves details about the item you're trying to add an associate to.
		 */
		self.getAddAssociateDetails = function() {
			self.isWaitingForResponse = true;
			self.hasSourceError = false;
			self.hasDestinationError = false;
			self.error = null;
			productInfoService.hide();
			upcSwapApi.getAddAssociateDetails(self.addAssociateList,
				self.loadData, self.setError);
		};

		/**
		 * The callback for when the front-end requests detailed information from the back end.
		 *
		 * @param results The fully populated request objects.
		 */
		self.loadData = function(results) {
			self.isWaitingForResponse = false;
			self.addAssociateList = results;
			self.hasData = true;
			for(var index = 0; index < self.addAssociateList.length; index++) {
				var upcSwap = self.addAssociateList[index];

				// See if the error columns need to be shown.
				if (upcSwap.source != null && upcSwap.source.errorMessage != null) {
					self.hasSourceError = true;
				}
				if (upcSwap.destination != null && upcSwap.destination.errorMessage != null) {
					self.hasDestinationError = true;
				}

				upcSwap.sourceInformation = formatSwapInformation(upcSwap.source);
			}

			self.showProductInfoWhenViewingOneProduct();
		};

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
		 * Callback for when there is an error returned from the server.
		 *
		 * @param error The error from the server.
		 */
		self.setError = function(error){
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
		 * Callback for when an add associate successfully happens.
		 *
		 * @param result The repsonse from the backend.
		 */
		self.saveSuccess = function(result) {
			self.showAfterUpdate = true;
			self.isWaitingForResponse = false;
			self.success = result.message;
			angular.forEach(result.data, function (associate) {
				if(associate.errorFound){
					self.success = null;
				}
			});
			self.addAssociateUpdateList=result.data;
			self.hasSourceUpdateError = result.data[0].errorFound;
			self.hasDestinationUpdateError = result.data[0].errorFound;
			self.showProductInfoWhenViewingOneProduct();
		};


		/**
		 * Will, if there is only one product being worked on, show the product info panel.
		 */
		self.showProductInfoWhenViewingOneProduct = function() {

			if (self.addAssociateList[0].source.productId == null) {
				return;
			}

			// If there is only one row, show the product information.
			var productId = self.addAssociateList[0].source.productId;

			// If there is more than one row, see if they are all related to the same product, if so, then
			// go ahead and show the prodcut info.
			for (var i = 1; i < self.addAssociateList.length; i++) {
				if (self.addAssociateList[i].source.productId != productId) {
					return;
				}
			}

			self.showProductInfo(self.addAssociateList[0].source.productId);
		};

		/**
		 * Submits a request to add the associate UPC.
		 */
		self.submitAddAssociate = function() {
			self.isWaitingForResponse = true;
			productInfoService.hide();
			upcSwapApi.addAssociateUpcs(self.addAssociateList, self.saveSuccess, self.setError);
		};

		/**
		 * Determines if the details for the page can be submitted.
		 *
		 * @returns {boolean}
		 */
		self.canSubmit = function() {
			// Can't submit if there are errors on any row.
			if (self.hasSourceError || self.hasDestinationError) {
				return false;
			}

			// If we've got data, you can submit.
			return 	self.hasData;
		};

		/**
		 * To handle when the user types in an item code, will clear out the UPC.
		 *
		 * @param addAssociate The UpcSwap to clear the UPC from.
		 */
		self.resetUpc = function(addAssociate) {
			self.hasData = false;
			if (addAssociate.source.itemCode != null) {
				addAssociate.source.upc = null;
			}
		};

		/**
		 * To handle when the user types in UPC, will clear out the item code.
		 *
		 * @param addAssociate The UpcSwap to clear the item code from.
		 */
		self.resetItemCode = function(addAssociate) {
			self.hasData = false;
			if (addAssociate.source.upc != null) {
				addAssociate.source.itemCode = null;
			}
		};

		/**
		 * Removes an add associate request from the list of requests being worked on.
		 *
		 * @param addAssociate The add associate request to remove.
		 */
		self.removeAddAssociate = function(addAssociate) {
			var indexToRemove = -1;
			for(var index = 0; index < self.addAssociateList.length; index++) {
				if (self.addAssociateList[index] == addAssociate) {
					indexToRemove = index;
					break;
				}
			}
			if (indexToRemove != -1) {
				self.addAssociateList.splice(indexToRemove, 1);
			}
		};

		/**
		 * Called when the user has updated the UPC to add.
		 *
		 * @param addAssociate The add associate reqeust the user edited.
		 */
		self.typedUpcToAdd = function(addAssociate) {
			self.hasData = false;
		};
	}

})();
