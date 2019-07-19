/*
 *
 * discontinueParametersController
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
'use strict';

/**
 * Controller to support the page that allows users to view and modify the base product discontinue rules.
 *
 * @author s573181
 * @since 2.0.2
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('DiscontinueParametersController', discontinueParametersController)

	discontinueParametersController.$inject = ['DiscontinueParametersService', 'DiscontinueParametersAuditService', 'DiscontinueParametersFactory', '$scope', '$rootScope'];

	/**
	 * Constructs the controller.
	 *
	 * @param discontinueParametersService API to call the backend.
	 * @param discontinueParametersFactory Passes data between this controller and the one for the template page.
	 * @param $scope The Angular scope representing this controller.
	 * @param $rootScope The Angular scope representing this application.
	 */
	function discontinueParametersController(discontinueParametersService, discontinueParametersAuditService, discontinueParametersFactory, $scope, $rootScope) {

		var self = this;

		/**
		 * The original DiscontinueParameters sent from the backend before the user has modfied them.
		 *
		 * @type {DiscontinueParameters}
		 */
		var originalParameters = {};

		/**
		 * Whether or not the user is currently editing the parameters.
		 *
		 * @type {boolean}
		 */
		self.editOptions = false;

		/**
		 * Stores error messages.
		 *
		 * @type {String}
		 */
		self.error = null;

		/**
		 * Stores success messages.
		 *
		 * @type {String}
		 */
		self.success = null;

		/**
		 * Initializes the data for the controller.
		 */
		self.init = function(){
			discontinueParametersService.setDefaultParametersObject(null);
			discontinueParametersFactory.getDiscontinueParameters({}, self.setParameters, self.fetchError);
			self.error = null;
		};

		/**
		 * Callback that will respond to fetch of the discontinue parameters from the backed.
		 *
		 * @param result A successful response from the backend.
		 */
		self.setParameters = function(result){
			self.data = result;
			angular.copy(result, originalParameters);
			discontinueParametersService.setParametersObject(originalParameters);
		};

		/**
		 * Callback that will respond to errors sent from the backend.
		 *
		 * @param error The object with error information.
		 */
		self.fetchError = function(error){
			if (error && error.data) {
				self.error = error.data.message;
			} else {
				self.error = "An unknown error occurred.";
			}
		};


		/**
		 * Callback for the successful save of data.
		 *
		 * @param result The response sent from the backent.
		 */
		self.saveSuccessful = function(result) {
			self.success = result.message;
			self.setParameters(result.data);
		};

		/**
		 * Toggles between editing and non-editing modes.
		 */
		self.setEditing = function(isEditing){
			self.editOptions = isEditing;
			discontinueParametersService.setEditing(self.editOptions);
			self.error = null;
			self.success = null;
		};

		/**
		 * Sets the parameters that the user is editing to their original state.
		 */
		self.refresh = function() {
			discontinueParametersService.setParametersObject(originalParameters);
			self.error = null;
		};

		/**
		 * Initiates a call to save the user's changes.
		 */
		self.saveParameters = function(){
			// This will be handled in DiscontinueParametersTemplateController. That will read the user's changes
			// from the form and broadcast a setParameterChanges event.
			$rootScope.$broadcast('getParameterChanges');
		};

		/**
		 * Callback that will handle the setParameterChanges event. It will see if anything has changed and, if so,
		 * send it to the backend.
		 */
		self.saveChanges = function() {
			// Get the changes to the discontinue rules
			var newParameters = discontinueParametersService.getParametersObject();

			// If there are changes, send them to the backend.
			if (!(JSON.stringify(newParameters) === JSON.stringify(originalParameters))){

				discontinueParametersFactory.saveParameters(newParameters, self.saveSuccessful, self.fetchError);

				self.setEditing(false);


			} else {

				self.error = "No changes detected."
			}
		};

		/**
		 * sends selected exception's information to discontinueParametersAuditService.
		 */
		self.showAudit = function(){
			self.isAuditing(true);
			discontinueParametersAuditService.setAuditParameters();
		};

		/**
		 * Sets the boolean value to whether or not a user is viewing the audit page.
		 *
		 * @param bool
		 */
		self.isAuditing = function(bool){
			self.viewingAudit = bool;
		};

		/**
		 * sets the viewingAudit flag to false that the front end knows to not display the audit information.
		 */
		self.closeDiscontinueParametersAudit = function(){
			self.isAuditing(false);
		};

		// Register callbacks.
		$scope.$on("setParametersChanges", self.saveChanges);
		$scope.$on('closeAuditParameters', self.closeDiscontinueParametersAudit);
	}
})();
