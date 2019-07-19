/*
 * reportsController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the ingredients report.
 *
 * @author d116773
 * @since 2.0.7
 */
(function() {
		angular.module('productMaintenanceUiApp').controller('ScaleReportController', scaleReportController);

		scaleReportController.$inject = ['urlBase', 'DownloadService'];

		/**
	 	* Creates the controller for the ingredients report.
	 	*/
		function scaleReportController(urlBase, downloadService) {

			var self = this;

			self.success = null;
			self.error = null;

			self.ingredients = null;
			self.isWaiting = false;

			self.init = function() {};

			/**
			 * Initiates an export.
			 */
			self.doExportIngredientReport = function() {
				self.isWaiting = true;
				var exportUrl = urlBase + "/pm/scaleReports/ingredient?ingredient=" + self.ingredients;
				var fileName = "IngredientExport.csv";
				downloadService.export(exportUrl, fileName, 1200, function() {self.isWaiting = false;});
			}

			/**
			 * Initiates an export.
			 */
			self.doExport2016ComplianceReport = function() {
				self.isWaiting = true;
				var exportUrl = urlBase + "/pm/scaleReports/NLEA2016";
				var fileName = "NLEA2016Export.csv";
				downloadService.export(exportUrl, fileName, 1200, function() {self.isWaiting = false;});
			}
			//Start - Changes are done for Scale Load by Department by Paranthaman,Dhayanithi
			/**
			 * Initiates an doExportIngredientsNotInStatementsReport export.
			 */
			self.doExportIngredientsNotInStatementsReport = function() {
				self.isWaiting = true;
				var exportUrl = urlBase + "/pm/scaleReports/ingredientsNotInStatements";
				var fileName = "IngredientsNotInStatementsExport.csv";
				downloadService.export(exportUrl, fileName, 1200, function() {self.isWaiting = false;});
			}
			//End - Changes are done for Scale Load by Department by Paranthaman,Dhayanithi

		}
	}
)();
