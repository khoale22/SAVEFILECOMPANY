/*
 *
 * productSellingUnitsApi.js
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
 * Constructs the API to call the backend for productSellingUnitsApi .
 *
 * @author m594201
 * @since 2.7.0
 */

(function () {

	angular.module('productMaintenanceUiApp').factory('productSellingUnitsApi', productSellingUnitsApi);

	productSellingUnitsApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to selling unit data.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function productSellingUnitsApi(urlBase, $resource) {
		return $resource(null, null, {
			'getGladsonData': {
				method: 'GET',
				url: urlBase + '/pm/gladson/getGladsonRetailDimensionalData',
				isArray:true
			},
			'getCrossLinkedListOfUPCs': {
				method: 'GET',
				url: urlBase + '/pm/upcInfo/getCrossLinkedListOfUPCs',
				isArray: false
			},
			'getByUPC': {
				method: 'GET',
				url: urlBase + '/pm/upcInfo/getByUPC',
				isArray: true
			},
			'getLEBUPCList': {
				method: 'GET',
				url: urlBase + '/pm/upcInfo/getLebUpcs',
				isArray: true
			},
			'getLEBSizesList': {
				method: 'GET',
				url: urlBase + '/pm/upcInfo/getLebSizes',
				isArray: true
			},
			'getUpcInfoAudits': {
				method: 'POST',
				url: urlBase + '/pm/upcInfo/upcInfoAudit',
				isArray: true
			},
			'getImageInformation': {
				method: 'POST',
				url: urlBase + '/pm/imageInfo/getImageInfo',
				isArray:false
			},
			'getImages': {
				method: 'POST',
				url: urlBase + '/pm/imageInfo/getImages',
				isArray:false
			},
			'getImageCategories': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getImageCategories',
				isArray:true
			},
			'getImageStatuses': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getImageStatuses',
				isArray:true
			},
			'getImageDestinations': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getImageDestinations',
				isArray:true
			},
			'getImagePriorities': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getImagePriorities',
				isArray:true
			},
			'findAllItemMastersLinkedToUpc': {
				method: 'GET',
				url: urlBase + '/pm/itemMaster/getAllItemMastersLinkedToUpc',
				isArray: true
			},
			'getImageSources': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getImageSources',
				isArray:true
			},
			'uploadImage': {
				method: 'POST',
				url: urlBase + '/pm/imageInfo/uploadImage',
				isArray:false
			},
			'updateImageInfo': {
				method: 'POST',
				url: urlBase + '/pm/imageInfo/updateImageInfo',
				isArray:false
			},
			'addDestinations': {
				method: 'POST',
				url: urlBase + '/pm/imageInfo/updateDestinations',
				isArray:false
			},
			'copyToHierarchy': {
				method: 'POST',
				url: urlBase + '/pm/imageInfo/copyToHierarchy',
				isArray:false
			},
			'copyToHierarchyPath': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/copyToHierarchyPath',
				isArray:false
			},
			'getThirdPartySellableStatus': {
				method: 'GET',
				url: urlBase + '/pm/onlineInfo/getThirdPartySellableStatus',
				isArray:false
			},
			'findAllRetailUnitOfMeasure': {
				method: 'GET',
				url: urlBase + '/pm/upcInfo/getUomList',
				isArray: true
			},
			'saveUpcInfo': {
				method: 'POST',
				url: urlBase + '/pm/upcInfo/saveUpcInfo',
				isArray: false
			},
			'saveDimensions': {
				method: 'POST',
				url: urlBase + '/pm/upcInfo/saveDimensions',
				isArray: false
			},
			'getDimensionsAudits': {
				method: 'GET',
				url: urlBase + '/pm/upcInfo/getDimensionsAudits',
				isArray: true
			},
			'getActiveOnlineImages': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getActiveOnlineImages',
				isArray:false
			},
			'getSubmittedImages': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getSubmittedImages',
				isArray:false
			},
            'getRejectedImages': {
                method: 'GET',
                url: urlBase + '/pm/imageInfo/getRejectedImages',
                isArray:false
            },
            'getCountSubmittedImages': {
                method: 'GET',
                url: urlBase + '/pm/imageInfo/getCountSubmittedImages',
                isArray:false
            },
            'checkImageValid': {
                method: 'GET',
                url: urlBase + '/pm/imageInfo/checkImageValid',
                isArray:false
            },
		});
	}
})();
