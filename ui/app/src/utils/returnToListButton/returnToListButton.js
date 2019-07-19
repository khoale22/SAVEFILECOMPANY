/*
 *   returnToListButtonComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Component of Re-usable button to return to an outer list.
 *
 * @author m314029
 * @since 2.14.0
 */
(function () {

	angular.module('productMaintenanceUiApp').directive('returnToListButton', returnToListButton);
	returnToListButton.$inject = ['HomeSharedService'];
	function returnToListButton(homeSharedService) {
		return {
			scope: {
				broadCastMessage: '<'
			},
			replace: true,
			restrict: 'E',
			// Inline template which is bound to message variable in the component controller
			templateUrl: 'src/utils/returnToListButton/returnToListButton.html',
			controllerAs : 'returnToListDirective',
			// The controller that handles our component logic
			controller: function ($scope){
				var self = this;

				self.RETURN_TO_PRODUCT_LIST = "returnToProductList";

				self.returnToList = function () {
					if($scope.broadCastMessage === self.RETURN_TO_PRODUCT_LIST) {
						homeSharedService.returnToProductList();
					}
				}
			}
		}
	}
})();
