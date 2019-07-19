/*
 *   eCommerceViewWarningsComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * product details -> product -> eCommerce View -> warnings component.
 *
 * @author vn00602
 * @since 2.14.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('eCommerceViewWarning', {
		scope: '=',
		bindings: {
			currentTab: '<',
			productMaster: '<',
			eCommerceViewDetails: '=',
			getECommerceViewDataSource:'&',
			isEditText: '='
		},
		// Inline template which is bind to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewWarnings.html',
		// The controller that handles our component logic
		controller: WarningController
	});

	WarningController.$inject = ['$scope', 'ECommerceViewApi', '$sce', 'PermissionsService'];

	/**
	 * ECommerce View Warnings component's controller definition.
	 *
	 * @param $scope scope of the eCommerceView component.
	 * @param eCommerceViewApi the api of eCommerceView.
	 * @constructor
	 */
	function WarningController($scope, eCommerceViewApi, $sce,permissionsService) {

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
		 * The warning logical attribute id.
		 *
		 * @type {number}
		 */
		self.WARNING_ATTRIBUTE_ID = 1677;

		/**
		 * The flag that check if Warnings is editable or not.
		 *
		 * @type {boolean}
		 */
		self.isEditingWarning = false;

        /**
         * Flag for waiting response from back end.
         *
         * @type {boolean}
         */
        self.isWaitingForResponse = false;
        const EDIT_WARNING_PHY_ATTR_ID = 1682;
        const SRC_SYS_ID_EDIT = 4;
        self.hasPermission = false;
        /**
         * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized.
         */
        this.$onInit = function () {
            self.isWaitingForResponse = true;
            self.findWarnings();
        };

        /**
         * Component will reload the data whenever the item is changed in.
         */
        this.$onChanges = function () {
            self.isWaitingForResponse = true;
            self.findWarnings();
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
		});

		/**
		 * Reload after save popup.
		 */
		$scope.$on(self.RELOAD_AFTER_SAVE_POPUP, function(event, attributeId, status) {
			if(attributeId === self.WARNING_ATTRIBUTE_ID){
				self.isEditingWarning = false;
				if(status === true){
					self.$onChanges();
				}else{
					self.isWaitingForResponse = true;
				}
			}
		});

		/**
		 * Find the warnings by product id or upc.
		 */
		self.findWarnings = function () {
			eCommerceViewApi.findWarnings({
					productId: self.productMaster.prodId,
					scanCodeId: self.productMaster.productPrimaryScanCodeId
				},
				self.warningResponseSuccess,
				self.fetchError);
		};

        /**
         * Load the warnings data response success.
         *
         * @param result the results to load.
         */
        self.warningResponseSuccess = function (result) {
            self.isWaitingForResponse = false;
            self.differentWithDefaultValue = false;
            self.isEditingWarning = false;
            if (!result) {
                self.eCommerceViewDetails.warnings = {};
                self.eCommerceViewDetails.warningsOrg = {};
            } else {
                self.eCommerceViewDetails.warnings = result;
                self.eCommerceViewDetails.warningsOrg = angular.copy(self.eCommerceViewDetails.warnings);
                self.differentWithDefaultValue = result.differentWithDefaultValue;
                self.setEditMode();
            }
        };

		/**
		 * Handle when click Edit source button, to find all the warning attribute priorities by product id or upc.
		 *
		 * @param attributeId
		 * @param headerTitle
		 */
		self.editSourceHandle = function (attributeId, headerTitle) {
			self.isEditText = self.isEditingWarning;
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
		self.editTextWarning = function () {
			self.isEditingWarning = true;
			self.isProductMaintenanceSourceSelected = true;
		};
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
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
                }
                return error.data.error;
            }
            return self.UNKNOWN_ERROR;
        };
        /**
         * Check edit status or not.
         *
         * @returns {boolean}
         */
        self.setEditMode = function () {
            if (self.eCommerceViewDetails.warnings != null) {
                self.isEditingWarning = self.eCommerceViewDetails.warnings.sourceSystemId === SRC_SYS_ID_EDIT && self.eCommerceViewDetails.warnings.physicalAttributeId == EDIT_WARNING_PHY_ATTR_ID;
            }
            self.hasPermission = permissionsService.getPermissions('PD_ECOM_05', 'EDIT');
        };
    }
})();
