/*
 * taskSummaryController.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to handle lifecycle of the task summary partial page and handle its associated functions.
 *
 * @author vn40486
 * @since 2.11.0
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('TaskSummaryController', taskSummaryController);
	taskSummaryController.$inject = ['$scope', '$location','ProductUpdatesTaskApi', 'NutritionUpdatesTaskApi',
		'EcommerceTaskApi', 'ProductInfoService', 'HomeSharedService'];

	/**
	 * TaskSummaryController definition.
	 * @param nutritionUpdatesTaskApi api used for data server communication.
	 */
	function taskSummaryController($scope, $location, productUpdatesTaskApi, nutritionUpdatesTaskApi, ecommerceTaskApi,
								   productInfoService, homeSharedService) {
		var self = this;
		self.showTaskPanel = true;

		//Initialize count variables.
		self.nutritionUpdatesCount;

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			self.getActiveEcommerceTaskCount();
			self.getActiveNutritionUpdatesCount();
			self.getActiveProductUpdatesCount();
		};

		/**
		 * Fetches count of product updates that are open.
		 */
		self.getActiveProductUpdatesCount = function() {
			self.loadingProductUpdatesCount = true;
			productUpdatesTaskApi.getActiveProductUpdatesCount(
				{},
				//success
				function(results) {
					self.loadingProductUpdatesCount = false;
					self.productUpdatesCount = results.data;
				},
				//error
				function(errors) {
					self.loadingProductUpdatesCount = false;
					self.productUpdatesCount = 'NA';
					console.log('Error during getActiveProductUpdatesCount '+errors);
				}
			);
		};

		/**
		 * Fetches count of nutrition updates that are open.
		 */
		self.getActiveNutritionUpdatesCount = function() {
			self.loadingNutritionUpdatesCount = true;
			nutritionUpdatesTaskApi.getActiveNutritionUpdatesCount(
				{},
				//success
				function(results) {
					self.loadingNutritionUpdatesCount = false;
					self.nutritionUpdatesCount = results.data;
				},
				//error
				function(errors) {
					self.loadingNutritionUpdatesCount = false;
					self.nutritionUpdatesCount = 'NA';
					console.log('Error during getActiveNutritionUpdatesCount '+errors);
				}
			);
		};

		/**
		 * Fetches count of ecommerce task that are open.
		 */
		self.getActiveEcommerceTaskCount = function() {
			self.loadingEcomTaskCount = true;
			ecommerceTaskApi.getActiveEcommerceTaskCount(
				{},
				//success
				function(results) {
					self.loadingEcomTaskCount = false;
					self.ecommerceTaskCount = results.data;
				},
				//error
				function(errors) {
					self.loadingEcomTaskCount = false;
					self.ecommerceTaskCount = 'NA';
					console.log('Error during getActiveEcommeTaskCount '+errors);
				}
			);
		};

		/**
		/**
		 * Handles navigation to a specified path. Much like an href.
		 * @param path
		 */
		$scope.go = function ( path ) {
			$location.path( path );
		};

		/**
		 * Handles firing a load ecommerce summary event. Uses angularjs's broadcast feature for the Inter
		 * component/controller communication.
		 */
		self.displayEcommerceTaskSummary = function() {
			homeSharedService.broadcastLoadEcommerceTaskSummary();
		};

		/**
		 * Used to show/hide the task panel. Mostly minimize/maximize.
		 */
		self.togglePanelView = function() {
			self.showTaskPanel = !self.showTaskPanel;
		};

		/**
		 * Used to handle minimize and maximize of task summary panel.
		 */
		self.toggleView = function() {
			$('#taskBody').collapse('toggle');
			self.showTaskPanel = !self.showTaskPanel;
		};

		/**
		 * Used to hide the panel body section.
		 */
		self.hideView = function() {
			$('#taskBody').collapse('hide');
			self.showTaskPanel = false;
		};

		/**
		 * Receives new search event broadcast from search controller.
		 */
		$scope.$on('handleNewSearch', function() {
			self.hideView();
		});
	}

})();
