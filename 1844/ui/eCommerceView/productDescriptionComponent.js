/*
 *   productDescriptionComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Component to support the page that allows users to show product description.
 *
 * @author vn70529
 * @since 2.12.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('productDescriptionComponent', {
		bindings: {
			currentTab: '<',
			productMaster: '<',
			eCommerceViewDetails: '=',
			changeSpellCheckStatus: '&',
			isEditText: '=',
			callbackfunctionAfterSpellCheck:'='
		},
		require:{
			parent:'^^eCommerceView'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/eCommerceView/productDescription.html',
		// The controller that handles our component logic
		controller: productDescriptionController
	});
	productDescriptionController.$inject = ['$timeout','ECommerceViewApi', '$scope', '$sce', 'VocabularyService', 'VocabularyApi', 'ngTableParams'];
	function productDescriptionController($timeout, eCommerceViewApi, $scope, $sce, vocabularyService, vocabularyApi, ngTableParams) {
		var self = this;
		/**
		 * The default error message.
		 *
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";
        /**
         * Romance copy attribute id is mandatory error message.
         *
         * @type {String}
         */
		self.ROMMANCE_COPY_IS_MANDATORY_ERROR = "Romance Copy is mandatory.";
		/**
		 * Romance copy attribute id.
		 *
		 * @type {number}
		 */
		self.ROMANCE_COPY_LOG_ATTR_ID = 1666;
		/**
		 * Edit romance physical attribute id.
		 *
		 * @type {number}
		 */
		self.EDIT_ROMANCE_PHY_ATTR_ID = 1600;
		/**
		 * System source id for edit mode.
		 *
		 * @type {number}
		 */
		self.SRC_SYS_ID_EDIT = 4;
		/**
		 * The number of requests to the server.
		 *
		 * @type {number}
		 */
		self.NUMBER_OF_REQUESTS = 2;
		/**
		 * Max length of romance copy description.
		 * @type {number}
		 */
		self.MAX_LENGTH_ROMANCE_COPY_DESCRIPTION = 3500;
		/**
		 * Reload eCommerce view key.
		 * @type {string}
		 */
		self.RELOAD_ECOMMERCE_VIEW = 'reloadECommerceView';
		/**
		 * Reset eCommerce view.
		 * @type {string}
		 */
		self.RESET_ECOMMERCE_VIEW = 'resetECommerceView';
		/**
		 * Reload after save popup.
		 * @type {string}
		 */
		self.RELOAD_AFTER_SAVE_POPUP = 'reloadAfterSavePopup';
		/**
		 * Validate warning eCommerce view key.
		 * @type {string}
		 */
		self.VALIDATE_WARNING = 'validateWarning';
		/**
		 * Current number of responses from server.
		 *
		 * @type {number}
		 */
		self.currentNumberOfResponses = 0;
		/**
		 * Holds the Editable status for romance copy.
		 *
		 * @type {boolean}
		 */
		self.editableRomanceCopy = false;
		/**
		 * Current color for romance copy.
		 *
		 * @type {string}
		 */
		self.editSourceTextColorForRomanceCopy ='black';
		/**
		 * Holds the selected index on table.
		 * @type {number}
		 */
		self.selectedRowIndexOnDescriptionSourceTable = -1;

		/**
		 * Define trust as HTML copy from $sce to use in ui.
		 */
		self.trustAsHtml = $sce.trustAsHtml;
		/**
		 * Flag to show type of format romance copy field.
		 * @type {boolean}
		 */
		self.htmlShow = false;
		/**
		 * HTML Tab Pressing flag.
		 * @type {boolean}
		 */
		self.htmlTabPressing = false;
		/**
		 * Constant order by asc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_ASC = "asc";
		/**
		 * Constant order by desc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_DESC = "desc";
        /**
         * The id of favor tab
         * @type {string}
         */
        const FAVOR_TAB_ID = 'Favor';
		/**
		 * Initialize the controller.
		 */
		self.init = function () {
			self.currentNumberOfResponses = 0;
			self.isWaitingForResponse = true;
			self.loadProductDescription();
			self.loadTags();
		};
		/**
		 * Component will reload the data whenever the item is changed in.
		 */
		this.$onChanges = function () {
			self.currentNumberOfResponses = 0;
			self.isWaitingForResponse = true;
			self.validateChangedPublishedSource();
            self.loadProductDescription();
            self.loadTags();
		}
		/**
		 * Destroy component.
		 */
		this.$onDestroy = function () {
			self.parent = null;
		};
		/**
		 * Check different between current value with default value.
		 */
		self.validateChangedPublishedSource = function () {
			self.differentWithDefaultValueByTag = false;
			eCommerceViewApi.validateChangedPublishedSource(
				{
					productId: self.productMaster.prodId,
					logicAttributeCode: 1729
				},
				//success case
				function (result) {
					if(result != null){
						self.differentWithDefaultValueByTag = result.data;
					}
				},self.fetchError);
		};
		/**
		 * Get product description.
		 */
		self.loadProductDescription = function () {
			self.editableRomanceCopy = false;
			self.editableRomanceCopyOrg = false;
			eCommerceViewApi.findProductDescription(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId,
				},
				//success case
				function (results) {
					self.currentNumberOfResponses++;
					self.eCommerceViewDetails.romanceCopy = self.convertNullValueToEmptyValue(results);
					self.eCommerceViewDetails.romanceCopyOrg = angular.copy(self.eCommerceViewDetails.romanceCopy);
					if(self.eCommerceViewDetails.romanceCopy != null) {
						self.editableRomanceCopy = self.isEditMode();
						self.editableRomanceCopyOrg = angular.copy(self.editableRomanceCopy);
						self.editSourceTextColorForRomanceCopys = self.eCommerceViewDetails.romanceCopy.differentWithDefaultValue ? '#0000FF' : 'black';
						//validate spell check of romance copy.
						self.validateRomancyTextForView();
					}
					self.validateProductDescription();
					self.hideLoading();
				}, self.fetchError);
		};
        /**
		 * Convert null value of property to empty value.
         * @param data the object holds the property to convert.
         * @returns {*}
         */
		self.convertNullValueToEmptyValue = function(data) {
			var varData = angular.copy(data);
			if(varData){
				if(varData.content === null){
					varData.content = "";
				}
			}
			return varData;
		};
		/**
		 * Get a list of tags.
		 */
		self.loadTags = function () {
			eCommerceViewApi.findTags(
				{
					productId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId,
				},
				//success case
				function (results) {
					self.currentNumberOfResponses++;
					self.tags = results;
					self.tagsOrig = angular.copy(results);
					self.hideLoading();
				}, self.fetchError);
		};
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.currentNumberOfResponses++;
			self.hideLoading();
			self.error = self.getErrorMessage(error);
		};
		self.hideLoading = function(){
			if(self.currentNumberOfResponses>= self.NUMBER_OF_REQUESTS){
				self.isWaitingForResponse = false;
			}
		};
		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				}
				return error.data.error;
			}
			return self.UNKNOWN_ERROR;
		};
		/**
		 * Check edit status or not.
		 *
		 * @returns {boolean}
		 */
		self.isEditMode = function(){
			return (self.eCommerceViewDetails.romanceCopy.sourceSystemId === self.SRC_SYS_ID_EDIT &&
				self.eCommerceViewDetails.romanceCopy.physicalAttributeId === self.EDIT_ROMANCE_PHY_ATTR_ID);

		};
		/**
		 * Show edit mode of romance copy.
		 */
		self.showEditText = function(){
			if(!self.editableRomanceCopy){
				self.editableRomanceCopy=true;
				vocabularyService.createSuggestionMenuContext($scope, $scope.suggestionsOrg);
			}
		};
		/**
		 * Show popup to edit description source.
		 */
		self.showDescriptionEditSource = function () {
			self.isEditText = self.editableRomanceCopy;
			self.parent.getECommerceViewDataSource(self.productMaster.prodId, self.productMaster.productPrimaryScanCodeId, self.ROMANCE_COPY_LOG_ATTR_ID, 0,'Romance Copy', null);
		};
		/**
		 * Show popup to edit tags source.
		 */
		self.showTagsEditSource = function () {
			self.parent.isWaitingForResponsePopup = true;
			self.parent.dataSourceTitle = 'Tags';
			self.parent.currentAttributeId = 1729;
			$('#dataSourceModalType1').modal({backdrop: 'static', keyboard: true});
			eCommerceViewApi.findAllTagAttributePriorities({
					productId: self.productMaster.prodId,
					scanCodeId: self.productMaster.productPrimaryScanCodeId,
				},self.dataSourceResponseSuccess, self.fetchErrorPopup
			);
		};
		/**
		 * Response success. Store and handle show data source.
		 * @param result
		 */
		self.dataSourceResponseSuccess = function (result) {
			result.productId = self.productMaster.prodId;
			self.parent.dataSourceResponseSuccess(result);
		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchErrorPopup= function (error) {
			self.parent.fetchErrorPopup(error);
		}
		/**
		 * Reset to origin data.
		 */
		self.reset = function(){
			self.tags = angular.copy(self.tagsOrig);
			self.eCommerceViewDetails.romanceCopy = angular.copy(self.eCommerceViewDetails.romanceCopyOrg);
		};
		/**
		 * Reset edit description source.
		 */
		self.resetEditDescriptionSource = function(){
			self.descriptionSources = angular.copy(self.descriptionSourcesOrig);
		}
		/**
		 * Hide edit description source.
		 */
		self.cancelEditDescriptionSource = function(){
			$("#editDescriptionSource").modal("hide");
		};
		/**
		 * Set selected row on edit description source table.
		 * @param index
		 */
		self.selectRowOnDescriptionSource = function(index){
			self.selectedRowIndexOnDescriptionSourceTable = index;
		};
		/**
		 * Validate the product description. If it is invalid, then show warning message on the top right panel.
		 */
		self.validateProductDescription = function(){
			self.eCommerceViewDetails.romanceCopyErrorMessages = [];
            if(self.currentTab.id != FAVOR_TAB_ID) {
                if (self.eCommerceViewDetails.romanceCopy === null ||
                    self.eCommerceViewDetails.romanceCopy === undefined) {
                    self.eCommerceViewDetails.romanceCopyErrorMessages.push(self.ROMMANCE_COPY_IS_MANDATORY_ERROR);
                }
                else if (self.eCommerceViewDetails.romanceCopy.mandatory !== null && self.eCommerceViewDetails.romanceCopy.mandatory !== undefined
						&& self.eCommerceViewDetails.romanceCopy.mandatory) {
                    self.eCommerceViewDetails.romanceCopyErrorMessages.push(self.ROMMANCE_COPY_IS_MANDATORY_ERROR);
                }
            }
		}
		/**
		 * Disable validate spell check of Romance Copy when open suggestions popup.
		 */
		$scope.$on('context-menu-opened', function (event, args) {
			self.openSuggestionsPopup = true;
		});
		/**
		 * Enable validate spell check of Romance Copy when close suggestions popup.
		 */
		$scope.$on('context-menu-closed', function (event, args) {
			self.openSuggestionsPopup = false;
		});
		/**
		 * Validate text for view mode. It will call after loaded romance description.
		 */
		self.validateRomancyTextForView = function(){
			if(!self.isEmpty(self.eCommerceViewDetails.romanceCopy.content)){
				self.isWaitingForCheckSpellingResponse = true;
				vocabularyService.validateRomancyTextForView(self.eCommerceViewDetails.romanceCopy.content, function(newText, suggestions){
					if(self.editableRomanceCopy){//load data of edit mode
						// Use $timeout and space char to clear cache on layout.
						self.eCommerceViewDetails.romanceCopy.content = self.eCommerceViewDetails.romanceCopy.content + ' ';
						$timeout(function () {
							self.eCommerceViewDetails.romanceCopy.content = angular.copy(newText);//color text
							self.romanceCopyContent = angular.copy(newText);
							self.romanceCopyContentOrg = angular.copy(newText);
							$scope.suggestionsOrg = angular.copy(suggestions);
							$timeout(function () {
								vocabularyService.createSuggestionMenuContext($scope, suggestions);
								self.isWaitingForCheckSpellingResponse = false;
							}, 1000);
						}, 100);
					}else{//load data of view mode
						self.romanceCopyContent = angular.copy(newText);
						self.romanceCopyContentOrg = angular.copy(newText);
						self.eCommerceViewDetails.romanceCopy.content = angular.copy(newText);//color text
						$scope.suggestionsOrg = angular.copy(suggestions);
						self.isWaitingForCheckSpellingResponse = false;
					}
				});
			}else{
				self.romanceCopyContent = '';
				self.romanceCopyContentOrg = '';
			}
		};
		/**
		 * Validate text for edit mode of romance copy.
		 */
		self.validateRomancyTextForEdit = function(){
			if(self.htmlTabPressing){
				self.htmlTabPressing = false;
				return;
			}
			if(!self.openSuggestionsPopup){//avoid conflict with suggestion popup
				if(!self.isEmpty(self.eCommerceViewDetails.romanceCopy.content)){
					var callbackFunction = self.getAfterSpellCheckCallback();
					self.isWaitingForCheckSpellingResponse = true;
					self.changeSpellCheckStatus({status: true});
					var contentHtml = $('#romanceCopyDiv').html();
					contentHtml = contentHtml.split("<br>").join("<div>");
					contentHtml = contentHtml.split("</p>").join("<div>");
					var contentHtmlArray = contentHtml.split("<div>");
					var contentNoneHtml = '';
					if(contentHtmlArray.length > 1) {
						for (var i = 0; i < contentHtmlArray.length; i++) {
							if(contentHtmlArray[i] != null && contentHtmlArray[i] != ''){
								contentNoneHtml = contentNoneHtml.concat(self.htmlToPlaintext(contentHtmlArray[i]));
								if(i < (contentHtmlArray.length - 1)){
									contentNoneHtml = contentNoneHtml.concat(" <br> ");
								}
							}
						}
						self.eCommerceViewDetails.romanceCopy.content = contentNoneHtml.split("&nbsp;").join(" ");
					}
					var response = vocabularyApi.validateRomanceCopySpellCheck({textToValidate: self.eCommerceViewDetails.romanceCopy.content});
					response.$promise.then(
						function (result) {
							// Use $timeout and space char to clear cache on layout.
							self.eCommerceViewDetails.romanceCopy.content = self.eCommerceViewDetails.romanceCopy.content + ' ';
							$timeout(function () {
								self.eCommerceViewDetails.romanceCopy.content = angular.copy(result.validatedText);//normal text
								vocabularyService.validateRomancyTextForView(self.eCommerceViewDetails.romanceCopy.content, function(newText, suggestions){
									self.romanceCopyContent = angular.copy(newText);
									self.eCommerceViewDetails.romanceCopy.content = angular.copy(newText);//color text
									$scope.suggestionsOrg = angular.copy(suggestions);
									$timeout(function () {
										vocabularyService.createSuggestionMenuContext($scope, suggestions);
									}, 1000);
									self.isWaitingForCheckSpellingResponse = false;
									self.changeSpellCheckStatus({status: false});
									$timeout(function () {
										// Save or publish if event occurs from save or publish button.
										self.processAfterSpellCheck(callbackFunction);
									}, 500);
								});
							}, 100);
						}, function (error) {
						}
					);
				}
			}
		};
		/**
		 * Check empty value.
		 *
		 * @param val
		 * @returns {boolean}
		 */
		self.isEmpty = function(val){
			if (val == null || val == undefined || val.trim().length == 0) {
				return true;
			}
			return false;
		};
		/**
		 * Reload ECommerceView.
		 */
		$scope.$on(self.RELOAD_ECOMMERCE_VIEW, function() {
			self.$onChanges();
		});
		/**
		 * Reset eCommerce view.
		 */
		$scope.$on(self.RESET_ECOMMERCE_VIEW, function() {
			self.htmlShow = false;
			self.editableRomanceCopy = angular.copy(self.editableRomanceCopyOrg);
			self.romanceCopyContent = angular.copy(self.romanceCopyContentOrg);
			self.eCommerceViewDetails.romanceCopy.content = angular.copy(self.romanceCopyContentOrg);
			if(self.editableRomanceCopy){//edit mode
				$timeout(function () {
					vocabularyService.createSuggestionMenuContext($scope, $scope.suggestionsOrg);
				}, 1000);
			}
		});
		/**
		 * Reload after save popup.
		 */
		$scope.$on(self.RELOAD_AFTER_SAVE_POPUP, function(event, attributeId, status) {
			if(attributeId === self.ROMANCE_COPY_LOG_ATTR_ID){
				self.editableRomanceCopy = false;
				if(status === true){
					self.$onChanges();
				}else{
					self.isWaitingForResponse = true;
				}
			}
		});
		/**
		 * Validate warning.
		 */
		$scope.$on(self.VALIDATE_WARNING, function() {
			self.validateProductDescription();
		});
		/**
		 * Show eCommerce View Changed Log.
		 */
		self.showeCommerceViewLog = function(){
			self.isWaitingGetECommerceViewAudit = true;
			eCommerceViewApi.getECommerceViewAudit(
				{
					primaryUPC: self.productMaster.productPrimaryScanCodeId,
				},
				//success case
				function (results) {
					self.eCommerceViewAudits = results;
					self.initECommerceViewAuditTable();
					self.isWaitingGetECommerceViewAudit = false;
				}, self.fetchError);

			$('#ecommerce-view-log-modal').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * Init data eCommerce View Audit.
		 */
		self.initECommerceViewAuditTable = function () {
			$scope.filter = {
				attributeName: '',
			};
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			$scope.eCommerceViewAuditTableParams = new ngTableParams({
				page: 1,
				count: 10,
				filter: $scope.filter,
				sorting: $scope.sorting

			}, {
				counts: [],
				data: self.eCommerceViewAudits
			});
		}
		/**
		 * Change sort.
		 */
		self.changeSort = function (){
			if($scope.sorting.changedOn === ORDER_BY_DESC){
				$scope.sorting.changedOn = ORDER_BY_ASC;
			}else {
				$scope.sorting.changedOn = ORDER_BY_DESC;
			}
		}

		/**
		 * Remove all tag html in text
		 * @param text
		 * @returns {string}
		 */
		self.htmlToPlaintext = function(text) {
			return text ? String(text).replace(/<[^>]+>/gm, '') : '';
		}
		/**
		 * Returns the function to do (save or publish) after spell check success.
		 * @return {null}
		 */
		self.getAfterSpellCheckCallback = function(){
			var callbackFunction = self.callbackfunctionAfterSpellCheck;
			self.callbackfunctionAfterSpellCheck = null;
			return callbackFunction;
		}
		/**
		 * Call the function for save or publish after spell check is success.
		 * @param callback
		 */
		self.processAfterSpellCheck = function(callback){
			if(callback != null && callback !== undefined){
				callback();
			}
		}

		/**
		 * Set type of text for romance copy field
		 * @param htmlFormat
		 */
		self.showFormatRomanceField = function (htmlFormat) {
			self.htmlShow = htmlFormat;
			self.eCommerceViewDetails.romanceCopy.htmlSave = htmlFormat;
			self.htmlTabPressing = false;
		};

		/**
		 * Prepare callback function for  after spell check is success.
		 */
		self.htmlTabClick = function(){
			self.htmlTabPressing = true;
		};
	}
})();
