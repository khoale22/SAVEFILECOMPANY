/**
 * Created by m314029 on 09/13/2018.
 */

'use strict';

/**
 * Constructs the API to call the backend for meta data.
 *
 * @author m314029
 * @since 2.21.0
 */
(function(){
	angular.module('productMaintenanceUiApp').factory('AttributeMetaDataApi', attributeMetaDataApi);

	attributeMetaDataApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function attributeMetaDataApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/attribute/metaData';
		return $resource(
			urlBase ,null,
			{
				'findAll': {
					method: 'GET',
					url: urlBase,
					isArray:false
				},
				'save': {
					method: 'POST',
					url: urlBase + '/saveAttributeMetadata',
					isArray: false
				}
			}
		);
	}
})();
