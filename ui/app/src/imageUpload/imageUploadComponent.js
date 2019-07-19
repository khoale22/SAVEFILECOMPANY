/*
 *   kitsComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Kits -> Kits Info page component.
 *
 * @author m594201
 * @since 2.8.0
 */
(function () {

    angular.module('productMaintenanceUiApp').controller('ImageUploadController', imageUploadController);

    imageUploadController.$inject = ['HomeApi','$location','$scope','$rootScope','productSellingUnitsApi', 'ngTableParams'];

    function imageUploadController(homeApi,$location,$scope,$rootScope, productSellingUnitsApi, ngTableParams) {

        var self = this;

        /**
         * This list holds all of the uploaded images.
         * @type {Array}
         */
        self.imagesUploadedToExport=[];

        /**
         * This list holds a of the upload image.
         * @type {Array}
         */
       // self.imageUpload=[];

        /**
         * This list holds all of the possible image categories
         * @type {Array}
         */
        self.imageCategories=[];
        /**
         * The currently selected image category
         * @type {null}
         */
        self.imageCategory=null;

        /**
         * This list holds all of the possible image sources
         * @type {Array}
         */
        self.imageSources =[];
        /**
         * The currently selected image source
         * @type {null}
         */
        self.imageSource = null;

        /**
         * This list holds all of the possible destination domains
         * @type {Array}
         */
        self.destinationDomains = [];
        /**
         * The list of currently selected destination domains
         * @type {Array}
         */
        self.selectedDestinations = [];
        /**
         * Flag for when all images are selected to be uploaded
         * @type {boolean}
         */
        self.uploadAllChecked=false;
        /**
         * Flag for when all images are selected to replace the current primary
         * @type {boolean}
         */
        self.primaryAllChecked=false;
        /**
         * Flag for when all alternate images are selected to upload with show on site
         * @type {boolean}
         */
        self.showOnSiteAllChecked=false;
        /**
         * Flag for when all images are selected to replace the current alternate
         * @type {boolean}
         */
        self.alternateAllChecked=false;
        /**
         * Flag for whether or not to continue uploading images
         * @type {boolean}
         */
        self.continueUploads=false;
        /**
         * Flag for if uploads are currently occurring
         * @type {boolean}
         */
        self.isUploading = false;
        /**
         * Keeps track if the image is still uploading to server or not.
         * @type {boolean}
         */
        $rootScope.isStillUploadImage = false;
        /**
         * List of images to be potentially uploaded
         * @type {Array}
         */
        self.imageData = [];
        /**
         * Code table for Existing image action dropdown
         * These actions are reserved for what to do with the current image image
         * if the new image is set to replace the current image
         * @type {*[]}
         */
        self.existingImageAction = [];
        /**
         * Code table for Existing primary action dropdown
         * These actions are reserved for what to do with the current primary image
         * if the new image is set to replace the current primary
         * @type {*[]}
         */
        self.existingPrimaryAction = [
            {
                description: 'Inactivate',
                keyword: 'INACT'
            },
            {
                description: 'Reject',
                keyword: 'REJ'
            },
            {
                description: 'Alternate',
                keyword: 'ALT'
            }
        ];
        /**
         * Code table for Existing alternate action dropdown
         * These actions are reserved for what to do with the current alternate image
         * if the new image is set to replace the current alternate
         * @type {*[]}
         */
        self.existingAlternateAction = [
            {
                description: 'Inactivate',
                keyword: 'INACT'
            },
            {
                description: 'Reject',
                keyword: 'REJ'
            },
            {
                description: 'None',
                keyword: null
            }
        ];
        /**
         * Message for no primary or alternate image selected
         * @type {string}
         */
        self.MESSAGE_NO_PRIMARY_OR_ALTERNATE_SELECTED = "Please select \"Set As Primary\" flag or \"Set As Alternate\" flag before uploading image";
        /**
         * Message for no destination selected
         * @type {string}
         */
        self.MESSAGE_NO_DESTINATION_SELECTED = "Please select at least one Destination Domain.";
        /**
         * Message for no confirm before upload images
         * @type {string}
         */
        self.MESSAGE_CONFIRM = "";
        /**
         * The currently selected existing primary header.
         * @type {Object}
         */
        self.existingImageHeader = null;
        /**
         * These variables are parameters for the paginated table, this is the start page and the number of elements per page
         * @type {number}
         */
        self.START_PAGE =1;
        self.PAGE_SIZE =7;

        /**
         * The paramaters that define the table showing the report.
         * @type {ngTableParams}
         */
        self.tableParams = null;

        /**
         * Swatches Category Code
         * @type {string}
         */
        self.CATEGORY_SWATCHES = 'SWAT';

        /**
         * List of valid file types
         * @type {[string,string,string,string,string]}
         */
        self.validFileTypes = ["jpg", "jpeg", "png"];

        /**
         * Initialize the default values for countUploadProcess and totalSuccessfullyUploaded and totalFailedUploaded.
         * @type {number}
         */
        self.countUploadProcess = 0;
        self.totalSuccessfullyUploaded = 0;
        self.totalFailedUploaded = 0;

        /**
         * This message is showed if the image does not meet specifications.
         * @type {string}
         */
        self.imageDoesNotMeetSpecifications = "Does not meet criteria.";

        /**
         * Initialize some const values.
         */
        const EMPTY = "";
        const UPLOAD_SUCCESS_MESSAGE = "Successfully Updated.";
        const UPLOAD_FAILED_MESSAGE = "Image Upload Failed.";
        const UPLOAD_NOT_SUCCESSFUL = "Not Successful.";
        const DOES_NOT_MEET_CRITERIA = "Does not meet criteria.";
        const INVALID_IMAGE_NAME = "Invalid image name, must start with possible UPC.";
        const SUCCESSFUL = "Successful.";

         /**
         * Alternate Images cannot be Show On Site without a Primary Image that is show On Site.
         * @type {string}
         */
        const UPLOAD_ALTERNATE_WITHOUT_PRIMARY_IMAGE = "Alternate Images cannot be \"Show On Site\" without a Primary Image that is \"Show On Site\"";

        var START_INDEX = 0;

        /**
         * Initialize the controller.
         */
        this.$onInit = function () {
            self.currentPage =  $location.url();
            //self.existingImageHeader = self.existingImageAction[0];
            productSellingUnitsApi.getImageCategories(self.loadCategories, self.fetchError);
            productSellingUnitsApi.getImageSources(self.loadSources, self.fetchError);
            productSellingUnitsApi.getImageDestinations(self.loadDestinations, self.fetchError);
            self.addFileCheckToBrowse();
            self.buildTable();
        };

        /**
         * The DownloadButton is enable or not.
         * @return {boolean}
         */
        self.isDownloadButtonDisable = function(){
            if(self.imageData.length === 0){
                return true;
            }
            for(var index = 0; index<self.imageData.length; index++){
                if(self.imageData[index].statusUpload !== undefined){
                    return false;
                }
            }
            return true;
        };

        /**
         * Show message to user when moving another page if the images are still uploading.
         */
        $scope.$on('$stateChangeStart', function (event, toState) {
            self.nextPage = toState.url;
            if(!angular.equals(self.currentPage , toState.url) && self.isUploading && !self.userHasClickedNavigate){
                event.preventDefault();
                $("#confirmToNavigate").modal("show");
            }
        });

        /**
         * This method will signal the api to stop uploading images and navigate to another page that user have choosed.
         */
        self.stopUploadImagesAndNavigate = function(){
            self.userHasClickedNavigate = true;
            self.stopUploads();
            self.removeModalConfirmToNavigate();
            $location.path(self.nextPage);
        };

        /**
         * This method will hide the modal confirmToNavigate and remove its modal-backdrop.
         */
        self.hideModalConfirmToNavigate = function(){
            self.removeModalConfirmToNavigate();
        };

        /**
         * This method will hide the modal confirmToNavigate and remove its modal-backdrop.
         */
        self.removeModalConfirmToNavigate = function(){
            $('#confirmToNavigate').modal('hide');
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        };

        /**
         * This method will navigate to another page that user have choosed.
         */
        self.navigateToAnotherPage = function(){
            self.removeModalConfirmToNavigate();
            $location.path(self.nextPage);
        };

        /**
         * This method to initialize var search and format name of the image.
         */
        self.initializeVarSearchAndFormatImageName = function (imageData) {
            var search = {};
            search.firstSearch = true;
            search.page = 0;
            search.pageSize = 25;
            var upcs = EMPTY;
            if(imageData.name.match(/_/) !== null){
                upcs=Number(imageData.name.split('_')[0]);
            } else{
                upcs=Number(imageData.name.split('.')[0]);
            }
            search.upcs = upcs;
            return search;
        };

        /**
         * This method will create an event listener to the upload button so that when files are added to the input,
         * all of the files are check to see if they are valid
         */
        self.addFileCheckToBrowse = function () {
            var uploadImageData = document.getElementById("selectedFiles");
            var imageTypeCode = EMPTY ;
            self.invalidFileType = true;
            uploadImageData.addEventListener('change', function (e) {
                self.primaryAllChecked=false;
                self.alternateAllChecked=false;
                self.showOnSiteAllChecked=false;
                self.uploadImageName = EMPTY;
                var imageDataOrg = angular.copy(self.imageData);
                angular.forEach(uploadImageData.files, function (imageData) {
                    imageTypeCode = imageData.type.split('/').pop();
                    if (self.validFileTypes.indexOf(imageTypeCode.toLowerCase()) > -1) {
                        self.uploadImageName = imageData.name;
                        self.invalidFileType = false;
                        var reader = new FileReader();
                        reader.onloadend = function () {
                            var imageCandidate={
                                name: imageData.name,
                                size: (imageData.size/1000).toFixed(2) + ' Kb',
                                status: status === EMPTY ? self.determineStatus(imageData.name.split('.')[0]) : status,
                                success: null,
                                setToPrimary: false,
                                existingImage: null,
                                setToAlternate: false,
                                setToShowOnSite: false,
                                destinationDomain: self.destinationDomains,
                                toUpload:false,
                                isUploading:false,
                                data: reader.result.split(',').pop()
                            };
                            self.checkImageUniqueAndResetData(imageCandidate , imageDataOrg);
                        };
                        reader.readAsDataURL(imageData);
                    } else {
                        self.invalidFileType = true;
                    }
                    self.firstSearch=true;
                });
                angular.element(uploadImageData).val(null);
            });
        };

        /**
         * This method checks if the image is unique or not and reset if data is changed.
         */

        self.checkImageUniqueAndResetData = function (imageCandidate , imageDataOrg){
            if(self.imageUnique(imageCandidate)){
                self.imageData.push(imageCandidate);
            }
            if(angular.toJson(self.imageData) !== angular.toJson(imageDataOrg)){  //reset if data is changed
                self.updateImageDataTable();
                var loadFirstTime = true;
                angular.forEach(imageDataOrg, function(image){
                    if(self.isValidToChange(image)){
                        loadFirstTime = false;
                    }
                });
                if(loadFirstTime){
                    //self.existingImageHeader = self.existingImageAction[0];
                }
            }
        };


        /**
         * This method checks if the image is unique based on name and data;
         * @param image
         * @returns {boolean}
         */
        self.imageUnique = function (image) {
            var notFound = true;
            for(var index = 0; index<self.imageData.length; index++){
                if(angular.equals(image.name, self.imageData[index].name) && angular.equals(image.data, self.imageData[index].data)){
                    notFound = false;
                    break;
                }
            }
            return notFound;
        };

        /**
         * This method does a series of regex tests to see if the name of the file is valid
         * @param name
         * @returns {string}
         */
        self.determineStatus= function (name ,size) {
            var status = EMPTY;
            var letters = /[a-zA-Z]/;
            var numberAndSpecialCharacter = /^[0-9_]+$/;
            var legalCharactersAndLetters = /\w/;
            if(name.match(letters) !== null  && name.match(legalCharactersAndLetters) || !name.match(numberAndSpecialCharacter)){
                status = INVALID_IMAGE_NAME;
            }
            return status;
        };

        /**
         * Call to the backend to the list of image categories
         * @param results
         */
        self.loadCategories = function (results) {
            self.imageCategories = results;
        };

        /**
         * Call to the backend to get the list of image sources
         * @param results
         */
        self.loadSources = function (results) {
            self.imageSources = results;
        };

        /**
         * Call to the backend to get the list of image destination domains
         * @param results
         */
        self.loadDestinations = function (results) {
            self.destinationDomains = results;
            self.selectedDestinations = results;
        };

        /**
         * Component will reload the kits data whenever the item is changed in casepack.
         */
        this.$onChanges = function () {
        };

        /**If there is an error this will display the error
         * @param error
         */
        self.fetchError = function (error) {
            self.isWaiting = false;
            self.data = null;
            if (error && error.data) {
                if (error.data.message != null && error.data.message != EMPTY) {
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
         * Sets the error
         * @param error
         */
        self.setError = function (error) {
            self.error = error;
        };

        /**
         * This method handles changes of changing an image candidates 'Set As Primary' status
         * @param index
         */
        self.changePrimary = function (index) {
            self.imageData[index].setToShowOnSite = self.imageData[index].setToPrimary;
            if(!self.imageData[index].setToPrimary){
                self.imageData[index].existingImage=null;
            } else {
                self.imageData[index].existingImage = self.existingPrimaryAction[0];
            }
            if(self.imageData[index].setToPrimary !== self.primaryAllChecked){
                self.primaryAllChecked = self.showOnSiteAllChecked = false;
                if(self.imageData[index].setToPrimary){
                    var allSelectedItems = true;
                    angular.forEach(self.imageData, function(image){
                        if(!image.setToPrimary && self.isValidToChange(image)){
                            allSelectedItems = false;
                        }
                    });
                    self.primaryAllChecked  = self.showOnSiteAllChecked = allSelectedItems;

                }
            }
            if(self.primaryAllChecked){
                self.existingImageAction = self.existingPrimaryAction;
                self.existingImageHeader = self.existingImageAction[0];
            }else{
                self.existingImageHeader = null;
            }
        };
        /**
         * This method handles changes of changing an image candidates 'Show On Site' status
         * @param index
         */
        self.changeShowOnSite = function (index) {
            if(!self.imageData[index].setToShowOnSite){
                self.imageData[index].existingImage = null;
            } else {
                self.imageData[index].existingImage = self.existingAlternateAction[0];
            }
            if(self.imageData[index].setToShowOnSite !== self.showOnSiteAllChecked){
                self.showOnSiteAllChecked = false;
                if(self.imageData[index].setToShowOnSite){
                    var allSelectedItems = true;
                    angular.forEach(self.imageData, function(image){
                        if(!image.setToShowOnSite && self.isValidToChange(image)){
                            allSelectedItems = false;
                        }
                    });
                    self.showOnSiteAllChecked = allSelectedItems;
                }
            }
            if(self.showOnSiteAllChecked){
                self.existingImageAction = self.existingAlternateAction;
                self.existingImageHeader = self.existingImageAction[0];
            }else{
                self.existingImageHeader = null;
            }
        };
        /**
         * This method handles changes of changing an image candidates 'Set As Alternate' status
         * @param index
         */
        self.changeAlternate = function (index) {
            if(self.imageData[index].setToAlternate !== self.alternateAllChecked){
                self.alternateAllChecked = false;
                if(self.imageData[index].setToAlternate){
                    var allSelectedItems = true;
                    angular.forEach(self.imageData, function(image){
                        if(!image.setToAlternate && self.isValidToChange(image)){
                            allSelectedItems = false;
                        }
                    });
                    self.alternateAllChecked = allSelectedItems;
                }
            }
            if(!self.imageData[index].setToAlternate){
                self.imageData[index].setToShowOnSite = false;
                self.showOnSiteAllChecked = self.alternateAllChecked;
                self.imageData[index].existingImage = null;
            }else if(self.imageData[index].setToShowOnSite){
                self.imageData[index].existingImage = self.existingAlternateAction[0];
            }
            if(self.alternateAllChecked &&  self.showOnSiteAllChecked){
                self.existingImageHeader = self.existingImageAction[0];
            }else{
                self.existingImageHeader = null;
            }
        };

        /**
         * This method handles the users request to remove an image form the list
         * @param index
         */
        self.removeImage = function (index) {
            if(angular.equals(self.imageData[index].statusUpload , UPLOAD_SUCCESS_MESSAGE)){
                self.totalSuccessfullyUploaded--;
            }else if(angular.equals(self.imageData[index].statusUpload , UPLOAD_FAILED_MESSAGE)){
                self.totalFailedUploaded--;
            }
            self.imageData.splice(index, 1);
            self.resetImageDataTable();
            if(self.imageData != null && self.imageData.length > 0){
                var imageArray = [];
                angular.forEach(self.imageData, function(image){
                    if(self.isValidToChange(image)){
                        imageArray.push(image);
                    }
                });
                if(imageArray != null && imageArray.length > 0){
                    var allSelectedPrimary = true;
                    var allSelectedAlternate = true;
                    var allSelectedShowOnSite = true;
                    var allSelectedItems = true;
                    angular.forEach(imageArray, function(image){
                        if(!image.setToPrimary){
                            allSelectedPrimary = false;
                        }
                        if(!image.setToAlternate){
                            allSelectedAlternate = false;
                        }
                        if(!image.toUpload){
                            allSelectedItems = false;
                        }
                        if(!image.setToShowOnSite){
                            allSelectedShowOnSite = false;
                        }
                    });
                    self.primaryAllChecked = allSelectedPrimary;
                    self.alternateAllChecked = allSelectedAlternate;
                    self.showOnSiteAllChecked = allSelectedShowOnSite;
                    self.uploadAllChecked = allSelectedItems;
                }
            }else{
                self.primaryAllChecked = false;
                self.uploadAllChecked = false;
                self.alternateAllChecked = false;
                self.showOnSiteAllChecked = false;
                self.existingImageHeader = null;
            }
            self.getImagesUploadedToExport();
        };

        /**
         * This method removes all images from the image candidate table
         */
        self.clearList = function () {
            self.resetCountImages();
            self.imageData = [];
            self.existingImageHeader = null;
            self.updateImageDataTable();
        };

        /**
         * This method makes all the calls to refresh the image candidate table
         */
        self.updateImageDataTable = function () {
            self.uploadAllChecked = false;
            //self.existingImageHeader = self.existingAlternateAction[0];;
            if(self.imageData.length == 0){
                self.primaryAllChecked=false;
                self.showOnSiteAllChecked=false;
                self.alternateAllChecked=false;
            }
            self.tableParams.total(self.imageData.length);
            self.tableParams.count(self.imageData.length);
            self.tableParams.reload();
        };

        /**
         * This method makes all the calls to reset the image candidate table
         */
        self.resetImageDataTable = function () {
            self.tableParams.total(self.imageData.length);
            self.tableParams.count(self.imageData.length);
            self.tableParams.reload();
        };

        /**
         * Reset variables count image.
         */
        self.resetCountImages = function () {
            self.totalCurrentSelectedImages = 0;
            self.countUploadProcess = 0;
            self.totalSuccessfullyUploaded = 0;
            self.totalFailedUploaded = 0;
        };

        /**
         * This method starts the image upload process
         */
        self.startUploads = function () {
            self.imagesUploadedToExport = [];
            self.countUploadProcess = 0;
            self.totalCurrentSelectedImages = _.pluck(_.filter(self.imageData, function(o){ return o.toUpload == true;}),'toUpload').length;
            if(self.validateRequireFields()){
                self.continueUploads = true;
                self.uploadImages(START_INDEX);
            }
            else {
                $('#confirmBeforeUploadModal').modal("show");
            }
        };

        /**
         * This method will signal the api to stop uploading images
         */
        self.stopUploads = function () {
            self.continueUploads = false;
            for (var index = 0; index < self.imageData.length; index++) {
                if(index != self.getCurrentIndex){
                    if(self.imageData[index].toUpload === true){
                        self.totalFailedUploaded++;
                        if(angular.equals(self.imageData[index].statusUpload , UPLOAD_FAILED_MESSAGE)){
                            self.totalFailedUploaded--;
                        }
                        self.imageData[index].statusUpload = UPLOAD_FAILED_MESSAGE;
                        self.imageData[index].isUploading = false;
                        self.imageData[index].toUpload = true;
                        self.imageData[index].status = UPLOAD_NOT_SUCCESSFUL;
                    }
                }
            }
        };

        /**
         * This method tests to see if the user still wants to upload an image, then checks the current image is valid
         * to upload, for that to happen it needs to have an empty status and not already been uploaded
         * @param index
         */
        self.uploadImages = function (index) {
            if(index<self.imageData.length && self.continueUploads){
                self.isUploading = true;
                $rootScope.isStillUploadImage = true;
                if (self.imageData[index].toUpload) {
                    self.imageData[index].status = self.validateImage(self.imageData[index]);
                    if (angular.equals(EMPTY, self.imageData[index].status) && angular.equals(null, self.imageData[index].success)) {
                        var existingImage = null;
                        if (self.imageData[index].existingImage !== null) {
                            existingImage = self.imageData[index].existingImage.keyword;
                        }
                        var upc = EMPTY;
                        if(self.imageData[index].name.match(/_/) !== null){
                            upc=Number(self.imageData[index].name.split('_')[0]);
                        } else{
                            upc=Number(self.imageData[index].name.split('.')[0]);
                        }
                        var imageToUpload = {
                            upc: upc,
                            imageCategoryCode: self.imageCategory.id,
                            imageSourceCode: self.imageSource.id,
                            imageName: self.imageData[index].name,
                            destinationList: self.imageData[index].destinationDomain,
                            imageData: self.imageData[index].data,
                            primary: self.imageData[index].setToPrimary,
                            alternate: self.imageData[index].setToAlternate,
                            showOnSite: self.imageData[index].setToShowOnSite,
                            existingImage: existingImage,
                        };
                        self.imageData[index].isUploading = true;
                        if(angular.equals(self.imageData[index].statusUpload , UPLOAD_FAILED_MESSAGE)){
                            self.totalFailedUploaded--;
                        }
                        self.getCurrentIndex = index;
                        homeApi.search(self.initializeVarSearchAndFormatImageName(self.imageData[index]), function(results){
                            if(results.complete && results.recordCount == 0 ){
                                self.countUploadProcess++;
                                self.totalFailedUploaded++;
                                self.uploadAllChecked = false;
                                self.imageData[index].statusUpload = UPLOAD_FAILED_MESSAGE;
                                self.imageData[index].isUploading = false;
                                self.imageData[index].toUpload = false;
                                self.imageData[index].status = UPLOAD_NOT_SUCCESSFUL;
                                self.uploadImages(++index);
                            }else if(results.complete && results.recordCount > 0){
                                self.uploadSingleImage(imageToUpload, index);
                            }
                        }, function (error) {
                            self.countUploadProcess++;
                            self.totalFailedUploaded++;
                            self.imageData[index].statusUpload = UPLOAD_FAILED_MESSAGE;
                            self.imageData[index].isUploading = false;
                            self.imageData[index].toUpload = true;
                            self.imageData[index].status = UPLOAD_NOT_SUCCESSFUL;
                            self.uploadImages(++index);
                        });
                    } else {
                        self.uploadImages(++index);
                    }
                } else {
                    self.uploadImages(++index);
                }
            } else {
                self.getImagesUploadedToExport();
                $rootScope.isStillUploadImage = false;
                self.isUploading = false;
            }
        };

        /**
         * This method to get the images upload to export.
         */
        self.getImagesUploadedToExport = function(){
            self.imagesUploadedToExport = [];
            for(var m = 0 ; m < self.imageData.length ; m++){
                if(self.imageData[m].statusUpload !== undefined){
                    self.imageUpload = {};
                    self.imageUpload.imageName = self.imageData[m].name;
                    self.imageUpload.imageFileSize = self.imageData[m].size;
                    self.imageUpload.result = self.imageData[m].success !== null ? SUCCESSFUL : "X";
                    self.imageUpload.statusText = self.imageData[m].success !== null ? UPLOAD_SUCCESS_MESSAGE :  self.imageData[m].status;
                    self.imagesUploadedToExport.push(self.imageUpload);
                }
            }
        };

        /**
         * This method to check the image meets specifications or not.
         * @param index
         */
        self.checkTheImageMeetSpecificationsOrNot = function(index){
            var upc = EMPTY ;
            if(self.imageData[index].name.match(/_/) !== null){
                upc=Number(self.imageData[index].name.split('_')[0]);
            } else{
                upc=Number(self.imageData[index].name.split('.')[0]);
            }
            productSellingUnitsApi.checkImageValid({upc : upc} , function (results) {
                if(results.data == true){
                    self.imageData[index].statusUpload = UPLOAD_SUCCESS_MESSAGE;
                    self.totalSuccessfullyUploaded++;
                    self.imageData[index].isUploading = false;
                    self.imageData[index].toUpload = false;
                    self.imageData[index].success = UPLOAD_SUCCESS_MESSAGE;
                    self.uploadAllChecked = false;
                    self.uploadImages(++index);
                }else{
                    self.totalSuccessfullyUploaded++;
                    self.imageData[index].statusUpload = UPLOAD_SUCCESS_MESSAGE;
                    self.imageData[index].isUploading = false;
                    self.imageData[index].toUpload = false;
                    self.imageData[index].status = DOES_NOT_MEET_CRITERIA;
                    self.updateImageDataTable();
                    self.uploadImages(++index);
                }
            });
        };
        /**
         * This method to handle when upload alternate image fail.
         * @param index
         */
        self.uploadAlternateAndShowOnSiteImageFail = function(index){
            self.totalFailedUploaded++;
            self.imageData[index].statusUpload = UPLOAD_FAILED_MESSAGE;
            self.imageData[index].status = UPLOAD_ALTERNATE_WITHOUT_PRIMARY_IMAGE;
            self.imageData[index].isUploading = false;
            self.imageData[index].toUpload = false;
            self.uploadAllChecked = false;
            self.uploadImages(++index);
        };
        /**
         * This method makes the call to the api and handles the response.
         * @param image
         * @param index
         */
        self.uploadSingleImage=function(image, index){
            self.countUploadProcess ++;
            productSellingUnitsApi.uploadImage(image,
                function(results){
                    //In  the case upload alternate and show on site image without primary image
                    if(results.message==""){
                        self.uploadAlternateAndShowOnSiteImageFail(index);
                    }else{
                        self.checkTheImageMeetSpecificationsOrNot(index);
                    }
                },
                function (error) {
                    self.totalFailedUploaded++;
                    self.imageData[index].statusUpload = UPLOAD_FAILED_MESSAGE;
                    self.imageData[index].isUploading = false;
                    if (error && error.data) {
                        if (error.data.message != null && error.data.message != EMPTY) {
                            self.imageData[index].status = error.data.message;
                        } else {
                            self.imageData[index].status = error.data.error;
                        }
                    }
                    else {
                        self.imageData[index].status = "An unknown error occurred.";
                    }
                    self.updateImageDataTable();
                    self.uploadImages(++index);
                });
        };

        /**
         * This method ensures that all of the fields are populated before uploading
         * @param image
         * @returns {string}
         */
        self.validateImage = function (image) {
            var status = EMPTY;
            if(self.imageCategory === null){
                status += "Missing Image Category\n";
            }
            if(self.imageSources === null){
                status += "Missing Image Source\n";
            }
            if(image.setToPrimary){
                if(image.existingImage === null){
                    status += "Missing Existing Primary Action"
                }
            }
            return status;
        };

        /**
         * This method will check all rows
         */
        self.checkAllRows= function (columnNumber) {
            if(columnNumber === 1){
                for(var index=0; index<self.imageData.length; index++){
                    if(self.isValidToChange(self.imageData[index])){
                        self.imageData[index].toUpload=self.uploadAllChecked;
                    }
                }
            }
            //when user check or uncheck on show on site header check box
            else if(columnNumber === 4){
                if(self.showOnSiteAllChecked){
                    self.existingImageAction = self.existingAlternateAction;
                    self.existingImageHeader = self.existingImageAction[0];
                }else{
                    self.existingImageHeader = null;
                }
                for(var index=0; index<self.imageData.length; index++){
                    if(self.isValidToChange(self.imageData[index])) {
                        self.imageData[index].setToShowOnSite = self.showOnSiteAllChecked;
                        if (self.showOnSiteAllChecked && self.alternateAllChecked) {
                            self.imageData[index].existingImage = self.existingImageHeader;
                        } else {
                            self.imageData[index].existingImage = null;
                        }
                    }
                }
            }
            //when user check or uncheck on set as primary header check box
            else if(columnNumber === 5){
                if (self.primaryAllChecked){
                    self.existingImageAction = self.existingPrimaryAction;
                    self.existingImageHeader = self.existingImageAction[0];
                }else{
                    self.existingImageHeader = null;
                }
                self.showOnSiteAllChecked = self.primaryAllChecked;
                for(var index=0; index<self.imageData.length; index++){
                    if(self.isValidToChange(self.imageData[index])) {
                        self.imageData[index].setToPrimary = self.primaryAllChecked;
                        self.imageData[index].setToShowOnSite = self.primaryAllChecked;
                        if (self.primaryAllChecked) {
                            self.imageData[index].existingImage = self.existingImageHeader;
                            self.imageData[index].setToAlternate = false;
                        } else {
                            self.imageData[index].existingImage = null;

                        }
                    }
                }
            }
            //when user check or uncheck on set as alternate header check box
            else if(columnNumber === 6){
                if(!self.alternateAllChecked){
                    self.showOnSiteAllChecked = false;
                    self.existingImageHeader = null;
                }
                for(var index=0; index<self.imageData.length; index++){
                    if(self.isValidToChange(self.imageData[index])) {
                        self.imageData[index].setToAlternate = self.alternateAllChecked;
                        if(!self.alternateAllChecked){
                            self.imageData[index].setToShowOnSite = false;
                            self.imageData[index].existingImage = null;

                        }else{
                            self.imageData[index].setToPrimary = false;
                        }
                    }
                }
            }
            else if(columnNumber === 7){
                for(var index=0; index<self.imageData.length; index++){
                    if(self.isValidToChange(self.imageData[index])) {
                        if (self.imageData[index].setToPrimary || (self.imageData[index].setToAlternate && self.imageData[index].setToShowOnSite)) {
                            self.imageData[index].existingImage = self.existingImageHeader;
                        }
                    }
                }
            }
        };
        /**
         * This method will handle changes to selecting an image to upload
         * @param index
         */
        self.selectImageToUpload=function (index) {
            if(self.imageData[index].toUpload !== self.uploadAllChecked){
                self.uploadAllChecked = false;
                if(self.imageData[index].toUpload){
                    var allSelectedItems = true;
                    angular.forEach(self.imageData, function(image){
                        if(!image.toUpload && self.isValidToChange(image)){
                            allSelectedItems = false;
                        }
                    });
                    self.uploadAllChecked = allSelectedItems;
                }
            }
        };
        /**
         * This method will confirm the image category and source are valid before allowing the user to press upload
         * @returns {boolean}
         */
        self.validToUpload = function () {
            var isDisabled = true;
            if(self.imageSource !== null && self.imageCategory !== null && self.imageData.length > 0){
                isDisabled = false;
            }
            return isDisabled;
        };

        /**
         * Constructs the table that shows the report.
         */
        self.buildTable = function() {
            self.tableParams = new ngTableParams({
                    page:1,
                    count: self.imageData.length,
                }, {
                    counts:[],
                    total: self.imageData.length,
                    getData: function ($defer, params) {
                        self.data = self.imageData;
                        $defer.resolve(self.data);
                    }
                }
            );
        };

        /**
         * This function will take all of the selected images and update their destination domains.
         */
        self.massFill =  function () {
            angular.forEach(self.imageData, function(image){
                if(image.toUpload && self.isValidToChange(image)){
                    image.destinationDomain = self.selectedDestinations
                }
            });
        };

        /**
         * This method will check to see if the image is currently available to make changes to.
         */
        self.isValidToChange = function (image) {
            if(image.status !== EMPTY || image.success != null){
                return false;
            } else {
                return true;
            }
        };

        self.isHeaderEnable = function () {
            for(var index = 0; index<self.imageData.length; index++){
                if(self.imageData[index].statusUpload == undefined  && !angular.equals(self.imageData[index].status ,INVALID_IMAGE_NAME)){
                    return true;
                }
            }
            return false
        };
        self.isHeaderAlternateEnable = function () {
            for(var index = 0; index < self.imageData.length; index++){
                if(self.imageData[index].setToPrimary && self.isValidToChange(self.imageData[index])){
                    return false;
                }
            }
            return true;
        }
        self.isHeaderPrimaryEnable = function () {
            for(var index = 0; index < self.imageData.length; index++){
                if(self.imageData[index].setToAlternate && self.isValidToChange(self.imageData[index])){
                    return false;
                }
            }
            return true;
        };
        /**
         * Check Destination
         *
         * @returns {boolean}
         */
        self.validateDestination = function () {
            var flag = true;
            for (var i = 0; i <self.imageData.length;i++) {
                if (self.imageData[i].destinationDomain.length===0){
                    flag = false;
                    break;
                }
            }
            return flag;
        };

        /**
         * Handle on change category.
         */
        self.onChangeCategory = function () {
            if (self.imageData !== null && self.imageData.length > 0
                && self.imageCategory !== null &&self.imageCategory.id===self.CATEGORY_SWATCHES) {
                self.primaryAllChecked = false;
                angular.forEach(self.imageData, function(image){
                    image.setToPrimary = false;
                });
            }
        };

        /**
         * Check when user not chose destination or alternate or primary flag
         *
         * @returns {boolean}
         */
        self.validateRequireFields = function () {
            var flag = true;
            for (var i = 0; i <self.imageData.length;i++) {
                if (self.imageData[i].toUpload && self.imageData[i].destinationDomain.length===0){
                    flag = false;
                    self.MESSAGE_CONFIRM = self.MESSAGE_NO_DESTINATION_SELECTED;
                    break;
                }
                if (self.imageData[i].toUpload && !self.imageData[i].setToAlternate && !self.imageData[i].setToPrimary){
                    flag = false;
                    self.MESSAGE_CONFIRM = self.MESSAGE_NO_PRIMARY_OR_ALTERNATE_SELECTED;
                    break;
                }
            }
            return flag;
        };


        /**
         * This method will show existing image header dropdown.
         * @returns {boolean}
         */
        self.showExistingImageHeader = function () {
            return (self.imageData != null && self.imageData.length > 0);
        };
    }
})();
