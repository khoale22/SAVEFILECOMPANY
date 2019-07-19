/*
 * excelUploadResultsController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * The controller to handle the layout for the batch upload Results.
 *
 * @author vn70529
 * @since 2.12
 */
(function() {

	angular.module('productMaintenanceUiApp')
		.controller('BatchUploadResultsController', batchUploadResultsController)
		.factory('BatchUploadResultsFactory', batchUploadResultsFactory);

	/**
	 * Constructs the controller.
	 *
	 * @param $scope the score of page.
	 * @param $uibModalInstance  the Modal Instance service.
	 * @param trackingId tracking id.
	 */
	function batchUploadResultsController($scope, $uibModalInstance, trackingId, $state, appConstants) {

		var self = this;

		$scope.trackingId = trackingId;

		/**
		 * Upload template to server.
		 */
		self.checkStatus= function(){


			/**
			 * Hide confirm dialog.
			 */
			 
			$state.go(appConstants.CHECK_STATUS,{trackingId:trackingId});
			self.close();
		}

		/**
		 * Close this dialog
		 */
		self.close = function() {
			$uibModalInstance.dismiss('cancel');
		};
	}

	/**
	 * Constructs the factory for show results modal.
	 *
	 * @param $uibModal the ui modal
	 * @returns {excelUploadResultsFactory} excelUploadResultsFactory object.
	 */
	function batchUploadResultsFactory($uibModal){

		var self = this;

		/**
		 * Show results modal.
		 *
		 * @param trackingId tracking id.
		 */
		self.showResultsModal = function(trackingId) {
			return $uibModal.open({
				templateUrl: 'src/batchUpload/utils/batchUploadResults.html',
				backdrop: 'static',
				windowClass: 'modal',
				controller: 'BatchUploadResultsController',
				controllerAs: 'batchUploadResultsController',
				size: 'md',
				resolve: {
					trackingId: function () {
						return trackingId;
					}
				}
			});
		};

		return self;
	}

})();
