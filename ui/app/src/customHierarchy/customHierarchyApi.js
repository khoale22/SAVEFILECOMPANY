/**
 * Created by m314029 on 8/7/2017.
 */

'use strict';

/**
 * Constructs the API to call the backend for custom hierarchy.
 *
 * @author m314029
 * @since 2.11.0
 */
(function(){
	angular.module('productMaintenanceUiApp').factory('CustomHierarchyApi', customHierarchyApi);

	customHierarchyApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function customHierarchyApi(urlBase, $resource) {
		var unassignProductUrlBase = urlBase;
		urlBase = urlBase + '/pm/customHierarchy';
		return $resource(
			urlBase ,null,
			{
				'findAllHierarchyContexts': {
					method: 'GET',
					url: urlBase + '/hierarchyContext/findAll',
					isArray:true
				},
				'findRelationshipByHierarchyContext': {
					method: 'POST',
					url: urlBase + '/entityRelationship/findByHierarchyContext',
					isArray:true
				},
				'getCustomHierarchyBySearch': {
					method: 'GET',
					url: urlBase + '/hierarchyContext/getCustomHierarchyBySearch',
					isArray:true
				},
				'getCustomHierarchyByChild': {
					method: 'GET',
					url: urlBase + '/hierarchyContext/getCustomHierarchyByChild',
					isArray:false
				},
				'updateCustomHierarchy': {
					method: 'POST',
					url: urlBase + '/hierarchyContext/updateCustomHierarchy',
					isArray:true
				},
				'addCustomHierarchy': {
					method: 'POST',
					url: urlBase + '/hierarchyContext/addCustomHierarchy',
					isArray:false
				},
				'loadCustomerHierarchyContext': {
					method: 'GET',
					url: urlBase + '/hierarchyContext/customerHierarchyContext',
					isArray:false
				},
				'updateCurrentLevel': {
					method: 'POST',
					url: urlBase + '/entityRelationship/updateCurrentLevel',
					isArray:false
				},
				'getCurrentLevelImages': {
					method: 'GET',
					url: urlBase + '/imageInformation/getImageInfo',
					isArray:true
				},
				'getImage': {
					method: 'POST',
					url: urlBase + '/imageInformation/getImages',
					isArray:false
				},
				'getImageCategories': {
					method: 'GET',
					url: urlBase + '/imageInformation/getImageCategories',
					isArray:true
				},
				'getImageStatuses': {
					method: 'GET',
					url: urlBase + '/imageInformation/getImageStatuses',
					isArray:true
				},
				'getImagePriorities': {
					method: 'GET',
					url: urlBase + '/imageInformation/getImagePriorities',
					isArray:true
				},
				'getImageSources': {
					method: 'GET',
					url: urlBase + '/imageInformation/getImageSources',
					isArray:true
				},
				'updateImageMetadata': {
					method: 'POST',
					url: urlBase + '/imageInformation/updateImages',
					isArray:false
				},
				'uploadImageMetadata': {
					method: 'POST',
					url: urlBase + '/imageInformation/uploadImage',
					isArray:false
				},
				'findAllParentsByChild': {
					method: 'GET',
					url: urlBase + '/entityRelationship/findAllParentsByChild',
					isArray: true
				},
				'massUpdate': {
					method: 'POST',
					url: unassignProductUrlBase + "/pm/massUpdate",
					isArray: false
				},
				'findProductsByParent': {
					method: 'POST',
					url: urlBase + "/entityRelationship/findProductsByParent",
					isArray: false
				},
				'linkLevels': {
					method: 'POST',
					url: urlBase + '/entityRelationship/linkLevels',
					isArray: false
				},
				'moveLevels': {
					method: 'POST',
					url: urlBase + '/entityRelationship/moveLevels',
					isArray: false
				},
				'saveRemoveLevel': {
					method: 'POST',
					url: urlBase + '/hierarchyContext/saveRemoveLevel',
					isArray:false
				},
				'deleteAllRemoveLevel': {
					method: 'POST',
					url: urlBase + '/hierarchyContext/deleteAllRemoveLevel',
					isArray:false
				},
				'deleteSingleRemoveLevel': {
					method: 'POST',
					url: urlBase + '/hierarchyContext/deleteSingleRemoveLevel',
					isArray:false
				},
				'findAllCustomerProductGroupsByParent': {
					method: 'POST',
					url: urlBase + "/entityRelationship/findAllCustomerProductGroupsByParent",
					isArray: false
				},
				'findAllCustomerProductGroupsNotOnParentEntity': {
					method: 'GET',
					url: urlBase + "/entityRelationship/findAllCustomerProductGroupsNotOnParentEntity",
					isArray: true
				},
				'getImmediateNonProductChildren': {
					method: 'GET',
					url: urlBase + "/entityRelationship/getImmediateNonProductChildren",
					isArray: true
				},
				'getCurrentLevel': {
					method: 'POST',
					url: urlBase + "/entityRelationship/getResolvedCurrentLevelByKey",
					isArray: false
				},
				'findAttributesByHierarchyContextAndEntity': {
					method: 'GET',
					url: urlBase + "/attribute/findAttributesByHierarchyContextAndEntity",
					isArray: true
				}
			}
		);
	}
})();
