/*
 *  productSubBrandApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for code table product sub brand.
 *
 * @author vn00602
 * @since 2.12.0
 */
(function () {
    angular.module('productMaintenanceUiApp').factory('productLineBrandApi', productLineBrandApi);

    productLineBrandApi.$inject = ['urlBase', '$resource'];

    /**
     * Constructs the API.
     *
     * @param urlBase The base URL to contact the backend.
     * @param $resource Angular $resource to extend.
     * @returns {*} The API.
     */
    function productLineBrandApi(urlBase, $resource) {
        urlBase = urlBase + '/pm/codeTable/productLineBrand';
        return $resource(
            urlBase, null,
            {
                'getProductLineBrandsPage': {
                    method: 'GET',
                    url: urlBase + '/getProductLineBrandsPage',
                    isArray: false
                },
                'deleteProductLineBrand': {
                    method: 'POST',
                    url: urlBase + '/deleteProductLineBrand',
                    isArray: false
                },
                'addProductLineBrand': {
                    method: 'GET',
                    url: urlBase + '/addProductLineBrand',
                    isArray: false
                }
            }
        );
    }
})();
