/*
 *   onlineAttributesComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Online Attributes -> Online Attributes page component.
 *
 * @author l730832
 * @since 2.13.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('onlineAttributes', {
		// isolated scope binding
		require: {
			productDetail: '^productDetail'
		},
		bindings: {
			listOfProducts: '=',
			productMaster: '<',
			sellingUnit: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/onlineAttributes/onlineAttributes.html',
		// The controller that handles our component logic
		controller: OnlineAttributesController
	});

	OnlineAttributesController.$inject = ['productSellingUnitsApi','productApi', 'UserApi', 'ngTableParams', '$scope',
		'$rootScope', 'HomeSharedService','ProductDetailAuditModal', 'productDetailApi',  'ProductSearchService',
		'$filter', 'appConstants'];

	function OnlineAttributesController(productSellingUnitsApi, productApi, userApi, ngTableParams, $scope, $rootScope,
										homeSharedService,ProductDetailAuditModal, productDetailApi,
										productSearchService, $filter, appConstants) {

		var self = this;

		/**
		 * The constant for online attributes tab.
		 * @type {string}
		 */
		var onlineAttributesTab= "onlineAttributes";

		/**
		 * The constant for tierPricing tab.
		 * @type {string}
		 */
		var tierPricingTab = "tierPricing";

		/**
		 * The constant for tags/specs tab.
		 * @type {string}
		 */
		var tagsAndSpecsTab = "tagsAndSpecs";

		/**
		 * The constant for relatedProducts tab.
		 * @type {string}
		 */
		var relatedProductsTab = "relatedProducts";

		/**
		 * Tier Pricing table params
		 * @type {null}
		 */
		self.tierPricingTableParams = null;

		/**
		 * The current tier pricing list.
		 * @type {null}
		 */
		self.currentTierPricingList = null;

		/**
		 * Products that are selected to be added to related products list
		 * @type {null}
		 */
		self.selectedProducts = [];

		/**
		 * The original tier pricing list.
		 * @type {null}
		 */
		self.originalTierPricingList = [];

		self.searchResults = null;
		/**
		 * The current related products list in pagination
		 */
		self.currentRelatedProductsList = [];

		/**
		 * The original related products list from rest service
		 */
		self.originalRelatedProductsList = [];

		/**
		 * The changed related products list
		 */
		self.changedRelatedProductsList = [];

		/**
		 * Determines which tab is currently selected.
		 * @type {null}
		 */
		self.currentTab = onlineAttributesTab;
		/**
		 * This flag states whether or not the date picker is open
		 * @type {boolean}
		 */
		self.startDatePickerOpened = false;
		/**
		 * This is for the options for the date picker
		 * @type {null}
		 */
		self.options=null;
		/**
		 * This flag is set to state if the newTierPricing row is shown or not
		 * @type {boolean}
		 */
		self.showNewTierPricing = false;
		/**
		 * This variable holds the new newTierPricing data
		 * @type {null}
		 */
		self.newTierPricing=null;
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
		 * This Object holds the default state of a new Tier Pricing
		 * @type {{key: {prodId, discountQuantity: null, effectiveTimeStamp: null}, discountTypeCodeDisplayName: string, discountValue: null}}
		 */
		self.originalNewTierPricring = {
			key:{
				prodId: self.productMaster.prodId,
				discountQuantity : null,
				effectiveTimeStamp : null
			},
			discountTypeCodeDisplayName: "",
			discountValue:null
		};
		/**
		 * This flag is for whether or not the new tier pricing row is selected to be deleted
		 * @type {boolean}
		 */
		self.selectedNewTierPricing=false;

		/**
		 * This holds a list of all indeses for the rows to be deleted
		 * @type {Array}
		 */
		self.deleteList=[];
		/**
		 * This flag states if all the rows in the tier pricing table has been selected
		 * @type {boolean}
		 */
		self.allChecked=false;
		/**
		 * This flag states if all the rows in the related products table has been selected
		 * @type {boolean}
		 */
		//self.allRelatedProductsChecked = false;
		/**
		 * This method converts the date to something the api will accept
		 * @type {*|Function}
		 */
		self.convertDate = $scope.$parent.$parent.$parent.$parent.convertDate;
		/**
		 * This array holds the original tags and specs given from the api
		 * @type {Array}
		 */
		self.tagsAndSpecsList=[];

		/**
		 * This is the currently selected attribute to possibly be added to the list of tags and specs attributes
		 * @type {null}
		 */
		self.currentAttribute=null;
		/**
		 * The list of possible tags and specs attributes
		 * @type {Array}
		 */
		self.attributes=[];
		/**
		 * The list of all attributes remaining after removing all attributes already attached to the product
		 * @type {Array}
		 */
		self.filteredAttributes=[];
		/**
		 * The current value for the attribute selected
		 * @type {null}
		 */
		self.currentValue=null;
		/**
		 * The list of currently selected values for the potential product to be added
		 * @type {Array}
		 */
		self.currentValues=[];
		/**
		 * The list of all possible values
		 * @type {Array}
		 */
		self.allValues = [];
		/**
		 * The list of values that are possible for a attribute
		 * @type {Array}
		 */
		self.filteredValues=[];
		/**
		 * This flag is for when the tags and specs attributes are still be loaded from the api
		 * @type {boolean}
		 */
		self.isLoading = false;

		/**
		 * This list of possible guarantee images
		 * @type {Array}
		 */
		self.guaranteeImages = [];
		/**
		 * The list of possible sold by options
		 * @type {Array}
		 */
		self.soldByOptions=[];
		/**
		 * The product trash can holds the online only flag
		 * @type {null}
		 */
		self.productTrashCan=null;

		/**
		 * The default current online attribute
		 * @type {{prodId: null, giftMessageSwitch: null, totallyTexas: boolean, goodsProduct: {wineProductSwitch: null, inStoreProductionSwitch: null, guaranteeImageCode: null, soldBy: null, maxCustomerOrderQuantity: null, minCustomerOrderQuantity: null, customerOrderIncrementQuantity: null, flexWeightSwitch: null, minWeight: null, maxWeight: null}, productTrashCan: {onlineSaleOnlySw: null}, productDescriptions: Array, productMarketingClaims: Array}}
		 */
		self.currentOnlineAttribute= {
			prodId:null,
			giftMessageSwitch: null,
			totallyTexas: false,
			goodsProduct: {
				wineProductSwitch: null,
				inStoreProductionSwitch: null,
				hebGuaranteeTypeCode:{
					hebGuaranteeTypeCode: null,
                    hebGuaranteeTypeDescription: null
				},
				soldBy: null,
				maxCustomerOrderQuantity: null,
				minCustomerOrderQuantity: null,
				customerOrderIncrementQuantity: null,
				flexWeightSwitch: null,
				minWeight: null,
				maxWeight: null
			},
			productTrashCan:{
				onlineSaleOnlySw: null
			},
			productDescriptions:[],
			productMarketingClaims:[],
			servingSize: null
		};
		/**
		 * States if the form has any errors
		 * @type {boolean}
		 */
		self.fieldErrors=false;
		/**
		 * This object holds all of the original online attribute data used to allow reseting the data
		 * @type {null}
		 */
		self.origianlOnlineAttribute =null;
		/**
		 * This object holds information about third party sellable flag
		 * @type {null}
		 */
		self.originalThirdPartySellable = null;
		/**
		 * This is used so that the tag and specs data doesn't hold up all the other tabs
		 * @type {boolean}
		 */
		self.isWaitingForTagsAndSpecs=false;

        /**
         * Related product tab
         * @type {String}
         */
        const RELATED_PRODUCT_TAB = "relatedProductTab";

        /**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
			self.error = null;
			self.success = null;
		};
		
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onChanges = function () {
			self.error = null;
			self.success = null;
			self.getOnlineAttributeData();
		};

		/**
		 * Get data of online attribute.
		 */
		self.getOnlineAttributeData = function () {
			self.isLoading = true;
			self.isWaitingForTagsAndSpecs=true;
			if(productSearchService.getToggledTab() !== null 
					&& productSearchService.getToggledTab() === RELATED_PRODUCT_TAB
					&& productSearchService.getReturnToListButtonClicked()) {
				productSearchService.setReturnToListButtonClicked(false);
				productSearchService.setToggledTab(null);
				//Set the tab and panel of Related Product tab will be active
				self.chooseTab(relatedProductsTab);
			}else{
				//Set the tab and panel of online Attribute tab will be active
                self.chooseTab(onlineAttributesTab);
			}
			productDetailApi.getUpdatedProduct(
				{
					prodId: self.productMaster.prodId
				},
				//success case
				function (result) {
					self.productMaster = angular.copy(result);
					self.buildCurrentOnlineAttribute(self.productMaster);
					self.getServingSize();
					self.setTotallyTexas();
					self.buildTierPricingTable();
					self.resetNewTierPricing();
					self.getOtherAttributes();
				}, self.fetchError);
		};

		/**
		 * Get data of other attributes.
		 */
		self.getOtherAttributes = function () {
			var productId = {'prodId': self.productMaster.prodId};
			productSellingUnitsApi.getGladsonData({'upc' :self.sellingUnit.upc} ,self.loadGladsonData, self.fetchError);
			productSellingUnitsApi.getThirdPartySellableStatus(productId ,self.loadThirdPartySellableData, self.fetchError);
			productApi.getTagsAndSpecsAttribute(productId, self.createTagsAndSpecs, self.fetchError);
			productApi.getChoice(productId, self.createChoiceTable, self.fetchError);
			productApi.getProductGroups(productId, self.createProductGroups, self.fetchError);
			productApi.getGuaranteeImages(self.getGuaranteeImages, self.fetchError);
			productApi.getSoldByOptions(self.setSoldByOptions, self.fetchError);
			productApi.getProductTrashCan(productId ,self.setProductTrashCan, self.fetchError);
			productApi.getTagsAndSpecsAttributeOptions(self.loadAttributes, self.fetchError);
			productApi.getTagsAndSpecsAttributeValues(self.loadValues, self.fetchError);
		};

		/**
		 * Sets up the list of possible sold by options
		 * @param results
		 */
		self.setSoldByOptions = function (results) {
			self.soldByOptions = angular.copy(results);
		};

		/**
		 * Sets up the guaranteeImages options
		 * @param results
		 */
		self.getGuaranteeImages = function (results) {
			self.guaranteeImages=angular.copy(results);
		};

		/**
		 * Sets up the product trash can from the backend
		 * @param results
		 */
		self.setProductTrashCan = function (results) {
			self.productTrashCan = angular.copy(results);
			self.currentOnlineAttribute.productTrashCan = angular.copy(self.productTrashCan);
			self.origianlOnlineAttribute.productTrashCan = angular.copy(self.currentOnlineAttribute.productTrashCan);
		};

		/**
		 * Gets the list of attributes for tags and specs
		 * @param results
		 */
		self.loadAttributes = function (results) {
			self.attributes = results;
			self.filterAttributes();
		};

		/**
		 * Gets the list of values for tags and specs from the backend
		 * @param results
		 */
		self.loadValues = function (results) {
			self.allValues = angular.copy(results);
			self.isLoading = false;
		};

		/**
		 * Sets up the tags and specs attributes
		 * @param results
		 */
		self.createTagsAndSpecs = function (results) {
			self.tagsAndSpecsList = results;
			angular.forEach(results, function(tagsAndSpecs){
				var valuesArray=[];
				var valueSingle;
				angular.forEach(tagsAndSpecs.values, function(value){
					if(value.selected){
						valuesArray.push(value);
						valueSingle=value;
					}
				});
				if(tagsAndSpecs.multiValueFlag){
					tagsAndSpecs.valuesSelected=angular.copy(valuesArray);
				} else {
					tagsAndSpecs.valuesSelected=angular.copy(valueSingle);
				}
			});
			//self.filterAttributes();
			self.buildTagsAndSpecsTableParams();
		};

		/**
		 * AJAX callback for getting related products from REST svc
		 * @param results
		 */
		self.createRelatedProducts = function (results) {
			if (results !== null && results.length > 0) {
            	angular.forEach(results, function(item){
            		item.toDelete = false;
            		item.isChecked = false;
            		item.toAdd = false;
            	});
            }
			self.originalRelatedProductsList = angular.copy(results);
			self.currentRelatedProductsList = angular.copy(results);
			self.changedRelatedProductsList = angular.copy(results);

			$rootScope.$broadcast('BuildRelatedProductsTableParams');
		};

		/**
		 * Load the results of the api call to get the data to determing if thirdPartySellable.
		 *
		 * @param results the values that help determine if thirdPartySellable is true;
		 */
		self.loadThirdPartySellableData = function (results) {
			self.thirdPartySellable = results;
			self.originalThirdPartySellable = angular.copy(self.thirdPartySellable);
			self.setThirdPartySellable();
		};

		/**
		 * Load the results from the API that contain the gladson data.
		 *
		 * @param results
		 */
		self.loadGladsonData = function (results) {
			self.gladson = angular.copy(results);
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			/** Execute component destroy events if any. */
		};

		/**
		 * Fetches the error from the back end.
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isLoading=false;
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
		 * @param error The error message.
		 */
		self.setError = function (error) {
			self.error = error;
		};

		/**
		 * Filter Attributes so that attributes cannot be repeated on tags and specs
		 */
		self.filterAttributes=function () {
			self.currentAttribute=null;
			self.filteredAttributes=[];
			var tempAttribute;
			var found=false;
			if(self.tagsAndSpecsList.length>0){
				var currentTagsAndSpecsCopy = angular.copy(self.tagsAndSpecsList);
				angular.forEach(self.attributes, function(attribute){
					found=false;
					for(var index = currentTagsAndSpecsCopy.length-1; index>=0;index--){
						tempAttribute = currentTagsAndSpecsCopy[index];
						if(tempAttribute.attributeId === attribute.key.attributeId){
							found=true;
							currentTagsAndSpecsCopy.splice(index, 1);
						}
					}
					if(!found){
						self.filteredAttributes.push(angular.copy(attribute));
					}
				})
			}else{
				self.filteredAttributes=angular.copy(self.attributes);
			}
		};


		/**
		 * Filter Attributes so that attributes cannot be repeated on tags and specs
		 */
		self.filterAttributes=function () {
			self.currentAttribute=null;
			self.filteredAttributes=[];
			var tempAttribute;
			var found=false;
			if(self.tagsAndSpecsList.length>0){
				var currentTagsAndSpecsCopy = angular.copy(self.tagsAndSpecsList);
				angular.forEach(self.attributes, function(attribute){
					found=false;
					for(var index = currentTagsAndSpecsCopy.length-1; index>=0;index--){
						tempAttribute = currentTagsAndSpecsCopy[index];
						if(tempAttribute.attributeId === attribute.key.attributeId){
							found=true;
							currentTagsAndSpecsCopy.splice(index, 1);
						}
					}
					if(!found){
						self.filteredAttributes.push(angular.copy(attribute));
					}
				})
			}else{
				self.filteredAttributes=angular.copy(self.attributes);
			}
		};

		/**
		 * Builds all of the tables for each of the views.
		 */
		self.buildAllTables = function() {
			self.isLoading = true;
			self.buildTierPricingTable();
			self.isLoading = false;
		};

		/**
		 * Builds the tier pricing table.
		 */
		self.buildTierPricingTable = function() {
			if(self.productMaster.tierPricings.length > 0) {
				self.currentTierPricingList = self.productMaster.tierPricings;
				self.originalTierPricingList = angular.copy(self.currentTierPricingList);
				self.tierPricingTableParams = new ngTableParams(
					{
						page: 1,
						count: 10
					},
					{
						counts: [],
						data: self.productMaster.tierPricings
					}
				);
			} else {
				self.currentTierPricingList = null;
			}
		};

		/**
		 * Determines which tab you are currently on.
		 * @param tab
		 */
		self.chooseTab = function(tab) {
			//Set the tab and panel of tab will be active
            switch (tab) {
                case relatedProductsTab:
                    self.activeRelatedProductTabClass = 'ng-scope active';
                    self.relatedProductTabPaneClass = 'tab-pane active';
                    self.onlineAttributeTabPaneClass = 'tab-pane';
                    self.activeOnlineAttributeTabClass = '';
                    self.activeTierPricingTabClass = '';
                    self.activeTagsAndSpecsTabClass = '';
                    self.tierPricingTabPaneClass = 'tab-pane';
                    self.tagsAndSpecsTabPaneClass = 'tab-pane';
                    break;
                case onlineAttributesTab:
                    self.onlineAttributeTabPaneClass = 'tab-pane active';
                    self.activeOnlineAttributeTabClass = 'ng-scope active';
                    self.relatedProductTabPaneClass = 'tab-pane';
                    self.activeRelatedProductTabClass = '';
                    self.activeTierPricingTabClass = '';
                    self.activeTagsAndSpecsTabClass = '';
                    self.tierPricingTabPaneClass = 'tab-pane';
                    self.tagsAndSpecsTabPaneClass = 'tab-pane';
                    break;
                case tierPricingTab:
                    self.onlineAttributeTabPaneClass = 'tab-pane';
                    self.activeOnlineAttributeTabClass = '';
                    self.relatedProductTabPaneClass = 'tab-pane';
                    self.activeRelatedProductTabClass = '';
                    self.activeTierPricingTabClass = 'ng-scope active';
                    self.activeTagsAndSpecsTabClass = '';
                    self.tierPricingTabPaneClass = 'tab-pane active';
                    self.tagsAndSpecsTabPaneClass = 'tab-pane';
                    break;
                case tagsAndSpecsTab:
                    self.onlineAttributeTabPaneClass = 'tab-pane';
                    self.activeOnlineAttributeTabClass = '';
                    self.relatedProductTabPaneClass = 'tab-pane';
                    self.activeRelatedProductTabClass = '';
                    self.activeTierPricingTabClass = '';
                    self.activeTagsAndSpecsTabClass = 'ng-scope active';
                    self.tierPricingTabPaneClass = 'tab-pane';
                    self.tagsAndSpecsTabPaneClass = 'tab-pane';
                    break;
                default:
            }
			self.currentTab = tab;
		};

		/**
		 * Builds the tags and specs table params.
		 */
		self.buildTagsAndSpecsTableParams = function() {
			if(self.currentTagsAndSpecsList = self.tagsAndSpecsList.length > 0) {
				self.currentTagsAndSpecsList = self.tagsAndSpecsList;
				self.originalTagsAndSpecsList = angular.copy(self.currentTagsAndSpecsList);
				self.tagsAndSpecsTableParams = new ngTableParams(
					{
						page: 1,
						count: 10
					},
					{
						counts: [],
						data: self.currentTagsAndSpecsList
					}
				);
			} else {
				self.currentTagsAndSpecsList = null;
			}
			self.isWaitingForTagsAndSpecs=false;
		};

		/**
		 * Based on the Marketing Claim Code and based on table MKT_CLM return true if the code set is 00002 for Primo Pick.
		 *
		 * @returns {boolean} Code and based on table MKT_CLM return true if the code set is 00002 for Own Brand.
		 */
		self.setTotallyTexas = function () {
			self.currentOnlineAttribute.totallyTexas = false;
			self.currentOnlineAttribute.productMarketingClaims = [];
			for(var index=0; index<self.productMaster.productMarketingClaims.length; index++){
				if(self.productMaster.productMarketingClaims[index] !== null) {
					if(self.productMaster.productMarketingClaims[index].marketingClaim.marketingClaimCode === '00005'){
						self.currentOnlineAttribute.productMarketingClaims.push(self.productMaster.productMarketingClaims[index]);
						if((self.productMaster.productMarketingClaims[index].marketingClaimStatusCode !== null) && (self.productMaster.productMarketingClaims[index].marketingClaimStatusCode.trim() === 'A')){
							self.currentOnlineAttribute.totallyTexas = true;
						}
						self.origianlOnlineAttribute.totallyTexas = angular.copy(self.currentOnlineAttribute.totallyTexas);
						self.origianlOnlineAttribute.productMarketingClaims = angular.copy(self.currentOnlineAttribute.productMarketingClaims);
						break;
					}
				}
			}
		};

		/**
		 *Based on the returned product descriptions on productMaster, if description Type is equal to SSIZE the serving size is that value.
		 *
		 */
		self.getServingSize = function () {
			self.currentOnlineAttribute.servingSize = null;
			self.currentOnlineAttribute.productDescriptions = [];
			for(var index = 0; index<self.productMaster.productDescriptions.length; index++){
				if(self.productMaster.productDescriptions[index] !== null){
					if(self.productMaster.productDescriptions[index].key.descriptionType === 'SSIZE'){
						self.currentOnlineAttribute.productDescriptions.push(self.productMaster.productDescriptions[index]);
						self.currentOnlineAttribute.servingSize = self.productMaster.productDescriptions[index].description;
						self.origianlOnlineAttribute.servingSize = angular.copy(self.currentOnlineAttribute.servingSize);
						self.origianlOnlineAttribute.productDescriptions = angular.copy(self.currentOnlineAttribute.productDescriptions);
						break;
					}
				}
			}
		};

		/**
		 * Based on the keyType = PROD and targetSystemId = 19 then third party sellable is true;
		 *
		 * @returns {boolean} thirdPartySellable flag
		 */
		self.setThirdPartySellable = function () {
			var thirdParty = false;
			if(self.thirdPartySellable !== undefined
					&& self.thirdPartySellable !== null
					&& self.thirdPartySellable.key !== undefined
					&& self.thirdPartySellable.key !== null){
				if(self.thirdPartySellable.key.keyType === "PROD " && self.thirdPartySellable.key.targetSystemId === 19){
					thirdParty = true;
				}
			}
			self.thirdPartySellable = thirdParty;
			self.originalThirdPartySellable = angular.copy(self.thirdPartySellable);
		};

		/**
		 * Calls all the set date picker functions.
		 */
		self.setDatePickers = function () {
			self.setDateForStartDatePicker();
		};

		/**
		 * Sets the current start date.
		 */
		self.setDateForStartDatePicker = function () {
			self.startDatePickerOpened = false;
			if (self.newTierPricing.key.effectiveTimeStamp !== null) {
				self.newTierPricing.key.effectiveTimeStamp =
					new Date(self.newTierPricing.key.effectiveTimeStamp.substring(0, 10).replace(/-/g, '\/'));
			} else {
				self.newTierPricing.key.effectiveTimeStamp = null;
			}
		};

		/**
		 * Open the FreightConfirmed picker to select a new date.
		 */
		self.openStartDatePicker = function () {
			self.startDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * This method is called whenever the add button is pressed on the tier pricing tab is pressed,
		 * this method will either show the newTierPricing row or add the newTierPricing row to the tier pricing list if
		 * the newTierPricing is valid.
		 */
		self.addNewTierPricing= function () {
			if(self.showNewTierPricing)
			{
				self.checkIfValid();
				if(self.currentTierPricingList !== null) {
					if (self.currentTierPricingList.length + 1 > 5) {
						$('#confirmAddThan5TierPricingModal').modal({backdrop: 'static', keyboard: true});
					}else{
						self.showNewTierPricing = true;
					}
				} else {
					self.showNewTierPricing=true;
				}
			}
			else
			{
				if(self.currentTierPricingList !== null) {
					if (self.currentTierPricingList.length + 1 > 5) {
						$('#confirmAddThan5TierPricingModal').modal({backdrop: 'static', keyboard: true});
					} else {
						self.showNewTierPricing = true;
					}
				}else{
					self.showNewTierPricing = true;
				}
			}
		};

		/**
		 *This method will be called add tierpricing.
		 */
		self.cancelAddTierPricing = function () {
			$('#confirmAddThan5TierPricingModal').modal("hide");
		};

		/**
		 * This Method ensures that the new tier pricing row all all the required information before adding it to the
		 * tier pricing list
		 */
		self.checkIfValid=function () {
			if(self.showNewTierPricing) {
				var validToAdd = true;
				switch (self.newTierPricing.discountTypeCode) {
					case("A"):
						self.newTierPricing.discountTypeCodeDisplayName = "Cents Off";
						break;
					case("P"):
						self.newTierPricing.discountTypeCodeDisplayName = "Percent Off";
						break;
					default:
				}
				for (var key in self.newTierPricing) {
					var value = self.newTierPricing[key];
					for (var innerKey in value) {
						var innerValue = value[innerKey];
						if (angular.equals("", innerValue) || angular.equals(null, innerValue)) {
							validToAdd = false;
						}
					}
					if (angular.equals("", value) || angular.equals(null, value)) {
						validToAdd = false;
					}
				}
				if (validToAdd) {
					if(self.currentTierPricingList === null){
						self.currentTierPricingList=[];
					}
					self.newTierPricing.key.effectiveTimeStamp = self.convertDate(self.newTierPricing.key.effectiveTimeStamp);
					self.newTierPricing.key.prodId = self.productMaster.prodId;
					self.currentTierPricingList.push(self.newTierPricing);
					self.resetNewTierPricing();
					self.showNewTierPricing = false;
				} else {
					self.error = "Need to fill out all fields before adding a new Tier Pricing Unit"
				}
			}
		};

		/**
		 * Builds the current online attribute and sets up the original online attribute
		 * @param productMaster
		 */
		self.buildCurrentOnlineAttribute = function(productMaster){
			self.currentOnlineAttribute.prodId= productMaster.prodId;
			self.currentOnlineAttribute.giftMessageSwitch = productMaster.giftMessageSwitch;
			if(productMaster.goodsProduct !== null){
				self.currentOnlineAttribute.goodsProduct = {
						wineProductSwitch: null,
						inStoreProductionSwitch: null,
						hebGuaranteeTypeCode:{
							hebGuaranteeTypeCode: null,
							hebGuaranteeTypeDescription: null
						},
						soldBy: null,
						maxCustomerOrderQuantity: null,
						minCustomerOrderQuantity: null,
						customerOrderIncrementQuantity: null,
						flexWeightSwitch: null,
						minWeight: null,
						maxWeight: null
				};
				self.currentOnlineAttribute.goodsProduct.wineProductSwitch = productMaster.goodsProduct.wineProductSwitch;
				self.currentOnlineAttribute.goodsProduct.inStoreProductionSwitch = productMaster.goodsProduct.inStoreProductionSwitch;
				self.currentOnlineAttribute.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode = null;
				self.currentOnlineAttribute.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeDescription = null;
				if(productMaster.goodsProduct.hebGuaranteeTypeCode !== null){
					self.currentOnlineAttribute.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode = productMaster.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode;
					self.currentOnlineAttribute.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeDescription = productMaster.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeDescription;
				}
				self.currentOnlineAttribute.goodsProduct.soldBy = productMaster.goodsProduct.soldBy.trim();
				self.currentOnlineAttribute.goodsProduct.maxCustomerOrderQuantity =(productMaster.goodsProduct.maxCustomerOrderQuantity).toFixed(2);
				self.currentOnlineAttribute.goodsProduct.minCustomerOrderQuantity = (productMaster.goodsProduct.minCustomerOrderQuantity).toFixed(2);
				self.currentOnlineAttribute.goodsProduct.customerOrderIncrementQuantity = (productMaster.goodsProduct.customerOrderIncrementQuantity).toFixed(2);
				self.currentOnlineAttribute.goodsProduct.flexWeightSwitch = productMaster.goodsProduct.flexWeightSwitch;
				self.currentOnlineAttribute.goodsProduct.minWeight = (productMaster.goodsProduct.minWeight).toFixed(4);
				self.currentOnlineAttribute.goodsProduct.maxWeight =(productMaster.goodsProduct.maxWeight).toFixed(4);
			}else{
				self.currentOnlineAttribute.goodsProduct = null;
			}
			self.origianlOnlineAttribute = angular.copy(self.currentOnlineAttribute);
		};

		/**
		 * This method resets the current Tier Pricing List to the  original Tier Pricing LIst
		 */
		self.resetNewTierPricing = function () {
			self.newTierPricing=angular.copy(self.originalNewTierPricring);
		};

		/**
		 * This method resets the Related Products List to the  original Related Product List
		 */
		self.resetRelatedProducts = function () {
			self.changedRelatedProductsList=angular.copy(self.originalRelatedProductsList);
			self.selectedProducts = [];
			$rootScope.$broadcast('ResetRelatedProductsTab');
			self.buildCurrentOnlineAttribute(self.productMaster);
			self.getServingSize();
			self.setTotallyTexas();
		};

		/**
		 * This method will validate that the input in the text input will only create float values
		 * @param evt
		 */
		self.validateFloatKeyPress= function (evt) {
			var charCode = (evt.which) ? evt.which : event.keyCode;
			if (self.newTierPricing.discountValue == null || angular.equals("", self.newTierPricing.discountValue)){
				if (charCode === 48){
					evt.preventDefault()
				}
			}
			if (charCode !== 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
				evt.preventDefault();
			}
			if (charCode === 46 && self.newTierPricing.discountValue.indexOf(".") !== -1) {
				evt.preventDefault();
			}
		};

		/**
		 * This method prevents non number characters from entering the input field.  It will also enforce only having
		 * a single decimal place and only allowing the number to have a set number of decimal places.
		 * @param value the current value about to be updated
		 * @param decimalPlaces the number of allowed decimal places
		 * @param evt the event that just happened
		 * @param id the id of the html element where the event occured
		 */
		self.validateVariableLengthFloatKeyPress= function (value, decimalPlaces, evt, id) {
			var text = "";
			var myElement = document.getElementById(id);
			var cursorPosition = myElement.selectionStart;
			if (typeof window.getSelection != "undefined") {
				text = window.getSelection().toString();
			} else if (typeof document.selection != "undefined" && document.selection.type == "Text") {
				text = document.selection.createRange().text;
			}
			var charCode = (evt.which) ? evt.which : event.keyCode;

			if (charCode !== 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
				evt.preventDefault();
			}
			if (charCode === 46 && value.indexOf(".") !== -1) {
				evt.preventDefault();
			}
			if(value !== null && value.indexOf(".")>=0){
				if(text.length === 0 && (cursorPosition>value.indexOf(".") ) && ((value.length - value.indexOf("."))>decimalPlaces)){
					evt.preventDefault();
				}
			}
		};

		/**
		 * This method will validate that the input in the text input will only create integer values
		 * @param evt
		 */
		self.validateIntegerKeyPress= function (evt) {
			var charCode = (evt.which) ? evt.which : event.keyCode;
			if (self.newTierPricing.key.discountQuantity == null || angular.equals("", self.newTierPricing.key.discountQuantity)){
				if (charCode === 48){
					evt.preventDefault()
				}
			}
			if (charCode !== 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
				evt.preventDefault();
			}
			if (charCode === 46 && self.newTierPricing.discountValue.indexOf(".") !== -1) {
				evt.preventDefault();
			}
		};

		/**
		 * This method will remove the selected tier pricing units
		 */
		self.deleteTierPricingUnits = function () {
			self.allChecked=false;
			$("#tierPricingTable tr td:nth-child(1) input[type=checkbox]").prop("checked", false);
			for(var index = 0; index<self.deleteList.length; index++){
				self.currentTierPricingList[self.deleteList[index]].toDelete=true;
			}
			for(var index = self.currentTierPricingList.length -1; index >=0; index--){
				if(self.currentTierPricingList[index].toDelete){
					self.currentTierPricingList.splice(index, 1);
				}
			}
			if(self.currentTierPricingList.length === 0){
				self.currentTierPricingList = null;
			}
			if(self.selectedNewTierPricing){
				self.resetNewTierPricing();
				self.showNewTierPricing=false;
			}
			self.deleteList=[];
		};

		/**
		 * This method will determine which reset to call based on the currently selected tab
		 * @param tab
		 */
		self.reset=function (tab) {
			self.success=null;
			self.error=null;
			if(tab === tierPricingTab){
				self.currentTierPricingList =angular.copy(self.originalTierPricingList);
				self.resetNewTierPricing();
				self.showNewTierPricing=false;
			} else if(tab === relatedProductsTab){
				self.currentRelatedProductsList =angular.copy(self.originalRelatedProductsList);
				self.resetRelatedProducts();
			}
			if(tab === tagsAndSpecsTab){
				self.currentTagsAndSpecsList =angular.copy(self.originalTagsAndSpecsList);
			}
			if(tab === onlineAttributesTab){
				self.resetErrors();
				self.currentOnlineAttribute = angular.copy(self.origianlOnlineAttribute);
				self.thirdPartySellable = angular.copy(self.originalThirdPartySellable);
			}
		};

		/**
		 * Remove all field errors on the online attribute form
		 */
		self.resetErrors = function () {
			self.maxCustomerOrderQuantityError=null;
			self.minCustomerOrderQuantityError = null;
			self.minWeightError = null;
			self.maxWeightError = null;
		};

		/**
		 * This method will determine what rows were created and which rows were deleted based on the original list
		 * @param tab
		 * @returns {boolean}
		 */
		self.isDifference = function (tab) {
			if(tab === tierPricingTab){
				if(self.currentTierPricingList !== null){
					delete self.currentTierPricingList.$$hashKey;
					if ((angular.toJson(self.originalTierPricingList) !== angular.toJson(self.currentTierPricingList)) || (angular.toJson(self.originalNewTierPricring) !== angular.toJson(self.newTierPricing))){
						$rootScope.contentChangedFlag = true;
						return true;
					} else{
						$rootScope.contentChangedFlag = false;
						return false;
					}
				} else {
					if((angular.toJson(self.originalTierPricingList) !== angular.toJson(self.currentTierPricingList)) || (angular.toJson(self.originalNewTierPricring) !== angular.toJson(self.newTierPricing))){
						$rootScope.contentChangedFlag = true;
						return true;
					} else{
						$rootScope.contentChangedFlag = false;
						return false;
					}
				}
			} else if(tab === relatedProductsTab){
				if(self.currentRelatedProductsList !== null){
					delete self.currentRelatedProductsList.$$hashKey;
					if (self.selectedProducts.length > 0) {
						$rootScope.contentChangedFlag = true;
						return true;
					}
					if((angular.toJson(self.changedRelatedProductsList) !== angular.toJson(self.originalRelatedProductsList))){
						$rootScope.contentChangedFlag = true;
						return true;
					} else{
						$rootScope.contentChangedFlag = false;
						return false;
					}
				} else {
					if (self.selectedProducts.length > 0) {
						$rootScope.contentChangedFlag = true;
						return true;
					}
					if((angular.toJson(self.changedRelatedProductsList) !== angular.toJson(self.originalRelatedProductsList))){
						$rootScope.contentChangedFlag = true;
						return true;
					} else{
						$rootScope.contentChangedFlag = false;
						return false;
					}
				}
			}
			if(tab === tagsAndSpecsTab){
				if(self.currentTagsAndSpecsList !== null) {
					if((angular.toJson(self.originalTagsAndSpecsList) !== angular.toJson(self.currentTagsAndSpecsList))){
						$rootScope.contentChangedFlag = true;
						return true;
					} else{
						$rootScope.contentChangedFlag = false;
						return false;
					}
				}
			}
			if(tab === onlineAttributesTab){
				if(self.thirdPartySellable !== self.originalThirdPartySellable){
					if(typeof self.thirdPartySellable !== "undefined") {
						$rootScope.contentChangedFlag = true;
						return true;
					}
				}
				if((angular.toJson(self.origianlOnlineAttribute) !== angular.toJson(self.currentOnlineAttribute))){
					if(self.origianlOnlineAttribute !== null) {
						$rootScope.contentChangedFlag = true;
						return true;
					}
				} else{
                    $rootScope.contentChangedFlag = false;
					return false;
				}
			}
			return false;
		};

		/**
		 * This method will determine what save methods to call based on the currently selected tab
		 * @param tab
		 */
		self.saveChanges= function (tab) {
			var check = false;
			self.error = null;
			self.success = null;
			if(tab === tierPricingTab){
				self.checkIfValid();
				if(self.currentTierPricingList !== null){
					for(var i = 0; i < self.currentTierPricingList.length -1; i++){
						for(var j = i+1; j < self.currentTierPricingList.length; j++){
							if(parseInt(self.currentTierPricingList[i].key.discountQuantity) === parseInt(self.currentTierPricingList[j].key.discountQuantity)
								&& self.currentTierPricingList[i].key.effectiveTimeStamp === self.currentTierPricingList[j].key.effectiveTimeStamp){
								self.error = "ProdDiscThrh: Unique constraint has violated.";
								check = true;
								break;
							}
						}
						if(check){
							break;
						}
					}
				}
				if(self.error === null){
					self.isLoading=true;
					var tierChanges = self.getTierPricingDifference();
					productApi.saveTierPricingChanges(tierChanges, self.loadNewUpdatedTierPricing, self.fetchError);
				}
			} else if(tab === relatedProductsTab){
				if(self.error === null){
					self.error = '';
					self.success = '';
					if (tab === relatedProductsTab) {
						self.confirmTitle = 'Save Confirmation';
						self.confirmMessage = 'Are you sure you want to save the changes?';
						$('#confirmSaveModal').modal({backdrop: 'static', keyboard: true});
					}
				}
			} else if(tab === tagsAndSpecsTab){
				self.isWaitingForTagsAndSpecs=true;
				self.saveTagsAndSpecsChanges();
				var temp = self.getTagsAndSpecsChanges(angular.copy(self.originalTagsAndSpecsList), angular.copy(self.currentTagsAndSpecsList));
				productApi.updateTagsAndSpecsAttribute(temp, self.getUpdatedTagsAndSpecs, self.fetchError);
			} else if(tab === onlineAttributesTab){
				self.isLoading=true;
				self.resetErrors();
				var onlineChanges = self.getOnlineAttributesDifferences();
				if(!self.fieldErrors) {
					if (self.thirdPartySellable !== self.originalThirdPartySellable) {
						var thirdParty = {
							key: {
								targetSystemId: 19,
								keyId: self.productMaster.prodId
							}
						};
						if (self.thirdPartySellable) {
							thirdParty.key.keyType = "PROD";
						} else {
							thirdParty.key.keyType = "DEL";
						}
						productApi.updateThirdPartySellable(thirdParty,
							function (results) {
								if (onlineChanges === null) {
									self.isLoading = false;
								}
								self.success = results.message;
								self.loadThirdPartySellableData(results.data);
							},
							self.fetchError);
					}
					if (self.currentOnlineAttribute.productTrashCan.onlineSaleOnlySw !== self.origianlOnlineAttribute.productTrashCan.onlineSaleOnlySw) {
						self.productTrashCan.onlineSaleOnlySw = self.currentOnlineAttribute.productTrashCan.onlineSaleOnlySw;
						productApi.updateProductTrashCan(self.productTrashCan,
							function (results) {
								if (onlineChanges === null) {
									self.isLoading = false;
								}
								self.success = results.message;
                                productApi.getProductTrashCan({'prodId': self.productMaster.prodId} ,self.setProductTrashCan, self.fetchError);
							},
							self.fetchError);
					}
					if (onlineChanges !== null) {
						productApi.updateOnlineAttribute(onlineChanges,
							function (results) {
								self.success = results.message;
								self.getOnlineAttributeData();
							},
							self.fetchError);
					}
				} else {
					self.isLoading = false;
				}
			}
		};

		/**
		 * This method will reset all values in a tags and specs attribute
		 * @param values
		 * @returns {*}
		 */
		self.resetValues = function(values){
			angular.forEach(values, function(value){
				value.selected = false;
			});
			return values;
		};

		/**
		 * This method will update a single value on a tags and specs value list
		 * @param valuesSelected
		 * @param values
		 */
		self.setFlags= function (valuesSelected, values, sequenceNumber) {
			if(valuesSelected !== null){
				for(var index = values.length -1; index>=0; index--){
					if(angular.equals(valuesSelected.attributeCodeId, values[index].attributeCodeId)){
						values[index].selected=true;
						values[index].sequenceNumber=sequenceNumber;
					}
				}
			}
		};


		/**
		 * This method will update all of the value lists for the products current tags and specs list
		 */
		self.saveTagsAndSpecsChanges=function () {
			angular.forEach(self.currentTagsAndSpecsList, function(attribute){
				attribute.values = self.resetValues(attribute.values);
				var sequenceNumber = 0;
				if(attribute.multiValueFlag){
					sequenceNumber =1;
					angular.forEach(attribute.valuesSelected, function(value){
						self.setFlags(value, attribute.values, sequenceNumber);
						sequenceNumber++;
					});
				} else {
					self.setFlags(attribute.valuesSelected, attribute.values, sequenceNumber);
				}
			});
		};

		/**
		 * This recursively traverses through an object that may contain lists or other objects inside of it. This will
		 * check each field inside of each object or list to determine whether or not that field has changed. If it has changed,
		 * it saves it to a temporary and then the temporary will be returned.
		 *
		 * @param original
		 * @param current
		 * @returns {{}}
		 */
		self.getTagsAndSpecsChanges = function(original, current) {
			var temp={
				prodId: self.productMaster.prodId,
				updates:null,
				deletes:null
			};
			for(var indexCurrent=current.length-1; indexCurrent>=0; indexCurrent--){
				for(var indexOriginal=original.length-1;indexOriginal>=0;indexOriginal--){
					if(current[indexCurrent].attributeId === original[indexOriginal].attributeId){
						if(current[indexCurrent].values.length >0 && self.valuesMatch(current[indexCurrent].values, original[indexOriginal].values, current[indexCurrent].multiValueFlag)){
							current.splice(indexCurrent, 1);
						} else if(current[indexCurrent].alternativeAttributeCodeText !== null && angular.equals(current[indexCurrent].alternativeAttributeCodeText, original[indexOriginal].alternativeAttributeCodeText)){
							current.splice(indexCurrent, 1);
						}
						original.splice(indexOriginal, 1);
						break;
					}
				}
			}
			angular.forEach(original, function(attribute){
				attribute = self.cleanUpValues(attribute);
			});
			angular.forEach(current, function(attribute){
				attribute = self.cleanUpValues(attribute);
			});
			temp.updates =angular.copy(current);
			temp.deletes =angular.copy(original);
			return temp;
		};

		/**
		 * This method will look at two tags and specs attributes and check if any of their values have changed
		 * @param currentValues
		 * @param originalValues
		 * @returns {boolean}
		 */
		self.valuesMatch = function(currentValues, originalValues, currentMultiSelectFlag){
			if(!currentMultiSelectFlag) {
				for (var index = currentValues.length - 1; index >= 0; index--) {
					if (currentValues[index].selected === originalValues[index].selected) {
						currentValues.splice(index, 1);
						originalValues.splice(index, 1);
					}
				}
			}
			return (currentValues.length ===0);
		};

		/**
		 * Deletes an attribute from the tags and specs list
		 * @param tagsAndSpecsAttribute
		 */
		self.deleteTagsAndSpecsAttribute= function(tagsAndSpecsAttribute){
			for(var index = self.currentTagsAndSpecsList.length-1; index>=0; index--){
				if(tagsAndSpecsAttribute.attributeId === self.currentTagsAndSpecsList[index].attributeId){
					self.currentTagsAndSpecsList.splice(index, 1);
					break;
				}
			}
		};

		/**
		 * Converts relationship code to a UI readable string
		 * @param code
		 * This method will filter all values that have the same attribute id as the currently selected attribute
		 */
		self.filterValues=function () {
			self.currentValues=[];
			self.currentValue=null;
			self.filteredValues=[];
			if(self.currentAttribute !== null){
				angular.forEach(self.allValues, function (value){
					if(angular.equals(value.key.attributeId, self.currentAttribute.key.attributeId)){
						self.filteredValues.push(value);
					}
				})
			}
		};


		/**
		 * This method will add a new attribute to the products tags and specs attributes
		 */
		self.addTagsAndSpecs=function(){
			var values=[];
			var newValue;
			angular.forEach(self.filteredValues, function(value){
				newValue={
					attributeCodeId: value.attributeCode.attributeCodeId,
					name: value.attributeCode.attributeValueText,
					selected: false
				};
				values.push(angular.copy(newValue));
			});
			var selected = self.buildNewTagsAndSpecsValue();
			var newTagsAndSpecs = {
				attributeId: self.currentAttribute.attribute.attributeId,
				attributeName: self.currentAttribute.attribute.attributeName,
				multiValueFlag: self.currentAttribute.attribute.multipleValueSwitch,
				prodId: self.productMaster.prodId,
				values: values,
				valuesSelected: selected
			};
			if(self.currentTagsAndSpecsList === null){
				self.currentTagsAndSpecsList = [];
			}
			self.currentTagsAndSpecsList.push(newTagsAndSpecs);
			self.filterAttributes();
		};


		/**
		 * This method will look at an attribute and remove all unnecessary values.
		 * @param attribute
		 * @param isCurrent
		 * @returns {*}
		 */
		self.cleanUpValues=function(attribute){
			for(var index=attribute.values.length -1; index>=0; index--){
				if(!attribute.values[index].selected){
					attribute.values.splice(index, 1);
				}
			}
			return attribute;
		};

		/**
		 * This method will take a list of attribute codes and create tags and specs attribute values
		 * @returns {*}
		 */
		self.buildNewTagsAndSpecsValue=function () {
			var selected;
			var newValue;
			if((self.currentValue !== null) || (self.currentValues.length>0)){
				if(self.currentAttribute.attribute.multipleValueSwitch){
					selected = [];
					angular.forEach(self.currentValues, function(value){
						newValue={
							attributeCodeId: value.attributeCode.attributeCodeId,
							name: value.attributeCode.attributeValueText,
							selected: false
						};
						selected.push(angular.copy(newValue));
					})
				} else{
					selected= {
						attributeCodeId: self.currentValue.attributeCode.attributeCodeId,
						name: self.currentValue.attributeCode.attributeValueText,
						selected: false
					};
				}
				return selected;
			}
			return null;
		};

		/**
		 * Load new tags and specs and get the success message
		 * @param results
		 */
		self.getUpdatedTagsAndSpecs = function(results){
			self.success = results.message;
			self.createTagsAndSpecs(results.data)
		};
		/**
		 * This method will take the change on the totally texas flag and create or update the marketing claim.
		 */
		self.updateTotallyTexas = function () {
			if(self.currentOnlineAttribute.productMarketingClaims.length ===0){
				var marketingClaim={
					key:{
						prodId: self.productMaster.prodId,
						marketingClaimCode: '00005'
					},
					marketingClaimStatusCode:'A'
				};
				self.currentOnlineAttribute.productMarketingClaims.push(marketingClaim);
			}
			if(self.currentOnlineAttribute.totallyTexas){
				self.currentOnlineAttribute.productMarketingClaims[0].marketingClaimStatusCode = 'A';
			} else {
				self.currentOnlineAttribute.productMarketingClaims[0].marketingClaimStatusCode = 'R';
			}

		};

		/**
		 * This method will take changes to serving size and either create or update the product description
		 */
		self.updateServingSize=function () {
			if(self.currentOnlineAttribute.productDescriptions.length===0){
				var description = {
					description: self.currentOnlineAttribute.servingSize,
					key:{
						descriptionType:'SSIZE',
						languageType: 'ENG',
						productId: self.productMaster.prodId
					}
				};
				self.currentOnlineAttribute.productDescriptions.push(description);
			} else {
				self.currentOnlineAttribute.productDescriptions[0].description=self.currentOnlineAttribute.servingSize;
			}
		};

		/**
		 * This method will gather all of the changes on the online attribute objects
		 * @returns {*}
		 */
		self.getOnlineAttributesDifferences = function () {
			self.fieldErrors = false;
			var different = false;
			var changes= {
					prodId : self.productMaster.prodId,
					goodsProduct : null
			};
			if(self.currentOnlineAttribute.totallyTexas !== self.origianlOnlineAttribute.totallyTexas){
				different = true;
				self.updateTotallyTexas();
				changes.productMarketingClaims = angular.copy(self.currentOnlineAttribute.productMarketingClaims);
				changes.totallyTexas = self.currentOnlineAttribute.totallyTexas;
			}
			if(self.currentOnlineAttribute.servingSize !== self.origianlOnlineAttribute.servingSize){
				different = true;
				self.updateServingSize();
				changes.productDescriptions = angular.copy(self.currentOnlineAttribute.productDescriptions);
			}
			if(self.currentOnlineAttribute.giftMessageSwitch !== self.origianlOnlineAttribute.giftMessageSwitch){
				different = true;
				changes.giftMessageSwitch = self.currentOnlineAttribute.giftMessageSwitch;
			}
			
			if(self.currentOnlineAttribute.goodsProduct !== null && self.origianlOnlineAttribute.goodsProduct !== null){
				changes.goodsProduct = {};
				if(self.currentOnlineAttribute.goodsProduct.wineProductSwitch !== self.origianlOnlineAttribute.goodsProduct.wineProductSwitch){
					different = true;
					changes.goodsProduct.wineProductSwitch = self.currentOnlineAttribute.goodsProduct.wineProductSwitch;
				}
				if(self.currentOnlineAttribute.goodsProduct.inStoreProductionSwitch !== self.origianlOnlineAttribute.goodsProduct.inStoreProductionSwitch){
					different = true;
					changes.goodsProduct.inStoreProductionSwitch = self.currentOnlineAttribute.goodsProduct.inStoreProductionSwitch;
				}
				if(self.currentOnlineAttribute.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode 
						!== self.origianlOnlineAttribute.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode){
					different = true;
					changes.goodsProduct.hebGuaranteeTypeCode = {};
					if(self.currentOnlineAttribute.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode === ""){
						changes.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode = " ";
					}else{
						changes.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode = self.currentOnlineAttribute.goodsProduct.hebGuaranteeTypeCode.hebGuaranteeTypeCode;
					}
				}
				if(self.currentOnlineAttribute.goodsProduct.soldBy !== self.origianlOnlineAttribute.goodsProduct.soldBy){
					different = true;
					changes.goodsProduct.soldBy = self.currentOnlineAttribute.goodsProduct.soldBy;
				}
				if(self.currentOnlineAttribute.goodsProduct.minWeight !== self.origianlOnlineAttribute.goodsProduct.minWeight){
					if(parseFloat(self.currentOnlineAttribute.goodsProduct.minWeight) > parseFloat(self.currentOnlineAttribute.goodsProduct.maxWeight)){
						self.fieldErrors = true;
						self.minWeightError = "Min. Weight cannot be higher than the Max. Weight";
					}else{
						different = true;
						changes.goodsProduct.minWeight = parseFloat(self.currentOnlineAttribute.goodsProduct.minWeight);
					}
				}
				if(self.currentOnlineAttribute.goodsProduct.maxWeight !== self.origianlOnlineAttribute.goodsProduct.maxWeight){
					if(parseFloat(self.currentOnlineAttribute.goodsProduct.maxWeight)>=99999.9999){
						self.fieldErrors = true;
						self.maxWeightError ="Invalid Max. Weight current value greater than 99999.99";
					}else {
						different = true;
						changes.goodsProduct.maxWeight = parseFloat(self.currentOnlineAttribute.goodsProduct.maxWeight);
					}
				}
				if(self.currentOnlineAttribute.goodsProduct.flexWeightSwitch !== self.origianlOnlineAttribute.goodsProduct.flexWeightSwitch){
					different = true;
					changes.goodsProduct.flexWeightSwitch = self.currentOnlineAttribute.goodsProduct.flexWeightSwitch;
				}
				if(self.currentOnlineAttribute.goodsProduct.customerOrderIncrementQuantity !== self.origianlOnlineAttribute.goodsProduct.customerOrderIncrementQuantity){
					different = true;
					changes.goodsProduct.customerOrderIncrementQuantity = self.currentOnlineAttribute.goodsProduct.customerOrderIncrementQuantity;
				}
				if(self.currentOnlineAttribute.goodsProduct.minCustomerOrderQuantity !== self.origianlOnlineAttribute.goodsProduct.minCustomerOrderQuantity){
					if(parseFloat(self.currentOnlineAttribute.goodsProduct.minCustomerOrderQuantity) > parseFloat(self.currentOnlineAttribute.goodsProduct.maxCustomerOrderQuantity)){
						self.fieldErrors = true;
						self.minCustomerOrderQuantityError="Min. Customer Order Quantity cannot be higher than the Max. Customer Order Quantity";
					} else if (parseFloat(self.currentOnlineAttribute.goodsProduct.minCustomerOrderQuantity) == 0.0) {
						self.fieldErrors = true;
						self.minCustomerOrderQuantityError="Min. Customer Order Quantity cannot be 0";
					} else{
						different = true;
						changes.goodsProduct.minCustomerOrderQuantity = parseFloat(self.currentOnlineAttribute.goodsProduct.minCustomerOrderQuantity);
					}
				}
				if(self.currentOnlineAttribute.goodsProduct.maxCustomerOrderQuantity !== self.origianlOnlineAttribute.goodsProduct.maxCustomerOrderQuantity){
					if(parseFloat(self.currentOnlineAttribute.goodsProduct.maxCustomerOrderQuantity) > 9999.99){
						self.fieldErrors=true;
						self.maxCustomerOrderQuantityError= "Invalid Max. Customer Order Quantity current value greater than 99999.99";
					}else{
						different = true;
						changes.goodsProduct.maxCustomerOrderQuantity = parseFloat(self.currentOnlineAttribute.goodsProduct.maxCustomerOrderQuantity);
					}
				}
			}
			if(!self.fieldErrors){
				if(different) {
					return changes;
				} else {
					return null;
				}
			} else {
				return null;
			}
		};

		/**
		 * After successfully updating the tier pricing list, this method will reset the tier pricing tab
		 * @param results
		 */
		self.loadUpdatedTierPricing=function (results) {
			self.error = null;
			self.success = results.message;
			self.buildAllTables();
			self.resetNewTierPricing();
		};

		/**
		 * After successfully updating the related products list, this method will reset the related products tab
		 * @param results
		 */
		self.loadNewUpdatedTierPricing=function (results) {
			self.error = null;
			self.success = 'Changes were successfully applied.';
			productApi.getTierPricing({'prodId': self.productMaster.prodId}, self.createTierPricing, self.fetchError);
		};

		/**
		 * AJAX callback for getting related products from REST svc
		 * @param results
		 */
		self.createTierPricing = function (results) {
			self.productMaster.tierPricings = results;
			self.originalTierPricingList = results;
			self.buildAllTables();
		};

		/**
		 * After successfully updating the related products list, this method will reset the related products tab
		 * @param results
		 */
		self.loadUpdatedRelatedProducts=function (results) {
			self.error = null;
			self.success = 'Changes were successfully applied.';
			productApi.getRelatedProducts({'prodId': self.productMaster.prodId}, self.handleRelatedProducts, self.fetchError);
		};
		
		/**
		 * AJAX callback for getting related products from REST svc
		 * @param results
		 */
		self.handleRelatedProducts = function (results) {
			self.createRelatedProducts(results);
			self.isLoading = false;
		};

		/**
		 * Gets all the changes in the image destinations
		 * @param index
		 */
		self.getTierPricingDifference = function () {
			var arrayIndex;
			var tierPricingChanges = {
				prodId: self.productMaster.prodId,
				tierPricingsAdded: [],
				tierPricingsRemoved: []

			};
			angular.forEach(self.currentTierPricingList, function (tierPricing) {
				var foundBanner = false;
				if (self.originalTierPricingList.length > 0) {
					for (arrayIndex = 0; arrayIndex < self.originalTierPricingList.length; arrayIndex++) {
						if (angular.equals(tierPricing.key, self.originalTierPricingList[arrayIndex].key)) {
							foundBanner = true;
							break;
						}
					}
				}
				if (!foundBanner) {
					tierPricingChanges.tierPricingsAdded.push(tierPricing);
				}
			});
			angular.forEach(self.originalTierPricingList, function (tierPricing) {
				var foundBanner = false;
				if (self.currentTierPricingList !== null){
					if (self.currentTierPricingList.length > 0) {
						for (arrayIndex = 0; arrayIndex < self.currentTierPricingList.length; arrayIndex++) {
							if (angular.equals(tierPricing.key, self.currentTierPricingList[arrayIndex].key)) {
								foundBanner = true;
								break;
							}
						}
					}
				}
				if (!foundBanner) {
					tierPricingChanges.tierPricingsRemoved.push(tierPricing)
				}
			});
			return tierPricingChanges;
		};

		/**
		 * Gets all the changes in the image destinations
		 * @param index
		 */
		self.getRelatedProductsDifference = function () {
			var relatedProductsUpdated = [];
			var prod = null;

			angular.forEach(self.originalRelatedProductsList, function (relatedProduct) {
				var foundBanner = false;

				if (self.changedRelatedProductsList !== null){
					if (self.changedRelatedProductsList.length > 0) {
						for (var arrayIndex = 0; arrayIndex < self.changedRelatedProductsList.length; arrayIndex++) {
							if (
								angular.equals(relatedProduct.key.productId, self.changedRelatedProductsList[arrayIndex].key.productId) && angular.equals(relatedProduct.key.relatedProductId, self.changedRelatedProductsList[arrayIndex].key.relatedProductId)
							) {
								if (self.changedRelatedProductsList[arrayIndex].toDelete !== true) {
									foundBanner = true;

									// check for differences in relationship type
									if (!angular.equals(relatedProduct.key.productRelationshipCode, self.changedRelatedProductsList[arrayIndex].key.productRelationshipCode)) {

										// Note: an update is sent as a delete and an add due to web services calls since key is updated
										prod = angular.copy(relatedProduct);
										prod.actionCode =  'D';
										relatedProductsUpdated.push(prod);

										// If we went from (type = addon) and (add on sellable == true) to another code, then turn off add on sellable flag
										if (relatedProduct.key.productRelationshipCode === 'ADDON') {
											self.changedRelatedProductsList[arrayIndex].relatedProduct.goodsProduct.sellableProduct = false;
										}

										// update product relationship with new relationship code and sellable field
										relatedProduct.key.productRelationshipCode = self.changedRelatedProductsList[arrayIndex].key.productRelationshipCode;
										relatedProduct.relatedProduct.goodsProduct.sellableProduct = self.changedRelatedProductsList[arrayIndex].relatedProduct.goodsProduct.sellableProduct;

										prod = angular.copy(relatedProduct);
										prod.actionCode =  'A';
										relatedProductsUpdated.push(prod);

										// only the sellable field was changed
									} else if (!angular.equals(relatedProduct.relatedProduct.goodsProduct.sellableProduct, self.changedRelatedProductsList[arrayIndex].relatedProduct.goodsProduct.sellableProduct)) {

										// Note: an update is sent as a delete and an add due to web services calls since key is updated
										prod = angular.copy(relatedProduct);
										prod.actionCode =  'D';
										relatedProductsUpdated.push(prod);

										relatedProduct.relatedProduct.goodsProduct.sellableProduct = self.changedRelatedProductsList[arrayIndex].relatedProduct.goodsProduct.sellableProduct;

										prod = angular.copy(relatedProduct);
										prod.actionCode =  'A';
										relatedProductsUpdated.push(prod);
									}

									break;
								}
							}
						}
					}
				}

				if (!foundBanner) {
					prod = angular.copy(relatedProduct);
					prod.actionCode =  'D';
					relatedProductsUpdated.push(prod);
				}
			});

			// need to look for added products
			angular.forEach(self.changedRelatedProductsList, function (relatedProduct) {
				if (relatedProduct.toAdd === true) {
					prod = angular.copy(relatedProduct);
					prod.actionCode =  'A';
					relatedProductsUpdated.push(prod);
				}
			});

			return relatedProductsUpdated;
		};

		/**
		 * Whenever one of the check boxes is checked this method will update a list of what indexes need to be deleted
		 * @param index
		 */
		self.updateDeleteList = function (index) {
			if(self.deleteList.indexOf(index)<0){
				self.deleteList.push(index);
			} else {
				self.allChecked = false;
				var iterator = self.deleteList.length;
				while (iterator--) {
					if (index === self.deleteList[iterator]) {
						self.deleteList.splice(iterator, 1);
					}
				}
			}
		};

		/**
		 * This method will check all rows
		 */
		self.checkAllRows= function () {
			self.deleteList=[];
			$("#tierPricingTable tr td:nth-child(1) input[type=checkbox]").prop("checked", self.allChecked);
			if(self.allChecked){
				if(self.currentTierPricingList !== null){
					for(var index=0; index<self.currentTierPricingList.length; index++){
						self.deleteList.push(index);
					}
				}
			}
		};

		/**
		 * This method converts all of the selected values in a tags and specs attributes into a human readable string
		 * @param values
		 * @returns {string}
		 */
		self.convertToString = function (values) {
			var selectedValues = [];
			angular.forEach(values, function(value){
				if(value.selected){
					selectedValues.push(value.name);
				}
			});
			var returnString = "" + selectedValues[0];
			if(selectedValues.length>1){
				for(var index = 1; index<selectedValues.length; index++){
					returnString = returnString + ", " + selectedValues[index];
				}
			}
			return returnString;

		};

		/**
		 * Creates the choice table.
		 * @param results
		 */
		self.createChoiceTable = function(results) {
			self.choiceList = results;
		};

		/**
		 * Creates the product groups lists
		 * @param results
		 */
		self.createProductGroups = function(results) {
			self.productGroupsList = results;
		};

		/**
		 * clears the deleted flag when paginating
		 * @param relatedProductsList
		 */
		self.clearDeletedFlag = function(relatedProductsList) {
			for(var index = 0; index<relatedProductsList.length; index++){
				if (relatedProductsList[index].toDelete !== undefined) {
					relatedProductsList[index].toDelete = false;
				}
			}
		};

		/**
		 * This method will be called when click on yes button of save confirmation modal to call the save method.
		 */
		self.agreeSave = function () {
			$('#confirmSaveModal').modal("hide");

			var relatedProductsChanges = self.getRelatedProductsDifference();

			self.isLoading = true;

			if (relatedProductsChanges.length === 0) {
				self.loadUpdatedRelatedProducts();
			} else {
				productApi.saveRelatedProducts(relatedProductsChanges, self.loadUpdatedRelatedProducts, self.fetchError);
			}
		};

		/**
		 * This method will be called when click on no button of save confirmation modal to call the cancel method.
		 */
		self.cancelSave = function () {
			$('#confirmSaveModal').modal("hide");
		};

		/**
		 * Show audit information modal
		 */
		self.showOnlineAttrributesAuditInfo = function () {
			if (angular.equals(self.currentTab, relatedProductsTab)) {
				self.showRelatedProductsAuditInfo();
			} else if (angular.equals(self.currentTab, onlineAttributesTab)) {
				self.showOnlineAttributesAuditInfo();
			} else if (angular.equals(self.currentTab, tagsAndSpecsTab)) {
				self.showTagsAndSpecsAuditInfo();
			} else if (angular.equals(self.currentTab, tierPricingTab)) {
				self.showTierPricingAuditInfo();
			}
		};

		/**
		 * Show related product audit information modal
		 */
		self.showRelatedProductsAuditInfo = function () {
			self.relatedProductsAuditInfo = productApi.getRelatedProductsAudits;
			var title ="Related Products";
			var parameters = {'prodId' :self.productMaster.prodId};
			ProductDetailAuditModal.open(self.relatedProductsAuditInfo, parameters, title);
		};

		/**
		 * Show online attributes audit information modal
		 */
		self.showOnlineAttributesAuditInfo = function () {
			self.onlineAttributesAuditInfo = productApi.getOnlineAttributesAudits;
			var title ="Online Attributes";
			var parameters = {'prodId' :self.productMaster.prodId};
			ProductDetailAuditModal.open(self.onlineAttributesAuditInfo, parameters, title);
		};

		/**
		 * Show tags and specs audit information modal
		 */
		self.showTagsAndSpecsAuditInfo = function () {
			self.tagsAndSpecsAuditInfo = productApi.getTagsAndSpecsAudits;
			var title ="Tags/Specs";
			var parameters = {'prodId' :self.productMaster.prodId};
			ProductDetailAuditModal.open(self.tagsAndSpecsAuditInfo, parameters, title);
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
		 * Show tier pricing attribute Changed Log.
		 */
		self.showTierPricingAuditInfo = function(){
			self.isWaitingGetTierPricingAttributeAudit = true;
			//self.getDisplayNameInformation();
			productApi.getTierPricingAudits(
				{
					prodId: self.productMaster.prodId
				},
				//success case
				function (results) {
					self.tierPricingAttributeAudits = results;
					self.initTierPricingAttributeAuditTable();
					self.isWaitingGetTierPricingAttributeAudit = false;
				}, self.fetchError);

			$('#tier-pricing-attribute-log-modal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Init data tier pricing attribute Audit.
		 */
		self.initTierPricingAttributeAuditTable = function () {
			$scope.filter = {
				attributeName: '',
			};
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			$scope.tierPricingAttributeAuditTableParams = new ngTableParams({
				page: 1,
				count: 10,
				filter: $scope.filter,
				sorting: $scope.sorting

			}, {
				counts: [],
				data: self.tierPricingAttributeAudits
			});
		};

		/**
		 * Change sort.
		 */
		self.changeSort = function (){
			if($scope.sorting.changedOn === ORDER_BY_DESC){
				$scope.sorting.changedOn = ORDER_BY_ASC;
			}else {
				$scope.sorting.changedOn = ORDER_BY_DESC;
			}
		};

        /**
         * Handles the results of the confirmation modal
         */
        $rootScope.$on('modalResults', function(event, args){
            if(args.result){
                self.success = null;
                self.error = null;
                self.currentTierPricingList =angular.copy(self.originalTierPricingList);
                self.currentRelatedProductsList =angular.copy(self.originalRelatedProductsList);
                self.currentTagsAndSpecsList =angular.copy(self.originalTagsAndSpecsList);
                self.resetErrors();
                self.currentOnlineAttribute = angular.copy(self.origianlOnlineAttribute);
                self.thirdPartySellable = angular.copy(self.originalThirdPartySellable);
            }
        });
	}
})();
