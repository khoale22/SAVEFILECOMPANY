/*
 *
 *  * discontinueParametersApi
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 */


'use strict';

/**
 * API to support viewing and updating product discontinue rules.
 *
 * @author m594201
 * @since 2.0.2
 */
(function () {

    angular.module('productMaintenanceUiApp').factory('DiscontinueParametersFactory', discontinueParametersFactory);

	discontinueParametersFactory.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to product discontinue rules.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
    function discontinueParametersFactory(urlBase, $resource) {
        return $resource(null, null, {
			// Returns a list of the default parameters for product discontinue.
			'getDiscontinueParameters': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/parameters',
				isArray:false
			},
			// Saves the default product discontinue parameters.
			'saveParameters': {
				method: 'PUT',
				url: urlBase + '/pm/productDiscontinue/parameters',
				isArray:false
			},
			// Returns a list of available exception types.
            'getExceptionTypes': {
                method: 'GET',
                url: urlBase + '/pm/productDiscontinue/exceptions/types',
                isArray: true
            },
			// Returns a list of all exceptions
			'getDiscontinueExceptionsByType': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/exceptions',
				isArray: true
			},
			// Saves a modified discontinue exception
			'updateDiscontinueExceptionRules': {
				method: 'PUT',
				url: urlBase + '/pm/productDiscontinue/exceptions',
				isArray: false
			},
			// Adds a new discontinue exception
			'addDiscontinueExceptionRules': {
				method: 'POST',
				url: urlBase + '/pm/productDiscontinue/exceptions',
				isArray: false
			},
			// Deletes a set of exception rules
			'deleteDiscontinueExceptionRules': {
				method: 'DELETE',
				url: urlBase + '/pm/productDiscontinue/exceptions',
				isArray: false
			},
			'getVendorsByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/vendor',
				isArray: false
			},
			'getClassesByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/itemClass/findByRegularExpression',
				isArray: false
			},
			'getCommoditiesByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/commodity/findByRegularExpression',
				isArray: false
			},
			'getSubDepartmentsByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/subDepartment',
				isArray: false
			},
			'getSubCommoditiesByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/subCommodity/findByRegularExpression',
				isArray: false
			},
			'getDiscontinueExceptionsParameterAudit': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/exceptionAudits',
				isArray: true
			},
			'getDiscontinueParameterAudit': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/parametersAudit',
				isArray: true
			},
			'getDiscontinueExceptionsParameterDeleteAudits': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/deletedParametersAudit',
				isArray: true
			},
			'getDeletedExceptionsAuditTypes': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/deletedParametersAuditTypes',
				isArray: true
			},
			'getBDMByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/bdm',
				isArray: false
			}
        });

    }
})();
