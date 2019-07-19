/*
 *   productGroupSearchController.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * The controller for the product group search.
 *
 * @author vn75469
 * @since 2.15.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.controller('ProductGroupSearchController', ProductGroupSearchController);

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

	app.constant('productGroupConstant',
		{
			CHANGE_MODE_EVENT: 'changeMode',
			CHANGE_MODE_EVENT_AFTER_DELETING: 'changeModeAfterDeleting',
			SEARCH_MODE: 'search-mode',
			DETAIL_MODE: 'detail-mode',
			CHANGE_PRODUCT_GROUP: 'reloadProductInfo',
			IMAGE_ENCODE: 'data:image/jpg;base64,',
			IMAGE_PRIORITY_CD_ALTERNATE: "A    ",
			IMAGE_PRIORITY_CD_PRIMARY: "P    ",
			SUCCESSFULLY_UPDATED: "Successfully Updated",
		});
	/**
	 * Directive for filter on textbox: input number only.
	 */
	app.directive('numbersOnly', function(){
		return {
			require: 'ngModel',
			link: function(scope, element, attrs, modelCtrl) {
				modelCtrl.$parsers.push(function (inputValue) {
					// this next if is necessary for when using ng-required on your input.
					// In such cases, when a letter is typed first, this parser will be called
					// again, and the 2nd time, the value will be undefined
					if (inputValue == undefined) return '';
					var transformedInput = inputValue.replace(/[^0-9]/g, '');
					if (transformedInput!=inputValue) {
						modelCtrl.$setViewValue(transformedInput);
						modelCtrl.$render();
					}
					return transformedInput;
				});
			}
		};
	});

	ProductGroupSearchController.$inject = ['$rootScope', '$scope', 'ngTableParams', 'codeTableApi', 'productGroupApi',
		'imageInfoApi', 'productGroupConstant', '$state', 'appConstants','CustomHierarchyApi','$http',
		'ProductGroupService', 'customHierarchyService', 'DownloadImageService', 'ProductSearchService'];

	/**
	 * Product Group Search controller definition.
	 * @param $rootScope
	 * @param $scope    scope of the product group.
	 * @param codeTableApi
	 * @param ngTableParams
	 * @param productGroupApi
	 * @param imageInfoApi
	 * @param productGroupConstant
	 * @param productGroupService
	 * @param customHierarchyApi
	 * @param $http
	 * @constructor
	 */

	function ProductGroupSearchController($rootScope, $scope, ngTableParams, codeTableApi, productGroupApi, imageInfoApi,
										  productGroupConstant, $state, appConstants,customHierarchyApi, $http,
										  productGroupService, customHierarchyService, downloadImageService, productSearchService) {

		var self = this;
		const FROM_PRODUCT_GROUP_SEARCH = "FROM_PRODUCT_GROUP_SEARCH";
		/**
		 * Flag for showing screen: PG Search or PG Details
		 * @type {string}
		 */
		self.mode = productGroupConstant.SEARCH_MODE;

		/**
		 * Messages.
		 * @type {string}
		 */
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		self.NO_PRODUCT_SELECTED = "<h6>No Product Group is selected.<br>" +
			"Please select Product Group Search criteria to view Product Group from the left pane “Product Group Search\".</h6>";
		self.NO_MATCHES_FOUND = "<h6>No matches found.</h6>";
		self.MESSAGE_CUSTOMER_HIERARCHY_ERROR = "Customer Hierarchy search only applies for lowest level. Please select a lowest level and try again.";

		/**
		 * Define search type.
		 * @type {number}
		 */
		const COMBINED_SEARCH = -1;
		const NOT_INPUT_KEYWORD = 0;
		const SEARCH_BY_ID = 1;
		const SEARCH_BY_NAME = 2;
		const SEARCH_BY_TYPE = 3;
		const SEARCH_BY_CUSTOMER_HIERARCHY = 4;
		const BROWSER_ALL = 5;

		/**
		 * Whether or not the controller is waiting for data.
		 *
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Flag check if search by keyword or search all.
		 * @type {boolean}
		 */
		self.isBrowserAll = false;

		/**
		 * Contains error message.
		 * @type {string|*}
		 */
		self.error = self.NO_PRODUCT_SELECTED;

		/**
		 * Data product group id input.
		 * @type {string}
		 */
		self.productGroupIdInput = "";
		/**
		 * Data product group name input.
		 * @type {string}
		 */
		self.productGroupNameInput = "";
		/**
		 * Data product group type input.
		 * @type {string}
		 */
		self.productGroupTypeInput = "";
		/**
		 * Data customer hierachy input input.
		 * @type {string}
		 */
		self.customerHierarchyInput = "";

		/**
		 * List of all Product Group Types.
		 * @type {Array}
		 */
		self.productGroupTypes = [];
		self.totalRecord = 0;

		/**
		 * HEB hierarchy context
		 *
		 * @type {{id: string, description: string, parentEntityId: number, childRelationships: null, isCollapsed: boolean}}
		 */
		self.hierarchyContext = {
			"id" : "CUST",
			"description" : "HEB.Com",
			"parentEntityId" : 2864,
			"childRelationships" : null,
			"isCollapsed" : true
		};
		/**
		 * Contain type of search.
		 */
		self.searchType;

		/**
		 * Contain search description show on header of result table.
		 *
		 * @type {string}
		 */
		self.searchDescription = "<strong>Search</strong>";

		/**
		 * Flag check for show/hide customer hierarchy.
		 * @type {boolean}
		 */
		self.isShowCustomerHierarchy = false;

		/**
		 * Flag wait load customer hierachy.
		 * @type {boolean}
		 */
		self.isWaitingForLoadCustomerHierarchyResponse = false;

		/**
		 * Customer hierachy context.
		 * @type {Array}
		 */
		self.customerHierarchyContext = [];

		/**
		 * Customer hierachy context root.
		 * @type {Array}
		 */
		self.customerHierarchyContextRoot = [];

		/**
		 * Customer hierachy search text.
		 * @type {String}
		 */
		self.customHierarchySearchText = "";

		/**
		 * Flag searching for customer hierachy.
		 * @type {boolean}
		 */
		self.searchingForCustomHierarchy = false;

		/**
		 * Flag searching for customer hierachy.
		 * @type {boolean}
		 */
		self.isSearchingOnClientSide = false;

		/**
		 * Define for level of hierachy.
		 */
		var STRING_TAG = "#";
		var ADD_CLICK_BACKGROUND_CLASS = "add-click-background";
		var ADD_CLICK_BACKGROUND_CLASS_REFERENCE = ".add-click-background";
		var FIRST_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "firstLevel";
		var SECOND_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "secondLevel";
		var THIRD_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "thirdLevel";
		var FOURTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "fourthLevel";
		var FIFTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "fifthLevel";
		var FIRST_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX_POP_UP = "firstLevelPopUp";
		var SECOND_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX_POP_UP = "secondLevelPopUp";
		var THIRD_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX_POP_UP = "thirdLevelPopUp";
		var FOURTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX_POP_UP = "fourthLevelPopUp";
		var FIFTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX_POP_UP = "fifthLevelPopUp";
		var STRING_HYPHEN = "-";
		/**
		 * Template for searching for custom hierachy.
		 */
		self.searchingForCustomHierarchyTextTemplate = "Searching HEB.Com custom hierarchy for: $searchText. Please wait...";
		/**
		 * Searching for custom hierachy text
		 */
		self.searchingForCustomHierarchyText = null;
		/**
		 * Flag check if exist/ not exist product group.
		 *
		 * @type {boolean}
		 */
		self.isExistProduct = false;

		/**
		 * The parameters passed to the application from ng-tables getData method. These need to be stored
		 * until all the data are fetched from the backend.
		 *
		 * @type {null}
		 */
		self.dataResolvingParams = null;

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 *
		 * @type {?}
		 */
		self.defer = null;

		/**
		 * Selected edited row index.
		 *
		 * @type {null}
		 */
		self.selectedRowIndex = -1;

		/**
		 * Default String of image data if image not found.
		 * @type {string}
		 */
		self.IMAGE_NOT_FOUND = 'image-not-found';

		/**
		 * Map contains image data.
		 *
		 * @type {Map}
		 */
		self.cacheImages = new Map();
		/**
		 * Current image loading.
		 *
		 * @type {string}
		 */
		self.imageBytes = '';

		/**
		 * Start position of page that want to show on Product Group Search results.
		 *
		 * @type {number}
		 */
		self.PAGE = 1;

		/**
		 * The number of records to show on the Product Group Search results.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 10;

		self.isShowDetailCustomerHierarchy = false;

		/**
		 * call when initial.
		 */
		self.init = function(){
			if(!customHierarchyService.getDisableReturnToList() || !productSearchService.getDisableReturnToList()){
				customHierarchyService.setDisableReturnToList(true);
				productGroupService.setProductGroupId(null);
			}
			self.findAllProductGroupType();
			self.buildResultDatatable(true);
			if(customHierarchyService.getFromPage() == FROM_PRODUCT_GROUP_SEARCH){
				customHierarchyService.setFromPage(null);
				self.isBrowserAll = productGroupService.isBrowserAll();
				if(self.isBrowserAll) {
					self.browserAll();
					productGroupService.setProductGroupId(null);
					productGroupService.setProductGroupTypeCode(null);
				}else{
					self.productGroupId = productGroupService.getProductGroupId();
					productGroupService.setProductGroupId(null);
					self.productGroupNameInput = productGroupService.getProductGroupName();
					self.productGroupTypeInput = productGroupService.getProductGroupTypeCode();
					self.customerHierarchyInput = productGroupService.getCustomerHierarchy();
					self.customHierarchySearchText = productGroupService.getCustomHierarchySearchText();
					if(self.customerHierarchyInput!=null && self.customerHierarchyInput != undefined) {
						self.isSelectedCustomHierarchy = true;
						self.showHideCustomerHierarchy();
						self.showHideDetailCustomerHierarchy();
					}
					productGroupService.setProductGroupId(null);
					productGroupService.setProductGroupTypeCode(null);
					self.search();
				}
				productGroupService.setBrowserAll(false);
				productGroupService.setCustomerHierarchy(null);
				productGroupService.setCustomHierarchySearchText(null);
				productGroupService.setProductGroupName(null);
			}else {
				if ((productGroupService.getReturnToListFlag() || productGroupService.getNavFromProdGrpTypePageFlag() || productGroupService.getNavigateFromCustomerHierPage())
					&& productGroupService.getProductGroupId() !== null
					&& productGroupService.getProductGroupId() !== undefined) {
					self.productGroupIdInput = productGroupService.getProductGroupId();
					productGroupService.setProductGroupId(null);
					self.search();
				}
			}
		};

		/**
		 * get all product group types from api.
		 */
		self.findAllProductGroupType = function () {
			codeTableApi.findAllProductGroupTypes().$promise.then(function (response) {
				self.productGroupTypes = response;
			});
		};

		/**
		 * Handle browser all button.
		 */
		self.browserAll = function(){
			productGroupService.setBrowserAll(true);
			productGroupService.setListOfProducts(null);
			self.success = '';
			self.error = '';
			self.isBrowserAll = true;
			self.resetTable();
			self.buildResultDatatable();
		};

		/**
		 * Handle search button.
		 */
		self.search = function(){
			self.success = '';
			self.error = '';
			self.isBrowserAll = false;
			self.searchType = self.getSearchType();
			if (self.isCombinedSearch()){
				$('#popupWarning').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			} else if(self.isNotInputKeyword()){
				self.isExistProduct = false;
				self.error = self.NO_PRODUCT_SELECTED;
			} else if (!self.isNullOrEmpty(self.customerHierarchyInput)){
				self.isLowestBranchOrHasNoRelationships(self.customerHierarchyInput)
			}
			else {
				self.resetTable();
				self.buildResultDatatable();
			}
		};

		/**
		 * Call api to search.
		 */
		self.callApiToSearch = function(){
			self.isWaiting = true;
			productGroupApi.findProductGroup(self.getPaginationParams(), self.callApiSuccess, self.callApiError);
		};

		/**
		 * Get pagination params from ui.
		 *
		 * @returns {{page: number, pageSize: number, firstSearch: (boolean|*), productGroupId: null, productGroupName: null, productGroupType: null, customerHierarchy: null}}
		 */
		self.getPaginationParams = function(){
			var page = (self.dataResolvingParams==null)?0:self.dataResolvingParams.page() - 1;
			var pageSize = (self.dataResolvingParams==null)?20:self.dataResolvingParams.count();
			var paginationParams = {
				'page': page,
				'pageSize': pageSize,
				'firstSearch': self.isFirstSearch(),
				'productGroupId': null,
				'productGroupName': null,
				'productGroupType': null,
				'customerHierarchyId': null
			};
			if (!self.isBrowserAll){
				if (self.searchType === SEARCH_BY_ID){
					if(productGroupService.getReturnToListFlag() && productGroupService.getProductGroupId() !== null
						&& productGroupService.getProductGroupId() !== undefined){
						paginationParams.productGroupId = productGroupService.getProductGroupId();
						productGroupService.setProductGroupId(null);
					}else{
						paginationParams.productGroupId = self.productGroupIdInput;
					}
				} else if(self.searchType === SEARCH_BY_NAME){
					paginationParams.productGroupName = self.productGroupNameInput;
				} else if(self.searchType === SEARCH_BY_TYPE){
					paginationParams.productGroupType = self.productGroupTypeInput.productGroupTypeCode.trim();
				} else if(self.searchType === SEARCH_BY_CUSTOMER_HIERARCHY){
					paginationParams.customerHierarchyId = self.customerHierarchyInput.key.childEntityId;
				}
			}

			return paginationParams;
		};

		/**
		 * Check the status to get or not get the total records from server.
		 *
		 * @returns {boolean|*} true then get the total records or not.
		 */
		self.isFirstSearch = function () {
			return  self.dataResolvingParams === null || self.dataResolvingParams.page() === 1;
		};

		/**
		 * Processing if call api success.
		 *
		 * @param response data response from api.
		 */
		self.callApiSuccess = function(response){
			self.isExistProductSelected = false;
			self.isWaiting = false;
			if(self.isFirstSearch()){
				self.totalRecord = response.recordCount;
			}
			if(self.totalRecord === 0){
				self.isExistProduct = false;
				self.error = self.NO_MATCHES_FOUND;
				self.searchDescription = "<strong>Searched for \"" + self.getSearchTypeText() + "</strong>\"";
			} else if(self.totalRecord === 1) {
				self.resetTable();
				self.error = null;
				var productGroupIds = [];
				productGroupIds.push(response.data[0].custProductGroupId);
				self.dataSent = {
					'productGroup': response.data[0],
					'productGroupIds': productGroupIds
				};
				self.mode = productGroupConstant.DETAIL_MODE;
				//self.goToProductGroupDetail(response.data[0].custProductGroupId);
			} else {
				self.isExistProduct = true;
				self.error = null;
				self.dataResolvingParams.total(self.totalRecord);
				self.defer.resolve(response.data);
				self.findAllImage(self.getListImageUri(response.data));
				if (self.getSearchTypeText() !== BROWSER_ALL){
					self.searchDescription = "<strong>Searched for \"" + self.getSearchTypeText() + "</strong>\""
						+ "  Result " + self.getRangeElementOfPage(self.totalRecord) + " of " + self.totalRecord;
				} else {
					self.searchDescription = "<strong>Search </strong>" +   "Result " + self.getRangeElementOfPage(self.totalRecord) + " of " + self.totalRecord;
				}
			}
		};

		/**
		 * Show message error when call api failed.
		 *
		 * @param error error response from api.
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
		 * Build ResultDataTable.
		 *
		 * @param isInitial flag check for ignore call api if init screen.
		 */
		self.buildResultDatatable = function (isInitial) {
			self.resultTable = new ngTableParams(
				{
					page: self.PAGE, /* show first page */
					count: self.PAGE_SIZE, /* count per page */
				},
				{
					counts: [],
					total: 0,
					paginationClass: 'min',
					getData: function ($defer, params) {
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.selectedRowIndex = -1;
						if(isInitial){
							isInitial = false;
							return;
						}{
							self.callApiToSearch();
						}
					}
				});
		};

		/**
		 * Reset data of table.
		 */
		self.resetTable = function(){
			self.resultTable = null;
			self.dataResolvingParams = null;
			self.defer = null;
		};

		/**
		 * Clear search form.
		 */
		self.clearSearchForm = function(){
			self.productGroupIdInput = "";
			self.productGroupNameInput = "";
			self.productGroupTypeInput = " ";
			self.customerHierarchyInput = "";
			self.isShowCustomerHierarchy = false;
			self.isShowDetailCustomerHierarchy = false;
		};

		/**
		 * Get search type to call api.
		 *
		 * @returns value of searchType.
		 */
		self.getSearchType = function(){
			var returnValue;
			var countParams = 0;
			if (!self.isNullOrEmpty(self.productGroupIdInput) ||
				(productGroupService.getReturnToListFlag() && productGroupService.getProductGroupId() !== null
					&& productGroupService.getProductGroupId() !== undefined)){
				countParams++;
				returnValue = SEARCH_BY_ID;
			}
			if(!self.isNullOrEmpty(self.productGroupNameInput)){
				countParams++;
				returnValue = SEARCH_BY_NAME;
			}
			if(self.productGroupTypeInput !== undefined && self.productGroupTypeInput != null) {
				if (!self.isNullOrEmpty(self.productGroupTypeInput.productGroupTypeCode)) {
					countParams++;
					returnValue = SEARCH_BY_TYPE;
				}
			}
			if(!self.isNullOrEmpty(self.customerHierarchyInput)){
				countParams++;
				returnValue = SEARCH_BY_CUSTOMER_HIERARCHY;
			}

			if(countParams === 0){
				returnValue = NOT_INPUT_KEYWORD;
			} else if(countParams > 1){
				returnValue = COMBINED_SEARCH;
			}
			return returnValue;
		};

		/**
		 * Get search type text: search by id, name or type.
		 *
		 * @returns {*}
		 */
		self.getSearchTypeText = function(){
			if (!self.isBrowserAll){
				if (self.searchType === SEARCH_BY_ID){
					return "Product Group ID: "  + self.productGroupIdInput;
				} else if(self.searchType === SEARCH_BY_NAME){
					return "Product Group Name: "  + self.productGroupNameInput;
				} else if(self.searchType === SEARCH_BY_TYPE){
					return "Product Group Type: "  + self.productGroupTypeInput.productGroupType.trim();
				} else if(self.searchType === SEARCH_BY_CUSTOMER_HIERARCHY){
					return "Customer Hierarchy: "  + self.customerHierarchyInput.childDescription.shortDescription;
				}
			} else {
				return BROWSER_ALL;
			}
		};

		/**
		 * Get range of element to show on screen.
		 *
		 * @param totalRecords.
		 * @returns String text of search range show on search description.
		 */
		self.getRangeElementOfPage = function(totalRecords){
			var startRange = self.PAGE_SIZE*self.getPaginationParams().page + 1;
			var lastRange;
			//Check if page is a last page.
			if ((Math.ceil(totalRecords / self.PAGE_SIZE) - 1) === self.getPaginationParams().page){
				lastRange = totalRecords;
			} else {
				lastRange = self.PAGE_SIZE*self.getPaginationParams().page + self.PAGE_SIZE;
			}
			//Check if page has only one item.
			if (startRange === lastRange){
				return startRange;
			} else {
				return startRange + " - " + lastRange;
			}
		};

		/**
		 * Check if not input keyword to search
		 *
		 * @returns {boolean} true if user not input keyword
		 */
		self.isNotInputKeyword = function(){
			return self.searchType === NOT_INPUT_KEYWORD;
		};

		/**
		 * Check if search type is combine search.
		 *
		 * @returns {boolean}
		 */
		self.isCombinedSearch = function(){
			return self.searchType === COMBINED_SEARCH;
		};

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === null || !object || object === "";
		};

		/**
		 * Handle button Show or hide CustomerHierarchy
		 */
		self.showHideCustomerHierarchy = function(){
			self.isShowCustomerHierarchy = !self.isShowCustomerHierarchy;
			if (self.customerHierarchyContext.length === 0 ){
				self.loadCustomerHierarchyContext();
			}
		};

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === null || !object || object === "" || object === undefined;
		};

		/**
		 * Set selected index of selected row.
		 *
		 * @param index the index of selected row.
		 */
		self.setSelectedRowIndexAction = function (index) {
			if (self.selectedRowIndex == index) {
				self.selectedRowIndex = -1;
			} else {
				self.selectedRowIndex = index;
			}
		};

		/**
		 * Find image in list images.
		 *
		 * @param imageUri uri of image.
		 * @returns {*} image data of image.
		 */
		self.findImageByUri = function(imageUri){
			if (self.cacheImages.has(imageUri)) {
				return self.cacheImages.get(imageUri);
			} else {
				return '';
			}
		};

		/**
		 * Get list image uri text of product group.
		 *
		 * @param data list images.
		 * @returns {Array} list images uri of images.
		 */
		self.getListImageUri = function(data){
			var listUri = [];
			angular.forEach(data,function (element) {
				if (element.primaryImage !== null){
					listUri.push(element.primaryImage.uriText);
				}
			});
			return listUri;

		};

		/**
		 * Call api to find all image.
		 *
		 * @param listUri
		 */
		self.findAllImage = function(listUri){
			angular.forEach(listUri, function (uri) {
				if (!self.cacheImages.has(uri)) {
					imageInfoApi.getImages(uri ,
						function (results) {
							if (results.data != null && results.data != '') {
								self.cacheImages.set(uri, productGroupConstant.IMAGE_ENCODE + results.data);
							} else {
								self.cacheImages.set(uri, self.IMAGE_NOT_FOUND);
							}
						}
					);
				}
			});
		};

		/**
		 * Show full product group image.
		 *
		 * @param uri uriText of image.
		 */
		self.showFullImage = function (productGroup) {
			self.imageFormat = productGroup.primaryImage.imageFormatCode;
			if (self.cacheImages.has(productGroup.primaryImage.uriText)) {
				self.imageBytes = self.cacheImages.get(productGroup.primaryImage.uriText);
			}
			$('#imageModal').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Get list product group id and product group sent to detail page
		 *
		 * @param productGroup product group selected.
		 */
		self.goToProductGroupDetail = function(productGroup){
			customHierarchyService.setDisableReturnToList(true);
			productGroupService.setNavFromProdGrpTypePageFlag(false);
			productGroupService.setCustomerHierarchyId(null);
			customHierarchyService.setEntityIdReceived(null);
			self.isWaiting = true;
			var paginationParams = {};
			var productGroupIds = [];
			if (!self.isBrowserAll){
				if (self.searchType === SEARCH_BY_ID){
					paginationParams.productGroupId = self.productGroupIdInput;
				} else if(self.searchType === SEARCH_BY_NAME){
					paginationParams.productGroupName = self.productGroupNameInput;
				} else if(self.searchType === SEARCH_BY_TYPE){
					paginationParams.productGroupType = self.productGroupTypeInput.productGroupTypeCode.trim();
				} else if(self.searchType === SEARCH_BY_CUSTOMER_HIERARCHY){
					paginationParams.customerHierarchyId = self.customerHierarchyInput.key.childEntityId;
				}
			}

			productGroupApi.findProductGroupIds(paginationParams).$promise.then(function (response) {
				productGroupIds = response;
				self.dataSent = {
					'productGroup': productGroup,
					'productGroupIds': productGroupIds
				};
				self.isWaiting = false;
				self.mode = productGroupConstant.DETAIL_MODE;
			});


		};
		/**
		 * watcher scope navigate to customer hierarchy
		 */
		$scope.$on('goToCustomerHierarchy', function(event) {
			self.goToCustomerHierarchy(productGroupService.getCustomerHierarchyId());
		});

		/**
		 * Handle when click on facing customer hierarchy link
		 */
		self.clickOnFacingCustomerHierarchy = function(customerHierarchyId) {
			customHierarchyService.setNavigatedFromOtherPage(true);
			customHierarchyService.setNavigatedForFirstSearch(true);
			customHierarchyService.setNotFacingHierarchyLink(false);
			customHierarchyService.setHierarchyContextId(null);
			customHierarchyService.setSelectedHierarchyContextForNavigation(null);
			customHierarchyService.setSelectedHierarchyContextRoot(null);
			customHierarchyService.setSelectedCustomHierarchyLevel(null);
			customHierarchyService.setEntityIdReceived(null);
			self.goToCustomerHierarchy(customerHierarchyId);
		}

		/**
		 * Go to customer hierarchy
		 */
		self.goToCustomerHierarchy = function(customerHierarchyId){
			customHierarchyService.setFromPage(FROM_PRODUCT_GROUP_SEARCH);
			customHierarchyService.setSelectedTab('PRODUCT_GROUP');
			if(productGroupService.getNavigateFromCustomerHierPage()){
				productGroupService.setNavigateFromCustomerHierPage(null);
				customHierarchyService.setDisableReturnToList(true);
			}
			else{
				customHierarchyService.setDisableReturnToList(false);
			}
			productGroupService.setCustomerHierarchyId(customerHierarchyId);
			productGroupService.setBrowserAll(self.isBrowserAll);
			if (!self.isBrowserAll){
				if (self.searchType === SEARCH_BY_ID){
					if(productGroupService.getReturnToListFlag() && productGroupService.getProductGroupId() !== null
						&& productGroupService.getProductGroupId() !== undefined){
					}else{
						productGroupService.setProductGroupId(self.productGroupIdInput);
					}
					//productGroupService.setProductGroupId(paginationParams.productGroupId);
				} else if(self.searchType === SEARCH_BY_NAME){
					productGroupService.setProductGroupName(self.productGroupNameInput);
				} else if(self.searchType === SEARCH_BY_TYPE){
					productGroupService.setProductGroupTypeCode(self.productGroupTypeInput);
				} else if(self.searchType === SEARCH_BY_CUSTOMER_HIERARCHY){
					productGroupService.setCustomerHierarchy(self.customerHierarchyInput);
					productGroupService.setCustomHierarchySearchText(self.customHierarchySearchText);
				}
			}
			$state.go(appConstants.CUSTOM_HIERARCHY_ADMIN,{customerHierarchyId:customerHierarchyId});
		};

		/**
		 * Handle clear result button.
		 */
		self.clearResults = function(){
			self.searchDescription = "<strong>Search</strong>";
			self.resetTable();
			self.isExistProduct = false;
			self.error = self.NO_PRODUCT_SELECTED;
		};

		/**
		 * Get class message to setting style for it.
		 * @param message text of message.
		 * @returns {*} class of html element.
		 */
		self.getClassOfMessage = function(message){
				if (message.length > 30){
					return "col-md-12 col-md-push-0";
				} else{
					return "col-md-4 col-md-push-4 alert alert-danger text-center myfade"
				}
		};

		/**
		 * Get style of clear button.
		 *
		 * @returns {*} CSS setting of button.
		 */
		self.getStyleOfClearBtn = function(){
			if (self.totalRecord > self.PAGE_SIZE){
				return "margin-top: -32px;margin-bottom: 10px;"
			} else {
				return "margin-bottom: 10px;"
			}
		}
		/**
		 * Listener event change mode.
		 */
		$scope.$on(productGroupConstant.CHANGE_MODE_EVENT, function(event, mode) {
			self.mode = mode;
			if(productGroupService.isBrowserAll()){
				self.productGroupIdInput = null;
				self.browserAll();
			}
		});
		/**
		 * Listener event change mode.
		 */
		$scope.$on(productGroupConstant.CHANGE_MODE_EVENT_AFTER_DELETING, function(event, mode){
			self.mode = mode;
			self.success = productGroupConstant.SUCCESSFULLY_UPDATED;
			if(!self.isBrowserAll ){
				if( self.totalRecord-1 > 0){
					self.resetTable();
					self.buildResultDatatable();
				}else{
					self.clearResults();
				}

			}else{
				self.resetTable();
				self.buildResultDatatable();
			}
		});


		/**
		 * Select custom hierarchy method.
		 *
		 * @param customHierarchyLevel
		 */
		self.selectCustomHierarchyLevel = function (customHierarchyLevel) {
			self.customerHierarchyInput = angular.copy(customHierarchyLevel);
			customHierarchyLevel.isCollapsed = !customHierarchyLevel.isCollapsed;
		}

		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param relationship
		 * @returns {boolean}
		 */
		self.isLowestBranchOrHasNoRelationships = function(relationship){
			if(relationship.childRelationships === null || relationship.childRelationships.length === 0) {
				var param = [];
				param.lowestLevelId = relationship.key.childEntityId;
				self.isWaiting = true;
				productGroupApi.checkLowestLevel(param).$promise.then(function (response) {
					if (!response.isLowestLevel){
						self.isWaiting = false;
						$('#hierarchyErrorPopup').modal({backdrop: 'static', keyboard: true});
						$('.modal-backdrop').attr('style', ' z-index: 100000; ');
					}
					else {
							self.resetTable();
							self.buildResultDatatable();
					}
				});
			}
			else {
				$('#hierarchyErrorPopup').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			}
		};

		/**
		 * Method to show hide detail hierarchy context.
		 */
		self.showHideDetailCustomerHierarchy = function () {
			self.isShowDetailCustomerHierarchy = !self.isShowDetailCustomerHierarchy;
		}

		/**
		 * Go to create Product Group page.
		 */
		self.goToCreateProductGroupPage = function (){
			productGroupService.setBrowserAll(false);
			productGroupService.setNavFromProdGrpTypePageFlag(false);
			productGroupService.setNavigateFromCustomerHierPage(false);
			self.dataSent = undefined;
			self.mode = productGroupConstant.DETAIL_MODE;
		}

		/**
		 * Load Customer Hierarchy Context. This method is used to search on client side at product group module.
		 */
		self.loadCustomerHierarchyContext = function(){
			self.isWaitingForLoadCustomerHierarchyResponse = true;
			customHierarchyApi.loadCustomerHierarchyContext({}, function(hierarchyContext){
				if(hierarchyContext != null){
					if(self.isSelectedCustomHierarchy && hierarchyContext.childRelationships.length> 0 ){
						self.setSelectedCustomHierarchy(hierarchyContext.childRelationships);
					}
					self.customerHierarchyContext.push(angular.copy(hierarchyContext));
					self.customerHierarchyContextRoot.push(angular.copy(hierarchyContext));
					if(!self.isSelectedCustomHierarchy) {
						self.customerHierarchyContext.isCollapsed = true;
						angular.forEach(self.customerHierarchyContext, function (element) {
							element.isCollapsed = true;
						})
						angular.forEach(self.customerHierarchyContextRoot, function (element) {
							element.isCollapsed = true;
						})
					}
					self.isSelectedCustomHierarchy = false;
					if(self.customHierarchySearchText != null && self.customHierarchySearchText.trim().length> 0){
						self.searchOnClient();
					}
				}
				self.isWaitingForLoadCustomerHierarchyResponse = false;

			}, function(error){
				fetchError(error);
				self.isWaitingForLoadCustomerHierarchyResponse = false;
			});
		}

		/**
		 * Search hierarchy Context by id and search text and show the results on the tree. This method is used to search on client side at product group module.
		 */
		self.searchOnClient = function(){
			initializeCustomHierarchySearchValues();
			var searchText =  self.customHierarchySearchText.trim().toLowerCase();
			var results = [];
			var currentHierarchyContext = null;
			var childRelationshipMatches = null;
			angular.forEach(self.customerHierarchyContextRoot, function (hierarchyContext) {
				// By hierarchyContextId
				childRelationshipMatches = self.findChildRelationshipMatches(searchText, hierarchyContext.childRelationships);
				currentHierarchyContext = angular.copy(hierarchyContext);
				currentHierarchyContext.childRelationships = childRelationshipMatches;
				results.push(currentHierarchyContext);
			});
			loadCustomHierarchySearch(results);
		}

		/**
		 * Find child relationship matches with search text. This method is used to search on client side at product group module.
		 *
		 * @param searchText the text to search for.
		 * @param childRelationships the list of child relationships.
		 * @returns {Array} the list of relationships matches with search text.
		 */
		self.findChildRelationshipMatches = function(searchText, childRelationships){
			var results = [];
			var currentChildRelationship;
			angular.forEach(childRelationships, function(childRelationship) {
				var matchingChildRelationships = [];
				if(!childRelationship.childRelationshipOfProductEntityType) {
					matchingChildRelationships =
						self.findChildRelationshipMatches(
							searchText, childRelationship.childRelationships);
				}
				if (matchingChildRelationships.length > 0 || (childRelationship.childDescription !== null && childRelationship.childDescription.shortDescription.trim().toLowerCase().indexOf(searchText) !== -1)) {
					currentChildRelationship = angular.copy(childRelationship);
					currentChildRelationship.childRelationships = matchingChildRelationships;
					results.push(currentChildRelationship);
				}
			});
			return results;
		}

		/**
		 * This function initializes the custom hierarchy search values by setting the search text, and setting
		 * searching for custom hierarchy to true.
		 */
		function initializeCustomHierarchySearchValues(){
			self.searchingForCustomHierarchy = true;
			setCurrentCustomHierarchySearchText();
		}

		/**
		 * This function sets the current search text based on the text the user has searched for.
		 */
		function setCurrentCustomHierarchySearchText(){
			self.searchingForCustomHierarchyText =
				self.searchingForCustomHierarchyTextTemplate
					.replace("$searchText", self.customHierarchySearchText);
		}

		/**
		 * This is the callback function for getting the custom hierarchy based on a search string.
		 */
		function loadCustomHierarchySearch(results){
			self.customerHierarchyContext = results;
			expandAllCustomHierarchyValues();
			self.searchingForCustomHierarchy = false;
			self.searchingForCustomHierarchyText = '';
			self.isShowDetailCustomerHierarchy = true;
		}

		/**
		 * This function sets the collapsed variable to false for all current levels of the product hierarchy.
		 */
		function expandAllCustomHierarchyValues(){
			angular.forEach(self.customerHierarchyContext, function (hierarchyContext) {
				hierarchyContext.isCollapsed = false;

				// for each sub department in department's subDepartmentList
				angular.forEach(hierarchyContext.childRelationships, function (firstRelationshipLevel) {
					firstRelationshipLevel.isCollapsed = false;

					// for each item class in sub department's itemClasses
					angular.forEach(firstRelationshipLevel.childRelationships, function (secondRelationshipLevel) {
						secondRelationshipLevel.isCollapsed = false;

						// for each commodity in item class's commodityList
						angular.forEach(secondRelationshipLevel.childRelationships, function (thirdRelationshipLevel) {
							thirdRelationshipLevel.isCollapsed = false;

							// for each commodity in item class's commodityList
							angular.forEach(thirdRelationshipLevel.childRelationships, function (fourthRelationshipLevel) {
								fourthRelationshipLevel.isCollapsed = false;
							});
						});
					});
				});
			});
		};

		/**
		 * This function clears the custom hierarchy search on client. This method is used to search on client side at product group module.
		 *
		 */
		self.clearCustomHierarchySearchOnClientSide = function () {
			self.customHierarchySearchText = null;
			self.isShowDetailCustomerHierarchy = false;
			self.searchingForCustomHierarchy = false;

			if (self.customerHierarchyContext.length > 0) {
				self.customerHierarchyContext = angular.copy(self.customerHierarchyContextRoot);
				self.customerHierarchyContext.isCollapsed = true;
				self.searchingForCustomHierarchy = false;
				self.searchingForCustomHierarchyText = '';
			}
		};

		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param hierarchyLevel
		 * @returns {boolean}
		 */
		self.isHierarchyLevelCollapsed = function(hierarchyLevel){
			if(typeof hierarchyLevel.isCollapsed === undefined){
				return true;
			}else {
				return hierarchyLevel.isCollapsed;
			}
		};

		/**
		 * This method removes the previous selected highlighted hierarchy level(if there is one), and then sets the
		 * current selected level in a custom hierarchy to be highlighted, and . This is done by using the string
		 * representation of the id tag (i.e. '#firstLevel') and the index of the current level and all indices of
		 * parent levels.
		 *
		 * @param firstIndex The index of the first level in the custom hierarchy.
		 * @param secondIndex The index of the second level in the custom hierarchy.
		 * @param thirdIndex The index of the third level in the custom hierarchy.
		 * @param fourthIndex The index of the fourth level in the custom hierarchy.
		 * @param fifthIndex The index of the fifth level in the custom hierarchy.
		 */
		self.highlightCurrentSelectedHierarchyLevel = function(firstIndex, secondIndex, thirdIndex, fourthIndex, fifthIndex, isPopUp) {
			// remove any html elements with 'add-click-background'
			$(ADD_CLICK_BACKGROUND_CLASS_REFERENCE).removeClass(ADD_CLICK_BACKGROUND_CLASS);

			// add a '#' tag reference to the unique element id
			var elementIdTag = STRING_TAG + self.getUniqueElementIdFromIndices(
				firstIndex, secondIndex, thirdIndex, fourthIndex, fifthIndex, isPopUp);

			// add 'add-click-background' class to current selected hierarchy level
			$(elementIdTag).addClass(ADD_CLICK_BACKGROUND_CLASS);
		};

		/**
		 * This function clears the custom hierarchy search, and either:
		 * if user has selected a hierarchy level, resets the view of that hierarchy expanded down to the level that
		 * was being previously viewed.
		 * else resets the custom hierarchy to the default non-filtered search.
		 *
		 */
		self.clearCustomHierarchySearch = function () {
			self.customHierarchySearchText = '';
				self.customerHierarchyContext = angular.copy(self.customerHierarchyContextRoot);

		};

		/**
		 * This method returns the string index of a given hierarchy level, which includes the string representation
		 * of the level (i.e. 'firstLevel') and the index of the current level and all indices of parent levels
		 * separated by a hyphen. The parent level indices and hyphen are required to guarantee a unique id per visible
		 * hierarchy level.
		 *
		 * @param firstIndex The index of the first level in the custom hierarchy.
		 * @param secondIndex The index of the second level in the custom hierarchy.
		 * @param thirdIndex The index of the third level in the custom hierarchy.
		 * @param fourthIndex The index of the fourth level in the custom hierarchy.
		 * @param fifthIndex The index of the fifth level in the custom hierarchy.
		 * @returns {string} Unique index for a hierarchy level.
		 */
		self.getUniqueElementIdFromIndices = function(firstIndex, secondIndex, thirdIndex, fourthIndex, fifthIndex, isPopUp) {
			var uniqueId;
			// first level
			if(secondIndex === null){
				uniqueId = FIRST_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex;
			}
			// second level
			else if(thirdIndex === null){
				uniqueId = SECOND_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex + STRING_HYPHEN + secondIndex;
			}
			// third level
			else if(fourthIndex === null){
				uniqueId = THIRD_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex + STRING_HYPHEN + secondIndex
					+ STRING_HYPHEN + thirdIndex;
			}
			// fourth level
			else if(fifthIndex === null){
				uniqueId = FOURTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex + STRING_HYPHEN + secondIndex
					+ STRING_HYPHEN + thirdIndex + STRING_HYPHEN + fourthIndex;
			}
			// fifth level
			else {
				uniqueId = FIFTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex + STRING_HYPHEN + secondIndex
					+ STRING_HYPHEN + thirdIndex + STRING_HYPHEN + fourthIndex + STRING_HYPHEN + fifthIndex;
			}

			if(isPopUp) {
				uniqueId = uniqueId + "PopUp";
			}
			return uniqueId;
		};

		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param relationship
		 * @returns {boolean}
		 */
		self.checkLowestBranchOrHasNoRelationships = function(relationship){
			if(relationship.lowestBranch){
				return true;
			}else if(relationship.childRelationships === null || relationship.childRelationships.length === 0) {
				return true;
			}
			return false;
		};
		/**
		 * Set selected custom hierarchy when return to list from custom hierarchy.
		 * @param childRelationships
		 * @return {*}
		 */
		self.setSelectedCustomHierarchy = function(childRelationships) {
			var result = false;
			if (childRelationships == null || childRelationships == undefined || childRelationships.length == 0
				|| self.customerHierarchyInput == null || self.customerHierarchyInput == undefined ||
				self.customerHierarchyInput.key == undefined || self.customerHierarchyInput.key == null) {
				return result;
			}
			var child = null;
			for (var i = 0; i < childRelationships.length; i++) {
				child = childRelationships[i];
				if (child.key.childEntityId == self.customerHierarchyInput.key.childEntityId) {
					child.isCollapsed = false;
					return true;
				}
				if (child.childRelationships !== null && child.childRelationships !== undefined && child.childRelationships.length > 0) {
					if (self.setSelectedCustomHierarchy(child.childRelationships)) {
						child.isCollapsed = false;
						return true;
					}
				}
			}
			return result;
		}
		/**
		 * Return selected css for selected row on custom hierarchy.
		 * @param level
		 * @return {string}
		 */
		self.getSelectedCustomHierarchyCss = function(level){
			if(self.customerHierarchyInput!= null && self.customerHierarchyInput != undefined &&
				self.customerHierarchyInput.key != undefined && self.customerHierarchyInput.key != null &&
				level.key.childEntityId ==  self.customerHierarchyInput.key.childEntityId) {
				return ADD_CLICK_BACKGROUND_CLASS;
			}
			return '';
		}
		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if (self.imageBytes !== ' ') {
				downloadImageService.download(self.imageBytes, self.imageFormat==''?'png':self.imageFormat.trim());
			}
		};

		/**
		 * Check enable searching
		 */
		self.checkEnableSearching = function () {
			if((self.productGroupIdInput !== null
				&& self.productGroupIdInput !== undefined
				&& self.productGroupIdInput !== "")
				|| (self.productGroupNameInput !== null
				&& self.productGroupNameInput !== undefined
				&& self.productGroupNameInput !== "")
				||(self.productGroupTypeInput !== null
				&& self.productGroupTypeInput !== undefined
				&& self.productGroupTypeInput !== "")
				||(self.customerHierarchyInput !== null
				&& self.customerHierarchyInput !== undefined
				&& self.customerHierarchyInput !== "")){
				self.search();
			}
		};
	}
})();
