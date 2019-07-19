/*
 *   ecommerceTaskDetailController.js
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
	angular.module('productMaintenanceUiApp').controller('EcommerceTaskDetailController',ecommerceTaskDetailController);
	ecommerceTaskDetailController.$inject = ['$rootScope', '$scope','EcommerceTaskApi', 'ngTableParams', '$stateParams', 'ImageApi',
		'DownloadService','urlBase', 'ProductSearchService', '$state', 'appConstants', 'productGroupApi', 'ECommerceViewApi', 'ProductSearchApi',
		'$filter', '$timeout','DownloadImageService','taskService'];
	function ecommerceTaskDetailController($rootScope, $scope, ecommerceTaskApi, ngTableParams, $stateParams, imageApi,
			downloadService, urlBase, productSearchService, $state, appConstants, productGroupApi, eCommerceViewApi, productSearchApi,
			$filter, $timeout, downloadImageService,taskService) {

		var self = this;

		/**
		 * Hierarchy context code for eCommerce
		 * @type {string}
		 */
		self.hierarchyContextCode = 'CUST';

		self.showEcommerceTaskSummary = false;

		/**
		 * Max time to wait for excel download.
		 *
		 * @type {number}
		 */
		self.WAIT_TIME = 1200;

		/**
		 * Tracks whether or not the user is waiting for a download.
		 *
		 * @type {boolean}
		 */
		self.downloading = false;

		/**
		 * Denotes whether back end should fetch total counts or not. Accordingly server side will decide on executing
		 * additional logic to fetch count information.
		 *
		 * @type {boolean}
		 */
		self.includeCounts = false;

		/**
		 * The number of rows per page constant.
		 * @type {int}
		 */
		self.DEFAULT_PAGE_SIZE = 25;
		/**
		 * The number of rows per page constant when navigate to Home page.
		 * @type {int}
		 */
		self.NAVIGATE_PAGE_SIZE = 100;

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
		 * Represents the currently selected work request referenced task.
		 * @type {null}
		 */
		self.selectedWorkRequest = null;

		/**
		 * Map of task type and description.
		 * @type {{MYTSK: string}}
		 */
		self.taskTypeMap = {'MYTSK':'Ecommerce View'};

		/**
		 * Map of task status code and it's description.
		 * @type {{ACTIV: string, CLOSD: string}}
		 */
		self.taskStatusMap = {'ACTIV':'Active', 'CLOSD':'Closed'};

		/**
		 * Represents the task currently displayed on screen.
		 * @type {{}}
		 */
		self.task = {};

		/**
		 * Represents the task currently displayed on screen.
		 * @type {{}}
		 */
		self.taskOrg = {};

		/**
		 * Logical attribute id of PDP template.
		 * @type {number}
		 */
		const LOGICAL_ATTR_PDP_TEMPLATE = 515;

		/**
		 * Sales channel of HEB.Com
		 * @type {string}
		 */
		const SALES_CHANNEL_HEB_COM = '01';

        /**
         * This message will show when there is no any Show On Site options are select.
         * @type {string}
         */
        const EMPTY_SHOW_ON_SITE = "Please select at least one Show On Site.";

        /**
		 * No image url constant.
		 * @type {string}
		 */
		self.NO_IMAGE_URL = 'images/no_image.png';

		/**
		 * Represents the supported imaged response data uri's mime and charset part. The <img> tags data uri
		 * representation generally looks like "data:[<mime type>][;charset=<charset>][;base64],<encoded data>". The
		 * "<ecoded data>" part represents the binary data expected from the server.
		 * @type {string}
		 */
		const RESP_IMAGE_DATA_TYPE = 'data:image/png;base64,';

		/**
		 * Thumbnail Image height constant.
		 * @type {number}
		 */
		const IMAGE_THUMBNAIL_HEIGHT = 40;

		/**
		 * Thumbnail Image width constant.
		 * @type {number}
		 */
		const IMAGE_THUMBNAIL_WIDTH = 40;

        /**
         * Constant for NO_CHANGE_UPDATE_MESSAGE
         * @type {string}
         */
        const NO_CHANGE_UPDATE_MESSAGE = "There are no changes on this page to be saved. Please make any changes to update.";

		/**
		 * Search criteria used during Add Products to Task.
		 */
		self.searchCriteria;

		/**
		 * Keeps track of selected selected products/Rows.
		 * @type {Array}
		 */
		self.selectedWorkRequests = [];

		/**
		 * Keeps track of excluded products/Rows when "Select All" is checked.
		 * @type {Array}
		 */
		self.excludedWorkRequests = [];
		/**
		 * Represents state of Select-All check box.
		 */
		self.allWorkRequests = false;

		/**
		 * Defaul user. Load all user's data.
		 * @type {{uid: string, fullName: string}}
		 */
		self.defaultUser = {uid:'',fullName:'All'};

		/**
		 * Currently logged in user.
		 * @type {{uid, fullName}}
		 */
		self.currentUser = {uid:$rootScope.currentUser.id, fullName: $rootScope.currentUser.name};

		/**
		 * Currently selected user/assignee initialized with the logged in user name.
		 * @type {{uid, fullName}}
		 */
		self.assignedTo = self.currentUser;

		/**
		 * List of assignee of products in the displayed task.
		 * @type {Array}
		 */
		self.assignedToUsers = [];

		/**
		 * Represents the task edit status.
		 * @type {boolean}
		 */
		self.isEditingTask =false;

        /**
		 * Represents the task delete status.
         * @type {boolean}
         */
		self.isDeletingTask = false;

		/**
		 * Search type product for ecommerce view
		 * @type {String}
		 */
		const SEARCH_TYPE = "basicSearch";
		/**
		 * Selection type product for ecommerce view
		 * @type {String}
		 */
		const SELECTION_TYPE = "Product ID";
		/**
		 * Ecommerce view tab
		 * @type {String}
		 */
		const ECOMMERCE_VIEW_TAB = 'ecommerceViewTab';

		/**
		 * Code table of show on site filter option.
		 * @type {[*]}
		 */
		self.showOnSiteOptions = [
			{code:'Y', desc:'Yes', checked: true},
			{code:'N', desc:'No', checked: true}
		];

		/**
		 * Currently displayed show on site user selection.
		 * @type {*}
		 */
		self.displayShowOnSiteOptions = angular.copy(self.showOnSiteOptions);

		/**
		 * Keeps track of user's show on site selection.
		 * @type {*}
		 */
		self.selectedShowOnSiteOptions = angular.copy(self.showOnSiteOptions);

		/**
		 * Currently selected image.
		 * @type {ProductScanImageURI}
		 */
		self.selectedImage = '';

		/**
		 * image downloading status.
		 * @type {boolean}
		 */
		self.isImageDownloading = false;
		/**
		 * Constant.
		 */
		const SPACE_STRING = ' ';
		const KEY_YES_STRING = 'Y';
		const KEY_NO_STRING = 'N';
		const ACTION_TYPE_PUBLISH = 4;

		/**
		 * The saleschannel.
		 * @type {Object}
		 */
		self.salesChannel;
		/**
		 * The list of channels.
		 * @type {Array}
		 */
		self.salesChannels=[];
		/**
		 * The pdf templates.
		 * @type {Object}
		 */
		self.pdpTemplate;
		/**
		 * The list of pdf templates.
		 * @type {Array}
		 */
		self.pdpTemplates = [];
		/**
		 * The list of fulfillmentProgram selected.
		 * @type {Array}
		 */
		self.fulfillmentProgramsSelected = [];
		/**
		 * List of FulfillmentPrograms of one channel.
		 * @type {Array}
		 */
		self.fulfillmentPrograms=[];
		/**
		 * Holds the list of FulfillmentPrograms of all channels.
		 * @type {Array}
		 */
		self.allFulfillmentPrograms=[];
		/**
		 * The showOnsite.
		 * @type {Object}
		 */
		self.showOnSite;
		/**
		 * The showOnsite select.
		 * @type {Array}
		 */
		self.showOnSiteSelect;
		/**
		 * The tomorrow date.
		 * @type {Object}
		 */
		self.tomorrow;
		/**
		 * Options for datepicker.
		 */
		self.options = {
			minDate: new Date(),
			maxDate: new Date("12/31/9999")
		};
		/**
		 * The effectiveDate.
		 * @type {Object}
		 */
		self.effectiveDate;
		/**
		 * The expirationDate.
		 * @type {Object}
		 */
		self.expirationDate;
		/**
		 * Flag for show on site start date open date picker
		 * @type {boolean}
		 */
		self.showOnSiteStartDateOpen = false;
		/**
		 * Flag for show on site end date open date picker
		 * @type {boolean}
		 */
		self.showOnSiteEndDateOpen = false;
		/**
		 * The object default select.
		 */
		self.objectSelectTemp = {
			id : null,
			description : "--Select--"
		};
		/**
		 * The list of work request selected.
		 * @type {Array}
		 */
		self.workRequestSelectedList = [];
		/**
		 * The search criteria.
		 */
		self.dataSearch = {};
		/**
		 * Setting for dropdown multiple select.
		 */
		$scope.dropdownMultiselectSettings = {
				showCheckAll: false,
				showUncheckAll: false,
				smartButtonMaxItems: 5,
				scrollableHeight: '250px',
				scrollable: true,
				checkBoxes: true
		};
		/**
		 * Text default for dropdown multiple select.
		 */
		$scope.projectText = {
			buttonDefaultText: "--Select--"
		};
		/**
		 * When click open date picker for anther attribute, store current status for date picker.
		 */
		self.openDatePicker = function (fieldName) {
			switch (fieldName) {
				case "showOnSiteStartDate":
					self.showOnSiteStartDateOpen = true;
					break;
				case "showOnSiteEndDate":
					self.showOnSiteEndDateOpen = true;
					break;
				default:
					break;
			}
			self.options = {
				minDate: new Date(),
				maxDate: new Date("12/31/9999")
			};
		};
		/**
		 * The description for mass fill data.
		 * @type {String}
		 */
		self.massFillDescription = '';
		/**
		 * List work request mass fill.
		 * @type {Array}
		 */
		self.workRequestMassFillList = [];
		/**
		 * The data mass fill for select all product.
		 * @type {Object}
		 */
		self.dataMassFillAllProduct = {};
		/**
		 * The product selected
		 * @type {number}
		 */
		self.productIdSelected = 0;
        /**
         * Assign to BDM flag
         * @type {Boolean}
         */
        self.isAssignToBDM = false;
        /**
         * Assign to EBM flag
         * @type {Boolean}
         */
        self.isAssignToEBM = false;
        /**
         * Remove all flag
         * @type {Boolean}
         */
        self.isRemoveAll = false;
        /**
		 * Task is deleted
         * @type {boolean}
         */
        self.isDeletedTask = false;
        /**
         * The constant for message when remove all successfully.
         * @type {string}
         */
        const REMOVE_ALL_SUCCESS_MESSAGE = "Delete products request submitted Successfully";
        /**
         * The constant for message and refresh when remove all successfully.
         * @type {string}
         */
        const REMOVE_ALL_SUCCESS_AND_REFRESH_MESSAGE = "Delete products request submitted Successfully. Please refresh after few seconds to see the change.";

		/**
         * This message will show when there is no any Sales Channel options are select.
         * @type {string}
         */
		const EMPTY_SALES_CHANNEL_FILTER_MESSAGE = "Please select at least one Sales Channel.";

		/**
		 * Constant for sales channels.
		 */
		const STORE_CODE = '02';
		const DINNER_TONIGHT_CODE = '07';
		const CENTRAL_MARKET_CODE = '06';
		const CENTRAL_MARKET_PREFIX = 'CM-';
		const HEB_TO_YOU_CODE = '03';
		const HEB_TO_YOU_PREFIX = '2U-';
		const EMPTY_STRING = "";
		/**
		 * The list sales channels filter.
		 * @type {Array}
		 */
		self.salesChannelsFilter =[];
		/**
		 * The list sales channels filter org.
		 * @type {Array}
		 */
		self.salesChannelsFilterOrg =[];
		/**
		 * Flag for check all Sales Channels.
		 * @type {boolean}
		 */
		self.allSalesChannels = true;
        /**
		 * Init function called during loading of this controller.
		 */
		self.init = function () {
			self.getTaskInfo($stateParams.taskId);
			self.getDataForMassFill();
			self.tomorrow = new Date();
    		self.tomorrow.setDate(new Date().getDate() + 1);
		};

		/**
		 * Fetches high level Task info.
		 * @param taskId task id.
		 */
		self.getTaskInfo = function(taskId) {
			ecommerceTaskApi.getTaskInfo(
				{ taskId: taskId},
				function(result) {
                    if(result.alertHideSw === KEY_YES_STRING){
                    	self.isDeletedTask = true;
                    } else {
                    	self.isDeletedTask = false;
                    }
					self.task = result;
					self.taskOrg = angular.copy(self.task);
					self.tableParams = self.buildTable();
					self.fetchProductsAssignee(self.task);
				},
				self.handleError
			);
		};

		/**
		 * Constructs the ng-table based data table.
		 */
		self.buildTable = function () {
			return new ngTableParams(
				{
					page: 1,
					count: self.DEFAULT_PAGE_SIZE
				}, {
					counts: [25,50,100],
					getData: function ($defer, params) {
						self.isWaiting = true;
						self.isNoRecordsFound = false;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.includeCounts = false;
						if (self.firstFetch) {
							self.includeCounts = true;
						}
						self.getTaskDetail(params.page() - 1);
					}
				}
			)
		};
		/**
		 * get product list to show on header product detail page.
		 * @param productId the product id.
		 * @param productPosition the current position of product on grid.
		 */
		self.getProductToNavigate = function(productId, productPosition){
			//Set search condition
			productSearchService.setSearchType(SEARCH_TYPE);
			productSearchService.setSelectionType(SELECTION_TYPE);
			productSearchService.setSearchSelection(productId);
			productSearchService.setListOfProducts($scope.data);
			productSearchService.setAlertId($stateParams.taskId);
			taskService.setTaskId($stateParams.taskId);
			//Set selected tab is ecommerceViewTab tab to navigate ecommerce view page
			productSearchService.setSelectedTab(ECOMMERCE_VIEW_TAB);
			//productGroupService.setProductGroupId(self.cusProductGroup.customerProductGroup.custProductGroupId);
			//Set from page navigated to
			productSearchService.setFromPage(appConstants.ECOMMERCE_TASK);
			productSearchService.setDisableReturnToList(false);

			productSearchService.productUpdatesTaskCriteria = {};
			productSearchService.productUpdatesTaskCriteria.trackingId = self.task.alertKey.trim();
			productSearchService.productUpdatesTaskCriteria.assignee = self.assignedTo.uid;
			productSearchService.productUpdatesTaskCriteria.showOnSite = self.getSelectedShowOnSite();
			productSearchService.productUpdatesTaskCriteria.salesChannelsFilter = self.getSelectedSalesChannelsFilter()
			productSearchService.productUpdatesTaskCriteria.pageIndex =  self.convertPagePerCurrentPageSizeToPagePerOneHundred(productPosition);
			productSearchService.productUpdatesTaskCriteria.pageSize = self.NAVIGATE_PAGE_SIZE;

			$state.go(appConstants.HOME_STATE);
		};
        /**
         * Convert page per current page size to page per one hundred.
         * @param productPosition the position of product on grid table.
         * @returns {number} the position of product per one hundred.
         */
        self.convertPagePerCurrentPageSizeToPagePerOneHundred = function(productPosition){
            var productPositionInDataBase = (self.tableParams.page()-1) * self.tableParams.count() + productPosition;
            return Math.floor(productPositionInDataBase/self.NAVIGATE_PAGE_SIZE) + 1;
        }
		/**
		 * search product to show on header product detail
		 * @param results
		 */
		self.searchProductToNavigate = function(results){
			var lstProducts=[];
			results.data.forEach(function(item){
				item.productMaster.alertId= self.task.alertID;
			});
			self.isWaiting = false;
			self.navigateToEcommerView(self.productIdSelected,results.data);
		};

		/**
		 * Fetches details of the task displayed on screen from database with pagination.
		 * @param page  selected page number.
		 */
		self.getTaskDetail = function(page) {
			self.clearMessage();
			self.dataSearch ={
				trackingId: self.task.alertKey.trim(),
				assignee: self.assignedTo.uid,
				showOnSite: self.getSelectedShowOnSite(),
				salesChannelsFilter : self.getSelectedSalesChannelsFilter(),
				includeCounts: self.includeCounts,
				page : page,
				pageSize : self.tableParams.count()
			};
			ecommerceTaskApi.getTaskDetail(self.dataSearch, self.loadData,self.handleError);
		};

		/**
		 * Callback for a successful call to get data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			// Update message in the case remove all
			if(self.isRemoveAll){
                self.isRemoveAll = false;
                if(results.data.length === 0){
                    self.message = REMOVE_ALL_SUCCESS_MESSAGE;
                }else{
                    self.message = REMOVE_ALL_SUCCESS_AND_REFRESH_MESSAGE;
                }
			}
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
				self.firstFetch = false;
			}
			self.resultMessage = self.getResultMessage(results.data.length, self.tableParams.page() -1);
			self.renderUserSelection(results.data, self.allWorkRequests, self.selectedWorkRequests, self.excludedWorkRequests);
			$scope.data = results.data;
			self.defer.resolve(results.data);
			if (results.data && results.data.length === 0) {
				self.isNoRecordsFound = true;
			}
			self.fetchProductImages(results.data);
			if (self.allWorkRequests) {
				self.selectedWorkRequests = [];
				$scope.data.forEach(function(item){
					var hasExist = false;
					self.excludedWorkRequests.forEach(function(workRequest){
						if(item.workRequestId == workRequest.workRequestId){
							hasExist = true;
						}
					});
					if(!hasExist){
						self.selectedWorkRequests.push(item);
					}
				});
			}
			self.convertWorkRequesToWorkRequestMassFill();
			self.isWaiting = false;
            if(results.data == null || results.data == undefined || results.data.length == 0){
            	self.isDeletingTask = true;
            } else {
            	self.isDeletingTask = false;
            }
		};

		/**
		 * Convert workRequest data to workRequestMassFill data.
		 */
		self.convertWorkRequesToWorkRequestMassFill = function(){
			$scope.data.forEach(function(item){
				if(self.dataMassFillAllProduct.isMassFillAllProduct){
					var hasExist = false;
					var workRequestMassFill = self.getWorkRequestMassFill(item, self.dataMassFillAllProduct.salesChannel, self.dataMassFillAllProduct.lstFulfillmentChannel,
												self.dataMassFillAllProduct.showOnSite?1:0, self.dataMassFillAllProduct.effectiveDate, self.dataMassFillAllProduct.expirationDate, self.dataMassFillAllProduct.pdpTemplate);
					self.workRequestMassFillList.forEach(function(workRequestMassFillOld){
						if(workRequestMassFillOld.workRequestId == item.workRequestId){
							hasExist = true;
						}
					});

					var hasExistExcludedWorkRequests = false;
					self.dataMassFillAllProduct.excludedAlerts.forEach(function(excludedAlerts){
						if(excludedAlerts == item.productId){
							hasExistExcludedWorkRequests = true;
						}
					});

					if(!hasExist && !hasExistExcludedWorkRequests){
						self.workRequestMassFillList.push(workRequestMassFill);
					}
				}
				self.workRequestMassFillList.forEach(function(workRequestMassFill){
					if(item.workRequestId == workRequestMassFill.workRequestId){
						self.mapDataMassFill(item, workRequestMassFill);
						item.isMassFill = true;
					}
				})
			})
		};

		/**
		 * Used to reload table data.
		 */
		self.refreshTable = function (page, clearMsg) {
			if(clearMsg) {
				self.clearMessage();
				self.salesChannel = angular.copy(self.objectSelectTemp);
				self.pdpTemplate = angular.copy(self.objectSelectTemp);
				self.showOnSite = angular.copy(self.objectSelectTemp);
				self.fulfillmentProgramsSelected = [];
				self.effectiveDate = null;
				self.expirationDate  = null;
			}
			self.firstFetch = true;
			self.resetWorkRequestsSelection(true);
			self.tableParams.page(page);
			self.tableParams.reload();
			self.massFillDescription = '';
			self.workRequestMassFillList = [];
			self.selectedWorkRequests = [];
			self.excludedWorkRequests = [];
			self.dataMassFillAllProduct	= {};
			self.allWorkRequests = false;
		};

		/**
		 * Return of back end after a user removes an alert. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.handleSuccess = function(resp){
			self.refreshTable(1, false);
			self.displayMessage(resp.message, false);
		};

		/**
		 * Callback that will respond to errors sent from the backend.
		 *
		 * @param error The object with error information.
		 */
		self.handleError = function(error){
			if (error && error.data && error.data.message) {
				self.displayMessage(error.data.message, true);
			} else if(error && error.data && error.data.statusText) {
				self.displayMessage(error.data.statusText, true);
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
			return "" + (self.tableParams.count() * currentPage + 1) +
				" - " + (self.tableParams.count() * currentPage + dataLength) + "  of  " +
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
		};

		/**
		 * Used to display any success of failure messages above the data table.
		 * @param message message to be displayed.
		 * @param isError is error or not; True - message displayed in red. False- message get displayed in blue.
		 */
		self.displayMessage = function(message, isError) {
            self.isError = isError;
            if(self.isRemoveAll){
                self.message = "";
            }else{
                self.message = message;
			}
		};

        /**
         * Used to display any success of failure messages for the task
         * @param message message to be displayed.
         * @param isError is error or not; True - message displayed in red. False- message get displayed in blue.
         */
        self.displayMessageTask = function(message, isError) {
            self.isErrorTask = isError;
            self.messageTask = message;
        };

		/**
		 * Used to clear any success/failure message displayed as result of last action by the user.
		 */
		self.clearMessage = function() {
			self.isError = false;
			self.message = '';
			self.isErrorMassFillData = false;
			self.isErrorRequireMess = null;
			self.errorMassFillDataLst = [];
		};

		/**
		 * Used to display fulfillment Channel description as a comma separated values.
		 * @param productFullfilmentChanels
		 * @returns {string}
		 */
		self.displayFulfilmentChannels = function(productFullfilmentChanels) {
			var fulfillmentChannelNames = [];
			_.forEach(productFullfilmentChanels, function (o) {
				var saleChannelPrefix = EMPTY_STRING;
				switch (o.key.salesChanelCode.trim()) {
					case CENTRAL_MARKET_CODE:
						saleChannelPrefix = CENTRAL_MARKET_PREFIX;
						break;
					case HEB_TO_YOU_CODE:
						saleChannelPrefix = HEB_TO_YOU_PREFIX;
				}
				fulfillmentChannelNames.push(SPACE_STRING + saleChannelPrefix + o.fulfillmentChannel.description.trim());
			});
			if(fulfillmentChannelNames.length >0){
				if(productFullfilmentChanels[0].hasMassFill){
					return productFullfilmentChanels[0].fulfillmentChannel.salesChannel.description.trim() + " - " + fulfillmentChannelNames.join();
				}
			}
			return fulfillmentChannelNames.join();
		};

		/**
		 * Used to pull out PDP attribute info out of all the attribute information of a particular product.
		 * @param masterDataExtensionAttributes
		 * @returns {string}
		 */
		self.displayPDPTemplate = function (masterDataExtensionAttributes) {
			return _.pluck(_.filter(masterDataExtensionAttributes,
				{key :{'attributeId': LOGICAL_ATTR_PDP_TEMPLATE}}), 'attributeValueText').join();
		};

		/**
		 * Used to show the status of show on site based on the show on site data for HEB.Com. It is assumed the default
		 * sales channel for Ecommerce Task screen is HEB.com
		 * @param productOnlines
		 * @returns {*}
		 */
		self.displayShowOnSite = function (productOnlines) {
			var tempArray = _.filter(productOnlines,{key :{'saleChannelCode': SALES_CHANNEL_HEB_COM}});
			tempArray.sort(function(date1, date2){
				return new Date(date2.key.effectiveDate) - new Date(date1.key.effectiveDate);
			});
			return _.pluck(tempArray, 'showOnSiteByExpirationDate')[0];
		};

		/**
		 * Used to fetch primary image of product referenced by the work request.
		 * @param workRequests
		 */
		self.fetchProductImages = function (workRequests) {
			if (workRequests) {
				_.forEach(workRequests, function (wrkRqst) {
					self.fetchProductImage(wrkRqst.productMaster);
				});
			}
		};

		/**
		 * Used to fetch primary image of product referenced by the Product Master.
		 * @param productMaster
		 */
		self.fetchProductImage = function (productMaster) {
			imageApi.getPrimaryImageByProductId(
				{
					productId : productMaster.prodId,
					salesChannelCode : SALES_CHANNEL_HEB_COM,
					width: IMAGE_THUMBNAIL_WIDTH,
					height: IMAGE_THUMBNAIL_HEIGHT
				},
				function(result) {
					if(result.data && result.data.image && result.data.image.length>0) {
						productMaster.image =  RESP_IMAGE_DATA_TYPE+result.data.image;
					} else {
						productMaster.image = self.NO_IMAGE_URL;
					}
				},
				function(error) {
					productMaster.image = self.NO_IMAGE_URL;
				}
			);
		};

		/**
		 * Used to fetch notes/comments of the selected work request (product).
		 * @param workRequestId
		 */
		self.getProductNotes = function(workRequestId) {
			ecommerceTaskApi.getProductNotes(
				{workRequestId : workRequestId},
				function(result) {
					console.log(result);
				},
				self.handleError
			);
		};

		/**
		 * Used to launch and display the product notes modal/pop-up screen.
		 * @param workRequest
		 */
		self.displayTaskNotes = function(task) {
			$rootScope.$broadcast('resetTaskNotes');
			$('#taskNotesModal').modal('show');
		};

		/**
		 * Used to launch and display the product notes modal/pop-up screen.
		 * @param workRequest
		 */
		self.displayProductNotes = function(workRequest) {
			self.selectedWorkRequest = workRequest;
			$timeout( function(){
				$rootScope.$broadcast('openProductNotes');
			}, 300);
		};

		/**
		 * Used to display Add products modal.
		 */
		self.addProducts = function() {
			self.searchCriteria = null;//clear any previously searched criteria.
			$rootScope.$broadcast('resetProductSearch');
			$('#addProductsModal').modal('show');
		};

		/**
		 * called by product-search-criteria when search button pressed
		 * @param searchCriteria
		 */
		self.updateSearchCriteria = function(searchCriteria) {
			self.searchCriteria = searchCriteria;
		};

		/**
		 * called by product-search-selection when select button pressed
		 * @param productSelection
		 */
		self.updateSearchSelection = function(selectedProducts) {
			if(!self.searchCriteria.isSelectAll){
				var productIds = [];
				angular.forEach(selectedProducts, function (selectedProduct) {
					if (productIds.indexOf(selectedProduct.prodId) == -1) {
						productIds.push(selectedProduct.prodId);
					}
				});
				self.searchCriteria.productIds = productIds.toString();
				self.searchCriteria.excludedProducts = null;
			}
			self.submitAddProducts();
		};

		/**
		 * Handles submit of add products. On click of submit, this method collects and sends all the selected products
		 * (and any exclusions) to the backend service to be added to this task.
		 */
		self.submitAddProducts = function() {
			self.searchCriteria.trackingId = self.task.alertKey.trim();
			ecommerceTaskApi.addProducts(
				self.searchCriteria,
				function(result) {
					self.displayMessage(result.message, false);
					$('#addProductsModal').modal("hide");
				},
				function(error) {
					self.handleError(error);
					$('#addProductsModal').modal("hide");
				}
			);
		};

		/**
		 * Resets all previous row selections when "Select All" is checked/unchecked.
		 */
		self.resetWorkRequestsSelection = function(includeSelectAll) {
			if (includeSelectAll) {
				self.allWorkRequests = false;
			}
			self.selectedWorkRequests = [];
			self.excludedWorkRequests = [];
			if(self.allWorkRequests){
				$scope.data.forEach(function(workRequest){
					self.selectedWorkRequests.push(workRequest)
				});
				self.dataMassFillAllProduct.isSelectedAllProduct = true;

			}else {
				self.dataMassFillAllProduct.isSelectedAllProduct = false;
			}
		};

		/**
		 * Keeps track of row selection and exclusions in consideration to the state of "Select All" option.
		 *
		 * @param alertChecked selected row state. True/False.
		 * @param alert	data (Alert) object of the row that was modified.
		 */
		self.toggleProductSelection = function(workRequest) {
			if (self.allWorkRequests) {//checking if "Select All" is checked?
				!workRequest.checked ? self.excludedWorkRequests.push(workRequest)
					: _.remove(self.excludedWorkRequests, function(o) {
						var check = o.workRequestId == workRequest.workRequestId;
							return check;
					});
			}
				workRequest.checked ? self.selectedWorkRequests.push(workRequest)
					: _.remove(self.selectedWorkRequests, function(o) {
						var check = o.workRequestId == workRequest.workRequestId;
						return check;
					});
		};

		/**
		 * Used to send selected products to back end to be deleted. If select-all is chosen, then it displays the
		 * remove-all confirmation modal for user to confirm his action, otherwise it just proceeds with sending the
		 * selected products and tracking id (task id) to the backend service for removal from the task.
		 */
		self.removeProducts = function(){
			self.clearMessage();
			if(self.allWorkRequests || (self.selectedWorkRequests != null && self.selectedWorkRequests.length > 0)){
				$('#removeAllProductsModal').modal("show");
			}else {
				self.confirmSelectProductsModalContent = "Please select at least one Product to remove.";
				$('#confirmSelectProductsModal').modal({ backdrop: 'static', keyboard: true });
			}
		};

		/**
		 * Handles removing all the products from the task. The backend service implementaion for this is a Batch handling,
		 * hence this method's service callback does not wait for all products to be removed and so simply confirms on
		 * successful submit of the batch.
		 */
		self.removeAllProducts = function() {
			if (self.allWorkRequests) {
				var data = {
					trackingId : self.task.alertKey.trim(),
					assignee: self.assignedTo.uid,
					showOnSite: self.getSelectedShowOnSite(),
					salesChannelsFilter : self.getSelectedSalesChannelsFilter(),
					productIds : self.getProducts(self.excludedWorkRequests)};
				self.isRemoveAll = true;
				ecommerceTaskApi.removeAllProducts(data,self.handleSuccess,self.handleError);
			} else {
				var data = {trackingId: self.task.alertKey.trim(), productIds: self.getProducts(self.selectedWorkRequests)};
				ecommerceTaskApi.removeProducts(data,self.handleSuccess,self.handleError);
			}
		};

		/**
		 * Utility function - used to collect product-ids from the given work requests.
		 * @param workRqstArr array of work requests.
		 * @returns {Array} array of product Ids.
		 */
		self.getProducts = function(workRqstArr) {
			var productIds = [];
			_.forEach(workRqstArr, function (o) {
				productIds.push(o.productId);
			});
			return productIds;
		};

		/**
		 * Used to maintain the state of previous row selections by the user. Checks/Unchecks rows during data-loading (as
		 * result of refresh or pagination).
		 *
		 * @param workRqstDataArr		new list of data fetched from backend.
		 * @param allWorkRqstChecked 	is select all checked; true/false.
		 * @param selectedAlerts 		list of selected work requests/products.
		 * @param excludedWorkRqst 		list of excluded work requests/products.
		 */
		self.renderUserSelection = function(workRqstDataArr, allWorkRqstChecked, selectedWorkRqst, excludedWorkRqst) {
			if (allWorkRqstChecked) {
				_.forEach(workRqstDataArr, function (workRqst) {
					var index = _.findIndex(excludedWorkRqst, function(o) { return o.workRequestId == workRqst.workRequestId;});
					workRqst.checked = !(index > -1);
					/*if (index > -1) {
						workRqst.checked = false;
					} else {
						workRqst.checked = true;
					}*/
				});
			} else {
				_.forEach(workRqstDataArr, function (workRqst) {
					var index = _.findIndex(selectedWorkRqst, function(o) { return o.workRequestId == workRqst.workRequestId;});
					workRqst.checked = index > -1;
					/*if (index > -1) {
						workRqst.checked = true;
					} else {
						workRqst.checked = false;
					}*/
				});
			}
		};

		/**
		 * Fetch list of users to whom the products in the task has been assgined to.
		 * @param task
		 */
		self.fetchProductsAssignee = function(task) {
			ecommerceTaskApi.getProductsAssignee(
				{trackingId : task.alertKey.trim()},
				function(result) {
					self.assignedToUsers = result;
				},
				self.handleError
			);
		};

		/**
		 * Handle change to assignee from the drop down. On change of user, this function sets the selected assignee
		 * and kick starts refresh of the table with newly changed assingee name.
		 * @param user selected user/assignee.
		 */
		self.toggleAssignee = function(user) {
			self.assignedTo = user;
			self.refreshTable(1, true);
		};

		/**
		 * Handles loading the currently logged in user's task information(products).
		 */
		self.loadMyTask = function () {
			self.toggleAssignee(self.currentUser);
		};

		/**
		 * Used to undo any task info modifications.
		 */
		self.resetTask = function () {
			self.task = angular.copy(self.taskOrg);
			self.isEditingTask = false;
            self.messageTask = null;
            self.isErrorTask = false;
        };

        /**
         * Check if has changed data
         * @returns {boolean}
         */
        self.hasDataChanged = function(){
            return !(JSON.stringify( angular.copy(self.taskOrg)) === JSON.stringify(angular.copy(self.task)))
        };

        /**
         * The funtion is used to update task.
         */
        self.updateTask = function() {
            if(self.hasDataChanged()) {
                ecommerceTaskApi.updateTask(
                    {taskId: self.task.alertID}, self.task,
                    function (response) {
                        self.displayMessageTask(response.message, false);
                        self.isEditingTask = false;
                        self.taskOrg = angular.copy(self.task);
                    },
                    self.handleError
                );
            }else {
                self.messageTask = NO_CHANGE_UPDATE_MESSAGE;
                self.isErrorTask = true;
            }
        };

		self.deleteTask = function() {
			ecommerceTaskApi.deleteTask(
				self.task,
				function (results) {
                    taskService.setReturnToEcommerceTaskList(true);
                    taskService.setAlertDataTxt(self.task.alertDataTxt);
                    $state.go(appConstants.HOME_STATE);
                },
				self.handleError
			);
        };
		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {
			var encodedUri = self.generateExportUrl();
			if(encodedUri !== self.EMPTY_STRING) {
				self.downloading = true;
				downloadService.export(encodedUri, 'eCommerceTaskDetails.csv', self.WAIT_TIME,
					function () {
						self.downloading = false;
					});
			}
		};

		/**
		 * Generates the URL to ask for the export.
		 *
		 * @returns {string} The URL to ask for the export.
		 */
		self.generateExportUrl = function() {
			var showOnSite = self.getSelectedShowOnSite();
			if (showOnSite === null) {
				showOnSite = '';
			}
			var salesChannelsFilter = self.getSelectedSalesChannelsFilter();
			if (salesChannelsFilter === null) {
				salesChannelsFilter = '';
			}
			return urlBase + '/pm/task/ecommerceTask/exportTaskDetailToCsv?' +
				'trackingId=' + self.task.alertKey.trim() +
				'&assignee=' + self.assignedTo.uid +
				'&showOnSite=' + showOnSite +
				'&salesChannelsFilter=' + salesChannelsFilter;
		};

		/**
		 * Called from child component eCommerceTaskHierarchyAssignment when the assignment changes are saved
		 */
		self.updateAssignment = function() {
			self.refreshTable();
		};
		/**
		 * Backup search condition product  for ecommerce View
		 */
		self.navigateToEcommerView = function(productId,products) {
			//Set search condition
			productSearchService.setSearchType(SEARCH_TYPE);
			productSearchService.setSelectionType(SELECTION_TYPE);
			productSearchService.setSearchSelection(productId);
			productSearchService.setListOfProducts(products);
			productSearchService.setAlertId($stateParams.taskId);
			taskService.setTaskId($stateParams.taskId);
			//Set selected tab is ecommerceViewTab tab to navigate ecommerce view page
			productSearchService.setSelectedTab(ECOMMERCE_VIEW_TAB);
			//productGroupService.setProductGroupId(self.cusProductGroup.customerProductGroup.custProductGroupId);
			//Set from page navigated to
			productSearchService.setFromPage(appConstants.ECOMMERCE_TASK);
			productSearchService.setDisableReturnToList(false);
			$state.go(appConstants.HOME_STATE);
		};

		/**
		 * Handles applying filter change for update reason. Eventually tiggeres reloading of data table based on user selection.
		 */
		self.applyFilterShowOnSite = function() {
            self.selectedShowOnSiteOptions = angular.copy(self.displayShowOnSiteOptions);
            var optionsSelected = _.pluck(_.filter(self.selectedShowOnSiteOptions, function(o){ return o.checked == true;}),'code');
            if(optionsSelected.length > 0){
                self.refreshTable(1, true);
                $('#showOnSiteFilter').removeClass("open");
            } else {
                self.isError = true;
                self.message = EMPTY_SHOW_ON_SITE;
            }
		};

		/**
		 * Resets show on site selections to initial state.
		 */
		self.resetFilterShowOnSite = function() {
			self.displayShowOnSiteOptions = angular.copy(self.showOnSiteOptions);
			self.selectedShowOnSiteOptions = angular.copy(self.showOnSiteOptions);
		};

		/**
		 * Cancels users selection. Changes displayed options state to previously saved state.
		 */
		self.cancelFilterShowOnSite = function() {
			self.displayShowOnSiteOptions = angular.copy(self.selectedShowOnSiteOptions);
			$('#showOnSiteFilter').removeClass("open");
		};

		/**
		 * Used to show on site status of Y/N based on user selection. Returns null if both options are selected or
		 * unselected, meaning to fetch all records.
		 * @returns {null}
		 */
		self.getSelectedShowOnSite = function() {
			var optionsSelected = _.pluck(_.filter(self.selectedShowOnSiteOptions, function(o){ return o.checked == true;}),'code');
			return (optionsSelected.length == 1) ? optionsSelected[0] : null;
		};

		/**
		 * Show full primary image, and allow user download
		 */
		self.showFullImage  = function (prodId) {
			$('#imageModal').modal({backdrop: 'static', keyboard: true});
			self.fetchProductFullImage(prodId);
		};

		/**
		 * Used to fetch primary image of product referenced by the product id.
		 * @param productMaster
		 */
		self.fetchProductFullImage = function (prodId) {
			self.selectedImage = null;
			self.isImageDownloading = true;
			imageApi.getPrimaryImageByProductId(
				{
					productId : prodId,
					salesChannelCode : SALES_CHANNEL_HEB_COM
				},
				function(result) {
					self.isImageDownloading = false;
					if(result.data) {
						self.selectedImage =  result.data;
					}
				},
				function(error) {
					self.isImageDownloading = false;
				}
			);
		};

		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if(self.selectedImage != null){
				var imageFormat = (self.selectedImage.imageFormat=='' ? 'png' : self.selectedImage.imageFormat).trim();
				var imageBytes = self.selectedImage.image;
				downloadImageService.download(imageBytes, imageFormat);
			}
		};

		/**
		 * Get data for mass fill.
		 */
		self.getDataForMassFill = function(){
			self.salesChannel = angular.copy(self.objectSelectTemp);
			self.pdpTemplate = angular.copy(self.objectSelectTemp);
			self.showOnSite = angular.copy(self.objectSelectTemp);
			self.fulfillmentProgramsSelected = [];
			self.effectiveDate = null;
			self.expirationDate  = null;
			self.showOnSiteSelects = [
				{
					id : null,
					description : "--Select--"
				},
				{
					id : 1,
					description : "Yes"
				},
				{
					id : 0,
					description : "No"
				}
			];
			self.loadPDPTemplates();
			self.loadFulfillmentPrograms();
			$scope.handleEventSelectDropdown = {
				onSelectionChanged: function() {
					if(self.fulfillmentProgramsSelected == null || self.fulfillmentProgramsSelected.length ==0){
						self.showOnSite = angular.copy(self.objectSelectTemp);
						self.effectiveDate = null;
						self.expirationDate  = null;
						self.fulfillmentPrograms.forEach(function(fulfillmentProgram){
							fulfillmentProgram.disabled = false;
						});
					}else{
						var fulfillmentProgramTemp = self.fulfillmentProgramsSelected[self.fulfillmentProgramsSelected.length-1];
						if(fulfillmentProgramTemp.abbreviation.trim() == 'Dsply'){
							self.fulfillmentPrograms.forEach(function(fulfillmentProgram){
								if(fulfillmentProgram.abbreviation.trim() != 'Dsply'){
									fulfillmentProgram.disabled = true;
								}
							});
							self.fulfillmentProgramsSelected = [fulfillmentProgramTemp];
						}else{
							self.fulfillmentPrograms.forEach(function(fulfillmentProgram){
								fulfillmentProgram.disabled = false;
							});
						}
					}
				}
			};
		};

		/**
		 * Load the list of channels from api.
		 */
		self.loadSalesChannels = function(){
			productGroupApi.findAllSaleChanel(
				//success
				function (results) {
					self.salesChannels = [];
					self.salesChannels.push(self.salesChannel);
					self.salesChannelsFilter = [];
					_.forEach(results, function (salesChannel) {
						if (salesChannel.id !== STORE_CODE && salesChannel.id !== DINNER_TONIGHT_CODE) {
							self.salesChannels.push(angular.copy(salesChannel));
							salesChannel.checked = true;
							self.salesChannelsFilter.push(salesChannel);
						}
					});
					self.salesChannelsFilterOrg = angular.copy(self.salesChannelsFilter);
					self.selectedSalesChannelsFilter = angular.copy(self.salesChannelsFilter);
				}, self.handleError
			);
		};

		/**
		 * Load the list of pdf templates from api.
		 */
		self.loadPDPTemplates = function () {
			eCommerceViewApi.findAllPDPTemplate(
				//success
				function (results) {
					self.pdpTemplates.push(self.pdpTemplate);
					self.pdpTemplates = self.pdpTemplates.concat(results);
				}
				,self.handleError
			);
		};
		/**
		 * Load the list of fulfillments for all sales channels from api.
		 */
		self.loadFulfillmentPrograms = function(){
			productSearchApi.queryFulfilmentChannels(function(results){
				self.allFulfillmentPrograms = results;
				self.loadSalesChannels();
			}, self.handleError);
		};
		/**
		 * Get the list of FulfillmentPrograms by sales channel id.
		 *
		 * @return the list of FulfillmentPrograms.
		 */
		self.getFulfillmentPrograms = function(channelId){
			var results = [];
			self.allFulfillmentPrograms.forEach(function(item, index){
					if(item.salesChannel.id ==  channelId){
						item.id = index;
						item.label = item.description.trim();
						results.push(item);
					}
				}
			);
			results.sort();
			results.reverse();
			return results;
		};

		/**
		 * Handle data when change sale channel.
		 */
		self.handleChangeSalesChannel = function(){
			self.fulfillmentProgramsSelected = [];
			self.showOnSite = angular.copy(self.objectSelectTemp);
			self.effectiveDate = null;
			self.expirationDate  = null;
			self.fulfillmentPrograms = self.getFulfillmentPrograms(self.salesChannel.id);
		};

		/**
		 * Handle data show on site date.
		 */
		self.handleShowOnSiteDate = function() {
			if(self.showOnSite == null || self.showOnSite.id == null || self.showOnSite.id == 0){
				self.effectiveDate = null;
				self.expirationDate  = null;
			}else{
				self.effectiveDate = self.tomorrow;
				self.expirationDate = new Date("12/31/9999");
			}
		};

		/**
		 * Mass fill data with product.
		 */
		self.massFillDataWithProduct = function(){
			self.clearMessage();
			if(!self.validateMassFillData()){
				if((self.selectedWorkRequests != null && self.selectedWorkRequests.length > 0) || (self.allWorkRequests && self.excludedWorkRequests.length<self.totalRecordCount)){
					self.isWaiting = true;
					if(self.dataMassFillAllProduct.isSelectedAllProduct){
						var workRequestMassFillListTemp = [];
						if(self.excludedWorkRequests != undefined && self.excludedWorkRequests != null && self.excludedWorkRequests.length >0 ){
							self.workRequestMassFillList.forEach(function(workRequestMassFillOld,index){
								self.excludedWorkRequests.forEach(function(workRequest){
									if(workRequestMassFillOld.workRequestId == workRequest.workRequestId){
										workRequestMassFillListTemp.push(workRequestMassFillOld);
									}
								});
							})
						}
						self.workRequestMassFillList = workRequestMassFillListTemp;
						self.setDataMassFillAllProduct();
					}
					self.selectedWorkRequests.forEach(function(workRequest){
						var workRequestMassFill = self.getWorkRequestMassFill(workRequest, self.salesChannel, self.fulfillmentProgramsSelected, self.showOnSite, self.effectiveDate, self.expirationDate, self.pdpTemplate);
						var hasExist = false;
						$scope.data.forEach(function(item){
							if(item.workRequestId == workRequest.workRequestId){
								self.mapDataMassFill(item, workRequestMassFill);
								item.isMassFill = true;
							}
						});
						self.workRequestMassFillList.forEach(function(workRequestMassFillOld){
							if(workRequestMassFillOld.workRequestId == workRequest.workRequestId){
								self.mapDataMassFill(workRequestMassFillOld, workRequestMassFill);
								hasExist = true;
							}
						});
						if(!hasExist){
							self.workRequestMassFillList.push(workRequestMassFill);
						}
					});
					$timeout(function() {
						self.isWaiting = false;
					}, 1500);
				}else {
					self.confirmSelectProductsModalContent = "Please select at least one Product to Mass Fill.";
					$('#confirmSelectProductsModal').modal({ backdrop: 'static', keyboard: true });
				}
			}
		};

		/**
		 * Mapp data between two workRequest.
		 */
		self.mapDataMassFill = function(workRequestMap, workRequestMassFill){
			workRequestMap.pdpTemplate = angular.copy(workRequestMassFill.pdpTemplate);
			workRequestMap.productMaster = angular.copy(workRequestMassFill.productMaster);
			workRequestMap.candidateFulfillmentChannels = angular.copy(workRequestMassFill.candidateFulfillmentChannels);
		};

		/**
		 * Set data mass fill when check all data.
		 */
		self.setDataMassFillAllProduct = function(){
			self.dataMassFillAllProduct.isMassFillAllProduct = true;
			var excludedAlerts = [];
			self.excludedWorkRequests.forEach(function(item){
				excludedAlerts.push(item.productId);
			});
			self.dataMassFillAllProduct.pdpTemplate = angular.copy(self.pdpTemplate);
			self.dataMassFillAllProduct.lstFulfillmentChannel = angular.copy(self.fulfillmentProgramsSelected);
			self.dataMassFillAllProduct.salesChannel = angular.copy(self.salesChannel);
			if(self.showOnSite != null && self.showOnSite.id == 1){
				self.dataMassFillAllProduct.isShowOnSite = true;
				self.dataMassFillAllProduct.effectiveDate = self.convertDateToStringWithYYYYMMDD(self.effectiveDate);
				self.dataMassFillAllProduct.expirationDate = self.convertDateToStringWithYYYYMMDD(self.expirationDate);
			}else{
				self.dataMassFillAllProduct.isShowOnSite = false;
				self.dataMassFillAllProduct.effectiveDate = null;
				self.dataMassFillAllProduct.expirationDate = null;
			}
			self.dataMassFillAllProduct.excludedAlerts = excludedAlerts;
		};

		/**
		 * Get WorkRequest when mass fill.
		 */
		self.getWorkRequestMassFill = function (workRequest, salesChannel, fulfillmentProgramsSelected, showOnSite, effectiveDate, expirationDate, pdpTemplate){
			var workRequestMassFill = angular.copy(workRequest);
			var productFullfilmentChanels = self.getFullfilmentChanelsMassFill(true, salesChannel, fulfillmentProgramsSelected, showOnSite, effectiveDate, expirationDate);
			var candidateFulfillmentChannels = self.getFullfilmentChanelsMassFill(false, salesChannel, fulfillmentProgramsSelected, showOnSite, effectiveDate, expirationDate);
			workRequestMassFill.isMassFill = true;
			if(pdpTemplate != null && pdpTemplate.id != null){
				var isMassFillPdpTemplateSuccess = false;
				workRequestMassFill.pdpTemplate = angular.copy(pdpTemplate);
				workRequestMassFill.productMaster.masterDataExtensionAttributes.forEach(function(masterDataExtensionAttribute, index){
					if(masterDataExtensionAttribute.key.attributeId ==  LOGICAL_ATTR_PDP_TEMPLATE){
						masterDataExtensionAttribute.attributeValueText = pdpTemplate.description;
						isMassFillPdpTemplateSuccess = true;
					}
				});
				if(!isMassFillPdpTemplateSuccess){
					var masterDataExtensionAttribute = {
						key : {attributeId : LOGICAL_ATTR_PDP_TEMPLATE},
						attributeValueText : pdpTemplate.description,
					}
					workRequestMassFill.productMaster.masterDataExtensionAttributes.push(masterDataExtensionAttribute);
				}
			}

			if(productFullfilmentChanels.length > 0){
				productFullfilmentChanels.forEach(function(productFullfilmentChanel, index){
					productFullfilmentChanel.key.productId = workRequestMassFill.productId;
				});
				workRequestMassFill.productMaster.productFullfilmentChanels = angular.copy(productFullfilmentChanels);
				workRequestMassFill.candidateFulfillmentChannels = angular.copy(candidateFulfillmentChannels);
			}
			return workRequestMassFill;
		};

		/**
		 * Get fullfilmentChanels mass fill
		 */
		self.getFullfilmentChanelsMassFill =function(isProduct, salesChannel, fulfillmentProgramsSelected, showOnSite, effectiveDate, expirationDate){
			var fullfilmentChanels = [];
			if(salesChannel != null && salesChannel.id != null && fulfillmentProgramsSelected != null && fulfillmentProgramsSelected.length > 0){
				fulfillmentProgramsSelected.forEach(function(fulfillmentChannelSelected, index){
					var fullfilmentChanel;
					if(isProduct){
						fullfilmentChanel = {
							key :{
								salesChanelCode : fulfillmentChannelSelected.key.salesChannelCode,
								fullfillmentChanelCode : fulfillmentChannelSelected.key.fulfillmentChannelCode
							},
							fulfillmentChannel : fulfillmentChannelSelected
						}
						if(showOnSite != null && showOnSite.id == 1){
							fullfilmentChanel.effectDate = self.convertDateToStringWithYYYYMMDD(effectiveDate);
							fullfilmentChanel.expirationDate = self.convertDateToStringWithYYYYMMDD(expirationDate);
						}else{
							fullfilmentChanel.effectDate = null;
							fullfilmentChanel.expirationDate = null;
						}
					}else{
						fullfilmentChanel = {
							key :{
								salesChannelCode : fulfillmentChannelSelected.key.salesChannelCode,
								fulfillmentChannelCode : fulfillmentChannelSelected.key.fulfillmentChannelCode
							},
						}
						if(self.showOnSite != null && self.showOnSite.id == 1){
							fullfilmentChanel.newData = KEY_YES_STRING;
							fullfilmentChanel.effectiveDate = self.convertDateToStringWithYYYYMMDD(effectiveDate);
							fullfilmentChanel.expirationDate = self.convertDateToStringWithYYYYMMDD(expirationDate);
						}else{
							fullfilmentChanel.newData = KEY_NO_STRING;
							fullfilmentChanel.effectiveDate = null;
							fullfilmentChanel.expirationDate = null;
						}
					}
					fullfilmentChanel.hasMassFill = true;
					fullfilmentChanels.push(fullfilmentChanel);
				});

			}
			return fullfilmentChanels;
		};

		/**
		 * Validate data mass fill.
		 */
		self.validateMassFillData = function(){
			self.isErrorMassFillData = false;
			self.isErrorRequireMess = null;
			self.errorMassFillDataLst = [];
			if((self.pdpTemplate == null || self.pdpTemplate.id == null)
				&& (self.salesChannel == null || self.salesChannel.id == null || self.fulfillmentProgramsSelected == null || self.fulfillmentProgramsSelected.length == 0)){
					self.isErrorMassFillData = true;
					self.isErrorRequireMess = "Please select value for Mass Fill."
			}else if(self.salesChannel != null && self.salesChannel.id != null && self.fulfillmentProgramsSelected != null && self.fulfillmentProgramsSelected.length > 0){
				if(self.showOnSite == null || self.showOnSite.id == null){
					self.isErrorMassFillData = true;
					self.errorMassFillDataLst.push("Fulfillment Program Value is a mandatory field.");
				}else if(self.showOnSite.id == 1){
					if (self.isDate1GreaterThanDate2(self.tomorrow, self.effectiveDate)) {
						self.errorMassFillDataLst.push("Start Date must be greater than Current Date.");
						self.isErrorMassFillData = true;
					}
					if(!self.isDate1GreaterThanDate2(self.expirationDate, self.effectiveDate)) {
						self.isErrorMassFillData = true;
						if (!self.isDate1GreaterThanDate2(self.tomorrow, self.effectiveDate)) {
							self.errorMassFillDataLst.push("Effective Date must be less than End Date.");
						}
						self.errorMassFillDataLst.push("End Date must be greater than Effective Date and less than 12/31/9999.");
					}
				}

			}
			return self.isErrorMassFillData;
		};

		/**
		 * Handle click button save mass fill.
		 */
		self.saveDataMassFill = function (){
			if(self.workRequestMassFillList == null || self.workRequestMassFillList.length == 0){
				self.isErrorMassFillData = true;
				self.isErrorRequireMess = NO_CHANGE_UPDATE_MESSAGE;
			}else {
				self.massFillDescription = '';
				$('#confirmSaveMassFill').modal({ backdrop: 'static', keyboard: true });
			}
		};

		/**
		 * Do save data mass fill.
		 */
		self.doSaveDataMassFill = function(){
			self.isWaiting = true;
			var massUpdateTaskRequest = {
				isSelectAll : self.dataMassFillAllProduct.isMassFillAllProduct,
				excludedAlerts : self.dataMassFillAllProduct.excludedAlerts,
				selectedCandidateWorkRequests : self.workRequestMassFillList,
				salesChannel : self.dataMassFillAllProduct.salesChannel,
				lstFulfillmentChannel : self.dataMassFillAllProduct.lstFulfillmentChannel,
				isShowOnSite : self.dataMassFillAllProduct.isShowOnSite==null || self.dataMassFillAllProduct.isShowOnSite==undefined || !self.dataMassFillAllProduct.isShowOnSite?KEY_NO_STRING:KEY_YES_STRING,
				effectiveDate : self.dataMassFillAllProduct.effectiveDate,
				expirationDate : self.dataMassFillAllProduct.expirationDate,
				pdpTemplate : self.dataMassFillAllProduct.pdpTemplate==null || self.dataMassFillAllProduct.pdpTemplate==undefined || self.dataMassFillAllProduct.pdpTemplate.id==null?null:self.dataMassFillAllProduct.pdpTemplate,
				trackingId: self.task.alertKey.trim(),
				assigneeId: self.assignedTo.uid,
				showOnSiteFilter: self.getSelectedShowOnSite(),
				salesChannelsFilter : self.getSelectedSalesChannelsFilter(),
				description : self.massFillDescription
			}
			ecommerceTaskApi.updateMassFillToProduct(massUpdateTaskRequest,self.massUpdateTaskRequestSuccess, self.handleError);
		};

		/**
		 * Handle data when save mass fill success.
		 */
		self.massUpdateTaskRequestSuccess = function (response) {
			self.isWaiting = false;
			self.trackingId = response.data;
			$('#confirmSaveSuccessMassFill').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Go to check status page when click button check status in popup.
		 */
		self.goToCheckStatusPage = function() {
			$('#confirmSaveSuccessMassFill').modal("hide");
			$('#confirmSaveSuccessMassFill').on('hidden.bs.modal', function () {
				$state.go(appConstants.CHECK_STATUS,{trackingId:self.trackingId});
			});
		};

		/**
		 * Get total record mass fill.
		 */
		self.getTotalRecordMassFill = function() {
			if(self.dataMassFillAllProduct.isMassFillAllProduct && self.dataMassFillAllProduct.excludedAlerts != null && self.dataMassFillAllProduct.excludedAlerts != undefined){
				var total = self.totalRecordCount;
				self.dataMassFillAllProduct.excludedAlerts.forEach(function(excludedWorkRequest){
					var hasExist = false;
					self.workRequestMassFillList.forEach(function(workRequestMassFill){
						if(excludedWorkRequest == workRequestMassFill.productId){
							hasExist = true;
						}
					});
					if(!hasExist){
						total = total - 1;
					}
				});
				return total;
			}else{
				return self.workRequestMassFillList.length;
			}
		};

		/**
		 * Get title for FufillmentProgram select dropdown.
		 */
		self.getTitleForFulfillmentProgram = function() {
			var fulfillmentChannelNames = [];
			_.forEach(self.fulfillmentProgramsSelected, function (o) {
				fulfillmentChannelNames.push(SPACE_STRING + o.description.trim());
			});
			return fulfillmentChannelNames.join();
		};

		/**
		 * Compare the date is greater than date 2 or not.
		 *
		 * @param date1 the date.
		 * @param date2 the date.
		 * @returns {boolean} true if the date1 is greater than date 2 or false.
		 */
		self.isDate1GreaterThanDate2 = function (date1, date2) {
			if ((new Date(self.convertDateToStringWithYYYYMMDD(date1)).getTime() > new Date(self.convertDateToStringWithYYYYMMDD(date2)).getTime())) {
				return true;
			}
			return false;
		};

		/**
		 * Convert the date to string with format: yyyy-MM-dd.
		 * @param date the date object.
		 * @returns {*} string
		 */
		self.convertDateToStringWithYYYYMMDD = function (date) {
			return $filter('date')(date, 'yyyy-MM-dd');
		};

		/**
		 * Show modal when click button assign task to BDM.
		 */
		self.assignToBDM = function(){
            self.isAssignToBDM = true;
            self.isAssignToEBM = false;
			if(self.allWorkRequests || (self.selectedWorkRequests != null && self.selectedWorkRequests.length > 0)){
				self.confirmAssignTaskContent = "Are you sure you want to assign to BDM the selected Product?";
				$('#confirmAssignTask').modal({ backdrop: 'static', keyboard: true });
			}else {
				self.confirmSelectProductsModalContent = "Please select at least one Product to assign to BDM";
				$('#confirmSelectProductsModal').modal({ backdrop: 'static', keyboard: true });
			}
		};

		/**
		 * Do assign task to BDM.
		 */
		self.doAssignToBDM = function(){
			var excludedAlerts = [];
			self.excludedWorkRequests.forEach(function(item){
				excludedAlerts.push(item.productId);
			});
			var massUpdateTaskRequest = {
				isSelectAll : self.allWorkRequests,
				excludedAlerts : excludedAlerts,
				selectedCandidateWorkRequests : self.selectedWorkRequests,
				trackingId: self.task.alertKey.trim(),
				assigneeId: self.assignedTo.uid,
				alertId: self.task.alertID
			}
			ecommerceTaskApi.assignToBDM(massUpdateTaskRequest,self.assignToBDMSuccess, self.handleError);
		};

		/**
		 * Handle data when assign task TO BDM success.
		 */
		self.assignToBDMSuccess = function(response) {
			self.refreshTable(1,true);
			self.confirmAssignTaskSuccessContent = "The product(s) have been assigned to the BDM(s) successfully.";
			$('#confirmAssignTaskSuccess').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Show modal when click button assign task to EBM.
		 */
		self.assignToEBM = function(){
            self.isAssignToBDM = false;
            self.isAssignToEBM = true;
			if(self.allWorkRequests || (self.selectedWorkRequests != null && self.selectedWorkRequests.length > 0)){
				self.confirmAssignTaskContent = "Are you sure you want to assign to eBM the selected Product?";
				$('#confirmAssignTask').modal({ backdrop: 'static', keyboard: true });
			}else {
				self.confirmSelectProductsModalContent = "Please select at least one Product to assign to eBM";
				$('#confirmSelectProductsModal').modal({ backdrop: 'static', keyboard: true });
			}
		};

		/**
		 * Do assign task to EBM.
		 */
		self.doAssignToEBM = function(){
			var excludedAlerts = [];
			self.excludedWorkRequests.forEach(function(item){
				excludedAlerts.push(item.productId);
			});
			var massUpdateTaskRequest = {
				isSelectAll : self.allWorkRequests,
				excludedAlerts : excludedAlerts,
				selectedCandidateWorkRequests : self.selectedWorkRequests,
				trackingId: self.task.alertKey.trim(),
				assigneeId: self.assignedTo.uid,
				alertId: self.task.alertID,
				role: "BDM"
			}
			ecommerceTaskApi.assignToEBM(massUpdateTaskRequest,self.assignToEBMSuccess, self.handleError);
		};

		/**
		 * Handle data when assign task to EBM success.
		 */
		self.assignToEBMSuccess = function(response) {
			self.refreshTable(1,true);
			self.confirmAssignTaskSuccessContent = "The product(s) have been assigned to the eBM(s) successfully.";
			$('#confirmAssignTaskSuccess').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Handle reset button.
		 */
		self.onReset = function(){
			if(self.task){
				self.fetchProductsAssignee(self.task);
			}
			self.assignedTo = self.currentUser;
			self.resetFilterShowOnSite();
			self.resetFilterFulfillmentChannel(true);
			self.refreshTable(1,true);
		};

		/**
		 * Check data before publish.
		 */
		self.publish = function () {
			if(self.allWorkRequests || (self.selectedWorkRequests !== null && self.selectedWorkRequests.length > 0)){
				$('#confirmatioPublishProductModal').modal("show");
			}else {
				self.confirmSelectProductsModalContent = "Please select at least one product to Publish";
				$('#confirmSelectProductsModal').modal({backdrop: 'static', keyboard: true});
			}
		};

		/**
		 * Publish product.
		 */
		self.publishProduct = function () {
			var excludedAlerts = [];
			self.excludedWorkRequests.forEach(function (item) {
				excludedAlerts.push(item.productId);
			});
			var massUpdateTaskRequest = {
				isSelectAll: self.allWorkRequests,
				selectedCandidateWorkRequests: self.selectedWorkRequests,
				alertType: 'MYTSK',
				trackingId: self.task.alertKey.trim(),
				effectiveDate: self.dataMassFillAllProduct.effectiveDate,
				expirationDate: self.dataMassFillAllProduct.expirationDate,
				assigneeId: self.assignedTo.uid,
				attributes: _.trim(self.dataMassFillAllProduct.attributes) === '' ? null : _.trim(self.dataMassFillAllProduct.attributes).split(","),
				excludedAlerts: excludedAlerts,
				actionType: ACTION_TYPE_PUBLISH,
				alertId:$stateParams.taskId
			};
			self.isWaiting = true;
			ecommerceTaskApi.publishProduct(massUpdateTaskRequest, function (response) {
				self.isWaiting = false;
				self.trackingId = response.data;
				$('#confirmSaveSuccessMassFill').modal("show");
			}, self.handleError)
		};

        /**
		 * Return to list ecommerce task in home page.
         */
		self.returnToEcommerceTaskList = function () {
			taskService.setReturnToEcommerceTaskList(true);
            $state.go(appConstants.HOME_STATE);
		}

		/**
		 * Handles applying filter change for Fulfillment Channel. Eventually tiggeres reloading of data table based on user selection.
		 */
		self.applyFilteFulfillmentChannel = function() {
			self.selectedSalesChannelsFilter = angular.copy(self.salesChannelsFilter);
			var selectedSalesChannels = _.pluck(_.filter(self.selectedSalesChannelsFilter, function(o){ return o.checked == true;}),'id');
			if(selectedSalesChannels.length > 0){
				self.refreshTable(1, true);
				$('#fulfillmentChannelFilter').removeClass("open");
			}else {
				self.isError = true;
				self.message = EMPTY_SALES_CHANNEL_FILTER_MESSAGE ;
			}
		};

		/**
		 * Resets Fulfillment Channel selections to initial state.
		 */
		self.resetFilterFulfillmentChannel = function(resetAllData) {
			self.salesChannelsFilter = angular.copy(self.salesChannelsFilterOrg);
			self.allSalesChannels = true;
			if(resetAllData) self.selectedSalesChannelsFilter = angular.copy(self.salesChannelsFilterOrg);
		};

		/**
		 * Cancels users selection. Changes displayed options state to previously saved state.
		 */
		self.cancelFilterFulfillmentChannel = function() {
			self.salesChannelsFilter = angular.copy(self.selectedSalesChannelsFilter);
			self.setSelectAllSalesChannels();
			$('#fulfillmentChannelFilter').removeClass("open");
		};

		/**
		 * Used to build list Sales Channel code/numbers based on the user selection. If user havent made any selection,
		 * it returns null instead of sending all the default-selected attributes code. This is done to save query fetch performance.
		 */
		self.getSelectedSalesChannelsFilter = function() {
			var selectedSalesChannels = _.pluck(_.filter(self.selectedSalesChannelsFilter, function(o){ return o.checked == true;}),'id');
			return (selectedSalesChannels.length != self.salesChannelsFilterOrg.length) ? selectedSalesChannels.join(): null;
		};

		/**
		 * Set flag select all Sales Channel or not when user selected Sales Channel
		 */
		self.setSelectAllSalesChannels = function () {
			var selectedSalesChannels = _.filter(self.salesChannelsFilter, function (o) { return o.checked == true; });
			self.allSalesChannels = selectedSalesChannels.length == self.salesChannelsFilterOrg.length ? true : false;
		}

		/**
		 * Set check/uncheck Sales Channel when user select all/unselect all Sales Channel
		 */
		self.setSelectedForAllSalesChannels = function () {
			_.map(self.salesChannelsFilter, function (o) { return o.checked = self.allSalesChannels; });
		}
	}
})();
