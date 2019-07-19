/*
 *
 * modalFactory.js
 *
 * Copyright (c) 2017 MagRabbit
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of MagRabbit.
 */

'use strict';

/**
 * The modal factory is used to generate dialog as notification modal, ...
 *
 * @author vn70529
 ~ @since 2.12
 */
(function() {
	angular.module('productMaintenanceUiApp').factory('ModalFactory', modalFactory);

	modalFactory.$inject = ['$uibModal'];

	/**
	 * Constructs for modalFactory.
	 *
	 * @param $uibModal uid modal service.
	 * @returns {modalFactory}
	 */
	function modalFactory($uibModal) {

		var self = this;

		/**
		 * Show notification modal.
		 *
		 * @param config holds the title and message to show on modal.
		 */
		self.showNotificationModal = function(config) {
			return $uibModal.open({
				templateUrl: 'src/utils/modals/notificationModal.html',
				backdrop: 'static',
				windowClass: 'modal',
				controller: 'NotificationModalController',
				controllerAs: 'notificationModalController',
				size: 'sm',
				resolve: {
					title: function () {
						return config.title;
					},
					message: function () {
						return config.message;
					},
					isShowConfirmDialog: function(){
						return false;
					}
				}
			});
		};

		self.showConfirmModal = function(config) {
			return $uibModal.open({
				templateUrl: 'src/utils/modals/notificationModal.html',
				backdrop: 'static',
				windowClass: 'modal',
				controller: 'NotificationModalController',
				controllerAs: 'notificationModalController',
				size: 'sm',
				resolve: {
					title: function () {
						return config.title;
					},
					message: function () {
						return config.message;
					},
					isShowConfirmDialog: function(){
						return true;
					}
				}
			}).result;
		};

		return self;
	}
})();
