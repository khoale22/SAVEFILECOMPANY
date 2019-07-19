/*
 *
 * notificationModalController.js
 *
 * Copyright (c) 2017 MagRabbit
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of MagRabbit.
 *
 */

'use strict';

/**
 * Controller to support the notification modal to show message.
 *
 * @author vn70529
 * @since 2.12
 */
(function() {

	angular.module('productMaintenanceUiApp').controller('NotificationModalController',
		notificationModalController);

	notificationModalController.$inject = [ '$uibModalInstance', 'title', 'message','isShowConfirmDialog'];
	/**
	 * /**
	 * Constructs for notificationModalController controller.
	 *
	 * @param $uibModalInstance
	 * @param title the title of modal.
	 * @param message the message to show on modal.
	 */
	function notificationModalController($uibModalInstance, title, message, isShowConfirmDialog) {


		var self = this;

		/**
		 * Variables
		 */
		self.title = title;
		self.message = message;
		self.isShowConfirmDialog = isShowConfirmDialog;

		/**
		 * Yes function
		 */
		self.yes = function() {
			$uibModalInstance.close();
		};

		/**
		 * Close function
		 */
		self.close = function() {
			$uibModalInstance.dismiss('cancel');
		};
	}
})();
