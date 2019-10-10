/*
 *   eCommerceViewIngredientsComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * product details -> product -> eCommerce View -> Ingredients component.
 *
 * @author vn00602
 * @since 2.14.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('eCommerceViewIngredient', {
		scope: '=',
		bindings: {
			currentTab: '<',
			productMaster: '<',
			eCommerceViewDetails: '=',
			isEditingIngredient:'=',
			success:'=',
			error:'='
		},
		require:{
			parent:'^^eCommerceView'
		},
		// Inline template which is bind to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/eCommerceViewIngredients.html',
		// The controller that handles our component logic
		controller: IngredientController
	});

	IngredientController.$inject = ['$scope', 'ECommerceViewApi', '$sce', 'PermissionsService','$rootScope'];

	/**
	 * ECommerce View Ingredients component's controller definition.
	 *
	 * @param $scope scope of the eCommerceView component.
	 * @param eCommerceViewApi the api of eCommerceView.
	 * @constructor
	 */
	function IngredientController($scope, eCommerceViewApi, $sce, permissionsService,$rootScope) {

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
		 * The source system id and physical attribute id of product maintenance source.
		 *
		 * @type {string}
		 */
		self.PRODUCT_MAINTENANCE_SOURCE_PHYSICAL = "4_1643";

        /**
         * The add action code.
         *
         * @type {string}
         */
        self.ADD_ACTION_CODE = "A";

		/**
		 * The delete action code.
		 *
		 * @type {string}
		 */
		self.DELETE_ACTION_CODE = "D";

		/**
		 * Flag for waiting response from back end.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * The list of all ingredient attribute priorities.
		 *
		 * @type {Array}
		 */
		self.ingredientAttributePriorities = [];

		/**
		 * The flag that waiting response data for modal.
		 *
		 * @type {boolean}
		 */
		self.isWaitingModal = false;

		/**
		 * The list of ingredient attribute priorities to display in modal.
		 *
		 * @type {Array}
		 */
		self.ingredientsTableModal = [];

		/**
		 * The original list of ingredient attribute priorities used to reset.
		 *
		 * @type {Array}
		 */
		self.ingredientsTableModalOrig = [];

		/**
		 * The ingredient attribute from eCommerceView source (13).
		 *
		 * @type {object}
		 */
		self.ingredientsECommerceModal = null;

		/**
		 * The value of radio button that show which source system is selected,
		 * it includes source system id and physical attribute id.
		 *
		 * @type {string}
		 */
		self.selectedSourceSystem = '';

		/**
		 * The object that contains selected ingredient attribute source on the modal.
		 *
		 * @type {object}
		 */
		self.eCommerceViewDetailsParams = {};

		/**
		 * The product maintenance source selected when click Edit text button.
		 *
		 * @type {boolean}
		 */
		self.isProductMaintenanceSourceSelected = false;

        /**
         * The successful message.
         *
         * @type {string}
         */
        self.successPopup = '';

		/**
		 * The ingredient logical attribute id.
		 *
		 * @type {number}
		 */
		self.INGREDIENT_ATTRIBUTE_ID = 1674;

        /**
         * Min length for value of source
         *
         * @type {number}
         */
        self.MIN_LENGTH_READ = 200;
        const EDIT_INGREDIENTS_PHY_ATTR_ID = 1643;
        const SRC_SYS_ID_EDIT = 4;
        /**
         * Holds the status of ebm permission.
         * @type {boolean}
         */
        self.hasPermission = false;
        /**
         * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized.
         */
        this.$onInit = function () {
            self.errorPopup = '';
            self.successPopup = '';
            self.isWaitingForResponse = true;
            self.isEditingIngredient = false;
            self.findIngredients();
        };

        /**
         * Component will reload the data whenever the item is changed in.
         */
        this.$onChanges = function () {
            self.errorPopup = '';
            self.successPopup = '';
            self.isWaitingForResponse = true;
            self.isEditingIngredient = false;
            self.isProductMaintenanceSourceSelected = false;
            self.findIngredients();
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
		 * Find the Ingredients by product id or upc.
		 */
		self.findIngredients = function () {
			eCommerceViewApi.findIngredients({
					productId: self.productMaster.prodId,
					scanCodeId: self.productMaster.productPrimaryScanCodeId
				},
				self.ingredientResponseSuccess,
				self.fetchError);
		};

        /**
         * Find all the ingredient attribute priorities by product id or upc.
         */
        self.editSourceIngredient = function () {
            $('#ingredientsModal').modal({backdrop: 'static', keyboard: true});
            self.isWaitingModal = true;
            eCommerceViewApi.findAllIngredientAttributePriorities({
                    productId: self.productMaster.prodId,
                    scanCodeId: self.productMaster.productPrimaryScanCodeId
                },
                self.ingredientsModalResponseSuccess,
                self.fetchError);
        };

        /**
         * Handle when click Edit text of Ingredients.
         */
        self.editTextIngredient = function () {
            self.isEditingIngredient = true;
            self.isProductMaintenanceSourceSelected = true;
        };

        /**
         * Handle when click Select for HEB.com button on the modal, that save product attribute override information,
         * and save favor source information.
         */
        self.saveIngredientAttributePriorities = function () {
            self.error = '';
            self.success = '';
            self.isEditingIngredient = false;
            if (self.selectedSourceSystem === self.ingredientsECommerceModal.sourceSystemSelected
                && angular.toJson(self.ingredientsTableModal) === angular.toJson(self.ingredientsTableModalOrig)) {
                $('#ingredientsModal').modal('hide');
                self.$onChanges();
                return;
            }
            $('#ingredientsModal').modal('hide');
            self.eCommerceViewDetailsParams = {};
            if (self.selectedSourceSystem !== self.ingredientsECommerceModal.sourceSystemSelected) {
                angular.forEach(self.ingredientsTableModal, function (ingredient) {
                    var currentSourceSystem = ingredient.sourceSystemId + '_' + ingredient.physicalAttributeId;
                    if (self.selectedSourceSystem === currentSourceSystem) {
                        if (!ingredient.sourceSystemDefault) {
                            self.eCommerceViewDetailsParams.ingredients = angular.copy(ingredient);
                            self.eCommerceViewDetailsParams.ingredients.actionCode = self.ADD_ACTION_CODE;
                        }
                    }
                    if (self.ingredientsECommerceModal.sourceSystemSelected === currentSourceSystem) {
                        self.eCommerceViewDetailsParams.ingredientsToDelete = angular.copy(ingredient);
                        self.eCommerceViewDetailsParams.ingredientsToDelete.actionCode = self.DELETE_ACTION_CODE;
                    }
                });
            }
            if (angular.toJson(self.ingredientsTableModal) !== angular.toJson(self.ingredientsTableModalOrig)) {
                angular.forEach(self.ingredientsTableModal, function (ingredient) {
                    var currentSourceSystem = ingredient.sourceSystemId + '_' + ingredient.physicalAttributeId;
                    if (self.PRODUCT_MAINTENANCE_SOURCE_PHYSICAL === currentSourceSystem) {
                        self.eCommerceViewDetailsParams.ingredientsAttribute = angular.copy(ingredient);
                    }
                });
            }
            if (self.eCommerceViewDetailsParams) {
                self.isWaitingModal = true;
                self.errorPopup = '';
                self.successPopup = '';
                self.eCommerceViewDetailsParams.productId = self.productMaster.prodId;
                self.eCommerceViewDetailsParams.scanCodeId = self.productMaster.productPrimaryScanCodeId;
                eCommerceViewApi.updateIngredientAttributePriorities(
                    self.eCommerceViewDetailsParams,
                    self.updateIngredientSourceResponseSuccess,
                    self.fetchError);
            }
            self.isProductMaintenanceSourceSelected = false;
        };

        /**
         * Handle when click Reset button on the modal.
         */
        self.resetIngredientModal = function () {
            if (self.selectedSourceSystem !== self.ingredientsECommerceModal.sourceSystemSelected) {
                self.selectedSourceSystem = self.ingredientsECommerceModal.sourceSystemSelected;
            }
            if (angular.toJson(self.ingredientsTableModal) !== angular.toJson(self.ingredientsTableModalOrig)) {
                self.ingredientsTableModal = angular.copy(self.ingredientsTableModalOrig);
            }
            self.isProductMaintenanceSourceSelected = false;
        };
        /**
         * Load the Ingredients data response success.
         *
         * @param result the results to load.
         */
        self.ingredientResponseSuccess = function (result) {
            self.isWaitingForResponse = false;
            self.differentWithDefaultValue = false;
            if (!result) {
                self.eCommerceViewDetails.ingredients = {};
                self.eCommerceViewDetails.ingredientsOrg = {};
            } else {
                self.eCommerceViewDetails.ingredients = result;
                self.eCommerceViewDetails.ingredientsOrg = angular.copy(self.eCommerceViewDetails.ingredients);
                self.differentWithDefaultValue = result.differentWithDefaultValue;
                self.setEditMode();
            }
        };

        /**
         * Load the table of ingredient attribute priorities.
         *
         * @param results
         */
        self.ingredientsModalResponseSuccess = function (results) {
            self.isWaitingModal = false;
            if (!results || results.length === 0) {
                self.ingredientAttributePriorities = [];
            } else {
                self.ingredientAttributePriorities = results;
                self.ingredientsECommerceModal = angular.copy(self.ingredientAttributePriorities[0]);
                if (self.isProductMaintenanceSourceSelected) {
                    self.selectedSourceSystem = self.PRODUCT_MAINTENANCE_SOURCE_PHYSICAL;
                } else {
                    self.selectedSourceSystem = self.ingredientsECommerceModal.sourceSystemSelected;
                }
                self.ingredientsTableModal = angular.copy(self.ingredientAttributePriorities);
                self.ingredientsTableModal.splice(0, 1);	// remove the first element from source 13 or 27.
                self.setEditableForModal();
                self.setSourceNameForModal();
                self.initDataForReadMoreAndReadLess();
                self.ingredientsTableModalOrig = angular.copy(self.ingredientsTableModal);
            }
        };

        /**
         * Update new ingredient source system id after response successfully.
         *
         * @param result
         */
        self.updateIngredientSourceResponseSuccess = function (result) {
            self.isWaitingModal = false;
            self.ingredientsECommerceModal.sourceSystemSelected = self.selectedSourceSystem;
            self.ingredientsTableModalOrig = angular.copy(self.ingredientsTableModal);
            if (result && result.message) {
                self.success = "Ingredient: " + result.message;
            }
            self.findIngredients();
        };

        /**
         * Ingredient from Product Maintenance source (4) is editable, otherwise not.
         */
        self.setEditableForModal = function () {
            angular.forEach(self.ingredientsTableModal, function (ingredient) {
                var currentSourceSystem = ingredient.sourceSystemId + '_' + ingredient.physicalAttributeId;
                if (currentSourceSystem === self.PRODUCT_MAINTENANCE_SOURCE_PHYSICAL) {
                    ingredient.isEditable = true;	// source 4 editable
                }
                if (!ingredient.content && currentSourceSystem !== self.ingredientsECommerceModal.sourceSystemSelected) {
                    ingredient.noneDisplay = true;	// hide source 27
                }
                ingredient.tagNumber = 1;	// tag number for sources with same name.
                ingredient.createdDate = !ingredient.content ? '' : ingredient.createdDate;	// not show created date when empty content.
            });
        };

		/**
		 * Set number for sources with same name.
		 */
		self.setSourceNameForModal = function () {
			var index = 0;
			angular.forEach(self.ingredientsTableModal, function (ingredient) {
				index += 1;
				var tag = 1;
				for (var i = index; i < self.ingredientsTableModal.length; i++) {
					if (ingredient.sourceSystemDescription === self.ingredientsTableModal[i].sourceSystemDescription
						&& self.ingredientsTableModal[i].tagNumber === 1) {
						tag += 1;
						self.ingredientsTableModal[i].tagNumber = tag;	// set next number.
					}
				}
				if (tag > 1 || ingredient.tagNumber > 1) {
					ingredient.sourceSystemDescription += '- ' + ingredient.tagNumber;
				}
			});
		};

		/**
		 * Read more
		 */
		self.readMore = function (ingredient) {
			for (var i = 0; i < self.ingredientsTableModal.length; i++) {
				if(self.ingredientsTableModal[i] === ingredient){
					self.ingredientsTableModal[i].numLimit = ingredient.length;
					self.ingredientsTableModal[i].isReadLess = true;
					self.ingredientsTableModal[i].isReadMore = false;
					break;
				}
			}
		};

		/**
		 * Read less
		 */
		self.readLess = function (ingredient) {
			for (var i = 0; i < self.ingredientsTableModal.length; i++) {
				if(self.ingredientsTableModal[i] === ingredient){
					self.ingredientsTableModal[i].numLimit = self.MIN_LENGTH_READ;
					self.ingredientsTableModal[i].isReadLess = false;
					self.ingredientsTableModal[i].isReadMore = true;
					break;
				}
			}
		};

		/**
		 * Init data for read more and read less
		 */
		self.initDataForReadMoreAndReadLess = function () {
			for (var i = 0; i < self.ingredientsTableModal.length; i++) {
				self.ingredientsTableModal[i].numLimit = self.MIN_LENGTH_READ;
				self.ingredientsTableModal[i].isReadMore = true;
				self.ingredientsTableModal[i].isReadLess = false;
			}
		};

		self.findAttributeMappingByLogicalAttribute = function(){
			self.parent.findAttributeMappingByLogicalAttribute(self.INGREDIENT_ATTRIBUTE_ID);
		}

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.isWaitingModal = false;
			self.success = null;
			self.successPopup = null;
			self.error = self.getErrorMessage(error);
			self.errorPopup = self.getErrorMessage(error);
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
        self.setEditMode = function () {
            if (self.eCommerceViewDetails.ingredients != null) {
                self.isEditingIngredient = self.eCommerceViewDetails.ingredients.sourceSystemId === SRC_SYS_ID_EDIT && self.eCommerceViewDetails.ingredients.physicalAttributeId == EDIT_INGREDIENTS_PHY_ATTR_ID;
            }
            self.hasPermission = permissionsService.getPermissions('PD_ECOM_03', 'EDIT');
        };

		/**
		 * Check change Ingredients.
		 */
		self.checkChange = function () {
			$rootScope.contentChangedFlag = true;
			if(angular.equals(self.eCommerceViewDetails.ingredientsOrg, self.eCommerceViewDetails.ingredients)){
				$rootScope.contentChangedFlag = false;
			}
		}
	}
})();
