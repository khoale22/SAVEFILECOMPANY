/*
 *   currentLevelPanelComponent.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Custom Hierarchy -> CurrentLevel Info page component.
 *
 * @author s753601
 * @since 2.16.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('currentLevelPanel', {
		// isolated scope binding
		bindings: {
			currentLevel: '<',
			intermediateView: '<',
			attributeId: '='
		},
		scope: {},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/customHierarchy/currentLevelPanel.html',
		// The controller that handles our component logic
		controller: currentLevelPanelController
	});


	currentLevelPanelController.$inject = ['customHierarchyService', 'CustomHierarchyApi', '$state', 'appConstants', 'ProductSearchService',
		'$timeout', '$rootScope', '$scope', 'DownloadImageService', 'PermissionsService'];

	/**
	 * Case Pack Import component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @constructor
	 */
	function currentLevelPanelController(customHierarchyService, customHierarchyApi, $state, appConstants, productSearchService,
			$timeout, $rootScope, $scope, downloadImageService, permissionsService) {
		/** All CRUD operation controls of Case pack Import page goes here */
		var self = this;

		/**
		 * The original current level attribute data used to compare when detecting changes
		 * @type {{}}
		 */
		self.originalAttributes = {};
		/**
		 * The original image data for the current level used to compare when detecting changes
		 * @type {Array}
		 */
		self.originalImages = [];

		/**
		 * The current images which hold all possible changed fields
		 * @type {Array}
		 */
		self.currentImages = [];
		/**
		 * Code table representation for all the possible image categories
		 * @type {Array}
		 */
		self.imageCategories = [];

		/**
		 * Code table representation for all the possible image categories
		 * @type {Array}
		 */
		self.imagePriority = [];

		/**
		 * Code table representation for all the possible image statuses
		 * @type {Array}
		 */
		self.imageStatus = [];

		/**
		 * Code table representation for all the possible image sources
		 * @type {Array}
		 */
		self.imageSource =[];

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
		 * Hold a list of all hierarchies
		 * @type {Array}
		 */
		self.destinationHierarchies = [];
		/**
		 * The currently selected destination location for the source level
		 * @type {null}
		 */
		self.currentDestinationLevel=null;
		/**
		 * The currently selected source level for a move or link action
		 * @type {null}
		 */
		self.currentSourceLevel = null;
		self.hierarchyEditErrorMessage=null;
		/**
		 * The type of hierarchy change the user is requesting
		 * @type {string}
		 */
		self.moveType="";
		/**
		 * This holds all of the proposed removed and added hierarchy levels
		 * @type {{relationshipsAdded: Array, relationshipsRemoved: Array}}
		 */
		self.hierarchyChanges = {
			relationshipsAdded: [],
			relationshipsRemoved: []
		};
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
		 * Category Code
		 * @type {string}
		 */
		const CATEGORY_SWATCHES = 'SWAT';

		self.PROPERTIES_TAB = "PROPERTIES_TAB";
		self.IMAGES_TAB = "IMAGES_TAB";
		self.ATTRIBUTES_TAB = "ATTRIBUTES_TAB";
		self.currentTab = null;
		var convertDate = $scope.$parent.convertDate;
		self.changingContents = false;
		self.waitingForUpdate = "Updating...please wait.";

		/**
		 * These strings are used to display errors in uploading an image
		 * @type {string}
		 */
		var INVALID_FILE_UPLOAD = "Attempting to upload an invalid file type";
		var MISSING_OR_INVALID_FILE = "Missing or Invalid file type cannot upload";
		var MISSING_IMAGE_SOURCE = "Missing Image Source";
		var MISSING_IMAGE_CATEGORY = "Missing Image Category";
		var IMAGE_UPLOAD_FAIL_TO_LAUNCH = "Failed to upload Image because of the following error(s)\n ";
		var UNDEFINED_TYPE = 'undefined';
		var foundLevel=false;
		var originalSourceLevel = null;
		var originalSourceHierarchy = null;
		var originalDestinationHierarchy=null;
		var CHANGE_TYPE_LINK="Link";
		var CHANGE_TYPE_MOVE="Move";
		var pathToSelectedLevel=null;

		/**
		 * Messages
		 * @type {string}
		 */
		self.IMG_ONE_PRIMARY_FOR_LEVEL = "Only one Image can be Primary for a Level.";
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


		this.$onInit = function () {
			customHierarchyApi.getImageCategories(self.loadCategoryData, self.fetchError);
			customHierarchyApi.getImageStatuses(self.loadStatusData, self.fetchError);
			customHierarchyApi.getImagePriorities(self.loadPriorityData, self.fetchError);
			customHierarchyApi.getImageSources(self.loadSourceData, self.fetchError);
			customHierarchyApi.findAllHierarchyContexts({}, self.loadHierarchyContexts, self.fetchError);
		};

		this.$onChanges = function () {
			self.originalCurrentLevelImages = [];
			self.currentLevelImages = [];
			self.changingContents = true;
			self.initializeCurrentSelectedTab();
			self.originalCurrentLevel = self.currentLevel;
			self.currentLevel = angular.copy(self.originalCurrentLevel);
			self.setDateForDatePicker();
			self.updateHasChanges();
			self.changingContents = false;
			self.getAttributes();
		};

		/**
		 * This function retrieves and displays entity attributes and its children,
		 * if any, based on the current-level of the product within the customHierarchy
		 */
		self.getAttributes = function(){
			if(permissionsService.getPermissions("EC_AMAN_02", "VIEW")){
				var parameters = {};
				if(self.intermediateView){
					parameters.hierarchyContextId = self.currentLevel.key.hierarchyContext;
					parameters.entityId= self.currentLevel.key.childEntityId;
				} else {
					parameters.hierarchyContextId = self.currentLevel.id;
					parameters.entityId= self.currentLevel.key.childEntityId;
				}
				customHierarchyApi.findAttributesByHierarchyContextAndEntity(parameters,
						function (result) {
					self.attributes = result;
				},
				function (error) {
					self.fetchError(error);
				});
			}
		};

		/**
		 * This method initializes the current selected tab. This is to handle if they had changed an image, then saved,
		 * the user should come back to having the image tab visible after the 'refresh' of new data.
		 */
		self.initializeCurrentSelectedTab = function(){
			self.currentTab = customHierarchyService.getCurrentLevelTab();

			if (self.currentTab === self.IMAGES_TAB) {
				$timeout(function(){
					$('#currentLevelTabs a[data-target="#imageInfo"]').tab('show');
				});
				customHierarchyApi.getCurrentLevelImages({entityId: self.currentLevel.key.childEntityId}, self.loadImageData, self.fetchError);
			}
		};

		/**
		 * This method will take image data for the current level and sync it with the service to communicate out to the child scopes
		 * @param results
		 */
		self.loadImageData = function(results){
			self.originalCurrentLevelImages = results;
			self.currentLevelImages = angular.copy(results);
			angular.forEach(results, function (imageMetaData) {
				imageMetaData.imagePriorityCode = imageMetaData.imagePriorityCode.trim();
				imageMetaData.imageStatusCode = imageMetaData.imageStatusCode.trim();
				self.getSingleImage(imageMetaData, results.indexOf(imageMetaData));
			});
		};

		/**
		 * This is the callback method that loads all of the hierarchy contexts received from the back end.
		 *
		 * @param results List of all hierarchy contexts.
		 */
		self.loadHierarchyContexts =function (results){
			self.hierarchyContexts = [];
			angular.forEach(results, function (hierarchyContext) {
				self.hierarchyContexts.push(hierarchyContext);
			});
		};

		/**
		 * Sets up category code table after getting it from the backend
		 * @param results
		 */
		self.loadCategoryData = function (results) {
			self.imageCategories = results;
		};

		/**
		 * Sets up status code table after getting it from the backend
		 * @param results
		 */
		self.loadStatusData = function (results) {
			self.imageStatus = results;
		};

		/**
		 * Sets up priority code table after getting it from the backend
		 * @param results
		 */
		self.loadPriorityData = function (results) {
			self.imagePriority = results;
		};

		/**
		 * Sets up source code table after getting it from the backend
		 * @param results
		 */
		self.loadSourceData = function (results) {
			self.imageSource = results;
		};

		/**
		 * Fetches the error from the back end.
		 * @param error
		 */
		self.fetchError = function (error) {
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
			customHierarchyService.setErrorMessage(error, true);
		};

		/**
		 * This method asks the webservice if the current level has any children or if the or if the current level
		 * is the product level
		 * @returns {*|boolean}
		 */
		self.checkIfLeafNode = function () {
			return self.intermediateView && (self.currentLevel.childRelationships === null || self.currentLevel.childRelationships.length === 0)
				&& (self.currentLevel.countOfProductChildren != null && self.currentLevel.countOfProductChildren > 0);
		};

		/**
		 * This method makes a call to get all of the images for the image info individually
		 * @param image Image object with metadata.
		 * @param index Index of images array currently being looked at.
		 */
		self.getSingleImage = function (image, index) {
			customHierarchyApi.getImage(image.uriText,
				function (results) {
					var imageData = "";
					var useImageData = false;
					if (results.data.length !== 0) {
						imageData = "data:image/jpg;base64," + results.data;
						useImageData = true;
					} else {
						useImageData = false;
					}
					image.image = imageData;
					image.useImageData = useImageData;
					self.currentLevelImages[index].image = angular.copy(imageData);
					self.currentLevelImages[index].useImageData = angular.copy(useImageData);
				},
				self.fetchError);
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
		 * Set the value as a date so date picker understands how to represent value.
		 */
		self.setDateForDatePicker = function () {
			self.effectiveDatePickerOpened = false;
			self.endDatePickerOpened = false;
		};

		/**
		 * Open the effective date picker to select a new date.
		 */
		self.openEffectiveDatePicker = function () {
			self.effectiveDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};
		/**
		 * Open the effective date picker to select a new date.
		 */
		self.openEndDatePicker = function () {
			self.endDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};
		/**
		 * Sets the current duty confirmation date.
		 */
		self.setDateForEffectiveDatePicker = function () {
			self.effectiveDatePickerOpened = false;
			if (self.currentLevelAttributes !== null) {
				if (self.currentLevelAttributes.effectiveDate !== null) {
					self.currentEffectiveDate =
						new Date(self.currentLevelAttributes.effectiveDate);
				} else {
					self.currentEffectiveDate = null;
				}
			}
		};
		/**
		 * Sets the current duty confirmation date.
		 */
		self.setDateForEndDatePicker = function () {
			self.endDatePickerOpened = false;
			if (self.currentLevelAttributes !== null) {
				if (self.currentLevelAttributes.endDate !== null) {
					self.currentEndDate =
						new Date(self.currentLevelAttributes.endDate);
				} else {
					self.currentEndDate = null;
				}
			}
		};

		/**
		 * Open the FreightConfirmed picker to select a new date.
		 */
		self.openEffectiveDatePicker = function () {
			self.effectiveDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Open the FreightConfirmed picker to select a new date.
		 */
		self.openEndDatePicker = function () {
			self.endDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * This method is used to infrom the home controller that the current level panel has a change
		 */
		self.updateHasChanges = function () {
			customHierarchyService.setIsCurrentDifferent(isDifference(self.currentTab));
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
			// customHierarchyService.setCurrentLevelImages(self.currentLevelImages);
			self.updateHasChanges();
		};

		/**
		 * Initializes the modal and sets the onchange for the file input to check the file type
		 */
		self.initModal = function () {
			var uploadImageData = document.getElementById("uploadImageData");
			var invalidSpan = document.getElementById("invalidSpan");
			var imageTypeCode = "";
			self.invalidFileType = true;
			uploadImageData.addEventListener('change', function (e) {
				self.uploadImageName = "";
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
		 * This method checks to make sure all required information is present to create a new image record
		 * @returns {boolean}
		 */
		self.validUpload = function () {
			var invalidUpload = false;
			var invalidComponents = IMAGE_UPLOAD_FAIL_TO_LAUNCH;
			if (angular.equals(self.uploadImageCategory, "")) {
				invalidComponents = invalidComponents + MISSING_IMAGE_CATEGORY;
				invalidUpload = true;
			}
			if (angular.equals(self.uploadImageSource, "")) {
				if (invalidUpload) {
					invalidComponents = invalidComponents + "\n ";
				}
				invalidComponents = invalidComponents + MISSING_IMAGE_SOURCE;
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
			self.uploadImageCategory = "";
			self.uploadImageSource = "";
			self.uploadImageDestinations = self.destinations;
			self.uploadImageImage = "";
		};

		/**
		 * This method will check if the upload request is valid and then make a call to the api to upload the image
		 * This method will also close the modal
		 */
		self.uploadImage = function () {
			customHierarchyService.setErrorMessage(null, true);
			if (self.validUpload()) {
				// $('.modal').modal('hide');
				var uploadImage = {
					entityId: self.currentLevel.key.childEntityId,
					imageCategoryCode: self.uploadImageCategory,
					imageSourceCode: self.uploadImageSource,
					imageData: self.uploadImageImage,
					imageName: self.uploadImageName
				};
				customHierarchyApi.uploadImageMetadata(uploadImage, self.uploadResult, self.fetchError);
				self.isWaitingForResponse = true;
			}
		};

		/**
		 * On successful upload reset the view
		 * @param results
		 */
		self.uploadResult = function (results) {
			customHierarchyService.setSuccessMessage(results.message, true);
			customHierarchyService.updateSelectedHierarchy();
		};

		/**
		 * Sends the user to the custom hierarchy for that image
		 * @param entityId
		 */
		self.navigateToCustomHierarchy = function (referenceUPC) {
			customHierarchyService.setReferenceUPC(referenceUPC);
			productSearchService.setToggledTab('imageInfoTab');
			productSearchService.setSelectedTab('imageInfoTab');
			$state.go(appConstants.HOME_STATE);
		};

		/**
		 * This method will determine which kind of move is being preformed
		 * @param moveType
		 */
		self.setMoveType = function(moveType){
			self.currentDestinationLevel = null;
			self.hierarchyChangeMessage = null;
			self.hierarchyChanges.relationshipsRemoved = [];
			self.hierarchyChanges.relationshipsAdded = [];
			self.hierarchyEditErrorMessage = null;
			self.moveType =  moveType;
			self.sourceHierarchy = angular.copy(customHierarchyService.getHierarchyContextSelected());
			self.destinationHierarchies = [];
			if(self.moveType === CHANGE_TYPE_LINK){
				self.destinationHierarchies = angular.copy(self.hierarchyContexts);
			} else {
				self.destinationHierarchies.push(angular.copy(customHierarchyService.getHierarchyContextSelected()));
				self.destinationHierarchies[0].isWaitingForRelationshipResponse = false;
			}
			foundLevel = false;
			self.currentSourceLevel = angular.copy(self.currentLevel);
			self.expandLevels(self.sourceHierarchy, self.currentSourceLevel);
			originalSourceHierarchy = angular.copy(self.sourceHierarchy);
			originalDestinationHierarchy = angular.copy(self.destinationHierarchies);
			originalSourceLevel = angular.copy(self.currentSourceLevel);
		};

		/**
		 * This method will expand the levels leading to the currently selected source level
		 * @param level
		 * @param target
		 * @returns {boolean}
		 */
		self.expandLevels = function(level, target){
			if(level.key !== 'undefined' && angular.equals(level.key, target.key)){
				level.isSelected = true;
				level.isCollapsed=false;
				foundLevel = true;
				return true;
			}else{
				if(!foundLevel){
					if(level.childRelationships!== null){
						for(var index=0; index<level.childRelationships.length; index++){
							if(self.expandLevels(level.childRelationships[index], target)){
								level.isCollapsed=false;
								return true;
							}
						}
					}
				}
			}
			return false;
		};

		/**
		 * This method is used to verify what that both a source and destination is selected
		 * @returns {boolean}
		 */
		self.isSourceAndDestinationSelected= function () {
			if(self.currentSourceLevel === null || self.currentDestinationLevel === null || self.hierarchyChanges.relationshipsAdded.length>0){
				return false;
			} else {
				return true;
			}
		};

		/**
		 * This method is used to verfy that all changes are valid
		 * @returns {boolean}
		 */
		self.isValidChange = function(){
			if(self.isSourceAndDestinationSelected()){
				self.hierarchyEditErrorMessage = null;
				// make sure the move/ link is not adding a node to itself
				if(typeof self.currentSourceLevel['key'] !== UNDEFINED_TYPE &&
					typeof self.currentDestinationLevel['key'] !== UNDEFINED_TYPE){
					if(self.currentSourceLevel.key.childEntityId === self.currentDestinationLevel.key.childEntityId){
						self.hierarchyEditErrorMessage = "Source Level cannot be added to itself.";
						return false;
					}
				}
				// if this is a link, the source cannot have non-product relationships
				if(self.moveType === CHANGE_TYPE_LINK &&
					self.currentSourceLevel.childRelationships !== null ){
					if(self.currentSourceLevel.childRelationships.length > 0){
						self.hierarchyEditErrorMessage = "Source Level cannot have any non-product relationships.";
						return false;
					}
				}
				// make sure the destination has no product children
				if(self.currentDestinationLevel.countOfProductChildren > 0){
					self.hierarchyEditErrorMessage = "Destination Level cannot have any product relationships.";
					return false;
				}
				// make sure the destination does not already have the source
				if(self.currentDestinationLevel.childRelationships !==null){
					for(var index = 0; index < self.currentDestinationLevel.childRelationships.length; index++){
						if(self.currentDestinationLevel.childRelationships[index].key.childEntityId === self.currentSourceLevel.key.childEntityId){
							self.hierarchyEditErrorMessage = "Source Level already in Destination Level.";
							return false;
						}
					}
				}
			}
			return true;
		};

		/**
		 * This method shows the purposed changes
		 */
		self.previewHierarchyChange = function(){
			var removeCandidate = angular.copy(self.currentSourceLevel);
			var tempDestinationLevel = angular.copy(self.currentSourceLevel);
			tempDestinationLevel.isSelected = false;
			self.hierarchyChangeMessage = self.moveType.concat(" ").concat(removeCandidate.childDescription.shortDescription).concat(" to ");

			if(typeof self.currentDestinationLevel.key  !== UNDEFINED_TYPE){
				tempDestinationLevel.key.parentEntityId = angular.copy(self.currentDestinationLevel.key.childEntityId);
				tempDestinationLevel.key.hierarchyContext = angular.copy(self.currentDestinationLevel.key.hierarchyContext);
				tempDestinationLevel.hierarchyContext = angular.copy(self.currentDestinationLevel.hierarchyContext);
				tempDestinationLevel.childDescription.key.hierarchyContext =angular.copy(self.currentDestinationLevel.hierarchyContext);
				tempDestinationLevel.parentDescription.shortDescription = angular.copy(self.currentDestinationLevel.childDescription.shortDescription);
				self.hierarchyChangeMessage = self.hierarchyChangeMessage.concat(self.currentDestinationLevel.childDescription.shortDescription);
			} else {
				tempDestinationLevel.key.parentEntityId = angular.copy(self.currentDestinationLevel.parentEntityId);
				tempDestinationLevel.key.hierarchyContext = angular.copy(self.currentDestinationLevel.id);
				tempDestinationLevel.hierarchyContext = angular.copy(self.currentDestinationLevel.id);
				tempDestinationLevel.childDescription.key.hierarchyContext =angular.copy(self.currentDestinationLevel.id);
				tempDestinationLevel.parentDescription.shortDescription = angular.copy(self.currentDestinationLevel.description);
				self.hierarchyChangeMessage = self.hierarchyChangeMessage.concat(self.currentDestinationLevel.description);
			}
			self.hierarchyChangeMessage = self.hierarchyChangeMessage.concat(".");
			self.hierarchyChanges.relationshipsAdded = [];
			tempDestinationLevel.isMoveSelected = true;
			tempDestinationLevel.isCollapsed = false;
			self.hierarchyChanges.relationshipsAdded.push(tempDestinationLevel);
			self.currentDestinationLevel.childRelationships.push(tempDestinationLevel);
			foundLevel = false;
			if(self.moveType === CHANGE_TYPE_MOVE){
				self.hierarchyChanges.relationshipsRemoved.push(removeCandidate);
				self.removeLevelFromSelectedHierarchy(self.destinationHierarchies, removeCandidate);
			}
		};

		/**
		 * Removes the target relationship from the current hierarchy context if present.
		 *
		 * @param destinationHierarchies List of all destination hierarchies.
		 * @param relationshipToRemove Relationship to remove.
		 */
		self.removeLevelFromSelectedHierarchy=function(destinationHierarchies, relationshipToRemove){
			for(var index = 0; index < destinationHierarchies.length; index++){
				if(destinationHierarchies[index].id === self.currentSourceLevel.key.hierarchyContext){
					self.removeLevel(destinationHierarchies[index], relationshipToRemove);
					break;
				}
			}
		};

		/**
		 * Removes the target relationship from the current searching level if present
		 * @param searchingLevel
		 * @param targetRelationship
		 */
		self.removeLevel=function(searchingLevel, targetRelationship){
			if(!foundLevel){
				for(var index = 0; index<searchingLevel.childRelationships.length; index++){
					if(angular.equals(searchingLevel.childRelationships[index].key, targetRelationship.key)){
						searchingLevel.childRelationships.splice(index, 1);
						foundLevel = true;
						self.currentSourceLevel = null;
						break;
					} else if(searchingLevel.childRelationships[index].childRelationships){
						self.removeLevel(searchingLevel.childRelationships[index], targetRelationship);
					}
				}
			}
		};

		/**
		 * save changes to hierarchy structure
		 */
		self.saveHierarchyChanges = function(){
			self.isWaitingForResponse = true;
			convertDatesInHierarchyChanges();
			if(self.hierarchyChanges.relationshipsRemoved.length > 0){
				customHierarchyApi.moveLevels(self.hierarchyChanges,
					function (results) {
						self.isWaitingForResponse = false;
						self.closeMoveLinkModal();
						customHierarchyService.setSuccessMessage(results.message, true);
						pathToSelectedLevel.push(self.hierarchyChanges.relationshipsAdded[0]);
						customHierarchyService.setHierarchyToSelectedLevelAndUpdate(pathToSelectedLevel);
					}, self.fetchMoveLinkError);
			} else {
				customHierarchyApi.linkLevels(self.hierarchyChanges,
					function (results) {
						self.isWaitingForResponse = false;
						self.closeMoveLinkModal();
						customHierarchyService.setSuccessMessage(results.message, true);
						customHierarchyService.updateSelectedHierarchy();
					}, self.fetchMoveLinkError);
			}
		};

		/**
		 * Convert dates within hierarchy changes back into back end readable dates.
		 */
		function convertDatesInHierarchyChanges(){
			angular.forEach(self.hierarchyChanges.relationshipsAdded, function(addedRelationship){
				addedRelationship.effectiveDate = convertDate(addedRelationship.effectiveDate);
				addedRelationship.expirationDate = convertDate(addedRelationship.expirationDate);
			});
			angular.forEach(self.hierarchyChanges.relationshipsRemoved, function(removedRelationship){
				removedRelationship.effectiveDate = convertDate(removedRelationship.effectiveDate);
				removedRelationship.expirationDate = convertDate(removedRelationship.expirationDate);
			});

		}

		/**
		 * This method wil ensure that the move/link modal closes cleanly
		 */
		self.closeMoveLinkModal = function(){
			$('#linkMoveModal').modal('hide');
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
		};
		/**
		 * This method processes any errors from the move/link action
		 * @param error
		 */
		self.fetchMoveLinkError = function(error){
			self.isWaitingForResponse = false;
			if (error && error.data) {
				self.isWaitingForResponse = false;
				if (error.data.message) {
					self.hierarchyEditErrorMessage =error.data.message;
				} else {
					self.hierarchyEditErrorMessage =error.data.error;
				}
			}
			else {
				self.hierarchyEditErrorMessage = "An unknown error occurred.";
			}
		};

		/**
		 * resets changes to hierarchy structure
		 */
		self.resetHierarchyChanges = function(){
			self.hierarchyEditErrorMessage = null;
			self.hierarchyChangeMessage = null;
			self.sourceHierarchy = angular.copy(originalSourceHierarchy);
			self.destinationHierarchies = angular.copy(originalDestinationHierarchy);
			self.currentSourceLevel = angular.copy(originalSourceLevel);
			self.currentDestinationLevel = null;
			self.hierarchyChanges.relationshipsRemoved = [];
			self.hierarchyChanges.relationshipsAdded = [];
		};

		/**
		 * This method ensures that there is a valid change to send to the api
		 * @returns {boolean}
		 */
		self.isValidSave = function(){
			return (self.hierarchyChanges.relationshipsAdded.length === 0);
		};

		/**
		 * Checks to see if there were any changes made.
		 * @returns {boolean}
		 */
		function isDifference(tab) {
			if (tab === self.PROPERTIES_TAB) {
				if(angular.toJson(self.currentLevel) !== angular.toJson(self.originalCurrentLevel)){
					$rootScope.contentChangedFlag = true;
					customHierarchyService.setCurrentLevelAttributeChanges(getCurrentLevelAttributeChanges());
					return true;
				} else{
					customHierarchyService.setCurrentLevelAttributeChanges(null);
					return false;
				}
			} else if (tab === self.IMAGES_TAB) {
				if(angular.toJson(self.originalCurrentLevelImages) !== angular.toJson(self.currentLevelImages)){
					$rootScope.contentChangedFlag = true;
					customHierarchyService.setCurrentLevelImageChanges(getCurrentLevelImageChanges());
					customHierarchyService.setValidateCurrentLevelImagesMessage(self.validationDataUpdate(self.currentLevelImages, self.originalCurrentLevelImages));
					return true;
				} else{
					customHierarchyService.setCurrentLevelImageChanges(null);
					customHierarchyService.setValidateCurrentLevelImagesMessage(null);
					return false;
				}
			} else if (tab === self.ATTRIBUTES_TAB) {
				return false;
			}

		}

		/**
		 * Validating Data of image info before call update.
		 *
		 * @param currentLevelImages list of image after edit.
		 * @param originalCurrentLevelImages list of image before edit.
		 * @returns {boolean} true if has no business logic error. Otherwise return false.
		 */
		self.validationDataUpdate = function (currentLevelImages, originalCurrentLevelImages) {
			var errorMessage = '';
			/**
			 * Check only one Primary Image can be Primary.
			 */
			if (self.countPrimary(currentLevelImages) > 1) {
				errorMessage = self.IMG_ONE_PRIMARY_FOR_LEVEL;
				return errorMessage;
			}
			if (self.isNotExistActiveAndPrimary(currentLevelImages)) {
				angular.forEach(currentLevelImages, function (currentLevelImage) {
					if (currentLevelImage.activeOnline) {
						/**
						 * Check Alternate Images cannot be "Active Online"
						 * without a Primary Image that is "Active Online".
						 */
						if (currentLevelImage.imagePriorityCode === PRIORITY_ALTERNATE) {
							errorMessage = errorMessage || self.IMG_ACTIVE_ONLINE_IN_ALTERNATE;
						}
						/**
						 * Check Swatch images cannot be "Active Online"
						 * without a Primary image that is "Active Online".
						 */
						if (currentLevelImage.imageCategoryCode === CATEGORY_SWATCHES) {
							errorMessage = errorMessage || self.IMG_ACTIVE_ONLINE_IN_CATEGORY_SWATCH;
						}
					}
				});
				if (errorMessage != '') {
					return errorMessage;
				}
			}
			angular.forEach(currentLevelImages, function (currentLevelImage) {
				/**
				 * Check an image in 'Rejected' status cannot be set as 'Primary'.
				 */
				if (currentLevelImage.imageStatusCode === STATUS_REJECTED
					&& currentLevelImage.imagePriorityCode === PRIORITY_PRIMARY) {
					errorMessage = errorMessage || self.IMG_PRIMARY_REJECTED;
				}
				if (currentLevelImage.activeOnline) {
					/**
					 * Check an image in "For review" status can not be made "Active Online".
					 */
					if (currentLevelImage.imageStatusCode === STATUS_FOR_REVIEW) {
						errorMessage = errorMessage || self.IMG_PRIMARY_FOR_REVIEW_ACTONLINE;
					}
					/**
					 * Check an image in "Rejected" status cannot be made "Active Online".
					 */
					if (currentLevelImage.imageStatusCode === STATUS_REJECTED) {
						errorMessage = errorMessage || self.IMG_STATUS_REJECTED;
					}
					/**
					 * Check an image cannot be "Active Online"
					 * without the "Image Category" or the "Primary/Alternate" defined.'
					 */
					if (currentLevelImage.imagePriorityCode === PRIORITY_NOT_APPLICABLE
						&& currentLevelImage.imageCategoryCode !== CATEGORY_SWATCHES) {
						errorMessage = errorMessage || self.IMG_ACTIVE_ONLINE_IS_NONE_CATEGORY_OR_PRIMARY_ALTERNATE;
					}
				}
				/**
				 * Check an image in "Approved" or  "Rejected" status cannot be moved to "For Review" Status.
				 */
				if (currentLevelImage.imageStatusCode === STATUS_FOR_REVIEW) {
					angular.forEach(originalCurrentLevelImages, function (originalCurrentLevelImage) {
						if (originalCurrentLevelImage.key.sequenceNumber === currentLevelImage.key.sequenceNumber
							&& originalCurrentLevelImage.imageStatusCode !== STATUS_FOR_REVIEW) {
							errorMessage = errorMessage || self.IMG_STATUS_CHANGE_TO_REVIEW;
						}
					});
				}
			});
			return errorMessage;
		};

		/**
		 * return true if not exist image has Active and Primary
		 * @param currentLevelImages
		 * @returns {boolean}
		 */
		self.isNotExistActiveAndPrimary = function (currentLevelImages) {
			var isNotExistActiveAndPrimary = true;
			angular.forEach(currentLevelImages, function (currentLevelImage) {
				if (currentLevelImage.activeOnline && currentLevelImage.imagePriorityCode === PRIORITY_PRIMARY) {
					isNotExistActiveAndPrimary = false;
				}
			});
			return isNotExistActiveAndPrimary;
		};

		/**
		 * Count number image has Primary.
		 * @param currentLevelImages
		 * @returns {number}
		 */
		self.countPrimary = function (currentLevelImages) {
			var count = 0;
			angular.forEach(currentLevelImages, function (currentLevelImage) {
				if (currentLevelImage.imagePriorityCode === PRIORITY_PRIMARY) {
					count++;
				}
			});
			return count;
		};

		/**
		 * This method is used to return a current level attribute object that only contains differences between the current an original
		 * current level attributes object
		 * @returns {*}
		 */
		function getCurrentLevelAttributeChanges() {
			var changes = {};
			if (self.currentLevel.childDescription.shortDescription !== self.originalCurrentLevel.childDescription.shortDescription) {
				changes.childDescription = {
					key: self.currentLevel.childDescription.key,
					longDescription: self.currentLevel.childDescription.shortDescription,
					shortDescription: self.currentLevel.childDescription.shortDescription
				};
				changes.genericChildEntity = {
					id: self.currentLevel.key.childEntityId,
					abbreviation: self.currentLevel.childDescription.shortDescription,
					displayText: self.currentLevel.childDescription.shortDescription
				};
			}
			if (self.currentLevel.active !== self.originalCurrentLevel.active) {
				changes.active = self.currentLevel.active;
			}
			if (!angular.equals(self.currentLevel.effectiveDate, self.originalCurrentLevel.effectiveDate)) {
				changes.effectiveDate = convertDate(self.currentLevel.effectiveDate);
			}
			if (!angular.equals(self.currentLevel.expirationDate, self.originalCurrentLevel.expirationDate)) {
				changes.expirationDate = convertDate(self.currentLevel.expirationDate);
			}
			if (!angular.equals(changes, {})) {
				changes.key = {
					parentEntityId: self.currentLevel.key.parentEntityId,
					childEntityId: self.currentLevel.key.childEntityId,
					hierarchyContext: self.currentLevel.key.hierarchyContext
				};
				return changes;
			} else {
				return null;
			}
		}

		/**
		 * Determines which tab you are currently on.
		 * @param tab
		 */
		self.setTab = function(tab) {
			self.resetPreviousTab(self.currentTab);
			self.currentTab = tab;
			customHierarchyService.setCurrentLevelTab(tab);
			if (self.currentTab === self.IMAGES_TAB) {
				customHierarchyApi.getCurrentLevelImages({entityId: self.currentLevel.key.childEntityId}, self.loadImageData, self.fetchError);
			}
		};

		/**
		 * Resets all of the values back to the original.
		 */
		self.resetPreviousTab = function(tab) {
			if(typeof tab !== 'undefined' && tab !== null) {
				customHierarchyService.setSuccessMessage(null, true);
				if (tab === self.PROPERTIES_TAB) {
					self.currentLevel = angular.copy(self.originalCurrentLevel);
				} else if (tab === self.IMAGES_TAB) {
					self.currentLevelImages = angular.copy(self.originalCurrentLevelImages);
				}
			}
		};

		/**
		 * This method is used to determine changes inthe currrent level metadata
		 * @returns {*}
		 */
		function getCurrentLevelImageChanges() {
			var changes = [];
			for(var index = 0; index < self.currentLevelImages.length; index++){
				var imageChanges = {};
				if(self.currentLevelImages[index].imageAltText !== self.originalCurrentLevelImages[index].imageAltText){
					imageChanges.imageAltText = self.currentLevelImages[index].imageAltText;
				}
				if(self.currentLevelImages[index].imageCategoryCode !== self.originalCurrentLevelImages[index].imageCategoryCode){
					imageChanges.imageCategoryCode = self.currentLevelImages[index].imageCategoryCode;
				}
				if(self.currentLevelImages[index].imagePriorityCode !== self.originalCurrentLevelImages[index].imagePriorityCode){
					imageChanges.imagePriorityCode = self.currentLevelImages[index].imagePriorityCode;
				}
				if(self.currentLevelImages[index].imageStatusCode !== self.originalCurrentLevelImages[index].imageStatusCode){
					imageChanges.imageStatusCode = self.currentLevelImages[index].imageStatusCode;
				}
				if(self.currentLevelImages[index].activeOnline !== self.originalCurrentLevelImages[index].activeOnline){
					imageChanges.activeOnline = self.currentLevelImages[index].activeOnline;
				}
				if(!angular.equals(imageChanges, {})){
					imageChanges.key={
						id: self.currentLevelImages[index].key.id,
						imageSubjectTypeCode: self.currentLevelImages[index].key.imageSubjectTypeCode,
						sequenceNumber: self.currentLevelImages[index].key.sequenceNumber
					};
					if(imageChanges.imageStatusCode == null || imageChanges.imageStatusCode == undefined){
						imageChanges.imageStatusCode = self.currentLevelImages[index].imageStatusCode;
					}
					if(imageChanges.activeOnline == true){
						imageChanges.imagePriorityCode = self.currentLevelImages[index].imagePriorityCode;
					}
					changes.push(angular.copy(imageChanges));
				}
			}
			if(changes.length > 0){
				return changes;
			} else {
				return null;
			}
		}

		/**
		 * This function determines if a hierarchy level has children or not. If the given hierarchy level has a field
		 * 'childRelationships', and it is an array, and it has a length greater than 0, return true. Else return
		 * false.
		 *
		 * @param hierarchyLevel Hierarchy level to test for children.
		 * @returns {boolean} True if has children, false otherwise.
		 */
		self.hasChildren = function(hierarchyLevel){
			if(typeof hierarchyLevel['childRelationships'] === 'undefined' ||
				hierarchyLevel.childRelationships === null){
				return false;
			}
			return angular.isArray(hierarchyLevel.childRelationships) &&
				hierarchyLevel.childRelationships.length > 0;
		};

		/**
		 * This creates a path array for the hierarchy level, to keep track of how to get back to this location. It will
		 * include all parent nodes, as well as the current level.
		 *
		 * @param previousPath Path array containing all parent nodes.
		 * @param currentLevel The current level getting its path array.
		 * @returns {array} Path array to get back to the current level.
		 */
		self.getPathToCurrentLevel = function(previousPath, currentLevel){
			var pathArray;
			// if this is the top level in the hierarchy, the previous path will be undefined; initialize as []
			if(!previousPath){
				pathArray = [];
			}
			// else set the path array to a copy of the previous path; if a copy was not done, children nodes would
			// sometimes get additional nodes that were not part of the path
			else {
				pathArray = angular.copy(previousPath);
			}
			// push the current level onto the path array
			pathArray.push(currentLevel);
			return pathArray;
		};

		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param hierarchyLevel
		 * @returns {boolean}
		 */
		self.isHierarchyLevelCollapsed = function(hierarchyLevel){
			if(typeof hierarchyLevel.isCollapsed === UNDEFINED_TYPE){
				return true;
			}else {
				return hierarchyLevel.isCollapsed;
			}
		};

		/**
		 * This method is called when the user selects a hierarchy context from within the hierarchy context view.
		 *
		 * @param hierarchyContext The hierarchy context selected by the user.
		 */
		self.selectHierarchyContext = function(hierarchyContext){
			if(self.currentDestinationLevel){
				self.currentDestinationLevel.isSelected = false;
			}
			if(hierarchyContext.childRelationships===null){
				hierarchyContext.isWaitingForRelationshipResponse = true;
				customHierarchyApi.findRelationshipByHierarchyContext(
					hierarchyContext,
					function (results) {
						hierarchyContext.childRelationships = results;
						hierarchyContext.isCollapsed = false;
						hierarchyContext.isWaitingForRelationshipResponse = false;
						hierarchyContext.isSelected = true;
						pathToSelectedLevel = [];
						self.currentDestinationLevel = hierarchyContext;
					},
					function (error) {
						hierarchyContext.isWaitingForRelationshipResponse = false;
						self.fetchError(error);
					}
				);
			} else {
				setCollapsedValue(hierarchyContext);
				hierarchyContext.isSelected = true;
				pathToSelectedLevel = [];
				self.currentDestinationLevel = hierarchyContext;
			}
		};

		/**
		 * Method called when user selects a custom hierarchy level from one of the custom hierarchies.
		 *
		 * @param pathToLevel The path to the hierarchy level selected by the user.
		 */
		self.selectCustomHierarchyLevel = function(pathToLevel){
			if(self.currentDestinationLevel){
				self.currentDestinationLevel.isSelected = false;
			}
			pathToSelectedLevel = pathToLevel;
			pathToLevel[pathToLevel.length - 1].isSelected = true;
			setCollapsedValue(pathToLevel[pathToLevel.length - 1]);
			self.currentDestinationLevel = pathToLevel[pathToLevel.length - 1];
		};

		/**
		 * This is a helper function to determine the collapsed value for a given level. If the selected value
		 * was previously selected, set the collapsed state to opposite of what it was before. Else set the collapsed
		 * state to false.
		 *
		 * @param selectedHierarchyLevel Current level selected by user.
		 */
		function setCollapsedValue(selectedHierarchyLevel){
			if (self.currentDestinationLevel){
				if(typeof self.currentDestinationLevel['key'] !== UNDEFINED_TYPE &&
					typeof selectedHierarchyLevel['key'] !== UNDEFINED_TYPE &&
					isOriginalHierarchyLevelEqualToSelectedLevel(self.currentDestinationLevel, selectedHierarchyLevel)){
					// set isCollapsed to opposite of previous value
					selectedHierarchyLevel.isCollapsed = !selectedHierarchyLevel.isCollapsed;
				} else if(typeof self.currentDestinationLevel['key'] === UNDEFINED_TYPE &&
					typeof selectedHierarchyLevel['key'] === UNDEFINED_TYPE &&
					self.currentDestinationLevel.id === selectedHierarchyLevel.id){
					selectedHierarchyLevel.isCollapsed = !selectedHierarchyLevel.isCollapsed;
				} else {
					selectedHierarchyLevel.isCollapsed = false;
				}
			} else {
				// else set isCollapsed to false
				selectedHierarchyLevel.isCollapsed = false;
			}
		}

		/**
		 * This method compares the original value of a custom hierarchy to the current selected value. This is done
		 * by comparing the keys in both. If they are equal, return true. Otherwise return false.
		 *
		 * @param originalLevel Original selected value.
		 * @param selectedLevel Current selected value.
		 * @returns {boolean} True if equal, false otherwise.
		 */
		function isOriginalHierarchyLevelEqualToSelectedLevel(originalLevel, selectedLevel){
			if(originalLevel &&
				originalLevel.key.childEntityId === selectedLevel.key.childEntityId &&
				originalLevel.key.parentEntityId === selectedLevel.key.parentEntityId &&
				originalLevel.key.hierarchyContext === selectedLevel.key.hierarchyContext){
				return true;
			} else {
				return false;
			}
		}

		/**
		 * sets the current attribute id selected
		 * @param attribute The currently selected attribute
		 */
		self.selectAttribute = function(attributeId){
			self.attributeId = angular.copy(attributeId);
		};

		/**
		 * Current image data.
		 * @type {string}
		 */
		self.imageBytes = '';

		/**
		 * Current image format.
		 * @type {string}
		 */
		self.imageFormat = '';

		/**
		 * Show popup to view full image.
		 */
		self.showFullImage = function (image) {
			self.imageFormat = image.imageFormatCode.trim();
			self.imageBytes = image.image;
			$('#fullImageModal').modal({ backdrop: 'static', keyboard: true });
		};

		/**
		 * Download current image.
		 */
		self.downloadImage = function () {
			if (self.imageBytes !== '') {
				downloadImageService.download(self.imageBytes, self.imageFormat===''?'png':self.imageFormat);
			}
		};
	}
}());
