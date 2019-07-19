/*
 * earleyUploadController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

(function () {
	angular.module('productMaintenanceUiApp').controller('EarleyUploadController', earleyUploadController);
	earleyUploadController.$inject = ['$scope', 'BatchUploadResultsFactory',  'BatchUploadApi'];


	function earleyUploadController($scope, batchUploadResultsFactory, batchUploadApi) {

		var self = this;

		self.HIERARCHY = "HIERARCHY";
		self.PRODUCT = "PRODUCT";
		self.ATTRIBUTE = "ATTRIBUTE";
		self.ATTRIBUTE_VALUE = "ATTRIBUTE_VALUE";
		self.PRODUCT_ATTRIBUTE_FILE = "PRODUCT_ATTRIBUTE_FILE";

		/**
		 * Any error message returned from the backend.
		 *
		 * @type {string}
		 */
		self.error = null;

		/**
		 * The list of allowed upload file types. It separates by comma.
		 *
		 * @type {string}
		 */
		self.allowedUploadFileTypes = ".csv";

		/**
		 * Holds the file needs to upload.
		 */
		self.fileUpload = null;

		self.fileType = self.PRODUCT;

		/**
		 * Call api to upload the file
		 *
		 * @param event event click.
		 * @param data the data to holds the description field.
		 */
		self.doUpload = function (event, data) {
			self.error = null;
			var formData = new FormData();
			formData.append('description', data.description);
			formData.append('file', self.fileUpload);
			if (!$scope.isWaitingForResponse) {
				$scope.isWaitingForResponse = true;

				switch (self.fileType) {
					case self.HIERARCHY:
						batchUploadApi.uploadEarleyHierarchyFile(
							formData,
							self.doUploadSuccess,
							self.doUploadError);
						break;
					case self.PRODUCT:
						batchUploadApi.uploadEarleyProductFile(
							formData,
							self.doUploadSuccess,
							self.doUploadError);
						break;
					case self.ATTRIBUTE:
						batchUploadApi.uploadEarleyAttributeFile(
							formData,
							self.doUploadSuccess,
							self.doUploadError);
						break;
					case self.ATTRIBUTE_VALUE:
						batchUploadApi.uploadEarleyAttributeValuesFile (
							formData,
							self.doUploadSuccess,
							self.doUploadError);
						break;
					case self.PRODUCT_ATTRIBUTE_FILE:
						batchUploadApi.uploadEarleyProductAttributeFile (
							formData,
							self.doUploadSuccess,
							self.doUploadError);
				}
			}
		};

		/**
		 * Callback for upload is success.
		 *
		 * @param response
		 */
		self.doUploadSuccess = function (response) {
			$scope.isWaitingForResponse = false;
			$("#fileUpload").val(null);
			self.fileUpload = null;
			$("#excelFileName").val(null);
			batchUploadResultsFactory.showResultsModal(response.transactionId);
		};

		/**
		 * Callback for upload is error.
		 * @param response
		 */
		self.doUploadError = function (response) {
			$scope.isWaitingForResponse = false;
			if(response.data){
				self.error = response.data.message;
			} else if (response.statusText && response.statusText != "") {
				self.error = response.statusText;
			} else {
				self.error = "Unknown error";
			}
			$("#fileUpload").val(null);
			self.fileUpload = null;
			$("#excelFileName").val(null);
		};

		/**
		 * Listen ok button event on confirmation upload file.
		 * If the user agree with uploading template, then they will click on ok button to upload.
		 */
		$scope.$on("okButtonClicked", self.doUpload);
	}
})();
