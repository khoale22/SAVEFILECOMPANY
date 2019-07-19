
/*
 *   eCommerceViewComponent.js
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
 * @since 2.15.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('productGroupeCommerceView', {
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productGroup/productGroupDetail/eCommerceView/eCommerceView.html',
		bindings: {
			productGroupSelected: '<',
			isList: '<'
		},
		// The controller that handles our component logic
		controller: ProductGroupeCommerceViewController
	});

	ProductGroupeCommerceViewController.$inject = ['$rootScope', '$scope', '$filter', 'ngTableParams', 'imageInfoApi', 'productGroupApi','productGroupConstant' , '$state', 'appConstants', 'customHierarchyService', 'ProductGroupService', 'DownloadImageService'];

	/**
	 * Product Group Detail - eCommerce View component's controller definition.
	 * @param $rootScope
	 * @param $scope scope of the product group image component.
	 * @param $filter
	 * @param ngTableParams
	 * @param imageInfoApi
	 * @param productGroupApi
	 * @constructor
	 */
	function ProductGroupeCommerceViewController($rootScope, $scope, $filter, ngTableParams, imageInfoApi, productGroupApi,productGroupConstant , $state, appConstants, customHierarchyService, productGroupService, downloadImageService) {

		var self = this;

		self.isWaiting = false;
		self.domainTabBarDataProvider = [];
		self.imageFormat='';
		/**
		 * Status on live of component.
		 * @type {boolean}
		 */
		self.onLive = true;
		/**
		 * store image alternates
		 * @type {Array}
		 */
		self.imageAlternates = [];
		/**
		 * primary image for e-commerce view
		 */
		self.primaryImage ;
		/**
		 * Title for deleting popup
		 * @type {String}
		 */
		self.CONFIRM_TITLE= 'Delete Product Group';
		/**
		 * Confirm message for deleting popup
		 * @type {String}
		 */
		self.CONFIRM_MESSAGE = "Do you really want to delete the selected product group? ";
		/**
		 * Backup productGroupChoiceTypes
		 * @type {Array}
		 */
		self.productGroupChoiceTypesOrg = [];
		/**
		 * Backup custProductGroupName
		 * @type {String}
		 */
		self.custProductGroupNameOrg = null;
		/**
		 * Backup custProductGroupDescription
		 * @type {String}
		 */
		self.custProductGroupDescriptionOrg = null;
		/**
		 * Backup custProductGroupDescriptionLong
		 * @type {String}
		 */
		self.custProductGroupDescriptionLongOrg = null;

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

		const THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
		/**
		 * message when product group name is empty
		 * @type {String}
		 */
		const PRODUCT_GROUP_NAME_EMPTY = "- Product Group Name is a mandatory field.";
		/**
		 * Customer hierarchy primary path
		 * @type {String}
		 */
		self.customerHierarchyPrimaryPath = '';
		/**
		 * message when product group name is empty
		 * @type {String}
		 */
		self.custProductGroupName = '';
		self.disableReturnToList = true;
		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			self.onLive = false;
		};
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
					salesChannel: saleChannels[0],
					hierCntxtCd: "CUST"
				},
				{
					displayName: "Hebtoyou",
					id: "Hebtoyou",
					logo: "images/logo_hebtoyou.png",
					salesChannel: saleChannels[2],
					hierCntxtCd: "HEBTO"
				},
				{
					displayName: "Blooms",
					id: "Blooms",
					logo: "images/logo_blooms.jpg",
					salesChannel: saleChannels[3],
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
				self.error = '';
				self.success = '';
			} else {
				self.isChangedTabAndSaveData = false;
			}
			self.loadProductGroupECommerceView();
		}

		self.init = function(){
			if(!customHierarchyService.getDisableReturnToList()){
				customHierarchyService.setDisableReturnToList(true);
				productGroupService.setCustomerHierarchyId(null);
			}
			self.isWaiting = true;
			// if(productGroupService.getNavFromProdGrpTypePageFlag()){
			// 	self.isList = true;
			// }
			if(self.isList || productGroupService.getNavFromProdGrpTypePageFlag()
				|| productGroupService.getNavigateFromCustomerHierPage() ||  productGroupService.isBrowserAll()){
				self.disableReturnToList = false;
			}
			productGroupApi.findAllSaleChanel(
				//success
				function (results) {
					self.isWaiting = false;
					angular.forEach(results, function (value) {
						value.id = value.id + "   ";
					});
					self.salesChannels = angular.copy(results);
					self.buildECommerceViewTabBySaleChanel(self.salesChannels);
					self.loadProductGroupECommerceView();
				}
				, self.callApiError
			);
		}
		self.loadProductGroupECommerceView = function(){
			if(self.currentTab==null || self.currentTab.salesChannel==null){
				return;
			}
			self.isWaiting = true;
			productGroupApi.getEcommerceView({
					'salesChannel': self.currentTab.salesChannel.id,
					'hierCntxtCd': self.currentTab.hierCntxtCd,
					'productGroupId':self.productGroupSelected
				},
				self.callApiSuccess, self.callApiError);
		}
		/**
		 * watcher for reload form event
		 */
		$scope.$on('reloadProductInfo', function(event, args) {
			self.productGroupSelected=args;
			self.loadProductGroupECommerceView();
		});
		/**
		 * Processing if call api success
		 * @param results
		 */
		self.callApiSuccess = function(response){
			self.isWaiting = false;
			self.dataForm=response;
			self.dataForm.customerProductGroup.custProductGroupName = self.dataForm.customerProductGroup.custProductGroupName.trim();
			self.dataForm.customerProductGroup.custProductGroupDescription = self.dataForm.customerProductGroup.custProductGroupDescription.trim();
			self.dataForm.customerProductGroup.custProductGroupDescriptionLong = self.dataForm.customerProductGroup.custProductGroupDescriptionLong.trim();
			self.customerHierarchyPrimaryPath = self.dataForm.primaryPath;
			if(self.dataForm.customerProductGroup !== null){
				self.backupData();
			}
			self.onLive=true;
			self.primaryImage=self.dataForm.customerProductGroup.primaryImage;
			self.filterAlternatesImage(self.dataForm.customerProductGroup.imageData);
			self.findPrimaryImageByUri();
			self.findImageAlternatesByUri(0);
			self.getImageChoiceOption();
		};
		/**
		 * filter alternate image
		 * @param data
		 */
		self.filterAlternatesImage = function(data){
			self.imageAlternates=[];
			angular.forEach(data,function (value) {
				if(value.imagePriorityCode== 'A'&& value.activeOnline){
					self.imageAlternates.push(value);
				}
			})
		}
		/**
		 * call api error and throw message
		 * @param error
		 */
		self.callApiError = function(error){
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
		 * find primary image
		 */
		self.findPrimaryImageByUri = function () {
			if (self.primaryImage!=null){
				var uritxt=self.primaryImage.uriText;
				if(uritxt===undefined || uritxt==null || uritxt=='')
					uritxt=self.primaryImage.imageURI;
				productGroupApi.getImageData({
						imageUri: uritxt
					},
					function (results) {
						if (results.data != null && results.data != '') {
							self.primaryImage['image'] = "data:image/jpg;base64," + results.data;
						} else {
							self.primaryImage['image']= null;
						}
					},
					//error
					self.fetchError
				);
			}
		}
		/**
		 * Find all the list of choice option information.
		 */
		self.findImageAlternatesByUri = function (index) {

			if (self.imageAlternates && self.imageAlternates.length > index) {
				var uritxt=self.imageAlternates[index].uriText;
				if(uritxt===undefined || uritxt==null || uritxt=='')
					uritxt=self.imageAlternates[index].imageURI;
				productGroupApi.getImageData({
						imageUri: uritxt
					},
					function (results) {
						if(self.imageAlternates[index]===undefined || self.imageAlternates[index]==null){
							return;
						}
						if (results.data != null && results.data != '') {
							self.imageAlternates[index]["image"] = "data:image/jpg;base64," + results.data;
						} else {
							self.imageAlternates[index]["image"] = null;
						}
						if (self.onLive) {
							self.findImageAlternatesByUri(index + 1);
						}
					},
					//error
					self.fetchError
				);
			}
		};
		self.lstImages = [];
		/**
		 * Find all the list of choice option information.
		 */
		self.getImageChoiceOption = function () {
			if (self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes){
				angular.forEach(self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes,function (value) {
					angular.forEach(value.productGroupChoiceOptions,function (valueOpt) {
						if(valueOpt.choiceOption.imageMetaDataChoiceOption!=null)
							self.lstImages.push(valueOpt.choiceOption.imageMetaDataChoiceOption);
					});
				})

			}
			self.findImageChoiceOptionByUri(0);
		};
		/**
		 * set data image choice option information.
		 */
		self.setImageChoiceOption = function (results) {
			if (self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes){
				angular.forEach(self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes,function (value) {
					angular.forEach(value.productGroupChoiceOptions,function (valueOpt) {
						if(valueOpt.choiceOption.imageMetaDataChoiceOption!=null &&
							valueOpt.choiceOption.imageMetaDataChoiceOption.uriText==results.message){
							if (results.data != null && results.data != '') {
								valueOpt.choiceOption.imageMetaDataChoiceOption["image"] = "data:image/jpg;base64," + results.data;
							} else {
								valueOpt.choiceOption.imageMetaDataChoiceOption["image"] = null;
							}
						}

					});
				})
			}
		}
		self.findImageChoiceOptionByUri = function (index) {
			if (self.lstImages!=null && self.lstImages.length > index){
				productGroupApi.getImageData({
						imageUri: self.lstImages[index].uriText
					},
					function (results) {
						self.setImageChoiceOption(results);
						if (self.onLive) {
							self.findImageChoiceOptionByUri(index+1);
						}
					},
					//error
					self.fetchError
				);
			}
		}
		/**
		 * Show full primary image, and allow user download
		 */
		self.showFullImage  = function (img) {
			self.imageBytes =img;
			// self.imageFormat =''
			$('#imageModal').modal({backdrop: 'static', keyboard: true});
			if(self.imageBytes == null){
				self.imageBytes = ' ';
				$('#imageModal').modal("hide");
			}
		};
		/**
		 * go back search form.
		 */
		self.goToSearchForm = function(){
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
			}else if(productGroupService.getNavFromProdGrpTypePageFlag()){
				customHierarchyService.setReturnToListFlag(true);
				customHierarchyService.setDisableReturnToList(true);
				$state.go(appConstants.CODE_TABLE);
			} else{
				$rootScope.$broadcast('changeMode','search-mode');
			}
		}
		/**
		 * call the search component to search product automatically
		 */
		self.goToCustomerHierarchy = function(){
			$rootScope.$broadcast('goToCustomerHierarchy');
		}
		self.getProductImageByPickerOption = function(){
			self.primaryImage={};
			self.imageAlternates=[];

			var lstOptSelect=[];
			var selected = false;
			angular.forEach(self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes,function (value) {
				if(value.pickerSwitch) {
					if (value && value.choiceType && value.choiceType.selectedOption){
						selected = true;
						var choiceType = value.choiceType.choiceTypeCode;
						var objData = {};
						objData[choiceType]= value.choiceType.selectedOption
						lstOptSelect.push(objData);

					}else{
						return;
					}
				}
			})
			productGroupApi.getProductImage({
					productGroupId: self.productGroupSelected,
					mapsOption:self.toObject(lstOptSelect),
					salesChannel:self.salesChannels[0].id
				},
				function (results) {
					if(results){
						self.setImageAlternateByImageURI(results);
						angular.forEach(results , function(value){
							if(productGroupConstant.IMAGE_PRIORITY_CD_PRIMARY==value.imagePriorityCode)
								self.primaryImage=value;
						});
						if(self.primaryImage){
							self.findPrimaryImageByUri();
						}
					}
				},
				//error
				self.fetchError
			);

		}
		/**
		 * convert array to object
		 * @param arr
		 * @returns {{}}
		 */
		self.toObject = function (arr) {
			var rv = {};
			for (var i = 0; i < arr.length; ++i){
				var keys=Object.keys(arr[i]);
				for (var j = 0; j < keys.length; j++) {
					var key=keys[j];
					rv[key]=arr[i][key];
				}
			}
			return rv;
		}
		/**
		 * set image alternate
		 * @param data
		 */
		self.setImageAlternateByImageURI = function(data){
			self.imageAlternates=[];
			angular.forEach(data , function(value){
				if(productGroupConstant.IMAGE_PRIORITY_CD_ALTERNATE==value.imagePriorityCode)
					self.imageAlternates.push(value);
			});
			self.findImageAlternatesByUri(0);
		}
		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if(self.imageBytes != null){
				downloadImageService.download(self.imageBytes, self.imageFormat==''?'jpg':self.imageFormat.trim());
			}
		};
		/**
		 * delete product group
		 * @returns
		 */
		self.deleteProductGroup = function () {
			self.isWaiting = true;
			var customerProductGroup = {};
			customerProductGroup.custProductGroupId = self.productGroupSelected;
			productGroupApi.deleteProductGroup(
				customerProductGroup,
				//success case
				function (result) {
					self.isWaiting = false;
					self.goToSearchFormAfterDeleting();
				}
				//error case
				,self.fetchError
			);
		}
		/**
		 * update product group
		 * @returns
		 */
		self.updateProductGroup = function () {
			self.isWaiting = true;
			var customerProductGroup = {};
			customerProductGroup.custProductGroupId = self.productGroupSelected;
			if(self.dataForm !== null && self.dataForm !== undefined
				&& self.dataForm.customerProductGroup !== null && self.dataForm.customerProductGroup !== undefined) {
				customerProductGroup.custProductGroupName = self.dataForm.customerProductGroup.custProductGroupName.trim();
				customerProductGroup.custProductGroupDescription = self.dataForm.customerProductGroup.custProductGroupDescription.trim();
				customerProductGroup.custProductGroupDescriptionLong = self.dataForm.customerProductGroup.custProductGroupDescriptionLong.trim();
			}
			productGroupApi.updateProductGroup(
				customerProductGroup,
				//success case
				function (result) {
					self.isWaiting = false;
					if (self.isChangedTab) {
						self.isChangedTab = false;
						$scope.active = self.currentTabIndexTemp;
					}
					self.loadProductGroupECommerceView();
					self.success = result.message;
					self.isChangedTabAndSaveData = true;
				}
				//error case
				,self.fetchError
			);
		}
		/**
		 * Reset data
		 * @returns
		 */
		self.resetData = function () {
			self.error = '';
			self.success = '';
			if(self.dataForm !== null && self.dataForm !== undefined
				&& self.dataForm.customerProductGroup !== null && self.dataForm.customerProductGroup !== undefined) {
				if(self.dataForm.customerProductGroup.custProductGroupName !== null
					&& self.dataForm.customerProductGroup.custProductGroupName !== undefined)
					self.dataForm.customerProductGroup.custProductGroupName = self.custProductGroupNameOrg;
				if(self.dataForm.customerProductGroup.custProductGroupDescription !== null
					&& self.dataForm.customerProductGroup.custProductGroupDescription !== undefined)
					self.dataForm.customerProductGroup.custProductGroupDescription = self.custProductGroupDescriptionOrg;
				if(self.dataForm.customerProductGroup.custProductGroupDescriptionLong !== null
					&& self.dataForm.customerProductGroup.custProductGroupDescriptionLong !== undefined)
					self.dataForm.customerProductGroup.custProductGroupDescriptionLong = self.custProductGroupDescriptionLongOrg;
				if(self.dataForm.customerProductGroup.productGroupType !== null
					&& self.dataForm.customerProductGroup.productGroupType !== undefined
					&& self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes !== null
					&& self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes !== undefined)
					self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes = angular.copy(self.productGroupChoiceTypesOrg);
			}
		}
		/**
		 * Check data change?
		 *
		 * @returns {boolean}
		 */
		self.isDataChanged = function () {
			var isChanged = false;
			if(self.dataForm !== null && self.dataForm !== undefined
				&& self.dataForm.customerProductGroup !== null && self.dataForm.customerProductGroup !== undefined){
				if (self.custProductGroupNameOrg !== self.dataForm.customerProductGroup.custProductGroupName
					|| self.custProductGroupDescriptionOrg !== self.dataForm.customerProductGroup.custProductGroupDescription
					|| self.custProductGroupDescriptionLongOrg !== self.dataForm.customerProductGroup.custProductGroupDescriptionLong) {
					isChanged = true;
				}
			}
			$rootScope.contentChangedFlag = isChanged;
			return isChanged;
		};
		/**
		 * Update product group
		 */
		self.handleForUpdatingProductGroup = function () {
			self.error = '';
			self.success = '';
			if(self.dataForm !== null && self.dataForm !== undefined
				&& self.dataForm.customerProductGroup !== null && self.dataForm.customerProductGroup !== undefined) {
				self.custProductGroupName = self.dataForm.customerProductGroup.custProductGroupName;

			}
			if(self.custProductGroupName === null || self.custProductGroupName == ''){
				self.error = PRODUCT_GROUP_NAME_EMPTY;
			}
			else if (self.hasDataChanged()) {
				self.updateProductGroup();
			} else {
				// Show message.
				self.error = THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
			}

		};
		/**
		 * Request delete the list of choice option.
		 */
		self.handleForDeletingProductGroup = function () {
			self.error = '';
			self.success = '';
			$('#confirmDeletingModal').modal({backdrop: 'static', keyboard: true});
		};
		/**
		 * Confirmed to delete/update change data
		 */
		self.confirmForDeletingProductGroup = function () {
			$('#confirmDeletingModal').modal("hide");
			self.deleteProductGroup();
		}
		/**
		 * go back search form after delete product group successfully.
		 */
		self.goToSearchFormAfterDeleting = function(){
			$rootScope.$broadcast('changeModeAfterDeleting','search-mode');
		}

		/**
		 * No confirm to delete/update change data
		 */
		self.noConfirmForDeletingProductGroup = function () {
			$('#confirmDeletingModal').modal("hide");
		};
		/**
		 * Backup data
		 */
		self.backupData = function(){
			self.custProductGroupNameOrg = self.dataForm.customerProductGroup.custProductGroupName;
			self.custProductGroupDescriptionOrg = self.dataForm.customerProductGroup.custProductGroupDescription;
			self.custProductGroupDescriptionLongOrg = self.dataForm.customerProductGroup.custProductGroupDescriptionLong;
			self.productGroupChoiceTypesOrg = angular.copy(self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes)

		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = (error.data.message);
				} else if (error.data.error) {
					self.error = (error.data.error);
				} else {
					self.error = error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Request delete the list of choice option.
		 */
		self.handleForDeletingProductGroup = function () {
			self.error = '';
			self.success = '';
			$('#confirmDeletingModal').modal({backdrop: 'static', keyboard: true});
		};
		/**
		 * Confirmed to delete/update change data
		 */
		self.confirmForDeletingProductGroup = function () {
			$('#confirmDeletingModal').modal("hide");
			self.deleteProductGroup();
		}
		/**
		 * go back search form after delete product group successfully.
		 */
		self.goToSearchFormAfterDeleting = function(){
			$rootScope.$broadcast('changeModeAfterDeleting','search-mode');
		}

		/**
		 * No confirm to delete/update change data
		 */
		self.noConfirmForDeletingProductGroup = function () {
			$('#confirmDeletingModal').modal("hide");
		};
		/**
		 * Backup data
		 */
		self.backupData = function(){
			self.custProductGroupNameOrg = self.dataForm.customerProductGroup.custProductGroupName;
			self.custProductGroupDescriptionOrg = self.dataForm.customerProductGroup.custProductGroupDescription;
			self.custProductGroupDescriptionLongOrg = self.dataForm.customerProductGroup.custProductGroupDescriptionLong;
			self.productGroupChoiceTypesOrg = angular.copy(self.dataForm.customerProductGroup.productGroupType.productGroupChoiceTypes)

		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = (error.data.message);
				} else if (error.data.error) {
					self.error = (error.data.error);
				} else {
					self.error = error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Check data change?
		 *
		 * @returns {boolean}
		 */
		self.hasDataChanged = function () {
			var isChanged = false;
			if(self.dataForm !== null && self.dataForm !== undefined
				&& self.dataForm.customerProductGroup !== null && self.dataForm.customerProductGroup !== undefined){
				if (self.custProductGroupNameOrg !== self.dataForm.customerProductGroup.custProductGroupName
					|| self.custProductGroupDescriptionOrg !== self.dataForm.customerProductGroup.custProductGroupDescription
					|| self.custProductGroupDescriptionLongOrg !== self.dataForm.customerProductGroup.custProductGroupDescriptionLong) {
					isChanged = true;
				}
			}
			return isChanged;
		};

		/**
		 * Handle change tab view.
		 */
		self.handleChangeTab = function (event, index) {
			self.currentTabIndexTemp = index;
			if (self.hasDataChanged()) {
				if (event !== undefined) {
					self.isChangedTab = true;
					event.preventDefault();
					self.showConfirmModal("Confirmation", "Unsaved data will be lost. Do you want to save the changes before continuing?");
				}
			} else{
				self.error = '';
				self.success = '';
			}
		};

		/**
		 * Show common modal.
		 */
		self.showConfirmModal = function (title, content) {
			self.commonConfirmModalTitle = title;
			self.commonConfirmModalContent = content;
			$('#confirm-change-tab-modal').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Reset data and change tab view when click change tab.
		 */
		self.resetDataAndChangeTab = function () {
			$scope.active = self.currentTabIndexTemp;
			self.resetData();
			self.isChangedTab = false;
		}
	}
})();
