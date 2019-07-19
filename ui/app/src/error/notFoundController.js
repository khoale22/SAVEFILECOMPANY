/*
 *   notFoundController.js
 *
 *   Copyright (c) 2019 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';
/**
 * The controller for the Not found page.
 *
 * @author vn70529
 * @since 2.29.0
 */
(function () {
    angular.module('productMaintenanceUiApp').controller('NotFoundController', notFoundController);
    notFoundController.$inject = ["$stateParams"];
    function notFoundController( $stateParams) {
        var self = this;

        const PAGE_NOT_ACCESS_MESSAGE = "User does not have access, please contact the service desk.";
        const PAGE_NOT_FOUND_MESSAGE = "User does not have access for module, please contact the service desk.";
        self.message = '';

        self.$onInit = function() {
            if($stateParams && $stateParams.pageNotAccess){
                self.message = PAGE_NOT_ACCESS_MESSAGE;
            } else{
                self.message = PAGE_NOT_FOUND_MESSAGE;
            }
        }
    }
})();
