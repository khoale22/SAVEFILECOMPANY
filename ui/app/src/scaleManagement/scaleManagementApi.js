/*
 *
 * scaleManagementApi.js
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
 * Constructs the API to call the backend for scale management api.
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('ScaleManagementApi', scaleManagementApi);

	scaleManagementApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function scaleManagementApi(urlBase, $resource) {
		return $resource(null, null, {
			// Get the scale management list by plu(s).
			'queryByPluList': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/pluList',
				isArray:false
			},
			// Get the scale management list by description.
			'queryByDescription': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/description',
				isArray:false
			},
			// Retrieve a list of found and not found PLUs in the scale upc table.
			'queryForMissingPLUs': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/hits/pluList',
				isArray:false
			},
			// Updates the scale management upc from the maintenance page.
			'updateScaleUpc': {
				method: 'PUT',
				url: urlBase + '/pm/scaleManagement/updateScaleUpc',
				isArray:false
			},
			// Bulk updates the scale management upc list from the maintenance page.
			'bulkUpdateScaleUpc': {
				method: 'PUT',
				url: urlBase + '/pm/scaleManagement/bulkUpdateScaleUpc'
			},
			// Get the scale management action codes information by action codes.
			'queryByActionCodes': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/actionCodes',
				isArray:false
			},
			// Get the scale management action codes information by action codes.
			'queryByActionCodeDescription': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/actionCodeDescription',
				isArray:false
			},
			// Retrieve a list of plus by action code in the scale upc table.
			'queryForPLUsByActionCode': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/pluListByActionCode',
				isArray:false
			},
			// Retrieve a list of found and not found action codes in the scale upc table.
			'queryForMissingActionCodes': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/hits/actionCodes',
				isArray:false
			},
			// Updates an action code with a new description.
			'updateActionCode': {
				method: 'PUT',
				url: urlBase + '/pm/scaleManagement/updateScaleActionCode',
				isArray:false
			},
			// Creates a new action code with a new description.
			'addActionCode': {
				method: 'POST',
				url: urlBase + '/pm/scaleManagement/addScaleActionCode',
				isArray:false
			},
			// Deletes an action code.
			'deleteActionCode': {
				method: 'DELETE',
				url: urlBase + '/pm/scaleManagement/deleteScaleActionCode',
				isArray:false
			},
			//Retrieve a list of All action codes in the scale upc table.
			'queryAllActionCodes': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/queryAllActionCodes',
				isArray:false
			},
			// Get all label formats.
			'queryForLabelFormats': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/labelFormats',
				isArray:false
			},
			// Retrieve a list of found and not found action codes in the scale upc table.
			'queryForMissingLabelFormats': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/labelFormats/hits',
				isArray:false
			},
			// Get label formats by description.
			'queryForLabelFormatsByDescription': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/labelFormats/description'
			},
			// Get label formats by code.
			'queryForLabelFormatsByCode': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/labelFormats/code'
			},
			// Get a list of UPCs that have a given label format set for label format one.
			'queryforUpcsByFormatCodeOne': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/labelFormats/upcs/one'
			},
			// Get a list of UPCs that have a given label format set for label format two.
			'queryforUpcsByFormatCodeTwo': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/labelFormats/upcs/two'
			},
			// Get a list of UPCs that have a given ingredient statement number.
			'queryforUpcsByIngredientStatementNumber': {
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/scaleUpc'
			},
			// Updates a label format code with a new description.
			'updateFormatCode': {
				method: 'PUT',
				url: urlBase + '/pm/scaleManagement/labelFormats/updateScaleLabelFormatCode',
				isArray:false
			},
			// Creates a new label format code with a new description.
			'addLabelFormat': {
				method: 'POST',
				url: urlBase + '/pm/scaleManagement/labelFormats/addScaleLabelFormatCode',
				isArray:false
			},
			// Deletes an action code.
			'deleteFormatCode': {
				method: 'DELETE',
				url: urlBase + '/pm/scaleManagement/labelFormats/deleteScaleLabelFormatCode',
				isArray:false
			},
			//Get Ingredient report by ingredient Code.
			'queryByIngredientCode': {
				method: 'GET',
				url: urlBase + '/pm/ingredient/ingredientCodes',
				isArray:false
			},
			// Get Ingredients that a code is a sub-ingredient of.
			'queryForSuperIngredients': {
				method: 'GET',
				url: urlBase + '/pm/ingredient/superIngredients',
				isArray:false
			},
			//Get Ingredient report by ingredient description.
			'queryByIngredientDescription':{
				method: 'GET',
				url: urlBase + '/pm/ingredient/ingredientDescription',
				isArray:false
			},
			//Get Ingredient report by Ingredient statement.
			'queryByIngredientStatement':{
				method: 'GET',
				url: urlBase + '/pm/ingredient/ingredientStatement',
				isArray:false
			},
			//Query for Nutrient information by nutrient code.
			'queryForNutrientsByNutrientCode':{
				method: 'GET',
				url: urlBase + '/pm/nutrients/nutrientCodes',
				isArray:false
				//Query for Nutrient information by Description.
			},'queryByNutrientDescription':{
				method: 'GET',
				url: urlBase + '/pm/nutrients/nutrientDescription',
				isArray:false
				//Query for all Nutrient information.
			},'queryForNutrientsByAll':{
				method: 'GET',
				url: urlBase + '/pm/nutrients/queryAllNutrientCodes',
				isArray:false
			},
			// Updates an action code with a new description.
			'updateNutritionData': {
				method: 'PUT',
				url: urlBase + '/pm/nutrients/updateNutritionData',
				isArray:false
			},
			// Creates a new nutrient code.
			'addNutrientData': {
				method: 'POST',
				url: urlBase + '/pm/nutrients/addNutrientData',
				isArray:false
			},
			// Deletes a nutrient code.
			'deleteNutrientData': {
				method: 'DELETE',
				url: urlBase + '/pm/nutrients/deleteNutrientData',
				isArray:false
			},
			// Finds nutrient unit of measure by regular exp ression.
			'findNutrientUomByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/nutrientUomCode/nutrientUomIndex',
				isArray:false
			},
			// Query for the nutrient statement rules by nutrient code
			'queryForNutrientStatementByNutrientCode': {
				method: 'GET',
				url: urlBase + '/pm/nutrientsStatement/nutrientStatementByCode',
				isArray:false
			},
			'queryForNutrientStatementsByAll': {
				method: 'GET',
				url: urlBase + '/pm/nutrientsStatement/queryAllNutrientStatement',
				isArray:false
			},
			'queryForNutrientStatementByStatementId': {
				method: 'GET',
				url: urlBase + '/pm/nutrientsStatement/nutrientStatementByNutrientStatementId',
				isArray:false
			},
			'queryForMissingNutrientStatementsByNutrientStatements': {
				method: 'GET',
				url: urlBase + '/pm/nutrientsStatement/hits/nutrientStatements',
				isArray:false
			},
			'queryForMissingNutrientCodesByNutrientStatements': {
				method: 'GET',
				url: urlBase + '/pm/nutrientsStatement/hits/nutrientCodes',
				isArray:false
			},
			//Add new nutrient statement
			'addStatement':{
				method: 'POST',
				url: urlBase + '/pm/nutrientsStatement/addStatement',
				isArray:false
			},
			// Updates an nutrition statement with a new data.
			'updateNutritionStatementData': {
				method: 'PUT',
				url: urlBase + '/pm/nutrientsStatement/updateNutrientStatementData',
				isArray:false
			},
			// Gets the mandated nutrients by statement Id.
			'getMandatedNutrientsByStatementId':{
				method: 'GET',
				url: urlBase + '/pm/nutrientsStatement/mandatedNutrientsByStatementId',
				isArray:true
			},
			// Removes a nutrient statement.
			'deleteNutrientStatement':{
				method: 'DELETE',
				url: urlBase + '/pm/nutrientsStatement/deleteNutrientStatement',
				isArray:false
			},
			// Searches to see if nutrient statement is available.
			'searchForAvailableNutrientStatement':{
				method: 'GET',
				url: urlBase + '/pm/nutrientsStatement/searchForAvailableNutrientStatement',
				isArray:false
			},
			// Query for the nutrient rounding rules by nutrient code
			'queryForNutrientRoundingRules':{
				method: 'GET',
				url: urlBase + '/pm/nutrients/nutrientRoundingRules',
				isArray:true
			},
			// Checks to see if any searched nutrient codes aren't found.
			'queryForMissingNutrientCodes': {
				method: 'GET',
				url: urlBase + '/pm/nutrients/hits/nutrientCodes',
				isArray:false
			},
			// Searches to see if nutrient code is available.
			'searchForAvailableNutrientCode':{
				method: 'GET',
				url: urlBase + '/pm/nutrients/searchForAvailableNutrientCode',
				isArray:false
			},
			// Updates a nutrient Rounding Rules.
			'updateRoundingRule': {
				method: 'PUT',
				url: urlBase + '/pm/nutrients/updateRoundingRules',
				isArray:false
			},
			// Retrieves all ingredient categories by category code.
			'queryByIngredientCategoryCode':{
				method: 'GET',
				url: urlBase + '/pm/ingredientCategory/ingredientCode',
				isArray:false
			},
			// Retrieves all ingredient categories by description.
			'queryByIngredientCategoryDescription':{
				method: 'GET',
				url: urlBase + '/pm/ingredientCategory/ingredientDescription',
				isArray:false
			},
			// Retrieves all ingredient categories.
			'getAllCategories':{
				method: 'GET',
				url: urlBase + '/pm/ingredientCategory/allCategories',
				isArray:false
			},
			// Retrieve a list of found and not found action codes in the scale upc table.
			'queryForMissingIngredientCategoryCodes': {
				method: 'GET',
				url: urlBase + '/pm/ingredientCategory/hits/ingredientCode',
				isArray:false
			},
			// Updates an an ingredient category with a new description.
			'updateIngredientCategory': {
				method: 'PUT',
				url: urlBase + '/pm/ingredientCategory/updateIngredientCategory',
				isArray:false
			},
			// Creates a new ingredient category..
			'addIngredientCategory': {
				method: 'POST',
				url: urlBase + '/pm/ingredientCategory/addIngredientCategory',
				isArray:false
			},
			// Deletes an ingredient category.
			'deleteIngredientCategory': {
				method: 'DELETE',
				url: urlBase + '/pm/ingredientCategory/deleteIngredientCategory',
				isArray:false
			},
			// Creates a new ingredient.
			'addIngredient': {
				method: 'POST',
				url: urlBase + '/pm/ingredient/addIngredient',
				isArray:false
			},
			// Creates a new ingredient.
			'updateIngredient': {
				method: 'POST',
				url: urlBase + '/pm/ingredient/updateIngredient',
				isArray:false
			},
			'getIngredientsByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/ingredient/ingredientRegex',
				isArray: false
			},
			//Query for all Categories information
			'getAllCategoriesList':{
				method: 'GET',
				url: urlBase + '/pm/ingredientCategory/allCategoriesList',
				isArray:true
			},
			// Deletes an ingredient.
			'deleteIngredient': {
				method: 'POST',
				url: urlBase + '/pm/ingredient/deleteIngredient'
			},
			// Gets all ingredients.
			'queryAllIngredients': {
				method: 'GET',
				url: urlBase + '/pm/ingredient/allIngredients'
			},
			// Retrieves all ingredient statements.
			'queryAllIngredientStatements': {
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/allIngredientStatement',
				isArray:false
			},
			// Retrieves ingredient statements by ingredient statements.
			'queryIngredientStatementByStatementCode': {
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/ingredientStatement',
				isArray: false
			},
			// Retrieves ingredient statements by ingredient code.
			'queryIngredientStatementByIngredientCode': {
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/ingredientCode',
				isArray: false
			},
			// Retrieves ingredient statements by ingredient code.
			'queryIngredientStatementByIngredientCodeOrdered': {
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/ingredientCodeOrdered',
				isArray: false
			},
			// Retrieves ingredient statements with ingredients contain the provided description.
			'queryIngredientStatementByDescription' : {
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/description',
				isArray: false
			},
			// Deletes an ingredient category.
			'deleteIngredientStatement': {
				method: 'DELETE',
				url: urlBase + '/pm/ingredientStatement/deleteIngredientStatement',
				isArray:false
			},
			// Creates a new ingredient statement.
			'addIngredientStatement': {
				method: 'POST',
				url: urlBase + '/pm/ingredientStatement/addIngredientStatement',
				isArray:false
			},
			// Creates a new ingredient statement based on a set of ingrdeients.
			'newIngredientStatement': {
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/newIngredientStatement',
				isArray:false
			},
			// Creates a new ingredient.
			'updateIngredientStatement': {
				method: 'POST',
				url: urlBase + '/pm/ingredientStatement/updateIngredientStatement',
				isArray:false
			},
			// Retrieve a list of found and not found PLUs in the scale upc table.
			'queryIngredientStatementsForMissingIngredientStatements': {
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/hits/ingredientStatements',
				isArray:false
			},
			// Finds the next statement number equal or less than the statement number provided.
			'queryForNextIngredientStatementNumber':{
				method: 'GET',
				url: urlBase + '/pm/ingredientStatement/nextIngredientStatement',
				isArray: true
			},
			'getNextIngredientCode':{
				method: 'GET',
				url: urlBase + '/pm/ingredient/nextIngredientCode',
				isArray: true
			},
			'getNutrientsByRegularExpression':{
				method:'GET',
				url: urlBase + '/pm/nutrients/getNutrientsByRegularExpression',
				isArray: false
			},
			'queryByGraphicsCodes': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/graphicsCode/graphicsCodes',
				isArray:false
			},
			'queryByGraphicsCodeDescription': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/graphicsCode/graphicsCodeDescription',
				isArray:false
			},
			'queryScaleUpcByScaleCode': {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/graphicsCode/scaleUpc',
				isArray:false
			},
			'findHitsByGraphicsCodes' : {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/graphicsCode/hits',
				isArray:false
			},
			'update' : {
				method: 'PUT',
				url: urlBase + '/pm/scaleManagement/graphicsCode',
				isArray:false
			},
			'deleteGraphicsCode' : {
				method: 'DELETE',
				url: urlBase + '/pm/scaleManagement/graphicsCode',
				isArray:false
			},
			'addGraphicsCode' : {
				method: 'POST',
				url: urlBase + '/pm/scaleManagement/graphicsCode/addGraphicsCode',
				isArray:false
			},
			'findAllGraphicsCodes' : {
				method: 'GET',
				url: urlBase + '/pm/scaleManagement/graphicsCode/findAllGraphicsCodes',
				isArray:false
			},
			'applyRoundingRuleToNutrient' : {
				method: 'PUT',
				url: urlBase + '/pm/nutrientsStatement/applyRoundingRuleToNutrient',
				isArray:false
			},
			'getNutrientStatementPanelHeaderBySourceSystemReferenceId': {
				method: 'GET',
                url: urlBase + '/pm/NLEA16NutrientStatement/:sourceSystemReferenceId',
                params: {sourceSystemReferenceId:'@sourceSystemReferenceId'},
                isArray: false
			},
			'findAllNutrientStatementPanels': {
				method: 'GET',
				url: urlBase + '/pm/NLEA16NutrientStatement/all',
				isArray:false
			},
			'findAllNutrientStatementPanelsByIds': {
				method: 'GET',
				url: urlBase + '/pm/NLEA16NutrientStatement',
				isArray:false
			},
			'updateNLEA16Statement':{
				method: 'POST',
				url: urlBase + '/pm/NLEA16NutrientStatement/update',
				isArray:false
			},
			'sendScaleMaintenance': {
				method: 'PUT',
				url: urlBase + '/pm/scaleManagement/sendMaintenance',
				isArray:false
			},
			'isNLEA16NutrientStatementExists': {
				method: 'GET',
				url: urlBase + '/pm/nutrientsStatement/isNLEA16NutrientStatementExists',
				isArray:false
			},
			'findAllUnitOfMeasures': {
				method: 'GET',
				url: urlBase + '/pm/NLEA16NutrientStatement/unitOfMeasures',
				isArray:true
			},
			'getMandatedNutrients':{
				method: 'GET',
				url: urlBase + '/pm/NLEA16NutrientStatement/mandatedNutrients',
				isArray:true
			},
			'addNLEA16Statement':{
				method: 'POST',
				url: urlBase + '/pm/NLEA16NutrientStatement/add',
				isArray:false
			},
			'isNutrientStatementExists':{
				method: 'GET',
				url: urlBase + '/pm/NLEA16NutrientStatement/isNutrientStatementExists',
				isArray:false
			},
            // Removes a nutrient statement.
            'deleteNLEA16NutrientStatement':{
                method: 'DELETE',
                url: urlBase + '/pm/NLEA16NutrientStatement/deleteNLEA16NutrientStatement',
                isArray:false
            }
		});
	}
})();
