/*
 *
 * productCasePackApi.js
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
 * Constructs the API to call the backend for productCasePackApi .
 * */

(function () {

	angular.module('productMaintenanceUiApp').factory('ProductCasePackCandidateApi', productCasePackCandidateApi);

	productCasePackCandidateApi.$inject = ['urlBase', '$resource'];

	function productCasePackCandidateApi(urlBase, $resource) {
		return $resource(null, null, {
			// Queries all Import Items by item code.
			'queryAllImportItemsByItemCode': {
				method: 'GET',
				url: urlBase + '/pm/importItem/queryByItemCodeAndItemType',
				isArray:true
			},
			// queries all  case pack container sizes.
			'findAllImportContainerSizes': {
				method: 'GET',
				url: urlBase + '/pm/importItem/containerSizes',
				isArray:true
			},
			// queries all  case pack inco terms.
			'findAllIncoTerms': {
				method: 'GET',
				url: urlBase + '/pm/importItem/incoTerms',
				isArray:true
			},
			// queries all countries by name.
			'findAllCountriesOrderByName': {
				method: 'GET',
				url: urlBase + '/pm/country/allCountriesOrderByName',
				isArray:true
			},
			// Finds information for the vendor component.
			'findVendorInfoByItemId': {
				method: 'GET',
				url: urlBase + '/pm/vendorInfo/findByItemId',
				isArray: true
			},
			'updateVendorInfo': {
				method: 'POST',
				url: urlBase +  '/pm/vendorInfo/updateVendorInfo',
				isArray: false
			},
			//Updates import items
			'updateImportItem' : {
				method: 'POST',
				url: urlBase + '/pm/importItem/update',
				isArray:false
			},
			// Finds all factories
			'findAllFactories' : {
				method: 'GET',
				url: urlBase + '/pm/factory/allfactories',
				isArray:true
			},
			// Updates the vendor item factory table with new factories.
			'updateVendorItemFactories' : {
				method: 'POST',
				url: urlBase + '/pm/vendorItemFactory/update',
				isArray:false
			},
			'saveMrt': {
				method: 'POST',
				url: urlBase +'/pm/mrtInfo/saveMrtInfo',
				isArray: false
			},
			'saveNewToMrt': {
				method: 'POST',
				url: urlBase +'/pm/mrtInfo/saveNewToMrt',
				isArray: false
			},
			'deleteFromMrt': {
				method: 'POST',
				url: urlBase +'/pm/mrtInfo/deleteFromMrt',
				isArray: false
			},
			'getMrtAuditInformation': {
				method: 'GET',
				url: urlBase + '/pm/mrtInfo/getMrtAuditInformation',
				isArray: true
			},
			'getAllItemTypes': {
				method: 'GET',
				url: urlBase + '/pm/casePack/getAllItemTypes',
				isArray: true
			},
			'getAllCandidateProductTypes': {
				method: 'GET',
				url: urlBase + '/pm/casePackCandidate/getAllCandidateProductTypes',
				isArray: true
			},
			'getAllDiscontinueReasons': {
				method: 'GET',
				url: urlBase + '/pm/casePack/getAllDiscontinueReasons',
				isArray: true
			},
			'calculateCheckDigit': {
				method: 'GET',
				url: urlBase + '/pm/casePack/calculateCheckDigit',
				isArray: false
			},
			'getAllOneTouchTypes': {
				method: 'GET',
				url: urlBase + '/pm/casePack/getAllOneTouchTypes',
				isArray: true
			},
			'confirmCheckDigit': {
				method: 'GET',
				url: urlBase + '/pm/casePack/confirmCheckDigit',
				isArray: false
			},
			'updateCasePackInfoCandidate': {
				method: 'POST',
				url: urlBase + '/pm/casePackCandidate/updateCasePackInfoCandidate',
				isArray: false
			},
			'updateCasePackImportCandidate': {
				method: 'POST',
				url: urlBase + '/pm/casePackCandidate/updateCasePackImportCandidate',
				isArray: false
			},
			'updateCasePackImportFactoriesCandidate': {
				method: 'POST',
				url: urlBase + '/pm/casePackCandidate/updateCasePackImportFactoriesCandidate',
				isArray: false
			},
			'updateCasePackVendorCandidate': {
				method: 'POST',
				url: urlBase + '/pm/casePackCandidate/updateCasePackVendorCandidate',
				isArray: false
			},
			'updateCasePackWarehouseCandidate': {
				method: 'POST',
				url: urlBase + '/pm/casePackCandidate/updateCasePackWarehouseCandidate',
				isArray: false
			},
			'updateCasePackWarehouseCommentsCandidate': {
				method: 'POST',
				url: urlBase + '/pm/casePackCandidate/updateCasePackWarehouseCommentsCandidate',
				isArray: false
			},
			'rejectCandidate': {
				method: 'GET',
				url: urlBase + '/pm/casePackCandidate/rejectCandidate',
				isArray: false
			},
			'activateCandidate': {
				method: 'GET',
				url: urlBase + '/pm/casePackCandidate/activateCandidate',
				isArray: false
			},
			'saveDRU': {
				method: 'POST',
				url: urlBase +'/pm/druInfo',
				isArray: false
			},
			'getItemMaster': {
				method: 'POST',
				url: urlBase +'/pm/itemMaster',
				isArray: false
			},
			'getCasePackInformation': {
				method: 'POST',
				url: urlBase +'/pm/casePack/getCasePackInformation',
				isArray: false
			},
			'getVendorAuditInformation': {
				method: 'POST',
				url: urlBase + '/pm/vendorInfo/getAuditInformation',
				isArray: true
			},
			'getImportAuditInformation':{
				method: 'POST',
				url: urlBase + '/pm/importItem/importItemAudit',
				isArray: true
			},
			'getWarehouseInfo': {
				method: 'GET',
				url: urlBase + '/pm/warehouse/getWarehouseInformation',
				isArray: true
			},
			'getOrderQuantityTypes': {
				method: 'GET',
				url: urlBase + '/pm/warehouse/getOrderQuantityTypes',
				isArray: true
			},
			'getFlowTypes': {
				method: 'GET',
				url: urlBase + '/pm/warehouse/getFlowTypes',
				isArray: true
			},
			'getRemarks': {
				method: 'POST',
				url: urlBase + '/pm/warehouse/getCommentAndRemarks',
				isArray: true
			},
			'updateRemarks': {
				method: 'POST',
				url: urlBase + '/pm/warehouse/saveCommentAndRemarks',
				isArray: false
			},
			'findPssDepartmentByDepartment': {
				method: 'GET',
				url: urlBase + '/pm/pssDepartment/findPssDepartmentByDepartment',
				isArray: true
			},
			'findPssDepartmentCode': {
				method: 'GET',
				url: urlBase + '/pm/pssDepartment/findPssDepartmentCode',
				isArray: false
			},
			'getDruAudits': {
				method: 'POST',
				url: urlBase + '/pm/druInfo/getDruAudits',
				isArray: true
			},
			'getCasePackAudits': {
				method: 'POST',
				url: urlBase + '/pm/casePack/casePackAudits',
				isArray: true
			},
			'saveWarehouseItemLocation':{
				method: 'POST',
				url: urlBase + '/pm/warehouse/saveWarehouseItemLocation',
				isArray: false
			},
			'findVendorItemFactoryByParameter' : {
			method: 'GET',
				url: urlBase + '/pm/vendorItemFactory/findByVendorItem',
				isArray:true
		    },
			'findAllDepartment': {
				method: 'GET',
				url: urlBase + '/pm/pssDepartment/findAllDepartment',
				isArray: true
			},
			'findAllMerchandiseType': {
				method: 'GET',
				url: urlBase + '/pm/pssDepartment/findAllMerchandiseType',
				isArray: true
			},
			'updateDepartment': {
				method: 'POST',
				url: urlBase + '/pm/pssDepartment/updateDepartment',
				isArray:false
			},
			'findItemMaster':{
				method: 'GET',
				url: urlBase + '/pm/pssDepartment/findItemMaster',
				isArray:false
			},
			'getCandidateInformation':{
				method: 'GET',
				url: urlBase + '/pm/casePackCandidate/getCandidateInformation',
				isArray:true
			},
			'checkCandidateAssociatedWithProd':{
				method: 'GET',
				url: urlBase + '/pm/vendorInfo/checkCandidateAssociatedWithProd',
				isArray:false
			},
			'findBicepVendorsByApVendorAndClass':{
				method: 'GET',
				url: urlBase + '/pm/vendorInfo/findBicepVendorsByApVendorAndClass',
				isArray:true
			},
			'copyItemDataToPsTable':{
				method: 'POST',
				url: urlBase + '/pm/vendorInfo/copyItemDataToPsTable',
				isArray:false
			}
		});
	}

})();
