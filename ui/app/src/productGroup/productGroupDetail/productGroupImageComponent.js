
/*
 *   productGroupImageComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Product Group Image Component.
 *
 * @author vn87351
 * @since 2.15.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('productGroupImage', {
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productGroup/productGroupDetail/productGroupImage.html',
		bindings: {
			productGroupSelected: '<',
			productGroupIds: '<'
		},
		// The controller that handles our component logic
		controller: ProductGroupImageInfoController
	});

	ProductGroupImageInfoController.$inject = ['$rootScope', '$scope', '$filter', 'ngTableParams', 'imageInfoApi', 'productGroupApi', 'productGroupConstant', 'ProductGroupService', '$state', 'appConstants', 'customHierarchyService', 'DownloadImageService'];

	/**
	 * product group detail- image info component's controller definition.
	 * @param $rootScope
	 * @param $scope scope of the product group image component.
	 * @param $filter
	 * @param ngTableParams
	 * @param imageInfoApi
	 * @param productGroupApi
	 * @constructor
	 */
		function ProductGroupImageInfoController($rootScope, $scope, $filter, ngTableParams, imageInfoApi, productGroupApi, productGroupConstant, productGroupService , $state, appConstants, customHierarchyService, downloadImageService) {

		var self = this;

		/**
		 * Flag for waiting response from back end.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Start position of page that want to show on Product Group Image.
		 *
		 * @type {number}
		 */
		const FIRST_PAGE = 1;

		/**
		 * The number of records to show on the Product Group Image.
		 *
		 * @type {number}
		 */
		const PAGE_SIZE = 10;

		/**
		 * Format of data time using for set file name image download.
		 *
		 * @type {string}
		 */
		const DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

		/**
		 * PNG format image.
		 *
		 * @type {string}
		 */
		const PNG_FORMAT = "png";

		/**
		 * Category Code
		 * @type {string}
		 */
		const CATEGORY_SWATCHES = 'SWAT';

		/**
		 * Priority Code
		 * @type {string}
		 */
		const PRIORITY_NOT_APPLICABLE = 'NA';
		const PRIORITY_ALTERNATE = 'A';
		const PRIORITY_PRIMARY = 'P';

		/**
		 * Status Code
		 * @type {string}
		 */
		const STATUS_REJECTED = 'R';
		const STATUS_FOR_REVIEW = 'S';

		/**
		 * Contains message  returned if success
		 * @type {string}
		 */
		self.success = '';

		/**
		 * Contain message returned if error
		 * @type {string}
		 */
		self.error = '';

		/**
		 * Messages
		 * @type {string}
		 */
		self.IMAGE_NOT_FOUND = 'image-not-found';
		self.MESSAGE_NO_DATA_CHANGE = "There are no changes on this page to be saved. Please make any changes to update.";
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		self.HAS_CHANGE_METADATA = "There are changes in the grid. Please save changes before Upload image.";
		self.SELECT_IMG_CATEGORY = "Please select Image Category.";
		self.SELECT_IMG_SOURCE = "Please select Image Source";
		self.SELECT_FILE = "Please select file to upload.";
		self.INCORRECT_FILE_FORMAT = " is incorrect file format.";
		self.CONFIRM_BEFORE_SAVE = "Would you like to continue with the upload?";
		self.UPLOAD_SUCCESSFUL = " is uploaded successfully and associated with the Product Group ID ";
		self.MESSAGE_NO_DATA_CHANGE = "There are no changes on this page to be saved. Please make any changes to update";
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		self.IMG_ONE_PRIMARY_FOR_PRODUCT_GROUP = "Only one Primary Image can be Primary for a Product Group.";
		self.IMG_PRIMARY_REJECTED = "An image in 'Rejected' status cannot be set as 'Primary'.";
		self.IMG_PRIMARY_FOR_REVIEW_ACTONLINE = 'An image in "For review" status can not be made "Active Online".';
		self.IMG_ACTIVE_ONLINE_IN_ALTERNATE
			= 'Alternate Images cannot be "Active Online" without a Primary Image that is "Active Online".';
		self.IMG_ACTIVE_ONLINE_IN_CATEGORY_SWATCH
			= 'Swatch images cannot be "Active Online" without a Primary image that is "Active Online".';
		self.IMG_ACTIVE_ONLINE_IS_NONE_CATEGORY_OR_PRIMARY_ALTERNATE
			= 'An image cannot be "Active Online" without the "Image Category" or the "Primary/Alternate" defined.';
		self.IMG_STATUS_REJECTED
			= 'An image in "Rejected" status cannot be made "Active Online".';
		self.IMG_STATUS_CHANGE_TO_REVIEW
			= 'An image in "Approved" or  "Rejected" status cannot be moved to "For Review" Status.';

		/**
		 * Title for deleting popup
		 * @type {String}
		 */
		self.CONFIRM_DELETE_TITLE= 'Delete Product Group';
		/**
		 * Confirm message for deleting popup
		 * @type {String}
		 */
		self.CONFIRM_DELETE_MESSAGE = "Do you really want to delete the selected product group? ";
		/**
		 * Lists contain data.
		 * @type {Array}
		 */
		self.productGroupImages = [];
		self.productGroupImagesOrigin = [];
		self.productGroupImagesHandle = [];
		self.imageCategories = [];
		self.imagePriorities = [];
		self.imageStatus = [];
		self.imageSources = [];
		self.cacheImages = null;
		self.imageTable = null;
		self.selectedRowIndex = -1;
		self.listEmptyRecord = [];

		/**
		 * Flag for upload image.
		 *
		 * @type {String} the flag for upload image.
		 */
		self.confirmationType = '';
		self.confirmationContent = '';
		self.confirmationTitle = '';
		self.UPLOAD_TITLE = 'Upload';
		self.IMAGE_UPLOAD_TITLE = 'Image Upload';

		/**
		 * List of valid file types
		 * @type {[string,string,string,string,string]}
		 */
		self.validFileTypes = ["jpg", "jpeg", "tif", "tiff", "png"];

		/**
		 * The file to upload.
		 *
		 * @type {{}} the file to upload.
		 */
		self.fileUpload = {};

		/**
		 * Define button will show in confirmation modal.
		 */
		self.ONLY_CLOSE_BUTTON = 1;
		self.OK_AND_CANCEL_BUTTONS = 2;

		/**
		 *Style for 2 modal.
		 * @type {Object}
		 */
		self.styleDisplay2Modal = { 'z-index': 1050 };

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
		self.disableReturnToList = true;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			if(!customHierarchyService.getDisableReturnToList()){
				customHierarchyService.setDisableReturnToList(true);
				productGroupService.setNavFromProdGrpTypePageFlag(false);
				productGroupService.setCustomerHierarchyId(null);
			}
			if(self.productGroupIds.length > 1 || productGroupService.getNavFromProdGrpTypePageFlag()
				|| productGroupService.getNavigateFromCustomerHierPage() || productGroupService.isBrowserAll()){
				self.disableReturnToList = false;
			}
			self.productGroupId = angular.copy(self.productGroupSelected);
			self.cacheImages = new Map();
			self.isWaiting = true;
			self.findAllImageCategories();
			self.findAllImagePriorities();
			self.findAllImageStatus();
			self.findAllImageSource();
			self.addEventForInputFile();
			self.findProductGroupById(self.productGroupId);
			self.findAllProductGroupImage(self.productGroupId);
		};

		/**
		 * Get list of Image Sources.
		 */
		self.findAllImageSource = function () {
			imageInfoApi.getImageSoures(function (response) {
				self.imageSources = response;
			}, function (error) {
				self.fetchError(error);
			});
		};

		/**
		 * Find product group by product group id.
		 */
		self.findProductGroupById = function (id) {
			productGroupApi.getProductGroupById(
				{ "productGroupId":  id},
				function (results) {
					self.productGroup = results;
				},
				function (error) {
					self.fetchError(error);
				}
			);
		};

		/**
		 * Call api to get all image of product group.
		 */
		self.findAllProductGroupImage = function(id){
			self.isWaiting = true;
			productGroupApi.findProductGroupImages(
				{ "productGroupId": id },
				function (results) {
					self.productGroupImages = results;
					self.productGroupImagesOrigin = angular.copy(self.productGroupImages);
					self.findAllImageDatas();
					self.addEmptyDataRow(results.length);
					self.buildDataTable();
					self.isWaiting = false;
				},
				function (error) {
					self.fetchError(error);
				}
			);
		};

		/**
		 * Add empty row to view.
		 * @param lengthOfData total image returned by api.
		 */
		self.addEmptyDataRow = function(lengthOfData){
			if ((lengthOfData % PAGE_SIZE) !== 0){
				var countEmptyRow = PAGE_SIZE - (lengthOfData % PAGE_SIZE);
				var imageCategoryName = '';
				if (lengthOfData > 0){
					imageCategoryName = self.productGroupImages[lengthOfData-1].imageCategoryName;
				}
				for (var i=0; i < countEmptyRow; i++){
					var emptyRow = {
						'id': i,
						'isEmptyRow': true,
						'imageCategoryName': imageCategoryName
					};
					self.productGroupImages.push(emptyRow);
				}
			}
		};

		/**
		 * Build ng table for show image on view.
		 */
		self.buildDataTable = function () {
			self.imageTable = new ngTableParams({
				page: FIRST_PAGE,
				count: PAGE_SIZE
			}, {
				counts: [],
				groupBy: 'imageCategoryName',
				data: self.productGroupImages
			});
		};

		/**
		 * get data of image by uri text.
		 *
		 * @param uriText of image.
		 * @returns {*} data of image. If image not found return empty string.
		 */
		self.getImageByUriText = function (uriText) {
			if (self.cacheImages.has(uriText)) {
				return self.cacheImages.get(uriText);
			} else {
				return '';
			}
		};

		/**
		 * Show full product group image
		 *
		 * @param image object.
		 */
		self.showFullImage = function (image) {
			self.imageFormat = image.imageFormatCode;
			if (self.cacheImages.has(image.uriText)) {
				self.imageBytes = self.cacheImages.get(image.uriText);
			}
			$('#imageModal').modal({ backdrop: 'static', keyboard: true });
		};


		/**
		 * call api to get image.
		 */
		self.findAllImageDatas = function () {
			angular.forEach(self.productGroupImages, function (image) {
				if (!self.cacheImages.has(image.uriText)) {
					imageInfoApi.getImages(image.uriText,
						function (results) {
							if (results.data !== null && results.data !== '') {
								self.cacheImages.set(image.uriText, productGroupConstant.IMAGE_ENCODE + results.data);
							} else {
								self.cacheImages.set(image.uriText, self.IMAGE_NOT_FOUND);
							}
						}
					);
				}
			});
		};

		/**
		 * Show message if occur error.
		 * @param error contain message returned from api.
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message !== null && error.data.message !== "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Call Api to get all image categories.
		 */
		self.findAllImageCategories = function () {
			imageInfoApi.getImageCategories().$promise.then(function (response) {
				self.imageCategories = response;
			});
		};

		/**
		 * Call Api to get all image primaries.
		 */
		self.findAllImagePriorities = function () {
			imageInfoApi.getImagePriorities().$promise.then(function (response) {
				self.imagePriorities = response;
			});
		};

		/**
		 * Call Api to get all image statuses.
		 */
		self.findAllImageStatus = function () {
			imageInfoApi.getImageStatuses().$promise.then(function (response) {
				self.imageStatus = response;
			});
		};

		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if (self.imageBytes !== ' ') {
				downloadImageService.download(self.imageBytes, self.imageFormat==''?PNG_FORMAT:self.imageFormat.trim());
			}
		};

		/**
		 * Set selected index of selected row.
		 *
		 * @param image image object
		 */
		self.onSelectRow = function (image) {
			if (!image.isEmptyRow){
				if (self.selectedRowIndex === image.key.sequenceNumber) {
					self.selectedRowIndex = -1;
				} else {
					self.selectedRowIndex = image.key.sequenceNumber;
				}
			}
		};

		/**
		 * Handle change image category.
		 */
		self.onChangeCategory = function (image) {
			if (image.imageCategoryCode === CATEGORY_SWATCHES) {
				image.imagePriorityCode = PRIORITY_NOT_APPLICABLE;
				angular.forEach(self.imagePriorities, function (imagePriority) {
					if (imagePriority.id === PRIORITY_NOT_APPLICABLE) {
						image.imagePriority = angular.copy(imagePriority);
					}
				});
			}
		};

		/**
		 * watcher for reload form event.
		 */
		$scope.$on(productGroupConstant.CHANGE_PRODUCT_GROUP, function(event, args) {
			self.productGroupId = args;
			self.loadProductGroupImage();
		});

		/**
		 * load data for product group info
		 */
		self.loadProductGroupImage = function(){
			self.isWaiting = true;
			self.findProductGroupById(self.productGroupId);
			self.findAllProductGroupImage(self.productGroupId);
		};

		/**
		 * Go back to search form.
		 */
		self.returnToSearchPage = function(){
			productGroupService.setProductGroupId(null);
			customHierarchyService.setNavigatedFromOtherPage(true);
			customHierarchyService.setNavigatedForFirstSearch(true);
			customHierarchyService.setNotFacingHierarchyLink(true);
			if(productGroupService.getNavigateFromCustomerHierPage() && productGroupService.getCustomerHierarchyId() !== null){
				self.goToCustomerHierarchy();
			}else if(productGroupService.getNavigateFromCustomerHierPage()){
				customHierarchyService.setReturnToListFlag(true);
				productGroupService.getNavigateFromCustomerHierPage(false);
				customHierarchyService.setSelectedTab("PRODUCT_GROUP");
				customHierarchyService.setDisableReturnToList(true);
				$state.go(appConstants.CUSTOM_HIERARCHY_ADMIN);
			}else if(productGroupService.getNavFromProdGrpTypePageFlag()){
				customHierarchyService.setReturnToListFlag(true);
				customHierarchyService.setDisableReturnToList(true);
				$state.go(appConstants.CODE_TABLE);
			} else{
				$rootScope.$broadcast('changeMode', 'search-mode');
			}

		};
		/**
		 * call go to customer hierarchy
		 */
		self.goToCustomerHierarchy = function(){
			$rootScope.$broadcast('goToCustomerHierarchy');
		}

		/**
		 * Call api to update metadata of images.
		 *
		 * @param isReturnToList
		 */
		self.saveProductGroupImages = function (isReturnToList) {
			self.error = '';
			self.success = '';
			self.productGroupImages.splice(self.productGroupImagesOrigin.length,
				self.productGroupImages.length - self.productGroupImagesOrigin.length);

			if (self.compareListImages()) {
				if (self.validationDataUpdate(self.productGroupImages, self.productGroupImagesOrigin)) {
					self.isWaiting = true;
					productGroupApi.updateProductGroupImages(self.productGroupImagesHandle,
						function (results) {
							self.imageTable = null;
							self.selectedRowIndex = -1;
							self.success = results.message;
							self.productGroupImages = results.data;
							self.productGroupImagesOrigin = angular.copy(self.productGroupImages);
							self.findAllImageDatas();
							self.addEmptyDataRow(self.productGroupImages.length);
							self.buildDataTable();
							self.isWaiting = false;
						},
						function (error) {
							self.fetchError(error);
						})
				}
				else {
				}
			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * compare list Images and Images Origin
		 * @returns {boolean} true if there is at least one changed
		 */
		self.compareListImages = function () {
			var hasChanged = false;
			angular.forEach(self.productGroupImages, function (productGroupImage) {
				if (!productGroupImage.isEmptyRow ){
					for (var i = 0, length = self.productGroupImagesOrigin.length; i < length; i++) {
						if (productGroupImage.key.sequenceNumber === self.productGroupImagesOrigin[i].key.sequenceNumber
							&& self.hasChangeImage(productGroupImage, self.productGroupImagesOrigin[i])) {
							hasChanged = true;
							self.productGroupImagesHandle.push(productGroupImage);
							break;
						}
					}
				}
			});
			$rootScope.contentChangedFlag = hasChanged;
			return hasChanged;
		};

		/**
		 * Check has changed attributes of product group image.
		 * @param productGroupImage object after change.
		 * @param productGroupImageOrigin object before changed.
		 * @returns {boolean} true if productGroupImage has changed attributes. Otherwise return false.
		 */
		self.hasChangeImage = function (productGroupImage, productGroupImageOrigin) {
			return (productGroupImage.imageAltText !== productGroupImageOrigin.imageAltText
				|| productGroupImage.imageCategoryCode !== productGroupImageOrigin.imageCategoryCode
				|| productGroupImage.activeOnline !== productGroupImageOrigin.activeOnline
				|| productGroupImage.imagePriorityCode !== productGroupImageOrigin.imagePriorityCode
				|| productGroupImage.imageStatusCode !== productGroupImageOrigin.imageStatusCode)
		};

		/**
		 * Validating Data of productGroup image before call update.
		 *
		 * @param productGroupImages list of product group image after edit.
		 * @param productGroupImagesOrigin list of product group image before edit.
		 * @returns {boolean} true if has no business logic error. Otherwise return false.
		 */
		self.validationDataUpdate = function (productGroupImages, productGroupImagesOrigin) {
			self.error = '';
			var flagCheck = true;
			/**
			 * Check only one Primary Image can be Primary for a productGroup.
			 */
			if (self.countPrimary(productGroupImages) > 1) {
				self.error = self.IMG_ONE_PRIMARY_FOR_PRODUCT_GROUP;
				return false;
			}
			if (self.isNotExistActiveAndPrimary(productGroupImages)) {
				angular.forEach(productGroupImages, function (productGroupImage) {
					if (productGroupImage.activeOnline) {
						/**
						 * Check Alternate Images cannot be "Active Online"
						 * without a Primary Image that is "Active Online".
						 */
						if (productGroupImage.imagePriorityCode === PRIORITY_ALTERNATE) {
							self.error = self.error || self.IMG_ACTIVE_ONLINE_IN_ALTERNATE;
							flagCheck = false;
						}
						/**
						 * Check Swatch images cannot be "Active Online"
						 * without a Primary image that is "Active Online".
						 */
						if (productGroupImage.imageCategoryCode === CATEGORY_SWATCHES) {
							self.error = self.error || self.IMG_ACTIVE_ONLINE_IN_CATEGORY_SWATCH;
							flagCheck = false;
						}
					}
				});
				if (!flagCheck) {
					return false;
				}
			}
			angular.forEach(productGroupImages, function (productGroupImage) {
				/**
				 * Check an image in 'Rejected' status cannot be set as 'Primary'.
				 */
				if (productGroupImage.imageStatusCode === STATUS_REJECTED
					&& productGroupImage.imagePriorityCode === PRIORITY_PRIMARY) {
					self.error = self.error || self.IMG_PRIMARY_REJECTED;
					flagCheck = false;
				}
				if (productGroupImage.activeOnline) {
					/**
					 * Check an image in "For review" status can not be made "Active Online".
					 */
					if (productGroupImage.imageStatusCode === STATUS_FOR_REVIEW) {
						self.error = self.error || self.IMG_PRIMARY_FOR_REVIEW_ACTONLINE;
						flagCheck = false;
					}
					/**
					 * Check an image in "Rejected" status cannot be made "Active Online".
					 */
					if (productGroupImage.imageStatusCode === STATUS_REJECTED) {
						self.error = self.error || self.IMG_STATUS_REJECTED;
						flagCheck = false;
					}
					/**
					 * Check an image cannot be "Active Online"
					 * without the "Image Category" or the "Primary/Alternate" defined.'
					 */
					if (productGroupImage.imagePriorityCode === PRIORITY_NOT_APPLICABLE
						&& productGroupImage.imageCategoryCode !== CATEGORY_SWATCHES) {
						self.error = self.error || self.IMG_ACTIVE_ONLINE_IS_NONE_CATEGORY_OR_PRIMARY_ALTERNATE;
						flagCheck = false;
					}
				}
				/**
				 * Check an image in "Approved" or  "Rejected" status cannot be moved to "For Review" Status.
				 */
				if (productGroupImage.imageStatusCode === self.STATUS_FOR_REVIEW) {
					angular.forEach(productGroupImagesOrigin, function (productGroupImageOrigin) {
						if (productGroupImageOrigin.key.sequenceNumber === productGroupImage.key.sequenceNumber
							&& productGroupImageOrigin.imageStatusCode !== STATUS_FOR_REVIEW) {
							self.error = self.error || self.IMG_STATUS_CHANGE_TO_REVIEW;
							flagCheck = false;
						}
					});
				}
			});
			return flagCheck;
		};

		/**
		 * return true if not exist image has Active and Primary
		 * @param productGroupImages
		 * @returns {boolean}
		 */
		self.isNotExistActiveAndPrimary = function (productGroupImages) {
			var isNotExistActiveAndPrimary = true;
			angular.forEach(productGroupImages, function (productGroupImage) {
				if (productGroupImage.activeOnline && productGroupImage.imagePriorityCode === PRIORITY_PRIMARY) {
					isNotExistActiveAndPrimary = false;
				}
			});
			return isNotExistActiveAndPrimary;
		};

		/**
		 * Count number of productGroup option image has Primary
		 * @param productGroupImages
		 * @returns {number}
		 */
		self.countPrimary = function (productGroupImages) {
			var count = 0;
			angular.forEach(productGroupImages, function (productGroupImage) {
				if (productGroupImage.imagePriorityCode === PRIORITY_PRIMARY) {
					count++;
				}
			});
			return count;
		};

		/**
		 * Add event for input type file.
		 */
		self.addEventForInputFile = function () {
			var uploadImageData = document.getElementById("button-browse-file");
			uploadImageData.addEventListener('change', function (e) {
				var imageData = uploadImageData.files[0];
				if (imageData !== undefined && imageData.type !== undefined) {
					var fileType = imageData.type.split('/').pop();
					if (!self.validFileTypes.includes(fileType.toLowerCase())) {
						self.showPopupConfirmation(self.ONLY_CLOSE_BUTTON,
							self.UPLOAD_TITLE, imageData.name + self.INCORRECT_FILE_FORMAT);
						$("#button-browse-file").val("");
					}
				}
			});
		};

		/**
		 * Show popup for upload product group image.
		 */
		self.showUploadImagePopup = function () {
			if (self.compareListImages()) {
				self.showPopupConfirmation(self.ONLY_CLOSE_BUTTON,
					self.IMAGE_UPLOAD_TITLE, self.HAS_CHANGE_METADATA);
			} else {
				$('#upload-file-modal').modal({ backdrop: 'static', keyboard: true });
				self.resetDataImageUpload();
			}
		};

		/**
		 * Show confirmation popup.
		 *
		 * @param confirmationType type of confirmation to show button of popup.
		 * @param confirmationTitle title of confirmation.
		 * @param confirmationContent content of confirmation.
		 */
		self.showPopupConfirmation = function (confirmationType, confirmationTitle, confirmationContent) {
			self.styleDisplay2Modal = { 'z-index': 1040 };
			self.confirmationType = confirmationType;
			self.confirmationTitle = confirmationTitle;
			self.confirmationContent = confirmationContent;
			$('#confirm-upload-image-modal').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Reset data upload productGroup image.
		 */
		self.resetDataImageUpload = function () {
			self.styleDisplay2Modal = { 'z-index': 1050 };
			self.imageCategorySelected = {
			};
			self.imageSourceSelected = {
			};
			self.fileUpload = {};
			document.getElementById("button-browse-file").value = "";
		};

		/**
		 * Handle click button browse.
		 */
		self.browseClick = function () {
			$("#button-browse-file").click();
		};
		/**
		 * Validate and upload productGroup image.
		 */
		self.prepareUploadImage = function () {
			if (self.isNullOrEmpty(self.imageCategorySelected.id)) {
				self.showPopupConfirmation(self.ONLY_CLOSE_BUTTON, self.UPLOAD_TITLE, self.SELECT_IMG_CATEGORY);
				self.imageCategorySelected.error = true;
			} else if (self.isNullOrEmpty(self.imageSourceSelected.id)) {
				self.showPopupConfirmation(self.ONLY_CLOSE_BUTTON, self.UPLOAD_TITLE, self.SELECT_IMG_SOURCE);
				self.imageSourceSelected.error = true;
			} else if (self.isNullOrEmpty(self.fileUpload) || self.isNullOrEmpty(self.fileUpload.name)) {
				self.showPopupConfirmation(self.ONLY_CLOSE_BUTTON, self.UPLOAD_TITLE, self.SELECT_FILE);
				if (self.fileUpload === undefined) {
					self.fileUpload = {};
				}
				self.fileUpload.error = true;
			} else {
				self.showPopupConfirmation(self.OK_AND_CANCEL_BUTTONS, self.UPLOAD_TITLE, self.CONFIRM_BEFORE_SAVE);
				self.fileUpload.error = true;
			}
		};

		/**
		 * Prepare data and call api to upload Image.
		 */
		self.doUploadImage = function () {
			var formData = new FormData();
			formData.append('fileUpload', self.fileUpload);
			formData.append('productGroupCode', self.productGroupId);
			formData.append('imgCategoryCode', self.imageCategorySelected.id);
			formData.append('imgSourceCode', self.imageSourceSelected.id);
			$('#confirm-upload-image-modal').modal("hide");
			$('#upload-file-modal').modal("hide");
			self.isWaiting = true;
			self.error = '';
			self.success = '';

			productGroupApi.uploadImage(formData,
				function (result) {
					self.isWaiting = false;
					self.productGroupImages = result.data;
					self.productGroupImagesOrigin = angular.copy(self.productGroupImages);
					self.success = 'Image "' + self.fileUpload.name + self.UPLOAD_SUCCESSFUL + self.productGroupId + '"';
					self.findAllImageDatas();
					self.addEmptyDataRow(self.productGroupImages.length);
					self.buildDataTable();
				}, function (error) {
					self.fetchError(error);
				}
			);
		};
		/**
		 * Show modal upload productGroup image.
		 */
		self.showConfirmModal = function (title, content, confirmationType) {
			self.styleDisplay2Modal = { 'z-index': 1040 };
			self.confirmModalTitle = title;
			self.confirmModalContent = content;
			self.confirmationType = confirmationType;
			$('#confirm-upload-image-modal').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Reset image metadata has changed to origin.
		 */
		self.reset = function () {
			self.selectedRowIndex = -1;
			self.error = '';
			self.success = '';
			self.productGroupImages = angular.copy(self.productGroupImagesOrigin);
			self.addEmptyDataRow(self.productGroupImages.length);
			self.buildDataTable();
		};

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === undefined || object === null || !object || object === "";
		};

		/**
		 * Handle delete product group.
		 */
		self.handleDeleteProductGroup = function () {
			self.error = '';
			self.success = '';
			$('#confirmDeletingModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Confirmed to delete/update change data
		 */
		self.onClickDelete = function () {
			$('#confirmDeletingModal').modal("hide");
			self.deleteProductGroup();
		};

		/**
		 * No confirm to delete/update change data
		 */
		self.onClickNoDelete = function () {
			$('#confirmDeletingModal').modal("hide");
		};

		/**
		 * Call API to delete product group.
		 */
		self.deleteProductGroup = function () {
			self.isWaiting = true;
			var customerProductGroup = {};
			customerProductGroup.custProductGroupId = self.productGroupSelected;
			productGroupApi.deleteProductGroup(
				customerProductGroup,
				function () {
					self.isWaiting = false;
					self.goToSearchFormAfterDeleting();
				},self.fetchError
			);
		};

		/**
		 * Go back search form after delete product group successfully.
		 */
		self.goToSearchFormAfterDeleting = function(){
			$rootScope.$broadcast('changeModeAfterDeleting','search-mode');
		}
	}
})();
