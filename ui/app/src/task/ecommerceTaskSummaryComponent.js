/*
 *   ecommerceTaskSummaryComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';
/**
 * Component of eCommerce task summary. Used to fetch and display list of eCommerce task only. This component does not
 * show details of any particular task.
 *
 * @author vn40486
 * @since 2.14.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('ecommerceTaskSummary', {
		// isolated scope binding
		bindings: {},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/task/ecommerceTaskSummary.html',
		// The controller that handles our component logic
		controller: ecommerceTaskSummaryController
	});
	ecommerceTaskSummaryController.$inject = ['$scope','EcommerceTaskApi', 'ngTableParams', 'HomeSharedService', '$location', 'taskService', 'ProductSearchService'];
	function ecommerceTaskSummaryController($scope, ecommerceTaskApi, ngTableParams, homeSharedService, $location, taskService, productSearchService) {
		var self = this;

        /**
         * initialize the default value for page.
         */
        const DEFAULT_PAGE = 1;

        /**
         * initialize the default value for taskStatus.
         */
        self.taskStatus = "Active";

		self.showEcommerceTaskSummary = false;
		/**
		 * Denotes whether back end should fetch total counts or not. Accordingly server side will decide on executing
		 * additional logic to fetch count information.
		 *
		 * @type {boolean}
		 */
		self.includeCounts = false;

		/* number of rows per page constant. */
		self.PAGE_SIZE = 15;

		/**
		 * The total number of records in the report.
		 * @type {int}
		 */
		self.totalRecordCount = null;

		/**
		 * The parameters passed to the application from ng-tables getData method. These need to be stored
		 * until all the data are fetched from the backend.
		 *
		 * @type {null}
		 */

		self.dataResolvingParams = null;
		/**
		 * The promise given to the ng-tables getData method. It should be called after data is fetched.
		 *
		 * @type {null}
		 */
		self.defer = null;

		/**
		 * The data being shown in the report.
		 * @type {Array}
		 */
		$scope.data = null;

		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;

		/**
		 * maintains state of data call.
		 * @type {boolean}
		 */
		self.firstFetch = true;

		/**
		 * Holds boolean state to fetch all task or only Active tasks. True = All task ; False = Active Tasks only.
		 * @type {boolean}
		 */
		self.fetchAll = false;

		/**
		 * Map of task type and it's description.
		 * @type {{MYTSK: string}}
		 */
		self.taskTypeMap = {'MYTSK':'Ecommerce View'};
		/**
		 * Map of task status code and it's description.
		 * @type {{ACTIV: string, CLOSD: string}}
		 */
		self.taskStatusMap = {'ACTIV':'Active', 'CLOSD':'Closed','ACTIVEANDCLOSED':'ActiveAndClosed'};

		/**
		 * Task Detail base url.
		 * @type {string}
		 */
		self.taskDetailURL = "/task/ecommerceTask/taskInfo/";

		/**
		 * on change event listener that builds/rebuilds the data table.
		 */
		this.$onChanges = function () {
            if(taskService.getReturnToEcommerceTaskList()){
                self.showEcommerceTaskSummary = true;
                if(taskService.getAlertDataTxt() === null
					|| taskService.getAlertDataTxt() === ""
					|| taskService.getAlertDataTxt() === undefined){
                    self.message = null;
                }
                else {
                    self.message = "The task " + '"' +taskService.getAlertDataTxt() + '"' + " has been deleted";
                }
            }
			self.tableParams = self.buildTable();
		};

		/**
		 * Constructs the ng-table based data table.
		 */
		self.buildTable = function () {
			return new ngTableParams(
				{
					page: 1,
					count: self.PAGE_SIZE
				}, {
					counts: [],
					getData: function ($defer, params) {
						self.isWaiting = true;
						self.isNoRecordsFound = false;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.includeCounts = false;
						if (self.firstFetch) {
							self.includeCounts = true;
						}
                        if(taskService.getReturnToEcommerceTaskList()){
                            taskService.setReturnToEcommerceTaskList(false);
                            taskService.setAlertDataTxt(null);
                            productSearchService.setSearchPanelVisibility();
                        }
						self.getAllTasks(params.page() - 1);
					}
				}
			)
		};

		/**
		 * Fetches all the active and closed tasks from database with pagination.
		 * @param page  selected page number.
		 */
		self.getAllTasks = function(page) {
			ecommerceTaskApi.getAllTasks(
				{
					includeCounts: self.includeCounts,
					page : page,
					pageSize : self.PAGE_SIZE,
                    taskStatus : self.taskStatus
				},
				self.loadData,self.handleError);
		}

		/**
		 * Callback for a successful call to get data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
				self.firstFetch = false;
			}
			self.resultMessage = self.getResultMessage(results.data.length, self.tableParams.page() -1);
			$scope.data = results.data;
			self.defer.resolve(results.data);
			if (results.data && results.data.length === 0) {
				self.isNoRecordsFound = true;
			}
			self.isWaiting = false;
		};

		/**
		 * Callback that will respond to errors sent from the backend.
		 *
		 * @param error The object with error information.
		 */
		self.handleError = function(error){
			if (error && error.data) {
				self.displayMessage(error.data.error, true);
			} else {
				self.displayMessage("An unknown error occurred.", true);
			}
			self.isWaiting = false;
		};

		/**
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		self.getResultMessage = function(dataLength, currentPage){
			return "" + (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + "  of  " +
				self.totalRecordCount.toLocaleString();
		};

		/**
		 * Used to reload table data.
		 */
		self.reloadTable = function (page, clearMsg) {
			if(clearMsg) {self.clearMessage();}
			self.firstFetch = true;
			self.tableParams.page(page);
			self.tableParams.reload();
		}

		/**
		 * Fetches tasks data based on user selection. Fetch only Active or All.
		 * @param isFetchAllSelected
		 */
		self.fetchTasksByStatus = function(isFetchAllSelected , taskStatus) {
			self.taskStatus = taskStatus;
			self.fetchAll = isFetchAllSelected;
            self.reloadTable(DEFAULT_PAGE, true);
		}

		/**
		 * Used to display any success of failure messages above the data table.
		 * @param message message to be displayed.
		 * @param isError is error or not; True - message displayed in red. False- message get displayed in blue.
		 */
		self.displayMessage = function(message, isError) {
			self.isError = isError;
			self.message = message;
		}

		/**
		 * Used to clear message displayed out of last action by user.
		 */
		self.clearMessage = function() {
			self.isError = false;
			self.message = '';
		}

		/**
		 * Open poup create new task when click Create New button
		 */
		self.openPopupCreateNewTask = function(){
			self.errorMessageCreateTask = null;		
			self.newTaskDescription = null;	
			self.createTaskForm.$setPristine();
			self.createTaskForm.$setUntouched();
			$('#createNewTaskModal').modal({ backdrop: 'static', keyboard: true });
			$('input[name="createNewTask"]').focus();
		}

		/**
		 * Handles creating new task based on the task name keyed in by the user. On success of task creation, it also
		 * redirects the user to task detail page of the created task.
		 * @param taskName
		 * @param $event
		 */
		self.createTask = function($event) {
			self.errorMessageCreateTask = null;
			if(self.newTaskDescription == undefined || self.newTaskDescription == null || self.newTaskDescription.trim() == ''){
				angular.forEach(self.createTaskForm.$error, function (field) {
					angular.forEach(field, function(errorField){
						errorField.$setTouched();
					})
				});
				self.errorMessageCreateTask = "Please enter the Task Description.";
			}else{
			var $btn = self.displayLoadingText(event);
			ecommerceTaskApi.createTask(
				self.newTaskDescription,
				function(result) {
					self.hideLoadingText($btn);
					if (result.data) {
						$('#createNewTaskModal').modal('hide');
						$scope.go(result.data);
					} else{
						self.displayMessage('Unable to create Task. Please try again later.', true);
					}
				},
				function(error) {
					self.hideLoadingText($btn);
					self.handleError(error);
				});
		}
			
		}

		/**
		 * Receives new search event broadcast from search controller.
		 */
		$scope.$on('loadEcommerceTaskSummary', function() {
			self.showEcommerceTaskSummary = true;
		});

		/**
		 * Receives new search event broadcast from search controller.
		 */
		$scope.$on('handleNewSearch', function() {
			self.showEcommerceTaskSummary = false;
		});

		/**
		 * Handles navigation to a specified path. Much like an href.
		 * @param path
		 */
		$scope.go = function ( taskId ) {
			taskService.setTaskId(taskId);
			$location.path( self.taskDetailURL+taskId);
		};

		/**
		 * Used to display the loading text based on user's action over the given save/submit buttons.
		 * @param event
		 * @returns {*|jQuery}
		 */
		self.displayLoadingText = function(event) {
			return $(event.target).button('loading');
		}

		/**
		 * Hides and reverts the button state back to the normal, display the original text like save/submit.
		 * @param $btn
		 */
		self.hideLoadingText = function($btn) {
			$btn.button('reset');
		}
	}
})();
