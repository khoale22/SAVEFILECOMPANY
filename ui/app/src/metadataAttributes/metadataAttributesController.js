/*
 * metadataAttributesController.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

(function() {

	angular.module('productMaintenanceUiApp').controller('MetadataAttributesController', metadataAttributesController);

	metadataAttributesController.$inject = ['AttributeMetaDataApi', 'ngTableParams'];

	function metadataAttributesController(attributeMetaDataApi, ngTableParams) {

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
		 * The current selected attribute
		 * @type {null}
		 */
		self.selectedAttribute = null;
		/**
		 * Whether New Attribute button is pressed
		 * @type {null}
		 */
		self.isAttributeSelected = false;
		/**
		 * Initiates the construction of the source priority table
		 */
		self.init = function () {
			self.isWaiting = true;
			self.touchedInit = true;
			self.newSearch(true);
		};

		/**
		 * Initiates a new search.
		 *
		 * @param findAll
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
		 * Builds the attribute maintenance table
		 */
		self.buildTable = function () {
			self.metadataAttributeTableParams = new ngTableParams(
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
						var searchNameText = params.filter()["name"];
						if (typeof searchNameText === "undefined") {
							searchNameText = "";
						}
						self.fetchData(includeCounts, params.page() - 1, searchNameText);
					}
				}
			);
		};

		/**
		 * Initiates a call to get the list of attribute maintenance records.
		 *
		 * @param includeCounts Whether or not to include getting record counts.
		 * @param page The page of data to ask for.
		 */
		self.fetchData = function(includeCounts, page, searchNameText) {
			attributeMetaDataApi.findAll({name: searchNameText,
				firstSearch: includeCounts, page: page, pageSize: self.PAGE_SIZE
			}, self.loadData, self.fetchError);
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
		 * If there is an error this will display the error
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message != null && error.data.message !== "") {
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
		 * sets the current attribute selected
		 * @param attribute The currently selected attribute
		 */
		self.selectAttribute = function(attribute){
			self.selectedAttribute = angular.copy(attribute);
			self.isAttributeSelected = true;
		};

		/**
		 * resets isAttributeSelected and selectedAttribute to default values
		 */
		self.returnFromDirective = function(){
			self.isAttributeSelected = false;
			self.selectedAttribute = null;
		};

	}

})();
