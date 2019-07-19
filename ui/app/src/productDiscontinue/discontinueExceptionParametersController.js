/*
 *
 *  discontinueExceptionParametersController.js
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
 * Controller to support the page that allows users to view and modify product discontinue exception rules.
 *
 * @author s573181
 * @since 2.0.2
 */

(function() {
	angular.module('productMaintenanceUiApp').controller('DiscontinueExceptionParametersController', discontinueExceptionParametersController);

	discontinueExceptionParametersController.$inject = ['DiscontinueParametersService','DiscontinueParametersFactory','discontinueDefinitions', 'DiscontinueParametersAuditService', '$scope'];

	/**
	 * Constructs the controller.
	 *
	 * @param discontinueParametersService Passes data between this controller and the one for the template page.
	 * @param discontinueParametersFactory The API to call the backend.
	 * @param discontinueParametersAuditService Passes data from this controller and the discontinueParameterAuditController.
	 * @param $scope the scope
	 */
	function discontinueExceptionParametersController(discontinueParametersService, discontinueParametersFactory, discontinueDefinitions, discontinueParametersAuditService, $scope) {

		var self = this;

		/**
		 * The object thatwill hold the defenitions for desicontinue terms.
		 * @type {?}
		 */
		self.discontinueDefinition = discontinueDefinitions;
		/**
		 * Whether or not the user is currently adding a new exception.
		 * @type {boolean}
		 */
		self.addingException = false;

		/**
		 * Whether or not a rule is selected by the user.
		 * @type {boolean}
		 */
		self.itemSelected = false;

		/**
		 * Whether or not the user is editing an excption.
		 * @type {boolean}
		 */
		self.editing = false;

		/**
		 * The list of available discontinue exception types.
		 * @type {Array}
		 */
		self.dropdownOptions = [];

		/**
		 * The error message to display.
		 *
		 * @type {String}
		 */
		self.error = null;

		/**
		 * The success message to display.
		 *
		 * @type {String}
		 */
		self.success = null;

		/**
		 * Wheter or not the selected exception has its never discontinue flag set.
		 *
		 * @type {boolean}
		 */
		self.neverDiscontinueSwitch = false;

		/**
		 * Whether or not the user is viewing the ExceptionParameters Audit page.
		 *
		 * @type {boolean}
		 */
		self.viewingAudit = false;

		/**
		 * Whether or not the user is viewing the ExceptionParameters delete Audits page.
		 *
		 * @type {boolean}
		 */
		self.viewingDeletesAudit = false;

		/**
		 * Whether or not the user is viewing the Deleted Exceptions.
		 *
		 * @type {boolean}
		 */
		self.isViewingDeletedExceptions = false;
		/**
		 * Keep track of latest Request for asynchronous calls.
		 * @type {number}
		 */
		self.latestRequest = 0;

		const DEFAULT_DISCONTINUE_TYPE = "All";

		/**
		 * Initializes the controller.
		 */
		self.init = function(){
			self.neverDiscontinueSwitch = false;
			self.error = null;
			self.success = null;

			// Get the list of exception types.
			discontinueParametersFactory.getExceptionTypes({}, self.setExceptionTypes, self.fetchError);

			// Get the full list of exceptions.
			self.getDiscoExceptionsByType(DEFAULT_DISCONTINUE_TYPE);

			// Set the default rules to nothing (these will be reset after the call below resolves).
			discontinueParametersService.setDefaultParametersObject(null);

			// Get the default rules.
			discontinueParametersFactory.getDiscontinueParameters({}, self.setExceptionDefaultRules, self.fetchError);
		};

		/**
		 * Will fetch a list of existing discontinue exceptions for a given type.
		 *
		 * @param selectedOption The type of exceptions to look for.
		 */
		self.getDiscoExceptionsByType = function(selectedOption){
			self.error = null;
            self.success = null;
			self.selectedDiscoType = selectedOption;
			self.addingException = false;
			self.neverDiscontinueSwitch = false;
			self.exceptionTypeId = null;
			self.setEditing(false);
			discontinueParametersService.setParametersObject(null);

			discontinueParametersFactory.getDiscontinueExceptionsByType({exceptionType: self.selectedDiscoType}, self.setDiscoExceptionList, self.fetchError)
		};

		/**
		 * Selects an exception, and displays the information requested.
		 *
		 * @param exceptionSelected
		 */
		self.selectException = function(exceptionSelected){
			self.exceptionTypeId = exceptionSelected.exceptionTypeId;
			self.originalObject = angular.copy(exceptionSelected);
			if(!self.isViewingDeletedExceptions) {
				self.itemSelected = true;
				self.setEditing(false);
				self.neverDiscontinueSwitch = exceptionSelected.neverDiscontinueSwitch;
				self.discontinueExceptionTitle = exceptionSelected.exceptionName;
				discontinueParametersService.setParametersObject(exceptionSelected);
			} else{
				self.showAudit();
			}
		};

		/**
		 * DeSelects an exception. This is called only after an exception has been deleted.
		 */
		self.deselectException = function(){
			self.exceptionTypeId = null;
			self.itemSelected = false;
			self.setEditing(false);
			self.neverDiscontinueSwitch = false;
			self.originalObject = null;
			self.discontinueExceptionTitle = null;

			discontinueParametersService.setParametersObject(null);
		};

		/*
		 * Functions related to adding an exception.
		 */

		/**
		 * Adds a row to the grid which will allow a user to create a new excption rule.
		 */
		self.addRow = function(){

			// if selected discontinue exception type is not SBT, exception name and typeID do not need to be set
			if(this.selectedDiscoType != 'SBT') {
				self.exceptionList.push({
						exceptionType: self.selectedDiscoType,
						addingException: true
					}
				);
			}

			// otherwise exception name and typeID need to be set
			else {
				self.exceptionList.push({
						exceptionType: self.selectedDiscoType,
						addingException: true,
						exceptionName: "SCAN BASED TRADE"
					}
				);
				self.exceptionTypeId = "";
			}
			self.setNewRules();
			self.setAddingExceptionStatus(true);
		};

		/**
		 * Sets the state of whether or not the user is currently adding a row.
		 *
		 * @param isAdding True if the user is adding a row and false otherwise.
		 */
		self.setAddingExceptionStatus = function(isAdding){
			self.addingException = isAdding;
			discontinueParametersService.setEditing(isAdding);
			document.getElementById("neverDiscontinueSwitch").disabled = !isAdding;
		};

		/**
		 * Initializes a new discontinue exception rule.
		 */
		self.setNewRules = function() {
			self.neverDiscontinueSwitch = false;
			// Create a new DiscontinueRules object and default the exception rule parameters.
			// The others will be set in the service.
			self.originalObject = {
				neverDiscontinueSwitch: self.neverDiscontinueSwitch,
				exceptionName: '',
				exceptionType: self.selectedDiscoType,
				exceptionNumber: 1,
				priorityNumber: 1
			};
			discontinueParametersService.setParametersObject(self.originalObject);

		};

		/**
		 * Cancels a user adding an exception.
		 */
		self.cancelAddException = function() {
			self.exceptionList.pop();
			self.setAddingExceptionStatus(false);
		};

		/**
		 * Adds a new discontinue exception rule.
		 */
		self.addExceptionRule = function(){

			if(self.exceptionSelected != null || self.selectedDiscoType === 'UPC') {
				var exceptionRule = discontinueParametersService.getParametersObject();
				if(self.exceptionSelected != null) {
					exceptionRule.exceptionTypeId = self.exceptionSelected.normalizedId;
				} else if(self.selectedDiscoType === 'UPC'){
					exceptionRule.exceptionTypeId = self.exceptionTypeId;
				}
				exceptionRule.neverDiscontinueSwitch = self.neverDiscontinueSwitch;
				self.setAddingExceptionStatus(true);
				discontinueParametersFactory.addDiscontinueExceptionRules(exceptionRule, self.updateTemplate, self.fetchError);
			}
			else {
				self.fetchError({data: {message: "Please select a " + self.selectedDiscoType + " to add an exception."}})
			}
		};

		/**
		 * Removes an added exception if the user cancels the add.
		 */
		self.removeAddedException = function(){
			self.exceptionSelected = null;
			self.exceptionList.pop();
			self.setAddingExceptionStatus(false);
			self.setEditing(false);
			discontinueParametersService.setParametersObject({});
		};

		/*
		 * Functions related to editing an exception.
		 */

		/**
		 * Sends a modified exception rule to the backend to be saved.
		 */
		self.updateExceptionRule = function(){
			var exceptionRule = discontinueParametersService.getParametersObject();
			exceptionRule.neverDiscontinueSwitch = self.neverDiscontinueSwitch;
			var difference = JSON.stringify(exceptionRule) === JSON.stringify(self.originalObject);
			if(!difference){
				discontinueParametersFactory.updateDiscontinueExceptionRules(exceptionRule, self.updateTemplate, self.fetchError);
			}
		};

		/**
		 * Cancels an edit on a discontinue exception.
		 */
		self.cancelExceptionEdit = function() {
            self.exceptionSelected = null;
			discontinueParametersService.setParametersObject(self.originalObject);
			self.setEditing(false);
		};

		/**
		 * Deletes a discontinue exception rule.
		 */
		self.deleteDiscontinueException = function(){
			var exceptionRule = discontinueParametersService.getParametersObject();
			self.deletingException = true;
			discontinueParametersFactory.deleteDiscontinueExceptionRules(exceptionRule, self.updateTemplate, self.fetchError);
		};

		/*
		 * State related functions
		 */

		/**
		 * Resets a modified exception to its original condition.
		 */
		self.refreshCurrentException = function(){
			discontinueParametersService.setParametersObject(self.originalObject);
		};

		/**
		 * Sets the state of whether or not the user is currently editing an exception rule.
		 *
		 * @param isEditing True of they are editing a rule and false otherwise.
		 */
		self.setEditing = function(isEditing){
			self.editing = isEditing;
			document.getElementById("neverDiscontinueSwitch").disabled = !isEditing;
			discontinueParametersService.setEditing(isEditing);
		};

		/**
		 * Return whether selected discontinue type should prevent 'Add' option from being visible.
		 *
		 * @returns {boolean} true if selected discontinue type is !'All' && !'SBT'
		 * 				false if selected discontinue type is 'All'
		 * 				false if selected discontinue type is 'SBT' and list containing all of the 'SBT'
		 * 					exceptions size is greater than 0 (There can only be one exception of 'SBT' type)
		 */
		self.isAddEligible = function(){

			// user must select a specific discontinue exception type to add an exception: return false
			if(this.selectedDiscoType == 'All'){
				return false;
			}

			// if there is at least 1 SBT exception, user cannot add another: return false
			else if(this.selectedDiscoType == 'SBT'){
				if(self.exceptionList.length > 0){
					return false;
				}
			}

			// otherwise, user can add an exception
			return true;
		};

		/**
		 * Return whether adding an exception should be able to search a cache for available options.
		 *
		 * @returns {boolean} true if selected discontinue type is !'SBT' && !'UPC'
		 * 				false otherwise
		 */
		self.isSearchable = function(){
			return (self.selectedDiscoType != 'SBT' && self.selectedDiscoType != 'UPC');
		};

		/**
		 * Gets values for dropdown based on search query.
		 *
		 * @param query String to search by.
		 */
		self.getCurrentDropDownResults = function(query){
			var thisRequest = ++self.latestRequest;
			if (query === null || !query.length || query.length === 0){
				self.valueList = [];
				return;
			}
			switch(self.selectedDiscoType) {
				case 'Vendor':
				{
					discontinueParametersFactory.
						getVendorsByRegularExpression({
							searchString: query,
							page: 0,
							pageSize: 20
						},
						//success
						function (results) {
							if(thisRequest === self.latestRequest) {
								self.valueList = results.data;
							}
						},
						//error
						function(results) {
							if (thisRequest === self.latestRequest) {
								self.valueList = [];
							}
						}
					);
					break;
				}
				case 'Class':{
					discontinueParametersFactory.
						getClassesByRegularExpression({
							searchString: query,
							page: 0,
							pageSize: 20
						},
						//success
						function (results) {
							if(thisRequest === self.latestRequest) {
								self.valueList = results.data;
							}
						},
						//error
						function(results) {
							if (thisRequest === self.latestRequest) {
								self.valueList = [];
							}
						}
					);
					break;
				}
				case 'Commodity':{
					discontinueParametersFactory.
						getCommoditiesByRegularExpression({
							searchString: query,
							page: 0,
							pageSize: 20
						},
						//success
						function (results) {
							if(thisRequest === self.latestRequest) {
								self.valueList = results.data;
							}
						},
						//error
						function(results) {
							if (thisRequest === self.latestRequest) {
								self.valueList = [];
							}
						}
					);
					break;
				}
				case 'Dept':
				{
					discontinueParametersFactory.
						getSubDepartmentsByRegularExpression({
							searchString: query,
							page: 0,
							pageSize: 20
						},
						//success
						function (results) {
							if(thisRequest === self.latestRequest) {
								self.valueList = results.data;
							}
						},
						//error
						function(results) {
							if (thisRequest === self.latestRequest) {
								self.valueList = [];
							}
						}
					);
					break;
				}
				case 'Sub-commodity':
				{
					discontinueParametersFactory.
						getSubCommoditiesByRegularExpression({
							searchString: query,
							page: 0,
							pageSize: 20
						},
						//success
						function (results) {
							if(thisRequest === self.latestRequest) {
								self.valueList = results.data;
							}
						},
						//error
						function(results) {
							if (thisRequest === self.latestRequest) {
								self.valueList = [];
							}
						}
					);
					break;
				}
			}
		};

		/**
		 * Return whether user can add an exception.
		 *
		 * @returns {boolean} true if isAddEligible() && not currently adding or editing an exception
		 * 				false otherwise
		 */
		self.canAdd = function(){
			return self.isAddEligible() && !self.addingException && !self.editing;
		};

		/**
		 * Return whether user can edit an exception.
		 *
		 * @returns {boolean} true if an item has been selected and user is not currently adding or editing an exception
		 * 				false otherwise
		 */
		self.canEdit = function(){
			return !self.addingException && self.itemSelected && !self.editing
		};

		/*
		 * Callbacks
		 */

		/**
		 * Callback to set the list of e exception types that will show up in the drop-down.
		 *
		 * @param results The response from the backend with the list of exception tyeps.
		 */
		self.setExceptionTypes = function(results) {
			self.dropdownOptions = results;
		};


		/**
		 * Callback for then the API is asked for a list of discontinue exception rules.
		 *
		 * @param results The list of discontinue exception rules returned from the server.
		 */
		self.setDiscoExceptionList = function(results){
			self.discontinueExceptionTitle = null;
			self.itemSelected = false;
			self.exceptionList = results;
			if(self.exceptionTypeId != null) {
				for (var index in self.exceptionList){
					if(self.exceptionList[index].exceptionTypeId != null &&
						self.exceptionList[index].exceptionTypeId.trim() === self.exceptionTypeId.trim()) {
						self.selectException(self.exceptionList[index]);
						break;
					}
				}
			}
		};

		/**
		 * Callback for when there is an error returned from the server.
		 *
		 * @param error The error from the server.
		 */
		self.fetchError = function(error){
			self.success = null;
			if (error && error.data) {
				self.error = error.data.message;
			} else {
				self.error = "An unknown error occured.";
			}
		};

		/**
		 * Callback for when the API asks for the default product discontinue rules.
		 *
		 * @param result The default product discontinue rules.
		 */
		self.setExceptionDefaultRules = function(result){
			discontinueParametersService.setDefaultParametersObject(result);
		};

		/**
		 * Callback for after a save or delete. It re
		 * @param results
		 */
		self.updateTemplate = function(results) {
			self.error = null;
			self.success = results.message;
			self.setEditing(false);
			self.setAddingExceptionStatus(false);
            self.exceptionSelected = null;

			// additional work only if an exception was deleted
			if(self.deletingException){
				self.deselectException();
				self.deletingException = false;
			}
			discontinueParametersFactory.getDiscontinueExceptionsByType({exceptionType: self.selectedDiscoType}, self.setDiscoExceptionList, self.fetchError);
		};

		/**
		 * sends selected exception's information to discontinueParametersAuditService.
		 */
		self.showAudit = function(){
			self.isAuditing(true);
			discontinueParametersAuditService.setAuditExceptionParameters(self.originalObject);
		};

		/**
		 * Switches the DeletedExceptionsView and sets the data to what needs to be displayed.
		 */
		self.changeDeletedExceptionsView = function(){
			if(self.isViewingDeletedExceptions){
				self.exceptionList = self.existingExceptionsList;
				self.isShowingDeletedExceptions(false);
			} else{
				self.isShowingDeletedExceptions(true);
				self.existingExceptionsList = self.exceptionList;
				self.getDeletedExceptions();
			}
		};

		/**
		 * Sets the boolean value to whether or not a user is viewing the delete audit page.
		 *
		 * @param value A boolean value representing the visibility of the deleted audits.
		 */
		self.isAuditingDeletes = function(value){
			self.viewingDeletesAudit = value;
		};

		/**
		 * Sets the boolean value to whether or not a user is viewing the audit page.
		 *
		 * @param value A boolean value representing the visibility of the audit screen.
		 */
		self.isAuditing = function(value){
			self.viewingAudit = value;
		};

		/**
		 * Sets the viewingAudit flag to false that the front end knows to not display the audit information.
		 */
		self.closeDiscontinueParametersAudit = function(){
			self.isAuditing(false);
		};

		/**
		 * Gets the deleted exception audits.
		 */
		self.getDeletedExceptions = function(){
			discontinueParametersFactory.getDeletedExceptionsAuditTypes({}, self.setDeletedDiscoExceptionList, self.fetchError);
		};

		/**
		 * Sets the deleted exceptions list.
		 *
		 * @param results The returned results of a method call.
		 */
		self.setDeletedDiscoExceptionList = function(results){
			self.discontinueExceptionTitle = null;
			self.itemSelected = false;
			self.exceptionList = results;
		};

		/**
		 * Sets the isViewingDeletedExceptions flag to whether it is viewing the deleted exceptions or not.
		 *
		 * @param value
		 */
		self.isShowingDeletedExceptions = function(value){
			self.isViewingDeletedExceptions = value;
		};

		// Register callbacks.
		$scope.$on('closeAuditParameters', self.closeDiscontinueParametersAudit);
	}
})();
