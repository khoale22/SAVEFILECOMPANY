/*
 * modals.js
 *  *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 */

(function () {
    'use strict';

    angular.module('productMaintenanceUiApp').directive('warningModal',

    function (){
        return{
            restrict: 'E',
            templateUrl: "src/utils/modals/warningModal.html"
        };
    });
})();
