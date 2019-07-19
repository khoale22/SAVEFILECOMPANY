/*
 *
 * discontinueParametersService
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * Manages communication between SinglePluPanel, the PluDirective and the Maintenance UPC component
 *
 * @author s753601
 * @since 2.8.0
 */
(function(){
	angular.module('productMaintenanceUiApp').service('singlePluPanelService', singlePluPanelService);

	singlePluPanelService.$inject = ['$rootScope'];

	/**
	 * Constructs the singlePluPanelService.
	 *
	 * @param $rootScope The Angular scope representing this application.
	 * @returns
	 */
	function singlePluPanelService($rootScope){
		var self = this;

		/**
		 * The currently selected scale upc the user want to look at the plu for
		 * @type {null}
		 */
		self.selectedScaleUpc = null;
		/**
		 * If a list of PLU is available the index of the currently selected scaleUpc
		 * @type {number}
		 */
		self.selectedScaleUpcIndex=0;
		/**
		 * The list of PLU the user wishes to view
		 * @type {Array}
		 */
		self.data= [];
		/**
		 * A flag to determine if the user is looking at a single plu or not.
		 * @type {null}
		 */
		self.currentView=null;

		/**
		 * Getters and setters for all of the above variables that need to be transmitted.
		 */
		return{
			/**
			 * Updates the selectedScaleUpc
			 * @param selectedScaleUpc
			 */
			setSelectedScaleUpc: function (selectedScaleUpc) {
				self.selectedScaleUpc=selectedScaleUpc;
			},
			/**
			 * Returns the current selectedScaleUpc
			 * @returns the current selectedScaleUpc
			 */
			getSelectedScaleUpc: function () {
				return self.selectedScaleUpc;
			},
			/**
			 * Updates the data
			 * @param data
			 */
			setData: function (data) {
				self.data=data;
			},
			/**
			 * Returns the current data
			 * @returns the current data
			 */
			getData: function () {
				return self.data;
			},
			/**
			 * Updates the currentView
			 * @param currentView
			 */
			setCurrentView: function (currentView) {
				self.currentView=currentView;
			},
			/**
			 * Returns the current currentView
			 * @returns the current currentView
			 */
			getCurrentView: function () {
				return self.currentView;
			},
			/**
			 * Updates the selectedScaleUpcIndex
			 * @param selectedScaleUpcIndex
			 */
			setSelectedScaleUpcIndex: function (selectedScaleUpcIndex) {
				self.selectedScaleUpcIndex=selectedScaleUpcIndex;
			},
			/**
			 * Returns the current selectedScaleUpcIndex
			 * @returns the current selectedScaleUpcIndex
			 */
			getSelectedScaleUpcIndex: function () {
				return self.selectedScaleUpcIndex;
			}
		}
	}
})();
