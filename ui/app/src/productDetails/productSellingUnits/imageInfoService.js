/*
* discontinueParametersService
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
 * Manages communication between SinglePluPanel, the PluDirective and the Maintenance UPC component
 *
 * @author s753601
 * @since 2.13.0
 */
(function(){
	angular.module('productMaintenanceUiApp').service('imageInfoService', imageInfoService);

	imageInfoService.$inject = ['$rootScope'];

	/**
	 * Constructs the singlePluPanelService.
	 *
	 * @param $rootScope The Angular scope representing this application.
	 * @returns
	 */
	function imageInfoService($rootScope){
		var self = this;

		self.entityId=null;

		return{
			setEntityId:function (entityId) {
				self.entityId=entityId;
			},
			getEntityId: function () {
				return self.entityId;
			}
		}
	}
})();
