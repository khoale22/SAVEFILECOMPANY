/*
 *
 * imageInfoApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author s753601
 * @since 2.15.0
 */
'use strict';
(function () {
	angular.module('productMaintenanceUiApp').factory('imageInfoApi', imageInfoApi);
	imageInfoApi.$inject = ['urlBase', '$resource'];

	function imageInfoApi(urlBase, $resource) {
		return $resource(null, null, {
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
			'getImagePriorities': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getImagePriorities',
				isArray:true
			},
			'getImageSoures': {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/getImageSources',
				isArray:true
			},
			'getImages': {
				method: 'POST',
				url: urlBase + '/pm/imageInfo/getImages'
			},
			'getImageInformation': {
				method: 'POST',
				url: urlBase + '/pm/imageInfo/getImageInfo',
				isArray:false
			},
		});
	}
})();
