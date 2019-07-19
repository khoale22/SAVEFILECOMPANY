/*
 *
 * discontinueParametersTemplateController
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * Controller that supports the panel that is used on the pages that support both the default and exception
 * product discontinue rules pages.
 *
 * @author m594201
 * @since 2.0.2
 */

(function() {
    angular.module('productMaintenanceUiApp').controller('DiscontinueParametersTemplateController', parametersTemplateController);

    parametersTemplateController.$inject = ['DiscontinueParametersService', '$scope', '$rootScope', 'discontinueDefinitions'];

	/**
	 * Constructs a new Controller for the panel that shows details of the default or
	 * exception product discontinue rules.
	 *
	 * @param discontinueParametersService The service that passes infomation between other controllers and this one.
	 * @param $scope The Angular scope representing this controller.
	 * @param $rootScope The Angular scope representing this application.
	 */
    function parametersTemplateController(discontinueParametersService, $scope, $rootScope, discontinueDefinitions) {

        var self = this;

        /**
         * The object thatwill hold the defenitions for desicontinue terms.
         * @type {?}
         */
        self.discontinueDefinition = discontinueDefinitions;

        /**
		 * Initializes the controller.
		 */
        self.init = function(){
            self.data = null;
            self.data = discontinueParametersService.getParametersObject();
        };


		/**
		 * Callback for when the user clicks on the edit button.
		 */
		self.editButtonClicked = function(){

			// Flip the status of all the checkboxes
			var input;
			input = document.getElementById("salesSwitch");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("storeSales");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("receiptsSwitch");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("storeReceipts");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("storeUnitSwitch");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("storeUnits");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("warehouseUnitSwitch");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("warehouseUnits");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("newProductSetupSwitch");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("newItemPeriod");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("purchaseOrderSwitch");
			input.disabled = !discontinueParametersService.isEditing();
			input = document.getElementById("purchaseOrders");
			input.disabled = !discontinueParametersService.isEditing();
		};

		/**
		 * Callback for when the controller on the default or exception pages announces the user asked to save any
		 * changes.
		 */
		self.getChanges = function() {
			var newRules = self.data;
			discontinueParametersService.setParametersObject(newRules);
			$rootScope.$broadcast('setParametersChanges');
		};

		// Register callbacks.
		$scope.$on('getParameterChanges',self.getChanges);
		$scope.$on('parametersObjectChanged', self.init);
		$scope.$on('parametersObjectEdit',self.editButtonClicked);
    }
})();
