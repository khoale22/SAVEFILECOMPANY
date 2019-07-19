/*
 *   choiceComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Code Table ->choice option page component.
 *
 * @author vn70516
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('choiceOption', {
		bindings:{
			onHandleTabChange: '&',
			sendingDataToTab:'&',
			messageSending:'='
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/codeTable/choice/choiceOption.html',
		// The controller that handles our component logic
		controller: ChoiceOptionController
	}).filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
	});

	ChoiceOptionController.$inject = ['$rootScope','$scope', 'ChoiceApi', 'ngTableParams','$filter', 'DownloadImageService'];

	/**
	 * Code Table choice option component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @constructor
	 */
	function ChoiceOptionController($rootScope, $scope, choiceApi, ngTableParams, $filter, downloadImageService) {
		/** All CRUD operation controls of choice option page goes here */
		var self = this;
		/**
		 * Error message
		 * @type {string}
		 */
		self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
		/**
		 * The default error message.
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * Flag for waiting response from back end.
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * The list of choice type information.
		 * @type {Array}
		 */
		self.choiceTypes = [];

		/**
		 * The list of choice option information.
		 * @type {Array}
		 */
		self.choiceOptions = [];

		/**
		 * The list of choice option image information.
		 * @type {Array}
		 */
		self.choiceOptionImages = [];

		/**
		 * The list of choice options to add new/edit/delete.
		 * @type {Array}
		 */
		self.choiceOptionsHandle = [];

		/**
		 * The list of choice options to add new/edit/delete back up.
		 * @type {Array}
		 */
		self.choiceOptionsHandleOrig = [];


		/**
		 * Save current choice type user want to delete, but fail in validation.
		 * @type {{}}
		 */
		self.choiceTypeValidationFail = {};

		/**
		 * Show error message in pop up.
		 * @type {string}
		 */
		self.errorPopup = '';

		/**
		 * The title model.
		 * @type {string}
		 */
		self.titleModel = '';

		/**
		 * The value action. Add New/Edit
		 * @type {string}
		 */
		self.action = '';

		/**
		 * Title for pop up confirm basing on each handle.
		 * @type {string}
		 */
		self.confirmTitle = '';

		/**
		 * Message for pop up confirm basing on each handle.
		 * @type {string}
		 */
		self.confirmMessage = '';

		/**
		 * The value action what you want to confirm.
		 * @type {string}
		 */
		self.confirmAction = '';

		/**
		 * The waiting loading image.
		 * @type {boolean}
		 */
		self.waitingImage = false;

		/**
		 * Current image loading.
		 * @type {string}
		 */
		self.imageBytes = '';

		/**
		 * Current image format.
		 * @type {string}
		 */
		self.imageFormat = '';

		/**
		 * Choice table
		 */
		self.choiceTableParams;
		self.RETURN_TAB = 'returnTab';

		/**
		 * Status on live of choice page component.
		 * @type {boolean}
		 */
		self.onLive = true;

		self.choiceOptionToDelete = null;

		/**
		 * Choice Option details.
		 */
		self.details = {
			isShowing: false
		};

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.onLive = true;
			self.isWaitingForResponse = true;
			self.findAllChoiceType();
			self.findAllChoiceOption();
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			self.onLive = false;
		};

		/**
		 * Find all the list of choice type information.
		 */
		self.findAllChoiceType = function () {
			choiceApi.findAllChoiceType(
				function (results) {
					self.choiceTypes = results;
					self.findAllChoiceOption();
				},
				//error
				self.fetchError
			);
		};

		/**
		 * Find all the list of choice option information.
		 */
		self.findAllChoiceOption = function () {
			choiceApi.findAllChoiceOption(
				function (results) {
					angular.forEach(results, function(value) {
						if(value.choiceType){
							value["choiceTypeName"] = value.choiceType.displayName;
						}
					});
					self.loadChoiceData(results);
					self.findAllChoiceOptionImageUri();
				},
				//error
				self.fetchError
			);
		};

		/**
		 * Find all the list of choice option information.
		 */
		self.findAllChoiceOptionImageUri = function () {
			choiceApi.findAllChoiceOptionImageUri(
				function (results) {
					self.isWaitingForResponse = false;
					self.choiceOptionImages = results;
					self.findChoiceOptionImageByUri(0);
				},
				//error
				self.fetchError
			);
		};

		/**
		 * Find all the list of choice option information.
		 */
		self.findChoiceOptionImageByUri = function (index) {
			if(self.choiceOptionImages && self.choiceOptionImages.length > index && self.choiceOptionImages[index].uriText != ''){
				choiceApi.findChoiceOptionImageByUri({
						imageUri:self.choiceOptionImages[index].uriText
					},
					function (results) {
						if(results.data != null && results.data!= ''){
							self.choiceOptionImages[index]["image"] = "data:image/jpg;base64,"+results.data;
						}else{
							self.choiceOptionImages[index]["image"] = ' ';
						}
						if(self.onLive){
							self.findChoiceOptionImageByUri(index+1);
						}
					},
					//error
					self.fetchError
				);
			}
		};

		/**
		 * Find image by choice option code.
		 * @param choiceOptionCode
		 * @returns {Array|*}
		 */
		self.findImageByChoiceCode = function (choiceOptionCode) {
			for(var i=0;i<self.choiceOptionImages.length;i++){
				if(self.choiceOptionImages[i].key.id == choiceOptionCode){
					return self.choiceOptionImages[i].image;
				}
			}
			return null;
		};

		/**
		 * Find choice option image by choice option code.
		 * @param choiceOptionCode
		 * @returns {Array|*}
		 */
		self.findChoiceImageByChoiceCode = function (choiceOptionCode) {
			for(var i=0;i<self.choiceOptionImages.length;i++){
				if(self.choiceOptionImages[i].key.id == choiceOptionCode){
					return self.choiceOptionImages[i];
				}
			}
			return null;
		};

		/**
		 * Check image available for choice option.
		 * @param choiceOptionCode
		 * @returns {boolean}
		 */
		self.imageAvailable = function (choiceOptionCode) {
			for(var i=0;i<self.choiceOptionImages.length;i++){
				if(self.choiceOptionImages[i].key.id == choiceOptionCode) {
					return true;
				}
			}
			return false;
		};

		/**
		 * Saves the factory data, updates switches to add checks to factories, sets the ng table up.
		 * @param results
		 */
		self.loadChoiceData = function(results){
			$scope.filter = {
				key:{
					choiceOptionCode: undefined
				} ,
				productChoiceText: undefined,
				choiceType: {
					displayName:undefined
				}
			};
			self.choiceOptions  = angular.copy(results);
			self.choiceTableParams = new ngTableParams(
				{
					page: 1,
					count:20,
					filter: $scope.filter
				}, {
					counts: [],
					data: self.initDataForNgTable()
				}
			);
		};

		/**
		 * processing data has returned from api
		 * @returns {Array}
		 */
		self.initDataForNgTable = function(){
			angular.forEach(self.choiceOptions,function (choiceOption) {
				choiceOption.isEditing = false;
			});
			return self.choiceOptions;
		};




		/**
		 * Request delete the list of choice option.
		 */
		self.deleteChoiceOption = function (choiceOption) {
			self.choiceOptionToDelete = angular.copy(choiceOption);
			self.confirmTitle = "Delete Choice";
			self.confirmMessage = "Are you sure you want to delete the selected Choice?";
			self.confirmAction = "DELETE";
			$('#confirmDeleteModal').modal({backdrop: 'static', keyboard: true});
		};


		/**
		 * Do delete the list of choice has been selected.
		 */
		self.doDeleteChoiceOption = function (choiceOption) {
			$('#confirmDeleteModal').modal("hide");
			self.error = '';
			self.success = '';
			self.isWaitingForResponse = true;
			//call to controller handle delete the list of factory
			choiceApi.deleteChoiceOptions(self.createChoiceOptionList(choiceOption),
				function (results) {
					self.isWaitingForResponse = false;
					self.factoryToDelete = null;
					self.success = results.message;
					self.loadChoiceData(results.data);
				},
				function (error) {
					self.fetchError(error);
				}
			)
		};

		self.createChoiceOptionList = function(choiceOption){
			var choiceOptions = [];
			choiceOptions.push(choiceOption);
			return choiceOptions;
		};

		/**
		 * Add new choice type handle when add new button click.
		 */
		self.addNewChoiceOptions = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.resetValidation();
			self.choiceOptionsHandle = [];
			var choiceOption = self.createChoiceOptionEmpty();
			self.choiceOptionsHandle.push(choiceOption);
			self.choiceOptionsHandleOrig = angular.copy(self.choiceOptionsHandle);
			self.titleModel = "Add New Choice Option";
			self.action = "ADD";
			$('#choiceOptionModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Do add new the list of choice type. Call to back end to insert to database.
		 */
		self.updateChoiceOptions = function () {
			if(self.validationChoiceOptionsBeforeUpdate()){
				if(self.action == "ADD"){
					$('#choiceOptionModal').modal("hide");
					self.isWaitingForResponse = true;
					choiceApi.addNewChoiceOptions(
						self.choiceOptionsHandle,
						function (results) {
							self.isWaitingForResponse = false;
							self.checkAllFlag = false;
							angular.forEach(results.data, function (value) {
								value["selected"] = false;
								self.choiceOptionsHandle = [];
							});
							self.loadChoiceData(results.data);
							self.success = results.message;
							self.resetAllFlag();
						},
						//error
						self.fetchError
					);
				}
			}
		};

		/**
		 * Check all field is valid before add new or update choice type.
		 * @returns {boolean}
		 */
		self.validationChoiceOptionsBeforeUpdate = function () {
			self.errorPopup = '';
			var emptyChoiceType = false;
			var emptyChoiceDesc = false;
			for (var i = 0; i < self.choiceOptionsHandle.length; i++) {
				if (self.choiceOptionsHandle[i].choiceType == null || self.choiceOptionsHandle[i].choiceType.choiceTypeCode == null || self.choiceOptionsHandle[i].choiceType.choiceTypeCode == '') {
					self.choiceOptionsHandle[i].addClass = 'active-tooltip';
					emptyChoiceType = true;
				}
				if(self.choiceOptionsHandle[i].productChoiceText == null || self.choiceOptionsHandle[i].productChoiceText == '') {
					self.choiceOptionsHandle[i].addClass = 'active-tooltip ng-invalid ng-touched';
					emptyChoiceDesc = true;
				}
				if(emptyChoiceType && emptyChoiceDesc){
					break;
				}
			}
			if(emptyChoiceType && emptyChoiceDesc){
				self.errorPopup = "Choice: <li>Choice Type is mandatory field.</li><li>Choice Option Text is mandatory field.</li>";
				return false;
			}else if(emptyChoiceType){
				self.errorPopup = "Choice: <li>Choice Type is mandatory field.</li>";
				return false;
			}else if(emptyChoiceDesc){
				self.errorPopup = "Choice: <li>Choice Option Text is mandatory field.</li>";
				return false;
			}else{
				return true;
			}
		};

		/**
		 * Create a empty choice type object.
		 * @returns {{}}
		 */
		self.createChoiceOptionEmpty = function () {
			var choiceOption = {};
			choiceOption["key"] = {};
			choiceOption["productChoiceText"] = '';
			choiceOption["choiceType"] = null;
			return choiceOption;
		};

		/**
		 * Add one more choice type
		 */
		self.addMoreRowChoiceOption = function () {
			if(self.validationChoiceOptionsBeforeUpdate()) {
				var choiceOption = self.createChoiceOptionEmpty();
				self.choiceOptionsHandle.push(choiceOption);
			}
		}

		/**
		 * Check change data in pop up handle edit/add new
		 * @returns {boolean}
		 */
		self.isDataChanged = function () {
			if(self.choiceOptionsHandle.length != self.choiceOptionsHandleOrig.length){
				return true;
			}
			var isDataChange = false;
			for(var i = 0; i < self.choiceOptionsHandle.length; i++){
				if($.trim(self.choiceOptionsHandle[i]['productChoiceText']) !== $.trim(self.choiceOptionsHandleOrig[i]['productChoiceText']) ||
					$.trim(self.choiceOptionsHandle[i]['choiceType']) !== $.trim(self.choiceOptionsHandleOrig[i]['choiceType'])){
					isDataChange = true;
					break;
				}
			}
			return isDataChange;
		}

		/**
		 * Reset data orig for pop up handle add new, edit.
		 */
		self.resetDataOrigForPopUp = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.resetValidation();
			self.choiceOptionsHandle = angular.copy(self.choiceOptionsHandleOrig);
			self.choiceSelectedTableParams.page(1);
			self.choiceSelectedTableParams.reload();
		}

		/**
		 * Show popup message confirm when close pop during data has been changed.
		 */
		self.closePopupHandle = function () {
			if(self.isDataChanged()){
				self.confirmTitle = "Confirmation";
				self.confirmMessage = "Unsaved data will be lost. Do you want to save the changes before continuing?";
				self.confirmAction = "SAVE";
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			}else{
				$('#choiceOptionModal').modal("hide");
			}
		}

		/**
		 * Confirmed to delete/update change data
		 */
		self.confirmActionHandle = function () {
			$('#confirmModal').modal("hide");
			if(self.confirmAction == "DELETE"){
				self.doDeleteChoiceOption();
			}else if(self.confirmAction == "SAVE"){
				self.updateChoiceOptions();
			}
		}

		/**
		 * No confirm to delete/update change data
		 */
		self.noConfirmActionHandle = function () {
			$('#confirmModal').modal("hide");
			$('#choiceOptionModal').modal("hide");
		};

		/**
		 * Clear all filter handle.
		 */
		self.clearFilter = function () {
			$scope.filter.key.choiceOptionCode = undefined;
			$scope.filter.productChoiceText = undefined;
			$scope.filter.choiceType.displayName = undefined;
		};

		/**
		 * Show full image, and allow user download
		 */
		self.showFullImage  = function (choiceOptionCode) {
			self.imageBytes = '';
			self.imageFormat =''
			var choiceImage = self.findChoiceImageByChoiceCode(choiceOptionCode);
			if(choiceImage != null && choiceImage.image != ' '){
				self.imageFormat = choiceImage.imageFormatCode;
				self.waitingImage = true;
				$('#imageModal').modal({backdrop: 'static', keyboard: true});
				choiceApi.findChoiceOptionImageByUri({
						imageUri:choiceImage.uriText
					},
					function (results) {
						self.waitingImage = false;
						if(results.data != null && results.data!= ''){
							self.imageBytes = "data:image/jpg;base64,"+results.data;
						}else{
							self.imageBytes = ' ';
							$('#imageModal').modal("hide");
						}
					},
					//error
					function (error) {
						self.waitingImage = false;
						$('#imageModal').modal("hide");
						self.fetchError(error);
					}
				);
			}
		}

		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if(self.imageBytes !== '' ){
				downloadImageService.download(self.imageBytes, self.imageFormat);
			}
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function(error) {
			self.isWaitingForResponse = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
			if(self.isReturnToTab){
				$rootScope.error = self.error;
				$rootScope.success = null;
				$rootScope.isEditedOnPreviousTab = true;
			}
			self.isWaitingForResponse = false;
		};

		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function(error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return self.UNKNOWN_ERROR;
			}
		};

		/** Allow show multi modal the same time wwith stack**/
		$('.modal').on('show.bs.modal', function (event) {
			var zIndex = 1040 + (10 * $('.modal:visible').length);
			$(this).css('z-index', zIndex);
			setTimeout(function() {
				$('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
			}, 0);
		});
		/**
		 * Reset validation.
		 */
		self.resetValidation = function () {
			$scope.addEditForm.$setPristine();
			$scope.addEditForm.$setUntouched();
			$scope.addEditForm.$rollbackViewValue();
		};

		/**
		 * go to factory detail page, show more information for user. User an edit, add new on here.
		 * @param tab
		 * @param item
		 */
		self.gotoChoiceOptionDetails = function (tab, item) {
			if(item === null){
				item = self.createChoiceOptionEmpty();
			}
			self.details = item;
			self.details.isShowing = true;
		};

		//Update Edit Inline
		self.hasOtherRowEditing = false;
		self.selectedRowId = null;
		self.selectedRowIndex = -1;
		self.selectedRow = null;
		self.isReturnToTab = false;
		self.VALIDATE_CHOICE_OPTION = 'validateChoiceOption';
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		self.MESSAGE_NO_DATA_CHANGE = 'There are no changes on this page to be saved. Please make any changes to update.';

		/**
		 * Select record to edit and enable it.
		 *
		 * @param choiceOption
		 */
		self.enableRow = function(choiceOption){
			self.error = '';
			self.success = '';
			choiceOption.isEditing = true;
			self.hasOtherRowEditing = true;
			self.selectedRowId = choiceOption.key.choiceOptionCode;
			self.selectedRowIndex = self.choiceOptions.indexOf(choiceOption);
			self.selectedRow = angular.copy(choiceOption);
			self.choiceTableParams.reload();
		};

		/**
		 *
		 * @param choiceOptionCode
		 * @returns {*}
		 */
		self.getButtonStyle = function(choiceOptionCode){
			if (self.selectedRowId !== null && choiceOptionCode !== self.selectedRowId){
				return 'opacity: 0.5;'
			}
			return 'opacity: 1.0;';
		};

		/**
		 * reset data of record
		 */
		self.resetCurrentRow = function(){
			self.error = '';
			self.success = '';
			self.choiceOptions[self.selectedRowIndex] = angular.copy(self.selectedRow);
			self.choiceOptions[self.selectedRowIndex].isEditing = false;
			self.resetAllFlag();
			self.choiceTableParams.reload();
		};

		/**
		 * reset all flags
		 */
		self.resetAllFlag = function(){
			self.hasOtherRowEditing = false;
			self.selectedRowId = null;
			self.selectedRowIndex = -1;
			self.selectedRow = null;
		};

		/**
		 * call api to update choice option.
		 * @param choiceOption
		 */
		self.saveData = function(choiceOption){
			$('#changeTabConfirmation').modal("hide");
			if (self.hasDataChanged(choiceOption)){
				if (self.validationBeforeUpdate(choiceOption)){
					self.isWaitingForResponse = true;
					choiceApi.updateChoiceOptions(
						self.createDataForRequestApi(choiceOption),
						function (results) {
							self.success = results.message;
							self.loadChoiceData(results.data);
							self.isWaitingForResponse = false;
							self.resetAllFlag();
							if(self.isReturnToTab){
								$rootScope.error = null;
								$rootScope.success = self.success;
								$rootScope.isEditedOnPreviousTab = true;
							}
							self.returnToTab();
						},
						//error
						self.fetchError
					);
				}

			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * Create list data for update
		 * @param choiceOption
		 * @returns {Array}
		 */
		self.createDataForRequestApi = function(choiceOption) {
			var choiceOptions = [];
			var choiceOptionForRequest = {
				productChoiceText: choiceOption.productChoiceText,
				key: {
					choiceOptionCode: choiceOption.key.choiceOptionCode
				},
				choiceType: {
					choiceTypeCode: choiceOption.choiceType.choiceTypeCode
				}
			};
			choiceOptions.push(choiceOptionForRequest);
			return choiceOptions;
		};

		/**
		 * Check if has changed data
		 * @param choiceOption
		 * @returns {boolean}
		 */
		self.hasDataChanged = function(choiceOption){
			if (choiceOption !== null && choiceOption !== undefined) {
				var choiceOptionTemp = angular.copy(choiceOption);
				var choiceOptionSelectedTemp = angular.copy(self.selectedRow);
				delete choiceOptionTemp.isEditing;
				delete choiceOptionSelectedTemp.isEditing;
				return !(JSON.stringify(choiceOptionTemp) === JSON.stringify(choiceOptionSelectedTemp))
			}
			return false;
		};

		/**
		 * Validation data before update
		 * @param choiceOption
		 * @returns {boolean}
		 */
		self.validationBeforeUpdate = function (choiceOption) {
			self.error = '';
			self.success = '';
			if (self.isNullOrEmpty(choiceOption.productChoiceText)) {
				choiceOption.addClass = 'active-tooltip ng-invalid ng-touched';
				self.error += '<li>Choice Option Text is mandatory field.</li>';
			}
			if (self.error !== ''){
				self.error = 'Choice Option:' + self.error;
				return false;
			}
			return true;
		};

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === null || !object || object === "";
		};

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function () {
			if (self.isReturnToTab) {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_CHOICE_OPTION, function () {
			if(self.details == null || self.details  == undefined || !self.details.isShowing ){
			if (self.selectedRow !== null && self.hasDataChanged(self.findChoiceOptionByCode(self.selectedRowId))) {
				self.isReturnToTab = true;
				self.error = '';
				self.success = '';
				$('#changeTabConfirmation').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
			}
		});

		/**
		 * find Choice Option by Choice Option Code
		 * @param choiceOptionCode
		 * @returns {*}
		 */
		self.findChoiceOptionByCode = function(choiceOptionCode){
			for(var i = 0; i < self.choiceOptions.length; i++) {
				if (self.choiceOptions[i].key.choiceOptionCode === choiceOptionCode){
					return self.choiceOptions[i];
				}
			}
		};

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function () {
			if (self.isReturnToTab) {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		};

		/**
		 * Close Popup
		 */
		self.doClosePopupConfirmation = function () {
			$('#changeTabConfirmation').modal('hide');
			if (self.isReturnToTab) {
				$('#changeTabConfirmation').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			}
		};

		/**
		 * Hides save confirm dialog.
		 */
		self.cancelConfirmDialog = function () {
			$('.modal-backdrop').attr('style', '');
			$('#changeTabConfirmation').modal('hide');
		};

		/**
		 * Show mesage after update choice option success.
		 */
		$scope.$on('showMessageUpdateChoice', function (event, message) {
			self.success = message;
		});
	}
})();
