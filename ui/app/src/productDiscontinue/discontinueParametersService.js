/*
 *
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
 * Manages communication between the panel to edit discontinue rules and either the default discontinue rules page
 * or the exception discontinue rules page.
 *
 * @author m594201
 * @since 2.0.2
 */
(function(){
    angular.module('productMaintenanceUiApp').service('DiscontinueParametersService', discontinueParametersService);

    discontinueParametersService.$inject = ['$rootScope'];

	/**
	 * Constructs the DiscontinueParametersService.
	 *
	 * @param $rootScope The Angular scope representing this application.
	 * @returns
	 */
    function discontinueParametersService($rootScope){

		var self = this;

        var parameterObject = null;
        var defaultParametersObject = null;
		var editing = false;

        return {
			/**
			 * Sets the discontinue rule to show up on the template (what the user will see and can edit).
			 *
			 * @param parameterObjectValue The discontinue rule to put into the template.
			 */
            setParametersObject:function(parameterObjectValue){
				parameterObject = angular.copy(parameterObjectValue);
				$rootScope.$broadcast('parametersObjectChanged');
			},
			/**
			 * Returns the discontinue rule in the template.
			 *
			 * @returns The discontinue rule in the template.
			 */
            getParametersObject:function(){

				// Not all of the data in the DB has all the necessary records. If it doesn't, then the parameter
				// will be null. In that case, set those values to the corresponding ones from the default rules.
                if(defaultParametersObject != null && parameterObject != null){
					if(!parameterObject.storeSales){
						parameterObject.storeSales = defaultParametersObject.storeSales;
						parameterObject.salesSwitch = defaultParametersObject.salesSwitch;
					}
					if(!parameterObject.storeReceipts){
						parameterObject.storeReceipts = defaultParametersObject.storeReceipts;
						parameterObject.receiptsSwitch = defaultParametersObject.receiptsSwitch;
					}
					if(!parameterObject.storeUnits){
						parameterObject.storeUnits = defaultParametersObject.storeUnits;
						parameterObject.storeUnitSwitch = defaultParametersObject.storeUnitSwitch;
					}
					if(!parameterObject.warehouseUnits){
						parameterObject.warehouseUnits = defaultParametersObject.warehouseUnits;
						parameterObject.warehouseUnitSwitch = defaultParametersObject.warehouseUnitSwitch;
					}
					if(!parameterObject.newItemPeriod){
						parameterObject.newItemPeriod = defaultParametersObject.newItemPeriod;
						parameterObject.newProductSetupSwitch = defaultParametersObject.newProductSetupSwitch;
					}
					if(!parameterObject.purchaseOrders){
						parameterObject.purchaseOrders = defaultParametersObject.purchaseOrders;
						parameterObject.purchaseOrderSwitch = defaultParametersObject.purchaseOrderSwitch;
					}
                }
                return parameterObject;
            },
			/**
			 * Sets the default discontinue rule. These are the generic ones to apply if an exception does not exist.
			 * They come from the prod_del_cntl_parm table.
			 *
			 * @param defaultParametersObjectValue The default discontinue rule.
			 */
            setDefaultParametersObject:function(defaultParametersObjectValue){
                defaultParametersObject = defaultParametersObjectValue;
            },
			/**
			 * Fires an event to tell the panel to allow the user to edit a rule.
			 */
			setEditing:function(isEditing){
				self.editing = isEditing;
				$rootScope.$broadcast('parametersObjectEdit');
			},
			/**
			 * Returns whether or not the user is editing a rule.
			 * @returns {boolean} True if the user is editing and false otherwise.
			 */
			isEditing:function(){
				return self.editing;
			}
        }
    }
})();
