/*
 *   productNotesComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';
/**
 * Used to display and create notes (candidate comments) for any particular product (candidate or a work request).
 *
 * @author vn40486
 * @since 2.15.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('productNotes', {
		// isolated scope binding
		bindings: {
			workRequest: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/task/productNotes.html',
		// The controller that handles our component logic
		controller: productNotesController
	});
	productNotesController.$inject = ['$scope','EcommerceTaskApi', 'ngTableParams'];
	function productNotesController($scope, ecommerceTaskApi, ngTableParams) {
		var self = this;
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
        self.comments = null;

		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;

        /**
         * The message is to check if there is no change in the row
         */
        const MESSAGE_NO_DATA_CHANGE = 'There are no changes on this page to be saved. Please make any changes to update.';

        /**
         * The message is showed if the product notes is updated Successfully
         */
        const SUCCESSFULLY_UPDATED = 'Note updated Successfully!!';

        /**
         * The message is showed if the product notes is updated Successfully
         */
        const SUCCESSFULLY_DELETED = 'Note deleted Successfully!!';

		/**
		 * Whether or not a user is adding a new note.
		 * @type {boolean}
		 */
		self.isAdding = false;
		
		/**
		 * Constructs the ng-table based data table.
		 */
		self.buildTable = function (workRequest) {
			return new ngTableParams(
				{},
				{
					counts: [],
					getData: function ($defer, params) {
						self.isWaiting = true;
						self.isNoRecordsFound = false;
						self.defer = $defer;
						self.dataResolvingParams = params;
						if(self.workRequest.workRequestId == workRequest.workRequestId) {
                            self.fetchData(workRequest);
                        }
					}
				}
			)
		};

        /**
         * Select record to edit and enable it.
         * @param comment
         */
        self.enableRow = function(comment){
            self.displayMessage(null , false);
            comment.isEditing = true;
            self.hasOtherRowEditing = true;
            self.selectedRow = angular.copy(comment);
            self.selectedRowIndex = self.comments.indexOf(comment);
        };

        /**
         * Check if has changed data
         * @param changedComment
         * @returns {boolean}
         */
        self.hasDataChanged = function(changedComment){
            if (changedComment !== null && changedComment !== undefined) {
                return !(JSON.stringify( angular.copy(changedComment)) === JSON.stringify(angular.copy(self.selectedRow)))
            }
            return false;
        };

        /**
         * reset data of record
         */
        self.resetCurrentRow = function(){
        	self.displayMessage(null , false);
            self.comments[self.selectedRowIndex] = angular.copy(self.selectedRow);
            self.comments[self.selectedRowIndex].isEditing = false;
            self.selectedRow = null;
        };

        /**
         * the funtion is to check if has any row Editing
         * @param comment
         * @returns {boolean}
         */
        self.checkIfHasAnyRowEditing = function(comment){
            if ((self.selectedRow !== null && self.selectedRow != undefined) || self.isAdding ){
                self.hasOtherRowEditing = true;
                return true;
            }else{
                self.hasOtherRowEditing = false;
            }
            return false;
        };

        /**
         * Request delete the product notes.
         * @param comment
         */
        self.deleteProductNotes = function (comment) {
            ecommerceTaskApi.deleteProductNotes(//  delete the product notes
                {key : comment.key},
                function (result) {
                    self.isAdding = false;
                    self.displayMessage(SUCCESSFULLY_DELETED, false);
                    self.reloadTable(false);
                },
                function (error) {
                    self.handleError(error);
                }
            );
        };

        /**
		 * Fetches all the noted specific to a product(candidate) from database.
		 */
		self.fetchData = function(workRequest) {
			ecommerceTaskApi.getProductNotes({workRequestId : workRequest.workRequestId},self.loadData,self.handleError);
		}

		/**
		 * Callback for a successful call to get data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
            self.comments = results;
			self.defer.resolve(results);
			if (results && results.length === 0) {
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
		 * Used to reload table data.
		 */
		self.reloadTable = function (clearMsg) {
			if(clearMsg) {self.clearMessage();}
			if(self.tableParams!=null) {
				self.tableParams.reload();
			}
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
		 * Used to handle Add note button click event. Creates a new Row in the data table for user to enter the new
		 * description.
		 */
		self.addNote = function() {
            self.hasOtherRowEditing = true;
            self.displayMessage(null , false);
			if(!self.isAdding){
				self.isAdding = true;
				var newResource  = generateNewRow();
                self.comments.unshift(newResource);
			}
		};

		/**
		 * Generates new angular resource for the new row.
		 * @returns {*}
		 */
		function generateNewRow() {
			var newRow = {comments:'',time:null,userId:null,isEditing : true};
			return angular.extend(newRow, ecommerceTaskApi.prototype);
		}

		/**
		 * Handles when editing is being cancelled.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.cancel = function(){
			if(self.isAdding) {
                self.comments.shift();
			}
            self.displayMessage(null , false);
			self.isAdding = false;
            self.hasOtherRowEditing = false;
		};

		/**
		 * Handles when reset click event. Reloads data table with the data fetched afresh from db.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.reset = function(){
            self.selectedRow = null;
			self.isAdding = false;
            self.hasOtherRowEditing = false;
			self.reloadTable(true);
		};

		/**
		 * Handles save of newly created notes/comment.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.save = function(changedComment){
            if(self.isAdding) {
                var comment = self.comments[0];
                if (self.isAdding && self.validateToAdd(comment)) {
                    ecommerceTaskApi.addProductNotes(  // add a new Product notes
                        {
                            key: {workRequestId: self.workRequest.workRequestId, sequenceNumber: -1},
                            comments: comment.comments
                        },
                        function (result) {
                            self.isAdding = false;
                            self.displayMessage('Note added Successfully!!', false);
                            self.reloadTable(false);
                        },
                        function (error) {
                            self.handleError(error);
                        }
                    );
                } else {
                    self.displayMessage('Please enter a comment to save.', true);
                }
            }else{
                if (self.hasDataChanged(changedComment) && self.validateToAdd(changedComment)){
                    ecommerceTaskApi.updateProductNotes(//  update the product notes
                        {key : changedComment.key, comments : changedComment.comments},
                        function (result) {
                            self.selectedRow = null;
                            self.displayMessage(SUCCESSFULLY_UPDATED, false);
                            self.reloadTable(false);
                        },
                        function (error) {
                            self.selectedRow = null;
                            self.handleError(error);
                        }
                    );
                }else if(!self.validateToAdd(changedComment)){
                    self.displayMessage('Please enter a comment to save.', true);
                }else {
                    self.displayMessage(MESSAGE_NO_DATA_CHANGE, true);
                }
			}
		};

		/**
		 * Validates whether user entered a comment. Returns FALSE if no comment is entered.
		 * @param comment
		 * @returns {boolean}
		 */
		self.validateToAdd = function(comment) {
			return (comment.comments && comment.comments.length > 0);
		}

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
		/**
		 * Listen the reset product note event.
		 */
		$scope.$on('openProductNotes', function() {
            self.reset();
			self.clearMessage();
			self.isAdding = false;
            self.comments = [];
			if(self.workRequest) {
				self.tableParams = self.buildTable(self.workRequest);
			}
			$('#productNotesModal').modal('show');
		});
	}
})();
