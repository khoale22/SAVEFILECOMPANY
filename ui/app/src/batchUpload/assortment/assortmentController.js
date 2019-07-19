/*
 *
 * eCommerceAttributeController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn55306
 * @since 2.12.0
 */
'use strict';

/**
 * The controller for the eCommerce Attribute Controller.
 */
(function() {

	var app=angular.module('productMaintenanceUiApp');
	app.controller('assortmentController', assortmentController);

	assortmentController.$inject = ['$sce','urlBase','$scope','$http','BatchUploadResultsFactory','BatchUploadApi', 'ngTableParams', '$log'];
	/**
	 * Constructs the controller.
	 * @param excelUploadResultsFactory the modal factory.
	 */
	function assortmentController($sce,urlBase,$scope,$http,batchUploadResultsFactory,batchUploadApi, ngTableParams, $log) {

		var self = this;
		self.PAGE_SIZE = 100;
		self.WAIT_TIME = 120;
		self.downloading = false;
		$scope.actionUrl =urlBase+'/pm/assortment/downloadTemplate';
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Attributes for each row in the table
		 * @type {Array}
		 */
		self.attributes = [];

		/**
		 * Used to keep track of the number of columns in the table
		 * @type {Array}
		 */
		self.columns = [];
		/**
		 * The maximum number of priorities an attribute has.
		 * @type {number}
		 */
		self.maxNumberofPriorities = 0;
		/**
		 * Used to keep track of the attribute name
		 * @type {Array}
		 */
		self.attributeNames = [];

		self.success;
		self.error;
		/**
		 * Initiates the construction of the eCommerce Attribute batch upload
		 */
		self.init = function () {

		};
		self.disableUpload=true;
		self.checkShowModal = function(){
			var file = document.getElementById("ecommerce-attr");
			self.disableUpload=!file.files.length>0;
		}
		/**
		* batch upload assortment
		 **/
		self.uploadeAssortmentCall = function(){
			self.success=undefined;
			self.error=undefined;
			self.isWaiting=true;
			var file = document.getElementById("ecommerce-attr").files[0];
			var name = document.getElementById("nameRequest").value;
			var fd = new FormData();
			fd.append('file', file);
			fd.append('name', name);
			batchUploadApi.uploadAssortment(fd,function (resp) {
				document.getElementById('form-upload').reset();
				var ele=document.getElementById("nameRequest");
				if(ele!=null){
					ele.value='';
				}
				self.isWaiting=false;
				self.error=undefined;
				batchUploadResultsFactory.showResultsModal(resp.transactionId);
			},function(err){
				document.getElementById('form-upload').reset();
				var ele=document.getElementById("nameRequest");
				if(ele!=null){
					ele.value='';
				}
				self.isWaiting=false;
				self.success=undefined;
				self.error=err.data.message;
			});
		}
	}


})();
