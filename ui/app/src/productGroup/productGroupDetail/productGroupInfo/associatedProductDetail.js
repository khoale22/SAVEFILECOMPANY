
/*
 *   associatedProductDetail.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * eCommerce View Component.
 *
 * @author vn87351
 * @since 2.14.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('associatedProductDetail', {
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productGroup/productGroupDetail/productGroupInfo/associatedProductDetail.html',
		bindings: {
			cusProductGroup: '=',
			cusProductGroupOrg:'=',
			seleted: '<',
			currentTab: '<',
			showErrorMessageOnPopup:'&',
			updateProductIdToView:'&',
			getActionCode:'&',
			isNotDuplicateProductIdOrUpc:'&',
			showMessageForConflictProductId:'&',
			requestFocusToCurrentProductId:'&',
			isWaitLoadAssociatedProductDetail:'=',
			showAssociatedProductDetailsByIndex:'&',
			clearProductError:'=',
			currentAssociatedProductPosition:'<',
			isCreateProductGroup: '<',
			setDataAssociatedProducts:'&',
		},
		// The controller that handles our component logic
		controller: AssociatedProductDetail
	});

	AssociatedProductDetail.$inject = ['$rootScope', '$scope', '$filter', 'ngTableParams', 'codeTableApi', 'productGroupApi', 'ProductGroupService',  'ProductSearchService', '$state', 'appConstants', 'DownloadImageService'];

	/**
	 * product group detail- product associated product detail controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @param codeTableApi
	 * @param ngTableParams
	 * @constructor
	 */
	function AssociatedProductDetail($rootScope, $scope, $filter,  ngTableParams, codeTableApi, productGroupApi, productGroupService, productSearchService, $state, appConstants, downloadImageService) {
		/** All CRUD operation controls of product group detail page goes here */

		var self = this;
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;
		/**
		 * Current upc
		 * @type {Long}
		 */
		self.currentUpc = null;
		/**
		 * Search type product for ecommerce view
		 * @type {String}
		 */
		const SEARCH_TYPE = "basicSearch";
		/**
		 * Selection type product for ecommerce view
		 * @type {String}
		 */
		const SELECTION_TYPE = "Product ID";
		/**
		 * Ecommerce view tab
		 * @type {String}
		 */
		const ECOMMERCE_VIEW_TAB = 'ecommerceViewTab';
		/**
		 * Flag for selected all option
		 * @type {Boolean}
		 */
		self.selectedAll = true;
		/**
		 * The list choice option picker switch
		 * @type {Array}
		 */
		self.defaultOptionList = [];
		/**
		 * The list choice option temp
		 * @type {Array}
		 */
		self.choiceOptionsTemp = [];
		/**
		 * The default error message.
		 *
		 * @type {string}
		 */
		const UNKNOWN_ERROR = "An unknown error occurred.";
		/**
		 * Number of request
		 * @type {Number}
		 */
		self.numberOfRequests = 0;
		/**
		 * Number of response
		 * @type {Number}
		 */
		self.numberOfResponses = 0;
		/**
		 * Id  product showing
		 * @type {Number}
		 */
		self.productShowing = null;
		/**
		 * Name field isSelected
		 */
		self.fieldIsSelect = "isSelected";
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
		 * init function
		 */
		self.init = function(){
			if(self.cusProductGroup===undefined || self.cusProductGroup ==null)
				return;
			if(self.cusProductGroup.lstProductDetails == null || self.cusProductGroup.lstProductDetails== undefined){
				self.cusProductGroup.lstProductDetails = [];
				self.cusProductGroup.lstProductDetailsTemp = [];
			}
			//Is exist product id is saved from product group info
			if(self.productShowing !== undefined
				&& self.productShowing != null
				&& self.productShowing !== ''
				&& !isNaN(self.productShowing)){
				self.getDataForDetailProduct();
			}else{
				self.productShowing = null;
			}
		};
		/**
		 * Handle data on change.
		 */
		this.$onChanges= function (){
			if(self.cusProductGroup && self.cusProductGroup.dataAssociatedProduct!=null){
				var rows=self.cusProductGroup.dataAssociatedProduct.rows;
				if(rows[self.currentAssociatedProductPosition] === null
					|| rows[self.currentAssociatedProductPosition].productId === undefined
					|| rows[self.currentAssociatedProductPosition].productId == null
					|| rows[self.currentAssociatedProductPosition].productId == ''){
					productGroupService.getSelectedProductId(null);
					self.productShowing=null;
					self.init();
				}
			}

		}
		/**
		 * watcher scope event for change associated table selected
		 */
		$scope.$on('changeAssociatedProduct', function(event, id, choiceOption, defaultOptionList, isReload) {
			if(isReload || self.productShowing!==id){
				productGroupService.setSelectedProductId(id)
				self.choiceOptionsTemp = choiceOption;
				self.defaultOptionList = defaultOptionList;
				self.productShowing=parseInt(id);
			self.init();
			}
		});
		/**
		 * get data for associated product detail
		 */
		self.getDataForDetailProduct = function () {
			//Get upc for product
			self.getProductPrimaryScanCode();
		};
		/**
		 * Get choice type and choice option.
		 */
		self.getChoice = function () {
			self.isWaitingForResponse = true;
			self.numberOfRequests++;
			productGroupApi.getChoice(
				{
					prodId: self.productShowing,
					productGroupTypeCode: self.cusProductGroup.customerProductGroup.productGroupType.productGroupTypeCode
				},
				function (result) {
					if(result != null){
						self.initChoiceData(result);
						self.checkDataAssociatedProduct();
					}
					self.numberOfResponses++;
					self.stopLoadAssociatedProductDetail();
				}, self.fetchError
			);
		};
		/**
		 * Init choice data
		 */
		self.initChoiceData = function (result) {
			self.choiceExistList = [];
			self.choiceList = result.productGroupChoiceOptions;
			self.loadChoiceData();
			angular.forEach(result.customerProductChoices, function (choiceOption, index) {
				self.choiceExistList.push(choiceOption);
			});
			//Push element have pickerSwitch = true from choice exist list
			var hasChoiceOption = false;
			for (var j = 0; j < self.choiceExistList.length; j++) {
				if(self.choiceExistList[j].productGroupChoiceType.pickerSwitch){
					self.choiceList.splice(0, 0, self.choiceExistList[j]);
					hasChoiceOption = true;
				}
			}
			//set selected item
			if(!hasChoiceOption){
				angular.forEach(self.defaultOptionList, function (choiceOptionSw, index) {
					self.choiceList.splice(0, 0, self.convertToChoiceOption(choiceOptionSw));
				});
				if(self.choiceList.length == self.defaultOptionList.length){
					self.selectedAll = true;
				}else{
					self.selectedAll = false;
				}
			}else{
				self.selectedAll = true;
			for (var i = 0; i < self.choiceList.length; i++) {
				self.choiceList[i][self.fieldIsSelect] = false;
				for (var j = 0; j < self.choiceExistList.length; j++) {
					if(self.choiceList[i].key.choiceOptionCode == self.choiceExistList[j].key.choiceOptionCode
						&& self.choiceList[i].key.choiceTypeCode == self.choiceExistList[j].key.choiceTypeCode){
						self.choiceList[i][self.fieldIsSelect] = true;
							self.choiceList[i].key = angular.copy(self.choiceExistList[j].key);
						}
					}
					if(!self.choiceList[i][self.fieldIsSelect]){
						self.selectedAll = false;
					}
				}
			}
			var productDetails = {
				productId: self.productShowing,
				choices: self.choiceList
			}
			self.cusProductGroup.lstProductDetails[self.currentAssociatedProductPosition] = productDetails;
			self.cusProductGroup.lstProductDetailsTemp[self.currentAssociatedProductPosition] = angular.copy(productDetails);
		};
		/**
		 * Convert data choice option switch
		 */
		self.convertToChoiceOption = function (choiceOptionSw){
			var choiceOption = {
				isSelected : true,
				choiceOption : choiceOptionSw,
				key:{
					choiceOptionCode : choiceOptionSw.key.choiceOptionCode,
					choiceTypeCode :choiceOptionSw.key.choiceTypeCode,
					productGroupTypeCode: self.cusProductGroup.customerProductGroup.productGroupType.productGroupTypeCode
				},
				productGroupChoiceType: {
						pickerSwitch : true,
						key:{
						choiceTypeCode :choiceOptionSw.key.choiceTypeCode,
						productGroupTypeCode: self.cusProductGroup.customerProductGroup.productGroupType.productGroupTypeCode
					}
				}
			}
			return choiceOption;
		}
		/**
		 * Saves the factory data, updates switches to add checks to factories, sets the ng table up.
		 */
		self.loadChoiceData = function(results){
			$scope.filter = {
				key:{
					productGroupTypeCode: undefined
				} ,
			};
			self.choiceOptions  = angular.copy(results);
			self.choiceTableParams = new ngTableParams(
				{
					page: 1,
					count:10,
					filter: $scope.filter
				}, {
					counts: [],
					data: self.choiceList
				}
			);
			self.choiceTableParams.reload();
		};
		/**
		 * Get product master by proId or upc.
		 */
		self.getProductPrimaryScanCode = function () {
			if(self.cusProductGroup.dataAssociatedProduct===undefined
				||self.cusProductGroup.dataAssociatedProduct==null
				|| self.productShowing ==null
				|| self.productShowing === '') {
				return;
			}
			self.isWaitingForResponse = true;
			self.startLoadAssociatedProductDetail();
			productGroupApi.getProductPrimaryScanCode(
				{
					prodId: self.productShowing,
					prodGroupId:self.cusProductGroup.customerProductGroup.custProductGroupId,
					prodGroupTypeCode:self.cusProductGroup.customerProductGroup.productGroupType.productGroupTypeCode,
					choiceOptions: self.choiceOptionsTemp
				},
				function (result) {
					if(result != null){
						self.clearProductError = false;
						self.currentUpc = result.productPrimaryScanCodeId;
						self.productShowing = result.productId;
						self.updateProductIdToView({productId: result.productId, upc: self.currentUpc});
						var rowSelected=self.cusProductGroup.dataAssociatedProduct.rows[self.currentAssociatedProductPosition];

						if(rowSelected.actionCode!==undefined && rowSelected.actionCode===ACTION_ADD){
							//check the message warning or error
							var product= {productId: result.productId};
							if(!self.isNotDuplicateProductIdOrUpc({product:product})){
								self.clearProductError=true;
								self.showMessageForConflictProductId({productId:result.productId});
							}else if (!angular.equals({}, result.errorMessage)) {
								var keyMessage = Object.keys(result.errorMessage);
								for (var idMessage = 0; idMessage < keyMessage.length; idMessage++) {
									if (keyMessage[idMessage].indexOf("error")!==-1) {
										self.clearProductError = true;
										break;
									}
								}
								self.showErrorMessageOnPopup({message: result.errorMessage[keyMessage[0]]});
							}
						}
						if (!self.clearProductError  ) {
							var productData= self.cusProductGroup.dataAssociatedProduct.rows[self.currentAssociatedProductPosition];
							productData.isDisabledUpdate = true;
							productData.productId=result.productId;
							// Update product  id into the list of associated product.
							self.numberOfResponses++;
						//Get brand, display name, size, romancy copy after get upc
							self.getPrimaryImageForProduct();
							if (self.cusProductGroup.customerProductGroup.productGroupType !== null
								&& self.cusProductGroup.customerProductGroup.productGroupType !== undefined) {
								self.getChoice();
							}
						self.getBrandInformation();
						self.getDisplayNameInformation();
						self.getSizeInformation();
						self.loadProductDescription();
						} else {
							self.currentUpc = null;
							self.productShowing = null;
							self.numberOfResponses = 0;
							self.numberOfRequests = 0;
							self.stopLoadAssociatedProductDetail();
							self.isWaitingForResponse = false;
							var productDuplicate= self.cusProductGroup.dataAssociatedProduct.rows[self.currentAssociatedProductPosition];
							productDuplicate.isDisabledUpdate = false;
							productDuplicate.productId='';

						}
					}
				}, self.fetchError
			);
		};
		/**
		 * call api error and throw message
		 * @param error
		 */
		self.callApiAssociatedProductError = function (error) {
			self.numberOfResponses++;
			self.stopLoadAssociatedProductDetail();
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
			self.clearProductError = true;
			self.showErrorMessageOnPopup({message: self.error});
		};
		/**
		 * response for update associated product success
		 * @param response
		 */
		self.updateAssociatedProductSuccess = function (response) {
			angular.forEach(self.cusProductGroup.dataAssociatedProduct.rows,function (value) {
				if ( value.productId !== undefined && value.productId != null && value.productId !=='') {
					value.actionCode='';
				}
			});
			self.cusProductGroup.lstProductDetailsTemp[self.currentAssociatedProductPosition] = angular.copy(self.cusProductGroup.lstProductDetails[self.currentAssociatedProductPosition]);
			self.cusProductGroupOrg = angular.copy(self.cusProductGroup);
			self.numberOfResponses++;
			self.stopLoadAssociatedProductDetail();
			self.success = response.message;
		};
		/**
		 * save associated product
		 * @param data
		 */
		self.saveAssociatedProduct = function(){
			productGroupApi.updateProductGroupInfo(self.setDataAssociatedProducts(),self.updateAssociatedProductSuccess, self.callApiAssociatedProductError);
		};
		/**
		 * check associated product
		 */
		self.checkDataAssociatedProduct = function(){
			if(!self.isCreateProductGroup){
				var dataSave=[];
				angular.forEach(self.cusProductGroup.dataAssociatedProduct.rows,function (value) {
					if (value.actionCode===ACTION_ADD  && value.productId != null && value.productId != undefined && value.productId !='') {
						dataSave.push(angular.copy(value));
					}
				});
				if(dataSave.length!=0){
					self.numberOfRequests+=1;
					self.isWaitingForResponse = true;
					self.saveAssociatedProduct();
					return;
				}
			}
		}

		/**
		 * Get brand information.
		 */
		self.getBrandInformation = function () {
			self.isWaitingForResponse = true;
			productGroupApi.getBrandInformation(
				{
					productId: self.productShowing,
					upc: self.currentUpc,
					saleChannel:  self.currentTab.saleChannel.id
				},
				function (result) {
					if(result != null){
						self.brand = angular.copy(result.brand);
						self.brandOrg = angular.copy(self.brand);
					}
					self.numberOfResponses++;
					self.stopLoadAssociatedProductDetail();
				}, self.fetchError
			);
		};
		/**
		 * Get display name information.
		 */
		self.getDisplayNameInformation = function () {
			self.isWaitingForResponse = true;
			productGroupApi.getDisplayNameInformation(
				{
					productId: self.productShowing,
					upc: self.currentUpc,
					saleChannel:  self.currentTab.saleChannel.id
				},
				function (result) {
					if(result != null){
						self.displayName = angular.copy(result.displayName);
						self.displayNameOrg = angular.copy(self.displayName);
					}
					self.numberOfResponses++;
					self.stopLoadAssociatedProductDetail();
				}, self.fetchError
			);
		};

		/**
		 * Get product description.
		 */
		self.loadProductDescription = function () {
			self.isWaitingForResponse = true;
			productGroupApi.findProductDescription(
				{
					productId: self.productShowing,
					upc: self.currentUpc,
				},
				//success case
				function (result) {
					if(result != null) {
						self.romanceCopy = result;
						if(self.romanceCopy.content != null){
							self.romanceCopy.content = he.decode(self.romanceCopy.content);
						}
					}
					self.numberOfResponses++;
					self.stopLoadAssociatedProductDetail();
				}, self.fetchError);
		};
		/**
		 * Get size information.
		 */
		self.getSizeInformation = function () {
			self.isWaitingForResponse = true;
			productGroupApi.getSizeInformation(
				{
					productId: self.productShowing,
					upc: self.currentUpc,
					saleChannel:  self.currentTab.saleChannel.id
				},
				function (result) {
					if(result != null){
						self.size = angular.copy(result.size);
						self.sizeOrg = angular.copy(self.size);
					}
					self.numberOfResponses++;
					self.stopLoadAssociatedProductDetail();
				}, self.fetchError
			);
		};
		/**
		 * Get general information when change tab.
		 */
		self.getPrimaryImageForProduct = function () {
			self.isWaitingForResponse = true;
			productGroupApi.getPrimaryImageForProduct(
				{
					productId: self.productShowing
				},
				//success case
				function (results) {
					//build data format image from byte[]
					if(results.imagePrimary != null){
						self.imagePrimary = "data:image/jpg;base64,"+results.imagePrimary;
					}else{
						self.imagePrimary = null;
					}
					self.numberOfResponses++;
					self.stopLoadAssociatedProductDetail();
				}
				//error case
				, self.fetchError
			);
		};
		/**
		 * Show full primary image, and allow user download
		 */
		self.showFullPrimaryImage  = function () {
			self.imageBytes = '';
			$('#imageModal').modal({backdrop: 'static', keyboard: true});
			if(self.imagePrimary != null){
				self.imageBytes = self.imagePrimary;
			}else{
				self.imageBytes = ' ';
				$('#imageModal').modal("hide");
			}
		};
		/**
		 * Backup search condition product  for ecommerce View
		 */
		self.navigateToEcommerView = function() {
			//Set search condition
			productSearchService.setSearchType(SEARCH_TYPE);
			productSearchService.setSelectionType(SELECTION_TYPE);
			productSearchService.setSearchSelection(self.productShowing);
			//Set selected tab is ecommerceViewTab tab to navigate ecommerce view page
			productSearchService.setSelectedTab(ECOMMERCE_VIEW_TAB);
			productGroupService.setProductGroupId(self.cusProductGroup.customerProductGroup.custProductGroupId);
			//Set from page navigated to
			productSearchService.setFromPage(appConstants.PRODUCT_GROUP);
			productSearchService.setDisableReturnToList(false);
			$state.go(appConstants.HOME_STATE);
		};
		/**
		 * is active for row selected
		 */
		$scope.isActive = function(item) {
			return $scope.selectedItem === item;
		};
		/**
		 *select row on choice table
		 */
		self.selectRow = function(choice){
			$scope.selectedItem = choice;
		};
		/**
		 * Select all on checkboxes
		 */
		self.selectAll  = function () {
			self.selectedAll = !self.selectedAll;
			angular.forEach(self.choiceList, function (choice,index) {
				if(!choice.productGroupChoiceType.pickerSwitch){
					choice.isSelected = self.selectedAll;
				}
			});
		};
		/**
		 * Handle flag select all
		 */
		self.setCheckAll = function () {
			var checkAll =  true;
			angular.forEach(self.choiceList, function (choice,index) {
				if(!choice.isSelected){
					checkAll = false;
				}
			});
			self.selectedAll = checkAll;
		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.showErrorMessageOnPopup({message: self.getErrorMessage(error)});
			self.productShowing = null;
			self.currentUpc = null;
		};
		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			self.isWaitingForResponse = false;
			self.numberOfRequests = 0;
			self.numberOfResponses = 0;
			self.isWaitLoadAssociatedProductDetail = false;
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return UNKNOWN_ERROR;
			}
		};
		/**
		 * Start loading for load associated product detail.
		 */
		self.startLoadAssociatedProductDetail = function(){
			self.numberOfResponses = 0;
			self.numberOfRequests = 6;
			self.isWaitLoadAssociatedProductDetail = true;
		}
		/**
		 * Stop loading when numberOfRequests equals numberOfResponses.
		 */
		self.stopLoadAssociatedProductDetail = function(){
			if(self.numberOfRequests == self.numberOfResponses){
				self.isWaitLoadAssociatedProductDetail = false;
				self.isWaitingForResponse = false;
			}
		}
		/**
		 * Download image.
		 */
		self.downloadImage = function(){
			if(self.imageBytes != null){
				downloadImageService.download(self.imageBytes, 'jpg');
			}
		}
	}
})();
