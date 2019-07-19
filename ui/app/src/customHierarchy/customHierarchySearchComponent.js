/*
 * customHierarchySearchComponent.js
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Custom Hierarchy -> Search component.
 *
 * @author m314029
 * @since 2.25.0
 */
(function() {
	angular.module('productMaintenanceUiApp').component('customHierarchySearch', {
		// isolated scope binding
		bindings: {
			onSelect: '&',
			onCustomHierarchyPage: '<'
		},
		scope: {},
		templateUrl: 'src/customHierarchy/customHierarchySearch.html',

		// The controller that handles our component logic
		controller: CustomHierarchySearchController
	});

	CustomHierarchySearchController.$inject = ['CustomHierarchyApi', '$stateParams', 'ProductSearchService', 'customHierarchyService', '$timeout', '$scope', '$rootScope'];

	function CustomHierarchySearchController(customHierarchyApi, $stateParams, productSearchService, customHierarchyService, $timeout, $scope, $rootScope) {

		var self = this;

		self.entityIdReceived = null;
		self.customHierarchySearchTitle = "Please enter name of custom hierarchy level to search for.";
		self.customHierarchySearchButtonTitle = "Search for entered text within custom hierarchy.";
		self.customHierarchySearchClearButtonTitle = "Clear search for custom hierarchy.";
		self.searchingForCustomHierarchyTextTemplate = "Searching $hierarchyContextDescription custom hierarchy " +
			"for: $searchText. Please wait...";
		self.customHierarchySearchTitleError = "Please select custom hierarchy(s) to search for first.";
		self.selectedLevel = {};
		self.NO_RESULTS_FOUND_MESSAGE = "No results found.";

		var UNDEFINED_TYPE = 'undefined';
		var UNKNOWN_ERROR = "An unknown error occurred.";
		var HIERARCHY_CONTEXT_ALL = {id: "ALL", description: "All", parentEntityId: 0};
		var DEFAULT_HIERARCHY_CODE_FROM_IMAGE="CUST";
		var CURRENT_HIERARCHY_CODE = null;
		var BASE_ENTITY_ID=null;
		var UPDATE_SELECTED_HIERARCHY_MESSAGE = "UPDATE_SELECTED_HIERARCHY";
		var currentRequest = 0;
		var SET_HIERARCHY_TO_SELECTED_LEVEL = "SET_HIERARCHY_TO_SELECTED_LEVEL";
		var SET_HIERARCHY_TO_SELECTED_LEVEL_AND_UPDATE = "SET_HIERARCHY_TO_SELECTED_LEVEL_AND_UPDATE";
		var IMAGES_TAB = "IMAGES_TAB";
		var DEFAULT_SUCCESS_MESSAGE = "Successfully added hierarchy context.";
		self.returnToList = false;
		self.selectedPathCustomHierarchy = null;
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the controller is initialized
		 * (or re-initialized).
		 */
		self.$onInit = function () {
			self.isWaitingForHierarchyContextResponse = true;
			BASE_ENTITY_ID=customHierarchyService.getEntityId();
			customHierarchyService.setEntityId(null);
			if($stateParams && $stateParams.customerHierarchyId  && !customHierarchyService.getNotFacingHierarchyLink()) {
				self.entityIdReceived = $stateParams.customerHierarchyId;
				customHierarchyService.setEntityIdReceived(self.entityIdReceived);
			}else if(customHierarchyService.getNavigatedFromOtherPage()
				&& customHierarchyService.getNotFacingHierarchyLink()){
				self.entityIdReceived = customHierarchyService.getEntityIdReceived();
			}
			else if(self.entityIdReceived == null
				&& customHierarchyService.getNavigatedFromOtherPage()
				&& customHierarchyService.getSelectedHierarchyContextForNavigation()!= null
				&& customHierarchyService.getSelectedHierarchyContextRoot()!= null
				&& customHierarchyService.getSelectedCustomHierarchyLevel()!= null){
				self.hierarchyContextSelected = customHierarchyService.getSelectedHierarchyContextRoot();
			}
			self.returnToList = customHierarchyService.getReturnToListFlag()
			customHierarchyApi.findAllHierarchyContexts({}, loadHierarchyContexts, fetchError);
			productSearchService.setCustomerHierarchySearching(false);
			self.selectedPathCustomHierarchy = customHierarchyService.getSelectedHierarchyContextForNavigation();
			customHierarchyService.setNavigatedFromOtherPage(false);
		};
		/**
		 * watcher scope event for  automatic search when back home page
		 */
		$scope.$on('initCustomerHierarchy', function(event) {
			//self.$onInit();
			self.hierarchyContextSelected = null;
			self.data = null;
		});

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		self.$onDestroy = function () {
			self.hierarchyContexts = [];
			$rootScope.contentChangedFlag = false;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		function fetchError(error) {
			self.addedHierarchyContext = false;
			customHierarchyService.setErrorMessage(getErrorMessage(error), true);
		}

		/**
		 * Sets the controller's error message.
		 * @param error The error message.
		 */
		function getErrorMessage(error) {
			if(error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return UNKNOWN_ERROR;
			}
		}

		/**
		 * This method is called when the user selects a hierarchy context root to view or when is navigated
		 * from other pages. It pushes either all of the
		 * hierarchy contexts into the hierarchy view if the user chose 'All', or just the one they selected.
		 *
		 * @param item
		 */
		self.selectHierarchyContextRoot = function (item, isClick) {
			//Checked when the user selects a hierarchy context root to view or when is navigated from other pages
			if(isClick){
				self.returnToList = false;
			customHierarchyService.setSelectedHierarchyContextRoot(item);
			}
			self.data = [];
			// if user chose to show 'ALL'
			if(item.id === HIERARCHY_CONTEXT_ALL.id){
				angular.forEach(self.hierarchyContexts, function (hierarchyContext) {
					// add all context hierarchies from the list that are not 'ALL'
					if(hierarchyContext.id !== HIERARCHY_CONTEXT_ALL.id){
						self.data.push(angular.copy(hierarchyContext));
					}
				})
			} else {
				self.data.push(angular.copy(item));
			}
		};

		/**
		 * Back end call to update new custom hierarchy
		 *
		 */
		self.updateCustomHierarchy = function () {
			self.isWaitingForHierarchyContextResponse = true;
			customHierarchyService.setWaitingForUpdate(true, true);
			var params = {
				newHierarchyName: self.newHierarchyName ,
				newHierarchyContext: self.newHierarchyContext
			};

			self.addedHierarchyContext = true;
			customHierarchyApi.updateCustomHierarchy(params, loadHierarchyContexts, fetchError);
		};

		/**
		 * This is the callback method that loads all of the hierarchy contexts received from the back end.
		 *
		 * @param results List of all hierarchy contexts.
		 */
		function loadHierarchyContexts(results){
			if(self.addedHierarchyContext){
				customHierarchyService.setSuccessMessage(DEFAULT_SUCCESS_MESSAGE, true);
			}
			self.addedHierarchyContext = false;
			self.hierarchyContexts = [];
			self.hierarchyContexts.push(HIERARCHY_CONTEXT_ALL);
			angular.forEach(results, function (hierarchyContext) {
				self.hierarchyContexts.push(hierarchyContext);
				if(angular.equals(hierarchyContext.id, DEFAULT_HIERARCHY_CODE_FROM_IMAGE) || angular.equals(hierarchyContext.id, CURRENT_HIERARCHY_CODE)){
					self.preselectedHierarchy=hierarchyContext;
				}
			});
			self.baseEntityId = null;
			if(self.entityIdReceived !== null && customHierarchyService.getHierarchyContextId() !== null){
				selectHierarchyPathToChildNotCust();
			}
			else if(self.entityIdReceived !== null){
				selectHierarchyPathToChild();
			} else {
				if(BASE_ENTITY_ID != null){
					customHierarchyService.setCurrentLevelTab(IMAGES_TAB);
					self.baseEntityId = BASE_ENTITY_ID;
					selectHierarchyPathToChild();
				}else{
					self.isWaitingForHierarchyContextResponse = false;
				}
			}
		}

		/**
		 * This presets the search to select HEB.com as the selected hierarchy context, as the user is coming to the
		 * custom hierarchy from a linked reference instead of clicking on 'Custom Hierarchy' from the nav bar.
		 */
		function selectHierarchyPathToChild(){
			for(var index = 0; index < self.hierarchyContexts.length; index++){
				if(self.hierarchyContexts[index].id === "CUST"){
					self.selectHierarchyContextRoot(self.hierarchyContexts[index], false);
					self.hierarchyContextSelected = self.hierarchyContexts[index];
					break;
				}
			}

			self.selectHierarchyContext(self.data[0], false, false);


		}

		/**
		 * Search lowest level when navigate other page, which not from facing customer hierarchy link
		 */
		function selectHierarchyPathToChildNotCust(){

			for(var index = 0; index < self.hierarchyContexts.length; index++){

				if(self.hierarchyContexts[index].id === customHierarchyService.getHierarchyContextId()){
					if(customHierarchyService.getSelectedHierarchyContextRoot() !== null
						&& customHierarchyService.getSelectedHierarchyContextRoot().id === HIERARCHY_CONTEXT_ALL.id){
						self.selectHierarchyContextRoot(customHierarchyService.getSelectedHierarchyContextRoot() , false);
						self.hierarchyContextSelected = customHierarchyService.getSelectedHierarchyContextRoot();
						break;
					}else {
						self.selectHierarchyContextRoot(self.hierarchyContexts[index], false);
						self.hierarchyContextSelected = self.hierarchyContexts[index];
						break;
					}

				}
			}

			if(customHierarchyService.getSelectedHierarchyContextRoot() !== null
				&& customHierarchyService.getSelectedHierarchyContextRoot().id === HIERARCHY_CONTEXT_ALL.id
				&& customHierarchyService.getSelectedHierarchyContextForNavigation()!= null){
				for(var index = 0; index < self.data.length; index++){
					if(self.data[index].id === customHierarchyService.getHierarchyContextId()){
						self.selectHierarchyContext(self.data[index], false, false);
						break;
					}
				}
			}else {
				self.selectHierarchyContext(self.data[0], false, false);
			}

		}

		/**
		 * This expands to the target level when a user is coming from a link to custom hierarchy.
		 *
		 * @param hierarchyContext Hierarchy context to expand.
		 * @param baseIdToFind Child id to expand to.
		 */
		function expandToSelectedLevel(hierarchyContext, baseIdToFind){
			for(var index = 0; index < hierarchyContext.childRelationships.length; index++) {
				if(expandLevels(hierarchyContext.childRelationships[index], baseIdToFind)){
					hierarchyContext.isCollapsed=false;
					break;
				}
			}
		}

		/**
		 * This method will expand the levels leading to the selected source level, and return whether or not the
		 * current level (or sub-levels) contained the selected source level, to indicate the level has been found.
		 *
		 * @param level Relationship level within a hierarchy.
		 * @param baseIdToFind Child id to expand to.
		 * @returns {boolean}
		 */
		function expandLevels(level, baseIdToFind){
			if(level.key !== 'undefined' && angular.equals(level.key.childEntityId, baseIdToFind) && level.defaultParent){
				self.previousSelectedValue = level;
				self.previousSelectedValue.isSelected = true;
				self.selectedLevel = level;
				self.onSelect({
					selectedLevel: angular.copy(level),
					requestNumber: currentRequest,
					keepCurrentMessages: false
				});
				return true;
			}else{
				if(level.childRelationships!== null){
					for(var index=0; index<level.childRelationships.length; index++){
						if(expandLevels(level.childRelationships[index], baseIdToFind)){
							level.isCollapsed=false;
							return true;
						}
					}
				}
			}
			return false;
		}

		/**
		 * This method is called when the user selects a hierarchy context from within the hierarchy context view. It
		 * is also called when a user searches for a hierarchy, then hits the 'clear' button to load the whole currently
		 * viewed hierarchy expanded to where user was looking previously.
		 *
		 * @param hierarchyContext The hierarchy context selected by the user.
		 * @param calledFromClearSearch Whether the selection of the hierarchy context was done from 'clear' search
		 * results or not.
		 */
		self.selectHierarchyContext = function (hierarchyContext, calledFromClearSearch, isClick) {
			if(isClick){
				customHierarchyService.setSelectedHierarchyContextForNavigation(hierarchyContext);
				customHierarchyService.setHierarchyContextId(hierarchyContext.id);
				customHierarchyService.setEntityIdReceived(null);
				self.entityIdReceived = null;
			}
			if($rootScope.contentChangedFlag){
				self.preSaveHierarchySelectedArguments = {
					hierarchyContext: hierarchyContext,
					calledFromClearSearch: calledFromClearSearch};
				self.selectedHierarchyContext = true;
				var result = document.getElementById("confirmationModal");
				var wrappedResult = angular.element(result);
				wrappedResult.modal("show");
			} else {
				self.preSaveHierarchySelectedArguments = null;
				self.selectedHierarchyContext = null;
				currentRequest++;
				deSelectPreviousSelectedLevel();
				self.rootLevelView = true;
				hierarchyContext.countOfProductChildren = 0;
				CURRENT_HIERARCHY_CODE = hierarchyContext.id;
				hierarchyContext.isCollapsed = !hierarchyContext.isCollapsed;
				if (!hierarchyContext.isCollapsed) {
					hierarchyContext.isWaitingForRelationshipResponse = true;
					self.baseChildId = BASE_ENTITY_ID;
					customHierarchyApi.findRelationshipByHierarchyContext(
						hierarchyContext,
						function (results) {
							hierarchyContext.childRelationships = results;
							setSelectedHierarchy(hierarchyContext.id);
							if(self.entityIdReceived){
								expandToSelectedLevel(hierarchyContext, self.entityIdReceived);
							}else if(customHierarchyService.getSelectedHierarchyContextForNavigation()!= null
								&& customHierarchyService.getSelectedHierarchyContextRoot()!= null
								&& customHierarchyService.getSelectedCustomHierarchyLevel()!= null
								&& customHierarchyService.getEntityIdReceived() != null){
								hierarchyContext.isCollapsed = false;
								expandToSelectedLevel(hierarchyContext, customHierarchyService.getEntityIdReceived());
							}
							if (self.baseEntityId) {
								expandToSelectedLevel(hierarchyContext, self.baseEntityId);
							}
							else if (calledFromClearSearch) {
								expandHierarchyToPreviouslyViewedPosition(hierarchyContext, 0, false);
							} else {
								self.selectedLevel = hierarchyContext;
								customHierarchyService.setHierarchyToSelectedLevel([], true);
									self.onSelect({
									selectedLevel: angular.copy(hierarchyContext),
									requestNumber: currentRequest,
									keepCurrentMessages: false
								});
							}
							hierarchyContext.isWaitingForRelationshipResponse = false;
						},
						function (error) {
							fetchError(error);
						}
					);

				}
			}
		};

		/**
		 * Receives the results of the confirmation modal and acts accordingly
		 */
		$rootScope.$on('modalResults', function(event, args){
			if(self.preSaveHierarchySelectedArguments){
				handleModalResults(args.result)
			}
		});

		/**
		 * Handles the results of the confirmation modal
		 * @param result
		 */
		function handleModalResults(result){
			if(result === true){
				$rootScope.contentChangedFlag = false;
				if(self.selectedHierarchyContext){
					self.selectHierarchyContext(
						self.preSaveHierarchySelectedArguments.hierarchyContext,
						self.preSaveHierarchySelectedArguments.calledFromClearSearch, false);
				} else {
					self.selectCustomHierarchyLevel(self.preSaveHierarchySelectedArguments);
				}
			}
			self.preSaveHierarchySelectedArguments = null;
			self.selectedHierarchyContext = null;
		}

		/**
		 * This method is called when the user successfully updates the selected hierarchy.
		 *
		 * @param hierarchyContext The hierarchy context selected by the user.
		 * @param keepCurrentMessages Whether to keep update/ success/ error messages or not.
		 * @param hierarchyContextIndex The index of the hierarchy context selected by the user.
		 * results or not.
		 */
		function refreshSelectedHierarchyContext(hierarchyContext, keepCurrentMessages, hierarchyContextIndex) {
			currentRequest++;
			deSelectPreviousSelectedLevel();
			self.rootLevelView = true;
			hierarchyContext.countOfProductChildren = 0;
			CURRENT_HIERARCHY_CODE = hierarchyContext.id;
			hierarchyContext.isWaitingForRelationshipResponse = true;
			self.baseChildId = BASE_ENTITY_ID;
			hierarchyContext.childRelationships = [];
			customHierarchyApi.findRelationshipByHierarchyContext(
				hierarchyContext,
				function (results) {
					self.data[hierarchyContextIndex].childRelationships = results;
					self.data[hierarchyContextIndex].isCollapsed = false;
					setSelectedHierarchy(null);  // want the hierarchy to update
					if(customHierarchyService.getHierarchyToSelectedLevel().length === 0) {
						self.selectedLevel = self.data[hierarchyContextIndex];
						self.onSelect({
							selectedLevel: angular.copy(self.data[hierarchyContextIndex]),
							requestNumber: currentRequest,
							keepCurrentMessages: true
						});
					} else {
						expandHierarchyToPreviouslyViewedPosition(self.data[hierarchyContextIndex], 0, keepCurrentMessages);
					}
					self.data[hierarchyContextIndex].isWaitingForRelationshipResponse = false;
				},
				function (error) {
					fetchError(error);
				}
			);
		}

		/**
		 * This method is called on a hierarchy context when a user hits the 'clear' search button, and has selected
		 * a hierarchy level. This method will expand that hierarchy down to the previously viewed position based
		 * on how many levels deep they had selected, so they can see all sibling branches at the level they were just
		 * viewing.
		 *
		 * (NOTE... angular.forEach does not support 'breaking' out of the loop, so a standard for loop was used instead)
		 *
		 * @param levelToExpand
		 * @param hierarchyLevelSelectedIndex
		 * @param keepCurrentMessages
		 */
		function expandHierarchyToPreviouslyViewedPosition(levelToExpand, hierarchyLevelSelectedIndex, keepCurrentMessages) {

			var currentLevel;
			currentRequest++;
			for(var index = 0; index < levelToExpand.childRelationships.length; index++){
				currentLevel = levelToExpand.childRelationships[index];
				if(isOriginalHierarchyLevelEqualToSelectedLevel(
						currentLevel,
						customHierarchyService.getHierarchyToSelectedLevel()[hierarchyLevelSelectedIndex])){
					currentLevel.isCollapsed = false;
					hierarchyLevelSelectedIndex++;
					if(customHierarchyService.getHierarchyToSelectedLevel().length > hierarchyLevelSelectedIndex &&
						typeof currentLevel['childRelationships'] !== 'undefined'){
						expandHierarchyToPreviouslyViewedPosition(currentLevel, hierarchyLevelSelectedIndex, keepCurrentMessages);
					} else {
						self.previousSelectedValue = currentLevel;
						self.previousSelectedValue.isSelected = true;
						self.selectedLevel = currentLevel;
						self.onSelect({
							selectedLevel: angular.copy(currentLevel),
							requestNumber: currentRequest,
							keepCurrentMessages: keepCurrentMessages
						});
					}
					break;
				}
			}
		}

		/**
		 * Method called when user selects a custom hierarchy level from one of the custom hierarchies.
		 *
		 * @param firstLevel The first level selected from the custom hierarchy.
		 * @param secondLevel The second level selected from the custom hierarchy.
		 * @param thirdLevel The third level selected from the custom hierarchy.
		 * @param fourthLevel The fourth level selected from the custom hierarchy.
		 * @param fifthLevel The fifth level selected from the custom hierarchy.
		 * @param sixthLevel The sixth level selected from the custom hierarchy.
		 */
		self.selectCustomHierarchyLevel = function (pathToLevel) {
			customHierarchyService.setEntityIdReceived(pathToLevel[pathToLevel.length -1].genericChildEntity.id);
			self.entityIdReceived = null;
			customHierarchyService.setSelectedCustomHierarchyLevel(pathToLevel);
			if($rootScope.contentChangedFlag){
				self.preSaveHierarchySelectedArguments = pathToLevel;
				self.selectedHierarchyContext = false;
				var result = document.getElementById("confirmationModal");
				var wrappedResult = angular.element(result);
				wrappedResult.modal("show");
			} else {
				self.preSaveHierarchySelectedArguments = null;
				self.selectedHierarchyContext = null;
				currentRequest++;
				deSelectPreviousSelectedLevel();

				customHierarchyService.setHierarchyToSelectedLevel(pathToLevel, true);

				var selectedHierarchyLevel = pathToLevel[pathToLevel.length - 1];
				selectedHierarchyLevel.isCollapsed = !selectedHierarchyLevel.isCollapsed;
				self.previousSelectedValue = selectedHierarchyLevel;
				self.previousSelectedValue.isSelected = true;
				self.selectedLevel = selectedHierarchyLevel;
				self.onSelect({
					selectedLevel: angular.copy(selectedHierarchyLevel),
					requestNumber: currentRequest,
					keepCurrentMessages: false
				});
				setSelectedHierarchy(self.selectedLevel.key.hierarchyContext);
				// if the user is using the home search, update the selected hierarchy level
				if(productSearchService.getCustomHierarchySearching()){
					updateProductSearchSelectedLevel(selectedHierarchyLevel);
				}
			}
			if(customHierarchyService.getReturnToListFlag()){
				self.returnToList = false;
			customHierarchyService.setReturnToListFlag(false) ;
				self.selectCustomHierarchyLevel(pathToLevel);
			}

		};

		function deSelectPreviousSelectedLevel(){

			if(self.previousSelectedValue){
				self.previousSelectedValue.isSelected = false;
			}
		}

		/**
		 * This function updates the selected hierarchy context in custom hierarchy service if it is different than
		 * what is already selected.
		 *
		 * @param hierarchyContextId ID of hierarchy context to set as the selected hierarchy context.
		 */
		function setSelectedHierarchy(hierarchyContextId){
			if(hierarchyContextId === null){
				hierarchyContextId = CURRENT_HIERARCHY_CODE;
				for (var index = 0; index < self.data.length; index++) {
					if (self.data[index].id === hierarchyContextId) {
						customHierarchyService.setHierarchyContextSelected(angular.copy(self.data[index]));
						break;
					}
				}
			}
			else if(customHierarchyService.getHierarchyContextSelected() === null ||
				customHierarchyService.getHierarchyContextSelected().id !== hierarchyContextId) {
				for (var index = 0; index < self.data.length; index++) {
					if (self.data[index].id === hierarchyContextId) {
						customHierarchyService.setHierarchyContextSelected(angular.copy(self.data[index]));
						break;
					}
				}
			}
		}

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
		 * This function clears the custom hierarchy search, and either:
		 * if user has selected a hierarchy level, resets the view of that hierarchy expanded down to the level that
		 * was being previously viewed.
		 * else resets the custom hierarchy to the default non-filtered search.
		 */
		self.clearCustomHierarchySearch = function () {
			self.customHierarchySearchText = null;
		};

		/**
		 * This function sets the current search text based on the text the user has searched for.
		 */
		function setCurrentCustomHierarchySearchText(){
			self.searchingForCustomHierarchyText =
				self.searchingForCustomHierarchyTextTemplate
					.replace("$searchText", self.customHierarchySearchText)
					.replace("$hierarchyContextDescription", self.hierarchyContextSelected.description);
		}

		/**
		 * This function initializes the custom hierarchy search values by setting the search text, and setting
		 * searching for custom hierarchy to true.
		 */
		function initializeCustomHierarchySearchValues(){
			setCurrentCustomHierarchySearchText();
			self.searchingForCustomHierarchy = true;
		}

		/**
		 * This is the callback function for getting the custom hierarchy based on a search string.
		 */
		function loadCustomHierarchySearch(results){
			self.data = results;
			expandAllHierarchyContexts(self.data);
			resetCustomHierarchySearchValues();
		}

		/**
		 * This function resets variables used for the custom hierarchy search to their default values.
		 */
		function resetCustomHierarchySearchValues(){
			self.searchingForCustomHierarchy = false;
			self.searchingForCustomHierarchyText = null;
		}

		/**
		 * This function updates the view of the custom hierarchy when a user clicks on the 'search' button, based
		 * on what the user has typed in the input by calling the api to get the searched values.
		 */
		self.updateCustomHierarchyViewOnSearch = function () {

			initializeCustomHierarchySearchValues();

			// this search for custom hierarchy levels had to be moved to the api because some browsers were taking
			// approximately 15 seconds in worst case searches, and other browsers were taking less than 5 seconds.
			customHierarchyApi.getCustomHierarchyBySearch(
				{
					searchString: self.customHierarchySearchText,
					hierarchyContextId: self.hierarchyContextSelected.id
				},
				loadCustomHierarchySearch,
				self.fetchError
			);
		};

		/**
		 * This function sets the collapsed variable to false for all current levels of the product hierarchy.
		 */
		function expandAllHierarchyContexts(dataToFullyExpand){
			angular.forEach(dataToFullyExpand, function(hierarchyContext) {
				hierarchyContext.isCollapsed = false;
				if(typeof hierarchyContext['childRelationships'] !== 'undefined'){
					expandAllCustomHierarchyRelationships(hierarchyContext.childRelationships);
				}
			});
		}

		/**
		 * This function sets the collapsed variable to false for all current levels of the product hierarchy.
		 */
		function expandAllCustomHierarchyRelationships(relationships){
			angular.forEach(relationships, function (relationship) {
				relationship.isCollapsed = false;
				if(typeof relationship['childRelationships'] !== 'undefined'){
					expandAllCustomHierarchyRelationships(relationship.childRelationships);
				}
			});
		}

		/**
		 * This method watches for a broadcast of the UPDATE_SELECTED_HIERARCHY_MESSAGE to notify this component that
		 * its view needs to be updated with fresh values.
		 */
		$scope.$on(UPDATE_SELECTED_HIERARCHY_MESSAGE, function(){
			var currentHierarchy = customHierarchyService.getHierarchyContextSelected();
			for(var index = 0; index < self.data.length; index++){
				if(currentHierarchy.id === self.data[index].id){
					refreshSelectedHierarchyContext(self.data[index], true, index);
					break;
				}
			}
		});

		/**
		 * This method watches for a broadcast of the UPDATE_SELECTED_HIERARCHY_MESSAGE to notify this component that
		 * its view needs to be updated with fresh values.
		 */
		$scope.$on(SET_HIERARCHY_TO_SELECTED_LEVEL, function(){
			var currentHierarchy = customHierarchyService.getHierarchyContextSelected();
			for(var index = 0; index < self.data.length; index++){
				if(currentHierarchy.id === self.data[index].id){
					refreshSelectedHierarchyContext(self.data[index], false, index);
					break;
				}
			}
		});

		/**
		 * This method watches for a broadcast of the UPDATE_SELECTED_HIERARCHY_MESSAGE to notify this component that
		 * its view needs to be updated with fresh values.
		 */
		$scope.$on(SET_HIERARCHY_TO_SELECTED_LEVEL_AND_UPDATE, function(){
			self.hierarchySelectedArray = customHierarchyService.getHierarchyToSelectedLevel();
			var currentHierarchy = customHierarchyService.getHierarchyContextSelected();
			for(var index = 0; index < self.data.length; index++){
				if(currentHierarchy.id === self.data[index].id){
					refreshSelectedHierarchyContext(self.data[index], true, index);
					break;
				}
			}
		});

		/**
		 * Resets values to null for add hierarchy modal.
		 */
		self.resetAddHierarchyContextModal = function(){
			self.newHierarchyName = null;
			self.newHierarchyContext = null;
		};

		/**
		 * This function calls the app.js function in the $scope to check if a string is empty.
		 *
		 * @param stringToCheck
		 * @returns {boolean}
		 */
		self.isEmptyString = function(stringToCheck){
			return $scope.$parent.isEmptyString(stringToCheck);
		};

		/**
		 * This function determines if a hierarchy level has children or not. If the given hierarchy level has a field
		 * 'childRelationships', and it is an array, and it has a length greater than 0, return true. Else return
		 * false.
		 *
		 * @param hierarchyLevel Hierarchy level to test for children.
		 * @returns {boolean} True if has children, false otherwise.
		 */
		self.hasChildren = function(hierarchyLevel){
			if(typeof hierarchyLevel['childRelationships'] === 'undefined' ||
				hierarchyLevel.childRelationships === null){
				return false;
			}
			return angular.isArray(hierarchyLevel.childRelationships) &&
				hierarchyLevel.childRelationships.length > 0;
		};

		/**
		 * This creates a path array for the hierarchy level, to keep track of how to get back to this location. It will
		 * include all parent nodes, as well as the current level.
		 *
		 * @param previousPath Path array containing all parent nodes.
		 * @param currentLevel The current level getting its path array.
		 * @returns {array} Path array to get back to the current level.
		 */
		self.getPathToCurrentLevel = function(previousPath, currentLevel){
			var pathArray;
			// if this is the top level in the hierarchy, the previous path will be undefined; initialize as []
			if(!previousPath){
				pathArray = [];
			}
			// else set the path array to a copy of the previous path; if a copy was not done, children nodes would
			// sometimes get additional nodes that were not part of the path
			else {
				pathArray = angular.copy(previousPath);
			}
			// push the current level onto the path array
			pathArray.push(currentLevel);
			return pathArray;
		};

		/**
		 * This sets the product search selected hierarchy level. If the selected level has any product or product
		 * group children, set to the selected level. Else set to null so the user cannot add the selected hierarchy
		 * to a search.
		 *
		 * @param possibleLowestLevel Selected level that may or may not have product children.
		 */
		function updateProductSearchSelectedLevel(possibleLowestLevel) {
			if(possibleLowestLevel.countOfProductChildren > 0) {
				productSearchService.setCustomerHierarchyLowestNode(possibleLowestLevel);
			} else {
				productSearchService.setCustomerHierarchyLowestNode(null);
			}
		}

		/**
		 * This generates a hierarchy context value by taking the first 5 characters of hierarchy name from the user input
		 */
		self.automaticDefaultHierarchyContextValue = function () {
			self.newHierarchyContext = self.newHierarchyName.substring(0,5).toUpperCase();
		};

		/**
		 * Helper function on whether or not to show hierarchy context children. This function returns true if all
		 * requirements are met on a given hierarchy context:
		 * 1. hierarchy context is not collapsed
		 * 2. hierarchy context has children
		 *
		 * @param hierarchyContext
		 * @returns {boolean}
		 */
		self.showHierarchyContextChildren = function(hierarchyContext){
			return !self.isHierarchyLevelCollapsed(hierarchyContext) &&
				self.hasChildren(hierarchyContext);
		}
	}
})();
