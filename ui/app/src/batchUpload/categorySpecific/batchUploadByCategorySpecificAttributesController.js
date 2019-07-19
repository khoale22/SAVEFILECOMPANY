/*
 * batchUploadByCategorySpecificAttributesController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * Controller to support the page that allows users to download or upload excel template.
 *
 * @author vn70529
 * @since 2.12
 */
(function () {
	angular.module('productMaintenanceUiApp').controller('BatchUploadByCategorySpecificAttributesController', batchUploadByCategorySpecificAttributesController);

	batchUploadByCategorySpecificAttributesController.$inject = ['$scope', 'urlBase', '$uibModal', 'BatchUploadResultsFactory', 'BatchUploadApi'];

	/**
	 * Constructs for batchUploadByCategorySpecificAttributes controller.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $uibModal uibmodal service to show modal.
	 */
	function batchUploadByCategorySpecificAttributesController($scope, urlBase, $uibModal, batchUploadResultsFactory,batchUploadApi) {

		var self = this;

		/**
		 * Holds the id of wine template.
		 *
		 * @type {number}
		 */
		self.WINE_TEMPLATE_ID = 5;

		/**
		 * Holds download template url.
		 *
		 * @type {string}
		 */
		self.generateTemplateURL = urlBase + '/pm/categorySpecific/generateTemplate/';

		/**
		 * The list of allowed upload file types. It separates by comma.
		 *
		 * @type {string}
		 */
		self.allowedUploadFileTypes = ".xls, .xlsx";

		/**
		 * Default option for default selected excel type.
		 *
		 * @type {{id: string, description: string}}
		 */
		self.optionDefault = {id: "", description: "-- Select --"};

		/**
		 * Initial the detault selected excel type.
		 *
		 * @type {{id: string, description: string}}
		 */
		self.selectedExcelType = self.optionDefault;

		/**
		 * The list of excel types.
		 *
		 * @type {Array}
		 */
		self.excelTypeOptions = [];

		/**
		 * Holds the file needs to upload.
		 */
		self.fileUpload;

		/**
		 * Initialize the controller.
		 */
		self.init = function () {
			self.loadExcelTypeOptions();
		}

		/**
		 * Load the list of excel type options from server.
		 */
		self.loadExcelTypeOptions = function () {

			self.excelTypeOptions[0] = {id: "1", description: "Apparel"};
			self.excelTypeOptions[1] = {id: "2", description: "Kitchenware"};
			self.excelTypeOptions[2] = {id: "3", description: "Large and Small Appliances"};
			self.excelTypeOptions[3] = {id: "4", description: "Softlines"};
			self.excelTypeOptions[4] = {id: "5", description: "Wine"};
		}

		/**
		 * Set selected excel type by default value if selected excel type is null.
		 *
		 * @param excelType selected excel type.
		 */
		self.excelTypeChanged = function (excelType) {

			if (excelType == null) {
				self.selectedExcelType = self.optionDefault
			}
		}

		/**
		 * Check the enable or disable status for the button on form.
		 * If selected excel type is null or default value then return true for disable status.
		 *
		 * @returns {boolean} if it is true then disabled status or enabled status.
		 */
		self.isDisabledButton = function () {
			return self.selectedExcelType == null || self.selectedExcelType.id == '' || $scope.isWaitingForResponse == true;
		}

		/**
		 * This action is used to generate template that selected from excel type option.
		 */
		self.generateTemplateAction = function () {
			return self.generateTemplateURL + self.selectedExcelType.id;
		}

		/**
		 * Call service to upload excel file.
		 */
		self.doUpload = function (event, data) {

			/**
			 * Prepare data form for upload.
			 */
			if(!$scope.isWaitingForResponse) {
				$scope.isWaitingForResponse = true;
				var formData = new FormData();
				formData.append('name', data.description);
				formData.append('file', self.fileUpload);
				formData.append('fileType',self.selectedExcelType.id);
				/**
				 * Execute uploading.
				 */
				return batchUploadApi.uploadCategorySpecific(formData, self.doUploadSuccessCallback, self.doUploadSuccessError);
			}
		}

		/**
		 * Callback for upload is success.
		 *
		 * @param response
		 */
		self.doUploadSuccessCallback = function (response) {

			self.selectedExcelType = self.optionDefault
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
