/*
 *   productDetailComponent.js
 *
 *   Copyright (c) 2016 HEB
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
	angular.module('productMaintenanceUiApp').component('productDetail', {
		// isolated scope binding
		bindings: {
			listOfProducts: '=',
			selectedProduct: '=',
			viewingProductDetails: '=',
			isPrevNextPageWaiting:'=',
			pageCount:'=',
			currentPage:'=',
			pageMessage:'=',
			getProductMasterByProductId:'&',
			isNeededResetSearchParams:'='
		},
		scope : {},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productDetailMain.html',
		// The controller that handles our component logic
		controller: productDetailCntrl
	});

	productDetailCntrl.$inject = ['kitsService', 'productDetailApi', 'productApi', 'BreakPackApi', '$rootScope',
		'$scope', 'ProductSearchService', 'customHierarchyService', 'ngTableParams','appConstants',
		'ProductGroupService','$location','taskService', '$state', 'HomeSharedService', '$timeout'];


	/**
	 * Product Detail Component's controller definition.
	 *
	 * @param kitsService
	 * @param productDetailApi
	 * @param productApi
	 * @param $scope
	 */
	function productDetailCntrl(kitsService, productDetailApi, productApi, breakPackApi, $rootScope,
								$scope, productSearchService, customHierarchyService, ngTableParams ,
								appConstants, productGroupService, $location, taskService, $state, homeSharedService, $timeout) {
		var self = this;
		self.loadingData = false;

		self.isRecalled = false;

		self.tabToShowAfterUpdate = null;

		self.tabTarget = null;

		/**
		 * flag if the product is a kit
		 * @type {boolean}
		 */
		self.isKit = false;
		/**
		 * flag if the product is part of a kit
		 * @type {boolean}
		 */
		self.isPartOfKit = false;

		self.breakPackLabel = "Break Pack";
		/**
		 * The key for showing next product
		 * @type {string}
		 */
		const NEXT_PRODUCT = 'nextProduct';
		/**
		 * From product panel page
		 * @type {String}
		 */
		const FROM_PRODUCT_PANEL = 'productPanel';
        /**
		 * Id tab basic search.
         * @type {string}
         */
		const ID_TAB_PRODUCT_BASIC_SEARCH = 'productSearchBasicTab';
		/**
		 * Active ecommerce view tab
		 * @type {String}
		 */
		self.activeEcommerceViewTabClass = null;
		/**
		 * Active nutrition fact tab
		 * @type {String}
		 */
		self.activeNutritionFactTabClass = null;
		self.isDelayNextBackButton = false;
		self.transverseDownList = false;
		/**
		 * Task Detail base url.
		 * @type {string}
		 */
		const TASK_DETAIL_URL = "/task/ecommerceTask/taskInfo/";

		this.$onInit = function () {
			taskService.setAlertId(angular.copy(productSearchService.getAlertId()));
			productSearchService.setAlertId(null);
			//--------vn70529-PM-1133----------
			//----Search results Landing-------
			//In the case searching with Product Id but not from task page and return to list to online attribute-> related product tab not clicked
			if(((productSearchService.getSelectionType() === productSearchService.PRODUCT_ID
				|| ((productSearchService.getProductSearchCriteria().productIds != null
                    || (productSearchService.getProductSearchCriteria().productIds == null
                        && productSearchService.getProductSearchCriteria().upcs == null
                        && productSearchService.getProductSearchCriteria().itemCodes == null
                        && productSearchService.getProductSearchCriteria().caseUpcs == null
                        && productSearchService.getProductSearchCriteria() != null))
                    && productSearchService.getSelectionType() == null))
				&& productSearchService.getSelectedTab() !==  NUTRITION_FACT_TAB
				&& productSearchService.getSelectedTab() !== ECOMMERCE_VIEW_TAB
                && !productSearchService.getReturnToListButtonClicked())||
				productSearchService.getSelectionType() === productSearchService.BYOS ||
				productSearchService.getSelectionType() === productSearchService.CUSTOM_HIERARCHY||
				productSearchService.getSelectionType() === productSearchService.PRODUCT_DESCRIPTION ||
				productSearchService.getSelectionType() === productSearchService.SUB_DEPARTMENT ||
				productSearchService.getSelectionType() === productSearchService.CLASS ||
				productSearchService.getSelectionType() === productSearchService.COMMODITY ||
				productSearchService.getSelectionType() === productSearchService.SUB_COMMODITY){
				productSearchService.setSelectedTab(self.DEFAULT_PRODUCT_TAB.id);
				self.selectedTab.name = self.DEFAULT_PRODUCT_TAB.name;
			} else if(productSearchService.getSelectionType() === productSearchService.ITEM_CODE || productSearchService.getSelectionType() == productSearchService.CASE_UPC
            		|| ((productSearchService.getProductSearchCriteria().itemCodes != null || productSearchService.getProductSearchCriteria().caseUpcs != null)
					&& productSearchService.getSelectionType() == null)){
				productSearchService.setSelectedTab(self.DEFAULT_CASE_PACK_TAB.id);
				self.selectedTab.name = self.DEFAULT_CASE_PACK_TAB.name;
				self.activeCasePackInfoTabClass = 'ng-scope active';
			} else if(productSearchService.getSelectionType() === productSearchService.UPC
				|| (productSearchService.getSelectionType() == null && productSearchService.getProductSearchCriteria().upcs != null)){
				productSearchService.setSelectedTab(self.DEFAULT_SELLING_UNIT_TAB.id);
				self.selectedTab.name = self.DEFAULT_SELLING_UNIT_TAB.name;
				self.activeUPCTabClass = 'ng-scope active';
			}
            //In the case return to list button clicked to return back online attribute->related product tab
			else if(productSearchService.getSelectionType() === productSearchService.PRODUCT_ID
					&& productSearchService.getToggledTab() !== null
					&& productSearchService.getToggledTab() === RELATED_PRODUCT_TAB
					&& productSearchService.getReturnToListButtonClicked()){
				productSearchService.setSelectedTab(ONLINE_ATTRIBUTES_TAB);

			}
            //In the case return to list button clicked to return back variant->variants or variants item sub tab
            else if(productSearchService.getToggledTab() !== null
				&& (productSearchService.getToggledTab() === VARIANTS_SUB_TAB || productSearchService.getToggledTab() === VARIANTS_ITEM_SUB_TAB)
                && productSearchService.getReturnToListButtonClicked()){
                productSearchService.setSelectedTab(VARIANTS_TAB);

            }
            //In the case return to list button wasn't be clicked to return back variant->variants or variants item sub tab
             if(productSearchService.getSelectionType() === productSearchService.PRODUCT_ID
                && productSearchService.getToggledTab() !== null
                && (productSearchService.getToggledTab() === VARIANTS_SUB_TAB
				 	|| productSearchService.getToggledTab() === VARIANTS_ITEM_SUB_TAB
				&& !productSearchService.getReturnToListButtonClicked())){
                 self.createListOfRelatedProduct();
            }
			//---------end-PM-1139------------
			//Set selected tab(ecommerceView tab) if exist in the productSearchService
			if(productSearchService.getSelectedTab() !== null){
				self.tabId = productSearchService.getSelectedTab();
				self.selectedTab.id == productSearchService.getSelectedTab();
				productSearchService.setSelectedTab(null);
				self.toggleTabForNavigation(self.tabId);
				if(self.tabId !== self.DEFAULT_PRODUCT_TAB.id
					&& self.tabId !== self.DEFAULT_CASE_PACK_TAB.id
					&& self.tabId !== self.DEFAULT_SELLING_UNIT_TAB.id) {
					if ((productSearchService.getFromPage() == appConstants.ECOMMERCE_TASK
							|| productSearchService.getFromPage() == appConstants.NUTRITION_UPDATES_TASK
							|| productSearchService.getFromPage() == appConstants.PRODUCT_UPDATES_TASK
                            || productSearchService.getFromPage() == appConstants.ECOMMERCE_PRODUCT_PUBLISH_TASK
							|| productSearchService.getFromPage() == appConstants.CUSTOM_HIERARCHY_ADMIN)
						&& productSearchService.getListOfProducts() !== null
                        && productSearchService.getToggledTab() !== RELATED_PRODUCT_TAB
                        && ! productSearchService.getReturnToListButtonClicked()) {
						self.listOfProducts = self.getFirstProductListFromOtherPage();
					} else if ((productSearchService.getFromPage() == appConstants.HOME
								||( productSearchService.getSelectionType() === productSearchService.PRODUCT_ID
								&& productSearchService.getToggledTab() !== null
								&& productSearchService.getToggledTab() === RELATED_PRODUCT_TAB
								&& productSearchService.getReturnToListButtonClicked()))
								&& productSearchService.getListOfProducts() !== null) {
						self.listOfProducts = self.getFirstProductListFromHomePage();
					}
				}
			}
			self.presetCasePackUPCSelection(0,0);
		};

		/**
		 * Get first product list when navigate from other page to home page.
		 */
		self.getFirstProductListFromOtherPage = function () {
			var returnList = [];
			var listOfProductTempOrg = angular.copy(productSearchService.getListOfProducts());
			angular.forEach(listOfProductTempOrg, function(item){
				if(item.productMaster){
					returnList.push(item.productMaster);
				}
			});
			return returnList;
		};

		/**
		 * Get first product list when navigate from home page.
		 */
		self.getFirstProductListFromHomePage = function () {
			var returnList = [];
			var listOfProductTempOrg = angular.copy(productSearchService.getListOfProducts());
			angular.forEach(listOfProductTempOrg, function(item){
				if(item){
					returnList.push(item);
				}
			});
			return returnList;
		};

		/** Left Tab Variables */
		self.selectedTab  = {};
		self.DEFAULT_PRODUCT_TAB = {id:'productInfoTab', name:'Product Info'};
		self.DEFAULT_CASE_PACK_TAB = {id:'casePackInfoTab', name:'Case Pack Info'};
		self.DEFAULT_SELLING_UNIT_TAB = {id:'upcInfoTab', name:'UPC Info'};

		/** Top Quick search - Variables */
		self.searchInput;

		/**
		 * The Product Primary Upc size.
		 * @type {null}
		 */
		self.primarySellingUnitSize = null;
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
		 * Ecommerce view tab
		 * @type {String}
		 */
		const ECOMMERCE_VIEW_TAB = 'ecommerceViewTab';

		/**
		 * Product info tab
		 * @type {String}
		 */
		const PRODUCT_INFO_TAB = 'productInfoTab';
		/**
		 * Product infor tab name
		 * @type {String}
		 */
		const PRODUCT_INFO_TAB_NAME = 'Product Info';
		/**
		 * Nutrition fact tab
		 * @type {String}
		 */
		const NUTRITION_FACT_TAB = 'nutritionFactsTab';
		/**
		 * Ecommerce view tab name
		 * @type {String}
		 */
		const ECOMMERCE_VIEW_TAB_NAME = 'eCommerce View';

		/**
		 * Nutrition fact tab name
		 * @type {String}
		 */
		const NUTRITION_FACT_TAB_NAME = 'Nutrition Facts';

		/**
		 * Image info tab name
		 * @type {String}
		 */
		const IMAGE_INFO_TAB = 'imageInfoTab';

		/**
		 * Image info tab
		 * @type {String}
		 */
		const IMAGE_INFO_TAB_NAME = 'Image Info';
		/**
		 * Variant tab
		 * @type {String}
		 */
		const VARIANTS_TAB = "variantsTab";
        /**
         * Variant tab name
         * @type {String}
         */
        const VARIANTS_TAB_NAME = "Variants";

		/**
		 * Product shelf attribute tab
		 * @type {String}
		 */
		const PRODUCT_SHELF_ATTRIBUTES_TAB  = "productShelfAttributesTab";
		/**
		 * Special attribute tab
		 * @type {String}
		 */
		const SPECIAL_ATTRIBUTES_TAB = "specialAttributesTab";
		/**
		 * Break pack tab
		 * @type {String}
		 */
		const BREAK_PACK_TAB  = "breakPackTab";
		/**
		 * Nutrition facts tab
		 * @type {String}
		 */
		const NUTRITION_FACTS_TAB = "nutritionFactsTab";
		/**
		 * Online attribute tab
		 * @type {String}
		 */
		const ONLINE_ATTRIBUTES_TAB = "onlineAttributesTab";


        /**
         * Online attribute tab name
         * @type {String}
         */
		const ONLINE_ATTRIBUTES_NAME = "Online Attributes";

        /**
         * Related product tab
         * @type {String}
         */
        const RELATED_PRODUCT_TAB = "relatedProductTab";
        /**
         * Variant sub tab
         * @type {String}
         */
        const VARIANTS_SUB_TAB = "variantsSubTab";
        /**
         * Variant item sub tab
         * @type {String}
         */
        const VARIANTS_ITEM_SUB_TAB = "variantsItemSubTab";
		/**
		 * Case pack info tab
		 * @type {String}
		 */
		const CASE_PACK_INFO_TAB = "casePackInfoTab";
		/**
		 * Item substitution tab
		 * @type {String}
		 */
		const ITEM_SUBSTITUTION_TAB = "itemSubstitutionTab";
		/**
		 * Case pack import tab
		 * @type {String}
		 */
		const CASE_PACK_IMPORT_TAB = "casePackImportTab";
		/**
		 * Vendor info tab
		 * @type {String}
		 */
		const VENDOR_INFO_TAB = "vendorInfoTab";
		/**
		 * Department tab
		 * @type {String}
		 */
		const DEPARTMENT_TAB  = "departmentTab";
		/**
		 * Mrt info tab
		 * @type {String}
		 */
		const MRT_INFO_TAB = "mrtInfoTab";
		/**
		 * Upc info tab
		 * @type {String}
		 */
		const UPC_INFO_TAB = "upcInfoTab";
		/**
		 * Dru tab
		 * @type {String}
		 */
		const DRU_TAB = "druTab";
		/**
		 * Ware house tab
		 * @type {String}
		 */
		const WARE_HOUSE_TAB = "warehouseTab";
		/**
		 * Taxonomy tab
		 * @type {String}
		 */
		const TAXONOMY_TAB = "taxonomyTab";
		/**
		 * Collapse case pack flag.
		 * @type {boolean}
		 */
		self.isCollapseCasePack = false;
		/**
		 * Collapse selling unit flag.
		 * @type {boolean}
		 */
		self.isCollapseSellingUnit = false;
		/**
		 * Initializes the component and re-initializes it on changes.
		 */
		self.$onChanges = function () {
			self.isKit = false;
			productDetailApi.getProductRecallData({'prodId' :self.selectedProduct.prodId} ,self.loadProductRecallData, self.fetchError);
			productApi.getKitsData({'productId': self.selectedProduct.prodId}, self.successIsKit, self.errorIsKit);
			breakPackApi.countProductRelationshipByProductId(
				{
					productId: self.selectedProduct.prodId
				},
				//success case
				function (results) {
					if(results != null && results.data != 0){
						self.breakPackLabel = "Break Pack (*)";
					}else{
						self.breakPackLabel = "Break Pack";
					}
				}
				//error case
				, function (error) {
					self.breakPackLabel = "Break Pack";
				}
			);
		};

		/**
		 * If there are product relationships of type kit then set the product as a kit, if not then check if one of its
		 * upcs is part of a kit
		 * @param results
		 */
		self.successIsKit = function (results) {
			if(results.length > 0){
				self.isKit=true;
				self.isPartOfKit=false;
				angular.forEach(results, function(relationship){
					if(!angular.equals(relationship.key.productId, self.selectedProduct.prodId)){
						self.isKit=false;
						self.isPartOfKit=true;
						self.selectedProduct.listOfInvolvedKits=results;
					}
				});
				kitsService.setIsKit(self.isKit);
			}
		};

		/**
		 * Callback that will respond to errors sent from the backend.
		 *
		 * @param error The object with error information.
		 */
		self.fetchError = function(error){
			if (error && error.data) {
				self.error = error.data.message;
			} else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Receives data from API call and assigns in to front end object.
		 *
		 * @param results
		 */
		self.loadProductRecallData = function (results) {
			self.isRecalled = false;
			self.productRecall = angular.copy(results);
			self.checkIfRecalledDataExists(self.productRecall);
		};

		/**
		 * Used to preset the selected case pack and selling unit record. By default first record in each table it set
		 * as selected record.
		 * @param casePackIndex
		 * @param sellingUnitIndex
		 */
		self.presetCasePackUPCSelection = function (casePackIndex, sellingUnitIndex) {
			if (self.selectedProduct.prodItems && self.selectedProduct.prodItems.length > 0) {
				self.selectedProduct.prodItems[casePackIndex].isSelected = true;
				self.selectedCasePack = self.selectedProduct.prodItems[casePackIndex].itemMaster;
			}
			if (self.selectedProduct.sellingUnits && self.selectedProduct.sellingUnits.length > 0) {
				self.selectedProduct.sellingUnits[sellingUnitIndex].isSelected = true;
				self.selectedSellingUnit = self.selectedProduct.sellingUnits[sellingUnitIndex];
			}
		};

		/**
		 * Handles tab change click action.
		 * @param msg
		 * @param $event
		 */
		self.handleTabChange = function(tab) {
			self.toggleTab(tab.id);
		};

		/**
		 * Opens the confirmation modal if a change has been made, otherwise changes tab
		 * @param msg
		 * @param $event
		 */
		self.confirmTabChange = function(msg, $event){
			self.removeCSSClasses();
			//Check tabs to show collapse on product summary
			switch (event.target.id){
				case PRODUCT_INFO_TAB:
				case PRODUCT_SHELF_ATTRIBUTES_TAB:
				case SPECIAL_ATTRIBUTES_TAB:
				case ONLINE_ATTRIBUTES_TAB:
				case BREAK_PACK_TAB:
				case NUTRITION_FACTS_TAB:
				case VARIANTS_TAB:
				case TAXONOMY_TAB:
					self.isCollapseCasePack = false;
					self.isCollapseSellingUnit = false;
					break;
				case CASE_PACK_INFO_TAB:
				case WARE_HOUSE_TAB:
				case VENDOR_INFO_TAB:
				case ITEM_SUBSTITUTION_TAB:
				case DEPARTMENT_TAB:
				case DRU_TAB:
				case MRT_INFO_TAB:
				case CASE_PACK_IMPORT_TAB:
					self.isCollapseCasePack = false;
					self.isCollapseSellingUnit = true;
					angular.element(document.querySelector("#casePack")).addClass("case-pack-expand-left");
					angular.element(document.querySelector("#sellingUnit")).addClass("selling-unit-collapse-right");
					break;
				case UPC_INFO_TAB:
				case IMAGE_INFO_TAB:
					self.isCollapseCasePack = true;
					self.isCollapseSellingUnit = false;
					angular.element(document.querySelector("#casePack")).addClass("case-pack-collapse-left");
					angular.element(document.querySelector("#sellingUnit")).addClass("selling-unit-expand-right");
					break;
			}

			if($rootScope.contentChangedFlag && (self.selectedTab.id !== event.target.id)){
				self.tabTarget = event.target;
				var result = document.getElementById("confirmationModal");
				var wrappedResult = angular.element(result);
				wrappedResult.modal("show");
			}
			else{
				self.handleTabChange(event.target);
			}
		};

		/**
		 * Remove css classes on product summary
		 */
		self.removeCSSClasses = function() {
			if(self.isCollapseCasePack && !self.isCollapseSellingUnit){
				angular.element(document.querySelector("#casePack")).removeClass("case-pack-collapse-left");
				angular.element(document.querySelector("#sellingUnit")).removeClass("selling-unit-expand-right");
			}else if(!self.isCollapseCasePack && self.isCollapseSellingUnit){
				angular.element(document.querySelector("#casePack")).removeClass("case-pack-expand-left")
				angular.element(document.querySelector("#sellingUnit")).removeClass("selling-unit-collapse-right");
			}
		};
		/**
		 * Receives the results of the confirmation modal and acts accordingly
		 */
		$rootScope.$on('modalResults', function(event, args){
			if(self.tabTarget !== null){
				self.handleModalResults(args.result)
			}
		});
		/**
		 * Handles the results of the confirmation modal
		 * @param result
		 */
		self.handleModalResults = function(result){
			if(result === true){
				$rootScope.contentChangedFlag = false;
				self.handleTabChange(self.tabTarget);
			}
			else{
				self.handleTabChange(self.selectedTab);
			}
			self.tabTarget = null;
		};

		/**
		 * Used to execute tab toggle events. Sets the selected tab and updates the selected Tab name  for Right Panel Header.
		 * @param tabId
		 */
		self.toggleTab = function (tabId) {
			var tabElementSelector = '#'+tabId;
			$(tabElementSelector).tab('show');
			self.selectedTab.id = tabId;
			self.selectedTab.name = angular.element(tabElementSelector)[0].innerText;
		};

		/**
		 * Used to execute tab toggle events. Sets the selected tab and updates the selected Tab name  for the navigation from other pages.
		 * @param tabId
		 */
		self.toggleTabForNavigation = function (tabId) {
			var tabElementSelector = '#'+tabId;
			$(tabElementSelector).tab('show');
			self.selectedTab.id = tabId;
			switch (tabId) {
				case ECOMMERCE_VIEW_TAB:
				{
					self.selectedTab.name = ECOMMERCE_VIEW_TAB_NAME;
					if(customHierarchyService.getReferenceUPC() !== null){
						self.activeSelectedImageTabClass = 'ng-scope active';
					} else {
						self.activeEcommerceViewTabClass = 'ng-scope active';
					}
					break;
				}
				case NUTRITION_FACT_TAB:
				{
					self.selectedTab.name = NUTRITION_FACT_TAB_NAME;
					if(customHierarchyService.getReferenceUPC() !== null){
						self.activeSelectedImageTabClass = 'ng-scope active';
					} else {
						self.activeNutritionFactTabClass = 'ng-scope active';
					}
					break;
				}
				case IMAGE_INFO_TAB:
				{
					self.selectedTab.name = IMAGE_INFO_TAB_NAME;
					if(customHierarchyService.getReferenceUPC() !== null){
						self.activeSelectedImageTabClass = 'ng-scope active';
					}
					break;
				}
				case PRODUCT_INFO_TAB:
				{
					self.selectedTab.name = PRODUCT_INFO_TAB_NAME;
					if(customHierarchyService.getReferenceUPC() !== null){
						self.activeSelectedImageTabClass = 'ng-scope active';
					} else {
						self.activeProductInfoTabClass = 'ng-scope active';
					}
					break;
				}
				case ONLINE_ATTRIBUTES_TAB:
				{
					self.selectedTab.name = ONLINE_ATTRIBUTES_NAME;
					if(customHierarchyService.getReferenceUPC() !== null){
						self.activeSelectedImageTabClass = 'ng-scope active';
					} else {
						self.activeOnlineAttributeTabClass = 'ng-scope active';
					}
					break;
				}
                case VARIANTS_TAB:
                {
                    self.selectedTab.name = VARIANTS_TAB_NAME;
                    if(customHierarchyService.getReferenceUPC() !== null){
                        self.activeSelectedImageTabClass = 'ng-scope active';
                    } else {
                        self.activeVariantsTabClass = 'ng-scope active';
                    }
                    break;
                }
			}
		};

		/**
		 * Handles changes required on the details page when a product is selected or changed. Used for the kits upc selection.
		 * @param product
		 * @param casePackIndex
		 * @param sellingUnitIndex
		 */
		self.handleProductChange = function(product, casePackIndex, sellingUnitIndex) {
			//$rootScope.$broadcast('loadCacheProducts');
			homeSharedService.broadcastClearMessage();
			self.selectedProduct = product;
			//self.primarySellingUnitSize = self.selectedProduct.productPrimarySellingUnit.tagSize;
			casePackIndex = casePackIndex ? casePackIndex:0;
			sellingUnitIndex = sellingUnitIndex ? sellingUnitIndex:0;
			self.presetCasePackUPCSelection(casePackIndex, sellingUnitIndex);
			productApi.getKitsData({'productId': self.selectedProduct.prodId}, self.successIsKit, self.errorIsKit);
		};

		/**
		 * Handles changes required on the details page when a product is selected or changed. Used for the kits upc selection.
		 * @param product
		 * @param casePackIndex
		 * @param sellingUnitIndex
		 */
		self.handleProductDropdownMenuChange = function(product) {
			homeSharedService.broadcastClearMessage();
			self.getProductMasterByProductId({productId: product.prodId});
		};

		/**
		 * Handles changes required on the details page when an item is selected or changed.
		 * @param item
		 */
		self.handleCasePackChange = function(item) {
			self.selectedCasePack = item;
			if(self.selectedCasePack && self.selectedCasePack.isCandidated &&
				(self.selectedTab.id === "itemSubstitutionTab" ||
					self.selectedTab.id === "departmentTab" ||
					self.selectedTab.id === "druTab" ||
					self.selectedTab.id === "mrtInfoTab")){
				self.toggleTab("casePackInfoTab");
			}
		};

		/**
		 * Handles changes required on the details page when a upc is selected or changed.
		 * @param upc
		 */
		self.handleSellingUnitChange = function(upc) {
			self.selectedSellingUnit = upc;
		};

		/**
		 * Function that sets the current selected values to the saved values that were available to the service when user
		 * navigated from kits table.  The values are then set to default value null, and the tab selected is set to go back to the kits page.
		 */
		self.returnToKitList = function () {
			self.selectedProduct = kitsService.getOriginalSelectedProduct();
			self.selectedCasePack = kitsService.getOriginalSelectedCasePack();
			self.selectedSellingUnit = kitsService.getOriginalSelectedSellingUnit();
			kitsService.setOriginalSelectedCasePack(null);
			kitsService.setOriginalSelectedProduct(null);
			kitsService.setOriginalSelectedSellingUnit(null);
			kitsService.setReturnEnabled(false)
			self.toggleTab('kitsTab');
		};

		/**
		 * Function that pulls the value that determines of the return button is displayed.
		 *
		 * @returns {*}
		 */
		self.isReturnEnabled = function () {
			return kitsService.isReturnEnabled();
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			//reset all values saved from kits navigation, and set value to hide return button
			kitsService.setOriginalSelectedKits(null);
			kitsService.setOriginalSelectedCasePack(null);
			kitsService.setOriginalSelectedProduct(null);
			kitsService.setOriginalSelectedSellingUnit(null);
			kitsService.setReturnEnabled(false);
			kitsService.setIsKit(false);
		};

		/**
		 * Function to check if recalled data was returned from API, if so show the recalled link.
		 *
		 * @param recalledData
		 */
		this.checkIfRecalledDataExists = function (recalledData) {
			if(angular.isUndefined(recalledData) || recalledData.qaNumber === null){
				self.isRecalled = false;
			} else {
				self.isRecalled = true;
			}
		};

		/**
		 * Handles changes required on the details page when an item is selected or changed to candidate status.
		 */
		self.handleCandidateStatusChange = function (psItemMasters) {
			self.psItemMasters = angular.copy(psItemMasters);
			if (self.selectedProduct.prodItems && self.selectedProduct.prodItems.length > 0) {
				var tempPsItemMaster = angular.copy(self.psItemMasters[0]);
				var tempProdItem = angular.copy(self.selectedProduct.prodItems[0]);
				self.checkItemIdExistInCasePack(tempPsItemMaster.itemCode);
				tempProdItem.itemMaster.key.itemCode = tempPsItemMaster.itemCode;
				tempProdItem.itemMaster.key.itemType = tempPsItemMaster.itemKeyType;
				tempProdItem.itemMaster.description = tempPsItemMaster.itemDescription;
				tempProdItem.itemMaster.pack = tempPsItemMaster.pack;
				tempProdItem.itemMaster.mrt = tempPsItemMaster.mrt;
				self.selectedProduct.prodItems.unshift(tempProdItem);

				angular.forEach(self.selectedProduct.prodItems, function(item){
					item.isSelected = false;
					item.itemMaster.isCandidated = false;
					item.itemMaster.isCandidatedStatus = true;
					item.itemMaster.newDataSw = false;
				});
				self.selectedProduct.prodItems[0].itemMaster.isCandidated = true;
				self.selectedProduct.prodItems[0].itemMaster.newDataSw = tempPsItemMaster.newData;
				self.selectedProduct.prodItems[0].isSelected = true;
			}

			self.presetCasePackUPCSelection(0,0);
			self.toggleTab("casePackInfoTab");
		};

		/**
		 * Check an item id is existing in Case Pack list and remove it.
		 * @param itmId
		 */
		self.checkItemIdExistInCasePack = function (itmId) {
			var array = angular.copy(self.selectedProduct.prodItems);
			for(var i = array.length - 1; i >= 0; i--) {
				if(array[i].itemMaster.key.itemCode === itmId) {
					self.selectedProduct.prodItems.splice(i, 1);
				}
			}
		};

		/**
		 * Handles changes of case pack tabs in candidate mode..
		 */
		self.handleCandidateTabChange = function (tabId) {
			self.toggleTab(tabId);
		};

		/**
		 * Handles the Product when values are updated.
		 *
		 * @param product
		 */
		self.handleProductMasterUpdate = function (product) {

			var foundIndex = false;
			var listOfProducts = [];
			if (self.listOfProducts) {
				for(var i = 0; i< self.listOfProducts.length; i++) {
					if(!foundIndex && self.listOfProducts[i].prodId === product.prodId) {
						foundIndex = true;
						listOfProducts.push(product);
					} else {
						listOfProducts.push(self.listOfProducts[i]);
					}
				}
			}
			self.listOfProducts = listOfProducts;
			if(self.selectedProduct.prodId == product.prodId) {
				self.selectedProduct = product;
			}
		};

		/**
		 * API request to get updated product
		 *
		 * @param product
		 */
		self.getUpdatedProduct = function () {

			// switch to desired tab
			if (self.tabToShowAfterUpdate !== null) {
				self.toggleTab(self.tabToShowAfterUpdate);
				self.tabToShowAfterUpdate = null;
			}

			productDetailApi.getUpdatedProduct(
				{prodId: self.selectedProduct.prodId}, self.handleProductMasterUpdate, self.fetchError);
		};

		/**
		 * Show published attribute Changed Log.
		 */
		self.showPublishedAttributeLog = function(){
			self.isWaitingGetPublishedAttributeAudit = true;
			self.getDisplayNameInformation();
			productDetailApi.getPublishedAttributesAudits(
				{
					prodId: self.selectedProduct.prodId,
					upc: self.selectedProduct.productPrimaryScanCodeId
				},
				//success case
				function (results) {
					self.publishedAttributeAudits = results;
					self.initPublishedAttributeAuditTable();
					self.isWaitingGetPublishedAttributeAudit = false;
				}, self.fetchError);

			$('#published-attribute-log-modal').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * Init data published attribute Audit.
		 */
		self.initPublishedAttributeAuditTable = function () {
			$scope.filter = {
				attributeName: '',
			};
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			$scope.publishedAttributeAuditTableParams = new ngTableParams({
				page: 1,
				count: 10,
				filter: $scope.filter,
				sorting: $scope.sorting

			}, {
				counts: [],
				data: self.publishedAttributeAudits
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

		/**
		 * Get display name information.
		 */
		self.getDisplayNameInformation = function () {
			self.isWaitingGetPublishedAttributeAudit = true;
			productDetailApi.getDisplayNameInformation(
				{
					productId: self.selectedProduct.prodId,
					upc: self.selectedProduct.productPrimaryScanCodeId,
					saleChannel:  '01'
				},
				function (result) {
					if(result != null){
						self.displayName = angular.copy(result.displayName);
						self.displayNameOrg = angular.copy(self.displayName);
					}
				}, self.fetchError
			);
		};

		/**
		 * Receives update product event broadcast from home shared service.
		 */
		$scope.$on('updateProduct', self.getUpdatedProduct);

		/**
		 * Receives update product event broadcast from home shared service.
		 */
		$scope.$on('navigateTabAndUpdateProduct', function(event, tab) {
			// switch to desired tab
			if (tab !== null) {
				self.toggleTab(tab);
			}
			productDetailApi.getUpdatedProduct(
				{prodId: self.selectedProduct.prodId}, self.handleProductMasterUpdate, self.fetchError);
		});

        /**
         * watcher scope event for create List of products
         */
        $scope.$on('CreateListOfProducts', function(event) {
            self.createListOfRelatedProduct();
        });

        /**
         * Create List of related  products
         */
        self.createListOfRelatedProduct = function(viewingProductDetails){
            var productMasters = [];
            angular.forEach(productSearchService.getListOfProducts(), function(item){
                productMasters.push(item.relatedProduct);
            });
            self.listOfProducts = productMasters;
        };

		/**
		 * Setter for the binding 'viewingProductDetails' received from homeController. This is a 2-way binding, so
		 * when this is changed, homeController's 'viewingProductDetails' also changes.
		 *
		 * @param viewingProductDetails
		 */
		self.setViewingProductDetails = function(viewingProductDetails){
			self.viewingProductDetails = viewingProductDetails;
			self.isNeededResetSearchParams = true;
		};

		/**
		 * get the index of the current Product from the listOfProducts
		 * @returns {number}
		 */
		self.getCurrentProductIndex = function(){
			var currentIndex = -1;
			if(self.listOfProducts != null && self.listOfProducts.length > 0){
				for(var index = 0; index< self.listOfProducts.length; index++) {
					if(self.selectedProduct && self.selectedProduct.prodId === self.listOfProducts[index].prodId){
						currentIndex = index;
						break;
					}
				}
			}
			return currentIndex;
		};

		/**
		 * Check first page is disabled or not.
		 * @returns {boolean}
		 */
		self.disableFirstPage = function(){
			if(self.isPrevNextPageWaiting){
				return true;
			}
			return self.currentPage <= 1;
		};
		/**
		 * alters the availability of the left arrow button based on whether selectedProduct has a Product before its
		 * self in the
		 * @returns {boolean}
		 */
		self.disableLeftArrow = function(){
			if(self.isPrevNextPageWaiting){
				return true;
			}
			var currentIndex = self.getCurrentProductIndex();
			if(currentIndex == 0){
				return self.currentPage <= 1;
			}else{
				return false;
			}
		};

		/**
		 *
		 * @returns {boolean}
		 */
		self.disableRightArrow = function(){
			if(self.isPrevNextPageWaiting){
				return true;
			}
			var currentIndex = self.getCurrentProductIndex();
			if(self.listOfProducts != null && (currentIndex == self.listOfProducts.length - 1)){
				return (self.currentPage >= self.pageCount);
			}
			return false;
		};
		/**
		 * Check last page is disabled or not.
		 * @returns {boolean}
		 */
		self.disableLastPage = function(){
			if(self.isPrevNextPageWaiting){
				return true;
			}
			return self.currentPage >= self.pageCount;
		};

		self.confirmProductChange = function (transverseDownList) {
			self.transverseDownList = transverseDownList;
			if($rootScope.contentChangedFlag) {
				var result = document.getElementById("confirmationNextProduct");
				var wrappedResult = angular.element(result);
				wrappedResult.modal("show");
			}
			else {
				self.nextProduct(transverseDownList);
			}
		};
		/**
		 * changes the selectedProduct to a Product that is one before or after the current selectedProduct in the listOfProducts
		 * @param transverseDownList
		 */
		self.nextProduct = function(transverseDownList){
			homeSharedService.broadcastClearMessage();
			var currentIndex = self.getCurrentProductIndex();
			if(currentIndex !== -1){
				$rootScope.contentChangedFlag = false;
				self.isDelayNextBackButton = true;
				if(transverseDownList){
					// going to the next product in the list
					if(currentIndex == self.listOfProducts.length - 1){
						// next page
						$rootScope.$broadcast('nextPage');
					}else {
						// Next product
						var selectedProduct = self.listOfProducts[currentIndex + 1];
						self.getProductMasterByProductId({productId: selectedProduct.prodId});
					}
				}else{
					// going to the previous product in the list
					if(currentIndex == 0) {
						// back page
						$rootScope.$broadcast('backPage');
					}else{
						// back product
						var selectedProduct = self.listOfProducts[currentIndex - 1];
						self.getProductMasterByProductId({productId: selectedProduct.prodId});
					}
				}
				$timeout(function () {
					self.isDelayNextBackButton = false;
				},2000);
			}
		};

		self.confirmProductChangeByFirstOrLastPage = function (transverseDownList) {
			self.transverseDownList = transverseDownList;
			if($rootScope.contentChangedFlag) {
				var result = document.getElementById("confirmationNextProduct");
				var wrappedResult = angular.element(result);
				wrappedResult.modal("show");
			}
			else {
				self.navigateToFirstOrLastPage(transverseDownList);
			}
		};

		/**
		 * changes the selectedProduct to a Product that is one before or after the current selectedProduct in the listOfProducts
		 * @param transverseDownList
		 */
		self.navigateToFirstOrLastPage = function(transverseDownList){
			homeSharedService.broadcastClearMessage();
			$rootScope.contentChangedFlag = false;
			self.isDelayNextBackButton = true;
			if(transverseDownList){
				// last page
				$rootScope.$broadcast('lastPage');
			}else{
				// first page
				$rootScope.$broadcast('firstPage');
			}
			$timeout(function () {
				self.isDelayNextBackButton = false;
			},2000);
		};

		/**
		 * update the current selected selling unit and selling unit list. This is a function binding and the  parent is
		 * being updated from the child.
		 *
		 * @param sellingUnit
		 */
		self.handleUpdatedSellingUnit = function (sellingUnit){
			var index = _.findIndex(self.selectedProduct.sellingUnits, function(list) {return list.upc == sellingUnit.upc});
			self.selectedProduct.sellingUnits[index].isSelected = true;
			self.selectedProduct.sellingUnits[index] = sellingUnit;
			self.selectedSellingUnit = sellingUnit;
		};

		/**
		 * update the current selected CasePack unit and proditems list. This is a function binding and the  parent is
		 * being updated from the child.
		 *
		 * @param casePack
		 */
		self.handleUpdatedCasePack = function (casePack){
			var index = _.findIndex(self.selectedProduct.prodItems, function(list) {return list.key.itemCode == casePack.key.itemCode});
			self.selectedProduct.prodItems[index].isSelected = true;
			self.selectedProduct.prodItems[index].itemMaster = casePack;
			self.selectedCasePack = casePack;
		};

		/**
		 * Show next product. This method is used to listens the next product event after publish was success.
		 */
		$scope.$on(NEXT_PRODUCT, function() {
			if(!self.disableRightArrow()) {
				self.nextProduct(true);
			}
		});
		/**
		 * call the search component to search product automatically
		 */
		self.searchAutomaticBackHomePage = function(){
			$rootScope.$broadcast('searchAutomaticBackHomePage');
		};

        /**
         * Used to execute tab toggle events. Sets the selected tab and updates the selected Tab name  for the navigation from other pages.
         * @param tabId
         */
        self.toggleTabAfterReturnToList = function (tabId) {
        	$('#'+tabId).tab('show');
        };

        /**
		 * handle when click on return to list button
		 */
		$scope.$on('returnToListEvent', function() {
			switch (productSearchService.getFromPage()) {
				case appConstants.PRODUCT_GROUP:
				{
					productGroupService.setReturnToListFlag(true);
					productSearchService.setDisableReturnToList(true);
					productSearchService.setFromPage(null);
					$state.go(appConstants.PRODUCT_GROUP);
					break;
				}
				case appConstants.ECOMMERCE_TASK:
				{
					taskService.setReturnToListFlag(true);
					productSearchService.setDisableReturnToList(true);
					productSearchService.setFromPage(null);
					$location.path(TASK_DETAIL_URL + taskService.getTaskId());
					break;
				}
				case appConstants.PRODUCT_UPDATES_TASK:
				{
					taskService.setReturnToListFlag(true);
					productSearchService.setDisableReturnToList(true);
					productSearchService.setFromPage(null);
					$state.go(appConstants.PRODUCT_UPDATES_TASK);
					break;
				}
				case appConstants.NUTRITION_UPDATES_TASK:
				{
					taskService.setReturnToListFlag(true);
					productSearchService.setDisableReturnToList(true);
					productSearchService.setFromPage(null);
					$state.go(appConstants.NUTRITION_UPDATES_TASK);
					break;
				}
				case appConstants.CUSTOM_HIERARCHY_ADMIN:
				{
					customHierarchyService.setNavigatedFromOtherPage(true);
					customHierarchyService.setNavigatedForFirstSearch(true);
					customHierarchyService.setReturnToListFlag(true);
					productSearchService.setDisableReturnToList(true);
					productSearchService.setFromPage(null);
					customHierarchyService.setSelectedTab('PRODUCT');
					customHierarchyService.setNotFacingHierarchyLink(true);
					$state.go(appConstants.CUSTOM_HIERARCHY_ADMIN);
					break;
				}
				case appConstants.HOME:
				{
                    //In the case return to list to variant or attribute->related product tabs
					if(productSearchService.getToggledTab() !== null
						&& (productSearchService.getToggledTab() === RELATED_PRODUCT_TAB
							|| productSearchService.getToggledTab() === VARIANTS_SUB_TAB
							|| productSearchService.getToggledTab() === VARIANTS_ITEM_SUB_TAB)){
                        productSearchService.setReturnToListButtonClicked(true);
                        productSearchService.setDisableReturnToList(true);
                        if(productSearchService.getToggledTab() === VARIANTS_SUB_TAB || productSearchService.getToggledTab() === VARIANTS_ITEM_SUB_TAB){
                            productSearchService.setSearchType(productSearchService.BASIC_SEARCH);
                            productSearchService.setSelectionType(productSearchService.PRODUCT_ID);
                            productSearchService.setSearchSelection(productSearchService.getSearchSelectionBackup());
						}
                        //In the case temporary variable has value
                        if(productSearchService.getListOfProductsTempBackup().length != 0){
                            productSearchService.setListOfProductsBackup(productSearchService.getListOfProductsTempBackup());
                        }
						if(productSearchService.getListOfProductsBackup().length > 1){
                            productSearchService.setListOfProducts(productSearchService.getListOfProductsBackup());
						}else{
                            productSearchService.setListOfProducts(null);
						}
						$('#productSearchBasicTab').tab('show');
						self.searchAutomaticBackHomePage();
					}
                    else{
                        if(!productSearchService.getIsBasicSearch()){
                        	productSearchService.setSearchTypeBackup(productSearchService.BASIC_SEARCH);
                            productSearchService.setSelectionTypeBackup(productSearchService.UPC);
                            productSearchService.setSearchSelectionBackup(null);
                            productSearchService.setIdTab(ID_TAB_PRODUCT_BASIC_SEARCH);
                        }
                        self.toggleTabAfterReturnToList(productSearchService.getIdTab());
                        productSearchService.setNavigateFromHomeSearchingResult(false);
                        productSearchService.setDisableReturnToList(true);
                        productSearchService.setFromPage(null);
                        self.searchAutomaticBackHomePage();
					}
					break;
				}
			}
		});
		/**
		 * Listen load item master loadItemMaster from homeController when change selected product to reload item master.
		 */
		$scope.$on('loadItemMaster', function() {
			self.presetCasePackUPCSelection(0,0);
		});
		/**
		 * Get the first item master from selected product.
		 */
		self.getItemMaster = function(){
			if(self.selectedProduct.prodItems && self.selectedProduct.prodItems.length>0){
				self.selectedProduct.prodItems[0].isSelected = true;//select item first
				self.selectedCasePack = self.selectedProduct.prodItems[0].itemMaster;
			}else{
				self.selectedCasePack = null;
			}
		};

		/**
		 * watcher scope event for  set styles for product summary
		 */
		$scope.$on('setStylesForProductSummary', function(event) {
			self.removeCSSClasses();
			if(!self.isCollapseCasePack && !self.isCollapseSellingUnit){
				angular.element(document.querySelector("#casePack")).removeClass("case-pack-collapse-left");
				angular.element(document.querySelector("#sellingUnit")).removeClass("selling-unit-expand-right");
			}
			else if(self.isCollapseCasePack && !self.isCollapseSellingUnit) {
				angular.element(document.querySelector("#casePack")).addClass("case-pack-collapse-left");
				angular.element(document.querySelector("#sellingUnit")).addClass("selling-unit-expand-right");
			}else if(!self.isCollapseCasePack && self.isCollapseSellingUnit){
				angular.element(document.querySelector("#casePack")).addClass("case-pack-expand-left");
				angular.element(document.querySelector("#sellingUnit")).addClass("selling-unit-collapse-right");
			}
		});
	}
}());
