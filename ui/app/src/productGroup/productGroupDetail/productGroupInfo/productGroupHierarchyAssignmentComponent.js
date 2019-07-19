/*
 *   eCommerceViewHierarchyAssignment.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * product details -> product -> eCommerce View -> eCommerceView Hierarchy Assignments.
 *
 * @author vn70633
 * @since 2.14.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('productGroupHierarchyAssignment', {
		scope: '=',
		bindings: {
			currentTab: '<',
			productGroupIdSelected: '<',
			customerHierarchyPrimaryPath: '=',
			addNewMode: '<',
			selectedHierarchyContexts:'=',
			productAssociated: '<'

		},
		// Inline template which is bind to message variable in the component controller
		templateUrl: 'src/productGroup/productGroupDetail/productGroupInfo/productGroupHierarchyAssignment.html',

		// The controller that handles our component logic
		controller: HierarchyAssignmentController
	});

	HierarchyAssignmentController.$inject = ['$scope', 'productGroupApi', '$sce','$timeout', '$rootScope'];

	/**
	 * ECommerce View Directions component's controller definition.
	 *
	 * @param $scope scope of the eCommerceView component.
	 * @param eCommerceViewApi the api of eCommerceView.
	 * @constructor
	 */
	function HierarchyAssignmentController($scope, productGroupApi, $sce, $timeout, $rootScope) {
		/**
		 * All CRUD operation controls of choice option page goes here.
		 */
		var self = this;
		var UNDEFINED_TYPE = 'undefined';
		var FIRST_HIERARCHY_LEVEL = "FIRST_HIERARCHY_LEVEL";
		var SECOND_HIERARCHY_LEVEL = "SECOND_HIERARCHY_LEVEL";
		var THIRD_HIERARCHY_LEVEL = "THIRD_HIERARCHY_LEVEL";
		var FOURTH_HIERARCHY_LEVEL = "FOURTH_HIERARCHY_LEVEL";
		var FIFTH_HIERARCHY_LEVEL = "FIFTH_HIERARCHY_LEVEL";
		var FIRST_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "firstLevel";
		var SECOND_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "secondLevel";
		var THIRD_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "thirdLevel";
		var FOURTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "fourthLevel";
		var FIFTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX = "fifthLevel";
		var STRING_HYPHEN = "-";
		var STRING_TAG = "#";
		var ADD_CLICK_BACKGROUND_CLASS = "add-click-background";
		var ADD_CLICK_BACKGROUND_CLASS_REFERENCE = ".add-click-background";
		var ACTION_CD_DELETE="D";
		var ACTION_CD_ADD="A";
		var ACTION_CD_PRIMARY="EP";
		var ACTION_CD_ALTERNATE="EA";		/**
		 * Message success
		 */
		self.success = null;
		/**
		 * Message error
		 */
		self.error = null;
		/**
		 * Hierachy first level selected original 
		 */
		self.originalFirstLevelSelected = null;
		/**
		 * Hierachy second level selected original 
		 */
		self.originalSecondLevelSelected = null;
		/**
		 * Hierachy third level selected original 
		 */
		self.originalThirdLevelSelected = null;
		/**
		 * Hierachy fourth level selected original 
		 */
		self.originalFourthLevelSelected = null;
		/**
		 * Hierachy fifth level selected original 
		 */
		self.originalFifthLevelSelected = null;
		/**
		 * Searching for custom hierachy text
		 */
		self.searchingForCustomHierarchyText = null;
		/**
		 * Custom hierachy search text
		 */
		self.customHierarchySearchText = null;
		/**
		 * Flag search for custom hierachy
		 */
		self.searchingForCustomHierarchy = false;
		/**
		 * Clear message key.
		 * @type {string}
		 */
		const CLEAR_MESSAGE = 'clearMessage';
		/**
		 * Title for pop up confirm basing on each handle.
		 * @type {string}
		 */
		self.confirmTitle = '';
		/**
		 * Message for pop up confirm basing on each handle.
		 * @type {string}
		 */
		self.confirmMessage = '';
		/**
		 * The value action what you want to confirm.
		 * @type {string}
		 */
		self.confirmAction = '';
		/**
		 * Flag for waiting response from back end.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;
		/**
		 * General eCommerce View Information
		 * @type {{}}
		 */
		self.customerHierarchyAssigment = {};
		/**
		 * General eCommerce View Information
		 * @type {{}}
		 */
		self.hierarchyContext = {};
		/**
		 * General eCommerce View Information
		 * @type {{}}
		 */
		self.hierarchyContextCurrentPath = [];
		/**
		 * General eCommerce View Information
		 * @type {{}}
		 */
		self.hierarchyContextLowestLevel = [];
		/**
		 * List of primary.
		 */
		self.primaryList = ["Primary","Alternate"];
		/**
		 * label for save.
		 */
		self.saveLabel = "Save";
		/**
		 *Style for 2 modal.
		 * @type {Object}
		 */
		self.styleDisplay2Modal = { 'z-index': 1050 };
		/**
		 * Load data for custom hierachy
		 */
		self.loadCustomHierarchy = function (){
			var firstIndex = self.findLevelIndex(self.customerHierarchyAssigment.customerHierarchyContext);
			var secondIndex=null;
			var thirdIndex = null;
			var forthIndex = null;
			var fifthIndex = null;
			if(firstIndex !== null){
				secondIndex = self.findLevelIndex(self.customerHierarchyAssigment.customerHierarchyContext.childRelationships[firstIndex]);
			}
			if(secondIndex !== null){
				thirdIndex = self.findLevelIndex(self.customerHierarchyAssigment.customerHierarchyContext.childRelationships[firstIndex].childRelationships[secondIndex]);
			}
			if(thirdIndex !==  null){
				forthIndex = self.findLevelIndex(self.customerHierarchyAssigment.customerHierarchyContext.childRelationships[firstIndex].childRelationships[secondIndex].childRelationships[thirdIndex]);

			}
			if(forthIndex !== null){
				fifthIndex = self.findLevelIndex(self.customerHierarchyAssigment.customerHierarchyContext.childRelationships[firstIndex].childRelationships[secondIndex].childRelationships[thirdIndex].childRelationships[forthIndex]);
			}
			$timeout(function() {
				self.highlightCurrentSelectedHierarchyLevel(firstIndex, secondIndex, thirdIndex, forthIndex, fifthIndex)
			});
		}
		/**
		 * This method takes level in the hierarchy and finds the next open child's level.
		 * @param hierarchyLevel the hierarchyLevel to be searched
		 * @returns {*} the index of the level to be opened.
		 */
		self.findLevelIndex = function (hierarchyLevel) {
			if(hierarchyLevel.childRelationships !== null){
				for (var index = 0; index < hierarchyLevel.childRelationships.length; index++) {
					var fifthRelationshipLevel = hierarchyLevel.childRelationships[index];
					if (!fifthRelationshipLevel.isCollapsed) {
						return index;
					}
				}
			}
			return null;
		};
		/**
		 * This method removes the previous selected highlighted hierarchy level(if there is one), and then sets the
		 * current selected level in a custom hierarchy to be highlighted, and . This is done by using the string
		 * representation of the id tag (i.e. '#firstLevel') and the index of the current level and all indices of
		 * parent levels.
		 *
		 * @param firstIndex The index of the first level in the custom hierarchy.
		 * @param secondIndex The index of the second level in the custom hierarchy.
		 * @param thirdIndex The index of the third level in the custom hierarchy.
		 * @param fourthIndex The index of the fourth level in the custom hierarchy.
		 * @param fifthIndex The index of the fifth level in the custom hierarchy.
		 */
		self.highlightCurrentSelectedHierarchyLevel = function(firstIndex, secondIndex, thirdIndex, fourthIndex, fifthIndex) {
			// remove any html elements with 'add-click-background'
			$(ADD_CLICK_BACKGROUND_CLASS_REFERENCE).removeClass(ADD_CLICK_BACKGROUND_CLASS);

			// add a '#' tag reference to the unique element id
			var elementIdTag = STRING_TAG + self.getUniqueElementIdFromIndices(
					firstIndex, secondIndex, thirdIndex, fourthIndex, fifthIndex);

			// add 'add-click-background' class to current selected hierarchy level
			$(elementIdTag).addClass(ADD_CLICK_BACKGROUND_CLASS);
		};
		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param hierarchyLevel
		 * @returns {boolean}
		 */
		self.isHierarchyLevelCollapsed = function(hierarchyLevel){
			if(typeof hierarchyLevel.isCollapsed === UNDEFINED_TYPE){
				return true;
			}else {
				return hierarchyLevel.isCollapsed;
			}
		};
		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param hierarchyLevel
		 * @returns {boolean}
		 */
		self.changeExpandHierarchy = function(hierarchyLevel){
			if(hierarchyLevel.isCollapsed){
				hierarchyLevel.isCollapsed=false;
			} else {
				hierarchyLevel.isCollapsed=true;
			}
		};
		/**
		 * Method called when user selects a custom hierarchy level from one of the custom hierarchies.
		 *
		 * @param hierarchyContext The hierarchy context of the custom hierarchy.
		 * @param firstLevel The first level in the custom hierarchy.
		 * @param secondLevel The second level in the custom hierarchy.
		 * @param thirdLevel The third level in the custom hierarchy.
		 * @param fourthLevel The fourth level in the custom hierarchy.
		 * @param fifthLevel The fifth level in the custom hierarchy.
		 */
		self.selectCustomHierarchyLevel = function (hierarchyContext, firstLevel, secondLevel, thirdLevel, fourthLevel, fifthLevel) {
			// first level selected
			if(secondLevel === null){
				selectFirstHierarchyLevel(firstLevel);
				setFirstHierarchyLevel(firstLevel);
			}
			// second level selected
			else if(thirdLevel === null){
				selectSecondHierarchyLevel(secondLevel);
				setFirstHierarchyLevel(firstLevel);
				setSecondHierarchyLevel(secondLevel);
			}
			// third level selected
			else if(fourthLevel === null){
				selectThirdHierarchyLevel(thirdLevel);
				setFirstHierarchyLevel(firstLevel);
				setSecondHierarchyLevel(secondLevel);
				setThirdHierarchyLevel(thirdLevel);
			}
			// fourth level selected
			else if(fifthLevel === null){
				selectFourthHierarchyLevel(fourthLevel);
				setFirstHierarchyLevel(firstLevel);
				setSecondHierarchyLevel(secondLevel);
				setThirdHierarchyLevel(thirdLevel);
				setFourthHierarchyLevel(fourthLevel);
			}
			// fifth level selected
			else {
				selectFifthHierarchyLevel(fifthLevel);
				setFirstHierarchyLevel(firstLevel);
				setSecondHierarchyLevel(secondLevel);
				setThirdHierarchyLevel(thirdLevel);
				setFourthHierarchyLevel(fourthLevel);
				setFifthHierarchyLevel(fifthLevel);
			}
		};
		/**
		 * This method compares the original value of a custom hierarchy to the current selected value. This is done
		 * by comparing the keys in both. If they are equal, return true. Otherwise return false.
		 *
		 * @param originalLevel Original selected value.
		 * @param selectedLevel Current selected value.
		 * @returns {boolean} True if equal, false otherwise.
		 */
		function isOriginalHierarchyLevelEqualToSelectedLevel(originalLevel, selectedLevel){
			if(originalLevel &&
				originalLevel.key.childEntityId === selectedLevel.key.childEntityId &&
				originalLevel.key.parentEntityId === selectedLevel.key.parentEntityId &&
				originalLevel.key.hierarchyContext === selectedLevel.key.hierarchyContext){
				return true;
			} else {
				return false;
			}
		}

		/**
		 * This method sets values required when a fourth hierarchy level or lower is selected. This is the original
		 * value of the fifth hierarchy level.
		 *
		 * @param fifthLevel
		 */
		function setFifthHierarchyLevel(fifthLevel){
			self.originalFifthLevelSelected = fifthLevel;
		}

		/**
		 * This method sets values required when a fourth hierarchy level is selected. This is setting the selected
		 * hierarchy level as FIFTH_HIERARCHY_LEVEL.
		 *
		 * @param fifthLevel The fifth level in the custom hierarchy.
		 */
		function selectFifthHierarchyLevel(fifthLevel) {
			self.selectedHierarchyLevel = FIFTH_HIERARCHY_LEVEL;
		}

		/**
		 * This method sets values required when a fourth hierarchy level or lower is selected. This is the original
		 * value of the fourth hierarchy level.
		 *
		 * @param fourthLevel
		 */
		function setFourthHierarchyLevel(fourthLevel){
			self.originalFourthLevelSelected = fourthLevel;
		}

		/**
		 * This method sets values required when a fourth hierarchy level is selected. These include whether or not
		 * the fourth level is collapsed, and setting the selected hierarchy level as FOURTH_HIERARCHY_LEVEL.
		 *
		 * @param fourthLevel The fourth level in the custom hierarchy.
		 */
		function selectFourthHierarchyLevel(fourthLevel) {
			// if original fourth selected equals selected fourthLevel
			if(isOriginalHierarchyLevelEqualToSelectedLevel(self.originalFourthLevelSelected, fourthLevel)){
				// set isCollapsed to opposite of previous value
				fourthLevel.isCollapsed = !fourthLevel.isCollapsed;
			}else {
				// else set isCollapsed to false
				fourthLevel.isCollapsed = false;
			}
			self.selectedHierarchyLevel = FOURTH_HIERARCHY_LEVEL;
		}

		/**
		 * This method sets values required when a third hierarchy level or lower is selected. This is the original
		 * value of the third hierarchy level.
		 *
		 * @param thirdLevel
		 */
		function setThirdHierarchyLevel(thirdLevel){
			self.originalThirdLevelSelected = thirdLevel;
		}

		/**
		 * This method sets values required when a third hierarchy level is selected. These include whether or not
		 * the third level is collapsed, and setting the selected hierarchy level as THIRD_HIERARCHY_LEVEL.
		 *
		 * @param thirdLevel The third level in the custom hierarchy.
		 */
		function selectThirdHierarchyLevel(thirdLevel) {
			// if original third selected equals selected thirdLevel
			if(isOriginalHierarchyLevelEqualToSelectedLevel(self.originalThirdLevelSelected, thirdLevel)){
				// set isCollapsed to opposite of previous value
				thirdLevel.isCollapsed = !thirdLevel.isCollapsed;
			}else {
				// else set isCollapsed to false
				thirdLevel.isCollapsed = false;
			}
			self.selectedHierarchyLevel = THIRD_HIERARCHY_LEVEL;
		}

		/**
		 * This method sets values required when a second hierarchy level or lower is selected. This is the original
		 * value of the second hierarchy level.
		 *
		 * @param secondLevel
		 */
		function setSecondHierarchyLevel(secondLevel){
			self.originalSecondLevelSelected = secondLevel;
		}

		/**
		 * This method sets values required when a second hierarchy level is selected. These include whether or not
		 * the second level is collapsed, and setting the selected hierarchy level as SECOND_HIERARCHY_LEVEL.
		 *
		 * @param secondLevel The second level in the custom hierarchy.
		 */
		function selectSecondHierarchyLevel(secondLevel) {
			// if original second selected equals selected secondLevel
			if(isOriginalHierarchyLevelEqualToSelectedLevel(self.originalSecondLevelSelected, secondLevel)){
				// set isCollapsed to opposite of previous value
				secondLevel.isCollapsed = !secondLevel.isCollapsed;
			}else {
				// else set isCollapsed to false
				secondLevel.isCollapsed = false;
			}
			self.selectedHierarchyLevel = SECOND_HIERARCHY_LEVEL;
		}

		/**
		 * This method sets values required when a first hierarchy level or lower is selected. This is the original
		 * value of the first hierarchy level.
		 *
		 * @param firstLevel
		 */
		function setFirstHierarchyLevel(firstLevel){
			self.originalFirstLevelSelected = firstLevel;
		}

		/**
		 * This method sets values required when a first hierarchy level is selected. These include whether or not
		 * the first level is collapsed, and setting the selected hierarchy level as FIRST_HIERARCHY_LEVEL.
		 *
		 * @param firstLevel The first level in the custom hierarchy.
		 */
		function selectFirstHierarchyLevel(firstLevel) {
			// if original first selected equals selected firstLevel
			if(isOriginalHierarchyLevelEqualToSelectedLevel(self.originalFirstLevelSelected, firstLevel)){
				// set isCollapsed to opposite of previous value
				firstLevel.isCollapsed = !firstLevel.isCollapsed;
			}else {
				// else set isCollapsed to false
				firstLevel.isCollapsed = false;
			}
			self.selectedHierarchyLevel = FIRST_HIERARCHY_LEVEL;
		}
		/**
		 * This method returns the string index of a given hierarchy level, which includes the string representation
		 * of the level (i.e. 'firstLevel') and the index of the current level and all indices of parent levels
		 * separated by a hyphen. The parent level indices and hyphen are required to guarantee a unique id per visible
		 * hierarchy level.
		 *
		 * @param firstIndex The index of the first level in the custom hierarchy.
		 * @param secondIndex The index of the second level in the custom hierarchy.
		 * @param thirdIndex The index of the third level in the custom hierarchy.
		 * @param fourthIndex The index of the fourth level in the custom hierarchy.
		 * @param fifthIndex The index of the fifth level in the custom hierarchy.
		 * @returns {string} Unique index for a hierarchy level.
		 */
		self.getUniqueElementIdFromIndices = function(firstIndex, secondIndex, thirdIndex, fourthIndex, fifthIndex) {
			var uniqueId;
			// first level
			if(secondIndex === null){
				uniqueId = FIRST_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex;
			}
			// second level
			else if(thirdIndex === null){
				uniqueId = SECOND_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex + STRING_HYPHEN + secondIndex;
			}
			// third level
			else if(fourthIndex === null){
				uniqueId = THIRD_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex + STRING_HYPHEN + secondIndex
					+ STRING_HYPHEN + thirdIndex;
			}
			// fourth level
			else if(fifthIndex === null){
				uniqueId = FOURTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex + STRING_HYPHEN + secondIndex
					+ STRING_HYPHEN + thirdIndex + STRING_HYPHEN + fourthIndex;
			}
			// fifth level
			else {
				uniqueId = FIFTH_HIERARCHY_LEVEL_ELEMENT_ID_PREFIX + firstIndex + STRING_HYPHEN + secondIndex
					+ STRING_HYPHEN + thirdIndex + STRING_HYPHEN + fourthIndex + STRING_HYPHEN + fifthIndex;
			}
			return uniqueId;
		};
		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param relationship
		 * @returns {boolean}
		 */
		self.isLowestBranchOrHasNoRelationships = function(relationship){
			if(relationship.lowestBranch){
				return true;
			}else if(relationship.childRelationships === null || relationship.childRelationships.length === 0) {
				return true;
			}
			return false;
		};
		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param relationship
		 * @returns {boolean}
		 */
		self.isChildRelationshipOfProductEntityType = function(relationship){
			return relationship.lowestLevel;
		};

		/**
		 * get Customer Hierarchy.
		 */
		self.getCustomHierarchy = function () {
			self.customerHierarchyAssigment = {};
			self.hierarchyContext = {};
			self.hierarchyContextLowestLevel = [];
			self.cleanMessage();
			self.isWaitingForResponse = true;
			productGroupApi.getCustomerHierarchy(
				{
					hierachyContext: self.currentTab.hierCntxtCd
				},
				//success case
				function (results) {
					self.customerHierarchyAssigment = angular.copy(results);
					self.getCustomHierarchyAssignment();
				}
				//error case
				, self.fetchError
			);
		}
		/**
		 * get Customer Hierarchy Assigment.
		 */
		self.getCustomHierarchyAssignment = function () {
			self.customerHierarchyAssigment.productId = self.productGroupIdSelected;
			self.customerHierarchyAssigment.hierachyContextCode = self.currentTab.hierCntxtCd;
			productGroupApi.getCustomerHierarchyAssignment(
				self.customerHierarchyAssigment,
				//success case
				function (results) {
					self.hierarchyContext = angular.copy(results.customerHierarchyContext);
					if(!self.addNewMode) {
						self.hierarchyContextCurrentPath = angular.copy(results.hierarchyContextCurrentPath);
					self.customerHierarchyAssigment.hierarchyContextCurrentPath = angular.copy(results.hierarchyContextCurrentPath);
					}else{
						self.customerHierarchyAssigment.hierarchyContextCurrentPath = angular.copy(self.hierarchyContextCurrentPath);
					}
					self.hierarchyContextLowestLevel = angular.copy(results.lowestLevel);
					self.isWaitingForResponse = false;
					self.loadCustomHierarchy();
				}
				//error case
				,self.fetchError
			);
		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.confirmAction ="";
			self.isWaitingForResponse = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = (error.data.message);
				} else if (error.data.error) {
					self.error = (error.data.error);
				} else {
					self.error = error;
				}
			}
			else {
				self.error = "An unknown error occurred.";

			}
		}
		/**
		 * remove path from HierarchyContext Current.
		 *
		 * @param currentPath The path is selected.
		 */
		self.removeHierarchyContextCurrentPath = function(currentPath){
			self.cleanMessage();
			if(self.hierarchyContextCurrentPath!=null && self.hierarchyContextCurrentPath.length!=0) {
				var hierarchyContextCurrentPathTemp = [];
				for(var i = self.hierarchyContextCurrentPath.length - 1; i >= 0; i--){
					if(self.addNewMode){
						if(self.hierarchyContextCurrentPath[i].key.childEntityId != currentPath.key.childEntityId){
							hierarchyContextCurrentPathTemp.push(self.hierarchyContextCurrentPath[i]);
						}
					}else{
					if(self.hierarchyContextCurrentPath[i].key.childEntityId == currentPath.key.childEntityId){
							self.hierarchyContextCurrentPath[i].action = ACTION_CD_DELETE;
					}
				}
			}
				if(self.addNewMode){
					self.hierarchyContextCurrentPath = hierarchyContextCurrentPathTemp;
				}
			}
		};
		/**
		 * add HierarchyContext Current from Hierarchy.
		 *
		 * @param currentPath The path is selected.
		 */
		self.addHierarchyContextCurrentPathRootHierarchy = function(currentPath){
			self.cleanMessage();
			var flagExist = false;
			var flagCurrentExist = false;
			var flagPrimary = false;
			if(self.hierarchyContextCurrentPath!=null && self.hierarchyContextCurrentPath.length!=0){
				angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
					if(contextCurrentPath.path==currentPath.path){
						flagExist= true;
					}
					if(contextCurrentPath.action!=ACTION_CD_DELETE){
						flagCurrentExist = true;
					}
				});
			}
			if(!flagExist){
				if(self.hierarchyContextLowestLevel!=null && self.hierarchyContextLowestLevel.length!=0) {
					angular.forEach(self.hierarchyContextLowestLevel, function (filterObj, filterKey) {
						if (currentPath.key.childEntityId == filterKey) {
							angular.forEach(filterObj, function (lowestLevel) {
								var currentPathAdd = angular.copy(lowestLevel);
								currentPathAdd.action = ACTION_CD_ADD;
								//currentPathAdd.suggestHierarchy = false;
								currentPathAdd.productId = self.productGroupIdSelected;
								if(!flagCurrentExist && !flagPrimary && currentPathAdd.allowPrimaryPath==true){
									flagPrimary = true;
									currentPathAdd.defaultParentValue = self.primaryList[0];
									self.hierarchyContextCurrentPath.splice(0, 0, currentPathAdd);
								} else {
									currentPathAdd.defaultParentValue = self.primaryList[1];
									self.hierarchyContextCurrentPath.push(currentPathAdd);
								}
							});
						}
					});
				}
			} else {
				self.styleDisplay2Modal = { 'z-index': 1040 };
				$('#customerProductGroupHierarchyAssigmentModel').modal({backdrop: 'static', keyboard: false});
				self.confirmTitle = "Task Message";
				self.confirmMessage = "This Hierarchy path is existed in Current Hirarchy, please choose another Hierarchy path to add .";
				$('#alertModal').modal({backdrop: 'static', keyboard: true});
			}

		};
		/**
		 * Close popup warning
		 */
		self.closePopupWarning = function () {
			$('#alertModal').modal("hide");
			self.styleDisplay2Modal = { 'z-index': 1050 };
			$('#customerProductGroupHierarchyAssigmentModel').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * Add new choice type handle when add new button click.
		 */
		self.showHierarchyAssignment = function () {
			if(self.addNewMode){
				self.saveLabel = "Add";
				self.hierarchyContextCurrentPath = [];
				if(self.selectedHierarchyContexts!=null) {
					self.hierarchyContextCurrentPath = angular.copy(self.selectedHierarchyContexts);
				}
			}else{
				self.saveLabel = "Save";
			}
			self.styleDisplay2Modal = { 'z-index': 1050 };
			$('#customerProductGroupHierarchyAssigmentModel').modal({backdrop: 'static', keyboard: true});
			self.getCustomHierarchy();
		}
		/**
		 * Show popup message confirm when close pop during data has been changed.
		 */
		self.closePopupHandle = function () {
			self.cleanMessage();
			if(self.isChangedData()){
				self.confirmTitle = "Confirmation";
				self.confirmMessage = "Unsaved data will be lost. Do you want to save the changes before continuing?";
				self.confirmAction = "SAVE";
				$('#customerProductGroupHierarchyAssigmentModel').modal({backdrop: 'static', keyboard: false});
				self.styleDisplay2Modal = { 'z-index': 1040 };
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			}else{
				$('#customerProductGroupHierarchyAssigmentModel').modal("hide");
			}
		}
		/**
		 * Confirmed to delete/update change data
		 */
		self.confirmActionHandle = function () {
			self.confirmAction="SAVE";
			self.closeConfirmPopupHandle();
			self.saveData();
		}
		/**
		 * No confirm to delete/update change data
		 */
		self.noConfirmActionHandle = function () {
			self.closeConfirmPopupHandle();
			$('#customerProductGroupHierarchyAssigmentModel').modal("hide");
		}
		/**
		 * No confirm to delete/update change data
		 */
		self.closeConfirmPopupHandle = function () {
			self.styleDisplay2Modal = { 'z-index': 1050 };
			self.confirmAction="";
			$('#confirmModal').modal("hide");
			$('#confirmModal').on('hidden.bs.modal', function () {
				angular.element(document.body).addClass("modal-open");
			});
		}
		/**
		 * Check has change data
		 */
		self.isChangedData = function () {
			self.cleanMessage();
			var isChanged =  false;
			if (self.addNewMode) {
				// Check data change on add new mode.
				if(self.customerHierarchyAssigment.hierarchyContextCurrentPath != null && self.customerHierarchyAssigment.hierarchyContextCurrentPath != undefined &&
					self.customerHierarchyAssigment.hierarchyContextCurrentPath.length > 0){
					if(self.hierarchyContextCurrentPath != null && self.hierarchyContextCurrentPath.length > 0){
						for (var i = 0; i < self.hierarchyContextCurrentPath.length; i++) {
							var isExisted = false;
							for (var j = 0; j < self.customerHierarchyAssigment.hierarchyContextCurrentPath.length; j++) {
								if (self.hierarchyContextCurrentPath[i].path == self.customerHierarchyAssigment.hierarchyContextCurrentPath[j].path &&
									self.hierarchyContextCurrentPath[i].defaultParentValue == self.customerHierarchyAssigment.hierarchyContextCurrentPath[j].defaultParentValue) {
									isExisted = true;
									break;
								}
							}
							if (!isExisted) {
								isChanged = true;
								break;
							}
						}
					}else{
						isChanged = true;
					}
				}else{
					if(self.hierarchyContextCurrentPath!=null && self.hierarchyContextCurrentPath.length>0){
						isChanged = true;
					}
				}
			} else {
				// Change data change on edit mode.
				if (self.hierarchyContextCurrentPath != null && self.hierarchyContextCurrentPath.length != 0) {
				angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
						if(!isChanged) {
					if ($.trim(contextCurrentPath.action) == ACTION_CD_ADD) {
								isChanged = true;
					}
							else if (self.customerHierarchyAssigment.hierarchyContextCurrentPath != null && self.customerHierarchyAssigment.hierarchyContextCurrentPath.length != 0) {
						angular.forEach(self.customerHierarchyAssigment.hierarchyContextCurrentPath, function (contextCurrentPathOri) {
									if(!isChanged) {
							if (contextCurrentPath.path == contextCurrentPathOri.path) {
											if ($.trim(contextCurrentPath.action) == ACTION_CD_DELETE || contextCurrentPath.defaultParentValue != contextCurrentPathOri.defaultParentValue) {
												isChanged = true;
									}
								}
							}
						});
					}
						}
				});
			}
			}
			return isChanged;
		}
		/**
		 * get data change
		 * @returns {Array}
		 */
		self.getDataChange = function () {
			self.cleanMessage();
			var lstChange = [];
			var countPrimaryPath = 0;
			var flagchange =false;
			var flagChangeEditAdd =false;
			if(self.hierarchyContextCurrentPath!=null && self.hierarchyContextCurrentPath.length!=0) {
				angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
					if ($.trim(contextCurrentPath.action) == ACTION_CD_ADD) {
						if (contextCurrentPath.defaultParentValue == self.primaryList[0]) {
							countPrimaryPath = countPrimaryPath + 1;
						}
						flagChangeEditAdd =true;
						lstChange.push(contextCurrentPath);
					}
					else if(self.customerHierarchyAssigment.hierarchyContextCurrentPath!=null && self.customerHierarchyAssigment.hierarchyContextCurrentPath!=0) {
						flagchange = false;
						angular.forEach(self.customerHierarchyAssigment.hierarchyContextCurrentPath, function (contextCurrentPathOri) {
							if (contextCurrentPath.path == contextCurrentPathOri.path) {
								if ($.trim(contextCurrentPath.action) == ACTION_CD_DELETE){
									contextCurrentPath.defaultParentValue=contextCurrentPathOri.defaultParentValue;
									lstChange.push(contextCurrentPath);
								}if (contextCurrentPath.defaultParentValue != contextCurrentPathOri.defaultParentValue) {
									flagChangeEditAdd =true;
									if (contextCurrentPath.defaultParentValue == self.primaryList[0]) {
										countPrimaryPath = countPrimaryPath + 1;
										contextCurrentPath.action = ACTION_CD_PRIMARY;
									} else {
										contextCurrentPath.action = ACTION_CD_ALTERNATE;
									}
									flagchange = true;
									lstChange.push(contextCurrentPath);
								}
							}
						});
						if (!flagchange && contextCurrentPath.defaultParentValue == self.primaryList[0] && $.trim(contextCurrentPath.action) != ACTION_CD_DELETE) {
							countPrimaryPath = countPrimaryPath + 1;
						}
					}
				});
			}
			if(lstChange.length!=0){
				var deleteAll = true;
				var errorDelete = false;
				angular.forEach(lstChange, function (contextCurrentPath) { 
					if($.trim(contextCurrentPath.action) != ACTION_CD_DELETE){
						deleteAll = false;
					}
				});
				if(deleteAll){
					if(self.productAssociated != null && self.productAssociated != undefined){
						angular.forEach(self.productAssociated.rows, function (row) { 
							if(row.productId != null && row.productId != undefined){
								errorDelete = true;
							}
						});
					}
				}
				if(errorDelete){
					self.error='- Please remove products from product group to unassign customer hierarchy.';
					lstChange = [];
				}
			}
			if(countPrimaryPath == 0 && flagChangeEditAdd && lstChange.length!=0){
				self.error='- A Product must be one Primary Paths.';
				lstChange = [];
			} else if(countPrimaryPath >1){
				self.error='- A Product cannot have more than one Primary Paths.';
				lstChange = [];
			}
			return lstChange;
		}
		/**
		 * Save
		 * @returns
		 */
		self.saveData = function () {
			var lstChange = self.getDataChange();
			if(lstChange.length==0 ){
				if(self.error==''){
					self.error='There are no changes on this page to be saved. Please make any change to update.';
				}
			}else if(self.addNewMode){
				//set primary path
				self.customerHierarchyPrimaryPath = '';
				self.selectedHierarchyContexts = angular.copy(self.hierarchyContextCurrentPath);
				angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
					if (contextCurrentPath.defaultParentValue == self.primaryList[0]){
						self.customerHierarchyPrimaryPath = contextCurrentPath.path;
					}
				});
				$('#customerProductGroupHierarchyAssigmentModel').modal("hide");
				$rootScope.$broadcast('addHierarchyOnAddNewMode',lstChange);
			} else {
				self.isWaitingForResponse = true;
				productGroupApi.saveCustomerHierarchyAssignment(
					lstChange,
					//success case
					function (result) {
						self.success = result.message;
						self.resetDataEdit();
						self.customerHierarchyAssigment.hierarchyContextCurrentPath = angular.copy(self.hierarchyContextCurrentPath);
						$rootScope.$broadcast(CLEAR_MESSAGE);
						//set primary path
						self.customerHierarchyPrimaryPath = '';
						angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
								if (contextCurrentPath.defaultParentValue == self.primaryList[0]){
									self.customerHierarchyPrimaryPath = contextCurrentPath.path;
								}

						});
						self.isWaitingForResponse = false;
						if(self.confirmAction=="SAVE"){
							self.noConfirmActionHandle();
							self.confirmAction ="";
						}
					}
					//error case
					,self.fetchError
				);
			}
		}
		/**
		 * Reset data
		 */
		self.resetData = function () {
			self.hierarchyContextCurrentPath = angular.copy(self.customerHierarchyAssigment.hierarchyContextCurrentPath);
			self.cleanMessage();
		}
		/**
		 * Clean message
		 */
		self.cleanMessage = function () {
			self.error='';
			self.success='';
		}
		/**
		 * Reset data edit
		 */
		self.resetDataEdit = function () {
			var flag = true;
			angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
				if ($.trim(contextCurrentPath.action) != ACTION_CD_DELETE){
					flag = false;
					contextCurrentPath.action='';
				}
			});
			if(flag){
				self.hierarchyContextCurrentPath = [];
			} else {
				for(var i = self.hierarchyContextCurrentPath.length - 1; i >= 0; i--){
					if(self.hierarchyContextCurrentPath[i].action== ACTION_CD_DELETE){
						self.hierarchyContextCurrentPath.splice(i, 1);
					}
				}
			}
		}
	}
})();
