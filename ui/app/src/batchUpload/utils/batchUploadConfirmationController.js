/*
 * excelUploadConfirmationController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * The controller to handle the layout of the excel Upload Confirmation.
 *
 * @author vn70529
 * @since 2.12
 */

(function() {

	angular.module('productMaintenanceUiApp')
		.controller('BatchUploadConfirmationController', batchUploadConfirmationController)
		.directive('batchUploadConfirmation', batchUploadConfirmation);

	batchUploadConfirmation.$inject = ['$uibModal', 'ModalFactory'];

	/**
	 * Batch upload confirmation directive for batch upload.
	 *
	 * @param $uibModal
	 * @param ModalFactory modal factory is used to show notification.
	 * @returns {{restrict: string, priority: number, link: link}}
	 */
	function batchUploadConfirmation($uibModal, modalFactory) {
		return {
			restrict: 'A',
			priority: -1,
			link: function (scope, element, attrs) {
				element.bind('click', function () {
					var isEmptyUploadFile = attrs.isEmptyUploadFile;

					if (isEmptyUploadFile == 'true') {
						modalFactory.showNotificationModal({title: 'Upload', message: 'Please select file to upload.'});
					} else {
						$uibModal.open({
							templateUrl: 'src/batchUpload/utils/batchUploadConfirmation.html',
							backdrop: 'static',
							windowClass: 'modal',
							controller: 'BatchUploadConfirmationController',
							controllerAs: 'batchUploadConfirmationController',
							size: 'md',
							resolve: {}
						});
					}
				});
			}
		};
	}

	batchUploadConfirmationController.$inject = ['$rootScope', '$uibModalInstance'];

	/**
	 * Constructs the controller.
	 *
	 * @param $rootScope the root Scope
	 * @param $uibModalInstance the Modal Instance.
	 */
	function batchUploadConfirmationController($rootScope, $uibModalInstance) {

		var self = this;

		/**
		 * Holds the description of upload file.
		 *
		 * @type {string}
		 */
		self.description = '';

		/**
		 * Upload template to server.
		 */
		self.ok = function(){

			/**
			 * Notify to controller that user click on ok button to agree upload file.
			 */
			$rootScope.$broadcast("okButtonClicked", {
				description: self.description
			});

			/**
			 * Hide confirm dialog.
			 */
			self.close();
		}

		/**
		 * Close this dialog
		 */
		self.close = function() {
			$uibModalInstance.dismiss('cancel');
		};
	}
})();
