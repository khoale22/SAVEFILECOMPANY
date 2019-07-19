/*
 *
 * metadataAttributeDetailsController.js
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 * @author s573181
 * @since 2.22.0
 */
'use strict';

/**
 * The controller for the Attribute Maintenance Details Screen
 */
(function() {
	angular.module('productMaintenanceUiApp').directive('metadataAttributeDetailsDirective', metadataAttributeDetailsController);

	metadataAttributeDetailsController.$inject = ['AttributeMetaDataApi', 'ApplicableTypeApi', 'attributeMaintenanceApi', 'AudienceApi', 'AttributeStatusApi'];

	/**
	 * Constructs the controller.
	 */
	function metadataAttributeDetailsController(attributeMetaDataApi, applicableTypeApi, attributeMaintenanceApi, audienceApi, attributeStatusApi) {
		return {
			scope: {
				attribute: '<',
				onReturn: '&'
			}
			,
			restrict: 'E',
			// Inline template which is bound to message variable in the component controller
			templateUrl: 'src/metadataAttributes/metadataAttributeDetails.html',
			controllerAs: 'metadataAttributeDetailsDirective',
			// The controller that handles our component logic

			controller:
				function ($scope) {
					var self = this;
					/**
					 * Whether or not the controller is waiting for data
					 * @type {boolean}
					 */
					self.isWaiting = false;
					/**
					 * The List of applicable types.
					 * @type {Array}
					 */
					self.applicableTypes = [];
					/**
					 * The selected applicable type.
					 * @type {{}}
					 */
					self.selectedApplicableType = null;

					/**
					 * The List of attribute domains.
					 * @type {Array}
					 */
					self.attributeDomains = [];
					/**
					 * The selected attribute domain.
					 * @type {{}}
					 */
					self.selectedAttributeDomain= null;
					/**
					 * The List of attribute status.
					 * @type {Array}
					 */
					self.attributeStatuses = [];
					/**
					 * The selected attribute status.
					 * @type {{}}
					 */
					self.selectedAttributeStatus= null;
					/**
					 * The List of audiences.
					 * @type {Array}
					 */
					self.audiences = [];
					/**
					 * The selected audience.
					 * @type {{}}
					 */
					self.selectedAudience= null;


					/**
					 * Sets the two way binded value to null so this directive will disappear due to the ng-show & ng-if
					 */
					self.returnToList = function () {
						$scope.onReturn();
					};

					/**
					 * Initiates the construction of the metadata attribute details controller.
					 */
					self.$onInit = function () {
						self.touchedInit = true;
						self.originalAttribute = angular.copy($scope.attribute);
						self.originalAttribute.applicableTo = self.originalAttribute.applicableTo.trim();
						self.attribute = $scope.attribute;
						self.fetchApplicableTypes();
						self.fetchAttributeDomains();
						self.fetchAudiences();
						self.fetchAttributeStatuses();
					};

					/**
					 * Sets selected drop down codes on the object to be saved. If attribute status code is changed,
					 * it updates the status changed date. Then saves the data.
					 */
					self.save = function () {
						self.isWaiting = true;
						var attributeMetaData = angular.copy(self.attribute);
						if(self.selectedApplicableType) {
							attributeMetaData.applicableTo = self.selectedApplicableType.id;
						}
						if(self.selectedAttributeDomain) {
							attributeMetaData.domainCode = self.selectedAttributeDomain.attributeDomainCode;
						}
						if(self.selectedAttributeStatus) {
							attributeMetaData.attributeStatusCode = self.selectedAttributeStatus.attributeStatusCode;
							attributeMetaData.attributeStatusChangedDate =  new Date().toJSON().slice(0,10);
						}
						if(self.selectedAudience) {
							attributeMetaData.audienceCode = self.selectedAudience.audienceCode;
						}
						attributeMetaDataApi.save(attributeMetaData, self.saveSuccess, self.fetchError);
					};

					/**
					 * Fetches the applicable types, and sets the drop down value.
					 */
					self.fetchApplicableTypes = function () {
						applicableTypeApi.findAll({}, self.loadApplicableTypesData, self.fetchError)
					};
					/**
					 * Fetches the attribute domains and sets the drop down value.
					 */
					self.fetchAttributeDomains = function () {
						attributeMaintenanceApi.getAttributeDomains({}, self.loadAttributeDomains, self.fetchError)
					};
					/**
					 * Fetches the audiences and sets the drop down value.
					 */
					self.fetchAudiences = function () {
						audienceApi.findAll({}, self.loadAudiences, self.fetchError)
					};
					/**
					 * Fetches the attribute statuses and sets the drop down value.
					 */
					self.fetchAttributeStatuses = function () {
						attributeStatusApi.findAll({}, self.loadAttributeStatuses, self.fetchError)
					};
					/**
					 * Sets the applicable types and drop down value.
					 * @param results
					 */
					self.loadApplicableTypesData = function (results) {
						self.isWaiting = false;
						self.applicableTypes = results;
						self.setSelectedApplicableType();
					};
					/**
					 * Sets the attribute domains and drop down value.
					 * @param results
					 */
					self.loadAttributeDomains = function (results) {
						self.isWaiting = false;
						self.attributeDomains = results;
						self.setSelectedAttributeDomains();
					};
					/**
					 * Sets the audiences and drop down value.
					 * @param results
					 */
					self.loadAudiences = function (results) {
						self.isWaiting = false;
						self.audiences = results;
						self.setSelectedAudiences();
					};
					/**
					 * Sets the attribute statuses and drop down value.
					 * @param results
					 */
					self.loadAttributeStatuses = function (results) {
						self.isWaiting = false;
						self.attributeStatuses = results;
						self.setSelectedAttributeStatus();
					};
					/**
					 * Resets the data to original state.
					 */
					self.reset = function () {
						self.attribute = angular.copy(self.originalAttribute);
						self.setSelectedApplicableType();
						self.setSelectedAttributeDomains();
						self.setSelectedAttributeStatus();
						self.setSelectedAudiences();
					};
					/**
					 * Checks if there has been any changes.
					 * @returns {boolean|{}}
					 */
					self.isDifference = function () {
						return JSON.stringify(self.originalAttribute) !== JSON.stringify(self.attribute)
							|| (self.selectedApplicableType &&
								self.selectedApplicableType.id !== self.originalAttribute.applicableTo)
							|| (self.selectedAttributeDomain &&
								self.selectedAttributeDomain.attributeDomainCode !== self.originalAttribute.domainCode)
							|| (self.selectedAudience &&
								self.selectedAudience.audienceCode !== self.originalAttribute.audienceCode)
							|| (self.selectedAttributeStatus &&
								self.selectedAttributeStatus.attributeStatusCode !== self.originalAttribute.attributeStatusCode);
					};
					/**
					 * Sets the applicable type drop down value if found.
					 */
					self.setSelectedApplicableType = function () {
						var isFound = false;
						for(var x=0; x<self.applicableTypes.length; x++) {
							if(self.applicableTypes[x].id === self.attribute.applicableTo) {
								self.selectedApplicableType = self.applicableTypes[x];
								isFound = true;
								break;
							}
						}
						if(!isFound) {
							self.selectedApplicableType = null;
						}
					};
					/**
					 * Sets the attribute domain drop down value if found.
					 */
					self.setSelectedAttributeDomains = function () {
						var isFound = false;
						for (var x = 0; x < self.attributeDomains.length; x++) {
							if (self.attributeDomains[x].attributeDomainCode === self.attribute.domainCode) {
								self.selectedAttributeDomain = self.attributeDomains[x];
								isFound = true;
								break;
							}
						}
						if (!isFound) {
							self.selectedAttributeDomain = null;
						}
					};
					/**
					 * Sets the audience drop down value if found.
					 */
					self.setSelectedAudiences = function () {
						var isFound = false;
						for (var x = 0; x < self.audiences.length; x++) {
							if (self.audiences[x].audienceCode === self.attribute.audienceCode) {
								self.selectedAudience = self.audiences[x];
								isFound = true;
								break;
							}
						}
						if (!isFound) {
							self.selectedAudience = null;
						}
					};
					/**
					 * Sets the attribute status down value if found.
					 */
					self.setSelectedAttributeStatus = function () {
						var isFound = false;
						for (var x = 0; x < self.attributeStatuses.length; x++) {
							if (self.attributeStatuses[x].attributeStatusCode === self.attribute.attributeStatusCode) {
								self.selectedAttributeStatus = self.attributeStatuses[x];
								isFound = true;
								break;
							}
						}
						if (!isFound) {
							self.selectedAttributeStatus = null;
						}
					};

					/**
					 * Callback for when a save successfully happens.
					 *
					 * @param result The repsonse from the backend.
					 */
					self.saveSuccess = function(result) {
						self.isWaiting = false;
						self.success = result.message;
						self.originalAttribute = angular.copy(result.data);
						self.attribute = result.data;
						self.fetchApplicableTypes();
						self.fetchAttributeDomains();
						self.fetchAudiences();
						self.fetchAttributeStatuses();
					};

					/**
					 * If there is an error this will display the error
					 * @param error
					 */
					self.fetchError = function (error) {
						self.isWaiting = false;
						self.data = null;
						if (error && error.data) {
							if (error.data.message !== null && error.data.message !== "") {
								self.setError(error.data.message);
							} else {
								self.setError(error.data.error);
							}
						}
						else {
							self.setError("An unknown error occurred.");
						}
					};

					/**
					 * Sets the error
					 * @param error
					 */
					self.setError = function (error) {
						self.error = error;
					};

				}
		}
	}

})();
