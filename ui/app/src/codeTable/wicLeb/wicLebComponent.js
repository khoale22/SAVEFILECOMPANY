/*
 * wicLebComponent.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * Component to support the page that allows users to show wicLeb.
 *
 * @author vn70529
 * @since 2.12.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('wicLebComponent', {
		templateUrl: 'src/codeTable/wicLeb/wicLeb.html',
		bindings: {
			seleted: '<'
		},
		controller: wicLebController
	});

	wicLebController.$inject = ['$rootScope', '$scope','ngTableParams', 'ngTableEventsChannel', 'WicLebApi'];

	/**
	 *  Constructs for WicLeb Controller.
	 * @param $scope
	 * @param ngTableParams The API to set up the report table.
	 * @param ngTableEventsChannel The API to set up event of report table.
	 * @param wicLebApi The API to call the backend.
	 */
	function wicLebController($rootScope, $scope, ngTableParams, ngTableEventsChannel, wicLebApi) {

		var self = this;
		/**
		 * Start position of page that want to show on WineScoringOrganization table
		 *
		 * @type {number}
		 */
		self.PAGE = 1;

		/**
		 * The number of records to show on the  WineScoringOrganization table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;

		/**
		 * Holds the list of yes or no status to show on filter select box.
		 * @type {[*]}
		 */
		self.LEB_FILTER_VALUES = [{id:'', title:''}, {id:'y', title:'Y'}, {id:'n', title:'N'}];

		/**
		 * Holds the list of sub categories.
		 *
		 * @type {Array} the list of sub categories.
		 */
		self.wicSubCategories = [];

		/**
		 * Holds the list of leb upcs.
		 *
		 * @type {Array} the list of leb upcs.
		 */
		self.lebUpcs = [];

		/**
		 * Holds the list of leb sizes.
		 *
		 * @type {Array} the list of leb sizes.
		 */
		self.lebSizes = [];

		/**
		 * Holds the list of selected row indes.
		 *
		 * @type {Array}
		 */
		self.selectedRowsIndexes = [];

		/**
		 * Holds the selected row index to show leb upcs or leb sizes.
		 *
		 * @type {number}
		 */
		self.selectedRowIndex = -1;

		/**
		 * Holds the selected wic sub category to show leb upcs or leb sizes.
		 *
		 * @type {null}
		 */
		self.selectedWicSubCategory = null;

		/**
		 * Holds the selected row index on the leb upcs table.
		 */
		self.selectedRowIndexOnLebUpcsTable = -1;

		/**
		 * Holds the enable or disable status of leb upcs and leb sizes button.
		 * @type {boolean} true leb upcs and leb sizes button is enabled or disabled.
		 */
		self.isEnableLebUPCsAndLEBSizeButton = false;

		/**
		 * Holds the showing status of modal.
		 *
		 * @type {boolean}
		 */
		self.isShowingModal = false;
		/**
		 * Initialize the controller.
		 */
		self.init = function () {
			self.loadWicSubCategories();
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
				$rootScope.isEditedOnPreviousTab = false;
			}
		}

		/**
		 * Initial the table to show list of Wic Sub Categories.
		 */
		self.loadWicSubCategories = function () {
			self.isWaitingForResponse=true;
			wicLebApi.findAllWicLebs().$promise.then(function (response) {
				self.wicSubCategories = response;
				angular.forEach(self.wicSubCategories, function(wicSubCategory) {
					wicSubCategory.wicCategoryDisplayName = wicSubCategory.wicCategory.displayName;
				});

				self.isWaitingForResponse=false;
				self.initWicSubCategoriesTableParams();
			});
		};

		/**
		 * Initial WicSubCategories table params.
		 */
		self.initWicSubCategoriesTableParams = function () {
			$scope.tableParams = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.PAGE_SIZE, /* count per page */
			}, {
				counts: [],
				debugMode: false,
				data: self.wicSubCategories
			});

			self.setupPagesChangedEventOnWicLebTable();
		}

		/**
		 * Setup PagesChangedEventOnWicLebTable.
		 */
		self.setupPagesChangedEventOnWicLebTable = function(){
			var logPagesChangedEvent = _.partial(self.resetSelection, $scope.tableParams, "pagesChanged");
			ngTableEventsChannel.onPagesChanged(logPagesChangedEvent, $scope);
		}

		/**
		 * Initial the table to show list of leb upcs.
		 */
		self.loadLebUpcsByWicCategoryIdAndWicSubCategoryId = function () {
			self.isWaitingForResponseLebUpcs = true;
			var params = {
				wicCategoryId:self.selectedWicSubCategory.wicCategory.id ,
				wicSubCategoryId:self.selectedWicSubCategory.key.wicSubCategoryId
			}

			wicLebApi.findAllLebUpcsByWicCategoryIdAndWicSubCategoryId(params).$promise.then(function (response) {
				self.lebUpcs = response;
				self.isWaitingForResponseLebUpcs = false;
				self.initLebUPCsTableParams();
			});
		};

		/**
		 * Initial leb upcs table params.
		 */
		self.initLebUPCsTableParams = function () {
			$scope.lebUPCsTableParams = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.PAGE_SIZE, /* count per page */
			}, {
				counts:[],
				debugMode: false,
				data: self.lebUpcs
			});
		}

		/**
		 * Initial the table to show list of leb sizes.
		 */
		self.loadLebSizesByWicCategoryIdAndWicSubCategoryId = function () {
			self.isWaitingForResponseLebSizes=true;
			var params = {
				wicCategoryId:self.selectedWicSubCategory.wicCategory.id ,
				wicSubCategoryId:self.selectedWicSubCategory.key.wicSubCategoryId
			}
			wicLebApi.findAllLebSizesByWicCategoryIdAndWicSubCategoryId(params).$promise.then(function (response) {
				self.lebSizes = response;
				self.isWaitingForResponseLebSizes=false;
				self.initLebSizesTableParams();
			});
		};

		/**
		 * Initial leb sizes table params.
		 */
		self.initLebSizesTableParams = function () {
			$scope.lebUPCsTableParams = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.PAGE_SIZE, /* count per page */
			}, {
				counts:[],
				debugMode: false,
				data: self.lebSizes
			});
		}

		/**
		 * Show leb upcs by selected category and sub category on the modal.
		 */
		self.showLebUPCsModal = function(){
			self.isShowingModal = true;
			$("#lebUPCsModal").modal({backdrop: 'static',keyboard: true});
			self.loadLebUpcsByWicCategoryIdAndWicSubCategoryId();
		}

		/**
		 *  Hides the leb upcs modal.
		 */
		self.hideLebUPCsModal = function(){
			self.isShowingModal = false;
			$("#lebUPCsModal").modal("hide");
		}

		/**
		 * Show the list of leb sizes by selected category and sub category
		 * on the modal.
		 */
		self.showLebSizesModal = function(){
			self.isShowingModal = true;
			$("#lebSizesModal").modal({backdrop: 'static',keyboard: true});
			self.loadLebSizesByWicCategoryIdAndWicSubCategoryId();
		}

		/**
		 * Hides the leb size modal.
		 */
		self.hideLebSizesModal = function(){
			self.isShowingModal = false;
			$("#lebSizesModal").modal("hide");
		}

		/**
		 * Select the row when user click on the row of wic / leb table.
		 *
		 * @param event the click event.
		 * @param rowIndex the selected row
		 */
		self.selectRow = function(event, rowIndex) {
			if(event.ctrlKey) {
				self.changeSelectionStatus(rowIndex);
			} else if(event.shiftKey) {
				self.selectWithShift(rowIndex);
			} else {
				self.selectedRowsIndexes = [rowIndex];
			}
			self.loadSelectedWicSubCategory();
		};

		/**
		 * Get the list of selected rows on the wic / leb table.
		 *
		 * @returns {Array} the list of selected rows.
		 */
		self.getSelectedRows = function() {
			var selectedRows = [];
			angular.forEach(self.selectedRowsIndexes, function(rowIndex) {
				selectedRows.push($scope.lebUPCsTableParams.data[rowIndex]);
			});
			return selectedRows;
		}

		/**
		 * Change the selection status on wic / leb table.
		 *
		 * @param rowIndex the index of row to select or unselect.
		 */
		self.changeSelectionStatus = function(rowIndex) {
			if(self.isRowSelected(rowIndex)) {
				self.unselect(rowIndex);
			} else {
				self.select(rowIndex);
			}
		}

		/**
		 * Select row with press shift button.
		 *
		 * @param rowIndex the index of row to select.
		 */
		self.selectWithShift = function(rowIndex) {
			var lastSelectedRowIndexInSelectedRowsList = self.selectedRowsIndexes.length - 1;
			var lastSelectedRowIndex = self.selectedRowsIndexes[lastSelectedRowIndexInSelectedRowsList];
			var selectFromIndex = Math.min(rowIndex, lastSelectedRowIndex);
			var selectToIndex = Math.max(rowIndex, lastSelectedRowIndex);
			self.selectRows(selectFromIndex, selectToIndex);
		}

		/**
		 * Select the rows by select from index to select to index.
		 *
		 * @param selectFromIndex the begin position to select.
		 * @param selectToIndex the end position to select.
		 */
		self.selectRows = function(selectFromIndex, selectToIndex) {
			for(var rowToSelect = selectFromIndex; rowToSelect <= selectToIndex; rowToSelect++) {
				self.select(rowToSelect);
			}
		}

		/**
		 * Select the row by row index on the wic / leb table.
		 *
		 * @param rowIndex the index of row to select.
		 */
		self.select = function(rowIndex) {
			if(!self.isRowSelected(rowIndex)) {
				self.selectedRowsIndexes.push(rowIndex)
			}
		}

		/**
		 * Unselect the row by row index on the wic /leb table.
		 *
		 * @param rowIndex the index of row.
		 */
		self.unselect = function(rowIndex) {
			var rowIndexInSelectedRowsList = self.selectedRowsIndexes.indexOf(rowIndex);
			var unselectOnlyOneRow = 1;
			self.selectedRowsIndexes.splice(rowIndexInSelectedRowsList, unselectOnlyOneRow);
		}

		/**
		 * Reset selection on row wic / leb table.
		 *
		 */
		self.resetSelection = function() {
			if(!self.isShowingModal) {
				self.selectedRowsIndexes = [];
				self.selectedRowIndex = -1;
				self.selectedWicSubCategory = null;
				self.isEnableLebUPCsAndLEBSizeButton = false;
			}
			self.selectedRowIndexOnLebUpcsTable = -1;
		}

		/**
		 * Check selected row status on wic / leb table.
		 *
		 * @param rowIndex the index of row to check.
		 * @returns {boolean} true the row is selected or not.
		 */
		self.isRowSelected = function(rowIndex) {
			return self.selectedRowsIndexes.indexOf(rowIndex) > -1;
		};

		/**
		 * Load current selected row on the wic / leb table.
		 */
		self.loadSelectedWicSubCategory = function(){
			var selectedRows = angular.copy(self.selectedRowsIndexes);
			selectedRows.sort();
			self.selectedWicSubCategory = null;
			self.isEnableLebUPCsAndLEBSizeButton = false;
			self.selectedRowIndex = -1;
			for(var i = 0; i< selectedRows.length;i++){
				if($scope.tableParams.data[selectedRows[i]].lebSwitch) {
					self.selectedRowIndex = selectedRows[i];
					self.selectedWicSubCategory = $scope.tableParams.data[self.selectedRowIndex];
					self.isEnableLebUPCsAndLEBSizeButton = true;
					break;
				}
			}
		}

		/**
		 * Select row on the leb upcs table.
		 *
		 * @param index the index of selected row.
		 */
		self.selectRowOnLebUpcsTable = function(index){
			self.selectedRowIndexOnLebUpcsTable = index;
		};

		/**
		 * Clear filter.
		 */
		self.clearFilter = function(){
			$scope.tableParams.filter().wicCategoryDisplayName = undefined;
			$scope.tableParams.filter().displayName = undefined;
			$scope.tableParams.filter().displayLebSwitch = undefined;
		};
	}
})();
