/*
 * homeSharedService.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * A shared service for different controllers of the home screen. Used to list commonly used functions and also for
 * communication between different controllers.
 *
 * Avoid using this service for  communication between different components inside the Product Details section.
 * Communication between different components of product details should continue to follow standard "bindings" way of
 * communication.
 *
 * @author vn40486
 * @since 2.13.0
 */
(function() {
	angular.module('productMaintenanceUiApp').factory('HomeSharedService', homeSharedService);
	homeSharedService.inject = ['$rootScope'];

	function homeSharedService($rootScope) {
		var sharedService = {};

		sharedService.prepForNewSearchBroadcast = function() {
			this.broadcastNewSearch();
		};

		sharedService.broadcastNewSearch = function() {
			$rootScope.$broadcast('handleNewSearch');
		};

		sharedService.broadcastUpdateProduct = function(){
			$rootScope.$broadcast('updateProduct');
		};

		sharedService.broadcastClearMessage = function(){
			$rootScope.$broadcast('clearMessage');
		};

		sharedService.broadcastNavigateTabAndUpdateProduct = function(tab){
			$rootScope.$broadcast('navigateTabAndUpdateProduct', tab);
		};

		sharedService.returnToProductList = function () {
			$rootScope.$broadcast('returnToProductList');
		};

		/**
		 * Used to broadcast a message to display the eCommerce task summary screen.
		 */
		sharedService.broadcastLoadEcommerceTaskSummary = function(){
			$rootScope.$broadcast('loadEcommerceTaskSummary');
		};

		return sharedService;
	}
})();
