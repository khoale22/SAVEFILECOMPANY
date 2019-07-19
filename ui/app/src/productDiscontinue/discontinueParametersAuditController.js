/*
 *
 *  discontinueParametersAuditController.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

'use strict';

/**
 * Controller to support the page that allows users to view and modify discontinue parameters Audits.
 * @author s573181
 * @since 2.0.3
 */

(function() {
	angular.module('productMaintenanceUiApp').controller('DiscontinueParametersAuditController', discontinueParametersAuditController);

	discontinueParametersAuditController.$inject = ['DiscontinueParametersAuditService', 'DiscontinueParametersFactory', '$rootScope','$scope'];

	/**
	 * Constructs the discontinueParametersAuditController.
	 *
	 * @param discontinueParametersAuditService the service to communicate between this contoller and
	 * DiscontinueExceptionParametersController
	 *
	 * @param $scope The Angular scope representing this application.
	 */
	function discontinueParametersAuditController(discontinueParametersAuditService, discontinueParametersFactory, $rootScope, $scope){

		var self = this;
		/**
		 *  Contains the audit records to be displayed.
		 *
		 * @type array of DiscontinueParametersAuditRecords.
		 */
		self.audits = null;
		/**
		 * Contains the header title of the audit.
		 *
		 * @type String value of the audit's header title.
		 */
		var auditTitle = null;

		/**
		 * Initializes the controller.
		 */
		self.init = function(){
			self.data = null;
			self.auditTitle = null;
			self.data = discontinueParametersAuditService.getAuditParameters();
			if(self.isViewingException || self.isViewingDeletedException){
				self.auditTitle = "Discontinue Rule Exception Audit";
			} else {
				self.auditTitle = "Discontinue Exception Audit";
			}
		};

		/**
		 * Sets the data variable to the results of the function call.
		 *
		 * @param results  -the results of the function call.
		 */
		self.setAudits = function (results){
			self.audits = results;
		};

		/**
		 * Initiates a call to to discontinueExceptionParametersController to tell it to close the Audit parameters.
		 */
		self.close = function(){
			$rootScope.$broadcast('closeAuditParameters');
		};

		/**
		 * Callback for when there is an error returned from the server.
		 *
		 * @param error The error from the server.
		 */
		self.fetchError = function(error){
			self.success = null;
			self.error = error.data.message;
		};

		/**
		 * Will fetch a list of  DiscontinueParametersAuditRecords that contain audits for the selected Discontinue
		 * exception.
		 */
		self.getDiscontinueExceptionParameterAudit = function(){
			self.isException(true);
			self.init();
			discontinueParametersFactory.getDiscontinueExceptionsParameterAudit({exceptionType: self.data.exceptionType,
				exceptionTypeId: self.data.exceptionTypeId}, self.setAudits, self.fetchError);
		};

		/**
		 * Will fetch a list of  DiscontinueParametersAuditRecords that contain audits for the Discontinue parameters.
		 */
		self.getDiscontinueParameterAudit = function(){
			self.isException(false);
			self.init();
			discontinueParametersFactory.getDiscontinueParameterAudit(self.setAudits, self.fetchError);
		};

		/**
		 * Sets the boolean value to whether or not a user is viewing the an exception audit page.
		 *
		 * @param isViewingException
		 */
		self.isException = function(isViewingException){
			self.isViewingException = isViewingException;
		};

		/**
		 * Sets the isViewingException to true and assigns a boolean to whether or not a person is viewing a Deleted Exception.
		 *
		 * @param isViewingDeletedException
		 */
		self.isDeletedException = function(isViewingDeletedException){
			self.isException(true);
			self.isViewingDeletedException = isViewingDeletedException;
		};

		/**
		 * Will fetch a list of  DiscontinueParametersAuditRecords that contain audits for a Discontinue exception.
		 */
		self.getDeletedDiscontinueExceptionParameterAudit = function(){
			self.isDeletedException(true);
			self.init();
			discontinueParametersFactory.getDiscontinueExceptionsParameterDeleteAudits(self.setAudits, self.fetchError);
		};

		// Register callbacks.
		$scope.$on('displayExceptionParametersAudit', self.getDiscontinueExceptionParameterAudit);
		$scope.$on('displayParametersAudit', self.getDiscontinueParameterAudit);
		$scope.$on('displayDeletesAudit', self.getDeletedDiscontinueExceptionParameterAudit);

	}
})();
