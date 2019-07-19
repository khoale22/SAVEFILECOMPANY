/*
 *
 * eCommerceAttributeController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author s753601
 * @since 2.5.0
 */
'use strict';

/**
 * The controller for the eCommerce Attribute Controller.
 */
(function() {

	angular.module('productMaintenanceUiApp').controller('eCommerceAttributeController', eCommerceAttributeController).directive('fileBrowser', function() {
		return {

			restrict: 'A',
			replace: true,
			transclude: true,
			scope: false,
			template: '<div class="input-prepend extended-date-picker col-md-10">' +
			'<input type="text" readonly class="col-md-10" style="border-radius: 0;height: 28px;font-size: 12px;">' +
			'<button title="Browse" type="button" class="btn btn-primary" style="border-radius: 0;border-top-right-radius: 5px;border-bottom-right-radius: 5px;">' +
			'<span class="glyphicon glyphicon-folder-open"></span><span class="image-preview-input-title"> Browse</span></button>' +
			'<div class="proxied-field-wrap" ng-transclude style="display:block;position:absolute;height:0;width:0;overflow:hidden;"></div>' +
			'</div>',
			link: function ($scope, $element, $attrs, $controller) {
				var button, fileField, proxy;
				fileField = $element.find('[type="file"]').on('change', function () {
					proxy.val(angular.element(this).val().replace(/^.*[\\\/]/, ''));
				});
				proxy = $element.find('[type="text"]').on('click', function () {
					fileField.trigger('click');
				});
				button = $element.find('[type="button"]').on('click', function () {
					fileField.trigger('click');
				});
			}
		}

	});

	eCommerceAttributeController.$inject = ['$sce','urlBase','$scope','$http','BatchUploadApi', 'ngTableParams', '$log', 'BatchUploadResultsFactory'];

	/**
	 * Constructs the controller.
	 */
	function eCommerceAttributeController($sce,urlBase,$scope,$http,batchUploadApi, ngTableParams, $log, batchUploadResultsFactory) {

		var self = this;
		self.PAGE_SIZE = 100;
		self.WAIT_TIME = 120;
		self.downloading = false;
		$scope.actionUrl =urlBase+'/pm/eCommerceAttribute/downloadTemplate';
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
		self.disableUpload=true;
		/**
		 * Initiates the construction of the eCommerce Attribute batch upload
		 */
		self.init = function () {

		};
		self.checkShowModal = function(){
			var file = document.getElementById("ecommerce-attr");
			self.disableUpload=!file.files.length>0;
		}

		/**
		* batch upload eCommerce Attribute
		 **/
		self.uploadeCommerceAttributeCall = function(){
			self.success=undefined;
			self.error=undefined;
			self.isWaiting=true;
			var file = document.getElementById("ecommerce-attr").files[0];
			var name = document.getElementById("nameRequest").value;
			var fd = new FormData();
			fd.append('file', file);
			fd.append('name', name);
			batchUploadApi.uploadeCommerceAttribute(fd,function (resp) {
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
