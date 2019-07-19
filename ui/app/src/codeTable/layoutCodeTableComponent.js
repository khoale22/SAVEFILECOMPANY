/*
 *   layoutCodeTableComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Product Detail page's main or top most parent component.
 *
 * @author vn40486
 * @since 2.3.0
 */
(function() {
	var myApp =angular.module('productMaintenanceUiApp');

	myApp.component('codeTableLayoutComponent', {
		// isolated scope binding
		bindings: {
			selectedItem: '='
		},
		// Inline template which is bound to message variable in the component controller
		templateUrl: 'src/codeTable/layoutCodeTable.html',
		// The controller that handles our component logic
		controller: CodeTableCntrl

	});


	CodeTableCntrl.$inject = ['$rootScope', '$scope', 'ProductGroupService', 'customHierarchyService', 'AttributeMetaDataApi'];
	/**
	 * control code table screen
	 */
	function CodeTableCntrl($rootScope,$scope, productGroupService, customHierarchyService, attributeMetaDataApi) {
		var self = this;
		self.loadingData = false;
		self.error = '';
		self.success = '';
		self.itemSending = {};
		self.arraySending= [];
		self.messageSending= '';
		self.VALIDATE_COUNTRY_CODE = 'validateCountryCode';
		self.VALIDATE_PRODUCT_CATEGORY = 'validateProductCategory';
		self.VALIDATE_STATE_WARNING = 'validateStateWarning';
		self.VALIDATE_CHOICE_TYPE = 'validateChoiceType';
		self.VALIDATE_WINE_SCORING_ORGANIZATION = "validateWineScoringOrganization";
		self.VALIDATE_VARIETAL_TYPE = 'validateVarietalType';
		self.VALIDATE_VARIETAL = 'validateVarietal';
		self.VALIDATE_WINE_AREA = 'validateWineArea';
		self.VALIDATE_WINE_REGION = 'validateWineRegion';
		self.VALIDATE_PRODUCT_GROUP_TYPE = 'validateProductGroupType';
		self.VALIDATE_WINE_MAKER = 'validateWineMaker';
		self.VALIDATE_CHOICE_OPTION = 'validateChoiceOption';
		self.VALIDATE_FACTORY = 'validateFactory';
		self.VALIDATE_TOBACCO_PRODUCT_TYPE_CODE = 'validateTobaccoProductTypeCode';
		self.VALIDATE_PRODUCT_LINE = 'validateProductLine';
		self.VALIDATE_TOP_2_TOP = 'validateTop2Top';
        self.VALIDATE_BRAND = 'validateBrand';
		self.VALIDATE_COST_OWNER = 'validateCostOwner';
		self.VALIDATE_PRODUCT_LINE_BRAND = 'validateProductLineBrand';
		self.VALIDATE_TOP_2_TOP = 'validateTop2Top';
                self.VALIDATE_TIER_CODE = 'validateTierCode';
		self.RETURN_TAB = 'returnTab';
		self.returnTab = null;
		self.showGenericCodeTable = false;

		/** Left Tab Variables */
		self.selectedTab  = self.DEFAULT_BRAND_COST_OWNER_T2T_TAB;
		self.DEFAULT_BRAND_COST_OWNER_T2T_TAB = {id:'brandCostOwnerTop2Top', title:'Brand - Cost Owner - Top 2 Top'};
		self.DEFAULT_WINE_SCORING_ORGANIZATION_TAB = {id:'wineScoringOrganizationComponent', title:'Wine - Scoring Organization'};
		self.WINE_VARIETAL_TAB = {id:'winevarietal', title:'Wine - Varietal'};
		self.WINE_VARIETAL_TYPE_TAB = {id:'winevarietalType', title:'Wine - Varietal Type'};
		self.WINE_AREA_TAB = {id:'wineArea', title:'Wine - Area'};
		self.WINE_REGION_TAB = {id:'wineRegion', title:'Wine - Region'};
		self.WINE_MAKER_TAB = {id:'wineMaker', title:'Wine - Maker'};
		self.FACTORY_TAB = {id:'factory', title:'Factory'};
		self.CHOICE_TYPE_TAB = {id:'choiceType', title:'Choice Type'};
		self.WIC_LEB_TAB = {id:'wicLebComponent', title:'WIC / LEB'};
		self.CHOICE_OPTION_TAB = {id:'choice', title:'Choice'};
		self.COUNTRY_CODE_TAB = {id:'countryCode', title:'Country Code'};
		self.PRODUCT_BRAND_TAB = {id:'productBrand', title:'Product Brand'};
		self.PRODUCT_SUB_BRAND_TAB = {id:'productSubBrand', title:'Sub-Brand'};
		self.STATE_WARNING_TAB = {id:'stateWarnings', title:'State Warnings'};
		self.PRODUCT_GROUP_TYPE_TAB = {id:'productGroupType', title:'Product Group Type'};
		self.DEFAULT_PRODUCT_CATEGORY_TAB = {id:'productCategory', title:'Product Category'};
		self.PRODUCT_CATEGORY_TAB = {id:'productCategory', title:'Product Category'};
		self.TOBACCO_PRODUCT_TYPE_CODE_TAB = {id:'tobaccoProductTypeCode',title:'Tobacco Product Type Code'};
		self.PRODUCT_LINE_TAB = {id:'productLine',title:'Product Line (Sub Brand)'};
		self.COST_OWNER_TAB = {id:'costOwner', title:'Cost Owner'};
		self.TOP_2_TOP_TAB = {id:'top2Top',title:'Top 2 Top'};
        self.BRAND_TAB = {id:'brand',title:'Brand'};
		self.PRODUCT_LINE_BRAND_TAB = {id:'productLineBrand',title:'Product Line-Brand'};
		self.TOP_2_TOP_TAB = {id:'top2Top',title:'Top 2 Top'};
        self.TIER_CODE_TAB = {id:'tierCode',title:'Tier Code'};

		this.$onInit = function () {
			if(productGroupService.getNavFromProdGrpTypePageFlag() || customHierarchyService.getNavigateFromProdGroupTypePage()){
				self.handleTabChange('product-group-type');
			}
		};

		/**
		 * Handles tab change click action.
		 * @param tab Tab that was selected.
		 * @param genericSelected Whether or not a generic tab was selected.
		 */
		self.handleTabChange = function(tab, genericSelected) {
			self.returnTab = tab;
			self.previousSelectedWasGeneric = genericSelected;
			// if selected tab is not equal to previous selected
			if(self.selectedTab && tab !== self.selectedTab.id){
				if(self.selectedTab.id === self.COUNTRY_CODE_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_COUNTRY_CODE, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.PRODUCT_CATEGORY_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_PRODUCT_CATEGORY, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.STATE_WARNING_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_STATE_WARNING, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.CHOICE_TYPE_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_CHOICE_TYPE, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.DEFAULT_WINE_SCORING_ORGANIZATION_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_WINE_SCORING_ORGANIZATION, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.WINE_VARIETAL_TYPE_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_VARIETAL_TYPE, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.WINE_VARIETAL_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_VARIETAL, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.WINE_AREA_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_WINE_AREA, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.WINE_REGION_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_WINE_REGION, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.PRODUCT_GROUP_TYPE_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_PRODUCT_GROUP_TYPE, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.WINE_MAKER_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_WINE_MAKER, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.CHOICE_OPTION_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_CHOICE_OPTION, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.FACTORY_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_FACTORY, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.TOBACCO_PRODUCT_TYPE_CODE_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_TOBACCO_PRODUCT_TYPE_CODE, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.PRODUCT_LINE_TAB.id){
					$rootScope.$broadcast(self.VALIDATE_PRODUCT_LINE, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.COST_OWNER_TAB.id) {
					$rootScope.$broadcast(self.VALIDATE_COST_OWNER, {
						tab: tab
					});
				} else if(self.selectedTab.id === self.TOP_2_TOP_TAB.id){
                    $rootScope.$broadcast(self.VALIDATE_TOP_2_TOP, {
                        tab: tab
                    });
                } else if(self.selectedTab.id === self.BRAND_TAB.id){
                    $rootScope.$broadcast(self.VALIDATE_BRAND, {
                        tab: tab
                    });
                } else if(self.selectedTab.id === self.TIER_CODE_TAB.id){
                    $rootScope.$broadcast(self.VALIDATE_TIER_CODE, {
                        tab: tab
                    });
                }
				else { // previous selected has no edit functionality
					self.setSelectedTab(tab, genericSelected);
				}
			} else {
				self.setSelectedTab(tab, genericSelected);
			}
		};
		/**
		 * Sets selected tab.
		 *
		 * @param tab Tab that was selected.
		 * @param genericSelected Whether or not a generic code table was selected.
		 */
		self.setSelectedTab = function(tab, genericSelected){
			if(genericSelected){
				self.selectedTab = {
					title: tab,
					name: tab
				};
				self.showGenericCodeTable = true;
			} else {
				self.genericCodeTableSelected = null;
				self.showGenericCodeTable = false;

				if (tab === 'brand-cost-t2t') {
					self.selectedTab = self.DEFAULT_BRAND_COST_OWNER_T2T_TAB;
				} else if (tab === 'wine-scroring-org') {
					self.selectedTab = self.DEFAULT_WINE_SCORING_ORGANIZATION_TAB;
				} else if (tab === 'wine-varietal') {
					self.selectedTab = self.WINE_VARIETAL_TAB;
				} else if (tab === 'wine-varietal-type') {
					self.selectedTab = self.WINE_VARIETAL_TYPE_TAB;
				} else if (tab === 'wine-area') {
					self.selectedTab = self.WINE_AREA_TAB;
				} else if (tab === 'wine-region') {
					self.selectedTab = self.WINE_REGION_TAB;
				} else if (tab === 'wine-maker') {
					self.selectedTab = self.WINE_MAKER_TAB;
				} else if (tab === 'factory') {
					self.selectedTab = self.FACTORY_TAB;
				} else if (tab === 'choice-type') {
					self.selectedTab = self.CHOICE_TYPE_TAB;
				} else if (tab === 'wic-leb') {
					self.selectedTab = self.WIC_LEB_TAB;
				} else if (tab === 'choice') {
					self.selectedTab = self.CHOICE_OPTION_TAB;
				} else if (tab === 'countryCode') {
					self.selectedTab = self.COUNTRY_CODE_TAB;
				} else if (tab === 'product-brand') {
					self.selectedTab = self.PRODUCT_BRAND_TAB;
				} else if (tab === 'sub-brand') {
					self.selectedTab = self.PRODUCT_SUB_BRAND_TAB;
				} else if (tab === 'state-warnings') {
					self.selectedTab = self.STATE_WARNING_TAB;
				} else if (tab === 'product-group-type') {
					self.selectedTab = self.PRODUCT_GROUP_TYPE_TAB;
				} else if (tab === 'productCategory') {
					self.selectedTab = self.DEFAULT_PRODUCT_CATEGORY_TAB;
				} else if (tab==='tobacco-product-group-type-code'){
					self.selectedTab = self.TOBACCO_PRODUCT_TYPE_CODE_TAB;
				} else if (tab==='productLine'){
					self.selectedTab = self.PRODUCT_LINE_TAB;
				} else if (tab==='productLineBrand'){
					self.selectedTab = self.PRODUCT_LINE_BRAND_TAB;
				} else if (tab === 'costOwner') {
					self.selectedTab = self.COST_OWNER_TAB;
				}else if (tab==='top2Top'){
                    self.selectedTab = self.TOP_2_TOP_TAB;
                }else if (tab==='brand'){
                    self.selectedTab = self.BRAND_TAB;
                }else if (tab==='tierCode'){
                    self.selectedTab = self.TIER_CODE_TAB;
                }
			}
		};
		/**
		 * Sending data from tab to tab.
		 * @param dataSending
		 * @param itemSending
		 */
		self.sendingDataToAnotherTab = function (dataSending, itemSending) {
			self.arraySending =  angular.copy(dataSending);
			self.itemSending = angular.copy(itemSending);
		};

		/**
		 * Sending message from tab to tab
		 * @param message
		 */
		self.sendingMessage = function (message) {
			self.messageSending = message;
		};

		/**
		 * Listener return to tab from child component.
		 */
		$scope.$on(self.RETURN_TAB, function() {
			self.setSelectedTab(self.returnTab, self.previousSelectedWasGeneric);
		});

		/**
		 * Gets current results for dropdown by calling api.
		 * @param query
		 */
		self.getCurrentDropDownResults = function(query){
			self.error = null;
			var parameters = {
				name: query,
				hasStandardCodeTable: true,
				firstSearch: false
			};
			attributeMetaDataApi.findAll(parameters).$promise.then(
				function(results){
					self.valueList = results.data;
				}, function(error){
					buildError("Could not load code table selection drop down due to: ", error);
				}
			)
		};

		/**
		 * Builds the error given a pre-text string, and api error message.
		 *
		 * @param errorPreText Pre-text for an error message.
		 * @param error Error message from api.
		 */
		function buildError(errorPreText, error){
			self.error = errorPreText + getError(error);
		}

		/**
		 * Gets the error given an api error message.
		 *
		 * @param error Error message from api.
		 * @returns {string}
		 */
		function getError(error){
			if (error && error.data) {
				if(error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return "An unknown error occurred.";
			}
		}
	}
}());
