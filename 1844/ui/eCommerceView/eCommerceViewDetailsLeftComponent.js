/*
 *   eCommerceViewDetailsComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Product -> eCommerce View page component -> eCommerce View details left panel.
 *
 * @author vn70516
 * @since 2.0.14
 */
(function () {

	angular.module('productMaintenanceUiApp').component('eCommerceViewDetailsLeft', {
		bindings: {
			currentTab: '<',
			productMaster: '<',
			eCommerceViewDetails: '=',
			stopWaitingLoading: '&',
			changeSpellCheckStatus: '&',
			getECommerceViewDataSource:'&',
			isEditText: '=',
			callbackfunctionAfterSpellCheck:'='
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewDetailsLeft.html',
		// The controller that handles our component logic
		controller: ECommerceViewDetailsLeftController
	});

	ECommerceViewDetailsLeftController.$inject = ['ECommerceViewApi', '$scope', '$timeout','$filter', '$sce', 'VocabularyService', 'ngTableParams', 'DownloadImageService'];


	function ECommerceViewDetailsLeftController(eCommerceViewApi, $scope, $timeout, $filter, $sce, vocabularyService, ngTableParams, downloadImageService) {

		var self = this;
		/**
		 * Reload eCommerce view key.
		 * @type {string}
		 */
		self.RELOAD_ECOMMERCE_VIEW = 'reloadECommerceView';
		/**
		 * Reset eCommerce view.
		 * @type {string}
		 */
		self.RESET_ECOMMERCE_VIEW = 'resetECommerceView';
		/**
		 * Reload after save popup.
		 * @type {string}
		 */
		self.RELOAD_AFTER_SAVE_POPUP = 'reloadAfterSavePopup';
		/**
		 * Validate warning eCommerce view key.
		 * @type {string}
		 */
		self.VALIDATE_WARNING = 'validateWarning';
		/**
		 * The id of Google tab.
		 */
		const GOOGLE_TAB_ID = 'Google';
        /**
         * The id of favor tab
         * @type {string}
         */
        const FAVOR_TAB_ID = 'Favor';
		/**
		 * Status on live of component.
		 * @type {boolean}
		 */
		self.onLive = true;

		/**
		 * Set mode for brand field.
		 * @type {boolean}
		 */
		self.brandEditMode = false;

		/**
		 * Set mode for display name field.
		 * @type {boolean}
		 */
		self.displayNameEditMode = false;

		/**
		 * Set mode for size field.
		 * @type {boolean}
		 */
		self.sizeEditMode = false;

		/**
		 * Returns convertDate(date) function from higher scope.
		 * @type {function}
		 */
		self.convertDate = $scope.$parent.$parent.$parent.$parent.convertDate;

		/**
		 * Define trust as HTML copy from $sce to use in ui.
		 */
		self.trustAsHtml = $sce.trustAsHtml;

		/**
		 * Current image format.
		 * @type {string}
		 */
		self.imageFormat = '';

		/**
		 * Current image loading.
		 * @type {string}
		 */
		self.imageBytes = '';

		/**
		 * Max length of textarea input.
		 *
		 * @type {number}
		 */
		self.MAX_LENGTH = 10000;

		/**
		 * Brand attribute id const
		 * @type {number}
		 */
		self.BRAND_ATTRIBUTE_ID = 1672;

		/**
		 * Size attribute id const
		 * @type {number}
		 */
		self.SIZE_ATTRIBUTE_ID = 1667;

		/**
		 * Display name attribute id const
		 * @type {number}
		 */
		self.DISPLAY_NAME_ATTRIBUTE_ID = 1664;

		/**
		 * Flag to show type of format brand field.
		 * @type {boolean}
		 */
		self.htmlShowBrand = false;

		/**
		 * Flag to show type of format display name field.
		 * @type {boolean}
		 */
		self.htmlShowDispName = false;

		/**
		 * HTML Tab Pressing flag.
		 * @type {boolean}
		 */
		self.htmlTabPressing = false;

		/**
		 * System source id for edit mode.
		 *
		 * @type {number}
		 */
		const SRC_SYS_ID_EDIT = 4;
		const EDIT_BRAND_PHY_ATTR_ID = 1642;
		const EDIT_DISPLAY_NAME_PHY_ATTR_ID = 1601;
		const EDIT_SIZE_PHY_ATTR_ID = 1636;
        /**
         * PDP Template ID is mandatory error message
         *
         * @type {String}
         */
        const PDP_TEMPLATE_ID_IS_MANDATORY_ERROR = "PDP Template ID is mandatory.";
        /**
         * Size is mandatory error message
         *
         * @type {String}
         */
        const SIZE_IS_MANDATORY_ERROR = "Size is mandatory.";
        /**
         * Brand is mandatory error message
         *
         * @type {String}
         */
        const BRAND_IS_MANDATORY_ERROR = "Brand is mandatory.";
        /**
         * Display name is mandatory error message
         *
         * @type {String}
         */
        const DISPLAY_NAME_IS_MANDATORY_ERROR = "Display Name is mandatory.";
        /**
         * Customer hierarchy assignment is mandatory error message
         *
         * @type {String}
         */
        const CUSTOMER_HIERARCHY_IS_MANDATORY_ERROR = "Customer hierarchy assignment is mandatory.";
        /**
         * A primary image that is active online is mandatory error message
         *
         * @type {String}
         */
        const PRIMARY_IMAGE_IS_MANDATORY_ERROR = "A primary image that is active online is mandatory.";

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			self.onLive = false;
		};

		/**
		 * Component will reload the kits data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
			if (self.currentTab != undefined && self.currentTab != null && self.currentTab != '' && self.currentTab.id != undefined && self.eCommerceViewDetails != null && self.eCommerceViewDetails != undefined) {
				self.onLive = false;
				self.onChangeData();
			}
		};

		/**
		 * Get general information when change tab.
		 */
		self.onChangeData = function () {
			//test
            /*self.getECommerceViewDataSource({
                productId: self.productMaster.prodId,
                scanCodeId: self.productMaster.productPrimaryScanCodeId,
                attributeId: attributeId,
                salesChannel: self.currentTab.saleChannel.id,
                headerTitle: headerTitle,
                typeSource: 'type1'});*/


			self.stopWaitingLoading({status: true});
			eCommerceViewApi.getMoreECommerceViewInformationByTab(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId,
					commodity: self.productMaster.commodityCode,
					saleChannel: self.currentTab.saleChannel.id,
					hierCntxtCd: self.currentTab.hierCntxtCd
				},
				//success case
				function (results) {
					//build data format image from byte[]
					if(results.imagePrimary != null && results.imagePrimary != ''){
						self.eCommerceViewDetails.imagePrimary = "data:image/jpg;base64,"+results.imagePrimary;
						if(results.imagePrimaryFormat != null && results.imagePrimaryFormat != '') {
							self.eCommerceViewDetails.imagePrimaryFormat = results.imagePrimaryFormat;
						}
					}else{
						self.eCommerceViewDetails.imagePrimary = null;
					}

					//set more eCommerce view information to current eCommerce view.
					self.brandContent = '';
					self.brandContentOrg = '';
					self.displayNameContent = '';
					self.displayNameContentOrg = '';
					self.setMoreECommerceViewInformationForCurrentECommerceViewDetail(results);
					// Original data.
					self.backupOriginalData();
					//Check product is publish
					//self.eCommerceViewDetails["isPublish"] = self.eCommerceViewDetails.showOnSite;
					//build data for date picker
					self.buildDataForFulfillmentDatePicker(self.eCommerceViewDetails);
					self.buildDataForShowOnSiteDatePicker(self.eCommerceViewDetails);

					//validation
					self.validationECommerceView();

					//find image alternates
					self.onLive = true;
					self.findImageAlternatesByUri(0);

					//stop loading waiting
					self.stopWaitingLoading({status: false});

					//validation spell check of Brand
					self.validateSpellCheckBrand();
					//validation spell check of Display Name
					self.validateSpellCheckDisplayName();
					self.setBrandEditMode();
					self.setDisplayNameEditMode();
					self.setSizeEditMode();
				}
				//error case
				, self.fetchError
			);
		};
		/**
		 * Get brand information.
		 */
		self.getBrandInformation = function () {
			self.isWaitingForResponse = true;
			eCommerceViewApi.getBrandInformation(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId,
					saleChannel: self.currentTab.saleChannel.id
				},
				function (result) {
					self.isWaitingForResponse = false;
					if(result != null){
						self.eCommerceViewDetails.brand = self.reformatContent(result.brand);
						self.eCommerceViewDetails.brandOrg = angular.copy(self.eCommerceViewDetails.brand);
						self.setBrandEditMode();
						//validation spell check of Brand
						self.validateSpellCheckBrand();
					}
					//validation
					self.validationECommerceView();
				}, self.fetchError
			);
		};
        /**
         * Reformat the content field, if it is null then set it to empty. Or if it is not null then trim space.
         * @param data
         * @returns {*}
         */
		self.reformatContent = function(data) {
			var varData = angular.copy(data);
			if(varData){
				if(varData.content === null){
					varData.content = "";
				}else{
					varData.content = angular.copy(varData.content.trim());
				}
			}
			return varData;
		};
		/**
		 * Get display name information.
		 */
		self.getDisplayNameInformation = function () {
			self.isWaitingForResponse = true;
			eCommerceViewApi.getDisplayNameInformation(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId,
					saleChannel: self.currentTab.saleChannel.id
				},
				function (result) {
					self.isWaitingForResponse = false;
					if(result != null){
						self.eCommerceViewDetails.displayName = self.reformatContent(result.displayName);
						self.eCommerceViewDetails.displayNameOrg = angular.copy(self.eCommerceViewDetails.displayName);
						self.setDisplayNameEditMode();
						//validation spell check of Display Name
						self.validateSpellCheckDisplayName();
					}
					//validation
					self.validationECommerceView();
				}, self.fetchError
			);
		};
		/**
		 * Get size information.
		 */
		self.getSizeInformation = function () {
			self.isWaitingForResponse = true;
			eCommerceViewApi.getSizeInformation(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId,
					saleChannel: self.currentTab.saleChannel.id
				},
				function (result) {
					self.isWaitingForResponse = false;
					if(result != null){
						self.eCommerceViewDetails.size = angular.copy(result.size);
						self.eCommerceViewDetails.sizeOrg = angular.copy(self.eCommerceViewDetails.size);
						self.setSizeEditMode();
					}
					//validation
					self.validationECommerceView();
				}, self.fetchError
			);
		};
		/**
		 * Backup original data.
		 */
		self.backupOriginalData = function () {
			self.eCommerceViewDetails.brandOrg = angular.copy(self.eCommerceViewDetails.brand);
			self.eCommerceViewDetails.sizeOrg = angular.copy(self.eCommerceViewDetails.size);
			self.eCommerceViewDetails.displayNameOrg = angular.copy(self.eCommerceViewDetails.displayName);
			self.eCommerceViewDetails.fulfillmentChannelsOrg = angular.copy(self.eCommerceViewDetails.fulfillmentChannels);
			self.eCommerceViewDetails.customerHierarchyPrimaryPathOrg = angular.copy(self.eCommerceViewDetails.customerHierarchyPrimaryPath);
			self.eCommerceViewDetails.productFullfilmentChanelsOrg = angular.copy(self.eCommerceViewDetails.productFullfilmentChanels);
			self.eCommerceViewDetails.showOnSiteOrg = angular.copy(self.eCommerceViewDetails.showOnSite);
			self.eCommerceViewDetails.showOnSiteStartDateOrg = angular.copy(self.eCommerceViewDetails.showOnSiteStartDate);
			self.eCommerceViewDetails.showOnSiteEndDateOrg = angular.copy(self.eCommerceViewDetails.showOnSiteEndDate);
		};
		/**
		 * Find all the list of choice option information.
		 */
		self.findImageAlternatesByUri = function (index) {
            if(self.currentTab.id != FAVOR_TAB_ID) {
                if (self.eCommerceViewDetails.imageAlternates && self.eCommerceViewDetails.imageAlternates.length > index) {
                    eCommerceViewApi.findECommerceViewImageByUri({
                            imageUri: self.eCommerceViewDetails.imageAlternates[index].productScanImageURI.imageURI
                        },
                        function (results) {
                            if (results.data != null && results.data != '') {
                                self.eCommerceViewDetails.imageAlternates[index]["image"] = "data:image/jpg;base64," + results.data;
                            } else {
                                self.eCommerceViewDetails.imageAlternates[index]["image"] = ' ';
                            }
                            if (self.onLive) {
                                self.findImageAlternatesByUri(index + 1);
                            }
                        },
                        //error
                        self.fetchError
                    );
                }
            }
		};

		/**
		 * Build data time picker for fulfillment information, and remove fulfillment exist fulfillment program.
		 * @param productFullfilmentChanels
		 */
		self.buildDataForFulfillmentDatePicker = function (eCommerceViewDetails) {
			if (eCommerceViewDetails.productFullfilmentChanels != undefined && eCommerceViewDetails.productFullfilmentChanels != null) {
				angular.forEach(eCommerceViewDetails.productFullfilmentChanels, function (value) {
					value["showStar"] = false;
					value["effectDateOpen"] = false;
					value["expirationDateOpen"] = false;
					if (value.effectDate != null) {
						value.effectDate = new Date(value.effectDate.replace(/-/g, '\/'));
						if (value.effectDate > new Date()) {
							value["showStar"] = true;
						}
					}
					if (value.expirationDate != null) {
						value.expirationDate = new Date(value.expirationDate.replace(/-/g, '\/'));
					}
					for (var i = 0; i < eCommerceViewDetails.fulfillmentChannels.length; i++) {
						if (angular.toJson(eCommerceViewDetails.fulfillmentChannels[i]) == angular.toJson(value.fulfillmentChannel)) {
							eCommerceViewDetails.fulfillmentChannels.splice(i, 1);
							break;
						}
					}
				})
				eCommerceViewDetails.productFullfilmentChanelsOrg = angular.copy(eCommerceViewDetails.productFullfilmentChanels);
			}
		};

		/**
		 * Show full primary image, and allow user download
		 */
		self.showFullPrimaryImage  = function () {
			//Hide lens and  section containing zoomed image
            var imageZoomFrame = document.getElementById("imageZoomFrame");
            var imageZoomResult = document.getElementById("imageZoomResult");
            var lens = document.getElementById("lens");
            imageZoomFrame.style.visibility = "hidden";
            lens.style.visibility = "hidden";
            imageZoomResult.style.visibility = "hidden";
			self.imageBytes = '';
			self.imageFormat =''
			$('#imageModal').modal({backdrop: 'static', keyboard: true});
			if(self.eCommerceViewDetails.imagePrimary != null){
				self.imageBytes = self.eCommerceViewDetails.imagePrimary;
				if(self.eCommerceViewDetails.imagePrimaryFormat != null && self.eCommerceViewDetails.imagePrimaryFormat != '')
				{
					self.imageFormat =  self.eCommerceViewDetails.imagePrimaryFormat;
				}
			}else{
				self.imageBytes = ' ';
				$('#imageModal').modal("hide");
			}
		};

		/**
		 * Show full alternate image, and allow user download
		 */
		self.showFullAlternateImage  = function (alternateImage) {
			self.imageBytes = '';
			self.imageFormat ='';
			$('#imageModal').modal({backdrop: 'static', keyboard: true});
			if(alternateImage != null && alternateImage.image != ' ') {
				self.imageBytes = alternateImage.image;
				if(alternateImage.productScanImageURI.imageType.imageFormat != null &&  alternateImage.productScanImageURI.imageType.imageFormat != '') {
					self.imageFormat = alternateImage.productScanImageURI.imageType.imageFormat;
				}
			}else{
				self.imageBytes = ' ';
				$('#imageModal').modal("hide");
			}
		};

		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if(self.imageBytes != null){
				downloadImageService.download(self.imageBytes, self.imageFormat);
			}
		};

		/**
		 * Build data time picker for show on site information.
		 * @param productFullfilmentChanels
		 */
		self.buildDataForShowOnSiteDatePicker = function (eCommerceViewDetails) {
			eCommerceViewDetails["showOnSiteStartDateOpen"] = false;
			eCommerceViewDetails["showOnSiteEndDateOpen"] = false;
			if (eCommerceViewDetails.showOnSiteStartDate != undefined && eCommerceViewDetails.showOnSiteStartDate != null) {
				eCommerceViewDetails.showOnSiteStartDate = new Date(eCommerceViewDetails.showOnSiteStartDate.replace(/-/g, '\/'));
			}
			if (eCommerceViewDetails.showOnSiteEndDate != undefined && eCommerceViewDetails.showOnSiteEndDate != null) {
				eCommerceViewDetails.showOnSiteEndDate = new Date(eCommerceViewDetails.showOnSiteEndDate.replace(/-/g, '\/'));
			}
			self.currentDate = new Date();
			if(eCommerceViewDetails.showOnSiteEndDate > self.currentDate){
				eCommerceViewDetails.showOnSite = true;
				eCommerceViewDetails.showOnSiteOrg = true;
			}else{
				eCommerceViewDetails.showOnSite = false;
				eCommerceViewDetails.showOnSiteOrg = false;
			}
		};

		/**
		 * Set more eCommerce view information to current eCommerce view.
		 * @param eCommerceViewDetails
		 */
		self.setMoreECommerceViewInformationForCurrentECommerceViewDetail = function (eCommerceViewDetails) {
			self.eCommerceViewDetails.imageAlternates = angular.copy(eCommerceViewDetails.imageAlternates);
			self.eCommerceViewDetails.brand = self.reformatContent(eCommerceViewDetails.brand);
			self.eCommerceViewDetails.size = angular.copy(eCommerceViewDetails.size);
			self.eCommerceViewDetails.displayName = self.reformatContent(eCommerceViewDetails.displayName);
			self.eCommerceViewDetails.fulfillmentChannels = angular.copy(eCommerceViewDetails.fulfillmentChannels);
			self.eCommerceViewDetails.customerHierarchyPrimaryPath = angular.copy(eCommerceViewDetails.customerHierarchyPrimaryPath);
            self.eCommerceViewDetails.customerHierarchyPrimaryPaths = angular.copy(eCommerceViewDetails.customerHierarchyPrimaryPaths);
			self.eCommerceViewDetails.productFullfilmentChanels = angular.copy(eCommerceViewDetails.productFullfilmentChanels);
			self.eCommerceViewDetails.showOnSite = angular.copy(eCommerceViewDetails.showOnSite);
			self.eCommerceViewDetails.showOnSiteStartDate = angular.copy(eCommerceViewDetails.showOnSiteStartDate);
			self.eCommerceViewDetails.showOnSiteEndDate = angular.copy(eCommerceViewDetails.showOnSiteEndDate);
			self.eCommerceViewDetails.productOnline = angular.copy(eCommerceViewDetails.productOnline);
			self.eCommerceViewDetails.productPublished = angular.copy(eCommerceViewDetails.productPublished);

		};

		/**
		 * Validation information eCommerce view
		 */
		self.validationECommerceView = function () {
			if (self.eCommerceViewDetails.brand !== undefined
				&& self.eCommerceViewDetails.displayName !== undefined
				&& self.eCommerceViewDetails.size !== undefined
				&& self.eCommerceViewDetails.customerHierarchyPrimaryPath !== undefined
				&& self.eCommerceViewDetails.imagePrimary !== undefined
				&& self.eCommerceViewDetails.pdpTemplateId !== undefined){
				var errorMessages = [];
                if (self.eCommerceViewDetails.pdpTemplateId == null || self.eCommerceViewDetails.pdpTemplateId == '') {
                    errorMessages.push(PDP_TEMPLATE_ID_IS_MANDATORY_ERROR);
                }
				if (self.eCommerceViewDetails.size == null || self.eCommerceViewDetails.size === undefined) {
					errorMessages.push(SIZE_IS_MANDATORY_ERROR);
				}
				else if (self.eCommerceViewDetails.size.mandatory !== undefined && self.eCommerceViewDetails.size.mandatory !== null
						&& self.eCommerceViewDetails.size.mandatory) {
                    errorMessages.push(SIZE_IS_MANDATORY_ERROR);
                   /* if(self.eCommerceViewDetails.publishedDate === null
						|| (self.eCommerceViewDetails.publishedDate !== null && angular.equals(self.eCommerceViewDetails.size.content , ""))){
                        errorMessages.push(SIZE_IS_MANDATORY_ERROR);
                    }*/
				}
				if (self.eCommerceViewDetails.brand == null || self.eCommerceViewDetails.brand == undefined) {
					errorMessages.push(BRAND_IS_MANDATORY_ERROR);
				}
				else if (self.eCommerceViewDetails.brand.mandatory !== undefined && self.eCommerceViewDetails.brand.mandatory !== null
						&& self.eCommerceViewDetails.brand.mandatory) {
					errorMessages.push(BRAND_IS_MANDATORY_ERROR);
				}
				if (self.eCommerceViewDetails.displayName == null || self.eCommerceViewDetails.displayName == undefined) {
					errorMessages.push(DISPLAY_NAME_IS_MANDATORY_ERROR);
				}
				else if (self.eCommerceViewDetails.displayName.mandatory !== undefined && self.eCommerceViewDetails.displayName.mandatory !== null
						&& self.eCommerceViewDetails.displayName.mandatory) {
					errorMessages.push(DISPLAY_NAME_IS_MANDATORY_ERROR);
				}
				if(self.currentTab.id != GOOGLE_TAB_ID) {
					if (self.isEmpty(self.eCommerceViewDetails.customerHierarchyPrimaryPath)) {
						errorMessages.push(CUSTOMER_HIERARCHY_IS_MANDATORY_ERROR);
					}
				}
				if(self.currentTab.id != FAVOR_TAB_ID) {
                    if (self.isEmpty(self.eCommerceViewDetails.imagePrimary)) {
                        errorMessages.push(PRIMARY_IMAGE_IS_MANDATORY_ERROR);
                    }
                }
                if (self.eCommerceViewDetails.customerHierarchyPrimaryPaths != null && self.eCommerceViewDetails.customerHierarchyPrimaryPaths.length > 1) {
                    errorMessages.push('Only one Hierarchy can be Primary. Please Update.');
                }
				self.eCommerceViewDetails.errorMessages = errorMessages;
			}
		};

		/**
		 * Check empty value.
		 *
		 * @param val
		 * @returns {boolean}
		 */
		self.isEmpty = function(val){
			if (val == null || val == undefined || val.trim().length == 0) {
				return true;
			}
			return false;
		};
		/**
		 * When click open date picker for anther attribute, store current status for date picker.
		 */
		self.openDatePicker = function (fieldName) {
			switch (fieldName) {
				case "showOnSiteStartDate":
					self.eCommerceViewDetails.showOnSiteStartDateOpen = true;
					break;
				case "showOnSiteEndDate":
					self.eCommerceViewDetails.showOnSiteEndDateOpen = true;
					break;
				default:
					break;
			}
			self.options = {
				minDate: new Date(),
				maxDate: new Date("9999-12-31")
			};
		};

		/**
		 * Turn on edit mode for any field source
		 * @param field
		 */
		self.turnOnEditMode = function (field) {
			switch (field) {
				case "brand":
					self.brandEditMode = true;
					break;
				case "displayName":
					self.displayNameEditMode = true;
					break;
				case "size":
					self.sizeEditMode = true;
					break;
				default:
					break;
			}
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
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
		 * Reload ECommerceView.
		 */
		$scope.$on(self.RELOAD_ECOMMERCE_VIEW, function() {
			self.$onChanges();
		});
		/**
		 * Reset eCommerce view.
		 */
		$scope.$on(self.RESET_ECOMMERCE_VIEW, function() {
			self.htmlShowDispName = false;
			self.htmlShowBrand = false;
			self.setBrandEditMode();
			self.setDisplayNameEditMode();
			self.setSizeEditMode();
			self.brandContent = angular.copy(self.brandContentOrg);
            if (!self.brandEditMode) {
                self.eCommerceViewDetails.brand.content = angular.copy(self.brandContentOrg);
            }
            if (!self.displayNameEditMode) {
                self.eCommerceViewDetails.displayName.content = angular.copy(self.displayNameContentOrg);
            }
			self.displayNameContent = angular.copy(self.displayNameContentOrg);
			self.validateSpellCheckDisplayName();
			self.validateSpellCheckBrand();
		});
		/**
		 * Reload after save popup.
		 */
		$scope.$on(self.RELOAD_AFTER_SAVE_POPUP, function(event, attributeId, status) {
			if(attributeId === self.BRAND_ATTRIBUTE_ID){
				self.brandEditMode = false;
				if(status === true){
					self.getBrandInformation();
				}else{
					self.isWaitingForResponse = true;
				}
			}
			if(attributeId === self.DISPLAY_NAME_ATTRIBUTE_ID){
				self.displayNameEditMode = false;
				if(status === true){
					self.getDisplayNameInformation();
				}else{
					self.isWaitingForResponse = true;
				}
			}
			if(attributeId === self.SIZE_ATTRIBUTE_ID){
				self.sizeEditMode = false;
				if(status === true){
					self.getSizeInformation();
				}else{
					self.isWaitingForResponse = true;
				}
			}
		});
		/**
		 * Validate warning.
		 */
		$scope.$on(self.VALIDATE_WARNING, function() {
			self.validationECommerceView();
		});

		/**
		 * Handle onpen edit source popup basing on attribute id and header title
		 * @param attributeId - The attribute id
		 * @param headerTitle - The header title
		 */
		self.editSourceHandle = function (attributeId, headerTitle) {
			switch (attributeId) {
				case self.BRAND_ATTRIBUTE_ID:
					self.isEditText = self.brandEditMode;
					break;
				case self.SIZE_ATTRIBUTE_ID:
					self.isEditText = self.sizeEditMode;
					break;
				case self.DISPLAY_NAME_ATTRIBUTE_ID:
					self.isEditText = self.displayNameEditMode;
					break;
				default:
					break;
			}
			self.getECommerceViewDataSource({
				productId: self.productMaster.prodId,
				scanCodeId: self.productMaster.productPrimaryScanCodeId,
				attributeId: attributeId,
				salesChannel: self.currentTab.saleChannel.id,
				headerTitle: headerTitle,
				typeSource: 'type1'});
		};

		/**
		 * Validate spell check of brand.
		 */
		self.validateSpellCheckBrand = function(){
			if(self.htmlBrandTabPressing){
				self.htmlBrandTabPressing = false;
				return;
			}
            if (self.brandEditMode) {
                self.validateBrandTextForEdit();
            } else {
                self.validateBrandTextForView();
            }
		};

		/**
		 * Validate text for view mode of brand. It will call after loaded romance description.
		 */
		self.validateBrandTextForView = function(){
			if(!self.isEmpty(self.eCommerceViewDetails.brand.content)){
				self.isWaitingForCheckSpellingResponseBrand = true;
				//self.changeSpellCheckStatus({status: true});
				vocabularyService.validateBrandTextForView(self.eCommerceViewDetails.brand.content, function(newText, suggestions){
					//self.eCommerceViewDetails.brand.content = newText;
					self.brandContent = angular.copy(newText);
					self.brandContentOrg = angular.copy(newText);
					self.eCommerceViewDetails.brand.content = angular.copy(newText);
					//self.eCommerceViewDetails.brand.content = angular.copy(newText);
					//self.brandOrg = angular.copy(self.eCommerceViewDetails.brand);
					//self.eCommerceViewDetails.brandOrg.content = angular.copy(newText);
					//vocabularyService.createSuggestionMenuContext($scope, suggestions);
					self.isWaitingForCheckSpellingResponseBrand = false;
					//self.changeSpellCheckStatus({status: false});
				});
			}
		};

		/**
		 * validate text for edit mode of brand.
		 */
		self.validateBrandTextForEdit = function(){
			if(!self.isEmpty(self.eCommerceViewDetails.brand.content)){
				var callbackFunction = self.getAfterSpellCheckCallback();
				self.isWaitingForCheckSpellingResponseBrand = true;
				self.changeSpellCheckStatus({status: true});
				vocabularyService.validateBrandTextForEdit(self.eCommerceViewDetails.brand.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					self.eCommerceViewDetails.brand.content = self.eCommerceViewDetails.brand.content + ' ';
					$timeout(function () {
						//self.eCommerceViewDetails.brand.content = newText;
						self.eCommerceViewDetails.brand.content = angular.copy(newText);
						//self.eCommerceViewDetails.brandOrg.content = angular.copy(newText);
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponseBrand = false;
						self.changeSpellCheckStatus({status: false});
						// Save or publish if event occurs from save or publish button.
						self.processAfterSpellCheck(callbackFunction);
					}, 100);
				});
			}
		};

		/**
		 * Validate camel case of display name.
		 */
		self.doValidateCamelCaseDisplayName = function(){
            if (self.displayNameEditMode) {
                self.validateCamelCaseDisplayName();
            }
		};

		/**
		 * Validate spell check of display name.
		 */
		self.validateSpellCheckDisplayName = function(){
			if(self.htmlDisplNmTabPressing){
				self.htmlDisplNmTabPressing = false;
				return;
			}
            if (self.displayNameEditMode) {
                self.validateDisplayNameTextForEdit();
            } else {
                self.validateDisplayNameTextForView();
            }
		};

		/**
		 * Validate text for view mode of display name. It will call after loaded display name.
		 */
		self.validateDisplayNameTextForView = function(){
			if(!self.isEmpty(self.eCommerceViewDetails.displayName.content)){
				self.isWaitingForCheckSpellingResponseDisplayName = true;
				//self.changeSpellCheckStatus({status: true});
				vocabularyService.validateDisplayNameTextForView(self.eCommerceViewDetails.displayName.content, function(newText, suggestions){
					self.displayNameContent = angular.copy(newText);
					self.displayNameContentOrg = angular.copy(newText);
					self.eCommerceViewDetails.displayName.content = angular.copy(newText);
					//vocabularyService.createSuggestionMenuContext($scope, suggestions);
					self.isWaitingForCheckSpellingResponseDisplayName = false;
					//self.changeSpellCheckStatus({status: false});
				});
			}
		};

		/**
		 * Validate text for edit mode of display name when focus out.
		 */
		self.validateDisplayNameTextForEditOldWay = function(){
			if(!self.isEmpty(self.eCommerceViewDetails.displayName.content)){
				self.isWaitingForCheckSpellingResponseDisplayName = true;
				self.changeSpellCheckStatus({status: true});
				vocabularyService.validateDisplayNameTextForEditOldWay(self.eCommerceViewDetails.displayName.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					self.eCommerceViewDetails.displayName.content = self.eCommerceViewDetails.displayName.content + ' ';
					$timeout(function () {
						self.eCommerceViewDetails.displayName.content = angular.copy(newText);
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponseDisplayName = false;
						self.changeSpellCheckStatus({status: false});
					}, 100);
				});
			}
		};
		/**
		 * Disable validate spell check of Display Name when open suggestions popup.
		 */
		$scope.$on('context-menu-opened', function (event, args) {
			self.openSuggestionsPopup = true;
		});
		/**
		 * Enable validate spell check of Display Name when close suggestions popup.
		 */
		$scope.$on('context-menu-closed', function (event, args) {
			self.openSuggestionsPopup = false;
		});
		/**
		 * Validate text for edit mode of display name when focus out.
		 */
		self.validateDisplayNameTextForEdit = function(){
			if(!self.openSuggestionsPopup){//avoid conflict with suggestion popup
				if(!self.isEmpty(self.eCommerceViewDetails.displayName.content)){
					var callbackFunction = self.getAfterSpellCheckCallback();
					self.isWaitingForCheckSpellingResponseDisplayName = true;
					self.changeSpellCheckStatus({status: true});
					vocabularyService.validateDisplayNameTextForEdit(self.eCommerceViewDetails.displayName.content, function(newText, suggestions){
						// Use $timeout and space char to clear cache on layout.
						self.eCommerceViewDetails.displayName.content = self.eCommerceViewDetails.displayName.content + ' ';
						$timeout(function () {
							self.displayNameContent = angular.copy(newText);
							self.eCommerceViewDetails.displayName.content = angular.copy(newText);//color text
							$scope.suggestionsOrg = angular.copy(suggestions);
							$timeout(function () {
								vocabularyService.createSuggestionMenuContext($scope, suggestions);
								// Save or publish if event occurs from save or publish button.
								self.processAfterSpellCheck(callbackFunction);
							}, 1000);
							self.isWaitingForCheckSpellingResponseDisplayName = false;
							self.changeSpellCheckStatus({status: false});
						}, 100);
					});
				}
			}
		};

		/**
		 * Validate text for edit mode of display name when click Aa button.
		 */
		self.validateCamelCaseDisplayNameOldWay = function(){
			if(!self.isEmpty(self.eCommerceViewDetails.displayName.content)){
				self.isWaitingForCheckSpellingResponseDisplayName = true;
				self.changeSpellCheckStatus({status: true});
				vocabularyService.validateCamelCaseDisplayNameOldWay(self.eCommerceViewDetails.displayName.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					self.eCommerceViewDetails.displayName.content = self.eCommerceViewDetails.displayName.content + ' ';
					$timeout(function () {
						self.eCommerceViewDetails.displayName.content = angular.copy(newText);
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponseDisplayName = false;
						self.changeSpellCheckStatus({status: false});
					}, 100);
				});
			}
		};
		/**
		 * Validate text for edit mode of display name when click Aa button.
		 */
		self.validateCamelCaseDisplayName = function(){
			if(!self.openSuggestionsPopup){//avoid conflict with suggestion popup
				if(!self.isEmpty(self.eCommerceViewDetails.displayName.content)){
					self.isWaitingForCheckSpellingResponseDisplayName = true;
					self.changeSpellCheckStatus({status: true});
					vocabularyService.validateCamelCaseDisplayName(self.eCommerceViewDetails.displayName.content, function(newText, suggestions){
						// Use $timeout and space char to clear cache on layout.
						self.eCommerceViewDetails.displayName.content = self.eCommerceViewDetails.displayName.content + ' ';
						$timeout(function () {
							self.displayNameContent = angular.copy(newText);
							self.eCommerceViewDetails.displayName.content = angular.copy(newText);//color text
							$scope.suggestionsOrg = angular.copy(suggestions);
							$timeout(function () {
								vocabularyService.createSuggestionMenuContext($scope, suggestions);
							}, 1000);
							self.isWaitingForCheckSpellingResponseDisplayName = false;
							self.changeSpellCheckStatus({status: false});
						}, 100);
					});
				}
			}
		};
		/**
		 * Returns the function to do (save or publish) after spell check success.
		 * @return {null}
		 */
		self.getAfterSpellCheckCallback = function(){
			var callbackFunction = self.callbackfunctionAfterSpellCheck;
			self.callbackfunctionAfterSpellCheck = null;
			return callbackFunction;
		}
		/**
		 * Call the function for save or publish after spell check is success.
		 * @param callback
		 */
		self.processAfterSpellCheck = function(callback){
			if(callback != null && callback !== undefined){
				callback();
			}
		}
		/**
		 * Check edit status or not for brand.
		 *
		 * @returns {boolean}
		 */
		self.setBrandEditMode = function(){
			if(self.eCommerceViewDetails.brand != null) {
				self.brandEditMode = self.eCommerceViewDetails.brand.sourceSystemId === SRC_SYS_ID_EDIT && self.eCommerceViewDetails.brand.physicalAttributeId == EDIT_BRAND_PHY_ATTR_ID;
			}
		};
		/**
		 * Check edit status or not for display name.
		 *
		 * @returns {boolean}
		 */
		self.setDisplayNameEditMode = function(){
			if(self.eCommerceViewDetails.displayName != null) {
				self.displayNameEditMode = self.eCommerceViewDetails.displayName.sourceSystemId === SRC_SYS_ID_EDIT && self.eCommerceViewDetails.displayName.physicalAttributeId == EDIT_DISPLAY_NAME_PHY_ATTR_ID;
			}
		};
		/**
		 * Check edit status or not for size.
		 *
		 * @returns {boolean}
		 */
		self.setSizeEditMode = function(){
			if(self.eCommerceViewDetails.size != null) {
				self.sizeEditMode = self.eCommerceViewDetails.size.sourceSystemId === SRC_SYS_ID_EDIT && self.eCommerceViewDetails.size.physicalAttributeId == EDIT_SIZE_PHY_ATTR_ID;
			}
		};

		/**
		 * Set type of text for brand field
		 * @param htmlFormat
		 */
		self.showFormatBrandField = function (htmlFormat) {
			self.htmlShowBrand = htmlFormat;
			self.eCommerceViewDetails.brand.htmlSave = htmlFormat;
			self.htmlBrandTabPressing = false;
		};

		/**
		 * Set type of text for display name field
		 * @param htmlFormat
		 */
		self.showFormatDisplayNameField = function (htmlFormat) {
			self.htmlShowDispName = htmlFormat;
			self.eCommerceViewDetails.displayName.htmlSave = htmlFormat;
			self.htmlDisplNmTabPressing = false;
		};

		/**
		 * Prepare callback function for  after spell check is success.
		 */
		self.htmlBrandTabClick = function(){
			self.htmlBrandTabPressing = true;
		};

        /**
         * Zoom one section of image when user hover on section of image
		 * Add some mouse events when use action on the image or lens
		 * @param primaryImageId - The identity of the image
         * @param imageZoomResultId - The identity of  The div tag which to display the zoomed image
		 * @param lenId - The identity of  The div tag which to watch image
		 * @param imageZoomFrameId - The identity of the div tag which round image (which have width and height < 650px)
         */
        self.moveMouseOnImage = function(primaryImageId, imageZoomResultId, lenId, imageZoomFrameId) {
            var img, lens, imageZoomResult, cx, cy, imageZoomFrame, width, height;
            img = document.getElementById(primaryImageId);
            imageZoomResult = document.getElementById(imageZoomResultId);
			lens = document.getElementById(lenId);
            imageZoomFrame = document.getElementById(imageZoomFrameId);
            width = img.naturalWidth;
            height = img.naturalHeight;
			/*insert lens:*/
            img.parentElement.insertBefore(lens, img);
			/*calculate the ratio between imageZoomResult DIV and lens:*/
            cx = imageZoomResult.offsetWidth / lens.offsetWidth;
            cy = imageZoomResult.offsetHeight / lens.offsetHeight;
			/*set background properties for the imageZoomResult DIV:*/
            imageZoomResult.style.backgroundImage = "url('" + self.eCommerceViewDetails.imagePrimary + "')";
            imageZoomResult.style.backgroundSize = (img.width * cx) + "px " + (img.height * cy) + "px";
			/*execute a function when someone moves the cursor over the image, or the lens:*/
            lens.addEventListener("mousemove", moveLens);
            img.addEventListener("mousemove", moveLens);
            /*Mouse out*/
            lens.addEventListener("mouseout", moveOut);
            img.addEventListener("mouseout", moveOut);
			/*and also for touch screens:*/
            lens.addEventListener("touchmove", moveLens);
            img.addEventListener("touchmove", moveLens);
            lens.addEventListener("touchmove",moveOut);
            img.addEventListener("touchmove", moveOut);
            /*Mouse roll*/
            img.addEventListener("wheel", moveOut);
            lens.addEventListener("wheel", moveOut);

            /**
             * Set background position of the imageZoomResult div when user move the lens
			 * Display what the lens "sees":
             * @param e - The event when user execute an action
             */
            function moveLens(e) {
                imageZoomResult.style.visibility = "visible";
                if(width < 650 && height < 650){
                    imageZoomFrame.style.visibility = "visible";
                    imageZoomResult.style.width = width  +  "px";
                    imageZoomResult.style.height = height + "px";
                    imageZoomResult.style.setProperty('box-shadow', 'none');
                    imageZoomResult.style.setProperty('border', 'none');
                }else{
                    imageZoomFrame.style.visibility = "hidden";
                    imageZoomResult.style.setProperty('box-shadow', '0 2px 4px 0 rgba(0,0,0,0.2)');
                    imageZoomResult.style.setProperty('border', '1px solid rgb(191,191,191)');
                    imageZoomResult.style.width = "650px";
                    imageZoomResult.style.height = "650px";
                }
                lens.style.visibility = "visible";

                var pos, x, y;
				/*prevent any other actions that may occur when moving over the image:*/
				e.preventDefault();
				/*get the cursor's x and y positions:*/
                pos = getCursorPos(e);
				/*calculate the position of the lens:*/
                x = pos.x - (lens.offsetWidth / 2);
                y = pos.y - (lens.offsetHeight / 2);
				/*prevent the lens from being positioned outside the image:*/
                if (x > img.width - lens.offsetWidth) {x = img.width - lens.offsetWidth;}
                if (x < 0) {x = 0;}
                if (y > img.height - lens.offsetHeight) {y = img.height - lens.offsetHeight;}
                if (y < 0) {y = 0;}

				/*set the position of the lens:*/
                lens.style.left = x + "px";
                lens.style.top = y + "px";
				/*display what the lens "sees":*/
                imageZoomResult.style.backgroundPosition = "-" + (x * cx) + "px -" + (y * cy) + "px";
            }
            /**
             * Get the cursor's x and y positions
             * @param e - The event when user execute an action
             */
            function getCursorPos(e) {
                var a, x = 0, y = 0;
                e = e || window.event;
				/*get the x and y positions of the image:*/
                a = img.getBoundingClientRect();
				/*calculate the cursor's x and y coordinates, relative to the image:*/
                x = e.pageX - a.left;
                y = e.pageY - a.top;
				/*consider any page scrolling:*/
                x = x - window.pageXOffset;
                y = y - window.pageYOffset;
                return {x : x, y : y};
            }
            /**
             * Hide imageZoomResult, imageZoomFrame divs and the lens
             * @param e - The event when user execute an action
             */
            function moveOut(e) {
                lens.style.visibility = "hidden";
                imageZoomResult.style.visibility = "hidden";
                imageZoomFrame.style.visibility = "hidden";
            }
        };

        /**
		 * Prepare callback function for  after spell check is success.
		 */
		self.htmlDisplNmTabClick = function(){
			self.htmlDisplNmTabPressing = true;
		};
	}
})();
