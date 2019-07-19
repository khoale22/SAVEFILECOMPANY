/*
 *   imageInfoComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Selling Unit -> Image Info component.
 *
 * @author s753601
 * @since 2.13.0
 */
(function () {

        angular.module('productMaintenanceUiApp').component('imageInfo', {
            // isolated scope binding
            bindings: {
                sellingUnit: '<',
                productMaster: '<'
            },
            // Inline template which is binded to message variable in the component controller
            templateUrl: 'src/productDetails/productSellingUnits/imageInfo.html',
            // The controller that handles our component logic
            controller: ImageInfoController
        });
        angular.module('productMaintenanceUiApp').directive('customNavigate', function ($templateCache, $compile, $timeout) {
            return {
                restrict: 'A',
                scope: true,
                link: function (scope, element, attr) {
                    var template = $templateCache.get(attr.templatePagination);
                    $timeout(function () {
                        // angular.element( document.querySelector( '#paging' ) ).remove();
                        angular.element(document.querySelector('#customPaging')).append($compile(template.replace('hidden', ''))(scope));
                    }, 1000);
                }
            }
        });

        ImageInfoController.$inject = ['productSellingUnitsApi', 'customHierarchyService', '$state', '$rootScope',
            'appConstants', 'imageInfoService', 'PermissionsService', 'ProductSearchService', 'DownloadImageService', 'ngTableParams', '$scope'];

        /**
         * UPC Info component's controller definition.
         * @constructor
         * @param productSellingUnitsApi
         * @param customHierarchyService
         * @param $state
         * @param $rootScope
         * @param appConstants
         * @param imageInfoService
         * @param permissionsService
         * @param productSearchService
         * @param downloadImageService
         * @param ngTableParams
         * @param $scope
         * @constructor
         */
        function ImageInfoController(productSellingUnitsApi, customHierarchyService, $state, $rootScope, appConstants,
                                     imageInfoService, permissionsService, productSearchService, downloadImageService, ngTableParams, $scope) {

            var self = this;

            /**
             * When attempting to make an invalid upload request these messages will tell you what the cause of the error is
             * @type {string}
             */
            var INVALID_FILE_UPLOAD = "Attempting to upload an invalid file type";
            var MISSING_OR_INVALID_FILE = "Missing or Invalid file type cannot upload";
            var MISSING_IMAGE_SOURCE = "Missing Image Source";
            var MISSING_IMAGE_CATEGORY = "Missing Image Category";
            var IMAGE_UPLOAD_FAIL_TO_LAUNCH = "Failed to upload Image because of the following error(s)\n ";
            var MISSING_DESTINATION_FOR_UPLOAD = "Missing Destination Domain";
            var MISSING_DESTINATION_DOMAIN = "Destination Domain is mandatory";
            var APPROVED_STATUS_CODE = "A";
            var STATUS_FOR_REVIEW = 'S';
            var STATUS_REJECTED = 'R';
            var PRIMARY_PRIORITY = "P";
            var PRIMARY_ALTERNATE = 'A';
            var PRIMARY_NOT_APPLICABLE = 'NA';
            var CATEGORY_SWATCHES = 'SWAT';
            var MISSING_PRIMARY_STRING = "Alternate Images cannot be \" Active Online\" without a Primary Image that is \"Active\".";

            /**
             * Validation messages
             */
            const ONLY_ONE_PRIMARY_FOR_PRODUCT = "Only image can be Primary for a product.";
            const IMG_ACTIVE_ONLINE_IN_ALTERNATE
                = 'Alternate Images cannot be "Active Online" without a Primary Image that is "Active Online".';
            const IMG_STATUS_CHANGE_TO_REVIEW
                = 'An image in "Approved" or  "Rejected" status cannot be moved to "For Review" Status.';
            const IMG_ACTIVE_ONLINE_IN_CATEGORY_SWATCH
                = 'Swatch images cannot be "Active Online" without a Primary image that is "Active Online".';
            const IMG_PRIMARY_REJECTED = "An image in 'Rejected' status cannot be set as 'Primary'.";
            const IMG_PRIMARY_FOR_REVIEW_ACTONLINE = 'An image in "For review" status can not be made "Active Online".';
            const IMG_STATUS_REJECTED = 'An image in "Rejected" status cannot be made "Active Online".';
            const IMG_ACTIVE_ONLINE_IS_NONE_CATEGORY_OR_PRIMARY_ALTERNATE
                = 'An image cannot be "Active Online" without the "Image Category" or the "Primary/Alternate" defined.';
            const MESSAGE_NO_DATA_CHANGE
                = "There are no changes on this page to be saved. Please make any changes to update.";
            /**
             * Boolean for if images are present
             * @type {boolean}
             */
            self.imageInfo = false;

            /**
             * List of Approved Images
             * @type {Array}
             */
            self.approvedImageInfo = [];

            /**
             * List of Active Online Images
             * @type {Array}
             */
            self.activeOnlineImages = [];

            /**
             * List of Pending and Rejected Images
             * @type {Array}
             */
            self.submittedImages = [];

            /**
             * List of URIs to be sent to webservice for image retrieval
             * @type {Array}
             */
            self.imageURIs = [];

            /**
             * Flag for indicating that the front end is still waiting on data from the backend
             */
            self.isWaitingForResponse = false;

            /**
             * Success message to be displayed
             */
            self.success = null;

            /**
             * Error message to be displayed
             */
            self.error = null;
            /**
             * Whenever the user attempts a change that is not valid this message will be displayed
             * @type {null}
             */
            self.invalidChange = null;

            /**
             * List of categories for the dropdown
             */
            self.categories = [];

            /**
             * List of statuses for the dropdown
             */
            self.statuses = [];

            /**
             * List of destinations for the dropdown
             \         */
            self.destinations = [];

            /**
             * List of priorities for the dropdown
             */
            self.priorities = [];
            /**
             * List of iamge sources for a dropdown
             * @type {{}}
             */
            self.sources = {};

            /**
             * Flag for displaying rejected images.
             */
            self.showRejected = false;

            /**
             * String for the show rejected button
             */
            self.showRejectedDisplay = "Show Rejected";

            /**
             * String for the show all button
             */
            self.showAllDisplay = "Show All";

            /**
             * Image category for new image candidate
             * @type {string}
             */
            self.uploadImageCategory = "";
            /**
             * Arrays of original data used for comparison and for data reset
             * @type {Array}
             */
            self.originalImages = [];
            self.originalActiveImages = [];
            self.originalSubmittedImage = [];

            /**
             * These flags are used to tell the component what part of the updated it is still waiting on
             * @type {boolean}
             */
            self.waitingForAddingDestintions = false;
            self.waitingForRemovingDestinations = false;
            self.waitingForDataUpdates = false;
            /**
             * A list of changes that need to be made to the upc's image info.
             * @type {Array}
             */
            self.destinationUpdates = [];


            /**
             * Image source for new image candidate
             * @type {string}
             */
            self.uploadImageSource = "";

            /**
             * List of destinations for new image candidate
             * @type {{}}
             */
            self.uploadImageDestinations = {};

            /**
             * Byte[] for new image candidate
             * @type {string}
             */
            self.uploadImageImage = "";

            /**
             * Current image loading.
             * @type {string}
             */
            self.imageBytes = '';

            /**
             * Current image format.
             * @type {string}
             */
            self.imageFormat = '';

            /**
             * This flag states if the current file is of a valid file type
             * @type {boolean}
             */
            self.invalidFileType = false;

            /**
             * List of valid file types
             * @type {[string,string,string,string,string]}
             */
            self.validFileTypes = ["jpg", "jpeg", "png"];

            /**
             * This object handles all of the image editing functionality
             * @type {null}
             */
            self.cropper = null;
            /**
             * This variable keeps track of the number of degrees the image has been spinned
             * @type {number}
             */
            self.currentAngle = 0;
            /**
             * This variable keeps track how if the x axis has been flipped
             * @type {number}
             */
            self.scaleX = 1;
            /**
             * This variable keeps track if the y axis has been flipped
             * @type {number}
             */
            self.scaleY = 1;
            /**
             * This variable holds the data for the new cropped image
             * @type {null}
             */
            self.editedImage = null;
            /**
             * This variable is used to send the new cropped image to the backend.
             * @type {null}
             */
            self.imageToEditData = null;

            /**
             * Flag stating if the image information is missing a destination domain
             * @type {boolean}
             */
            self.missingDestination = false;

            /**
             * A list of selected images to possibly be copied to an hierarchy
             * @type {Array}
             */
            self.selectedImages = [];
            /**
             * Flag for if all the images are selected
             * @type {boolean}
             */
            self.selectAllImages = false;
            /**
             * A flag to state if either a single or all entities in the default path need to be copied
             * @type {null}
             */
            self.hierarchyLevels = null;
            /**
             * The default hierarchy path for a product
             * @type {Array}
             */
            self.hierarchyPath = [];
            /**
             * The string used to display the default path of a product
             * @type {null}
             */
            self.hierarchyString = null;
            /**
             * The list of upcs attached to a product and used to get all of the images attached to the product
             * @type {Array}
             */
            self.upcs = [];
            /**
             * Flag stating if there is a default hierarchy path available.
             * @type {boolean}
             */
            self.isDisableHierarchy = false;
            /**
             * This flag is set to true when the images status is in an illegal state
             * @type {boolean}
             */
            self.invalidStatus = false;
            /**
             * This string is set when the images status is in an illegal state
             * @type {string}
             */
            self.invalidStatusString = "";

            /**
             * Check if it is the first time to load images or not.
             *
             * @type {boolean}
             */
            self.isFisrtLoad = true;

            /**
             * The default page number.
             *
             * @type {number}
             */
            self.PAGE = 1;

            /**
             * Total element in page
             * @type {number}
             */
            self.PAGE_SIZE = 10;

            /**
             * The ngTable object that will be waiting for data while the report is being refreshed.
             *
             * @type {object}
             */
            self.defer = null;

            /**
             * The parameters passed from the ngTable when it is asking for data.
             *
             * @type {object}
             */
            self.dataResolvingParams = null;

            /**
             * The total number of pages in the report.
             *
             * @type {null}
             */
            self.totalPages = null;

            /**
             * The total records in the report.
             *
             * @type {null}
             */
            self.totalRecordCount = null;

            /**
             * The flag for show/hide rejected images.
             * @type {boolean}
             */
            self.isShowRejected = false;

            /**
             * Check if it is search with count or not.
             *
             * @type {boolean}
             */
            self.includeCount = false;

            /**
             * The flag check for show/hide navigation confirm popup.
             * @type {boolean}
             */
            self.isShowingNavigationConfirmPopup = false;

            /**
             * Total images of selling unit UPC.
             * @type {number}
             */
            self.totalImages = 0;

            /**
             * Total approved images of selling unit UPC.
             * @type {number}
             */
            self.totalApprovedImages = 0;

            /**
             * Total need approving images of selling unit UPC.
             * @type {number}
             */
            self.totalNeedingApprovingImages = 0;

            /**
             * Set value margin top css of object.
             * @type {string}
             */
            self.marginTop = "0px";

            /**
             * Function getActiveOnlineImages has called.
             * @type {boolean}
             */
            self.getActiveOnlineImagesHasCalled = false;

            /**
             * Current productId.
             * @type {null}
             */
            self.currentProductId = null;

            /**
             * Initialize some default values .
             * @type {string}
             */
              const EMPTY = "";
              const SHOW_ALL = "Show All";
              const HIDE_ALL = "Hide All";
              const HIDE_REJECTED = "Hide Rejected";
              const SHOW_REJECTED = "Show Rejected";

            /**
             * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
             * (or re-initialized).
             */
            this.$onInit = function () {
                self.disableReturnToList = productSearchService.getDisableReturnToList();
                self.imageInfo = false;
                productSellingUnitsApi.getImageCategories(self.loadImageCategories, self.handleError);
                productSellingUnitsApi.getImageStatuses(self.loadImageStatuses, self.handleError);
                productSellingUnitsApi.getImageDestinations(self.loadDestinations, self.handleError);
                productSellingUnitsApi.getImagePriorities(self.loadPriorities, self.handleError);
                productSellingUnitsApi.getImageSources(self.loadImageSources, self.handleError);
                self.cropper = window.Cropper;
                var image = document.getElementById('imageToEdit');
                self.cropper = new Cropper(image, {
                    minContainerWidth: 1000,
                    minContainerHeight: 750
                });
            };

            /**
             * Component will reload the vendor data whenever the item is changed in casepack.
             */
            this.$onChanges = function () {
                self.isAllButtonClicked = undefined;
                self.error = null;
                self.success = null;
                self.invalidChange = null;
                self.resetCountImages();
                if (self.productMaster !== null && self.productMaster !== undefined) {
                    self.isWaitingForResponse = true;
                    self.imageInfo = false;
                    self.upcs = [];
                    angular.forEach(self.productMaster.sellingUnits, function (sellingUnit) {
                        self.upcs.push(sellingUnit.upc);
                    });
                    if(!self.getActiveOnlineImagesHasCalled || self.currentProductId === self.productMaster.prodId){
                        self.getActiveOnlineImages();
                    } else {
                        self.getActiveOnlineImagesHasCalled = false;
                    }
                }
            };

            /**
             * Reset variables count image
             */
            self.resetCountImages = function () {
                self.totalImages = 0;
                self.totalApprovedImages = 0;
                self.totalNeedingApprovingImages = 0;
            };
            /**
             * Load image sources that response from api.
             * @param results the list of image sources.
             */
            self.loadImageSources = function (results) {
                self.sources = results;
            };

            /**
             * Load image categories that response from api.
             * @param results the list of image categories.
             */
            self.loadImageCategories = function (results) {
                self.categories = results;
            };

            /**
             * Load image statuses that response from api.
             * @param results the list of image status.
             */
            self.loadImageStatuses = function (results) {
                angular.forEach(results, function (status) {
                    if (status.id !== '') {
                        self.statuses.push(status);
                    }
                });
            };

            /**
             * Load destination that response from api.
             * @param results the list of destinations.
             */
            self.loadDestinations = function (results) {
                self.destinations = results;
                self.uploadImageDestinations = results;
            };

            /**
             * Load priorities that response from api.
             * @param results the list of Priorities.
             */
            self.loadPriorities = function (results) {
                self.priorities = results;
            };

            /**
             * Build ng table for Active Online Images
             */
            self.getActiveOnlineImages = function () {
                self.getActiveOnlineImagesHasCalled = true;
				self.isWaitingForResponse = true;
				self.currentProductId = self.productMaster.prodId;
				productSellingUnitsApi.getActiveOnlineImages({
                    upcs: self.upcs
                }, self.loadActiveOnlineImageTable, self.handleError);
            };

            /**
             * Load active online image that response from api.
             * @param results the list of active online image info.
             */
            self.loadActiveOnlineImageTable = function (results) {
                self.activeOnlineImages = results.data;
                self.originalActiveImages = angular.copy(self.activeOnlineImages);
                self.buildActiveOnlineImageTable();
				self.loadActiveOnlineImages();
				//Count images follow image status in active online.
                for(var index in results.data){
                    if(results.data[index].imageStatus.id === APPROVED_STATUS_CODE){
                        self.totalApprovedImages++;
                    }
                    if(results.data[index].imageStatus.id === STATUS_REJECTED || results.data[index].imageStatus.id === STATUS_FOR_REVIEW){
                        self.totalNeedingApprovingImages++;
                    }
                }
                var upcs = [];
                upcs.push(self.sellingUnit.upc);
                var params = {
                    upcs : upcs
                };
                //Get submitted images of upc with all image status.
                productSellingUnitsApi.getCountSubmittedImages(params,
                    self.countImages,
                    self.handleError);
            };

            /**
             * Count images follow image status in submitted images.
             * @param results the list submitted image info.
             */
            self.countImages = function (results) {
                self.totalApprovedImages += results.approved;
                self.totalNeedingApprovingImages += results.needApproved;
                self.totalImages = self.totalApprovedImages + self.totalNeedingApprovingImages;
                if(self.isNavigateToNewPage){
                    self.isNavigateToNewPage = false;
                    self.buildSubmittedImageTable(self.nextPage);
                }else{
                    self.buildSubmittedImageTable(null);
                }
            };

            /**
             * Build ng table for active online image.
             */
            self.buildActiveOnlineImageTable = function () {
                self.activeOnlineImageTable = new ngTableParams({
                    page: 1,
                    count: self.activeOnlineImages.length
                }, {
                    counts: [],
                    orderBy: 'imagePriorityCode',
                    data: self.activeOnlineImages
                });
			};

            /**
             * Build ng table for Submitted Images
             * @param page the page index. if it is, then get default page.
             */
			self.buildSubmittedImageTable = function (page) {
				self.isWaitingForResponse = true;
				self.firstSearch=true;
				self.submittedImageTable = new ngTableParams(
					{
						page: page == null ? self.PAGE : page,
						count: self.PAGE_SIZE
					}, {
						counts: [],

						/**
						 * Called by ngTable to load data.
						 *
						 * @param $defer The object that will be waiting for data.
						 * @param params The parameters from the table helping the function determine what data to get.
						 */
						getData: function ($defer, params) {

							self.isWaitingForResponse = true;
							// Save off these parameters as they are needed by the callback when data comes back from
							// the back-end.
							self.defer = $defer;
							self.dataResolvingParams = params;

							// If it is first time to search, then it search with count, otherwise it doesn't include count.
							if (self.firstSearch) {
								self.includeCount = true;
								self.firstSearch = false;
							} else {
								self.includeCount = false;
							}
							// Issue calls to the backend to get the data.

                            if(self.isAllButtonClicked){
                                if(angular.equals(self.showAllDisplay , HIDE_ALL)){
                                    self.isShowRejected = true;
                                    self.showRejectedDisplay = HIDE_REJECTED;
                                    self.getSubmittedImagePage(params.page() - 1);
                                }else{
                                    self.isShowRejected = false;
                                    self.showRejectedDisplay = SHOW_REJECTED;
                                    self.resetSubmittedImageToNullObject();
                                }
                            }else if(self.isAllButtonClicked === false){
                                if(angular.equals(self.showRejectedDisplay , HIDE_REJECTED)) {
                                    if (_.pluck(_.filter(self.originalSubmittedImage, function(o){ return o.imageStatus.id === STATUS_FOR_REVIEW;})).length >0 ||
                                        _.pluck(_.filter(self.originalSubmittedImage, function(o){ return o.imageStatus.id === APPROVED_STATUS_CODE;})).length >0 ||
                                        (self.originalSubmittedImage.length > 0 && !angular.equals(self.showAllDisplay , SHOW_ALL))){
                                        self.isShowAllImages = true;
                                        self.showAllDisplay = HIDE_ALL;
                                        self.getSubmittedImagePage(params.page() - 1);
                                    }else{
                                        self.getRejectedImagePage(params.page() - 1);
                                    }
                                }else{
                                   if (_.pluck(_.filter(self.originalSubmittedImage, function(o){ return o.imageStatus.id === STATUS_FOR_REVIEW;})).length === 0 &&
                                       _.pluck(_.filter(self.originalSubmittedImage, function(o){ return o.imageStatus.id === APPROVED_STATUS_CODE;})).length === 0 &&
                                       !angular.equals(self.showAllDisplay , HIDE_ALL)){
                                       self.isShowAllImages = false;
                                       self.showAllDisplay = SHOW_ALL;
                                       self.resetSubmittedImageToNullObject();
                                   }else{
                                       self.isShowRejected = false;
                                       self.showRejectedDisplay = SHOW_REJECTED;
                                       self.getSubmittedImagePage(params.page() - 1);
                                   }
                                }
                            }else{
                                self.getSubmittedImagePage(params.page() - 1);
                            }
						}
					}
				);
			};

            /**
             * This function to reset submittedImages to null object.
             */
			self.resetSubmittedImageToNullObject = function (){
                self.isWaitingForResponse = false;
                self.dataResolvingParams.data = [];
                self.submittedImages = [];
                self.originalSubmittedImage = angular.copy(self.submittedImages);
                self.defer.resolve(self.submittedImages);
            };

            /**
             * Get the rejected image by page.
             * @param page the page index.
             */
            self.getRejectedImagePage = function (page) {
                var upcs = [];
                upcs.push(self.sellingUnit.upc);
                var params = {
                    upcs : upcs,
                    page: page,
                    pageSize: self.PAGE_SIZE,
                    includeCounts: self.includeCount
                };
                productSellingUnitsApi.getRejectedImages(params,
                    self.loadSubmittedImageTable,
                    self.handleError);
            };

            /**
             * Get the submitted image by page.
             * @param page the page index.
             */
			self.getSubmittedImagePage = function (page) {
				var upcs = [];
				upcs.push(self.sellingUnit.upc);
				var params = {
					upcs : upcs,
					isShowRejected:  self.isShowRejected,
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCount
				};
				productSellingUnitsApi.getSubmittedImages(params,
					self.loadSubmittedImageTable,
					self.handleError);
			};

            /**
             * Load submitted image into the table.
             * @param results the list of submitted image.
             */
			self.loadSubmittedImageTable = function (results) {
				self.error = null;
				if (results.data.complete) {
					self.totalRecordCount = results.data.recordCount;
					self.totalPages = results.data.pageCount;
					self.dataResolvingParams.total(self.totalRecordCount);
				}
				if (results.data.data.length === 0) {
					self.dataResolvingParams.data = [];
                    self.submittedImages = [];
                    self.originalSubmittedImage = angular.copy(self.submittedImages);
					self.defer.resolve(self.submittedImages);
					self.error = self.NO_RECORDS_FOUND;
				} else {
					self.submittedImages = results.data.data;
                    self.originalSubmittedImage = angular.copy(self.submittedImages);
					self.defer.resolve(self.submittedImages);
                    angular.forEach(self.submittedImages, function (image) {
                        image.selected = false;
                        image.useImageData = false;
                        image.imagePriorityCode = self.trimData(image.imagePriorityCode);
                        image.imageStatusCode = self.trimData(image.imageStatusCode);
                    });
                    self.loadSubmittedImages();
                }
                if(self.totalPages>1){
                    self.marginTop = "20px";
                }else {
                    self.marginTop = "0px";
                }
                self.isWaitingForResponse = false;
            };

            /**
             * Switch the image between active online and submitted.
             * If it is submitted then move it to online and else move it to submitted.
             * @param image the image info.
             */
            self.switchImageBetweenActiveOnlineAndSubmitted = function (image) {
                if (image.activeOnline) {
                    self.submittedImages.splice(self.submittedImages.indexOf(image), 1);
                    self.activeOnlineImages.push(angular.copy(image));
                } else {
                    self.activeOnlineImages.splice(self.activeOnlineImages.indexOf(image), 1);
                    self.submittedImages.push(angular.copy(image));
                }
                self.buildActiveOnlineImageTable();
            };

            /**
             * Get all active online image data from api.
             */
            self.loadActiveOnlineImages = function () {
                angular.forEach(self.activeOnlineImages, function (image) {
                    if (image !== undefined && image !== null) {
                        self.getSingleImage(image);
                    }
                })
            };

            /**
             * Get all active online image data from api.
             */
            self.loadSubmittedImages = function () {
                angular.forEach(self.submittedImages, function (image) {
                    if (image !== undefined && image !== null) {
                        self.getSingleImage(image);
                    }
                })
            };

            /**
             * This method makes a call to get all of the images for the image info individually
             * @param image the image.
             */
            self.getSingleImage = function (image) {
                productSellingUnitsApi.getImages(image.imageURI,
                    function (results) {
                        var imageData = EMPTY;
                        var useImageData = false;
                        if (image.imageFormat.indexOf("TIF") != -1) {
                            image.useImageData = false;
                        } else if (results.data.length !== 0) {
                            imageData = "data:image/jpg;base64," + results.data;
                            useImageData = true;
                        } else {
                            useImageData = false;
                        }
                        image.imagePriorityCode = self.trimData(image.imagePriorityCode);
                        image.imageStatusCode = self.trimData(image.imageStatusCode);
                        image.image = imageData;
                        image.useImageData = useImageData;
                        for (var originalIndex = 0; originalIndex < self.originalActiveImages.length; originalIndex++) {
                            if (image.key.id === self.originalActiveImages[originalIndex].key.id
                                && image.key.sequenceNumber === self.originalActiveImages[originalIndex].key.sequenceNumber) {
                                self.originalActiveImages[originalIndex] = angular.copy(image);
                                break;
                            }
                        }
                        for (originalIndex = 0; originalIndex < self.originalSubmittedImage.length; originalIndex++) {
                            if (image.key.id === self.originalSubmittedImage[originalIndex].key.id
                                && image.key.sequenceNumber === self.originalSubmittedImage[originalIndex].key.sequenceNumber) {
                                self.originalSubmittedImage[originalIndex] = angular.copy(image);
                                break;
                            }
                        }
                    });
            };

            /**
             * Handle the error from the back end.
             * @param error the error.
             */
            self.handleError = function (error) {
                self.setDisplayNameForShowOrHideRejectedButton();
                self.setDisplayNameForShowOrHideAllButton();
                self.isWaitingForResponse = false;
                if (error && error.data) {
                    self.isWaitingForResponse = false;
                    if (error.data.message) {
                        self.setError(error.data.message);
                    } else {
                        self.setError(error.data.error);
                    }
                }
                else {
                    self.setError("An unknown error occurred.");
                }
            };

            /**
             * Sets the controller's error message.
             * @param error The error message.
             */
            self.setError = function (error) {
                self.error = error;
            };

            /**
             * Sends the user to the custom hierarchy for that image
             * @param entityId
             */
            self.navigateToCustomHierarchy = function (entityId) {
                imageInfoService.setEntityId(entityId);
                customHierarchyService.setEntityId(entityId);
                $state.go(appConstants.CUSTOM_HIERARCHY_ADMIN);
            };

            /**
             * This method converts the list of destinations into a more readable form
             * @param destinations the list of destinations
             * @returns {string} a human readable list of destinations
             */
            self.getDestinations = function (destinations) {
                var string = EMPTY;
                if (destinations.length > 0) {
                    string = destinations[0].description;
                    for (var index = 1; index < destinations.length; index++) {
                        string += ", " + destinations[index].description;
                    }
                }
                return string;
            };

            /**
             * This method checks to make sure all required information is present to create a new image record
             * @returns {boolean} true it is valid or false it's invalid.
             */
            self.isValidImageForUpload = function () {
                var invalidUpload = false;
                var invalidComponents = IMAGE_UPLOAD_FAIL_TO_LAUNCH;
                if (angular.equals(self.uploadImageCategory, EMPTY)) {
                    invalidComponents = invalidComponents + MISSING_IMAGE_CATEGORY;
                    invalidUpload = true;
                }
                if (angular.equals(self.uploadImageSource, EMPTY)) {
                    if (invalidUpload) {
                        invalidComponents = invalidComponents + "\n ";
                    }
                    invalidComponents = invalidComponents + MISSING_IMAGE_SOURCE;
                    invalidUpload = true;
                }
                if (self.uploadImageDestinations.length === 0) {
                    if (invalidUpload) {
                        invalidComponents = invalidComponents + "\n ";
                    }
                    invalidComponents = invalidComponents + MISSING_DESTINATION_FOR_UPLOAD;
                    invalidUpload = true;
                }
                if (self.invalidFileType) {
                    if (invalidUpload) {
                        invalidComponents = invalidComponents + "\n ";
                    }
                    invalidComponents = invalidComponents + MISSING_OR_INVALID_FILE;
                    invalidUpload = true;
                }
                if (invalidUpload) {
                    alert(invalidComponents);
                    return false;
                }
                return true;
            };

            /**
             * After successfully uploading an image resets the fields
             */
            self.resetUploadFields = function () {
                document.getElementById("uploadImageData").value = null;
                self.uploadImageCategory = EMPTY;
                self.uploadImageSource = EMPTY;
                self.uploadImageDestinations = self.destinations;
                self.uploadImageImage = EMPTY;
            };

            /**
             * This method will check if the upload request is valid and then make a call to the api to upload the image
             * This method will also close the modal
             */
            self.uploadImage = function () {
                self.error = null;
                self.success = null;
                if (self.isValidImageForUpload()) {
                    $('.modal').modal('hide');
                    var uploadImage = {
                        upc: self.sellingUnit.upc,
                        imageCategoryCode: self.uploadImageCategory,
                        imageSourceCode: self.uploadImageSource,
                        destinationList: self.uploadImageDestinations,
                        imageData: self.uploadImageImage,
                        imageName: self.uploadImageName
                    };
                    productSellingUnitsApi.uploadImage(uploadImage, self.uploadImageResult, self.handleError);
                    self.isWaitingForResponse = true;
                }
            };

            /**
             * On successful upload reset the view
             * @param results
             */
            self.uploadImageResult = function (results) {
                self.resetCountImages();
                self.success = 'Image "' + self.uploadImageName
                    + '" is uploaded successfully and associated with the UPC "'
                    + self.sellingUnit.upc + '"';
                self.resetUploadFields();
                self.getActiveOnlineImages();
            };


            /**
             * Initializes the modal and sets the onchange for the file input to check the file type
             */
            self.initModal = function () {
                var uploadImageData = document.getElementById("uploadImageData");
                var invalidSpan = document.getElementById("invalidSpan");
                var imageTypeCode = EMPTY;
                self.invalidFileType = true;
                uploadImageData.addEventListener('change', function (e) {
                    self.uploadImageName = EMPTY;
                    var imageData = uploadImageData.files[0];
                    imageTypeCode = imageData.type.split('/').pop();
                    if (self.validFileTypes.includes(imageTypeCode.toLowerCase())) {
                        invalidSpan.style.display = 'none';
                        self.uploadImageName = imageData.name;
                        self.invalidFileType = false;
                        var reader = new FileReader();
                        reader.onloadend = function () {
                            self.uploadImageImage = reader.result.split(',').pop();
                        };
                        reader.readAsDataURL(imageData);
                    } else {
                        self.invalidFileType = true;
                        invalidSpan.style.display = 'block';
                        alert(INVALID_FILE_UPLOAD);
                    }
                });
            };

            /**
             * This method resets all of the image info data to its original value
             */
            self.resetImageInfo = function () {
                self.selectedImages = [];
                self.error = null;
                self.success = null;
                self.invalidStatusString = '';
                self.activeOnlineImages = angular.copy(self.originalActiveImages);
                self.submittedImages = angular.copy(self.originalSubmittedImage);
                self.buildActiveOnlineImageTable();
            };

            /**
             * This method will take determine the changes to be made then send the changes to the backend for updating
             */
            self.saveImageInfo = function () {
                self.isWaitingForResponse = true;
                self.error = EMPTY;
                self.success = null;
                self.missingDestination = false;
                self.invalidStatus = false;
                var allImagesOrigin = self.originalActiveImages.concat(self.originalSubmittedImage);
                var allImages = self.activeOnlineImages.concat(self.submittedImages);
                var imageChanges = self.getChangedImageInfoList(allImages, allImagesOrigin);

                if (imageChanges.length !== 0) {
                    if (self.missingDestination) {
                        self.error = MISSING_DESTINATION_DOMAIN;
                        self.isWaitingForResponse = false;
                        if (self.isChangeShowRejected) {
                            self.isChangeShowRejected = false;
                            self.setDisplayNameForShowOrHideAllButton();
                            self.setDisplayNameForShowOrHideRejectedButton();
                        }
                    } else if (self.invalidStatus || self.isActiveOnlineAndPrimaryNotExists(allImages) || !self.isValidImageInfo(allImages)) {
                        self.error = self.invalidStatusString;
                        self.isWaitingForResponse = false;
                        if (self.isChangeShowRejected) {
                            self.isChangeShowRejected = false;
                            self.setDisplayNameForShowOrHideAllButton();
                            self.setDisplayNameForShowOrHideRejectedButton();
                        }
                    } else {
                        if (imageChanges.length > 0) {
                            self.waitingForDataUpdates = true;
                            productSellingUnitsApi.updateImageInfo({upcs: self.upcs}, imageChanges, self.saveImageInfoResult, self.handleError);
                        }
                    }
                } else {
                    self.error = MESSAGE_NO_DATA_CHANGE;
                    self.isWaitingForResponse = false;
                }
            };

            /**
             * This method catches the success result for save image info and copy image info.
             * @param results the success result.
             */
            self.saveImageInfoResult = function (results) {
                self.resetCountImages();
                if (results.message !== null) {
                    self.success = results.message;
                }
                self.getActiveOnlineImages();
            };

            /**
             * Check the primary priority on the list of active online images and submitted images that is not exists
             * then return true or if it is existing then return false.
             * @returns {boolean} true if it is not exists, otherwise return false.
             */
            self.isActiveOnlineAndPrimaryNotExists = function (images) {
                var isExisting = false;
                var flagCheck = false;
                angular.forEach(images, function (image) {
                    if (image.activeOnline && self.trimData(image.imagePriorityCode) === PRIMARY_PRIORITY) {
                        isExisting = true;
                    }
                });
                if (!isExisting) {
                    angular.forEach(images, function (image) {
                        if (image.activeOnline) {
                            if (self.trimData(image.imagePriorityCode) === PRIMARY_ALTERNATE) {
                                self.invalidStatusString = IMG_ACTIVE_ONLINE_IN_ALTERNATE;
                                flagCheck = true;
                            } else if (self.trimData(image.imageCategoryCode) === CATEGORY_SWATCHES) {
                                self.invalidStatusString = IMG_ACTIVE_ONLINE_IN_CATEGORY_SWATCH;
                                flagCheck = true;
                            }
                        }
                    });
                }
                return flagCheck;
            };

            /**
             * Validation images before save.
             * @param images
             * @returns {boolean}
             */
            self.isValidImageInfo = function (images) {
                var flagCheck = true;
                var originalImages = self.originalActiveImages.concat(self.originalSubmittedImage);
                angular.forEach(images, function (image) {
                    if (image.imageStatusCode === STATUS_FOR_REVIEW) {
                        angular.forEach(originalImages, function (originalImage) {
                            if (originalImage.key.sequenceNumber === image.key.sequenceNumber
                                && self.trimData(originalImage.imageStatusCode) !== STATUS_FOR_REVIEW) {
                                self.invalidStatusString = IMG_STATUS_CHANGE_TO_REVIEW;
                                flagCheck = false;
                            }
                        });
                    } else if (self.trimData(image.imageStatusCode) === STATUS_REJECTED && self.trimData(image.imagePriorityCode) === PRIMARY_PRIORITY) {
                        self.invalidStatusString = IMG_PRIMARY_REJECTED;
                        flagCheck = false;
                    }
                    if (image.activeOnline) {
                        if (self.trimData(image.imageStatusCode) === STATUS_FOR_REVIEW) {
                            self.invalidStatusString = IMG_PRIMARY_FOR_REVIEW_ACTONLINE;
                            flagCheck = false;
                        }
                        if (self.trimData(image.imageStatusCode) === STATUS_REJECTED) {
                            self.invalidStatusString = IMG_STATUS_REJECTED;
                            flagCheck = false;
                        }
                        if (self.trimData(image.imagePriorityCode) === PRIMARY_NOT_APPLICABLE
                            && self.trimData(image.imageCategoryCode) !== CATEGORY_SWATCHES) {
                            self.invalidStatusString = IMG_ACTIVE_ONLINE_IS_NONE_CATEGORY_OR_PRIMARY_ALTERNATE;
                            flagCheck = false;
                        }
                    }
                });
                return flagCheck;
            };


            /**
             * Get the list of changed image info current image info list.
             * @param currentImageInfoList the list of current image info.
             * @param originalImageInfoList the of original image info.
             * @returns {Array} the list of changed image info.
             */
            self.getChangedImageInfoList = function (currentImageInfoList, originalImageInfoList) {
                self.destinationUpdates = [];
                var changedImageInfoList = [];

                var isChanged = false;
                for (var index = 0; index < currentImageInfoList.length; index++) {
                    var currentImage = {
                        key: {
                            id: currentImageInfoList[index].key.id,
                            sequenceNumber: currentImageInfoList[index].key.sequenceNumber,
                        },
                        productScanImageBannerList: []
                    };
                    isChanged = false;
                    for (var originalIndex = 0; originalIndex < originalImageInfoList.length; originalIndex++) {
                        if (currentImageInfoList[index].key.id === originalImageInfoList[originalIndex].key.id
                            && currentImageInfoList[index].key.sequenceNumber === originalImageInfoList[originalIndex].key.sequenceNumber) {
                            if (!angular.equals(self.trimData(currentImageInfoList[index].altTag), self.trimData(originalImageInfoList[originalIndex].altTag))) {
                                currentImage.altTag = currentImageInfoList[index].altTag;
                                isChanged = true;
                            }
                            if (!angular.equals(self.trimData(currentImageInfoList[index].imageCategoryCode), self.trimData(originalImageInfoList[originalIndex].imageCategoryCode))) {
                                currentImage.imageCategoryCode = currentImageInfoList[index].imageCategoryCode;
                                isChanged = true;
                            }
                            if (!angular.equals(self.trimData(currentImageInfoList[index].imagePriorityCode), self.trimData(originalImageInfoList[originalIndex].imagePriorityCode))) {
                                currentImage.imagePriorityCode = currentImageInfoList[index].imagePriorityCode;
                                isChanged = true;
                            }

                            if (!angular.equals(currentImageInfoList[index].destinations, originalImageInfoList[originalIndex].destinations)) {
                                currentImage.destinationChanges = self.getChangedDestination(currentImageInfoList[index], originalImageInfoList[originalIndex])
                                isChanged = true;
                            }

                            if (!angular.equals(currentImageInfoList[index].activeOnline, originalImageInfoList[originalIndex].activeOnline)) {
                                currentImage.activeOnline = currentImageInfoList[index].activeOnline;
                                isChanged = true;
                            }
                            if (!angular.equals(self.trimData(currentImageInfoList[index].imageStatusCode), self.trimData(originalImageInfoList[originalIndex].imageStatusCode))) {
                                currentImage.imageStatusCode = currentImageInfoList[index].imageStatusCode;
                                isChanged = true;
                            }

                            if (isChanged) {
                                changedImageInfoList.push(currentImage);
                                if (!angular.equals(currentImageInfoList[index].imageStatusCode.trim(), STATUS_REJECTED) && currentImageInfoList[index].destinations.length === 0) {
                                    self.missingDestination = true;
                                }
                            }
                            break;
                        }
                    }

                }
                return changedImageInfoList;
            };

            /**
             * Trim data string, if data string is null or undefined then return empty.
             * @param dataString the data string needs to trim.
             * @returns {string} the data string was trimmed.
             */
            self.trimData = function (dataString) {
                return (dataString == null || dataString == undefined) ? '' : dataString.trim();
            }

            /**
             * Gets changed destination of image info.
             * @param image the image info
             * @param originalImageInfo the original image info.
             * @returns {{upc: *, sequenceNumber: (Document.sequenceNumber|*), activeSwitch: (string|boolean), destinationsAdded: Array, destinationsRemoved: Array}}
             */
            self.getChangedDestination = function (imageInfo, originalImageInfo) {
                var arrayIndex;
                var changedDestination = {
                    upc: imageInfo.key.id,
                    sequenceNumber: imageInfo.key.sequenceNumber,
                    activeSwitch: imageInfo.activeSwitch,
                    destinationsAdded: [],
                    destinationsRemoved: []
                };
                angular.forEach(imageInfo.destinations, function (destination) {
                    var originalDestinations = originalImageInfo.destinations;
                    var foundBanner = false;
                    if (originalDestinations.length > 0) {
                        for (arrayIndex = 0; arrayIndex < originalDestinations.length; arrayIndex++) {
                            if (angular.equals(destination.id, originalDestinations[arrayIndex].id)) {
                                foundBanner = true;
                                break;
                            }
                        }
                    }
                    if (!foundBanner) {
                        changedDestination.destinationsAdded.push(destination.id);
                    }
                });
                angular.forEach(originalImageInfo.destinations, function (destination) {
                    var newDestinations = imageInfo.destinations;
                    var foundBanner = false;
                    if (newDestinations.length > 0) {
                        for (arrayIndex = 0; arrayIndex < newDestinations.length; arrayIndex++) {
                            if (angular.equals(destination.id, newDestinations[arrayIndex].id)) {
                                foundBanner = true;
                                break;
                            }
                        }
                    }
                    if (!foundBanner) {
                        changedDestination.destinationsRemoved.push(destination.id)
                    }
                });
                self.destinationUpdates.push(changedDestination);
                return changedDestination;
            };

            /**
             * Updates the cropper to a new image
             * @param imageData
             */
            self.setImageToEdit = function (imageData) {
                self.imageToEditData = imageData;
                var modal = document.getElementById("editImageModalDialog");
                modal.style.width = EMPTY + 1030 + "px";
                self.cropper.replace(imageData.image);
            };

            /**
             * Show full image for view.
             * @param image
             */
            self.showFullImage = function (image) {
                self.imageBytes = image.image;
                self.imageFormat = self.trimData(image.imageFormat);

                if (self.imageBytes !== '') {
                    $('#imagePopup').modal({backdrop: 'static', keyboard: true});
                }

            };

            /**
             * Spin the editing image
             * @param degrees
             */
            self.spinCropper = function (degrees) {
                self.currentAngle += degrees;
                self.cropper.rotateTo(self.currentAngle);
            };

            /**
             * Set the movement mode to crop(create new crop box or move it) on the editing image
             */
            self.cropMode = function () {
                self.cropper.setDragMode("crop");
            };

            /**
             * Set the movement mode to move (move image or crop box) on the editing image
             */
            self.moveMode = function () {
                self.cropper.setDragMode("move");
            };

            /**
             * Zoom into the editing image
             */
            self.zoomIn = function () {
                self.cropper.zoom(0.1);
            };

            /**
             * Zoom out of editing image
             */
            self.zoomOut = function () {
                self.cropper.zoom(-0.1);
            };

            /**
             * Flip image on its X axis
             */
            self.flipImageX = function () {
                self.scaleX = self.scaleX * -1;
                self.cropper.scale(self.scaleX, self.scaleY);
            };

            /**
             * Flip image on its Y axis
             */
            self.flipImageY = function () {
                self.scaleY = self.scaleY * -1;
                self.cropper.scale(self.scaleX, self.scaleY);
            };

            /**
             * preview the proposed changes to the image
             */
            self.previewCroppedImage = function () {
                self.editedImage = self.cropper.getCroppedCanvas().toDataURL();
                var preview = document.getElementById("previewImage");
                var previewModal = document.getElementById("previewImageModalDialog");
                previewModal.style.width = "1000px";
                preview.src = self.editedImage;
            };

            /**
             *Save new cropped image and send it to the backend to update the database
             */
            self.saveImage = function () {
                $('.modal').modal('hide');
				self.uploadImageName = EMPTY + self.imageToEditData.key.id + "." + self.imageToEditData.imageType.imageFormat + EMPTY;
				var uploadImage = {
                    upc: self.imageToEditData.key.id,
                    imageCategoryCode: self.imageToEditData.imageCategoryCode,
                    imageSourceCode: self.imageToEditData.imageSource.id,
                    imageData: self.editedImage.split(',').pop(),
                    imageName: self.uploadImageName
                };
				productSellingUnitsApi.uploadImage(uploadImage, self.uploadImageResult, self.handleError);
                self.isWaitingForResponse = true;
            };
            /**
             * This method will check the category of an image and if it is set to 'Swatches' it will automatically
             * set the priority of the image to Not Applicable
             * @param image
             */
            self.checkCategory = function (image) {
                if (angular.equals(image.imageCategoryCode, "SWAT")) {
                    image.imagePriorityCode = 'NA'
                }
            };
            /**
             * This method will update the selectedImages list
             * @param image the image to be added or removed
             * @param selected whether to add(true) or remove (false) the image
             */
            self.updateSelectedImage = function (image, selected) {
                if (selected) {
                    self.selectedImages.push(angular.copy(image));
                } else {
                    var index = self.selectedImages.length;
                    while (index--) {
                        if (angular.equals(image.id, self.selectedImages[index].id)) {
                            if (angular.equals(image.sequenceNumber, self.selectedImages[index].sequenceNumber)) {
                                self.selectedImages.splice(index, 1);
                            }
                        }
                    }
                }
            };

            /**
             * This method will add or remove all of the images from the selected images list
             */
            self.selectAll = function () {
				self.selectedImages = [];
				angular.forEach(self.activeOnlineImages, function (image) {
                    image.selected = self.selectAllImages;
                    if (self.selectAllImages) {
                        self.updateSelectedImage(image, image.selected);
                    }
				});
				angular.forEach(self.submittedImages, function (image) {
                    image.selected = self.selectAllImages;
                    if (self.selectAllImages) {
                        self.updateSelectedImage(image, image.selected);
                    }
                });
            };
            /**
             * This method is called to initialize the copy to hierarchy modal
             */
            self.copyToHierarchyModalInit = function () {
                if (permissionsService.getPermissions('CP_IMG_01', 'EDIT')) {
                    var params = {
                        prodId: self.productMaster.prodId,
                    };
                    productSellingUnitsApi.copyToHierarchyPath(params,
                        function (results) {
                            self.hierarchyPath = results.data;
                            if (self.hierarchyPath.length > 0) {
                                self.hierarchyString = "Nominate image for the hierarchy " + self.hierarchyPath[self.hierarchyPath.length - 1].displayText;
                                for (var index = self.hierarchyPath.length - 2; index >= 0; index--) {
                                    self.hierarchyString = self.hierarchyString + "→" + self.hierarchyPath[index].displayText;
                                }
                                self.hierarchyString = self.hierarchyString + " at the level:";
                                self.isDisableHierarchy = false;
                            } else {
                                self.isDisableHierarchy = true;
                            }
                        },
                        self.handleError);
                }
            };

            /**
             * Check for disable or enable Copy to Hierarchy button.
             * @returns {boolean}
             */
            self.isDisableCopyToHierarchyButton = function () {
                return (!self.selectedImages.length > 0);
            };
            /**
             * This method will validate that all of the images to be copied to the customer hierarchy are valid
             * @returns {boolean} true it is valid or of invalid.
             */
            self.isValidHierarchy = function () {
                if (self.selectedImages.length === 0) {
                    return false;
                }
                var isValid = true;
                angular.forEach(self.selectedImages, function (hierarchyCandidate) {
                    if (!angular.equals(hierarchyCandidate.imageStatusCode, APPROVED_STATUS_CODE)) {
                        isValid = false;
                    }
                });
                return isValid;
            };
            /**
             * This method will copy an image's information to the customer hierarchy
             */
            self.copyToHierarchy = function () {
                $('.modal').modal('hide');
                var path;
                if (angular.equals(self.hierarchyLevels, "single")) {
                    path = [
                        self.hierarchyPath[0]
                    ];
                } else {
                    path = self.hierarchyPath;
				}
				angular.forEach(self.selectedImages, function (image) {
					image.imageData = null;
					image.image = null;
                });
                var copyToHierarchyRequest = {
                    imageInfo: self.selectedImages,
                    genericEntities: path,
                    upcs: self.upcs
                };
                self.isWaitingForResponse = true;
                productSellingUnitsApi.copyToHierarchy(copyToHierarchyRequest, self.saveImageInfoResult, self.handleError);
                self.selectedImages = [];
                angular.forEach(self.activeOnlineImages, function (images) {
                    images.selected = false;
				});
				angular.forEach(self.submittedImages, function (images) {
                    images.selected = false;
                });
            };
            /**
             * This method is used to determine what image to display
             * @param imageInfo
             * @returns {boolean} true if imageInfo is still waiting for image from api false if the api has returned image info
             */
            self.isImageUndefined = function (imageInfo) {
                return typeof imageInfo['image'] === 'undefined';
            };

            /**
             * handle when click on return to list button
             */
            self.returnToList = function () {
                $rootScope.$broadcast('returnToListEvent');
            };

            /**
             * Download current image.
             */
            self.downloadImage = function (imageBytes, imageFormat) {
                if (imageBytes !== '') {
                    downloadImageService.download(imageBytes, imageFormat);
                }
            };

            /**
             * This method is used to determine if there is a change to save
             * @returns {boolean}
             */
            self.detectChange = function () {
                var allOriginalImages = self.originalActiveImages.concat(self.originalSubmittedImage);
                var allCurrentImages = self.activeOnlineImages.concat(self.submittedImages);
                var imageChanges = self.getChangedImageInfoList(allCurrentImages, allOriginalImages);
                if (imageChanges.length > 0) {
                    $rootScope.contentChangedFlag = true;
                    return true;
                }
                $rootScope.contentChangedFlag = false;
                return false;
            };

            /**
             * This method will be called when user click on Show/Hide rejected images.
             * If there is any change on image info then a confirm popup will display.
             */
            self.onShowOrHideRejectedImage = function () {
                self.isAllButtonClicked = false;
                if (self.detectChange()) {
                    self.isShowingNavigationConfirmPopup = false;
                    $('#confirmationShowHideReject').modal({backdrop: 'static', keyboard: true});
                } else {
                    self.setDisplayNameForShowOrHideRejectedButton();
                    self.buildSubmittedImageTable(null);
                }
            };

            /**
             * This method will be called when user click on Show/Hide all images.
             * If there is any change on image info then a confirm popup will display.
             */
            self.onShowOrHideALlImage = function () {
                self.isAllButtonClicked = true;
                if (self.detectChange()) {
                    self.isShowingNavigationConfirmPopup = false;
                    $('#confirmationShowHideReject').modal({backdrop: 'static', keyboard: true});
                } else {
                    self.setDisplayNameForShowOrHideAllButton();
                    self.buildSubmittedImageTable(null);
                }
            };

            /**
             * This method will call when user click on Yes button of save confirm popup.
             * When switch between show rejected image and hide rejected image. If there is any change on it,
             * then a confirm popup ask save changed data with Yes/No buttons.
             */
            self.onYesConfirmedButtonClicked = function () {
                if(self.isShowingNavigationConfirmPopup){
                    // Case 1: On navigation confirm popup.
                    $('#confirmationShowHideReject').modal("hide");
                    self.isNavigateToNewPage = true;
                }else{
                    // Case 2: On Show or hide rejected confirm popup.
                    self.setDisplayNameForShowOrHideAllButton();
                    self.setDisplayNameForShowOrHideRejectedButton();
                    self.isChangeShowRejected = true;
                }
                self.saveImageInfo();
            };

             /**
             * This method with remove all messages.
             */
             self.resetAllMessage = function(){
                self.error = null;
                self.success = null;
                self.invalidStatusString = '';
            };


             /**
             * This method will call when user click on No button of save confirm popup.
             * When switch between show rejected image and hide rejected image. If there is any change on it,
             * then a confirm popup ask save changed data with Yes/No buttons.
             */
            self.onNoConfirmedButtonClicked = function () {
                self.resetAllMessage();
                self.resetCountImages();
                if(self.isShowingNavigationConfirmPopup){
                    // Case 1: On navigation confirm popup.
					$('#confirmationShowHideReject').modal("hide");
					self.isNavigateToNewPage = true;
                    self.getActiveOnlineImages();
                }else{
                    // Case 2: On Show or hide rejected confirm popup.
                    self.setDisplayNameForShowOrHideAllButton();
                    self.setDisplayNameForShowOrHideRejectedButton();
                    self.buildSubmittedImageTable(null);
                    self.getActiveOnlineImages();
                }
            };

            /**
             * Set display name for button show/hide rejected button.
             */
            self.setDisplayNameForShowOrHideRejectedButton = function () {
                self.isShowRejected = !self.isShowRejected;
                if (self.isShowRejected) {
                    self.showRejectedDisplay = HIDE_REJECTED;
                } else {
                    self.showRejectedDisplay = SHOW_REJECTED;
                }
            };

            /**
             * Set display name for button show/hide all button.
             */
            self.setDisplayNameForShowOrHideAllButton = function () {
                self.isShowAllImages = !self.isShowAllImages;
                if (self.isShowAllImages) {
                    self.showAllDisplay = HIDE_ALL;
                } else {
                    self.showAllDisplay = SHOW_ALL;
                }
            };

            /**
             * Handle event navigating on UI
             */
            $scope.selectPage = function (params, page) {
                if (self.submittedImageTable.page() == page) {
                    return false;
                }
                if (self.detectChange()) {
                    self.nextPage = page;
                    self.isShowingNavigationConfirmPopup = true;
                    $('#confirmationShowHideReject').modal({backdrop: 'static', keyboard: true});
                } else {
					self.success = null;
                	self.error = null;
                	self.submittedImageTable.page(page);
                }
            };
        }
    }
)();
