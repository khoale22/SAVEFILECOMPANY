/*
 * brandApi.js
 *
 * Copyright (c) 2019 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';
/**
 * Creates a factory to create methods to contact product maintenance's brand API.
 *
 * @author vn70529
 * @since 2.41.0
 */
(function () {

    angular.module('productMaintenanceUiApp').factory('brandApi', brandApi);
    brandApi.$inject = ['urlBase', '$resource'];

    /**
     * Constructs the API to call the backend for brand.
     *
     * Supported method:
     *
     * @param urlBase The base URL to use to contact the backend.
     * @param $resource Angular $resource used to construct the client to the REST service.
     * @returns {*} The brandApi factory.
     */
    function brandApi(urlBase, $resource) {
        var newUrlBase = urlBase + '/pm/codeTable/brand';
        return $resource(urlBase, null, {
            'findAll': {
                url: newUrlBase + '/findPage',
                method: 'GET',
                isArray: false
            },
            'addBrands': {
                url: newUrlBase + '/addBrands',
                method: '' + 'POST',
                isArray: false
            },
            'updateBrand': {
                method: 'PUT',
                url: newUrlBase + '/updateBrand',
                isArray: false
            },
            'deleteBrand': {
                method: 'DELETE',
                url: newUrlBase + '/deleteBrand',
                isArray: false
            }
        });
    }

})();
