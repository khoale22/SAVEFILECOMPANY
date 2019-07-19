/*
 *   productShelfAttributesComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Product -> Shelf Attributes page component.
 *
 * @author s573181
 * @since 2.6.0
 */

(function () {

	angular.module('productMaintenanceUiApp').component('productShelfAttributes', {
		// isolated scope binding
		bindings: {
			productMaster: '<'
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/productShelfAttributes.html',
		// The controller that handles our component logic
		controller: ProductShelfAttributesController
	});

	ProductShelfAttributesController.$inject = ['ProductInfoApi', 'VocabularyApi', 'UserApi', '$scope', '$rootScope',
		'ProductDetailAuditModal', 'productDetailApi', 'PermissionsService', 'ProductSearchService', '$filter'];

	/**
	 * Product shelf attributes component's controller definition.
	 * @constructor
	 */
	function ProductShelfAttributesController(productInfoApi, vocabularyApi, userApi, $scope, $rootScope,
											  productDetailAuditModal, productDetailApi, permissionService,
											  productSearchService, $filter) {

		/** All CRUD operation controls of Case pack Import page goes here */
		var self = this;

		self.valueDataChanged = false;
		/**
		 * Constant for THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING
		 * @type {string}
		 */
		const THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
		/**
		 * Constant for TAG1
		 * @type {string}
		 */
		const TAG1 = "TAG1";
		/**
		 * Constant for TAG2
		 * @type {string}
		 */
		const TAG2 = "TAG2";
		/**
		 * Constant for ENGLISH
		 * @type {string}
		 */
		const ENGLISH = "ENG";
		/**
		 * Constant for SPANISH
		 * @type {string}
		 */
		const SPANISH = "SPN";
		/**
		 * Constant for COOL
		 * @type {string}
		 */
		const COOL = "COOL";
		/**
		 * Constant for DISTINCTIVE_CODE
		 * @type {string}
		 */
		const DISTINCTIVE_CODE = "00001";
		/**
		 * Constant for PRIMO_PICK_CODE
		 * @type {string}
		 */
		const PRIMO_PICK_CODE = "00002";
		/**
		 * Constant for GO_LOCAL_CODE
		 * @type {string}
		 */
		const GO_LOCAL_CODE = "00004";
		/**
		 * Constant for PRODUCE_CLASS which is 42.
		 * @type {number}
		 */
		const PRODUCE_CLASS = 42;
		/**
		 * Constant for the organic code.
		 * @type {string}
		 */
		const ORGANIC_CODE = "O95";
		/**
		 * Constant for the product maintenance source system.
		 * @type {number}
		 */
		const PRODUCT_MAINTENANCE_SOURCE = 4;
		/**
		 * Constant for the vestcom(8) source system.
		 * @type {number}
		 */
		const VESTCOM_8_SOURCE = 8;
		/**
		 * Constant for PRODUCE_SUB_DEPT which is 09A.
		 * @type {number}
		 */
		const PRODUCE_SUB_DEPT = '09A';
		/**
		 * Constant for the English description.
		 * @type {string}
		 */
		self.ENGLISH_DESCRIPTION_TEXT = "General English product description. 30 characters long.";
        /**
		 * Constant for the Proposed Servicecase Sign Description.
         * @type {string}
         */
        self.PROPOSED_SERVICECASE_SIGN_DESCRIPTION_TEXT = "Please enter the Proposed Servicecase Sign Description within 56 characters";
        /**
		 * Constant for the  Approved Servicecase Sign Description.
         * @type {string}
         */
        self.APPROVED_SERVICECASE_SIGN_DESCRIPTION_TEXT = "Please enter the Approved Servicecase Sign Description within 56 characters";
		/**
		 * Constant for the Spanish description.
		 * @type {string}
		 */
		self.SPANISH_DESCRIPTION_TEXT = "Descripción general español. Puede ser de 30 caracteres de largo.";
		/**
		 * Constant for the English Customer Friendly Description 1 information.
		 * @type {string}
		 */
		self.ENGLISH_CFD1_INFO = "This populates description on Signs and Tags (first line).";
		/**
		 * Constant for the English Customer Friendly Description 2 information.
		 * @type {string}
		 */
		self.ENGLISH_CFD2_INFO = "This populates additional description on Signs and Tags (second line).";
		/**
		 * Constant for the Spanish Customer Friendly Description 1 information.
		 * @type {string}
		 */
		self.SPANISH_CFD1_INFO = "Esta descripción llena de signos y etiquetas (primera línea).";
		/**
		 * Constant for the Spanish Customer Friendly Description 2 information.
		 * @type {string}
		 */
		self.SPANISH_CFD2_INFO = "Esto rellena descripción adicional de Señales y Etiquetas (segunda línea).";
		/**
		 * Constant for the help text.
		 * @type {string}
		 */
		self.HELP_TEXT = "Need a word in all CAPS? Call ext. 88185.";
		/**
		 * String id for html element used for English CFD1 input
		 * @type {string}
		 */
		self.ENGLISH_CFD1_INPUT_ID = "englishCustomerFriendlyDescriptionOne";
		/**
		 * String id for html element used for English CFD2 input
		 * @type {string}
		 */
		self.ENGLISH_CFD2_INPUT_ID = "englishCustomerFriendlyDescriptionTwo";
		/**
		 * String id for html element used for Spanish CFD1 input
		 * @type {string}
		 */
		self.SPANISH_CFD1_INPUT_ID = "spanishCustomerFriendlyDescriptionOne";
		/**
		 * String id for html element used for Spanish CFD2 input
		 * @type {string}
		 */
		self.SPANISH_CFD2_INPUT_ID = "spanishCustomerFriendlyDescriptionTwo";
		/**
		 * String id for html element used for COOL input
		 * @type {string}
		 */
		self.COOL_INPUT_ID = "coolDescription";
		/**
		 * String id for html element used for Service Case Callout input
		 * @type {string}
		 */
		self.SERVICE_CASE_CALLOUT_ID = "serviceCaseCalloutDescription";
		/**
		 * Need more info
		 * @type {string}
		 */
		self.NEED_MORE_INFO = "Need more info";
		/**
		 * Does not meet Primo Pick criteria
		 * @type {string}
		 */
		self.DOES_NOT_MEET_PRIMO_PICK = "Does not meet Primo Pick criteria";
		/**
		 * Reject status
		 * @type {string}
		 */
		self.REJECT_STATUS = 'R';
		/**
		 * Primo pick Approved status.
		 * @type {string}
		 */
		self.APPROVED_STATUS = 'A';
		/**
		 * Primo pick submitted status
		 * @type {string}
		 */
		self.SUBMITTED_STATUS = 'S';
		/**
		 * Object for English CFD1
		 * @type {string}
		 */
		self.englishCustomerFriendlyDescription1 = null;
		self.defaultEnglishCustomerFriendlyDescription1 = {
			description: null,
			key: {
				productId: null,
				languageType: null,
				descriptionType: null
			},
			id: self.ENGLISH_CFD1_INPUT_ID
		};
		/**
		 * Object for English CFD2
		 * @type {string}
		 */
		self.englishCustomerFriendlyDescription2 = null;
		self.defaultEnglishCustomerFriendlyDescription2 = {
			description: null,
			key: {
				productId: null,
				languageType: null,
				descriptionType: null
			},
			id: self.ENGLISH_CFD2_INPUT_ID
		};
		/**
		 * Object for Spanish CFD1
		 * @type {string}
		 */
		self.spanishCustomerFriendlyDescription1 = null;
		self.defaultSpanishCustomerFriendlyDescription1 = {
			description: null,
			key: {
				productId: null,
				languageType: null,
				descriptionType: null
			},
			id: self.SPANISH_CFD1_INPUT_ID
		};
		/**
		 * Object for Spanish CFD2
		 * @type {string}
		 */
		self.spanishCustomerFriendlyDescription2 = null;
		self.defaultSpanishCustomerFriendlyDescription2 = {
			description: null,
			key: {
				productId: null,
				languageType: null,
				descriptionType: null
			},
			id: self.SPANISH_CFD2_INPUT_ID
		};
		/**
		 * Object for COOL
		 * @type {string}
		 */
		self.coolDescription = null;
		self.defaultCoolDescription = {
			description: null,
			key: {
				productId: null,
				languageType: null,
				descriptionType: null
			},
			id: self.COOL_INPUT_ID
		};
		/**
		 * Object for Service Case Callout
		 * @type {string}
		 */
		self.serviceCaseCalloutDescription = null;
		self.defaultServiceCaseCallout = {
			description: null,
			key: {
				productId: null,
				languageType: null,
				descriptionType: null
			},
			id: self.SERVICE_CASE_CALLOUT_ID
		};
		/**
		 * Empty Array for all of the customer friendly descriptions.
		 * @type {Array}
		 */
		self.customerFriendlyDescriptionList = [];
		/**
		 * The original english customer friendly description 1.
		 * @type {null}
		 */
		self.originalEnglishCustomerFriendlyDescription1 = null;
		/**
		 * The original english customer friendly description 2.
		 * @type {null}
		 */
		self.originalEnglishCustomerFriendlyDescription2 = null;
		/**
		 * The original spanish customer friendly description 1.
		 * @type {null}
		 */
		self.originalSpanishCustomerFriendlyDescription1 = null;
		/**
		 * The original spanish customer friendly description 2.
		 * @type {null}
		 */
		self.originalSpanishCustomerFriendlyDescription2 = null;
		/**
		 * The original cool description.
		 * @type {null}
		 */
		self.originalCoolDescription = null;
		/**
		 * The original service case callout.
		 * @type {null}
		 */
		self.originalServiceCaseCallout = null;
		/**
		 * String for primo pick status
		 * @type {string}
		 */
		self.primoPickStatus = "";
		/**
		 * Boolean for whether or not product is a primo pick
		 * @type {boolean}
		 */
		self.isPrimoPick = false;

		self.primoPickStatusId = null;
		/**
		 * Seleted reason code
		 * @type {String}
		 */
		self.reasonCode = null;

		/**
		 * String for  primo pick short description
		 * @type {string}
		 */
		self.primoPickShortDescription = "";
		/**
		 * String for  primo pick long description
		 * @type {string}
		 */
		self.primoPickLongDescription = "";
		/**
		 * Boolean for whether or not product is distinctive
		 * @type {boolean}
		 */
		self.isDistinctive = false;
		self.originalDistinctive = false;

		/**
		 * Date for start of primo pick.
		 * @type {date}
		 */
		self.startDate = null;
		/**
		 * Date for end of primo pick.
		 * @type {date}
		 */
		self.endDate = null;
		/**
		 * Current date for start of primo pick.
		 * @type {date}
		 */
		self.currentStartDate = null;
		/**
		 * Current date for start of primo pick.
		 * @type {date}
		 */
		self.currentEndDate = null;
		/**
		 * Boolean for whether or not product is go local.
		 * @type {boolean}
		 */
		self.isGoLocal = false;
		/**
		 * If the startDatePicker is opened.
		 * @type {boolean}
		 */
		self.startDatePickerOpened = false;
		/**
		 * If the endDatePicker is opened.
		 * @type {boolean}
		 */
		self.endDatePickerOpened = false;

		/**
		 * Keeps track of current request.
		 * @type {number}
		 */
		self.latestRequest = 0;
		/**
		 * Returns convertDate(date) function from higher scope.
		 * @type {function}
		 */
		self.convertDate = $scope.$parent.$parent.$parent.$parent.convertDateWithSlash;
		self.dateToJson = $scope.$parent.$parent.$parent.$parent.dateToJson;

		/**
		 * Nutritional value list for the front end to display.
		 * @type {Array}
		 */
		self.nutritionalClaimsValueList = [];

		/**
		 * Nutritional value list for the front end to display.
		 * @type {Array}
		 */
		self.nutritionalClaimsValueListOrig = [];

		/**
		 * Sample organic object to add to the list if it isn't organic and the source system is not coming from
		 * vestcom. This way it shows ORGANIC and it is shown as an empty checkbox.
		 * @type {{description: string, abbreviation: string, id: string}}
		 */
		self.sampleOrganicObject = {
			description: "ORGANIC",
			abbreviation: "ORGANIC",
			id: "O95"
		};

        /**
         * Whether or not it is organic or not.
         * @type {null}
         */
        self.isOrganic = false;

		/**
		 * Whether or not to show the healthy aisle.
		 * @type {boolean}
		 */
		self.showHealthyAisle = false;

		self.originalProductMaster=null;

		self.primoPickStatusList = [
			{id: 'S', description: 'Submitted'},
			{id: 'A', description: 'Approved'},
			{id: 'R', description: 'Rejected'}
		];

		self.serviceCaseSignStatusList = [
			{id: '111', description: 'Submitted'},
			{id: '108', description: 'Approved'},
			{id: '105', description: 'Rejected'}
		];

		self.serviceCaseSignDescription = null;

		self.tagTypes = null;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		};
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onChanges = function () {
			self.getData();
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};
		/**
		 * Gets the bicep vendor import data.
		 */
		self.getData = function () {
			productInfoApi.getListOfDescriptionTypes({}, self.loadDescriptionTypes, self.fetchError);
			productInfoApi.getServiceCaseTagData(self.productMaster.prodId, self.setServiceCaseTagData, self.fetchError);
			productInfoApi.queryTagTypes({}, function(result) {self.tagTypes = result; self.setTagType()}, self.fetchError);
			self.getProductMaster();
		};

		/**
		 * Get product master data.
		 */
		self.getProductMaster = function () {
			self.success = null;
			self.error = null;
			self.isLoading = true;
			productDetailApi.getUpdatedProduct(
				{
					prodId: self.productMaster.prodId
				},
				//success case
				function (result) {
					self.isLoading = false;
					self.productMaster = angular.copy(result);
					self.originalProductMaster = angular.copy(result);
					self.setData();
				}, self.fetchError);
		};
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isLoading = false;
			self.primoPickStatusId = angular.copy(self.originalPrimoPickStatusId);
			if (error && error.data) {
				if (error.data.message) {
					self.setError(error.data.message);
				} else {
					self.setError(error.data.error);
				}
			}
			else {
				self.setError("An unknown error occurred.");
			}
		};

		/**
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		self.setError = function (error) {
			self.error = error;
		};

		self.setData = function () {
			if(self.isOrganic !== false) {
				self.isOrganic = false;
			}
			self.setDescriptions();
			self.setPrimoData();
			self.setDatePickers();
			self.setNutritionalValues();
			if (self.productMaster.tagItemType.trim() === "ITMCD") {
				self.tagItemType = "WHS";
			} else {
				self.tagItemType = self.productMaster.tagItemType;
			}
			self.setTagType();
			self.isLoading = false;
		};

		/**
		 * Constructs a user-friendly tag-type description.
		 */
		self.setTagType = function() {
			if (self.productMaster !== null
					&& self.productMaster.goodsProduct !== null
					&& self.tagTypes !== null) {
				for (var i = 0; i < self.tagTypes.length; i++) {
					if (self.tagTypes[i].tagTypeCode == self.productMaster.goodsProduct.tagType) {
						self.tagTypeDescription = self.tagTypes[i].tagTypeDescription;
					}
				}
			}
		};

		/**
		 * Handles Status Changes for Service Case Tag
		 */
		self.handleServiceCaseStatus = function (status) {
			self.serviceCaseTagStatus = status;
		};

		/**
		 * This will load and find the description types of the description lengths of the customer friendly descriptions
		 * for tags 1 and tags 2.
		 *
		 * @param results
		 */
		self.loadDescriptionTypes = function(results) {
			self.descriptionTypeList = [];
			self.descriptionTypeList = results;
			angular.forEach(self.descriptionTypeList, function (descriptionType, key) {
				switch(descriptionType.id.trim()) {
					case "TAG1": {
						document.getElementById(self.ENGLISH_CFD1_INPUT_ID).maxLength = descriptionType.descriptionLength - 1;
						document.getElementById(self.SPANISH_CFD1_INPUT_ID).maxLength = descriptionType.descriptionLength - 1;
						break;
					}
						break;
					case "TAG2": {
						document.getElementById(self.ENGLISH_CFD2_INPUT_ID).maxLength = descriptionType.descriptionLength - 1;
						document.getElementById(self.SPANISH_CFD2_INPUT_ID).maxLength = descriptionType.descriptionLength - 1;
						break;
					}
					default:
						break;
				}
			})
		};

		/**
		 * Sets All of the descriptions.
		 * englishCustomerFriendlyDescription1 is the first line of the english customer friendly description
		 * englishCustomerFriendlyDescription2 is the second line of the english customer friendly description
		 * spanishCustomerFriendlyDescription1 is the first line of the spanish customer friendly description
		 * spanishCustomerFriendlyDescription2 is the second line of the spanish customer friendly description
		 * primoPickShortDescription Primo Pick short description which is a used in flyers.
		 * primoPickLongDescription Primo Pick long or "romance copy" a more complete description of a product.
		 * cool Country of Origin either manufactured, distributed from or housed.
		 */
		self.setDescriptions = function () {
			// clear out everything set here

			self.primoPickShortDescription = null;
			self.primoPickLongDescription = null;
			self.originalPrimoPickShortDescription = null;
			self.originalPrimoPickLongDescription = null;
			self.originalEnglishCustomerFriendlyDescription1 = null;
			self.englishCustomerFriendlyDescription1 = angular.copy(self.defaultEnglishCustomerFriendlyDescription1);
			self.originalSpanishCustomerFriendlyDescription1 = null;
			self.spanishCustomerFriendlyDescription1 = angular.copy(self.defaultSpanishCustomerFriendlyDescription1);
			self.originalEnglishCustomerFriendlyDescription2 = null;
			self.englishCustomerFriendlyDescription2 = angular.copy(self.defaultEnglishCustomerFriendlyDescription2);
			self.originalSpanishCustomerFriendlyDescription2 = null;
			self.spanishCustomerFriendlyDescription2 = angular.copy(self.defaultSpanishCustomerFriendlyDescription2);
			self.coolDescription = angular.copy(self.defaultCoolDescription);
			self.originalCoolDescription = null;
			self.originalServiceCaseCallout = null;
			self.serviceCaseCalloutDescription = angular.copy(self.defaultServiceCaseCallout);

			angular.forEach(self.productMaster.productDescriptions, function (description, key) {
				switch (description.key.descriptionType.trim()) {
					case "TAG1":
						if (angular.equals(description.key.languageType.trim(), "ENG")) {
							self.originalEnglishCustomerFriendlyDescription1 = angular.copy(description.description);
							self.englishCustomerFriendlyDescription1 = {
								description: description.description,
								key: description.key,
								id: self.ENGLISH_CFD1_INPUT_ID
							}
						}
						if (angular.equals(description.key.languageType.trim(), "SPN")) {
							self.originalSpanishCustomerFriendlyDescription1 = angular.copy(description.description);
							self.spanishCustomerFriendlyDescription1 = {
								description: description.description,
								key: description.key,
								id: self.SPANISH_CFD1_INPUT_ID
							}
						}
						break;
					case "TAG2":
						if (angular.equals(description.key.languageType.trim(), "ENG")) {
							self.originalEnglishCustomerFriendlyDescription2 = angular.copy(description.description);
							self.englishCustomerFriendlyDescription2 = {
								description: description.description,
								key: description.key,
								id: self.ENGLISH_CFD2_INPUT_ID
							}
						}
						if (angular.equals(description.key.languageType.trim(), "SPN")) {
							self.originalSpanishCustomerFriendlyDescription2 = angular.copy(description.description);
							self.spanishCustomerFriendlyDescription2 = {
								description: description.description,
								key: description.key,
								id: self.SPANISH_CFD2_INPUT_ID
							}
						}
						break;
					case "COOL":
						self.originalCoolDescription = angular.copy(description.description);
						self.coolDescription = {
							description: description.description,
							key: description.key,
							id: self.COOL_INPUT_ID
						}
						break;
					case "PRIMS":
						self.originalPrimoPickShortDescription = description.description;
						self.primoPickShortDescription = description.description;
						break;
					case "PRIML":
						self.originalPrimoPickLongDescription = description.description;
						self.primoPickLongDescription = description.description;
						break;
					case "SRVCC":
						self.originalServiceCaseCallout = angular.copy(description.description);
						self.serviceCaseCalloutDescription = {
							description: description.description,
							key: description.key,
							id: self.SERVICE_CASE_CALLOUT_ID
						};
						break;
					default:
						break;
				}
			})

		};

		/**
		 * Sets the primo data.
		 */
		self.setPrimoData = function () {
			// Reset everything this function my change.
			self.primoPickStatus = null;
			self.originalPrimoPickStatus = null;
			self.primoPickStatusId = null;
			self.reasonCode = null;
			self.originalPrimoPickStatusId = null;
			self.originalReasonCode = null;

			self.isGoLocal = false;
			self.originalIsGoLocal = false;
			self.isDistinctive = false;
			self.originalDistinctive = false;
			self.startDate = null;
			self.endDate = null;
			self.isPrimoPick = self.productMaster.primoPick;
			if (self.productMaster.productMarketingClaims !== null && self.productMaster.productMarketingClaims.length > 0) {
				for (var x = 0; x < self.productMaster.productMarketingClaims.length; x++) {
					if (self.productMaster.productMarketingClaims[x].key.marketingClaimCode === PRIMO_PICK_CODE) {
						self.primoPickStatus = self.getPrimoStatus(self.productMaster.productMarketingClaims[x].marketingClaimStatusCode);
						self.originalPrimoPickStatus = self.primoPickStatus;
						self.primoPickStatusId = self.primoPickStatus.id;
						self.originalPrimoPickStatusId = self.primoPickStatusId;
						if(self.productMaster.productMarketingClaims[x].statusChangeReason != null){
							self.reasonCode = self.productMaster.productMarketingClaims[x].statusChangeReason.trim();
							self.originalReasonCode = self.reasonCode;
						}

						if (angular.equals(self.primoPickStatus.id, "A")) {
							self.startDate = self.productMaster.productMarketingClaims[x].promoPickEffectiveDate;
							self.endDate = self.productMaster.productMarketingClaims[x].promoPickExpirationDate;
						}
					}
					if (self.productMaster.productMarketingClaims[x].key.marketingClaimCode === DISTINCTIVE_CODE) {
						self.isDistinctive = true;
						self.originalDistinctive = true;
					}
					if (self.productMaster.productMarketingClaims[x].key.marketingClaimCode === GO_LOCAL_CODE) {
						self.isGoLocal = true;
						self.originalIsGoLocal = true;
					}
				}
			}
		};

		/**
		 * Calls all the set date picker functions.
		 */
		self.setDatePickers = function () {
			self.setDateForStartDatePicker();
			self.setDateForEndDatePicker();
		};

		/**
		 * Sets the current start date.
		 */
		self.setDateForStartDatePicker = function () {
			self.startDatePickerOpened = false;
			if (self.startDate !== null) {
				self.currentStartDate =
					new Date(self.startDate.substring(0, 10).replace(/-/g, '\/'));
			} else {
				self.currentStartDate = null;
			}
			self.originalStartDate = self.currentStartDate;
		};

		/**
		 * Open the primo pick start date picker to select a new date.
		 */
		self.openStartDatePicker = function () {
			self.startDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Open the primo pick end date picker to select a new date.
		 */
		self.openEndDatePicker = function () {
			self.endDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Sets the current duty confirmation date.
		 */
		self.setDateForEndDatePicker = function () {
			self.endDatePickerOpened = false;
			if (self.endDate !== null) {
				self.currentEndDate =
					new Date(self.endDate.substring(0, 10).replace(/-/g, '\/'));
			} else {
				self.currentEndDate = null;
			}
			self.originalEndDate = self.currentEndDate;
		};

		/**
		 * Open the FreightConfirmed picker to select a new date.
		 */
		self.openFreightConfirmationDatePicker = function () {
			self.endDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Uses the results from API call to populate Service Case Tag Section
		 * @param results
		 */
		self.setServiceCaseTagData = function (results){
			self.serviceCaseTagData = {
				productId: results.productId,
				status: results.status,
				proposedDescription: results.proposedDescription,
				approvedDescription: results.approvedDescription
			};
			self.serviceCaseSignDescription = null;
			self.serviceCaseTagDataModified = angular.copy(self.serviceCaseTagData);

			for (var i = 0; i < self.serviceCaseSignStatusList.length; i++) {
				if (self.serviceCaseSignStatusList[i].id == results.status) {
					self.serviceCaseSignDescription = self.serviceCaseSignStatusList[i].description;
				}
			}
			if (self.serviceCaseSignDescription == null) {
				self.serviceCaseSignDescription = results.status;
			}
		};

		/**
		 * Returns the Primo Status based on the primo code.
		 * @param status
		 * @returns {*}
		 */
		self.getPrimoStatus = function (status) {
			switch (status.trim()) {
				case "A": {
					return self.primoPickStatusList[1];
				}
				case "R": {
					return self.primoPickStatusList[2];
				}
				case "S": {
					return self.primoPickStatusList[0];
				}
				default: {
					return null;
				}
			}
		};

		/**
		 * After a customer friendly description has changed, it must be validated against a dictionary in order for
		 * the user to save the shelf attributes. This function sets a variable 'needsCustomerFriendlyValidation' on
		 * the html input for the customer friendly description to true when a user changes a customer friendly
		 * description.
		 *
		 * @param customerFriendlyDescriptionId the id of the customer friendly description that has changed.
		 */
		self.customerFriendlyDescriptionChanged = function (customerFriendlyDescriptionId){
			self.valueDataChanged = true;
			var customerFriendlyDescriptionElement = document.getElementById(customerFriendlyDescriptionId);
			customerFriendlyDescriptionElement.needsCustomerFriendlyValidation = true;
			customerFriendlyDescriptionElement.isValid = false;
			customerFriendlyDescriptionElement.hasError = false;
		};

		/**
		 * Validates a customer friendly description based on available dictionary words. This is for Shelf Attributes:
		 * English Customer Friendly Desc-1,
		 * English Customer Friendly Desc-2,
		 * Spanish Customer Friendly Desc-1, and
		 * Spanish Customer Friendly Desc-2
		 * This is triggered when user leaves the input containing the updated value, and only should call back end if
		 * the user has changed the value, which would be true when
		 * (html input for customer friendly description).needsCustomerFriendlyValidation === true.
		 *
		 * @param blurEvent Blur event after user leaves one of the customer friendly descriptions.
		 */
		self.validateCustomerFriendlyDescription = function (blurEvent, descriptionObject) {
			if (blurEvent.currentTarget.needsCustomerFriendlyValidation === true) {
				blurEvent.currentTarget.isWaitingForResponse = true;
				var request = ++self.latestRequest;
				var response = vocabularyApi.validateText({textToValidate: blurEvent.currentTarget.value});
				response.$promise.then(
					function (result) {
						blurEvent.currentTarget.value = result.validatedText;
						if (request === self.latestRequest) {
							descriptionObject.description = result.validatedText;
							blurEvent.currentTarget.needsCustomerFriendlyValidation = false;
							blurEvent.currentTarget.isWaitingForResponse = false;
							blurEvent.currentTarget.isValid = true;
						}
					}, function (error) {
						self.fetchError(error);
						blurEvent.currentTarget.hasError = true;
						blurEvent.currentTarget.isWaitingForResponse = false;
					}
				);
			}
		};

		/**
		 * This is a helper method to determine if the customer friendly description has changed. This
		 * will occur when the user has made a change, but before calling the back end to wait for a response.
		 *
		 * @param customerFriendlyDescriptionId the id of the customer friendly description that needs to be validated.
		 * @returns {boolean} Return true if the customer friendly description has changed, false otherwise.
		 */
		self.hasCustomerFriendlyDescriptionChanged = function (customerFriendlyDescriptionId) {
			var customerFriendlyDescriptionElement = document.getElementById(customerFriendlyDescriptionId);
			return customerFriendlyDescriptionElement !== null &&
				customerFriendlyDescriptionElement.needsCustomerFriendlyValidation === true &&
				customerFriendlyDescriptionElement.isWaitingForResponse !== true &&
				customerFriendlyDescriptionElement.isValid !== true &&
				customerFriendlyDescriptionElement.hasError !== true;
		};

		/**
		 * This is a helper method to determine if the customer friendly description is waiting for back end validation.
		 * This will occur when the user has made a change and clicked out of the input to trigger the back end call,
		 * and the back end has not responded.
		 *
		 * @param customerFriendlyDescriptionId the id of the customer friendly description that needs to be validated.
		 * @returns {boolean} Return true if the customer friendly description is waiting for for a response,
		 * false otherwise.
		 */
		self.isCustomerFriendlyDescriptionWaitingForValidation = function (customerFriendlyDescriptionId) {
			var customerFriendlyDescriptionElement = document.getElementById(customerFriendlyDescriptionId);
			return customerFriendlyDescriptionElement !== null &&
				customerFriendlyDescriptionElement.needsCustomerFriendlyValidation === true &&
				customerFriendlyDescriptionElement.hasError === false &&
				customerFriendlyDescriptionElement.isValid !== true &&
				customerFriendlyDescriptionElement.isWaitingForResponse === true;
		};

		/**
		 * This is a helper method to determine if the customer friendly description has completed validation. This
		 * will occur when the user has made a change, and the back end has returned the validated data.
		 *
		 * @param customerFriendlyDescriptionId the id of the customer friendly description that needs to be validated.
		 * @returns {boolean} Return true if the customer friendly description is valid, false otherwise.
		 */
		self.isCustomerFriendlyDescriptionValid = function (customerFriendlyDescriptionId) {
			var customerFriendlyDescriptionElement = document.getElementById(customerFriendlyDescriptionId);
			return customerFriendlyDescriptionElement !== null &&
				customerFriendlyDescriptionElement.isValid === true;
		};

		/**
		 * This is a helper method to determine if the customer friendly description had a validation error. This
		 * will occur when the back end has thrown an error when validating the customer friendly description. This is
		 * used to prevent a user save after error occurs.
		 *
		 * @param customerFriendlyDescriptionId the id of the customer friendly description to check for error.
		 * @returns {boolean} Return true if the customer friendly description has error, false otherwise.
		 */
		self.doesCustomerFriendlyDescriptionHaveError = function (customerFriendlyDescriptionId) {
			var customerFriendlyDescriptionElement = document.getElementById(customerFriendlyDescriptionId);
			return customerFriendlyDescriptionElement !== null &&
				customerFriendlyDescriptionElement.hasError === true;
		};

		/**
		 * This is a helper method to run a callback function for each of the customer friendly descriptions. For
		 * example: if you want to disable the save when any customer friendly description has been changed, sending
		 * this function 'self.hasCustomerFriendlyDescriptionChanged' will perform that method on all of the four
		 * customer friendly descriptions. If callback(any of the four customer friendly descriptions) is true,
		 * return true. Otherwise, return false.
		 *
		 * @param callbackFunction Callback function to perform on all of the customer friendly descriptions.
		 * @returns {boolean} Return true if any of the callback(anyCustomerFriendlyDescription) is true,
		 * false otherwise.
		 */
		self.callBackForEachCustomerFriendlyDescription = function (callbackFunction) {
			if (callbackFunction(self.ENGLISH_CFD1_INPUT_ID) ||
				callbackFunction(self.ENGLISH_CFD2_INPUT_ID) ||
				callbackFunction(self.SPANISH_CFD1_INPUT_ID) ||
				callbackFunction(self.SPANISH_CFD2_INPUT_ID)) {
				return true;
			}
			return false;
		};

		/**
		 * This method checks to see if the save button should be disabled. It will be disabled when any of the
		 * customer friendly descriptions have been changed, but haven't been validated. It will also be disabled if
		 * any of the customer friendly descriptions have an error. It will also be disabled if any of the customer
		 * friendly descriptions are waiting for a back end response.
		 *
		 * @returns {boolean} Return true if any customer friendly description needs validation, false otherwise.
		 */
		self.isSaveDisabled = function () {
			$rootScope.contentChangedFlag = self.checkDataChanged();
			//if any customer friendly descriptions has changed, save should be disabled
			if (self.callBackForEachCustomerFriendlyDescription(self.hasCustomerFriendlyDescriptionChanged)) {
				$rootScope.contentChangedFlag = true;
				return true;
			}

			//if any customer friendly descriptions have an error, save should be disabled
			if (self.callBackForEachCustomerFriendlyDescription(self.doesCustomerFriendlyDescriptionHaveError)) {
				$rootScope.contentChangedFlag = true;
				return true;
			}

			//if any customer friendly descriptions is waiting for a response, save should be disabled
			if (self.callBackForEachCustomerFriendlyDescription(self.isCustomerFriendlyDescriptionWaitingForValidation)) {
				$rootScope.contentChangedFlag = true;
				return true;
			}
			return !self.valueDataChanged;
		};

		/**
		 * Checks to see whether or not the descriptions have been changed or not and if they have been changed then they
		 * are added to an updateArray list to be sent to the back end.
		 */
		self.updateDescriptions = function (productMaster) {

			// If the english description changed, set it.
			if(self.hasValueChanged(self.productMaster.description, self.originalProductMaster.description)) {
				productMaster.description = self.productMaster.description;
			}

			// If the spanish description has changed set it.
			if(self.hasValueChanged(self.productMaster.spanishDescription, self.originalProductMaster.spanishDescription)) {
				productMaster.spanishDescription = self.productMaster.spanishDescription;
			}


			// Push each customer friendly object to the list
			self.customerFriendlyDescriptionList.push(angular.copy(self.englishCustomerFriendlyDescription1));
			self.customerFriendlyDescriptionList.push(angular.copy(self.englishCustomerFriendlyDescription2));
			self.customerFriendlyDescriptionList.push(angular.copy(self.spanishCustomerFriendlyDescription1));
			self.customerFriendlyDescriptionList.push(angular.copy(self.spanishCustomerFriendlyDescription2));
			self.customerFriendlyDescriptionList.push(angular.copy(self.serviceCaseCalloutDescription));
			self.customerFriendlyDescriptionList.push(angular.copy(self.coolDescription));
			self.updatePrimoPickStatus = null;

			// The array to hold all of the descriptions that have been updated.
			var updateArray = [];

			if (self.hasValueChanged(self.originalPrimoPickStatusId, self.primoPickStatusId) ||
				self.hasValueChanged(self.originalReasonCode, self.reasonCode) ||
				self.hasValueChanged(self.originalEndDate, self.currentEndDate) ||
				self.hasValueChanged(self.originalStartDate, self.currentStartDate) ||
				self.hasValueChanged(self.originalPrimoPickLongDescription, self.primoPickLongDescription) ||
				self.hasValueChanged(self.originalPrimoPickShortDescription, self.primoPickShortDescription) ||
				self.hasValueChanged(self.originalDistinctive, self.isDistinctive)
			) {
				// If they've changed the primo pick description and the status is not approved, that's an error.
				if (self.primoPickStatusId != self.primoPickStatusList[1].id &&
					self.hasValueChanged(self.originalPrimoPickShortDescription, self.primoPickShortDescription)) {
					self.error = "Primo Pick shelf tag can only be modified for approved Primo Picks";
					return false;
				}

				// Figure out what the primo pic status should be, there are times when it's complicated.
				var newPrimoPickStatus = null;
				if (self.primoPickStatusId != null) {
					newPrimoPickStatus = self.primoPickStatusId;
				} else {
					// It's null, see if the proposed description is not empty and product is distinctive,
					// that means it's going to submitted.
					if (self.isDistinctive && self.primoPickLongDescription != undefined &&
						self.primoPickLongDescription != null && self.primoPickLongDescription !== '') {
						newPrimoPickStatus = self.primoPickStatusList[0].id;
					}
					// Otherwise, it's just the distinctive that's changed.
				}
				productMaster.primoPickProperties = {
					distinctive: self.isDistinctive,
					primoPickProposedDescription: self.primoPickLongDescription,
					primoPickDescription: self.primoPickShortDescription,
					primoPickStatus: newPrimoPickStatus,
					primoPickEffectiveDate: self.dateToJson(self.currentStartDate),
					primoPickExpirationDate: self.dateToJson(self.currentEndDate),
					statusChangeReason : self.reasonCode
				};

				if (self.hasValueChanged(self.originalPrimoPickLongDescription, self.primoPickLongDescription)) {
					updateArray.push({
						description: self.primoPickLongDescription,
						key: {
							productId: self.productMaster.prodId,
							languageType: 'ENG',
							descriptionType: 'PRIML'
						},
						id: 'PRIML'
					});
				}
				if (self.hasValueChanged(self.originalPrimoPickShortDescription, self.primoPickShortDescription)) {
					updateArray.push({
						description: self.primoPickShortDescription,
						key: {
							productId: self.productMaster.prodId,
							languageType: 'ENG',
							descriptionType: 'PRIMS'
						},
						id: 'PRIMS'
					});
				}
			}

			// Loop through the customer friendly description list
			angular.forEach(self.customerFriendlyDescriptionList, function(descriptionObject, key) {
				if (descriptionObject == null) {
					return;
				}
				switch (descriptionObject.id) {
					// English customer friendly tag 1
					case self.ENGLISH_CFD1_INPUT_ID: {
						// If the description was changed then it doesn't have to do anything because everything is already set.
						if (self.hasValueChanged(descriptionObject.description, self.originalEnglishCustomerFriendlyDescription1)) {
							// If the description type is null then it was not originally in the database.
							if (descriptionObject.key.descriptionType === null) {
								descriptionObject.key.productId = self.productMaster.prodId;
								descriptionObject.key.languageType = "ENG";
								descriptionObject.key.descriptionType = "TAG1";
							}
							updateArray.push(self.customerFriendlyDescriptionList[self.customerFriendlyDescriptionList.indexOf(descriptionObject)]);
						}
						break;
					}
					// English customer friendly tag 2
					case self.ENGLISH_CFD2_INPUT_ID: {
						// If the description was changed then it doesn't have to do anything because everything is already set.
						if (self.hasValueChanged(descriptionObject.description, self.originalEnglishCustomerFriendlyDescription2)) {
							// It was not originally in the database.
							if (descriptionObject.key.descriptionType === null) {
								descriptionObject.key.productId = self.productMaster.prodId;
								descriptionObject.key.languageType = "ENG";
								descriptionObject.key.descriptionType = "TAG2";
							}
							updateArray.push(self.customerFriendlyDescriptionList[self.customerFriendlyDescriptionList.indexOf(descriptionObject)]);
						}
						break;
					}

					// Spanish customer friendly tag 1
					case self.SPANISH_CFD1_INPUT_ID: {
						// If the description was changed then it doesn't have to do anything because everything is already set.
						if (self.hasValueChanged(descriptionObject.description, self.originalSpanishCustomerFriendlyDescription1)) {
							// It was not originally in the database.
							if (descriptionObject.key.descriptionType === null) {
								descriptionObject.key.productId = self.productMaster.prodId;
								descriptionObject.key.languageType = "SPN";
								descriptionObject.key.descriptionType = "TAG1";
							}
							updateArray.push(self.customerFriendlyDescriptionList[self.customerFriendlyDescriptionList.indexOf(descriptionObject)]);
						}
						break;
					}
					// Spanish customer friendly tag 2
					case self.SPANISH_CFD2_INPUT_ID: {
						// If the description was changed then it doesn't have to do anything because everything is already set.
						if (self.hasValueChanged(descriptionObject.description, self.originalSpanishCustomerFriendlyDescription2)) {
							// It was not originally in the database.
							if (descriptionObject.key.descriptionType === null) {
								descriptionObject.key.productId = self.productMaster.prodId;
								descriptionObject.key.languageType = "SPN";
								descriptionObject.key.descriptionType = "TAG2";
							}
							updateArray.push(self.customerFriendlyDescriptionList[self.customerFriendlyDescriptionList.indexOf(descriptionObject)]);
						}
						break;
					}
					// COOL description
					case self.COOL_INPUT_ID: {
						// If the description was changed then it doesn't have to do anything because everything is already set.
						if (self.hasValueChanged(descriptionObject.description, self.originalCoolDescription)) {
							// It was not originally in the database.
							if (descriptionObject.key.descriptionType === null) {
								descriptionObject.key.productId = self.productMaster.prodId;
								descriptionObject.key.languageType = "ENG";
								descriptionObject.key.descriptionType = "COOL";
							}
							updateArray.push(self.customerFriendlyDescriptionList[self.customerFriendlyDescriptionList.indexOf(descriptionObject)]);
						}
						break;
					}
					// Service Case Callout description
					case self.SERVICE_CASE_CALLOUT_ID: {
						// If the description was changed then it doesn't have to do anything because everything is already set.
						if (self.hasValueChanged(descriptionObject.description, self.originalServiceCaseCallout)) {
							// It was not originally in the database.
							if (descriptionObject.key.descriptionType === null) {
								descriptionObject.key.productId = self.productMaster.prodId;
								descriptionObject.key.languageType = "ENG";
								descriptionObject.key.descriptionType = "SRVCC";
							}
							updateArray.push(self.customerFriendlyDescriptionList[self.customerFriendlyDescriptionList.indexOf(descriptionObject)]);
						}
						break;
					}
					default:
						descriptionObject.key.descriptionType = null;
						descriptionObject.description = null;
						break;
				}
			});

			if (updateArray.length > 0) {
				productMaster.productDescriptions = updateArray;
			}
			return true;
		};
		/**
		 * check warning when save primo pick
		 */
		self.checkWarningShelfAttribute = function(){
			self.success = null;
			self.error = null;
			if(self.validateShelfAttribute()){
				if(self.isPrimoPick && self.primoPickStatusId==='A' && !self.isDistinctive && self.originalDistinctive){
					$('#confirm-uncheck-distinctive').modal({backdrop: 'static', keyboard: true});
				}else{
					self.updateShelfAttribute();
				}
			}
		};
		/**
		 * validate line breaks in text
		 */
		self.hasLineBreaks = function(text){
			return /\r|\n/.exec(text);
		};
		/**
		 * validate data primo pick when save shelf attribute
		 */
		self.validateShelfAttribute = function (){
			var isValid = true;
			if(self.primoPickStatusId === self.REJECT_STATUS && self.originalPrimoPickStatusId !== self.REJECT_STATUS
				&& (self.reasonCode === null || self.reasonCode.trim()==='')){
				self.error = "Reject reason is required for rejected Primo Picks.";
				isValid = false;
			}
			if(self.hasValueChanged(self.originalProductMaster.primoPickProposedDescription, self.primoPickLongDescription)){
				if(self.primoPickLongDescription != null && self.primoPickLongDescription.trim().length > 3000){
					self.error = "Please enter the Primo Pick Story within 3000 characters";
					isValid = false;
				}else
					if(self.hasLineBreaks(self.primoPickLongDescription)){
						self.error = "The Primo Pick text must be entered as a single, continuous line. Please remove any returns or line breaks.";
						isValid = false;
					}
			}
			if(self.hasValueChanged(self.originalProductMaster.primoPickDescription, self.primoPickShortDescription)){
				if(self.primoPickShortDescription != null && self.primoPickShortDescription.trim().length > 3000){
					if(self.error != null){
						self.error = "Please enter the Primo Pick Story / Shelf Tag within 3000 characters";
					}else{
						self.error = "Please enter the Primo Pick Shelf Tag within 3000 characters";
					}
					isValid = false;
				}
			}
			return isValid;
		};

		/**
		 * Compares whether the original value has changed using a json format
		 *
		 * @returns {boolean}
		 */
		self.checkDataChanged = function () {
			if (self.originalProductMaster != null
					&& self.coolDescription != null
					&& self.spanishCustomerFriendlyDescription1 != null
					&& self.spanishCustomerFriendlyDescription2 != null
					&& self.englishCustomerFriendlyDescription1 != null
					&& self.englishCustomerFriendlyDescription2 != null
					&& self.serviceCaseCalloutDescription != null){
				if (self.hasValueChanged(self.productMaster.description, self.originalProductMaster.description)
						|| self.hasValueChanged(self.coolDescription.description, self.originalCoolDescription)
						|| self.hasValueChanged(self.spanishCustomerFriendlyDescription1.description, self.originalSpanishCustomerFriendlyDescription1)
						|| self.hasValueChanged(self.spanishCustomerFriendlyDescription2.description, self.originalSpanishCustomerFriendlyDescription2)
						|| self.hasValueChanged(self.englishCustomerFriendlyDescription1.description, self.originalEnglishCustomerFriendlyDescription1)
						|| self.hasValueChanged(self.englishCustomerFriendlyDescription2.description, self.originalEnglishCustomerFriendlyDescription2)
						|| self.hasValueChanged(self.productMaster.spanishDescription, self.originalProductMaster.spanishDescription)
						|| self.hasValueChanged(self.originalPrimoPickStatusId, self.primoPickStatusId)
						|| self.hasValueChanged(self.originalDistinctive, self.isDistinctive)
						|| self.hasValueChanged(self.originalIsGoLocal, self.isGoLocal)
						|| self.hasValueChanged(self.originalProductMaster.primoPickProposedDescription, self.primoPickLongDescription)
						|| self.hasValueChanged(self.originalPrimoPickShortDescription, self.primoPickShortDescription)
						|| self.hasValueChanged(self.serviceCaseTagDataModified.status, self.serviceCaseTagData.status)
						|| self.hasValueChanged(self.serviceCaseTagDataModified.proposedDescription, self.serviceCaseTagData.proposedDescription)
						|| self.hasValueChanged(self.serviceCaseTagDataModified.approvedDescription, self.serviceCaseTagData.approvedDescription)
						|| self.hasValueChanged(self.originalServiceCaseCallout, self.serviceCaseCalloutDescription.description)) {
					return true;
				}
				if (self.originalProductMaster.goodsProduct !== null
						&& self.hasValueChanged(self.originalProductMaster.goodsProduct.tagType, self.productMaster.goodsProduct.tagType)) {
					return true;
				}
			}
			return false;
		};
		/**
		 * Updates the shelf attribute.
		 */
		self.updateShelfAttribute = function () {
			$('#confirm-uncheck-distinctive').modal("hide");
			self.success = null;
			self.error = null;

			var productMaster = {
				prodId: self.productMaster.prodId
			};
			var varOriginalProductMaster = angular.copy(productMaster);

			// Re-Set the customer friendly description list back to empty to prepare for another update.
			self.customerFriendlyDescriptionList = [];

			if (self.hasValueChanged(self.originalPrimoPickLongDescription, self.primoPickLongDescription)
				&& self.primoPickStatusId !== null && self.primoPickStatusId.trim() === 'R'
				&& !self.userCanEditResource('PD_SHLF_03')
				&& self.primoPickLongDescription.length > 0) {
				self.primoPickStatusId = 'S';
			}

			if (!self.updateDescriptions(productMaster)) {
				return;
			}

			// If they've modified go local, then update that
			if (self.hasValueChanged(self.originalIsGoLocal, self.isGoLocal)) {
				productMaster.goLocal = self.isGoLocal;
			}

			// If they've changed the tag type, send that back.
			if (self.originalProductMaster.goodsProduct !== null 
					&& self.hasValueChanged(self.originalProductMaster.goodsProduct.tagType, self.productMaster.goodsProduct.tagType)) {
				productMaster.goodsProduct = {tagType: self.productMaster.goodsProduct.tagType};
			}

			// If they'vd changed the primo pick status ID, send that back
			if(self.hasValueChanged(self.originalPrimoPickStatusId, self.primoPickStatusId)){
				productMaster.primoPickStatusId = self.primoPickStatusId;
			}

			// If they'vd changed the is distinctive, send that back
			if(self.hasValueChanged(self.originalDistinctive, self.isDistinctive)){
				productMaster.isDistinctive = self.isDistinctive;
			}

			// If they'vd changed the primo pick Long description, send that back
			if(self.hasValueChanged(self.originalProductMaster.primoPickProposedDescription, self.primoPickLongDescription)){
				productMaster.primoPickLongDescription = self.primoPickLongDescription;
			}

			// If they'vd changed the primo pick short description, send that back
			if(self.hasValueChanged(self.originalProductMaster.primoPickDescription, self.primoPickShortDescription)){
				productMaster.primoPickShortDescription = self.primoPickShortDescription;
			}

            // If they've update the service case sign stuff, send that to the back end as well.
            if(self.hasValueChanged(self.serviceCaseTagDataModified.approvedDescription, self.serviceCaseTagData.approvedDescription)) {
                productMaster.serviceCaseSign = self.serviceCaseTagDataModified;
            }

            // If they've update the service case sign stuff, send that to the back end as well.
            if(self.hasValueChanged(self.serviceCaseTagDataModified.proposedDescription, self.serviceCaseTagData.proposedDescription)) {
                productMaster.serviceCaseSign = self.serviceCaseTagDataModified;
            }

            // If they've update the service case sign stuff, send that to the back end as well.
            if(self.hasValueChanged(self.serviceCaseTagDataModified.status, self.serviceCaseTagData.status)) {
                productMaster.serviceCaseSign = self.serviceCaseTagDataModified;
            }

            // If they've update the service case call out, send that to the back end as well.
            if(self.hasValueChanged(self.originalServiceCaseCallout, self.serviceCaseCalloutDescription.description)) {
                productMaster.serviceCaseCalloutDescription = {description: self.serviceCaseCalloutDescription.description};
            }

            // If they've update the organic flag
            if(self.hasValueChanged(self.nutritionalClaimsValueListOrig, self.nutritionalClaimsValueList)){
                productMaster.sellingUnits = [];
                angular.forEach(self.nutritionalClaimsValueList, function (nutritionalClaims, index) {
                    if(self.hasValueChanged(nutritionalClaims, self.nutritionalClaimsValueListOrig[index])){
                        angular.forEach(self.productMaster.sellingUnits, function (sellingUnit, key) {
                            var sellingUnitChanged = angular.copy(sellingUnit);
                            sellingUnitChanged.nutritionalClaims = [];
                            if(nutritionalClaims.selected){
                                sellingUnitChanged.nutritionalClaims.push(self.createNewNutritionalClaimOrganic(sellingUnit.upc, true));
                            }else{
                                angular.forEach(sellingUnit.nutritionalClaims, function (nutritionalClaim, index) {
                                    if(nutritionalClaim.key.nutritionalClaimsCode.trim() ===  ORGANIC_CODE){
                                        nutritionalClaim.selected = false;
                                        sellingUnitChanged.nutritionalClaims.push(nutritionalClaim);
                                    }
                                });
                            }
                            productMaster.sellingUnits.push(sellingUnitChanged);
                        });
                    }
                });
            }

			//Check change
			if(!angular.equals(productMaster, varOriginalProductMaster)){
				self.isLoading = true;
				productInfoApi.updateShelfAttributes(productMaster, self.loadUpdatedShelfAttributesData, self.fetchError);
			}else{
				self.error = THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
			}
		};

        /**
         * Create new nutritional claim organic.
         * @param upc the upc
		 * @param selected the selected flag
         */
        self.createNewNutritionalClaimOrganic = function (upc, selected) {
            var newNutritionalClaimOrganic = {
                nutritionalClaimsAttribute: self.sampleOrganicObject,
                key: {
                    upc : upc,
                    nutritionalClaimsCode : ORGANIC_CODE
                },
                sourceSystemId : PRODUCT_MAINTENANCE_SOURCE,
                selected : selected,
            };
            return newNutritionalClaimOrganic;
        };

		/**
		 * After the update has been successful it will re-load the shelf attributes data.
		 * @param results
		 */
		self.loadUpdatedShelfAttributesData = function (results) {
			self.productMaster = angular.copy(results.data);
			self.originalProductMaster = angular.copy(results.data);
			self.setData();
			if (results.message !== null) {
				self.valueDataChanged = false;
				self.success = results.message;
			} else {
				results.message = null;
			}
			productInfoApi.getServiceCaseTagData(self.productMaster.prodId, self.setServiceCaseTagData, self.fetchError);
		};

		/**
		 * Compares whether the original value has changed using a json format
		 *
		 * @param originalValue the original value.
		 * @param comparisonValue The value being compared on.
		 * @returns {boolean}
		 */
		self.hasValueChanged = function (originalValue, comparisonValue) {
			return $.trim(angular.toJson(originalValue)) !== $.trim(angular.toJson(comparisonValue));
		};

        /**
         * Sets all of the nutritional values if applicable. i.e. If organic, gluten free, low saturated fat.
         * If it is coming from vestcom(8) then just show organic. If it isn't coming from vestcom(4) and it is organic,
         * Show it as ORGANIC with a filled checkbox. If it isn't in the table at all, then show it as ORGANIC with a
         * non-filled checkbox.
         */
        self.setNutritionalValues = function() {
            self.nutritionalClaimsValueList = [];
            var isExitingOrganic = false;
            var isProductPrimaryUpc = false;
            var isOrganicPrimaryUpc = false;
            var organicForNonePrimaryUPC = null;
            //Find organic for product level
            for (var i = 0; i < self.productMaster.sellingUnits.length; i++) {
                //In the case product primary then list of nutritional claim is list of nutritional claim of primary upc
                if(self.productMaster.sellingUnits[i].productPrimary) {
                    isProductPrimaryUpc = true;
                    self.nutritionalClaimsValueList = angular.copy(self.productMaster.sellingUnits[i].nutritionalClaims);
                    if (self.nutritionalClaimsValueList != null && self.nutritionalClaimsValueList.length !== 0) {
                        angular.forEach(self.nutritionalClaimsValueList, function (nutritionalClaimsValueList, index) {
                            self.nutritionalClaimsValueList[index].selected = true;
                            // Check for produce only organic.
                            if (nutritionalClaimsValueList.key.nutritionalClaimsCode.trim() === ORGANIC_CODE) {
                                isExitingOrganic = isOrganicPrimaryUpc = true;
                            }
                        });
                    }
                }
                else{
                    //In the case had not any organic for primary UPC then find organic remain upcs of the product
                    if(!isExitingOrganic && !isOrganicPrimaryUpc){
                        angular.forEach(self.productMaster.sellingUnits[i].nutritionalClaims, function (nutritionalClaim, index) {
                            if(nutritionalClaim.key.nutritionalClaimsCode.trim() === ORGANIC_CODE) {
                                nutritionalClaim.selected = true;
                                organicForNonePrimaryUPC = angular.copy(nutritionalClaim);
                                isExitingOrganic = true;
                            }
                        });
                    }
                }
                //In the case had organic and had product primary then stop the loop
                if(isProductPrimaryUpc && isExitingOrganic){
                    break;
                }
            }
            //In the case hadn't organic for primary upc and had organic for none primary upc
            if(organicForNonePrimaryUPC !== null && !isOrganicPrimaryUpc){
                self.nutritionalClaimsValueList.push(organicForNonePrimaryUPC);
            }
            //In the case hadn't organic for primary upc and none primary upc
            if(isExitingOrganic === false){
                self.nutritionalClaimsValueList.splice(0, 0, angular.copy(self.createNewNutritionalClaimOrganic(null, false)));
            }

            //Backup information of nutrition claims value list
            self.nutritionalClaimsValueList  = $filter('orderBy')(self.nutritionalClaimsValueList, 'nutritionalClaimsAttribute.description');
            self.nutritionalClaimsValueListOrig = angular.copy(self.nutritionalClaimsValueList);
        };

		/**
		 * Check organic checkbox is visible or not.
		 * @param nutritionalClaimsValue
		 * @returns {boolean}
		 */
		self.organicVisible = function (nutritionalClaimsValue) {
			if((self.productMaster.classCode === PRODUCE_CLASS || self.productMaster.departmentString === PRODUCE_SUB_DEPT)
					|| (!(self.productMaster.classCode === PRODUCE_CLASS || self.productMaster.departmentString === PRODUCE_SUB_DEPT)
							&& (nutritionalClaimsValue.key.nutritionalClaimsCode.trim() ===  ORGANIC_CODE) 
							&& (nutritionalClaimsValue.sourceSystemId === VESTCOM_8_SOURCE))
					|| (nutritionalClaimsValue.key.nutritionalClaimsCode.trim() !==  ORGANIC_CODE)){
				return true;
			}
			return false;
		};

		/**
		 * Check organic checkbox is editable or not.
		 * @param nutritionalClaimsValue
		 * @returns {boolean}
		 */
        self.organicEditable = function (nutritionalClaimsValue) {
            for (var i = 0; i < self.productMaster.sellingUnits.length; i++){
                for (var j = 0; j < self.productMaster.sellingUnits[i].nutritionalClaims.length; j++) {
                    if( self.productMaster.sellingUnits[i].nutritionalClaims[j].key.nutritionalClaimsCode.trim() === ORGANIC_CODE
                        &&  self.productMaster.sellingUnits[i].nutritionalClaims[j].sourceSystemId === VESTCOM_8_SOURCE) {
                        return false;
                    }
                }
            }
            if(nutritionalClaimsValue.key.nutritionalClaimsCode.trim() ===  ORGANIC_CODE
                && nutritionalClaimsValue.sourceSystemId === PRODUCT_MAINTENANCE_SOURCE){
                return true;
            }
            return false;
        };

		self.resetShelfAttribute =function () {
			self.success = null;
			self.error = null;
			self.productMaster = angular.copy(self.originalProductMaster);
			self.setData();
			self.setServiceCaseTagData(self.serviceCaseTagData);
			self.customerFriendlyDescriptionList=[];
			self.valueDataChanged = false;
			document.getElementById(self.ENGLISH_CFD1_INPUT_ID).isValid = false;
			document.getElementById(self.ENGLISH_CFD1_INPUT_ID).hasError = false;
			document.getElementById(self.ENGLISH_CFD1_INPUT_ID).isWaitingForResponse = false;
			document.getElementById(self.ENGLISH_CFD1_INPUT_ID).needsCustomerFriendlyValidation = false;
			document.getElementById(self.ENGLISH_CFD2_INPUT_ID).isValid = false;
			document.getElementById(self.ENGLISH_CFD2_INPUT_ID).hasError = false;
			document.getElementById(self.ENGLISH_CFD2_INPUT_ID).isWaitingForResponse = false;
			document.getElementById(self.ENGLISH_CFD2_INPUT_ID).needsCustomerFriendlyValidation = false;
			document.getElementById(self.SPANISH_CFD1_INPUT_ID).isValid = false;
			document.getElementById(self.SPANISH_CFD1_INPUT_ID).hasError = false;
			document.getElementById(self.SPANISH_CFD1_INPUT_ID).isWaitingForResponse = false;
			document.getElementById(self.SPANISH_CFD1_INPUT_ID).needsCustomerFriendlyValidation = false;
			document.getElementById(self.SPANISH_CFD2_INPUT_ID).isValid = false;
			document.getElementById(self.SPANISH_CFD2_INPUT_ID).hasError = false;
			document.getElementById(self.SPANISH_CFD2_INPUT_ID).isWaitingForResponse = false;
			document.getElementById(self.SPANISH_CFD2_INPUT_ID).needsCustomerFriendlyValidation = false;
		};

		/**
		 * Show shelf attributes audit information modal
		 */
		self.showShelfAttributesAuditInfo = function () {
			self.shelfAttributesAuditInfo = productInfoApi.getShelfAttributesAudits;
			var title ="Shelf Attributes";
			var parameters = {'prodId' :self.productMaster.prodId};
			productDetailAuditModal.open(self.shelfAttributesAuditInfo, parameters, title);
		};
		/**
		 * Check permission editable by resource
		 * @param rs
		 * @returns {*|boolean}
		 */
		self.userCanEditResource = function(rs) {
			return permissionService.getPermissions(rs, 'EDIT');
		};
		/**
		 * Returns whether or not the user has any permission that allows them to save on this page.
		 */
		self.userCanSave = function() {
			return permissionService.getPermissions('PD_SHLF_01', 'EDIT') ||
				permissionService.getPermissions('PD_SHLF_02', 'EDIT') ||
				permissionService.getPermissions('PD_SHLF_03', 'EDIT') ||
				permissionService.getPermissions('PD_SHLF_04', 'EDIT') ||
				permissionService.getPermissions('PD_SHLF_05', 'EDIT') ||
				permissionService.getPermissions('PD_SHLF_06', 'EDIT') ||
				permissionService.getPermissions('PD_SHLF_07', 'EDIT') ||
				permissionService.getPermissions('PD_SHLF_08', 'EDIT') ||
				permissionService.getPermissions('PD_SHLF_10', 'EDIT')
				;
		};

		self.valueChanged = function () {
			self.valueDataChanged = true;
		};
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};

		/**
		 * Returns the added date, or null value if date doesn't exist.
		 */
		self.getAddedDate = function() {
			if(self.productMaster.createdDateTime === null || angular.isUndefined(self.productMaster.createdDateTime)) {
				return '01/01/1901 00:00';
			} else if (parseInt(self.productMaster.createdDateTime.substring(0, 4)) < 1900) {
				return '01/01/0001 00:00';
			} else {
				return $filter('date')(self.productMaster.createdDateTime, 'MM/dd/yyyy HH:mm');
			}
		};

		/**
		 * Returns createUser or '' if not present.
		 */
		self.getCreateUser = function() {
			if(self.productMaster.displayCreatedName === null || self.productMaster.displayCreatedName.trim().length == 0) {
				return '';
			}
			return self.productMaster.displayCreatedName;
		};

		/**
		 * Get current date.
		 * @returns {Date}
		 */
		self.getCurrentDate  = function () {
			var today = new Date();
			var dd = today.getDate();
			var mm = today.getMonth()+1;
			var yyyy = today.getFullYear();
			if(dd<10) {
				dd = '0'+dd
			}
			if(mm<10) {
				mm = '0'+mm
			}

			today = yyyy + '-' + mm + '-' + dd;
			return today;
		};
	}
})();
