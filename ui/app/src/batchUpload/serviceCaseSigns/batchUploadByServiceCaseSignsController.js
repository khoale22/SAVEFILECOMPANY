/*
 * excelUploadByServiceCaseSignsController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * Controller to support the page that allows users to download or upload excel template for Service Case Signs.
 *
 * @author vn70529
 * @since 2.12
 */
(function () {
	angular.module('productMaintenanceUiApp').controller('BatchUploadByServiceCaseSignsController', batchUploadByServiceCaseSignsController);
	batchUploadByServiceCaseSignsController.$inject = ['$scope', 'urlBase', 'BatchUploadResultsFactory',  'BatchUploadApi'];

	/**
	 * Constructs for excel Upload By Service Case Signs controller.
	 *
	 * @param $scope
	 * @param urlBase  The base URL to use to contact the backend.
	 * @param $uibModal  uibmodal service to show modal.
	 * @param batchUploadResultsFactory the modal factory.
	 * @param batchUploadApi the service of nutrition ingredients.
	 */
	function batchUploadByServiceCaseSignsController($scope, urlBase, batchUploadResultsFactory, batchUploadApi) {

		var self = this;

		/**
		 * Holds download template url.
		 *
		 * @type {string}
		 */
		self.downloadTemplateURL = urlBase + '/pm/serviceCaseSigns/downloadTemplate/';

		/**
		 * The list of allowed upload file types. It separates by comma.
		 *
		 * @type {string}
		 */
		self.allowedUploadFileTypes = ".xls, .xlsx";

		/**
		 * Holds the file needs to upload.
		 */
		self.fileUpload;

		/**
		 * Call api to upload excel file.
		 *
		 * @param event event click.
		 * @param data the data to holds the description field.
		 */
		self.doUpload = function (event, data) {
			var formData = new FormData();
			formData.append('description', data.description);
			formData.append('file', self.fileUpload);
			if (!$scope.isWaitingForResponse) {
				$scope.isWaitingForResponse = true;
				batchUploadApi.uploadTemplateForServiceCaseSigns(
					formData,
					self.doUploadSuccessCallback,
					self.doUploadSuccessError);
			}
		}
		/**
		 * Callback for upload is success.
		 *
		 * @param response
		 */
		self.doUploadSuccessCallback = function (response) {
			$scope.isWaitingForResponse = false;
			$("#fileUpload").val(null);
			self.fileUpload = null;
			$("#excelFileName").val(null);
			batchUploadResultsFactory.showResultsModal(response.transactionId);
		}
		/**
		 * Callback for upload is error.
		 * @param response
		 */
		self.doUploadSuccessError = function (response) {
			$scope.isWaitingForResponse = false;
			if(response.data){
				self.error = response.data.message;
			}
			$("#fileUpload").val(null);
			self.fileUpload = null;
			$("#excelFileName").val(null);
		}
		/**
		 * Listen ok button event on confirmation upload file.
		 * If the user agree with uploading template, then they will click on ok button to upload.
		 */
		$scope.$on("okButtonClicked", self.doUpload);
	}
})();
