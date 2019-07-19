/*
 *   eCommerceViewComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Product -> eCommerce View page component.
 *
 * @author vn70516
 * @since 2.0.14
 */
(function () {

	angular.module('productMaintenanceUiApp').component('eCommerceView', {
		bindings: {
			productMaster: '<',
			isLastProduct: '&'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceView.html',
		// The controller that handles our component logic
		controller: ECommerceViewController
	});

	ECommerceViewController.$inject = ['ECommerceViewApi', '$scope', '$timeout', '$filter', '$sce', '$rootScope', 'VocabularyService', 'ProductGroupService', '$state', 'appConstants', 'ProductSearchService', 'taskService', '$location', 'PermissionsService', 'customHierarchyService'];


	function ECommerceViewController(eCommerceViewApi, $scope, $timeout, $filter, $sce, $rootScope, vocabularyService, productGroupService, $state, appConstants, productSearchService, taskService, $location, permissionsService, customHierarchyService) {

		var self = this;
		self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';

        self.SELECT_DATE_SUBSCRIPTION_ELIGIBLE = 'Please select the date for the Subscription Eligible.';

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
		 * Clear message key.
		 * @type {string}
		 */
		self.CLEAR_MESSAGE = 'clearMessage';
		/**
		 * The key for showing next product
		 * @type {string}
		 */
		const NEXT_PRODUCT = 'nextProduct';
        /**
         * The id of favor tab
         * @type {string}
         */
        const FAVOR_TAB_ID = 'Favor';
        /**
         * sale channel of heb.com.
         * @type {string}
         */
        const SAL_CHANNEL_HEB_COM = '01';
        /**
         * Check edit text mode
         * @type {boolean}
         */
        self.isEditText = false;

		/**
		 * Romance copy logical attribute id.
		 *
		 * @type {number}
		 */
		self.LOG_ATTR_ID_ROMANCE_COPY = 1666;

		/**
		 * Ingredient editable phycical attribute id.
		 *
		 * @type {number}
		 */
		self.PHY_ATTR_ID_INGREDIENT = 1643;
		/**
		 * Ingredient logical attribute id.
		 *
		 * @type {number}
		 */
		self.LOG_ATTR_ID_INGREDIENT = 1674;
		/**
		 * Brand attribute logical attribute id.
		 * @type {number}
		 */
		self.LOG_ATTR_ID_BRAND = 1672;
		/**
		 * Display name logical attribute id.
		 * @type {number}
		 */
		self.LOG_ATTR_ID_DISPLAY_NAME = 1664;

		/**
		 * Size logical attribute id.
		 * @type {number}
		 */
		self.LOG_ATTR_ID_SIZE = 1667;
		/**
		 * The direction logical attribute id.
		 *
		 * @type {number}
		 */
		self.LOG_ATTR_ID_DIRECTION= 1676;
		/**
		 * The warning logical attribute id.
		 *
		 * @type {number}
		 */
		self.LOG_ATTR_ID_WARNING = 1677;
		/**
		 * The tag logical attribute id.
		 *
		 * @type {number}
		 */
		self.LOG_ATTR_ID_TAG = 1729;

		/**
		 * Nutrient logical attribute id.
		 *
		 * @type {number}
		 */
		self.LOG_ATTR_ID_NUTRIENT = 1679;

		/**
		 * Specification logical attribute id.
		 *
		 * @type {number}
		 */
		self.LOG_ATTR_ID_SPEC = 1728;
        /**
         * Favor item description logical attribute id.
         *
         * @type {number}
         */
        self.LOG_ATTR_ID_FAVOR_ITEM_DESCRIPTION = 3989;
        /**
         * The current error message.
         * @type {String}
         */
        self.error = null;
        /**
         * Keeps track of whether front end is waiting for back end response.
         *
         * @type {boolean}
         */
        self.isWaitingForResponse = false;

		/**
		 * The current tab information
		 * @type {{}}
		 */
		self.currentTab = {};

		/**
		 * The list of sale channel
		 * @type {Array}
		 */
		self.saleChannels = [];

		/**
		 * The domain tab bar data provider.
		 */
		self.domainTabBarDataProvider = [];

		/**
		 * eCommerce View details information.
		 * @type {{}}
		 */
		self.eCommerceViewDetails = {};
		/**
		 * Holds the status to request publish action after save success.
		 * @type {boolean}
		 */
		self.isRequestPublishAfterSave = false;
		/**
		 * The current error message.
		 * @type {String}
		 */
		self.errorPopup = null;

		/**
		 * The data source title basing on attribute id.
		 * @type {string}
		 */
		self.dataSourceTitle = '';

		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponsePopup = false;
		/**
		 * Keeps track of whether front end is waiting for check spelling response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForCheckSpellingResponse = false;
		/**
		 * The list of data source provider information.
		 * @type {Array}
		 */
		self.eCommerceViewAttributePriority = {};

		/**
		 * The list of data source provider information back up
		 * @type {Array}
		 */
		self.eCommerceViewAttributePriorityOrg = {};
		/**
		 * Holds the new subscription info.
		 *
		 * @type {{subscriptionStartDate: null, subscriptionEndDate: null, subscription: boolean}}
		 */
		self.newSubscription=null;

		/**
		 * The attribute mapping data provider.
		 */
		self.attributeMappingDataProvider = [];

		/**
		 * The current attribute id. when set open edit source
		 */
		self.currentAttributeId;


		/**
		 * identify if user click on Aa or Reset button
		 */
		self.clickOnAaOrResetButton = false;

		/**
		 * Keeps track of whether front end is waiting for back end response for attribute mapping pop up.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponsePopupAttr = false;

		/**
		 * Min length for value of source
		 *
		 * @type {number}
		 */
		self.MIN_LENGTH_READ = 200;
		/**
		 * From page navigated to ecommerce view page
		 * @type {String}
		 */
		const FROM_PAGE_DETAIL_PRODUCT = 'associatedProductDetail';
		/**
		 * From page navigated to ecommerce view page
		 * @type {String}
		 */
		const FROM_PAGE_ECOMMERCE_TASK_DETAIL = 'ecommerceTaskDetail';
		/**
		 * enable or disable return to list button
		 *
		 * @type {Boolean}
		 */
		self.disableReturnToList = true;

		/**
		 * Keeps track of whether front end is waiting for back end response for attribute mapping popup.
		 *
		 * @type {boolean}
		 */
		self.errorPopupAttr = false;
		self.vocabularyService = vocabularyService;
		/**
		 * Task Detail base url.
		 * @type {string}
		 */
		const TASK_DETAIL_URL = "/task/ecommerceTask/taskInfo/";

		/**
		 * Constant when start date invalid.
		 *
		 * @type {String}
		 */
		const START_DATE_MANDATORY = 'Start Date is mandatory.';
		/**
		 * Constant when start date invalid.
		 *
		 * @type {String}
		 */
		const START_GREATER_OR_EQUAL_CUR = 'Start Date must be greater than or equal to Current Date.';
		/**
		 * Constant when start date invalid.
		 *
		 * @type {String}
		 */
		const START_LESS_END = 'Start Date must be less than End Date.';
		/**
		 * Constant when end date invalid.
		 *
		 * @type {String}
		 */
		const END_DATE_MANDATORY = 'End Date is mandatory.';
		/**
		 * Constant when end date invalid.
		 *
		 * @type {String}
		 */
		const END_GREATER_CUR_AND_LESS_MAX = 'End Date must be greater than Current Date and less than 12/31/9999.';
		/**
		 * Constant when end date invalid.
		 *
		 * @type {String}
		 */
		const END_GREATER_START_AND_LESS_MAX = 'End Date must be greater than Start Date and less than 12/31/9999.';
		/**
		 * Constant for empty string.
		 * @type {String}
		 */
		const EMPTY = '';
		/**
		 * Constant for RED.
		 * @type {String}
		 */
        const RED = 'red';

        /**
         * Initialize values for heb guarantee type code
         */
        const HEB_GUARANTEE_TYPE_CODE_02 = '00002';
        const HEB_GUARANTEE_TYPE_CODE_04 = '00004';
        const HEB_GUARANTEE_TYPE_CODE_05 = '00005';
        const HEB_GUARANTEE_TYPE_CODE_06 = '00006';
        const HEB_GUARANTEE_TYPE_CODE_07 = '00007';

        /**
         * Constant type of file of image
         */
        self.GUARANTEE_IMAGE_PATH_002 = 'images/guarantee-00002.png';
        self.GUARANTEE_IMAGE_PATH_004 = 'images/guarantee-00004.png';
        self.GUARANTEE_IMAGE_PATH_005 = 'images/guarantee-00005.png';
        self.GUARANTEE_IMAGE_PATH_006 = 'images/guarantee-00006.png';
        self.GUARANTEE_IMAGE_PATH_007 = 'images/guarantee-00007.png';

		self.isEBM = false;
		/**
		 * Holds the save or publish function to do after spell check.
		 * @type {null}
		 */
		self.callbackfunctionAfterSpellCheck = null;
		/**
		 * Flag to show type of html.
		 * @type {boolean}
		 */
		self.htmlMode = false;
		/**
		 * HTML Tab Pressing flag.
		 * @type {boolean}
		 */
		self.htmlTabPressing = false;
        /**
         *  Keeps track of whether $onInit is run or not.
         */
        self.hasLoadInit = false;
		/**
		 * Initialize the controller.
		 */
        this.$onInit = function () {
            //Check to enable or disable the return to list button
            self.disableReturnToList = productSearchService.getDisableReturnToList();
            self.countRequest = 0;
            self.hasLoadInit = true;
            self.loadDataInit();
            self.error = '';
            self.success = '';
        };

        /**
         * Component will reload the kits data whenever the item is changed in casepack.
         */
        this.$onChanges = function () {
            self.error = '';
            self.success = '';
            self.getECommerceViewInformation();
            if(self.hasLoadInit){
                self.getHebGuaranteeTypeCode();
            }
        };

		/**
		 * Load data init
		 */
		self.loadDataInit = function () {
			self.isWaitingForResponse = true;
			eCommerceViewApi.findAllSaleChanel(
				//success
				function (results) {
					angular.forEach(results, function (value) {
						value.id = value.id + "   ";
					});
					self.saleChannels = angular.copy(results);
					self.buildECommerceViewTabBySaleChanel(self.saleChannels);
					self.getECommerceViewInformation();
				}
				, self.fetchError
			);
			self.getHebGuaranteeTypeCode();
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
				},
				{
					displayName: "Google",
					id: "Google",
					logo: "images/logo_google_shopping.jpg",
					saleChannel: saleChannels[4],
					hierCntxtCd: "GOOGL"
				},
				{
					displayName: "Central Market",
					id: "Central_M",
					logo: "images/logo_central_market.jpg",
					saleChannel: saleChannels[5],
					hierCntxtCd: "CM"
				},
				{
                    displayName: "Favor",
                    id: FAVOR_TAB_ID,
                    logo: "images/logo_favor.png",
                    saleChannel: saleChannels[7],
                    hierCntxtCd: "FAVOR"
				}
			];
		};

		/**
		 * Get eCommerce View information by product id, sale channel,...ect
		 */
		self.getECommerceViewInformation = function () {
			eCommerceViewApi.getECommerceViewInformation(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId
				},
				//success case
				function (results) {
					self.buildDataForSubscriptionDatePicker(results);
					//store data eCommerce view details
					self.eCommerceViewDetails.publishedDate = results.publishedDate;
					self.eCommerceViewDetails.publishedBy = results.publishedBy;
					self.eCommerceViewDetails.snipes = results.snipes;
					self.eCommerceViewDetails.pdpTemplateId = results.pdpTemplateId;
					self.eCommerceViewDetails.pdpTemplateIdOrg = angular.copy(self.eCommerceViewDetails.pdpTemplateId);
					self.eCommerceViewDetails.storeCount = results.storeCount;
					self.eCommerceViewDetails.publishedDateString = results.publishedDateString;
					//validation
					self.validateWarning();
				}
				//error case
				, self.fetchError
			);
		};
		/**
		 * Build data time picker for show on site information.
		 * @param productFullfilmentChanels
		 */
		self.buildDataForSubscriptionDatePicker = function (eCommerceViewDetails) {
			self.eCommerceViewDetails["subscription"] = self.productMaster.subscription;
			self.eCommerceViewDetails["subscriptionStartDateOpen"] = false;
			self.eCommerceViewDetails["subscriptionEndDateOpen"] = false;
                        self.eCommerceViewDetails["subscriptionStartDate"] = null;
                        self.eCommerceViewDetails["subscriptionEndDate"] = null;
                        self.eCommerceViewDetails["subscriptionStartDateOrg"] = null;
                        self.eCommerceViewDetails["subscriptionEndDateOrg"] = null;

			if (self.productMaster.subscriptionStartDate != undefined && self.productMaster.subscriptionStartDate != null) {
				self.eCommerceViewDetails["subscriptionStartDate"] = new Date(self.productMaster.subscriptionStartDate.replace(/-/g, '\/'));
				self.eCommerceViewDetails["subscriptionStartDateOrg"] = angular.copy(self.eCommerceViewDetails["subscriptionStartDate"]);
			}
			if (self.productMaster.subscriptionEndDate != undefined && self.productMaster.subscriptionEndDate != null) {
				self.eCommerceViewDetails["subscriptionEndDate"] = new Date(self.productMaster.subscriptionEndDate.replace(/-/g, '\/'));
				self.eCommerceViewDetails["subscriptionEndDateOrg"] = angular.copy(self.eCommerceViewDetails["subscriptionEndDate"]);
			}
			self.eCommerceViewDetails["subscriptionOrg"] = angular.copy(self.eCommerceViewDetails["subscription"]);
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
            $rootScope.contentChangedFlag = true;
			self.isWaitingForResponse = false;
			self.nextTab = null;
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
			$rootScope.$broadcast(self.RELOAD_AFTER_SAVE_POPUP, self.currentAttributeId, true);
		};
        /**
		 * Get heb guarantee type code
         */
		self.getHebGuaranteeTypeCode = function(){
			self.hebGuaranteeTypeDescription = '';
            self.hebGuaranteeImage = null;
            self.hebGuaranteeTypeCode = null;
			eCommerceViewApi.findHebGuaranteeTypeCode({productId:self.productMaster.prodId},
				//success
				function (results) {
                    if(results != null ){
                        self.hebGuaranteeTypeDescription = results.hebGuaranteeTypeDescription;
                        self.hebGuaranteeTypeCode = results.hebGuaranteeTypeCode;
                        if(results.hebGuaranteeTypeCode != undefined
                            && $.trim(results.hebGuaranteeTypeCode) != ""){
                            self.hebGuaranteeImage = self.findGuaranteeImagePath(results.hebGuaranteeTypeCode);
                        }
					}
				}
				, self.fetchError
			);
		};

        /**
		 * Find guarantee image path base on guarantee image type code
         * @param hebGuaranteeTypeCode
         * @returns {*}
         */
		self.findGuaranteeImagePath = function(hebGuaranteeTypeCode){
			switch (hebGuaranteeTypeCode){
                case HEB_GUARANTEE_TYPE_CODE_02:
                	return self.GUARANTEE_IMAGE_PATH_002;
                case HEB_GUARANTEE_TYPE_CODE_04:
                    return self.GUARANTEE_IMAGE_PATH_004;
                case HEB_GUARANTEE_TYPE_CODE_05:
                    return self.GUARANTEE_IMAGE_PATH_005;
                case HEB_GUARANTEE_TYPE_CODE_06:
                    return self.GUARANTEE_IMAGE_PATH_006;
                case HEB_GUARANTEE_TYPE_CODE_07:
                    return self.GUARANTEE_IMAGE_PATH_007;
			}
			return null;
		}

		/**
		 * validate when user change on end date
		 */
		self.changeEndDate = function () {
			if (self.eCommerceViewDetails.showOnSiteEndDate === null || self.eCommerceViewDetails.showOnSiteEndDate == EMPTY) {
				self.eCommerceViewDetails.endDateErrorMgs = END_DATE_MANDATORY;
				self.eCommerceViewDetails.isEndDateErrorMgs = true;
			} else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, new Date())) {
				self.eCommerceViewDetails.endDateErrorMgs = END_GREATER_CUR_AND_LESS_MAX;
				self.eCommerceViewDetails.isEndDateErrorMgs = true;
			}
			else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, self.eCommerceViewDetails.showOnSiteStartDate)) {
				self.eCommerceViewDetails.endDateErrorMgs = END_GREATER_START_AND_LESS_MAX;
				self.eCommerceViewDetails.isEndDateErrorMgs = true;
			}
			else{
				self.eCommerceViewDetails.endDateErrorMgs = EMPTY;
				self.eCommerceViewDetails.isEndDateErrorMgs = false;
			}
		};

		/**
		 * validate when user change on end date
		 */
		self.changeStartDate = function () {
			if (self.eCommerceViewDetails.showOnSiteStartDate === null || self.eCommerceViewDetails.showOnSiteStartDate == EMPTY) {
				self.eCommerceViewDetails.startDateErrorMgs = START_DATE_MANDATORY;
				self.eCommerceViewDetails.isStartDateErrorMgs = true;
			} else if (!self.isDate1GreaterThanOrEqualToDate2(self.eCommerceViewDetails.showOnSiteStartDate, new Date())) {

				self.eCommerceViewDetails.startDateErrorMgs = START_GREATER_OR_EQUAL_CUR;
				self.eCommerceViewDetails.isStartDateErrorMgs = true;
				// self.eCommerceViewDetails.endDateErrorMgs = END_GREATER_START_AND_LESS_MAX;
				// self.eCommerceViewDetails.isEndDateErrorMgs = true;

			}
			else if(!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, self.eCommerceViewDetails.showOnSiteStartDate)) {
				if(self.eCommerceViewDetails.showOnSiteEndDate !== null){
					self.eCommerceViewDetails.startDateErrorMgs = START_LESS_END;
					self.eCommerceViewDetails.isStartDateErrorMgs = true;
				}else{
					self.eCommerceViewDetails.startDateErrorMgs = EMPTY;
					self.eCommerceViewDetails.isStartDateErrorMgs = false;
				}

			}

			else{
				self.eCommerceViewDetails.startDateErrorMgs = EMPTY;
				self.eCommerceViewDetails.isStartDateErrorMgs = false;
			}

		};
		/**
		 * Save the data.
		 */
		self.save = function () {
			self.callbackfunctionAfterSpellCheck = null;
			self.error = '';
			self.success = '';
			self.errorMessages = [];
			self.isRequestPublishAfterSave = false;
			self.newSubscription = null;
            if (self.eCommerceViewDetails.showOnSite != self.eCommerceViewDetails.showOnSiteOrg) {
                self.changeEndDate();
                self.changeStartDate();
            }
            if (self.isDataChanged()) {
                if (self.validateDataChanged()) {
                    // save data.
                    self.updateECommerceViewInformation();
                }
            } else {
                // Show message.
                if(self.eCommerceViewDetails.subscription && self.eCommerceViewDetails.subscriptionStartDate == null && self.eCommerceViewDetails.subscriptionEndDate == null) {
                    self.error = self.SELECT_DATE_SUBSCRIPTION_ELIGIBLE;
                } else {
                self.error = self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
            }
            }
        };
		/**
		 * Save data for heb.
		 */
		self.updateECommerceViewInformation = function () {
			self.isWaitingForResponse = true;
			eCommerceViewApi.updateECommerceViewInformation(self.eCommerceViewDetailsParams,
				//success
				function (results) {
                    $rootScope.contentChangedFlag = false;
					if(self.newSubscription != null){
						/* update new subscription into product to avoid reloading the product.*/
						self.productMaster.subscription = self.newSubscription.subscription;
						self.productMaster.subscriptionStartDate = self.newSubscription.subscriptionStartDate;
						self.productMaster.subscriptionEndDate = self.newSubscription.subscriptionEndDate;
					}
					if (self.isRequestPublishAfterSave) {
						// Execute publish after save success.
						self.doPublish();
					} else {
						self.error = '';
						self.success = results.message;
						self.isWaitingForResponse = false;
						if(self.nextTab !== null){//move to next tab
							$scope.active = self.nextTab;
						}else{
							self.updateNewDataToOriginalObject();
						}
					}
				},
				self.fetchError
			);
        };

        /**
		 * Validate data.
		 *
		 * @returns {boolean}
		 */
		self.validateDataChanged = function () {
			self.errorMessages = [];
			/* validate show on site.*/
			var checkDateShowOnSite = false;
			//if(self.isShowOnSiteChanged){
			var showOnSiteTitle = 'Show On Site';
			if(self.eCommerceViewDetails.showOnSite){
				if (self.isStartDateShowOnSiteChanged()) {
					if (self.eCommerceViewDetails.showOnSiteStartDate === null) {
						self.errorMessages.push(showOnSiteTitle + '<li>Start Date is mandatory.</li>');
						showOnSiteTitle = '';
					} else if (!self.isDate1GreaterThanOrEqualToDate2(self.eCommerceViewDetails.showOnSiteStartDate, new Date())) {
						self.errorMessages.push(showOnSiteTitle + '<li>Start Date must be greater than or equal to Current Date.</li>');
						showOnSiteTitle = '';
					}
					else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, self.eCommerceViewDetails.showOnSiteStartDate)) {
						if(self.eCommerceViewDetails.showOnSiteEndDate !== null && self.eCommerceViewDetails.showOnSiteStartDate !== null)
							self.errorMessages.push(showOnSiteTitle + '<li>Start Date must be less than End Date.</li>');
					}
					checkDateShowOnSite = true;
				}
				if (self.isEndDateShowOnSiteChanged() || checkDateShowOnSite) {
					if (self.eCommerceViewDetails.showOnSiteEndDate === null) {
						self.errorMessages.push(showOnSiteTitle + '<li>End Date is mandatory.</li>');
						showOnSiteTitle = '';
					} else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, new Date())) {
						self.errorMessages.push(showOnSiteTitle + '<li>End Date must be greater than Current Date and less than 12/31/9999.</li>');
						showOnSiteTitle = '';
					}
					else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.showOnSiteEndDate, self.eCommerceViewDetails.showOnSiteStartDate)) {
						if(self.eCommerceViewDetails.showOnSiteEndDate !== null && self.eCommerceViewDetails.showOnSiteStartDate !== null)
							self.errorMessages.push(showOnSiteTitle + '<li>End Date must be greater than Start Date and less than 12/31/9999.</li>');
					}
				}
			}

			var subcription = 'Subscription Eligible';
			var checkDateSubscription = false;
			//if (self.isSubscriptionChanged()) {
			if (self.eCommerceViewDetails.subscription && self.isStartDateSubscriptionChanged()) {
				if (self.eCommerceViewDetails.subscriptionStartDate === null) {
					self.errorMessages.push(subcription+'<li>Start Date is mandatory.</li>');
					subcription = '';
				}else if (!self.isDate1GreaterThanOrEqualToDate2(self.eCommerceViewDetails.subscriptionStartDate, new Date())) {
					self.errorMessages.push(subcription+'<li>Start Date must be greater than or equal to Current Date.</li>');
					subcription = '';
				}
				var checkDateSubscription = true;
			}
			if (self.eCommerceViewDetails.subscription && self.isEndDateSubscriptionChanged()) {
				if (self.eCommerceViewDetails.subscriptionEndDate === null) {
					self.errorMessages.push(subcription+'<li>End Date is mandatory.</li>');
					subcription = '';
				}else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.subscriptionEndDate, new Date())) {
					self.errorMessages.push(subcription+'<li>End Date must be greater than Current Date.</li>');
					subcription = '';
				}
				var checkDateSubscription = true;
			}
			if(checkDateSubscription){
				if (self.eCommerceViewDetails.subscriptionStartDate !== null && self.eCommerceViewDetails.subscriptionEndDate !== null) {
					if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.subscriptionEndDate, self.eCommerceViewDetails.subscriptionStartDate)) {
						self.errorMessages.push(subcription+'<li>Start Date must be less than End Date.</li>');
					}
				}
			}

            //	}
            /* Validate Product Fullfilment Chanel. */
            if (self.isProductFullfilmentChanelsChanged()) {
                self.validateFullfilmentChanelHeb();
            }
            if (self.errorMessages.length > 0) {
                var errorMessagesAsString = '';
                angular.forEach(self.errorMessages, function (errorMessage) {
                    errorMessagesAsString += errorMessage;
                });
                self.error = $sce.trustAsHtml('<ul style="text-align: left;">' + errorMessagesAsString + '</ul>');
                return false;
            }
            return true;
        };
		/**
		 * Check start date is changed for show on site.
		 *
		 * @returns {boolean} true start date is changed or not.
		 */
		self.isStartDateShowOnSiteChanged = function () {
			var isChanged = false;
			if (self.eCommerceViewDetails.showOnSiteStartDateOrg == null) {
				if (self.eCommerceViewDetails.showOnSiteStartDate != self.eCommerceViewDetails.showOnSiteStartDateOrg) {
					isChanged = true;
				}
			} else {
				var newDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDate);
				var oldDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDateOrg);
				if (oldDate !== newDate) {
					isChanged = true;
				}else{
					if (self.eCommerceViewDetails.showOnSiteOrg != self.eCommerceViewDetails.showOnSite) {
						isChanged = true;
					}
				}
			}
			return isChanged;
		};

		/**
		 * Check end date is changed for show on site.
		 *
		 * @returns {boolean} true show on site is changed or not.
		 */
		self.isEndDateShowOnSiteChanged = function () {
			var isChanged = false;
			if (self.eCommerceViewDetails.showOnSiteEndDateOrg == null) {
				if (self.eCommerceViewDetails.showOnSiteEndDate != self.eCommerceViewDetails.showOnSiteEndDateOrg) {
					isChanged = true;
				}
			} else {
				var newDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDate);
				var oldDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDateOrg);
				if (oldDate !== newDate) {
					isChanged = true;
				} else if (self.eCommerceViewDetails.showOnSiteOrg != self.eCommerceViewDetails.showOnSite) {
					isChanged = true;
				}
			}
			return isChanged;
		};
		/**
		 * Validate fullfilment chanel data for Heb.
		 */
		self.validateFullfilmentChanelHeb = function () {
			var errors = [];
			var displayOnlyItem = null;
			for (var i = 0; i < self.eCommerceViewDetails.productFullfilmentChanels.length; i++) {
				var check = false;
				if(self.checkEffectDateChanged(self.eCommerceViewDetails.productFullfilmentChanels[i])){
					self.eCommerceViewDetails.productFullfilmentChanels[i].effectDateErrorCss = '';
					self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDateErrorCss = '';
					if (self.eCommerceViewDetails.productFullfilmentChanels[i].effectDate == null) {
						if (errors.indexOf('<li>Effective Date is mandatory.</li>') == -1) {
							errors.push('<li>Effective Date is mandatory.</li>');
						}
						self.eCommerceViewDetails.productFullfilmentChanels[i].effectDateErrorCss = RED;
					} else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.productFullfilmentChanels[i].effectDate, new Date())) {
						if (errors.indexOf('<li>Effective Date must be greater than Current Date.</li>') == -1) {
							errors.push('<li>Effective Date must be greater than Current Date.</li>');
						}
						self.eCommerceViewDetails.productFullfilmentChanels[i].effectDateErrorCss = RED;
					}
					check = true;
				}
				if(self.checkExpirationDateChanged(self.eCommerceViewDetails.productFullfilmentChanels[i])){
					if (self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDate == null) {
						if (errors.indexOf('<li>End Date is mandatory.</li>') === -1) {
							errors.push('<li>End Date is mandatory.</li>');
						}
						self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDateErrorCss = RED;
					} else if (!self.isDate1GreaterThanDate2(self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDate, new Date())) {
						if (errors.indexOf('<li>End Date must be greater than Current Date.</li>') == -1) {
							errors.push('<li>End Date must be greater than Current Date.</li>');
						}
						self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDateErrorCss = RED;
					}
					check = true;
				}
				if(check){
					if (self.eCommerceViewDetails.productFullfilmentChanels[i].effectDate != null &&
						self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDate != null) {
						if (!self.isDate1GreaterThanOrEqualToDate2(self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDate, self.eCommerceViewDetails.productFullfilmentChanels[i].effectDate)) {
							if (errors.indexOf('<li>Effective Date must be less than or equal to End Date.</li>') == -1) {
								errors.push('<li>Effective Date must be less than or equal to End Date.</li>');
							}
							self.eCommerceViewDetails.productFullfilmentChanels[i].effectDateErrorCss = RED;
							self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDateErrorCss = RED;
						}
					}
				}
				if(self.eCommerceViewDetails.productFullfilmentChanels[i].key !== null
					&& self.eCommerceViewDetails.productFullfilmentChanels[i].key.fullfillmentChanelCode !== null
					&& self.eCommerceViewDetails.productFullfilmentChanels[i].key.fullfillmentChanelCode.trim() === '03'){
					displayOnlyItem = angular.copy(self.eCommerceViewDetails.productFullfilmentChanels[i]);
				}
			}
			if (errors.length == 0 && displayOnlyItem !== null) {//check display only rule
				angular.forEach(self.eCommerceViewDetails.productFullfilmentChanels, function(productFullfilmentChanel){
					if(productFullfilmentChanel.key !== null
						&& productFullfilmentChanel.key.fullfillmentChanelCode !== null
						&& productFullfilmentChanel.key.fullfillmentChanelCode.trim() !== '03'){
						var a = self.convertDateToStringWithYYYYMMDD(productFullfilmentChanel.effectDate);
						var b = self.convertDateToStringWithYYYYMMDD(displayOnlyItem.effectDate);
						var c = self.convertDateToStringWithYYYYMMDD(productFullfilmentChanel.expirationDate);
						var d = self.convertDateToStringWithYYYYMMDD(displayOnlyItem.expirationDate);
						if(a === b && c === d){
							if (errors.indexOf('<li>Cannot set sellable and non-sellable with overlapping dates.</li>') === -1) {
								errors.push('<li>Cannot set sellable and non-sellable with overlapping dates.</li>');
							}
						}
					}
				});
			}
			if (errors.length > 0) {
				self.errorMessages.push('Fulfillment Program');
				self.errorMessages = self.errorMessages.concat(errors);
			}
        };

        /**
		 * Check the data change for ProductFullfilmentChanels.
		 *
		 * @returns {boolean} true subscription is changed or not.
		 */
		self.checkEffectDateChanged = function (checkItem) {
			var isChanged = false;
			if (self.eCommerceViewDetails.productFullfilmentChanelsOrg == null) {
				isChanged = true;
			} else {
				var oldItem = self.getProductFullfilmentChanelOrg(checkItem);
				if (oldItem == null) {
					isChanged = true;
				}else{
					if (checkItem.effectDate != null && oldItem.effectDate != null) {
						if (!self.isDate1EqualToDate2(checkItem.effectDate, oldItem.effectDate)) {
							isChanged = true;
						}
					} else {
						if (checkItem.effectDate != oldItem.effectDate) {
							isChanged = true;
						}
					}
				}
			}
			return isChanged;
		};
		/**
		 * Check the data change for ProductFullfilmentChanels.
		 *
		 * @returns {boolean} true subscription is changed or not.
		 */
		self.checkExpirationDateChanged = function (checkItem) {
			var isChanged = false;
			if (self.eCommerceViewDetails.productFullfilmentChanelsOrg == null) {
				isChanged = true;
			} else {
				var oldItem = self.getProductFullfilmentChanelOrg(checkItem);
				if (oldItem == null) {
					isChanged = true;
				}else{
					if (checkItem.expirationDate != null && oldItem.expirationDate != null) {
						if (!self.isDate1EqualToDate2(checkItem.expirationDate, oldItem.expirationDate)) {
							isChanged = true;
						}
					} else {
						if (checkItem.expirationDate != oldItem.expirationDate) {
							isChanged = true;
						}
					}
				}
			}
			return isChanged;
		};
		/**
		 * Change the data change.
		 *
		 * @returns {boolean}
		 */
		self.isDataChanged = function () {
			var isChanged = false;
			self.eCommerceViewDetailsParams = {};
			self.eCommerceViewDetailsParams.scanCodeId = self.productMaster.productPrimaryScanCodeId;
            self.eCommerceViewDetailsParams.productId = self.productMaster.prodId;
            // Romance Copy.
            self.eCommerceViewDetailsParams.attributeListChange = [];
            if (self.isNotEqualRomanceCopy(self.eCommerceViewDetails.romanceCopy, self.eCommerceViewDetails.romanceCopyOrg)) {
                //set data change
                self.eCommerceViewDetailsParams.romanceCopy = angular.copy(self.eCommerceViewDetails.romanceCopy);
                self.eCommerceViewDetailsParams.romanceCopy.logicAttributeId = self.LOG_ATTR_ID_ROMANCE_COPY;
                self.eCommerceViewDetailsParams.attributeListChange.push(self.LOG_ATTR_ID_ROMANCE_COPY);
                isChanged = true;
                if (self.eCommerceViewDetailsParams.romanceCopy.htmlSave) {
                    self.eCommerceViewDetailsParams.romanceCopy.content = angular.copy(self.eCommerceViewDetails.romanceCopy.htmlContent);
                } else {
                    //format data changed with html tag (br, bullet)
                    var contentHtml = $('#romanceCopyDiv').html();
                    contentHtml = contentHtml.split("<br>").join("<div>");
                    contentHtml = contentHtml.split("</p>").join("<div>");
                    var contentHtmlArray = contentHtml.split("<div>");
                    var contentNoneHtml = '';
                    if (contentHtmlArray.length > 1) {
                        for (var i = 0; i < contentHtmlArray.length; i++) {
                            if (contentHtmlArray[i] != null && contentHtmlArray[i] != '') {
                                contentNoneHtml = contentNoneHtml.concat(self.htmlToPlaintext(contentHtmlArray[i]));
                                if (i < (contentHtmlArray.length - 1)) {
                                    contentNoneHtml = contentNoneHtml.concat(" <br> ");
                                }
                            }
                        }
                        contentNoneHtml = contentNoneHtml.split("&nbsp;").join(" ");
                        var escapeArray = contentNoneHtml.split(" ");
                        var content = '';
                        for (var i = 0; i < escapeArray.length; i++) {
                            if (escapeArray[i] != " " && escapeArray[i] != "<br>") {
                                content = content.concat(self.encodeHTMLEntity(escapeArray[i])).concat(" ");
                            } else {
                                content = content.concat(escapeArray[i]).concat(" ");
                            }
                        }
                        self.eCommerceViewDetailsParams.romanceCopy.content = self.removeBreakTag(content);
                    } else if (contentHtmlArray.length == 1) {
                        self.eCommerceViewDetailsParams.romanceCopy.content = self.removeBreakTag(self.encodeHTMLEntity(self.htmlToPlaintext(contentHtml)));
                    }
                }
            }
            //Favor item description
            if(self.currentTab.id === FAVOR_TAB_ID) {
                if (self.isNotEqualRomanceCopy(self.eCommerceViewDetails.favorItemDescription, self.eCommerceViewDetails.favorItemDescriptionOrg)) {
                    //set data change
                    self.eCommerceViewDetailsParams.favorItemDescription = angular.copy(self.eCommerceViewDetails.favorItemDescription);
                    self.eCommerceViewDetailsParams.favorItemDescription.logicAttributeId = self.LOG_ATTR_ID_FAVOR_ITEM_DESCRIPTION;
                    self.eCommerceViewDetailsParams.attributeListChange.push(self.LOG_ATTR_ID_FAVOR_ITEM_DESCRIPTION);
                    isChanged = true;
                    if (self.eCommerceViewDetailsParams.favorItemDescription.htmlSave) {
                        self.eCommerceViewDetailsParams.favorItemDescription.content = angular.copy(self.eCommerceViewDetails.favorItemDescription.htmlContent);
                    } else {
                        //format data changed with html tag (br, bullet)
                        var contentHtml = $('#favorItemDescriptionDiv').html();
                        contentHtml = contentHtml.split("<br>").join("<div>");
                        contentHtml = contentHtml.split("</p>").join("<div>");
                        var contentHtmlArray = contentHtml.split("<div>");
                        var contentNoneHtml = '';
                        if (contentHtmlArray.length > 1) {
                            for (var i = 0; i < contentHtmlArray.length; i++) {
                                if (contentHtmlArray[i] != null && contentHtmlArray[i] != '') {
                                    contentNoneHtml = contentNoneHtml.concat(self.htmlToPlaintext(contentHtmlArray[i]));
                                    if (i < (contentHtmlArray.length - 1)) {
                                        contentNoneHtml = contentNoneHtml.concat(" <br> ");
                                    }
                                }
                            }
                            contentNoneHtml = contentNoneHtml.split("&nbsp;").join(" ");
                            var escapeArray = contentNoneHtml.split(" ");
                            var content = '';
                            for (var i = 0; i < escapeArray.length; i++) {
                                if (escapeArray[i] != " " && escapeArray[i] != "<br>") {
                                    content = content.concat(self.encodeHTMLEntity(escapeArray[i])).concat(" ");
                                } else {
                                    content = content.concat(escapeArray[i]).concat(" ");
                                }
                            }
                            self.eCommerceViewDetailsParams.favorItemDescription.content = self.removeBreakTag(content);
                        } else if (contentHtmlArray.length == 1) {
                            self.eCommerceViewDetailsParams.favorItemDescription.content = self.removeBreakTag(self.encodeHTMLEntity(self.htmlToPlaintext(contentHtml)));
                        }
                    }
                }
            }
            // Brand
            if (self.isNotEqualBrandDisplayName(self.eCommerceViewDetails.brand, self.eCommerceViewDetails.brandOrg)) {
                self.eCommerceViewDetailsParams.brand = self.eCommerceViewDetails.brand;
                self.eCommerceViewDetailsParams.brand.logicAttributeId = self.LOG_ATTR_ID_BRAND;
                if (self.eCommerceViewDetails.brand.htmlSave) {
                    self.eCommerceViewDetailsParams.brand.content = angular.copy(self.eCommerceViewDetails.brand.htmlContent);
                }
                self.eCommerceViewDetailsParams.attributeListChange.push(self.LOG_ATTR_ID_BRAND);
                isChanged = true;
            }
            if (self.isNotEqualBrandDisplayName(self.eCommerceViewDetails.displayName, self.eCommerceViewDetails.displayNameOrg)) {
                self.eCommerceViewDetailsParams.displayName = self.eCommerceViewDetails.displayName;
                self.eCommerceViewDetailsParams.displayName.content = self.encodeHTMLEntity(self.eCommerceViewDetailsParams.displayName.content);
                self.eCommerceViewDetailsParams.displayName.logicAttributeId = self.LOG_ATTR_ID_DISPLAY_NAME;
                if (self.eCommerceViewDetails.displayName.htmlSave) {
                    self.eCommerceViewDetailsParams.displayName.content = angular.copy(self.eCommerceViewDetails.displayName.htmlContent);
                }
                self.eCommerceViewDetailsParams.attributeListChange.push(self.LOG_ATTR_ID_DISPLAY_NAME);
                isChanged = true;
            }
            if (self.isNotEqual(self.eCommerceViewDetails.size, self.eCommerceViewDetails.sizeOrg)) {
                self.eCommerceViewDetailsParams.size = self.eCommerceViewDetails.size;
                self.eCommerceViewDetailsParams.size.logicAttributeId = self.LOG_ATTR_ID_SIZE;
                self.eCommerceViewDetailsParams.attributeListChange.push(self.LOG_ATTR_ID_SIZE);
                isChanged = true;
            }
            if (self.isNotEqual(self.eCommerceViewDetails.warnings, self.eCommerceViewDetails.warningsOrg)) {
                self.eCommerceViewDetailsParams.warnings = self.eCommerceViewDetails.warnings;
                self.eCommerceViewDetailsParams.warnings.logicAttributeId = self.LOG_ATTR_ID_WARNING;
                self.eCommerceViewDetailsParams.attributeListChange.push(self.LOG_ATTR_ID_WARNING);
                isChanged = true;
            }
            if (self.isNotEqual(self.eCommerceViewDetails.directions, self.eCommerceViewDetails.directionsOrg)) {
                self.eCommerceViewDetailsParams.directions = self.eCommerceViewDetails.directions;
                self.eCommerceViewDetailsParams.directions.logicAttributeId = self.LOG_ATTR_ID_DIRECTION;
                self.eCommerceViewDetailsParams.attributeListChange.push(self.LOG_ATTR_ID_DIRECTION);
                isChanged = true;
            }
            if (self.isEditingIngredient === true) {
                if (self.isNotEqual(self.eCommerceViewDetails.ingredients, self.eCommerceViewDetails.ingredientsOrg)) {
                    self.eCommerceViewDetailsParams.ingredients = self.eCommerceViewDetails.ingredients;
                    self.eCommerceViewDetailsParams.ingredients.logicAttributeId = self.LOG_ATTR_ID_INGREDIENT;
                    self.eCommerceViewDetailsParams.ingredients.physicalAttributeId = self.PHY_ATTR_ID_INGREDIENT;
                    self.eCommerceViewDetailsParams.attributeListChange.push(self.LOG_ATTR_ID_INGREDIENT);
                    isChanged = true;
                }
            }
            var orgDescription = '';
            var selectedDescription = '';
            if (self.eCommerceViewDetails.pdpTemplateIdOrg != null && self.eCommerceViewDetails.pdpTemplateIdOrg !== undefined) {
                orgDescription = self.eCommerceViewDetails.pdpTemplateIdOrg;
            }
            if (self.eCommerceViewDetails.pdpTemplateId != null && self.eCommerceViewDetails.pdpTemplateId !== undefined) {
                selectedDescription = self.eCommerceViewDetails.pdpTemplateId;
            }
            if (orgDescription !== selectedDescription) {
                self.eCommerceViewDetailsParams.pdpTemplate = {content: selectedDescription};
                isChanged = true;
            }
            if (self.isShowOnSiteChanged()) {
                self.eCommerceViewDetailsParams.showOnSite = self.eCommerceViewDetails.showOnSite;
                self.currentDate = self.convertDateToStringWithYYYYMMDD(new Date());
                self.eCommerceViewDetailsParams.showOnSiteStartDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDate);
                if (self.eCommerceViewDetailsParams.showOnSiteStartDate <= self.currentDate && !self.eCommerceViewDetailsParams.showOnSite) {
                    self.eCommerceViewDetailsParams.showOnSiteEndDate = self.currentDate;
                }
                else {
                    self.eCommerceViewDetailsParams.showOnSiteEndDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDate);
                }
                self.eCommerceViewDetailsParams.salsChnlCd = self.getSalsChnlCd();
                isChanged = true;
            }
            if (self.isSubscriptionChanged()) {
                self.eCommerceViewDetailsParams.subscription = self.eCommerceViewDetails.subscription;
                self.eCommerceViewDetailsParams.subscriptionStartDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionStartDate)
                self.eCommerceViewDetailsParams.subscriptionEndDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionEndDate);
                self.eCommerceViewDetailsParams.subscriptionChanged = true;
                /* Catch new subscription*/
                self.newSubscription = {
                    subscriptionStartDate: self.eCommerceViewDetailsParams.subscriptionStartDate,
                    subscriptionEndDate: self.eCommerceViewDetailsParams.subscriptionEndDate,
                    subscription: self.eCommerceViewDetailsParams.subscription
                }
                isChanged = true;
            }
            // Fullfilment
            if (self.isProductFullfilmentChanelsChanged()) {
                self.eCommerceViewDetailsParams.productFullfilmentChanels = [];
                var oldItem = null;
                var newItem = null;
                angular.forEach(self.eCommerceViewDetails.productFullfilmentChanels, function (item) {
                    oldItem = self.getProductFullfilmentChanelOrg(item);
                    newItem = angular.copy(item);
                    if (oldItem === null) {
                        // Add new item.
                        newItem.actionCode = 'A';
                        newItem.effectDate = self.convertDateToStringWithYYYYMMDD(newItem.effectDate);
                        newItem.expirationDate = self.convertDateToStringWithYYYYMMDD(newItem.expirationDate);
                        delete newItem['lastUpdateTime'];
                        self.eCommerceViewDetailsParams.productFullfilmentChanels.push(newItem);
                    } else {
                        if (!self.isDate1EqualToDate2(newItem.effectDate,
                            oldItem.effectDate) ||
                            !self.isDate1EqualToDate2(newItem.expirationDate,
                                oldItem.expirationDate)) {
                            // Update item
                            newItem.effectDate = self.convertDateToStringWithYYYYMMDD(newItem.effectDate);
                            newItem.expirationDate = self.convertDateToStringWithYYYYMMDD(newItem.expirationDate);
                            if (newItem.actionCode === null) {
                                newItem.actionCode = '';
                            }
                            delete newItem['lastUpdateTime'];
                            self.eCommerceViewDetailsParams.productFullfilmentChanels.push(newItem);
                        }
                    }
                });
                // Add Remove list of item.
                if (self.eCommerceViewDetails.productFullfilmentChanelsRemoveList !== null &&
                    (self.eCommerceViewDetails.productFullfilmentChanelsRemoveList instanceof Array) &&
                    self.eCommerceViewDetails.productFullfilmentChanelsRemoveList.length > 0) {
                    angular.forEach(self.eCommerceViewDetails.productFullfilmentChanelsRemoveList, function (item) {
                        item.effectDate = self.convertDateToStringWithYYYYMMDD(item.effectDate);
                        item.expirationDate = self.convertDateToStringWithYYYYMMDD(item.expirationDate);
                        delete item['lastUpdateTime'];
                        self.eCommerceViewDetailsParams.productFullfilmentChanels.push(item);
                    });
                }
                isChanged = true;
            } else {
            	self.eCommerceViewDetailsParams.productFullfilmentChanels = [];
            }
            // PanelType
            if (self.isPanelTypeChanged()) {
                self.eCommerceViewDetailsParams.nutriPanelTypCd = self.eCommerceViewDetails.panelType.panelTypeCode;
                isChanged = true;
            }
            return isChanged;
        };
        /**
         * Get sale channel code.
         */
        self.getSalsChnlCd = function () {
            var salsChnlCd = null;
            if (self.currentTab.saleChannel != null && self.currentTab.saleChannel !== undefined) {
                salsChnlCd = self.currentTab.saleChannel.id.trim();
            } else {
                salsChnlCd = SAL_CHANNEL_HEB_COM;
            }
            return salsChnlCd;
        };
		/**
		 * Compare the content property of two objects.
		 *
		 * @param newObject the new object.
		 * @param oldObject the old object.
		 * @returns {boolean} true the content of two objects are not the same or the same.
		 */
		self.isNotEqual = function (newObject, oldObject) {
			if (newObject !== null && newObject !== undefined) {
				if (oldObject === null || oldObject === undefined) {
					return true;
				}
				if(newObject.content === undefined || oldObject.content === undefined){
					return false;
				}
				if (newObject.content === oldObject.content) {
					// there is no data change.
					return false;
				} else {
					return true;
				}
			}
			return false;
		};

		/**
		 * Compare the content property of two objects.
		 *
		 * @param newObject the new object.
		 * @param oldObject the old object.
		 * @returns {boolean} true the content of two objects are not the same or the same.
		 */
		self.isNotEqualHasHTMLTab = function (newObject, oldObject) {
			if (newObject !== null && newObject !== undefined) {
				if (oldObject === null || oldObject === undefined) {
					return true;
				}
				if(newObject.content === undefined || oldObject.content === undefined){
					return false;
				}
				if(newObject.htmlSave){
					if (newObject.htmlContent === oldObject.htmlContent) {
						// there is no data change.
						return false;
					} else {
						return true;
					}
				}else{
					if (newObject.content === oldObject.content) {
						// there is no data change.
						return false;
					} else {
						return true;
					}
				}
			}
			return false;
		};

		/**
		 * Compare the content property of two objects.
		 *
		 * @param newObject the new object.
		 * @param oldObject the old object.
		 * @returns {boolean} true the content of two objects are not the same or the same.
		 */
		self.isNotEqualRomanceCopy = function (newObject, oldObject) {
			if (newObject !== null && newObject !== undefined) {
				if (oldObject === null || oldObject === undefined) {
					return true;
				}
				if(newObject.content === undefined || oldObject.content === undefined){
					return false;
				}
				if(newObject.htmlSave){
					if (newObject.htmlContent === oldObject.htmlContent) {
						// there is no data change.
						return false;
					} else {
						return true;
					}
				}else{
					var newText = $("<div/>").html(newObject.content).text();
					var oldText = $("<div/>").html(oldObject.content).text();
					newText = newText.split(/\s+/).join(" ");
					oldText = oldText.split(/\s+/).join(" ");
					if (newText === oldText) {
						// there is no data change.
						return false;
					} else {
						return true;
					}
				}
			}
			return false;
		};
		/**
		 * Compare the content property of two objects.
		 *
		 * @param newObject the new object.
		 * @param oldObject the old object.
		 * @returns {boolean} true the content of two objects are not the same or the same.
		 */
		self.isNotEqualBrandDisplayName = function (newObject, oldObject) {
			if (newObject !== null && newObject !== undefined) {
				if (oldObject === null || oldObject === undefined) {
					return true;
				}
				if(newObject.content === undefined || oldObject.content === undefined){
					return false;
				}
				if(newObject.htmlSave){
					if (newObject.htmlContent === oldObject.htmlContent) {
						return false;
					} else {
						return true;
					}
				}else{
					if (newObject.content === oldObject.content) {
						return false;
					} else {
						var newText = $("<div/>").html(newObject.content).text();
						var oldText = $("<div/>").html(oldObject.content).text();
						newText = newText.split(/\s+/).join(" ");
						oldText = oldText.split(/\s+/).join(" ");
						if (newText === oldText) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
			return false;
		};
		/**
		 * Compare the dat1 and date2 are the same or not.
		 *
		 * @param date1 the date.
		 * @param date2 the date.
		 * @returns {boolean} true if the date equal to date 2 or false.
		 */
		self.isDate1EqualToDate2 = function (date1, date2) {
			if ((new Date(self.convertDateToStringWithYYYYMMDD(date1)).getTime() == new Date(self.convertDateToStringWithYYYYMMDD(date2)).getTime())) {
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
		 * Check the data change for subscription.
		 *
		 * @returns {boolean} true subscription is changed or not.
		 */
		self.isSubscriptionChanged = function () {
			var isChanged = false;
			if (self.eCommerceViewDetails.subscriptionStartDate == null || self.eCommerceViewDetails.subscriptionEndDate == null) {
				if (self.eCommerceViewDetails.subscriptionStartDate != self.eCommerceViewDetails.subscriptionStartDateOrg) {
					isChanged = true;
				} else if (self.eCommerceViewDetails.subscriptionEndDate != self.eCommerceViewDetails.subscriptionEndDateOrg) {
					isChanged = true;
				}
			} else {
				var newDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionStartDate);
				var oldDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionStartDateOrg);
				if (oldDate !== newDate) {
					isChanged = true;
				} else {
					newDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionEndDate);
					oldDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionEndDateOrg);
					if (oldDate !== newDate) {
						isChanged = true;
					} else if (self.eCommerceViewDetails.subscription != self.eCommerceViewDetails.subscriptionOrg) {
						isChanged = true;
					}
				}
			}
			return isChanged;
		};
		/**
		 * Check the data change for subscription.
		 *
		 * @returns {boolean} true subscription is changed or not.
		 */
		self.isStartDateSubscriptionChanged = function () {
			var isChanged = false;
			if (self.eCommerceViewDetails.subscriptionStartDate == null) {
				if (self.eCommerceViewDetails.subscriptionStartDate != self.eCommerceViewDetails.subscriptionStartDateOrg) {
					isChanged = true;
				}
			} else {
				var newDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionStartDate);
				var oldDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionStartDateOrg);
				if (oldDate !== newDate) {
					isChanged = true;
				} else {
					if (self.eCommerceViewDetails.subscription != self.eCommerceViewDetails.subscriptionOrg) {
						isChanged = true;
					}
				}
			}
			return isChanged;
		};
		/**
		 * Check the data change for subscription.
		 *
		 * @returns {boolean} true subscription is changed or not.
		 */
		self.isEndDateSubscriptionChanged = function () {
			var isChanged = false;
			if (self.eCommerceViewDetails.subscriptionEndDate == null) {
				if (self.eCommerceViewDetails.subscriptionEndDate != self.eCommerceViewDetails.subscriptionEndDateOrg) {
					isChanged = true;
				}
			} else {
				var newDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionEndDate);
				var oldDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.subscriptionEndDateOrg);
				if (oldDate !== newDate) {
					isChanged = true;
				} else if (self.eCommerceViewDetails.subscription != self.eCommerceViewDetails.subscriptionOrg) {
					isChanged = true;
				}

			}
			return isChanged;
		};
		/**
		 * Check the data change for show on site.
		 *
		 * @returns {boolean} true show on site is changed or not.
		 */
		self.isShowOnSiteChanged = function () {
			var isChanged = false;
			if (self.eCommerceViewDetails.showOnSiteStartDateOrg == null || self.eCommerceViewDetails.showOnSiteEndDateOrg == null) {
				if (self.eCommerceViewDetails.showOnSiteStartDate != self.eCommerceViewDetails.showOnSiteStartDateOrg) {
					isChanged = true;
				} else if (self.eCommerceViewDetails.showOnSiteEndDate != self.eCommerceViewDetails.showOnSiteEndDateOrg) {
					isChanged = true;
				}
			} else {
				var newDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDate);
				var oldDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteStartDateOrg);
				if (oldDate !== newDate) {
					isChanged = true;
				} else {
					newDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDate);
					oldDate = self.convertDateToStringWithYYYYMMDD(self.eCommerceViewDetails.showOnSiteEndDateOrg);
					if (oldDate !== newDate) {
						isChanged = true;
					} else if (self.eCommerceViewDetails.showOnSiteOrg != self.eCommerceViewDetails.showOnSite) {
						isChanged = true;
					}
				}
			}
			return isChanged;
		};
		/**
		 * Check the data change for ProductFullfilmentChanels.
		 *
		 * @returns {boolean} true subscription is changed or not.
		 */
		self.isProductFullfilmentChanelsChanged = function () {
			var isChanged = false;
			if (self.eCommerceViewDetails.productFullfilmentChanelsOrg == null) {
				if (self.eCommerceViewDetails.productFullfilmentChanels != null) {
					isChanged = true;
				}
			} else {
				if (self.eCommerceViewDetails.productFullfilmentChanelsOrg.length != self.eCommerceViewDetails.productFullfilmentChanels.length) {
					isChanged = true;
				} else {
					var oldItem = null;
					for (var i = 0; i < self.eCommerceViewDetails.productFullfilmentChanels.length; i++) {
						oldItem = self.getProductFullfilmentChanelOrg(self.eCommerceViewDetails.productFullfilmentChanels[i]);
						if (oldItem == null) {
							isChanged = true;
							break;
						}
						if (self.eCommerceViewDetails.productFullfilmentChanels[i].effectDate != null &&
							oldItem.effectDate != null) {
							if (!self.isDate1EqualToDate2(self.eCommerceViewDetails.productFullfilmentChanels[i].effectDate, oldItem.effectDate)) {
								isChanged = true;
								break;
							}
						} else {
							if (self.eCommerceViewDetails.productFullfilmentChanels[i].effectDate != oldItem.effectDate) {
								isChanged = true;
								break;
							}
						}
						if (self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDate != null &&
							oldItem.expirationDate != null) {
							if (!self.isDate1EqualToDate2(self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDate, oldItem.expirationDate)) {
								isChanged = true;
								break;
							}
						} else {
							if (self.eCommerceViewDetails.productFullfilmentChanels[i].expirationDate != oldItem.expirationDate) {
								isChanged = true;
								break;
							}
						}
					}
				}
			}
			return isChanged;
		};
		/**
		 * Get ProductFullfilmentChanel from ProductFullfilmentChanel Orginal.
		 *
		 * @param newProductFullfilmentChanel the new ProductFullfilmentChanel.
		 * @returns {*}
		 */
		self.getProductFullfilmentChanelOrg = function (newProductFullfilmentChanel) {
			if (newProductFullfilmentChanel.key != null) {
				if (newProductFullfilmentChanel.key.fullfillmentChanelCode != null &&
					newProductFullfilmentChanel.key.fullfillmentChanelCode.trim().length > 0) {
					var item = null;
					for (var i = 0; i < self.eCommerceViewDetails.productFullfilmentChanelsOrg.length; i++) {
						item = self.eCommerceViewDetails.productFullfilmentChanelsOrg[i];
						if (item.key != null && item.key.fullfillmentChanelCode != null && item.key.fullfillmentChanelCode.trim() === newProductFullfilmentChanel.key.fullfillmentChanelCode.trim()) {
							return item;
						}
					}
				}
			}
			return null;
		};
		/**
		 * Check the panel type is changed or not.
		 *
		 * @returns {boolean}
		 */
		self.isPanelTypeChanged = function () {
			var isChanged = false;
			if (self.eCommerceViewDetails.panelTypeOrg != null) {
				if (self.eCommerceViewDetails.panelType == null || self.eCommerceViewDetails.panelType === undefined) {
					isChanged = true;
				} else if (self.eCommerceViewDetails.panelType.panelTypeCode != self.eCommerceViewDetails.panelTypeOrg.panelTypeCode) {
					isChanged = true;
				}
			}
			return isChanged;
		}
		self.stopWaitingLoading = function (status) {
			self.isWaitingForResponse = status;
		};
		/**
		 * Convert the date to string with format: yyyy-MM-dd.
		 * @param date the date object.
		 * @returns {*} string
		 */
		self.convertDateToStringWithYYYYMMDD = function (date) {
			return $filter('date')(date, 'yyyy-MM-dd');
		};
		/**
		 * Reset data. It will be called when click on reset button.
		 */
		self.reset = function () {
			$rootScope.contentChangedFlag = false;
			self.htmlMode = false;
			self.error = '';
			self.success = '';
			self.eCommerceViewDetails.endDateErrorMgs = '';
			self.eCommerceViewDetails.isEndDateErrorMgs = false;
			self.eCommerceViewDetails.startDateErrorMgs = '';
            self.eCommerceViewDetails.isStartDateErrorMgs = false;
            self.eCommerceViewDetails.romanceCopy = angular.copy(self.eCommerceViewDetails.romanceCopyOrg);
            self.eCommerceViewDetails.favorItemDescription = angular.copy(self.eCommerceViewDetails.favorItemDescriptionOrg);
            self.eCommerceViewDetails.pdpTemplateId = angular.copy(self.eCommerceViewDetails.pdpTemplateIdOrg);
            self.eCommerceViewDetails.subscriptionStartDate = angular.copy(self.eCommerceViewDetails.subscriptionStartDateOrg);
            self.eCommerceViewDetails.subscriptionEndDate = angular.copy(self.eCommerceViewDetails.subscriptionEndDateOrg);
            self.eCommerceViewDetails.subscription = angular.copy(self.eCommerceViewDetails.subscriptionOrg);
            self.eCommerceViewDetails.brand = angular.copy(self.eCommerceViewDetails.brandOrg);
			self.eCommerceViewDetails.displayName = angular.copy(self.eCommerceViewDetails.displayNameOrg);
			self.eCommerceViewDetails.size = angular.copy(self.eCommerceViewDetails.sizeOrg);
			self.eCommerceViewDetails.warnings = angular.copy(self.eCommerceViewDetails.warningsOrg);
			self.eCommerceViewDetails.directions = angular.copy(self.eCommerceViewDetails.directionsOrg);
			self.eCommerceViewDetails.ingredients = angular.copy(self.eCommerceViewDetails.ingredientsOrg);
			self.eCommerceViewDetails.showOnSite = angular.copy(self.eCommerceViewDetails.showOnSiteOrg);
			self.resetShowOnsite();
			self.eCommerceViewDetails.fulfillmentChannels = angular.copy(self.eCommerceViewDetails.fulfillmentChannelsOrg);
			self.eCommerceViewDetails.productFullfilmentChanels = angular.copy(self.eCommerceViewDetails.productFullfilmentChanelsOrg);
			self.eCommerceViewDetails.panelType = angular.copy(self.eCommerceViewDetails.panelTypeOrg);
			self.eCommerceViewDetails.productFullfilmentChanelsRemoveList = [];
			self.eCommerceViewDetails.currentProductFulfillment = null;
			self.validateWarning();
			self.resetEcommerceView();
		};
		/**
		 * Reset start date and end date.
		 */
		self.resetShowOnsite = function () {
			if (self.eCommerceViewDetails.showOnSiteEndDateOrg !== undefined && self.eCommerceViewDetails.showOnSiteEndDateOrg !== null) {
				self.eCommerceViewDetails.showOnSiteEndDate = new Date(self.eCommerceViewDetails.showOnSiteEndDateOrg.replace(/-/g, '\/'));
			}
			else {
				self.eCommerceViewDetails.showOnSiteEndDate = null;
			}
			if (self.eCommerceViewDetails.showOnSiteStartDateOrg !== undefined && self.eCommerceViewDetails.showOnSiteStartDateOrg !== null) {
				self.eCommerceViewDetails.showOnSiteStartDate = new Date(self.eCommerceViewDetails.showOnSiteStartDateOrg.replace(/-/g, '\/'));
			} else {
				self.eCommerceViewDetails.showOnSiteStartDate = null;
			}
		}
		/**
		 * Reset status of ecommerce view components. It will be called when click on reset button.
		 */
		self.resetEcommerceView = function () {
			$rootScope.$broadcast(self.RESET_ECOMMERCE_VIEW);
		};
		/**
		 * Update new date to original object. It will be called after save success to reload the data for eCommerce View.
		 */
		self.updateNewDataToOriginalObject = function () {
			self.eCommerceViewDetails={};
			self.resetEcommerceView();
			$rootScope.$broadcast(self.RELOAD_ECOMMERCE_VIEW);
		};
		/**
		 * Reload ECommerceView. Listen reload ECommerce View event.
		 */
		$scope.$on(self.RELOAD_ECOMMERCE_VIEW, function () {
			self.loadDataInit();
		});
		/**
		 * This method will be called when the publish button clicked.
		 * It will show publish confirmation, if the data is valid or show the invalid data.
		 */
		self.publish = function () {
			self.callbackfunctionAfterSpellCheck = null;
			self.error = '';
			self.success = '';
			if (self.isValidatePublishData()) {
				self.confirmTitle = 'Publish Confirmation';
				self.confirmMessage = 'Are you sure you want to publish the selected Product?';
				$('#confirmPublishModal').modal({backdrop: 'static', keyboard: true});
			} else {
				self.error = 'Product can not be published as mandatory field(s) are not entered.';
			}
			self.validateWarning();
		};
        /**
         * This method will be called when click on yes button of publish confirmation modal to call the method to publish.
         */
        self.agreePublish = function () {
            $('#confirmPublishModal').modal("hide");
            self.publishForHeb();
        };
		/**
		 * This method is used to publish data for heb.
		 */
		self.publishForHeb = function () {
			self.isRequestPublishAfterSave = false;
			// Check the data change.
			if (self.isDataChanged()) {
				/* validate data change to save. */
				if (self.validateDataChanged()) {
					self.isRequestPublishAfterSave = true;
					/* Save data. */
					self.updateECommerceViewInformation();
				}
			} else {
				// check fulfillment.
				self.checkFulfillment();
			}
		};

        /**
         * Request to the server to publish.
         */
        self.doPublish = function () {
            var eCommerceViewDetailsParams = {
                productId: self.productMaster.prodId,
                scanCodeId: self.productMaster.productPrimaryScanCodeId,
                alertId: taskService.getAlertId()
            };
            if (self.currentTab.saleChannel != null && self.currentTab.saleChannel !== undefined) {
                // Set channel code for another tab.
                eCommerceViewDetailsParams.salsChnlCd = self.currentTab.saleChannel.id.trim();
            } else {
                eCommerceViewDetailsParams.salsChnlCd = SAL_CHANNEL_HEB_COM;
            }
            /* Call the service to publish. */
            eCommerceViewApi.publishECommerceViewDataToHebCom(eCommerceViewDetailsParams,
                function (results) {
                    /* Publish success. */
                    self.error = '';
                    self.success = results.message;
                    self.isWaitingForResponse = false;
                    if (productSearchService.getFromPage() == appConstants.HOME) {//navigate to next productId
                        if (self.isLastProduct()) {
                            self.updateNewDataToOriginalObject();
                        } else {
                            $rootScope.$broadcast(NEXT_PRODUCT);
                        }
                    } else {
						$rootScope.$broadcast(self.RELOAD_ECOMMERCE_VIEW);
						setTimeout(function () {
							if(self.isLastProduct()){
								$rootScope.$broadcast("reloadPreviousPageAfterPublish");
							}else {
								$rootScope.$broadcast("reloadCurrentPageAfterPublish");
							}
						}, 2000);
					}
				}
				,
				self.fetchError
			);
		};
		/**
		 * Check fulfillment.
		 */
		self.checkFulfillment = function () {
			self.isWaitingForResponse = true;
			eCommerceViewApi.checkFulfillment({productId: self.productMaster.prodId},
				function (results) {
					if (results.data == true && self.isValidatePublishData()) {
						// publish after check fulfillment success.
						self.doPublish();
					} else {
						self.isWaitingForResponse = false;
						if (results.data == false && !self.isValidatePublishData()) {
							self.error = 'Product can not be published as mandatory fields are not entered, fulfillment channel is not set.';
						} else if (results.data == false) {
							self.error = 'Product can not be published as fulfillment channel is not set.';
						} else {
							self.error = 'Product can not be published as mandatory field(s) are not entered.';
						}
					}
					self.validateWarning();
				}
				,
				self.fetchError
			);
		};
		/**
		 * Validate the data for publish.
		 * @returns {boolean}
		 */
        self.isValidatePublishData = function () {
            if (self.eCommerceViewDetails.size == null || self.eCommerceViewDetails.size == undefined || self.isEmpty(self.eCommerceViewDetails.size.content)) {
                return false;
            } else if (self.eCommerceViewDetails.size.mandatory !== undefined && self.eCommerceViewDetails.size.mandatory) {
                return false;
            }
            if (self.eCommerceViewDetails.brand == null || self.eCommerceViewDetails.brand == undefined || self.isEmpty(self.eCommerceViewDetails.brand.content)) {
                return false;
            } else if (self.eCommerceViewDetails.brand.mandatory !== undefined && self.eCommerceViewDetails.brand.mandatory) {
                return false;
            }
            if (self.eCommerceViewDetails.displayName == null || self.eCommerceViewDetails.displayName == undefined || self.isEmpty(self.eCommerceViewDetails.displayName.content)) {
                return false;
            } else if (self.eCommerceViewDetails.displayName.mandatory !== undefined && self.eCommerceViewDetails.displayName.mandatory) {
                return false;
            }
            if (self.isEmpty(self.eCommerceViewDetails.customerHierarchyPrimaryPath)) {
                return false;
            }
            // Ignore check image mandatory
            if (self.currentTab.id != FAVOR_TAB_ID) {
                if (self.isEmpty(self.eCommerceViewDetails.imagePrimary)) {
                    return false;
                }
                if (self.eCommerceViewDetails.romanceCopy === null ||
                    self.eCommerceViewDetails.romanceCopy === undefined ||
                    self.isEmpty(self.eCommerceViewDetails.romanceCopy.content)) {
                    return false;
                } else if ( self.eCommerceViewDetails.romanceCopy.mandatory !== undefined && self.eCommerceViewDetails.romanceCopy.mandatory) {
                    return false;
                }
            }
            return true;
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
		 * Validate and show warning message.
		 */
		self.validateWarning = function(){
			$rootScope.$broadcast(self.VALIDATE_WARNING);
		};
		/**
		 * Clear message.
		 */
		self.clearMessage = function(){
			self.error = '';
			self.success='';
		};
		/**
		 * Clear message listener.
		 */
		$scope.$on(self.CLEAR_MESSAGE, function() {
			self.clearMessage();
		});


		/**
		 * Handle get information data source basing on product, attribute, sales channel, product primary scan
		 * code. Return the group information include master data extension, attribute priority...ect
		 */
		self.getECommerceViewDataSource = function (productId, scanCodeId, attributeId, salesChannel, headerTitle, typeSource) {
			self.errorPopup = '';
			self.dataSourceTitle = headerTitle;
			self.isWaitingForResponsePopup = true;
			self.currentAttributeId = attributeId;
			self.isEBM = self.isEBMByAttributeId(attributeId);
			self.htmlMode = false;
			$('#dataSourceModalType1').modal({backdrop: 'static', keyboard: true});
			eCommerceViewApi.getECommerceViewDataSource({
					productId: productId,
					scanCodeId: scanCodeId,
					attributeId: attributeId,
					salesChannel: salesChannel
				},
				self.dataSourceResponseSuccess,
				self.fetchErrorPopup
			);
		};

		/**
		 * Response success. Store and handle show data source.
		 * @param result
		 */
		self.dataSourceResponseSuccess = function (result) {
			//handle data to showing
			self.isWaitingForResponsePopup = false;
			self.eCommerceViewAttributePriority = angular.copy(result);
			self.eCommerceViewAttributePriorityOrg = angular.copy(result);
			self.checkSelectedAt = 0;
			self.checkSourceEditableAt = 0;
			self.initDataForReadMoreAndReadLess();
			if(self.isEditText == true){
				if(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails != null){
					for (var i =0; i<self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails.length; i++) {
						if (self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].selected) {
							self.checkSelectedAt = i+1;
						}
						if (self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].sourceEditable) {
							self.checkSourceEditableAt = i+1;
						}
					}
					if(self.checkSourceEditableAt > 0 ){
						self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[self.checkSelectedAt-1].selected = false;
						self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[self.checkSourceEditableAt-1].selected = true;
					}
				}
				self.isEditText = false;
			}
			self.validateSpellCheckForView();
		};

		/**
		 * Init data for read more and read less
		 */
		self.initDataForReadMoreAndReadLess = function () {
			for (var i =0; i<self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails.length; i++) {
				self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].numLimit = self.MIN_LENGTH_READ;
				self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].isReadMore = true;
				self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].isReadLess = false;
			}
		};

		/**
		 * Read more
		 */
		self.readMore = function (eCommerceViewAttributePriorityDetail) {
			for (var i = 0; i < self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails.length; i++) {
				if(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i] === eCommerceViewAttributePriorityDetail){
					self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].numLimit = eCommerceViewAttributePriorityDetail.length;
					self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].isReadMore = false;
					self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].isReadLess = true;
					break;
				}
			}
		};

		/**
		 * Read less
		 */
		self.readLess = function (eCommerceViewAttributePriorityDetail) {
			for (var i = 0; i < self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails.length; i++) {
				if(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i] === eCommerceViewAttributePriorityDetail){
					self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].numLimit = self.MIN_LENGTH_READ;
					self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].isReadLess = false;
					self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].isReadMore = true;
					break;
				}
			}
		};

		/**
		 * Show value for radio button data source selected.
		 * @param selected
		 * @returns {*}
		 */
		self.valueSourceSelectedRadioButton = function (selected) {
			if(selected){
				return selected;
			}
			return !selected;
		};

		/**
		 * Handle change data source selected.
		 * @param index
		 */
		self.dataSourceSelectedChangedHandle = function (index) {
			if(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails != null){
				for (var i =0; i<self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails.length; i++){
					if(i != index){
						self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails[i].selected = false;
					}
				}
			}
		};

		/**
		 * Handle update information data source
		 */
		self.saveDataAttributePriorities = function () {
			self.callbackfunctionAfterSpellCheck = null;
			self.errorPopup = '';
			self.error = '';
			self.success = '';
			if(angular.toJson(self.eCommerceViewAttributePriority) != angular.toJson(self.eCommerceViewAttributePriorityOrg)){
				self.eCommerceViewAttributePriority.productId = self.productMaster.prodId;
				self.eCommerceViewAttributePriority.primaryScanCode = self.productMaster.productPrimaryScanCodeId;
				self.eCommerceViewAttributePriority.attributeId = self.currentAttributeId;
				//correct html text for romance copy attribute
				if(self.currentAttributeId == 1666 || self.currentAttributeId == 1664 || self.currentAttributeId  == 1672 ||
					self.currentAttributeId  == self.LOG_ATTR_ID_FAVOR_ITEM_DESCRIPTION){
					if(self.eCommerceViewAttributePriority != null && self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails != null){
						angular.forEach(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails, function(eCommerceViewPriorityDetails){
							if(eCommerceViewPriorityDetails != null && eCommerceViewPriorityDetails.sourceEditable === true){
								if(self.htmlMode){
									if(eCommerceViewPriorityDetails.htmlContent != null && eCommerceViewPriorityDetails.htmlContent.content != null){
										eCommerceViewPriorityDetails.content.content = angular.copy(eCommerceViewPriorityDetails.htmlContent.content);
									}
								}else{
									if(eCommerceViewPriorityDetails.content != null && eCommerceViewPriorityDetails.content.content != null){
										if(self.currentAttributeId == 1666 || self.currentAttributeId  == self.LOG_ATTR_ID_FAVOR_ITEM_DESCRIPTION){
											//correct html format before save for romance copy attribute
											//format data changed with html tag (br, bullet)
											var contentHtml = $('#romanceCopyPopupDiv').html();
											contentHtml = contentHtml.split("<br>").join("<div>");
											contentHtml = contentHtml.split("</p>").join("<div>");
											var contentHtmlArray = contentHtml.split("<div>");
											var contentNoneHtml = '';
											if(contentHtmlArray.length > 1) {
												for (var i = 0; i < contentHtmlArray.length; i++) {
													if(contentHtmlArray[i] != null && contentHtmlArray[i] != ''){
														contentNoneHtml = contentNoneHtml.concat(self.htmlToPlaintext(contentHtmlArray[i]));
														if(i < (contentHtmlArray.length - 1)){
															contentNoneHtml = contentNoneHtml.concat(" <br> ");
														}
													}
												}
												contentNoneHtml = contentNoneHtml.split("&nbsp;").join(" ");
												var escapeArray = contentNoneHtml.split(" ");
												var content = '';
												for (var i = 0; i < escapeArray.length; i++) {
													if(escapeArray[i] != " " && escapeArray[i] != "<br>"){
														content = content.concat(self.encodeHTMLEntity(escapeArray[i])).concat(" ");
													}else{
														content = content.concat(escapeArray[i]).concat(" ");
													}
												}
												eCommerceViewPriorityDetails.content.content = self.removeBreakTag(content);
											}else if(contentHtmlArray.length == 1) {
												eCommerceViewPriorityDetails.content.content = self.removeBreakTag(self.encodeHTMLEntity(self.htmlToPlaintext(contentHtml)));
											}
										}else {
											eCommerceViewPriorityDetails.content.content = self.encodeHTMLEntity(eCommerceViewPriorityDetails.content.content);
										}
									}
								}
							}
						});
					}
				}
				//handle update information
				eCommerceViewApi.updateECommerceViewDataSource(
					self.eCommerceViewAttributePriority,
					self.dataSourceUpdateResponseSuccess,
					self.fetchError
				);
				$rootScope.$broadcast(self.RELOAD_AFTER_SAVE_POPUP, self.currentAttributeId, false);
			}
			$('#dataSourceModalType1').modal("hide");
		};

		/**
		 * Encode data to HTML entity.
		 * @param data
		 */
		self.encodeHTMLEntity  = function (data) {
			//useNamedReferences => encode by name
			//allowUnsafeSymbols => (&, <, >, ", ', and `) will be ignored, only non-ASCII characters will be encoded.
			var returnString = he.encode(data, {'useNamedReferences': true, 'allowUnsafeSymbols': true});
			//fix like old PM app
			returnString = returnString.replace(/&middot;/g, "&bull;");
			returnString = returnString.replace(/&#x2011;/g, " ");
			returnString = returnString.replace(/"/g, "&quot;");
			return returnString;
		};

		/**
		 * Response success. Store and handle show data source.
		 * @param result
		 */
		self.dataSourceUpdateResponseSuccess = function (result) {
			self.isWaitingForResponse = false;
			self.error = '';
			switch (self.currentAttributeId) {
				case self.LOG_ATTR_ID_ROMANCE_COPY:
					self.success = "Romance copy: "+result.message;
					break;
				case self.LOG_ATTR_ID_BRAND:
					self.success = "Brand: "+result.message;
					break;
				case self.LOG_ATTR_ID_SIZE:
					self.success = "Size: "+result.message;
					break;
				case self.LOG_ATTR_ID_DIRECTION:
					self.success = "Direction: "+result.message;
					break;
				case self.LOG_ATTR_ID_WARNING:
					self.success = "Warning: "+result.message;
					break;
				case self.LOG_ATTR_ID_TAG:
					self.success = "Tag: "+result.message;
					break;
				case self.LOG_ATTR_ID_DISPLAY_NAME:
					self.success = "Display name: "+result.message;
					break;
                case self.LOG_ATTR_ID_FAVOR_ITEM_DESCRIPTION:
                    self.success = "Favor Item Description: "+result.message;
                    break;
				default:
					break;
			}

			$rootScope.$broadcast(self.RELOAD_AFTER_SAVE_POPUP, self.currentAttributeId, true);
		};


		/**
		 * Handle reset information data source
		 */
		self.resetDataAttributePriorities = function () {
			self.errorPopup = '';
			self.eCommerceViewAttributePriority = angular.copy(self.eCommerceViewAttributePriorityOrg);
			self.validateSpellCheckForView();
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchErrorPopup= function (error) {
			self.isWaitingForResponse = false;
			if (error && error.data) {
				if (error.data.message) {
					self.errorPopup = (error.data.message);
				} else if (error.data.error) {
					self.errorPopup = (error.data.error);
				} else {
					self.errorPopup = error;
				}
			}
			else {
				self.errorPopup = "An unknown error occurred.";
			}
		};
		/**
		 * Check the current tab is google or not.
		 *
		 * @returns {boolean} true if current is google.
		 */
		self.isGoogleTab = function(){
			if(self.currentTab.saleChannel != null && self.currentTab.saleChannel !== undefined){
				// Set channel code for another tab.
				if(self.currentTab.saleChannel.id.trim() == '05'){
					return true;
				}
			}
			return false;
		};
		/**
		 * Set spell check is working or not.
		 *
		 * @returns {boolean} true or false.
		 */
		self.changeSpellCheckStatus = function(status){
			self.isSpellCheck = false;
			/// fix bugs App still shows warning pop-up when the user has clicked the Save button.
			if(status){
				self.isSpellCheck = true;
				$rootScope.contentChangedFlag = false;
                if (self.isDataChanged()) {
                    $rootScope.contentChangedFlag = true;
                }
			}
		};

		/**
		 * Get attribute mapping information.
		 * @param attributeId
		 */
		self.findAttributeMappingByLogicalAttribute = function (attributeId) {
			self.isWaitingForResponsePopupAttr = true;
			$('#attributeMapping').modal({backdrop: 'static', keyboard: true});
			eCommerceViewApi.findAttributeMappingByLogicalAttribute(
				{
					attributeId:attributeId
				},
				//success
				function (results) {
					self.isWaitingForResponsePopupAttr = false;
					self.attributeMappingDataProvider = angular.copy(results);
				},
				function (error) {
					self.isWaitingForResponsePopupAttr = false;
					if (error && error.data) {
						if (error.data.message) {
							self.errorPopupAttr = (error.data.message);
						} else if (error.data.error) {
							self.errorPopupAttr = (error.data.error);
						} else {
							self.errorPopupAttr = error;
						}
					}
					else {
						self.errorPopupAttr = "An unknown error occurred.";
					}
				}
			);
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
		 * validate text for edit mode.
		 */
		self.checkErrors = function(){
			var eCommerceViewPriorityDetails = self.getECommerceViewPriorityDetailsByEditableSource();
			if(eCommerceViewPriorityDetails != null && !self.isEmpty(eCommerceViewPriorityDetails.content.content)) {
				self.isWaitingForCheckSpellingResponse= true;
				vocabularyService.validateRomancyTextForView(eCommerceViewPriorityDetails.content.content , function (newText, suggestions) {
					// Use $timeout and space char to clear cache on layout.
					eCommerceViewPriorityDetails.content.content = eCommerceViewPriorityDetails.content.content + ' ';
					$timeout(function () {
						eCommerceViewPriorityDetails.content.content = angular.copy(newText);
						$timeout(function () {
							vocabularyService.createSuggestionMenuContext($scope, suggestions);
							self.isWaitingForCheckSpellingResponse = false;
						}, 1000);
					}, 100);
				});
			}
		};
		/**
		 * Get eCommerceViewPriorityDetails object from edit source.
		 * @returns {*}
		 */
		self.getECommerceViewPriorityDetailsByEditableSource = function(){
			var result = null;
			angular.forEach(self.eCommerceViewAttributePriority.eCommerceViewAttributePriorityDetails, function(eCommerceViewPriorityDetails){
				if(result == null && eCommerceViewPriorityDetails.sourceEditable === true){
					result = eCommerceViewPriorityDetails;
				}
			});
			return result;
		};
		/**
		 * Error message listener.
		 */
		$scope.$on('error_message', function(event, data) {
			self.error = data.error;
		});
		/**
		 * Validate spell check.
		 */
		self.isSpellCheckAttributeId = function(){
			var check = false;
			if(self.LOG_ATTR_ID_ROMANCE_COPY == self.eCommerceViewAttributePriority.attributeId
				|| self.LOG_ATTR_ID_BRAND == self.eCommerceViewAttributePriority.attributeId
				|| self.LOG_ATTR_ID_DISPLAY_NAME == self.eCommerceViewAttributePriority.attributeId
                || self.LOG_ATTR_ID_FAVOR_ITEM_DESCRIPTION == self.eCommerceViewAttributePriority.attributeId){
				check = true;
			}
			return check;
		};
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
		/**
		 * call the search component to search product automatically
		 */
		self.searchAutomaticCustomHierarchy = function(){
			$rootScope.$broadcast('searchAutomaticCustomHierarchy');
		};
		/**
		 * call the search component to search product automatically
		 */
		self.searchAutomaticCustomHierarchy = function(){
			$rootScope.$broadcast('searchAutomaticCustomHierarchy');
		}
		/**
		 * Validate spell check.
		 */
		self.validateSpellCheck = function(){
			if(self.htmlTabPressing){
				self.htmlTabPressing = false;
				return;
			}
			//validation spellcheck
			$timeout(function () {
				if(self.eCommerceViewAttributePriority.attributeId == self.LOG_ATTR_ID_BRAND){
					self.validateBrandTextForEdit();
				}
				/*if(self.eCommerceViewAttributePriority.attributeId == self.LOG_ATTR_ID_DISPLAY_NAME){
					if(!self.clickOnAaOrResetButton) {
						self.validateDisplayNameTextForEdit();
					}
					self.clickOnAaOrResetButton = false;
				}*/
			}, 200);
		};
		/**
		 * Validate spell check of Romance Copy.
		 */
		self.validateSpellCheckRomanceCopy = function(){
			if(self.htmlTabPressing){
				self.htmlTabPressing = false;
				return;
			}
			if(!self.openRomanceSuggestionsPopup){
				self.validateRomanceCopyTextForEdit();
			}
		};
		/**
		 * Disable validate spell check of Romance Copy when open suggestions popup.
		 */
		$scope.$on('context-menu-opened', function (event, args) {
			self.openRomanceSuggestionsPopup = true;
		});
		/**
		 * Enable validate spell check of Romance Copy when close suggestions popup.
		 */
		$scope.$on('context-menu-closed', function (event, args) {
			self.openRomanceSuggestionsPopup = false;
		});
		/**
		 * Validate spell check.
		 */
		self.validateSpellCheckForView = function(){
			//validation spellcheck
			if(self.eCommerceViewAttributePriority.attributeId == self.LOG_ATTR_ID_BRAND){
				self.validateBrandTextForView();
			}
			if(self.eCommerceViewAttributePriority.attributeId == self.LOG_ATTR_ID_DISPLAY_NAME){
				self.validateDisplayNameTextForView();
			}
		};
		/**
		 * Validate camel case of display name.
		 */
		self.doValidateCamelCaseDisplayName = function(){

			if(self.eCommerceViewAttributePriority.attributeId == self.LOG_ATTR_ID_DISPLAY_NAME){
				self.clickOnAaOrResetButton = true;
				self.validateCamelCaseDisplayName();
			}
		};
		/**
		 * Validate text for view mode of brand. It will call after loaded romance description.
		 */
		self.validateBrandTextForView = function(){
			var eCommerceViewPriorityDetails = self.getECommerceViewPriorityDetailsByEditableSource();
			if(eCommerceViewPriorityDetails != null && !self.isEmpty(eCommerceViewPriorityDetails.content.content)) {
				self.isWaitingForCheckSpellingResponse = true;
				vocabularyService.validateBrandTextForView(eCommerceViewPriorityDetails.content.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					eCommerceViewPriorityDetails.content.content = eCommerceViewPriorityDetails.content.content + ' ';
					$timeout(function () {
						eCommerceViewPriorityDetails.content.content = newText;
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponse = false;
					}, 100);
				});
			}
		};
		/**
		 * Validate text for view mode of display name. It will call after loaded display name.
		 */
		self.validateDisplayNameTextForView = function(){
			var eCommerceViewPriorityDetails = self.getECommerceViewPriorityDetailsByEditableSource();
			if(eCommerceViewPriorityDetails != null && !self.isEmpty(eCommerceViewPriorityDetails.content.content)) {
				self.isWaitingForCheckSpellingResponse = true;
				vocabularyService.validateDisplayNameTextForView(eCommerceViewPriorityDetails.content.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					eCommerceViewPriorityDetails.content.content = eCommerceViewPriorityDetails.content.content + ' ';
					$timeout(function () {
						eCommerceViewPriorityDetails.content.content = newText;
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponse = false;
					}, 100);
				});
			}
		};
		/**
		 * validate text for edit mode of brand.
		 */
		self.validateBrandTextForEdit = function(){
			var eCommerceViewPriorityDetails = self.getECommerceViewPriorityDetailsByEditableSource();
			if(eCommerceViewPriorityDetails != null && !self.isEmpty(eCommerceViewPriorityDetails.content.content)) {
				self.isWaitingForCheckSpellingResponse = true;
				vocabularyService.validateBrandTextForEdit(eCommerceViewPriorityDetails.content.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					eCommerceViewPriorityDetails.content.content = eCommerceViewPriorityDetails.content.content + ' ';
					$timeout(function () {
						eCommerceViewPriorityDetails.content.content = newText;
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponse = false;
					}, 100);
				});
			}
		};
		/**
		 * validate text for edit mode of display name.
		 */
		self.validateDisplayNameTextForEdit = function(){
			var eCommerceViewPriorityDetails = self.getECommerceViewPriorityDetailsByEditableSource();
			if(eCommerceViewPriorityDetails != null && !self.isEmpty(eCommerceViewPriorityDetails.content.content)) {
				self.isWaitingForCheckSpellingResponse = true;
				vocabularyService.validateDisplayNameTextForEdit(eCommerceViewPriorityDetails.content.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					eCommerceViewPriorityDetails.content.content = eCommerceViewPriorityDetails.content.content + ' ';
					$timeout(function () {
						eCommerceViewPriorityDetails.content.content = newText;
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponse = false;
					}, 100);
				});
			}
		};
		/**
		 * validate text for edit mode of display name.
		 */
		self.validateCamelCaseDisplayNameOldWay = function(){
			var eCommerceViewPriorityDetails = self.getECommerceViewPriorityDetailsByEditableSource();
			if(eCommerceViewPriorityDetails != null && !self.isEmpty(eCommerceViewPriorityDetails.content.content)) {
				self.isWaitingForCheckSpellingResponse = true;
				vocabularyService.validateCamelCaseDisplayNameOldWay(eCommerceViewPriorityDetails.content.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					eCommerceViewPriorityDetails.content.content = eCommerceViewPriorityDetails.content.content + ' ';
					$timeout(function () {
						eCommerceViewPriorityDetails.content.content = newText;
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponse = false;
					}, 100);
				});
			}
		};
		self.validateCamelCaseDisplayName = function(){
			//if(!self.openSuggestionsPopup){//avoid conflict with suggestion popup
			var eCommerceViewPriorityDetails = self.getECommerceViewPriorityDetailsByEditableSource();
			if(eCommerceViewPriorityDetails != null && !self.isEmpty(eCommerceViewPriorityDetails.content.content)) {
				self.isWaitingForCheckSpellingResponse = true;
				vocabularyService.validateCamelCaseDisplayName(eCommerceViewPriorityDetails.content.content, function(newText, suggestions){
					// Use $timeout and space char to clear cache on layout.
					eCommerceViewPriorityDetails.content.content = eCommerceViewPriorityDetails.content.content + ' ';
					$timeout(function () {
						eCommerceViewPriorityDetails.content.content = angular.copy(newText);
						$timeout(function () {
							vocabularyService.createSuggestionMenuContext($scope, suggestions);
							self.isWaitingForCheckSpellingResponse = false;
						}, 1000);
					}, 100);
				});
			}
			//}
		};
		/**
		 * Validate text for edit mode of romance copy.
		 */
		self.validateRomanceCopyTextForEdit = function(){
			var eCommerceViewPriorityDetails = self.getECommerceViewPriorityDetailsByEditableSource();
			if(eCommerceViewPriorityDetails != null && !self.isEmpty(eCommerceViewPriorityDetails.content.content)) {
				var callbackFunction = self.getAfterSpellCheckCallback();
				var contentHtml = $('#romanceCopyPopupDiv').html();
				contentHtml = contentHtml.split("<br>").join("<div>");
				contentHtml = contentHtml.split("</p>").join("<div>");
				var contentHtmlArray = contentHtml.split("<div>");
				var contentNoneHtml = '';
				if(contentHtmlArray.length > 1) {
					for (var i = 0; i < contentHtmlArray.length; i++) {
						if(contentHtmlArray[i] != null && contentHtmlArray[i] != ''){
							contentNoneHtml = contentNoneHtml.concat(self.htmlToPlaintext(contentHtmlArray[i]));
							if(i < (contentHtmlArray.length - 1)){
								contentNoneHtml = contentNoneHtml.concat(" <br> ");
							}
						}
					}
					eCommerceViewPriorityDetails.content.content = contentNoneHtml.split("&nbsp;").join(" ");
				}

				self.isWaitingForCheckSpellingResponse = true;
				vocabularyService.validateRomancyTextForEdit(eCommerceViewPriorityDetails.content.content, function(newText){
					// Use $timeout and space char to clear cache on layout.
					eCommerceViewPriorityDetails.content.content = eCommerceViewPriorityDetails.content.content + ' ';
					$timeout(function () {
						eCommerceViewPriorityDetails.content.content = newText;
						//vocabularyService.createSuggestionMenuContext($scope, suggestions);
						self.isWaitingForCheckSpellingResponse = false;
						$timeout(function () {
							// Save or publish if event occurs from save or publish button.
							self.processAfterSpellCheck(callbackFunction);
						}, 500);
					}, 100);
				});
			}
		};

		/**
		 * Handle yes button of confirmation modal.
		 */
		self.yesConfirmModal = function(){
			//save data
			self.save();
		};

		/**
		 * Handle no button of confirmation modal.
		 */
		self.noConfirmModal = function(){
			if(self.nextTab !== null){//move to next tab
				self.success = null;
				$scope.active = self.nextTab;
			}
		};

		/**
		 * Handle cancel button of confirmation modal.
		 */
		self.cancelConfirmModal = function(){
			//do nothing
			self.nextTab = null;
			self.success = null;
			self.error = null;
		};

		/**
		 * Change tab handle. Set current tab value.
		 * @param tab
		 */
		self.tabSelected = function (tab) {
			self.eCommerceViewDetails.errorMessages = null;
			self.eCommerceViewDetails.romanceCopyErrorMessages = null;
			self.nextTab = null;
			self.currentTab = tab;
			self.error = '';
		};

		/**
		 * Handle event when tab is deactivated.
		 */
		self.tabDeselect = function ($event, $selectedIndex) {
			if(permissionsService.getPermissions("PD_ECOM_01", "EDIT")){
				var isChanged = false;
                if (self.isDataChanged()) {
                    isChanged = true;
                    $rootScope.contentChangedFlag = true;
                }
				if(isChanged){//data is changed
					if($event){
						$event.preventDefault();
						self.nextTab = $selectedIndex;//save next tab
						$('#ecommerceViewConfirmationModal').modal("show");
					}
				}else{
					//do nothing
					self.nextTab = null;
					self.success = null;
				}
			}
		};

		/**
		 * Remove all tag html
		 * @param text
		 * @returns {string}
		 */
		self.htmlToPlaintext = function(text) {
			return text ? String(text).replace(/<[^>]+>/gm, '') : '';
		};
		/**
		 * Prepare callback function for save after spell check is success.
		 */
		self.savePressing = function(){
			self.callbackfunctionAfterSpellCheck = self.save;
		};
		/**
		 * Prepare callback function for save after spell check is success.
		 */
		self.publishPressing = function(){
			self.callbackfunctionAfterSpellCheck = self.publish;
		};
		/**
		 * Prepare callback function for save after spell check is success.
		 */
		self.saveHebComPressing = function(){
			self.callbackfunctionAfterSpellCheck = self.saveDataAttributePriorities;
		};
		/**
		 * Returns the function to do (save or publish) after spell check success.
		 * @return {null}
		 */
		self.getAfterSpellCheckCallback = function(){
			var callbackFunction = self.callbackfunctionAfterSpellCheck;
			self.callbackfunctionAfterSpellCheck = null;
			return callbackFunction;
		};
		/**
		 * Call the function for save or publish after spell check is success.
		 * @param callback
		 */
		self.processAfterSpellCheck = function(callback){
			if(callback != null && callback !== undefined){
				callback();
			}
		};
		self.isEBMByAttributeId = function(attributeId){
			if(attributeId == self.LOG_ATTR_ID_WARNING){
				// warning
				return  permissionsService.getPermissions('PD_ECOM_05', 'EDIT');
			}else if(attributeId == self.LOG_ATTR_ID_DIRECTION){
				// directions
				return  permissionsService.getPermissions('PD_ECOM_04', 'EDIT');
			}
			return true;
		};

		/**
		 * Set type of text for romance copy field
		 * @param htmlFormat
		 */
		self.changeHTMLMode = function (htmlMode) {
			self.htmlMode = htmlMode;
			self.htmlTabPressing = false;
		};

		/**
		 * Prepare callback function for  after spell check is success.
		 */
		self.htmlTabClick = function(){
			self.htmlTabPressing = true;
		};
		/**
		 * Remove "<br>" tag at the end of string.
		 * @param string
		 * @returns {string}
		 */
		self.removeBreakTag = function(string){
			var text = angular.copy(string);
			var removeData = "<br>";
			if(!self.isEmpty(text)) {
				text = text.trim();
				while (_.endsWith(text, removeData)) {
					text = text.slice(0, -removeData.length);
					if(!self.isEmpty(text)) {
						text = text.trim();
					}
				}
			}
			return text;
		};
	}
})();
