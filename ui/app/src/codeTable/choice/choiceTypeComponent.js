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
 * Code Table ->choice type page component.
 *
 * @author vn70516
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('choiceType', {
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/codeTable/choice/choiceType.html',
		// The controller that handles our component logic
		controller: ChoiceTypeController
	}).filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
	});

	ChoiceTypeController.$inject = ['$rootScope', '$scope', 'ChoiceTypeApi','$filter','ngTableParams'];

	/**
	 * Code Table choice type component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @constructor
	 */
	function ChoiceTypeController($rootScope, $scope, choiceTypeApi, $filter, ngTableParams) {
		/** All CRUD operation controls of choice option page goes here */
		var self = this;
		/**
		 * Error Message
		 * @type {string}
		 */
		self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
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
		 * The list of choice type filtered.
		 * @type {Array}
		 */
		self.choiceTypeFiltered = [];

		/**
		 * The flag all choice option seleted.
		 * @type {boolean}
		 */
		self.checkAllFlag = false;

		/**
		 * The error message in popup
		 * @type {string}
		 */
		self.errorPopup = '';

		/**
		 * The list of choice type to add new/edit/delete.
		 * @type {Array}
		 */
		self.choiceTypesHandle = [];

		/**
		 * The list of choice type to add new/edit/delete back up
		 * @type {Array}
		 */
		self.choiceTypesHandleOrig = [];

		/**
		 * The flag allow delete choice type.
		 * @type {boolean}
		 */
		self.allowDeleteChoiceType = true;

		/**
		 * The label for button close in confirm pop up.
		 * @type {string}
		 */
		self.labelClose = 'No';

		/**
		 * Save current choice type user want to delete, but fail in validation.
		 * @type {{}}
		 */
		self.choiceTypeValidationFail = {};

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
		 * Show error message in pop up.
		 * @type {string}
		 */
		self.errorPopup = '';

		/**
		 * init table params
		 */
		self.choiceTypeTableParams;

		/**
		 * init table params selected
		 */
		self.choiceTypeTableParamsSelected;

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
		 * Data resolving params.
		 */
		self.dataResolvingParams;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.isWaitingForResponse = true;
			self.choiceTypes = [];
			self.findAllChoiceType();
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
		};

		/**
		 * Find all the list of choice type information.
		 */
		self.findAllChoiceType = function () {
			choiceTypeApi.findAllChoiceType(
				function (results) {
					self.isWaitingForResponse = false;
					angular.forEach(results, function (value) {
						value["selected"] = false;
						if(value.parentChoiceType != null){
							value["parentDisplayName"] = value.parentChoiceType.displayName
						}else{
							value["parentDisplayName"] = '';
						}

					});
					self.loadChoiceTypeData(results);
				},
				//error
				self.fetchError
			);
		}

		/**
		 * Saves the choice type data, updates switches to add checks to choice type, sets the ng table up.
		 * @param results
		 */
		self.loadChoiceTypeData = function(results){
			$scope.filter = {
				choiceTypeCode:undefined,
				abbreviation: undefined,
				displayName: undefined,
				parentDisplayName:undefined
			};
			self.choiceTypes = angular.copy(results);
			self.choiceTypeTableParams = new ngTableParams(
				{
					page: 1,
					count:20,
					filter: $scope.filter
				}, {
					counts: [],
					data: self.initDataForNgTable()
				}
			);
			self.updateChoiceTypeList();
		};

		self.initDataForNgTable = function(){
			angular.forEach(self.choiceTypes,function (choiceType) {
				choiceType.choiceTypeCode = choiceType.choiceTypeCode.trim();
				choiceType.abbreviation = choiceType.abbreviation.trim();
				choiceType.description = choiceType.description.trim();
				choiceType.isEditing = false;
			});
			return self.choiceTypes;
		};

		/**
		 * Loads the 1st pages.
		 */
		self.updateChoiceTypeList = function(){
			if(null!=self.choiceTypeTableParams){
				self.choiceTypeTableParams.page(1);
				self.choiceTypeTableParams.reload();
			}
		};

		/**
		 * Saves the choice type data, updates switches to add checks to choice type, sets the ng table up.
		 * @param results
		 */
		self.loadChoiceTypeDataSelected = function(){
			self.choiceTypeTableParamsSelected = new ngTableParams(
				{
					page: 1,
					count:20,
					filter: $scope.filter
				}, {
					counts: [],
					getData: function ($defer, params) {
						var data = self.choiceTypesHandle;
						params.total(data.length);
						$defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
						self.dataResolvingParams = params;
					}
				}
			);
			self.updateChoiceTypeList();
		};

		/**
		 * Refresh flag check all in header grid. This flag show to user know, all row in current page has been
		 * selected.
		 */
		self.refreshCheckAllFlag = function () {
			self.checkAllFlag = true;
			for (var i = 0; i < self.choiceTypes.length; i++) {
				if (self.choiceTypes[i] && self.choiceTypes[i].selected == false) {
					self.checkAllFlag = false;
					break;
				}
			}
		};

		/**
		 * User checked/unchecked in checkbox on header data tables.
		 */
		self.checkAllHandle = function () {
			angular.forEach(self.choiceTypes, function (value) {
				if(self.choiceTypeFiltered.indexOf(value) >= 0) {
					value.selected = self.checkAllFlag;
				}
			});
		};

		/**
		 * Check exist element has been selected.
		 * @returns {boolean}
		 */
		self.hasDataSelected = function () {
			for (var i = 0; i < self.choiceTypes.length; i++) {
				if (self.choiceTypes[i] && self.choiceTypes[i].selected == true) {
					return true;
				}
			}
			return false;
		};

		/**
		 * Add new choice type handle when add new button click.
		 */
		self.addNewChoiceType = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.resetValidation();
			self.choiceTypesHandle = [];
			var choiceType = self.createChoiceTypeEmpty();
			self.choiceTypesHandle.push(choiceType);
			self.choiceTypesHandleOrig = angular.copy(self.choiceTypesHandle);
			self.titleModel = "Add New Choice Type";
			self.action = "ADD";
			$('#choiceTypeModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Create a empty choice type object.
		 * @returns {{}}
		 */
		self.createChoiceTypeEmpty = function () {
			var choiceType = {};
			choiceType["abbreviation"] = '';
			choiceType["description"] = '';
			choiceType["parentChoiceType"] = null;
			return choiceType;
		};

		/**
		 * Add one more choice type
		 */
		self.addMoreRowChoiceType = function () {
			if(self.validationChoiceTypeBeforeUpdate()) {
				var choiceType = self.createChoiceTypeEmpty();
				self.choiceTypesHandle.push(choiceType);
			}
		};

		/**
		 * Do add new the list of choice type. Call to back end to insert to database.
		 */
		self.updateChoiceType = function () {
			if(self.validationChoiceTypeBeforeUpdate()){
				if(self.action == "ADD"){
					$('#choiceTypeModal').modal("hide");
					self.isWaitingForResponse = true;
					choiceTypeApi.addNewChoiceTypes(
						self.choiceTypesHandle,
						function (results) {
							self.isWaitingForResponse = false;
							self.checkAllFlag = false;
							angular.forEach(results.data, function (value) {
								value["selected"] = false;
							});
							self.loadChoiceTypeData(results.data);
							self.success = results.message;
						},
						//error
						self.fetchError
					);
				}
			}
		};
		/**
		 * get change from table edit
		 * @returns {Array}
		 */
		self.getDataChange = function () {
			var lstChange = [];
			angular.forEach(self.choiceTypesHandle, function (value, key) {
				if($.trim(self.choiceTypesHandleOrig[key]['abbreviation']) !== $.trim(value['abbreviation']) ||
					$.trim(self.choiceTypesHandleOrig[key]['description']) !== $.trim(value['description'])||
					$.trim(self.choiceTypesHandleOrig[key]['parentChoiceType']) !== $.trim(value['parentChoiceType'])){
					delete value['addClass'];
					lstChange.push(value);
				}
			});
			return lstChange;
		};

		/**
		 * Check all field is valid before add new or update choice type.
		 * @returns {boolean}
		 */
		self.validationChoiceTypeBeforeUpdate = function () {
			var emptyChoiceAbb = false;
			var emptyChoiceDesc = false;
			for (var i = 0; i < self.choiceTypesHandle.length; i++) {
				if (self.choiceTypesHandle[i].abbreviation == null || self.choiceTypesHandle[i].abbreviation == '') {
					self.choiceTypesHandle[i].addClass = 'ng-invalid ng-touched';
					emptyChoiceAbb = true;
				}
				if(self.choiceTypesHandle[i].description == null || self.choiceTypesHandle[i].description == '') {
					self.choiceTypesHandle[i].addClass = 'ng-invalid ng-touched';
					emptyChoiceDesc = true;
				}
				if(emptyChoiceAbb && emptyChoiceDesc){
					break;
				}
			}
			if(emptyChoiceAbb && emptyChoiceDesc){
				self.errorPopup = "Choice Type: <li>Choice Type Abbreviation is mandatory field.</li><li>Choice Type Description is mandatory field.</li>";
				return false;
			}else if(emptyChoiceAbb){
				self.errorPopup = "Choice Type: <li>Choice Type Abbreviation is mandatory field.</li>";
				return false;
			}else if(emptyChoiceDesc){
				self.errorPopup = "Choice Type: <li>Choice Type Description is mandatory field.</li>";
				return false;
			}else{
				return true;
			}
		}

		/**
		 * Check change data in pop up handle edit/add new
		 * @returns {boolean}
		 */
		self.isDataChanged = function () {
			if(self.choiceTypesHandle.length != self.choiceTypesHandleOrig.length){
				return true;
			}
			var isDataChange = false;
			for(var i = 0; i < self.choiceTypesHandle.length; i++){
				if($.trim(self.choiceTypesHandle[i]['abbreviation']) !== $.trim(self.choiceTypesHandleOrig[i]['abbreviation']) ||
					$.trim(self.choiceTypesHandle[i]['description']) !== $.trim(self.choiceTypesHandleOrig[i]['description'])||
					$.trim(self.choiceTypesHandle[i]['parentChoiceType']) !== $.trim(self.choiceTypesHandleOrig[i]['parentChoiceType'])){
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
			self.choiceTypesHandle = angular.copy(self.choiceTypesHandleOrig);
			self.dataResolvingParams.page(1);
			self.dataResolvingParams.reload();
		};

		/**
		 * Show popup message confirm when close pop during data has been changed.
		 */
		self.closePopupHandle = function () {
			if(self.isDataChanged()){
				self.confirmTitle = "Confirmation";
				self.confirmMessage = "Unsaved data will be lost. Do you want to save the changes before continuing?";
				self.confirmAction = "SAVE";
				self.allowYesButton=true;
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			}else{
				$('#choiceTypeModal').modal("hide");
			}
		}

		/**
		 * Confirmed to delete/update change data
		 */
		self.confirmActionHandle = function () {
			$('#confirmModal').modal("hide");
			if(self.confirmAction == "DELETE"){
				self.doDeleteChoiceType();
			}else if(self.confirmAction == "SAVE"){
				self.updateChoiceType();
			}
		}

		/**
		 * No confirm to delete/update change data
		 */
		self.noConfirmActionHandle = function () {
			$('#confirmModal').modal("hide");
			$('#choiceTypeModal').modal("hide");
		}

		/**
		 * Clear all filter handle.
		 */
		self.clearFilter = function () {
			self.loadChoiceTypeData(self.choiceTypes);
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
				$rootScope.isEditedOnPreviousTab = true;
			}
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
				return "An unknown error occurred.";
			}
		}

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
		 * Update: edit inline and delete one row
		 */
		self.choiceTypeToDelete = null;
		self.hasOtherRowEditing = false;
		self.selectedRowId = null;
		self.selectedRowIndex = -1;
		self.selectedRow = null;
		self.cannotDeleteChoiceType = false;
		self.MESSAGE_CONFIRM_DELETE = "Are you sure you want to delete the selected Choice Type ?";
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		self.MESSAGE_NO_DATA_CHANGE = 'There are no changes on this page to be saved. Please make any changes to update.';
		self.VALIDATE_CHOICE_TYPE = 'validateChoiceType';
		self.RETURN_TAB = 'returnTab';
		self.isReturnToTab = false;

		/**
		 * show popup to confirmation delete
		 * @param choiceType
		 */
		self.showPopupConfirmDelete = function(choiceType){
			self.error = '';
			self.success = '';
			self.choiceTypeToDelete = angular.copy(choiceType);
			var listChildrenChoiceType = self.validationChoiceTypeBeforeDelete(choiceType.choiceTypeCode);

			if (listChildrenChoiceType !== '') {
				self.confirmMessage = "The Choice Type " + choiceType.choiceTypeCode +
					" is currently Parent Choice Type of <b>[" + listChildrenChoiceType +
					"]</b>.<br> Choice Type cannot be removed.";
				self.cannotDeleteChoiceType = true;
			} else {
				self.cannotDeleteChoiceType = false;
				self.confirmMessage = "Are you sure you want to delete the selected Choice Type ?";
			}
			$('#confirmationDelete').modal({backdrop: 'static', keyboard: true});

			$('.modal-backdrop').attr('style', ' z-index: 100000; ');
		};

		/**
		 * Validation choice type before delete.
		 * @returns {string}
		 */
		self.validationChoiceTypeBeforeDelete = function (choiceTypeCode) {
			if (choiceTypeCode !== null){
				return self.findAllChildrenChoiceType(choiceTypeCode);
			}
			return '';
		};

		/**
		 * Find list of children choice type by an choice type.
		 * @param choiceTypeCode
		 * @returns {string}
		 */
		self.findAllChildrenChoiceType = function (choiceTypeCode) {
			var listChildrenChoiceType = '';
			for (var i = 0; i < self.choiceTypes.length; i++) {
				if (self.choiceTypes[i] && self.choiceTypes[i].parentChoiceType
					&& self.choiceTypes[i].parentChoiceType.choiceTypeCode === choiceTypeCode
					&& self.choiceTypes[i].choiceTypeCode !== choiceTypeCode) {
					listChildrenChoiceType += self.choiceTypes[i].choiceTypeCode + ", ";

				}
			}
			if (listChildrenChoiceType != '') {
				listChildrenChoiceType = listChildrenChoiceType.substring(0, listChildrenChoiceType.length - 2);
			}
			return listChildrenChoiceType;
		};

		/**
		 * hide popup and call api to delete data.
		 */
		self.doDeleteData = function(choiceType){
			$('#confirmationDelete').modal("hide");
			self.isWaitingForResponse = true;
			choiceTypeApi.deleteChoiceTypes(
				self.createChoiceTypeList(choiceType),
				function (results) {
					self.isWaitingForResponse = false;
					self.choiceTypeToDelete = null;
					self.success = results.message;
					self.loadChoiceTypeData(results.data);
				},
				function (error) {
					self.fetchError(error);
				}
			);
		};

		/**
		 * create list of choice type for delete.
		 * @param choiceType
		 * @returns {Array}
		 */
		self.createChoiceTypeList = function(choiceType){
			var choiceTypes = [];
			choiceTypes.push(choiceType);
			return choiceTypes;
		};

		/**
		 * Select record to edit and enable it.
		 *
		 * @param productCategory
		 */
		self.enableRow = function(choiceType){
			self.error = '';
			self.success = '';
			choiceType.isEditing = true;
			self.hasOtherRowEditing = true;
			self.selectedRowId = choiceType.choiceTypeCode;
			self.selectedRowIndex = self.choiceTypes.indexOf(choiceType);
			self.selectedRow = angular.copy(choiceType);
			self.choiceTypeTableParams.reload();
		};

		/**
		 * get style of button
		 * @param choiceTypeCode
		 * @returns {*}
		 */
		self.getButtonStyle = function(choiceTypeCode){
			if (self.selectedRowId !== null && choiceTypeCode !== self.selectedRowId){
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
			self.choiceTypes[self.selectedRowIndex] = angular.copy(self.selectedRow);
			self.choiceTypes[self.selectedRowIndex].isEditing = false;
			self.resetAllFlag();
			self.choiceTypeTableParams.reload();
		};

		/**
		 * reset all flags
		 */
		self.resetAllFlag = function(){
			self.hasOtherRowEditing = false;
			self.choiceTypeToDelete = null;
			self.selectedRowId = null;
			self.selectedRowIndex = -1;
			self.selectedRow = null;
			self.cannotDeleteChoiceType = false;
		};

		/**
		 * call api to update choice type
		 * @param choiceType
		 */
		self.saveData = function(choiceType){
			$('#changeTabConfirmation').modal("hide");
			if (self.hasDataChanged(choiceType)){
				if (self.validationBeforeUpdate(choiceType)){
					self.isWaitingForResponse = true;
					choiceTypeApi.updateChoiceTypes(
						self.createDataForRequestApi(choiceType),
						function (results) {
							self.isWaitingForResponse = false;
							self.loadChoiceTypeData(results.data);
							self.success = results.message;
							self.resetAllFlag();
							if(self.isReturnToTab){
								$rootScope.success = self.success;
								$rootScope.isEditedOnPreviousTab = true;
							}
							self.returnToTab();
						},
						function (error) {
							self.fetchError(error);
						}
					);
				}
			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * Validation data before save
		 * @param choiceType
		 * @returns {boolean}
		 */
		self.validationBeforeUpdate = function (choiceType) {
			self.error = '';
			if (self.isNullOrEmpty(choiceType.abbreviation)) {
				self.error += '<li>Choice Type Abbreviation is mandatory field.</li>';
			}
			if (self.isNullOrEmpty(choiceType.description)) {
				self.error += '<li>Choice Type Description is mandatory field.</li>';
			}
			if (self.error !== ''){
				self.error = 'Choice Type:' + self.error;
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
		 * Create list data for update
		 * @param choiceType
		 * @returns {Array}
		 */
		self.createDataForRequestApi = function(choiceType){
			var choiceTypes = [];
			var choiceTypeForRequest = {
				choiceTypeCode: choiceType.choiceTypeCode,
				abbreviation: choiceType.abbreviation,
				description: choiceType.description,
				parentChoiceType: {
					choiceTypeCode: choiceType.parentChoiceType.choiceTypeCode
				},
			};
			choiceTypes.push(choiceTypeForRequest);
			return choiceTypes;
		};

		/**
		 * Check if has changed data
		 * @param choiceType
		 * @returns {boolean}
		 */
		self.hasDataChanged = function(choiceType){
			if (choiceType !== null && choiceType !== undefined) {
				var choiceTypeTemp = angular.copy(choiceType);
				var choiceTypeSelectedTemp = angular.copy(self.selectedRow);
				delete choiceTypeTemp.isEditing;
				delete choiceTypeSelectedTemp.isEditing;
				return !(JSON.stringify(choiceTypeTemp) === JSON.stringify(choiceTypeSelectedTemp))
			}
			return false;
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_CHOICE_TYPE, function () {
			if (self.selectedRow !== null && self.hasDataChanged(self.findChoiceTypeById(self.selectedRowId))) {
				self.isReturnToTab = true;
				self.confirmTitle = 'Confirmation';
				self.confirmMessage = self.MESSAGE_CONFIRM_CLOSE;
				self.error = '';
				self.success = '';
				$('#changeTabConfirmation').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});

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
			self.choiceTypeToDelete = null;
			$('#confirmationDelete').modal("hide");
			$('#changeTabConfirmation').modal("hide");

			if (self.isReturnToTab) {
				$('#changeTabConfirmation').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			}
		};

		/**
		 * find Choice Type by Choice Type Code
		 * @param choiceTypeId
		 * @returns {*}
		 */
		self.findChoiceTypeById = function(choiceTypeId){
			for(var i = 0; i < self.choiceTypes.length; i++) {
				if (self.choiceTypes[i].choiceTypeCode === choiceTypeId){
					return self.choiceTypes[i];
				}
			}
		};

		/**
		 * Hides save confirm dialog.
		 */
		self.cancelConfirmDialog = function () {
			$('.modal-backdrop').attr('style', '');
			$('#changeTabConfirmation').modal('hide');
		};
	}
})();
