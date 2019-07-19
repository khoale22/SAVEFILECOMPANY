/*
 *
 * attributeMaintenanceController.js
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author a786878
 * @since 2.15.0
 */
'use strict';

/**
 * The controller for the Attribute Maintenance Controller.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('AttributeMaintenanceController', attributeMaintenanceController);

	attributeMaintenanceController.$inject = ['attributeMaintenanceApi', 'ngTableParams', '$log', '$filter','sourceSystemApi'];

	/**
	 * Constructs the controller.
	 */
	function attributeMaintenanceController(attributeMaintenanceApi, ngTableParams, $log, $filter, sourceSystemApi) {

		var self = this;

		/**
		 * Size of pages for data table
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;

		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * The original Attribute Maintenance list from rest service
		 */
		self.originalAttributeMaintenanceList = [];

		/**
		 * The current selected attribute's id
		 * @type {null}
		 */
		self.selectedAttributeId = null;

		/**
		 * Whether Create New Attribute button is pressed
		 * @type {null}
		 */
		self.isCreateNewAttributeSelected = false;

		/**
		 * list of all Source Systems
		 * @type {Array}
		 */
		self.sourceSystems = [];

		/**
		 * Initiates the construction of the attribute maintenance table
		 */
		self.init = function () {
			self.isWaiting = true;
			self.touchedInit = true;
			self.newSearch(true);
			self.fetchSourceSystemData();

		};

		/**
		 * If there is an error this will display the error
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message != null && error.data.message != "") {
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
		 * Sets the error
		 * @param error
		 */
		self.setError = function (error) {
			self.error = error;
		};

		/**
		 * Builds the attribute maintenance table
		 */
		self.buildTable = function () {
			self.attributeMaintenanceTableParams = new ngTableParams(
				{
					page: 1,
					count: self.PAGE_SIZE,
				},
				{
					counts: [],

					getData: function ($defer, params) {
						self.recordsVisible = 0;
						self.isWaiting = true;
						self.data = null;

						self.defer = $defer;
						self.dataResolvingParams = params;

						var includeCounts = false;

						if (self.firstSearch) {
							includeCounts = true;
							params.page(1);
							self.firstSearch = false;
							self.startRecord = 0;
						}

						var attributeId = params.filter()["attributeId"];
						var searchText = params.filter()["attributeName"];
						var sourceSystemId = params.filter()["sourceSystemId"];

						if (typeof attributeId === "undefined") {
							attributeId = null;
						}
						if (typeof searchText === "undefined") {
							searchText = "";
						}

						if (typeof sourceSystemId === "undefined") {
							sourceSystemId = null;

						}
						self.fetchData(includeCounts, params.page() - 1, searchText, sourceSystemId, attributeId);
					}
				}
			);
		};

		/**
		 * Initiates a call to get the list of attribute maintenance records.
		 *
		 * @param includeCounts Whether or not to include getting record counts.
		 * @param page The page of data to ask for.
		 * @param searchText text being used for attribute filtering.
		 * @param sourceSystemId ID being used for attribute filtering.
		 * @param attributeId ID being used for attribute filtering.
		 */
		self.fetchData = function(includeCounts, page, searchText, sourceSystemId, attributeId) {
			attributeMaintenanceApi.getAttributes({
				attributeName: searchText, attributeId: attributeId, sourceSystemId: sourceSystemId, page: page, pageSize: self.PAGE_SIZE, includeCounts: includeCounts
			}, self.loadData, self.fetchError);
		};

		/**
		 * Initiates a call to get the list of source system records.
		 *
		 */
		self.fetchSourceSystemData = function() {
			sourceSystemApi.findAll({}, self.loadSourceSystemData, self.fetchError);
		};

		/**
		 * Callback for when data is successfully returned from the backend.
		 *
		 * @param results The data returned from the backend.
		 */
		self.loadData = function(results) {
			self.isWaiting = false;
			self.data = results.data;
			self.startRecord = results.page * self.PAGE_SIZE;
			self.recordsVisible = results.data.length;
			self.defer.resolve(self.data);

			if (results.complete) {
				self.totalPages = results.pageCount;
				self.totalRecords = results.recordCount;
				self.dataResolvingParams.total(self.totalRecords);
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
			}
		};

		/**
		 * Callback for when data is successfully returned from the backend.
		 *
		 * @param results The data returned from the backend.
		 */
		self.loadSourceSystemData = function(results) {
			self.isWaiting = false;
			self.sourceSystems.push({id:null,title:"All Sources"})
			for (var x=0;x<results.length;x++)
			{
				var result=results[x];
				self.sourceSystems.push({id:result.id, title:result.displayName})
			}

		};

		/**
		 * Initiates a new search.
		 *
		 * @param findAll Should this search return all vendor subscriptions?
		 */
		self.newSearch = function(findAll) {
			self.error = null;
			self.success = null;
			self.findAll = findAll;
			self.firstSearch = true;
			if (self.tableParams == null) {
				self.buildTable();
			} else {
				self.tableParams.reload();
			}
		};

		/**
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		self.getResultMessage = function(dataLength, currentPage){
			return "Result " + (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + " of " + self.totalRecords;
		};

		/**
		 * sets the current attribute id selected
		 * @param attribute The currently selected attribute
		 */
		self.selectAttribute = function(attributeId){
			self.selectedAttributeId = angular.copy(attributeId);
			self.isCreateNewAttributeSelected = true;
		}

		/**
		 * resets isCreateNewAttributeSelected and selectedAttributeId to default values
		 */
		self.returnFromDirective = function(){
			self.isCreateNewAttributeSelected = false;
			self.selectedAttributeId = null;
		}
	}
})();

