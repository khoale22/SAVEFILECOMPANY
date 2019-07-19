/*
 *   eCommerceViewDirectionsComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * product details -> product -> eCommerce View -> Directions component.
 *
 * @author vn00602
 * @since 2.14.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('eCommerceViewDirection', {
		scope: '=',
		bindings: {
			currentTab: '<',
			productMaster: '<',
			eCommerceViewDetails: '=',
			getECommerceViewDataSource:'&',
			isEditText: '='
		},
		// Inline template which is bind to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewDirections.html',
		// The controller that handles our component logic
		controller: DirectionController
	});

	DirectionController.$inject = ['$scope', 'ECommerceViewApi', '$sce', 'PermissionsService','$rootScope'];

	/**
	 * ECommerce View Directions component's controller definition.
	 *
	 * @param $scope scope of the eCommerceView component.
	 * @param eCommerceViewApi the api of eCommerceView.
	 * @constructor
	 */
	function DirectionController($scope, eCommerceViewApi, $sce, permissionsService,$rootScope) {

		/**
		 * All CRUD operation controls of choice option page goes here.
		 */
		var self = this;

		/**
		 * Reload eCommerce view key.
		 * @type {string}
		 */
		self.RELOAD_ECOMMERCE_VIEW = 'reloadECommerceView';

		/**
		 * Reset eCommerce view.
		 * @type {string}
		 */
		self.RESET_ECOMMERCE_VIEW = 'resetECommerceView';

		/**
		 * Reload after save popup.
		 * @type {string}
		 */
		self.RELOAD_AFTER_SAVE_POPUP = 'reloadAfterSavePopup';

		/**
		 * The default error message.
		 *
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * Max length of textarea input.
		 *
		 * @type {number}
		 */
		self.MAX_LENGTH = 10000;

		/**
		 * The direction logical attribute id.
		 *
		 * @type {number}
		 */
		self.DIRECTION_ATTRIBUTE_ID = 1676;

		/**
		 * Flag for waiting response from back end.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * The product maintenance source selected when click Edit text button.
		 *
		 * @type {boolean}
		 */
		self.isProductMaintenanceSourceSelected = false;

		/**
		 * The flag that check if Directions is editable or not.
		 *
		 * @type {boolean}
		 */
		self.isEditingDirection = false;
		const EDIT_DIRECTION_PHY_ATTR_ID = 1654;
		const SRC_SYS_ID_EDIT = 4;
		self.hasPermission = false;
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized.
		 */
		this.$onInit = function () {
			self.isWaitingForResponse = true;
			self.findDirections();
		};

		/**
		 * Component will reload the data whenever the item is changed in.
		 */
		this.$onChanges = function () {
			self.isWaitingForResponse = true;
			self.isProductMaintenanceSourceSelected = false;
            self.findDirections();
		};

		/**
		 * Reload ECommerceView.
		 */
		$scope.$on(self.RELOAD_ECOMMERCE_VIEW, function() {
			self.$onChanges();
		});

		/**
		 * Reset eCommerce view.
		 */
		$scope.$on(self.RESET_ECOMMERCE_VIEW, function() {
			self.setEditMode();
			self.isProductMaintenanceSourceSelected = false;
		});

		/**
		 * Reload after save popup.
		 */
		$scope.$on(self.RELOAD_AFTER_SAVE_POPUP, function(event, attributeId, status) {
			if(attributeId === self.DIRECTION_ATTRIBUTE_ID){
				self.isEditingDirection = false;
				if(status === true){
					self.$onChanges();
				}else{
					self.isWaitingForResponse = true;
				}
			}
		});

		/**
		 * Find the directions by product id or upc.
		 */
		self.findDirections = function () {
			eCommerceViewApi.findDirections({
					productId: self.productMaster.prodId,
					scanCodeId: self.productMaster.productPrimaryScanCodeId
				},
				self.directionResponseSuccess,
				self.fetchError);
		};

		/**
		 * Handle when click Edit source button, to find all the direction attribute priorities by product id or upc.
		 *
		 * @param attributeId
		 * @param headerTitle
		 */
		self.editSourceHandle = function (attributeId, headerTitle) {
			self.isEditText = self.isEditingDirection;
			self.getECommerceViewDataSource({
				productId: self.productMaster.prodId,
				scanCodeId: self.productMaster.productPrimaryScanCodeId,
				attributeId: attributeId,
				salesChannel: self.currentTab.saleChannel.id,
				headerTitle: headerTitle,
				typeSource: 'type1'});
		};

		/**
		 * Handle when click Edit text of Directions.
		 */
		self.editTextDirection = function () {
			self.isEditingDirection = true;
			self.isProductMaintenanceSourceSelected = true;
		};

		/**
		 * Load the directions data response success.
		 *
		 * @param result the results to load.
		 */
		self.directionResponseSuccess = function (result) {
			self.isWaitingForResponse = false;
			self.differentWithDefaultValue = false;
			self.isEditingDirection = false;
			if (!result) {
				self.eCommerceViewDetails.directions = {};
				self.eCommerceViewDetails.directionsOrg =  {};
			} else {
				self.eCommerceViewDetails.directions = result;
				self.eCommerceViewDetails.directionsOrg = angular.copy(self.eCommerceViewDetails.directions);
				self.differentWithDefaultValue = result.differentWithDefaultValue;
				self.setEditMode();
			}
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.isEditingDirection = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
		};

		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			} else {
				return self.UNKNOWN_ERROR;
			}
		};
		/**
		 * Check edit status or not.
		 *
		 * @returns {boolean}
		 */
		self.setEditMode = function(){
            if (self.eCommerceViewDetails.directions != null) {
                self.isEditingDirection = self.eCommerceViewDetails.directions.sourceSystemId === SRC_SYS_ID_EDIT && self.eCommerceViewDetails.directions.physicalAttributeId == EDIT_DIRECTION_PHY_ATTR_ID;
            }
            self.hasPermission = permissionsService.getPermissions('PD_ECOM_04', 'EDIT');
		};

		/**
		 * Check change direction.
		 */
		self.checkChangeDirection = function () {
			$rootScope.contentChangedFlag = true;
			if(angular.equals(self.eCommerceViewDetails.directions, self.eCommerceViewDetails.directionsOrg)){
				$rootScope.contentChangedFlag = false;
			}
		}
	}
})();
