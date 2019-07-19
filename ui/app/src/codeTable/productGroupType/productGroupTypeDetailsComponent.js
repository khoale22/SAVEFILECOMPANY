/*
 *
 * productGroupTypeDetailsController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
'use strict';

/**
 * The controller for the Product Group Type Details Controller.
 * @author vn70529
 * @since 2.12.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('productGroupTypeDetailsComponent', {
		templateUrl: 'src/codeTable/productGroupType/productGroupTypeDetails.html',
		bindings: { 'details': '=' },
		controller: productGroupTypeDetailsController
	});

	app.filter('propsFilter', function () {
		return function (items, props) {
			var out = [];

			if (angular.isArray(items)) {
				var keys = Object.keys(props);

				items.forEach(function (item) {
					var itemMatches = false;

					for (var i = 0; i < keys.length; i++) {
						var prop = keys[i];
						var text = props[prop].toLowerCase();
						if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
							itemMatches = true;
							break;
						}
					}

					if (itemMatches) {
						out.push(item);
					}
				});
			} else {
				// Let the output be the input untouched
				out = items;
			}

			return out;
		};
	});

	productGroupTypeDetailsController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'ProductGroupTypeDetailsApi',
		'$filter', '$state', 'appConstants', 'ProductGroupService', '$window', 'DownloadImageService', 'customHierarchyService'];

	/**
	 * Constructs the controller.
	 */
	function productGroupTypeDetailsController($rootScope, $scope, ngTableParams, productGroupTypeDetailsApi, $filter,
											   $state, appConstants, productGroupService, $window, downloadImageService, customHierarchyService) {

		var self = this;
		/**
		 * Constant.
		 */
		const UPDATE_ERROR = "Error"
		const UPDATE_ERROR_MESSAGE = "Changes to pickers cannot be made at this point as Choice has associated products."

		/**
		 * The unknown error message.
		 *
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * The default no records found message.
		 *
		 * @type {string}
		 */
		self.NO_RECORDS_FOUND = "No records found.";
		/**
		 * Start position of page that want to show on country code table
		 *
		 * @type {number}
		 */
		self.PAGE = 1;
		/**
		 * The number of records to show on the  country code table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 3;
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * The error message.
		 *
		 * @type {string}
		 */
		self.error = '';
		/**
		 * The image.
		 *
		 * @type {Object}
		 */
		self.images = {};
		/**
		 * The index product group choice type selected.
		 *
		 * @type {number}
		 */
		self.selectedChoiceTypeIndex = 0;
		/**
		 * The subDepartments.
		 *
		 * @type {object}
		 */
		self.subDepartments = {};
		/**
		 * The list of Departments.
		 *
		 * @type {Array}
		 */
		self.departments = [];
		/**
		 * The list of Choice Type.
		 *
		 * @type {Array}
		 */
		self.lstChoiceTypes = [];
		/**
		 * The list of Choice Type Term.
		 *
		 * @type {Array}
		 */
		self.lstChoiceTypesTerm = [];
		/**
		 * The Product Group Type.
		 *
		 * @type {Object}
		 */
		self.productGroupType = {};
		/**
		 * The Product Group Choice Type.
		 *
		 * @type {Object}
		 */
		self.productGroupChoiceType = {};
		/**
		 * The Product Group Type Term.
		 *
		 * @type {Object}
		 */
		self.productGroupTypeTerm = {};
		/**
		 * The flag next or previous ProductGroupType.
		 *
		 * @type {boolean}
		 */
		self.nextProductGroupTypeView = null;
		/**
		 * The flag check change data Product Group Type.
		 *
		 * @type {boolean}
		 */
		self.isChangeProductGroupTypeView = false;
		/**
		 * The flag wait load Choice Type.
		 *
		 * @type {boolean}
		 */
		self.isWaitingLoadChoiceType = false;
		/**
		 * The flag wait load SubDepartment.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForLoadSubDepartment = false;
		/**
		 *Check all flag.
         * @type {Boolean}
		 */
		self.checkAllFlag = false;
		/**
		 * The flag check all Choice Type.
		 *
		 * @type {boolean}
		 */
		self.checkAllProductGroupChoiceTypeFlag = false;
		/**
		 * Title of confirm modal.
		 *
		 * @type {String} the title of confirm modal.
		 */
		self.commonConfirmModalTitle = '';

		/**
		 * Content of confirm modal.
		 *
		 * @type {String} the content of confirm modal.
		 */
		self.commonConfirmModalContent = '';

		/**
		 * Type confirm modal.
		 *
		 * @type {String} the type confirm modal.
		 */
		self.typeModal = '';
		/**
		 * The flag return to list Product Group Type.
		 *
		 * @type {boolean}
		 */
		self.isReturnToList = false;
		/**
		 * The confirm popup title
		 *
		 * @type {string}
		 */
		self.confirmHeaderTitle = "Confirmation";

		/**
		 * The content message of confirmation return to list popup.
		 *
		 * @type {string}
		 */
		self.MESSAGE_CONFIRM_RETURN_TO_LIST = "Unsaved data will be lost. Do you want to save the changes before continuing?";

		/**
		 * The list Choice Types Change.
		 *
		 * @type {Array}
		 */
		self.lstChoiceTypesChange = [];

		/**
		 * The list Choice Option Change.
		 *
		 * @type {Array}
		 */
		self.lstChoiceOptChange = [];

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
		 * The Product Group Type Update.
		 *
		 * @type {object}
		 */
		self.productGroupTypeUpdate = {};

		self.VALIDATE_PRODUCT_GROUP_TYPE = 'validateProductGroupType';
		self.RETURN_TAB = 'returnTab';
		self.isReturnToTab = false;
		self.noSaveData = false;
		self.ACTION_ADD = 'A';
		self.ACTION_DELETE = 'D';
		self.ACTION_UPDATE = '';
		/**
		 * Initiates the construction of the product Group Type Details Controller.
		 */
		self.init = function () {
			self.success = null;
			self.error = null;
			if (self.details.productGroupType.productGroupTypeCode == '') {
				self.loadDataAddProductGroupType();
			} else {
				self.loadProductGroupTypeDetails();
			}
			self.setDisableStatusForPreviousNextButton();
			self.getDataProductGroupChoiceType();
			if ($rootScope.isEditedOnPreviousTab) {
				self.error = $rootScope.error;
				self.success = $rootScope.success;
				$rootScope.isEditedOnPreviousTab = false;
			}
			$('#common-confirm-modal').on('hidden.bs.modal', function () {
				if (self.typeModal == 'changeProductGroupTypeShow' && self.isChangeProductGroupTypeView && self.noSaveData) {
					self.changeProductGroupTypeView();
					self.isChangeProductGroupTypeView = false;
					self.noSaveData = false;
					$scope.$apply();
				}
			});
		};

		/**
		 * Load data for add new Product Group Type.
		 */
		self.loadDataAddProductGroupType = function () {
			self.clearData();
			self.success = '';
			self.error = '';
			self.isWaitingForResponse = true;
			productGroupTypeDetailsApi.findAllDepartments(function (response) {
				self.departments = response;
				self.isWaitingForResponse = false;
			}, function (error) {
				self.fetchError(error);
			});
			self.productGroupType = {
				customerProductGroups: [],
				departmentNumberString: "",
				productGroupChoiceTypes: [],
				productGroupType: "",
				productGroupTypeCode: "",
				productGroupTypeSummary: "",
				subDepartmentId: ""
			}
			self.departmentsOrig = angular.copy(self.departments);
			self.subDepartmentsOrig = angular.copy(self.subDepartments);
			self.productGroupTypeTerm = angular.copy(self.productGroupType);
		}

		/**
		 * Load data for Product Group Type Details.
		 */
		self.loadProductGroupTypeDetails = function (messSaveData) {
			self.clearData();
			self.success = messSaveData;
			self.error = '';
			self.isWaitingForResponse = true;
			productGroupTypeDetailsApi.findProductGroupTypeDetailsById({ productGroupTypeCode: self.details.productGroupType.productGroupTypeCode },
				function (response) {
					self.productGroupType = response.productGroupType;
					self.productGroupType.productGroupType = self.productGroupType.productGroupType.trim();
					if (self.productGroupType.productGroupChoiceTypes != null &&
						self.productGroupType.productGroupChoiceTypes.length > 0) {
						self.productGroupType.productGroupChoiceTypes = $filter('orderBy')(self.productGroupType.productGroupChoiceTypes, 'choiceType.description', false)
						self.productGroupChoiceType = self.productGroupType.productGroupChoiceTypes[0];
						self.selectChoiceType(self.productGroupChoiceType, 0);
					}
					self.productGroupTypeTerm = angular.copy(self.productGroupType);
					self.departments = response.departments;
					self.departmentsOrig = angular.copy(self.departments);
					if(response.subDepartments != null && response.subDepartments != undefined){
						_.remove(response.subDepartments, function(o) {
							return o.key.subDepartment == "";
						});
					}
					self.subDepartments[self.productGroupType.departmentNumberString.trim()] = response.subDepartments;
					self.subDepartmentsOrig = angular.copy(self.subDepartments);
					self.thumbnailUris = response.thumbnailUris;
					self.productGroupTypeName = self.productGroupType.productGroupType;
					self.customerProductGroups = angular.copy(self.productGroupType.customerProductGroups);
					self.department = self.getSelectedDepartmentByProductGroupType();
					self.subDepartment = self.getSelectedSubDepartmentByProductGroupType(response.subDepartments);
					self.initCustProductGroupTableParams();
					self.isWaitingForResponse = false;
				}, function (error) {
					self.fetchError(error);
				});
		}

		/**
		 * Initial cust product group table for details mode.
		 */
		self.initCustProductGroupTableParams = function () {
			$scope.custProductGroupTableParams = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.PAGE_SIZE, /* count per page */
				sorting: { custProductGroupId: "asc" }
			}, {
					counts: [],
					debugMode: false,
					data: self.customerProductGroups
				});
		}
		self.getDataProductGroupChoiceType = function () {
			self.isWaitingLoadChoiceType = true;
			productGroupTypeDetailsApi.findAllChoiceTypes(function (response) {
				self.lstChoiceTypes = response;
				self.lstChoiceTypesTerm = angular.copy(self.lstChoiceTypes);
				self.initChoiceTypeTable();
				self.isWaitingLoadChoiceType = false;
			}, function (error) {
				self.fetchError(error);
				self.isWaitingLoadChoiceType = false;
			});
		}
		/**
		 * Load sub department when the department is selected.
		 *
		 * @param department department object.
		 */
		self.selectDepartment = function (department) {
			if(department == null || department == undefined){
				self.subDepartment = null;
			}else{
			if (self.subDepartments[department.key.department] == null || self.subDepartments[department.key.department] == undefined || self.subDepartments[department.key.department].length ==0) {
				self.isWaitingForLoadSubDepartment = true;
				productGroupTypeDetailsApi.findSubDepartmentsByDepartment({ departmentCode: department.key.department },
					function (response) {
							if(response != null && response != undefined){
								_.remove(response, function(o) {
									return o.key.subDepartment == "";
								});
							}
						self.subDepartments[department.key.department] = response;
						self.subDepartmentsOrig = angular.copy(self.subDepartments);
						if (response.length > 0) {
							self.subDepartment = response[0];
						}
						self.isWaitingForLoadSubDepartment = false;
					}, function (error) {
						self.isWaitingForLoadSubDepartment = false;
						self.fetchError(error);
					});
			} else {
				self.subDepartment = self.subDepartments[department.key.department][0];
			}
		}
			
		}

		/**
		 * Selects choice type.
		 */
		self.selectChoiceType = function (productGroupChoiceType, selectedRowIndex) {
			self.selectedChoiceTypeIndex = selectedRowIndex;
			self.productGroupChoiceType = productGroupChoiceType;
			productGroupTypeDetailsApi.findChoiceOptionsByChoiceTypeCode({ choiceTypeCode: productGroupChoiceType.choiceType.choiceTypeCode },
				function (response) {
					self.choiceOptions = response;
					if (response.length > 0) {
						angular.forEach(self.choiceOptions, function (choiceOption) {
							self.setStatusForChoiceOption(choiceOption);
						});
					}
				}, function (error) {
					self.choiceOptions = [];
					self.fetchError(error);
				});
		}

		/**
		 * Sets the status for choice option.
		 *
		 * @param choiceOption the choice option.
		 */
		self.setStatusForChoiceOption = function (choiceOption) {
			if (self.productGroupChoiceType.productGroupChoiceOptions !== undefined && self.productGroupChoiceType.productGroupChoiceOptions.length > 0) {
				for (var i = 0; i < self.productGroupChoiceType.productGroupChoiceOptions.length; i++) {
					if (self.productGroupChoiceType.productGroupChoiceOptions[i].key.choiceOptionCode === choiceOption.key.choiceOptionCode) {
						choiceOption.isSelected = true;
						break;
					}
				}
			} else {
				choiceOption.isSelected = false;
			}
		}

		/**
		 * Clear data.
		 */
		self.clearData = function () {
			self.choiceOptions = [];
			self.productGroupType = {};
			self.departments = [];
			self.departmentsOrig = [];
			self.subDepartments = [];
			self.subDepartmentsOrig = [];
			self.thumbnailUris = [];
			self.productGroupTypeName = [];
			self.customerProductGroups = [];
			self.productGroupChoiceType = {};
			self.department = {};
			self.subDepartment = {};
			self.isChangeProductGroupTypeView = false;
		}
		/**
		 * Get the department by produc group type.
		 */
		self.getSelectedDepartmentByProductGroupType = function () {
			for (var i = 0; i < self.departments.length; i++) {
				if (self.productGroupType.departmentNumberString.trim() === self.departments[i].key.department) {
					return self.departments[i];
				}
			}
		}
		/**
		 * Get the sub department by produc group type.
		 *
		 * @param subDepartments the list of sub departments.
		 * @returns {*}
		 */
		self.getSelectedSubDepartmentByProductGroupType = function (subDepartments) {
			if (subDepartments != null && subDepartments.length > 0) {
				for (var i = 0; i < subDepartments.length; i++) {
					if (self.productGroupType.subDepartmentId.trim() === subDepartments[i].key.subDepartment) {
						return subDepartments[i];
					}
				}
			}
			return null;
		}
		/**
		 * Listener the data change on cust product group table to load image.
		 */
		$scope.$watchCollection('custProductGroupTableParams.data', function (newValue, oldValue) {
			if (!newValue) {
				return;
			}
			angular.forEach(newValue, function (custProductGroup) {
				self.loadThumbnailImageByCustProductGroupId(custProductGroup.custProductGroupId);
			});
		});
		/**
		 * Load thumbnail image by custProductGroupId.
		 *
		 * @param custProductGroupId the id of custProductGroup.
		 */
		self.loadThumbnailImageByCustProductGroupId = function (custProductGroupId) {
			if (self.images[custProductGroupId.toString()] == undefined || self.images[custProductGroupId.toString()] == null) {
				self.images[custProductGroupId.toString()] = { isLoading: true, image: null };
			}
			if (self.images[custProductGroupId.toString()].image == null) {
				self.images[custProductGroupId.toString()].isLoading = true;
				self.images[custProductGroupId.toString()].image = "images/no_image.png";
				var imageUrl = self.thumbnailUris[custProductGroupId.toString()];
				if (imageUrl != null && imageUrl.length > 0) {
					productGroupTypeDetailsApi.findThumbnailImageByUri({ imageUri: imageUrl },
						function (response) {
							if (response.data != null && response.data.length > 0) {
								self.images[custProductGroupId.toString()].image = "data:image/jpg;base64," + response.data;
							}
							self.images[custProductGroupId.toString()].isLoading = false;
						}, function (error) {
							self.images[custProductGroupId.toString()].isLoading = false;
						});
				} else {
					self.images[custProductGroupId.toString()].isLoading = false;
				}
			}
		}
		/**
		 * Show next previous Product Group Type.
		 */
		self.nextProductGroupType = function () {
			self.nextProductGroupTypeView = true;
			if(self.validateProductGroupType()){
			if (self.hasChangeDataProductGroupType()) {
				self.showCommonConfirmModal("Confirmation", "Unsaved data will be lost. Do you want to save the changes before continuing?", "changeProductGroupTypeShow");
			} else {
				self.error = null;
				self.success = null;
				self.changeProductGroupTypeView();
			}
		}
		}
		/**
		 * Show previous Product Group Type.
		 */
		self.previousProductGroupType = function () {
			self.nextProductGroupTypeView = false;
			if(self.validateProductGroupType()){
			if (self.hasChangeDataProductGroupType()) {
				self.showCommonConfirmModal("Confirmation", "Unsaved data will be lost. Do you want to save the changes before continuing?", "changeProductGroupTypeShow");
			} else {
				self.error = null;
				self.success = null;
				self.changeProductGroupTypeView();
			}
		}
		}
		/**
		 * Change Product Group Type view.
		 */
		self.changeProductGroupTypeView = function (messSaveData) {
			var index = self.details.productGroupTypes.indexOf(self.details.productGroupType);
			if (self.nextProductGroupTypeView) {
				if (index < self.details.productGroupTypes.length - 1) {
					self.details.productGroupType = self.details.productGroupTypes[index + 1];
					self.loadProductGroupTypeDetails(messSaveData);
				}
			} else {
				if (index > 0) {
					self.details.productGroupType = self.details.productGroupTypes[index - 1];
					self.loadProductGroupTypeDetails(messSaveData);
				}
			}
			self.setDisableStatusForPreviousNextButton();
		}
		/**
		 * Set disable status for previous and next button.
		 */
		self.setDisableStatusForPreviousNextButton = function () {
			if (self.details.productGroupTypes.length <= 1) {
				self.isFirstPage = true;
				self.isLastPage = true;
			} else {
				var index = self.details.productGroupTypes.indexOf(self.details.productGroupType);
				if (index === 0) {
					self.isFirstPage = true;
					self.isLastPage = false;
				} else if (index == self.details.productGroupTypes.length - 1) {
					self.isFirstPage = false;
					self.isLastPage = true;
				} else {
					self.isFirstPage = false;
					self.isLastPage = false;
				}
			}
		}
		/**
		 * Filter department by search key.
		 *
		 * @param searchKey the key to search.
		 */
		self.filterDepartment = function (searchKey) {
			var results = [];
			if (self.departmentsOrig != null && self.departmentsOrig.length > 0) {
				for (var i = 0; i < self.departmentsOrig.length; i++) {
					if (self.departmentsOrig[i].displayName.toLowerCase().search(searchKey) > -1) {
						results.push(self.departmentsOrig[i]);
					}
				}
			}
			self.departments = results;
		}
		/**
		 * Filter department by search key.
		 *
		 * @param searchKey the key to search.
		 */
		self.filterSubDepartment = function (searchKey) {
			if (self.department != null && self.department.key != null) {
				var subDepartmentsTemp = self.subDepartmentsOrig[self.department.key.department];
				var results = [];
				if (subDepartmentsTemp != null && subDepartmentsTemp.length > 0) {
					for (var i = 0; i < subDepartmentsTemp.length; i++) {
						if (subDepartmentsTemp[i].displayName.toLowerCase().search(searchKey) > -1) {
							results.push(subDepartmentsTemp[i]);
						}
					}
				}
				self.subDepartments[self.department.key.department] = results;
			}
		}
		/**
		 * Init data Choice Type table.
		 */
		self.initChoiceTypeTable = function () {
			$scope.filter = {
				description: '',
			};
			$scope.choiceTypeTableParams = new ngTableParams({
				page: 1,
				count: 10,
				filter: $scope.filter
			}, {
					counts: [],
					data: self.lstChoiceTypes
				});
		}
		/**
		 * Show modal add choice type.
		 */
		self.showAddChoiceTypeModal = function () {
			self.resetDataChoiceType();
			$('#add-choice-type-modal').modal({ backdrop: 'static', keyboard: true });
		}
		/**
		 * Reset data for table choice type.
		 */
		self.resetDataChoiceType = function () {
			self.lstChoiceTypes = angular.copy(self.lstChoiceTypesTerm);
			angular.forEach(self.productGroupType.productGroupChoiceTypes, function (productGroupChoiceType) {
				for (var i = 0; i < self.lstChoiceTypes.length; i++) {
					if (productGroupChoiceType.choiceType.choiceTypeCode == self.lstChoiceTypes[i].choiceTypeCode) {
						self.lstChoiceTypes[i].isSelected = true;
						self.lstChoiceTypes[i].isSelectedPicker = productGroupChoiceType.pickerSwitch;
						break;
					}
				}
			});
			self.initChoiceTypeTable();
			self.setCheckAllFlag();
		}
		/**
		 * Handle check all choice type
		 */
		self.checkAllChoiceType = function () {
			var lstData = $scope.choiceTypeTableParams.filter() ? $filter('filter')(self.lstChoiceTypes, $scope.choiceTypeTableParams.filter()) : self.lstChoiceTypes;
			angular.forEach(lstData, function (choiceType) {
				choiceType.isSelected = self.checkAllFlag;
			});
		}
		/**
		 * Handle flag check all choice type.
		 */
		self.setCheckAllFlag = function () {
			var check = true;
			var lstData = $scope.choiceTypeTableParams.filter() ? $filter('filter')(self.lstChoiceTypes, $scope.choiceTypeTableParams.filter()) : self.lstChoiceTypes;
			for (var i = 0; i < lstData.length; i++) {
				if (lstData[i].isSelected == undefined || !lstData[i].isSelected) {
					check = false;
					break;
				}
			}
			if (lstData.length == 0) {
				check = false;
			}
			self.checkAllFlag = check;
		}
		/**
		 * Add choice type for Product Group Type.
		 */
		self.addChoiceType = function () {
			var lstProductGroupChoiceTypesTerm = [];
			angular.forEach(self.lstChoiceTypes, function (choiceType) {
				if (choiceType.isSelected) {
					var productGroupChoiceTypeNew = {
						"choiceType": choiceType,
						"pickerSwitch": choiceType.isSelectedPicker,
						"productGroupChoiceOptions": []
					};
					angular.forEach(self.productGroupType.productGroupChoiceTypes, function (productGroupChoiceType) {
						if (choiceType.choiceTypeCode == productGroupChoiceType.choiceType.choiceTypeCode) {
							productGroupChoiceTypeNew.productGroupChoiceOptions = productGroupChoiceType.productGroupChoiceOptions;
						}
					});
					lstProductGroupChoiceTypesTerm.push(productGroupChoiceTypeNew);
				}
			});
			self.productGroupType.productGroupChoiceTypes = angular.copy(lstProductGroupChoiceTypesTerm);
			self.productGroupType.productGroupChoiceTypes = $filter('orderBy')(self.productGroupType.productGroupChoiceTypes, 'choiceType.description', false)
			self.resetDataChoiceOption();
		}
		/**
		 * Handle remove Product Group Choice type.
		 */
		self.removeProductGroupChoiceType = function () {
			self.showCommonConfirmModal("Delete Choice Type", "Are you sure you want to delete the selected Choice Type ?", "deleteChoiceType");
		}
		/**
		 * Do remove Product Group Choice type.
		 */
		self.doRemoveProductGroupChoiceType = function () {
			var lstProductGroupChoiceTypes = [];
			angular.forEach(self.productGroupType.productGroupChoiceTypes, function (productGroupChoiceType) {
				if (!productGroupChoiceType.isSelected) {
					lstProductGroupChoiceTypes.push(productGroupChoiceType);
				}
			});
			self.productGroupType.productGroupChoiceTypes = angular.copy(lstProductGroupChoiceTypes);
			self.productGroupType.productGroupChoiceTypes = $filter('orderBy')(self.productGroupType.productGroupChoiceTypes, 'choiceType.description', false)
			self.checkAllProductGroupChoiceTypeFlag = false;
			self.resetDataChoiceOption();
		}
		/**
		 * Reset data for Choice Option.
		 */
		self.resetDataChoiceOption = function () {
			if (self.productGroupType.productGroupChoiceTypes != null &&
				self.productGroupType.productGroupChoiceTypes.length > 0) {
				self.productGroupChoiceType = self.productGroupType.productGroupChoiceTypes[0];
				self.selectChoiceType(self.productGroupChoiceType, 0);
			} else {
				self.productGroupChoiceType = {};
			}
		}
		/**
		 * Handle check all Product Group Choice type.
		 */
		self.checkAllProductGroupChoiceType = function () {
			var check = true;
			angular.forEach(self.productGroupType.productGroupChoiceTypes, function (productGroupChoiceType) {
				productGroupChoiceType.isSelected = self.checkAllProductGroupChoiceTypeFlag;
			});
		}
		/**
		 * Handle flag check all Product Group Choice type.
		 */
		self.setCheckAllProductGroupChoiceTypeFlag = function () {
			var check = true;
			angular.forEach(self.productGroupType.productGroupChoiceTypes, function (productGroupChoiceType) {
				if (!productGroupChoiceType.isSelected) {
					check = false;
				}
			});
			if (self.productGroupType.productGroupChoiceTypes.length == 0) {
				check = false;
			}
			self.checkAllProductGroupChoiceTypeFlag = check;
		}
		/**
		 * Handle choice option selected.
		 */
		self.handleChoiceOptionSelected = function (choiceOption) {
			if (choiceOption.isSelected) {
				var productGroupChoiceOption = {
					choiceOption: choiceOption,
					key: {
						productGroupTypeCode: self.productGroupType.productGroupTypeCode,
						choiceTypeCode: self.productGroupChoiceType.choiceType.choiceTypeCode,
						choiceOptionCode: choiceOption.key.choiceOptionCode,
					}
				}
				self.productGroupChoiceType.productGroupChoiceOptions.push(productGroupChoiceOption);
			} else {
				var index = 0;
				for (var i = 0; i < self.productGroupChoiceType.productGroupChoiceOptions.length; i++) {
					if (self.productGroupChoiceType.productGroupChoiceOptions[i].key.choiceOptionCode === choiceOption.key.choiceOptionCode) {
						index = i;
						break;
					}
				}
				self.productGroupChoiceType.productGroupChoiceOptions.splice(index, 1);
			}
		}
		/**
		 * Handle selected subDepartment.
		 */
		self.selectSubDepartment = function () {
			if (self.department.key == undefined) {
				self.showCommonConfirmModal("Confirmation", "Please select Department before select Sub Department.", "selectDepartment");
			}
		}
		/**
		 * Handle save Product Group Type.
		 */
		self.saveProductGroupType = function () {
			if (!self.isWaitingForLoadSubDepartment && self.validateProductGroupType()) {
				self.productGroupType.departmentNumberString = self.subDepartment.key.department;
				self.productGroupType.subDepartmentId = self.subDepartment.key.subDepartment == ''?' ':self.subDepartment.key.subDepartment;
				if (self.hasChangeDataProductGroupType()) {
					self.setDataProductGroupTypeUpdate();
					if (!self.validateChangePicker()) {
						self.doSaveProductGroupType();
					} else {
						self.success = '';
						self.error = 'Please select at least one applicable value for the Picker Choice Type.';
						self.isChangeProductGroupTypeView = false;
						self.isReturnToList = false;
						self.isReturnToTab = false;
						$window.scrollTo(0, 0);
					}
				} else {
					self.success = '';
					self.error = 'There are no changes on this page to be saved. Please make any changes to update.';
					self.isChangeProductGroupTypeView = false;
					self.isReturnToList = false;
					self.isReturnToTab = false;
				}
			} else {
				self.isChangeProductGroupTypeView = false;
				self.isReturnToList = false;
				self.isReturnToTab = false;
			}

		}
		/**
		 * Validate Product Group Type.
		 */
		self.validateProductGroupType = function () {
			var notError = true;
			if (self.productGroupType.productGroupType === null || self.productGroupType.productGroupType.trim() == '') {
				self.errorProductGroupType = true;
				notError = false;
			}
			if (self.department == null || self.department == undefined || self.department.key == undefined) {
				self.errorDepartment = true;
				notError = false;
			}
			if (self.subDepartment == null || self.subDepartment == undefined || self.subDepartment.key == undefined) {
				self.errorSubDepartment = true;
				notError = false;
			}
			return notError;
		}
		/**
		 * Validate Product Group Type.
		 */
		self.validateChangePicker = function () {
			var isChangePickerError = false;
			angular.forEach(self.productGroupType.productGroupChoiceTypes, function (choiceType) {
				if (choiceType.pickerSwitch && (choiceType.productGroupChoiceOptions == null || choiceType.productGroupChoiceOptions == undefined || choiceType.productGroupChoiceOptions.length ==0)) {
					isChangePickerError = true;
				}

			});
			return isChangePickerError;
		}
		/**
		 * Check has change data Product Group Type.
		 */
		self.hasChangeDataProductGroupType = function () {
			var hasChange = false;
			if (self.productGroupType !== null && self.productGroupType.productGroupType.trim() !== self.productGroupTypeTerm.productGroupType.trim()) {
				hasChange = true;
			} else if (self.department !== null && self.department !== undefined && self.department.key !== undefined && self.department.key.department.trim() !== self.productGroupTypeTerm.departmentNumberString.trim()) {
				hasChange = true;
			} else if (self.subDepartment !== null && self.subDepartment !== undefined && self.subDepartment.key !== undefined && self.subDepartment.key.subDepartment.trim() !== self.productGroupTypeTerm.subDepartmentId.trim()) {
				hasChange = true;
			} else {
				hasChange = self.checkChangeProductGroupChoiceType(self.productGroupType.productGroupChoiceTypes, self.productGroupTypeTerm.productGroupChoiceTypes);
			}
			return hasChange;

		}
		/**
		 * Check has change data Product Group Choice Type.
		 */
		self.checkChangeProductGroupChoiceType = function (lstProductGroupChoiceType, lstProductGroupChoiceTypeTerm) {
			var hasChange = true;
			if (lstProductGroupChoiceType.length !== lstProductGroupChoiceTypeTerm.length) {
				return true;
			} else if (lstProductGroupChoiceType.length == 0) {
				return false;
			} else {
				for (var i = 0; i < lstProductGroupChoiceType.length; i++) {
					hasChange = true;
					for (var j = 0; j < lstProductGroupChoiceTypeTerm.length; j++) {
						if (lstProductGroupChoiceType[i].choiceType.choiceTypeCode == lstProductGroupChoiceTypeTerm[j].choiceType.choiceTypeCode) {
							if (lstProductGroupChoiceType[i].pickerSwitch == lstProductGroupChoiceTypeTerm[j].pickerSwitch
								&& !self.hasChangeChoiceOption(lstProductGroupChoiceType[i].productGroupChoiceOptions, lstProductGroupChoiceTypeTerm[j].productGroupChoiceOptions)) {
								hasChange = false;
							}
						}
					}
					if (hasChange) {
						break;
					}
				}
			}

			return hasChange;
		}
		/**
		 * Check has change data Product Group Choice Option.
		 */
		self.hasChangeChoiceOption = function (lstChoiceOption, lstChoiceOptionTerm) {
			var hasChange = true;
			if (lstChoiceOption.length !== lstChoiceOptionTerm.length) {
				return true;
			} else if (lstChoiceOption.length == 0) {
				return false;
			} else {
				for (var i = 0; i < lstChoiceOption.length; i++) {
					hasChange = true;
					angular.forEach(lstChoiceOptionTerm, function (choiceOptionTerm) {
						if (lstChoiceOption[i].key.choiceOptionCode == choiceOptionTerm.key.choiceOptionCode) {
							hasChange = false;
						}
					});
					if (hasChange) {
						break;
					}
				}
			}
			return hasChange;

		}
		/**
		 * Do save Product Group Type.
		 */
		self.doSaveProductGroupType = function () {
			if (self.productGroupType.productGroupTypeCode == '') {
				self.addNewProductGroupType();
			} else {
				self.updateProductGroupType();
			}
		}
		/**
		 * Add new Product Group Type.
		 */
		self.addNewProductGroupType = function () {
			self.success = '';
			self.error = '';
			self.isWaitingForResponse = true;
			var productGroupType = angular.copy(self.productGroupTypeUpdate);
			productGroupTypeDetailsApi.addProductGroupType(productGroupType,
				function (response) {
					self.isWaitingForResponse = false;
					if(response.message == UPDATE_ERROR){
						self.error = UPDATE_ERROR_MESSAGE;
						self.isReturnToList = false;
						self.isReturnToTab = false;
						$window.scrollTo(0, 0);
					}else {
					if (self.isReturnToTab) {
						self.returnToTab();
					} else {
						self.returnToList();
						self.details.success = response.message;
						self.details.isLoadData = true;
					}
					}
				}, function (error) {
					self.isReturnToList = false;
					self.isReturnToTab = false;
					self.fetchError(error);
				});
		}
		/**
		 * Update Product Group Type.
		 */
		self.updateProductGroupType = function () {
			self.success = '';
			self.error = '';
			self.isWaitingForResponse = true;
			var productGroupType = angular.copy(self.productGroupTypeUpdate);
			productGroupTypeDetailsApi.updateProductGroupType(productGroupType,
				function (response) {
					self.isWaitingForResponse = false;
					if(response.message == UPDATE_ERROR){
						self.error = UPDATE_ERROR_MESSAGE;
						self.isChangeProductGroupTypeView = false;
						self.isReturnToList = false;
						self.isReturnToTab = false;
						$window.scrollTo(0, 0);
					}else {
						self.success = response.message;
						self.productGroupType = angular.copy(response.data.productGroupType);
						self.productGroupType.productGroupType = self.productGroupType.productGroupType.trim();
						self.productGroupType.productGroupChoiceTypes = $filter('orderBy')(self.productGroupType.productGroupChoiceTypes, 'choiceType.description', false)
						self.productGroupChoiceType = self.productGroupType.productGroupChoiceTypes[0];
						self.selectChoiceType(self.productGroupChoiceType, 0);
						self.productGroupTypeTerm = angular.copy(self.productGroupType);
						self.isWaitingForResponse = false;
						$window.scrollTo(0, 0);
						if (self.isChangeProductGroupTypeView) {
							self.changeProductGroupTypeView(response.message);
						}
						if (self.isReturnToList) {
							self.details.success = self.success;
							self.returnToList();
						}
						if (self.isReturnToTab) {
							$rootScope.success = self.success;
							$rootScope.isEditedOnPreviousTab = true;
						}
						if (self.isReturnToTab) self.returnToTab();
						self.getDataProductGroupChoiceType();
					}

				}, function (error) {
					self.isChangeProductGroupTypeView = false;
					self.isReturnToList = false;
					self.isReturnToTab = false;
					self.fetchError(error);
				});
		}
		/**
		 * Set data for Product Group Type update.
		 */
		self.setDataProductGroupTypeUpdate = function(){
			self.productGroupTypeUpdate = angular.copy(self.productGroupType);
			self.getProductGroupChoiceTypeSave(self.productGroupType.productGroupChoiceTypes, self.productGroupTypeTerm.productGroupChoiceTypes);
			self.productGroupTypeUpdate.productGroupChoiceTypes = self.lstChoiceTypesChange;

		}
		/**
		 * Get Product Group Choice Type change.
		 */
		self.getProductGroupChoiceTypeSave = function (lstProductGroupChoiceType, lstProductGroupChoiceTypeTerm) {
			self.lstChoiceTypesChange = [];
			var lstChoiceOptionChange = [];
			var action = '';
			var choiceTypeChange = {};
			angular.forEach(lstProductGroupChoiceType, function (choiceType) {
				choiceTypeChange = {};
				action = self.ACTION_ADD;
				lstChoiceOptionChange = [];
				for (var i = 0; i < lstProductGroupChoiceTypeTerm.length; i++) {
					if (choiceType.choiceType.choiceTypeCode == lstProductGroupChoiceTypeTerm[i].choiceType.choiceTypeCode) {
						if (choiceType.pickerSwitch !== lstProductGroupChoiceTypeTerm[i].pickerSwitch) {
							action = self.ACTION_UPDATE;
						} else {
							action = undefined;
						}
						lstChoiceOptionChange = self.getLstChoiceOptionChange(choiceType.productGroupChoiceOptions, lstProductGroupChoiceTypeTerm[i].productGroupChoiceOptions);
						break;
					}
				}
				if(action === self.ACTION_ADD){
					angular.forEach(choiceType.productGroupChoiceOptions, function (choiceOption) {
						choiceOption.action =self.ACTION_ADD;
						lstChoiceOptionChange.push(choiceOption);
					});
				}
				choiceTypeChange = {
					choiceType: choiceType.choiceType,
					productGroupChoiceOptions: lstChoiceOptionChange,
					pickerSwitch: choiceType.pickerSwitch === null || choiceType.pickerSwitch === undefined?false:choiceType.pickerSwitch,
					action: action,
				}
				self.lstChoiceTypesChange.push(choiceTypeChange);
			});
			angular.forEach(lstProductGroupChoiceTypeTerm, function (choiceTypeTerm) {
				choiceTypeChange = {};
				action = self.ACTION_DELETE;
				for (var i = 0; i < lstProductGroupChoiceType.length; i++) {
					if (choiceTypeTerm.choiceType.choiceTypeCode == lstProductGroupChoiceType[i].choiceType.choiceTypeCode) {
						action = self.ACTION_UPDATE;
						break;
					}
				}
				if (action == self.ACTION_DELETE) {
					lstChoiceOptionChange = [];
					angular.forEach(choiceTypeTerm.productGroupChoiceOption, function (choiceOption) {
						choiceOption.action = self.ACTION_DELETE;
						lstChoiceOptionChange.push(choiceOption);
					});
					choiceTypeChange = {
						choiceType: choiceTypeTerm.choiceType,
						productGroupChoiceOptions: lstChoiceOptionChange,
						pickerSwitch: choiceTypeTerm.pickerSwitch,
						action: action
					}
					self.lstChoiceTypesChange.push(choiceTypeChange);
				}
			});
		}
		/**
		 * Get Product Group Choice Option Change.
		 */
		self.getLstChoiceOptionChange = function(lstChoiceOptCurrent, lstChoiceOptionOrg) {
			var lstChoiceOptChange = [];
			angular.forEach(lstChoiceOptCurrent, function (choiceOptCurrent) {
				var isNew = true;
				for (var i = 0; i < lstChoiceOptionOrg.length; i++) {
					if (choiceOptCurrent.key.choiceOptionCode == lstChoiceOptionOrg[i].key.choiceOptionCode) {
						isNew = false;
						break;
					}
				}
				if (isNew) {
					choiceOptCurrent.action = self.ACTION_ADD;
					lstChoiceOptChange.push(choiceOptCurrent);
				}
			});
			angular.forEach(lstChoiceOptionOrg, function (choiceOptOrg) {
				var isDelete = true;
				for (var i = 0; i < lstChoiceOptCurrent.length; i++) {
					if (choiceOptOrg.key.choiceOptionCode == lstChoiceOptCurrent[i].key.choiceOptionCode) {
						isDelete = false;
						break;
					}
				}
				if (isDelete) {
					choiceOptOrg.action = self.ACTION_DELETE;
					lstChoiceOptChange.push(choiceOptOrg);
				}
			});
			return lstChoiceOptChange;
		}
		/**
		 * Handle diabled remove choice type button.
		 */
		self.disabledRemoveChoiceTypeBtn = function () {
			var isDiabled = true;
			if (self.productGroupType.productGroupChoiceTypes !== undefined) {
				angular.forEach(self.productGroupType.productGroupChoiceTypes, function (productGroupChoiceType) {
					if (productGroupChoiceType.isSelected) {
						isDiabled = false;
					}
				});
			}

			return isDiabled;
		}
		/**
		 * Show confirm modal.
		 */
		self.showCommonConfirmModal = function (title, content, typeModal) {
			self.commonConfirmModalTitle = title;
			self.commonConfirmModalContent = content;
			self.typeModal = typeModal;
			$('#common-confirm-modal').modal({ backdrop: 'static', keyboard: true });
		};
		/**
		 * Handle return to list Product Group Type.
		 */
		self.returnToListClick = function () {
			if (self.hasChangeDataProductGroupType()) {
				self.showCommonConfirmModal("Confirmation", "Unsaved data will be lost. Do you want to save the changes before continuing?", "returnToList");
			}
			else {
				self.returnToList();
			}
		}
		/**
		 * Close modal and returns to list Product Group Type.
		 */
		self.closeModalAndReturnToList = function () {
			$('#common-confirm-modal').on('hidden.bs.modal', function () {
				self.returnToList();
				$scope.$apply();
			});
			$("#common-confirm-modal").modal('hide');
		};
		/**
		 * Close modal and change Product Group Type view.
		 */
		self.closeModalAndChangeProductGroupTypeView = function () {
			$("#common-confirm-modal").modal('hide');
		};
		/**
		 * Returns to list Product Group Type.
		 */
		self.returnToList = function () {
			self.details.isShowing = false;
		}
		/**
		 *Reset data Product Group Type.
		 */
		self.resetData = function () {
			self.success = '';
			self.error = '';
			self.productGroupType = angular.copy(self.productGroupTypeTerm);
			self.department = self.getSelectedDepartmentByProductGroupType();
			self.selectDepartment(self.department);
			self.subDepartment = self.getSelectedSubDepartmentByProductGroupType(self.subDepartments[self.productGroupType.departmentNumberString.trim()]);
			self.resetDataChoiceOption();
			self.checkAllProductGroupChoiceTypeFlag = false;
		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.success = null;
			self.error = self.getErrorMessage(error);
			self.isWaitingForResponse = false;
			if (self.isReturnToTab) {
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			}
		}
		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
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
		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_PRODUCT_GROUP_TYPE, function () {
			if (self.details.isShowing) {
				if (self.hasChangeDataProductGroupType()) {
					self.showCommonConfirmModal("Confirmation", "Unsaved data will be lost. Do you want to save the changes before continuing?", "returnToTab");
				} else {
					$rootScope.$broadcast(self.RETURN_TAB);
				}
			}

		});
		/**
		 * Close modal and returns to list Product Group Type.
		 */
		self.closeModalAndReturnToTab = function () {
			$('#common-confirm-modal').on('hidden.bs.modal', function () {
				self.returnToTab();
				$scope.$apply();
			});
			$("#common-confirm-modal").modal('hide');
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
		 * Handle go to customer hierarchy page when click customer hierarchy link.
		 */
		self.goToCustomerHierarchy = function(customerHierarchyId){
			customHierarchyService.setSelectedTab("PRODUCT_GROUP");
			customHierarchyService.setNavigateFromProdGroupTypePage(true);
			customHierarchyService.setDisableReturnToList(false);
			customHierarchyService.setProductGroupTypeCodeNav(self.productGroupType.productGroupTypeCode);
			$state.go(appConstants.CUSTOM_HIERARCHY_ADMIN,{customerHierarchyId:customerHierarchyId});
		};
		/**
		 * Handle go to customer product group page when click customer product group link.
		 */
		self.goToCustProductGroupPage = function(custProductGroupId){
			productGroupService.setNavFromProdGrpTypePageFlag(true);
			productGroupService.setProductGroupId(custProductGroupId);
			productGroupService.setProductGroupTypeCodeNav(self.productGroupType.productGroupTypeCode);
			$state.go(appConstants.PRODUCT_GROUP);
		};

		/**
		 * Find image format by customer product id
		 *
		 * @param custProductGroupId
		 * @returns {*}
		 */
		self.findImageFormat = function (custProductGroupId){
			var i;
			var length = self.productGroupType.customerProductGroups.length;
			for(i = 0; i < length; i++){
				if (self.productGroupType.customerProductGroups[i].custProductGroupId === custProductGroupId
					&& self.productGroupType.customerProductGroups[i].primaryImage !== null){
					return self.productGroupType.customerProductGroups[i].primaryImage.imageFormatCode;
				}
			}
		};

		/**
		 * Show image popup
		 *
		 * @param custProductGroupId
		 */
		self.showFullImage = function (custProductGroupId) {
			self.imageFormat = self.findImageFormat(custProductGroupId);
			if (self.images[custProductGroupId.toString()].image
				&& self.images[custProductGroupId.toString()].image !== ''
				&& self.images[custProductGroupId.toString()].image !== 'images/no_image.png'
				&& self.imageFormat){
				self.imageBytes = self.images[custProductGroupId.toString()].image;
				$('#imageModal').modal({ backdrop: 'static', keyboard: true });
			}
		};

		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if(self.imageBytes !== '' ){
				downloadImageService.download(self.imageBytes, self.imageFormat);
			}
		};
	}
})();
