/*
 * checkDigitCalculatorApi.js
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for check digit calculator.
 *
 * @author vn75469
 * @since 2.16.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('checkDigitCalculatorApi', checkDigitCalculatorApi);
	checkDigitCalculatorApi.$inject = ['urlBase', '$resource'];
	function checkDigitCalculatorApi(urlBase, $resource) {
		return $resource(urlBase + '/pm/utilities/checkDigitCalculator/:upcCode');
	}
})();
