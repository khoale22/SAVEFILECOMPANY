/*
 *   productLevelPaths.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Custom Hierarchy -> CurrentLevel Info page component.
 *
 * @author s753601
 * @since 2.16.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('productLevelPaths', {
		// isolated scope binding
		bindings: {
			pathList: '<',
			primaryPath: '=',
			changedPrimaryPath: '=',
			entityList: '=',
			multipleParents: '='
		},
		scope: {},
		templateUrl: 'src/customHierarchy/productLevelPaths.html',
		controller: productLevelPathsController
	});

	productLevelPathsController.$inject = [];

	/**
	 * Product Level Path's controller definition.
	 * @constructor
	 */
	function productLevelPathsController(){
		var self = this;
		self.primaryPath = null;
		self.existingParent = false;

		/**
		 * Angular on it function of this component.
		 */
		self.$onInit = function(){
			checkForCorrectNumberOfPrimaries();
		};

		/**
		 * Helper function to determine if there are the correct number of default parent paths. This should be 1 and
		 * only 1. If more than one are found, do not set any as the current default path. If there are no current
		 * default paths, return. Else set the current primary path to the only existing default path.
		 */
		function checkForCorrectNumberOfPrimaries(){
			var primaryPathIndex = -1;
			for(var index = 0; index < self.pathList.length; index++){
				if(isBottomPrimaryPath(self.pathList[index])){
					primaryPathIndex = index;
				}
			}

			if(primaryPathIndex !== -1){
				self.primaryPath = primaryPathIndex;
				self.changedPrimaryPath = primaryPathIndex;
			}
		}

		/**
		 * Helper method to determine if the current path is a primary path at it's lowest level. If you are looking at
		 * a lowest level of a custom hierarchy (next level of child relationships does not have child relationships),
		 * return whether this level is the default parent. Else recursively call this function on the first element of
		 * its child relationships.
		 *
		 * @param currentPath Current path to determine if bottom primary path or not.
		 * @returns {boolean}
		 */
		function isBottomPrimaryPath(currentPath){
			if(!currentPath){
				return false;
			}
			// if this is the bottom most hierarchy level
			if(currentPath.childRelationships !== null && currentPath.childRelationships[0].childRelationships == null){
				// return whether its child relationship is a default parent
				self.entityList.push(currentPath.childRelationships[0]);
				if(currentPath.childRelationships[0].defaultParent && !self.existingParent) {
					self.existingParent = true;
					return currentPath.childRelationships[0].defaultParent;
				} else if(currentPath.childRelationships[0].defaultParent && self.existingParent){
					self.multipleParents = true;
					return false;
				} else{
					return currentPath.childRelationships[0].defaultParent
				}
			} else {
				// else return the recursive call to this function
				return isBottomPrimaryPath(currentPath.childRelationships[0]);
			}
		}

		/**
		 * Helper function to determine if the current level has hierarchy level children.
		 *
		 * @param currentLevel Hierarchy level to compare.
		 * @returns {boolean}
		 */
		self.hasHierarchyLevelChildren = function(currentLevel) {
			return currentLevel.childRelationships != null && currentLevel.childRelationships[0].childRelationships != null
		}
	}
}());
