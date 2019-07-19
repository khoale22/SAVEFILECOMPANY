/*
 *
 * WineMakerController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.12.0
 */
'use strict';

/**
 * The controller for the Wine Maker Controller.
 * @author vn87351
 * @since 2.12.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('wineMakerComponent', {
		templateUrl: 'src/codeTable/wine/wineMaker.html',
		bindings: {seleted: '<'},
		controller: WineMakerController
	});
	app.filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
	});
	app.directive('trimSpace', function () {
		return {
			link: function link(scope, element) {
				element.bind('change', function () {
					$(element).val($.trim($(element).val()));
				});
			}
		}
	});

	WineMakerController.$inject = ['$rootScope', 'codeTableApi', '$scope', 'ngTableParams', '$filter'];

	/**
	 * Constructs the controller.
	 */
	function WineMakerController($rootScope, codeTableApi, $scope, ngTableParams, $filter) {

		var self = this;
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * check all flag
		 * @type {boolean}
		 */
		self.checkAllValue = false;

		/**
		 * Return tab key.
		 * @type {string}
		 */
		self.RETURN_TAB = 'returnTab';

		/**
		 * message susscess from api
		 */
		self.success = '';
		/**
		 * message error form api
		 */
		self.error = '';
		/**
		 * list data wine Area
		 * @type {Array}
		 */
		self.wineMaker = [];
		/**
		 * The flag allow delete choice type.
		 * @type {boolean}
		 */
		self.allowDelete = true;

		/**
		 * The message show in popup confirm.
		 * @type {string}
		 */
		self.DELETE_WINE_MAKER_CONFIRM_MESSAGE = "Are you sure you want to delete the selected Wine Maker ?";

		self.CONFIRM_TITLE = "Confirmation";

		self.REQUIRED_WINE_MAKER_NAME_ERROR = "Wine Maker Name is a mandatory field.";

		self.REQUIRED_WINE_MAKER_DESCRIPTION_ERROR = "Wine Maker Description is a mandatory field.";

		self.UNSAVED_DATA_CONFIRM_MESSAGE = "Unsaved data will be lost. Do you want to save the changes before continuing ?";

		self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';

		self.VALIDATE_WINE_MAKER = 'validateWineMaker';
		/**
		 * The label for button close in confirm pop up.
		 * @type {string}
		 */
		self.labelClose = 'No';

		self.isEditing = false;

		self.selectedRow = null;

		self.wineMakerPre = null;

		self.wineMakerPreOrigin = null;

		self.wineMakerAddData = [];

		self.wineMakerHandle = [];

		self.dataPopupError = '';

		self.isReturnToTab = false;

		/**
		 * Initiates the construction of the Wine Varietal Controller.
		 */
		self.init = function () {
			self.isWaiting = true;
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
			self.findAllWineMaker();
		};

		/**
		 *If there is an error this will display the error
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message != null && error.data.message != "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
			if(self.isReturnToTab) 	$rootScope.error = self.error;
		};
		/**
		 * init data for table on popup
		 */
		self.dataPopupInit = function () {
			$scope.tableParamsPopup = new ngTableParams({
				page: 1,
				count: 15
			}, {
				counts: [],
				debugMode: true,
				data: self.wineMakerAddData
			});
		}
		/**
		 * get wine Maker
		 */
		self.findAllWineMaker = function () {
			codeTableApi.getAllWineMaker().$promise.then(function (res) {
				self.callApiResponseSuccess(res);
				self.isDelete = false;
				self.wineMakerPreOrigin = null;
				self.isWaiting = false;
			});
		}

		/**
		 * Handle after call ws.
		 *
		 * @param data
		 */
		self.callApiResponseSuccess = function (data) {
			self.wineMakerHandle = angular.copy(data);
			angular.forEach(self.wineMakerHandle, function (value) {
				value.isEditing = false;
				value['wineMakerName'] = value['wineMakerName'].trim();
				value['wineMakerDescription'] = value['wineMakerDescription'].trim();
			});
			self.filterDatas();
			self.isWaiting = false;
		}
		/**
		 * filter data maker
		 */
		self.filterDatas = function () {
			$scope.filter = {
				wineMakerId: undefined,
				wineMakerName: undefined,
				wineMakerDescription: undefined
			};
			$scope.tableParams = new ngTableParams({
				page: 1,
				count: 20,
				filter: $scope.filter
			}, {
				counts: [],
				debugMode: true,
				data: self.wineMakerHandle
			});
		}

		/**
		 * the sort function. parse id to number before sort
		 * @param obj
		 * @returns {Number}
		 */
		$scope.sorterFunc = function (obj) {
			return parseInt(obj.wineMakerId);
		};
		/**
		 * add new row and open popup
		 */
		self.addNew = function () {
			var type = self.createWineMakerEmpty();
			self.dataPopupTitle = "Add Wine Makers";
			self.wineMakerAddData.push(type);
			self.dataPopupInit();
			$('#wineMakerModal').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * add new row for wine maker
		 */
		self.addMoreRowWineMaker = function () {
			self.dataPopupError = '';
			if (self.validation(self.wineMakerAddData)) {
				var type = self.createWineMakerEmpty();
				self.wineMakerAddData.push(type);
			}
			self.dataPopupInit();
		}

		/**
		 * Edit action handle
		 */
		self.editItem = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.dataPopup.error = '';
			self.dataPopup.data = [];
			angular.forEach(self.wineMaker, function (value) {
				if (value.selected == true) {
					value.addClass = 'active-tooltip';
					self.dataPopup.data.push(angular.copy(value));
				}
			});
			self.dataPopup.dataOrigin = angular.copy(self.dataPopup.data);
			self.dataPopup.title = "Edit Wine Maker";
			self.dataPopupInit()
			self.dataPopup.mode = "edit";
			$('#wineMakerModal').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * User checked/unchecked in checkbox on header data tables.
		 */
		self.checkAllHandle = function () {
			var lst = $filter('filter')(self.wineMaker, $scope.tableParams.filter());
			angular.forEach(lst, function (value) {
				value.selected = self.checkAllFlag;
			});
		};
		/**
		 * Create a empty object.
		 * @returns {{}}
		 */
		self.createWineMakerEmpty = function () {
			var wineMaker = {};
			wineMaker["wineMakerId"] = '';
			wineMaker["wineMakerName"] = '';
			wineMaker["wineMakerDescription"] = '';
			return wineMaker;
		}
		/**
		 * Check all field is valid before add new or update.
		 * @returns {boolean}
		 */
		self.validation = function (wineMakerList) {
			var errorMessages = [];
			var errorMessage = '';
			for (var i = 0; i < wineMakerList.length; i++) {
				if (self.isNullOrEmpty(wineMakerList[i].wineMakerName)) {
					errorMessage = "<li>" + self.REQUIRED_WINE_MAKER_NAME_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) == -1) {
						errorMessages.push(errorMessage);
					}
					wineMakerList[i].addClass = 'active-tooltip ng-invalid ng-touched';
				}
				if (self.isNullOrEmpty(wineMakerList[i].wineMakerDescription)) {
					errorMessage = "<li>" + self.REQUIRED_WINE_MAKER_DESCRIPTION_ERROR + "</li>";
					if (errorMessages.indexOf(errorMessage) == -1) {
						errorMessages.push(errorMessage);
					}
					wineMakerList[i].addClass = 'active-tooltip ng-invalid ng-touched';
				}
				if (errorMessages.length === 2) {
					break;
				}
			}
			if (errorMessages.length > 0) {
				var errorMessagesAsString = 'Wine Maker:';
				angular.forEach(errorMessages, function (errorMessage) {
					errorMessagesAsString += errorMessage;
				});
				self.dataPopupError = errorMessagesAsString;
				return false;
			}
			return true;

		};
		/**
		 * save info. Call to back end to insert to database.
		 */
		self.saveData = function (attribute) {
			self.clearMessages();
			self.closeConfirmPopup();
			if (self.isDataChanged(attribute)) {
				if (self.validation(self.wineMakerHandle)) {
					self.isWaiting = true;
					codeTableApi.updateWineMaker(attribute, function (results) {
							self.callApiResponseSuccess(results.data);
							self.success = results.message;
							self.isAddingActionCode = false;
							if (self.isReturnToTab) {
								$rootScope.success = self.success;
								$rootScope.isEditedOnPreviousTab = true;
							}
							self.returnToTab(true);
							self.isEditing = false;
							self.selectedRow = null;
							self.wineMakerPreOrigin = null;
						}, self.fetchError
					);
				}
			}
			else {
				self.error = "";
				self.error = self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
			}
		}

		/**
		 * Handle to call ws to add new wine maker.
		 */
		self.doAddWineMaker = function () {
			if (self.validation(self.wineMakerAddData)) {
				$('#confirmModalSave').modal("hide");
				$('#wineMakerModal').modal("hide");
				self.isWaiting = true;
				codeTableApi.addNewWineMaker(self.wineMakerAddData, function (results) {
						self.callApiResponseSuccess(results.data);
						self.success = results.message;
						self.isAddingActionCode = false;
						self.isEditing = false;
						self.wineMakerPreOrigin = null;
						self.wineMakerAddData = [];
					}, self.fetchError
				);
			}
		};

		/**
		 * Do delete item, call to back end to delete.
		 */
		self.doDeleteItem = function () {
			$('#confirmModal').modal("hide");
			self.clearMessages();
			var deleteData = [];
			self.clearMessages();
			self.isWaiting = true;
			deleteData.push(self.wineMakerPreOrigin);
			codeTableApi.deleteWineMaker(
				deleteData,
				function (result) {
					self.callApiResponseSuccess(result.data);
					self.success = result.message;
					self.isDelete = false;
					self.wineMakerPreOrigin = null;
				},
				function (error) {
					self.fetchError(error);
				}
			);
		}
		/**
		 * Delete handle when click on delete button.
		 */
		self.deleteHandle = function (attribute) {
			self.isDelete = true;
			self.wineMakerPreOrigin = angular.copy(attribute);
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			self.messageConfirm = self.DELETE_WINE_MAKER_CONFIRM_MESSAGE;
			self.titleConfirm = self.CONFIRM_TITLE;
		}

		/**
		 * Check has data changed.
		 *
		 * @param wineMaker
		 * @returns {boolean}
		 */
		self.isDataChanged = function (wineMaker) {
			if ($.trim(wineMaker['wineMakerName']) !== $.trim(self.wineMakerPreOrigin['wineMakerName']) ||
				$.trim(wineMaker['wineMakerDescription']) !== $.trim(self.wineMakerPreOrigin['wineMakerDescription'])) {
				return true;
			}
			return false;
		}

		/**
		 * close the popup function
		 */
		self.closePopup = function () {
			if (self.isDataChanged()) {
				$('#wineMakerModal').css({"z-index": 0});
				$('#confirmModalSave').modal({backdrop: 'static', keyboard: true});
			} else {
				$('#wineMakerModal').modal("hide");
			}
		}
		/*
		 * @author: thanhtran
		 * */
		self.decodeHTML2 = function (text) {
			var divs = $('<textarea/>').html(text).text();
			return divs;
		}
		/**
		 * do close popup
		 */
		self.doClosePopup = function () {
			$('#confirmModalSave').modal("hide");
			$('#wineMakerModal').modal("hide");
			self.wineMakerAddData = [];
			self.removeCssPopup();
		}
		/**
		 * remove css popup zindex
		 */
		self.removeCssPopup = function () {
			$('#wineMakerModal').css({"z-index": ''});
		}

		/**
		 * Handle when click edit button.
		 *
		 * @param attribute
		 */
		self.editWineMaker = function (attribute) {
			self.clearMessages();
			self.selectedRow = true;
			self.isEditing = true;
			if (self.wineMakerPre !== null) {
				self.wineMakerPre.isEditing = false;
				attribute.isEditing = true;
				self.wineMakerPre = attribute;
				self.wineMakerPreOrigin = angular.copy(attribute);
			}
			else {
				if (self.wineMakerPre === null) {
					attribute.isEditing = true;
					self.wineMakerPre = attribute;
					self.wineMakerPreOrigin = angular.copy(attribute);
				}
			}
		};

		/**
		 * Reset data edited.
		 *
		 * @param attribute
		 */
		self.cancel = function (attribute) {
			self.clearMessages();
			self.selectedRow = null;
			attribute.isEditing = false;
			self.isEditing = false;
			attribute.wineMakerDescription = self.wineMakerPreOrigin.wineMakerDescription;
			attribute.wineMakerName = self.wineMakerPreOrigin.wineMakerName;
			self.wineMakerPreOrigin = null;
			$scope.tableParams.reload();
			console.log(self.wineMakerHandle.length);
		};

		/**
		 * Clear all the messages when click buttons.
		 */
		self.clearMessages = function () {
			self.error = '';
			self.success = '';
			self.dataPopupError = '';
			self.wineMakerAddData = [];
		};

		/**
		 * Handle when switch to another tab.
		 */
		$scope.$on(self.VALIDATE_WINE_MAKER, function () {
			if (self.selectedRow !== null) {
				if (self.wineMakerPre !== null) {
					if (self.isDataChanged(self.wineMakerPre)) {
						self.isReturnToTab = true;
						self.error = '';
						self.success = '';
						self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE;
						$('#confirmModalSave').modal({backdrop: 'static', keyboard: true});
					}
					else $rootScope.$broadcast(self.RETURN_TAB);
				}
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function (clickYes) {
			self.closeConfirmPopup();
			if (self.isReturnToTab && clickYes) {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
			else {
				$('#confirmModalSave').on('hidden.bs.modal', function () {
					$rootScope.$broadcast(self.RETURN_TAB);
					$scope.$apply();
				});
			}
		};

		/**
		 *Handle to save data when swith to another tab.
		 */
		self.wineMakerTabchange = function () {
			self.saveData(self.wineMakerPre);
		}

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
		 * Close confirm popup
		 */
		self.closeConfirmPopup = function () {
			$('#confirmModalSave').modal("hide");
			$('.modal-backdrop').attr('style', ' ');
		}

		/**
		 * Handle when click close on add popup to show confirm popup when has data add
		 * or hide add popup when have not data add.
		 */
		self.closeModalUnsavedData = function () {
			if (self.wineMakerAddData[0].wineMakerName.length !== 0 || self.wineMakerAddData[0].wineMakerDescription.length !== 0) {
				self.titleConfirm = self.CONFIRM_TITLE;
				self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE;
				$('#confirmModalSave').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			}
			else {
				$('#wineMakerModal').modal("hide");
				self.wineMakerAddData = [];
				self.clearMessages();
			}
		}
	}
})();
