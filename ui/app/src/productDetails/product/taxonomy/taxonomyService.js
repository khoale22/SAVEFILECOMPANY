/*
* taxonomyService
*
* Copyright (c) 2018 HEB
* All rights reserved.
*
* This software is the confidential and proprietary information
* of HEB.
*
*/

'use strict';

/**
 * Manages communication between Taxonomy component and children components.
 *
 * @author s753601
 * @since 2.13.0
 */
(function(){
	angular.module('productMaintenanceUiApp').service('taxonomyService', taxonomyService);

	taxonomyService.$inject = ['$rootScope'];

	/**
	 * Constructs the taxonomyService.
	 *
	 * @returns
	 */
	function taxonomyService($rootScope){
		var self = this;
		self.serviceScope = $rootScope.$new(true);

		const SAVE_MESSAGE = "saveProductTaxonomy";
		const RESET_MESSAGE = "resetProductTaxonomy";

		return {

			/**
			 * Save button is hit from the taxonomy parent component.
			 */
			save:function(){
				self.serviceScope.$broadcast(SAVE_MESSAGE);
			},

			/**
			 * Reset button is hit from the taxonomy parent component.
			 */
			reset:function(){
				self.serviceScope.$broadcast(RESET_MESSAGE);
			},

			/**
			 * Getter for this service's root scope.
			 *
			 * @returns {*|Object} The service scope.
			 */
			getServiceScope:function(){
				return self.serviceScope;
			},

			/**
			 * Getter for SAVE_MESSAGE.
			 *
			 * @returns {string} The string 'saveProductTaxonomy'.
			 */
			getSaveMessage:function(){
				return SAVE_MESSAGE;
			},

			/**
			 * Getter for RESET_MESSAGE.
			 *
			 * @returns {string} The string 'resetProductTaxonomy'.
			 */
			getResetMessage:function(){
				return RESET_MESSAGE;
			}
		}
	}
})();
