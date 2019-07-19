/**
 * Created by s573181 on 10/25/2018.
 */

'use strict';

/**
 * Constructs the API to call the backend for Audience.
 *
 * @author s573181
 * @since 2.22.0
 */
(function(){
	angular.module('productMaintenanceUiApp').factory('AudienceApi', audienceApi);

	audienceApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function audienceApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/attribute/audience';
		return $resource(
			urlBase ,null,
			{
				'findAll': {
					method: 'GET',
					url: urlBase,
					isArray:true
				}
			}
		);
	}
})();
