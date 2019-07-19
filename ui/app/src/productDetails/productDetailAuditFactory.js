/*
 * productDetailAuditFactory.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
/**
 * Factory to support the product Detail Audit.
 *
 * @author l730832
 * @since 2.6.0
 */
(function () {
	'use strict';

	var app = angular.module('productMaintenanceUiApp');
	app.factory('ProductDetailAuditModal', ['$uibModal', ProductDetailAuditModal]);


	function ProductDetailAuditModal($uibModal) {
		return {
			open: open
		};

		function open(callback, parameters, title) {
			return $uibModal.open({
				templateUrl: 'src/productDetails/productDetailAudit.html',
				controller: 'ProductDetailAuditModalController',
				controllerAs: 'ProductDetailAuditModalController',
				resolve: {
					callback: function () {
						return callback;
					},
					parameters: function () {
						return parameters;
					},
					title: function () {
						return title;
					}
				}
			});
		}
	}
})();
