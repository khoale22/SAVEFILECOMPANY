/*
 *
 * sourcePriorityController.js
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
 * The controller for the Source Priority Controller.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('SourcePriorityController', sourcePriorityController);

	sourcePriorityController.$inject = ['eCommerceApi', 'ngTableParams', '$log'];

	/**
	 * Constructs the controller.
	 */
	function sourcePriorityController(eCommerceApi, ngTableParams, $log) {

		var self = this;

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

		/**
		 * Initiates the construction of the source priority table
		 */
		self.init = function () {
			self.isWaiting = true;
			self.touchedInit = true;
			self.getSourcePriorityTableData();
		};

		/**
		 * Loads the priority table data from the back end
		 */
		self.getSourcePriorityTableData = function () {
			eCommerceApi.getSourcePriorityTable(
				self.loadSourcePriorityTable,
				self.fetchError);
		};

		/**
		 *If there are no errors build this method will initiate the table construction
		 * @param results
		 */
		self.loadSourcePriorityTable = function (results) {
			self.sourcePriorityTable = results;
			self.buildTable();
			self.isWaiting = false;
		};

		/**
		 *If there is an error this will display the error
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message != null && error.data.message != "") {
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

		/**
		 *Builds the priority table
		 */
		self.buildTable = function () {
			var currentAttribute = {
				name:null,
				sourcePriorities:[],
				titleString: ""
			};
			angular.forEach(self.sourcePriorityTable, function(value, key){
				if(self.attributeNames.indexOf(value.logicalPhysicalRelationship.attribute.attributeName) == -1) {
					self.attributeNames.push(value.logicalPhysicalRelationship.attribute.attributeName);
					if (currentAttribute.name != null) {
						self.tagPriorityNames(currentAttribute);
						self.attributes.push(currentAttribute);
						self.checkColumnsNeeded(currentAttribute);
					}
					currentAttribute = {
						id:value.key.logicalAttributeId,
						name:value.logicalPhysicalRelationship.attribute.attributeName,
						sourcePriorities:[],
						titleString: ""
					}
				}
				var sourceDescription = null;
				if(value.logicalPhysicalRelationship.relationshipGroup === null){
					if(value.attribute === null){
						sourceDescription = value.logicalPhysicalRelationship.attribute.attributeName;
					} else {
						sourceDescription = value.attribute.attributeName;
					}
				}else  {
					sourceDescription = value.logicalPhysicalRelationship.relationshipGroup.relationshipGroupDescription;
				}
				var sourcePriority = {
					priorityNumber: value.attributePriorityNumber,
					priorityName: value.sourceSystem.description,
					priorityDescription: sourceDescription,
					tag: 1
				};
				currentAttribute.sourcePriorities.push(sourcePriority)
			});
			self.attributes.push(currentAttribute);
			self.checkColumnsNeeded(currentAttribute);
			self.tagPriorityNames(currentAttribute);
			self.buildColumns();
		};

		/**
		 * This will check the current attribute to see how many priorities it has, this is done because the priorities
		 * are not promised contiguous
		 * @param currentAttribute
		 */
		self.checkColumnsNeeded = function (currentAttribute) {
			if(currentAttribute.sourcePriorities.length > self.maxNumberofPriorities){
				self.maxNumberofPriorities = currentAttribute.sourcePriorities.length;
			}
		}

		/**
		 * Creates a list with all of the column names based on the priorities
		 * @param maxNumber
		 */
		self.buildColumns = function(){
			self.columns.push({title: 'Attribute Name',
				field: 'name',
				visible: true});
			for(var i = 1; i<=self.maxNumberofPriorities; i++){
				self.columns.push({
					title: i,
					field: i,
					visible: true});
			}
		};

		/**
		 *If a priority has the same description more than once this will place a tag to differentiate them.
		 */
		self.tagPriorityNames = function (attribute) {
			var index = 0;
			angular.forEach(attribute.sourcePriorities, function(value, key){
				index = index +1;
				var tag = 1;
				var name = value.priorityName;
				for(var i = index; i<attribute.sourcePriorities.length; i++){
					if(attribute.sourcePriorities[i].tag === 1) {
						if (name.localeCompare(attribute.sourcePriorities[i].priorityName) === 0) {
							tag = tag + 1;
							attribute.sourcePriorities[i].tag = tag;
						}
					}
				}
				var priorityName = null;
				if(tag>1 || value.tag>1){
					priorityName = value.priorityName.trim() + "- " +value.tag;
				} else {
					priorityName = value.priorityName.trim()
				}
				attribute[index] = priorityName;
				attribute.titleString = attribute.titleString + priorityName + ":    " + value.priorityDescription + "\n";
			})
		};
	}
})();

