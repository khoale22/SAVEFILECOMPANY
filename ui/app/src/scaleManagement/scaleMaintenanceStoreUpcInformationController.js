/*
 *
 * attributeMaintenanceDetailsController.js
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 * @author a786878
 * @since 2.16.0
 */
'use strict';

/**
 * The controller for the Attribute Maintenance Details Screen
 */
(function() {
    angular.module('productMaintenanceUiApp').directive('scaleMaintenanceStoreUpcInformationDirective', scaleMaintenanceStoreUpcInformationController);

    scaleMaintenanceStoreUpcInformationController.$inject = ['ScaleMaintenanceApi'];

    /**
     * Constructs the controller.
     */
    function scaleMaintenanceStoreUpcInformationController(scaleMaintenanceApi) {

        return {

            scope: {
                scaleMaintenance: '<',
                onReturn: '&'
            },
            restrict: 'E',
            // Inline template which is bound to message variable in the component controller
            templateUrl: 'src/scaleManagement/scaleMaintenanceStoreUpcInformation.html',
            controllerAs : 'scaleMaintenanceStoreUpcInformationDirective',
            // The controller that handles our component logic

            controller: function ($scope) {
                var self = this;
                /**
                 * Whether or not the controller is waiting for data
                 * @type {boolean}
                 */
                self.isWaiting = false
                /**
                 * the servingSizeDescription.
                 * @type {null}
                 */
                var servingSizeDescription = null;

                /**
                 * Initiates the construction of the attribute maintenance controller
                 */
                self.$onInit = function () {
                    self.scaleMaintenance = $scope.scaleMaintenance;
                    const nutrientStatement = self.scaleMaintenance.scaleMaintenanceUpc.scaleProductAsJson.nutrientStatement;
                    scaleMaintenanceApi.findServingSizeDescriptionByNutrientStatement(
                        nutrientStatement,self.loadServingSizeDescription, fetchError)
                };

                self.returnToList = function (){
                    $scope.onReturn();
                };

                self.loadServingSizeDescription = function (result) {
                    self.isWaiting = false;
                    self.error = null;
                    self.servingSizeDescription = result.data;
                };

                /**
                 * Callback for when the backend returns an error.
                 *
                 * @param error The error from the backend.
                 */
                function fetchError(error) {
                    self.isWaiting = false;
                    if (error && error.data) {
                        if(error.data.message) {
                            error = error.data.message;
                        } else {
                            error = error.data.error;
                        }
                    }
                    else {
                        error = "An unknown error occurred.";
                    }
                }
            }
        }
    }
})();

