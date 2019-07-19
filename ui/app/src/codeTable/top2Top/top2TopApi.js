/*
 * top2TopApi.js
 *
 * Copyright (c) 2019 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';
/**
 * Creates a factory to create methods to contact product maintenance's Top2Top API.
 *
 * @author vn73545
 * @since 2.41.0
 */
(function () {

    angular.module('productMaintenanceUiApp').factory('top2TopApi', top2TopApi);
    top2TopApi.$inject = ['urlBase', '$resource'];

    /**
     * Constructs the API to call the backend for top 2 top.
     *
     * Supported method:
     *
     * @param urlBase The base URL to use to contact the backend.
     * @param $resource Angular $resource used to construct the client to the REST service.
     * @returns {*} The top2TopApi factory.
     */
    function top2TopApi(urlBase, $resource) {
        var newUrlBase = urlBase + '/pm/codeTable/top2Top';
        return $resource(urlBase, null, {
            'findAll': {
                url: newUrlBase + '/findAll',
                method: 'GET',
                isArray: false
            },
            'addTop2Tops': {
                url: newUrlBase + '/add',
                method: '' +
                'POST',
                isArray: false
            },
            'updateTop2Top': {
                method: 'PUT',
                url: newUrlBase + '/update',
                isArray: false
            },
            'deleteTop2Top': {
                method: 'DELETE',
                url: newUrlBase + '/delete',
                isArray: false
            }
        });
    }
})();
