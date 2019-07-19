
'use strict';

/**
 * Controller to support the page that allows users to swap a warehouse upc to a new item code.
 *
 * @author m594201
 * @since 2.4.0
 */

(function () {
	angular.module('productMaintenanceUiApp').controller('SwapWarehouseUpcSwapController', swapWarehouseUpcSwapController);

	swapWarehouseUpcSwapController.$inject = ['UpcSwapApi', 'ProductInfoService', '$scope', '$timeout'];

	function swapWarehouseUpcSwapController(upcSwapApi, productInfoService, $scope, $timeout) {

		var self = this;

		/**
		 * Keeps track of whether a source has an error message.
		 *
		 * @type {boolean}
		 */
		self.hasSourceError = false;

		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Keeps track of whether a destination has an error message.
		 *
		 * @type {boolean}
		 */
		self.hasDestinationError = false;

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
		 * Whether or not the user is only working on one row of swaps.
		 *
		 * @type {boolean}
		 */
		self.showingSingleRow = false;

		/**
		 * Empty Upc Swap Object. Initialized to null, set as a new Upc Swap from back end after successful retrieve.
		 *
		 * @type {Object}
		 */
		self.emptyUpcSwapRecord = null;

		/**
		 * Initializes the controller.
		 */
		self.init = function () {
			// If the product info service already has data in it, get rid of it.
			productInfoService.hide();

			// get an empty upc swap
			self.getEmptyUpcSwap();
		};

        /**
         * Clear all datas that user entered or the system generated.
         */
        self.clearData = function(){
            productInfoService.hide();
            self.upcSwapList = [];
            self.addEmptyDataRow();
            self.showAfterUpdate =false;
        };

        /**
         * This function is used to check datas are null or not.
         *
         * @returns {boolean}
         */
        self.isEmptyData = function(){
            for (var i = 0; i < self.upcSwapList.length; i++) {
                if ((self.upcSwapList[i].source.productId != null && self.upcSwapList[i].source.productId != "")
                    || (self.upcSwapList[i].sourceUpc != null && self.upcSwapList[i].sourceUpc != "")
                    || (self.upcSwapList[i].source.itemCode != null && self.upcSwapList[i].source.itemCode != "")
                    || (self.upcSwapList[i].destination.productId != null && self.upcSwapList[i].destination.productId != "")
                    || (self.upcSwapList[i].destinationPrimaryUpc != null && self.upcSwapList[i].destinationPrimaryUpc != "")
                    || (self.upcSwapList[i].destination.itemCode != null && self.upcSwapList[i].destination.itemCode != "")
                    || (self.isViewingProductInfo() == true)
                ){
                    return false;
                }
            }
            return true;
        };

		/**
		 * Function to get an empty upc swap. If this is the first time being called (self.emptyUpcSwapRecord === null),
		 * call back end to fetch an empty upc swap. Else just call add empty data row.
		 */
		self.getEmptyUpcSwap = function () {
			if(self.emptyUpcSwapRecord === null) {
				upcSwapApi.getEmptySwap(self.addEmptyRowCallback, self.setError);
			} else {
				self.addEmptyDataRow();
			}
		};

		/**
		 * The callback from the request from the backend for a new, empty upc swap request.
		 *
		 * @param result The empty request sent from the backend.
		 */
		self.addEmptyRowCallback = function (result) {
			self.emptyUpcSwapRecord = result;
			self.addEmptyDataRow();
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
		 * Helper method to determine if the upc swap list has more than one swap.
		 *
		 * @returns {boolean}
		 */
		self.hasMoreThanOneSwap = function () {
			return self.upcSwapList !== null && self.upcSwapList.length > 1;
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
		 * Add an empty row to the upc swap array.
		 */
		self.addEmptyDataRow = function(){
			self.resetDefaultValues();
			self.upcSwapList.push(angular.copy(self.emptyUpcSwapRecord));
		};

		/**
		 * Resets default values for this controller
		 */
		self.resetDefaultValues = function(){
			self.error = null;
			self.hasSourceError = false;
			self.hasDestinationError = false;
		};

		/**
		 * Returns whether or not data row is empty of required information (source upc or source item code) and
		 * (destination primary upc or destination item code)
		 *
		 * @param upcSwapRecord
		 * @returns {boolean}
		 */
		self.isEmptyRecord = function(upcSwapRecord){
			return !(upcSwapRecord !== null &&
			(upcSwapRecord.sourceUpc !== null || upcSwapRecord.source !== null && upcSwapRecord.source.itemCode !== null)
			&& (upcSwapRecord.destination !== null && (upcSwapRecord.destinationPrimaryUpc !== null || upcSwapRecord.destination.itemCode !== null)));

		};

		/**
		 * Return if a passed in string is null or empty.
		 *
		 * @param string
		 */
		self.isEmptyString = function(string){
			return (string === null || string.length === 0);
		};

		/**
		 * Callback for when there is an error returned from the server.
		 *
		 * @param error The error from the server.
		 */
		self.setError = function(error){
			self.isWaitingForResponse = false;
			if(error){
				if (error.data) {
					if (error.data.message && error.data.message !== "") {
						self.error = error.data.message;
					} else {
						self.error = error.data.error;
					}
				} else if(error.message) {
					self.error = error.message;
				} else if(error.error) {
					self.error = error.error;
				} else {
					self.error = error;
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
			self.isWaitingForResponse = false;
			for(var index = 0; index < results.length; index++){
				upcSwap = results[index];

				// if the upcSwap source or destination side has an error
				if(upcSwap.errorFound) {
					errorFound = true;
					self.resetErrorDataRecord(upcSwap);
				} else {
					upcSwap.sourceInformation = formatSwapInformation(upcSwap.source);
					upcSwap.destinationInformation = formatSwapInformation(upcSwap.destination);
				}

				upcSwapList.push(upcSwap);

			}
			self.upcSwapList = upcSwapList;

			// If the user is only looking at one row, go ahead and show the product details page.
			if (results.length === 1 && !errorFound) {
				self.showingSingleRow = true;
				self.showProductInfo(results[0].source.productId, results[0].destination.productId);
			} else {
				self.showingSingleRow = false;
			}
		};

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

		/**
		 * Returns whether or not the user is currently viewing product information.
		 *
		 * @returns {boolean} True if the user is viewing product information and false otherwise.
		 */
		self.isViewingProductInfo = function() {
			return productInfoService.isVisible();
		};


		/**
		 * Returns whether or not the data row has fetched the details from the back end.
		 *
		 * @param upcSwapRecord data row to look at
		 * @returns {boolean}
		 */
		self.alreadyFetchedDetails = function(upcSwapRecord){
			return (upcSwapRecord !== null && upcSwapRecord.source !== null && upcSwapRecord.source.productId !== null &&
			upcSwapRecord.destination !== null && upcSwapRecord.destination.productId !== null);
		};

		/**
		 * Get the details of the upc('s) and item code('s) selected.
		 */
		self.getWarehouseUpcSwapDetails = function () {

			self.resetDefaultValues();
			productInfoService.hide();
			var upcSwapList = [];
			var foundData = false;
			var upcSwap;
			for(var index = 0; index < self.upcSwapList.length; index++){
				upcSwap = self.upcSwapList[index];
				upcSwap.destination.errorMessage = null;
				upcSwap.source.errorMessage = null;
				upcSwap.errorFound = false;
				if(!self.isEmptyRecord(upcSwap)){
					foundData = true;
					upcSwapList.push(upcSwap);
				}
			}

			if(!foundData){
				self.setError(({data: {message: "Please enter upc and item code before submitting request."}}));
				return;
			}

			if(upcSwapList.length > 0) {
				self.isWaitingForResponse = true;
				upcSwapApi.getWarehouseSwapUpcSwapsDetails(
					upcSwapList, self.setUpcSwapDetails, self.setError);
			}
		};

		/**
		 * Submit a warehouse upc swap.
		 */
		self.submitWarehouseToWarehouseUpcSwap = function () {
			var whsUpcSwapList = [];
			var foundData = false;
			var upcSwap;
			for(var index = 0; index < self.upcSwapList.length; index++){
				upcSwap = self.upcSwapList[index];
				if(!self.isEmptyRecord(upcSwap)){
					foundData = true;
					whsUpcSwapList.push(upcSwap);
				}
			}
			if(!foundData){
				self.setError(({data: {message: "Please submit at least one upc swap."}}));
			} else {
				self.isWaitingForResponse = true;
				upcSwapApi.submitWhsToWhsUpcSwap(whsUpcSwapList, self.setUpcSwapSuccess, self.setError);
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
			if (upcSwap.source !== null && upcSwap.source.errorMessage !== null) {
				self.hasSourceError = true;
			}

			// if the upcSwap destination side had an error
			if (upcSwap.destination !== null && upcSwap.destination.errorMessage !== null) {
				self.hasDestinationError = true;
			}

			// set fields to null so user has to refetch data before submitting
			upcSwap.sourcePrimaryUpc = null;
			upcSwap.selectSourcePrimaryUpc = null;
			if(upcSwap.source !== null) {
				upcSwap.source.productId = null;
				upcSwap.source.itemDescription = null;
				upcSwap.source.balanceOnHand = null;
				upcSwap.source.balanceOnOrder = null;
				upcSwap.source.onLiveOrPendingPog = null;
				upcSwap.source.productRetailLink = null;
				upcSwap.source.onUpcomingDelivery = null;
				upcSwap.source.purchaseOrderDisplayText = null;
			}
			upcSwap.destinationPrimaryUpcSelected = null;
			if(upcSwap.destination !== null) {
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

		/**
		 * Callback for when there is a successful return after submitting a upc swap.
		 *
		 * @param results The results from the server.
		 */
		self.setUpcSwapSuccess = function(results){
			self.isWaitingForResponse = false;
			self.afterUpdateList = results;
			self.errorResultList = self.getErrorList(results);
			self.showAfterUpdate = true;
			productInfoService.hide();
		};

		/**
		 * Closes the view for upc swap submission details.
		 */
		self.closePage = function(){
			self.showAfterUpdate = false;
			self.upcSwapList = self.errorResultList;
			if(self.upcSwapList.length === 0) {
				self.addEmptyDataRow();
			}
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
		 *  This method is used to disable 'Fetch Details' button by checking to see if any row of a upc swap does not
		 *  have necessary components or if there are only empty upc swaps. If someone has entered one side of upc swap
		 *  (source), but not entered other side (destination), this is considered 'not filled in'.
		 *  Returns true if:
		 *  1. any upc swap row is not filled in, or
		 *  2. all upc swaps are empty
		 *  otherwise, return false
		 *
		 * @returns {boolean} Return true if any upc swap row is considered not filled in or all empty upc rows,
		 * false otherwise.
		 */
		self.isFetchDetailsDisabled = function () {
			var upcRowNotReadyForFetchDetails = false;
			var atLeastOneUpcSwapToFetch = false;
			for(var index = 0; index < self.upcSwapList.length; index++){

				// ignore empty rows
				if(self.isEmptyRecord(self.upcSwapList[index])){
					continue;
				}

				// if any upc swap is not filled in, set upcRowNotReadyForFetchDetails = true and break out of loop
				if(self.isUpcSwapRowNotFilledIn(self.upcSwapList[index])){
					upcRowNotReadyForFetchDetails = true;
					break;
				}

				// at least one filled in upc swap
				atLeastOneUpcSwapToFetch = true;
			}
			return upcRowNotReadyForFetchDetails || !atLeastOneUpcSwapToFetch;
		};

		/**
		 * Helper method to see if a particular upc swap essential parts should be considered 'not filled in'. The
		 * essential parts are a combination of either:
		 * 1. the source upc or the source item code, AND
		 * 2. the destination upc or the destination item code
		 * If the upc swap has both of these components, it is 'filled in': return false. Otherwise, return true.
		 *
		 * @param upcSwap The upc swap to test for essential components
		 * @returns {boolean} true if not 'filled in', false otherwise
		 */
		self.isUpcSwapRowNotFilledIn = function (upcSwap) {

			// if the source upc OR source item code is filled in
			if(!this.isEmptyString(upcSwap.sourceUpc) || !this.isEmptyString(upcSwap.source.itemCode)) {
				// and if the destination upc AND the destination item code is not filled in
				if(this.isEmptyString(upcSwap.destinationPrimaryUpc) && this.isEmptyString(upcSwap.destination.itemCode)){
					// the upc swap record is considered not filled in
					return true;
				}
			}

			// else if the destination upc OR destination item code is filled in
			else if(!this.isEmptyString(upcSwap.destinationPrimaryUpc) || !this.isEmptyString(upcSwap.destination.itemCode)) {
				// and if the source upc AND the source item code is not filled in
				if(this.isEmptyString(upcSwap.sourceUpc) && this.isEmptyString(upcSwap.source.itemCode)){
					// the upc swap record is considered not filled in
					return true;
				}
			}

			// otherwise the upc swap record is considered filled in
			return false;
		};

		/**
		 * The method returns the current value of the text being focused by the user. It returns an item code or UPC
		 * from the upc swap source or destination.
		 *
		 * @param upcSwap The upc swap with updated information.
		 * @param isSourceChanged Whether or not the source was changed (else destination).
		 * @param isUpcChanged Whether or not the UPC was changed (else item code).
		 * @returns {Number}
		 */
		function getCurrentFocusText(upcSwap, isSourceChanged, isUpcChanged) {

			// if source and upc were changed
			if(isSourceChanged && isUpcChanged) {
				return upcSwap.sourceUpc;
			}
			// else if source and item code were changed
			else if(isSourceChanged && !isUpcChanged){
				return upcSwap.source.itemCode;
			}
			// else if destination and upc were changed
			else if(!isSourceChanged && isUpcChanged) {
				return upcSwap.destinationPrimaryUpc;
			}
			// else destination and item code were changed
			else {
				return upcSwap.destination.itemCode;
			}
		}

		/**
		 * This method resets other information on a upc swap so the user must 'Fetch Details' on this upc swap before
		 * submitting. Any information that cannot be input by the user needs to be reset. If the user just entered:
		 * 1. source upc: the source item code needs to be reset.
		 * 2. source item code: the source upc needs to be reset.
		 * 3. destination upc: the destination item code needs to be reset.
		 * 4. destination item code: the destination upc needs to be reset.
		 *
		 * @param upcSwap The upc swap that needs to be reset.
		 * @param isSourceChanged Whether or not the source was changed (else destination).
		 * @param isUpcChanged Whether or not the UPC was changed (else item code).
		 */
		function resetUpcSwap(upcSwap, isSourceChanged, isUpcChanged) {
			// if source and upc were changed
			if(isSourceChanged && isUpcChanged) {
				upcSwap.source.itemCode = null;
			}
			// else if source and item code were changed
			else if(isSourceChanged && !isUpcChanged){
				upcSwap.sourceUpc = null;
			}
			// else if destination and upc were changed
			else if(!isSourceChanged && isUpcChanged) {
				upcSwap.destination.itemCode = null;
			}
			// else destination and item code were changed
			else {
				upcSwap.destinationPrimaryUpc = null;
			}

			// reset other source information
			upcSwap.source.productId = null;
			upcSwap.selectSourcePrimaryUpc = null;
			upcSwap.source.itemDescription = null;

			// reset destination information
			upcSwap.destination.productId = null;
			upcSwap.destinationPrimaryUpcSelected = null;
			upcSwap.destination.itemDescription = null;
		}

		/**
		 * This function sets a particular attribute (or particular nested attribute) values on a upc swap based on the
		 * text entered by the user.
		 * The outerAttributeToChange parameter is the attribute at the first level (i.e. 'sourceUpc').
		 * The nestedAttributeToChange parameter will only be used when the particular attribute is a
		 * nested attribute (i.e. 'source.itemCode'); otherwise it is null.
		 *
		 * @param upcSwap Upc swap to change.
		 * @param textSplit The text the user has entered after it has been split.
		 * @param inputIndex Index of the upc swap with updated information.
		 * @param isSourceChanged Whether or not the source was changed (else destination).
		 * @param isUpcChanged Whether or not the UPC was changed (else item code).
		 * @param outerAttributeToChange Outer attribute to change.
		 * @param nestedAttributeToChange Nested attribute to change.
		 * @param commonIdText The common id for the html element in question.
		 */
		function setUpcSwapValuesOnChange(upcSwap, textSplit, inputIndex, isSourceChanged, isUpcChanged, outerAttributeToChange, nestedAttributeToChange, commonIdText){
			// if attribute to change was nested
			if(nestedAttributeToChange !== null){
				upcSwap[outerAttributeToChange][nestedAttributeToChange] = textSplit[0];
			}
			// else attribute was not nested
			else {
				upcSwap[outerAttributeToChange] = textSplit[0];
			}
			var focusUpc;
			var swap;
			for(var index = 0; index < textSplit.length; index++){

				focusUpc = index + inputIndex;

				// if current upc swap exists
				if(self.upcSwapList.length > (focusUpc)) {
					swap = self.upcSwapList[focusUpc];
					if(nestedAttributeToChange !== null){
						swap[outerAttributeToChange][nestedAttributeToChange] = textSplit[index];
					} else {
						swap[outerAttributeToChange] = textSplit[index];
					}
					resetUpcSwap(swap, isSourceChanged, isUpcChanged);
				}

				// else create a new data row
				else {
					swap = angular.copy(self.emptyUpcSwapRecord);
					if(nestedAttributeToChange !== null){
						swap[outerAttributeToChange][nestedAttributeToChange] = textSplit[index];
					} else {
						swap[outerAttributeToChange] = textSplit[index];
					}
					self.upcSwapList.push(swap);
				}
			}

			// sets focus on last upc swap updated after angular renders it (null pointer otherwise)
			$timeout(function(){
				$scope.$evalAsync(function() {
					document.getElementById(commonIdText + focusUpc).focus();
				});
			});
		}

		/**
		 * Helper method to wipe out other information on a upc swap when user changes one of the available inputs,
		 * so that the user must fetch details again before submitting the upc swap(s). This method takes in the
		 * index of the upc swap that was changed, whether the source or destination was changed, and whether the UPC
		 * or item code was changed.
		 *
		 * @param inputIndex Index of the upc swap with updated information.
		 * @param isSourceChanged Whether or not the source was changed (else destination).
		 * @param isUpcChanged Whether or not the UPC was changed (else item code).
		 * @param IEClipData Internet Explorer clipboard data.
		 */
		self.updateDataOnChange = function (inputIndex, isSourceChanged, isUpcChanged, IEClipData) {

			var upcSwap = self.upcSwapList[inputIndex];
			var currentPlaceText = getCurrentFocusText(upcSwap, isSourceChanged, isUpcChanged);

			if(currentPlaceText === null && IEClipData === null){
				return;
			}

			var textSplit;
			// if internet explorer paste event
			if(typeof IEClipData !== 'undefined'){
				textSplit = IEClipData;
			}
			// else other browser paste event
			else {
				textSplit = currentPlaceText.split(/[\s,]+/);
			}

			// if source and upc were changed
			if(isSourceChanged && isUpcChanged) {
				setUpcSwapValuesOnChange(upcSwap, textSplit, inputIndex, isSourceChanged, isUpcChanged, 'sourceUpc', null, 'sourceUpc');
			}
			// else if source and item code were changed
			else if(isSourceChanged && !isUpcChanged){
				setUpcSwapValuesOnChange(upcSwap, textSplit, inputIndex, isSourceChanged, isUpcChanged, 'source', 'itemCode', 'sourceItemCode');
			}
			// else if destination and upc were changed
			else if(!isSourceChanged && isUpcChanged) {
				setUpcSwapValuesOnChange(upcSwap, textSplit, inputIndex, isSourceChanged, isUpcChanged, 'destinationPrimaryUpc', null, 'destinationUpc');
			}
			// else destination and item code were changed
			else {
				setUpcSwapValuesOnChange(upcSwap, textSplit, inputIndex, isSourceChanged, isUpcChanged, 'destination', 'itemCode', 'destinationItemCode');
			}
		};

		/**
		 * Gets the upc swap data in the correct format when pasted in IE. Other browsers will already have the
		 * information needed to process the pasted information. All browsers call both 'ng-paste' and 'ng-change' on
		 * 'paste' event. Non IE browsers can just rely on 'change' event method self.updateDataOnChange.
		 *
		 * @param inputIndex Index of the upc swap with updated information.
		 * @param isSourceChanged Whether or not the source was changed (else destination).
		 * @param isUpcChanged Whether or not the UPC was changed (else item code).
		 */
		self.updateDataOnPaste = function (inputIndex, isSourceChanged, isUpcChanged) {
			if(window.clipboardData) {
				var clipData = window.clipboardData.getData('Text');
				clipData.replace(/(\s)/g, " ");
				clipData = self.removeEmptyValuesFromArray(clipData.split(/[\s,]+/));
				self.updateDataOnChange(inputIndex, isSourceChanged, isUpcChanged, clipData)
			}
		};

		/**
		 * Remove any element from array that should be treated as empty. {This method needed to be added for
		 * internet explorer to show expected behavior}
		 *
		 * @param arrayToInspect array to inspect for empty elements
		 * @returns {Array}
		 */
		self.removeEmptyValuesFromArray = function (arrayToInspect) {
			var currentElement;
			var arrayToReturn = [];
			for(var index = 0; index < arrayToInspect.length; index++){
				currentElement = arrayToInspect[index];
				// if element is null or empty, continue
				if(currentElement === null || typeof currentElement === 'undefined' || currentElement === ""){
					continue;
				}
				arrayToReturn.push(currentElement);
			}
			return arrayToReturn;
		};
	}
})();
