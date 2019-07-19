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
 * @author vn00602
 * @since 2.14.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('eCommerceViewHierachyAssigement', {
		scope: '=',
		bindings: {
			currentTab: '<',
			productMaster: '<',
			eCommerceViewDetails: '=',
			validationECommerceView:'&',
			hierarchyPathLimit: '<'
		},
		// Inline template which is bind to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewHierachyAssigement.html',
		// The controller that handles our component logic
		controller: HierachyAssigementController
	});

	HierachyAssigementController.$inject = ['$scope', 'ECommerceViewApi', '$sce', 'imageInfoService','$timeout', '$rootScope'];

	/**
	 * ECommerce View Directions component's controller definition.
	 *
	 * @param $scope scope of the eCommerceView component.
	 * @param eCommerceViewApi the api of eCommerceView.
	 * @constructor
	 */
	function HierachyAssigementController($scope, eCommerceViewApi, $sce,imageInfoService, $timeout, $rootScope) {
		/**
		 * All CRUD operation controls of choice option page goes here.
		 */
		var self = this;
		// self.data = null;
		// self.originalData = null;
		self.success = null;
		self.error = null;
		// self.hierarchyContexts = null;
		// self.hierarchyContextSelected = null;
		// self.singleHierarchyContextSelected = null;
		self.firstLevelSelected = null;
		self.secondLevelSelected = null;
		self.thirdLevelSelected = null;
		self.fourthLevelSelected = null;
		self.fifthLevelSelected = null;
		self.originalFirstLevelSelected = null;
		self.originalSecondLevelSelected = null;
		self.originalThirdLevelSelected = null;
		self.originalFourthLevelSelected = null;
		self.originalFifthLevelSelected = null;
		self.searchingForCustomHierarchyText = null;
		self.customHierarchySearchText = null;
		self.searchingForCustomHierarchy = false;
		self.isWaitingForHierarchyContextResponse = false;
		self.hierarchyLevelSelected = null;

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
		var ACTION_CD_ALTERNATE="EA";
        /**
		 * save data from ecommerce view hierarchy assignment.
         * @type {string}
         */
        self.SAVE_DATA_ECOMMERCE = "saveDataEcommerce";
		/**
		 * Clear message key.
		 * @type {string}
		 */
		self.CLEAR_MESSAGE = 'clearMessage';
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
		self.hierarchyContextSuggestPath = [];
		/**
		 * General eCommerce View Information
		 * @type {{}}
		 */
		self.hierarchyContextLowestLevel = [];
		self.primaryList = ["Primary","Alternate"];
		/**
		 *Style for 2 modal.
		 * @type {Object}
		 */
		self.styleDisplay2Modal = { 'z-index': 1050 };
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
			if(typeof hierarchyLevel !== UNDEFINED_TYPE && hierarchyLevel.childRelationships !== null){
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

			// self.singleHierarchyContextSelected = hierarchyContext;

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
			// if(relationship.childRelationships === null || relationship.countOfProductChildren > 0){
			// 	return true;
			// }
			return relationship.lowestLevel;
		};

		/**
		 * get Customer Hierarchy.
		 */
		self.getCustomHierarchy = function () {
			self.customerHierarchyAssigment = {};
			self.hierarchyContext = {};
			self.hierarchyContextCurrentPath = [];
			self.hierarchyContextSuggestPath = [];
			self.hierarchyContextLowestLevel = [];
			self.cleanMessage();
			self.isWaitingForResponse = true;
			self.getCustomHierarchyAssigment();
			// eCommerceViewApi.getCustomHierarchy(
			// 	{
			// 		hierachyContext: self.currentTab.hierCntxtCd
			// 	},
			// 	//success case
			// 	function (results) {
			// 		self.customerHierarchyAssigment = angular.copy(results);
			// 		// self.hierarchyContext = angular.copy(results.customerHierarchyContext);
			// 		self.getCustomHierarchyAssigment();
			// 	}
			// 		//error case
			// 	, self.fetchError
			// );
		}
		/**
		 * get Customer Hierarchy Assigment.
		 */
		self.getCustomHierarchyAssigment = function () {
			eCommerceViewApi.getCustomHierarchyAssigment({
					productId :self.productMaster.prodId,
					upc : self.productMaster.productPrimaryScanCodeId,
					subCommodity : self.productMaster.subCommodityCode,
					hierachyContextCode : self.currentTab.hierCntxtCd,
					entyId : 0,
					commodityCode : self.productMaster.commodityCode,
					classCode : self.productMaster.classCode
				},
				//success case
				function (results) {
					self.hierarchyContext = angular.copy(results.customerHierarchyContext);
					self.customerHierarchyAssigment.hierarchyContextCurrentPath = angular.copy(results.hierarchyContextCurrentPath);
					self.hierarchyContextCurrentPath = angular.copy(results.hierarchyContextCurrentPath);
					self.customerHierarchyAssigment.hierarchyContextSuggestPath = angular.copy(results.hierarchyContextSuggestPath);
					self.hierarchyContextSuggestPath = angular.copy(results.hierarchyContextSuggestPath);
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
				for(var i = self.hierarchyContextCurrentPath.length - 1; i >= 0; i--){
					if(self.hierarchyContextCurrentPath[i].key.childEntityId == currentPath.key.childEntityId){
						if (self.hierarchyContextCurrentPath[i].suggestHierarchy == true) {
							self.hierarchyContextCurrentPath[i].action = ACTION_CD_DELETE;
							self.hierarchyContextSuggestPath.push(self.hierarchyContextCurrentPath[i]);
						} else {
							currentPath.defaultParentValue = self.primaryList[1];
							self.hierarchyContextCurrentPath.splice(i, 1);
						}
					}
				}
			}
		};
		/**
		 * add HierarchyContext Current from Suggest Path.
		 *
		 * @param currentPath The path is selected.
		 */
		self.addHierarchyContextCurrentPath = function(currentPath){
			self.cleanMessage();
			var flagExist = false;
			var flagCurrentExist = false;
			var flagPrimary = false;
			if(self.hierarchyContextCurrentPath!=null && self.hierarchyContextCurrentPath.length!=0){
				flagCurrentExist = true;
			}
			angular.forEach(self.hierarchyContextSuggestPath, function (contextSuggestPath) {
				if (contextSuggestPath.key.childEntityId == currentPath.key.childEntityId) {
					flagExist = false;
					angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
						if(contextSuggestPath.path == contextCurrentPath.path){
							flagExist = true;
							contextCurrentPath.action = '';
						}
					});
					if(!flagExist){
						var currentPathAdd = angular.copy(contextSuggestPath);
						currentPathAdd.action = ACTION_CD_ADD;
						if(!flagCurrentExist && !flagPrimary && currentPathAdd.allowPrimaryPath==true) {
							flagPrimary = true;
							currentPathAdd.defaultParentValue = self.primaryList[0];
							self.hierarchyContextCurrentPath.splice(0, 0, currentPathAdd);
						} else {
							self.hierarchyContextCurrentPath.push(angular.copy(currentPathAdd));
						}
					}
				};
			});
			// if(self.hierarchyContextCurrentPath.length==1 && self.hierarchyContextCurrentPath[0].allowPrimaryPath==true){
			// 	self.hierarchyContextCurrentPath[0].defaultParentValue = self.primaryList[0];
			// }
			for(var i = self.hierarchyContextSuggestPath.length - 1; i >= 0; i--){
				if( self.hierarchyContextSuggestPath[i].key.childEntityId == currentPath.key.childEntityId){
					self.hierarchyContextSuggestPath.splice(i, 1);
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
								currentPathAdd.suggestHierarchy = false;
								currentPathAdd.productId = self.productMaster.prodId;
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
				// if(self.hierarchyContextCurrentPath.length==1 && self.hierarchyContextCurrentPath[0].allowPrimaryPath==true){
				// 	self.hierarchyContextCurrentPath[0].defaultParentValue = self.primaryList[0];
				// }
			} else {
				self.styleDisplay2Modal = { 'z-index': 1040 };
				$('#customerHierarchyAssigmentModel').modal({backdrop: 'static', keyboard: false});
				self.confirmTitle = "Task Message";
				self.confirmMessage = "This Hierarchy path is existed in Current Hirarchy, please choose another Hierarchy path to add .";
				$('#alertModal').modal({backdrop: 'static', keyboard: true});
			}

		};
		self.closePopupWarning = function () {
			$('#alertModal').modal("hide");
			self.styleDisplay2Modal = { 'z-index': 1050 };
			$('#customerHierarchyAssigmentModel').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * Add new choice type handle when add new button click.
		 */
		self.showHierachyAssigement = function () {
			self.styleDisplay2Modal = { 'z-index': 1050 };
			$('#customerHierarchyAssigmentModel').modal({backdrop: 'static', keyboard: true});
			self.getCustomHierarchy();
		}
		/**
		 * Show popup message confirm when close pop during data has been changed.
		 */
		self.closePopupHandle = function () {
			self.cleanMessage();
			if(self.checkchange().length!==0){
				self.confirmTitle = "Confirmation";
				self.confirmMessage = "Unsaved data will be lost. Do you want to save the changes before continuing?";
				self.confirmAction = "SAVE";
				$('#customerHierarchyAssigmentModel').modal({backdrop: 'static', keyboard: false});
				self.styleDisplay2Modal = { 'z-index': 1040 };
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			}else{
				$('#customerHierarchyAssigmentModel').modal("hide");
			}
		}
		/**
		 * Confirmed to delete/update change data
		 */
		self.confirmActionHandle = function () {
			self.confirmAction=="SAVE";
			self.saveData();
		}
		/**
		 * No confirm to delete/update change data
		 */
		self.noConfirmActionHandle = function () {
			self.styleDisplay2Modal = { 'z-index': 1050 };
			self.confirmAction=="";
			$('#confirmModal').modal("hide");
			$('#customerHierarchyAssigmentModel').modal("hide");
		}
		/**
		 * No confirm to delete/update change data
		 */
		self.closeConfirmPopupHandle = function () {
			self.styleDisplay2Modal = { 'z-index': 1050 };
			self.confirmAction=="";
			$('#confirmModal').modal("hide");
		}

		self.checkchange = function () {
			self.cleanMessage();
			var lstChange = [];
			if(self.hierarchyContextCurrentPath!=null && self.hierarchyContextCurrentPath.length!=0) {
				angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
					if ($.trim(contextCurrentPath.action) == ACTION_CD_ADD) {
						lstChange.push(contextCurrentPath);
					}
					else if(self.customerHierarchyAssigment.hierarchyContextCurrentPath!=null && self.customerHierarchyAssigment.hierarchyContextCurrentPath.length!=0) {
						angular.forEach(self.customerHierarchyAssigment.hierarchyContextCurrentPath, function (contextCurrentPathOri) {
							if (contextCurrentPath.path == contextCurrentPathOri.path) {
							//if (contextCurrentPath.key.parentEntityId == contextCurrentPathOri.key.parentEntityId && contextCurrentPath.key.childEntityId == contextCurrentPathOri.key.childEntityId) {
								if ($.trim(contextCurrentPath.action) == ACTION_CD_DELETE){
									lstChange.push(contextCurrentPath);
								} if (contextCurrentPath.defaultParentValue != contextCurrentPathOri.defaultParentValue) {
									if (contextCurrentPath.defaultParentValue == self.primaryList[0]) {
										contextCurrentPath.action = ACTION_CD_PRIMARY;
									} else {
										contextCurrentPath.action = ACTION_CD_ALTERNATE;
									}
									lstChange.push(contextCurrentPath);
								}
							}
						});
					}
				});
			}
			return lstChange;
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
			var flagchangeEditAdd =false;
			if(self.hierarchyContextCurrentPath!=null && self.hierarchyContextCurrentPath.length!=0) {
				angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
					if ($.trim(contextCurrentPath.action) == ACTION_CD_ADD) {
						if (contextCurrentPath.defaultParentValue == self.primaryList[0]) {
							countPrimaryPath = countPrimaryPath + 1;
						}
						flagchangeEditAdd =true;
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
									flagchangeEditAdd =true;
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
			if(countPrimaryPath == 0 && flagchangeEditAdd && lstChange.length!=0){
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
			} else {
				self.isWaitingForResponse = true;
				eCommerceViewApi.saveCustomHierarchyAssigment(
					lstChange,
					//success case
					function (result) {
						self.success = result.message;
						self.resetDataEdit();
						self.customerHierarchyAssigment.hierarchyContextCurrentPath = angular.copy(self.hierarchyContextCurrentPath);
						self.customerHierarchyAssigment.hierarchyContextSuggestPath = angular.copy(self.hierarchyContextSuggestPath);
						self.validationECommerceView();
                        $rootScope.$emit(self.SAVE_DATA_ECOMMERCE);
						// Clear the message on eCommerce View.
						$rootScope.$broadcast(self.CLEAR_MESSAGE);
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
		self.resetData = function () {
			self.hierarchyContextCurrentPath = angular.copy(self.customerHierarchyAssigment.hierarchyContextCurrentPath);
			self.hierarchyContextSuggestPath = angular.copy(self.customerHierarchyAssigment.hierarchyContextSuggestPath );
			self.cleanMessage();
		}
		self.cleanMessage = function () {
			self.error='';
			self.success='';
		}
		self.resetDataEdit = function () {
			var flag = true;
			angular.forEach(self.hierarchyContextCurrentPath, function (contextCurrentPath) {
				if ($.trim(contextCurrentPath.action) != ACTION_CD_DELETE){
					flag = false;
					if (self.eCommerceViewDetails && contextCurrentPath.defaultParentValue == self.primaryList[0]){
						self.eCommerceViewDetails.customerHierarchyPrimaryPath = contextCurrentPath.path;
					}
					contextCurrentPath.suggestHierarchy = true;
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
			angular.forEach(self.hierarchyContextSuggestPath, function (contextSuggestPath) {
				contextSuggestPath.action= '';
			});

			if(self.eCommerceViewDetails && self.hierarchyContextCurrentPath.length==0){
				self.eCommerceViewDetails.customerHierarchyPrimaryPath = '';
			}
		}

		/**
		 * Checks if the user can add a hierarchy Path based on the hierarchy Path Limit
		 *
		 * @returns {boolean}
		 */
		self.isMaxHierarchyPathLimit = function () {

			if(!self.hierarchyPathLimit || this.findPathWithoutActionCodeD().length < self.hierarchyPathLimit){
				return false;
			}
			else{
				return true;
			}
		};

		/**
		 * Returns all the paths in hierarchyContextCurrentPath that do not have the action code D
		 *
		 * @returns {Array}
		 */
		self.findPathWithoutActionCodeD = function () {
			var toReturn = [];
			for(var index=0; index < self.hierarchyContextCurrentPath.length; index++){
				if(self.hierarchyContextCurrentPath[index].action !== 'D'){
					toReturn.push(self.hierarchyContextCurrentPath[index]);
				}
			}
			return toReturn;
		};

		/**
		 * Checks if there is only one hierarchyContextCurrentPath, then the primary path cannot be changed to alternate.
		 *
		 * @returns {boolean}
		 */
		self.canUserChangePrimaryPath = function () {
			if(self.hierarchyContextCurrentPath.length === 1){
				return true;
			}
			else {
				return false;
			}
		}

	}
})();
