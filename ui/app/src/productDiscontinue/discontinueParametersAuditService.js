/*
 *
 *  discontinueParametersAuditService.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 */

'use strict';


/**
 *  Manages communication between the discontinueParametersAudit.html and the desired audit results, such as
 *  deleted exceptions, parametersAudit, and exceptionParametersAudit
 *
 * @author s573181
 * @since  2.0.3
 */

(function(){
	angular.module('productMaintenanceUiApp').service('DiscontinueParametersAuditService', discontinueParametersAuditService);

	discontinueParametersAuditService.$inject = ['$rootScope'];

	var self = this;
	/**
	 *  Contains the audit records to be displayed.
	 *
	 * @type array of DiscontinueParametersAuditRecords.
	 */
	var auditParameters = null;

	/**
	 *  Constructs the discontinueParametersAuditService
	 *
	 * @param $rootScope The Angular scope representing this application.
	 */
	function discontinueParametersAuditService($rootScope){

		return{
			/**
			 * Sets the auditParameters in the discontinueParametersAudit.
			 */
			setAuditParameters:function(){
				auditParameters = {exceptionType: "Admin", exceptionTypeId: "", exceptionName: ""};
				$rootScope.$broadcast('displayParametersAudit');
			},
			/**
			 * Sets the auditParameters for the Exception parameters.
			 *
			 * @param auditExceptionParametersValue the auditParameters to be set.
			 */
			setAuditExceptionParameters:function(auditExceptionParametersValue){
				auditParameters = angular.copy(auditExceptionParametersValue);
				$rootScope.$broadcast('displayExceptionParametersAudit');
			},
			/**
			 * Sets the auditParameters for the deletes in the discontinueParametersAudit.
			 */
			getDeletesAuditParameters:function(){
				auditParameters = {exceptionType: "Deletes Audit", exceptionTypeId: "", exceptionName: ""};
				$rootScope.$broadcast('displayDeletesAudit');
			},
			/**
			 * Returns the auditParameters from the discontinueParametersAudit.
			 *
			 * @returns the auditParameters.
			 */
			getAuditParameters:function(){
				return auditParameters;
			}

		}
	}
})();
