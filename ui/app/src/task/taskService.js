/*
 * productGroupService.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Creates the service used to interact with the product group detail.
 */
(function() {
	angular.module('productMaintenanceUiApp').service('taskService', taskService);

	function taskService() {

		var self = this;

		/**
		 * Selected product Id
		 * @type {Long}
		 */
		self.taskId = null;

		/**
		 * Navigate from ecommerce view page to another page or not
		 * @type {Boolean}
		 */
		self.returnToListFlag = false;

        /**
         * Navigate from ecommerce task page to home page.
         * @type {Boolean}
         */
        self.returnToEcommerceTaskList = false;

		/**
		 * Selected alertId
		 * @type {Long}
		 */
		self.alertId = null;

        /**
		 * Selected name of task.
         * @type {String}
         */
		self.alertDataTxt = null;

		return {
			/**
			 * Initializes the controller.
			 *
			 * @param showFullPanel Whether or not to show the full panel (excludes item status filter). This will add
			 * things like the MRT tab, the add button for complex search, product description search, etc.
			 * @param showStatusFilter Whether or not to show the item status filter options.
			 * @param basicSearchCallback The callback for when the user clicks on a basic search button. Basic
			 * searches only have one set of criteria.
			 * @param complexSearchCallback The callback for when the user clicks on the complex search button. Complex
			 * searches can contain multiple sets of criteria.
			 */
			init:function() {

			},
			/**
			 * Returns ProductGroupTypeCode
			 *
			 * @returns {null|*}
			 */
			getTaskId:function() {
				return self.taskId;
			},

			/**
			 * Sets the values the product group type code.
			 *
			 * @param selectedProductId
			 */
			setTaskId:function(taskId) {
				self.taskId = taskId;
			},

			/**
			 * Returns the values the user has chosen
			 * object representing a BDM or a level of  a hierarchy.
			 *
			 * @returns {null|*}
			 */
			getAlertId:function() {
				return self.alertId;
			},

			/**
			 * Sets the values the user has chosen
			 *
			 * @param alertId
			 */
			setAlertId:function(alertId) {
				self.alertId = alertId;
			},

			/**
			 * Returns return to list flag
			 *
			 * @returns {true|false}
			 */
			getReturnToListFlag:function() {
				return self.returnToListFlag;
			},

            /**
			 * Sets return to list ecommerce task in home page.
			 *
             * @param returnToEcommerceTaskList
             */
			setReturnToEcommerceTaskList:function(returnToEcommerceTaskList) {
				self.returnToEcommerceTaskList = returnToEcommerceTaskList;
			},

            /**
             * Returns return to list ecommerce task in home page.
             *
             * @returns {true|false}
             */
            getReturnToEcommerceTaskList:function() {
                return self.returnToEcommerceTaskList;
            },

            /**
             * Sets return to list flag.
             *
             * @param returnToListFlag
             */
            setReturnToListFlag:function(returnToListFlag) {
                self.returnToListFlag = returnToListFlag;
            },

            /**
             * Returns alert Data txt
             *
             * @returns {true|false}
             */
            getAlertDataTxt:function() {
                return self.alertDataTxt;
            },

            /**
             * Sets return alert data txt.
             *
             * @param alertDataTxt
             */
            setAlertDataTxt:function(alertDataTxt) {
                self.alertDataTxt = alertDataTxt;
            },
		}
	}
})();
