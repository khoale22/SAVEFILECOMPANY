/*
 *   productGroupInfoComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * product group info Component.
 *
 * @author vn87351
 * @since 2.14.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('productGroupInfo', {
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productGroup/productGroupDetail/productGroupInfo/productGroupInfo.html',
		bindings: {
			productGroupSelected: '=',
			isList: '<'
		},
		// The controller that handles our component logic
		controller: ProductGroupInfoController
	});
	app.filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
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

	ProductGroupInfoController.$inject = ['$window','$rootScope', '$scope', 'ngTableParams', 'codeTableApi', 'productGroupApi', '$filter', 'productGroupConstant','$timeout', 'ProductGroupService', '$state', 'appConstants', 'customHierarchyService'];

	/**
	 * product group detail- product group info component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @param codeTableApi
	 * @param ngTableParams
	 * @constructor
	 */
	function ProductGroupInfoController($window,$rootScope, $scope, ngTableParams, codeTableApi, productGroupApi, $filter, productGroupConstant, $timeout, productGroupService, $state, appConstants, customHierarchyService) {
		/** All CRUD operation controls of product group detail page goes here */

		var self = this;
		/**
		 * The selected associated product, it used to post event into product group associated product table..
		 */
		const SELECTED_ASSOCIATED_PRODUCT = 'selectedAssociatedProduct';
		/**
		 * Product Group Name Key
		 */
		const PRODUCT_GROUP_NAME_KEY = '_productGroupName_';
		/**
		 * Product Id Key
		 */
		const PRODUCT_ID_KEY = '_productID_';
		/**
		 * Message Associated product is exist
		 */
		const ASSOCIATED_PRODUCT_IS_EXIST = 'The  Product ID '+PRODUCT_ID_KEY+'  is already associated to Product Group: '+PRODUCT_GROUP_NAME_KEY+'.';
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;
		self.disableReturnToList = true;
		/**
		 * Data of form create/edit product group
		 * @type {Object}
		 */
		self.dataForm;
		/**
		 * Options for datepicker
		 */
		self.options = {
			minDate: new Date(),
			maxDate: new Date("12/31/9999")
		};
		/**
		 * Flag for show on site start date open date picker
		 * @type {boolean}
		 */
		self.showOnSiteStartDateOpen = false;
		/**
		 * Flag for show on site end date open date picker
		 * @type {boolean}
		 */
		self.showOnSiteEndDateOpen = false;
		/**
		 * List data of product group type
		 * @type {Array}
		 */
		self.productGroupTypeData = [];
		/**
		 * Whether or not the controller is waiting for data.
		 * @type {Boolean}
		 */
		self.isWaitingLoadProductGroupType = false;
		/**
		 * The ChoiceType when selected ChoiceType in table.
		 * @type {Object}
		 */
		self.choiceTypeSelected = {};
		/**
		 * The flag for create product group.
		 * @type {Boolean}
		 */
		self.isCreateProductGroup = false;
		/**
		 * Message success create or update product group
		 * @type {String}
		 */
		self.success = null;
		/**
		 * Message error create or update product group
		 * @type {String}
		 */
		self.error = null;
		/**
		 * The flag for error product group type.
		 * @type {Boolean}
		 */
		self.isProductGroupTypeError = false;
		/**
		 * The flag for error product group name.
		 * @type {Boolean}
		 */
		self.isProductGroupNameError = false;
		/**
		 * The flag for error Effective Date.
		 * @type {Boolean}
		 */
		self.isEffectiveDateError = false;
		/**
		 * The flag for error Expiration Date.
		 * @type {Boolean}
		 */
		self.isExpirationDateError = false;
		/**
		 * Message error validate Effective Date.
		 * @type {String}
		 */
		self.messEffectiveDateError = '';
		/**
		 * Message error validate Expiration Date.
		 * @type {String}
		 */
		self.messExpirationDateError = '';
		/**
		 * Current associated product position.
		 * @type {Number}
		 */
		self.currentAssociatedProductPosition = 0;
		/**
		 * Current tab index temp.
		 * @type {Number}
		 */
		self.currentTabIndexTemp = undefined;
		/**
		 * Flag for change tab view.
		 * @type {Boolean}
		 */
		self.isChangedTab = false;
		/**
		 * Flag for save data before change tab.
		 * @type {Boolean}
		 */
		self.isChangedTabAndSaveData = false;
		/**
		 * Customer hierachy primary path.
		 * @type {String}
		 */
		self.customerHierarchyPrimaryPath = '';
		/**
		 * Flag for wait load data associated product.
		 * @type {Boolean}
		 */
		self.isWaitLoadAssociatedProductDetail = false;
		/**
		 * Data form product group temp.
		 * @type {Object}
		 */
		self.dataFormTemp = {};
		/**
		 * Hierachy context selected.
		 * @type {Object}
		 */
		self.selectedHierarchyContexts = null;
		/**
		 * Flag clear product error or no.
		 * @type {Boolean}
		 */
		self.clearProductError = true;
		/**
		 * String empty
		 */
		const EMPTY_STRING = '';
		/**
		 * All field
		 */
		const ALL_FIELD = 'All';
		/**
		 * Product Group Name field
		 */
		const PRODUCT_GROUP_NAME_FIELD = 'Product Group Name';
		/**
		 * Product Group Type field
		 */
		const PRODUCT_GROUP_TYPE_FIELD = 'Product Group Type';
		/**
		 * Effective Date field
		 */
		const EFFECTIVE_DATE_FIELD = 'Effective Date';
		/**
		 * Expiration Date field
		 */
		const EXPIRATION_DATE_FIELD = 'Expiration Date';
		/**
		 * Action code Add
		 */
		const ACTION_ADD = 'A';
		/**
		 * Action code delete
		 */
		const ACTION_DELETE = 'D';
		/**
		 * Action code Update
		 */
		const ACTION_UPDATE = '';
		/**
		 * Flag for the first load data choice option.
		 * @type {Boolean}
		 */
		self.firstLoadDataChoiceOption = true;
		/**
		 * When click open date picker for anther attribute, store current status for date picker.
		 */
		self.openDatePicker = function (fieldName) {
			switch (fieldName) {
				case "showOnSiteStartDate":
					self.showOnSiteStartDateOpen = true;
					break;
				case "showOnSiteEndDate":
					self.showOnSiteEndDateOpen = true;
					break;
				default:
					break;
			}
			self.options = {
				minDate: new Date(),
				maxDate: new Date("12/31/9999")
			};
		};
		/**
		 * Message for error no matches found
		 */
		self.NO_MATCHES_FOUND = "No matches found";
		/**
		 * The domain tab bar data provider.
		 */
		self.domainTabBarDataProvider = [];
		/**
		 * List of product group info change log.
		 */
		self.productGroupInfoAudits = [];
		/**
		 * Flag wait load product group audit.
		 */
		self.isWaitingGetProductGroupInfoAudit = false;
		/**
		 * Constant order by asc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_ASC = "asc";
		/**
		 * Constant order by desc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_DESC = "desc";
		/**
		 * Flag for page display when navigate from Product Group Type page.
		 */
		self.navFromProdGrpTypePageFlag = false;
		/**
		 * Build eCommerce View Tab by the list of sale channel. Ignore sale channel element with index=1(Store)
		 * @param saleChannels
		 */
		self.buildECommerceViewTabBySaleChanel = function (saleChannels) {
			self.domainTabBarDataProvider = [
				{
					displayName: "Heb.com",
					id: "HebCom",
					logo: "images/logo_hebcom.png",
					saleChannel: saleChannels[0],
					hierCntxtCd: "CUST"
				},
				{
					displayName: "Hebtoyou",
					id: "Hebtoyou",
					logo: "images/logo_hebtoyou.png",
					saleChannel: saleChannels[2],
					hierCntxtCd: "HEBTO"
				},
				{
					displayName: "Blooms",
					id: "Blooms",
					logo: "images/logo_blooms.jpg",
					saleChannel: saleChannels[3],
					hierCntxtCd: "BLOOM"
				}
			];
		}
		/**
		 * Change tab handle. Set current tab value.
		 * @param tab
		 */
		self.tabSelected = function (tab) {
			self.currentTab = tab;
			if (!self.isChangedTabAndSaveData) {
			self.error = EMPTY_STRING;
			self.success = EMPTY_STRING;
			} else {
				self.isChangedTabAndSaveData = false;
			}
			self.isProductGroupNameError = false;
			self.isProductGroupTypeError = false;
			self.isEffectiveDateError = false;
			self.isExpirationDateError = false;
			self.messEffectiveDateError = EMPTY_STRING;
			self.messExpirationDateError = EMPTY_STRING;
			self.currentAssociatedProductPosition = 0;
			if (!self.isCreateProductGroup) {
				self.loadProductGroupInfo();
			}
		}
		/**
		 * Init data product group.
		 */
		self.init = function () {
			if(self.isList || productGroupService.getNavFromProdGrpTypePageFlag()
				|| productGroupService.getNavigateFromCustomerHierPage() || productGroupService.isBrowserAll()){
				self.disableReturnToList = false;
			}
			if(!customHierarchyService.getDisableReturnToList()){
				customHierarchyService.setDisableReturnToList(true);
				productGroupService.setCustomerHierarchyId(null);
			}
			self.isWaiting = true;
			self.navFromProdGrpTypePageFlag = productGroupService.getNavFromProdGrpTypePageFlag();
			self.getProductGroupType();
			productGroupApi.findAllSaleChanel(
				//success
				function (results) {
					angular.forEach(results, function (value) {
						value.id = value.id + "   ";
					});
					self.saleChannels = angular.copy(results);
					self.buildECommerceViewTabBySaleChanel(self.saleChannels);
					self.isWaiting = false;
				}
				, self.callApiError
			);
			self.hierarchyData = null;
			if (self.productGroupSelected !== undefined) {
				self.loadProductGroupInfo();
				self.isCreateProductGroup = false;
			} else {
				self.isCreateProductGroup = true;
				var customerProductGroup = {
					custProductGroupId: EMPTY_STRING,
					custProductGroupName: EMPTY_STRING,
					productGroupType: {},
					custProductGroupDescription: EMPTY_STRING,
					custProductGroupDescriptionLong: EMPTY_STRING
				}
				self.dataForm = {
					customerProductGroup: customerProductGroup
				}
				self.dataFormTemp = angular.copy(self.dataForm);

			}
		}
		/**
		 * watcher for get customer hierarchy data
		 * */
		$scope.$on('addHierarchyOnAddNewMode', function (event, args) {
			self.hierarchyData = args;
		});
		/**
		 * watcher for reload form event
		 */
		$scope.$on('reloadProductInfo', function (event, args) {
			self.productGroupSelected = args;
			self.loadProductGroupInfo();
		});
		/**
		 * load data for product group info
		 */
		self.loadProductGroupInfo = function () {
			if (self.currentTab == null || self.currentTab.saleChannel == null || self.productGroupSelected === undefined) {
				return;
			}
			self.resetSelectedAssociatedProduct();
			self.isWaiting = true;
			productGroupApi.findProductGroupInfo({
					'saleChannel': self.currentTab.saleChannel.id,
					'hierCntxtCd': self.currentTab.hierCntxtCd,
					'productGroupId': self.productGroupSelected,
					'tab': productGroupConstant.PRODUCT_GROUP_INFO_TAB
				},
				self.callApiSuccess, self.callApiError);
		}
		/**
		 * Processing if call api success
		 * @param results
		 */
		self.callApiSuccess = function (response) {
			self.isWaiting = false;
			self.dataForm = response;
			if (response === null) {
				self.error = self.NO_MATCHES_FOUND;
				return;
			}
			self.dataForm.customerProductGroup.custProductGroupName = self.dataForm.customerProductGroup.custProductGroupName.trim();
			self.dataForm.customerProductGroup.custProductGroupDescription = self.dataForm.customerProductGroup.custProductGroupDescription.trim();
			self.dataForm.customerProductGroup.custProductGroupDescriptionLong = self.dataForm.customerProductGroup.custProductGroupDescriptionLong.trim();
			if(self.dataForm.productOnline != null){
				self.dataForm.productOnline.expirationDate = new Date(self.dataForm.productOnline.expirationDate);
				self.dataForm.productOnline.key.effectiveDate = new Date(self.dataForm.productOnline.key.effectiveDate);
			}
			self.customerHierarchyPrimaryPath = self.dataForm.primaryPath;
			if (!productGroupService.getReturnToListFlag() &&
				productGroupService.getSelectedProductId() !== null && productGroupService.getSelectedProductId() !== undefined) {
				productGroupService.setSelectedProductId(null);
			}
			self.dataFormTemp = angular.copy(self.dataForm);
			self.firstLoadDataChoiceOption = true;
			$timeout(function(){
				$rootScope.$broadcast('reloadAssociatedProduct', self.dataForm.dataAssociatedProduct);
			}, 300);
		};
		/**
		 * Check enabled or disabled btn select Product Group Type.
		 */
		self.checkDisableBtnProductGroupType = function (){
			var isDisableBtnProductGroupType = false;
			if(self.dataForm !== undefined && self.dataForm.dataAssociatedProduct != undefined && self.dataForm.dataAssociatedProduct.rows != undefined){
				angular.forEach(self.dataForm.dataAssociatedProduct.rows, function (data,index) {
					if (self.dataForm.dataAssociatedProduct.rows[index].productId != null && self.dataForm.dataAssociatedProduct.rows[index].productId != EMPTY_STRING) {
						isDisableBtnProductGroupType = true;
			}
				});
			}
			return isDisableBtnProductGroupType;
		}
		/**
		 * Check warning customer hierachy path.
		 */
		self.isWarningCustomerHierarchyPath = function(){
			return !self.isCreateProductGroup && (self.customerHierarchyPrimaryPath== null || self.customerHierarchyPrimaryPath== undefined || self.customerHierarchyPrimaryPath == EMPTY_STRING);
		}
		/**
		 * Check warning associated product.
		 */
		self.isWarningAssociatedProduct = function(){
			var isWarning = !self.isCreateProductGroup && (self.dataForm == undefined || self.dataForm.dataAssociatedProduct == null);
			if(!self.isCreateProductGroup && !isWarning){
				isWarning = true;
				angular.forEach(self.dataForm.dataAssociatedProduct.rows , function (value,key) {
					if(value.productId!=undefined && value.productId!=EMPTY_STRING){
						isWarning = false;
					}
				});
			}
			return isWarning;
		}
		/**
		 * call api error and throw message
		 * @param error
		 */
		self.callApiError = function (error) {
			self.success=EMPTY_STRING;
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
		};
		/**
		 * get data product group type
		 */
		self.getProductGroupType = function () {
			self.isWaiting = true;
			productGroupApi.findProductGroupType(function (response) {
				self.productGroupTypeData = response;
				self.currentproductGroupTypeData = response;
			}, self.callApiError);
		}
		/**
		 * Populates current category list based on search query string.
		 * @param query search string
		 * @returns {Array}
		 */
		self.getCurrentDropDownResults = function (query) {
			var currentList = [];
			if (query === null || !query.length || query.length === 0) {
				currentList = self.productGroupTypeData;
			} else {
				for (var index = 0; index < self.productGroupTypeData.length; index++) {
					if (self.equalsIgnoreCase(self.productGroupTypeData[index].productGroupTypeSummary, query)) {
						currentList.push(self.productGroupTypeData[index]);
					}
				}
			}
			self.currentproductGroupTypeData = currentList;
		};
		/**
		 * Helper comparison function for populating category dropdown.
		 * @param compareString category dropdown string
		 * @param query search string
		 * @returns {Array|{index: number, input: string}}
		 */
		self.equalsIgnoreCase = function (compareString, query) {
			return compareString.toUpperCase().match(query.toUpperCase());
		};
		/**
		 * go back search form.
		 */
		self.navigateSearchForm = function () {
			productGroupService.setProductGroupId(null);
			customHierarchyService.setNavigatedFromOtherPage(true);
			customHierarchyService.setNavigatedForFirstSearch(true);
			customHierarchyService.setNotFacingHierarchyLink(true);
			if(productGroupService.getNavigateFromCustomerHierPage() && productGroupService.getCustomerHierarchyId() !== null){
				self.goToCustomerHierarchy();
			}else if(productGroupService.getNavigateFromCustomerHierPage()){
				customHierarchyService.setReturnToListFlag(true);
				productGroupService.getNavigateFromCustomerHierPage(false);
				customHierarchyService.setSelectedTab("PRODUCT_GROUP");
				customHierarchyService.setDisableReturnToList(true);
				$state.go(appConstants.CUSTOM_HIERARCHY_ADMIN);
			}else if(self.navFromProdGrpTypePageFlag){
				customHierarchyService.setReturnToListFlag(true);
				customHierarchyService.setDisableReturnToList(true);
				$state.go(appConstants.CODE_TABLE);
			} else{
				$rootScope.$broadcast('changeMode', 'search-mode');
			}
		}

		/**
		 * call goToCustomerHierarchy
		 */
		self.goToCustomerHierarchy = function(){
			$rootScope.$broadcast('goToCustomerHierarchy');
		}

		/**
		 * Validate product group info.
		 */
		self.validateProductGroupInfo = function (fieldName) {
			var isValid = true;
			if (fieldName === ALL_FIELD || fieldName === PRODUCT_GROUP_NAME_FIELD) {
				if (self.dataForm.customerProductGroup.custProductGroupName.trim() === EMPTY_STRING) {
					self.isProductGroupNameError = true;
					isValid = false;
				} else {
					self.isProductGroupNameError = false;
				}
			}
			if (fieldName === ALL_FIELD || fieldName === PRODUCT_GROUP_TYPE_FIELD) {
				if (self.dataForm.customerProductGroup.productGroupType.productGroupType === undefined
					|| self.dataForm.customerProductGroup.productGroupType.productGroupType.trim() == EMPTY_STRING) {
					self.isProductGroupTypeError = true;
					isValid = false;
				} else {
					self.isProductGroupTypeError = false;
				}
			}
			if (!self.isCreateProductGroup && (fieldName === ALL_FIELD || fieldName === EFFECTIVE_DATE_FIELD) && self.checkChangedProductOnline(self.dataForm.productOnline)) {
				self.messEffectiveDateError = EMPTY_STRING;
				self.isEffectiveDateError = false;
				if(self.dataForm.productOnline != null && self.dataForm.productOnline.showOnSiteByExpirationDate){
					if (self.dataForm.productOnline.key == null || self.dataForm.productOnline.key.effectiveDate == null) {
						self.messEffectiveDateError = 'Start Date is mandatory.';
						self.isEffectiveDateError = true;
						isValid = false;
					} else if (!self.isDate1GreaterThanOrEqualToDate2(self.dataForm.productOnline.key.effectiveDate, new Date())) {
						self.messEffectiveDateError = 'Start Date must be greater than or equal Current Date.';
						self.isEffectiveDateError = true;
						isValid = false;
					} else {
						if (!self.isDate1GreaterThanDate2(self.dataForm.productOnline.expirationDate, self.dataForm.productOnline.key.effectiveDate)) {
							self.messEffectiveDateError = 'Start Date must be less than End Date';
							self.isEffectiveDateError = true;
							isValid = false;
						}else {
							if(self.messExpirationDateError == 'End Date must be greater than Start Date and less than 12/31/9999.'){
								self.messExpirationDateError = EMPTY_STRING;
								self.isExpirationDateError = false;
							}
						}
					}
				}
			}
			if (!self.isCreateProductGroup && (fieldName === ALL_FIELD || fieldName === EXPIRATION_DATE_FIELD) && self.checkChangedProductOnline(self.dataForm.productOnline)) {
				self.messExpirationDateError = EMPTY_STRING;
				self.isExpirationDateError = false;
				if(self.dataForm.productOnline != null && self.dataForm.productOnline.showOnSiteByExpirationDate){
					if (self.dataForm.productOnline.expirationDate == null) {
						self.messExpirationDateError =  'End Date is mandatory.';
						self.isExpirationDateError = true;
						isValid = false;
					} else {
						if (!self.isDate1GreaterThanDate2(self.dataForm.productOnline.expirationDate, self.dataForm.productOnline.key.effectiveDate)) {
							self.messExpirationDateError = 'End Date must be greater than Start Date and less than 12/31/9999.';
							self.isExpirationDateError = true;
							isValid = false;
						} else{
							if(self.messEffectiveDateError == 'Start Date must be less than End Date'){
								self.messEffectiveDateError = EMPTY_STRING;
								self.isEffectiveDateError = false;
							}
						}
					}
				}
			}
			return isValid;
		}
		/**
		 * Set data associated product change for product group info
		 */
		self.setDataAssociatedProducts = function (productGroupInfo){
			var customerProductChoices = [];
			if(self.isCreateProductGroup){
				if(productGroupInfo.dataAssociatedProduct!= null) {
					angular.forEach(productGroupInfo.dataAssociatedProduct.rows, function (data, index) {
						if (data.productId != null && data.productId != undefined && data.productId !=EMPTY_STRING) {
							data.productId = data.productId.toString();
							self.getLstChoiceOption(productGroupInfo.lstProductDetails[index].choices,true,0,data.productId,customerProductChoices)
						}
					});
				}
			}else {
				if(productGroupInfo.dataAssociatedProduct.rows[self.currentAssociatedProductPosition].productId != null && productGroupInfo.dataAssociatedProduct.rows[self.currentAssociatedProductPosition].productId != EMPTY_STRING){
					if(productGroupInfo.dataAssociatedProduct.rows[self.currentAssociatedProductPosition].productId != self.dataFormTemp.dataAssociatedProduct.rows[self.currentAssociatedProductPosition].productId){
						self.getLstChoiceOption(productGroupInfo.lstProductDetails[self.currentAssociatedProductPosition].choices,true,self.currentAssociatedProductPosition,productGroupInfo.dataAssociatedProduct.rows[self.currentAssociatedProductPosition].productId,customerProductChoices);
					}else{
						self.getLstChoiceOption(productGroupInfo.lstProductDetails[self.currentAssociatedProductPosition].choices,false,self.currentAssociatedProductPosition,productGroupInfo.dataAssociatedProduct.rows[self.currentAssociatedProductPosition].productId,customerProductChoices);
					}
				}

			}
			productGroupInfo.dataAssociatedProduct.customerProductChoices = customerProductChoices;
		}
		/**
		 * Get list choice option change.
		 */
		self.getLstChoiceOption = function (lstChoiceOption,isAddNew, index, productId,  customerProductChoices){
			if(isAddNew){
				angular.forEach(lstChoiceOption, function (choiceOption) {
					if(choiceOption.isSelected){
						choiceOption.action = ACTION_ADD;
						choiceOption.key.productId = productId;
						customerProductChoices.push(choiceOption);
					}
				});
			}else{
				var lstChoiceOptionTemp = self.dataForm.lstProductDetailsTemp[index].choices;
				angular.forEach(lstChoiceOptionTemp, function (choiceOptionTemp,indexTemp) {
					var choiceOption = lstChoiceOption[indexTemp]
					if(!choiceOptionTemp.isSelected && choiceOption.isSelected){
						choiceOption.action = ACTION_ADD;
						choiceOption.key.productId = productId;
						customerProductChoices.push(choiceOption);
					}else if(choiceOptionTemp.isSelected && !choiceOption.isSelected) {
						choiceOption.action = ACTION_DELETE;
						choiceOption.key.productId = productId;
						customerProductChoices.push(choiceOption);
					}
				});
			}
		}
		/**
		 * Get product online change.
		 */
		self.getProductOnline = function (productGroup) {
			var productOnline = angular.copy(productGroup.productOnline);
			if(productGroup.productOnline !== null){
				if(productGroup.productOnline !== null && productGroup.productOnline.key !== null && productGroup.productOnline.key.effectiveDate !== null){
					productOnline.key.effectiveDate = self.convertDateToStringWithYYYYMMDD(productGroup.productOnline.key.effectiveDate);
					}
				if(productGroup.productOnline !== null && productGroup.productOnline.expirationDate !== null){
					productOnline.expirationDate = self.convertDateToStringWithYYYYMMDD(productGroup.productOnline.expirationDate);
					}
				if(self.checkChangedProductOnline(productGroup.productOnline)) {
					productOnline.key.saleChannelCode = self.currentTab.saleChannel.id;
					productOnline.key.productId = productGroup.customerProductGroup.custProductGroupId;
					productOnline.showOnSite = productGroup.productOnline.showOnSiteByExpirationDate;
					productOnline.action = ACTION_UPDATE;
				}else{
					productOnline.action = null;
				}
			}
			return productOnline;
		}
		/**
		 * Save product group info.
		 */
		self.saveProductGroup = function () {
			self.success = EMPTY_STRING;
			self.error = EMPTY_STRING;
			if(self.isCreateProductGroup){
				if(self.validateProductGroupInfo(ALL_FIELD)){
					self.createProductGroup();
				}
			}else{
				if(!self.checkChangedData()){
					self.success = EMPTY_STRING;
					self.error = 'There are no changes on this page to be saved. Please make any changes to update.';
					$window.scrollTo(0, 0);
				}else {
					if(self.validateProductGroupInfo(ALL_FIELD)) {
						self.updateProductGroup();
					}
				}
			}
		}
		/**
		 * get data to save associated product
		 */
		self.getDataSaveAssociatedProduct = function(){
			var productGroupInfo = angular.copy(self.dataForm);
			productGroupInfo.customerProductGroup= angular.copy(self.dataFormTemp.customerProductGroup);
			productGroupInfo.productOnline= self.getProductOnline(angular.copy(self.dataFormTemp));
			self.setDataAssociatedProducts(productGroupInfo);
			return productGroupInfo;
		}
		/**
		 * Create a new product group.
		 */
		self.createProductGroup = function(){
			var productGroupInfo = angular.copy(self.dataForm);
			productGroupInfo.hierarchies = self.hierarchyData;
			self.setDataAssociatedProducts(productGroupInfo);
			self.isWaiting = true;
			productGroupApi.createProductGroupInfo(productGroupInfo,self.updateProductGroupSuccess, self.callApiError);
		}
		/**
		 * Update the product group.
		 */
		self.updateProductGroup = function(){
			var productGroupInfo = angular.copy(self.dataForm);
			self.setDataAssociatedProducts(productGroupInfo);
			productGroupInfo.customerProductGroup.action = self.checkChangeProductGroupInfo()?ACTION_UPDATE:null;
			productGroupInfo.productOnline = self.getProductOnline(productGroupInfo);
			self.isWaiting = true;
			productGroupApi.updateProductGroupInfo(productGroupInfo,self.updateProductGroupSuccess, self.callApiError);
		}
		/**
		 * Handle after save product group info success.
		 */
		self.updateProductGroupSuccess = function (response) {
			self.isWaitLoadAssociatedProductDetail = false;
			if (self.isChangedTab) {
				self.isChangedTab = false;
				$scope.active = self.currentTabIndexTemp;
			}
			self.productGroupSelected = response.data;
			self.isWaiting = false;
			self.loadProductGroupInfo();
			self.success = response.message;
			self.isCreateProductGroup = false;
			self.isChangedTabAndSaveData = true;
		}
		/**
		 * Reset data product group info.
		 */
		self.resetData = function () {
			if(self.isCreateProductGroup){
				self.hierarchyData = null;
				self.customerHierarchyPrimaryPath = EMPTY_STRING;
				self.dataForm.dataAssociatedProduct = undefined;
				self.dataForm = angular.copy(self.dataFormTemp);
			}else{
				if(self.checkChangedData()){
					self.loadProductGroupInfo();
				}
			}
			self.isWaitLoadAssociatedProductDetail = false;
			self.isProductGroupNameError = false;
			self.isProductGroupTypeError = false;
			self.isEffectiveDateError = false;
			self.isExpirationDateError = false;
			self.messEffectiveDateError = EMPTY_STRING;
			self.messExpirationDateError = EMPTY_STRING;
			self.success = EMPTY_STRING;
			self.error = EMPTY_STRING;
			self.selectedHierarchyContexts = null;
		}
		/**
		 * Handle change show on site switch.
		 */
		self.changeShowOnSiteSw = function (){
			self.isEffectiveDateError = false;
			self.isExpirationDateError = false;
			self.messEffectiveDateError = EMPTY_STRING;
			self.messExpirationDateError = EMPTY_STRING;
			if(!self.dataForm.productOnline.showOnSiteByExpirationDate){
				if(self.dataFormTemp.productOnline !== null){
					self.dataForm.productOnline.expirationDate = angular.copy(self.dataFormTemp.productOnline.expirationDate);
					self.dataForm.productOnline.key.effectiveDate = angular.copy(self.dataFormTemp.productOnline.key.effectiveDate);
				}else{
					self.dataForm.productOnline.expirationDate = null;
					self.dataForm.productOnline.key.effectiveDate = null;
				}
			}else {
				if(self.dataForm.productOnline.expirationDate == null || self.dataForm.productOnline.expirationDate == undefined){
					self.dataForm.productOnline.expirationDate = new Date("12/31/9999");
					self.dataForm.productOnline.key = {
						effectiveDate : new Date()
					}
				}
			}
		}
		/**
		 * Show previous product.
		 */
		self.previousProduct = function () {
			if(!self.isWaitLoadAssociatedProductDetail) {
				self.showAssociatedProductDetailsByIndex(self.prevAssociatedProductPosition);
			}
		}
		/**
		 * Show next product.
		 */
		self.nextProduct = function () {
			if(!self.isWaitLoadAssociatedProductDetail) {
				self.showAssociatedProductDetailsByIndex(self.nextAssociatedProductPosition);
			}
		}
		/**
		 * Show product details by the position of product in arraylist.
		 *
		 * @param index the position of product in array list.
		 */
		self.showAssociatedProductDetailsByIndex = function (index) {
			self.currentAssociatedProductPosition = index;

			var product = self.dataForm.dataAssociatedProduct.rows[self.currentAssociatedProductPosition];
			if(product != null) {
				if(productGroupService.getReturnToListFlag()
						&& productGroupService.getSelectedProductId() !== null
						&& productGroupService.getSelectedProductId() !== undefined){
						$scope.currentProductId = productGroupService.getSelectedProductId();
					}else {
				$scope.currentProductId = product.productId;
					}
				$rootScope.$broadcast(SELECTED_ASSOCIATED_PRODUCT, product, self.firstLoadDataChoiceOption);
				self.firstLoadDataChoiceOption = false;
				product.isDisabledUpdate = false;
				if( product.productId!==undefined && product.productId!==EMPTY_STRING && product.productId!=null && self.isNotDuplicateProductIdOrUpc(product) )
					product.isDisabledUpdate = true;
			}
		}
		/**
		 * Returns the enable/disable status for next button.
		 */
		self.isEnableNextProductSelect = function () {
			if (self.dataForm == undefined || self.dataForm.dataAssociatedProduct == undefined) {
				return false;
			}
			self.nextAssociatedProductPosition = -1;
			angular.forEach(self.dataForm.dataAssociatedProduct.rows , function (value,key) {
				if(value.productId!==undefined && value.productId!==EMPTY_STRING && key>self.currentAssociatedProductPosition){
					if(self.nextAssociatedProductPosition ===-1 &&
						value.productId!==undefined && value.productId!=null && value.productId!==EMPTY_STRING){
						self.nextAssociatedProductPosition = key;
					}
				}
			});
			return (self.nextAssociatedProductPosition>-1);
		}
		/**
		 * Returns the enable/disable status for previous button.
		 */
		self.isEnablePrevProductSelect = function () {
			if (self.dataForm == undefined || self.dataForm.dataAssociatedProduct == undefined) {
				return false;
			}
			self.prevAssociatedProductPosition = -1;
			angular.forEach(self.dataForm.dataAssociatedProduct.rows , function (value,key) {
				if(value.productId!==undefined && value.productId!=null && value.productId!==EMPTY_STRING && key<self.currentAssociatedProductPosition){
					self.prevAssociatedProductPosition = key;
				}
			});
			return (self.prevAssociatedProductPosition>-1);
		}
		/**
		 * Reset selected associated product.
		 */
		self.resetSelectedAssociatedProduct = function () {
			self.currentAssociatedProductPosition = 0;
			$scope.currentProductId = null;
		}
		/**
		 * This method will be called from associated product detail component to update product id into the list of associated product list.
		 *
		 * @param productId the id of product.
		 * @param upc the upc.
		 */
		self.updateProductIdToView = function (productId, upc) {
			if (self.dataForm != null && productId != null) {
				var index = 0;
				angular.forEach(self.dataForm.dataAssociatedProduct.rows, function (product) {
					if (product.productId == upc) {
						product.productId = productId;
						if (index == self.currentAssociatedProductPosition) {
							$scope.currentProductId = productId;
						}
					}
					index++;
				});
				if(!self.isNotDuplicateProductIdOrUpc({productId:productId})){
					self.showMessageForConflictProductId({productId:productId});
				}
			}
		}
		/**
		 * Check duplicate product id on client side before request to server.
		 *
		 * @param product
		 */
		self.isNotDuplicateProductIdOrUpc = function(product){
			var numberOfProductIds = 0;
			angular.forEach(self.dataForm.dataAssociatedProduct.rows, function(prod){
				if((numberOfProductIds < 2)){
					if(product.productId === prod.productId && prod.productId != undefined && prod.productId != EMPTY_STRING){
						numberOfProductIds+=1;
					}
				}
			});
			return !(numberOfProductIds > 1);
		}
		/**
		 * Check the product id in the list, if it is exist then return true or false.
		 *
		 * @param product
		 */
		self.isExistProductIdInAssociatedProduct = function(product){
			var isExisting = false;
			angular.forEach(self.dataForm.dataAssociatedProduct.rows, function(prod){
				if(!isExisting){
					if(product.productId == prod.productId && prod.productId != undefined && prod.productId != EMPTY_STRING){
						isExisting = true;
					}
				}
			});
			return isExisting;
		}
		/**
		 * Clear error product id on text box.
		 */
		self.clearErrorProductId = function(){
			if(self.clearProductError){
				self.dataForm.dataAssociatedProduct.rows[self.currentAssociatedProductPosition].isDisabledUpdate =false;
				self.dataForm.dataAssociatedProduct.rows[self.currentAssociatedProductPosition].productId = null;
				$scope.currentProductId = null;
			}

		}
		/**
		 * Get the action code from current associated product.
		 */
		self.getActionCode = function(){
			var product = self.dataForm.dataAssociatedProduct.rows[self.currentAssociatedProductPosition];
			return product.actionCode;
		}
		/**
		 * Check has change product group info.
		 */
		self.checkChangedData = function () {
			var hasChangeData = self.dataForm !== undefined && self.dataForm.customerProductGroup !==undefined && (self.checkChangeProductGroupInfo() || self.checkChangeAssociatedProduct() || self.checkChangedProductOnline(self.dataForm.productOnline));
			$rootScope.contentChangedFlag = hasChangeData;
			return hasChangeData;
		}
		/**
		 * Check change data product group online
		 */
		self.checkChangedProductOnline = function(productOnline){
			var isChanged = false;
			if(self.isCreateProductGroup){
				return isChanged;
			}
			if(productOnline !== null && productOnline !== undefined && (self.dataFormTemp.productOnline == null || self.dataFormTemp.productOnline == undefined)){
				if(!productOnline.showOnSiteByExpirationDate && productOnline.expirationDate == null){
					isChanged = false;
				} else {
					isChanged = true;
				}
			}else if(productOnline !== null  && productOnline !== undefined && self.dataFormTemp.productOnline !==  null && self.dataFormTemp.productOnline !== undefined){
				if(productOnline.showOnSiteByExpirationDate != self.dataFormTemp.productOnline.showOnSiteByExpirationDate){
					isChanged= true;
				}
				if(self.convertDateToStringWithYYYYMMDD(productOnline.expirationDate) != self.convertDateToStringWithYYYYMMDD(self.dataFormTemp.productOnline.expirationDate)){
					isChanged= true;
				}
				if(self.convertDateToStringWithYYYYMMDD(productOnline.key.effectiveDate) != self.convertDateToStringWithYYYYMMDD(self.dataFormTemp.productOnline.key.effectiveDate)){
					isChanged= true;
				}
			}
			return isChanged;
		}
		/**
		 * Check change data associated product.
		 */
		self.checkChangeAssociatedProduct = function(){
			if(self.isCreateProductGroup) return false;
			var isChanged = false;
			angular.forEach(self.dataForm.dataAssociatedProduct.rows, function (data,index) {
				if (data.productId != self.dataFormTemp.dataAssociatedProduct.rows[index].productId) {
					isChanged = true;
				}
			});

			if(!isChanged){
				angular.forEach(self.dataForm.lstProductDetailsTemp, function (data,index) {
					angular.forEach(data.choices, function (choicesTemp,index1) {
						if (choicesTemp.isSelected != self.dataForm.lstProductDetails[index].choices[index1].isSelected) {
							isChanged = true;
						}
					});
				});
			}
			return isChanged;
		}
		/**
		 * Check change product group info
		 */
		self.checkChangeProductGroupInfo = function (){
			var isChanged = false;
			if (self.dataFormTemp.customerProductGroup.custProductGroupName.trim() !== self.dataForm.customerProductGroup.custProductGroupName.trim()) {
				isChanged = true;
			}
			if (self.dataFormTemp.customerProductGroup.productGroupType.productGroupTypeCode !== self.dataForm.customerProductGroup.productGroupType.productGroupTypeCode) {
				isChanged = true;
			}
			if (self.dataFormTemp.customerProductGroup.custProductGroupDescription.trim() !== self.dataForm.customerProductGroup.custProductGroupDescription.trim()) {
				isChanged = true;
			}
			if (self.dataFormTemp.customerProductGroup.custProductGroupDescriptionLong.trim() !== self.dataForm.customerProductGroup.custProductGroupDescriptionLong.trim()) {
				isChanged = true;
			}
			return isChanged;
		}
		/**
		 * Show common modal.
		 */
		self.showCommonConfirmModal = function (title, content, typeModal) {
			self.commonConfirmModalTitle = title;
			self.commonConfirmModalContent = content;
			self.typeModal = typeModal;
			$('#common-confirm-modal').modal({ backdrop: 'static', keyboard: true });
		}
		/**
		 * Reset data and change tab view when click change tab.
		 */
		self.resetDataAndChangeTabView = function () {
			$scope.active = self.currentTabIndexTemp;
			self.resetData();
			self.isChangedTab = false;
		}
		/**
		 * Handle change tab view.
		 */
		self.handleChangeTab = function (tab, event, index) {
			self.currentTabIndexTemp = index;
			if (self.isCreateProductGroup || self.checkChangedData()) {
				if (event !== undefined) {
					self.isChangedTab = true;
					event.preventDefault();
					if (self.validateProductGroupInfo(ALL_FIELD)) {
						self.showCommonConfirmModal("Confirmation", "Unsaved data will be lost. Do you want to save the changes before continuing?", "changeTab");
					}
				}
			}else{
				self.error = EMPTY_STRING;
				self.success = EMPTY_STRING;
			}
		}
		/**
		 * Do show comfirm modal delete product group.
		 */
		self.showConfirmModalDelete = function () {
			self.showCommonConfirmModal("Delete Product Group", "Do you really want to delete the selected product group?", "delete");
		}
		/**
		 * Do delete product group.
		 */
		self.deleteProductGroup = function () {
			self.error=EMPTY_STRING;
			self.success=EMPTY_STRING;
			self.isWaiting = true;
			productGroupApi.deleteProductGroupInfo(self.dataForm.customerProductGroup.custProductGroupId.toString(),
				function (response) {
					self.isWaiting = false;
					self.error=EMPTY_STRING;
					self.success = response.message;
					self.goToSearchFormAfterDeleting();

				}, function (error) {
					self.isWaiting = false;
					self.callApiError(error)
				});
		}
		/**
		 * go back search form after delete product group successfully.
		 */
		self.goToSearchFormAfterDeleting = function () {
			$rootScope.$broadcast('changeModeAfterDeleting', 'search-mode');
		}
		/**
		 * Get Associated Product of product group
		 */
		self.getAssociatedProduct = function () {
			if(!self.isCreateProductGroup){
				if(self.dataForm.customerProductGroup.productGroupType.productGroupTypeCode != self.dataFormTemp.customerProductGroup.productGroupType.productGroupTypeCode){
					self.isWaitLoadAssociatedProductDetail = true;
				}else{
					self.isWaitLoadAssociatedProductDetail = false;
				}
			}
			self.isWaitLoadAssociatedProduct = true;
			var productGroupId = self.isCreateProductGroup ? '0' : self.dataForm.customerProductGroup.custProductGroupId.toString();
			productGroupApi.getAssociatedProduct({
				productGroupId: productGroupId,
				customerProductGroupCode: self.dataForm.customerProductGroup.productGroupType.productGroupTypeCode
			},
				function (response) {
					self.isWaitLoadAssociatedProduct = false;
					self.dataForm.dataAssociatedProduct = response;
					$rootScope.$broadcast('reloadAssociatedProduct', self.dataForm.dataAssociatedProduct);
					productGroupService.setProductGroupTypeCode(self.dataForm.customerProductGroup.productGroupType.productGroupTypeCode);
				}, function (error) {
					self.isWaitLoadAssociatedProduct = false;
					self.callApiError(error)
				});
		}
		/**
		 * Show error message popup.
		 *
		 * @param message
		 */
		self.showErrorMessageOnPopup = function(message){
			self.errorPopup = message;
			$('#errorMessageModal').modal({ backdrop: 'static', keyboard: true });
		}
		/**
		 * Close popup error
		 */
		self.closeErrorMessagePopup = function(){
			self.clearErrorProductId();
			self.requestFocusToCurrentProductId();
		}
		/**
		 * Show message for conflict product id case.
		 *
		 * @param product the product object.
		 */
		self.showMessageForConflictProductId = function(product){
			if(product !== undefined){
				self.clearProductError = true;
				product.isDisabledUpdate = false;
				var message = ASSOCIATED_PRODUCT_IS_EXIST.replace(PRODUCT_ID_KEY, product.productId);
				message = message.replace(PRODUCT_GROUP_NAME_KEY, self.dataForm.customerProductGroup.custProductGroupName);
				self.showErrorMessageOnPopup(message);
			}

		}
		/**
		 * Focus to the current that holds current product id.
		 */
		self.requestFocusToCurrentProductId = function(){
			$("[id=row"+self.currentAssociatedProductPosition+"]").find(":text").focus();
		}
		/**
		 * Convert the date to string with format: yyyy-MM-dd.
		 * @param date the date object.
		 * @returns {*} string
		 */
		self.convertDateToStringWithYYYYMMDD = function (date) {
			return $filter('date')(date, 'yyyy-MM-dd');
		};
		/**
		 * Compare date1 greate than or equal to date2.
		 *
		 * @param date1 the date.
		 * @param date2 the date.
		 * @returns {boolean} true if the date is greater than or equal to the date 2.
		 */
		self.isDate1GreaterThanOrEqualToDate2 = function (date1, date2) {
			if ((new Date(self.convertDateToStringWithYYYYMMDD(date1)).getTime() >= new Date(self.convertDateToStringWithYYYYMMDD(date2)).getTime())) {
				return true;
			}
			return false;
		};
		/**
		 * Compare the date is greater than date 2 or not.
		 *
		 * @param date1 the date.
		 * @param date2 the date.
		 * @returns {boolean} true if the date1 is greater than date 2 or false.
		 */
		self.isDate1GreaterThanDate2 = function (date1, date2) {
			if ((new Date(self.convertDateToStringWithYYYYMMDD(date1)).getTime() > new Date(self.convertDateToStringWithYYYYMMDD(date2)).getTime())) {
				return true;
			}
			return false;
		};
		/**
		 * show message
		 * @param error
		 * @param success
		 */
		self.showMessage = function(error,success){
			self.error=EMPTY_STRING;
			self.success=EMPTY_STRING;
			if(error!==undefined && error!==EMPTY_STRING){
				self.error = error;
			}else{
				self.success=success;
			}
		}
		/**
		 * Show modal product group change log.
		 */
		self.showProductGroupInfoChangeLog = function(){
			self.initProductGroupInfoLogTable();

			$('#product-group-info-change-log-modal').modal({backdrop: 'static', keyboard: true});
			self.getProductGroupInfoAudit();
			//$('#product-group-info-change-log-modal').modal('show');
		}

		self.getProductGroupInfoAudit = function(){
			self.isWaitingGetProductGroupInfoAudit = true;
			productGroupApi.getProductGroupInfoAudit({
				custProductGroupId: self.dataForm.customerProductGroup.custProductGroupId,
				salesChannel: self.currentTab.saleChannel.id,
				hierarchyContext: self.currentTab.hierCntxtCd

			},
			function (results) {
				self.productGroupInfoAudits = results;
				self.initProductGroupInfoLogTable();
				self.isWaitingGetProductGroupInfoAudit = false;
			}, function (error) {
				self.isWaitingGetProductGroupInfoAudit = false;
				self.callApiError(error)
			});
		}
		/**
		 * Init data product group info change log.
		 */
		self.initProductGroupInfoLogTable = function () {
			$scope.filter = {
				attributeName: '',
			};
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			$scope.productGroupInfoLogTableParams = new ngTableParams({
				page: 1,
				count: 10,
				filter: $scope.filter,
				sorting: $scope.sorting

			}, {
					counts: [],
					data: self.productGroupInfoAudits
				});
		}
		/**
		 * Change sort.
		 */
		self.changeSort = function (){
			if($scope.sorting.changedOn === ORDER_BY_DESC){
				$scope.sorting.changedOn = ORDER_BY_ASC;
			}else {
				$scope.sorting.changedOn = ORDER_BY_DESC;
			}
		}
	}
})();
