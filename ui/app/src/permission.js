/*
 * permission.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 *
 */

'use strict';

/**
 * Collection of custom directives to aid in customizing the application based on user permissions.
 *
 * @author d116773
 */

var CAN_VIEW = 'canView';
var CAN_EDIT = 'canEdit';
var CANT_VIEW = 'cantView';
var VIEW_ONLY = 'viewOnly';

/**
 * Constructs the function that Angular will call to resolve the directive.
 *
 * @param ngIf The root ngIf directive being overridden.
 * @param directiveName The name of the directive to crate a function to support. Angular will use this as the key
 * in a map of attributes it passes to the resolution function. The attribute with that key will be the name of the
 * protected resource as passed to the directive (PRODUCT_DISCONTINUE_REPORT in the example below).
 * @param permissionType The type of permission as defined in ARBAF.
 * @returns {Function} The function Angular will call to resolve the directive.
 */
function getPermissionFunction (ngIf, directiveName, permissionType, permissionsService) {

	return function($scope, $element, $attr) {

		// Look up the name of the resource.
		var value = $attr[directiveName];

		var hasPermission;

		// Determine which function in the permissions service should be called an how.
		if (permissionType == CANT_VIEW) {
			// Can't view is the opposite of can view.
			hasPermission = !permissionsService.getPermissions(value, "VIEW");
		} else if (permissionType == VIEW_ONLY) {
			// Since getPermission for VIEW treats a permission of EDIT as explicitly having VIEW,
			// this function is used to see if the user only hals VIEW permission and not EDIT.
			hasPermission = permissionsService.hasViewOnly(value);
		} else {
			// The rest just call the regular getPermissions.
			hasPermission = permissionsService.getPermissions(value, permissionType);
		}

		// Forward back on to Angular to apply the ngIf.
		$attr.ngIf = function() {
			return hasPermission;
		};
		ngIf.link.apply(ngIf, arguments);
	}
}

/**
 * Custom directive cant-view that can be used to decide whether or not to show or hide stuff based on if the user has view
 * permission or not. This is the opposite of the others in that it will show things when a user does not have
 * a particular permission. It is based on ng-if, so it does not just make stuff visible or invisible. It removes
 * them from the DOM with all the same implications as ng-if.
 *
 * Example usage:
 * <li class="dropdown" cant-view="PRODUCT_DISCONTINUE_REPORT">
 * </li>
 *
 * This will show a list if the user does not have view or edit permission on the PRODUCT_DISCONTINUE_REPORT resource.
 *
 * @since 2.0.0
 */

(function() {
	angular.module('productMaintenanceUiApp').directive('cantView',['ngIfDirective','PermissionsService',function(ngIfDirective, permissionsService){

		var ngIf = ngIfDirective[0];
		return {
			transclude: ngIf.transclude,
			priority: ngIf.priority,
			terminal: ngIf.terminal,
			restrict: ngIf.restrict,
			link:  getPermissionFunction(ngIf, CANT_VIEW, CANT_VIEW, permissionsService)
		};
	}] );
})();

/**
 * Custom directive view-only that can be used to decide whether or not to show or hide stuff based on if the user has view
 * permission and not edit permission. It is based on ng-if, so it does not just make stuff visible or invisible. It removes
 * them from the DOM with all the same implications as ng-if.
 *
 * Example usage:
 * <li class="dropdown" view-only="PRODUCT_DISCONTINUE_REPORT">
 * </li>
 *
 * This will show a list if the user has view permission on the PRODUCT_DISCONTINUE_REPORT resource but does
 * not have edit permission on PRODUCT_DISCONTINUE_REPORT.
 *
 * @since 2.7.0
 */
(function() {
	angular.module('productMaintenanceUiApp').directive('viewOnly',['ngIfDirective','PermissionsService',function(ngIfDirective, permissionsService){

		var ngIf = ngIfDirective[0];
		return {
			transclude: ngIf.transclude,
			priority: ngIf.priority,
			terminal: ngIf.terminal,
			restrict: ngIf.restrict,
			link:  getPermissionFunction(ngIf, VIEW_ONLY, VIEW_ONLY, permissionsService)
		};
	}] );
})();

/**
 * Custom directive can-view that can be used to decide whether or not to show or hide stuff based on if the user has view
 * permission or not. It is based on ng-if, so it does not just make stuff visible or invisible. It removes
 * them from the DOM with all the same implications as ng-if.
 *
 * Example usage:
 * <li class="dropdown" can-view="PRODUCT_DISCONTINUE_REPORT">
 * </li>
 *
 * This will show a list if the user has view permission on the PRODUCT_DISCONTINUE_REPORT resource.
 *
 * @since 2.0.0
 */

(function() {
	angular.module('productMaintenanceUiApp').directive('canView',['ngIfDirective','PermissionsService',function(ngIfDirective, permissionsService){

		var ngIf = ngIfDirective[0];

		return {
			transclude: ngIf.transclude,
			priority: ngIf.priority,
			terminal: ngIf.terminal,
			restrict: ngIf.restrict,
			link:  getPermissionFunction(ngIf, CAN_VIEW, 'VIEW', permissionsService)
		};
	}] );
})();

/**
 * Custom directive that can be used to decide whether or not to show or hide stuff based on if the user has edit
 * permission or not. It is based on ng-if, so it does not just make stuff visible or invisible. It removes
 * them from the DOM with all the same implications as ng-if.
 *
 * Example usage:
 * <li class="dropdown" can-edit="PRODUCT_DISCONTINUE_REPORT">
 * </li>
 *
 * This will show a list if the user has edit permission on the PRODUCT_DISCONTINUE_REPORT resource.
 *
 * @since 2.0.0
 */

(function() {
	angular.module('productMaintenanceUiApp').directive('canEdit', ['ngIfDirective','PermissionsService',function(ngIfDirective, permissionsService){

		 var ngIf = ngIfDirective[0];

		return {
			transclude: ngIf.transclude,
			priority: ngIf.priority,
			terminal: ngIf.terminal,
			restrict: ngIf.restrict,
			link: getPermissionFunction(ngIf, CAN_EDIT, 'EDIT', permissionsService)
		}
	}] );
})();

/**
 * Custom service that can be used to decide whether or not to show or hide stuff based on if the user has edit
 * permission or not.  It returns true if user has permission or false if they do not.
 *
 * Example usage:
 * var hasPermission =	permissionsService.getPermissions(value, permissionType);
 *
 */
(function() {
	angular.module('productMaintenanceUiApp').service('PermissionsService', permissionsService);

	permissionsService.$inject = ['$rootScope'];
	function permissionsService($rootScope){

		return {
			getPermissions:function (resource, permissionType) {

				// Look up the name of the resource.
				// Append the type of permission
				var permissionName = resource + '-' + permissionType;
				var checkForBoth = false;
				var editPermissionName;

				var hasPermission = false;

				// Get the user's set of permissions
				var currentUser = $rootScope.currentUser;

				// If there is no logged in user, then return false
				if (currentUser == null || currentUser.roles == null) {
					return false;
				}

				if(permissionType === "VIEW"){
					checkForBoth = true;
					editPermissionName = resource + '-EDIT';
				}

				// Loop through and see if they have the permission being asked for.
				for (var i = 0; i < currentUser.roles.length; i++) {
					if (!checkForBoth && currentUser.roles[i] == permissionName) {
						hasPermission =  true;
						break;
					} else if (checkForBoth &&
						(currentUser.roles[i] == permissionName || currentUser.roles[i] == editPermissionName)) {
						hasPermission = true;
						break;
					}
				}
				return hasPermission;
			},
			// The above function will treat edit as a superset of view, this determines if the user
			// has view, but not edit permission
			hasViewOnly: function(resource) {
				// Look up the name of the resource.
				// Append the type of permission
				var vewPermission = resource + '-VIEW';
				var editPermission = resource + '-EDIT';

				var hasViewPermission = false;

				// Get the user's set of permissions
				var currentUser = $rootScope.currentUser;

				// Loop through and see if they have the permission being asked for.
				for (var i = 0; i < currentUser.roles.length; i++) {
					if (currentUser.roles[i] == vewPermission) {
						hasViewPermission =  true;
					}
					if (currentUser.roles[i] == editPermission) {
						return false;
					}
				}
				return hasViewPermission;
			}
		}
	}
})();
