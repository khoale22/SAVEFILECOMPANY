/*
 *   choiceOptionDetailsComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Code Table -> choice option page component.
 *
 * @author vn70529
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('choiceOptionDetails', {
		bindings: {
			itemSending: '='
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/codeTable/choice/choiceOptionDetails.html',
		// The controller that handles our component logic
		controller: ChoiceOptionDetailsController
	});

	ChoiceOptionDetailsController.$inject = ['$rootScope', '$scope', '$filter', 'ChoiceDetailApi', 'ngTableParams','imageInfoApi', 'DownloadImageService'];

	/**
	 * Code Table factory component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @constructor
	 */
	function ChoiceOptionDetailsController($rootScope, $scope, $filter, choiceDetailApi, ngTableParams,imageInfoApi, downloadImageService) {
		/** All CRUD operation controls of choice option page goes here */
		var self = this;

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

		self.IMG_ONE_PRIMARY_FOR_CHOICE = "Only one Primary Image can be Primary for a Choice.";

		self.IMG_PRIMARY_REJECTED = "An image in 'Rejected' status cannot be set as 'Primary'";

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

		self.MESSAGE_NO_DATA_CHANGE = "There are no changes on this page to be saved. Please make any changes to update";

		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";

		/**
		 * Category Code
		 * @type {string}
		 */
		self.CATEGORY_BEAUTY_SHOT = 'BSHOT';
		self.CATEGORY_PRODUCT_IMAGE = 'PROD';
		self.CATEGORY_SWATCHES = 'SWAT';

		/**
		 * Primary Code
		 * @type {string}
		 */
		self.PRIMARY_ALTERNATE = 'A';
		self.PRIMARY_NOT_APPLICABLE = 'NA';
		self.PRIMARY_PRIMARY = 'P';

		/**
		 * Status Code
		 * @type {string}
		 */
		self.STATUS_APPROVED = 'A';
		self.STATUS_REJECTED = 'R';
		self.STATUS_FOR_REVIEW = 'S';

		/**
		 * Flag for waiting response from back end.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Total element in page
		 * @type {number}
		 */
		self.PAGE_SIZE = 10;
		/**
		 * Lists contain data
		 * @type {Array}
		 */
		self.imageCategories = [];

		self.imagePrimaries = [];

		self.imageStatus = [];

		self.choiceOptionImages = [];

		self.choiceImageActiveOnline = [];

		self.choiceImageNotActiveOnline = [];

		self.choiceImagesOrigin = [];

		self.choiceImagesHandle = [];

		self.tableActive = null;

		self.tableNotActive = null;

		self.cacheChoiceImages = null;

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
		 * Flags
		 *
		 */
		self.isShowReject = false;

		/**
		 * The Image Category selected.
		 *
		 * @type {{}} the Image Category selected.
		 */
		self.imageCategorySelected = {};

		/**
		 * The Image Source selected.
		 *
		 * @type {{}} the Image Source selected.
		 */
		self.imageSourceSelected = {};

		/**
		 * Holds the list of Image Source.
		 *
		 * @type {Array} the list of Image Source.
		 */
		self.lstImageSource = [];

		/**
		 * The file to upload.
		 *
		 * @type {{}} the file to upload.
		 */
		self.fileUpload = {};

		/**
		 * Title of confirm modal upload.
		 *
		 * @type {String} the title of confirm modal upload.
		 */
		self.confirmModalUploadTitle = '';

		/**
		 * Content of confirm modal upload.
		 *
		 * @type {String} the content of confirm modal upload.
		 */
		self.confirmModalUploadContent = '';

		/**
		 * Flag for upload image.
		 *
		 * @type {String} the flag for upload image.
		 */
		self.typeModal = '';

		/**
		 * List of valid file types
		 * @type {[string,string,string,string,string]}
		 */
		self.validFileTypes = ["jpg", "jpeg", "tif", "tiff", "png"];

		/**
		 *Style for 2 modal.
		 * @type {Object}
		 */
		self.styleDisplay2Modal = { 'z-index': 1050 };

		self.isReturnToTab = false;
		self.RETURN_TAB = 'returnTab';
		self.VALIDATE_CHOICE_OPTION = 'validateChoiceOption';
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.choiceOption = angular.copy(self.itemSending);
			self.cacheChoiceImages = new Map();
			self.isWaiting = true;
			self.findAllChoiceImages();
			self.findAllChoiceImageCategories();
			self.findAllChoiceImagePriorities();
			self.findAllChoiceImageStatus();
			self.findAllChoiceImageSource();
			self.addEventForInputFile();
		};

		/**
		 * Call Api to get all image categories
		 */
		self.findAllChoiceImageCategories = function () {
			imageInfoApi.getImageCategories().$promise.then(function (response) {
				self.imageCategories = response;
			});
		};

		/**
		 * Call Api to get all image primaries
		 */
		self.findAllChoiceImagePriorities = function () {
			imageInfoApi.getImagePriorities().$promise.then(function (response) {
				self.imagePrimaries = response;
			});
		};

		/**
		 * Call Api to get all image statuses
		 */
		self.findAllChoiceImageStatus = function () {
			imageInfoApi.getImageStatuses().$promise.then(function (response) {
				self.imageStatus = response;
			});
		};

		/**
		 * Call Api to get all images of choice option.
		 */
		self.findAllChoiceImages = function () {
			self.isWaiting = true;
			choiceDetailApi.findAllChoiceImages(
				{ "choiceOptionId": self.choiceOption.key.choiceOptionCode },
				function (results) {
					self.choiceOptionImages = results;
					self.choiceImagesOrigin = angular.copy(self.choiceOptionImages);
					self.classifyImage();
					self.initShowHideRejectValue();
					self.findAllImageDatas();
					self.buildTableActive();
					self.buildTableNotActive();
					self.isWaiting = false;
				},
				function (error) {
					self.fetchError(error);
				}
			);
		};

		/**
		 * Show message if cannot get data from api
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message !== null && error.data.message !== "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			};
			if(self.isReturnToTab){
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			}
		};

		/**
		 * get data of image by uri text
		 * @param uriText
		 * @returns {*}
		 */
		self.getImageByUriText = function (uriText) {
			if (self.cacheChoiceImages.has(uriText)) {
				return self.cacheChoiceImages.get(uriText);
			} else {
				return '';
			}
		};

		/**
		 * Show full choice image
		 * @param choiceImage
		 */
		self.showFullImage = function (choiceImage) {
			self.imageFormat = choiceImage.imageFormatCode;
			if (self.cacheChoiceImages.has(choiceImage.uriText)) {
				self.imageBytes = self.cacheChoiceImages.get(choiceImage.uriText);
			}
			$('#choiceDetailImageModal').modal({ backdrop: 'static', keyboard: true });

		};

		/**
		 * call api to get image
		 */
		self.findAllImageDatas = function () {
			angular.forEach(self.choiceOptionImages, function (choiceImage) {
				if (!self.cacheChoiceImages.has(choiceImage.uriText)) {
					imageInfoApi.getImages(choiceImage.uriText,
						function (results) {
							if (results.data != null && results.data != '') {
								self.cacheChoiceImages.set(choiceImage.uriText, "data:image/jpg;base64," + results.data);
							} else {
								self.cacheChoiceImages.set(choiceImage.uriText, self.IMAGE_NOT_FOUND);
							}
						}
					);
				}
			});
		};

		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if (self.imageBytes !== '') {
				downloadImageService.download(self.imageBytes, self.imageFormat);
			}
		};

		/**
		 * Build ng table for Active Online Images
		 */
		self.buildTableActive = function () {
			self.tableActive = new ngTableParams({
				page: 1,
				count: self.PAGE_SIZE
			}, {
				counts: [],
				total: 1,
				data: self.choiceImageActiveOnline
			});
		};

		/**
		 * Build ng table for Not Active Online Images
		 */
		self.buildTableNotActive = function () {
			self.tableNotActive = new ngTableParams({
				page: 1,
				count: self.PAGE_SIZE
			}, {
				counts: [],
				groupBy: 'imageCategoryName',
				data: self.choiceImageNotActiveOnline
			});
		};

		/**
		 * Handle event change active online of image
		 * @param choiceImage
		 */
		self.changeActiveOnline = function (choiceImage) {
			if (choiceImage.activeOnline) {
				choiceImage.activeOnline = false;
				self.choiceImageNotActiveOnline.push(choiceImage);
				self.choiceImageActiveOnline.splice(self.choiceImageActiveOnline.indexOf(choiceImage), 1);
			} else {
				choiceImage.activeOnline = true;
				self.choiceImageActiveOnline.push(choiceImage);
				self.choiceImageNotActiveOnline.splice(self.choiceImageNotActiveOnline.indexOf(choiceImage), 1);
			}

			if(self.choiceImageActiveOnline.length % self.PAGE_SIZE === 0){
				self.buildTableActive();
			} else {
				self.tableActive.reload();
			}

			if(self.choiceImageNotActiveOnline.length % self.PAGE_SIZE === 0){
				self.buildTableNotActive();
			} else {
				self.tableNotActive.reload();
			}

		};

		/**
		 * Classify choice image to two list: Active Online and Not Active Online
		 */
		self.classifyImage = function () {
			self.choiceImageActiveOnline.length = 0;
			self.choiceImageNotActiveOnline.length = 0;
			angular.forEach(self.choiceOptionImages, function (choiceImage) {
				if (choiceImage.activeOnline) {
					self.choiceImageActiveOnline.push(choiceImage);
				} else {
					self.choiceImageNotActiveOnline.push(choiceImage);
				}
			})
		};

		/**
		 * Handle Event of button show/hide Rejected Image
		 */
		self.showHideReject = function () {
			self.isShowReject = !self.isShowReject;
			self.tableNotActive.reload();
			self.tableActive.reload();
		};

		/**
		 * Handle Event of button reset
		 */
		self.reset = function () {
			self.error = '';
			self.success = '';
			self.choiceOptionImages.length = 0;
			self.choiceOptionImages = angular.copy(self.choiceImagesOrigin);
			self.classifyImage();
			self.initShowHideRejectValue();
			self.tableActive.reload();
			self.tableNotActive.reload();
		};

		/**
		 * Return to choice option page.
		 */
		self.returnToList = function () {
			if (self.compareListImages()) {
				self.showConfirmUploadImageModal("Confirmation", "Unsaved data will be lost.Do you want to save the changes before continuing?", "returnToList");
			} else {
				self.itemSending.isShowing = false;
				$rootScope.$broadcast('showMessageUpdateChoice',null);
			}

		};

		/**
		 * Check all choice images are Rejected
		 * @returns {boolean}
		 */
		self.hasAllImageAreRejected = function (listOfImages) {
			var count = 0;
			angular.forEach(listOfImages, function (choiceImage) {
				if (choiceImage.imageStatusCode===self.STATUS_REJECTED) {
					count++;
				}
			});
			return count === listOfImages.length;
		};

		/**
		 * return true if not exist image has Active and Primary
		 * @param choiceImages
		 * @returns {boolean}
		 */
		self.isNotExistActiveAndPrimary = function (choiceImages) {
			var isNotExistActiveAndPrimary = true;
			angular.forEach(choiceImages, function (choiceImage) {
				if (choiceImage.activeOnline && choiceImage.imagePriorityCode === self.PRIMARY_PRIMARY) {
					isNotExistActiveAndPrimary = false;
				}
			});
			return isNotExistActiveAndPrimary;
		};

		/**
		 * Count number of choice option image has Primary
		 * @param choiceImages
		 * @returns {number}
		 */
		self.countPrimary = function (choiceImages) {
			var count = 0;
			angular.forEach(choiceImages, function (choiceImage) {
				if (choiceImage.imagePriorityCode === self.PRIMARY_PRIMARY) {
					count++;
				}
			});
			return count;
		};

		/**
		 * Validating Data of choice image before call update
		 * @param choiceImages
		 * @param choiceImageOrigins
		 * @returns {boolean}
		 */
		self.validationDataUpdate = function (choiceImages, choiceImageOrigins) {
			self.error = '';
			var flagCheck = true;
			/**
			 * Check only one Primary Image can be Primary for a Choice.
			 */
			if (self.countPrimary(choiceImages) > 1) {
				self.error = self.IMG_ONE_PRIMARY_FOR_CHOICE;
				return false;
			}
			if (self.isNotExistActiveAndPrimary(choiceImages)) {
				angular.forEach(choiceImages, function (choiceImage) {
					if (choiceImage.activeOnline) {
						/**
						 * Check Alternate Images cannot be "Active Online"
						 * without a Primary Image that is "Active Online".
						 */
						if (choiceImage.imagePriorityCode === self.PRIMARY_ALTERNATE) {
							self.error = self.error || self.IMG_ACTIVE_ONLINE_IN_ALTERNATE;
							flagCheck = false;
						}
						/**
						 * Check Swatch images cannot be "Active Online"
						 * without a Primary image that is "Active Online".
						 */
						if (choiceImage.imageCategoryCode === self.CATEGORY_SWATCHES) {
							self.error = self.error || self.IMG_ACTIVE_ONLINE_IN_CATEGORY_SWATCH;
							flagCheck = false;
						}
					}
				});
				if (!flagCheck) {
					return false;
				}
			}
			angular.forEach(choiceImages, function (choiceImage) {
				/**
				 * Check an image in 'Rejected' status cannot be set as 'Primary'.
				 */
				if (choiceImage.imageStatusCode === self.STATUS_REJECTED
					&& choiceImage.imagePriorityCode === self.PRIMARY_PRIMARY) {
					self.error = self.error || self.IMG_PRIMARY_REJECTED;
					flagCheck = false;
				}
				if (choiceImage.activeOnline) {
					/**
					 * Check an image in "For review" status can not be made "Active Online".
					 */
					if (choiceImage.imageStatusCode === self.STATUS_FOR_REVIEW) {
						self.error = self.error || self.IMG_PRIMARY_FOR_REVIEW_ACTONLINE;
						flagCheck = false;
					}
					/**
					 * Check an image in "Rejected" status cannot be made "Active Online".
					 */
					if (choiceImage.imageStatusCode === self.STATUS_REJECTED) {
						self.error = self.error || self.IMG_STATUS_REJECTED;
						flagCheck = false;
					}
					/**
					 * Check an image cannot be "Active Online"
					 * without the "Image Category" or the "Primary/Alternate" defined.'
					 */
					if (choiceImage.imagePriorityCode === self.PRIMARY_NOT_APPLICABLE
						&& choiceImage.imageCategoryCode !== self.CATEGORY_SWATCHES) {
						self.error = self.error || self.IMG_ACTIVE_ONLINE_IS_NONE_CATEGORY_OR_PRIMARY_ALTERNATE;
						flagCheck = false;
					}
				}
				/**
				 * Check an image in "Approved" or  "Rejected" status cannot be moved to "For Review" Status.
				 */
				if (choiceImage.imageStatusCode === self.STATUS_FOR_REVIEW) {
					angular.forEach(choiceImageOrigins, function (choiceImageOrigin) {
						if (choiceImageOrigin.key.sequenceNumber === choiceImage.key.sequenceNumber
							&& choiceImageOrigin.imageStatusCode !== self.STATUS_FOR_REVIEW) {
							self.error = self.error || self.IMG_STATUS_CHANGE_TO_REVIEW;
							flagCheck = false;
						}
					});
				}
			});
			return flagCheck;
		};

		/**
		 * compare list Images and Images Origin
		 * @returns {boolean} true if there is at least one changed
		 */
		self.compareListImages = function () {
			var hasChanged = false;
			self.choiceOptionImages.length = 0;
			self.choiceImagesHandle.length = 0;
			self.choiceOptionImages = self.choiceImageActiveOnline.concat(self.choiceImageNotActiveOnline);
			angular.forEach(self.choiceOptionImages, function (choiceImage) {
				for (var i = 0, length = self.choiceImagesOrigin.length; i < length; i++) {
					if (choiceImage.key.sequenceNumber === self.choiceImagesOrigin[i].key.sequenceNumber
						&& self.hasChangeImage(choiceImage, self.choiceImagesOrigin[i])) {
						hasChanged = true;
						self.choiceImagesHandle.push(choiceImage);
						break;
					}
				}
			});
			return hasChanged;
		};

		/**
		 * Check has changed attributes of choice image
		 * @param choiceImage after changed
		 * @param choiceImageOrigin befor changed
		 * @returns {boolean} true if choiceImage has changed attributes. Otherwise return false
		 */
		self.hasChangeImage = function (choiceImage, choiceImageOrigin) {
			return (choiceImage.imageAltText !== choiceImageOrigin.imageAltText
				|| choiceImage.imageCategoryCode !== choiceImageOrigin.imageCategoryCode
				|| choiceImage.activeOnline !== choiceImageOrigin.activeOnline
				|| choiceImage.imagePriorityCode !== choiceImageOrigin.imagePriorityCode
				|| choiceImage.imageStatusCode !== choiceImageOrigin.imageStatusCode)
		};

		/**
		 * Call Api to update attributes of choice image
		 * @param isReturnToList
		 */
		self.saveChoiceImages = function (isReturnToList) {
			self.error = '';
			self.success = '';
			$('#changeTabConfirmation').modal('hide');
			if (self.compareListImages()) {
				if (self.validationDataUpdate(self.choiceOptionImages, self.choiceImagesOrigin)) {
					self.isWaiting = true;
					choiceDetailApi.updateChoiceImages(self.choiceImagesHandle,
						function (result) {
								self.isWaiting = false;
								self.choiceOptionImages = result.data;
								self.choiceImagesOrigin = angular.copy(self.choiceOptionImages);
								self.success = result.message;
								self.classifyImage();
								self.tableActive.reload();
								self.tableNotActive.reload();
							if (isReturnToList) {
								$rootScope.$broadcast('showMessageUpdateChoice',result.message);
								self.itemSending.isShowing = false;
							} else if(self.isReturnToTab){
								$rootScope.error = null;
									$rootScope.success = self.success;
									$rootScope.isEditedOnPreviousTab = true;
								self.returnToTab();
							}
						},
						function (error) {
							self.isWaiting = false;
							self.fetchError(error);
						});
				}
			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * Get data Image Category and Image Source.
		 */
		self.findAllChoiceImageSource = function () {
			imageInfoApi.getImageSoures(function (response) {
				self.lstImageSource = response;
			}, function (error) {
				self.fetchError(error);
			});
		};
		/**
		 * Add event for input type file.
		 */
		self.addEventForInputFile = function () {
			var uploadImageData = document.getElementById("upload-choice-image-input");
			uploadImageData.addEventListener('change', function (e) {
				var imageData = uploadImageData.files[0];
				if (imageData != undefined && imageData.type != undefined) {
					var fileType = imageData.type.split('/').pop();
					if (!self.validFileTypes.includes(fileType.toLowerCase())) {
						self.showConfirmUploadImageModal("Upload", imageData.name + " is incorrect file format", "confirmModal");
					}
				}
			});
		};

		/**
		 * Show Upload Image Modal.
		 */
		self.showUploadImageModal = function () {
			if (self.compareListImages()) {
				self.showConfirmUploadImageModal("Choice Image Upload", "There are changes in the grid. Please save changes before Upload Image.", "confirmModal");
			} else {
				$('#upload-file-modal').modal({ backdrop: 'static', keyboard: true });
				self.resetDataImageUpload();
			}

		};
		/**
		 * Reset data upload choice image.
		 */
		self.resetDataImageUpload = function () {
			self.styleDisplay2Modal = { 'z-index': 1050 };
			self.imageCategorySelected = {
				description: "Select Image Category"
			};
			self.imageSourceSelected = {
				description: "Select Image Source"
			};
			self.fileUpload = {};
			document.getElementById("upload-choice-image-input").value = "";
		};

		/**
		 * Handle click button browse.
		 */
		self.browseClick = function () {
			angular.element(document.querySelector('#upload-choice-image-input')).click();
		};
		/**
		 * Validate and upload choice image.
		 */
		self.uploadChoiceImage = function () {
			if (self.imageCategorySelected.id === undefined || angular.equals(self.imageCategorySelected.id, '')) {
				self.showConfirmUploadImageModal("Upload", "Please select Image Category.", "confirmModal");
				self.imageCategorySelected.error = true;
			} else if (self.imageSourceSelected.id === undefined || angular.equals(self.imageSourceSelected.id, '')) {
				self.showConfirmUploadImageModal("Upload", "Please select Image Source.", "confirmModal");
				self.imageSourceSelected.error = true;
			} else {
				if (self.fileUpload === undefined || self.fileUpload.name === undefined || angular.equals(self.fileUpload.name, '')) {
					self.showConfirmUploadImageModal("Upload", "Please select image file to upload.", "confirmModal");
					if (self.fileUpload === undefined) {
						self.fileUpload = {};
					}
					self.fileUpload.error = true;
				} else {
					var fileType = self.fileUpload.type.split('/').pop();
					if (self.validFileTypes.includes(fileType.toLowerCase())) {
						self.showConfirmUploadImageModal("Upload", "Would you like to continue to upload this file?", "continueUpload");
					} else {
						self.showConfirmUploadImageModal("Upload", self.fileUpload.name + " is incorrect file format", "confirmModal");
						self.fileUpload.error = true;
					}
				}
			}
		};
		/**
		 * Upload Image.
		 */
		self.uploadImage = function () {
			var formData = new FormData();
			formData.append('fileUpload', self.fileUpload);
			formData.append('chcOptCd', self.choiceOption.key.choiceOptionCode);
			formData.append('imgCategoryCode', self.imageCategorySelected.id);
			formData.append('imgSourceCode', self.imageSourceSelected.description);
			$('#confirm-upload-image-modal').modal("hide");
			$('#upload-file-modal').modal("hide");
			self.isWaiting = true;
			self.error = '';
			self.success = '';
			choiceDetailApi.uploadChoiceImage(formData,
				function (result) {
					self.isWaiting = false;
					self.choiceOptionImages = result.data;
					self.choiceImagesOrigin = angular.copy(self.choiceOptionImages);
					self.success = 'Image "' + self.fileUpload.name
						+ '" is uploaded successfully and associated with the Choice "'
						+ self.choiceOption.key.choiceOptionCode + '"';
					self.classifyImage();
					self.findAllImageDatas();
					self.tableActive.reload();
					self.tableNotActive.reload();
				}, function (error) {
					self.fetchError(error);
				}
			);
		};
		/**
		 * Show modal upload choice image.
		 */
		self.showConfirmUploadImageModal = function (title, content, typeModal) {
			self.styleDisplay2Modal = { 'z-index': 1040 };
			self.confirmModalUploadTitle = title;
			self.confirmModalUploadContent = content;
			self.typeModal = typeModal;
			$('#confirm-upload-image-modal').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Validate file upload.
		 */
		self.validateFileUpload = function () {
			var fileError = false;
			if (self.fileUpload !== undefined && self.fileUpload.name !== undefined) {
				var fileType = self.fileUpload.type.split('/').pop();
				if (!self.validFileTypes.includes(fileType.toLowerCase())) {
					fileError = true;
				}
			}
			return fileError;
		};

		/**
		 * Handle change image category.
		 */
		self.onChangeCategory = function (choiceImage) {
			if (choiceImage.imageCategoryCode === self.CATEGORY_SWATCHES) {
				choiceImage.imagePriorityCode = self.PRIMARY_NOT_APPLICABLE;
				angular.forEach(self.imagePrimaries, function (imagePriority) {
					if (imagePriority.id === self.PRIMARY_NOT_APPLICABLE) {
						choiceImage.imagePriority = angular.copy(imagePriority);
					}
				});
			}
		};

		/**
		 * Check if not exist any Rejected Image on Grid
		 */
		self.hasRejectedOnList = function () {
			var hasRejected = false;
			angular.forEach(self.choiceImageNotActiveOnline, function (choiceImage) {
				if (choiceImage.imageStatusCode === self.STATUS_REJECTED) {
					hasRejected = true;
				}
			});
			angular.forEach(self.choiceImageActiveOnline, function (choiceImage) {
				if (choiceImage.imageStatusCode === self.STATUS_REJECTED) {
					hasRejected = true;
				}
			});
			return hasRejected;
		};

		/**
		 * Handle save data before return to list choice.
		 */
		self.saveDataAndReturnToList = function () {
			$('#confirm-upload-image-modal').modal("hide");
			self.saveChoiceImages(true);
		};

		/**
		 * Handle no save data and return to list choice.
		 */
		self.noSaveDataAndReturnToList = function () {
			$('#confirm-upload-image-modal').modal("hide");
			self.itemSending.isShowing = false;
			$rootScope.$broadcast('showMessageUpdateChoice',null);
		};

		/**
		 * Check if not exist any rejected record
		 * @returns {boolean}
		 */
		self.notExistRejectedRecord = function(){
			var returnValue = true;
			angular.forEach(self.choiceImagesOrigin, function (choiceImageOrigin) {
				if (choiceImageOrigin.imageStatusCode === self.STATUS_REJECTED) {
					returnValue = false;
				}
			});
			return returnValue;
		};

		/**
		 * init value of self.isShowReject
		 */
		self.initShowHideRejectValue = function (){
			if (self.notExistRejectedRecord()){
				self.isShowReject = true;
			} else {
				self.isShowReject = false;
			}
		};

		/**
		 * Check if image is rejected and not show on grid
		 * @param choiceImage
		 * @returns {boolean}
		 */
		self.hasRejected = function(choiceImage){
			return (choiceImage.imageStatusCode === self.STATUS_REJECTED && !self.isShowReject);
		};

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function () {
			if (self.isReturnToTab) {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_CHOICE_OPTION, function () {
			if(self.itemSending.isShowing){
			if (self.compareListImages()) {
				self.isReturnToTab = true;
				self.error = '';
				self.success = '';
				$('#changeTabConfirmation').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
			}

		});

		/**
		 * Close Popup
		 */
		self.doClosePopupConfirmation = function () {
			$('#changeTabConfirmation').modal('hide');
			if (self.isReturnToTab) {
				$('#changeTabConfirmation').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			}
		};

		/**
		 * Hides save confirm dialog.
		 */
		self.cancelConfirmDialog = function () {
			$('.modal-backdrop').attr('style', '');
			$('#changeTabConfirmation').modal('hide');
		};
	}
})();
