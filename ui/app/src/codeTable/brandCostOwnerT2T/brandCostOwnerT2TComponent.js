/*
 * brandCostOwnerT2TComponent.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * Component to support the page that allows users to show brand Cost Owner T2T.
 *
 * @author vn70529
 * @since 2.12.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('brandCostOwnerT2TComponent', {
		templateUrl: 'src/codeTable/brandCostOwnerT2T/brandCostOwnerT2T.html',
		bindings: {
			seleted: '<'
		},
		controller: brandCostOwnerT2TController
	});
	brandCostOwnerT2TController.$inject = ['$rootScope', 'urlBase', 'ngTableParams', 'DownloadService', 'BrandCostOwnerT2TApi','uiSelectConfig'];
	/**
	 * Constructs for brand Cost Owner T2T Controller.
	 *
	 * @param urlBase
	 * @param ngTableParams
	 * @param downloadService
	 * @param brandCostOwnerT2TApi
	 */
	function brandCostOwnerT2TController($rootScope, urlBase, ngTableParams, downloadService, brandCostOwnerT2TApi,uiSelectConfig) {
		urlBase += '/pm/brndCstOwnrT2T';
		var self = this;
		/**
		 * The default no records found message.
		 *
		 * @type {string}
		 */
		self.NO_RECORDS_FOUND = "No records found.";
		/**
		 * The unknown error message.
		 *
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";
		/**
		 * Start position of page that want to show on  brand Cost Owner T2T table
		 *
		 * @type {number}
		 */

		self.PAGE = 1;
		/**
		 * The number of records to show on the  brand Cost Owner T2T table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;

		/**
		 * Max time to wait for excel download.
		 *
		 * @type {number}
		 */
		self.WAIT_TIME = 1200;
		/**
		 * Holds excel export all url.
		 *
		 * @type {string}
		 */
		self.exportAllURL = urlBase + '/exportAllToCSV?';
		/**
		 * Holds excel export criteria url.
		 *
		 * @type {string}
		 */
		self.exportByCriteriaURL = urlBase + '/exportByCriteriaToCSV?';
		/**
		 * The parameters that define the table showing the report.
		 *
		 * @type {ngTableParams}
		 */
		self.tableParams = null;

		/**
		 * The parameters passed from the ngTable when it is asking for data.
		 *
		 * @type {?}
		 */
		self.dataResolvingParams = null;

		/**
		 * The ngTable object that will be waiting for data while the user is
		 * being refreshed.
		 *
		 * @type {brandCostOwnerT2T}
		 */
		self.defer = null;

		/**
		 * Holds the index of selected row on the table.
		 * @type {number}
		 */
		self.selectedRowIndex = -1;

		/**
		 * Holds the status get the total records of brand cost owner t2t or not.
		 *
		 * @type {boolean}
		 */
		self.includeCount = true;

		/**
		 * Holds the total brand cost owner t2ts.
		 * @type {number}
		 */
		self.totalBrandCostOwnerT2Ts = 0;
		/**
		 * Max item show on dropdown
		 * @type {number}
		 */
		const MAX_DROPDOWN_SIZE = 100;
		/**
		 * The default search text
		 * @type {string}
		 */
		const DEFAULT_SEARCH_TEXT = '';

		/**
		 * Initialize the controller.
		 */
		self.init = function () {
			/* Initial brand cost owner top 2 top table.*/
			self.buildBrandCostOwnerT2TTable();
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
		};
		/**
		 * Open close event for product brand filter
		 * @param isOpen
		 * @param selected
		 */
		self.onOpenCloseProductBrand = function(isOpen,selected){
			console.log('isOpen='+isOpen,selected)
			if(isOpen && selected===undefined ){
				if(self.topToTop === undefined && self.costOwner===undefined && self.productBrand === undefined){
					self.loadProductBrands(isOpen,DEFAULT_SEARCH_TEXT);
				}else{
					self.loadProductBrandBySelectFilter();
				}
			}
		}
		/**
		 * Open close event for cost owner filter
		 * @param isOpen
		 * @param selected
		 */
		self.onOpenCloseCostOwner = function(isOpen,selected){
			console.log('isOpen='+isOpen,selected)
			if(isOpen && selected===undefined ){
				if(self.topToTop === undefined && self.costOwner===undefined && self.productBrand === undefined){
					self.loadCostOwners(isOpen,DEFAULT_SEARCH_TEXT);
				}else{
					self.loadProductBrandBySelectFilter();
				}
			}
		}

		/**
		 * Open close event for top to top filter
		 * @param isOpen
		 * @param selected
		 */
		self.onOpenCloseTopToTop = function(isOpen,selected){
			console.log('isOpen='+isOpen,selected)
			if(isOpen && selected===undefined ){
				if(self.topToTop === undefined && self.costOwner===undefined && self.productBrand === undefined){
					self.loadTopToTops(isOpen,DEFAULT_SEARCH_TEXT);
				}else{
					self.loadProductBrandBySelectFilter();
				}
			}
		}
		/**
		 * Initial the table to show list of brand Cost Owner T2Ts.
		 */
		self.buildBrandCostOwnerT2TTable = function () {
			self.tableParams = new ngTableParams(
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
						self.isWaitingForResponse = true;
						// Reset selected row.
						self.error = '';
						self.selectedRowIndex = -1;
						// Request the data from server.
						self.lockTextBoxSearch();
						self.loadBrndCstOwnrT2Ts();
					}
				});
		};

		/**
		 * Load product brand by search text
		 * @param search
		 */
		self.loadProductBrands = function(isOpen,search){
			if(isOpen){
				$('#productBrandFilter .ui-select-refreshing').removeClass("ng-hide");
				brandCostOwnerT2TApi.findProductBrands({
					'pageSize': MAX_DROPDOWN_SIZE,
					'includeCount': false,
					'searchText':search
				},function(response){
					$('#productBrandFilter .ui-select-refreshing').addClass("ng-hide");
					self.productBrands=response.data;
				},self.fetchError);
			}

		}

		/**
		 * Load product brand by selected filter
		 */
		self.loadProductBrandBySelectFilter = function(){
			var dataSelected = {};
			if(self.productBrand)
				dataSelected['productBrandId'] = self.productBrand.productBrandId;
			if(self.costOwner)
				dataSelected['costOwnerId'] = self.costOwner.costOwnerId;
			if(self.topToTop)
				dataSelected['topToTopId'] = self.topToTop.topToTopId;
			brandCostOwnerT2TApi.filterBySelectedData(dataSelected,function(response){
				self.productBrands=response.data;
				self.costOwners = [];
				self.topToTops = [];
				angular.forEach(self.productBrands, function(productBrand){
					angular.forEach(productBrand.productBrandCostOwners, function(item){
						if(item.costOwner){
							self.costOwners.push(item.costOwner);
							if(item.costOwner.topToTop){
								self.topToTops.push(item.costOwner.topToTop);
							}
						}

					});
				});
			},self.fetchError);
		}
		/**
		 * Select product brand event
		 * @param selected
		 */
		self.selectedProductBrand = function(selected){
			self.tableParams.page(1);
			self.tableParams.reload();
		}
		/**
		 * Select cost owner event
		 * @param selected
		 */
		self.selectedCostOwner = function(selected){
			self.tableParams.page(1);
			self.tableParams.reload();
		}

		/**
		 * Select top to top event
		 * @param selected
		 */
		self.selectedTopToTop = function(selected){
			self.tableParams.page(1);
			self.tableParams.reload();
		}

		/**
		 * Load cost owner brand by search text
		 * @param search
		 */
		self.loadCostOwners = function(isOpen, search){
			if(isOpen) {
				$('#costOwnerFilter .ui-select-refreshing').removeClass("ng-hide");
				brandCostOwnerT2TApi.filterCostOwnerByIdAndDescription({
					'pageSize': MAX_DROPDOWN_SIZE,
					'includeCount': true,
					'searchText':search
				},function(response){
					$('#costOwnerFilter .ui-select-refreshing').addClass("ng-hide");
					self.costOwners=response.data;
				},self.fetchError);
			}
		}

		/**
		 * Load cost owner brand by search text
		 * @param search
		 */
		self.loadTopToTops = function( isOpen, search){
			if(isOpen) {
				$('#topToTopFilter .ui-select-refreshing').removeClass("ng-hide");
				brandCostOwnerT2TApi.filterTopToTopByIdAndName({
					'pageSize': MAX_DROPDOWN_SIZE,
					'includeCount': true,
					'searchText':search
				},function(response){
					$('#topToTopFilter .ui-select-refreshing').addClass("ng-hide");
					self.topToTops=response.data;
				},self.fetchError);
			}

		}

		/**
		 * Load brand cost owner top 2 top.
		 */
		self.loadBrndCstOwnrT2Ts = function(){
			if(self.isFiltering()){
				/* Find by criteria*/
				brandCostOwnerT2TApi.findBrndCstOwnrT2TsByCriteria(
					self.getPaginationParamsWithFilterParams(),
					self.getBrndCstOwnrT2TsSuccess,
					self.getBrndCstOwnrT2TsError);
			}else{
				/* Find all*/
				brandCostOwnerT2TApi.findAllBrndCstOwnrT2Ts(
					self.getPaginationParams(),
					self.getBrndCstOwnrT2TsSuccess,
					self.getBrndCstOwnrT2TsError);
			}
		}
		/**
		 * Initial the pagination params of the table to show list of brand Cost Owner T2Ts.
		 * @returns {{page: number, pageSize, includeCount: (boolean|*)}}
		 */
		self.getPaginationParams = function () {
			var paginationParams = {
				'page': self.dataResolvingParams.page() - 1,
				'pageSize': self.dataResolvingParams.count(),
				'includeCount': self.isIncludeCount()
			}
			return paginationParams;
		};
		/**
		 * Initial the pagination params and filter params of the table to show list of brand Cost Owner T2Ts.
		 *
		 * @returns {{productBrandDescription: string, costOwnerName: string, top2TopName: string, page: number, pageSize, includeCount: (boolean|*)}}
		 */
		self.getPaginationParamsWithFilterParams = function () {
			var paginationParams = {
				'page': self.dataResolvingParams.page() - 1,
				'pageSize': self.dataResolvingParams.count(),
				'includeCount': self.isIncludeCount()
			};
			if(self.getProductBrandIdOrDescriptionCriteria() != DEFAULT_SEARCH_TEXT){
				paginationParams.productBrandId = self.getProductBrandIdOrDescriptionCriteria() ;
			}
			if(self.getCostOwnerIdOrNameCriteria() != DEFAULT_SEARCH_TEXT){
				paginationParams.costOwner = self.getCostOwnerIdOrNameCriteria();
			}
			if(self.getTop2TopIdOrNameCriteria() != DEFAULT_SEARCH_TEXT){
				paginationParams.top2Top = self.getTop2TopIdOrNameCriteria();
			}
			return paginationParams;
		};
		/**
		 * Check the status to get or not get the total records from server.
		 *
		 * @returns {boolean|*} true then get the total records or not.
		 */
		self.isIncludeCount = function () {
			self.includeCount = (self.dataResolvingParams.page() - 1) === 0;
			return self.includeCount;
		};

		/**
		 * lock all text box on filter
		 */
		self.lockTextBoxSearch = function(){
			$("input[name='productBrandDescription']").attr("disabled", "disabled");
			$("input[name='costOwnerName']").attr("disabled", "disabled");
			$("input[name='top2TopName']").attr("disabled", "disabled");
		}

		/**
		 * Unlock all text box on filter.
		 */
		self.unLockTextBoxSearch = function(){
			$("input[name='productBrandDescription']").removeAttr("disabled");
			$("input[name='costOwnerName']").removeAttr("disabled");
			$("input[name='top2TopName']").removeAttr("disabled");
		}

		/**
		 * Get product brand id to filter.
		 *
		 * @returns {string} the product brand id.
		 */
		self.getProductBrandIdOrDescriptionCriteria = function () {
			var keySearch = DEFAULT_SEARCH_TEXT;
			if(self.productBrand){
				keySearch=self.productBrand.productBrandId;
			}
			return keySearch;
		};

		/**
		 * Get the cost owner id or name to filter.
		 *
		 * @returns {string} the cost owner id or name.
		 */
		self.getCostOwnerIdOrNameCriteria = function () {
			var keySearch = DEFAULT_SEARCH_TEXT;
			if(self.costOwner){
				keySearch = self.costOwner.costOwnerId;
			}
			return keySearch;
		};

		/**
		 * Get the top 2 top id or name to filter.
		 *
		 * @returns {string} the top 2 top id or name.
		 */
		self.getTop2TopIdOrNameCriteria = function () {
			var keySearch = DEFAULT_SEARCH_TEXT;
			if(self.topToTop){
				keySearch = self.topToTop.topToTopId;
			}
			return keySearch;
		};

		/**
		 * Check the table is filtering or not.
		 */
		self.isFiltering = function(){
			return self.getProductBrandIdOrDescriptionCriteria() != DEFAULT_SEARCH_TEXT
				|| self.getCostOwnerIdOrNameCriteria() != DEFAULT_SEARCH_TEXT
				|| self.getTop2TopIdOrNameCriteria() != DEFAULT_SEARCH_TEXT;
		}

		/**
		 * Callback for a successful call to get the list of brand Cost Owner T2Ts from backend.
		 */
		self.getBrndCstOwnrT2TsSuccess = function (response) {
			self.unLockTextBoxSearch();
			if (self.includeCount) {
				self.totalBrandCostOwnerT2Ts = response.totalElements;
			}
			self.dataResolvingParams.total(self.totalBrandCostOwnerT2Ts);
			self.defer.resolve(response.content);
			self.isWaitingForResponse = false;
			if(response.content.length === 0){
				self.error = self.NO_RECORDS_FOUND;
			}
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error
		 *            The error from the backend.
		 */
		self.getBrndCstOwnrT2TsError = function (error) {
			self.isWaitingForResponse = false;
			self.unLockTextBoxSearch();
			self.fetchError(error);
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
		 * This method is used  when user click on icon download to export CSV..
		 */
		self.exportBrndCstOwnrT2Ts = function () {
			self.downloading = true;
			if(self.isFiltering()){
				var params = [];
				if(self.getProductBrandIdOrDescriptionCriteria() != DEFAULT_SEARCH_TEXT){
					params.push('productBrandId=' + self.getProductBrandIdOrDescriptionCriteria());
				}
				if(self.getCostOwnerIdOrNameCriteria() != DEFAULT_SEARCH_TEXT){
					params.push('costOwner=' + self.getCostOwnerIdOrNameCriteria());
				}
				if(self.getTop2TopIdOrNameCriteria() != DEFAULT_SEARCH_TEXT){
					params.push('top2Top=' + self.getTop2TopIdOrNameCriteria());
				}
				var exportUrl = self.exportByCriteriaURL + params.join('&');
				downloadService.export(exportUrl, self.createExportFileName(), self.WAIT_TIME,
					function () {
						self.downloading = false;
					});
			}else{
				downloadService.export(self.exportAllURL, self.createExportFileName(), self.WAIT_TIME,
					function () {
						self.downloading = false;
					});
			}
		};

		/**
		 * Create the name of export file. It will call from exportBrndCstOwnrT2Ts method.
		 *
		 * @returns {string} the name of export file.
		 */
		self.createExportFileName = function () {
			var d = new Date();
			var fileName = 'SearchResults_';
			fileName += (d.getMonth() + 1);
			fileName += '_' + (d.getMonth() + 1);
			fileName += '_' + (d.getDate() + 1);
			fileName += '_' + (d.getFullYear() + 1);
			fileName += '_' + (d.getHours() + 1);
			fileName += '_' + (d.getMinutes() + 1);
			fileName += '_' + (d.getSeconds() + 1);
			fileName += '.csv';
			return fileName;
		}

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.success = null;
			self.error = self.getErrorMessage(error);
			self.isWaitingForResponse = false;
		};


		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return self.UNKNOWN_ERROR;
			}
		};

		/**
		 * Clear filter.
		 */
		self.clearFilter = function(){
			self.productBrand = undefined;
			self.productBrands= [];
			self.costOwner = undefined;
			self.costOwners = [];
			self.topToTop = undefined;
			self.topToTops = [];
			self.tableParams.page(1);
			self.tableParams.reload();
		};

	}
})();
