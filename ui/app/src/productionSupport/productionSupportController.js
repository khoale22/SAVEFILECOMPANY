/*
 * productionSupportController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Controller to support the page with production support functions.
 *
 * @author d116773
 * @since 2.0.2
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('ProductionSupportController', productionSupportController);

	productionSupportController.$inject = ['BatchApi'];

	/**
	 * Creates the controller for the production support page.
	 *
	 * @param batchApi The API to get information related to batch jobs.
	 */
	function productionSupportController(batchApi) {
		var self = this;

		/**
		 * The list of available jobs.
		 */
		var jobList = null;

		/**
		 * The most recent error from the backend.
		 */
		var error = null;

		/**
		 * The batch job the user has selected to run.
		 */
		var selectedJob = null;

		/**
		 * The status of the last call to kick off a job.
		 */
		var jobStatus = null;

		/**
		 * Calls the backend to retrieve a list of available batch jobs to run.
		 */
		self.getJobsList = function() {
			batchApi.get(self.setJobsList, self.fetchError);
		};

		/**
		 * Callback for when the call to get the list of jobs is successful.
		 *
		 * @param results The list of batch jobs that the backend supports.
		 */
		self.setJobsList = function(results) {
			self.jobList = results;
		};

		/**
		 * Callback for when any call to the backend fails.
		 *
		 * @param results The error from the backend.
		 */
		self.fetchError = function(results) {
			if (results && results.data) {
				self.error = results.data.message;
			} else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Called to initialize this controller.
		 */
		self.init = function() {
			self.getJobsList();
		};

		/**
		 * Calls the backend to kickoff a job. The selectedJob property of the object should be set.
		 */
		self.runJob = function () {
			self.error = null;
			self.jobStatus = null;
			batchApi.save({jobName: self.selectedJob}, {}, self.runJobSuccess, self.fetchError);
		};

		/**
		 * Callback for when the call to kick off a batch job succeeds.
		 *
		 * @param results The response from the backend.
		 */
		self.runJobSuccess = function(results) {
			self.jobStatus = results.status;
		};
	}
})();
