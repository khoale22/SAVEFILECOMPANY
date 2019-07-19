/*
 *   commonService.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */

'use strict';

/**
 * A generic service that provides common utility operations that can be re-used across the modules, controllers and
 * components.
 *
 * @author vn40486
 * @since 2.3.0
 */
(function() {
	angular.module('productMaintenanceUiApp').service('PMCommons', function () {

		return {
			/**
			 * Handles show or hide of the loading spinner.
			 * @param isLoading true or false
			 */
			displayLoading: function(ctrl, isLoadingData) {
				ctrl.isLoading = isLoadingData;
			}
		}
	});
})();
