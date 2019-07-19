/*
 *   addNewVariantsComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * variants -> variants Info page component.
 *
 * @author vn87351
 * @since 2.15.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('addNewVariants', {
		// isolated scope binding
		require: {
			productDetail: '^productDetail'
		},
		bindings: {
			listOfProducts: '=',
			productMaster: '<',
			itemMaster: '<',
			variantData:'<',
            romanceCopy: '=',
            displayName: '=',
            size: '=',
            brand: '='

		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/variants/addNew.html',
		// The controller that handles our component logic
		controller: AddNewVariantsController
	});

	AddNewVariantsController.$inject = ['productApi', '$scope', '$timeout', 'ProductFactory','variantsApi','ngTableParams','$rootScope', 'ECommerceViewApi'];


	function AddNewVariantsController(productApi, $scope, $timeout,  productFactory,variantsApi,ngTableParams,$rootScope, eCommerceViewApi) {

		var self = this;
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;

        /**
         * Represents state of Select All check box.
         */
        self.checkAllCheckBox = false;
		/**
		 * message success from api
		 */
		self.success = '';
		/**
		 * message error form api
		 */
		self.error = '';
        /**
		 * Base object for add new a variants.
         * @type {{upc: string, customerFriendlyDescription1: {}, customerFriendlyDescription2: {}, productDescription: string}}
         */
		self.itemAddTemp = {
			upc: '',
			customerFriendlyDescription1: {},
			customerFriendlyDescription2: {},
			productDescription: ''
		};

        /**
		 * The array of value has been added new.
         * @type {Array}
         */
		self.addVariantsData = [];

        /**
		 * Step number handle for add new variants.
         * @type {number}
         */
		self.stepAddNewSelected = 1;

        /**
		 * Number variant has been added one times.
         * @type {number}
         */
		self.numberVariantsAdd = 0;

        /**
		 * Customer friendly description type 1
         * @type {string}
         */
		self.CUSTOMER_FRIENDLY_DESCRIPTION_1='TAG1';
        /**
		 * Customer friendly description type 2
         * @type {string}
         */
		self.CUSTOMER_FRIENDLY_DESCRIPTION_2='TAG2';
        /**
		 *
         * @type {string}
         */
		self.LANGUAGE_TYPE_ENGLISH = "ENG";
        /**
		 * Product type code for variant product.
         * @type {string}
         */
		self.PRODUCT_TYPE_CODE_VARIANT='VARNT';

        /**
		 * Product extension data code for display name field.
         * @type {string}
         */
		const PROD_EXT_DTA_CD_ESHRT= 'ESHRT';
        /**
         * Product extension data code for brand field.
         * @type {string}
         */
        const PROD_EXT_DTA_CD_ELONG= 'ELONG';
        /**
         * Product key code for master data extension table.
         * @type {string}         */

        const PROD_KEY_CD= 'PROD';

        /**
		 * Attribute id for brand field.
         * @type {number}
         */
        const BRAND_ATTRIBUTE_ID = 1642;

        /**
		 * Attribute id for display name field.
         * @type {number}
         */
        const SIZE_ATTRIBUTE_ID = 1636;
        /**
		 * Source system for edit-able
         * @type {number}
         */
        const SOURCE_SYS_4 = 4;

        /**
		 * Default value for customer friendly description.
         * @type {{key: {descriptionType: string, languageType: string}, description: string}}
         */
        const DEFAULT_CUSTOMER_FRIENDLY_DESCRIPTION_1 = {
            key: {
                descriptionType: self.CUSTOMER_FRIENDLY_DESCRIPTION_1,
                languageType: self.LANGUAGE_TYPE_ENGLISH
            },
            description: ''
        };

        /**
		 * Loading variant data table result
         */
		self.loadAddNewTable = function(){
			$scope.addVariantsTableParams = new ngTableParams({
				page: 1,
				count: 20
			}, {
				counts: [],
				data: self.addVariantsData//self.initDataForNgTable()
			});
			$scope.addVariantsTableParams.reload();
		};

        /**
		 * Next handle when add new variant.
         */
		self.addNewVariantStepNext = function(){
			self.error= '';
			if(self.stepAddNewSelected==1){
				if(self.numberVariantsAdd===undefined || parseInt(self.numberVariantsAdd) < 0 || parseInt(self.numberVariantsAdd) > 999){
					self.error = "The number of Variants must be greater than 0 and less than or equal to 999.";
				}
				else if(self.isSameAssortment===undefined || self.isSameAssortment===''){
					self.error = "Please select whether the variants within the assortment share the same UPC or not.";
				}
				else if( self.isSameAssortment!=''){
					if(self.numberVariantsAdd==0){
						self.numberVariantsAdd=5;
					}
					self.createCandidateWorkRequest();
				}
			}else if(!self.validateTab2()){
				self.stepAddNewSelected=3;
			}

		};

        /**
		 * Validation data inputs to add new variant.
         * @returns {boolean}
         */
		self.validateTab2 = function(){
			var invalid = false;
			self.error= '';
			angular.forEach(self.addVariantsData, function(value,key){
				if(self.error===''){
					if(value.upc === undefined || value.upc==null || value.upc ===''){
						self.error = 'Variant UPC is a mandatory field';
						invalid = true;
					}else if(value.productDescription== undefined || value.productDescription===''){
						self.error = 'Product Description is a mandatory field';
						invalid = true;
					}
				}
			});
			return invalid;
		};

        /**
		 * Previous handle, go back step when add new variant.
         */
		self.previous = function(){
			self.error= '';
			self.stepAddNewSelected=1;
			$('#btnstep'+self.stepAddNewSelected).tab('show');
		};

        /**
		 * Generate data for candidate product master basing each variant, that has been added.
          */
		self.generateCandidateProductMaster = function(){
			angular.forEach(self.addVariantsData, function(value,key){
				var psProductMaster = {};
				psProductMaster.productId = self.productMaster.prodId;
				//CFD1, CFD2 field
				psProductMaster.candidateDescriptions = [];
                psProductMaster.candidateScanCodeExtents = [];
                psProductMaster.candidateMasterDataExtensionAttributes = [];
				if(value.customerFriendlyDescription1!==undefined && value.customerFriendlyDescription1!=null
					&& value.customerFriendlyDescription1.description!==undefined && value.customerFriendlyDescription1.description!=='')
					psProductMaster.candidateDescriptions.push(value.customerFriendlyDescription1);
				if(value.customerFriendlyDescription2!==undefined && value.customerFriendlyDescription2!=null
					&& value.customerFriendlyDescription2.description!==undefined && value.customerFriendlyDescription2.description!=='')
					psProductMaster.candidateDescriptions.push(value.customerFriendlyDescription2);
				//Product Description field
				psProductMaster.description = value.productDescription;
                //Romance Copy field
				if(self.romanceCopy){
					//Generate data for romance copy field.
                    var candidateScanCodeExtentsForRomanceKey ={};
                    candidateScanCodeExtentsForRomanceKey.scanCodeId =  0;
                    candidateScanCodeExtentsForRomanceKey.productExtentDataCode = PROD_EXT_DTA_CD_ELONG;
                    var candidateScanCodeExtentsForRomance = {};
                    candidateScanCodeExtentsForRomance.workRequestId = self.candidateWorkRequest.workRequestId;
                    candidateScanCodeExtentsForRomance.productDescription = self.romanceCopy;
                    candidateScanCodeExtentsForRomance.key = candidateScanCodeExtentsForRomanceKey;
                    //Add romance copy data to candidate product master
                    psProductMaster.candidateScanCodeExtents.push(candidateScanCodeExtentsForRomance);
				}
                //Display Name field
                if(self.displayName) {
                    //Generate data for display name field.
                    var candidateScanCodeExtentsForDispNmKey = {};
                    candidateScanCodeExtentsForDispNmKey.scanCodeId = 0;
                    candidateScanCodeExtentsForDispNmKey.productExtentDataCode = PROD_EXT_DTA_CD_ESHRT;
                    var candidateScanCodeExtentsForDispNm = {};
                    candidateScanCodeExtentsForDispNm.workRequestId = self.candidateWorkRequest.workRequestId;
                    candidateScanCodeExtentsForDispNm.productDescription = self.displayName;
                    candidateScanCodeExtentsForDispNm.key = candidateScanCodeExtentsForDispNmKey;
                    //Add display name data to candidate product master
                    psProductMaster.candidateScanCodeExtents.push(candidateScanCodeExtentsForDispNm);
                }
                //Brand field
                if(self.brand) {
                    //Generate data for brand field.
                    var brandDataExtensionAttributes = {};
                    var brandKey = {};
                    brandKey.workRequestId = self.candidateWorkRequest.workRequestId;
                    brandKey.attributeId = BRAND_ATTRIBUTE_ID;
                    brandKey.itemProductKey = PROD_KEY_CD;
                    brandKey.dataSourceSystem = SOURCE_SYS_4;
                    brandDataExtensionAttributes.key = brandKey;
                    brandDataExtensionAttributes.attributeValueText = self.brand;
                    //Add brand data to candidate product master
                    psProductMaster.candidateMasterDataExtensionAttributes.push(brandDataExtensionAttributes);
                }
                //Size field
                if(self.size) {
                    //Generate data for size field.
                    var sizeDataExtensionAttributes = {};
                    var sizeKey = {};
                    sizeKey.workRequestId = self.candidateWorkRequest.workRequestId;
                    sizeKey.attributeId = SIZE_ATTRIBUTE_ID;
                    sizeKey.itemProductKey = PROD_KEY_CD;
                    sizeKey.dataSourceSystem = SOURCE_SYS_4;
                    sizeDataExtensionAttributes.key = sizeKey;
                    sizeDataExtensionAttributes.attributeValueText = self.size;
					//Add size data to candidate product master
                    psProductMaster.candidateMasterDataExtensionAttributes.push(sizeDataExtensionAttributes);
                }
				//Add ps product master
                psProductMaster.productTypeCode=self.PRODUCT_TYPE_CODE_VARIANT;
                psProductMaster.productScanCodeVariant = parseInt(value.upc.upc);
                self.candidateWorkRequest.candidateProductMaster.push(psProductMaster);
			});
			self.isWaiting = true;
			variantsApi.updateCandidateWorkRequestVariants(self.candidateWorkRequest,function(response){
					self.isWaiting = false;
					self.closeAddNewForm('Variant product successfully added.');
				},
				self.callApiErrorAddForm
			);
		};

        /**
		 * Create candidate work request when start add new variant.
         */
		self.createCandidateWorkRequest = function(){
			self.isWaiting = true;
			variantsApi.createCandidateWorkRequestVariants({
					productId:self.productMaster.prodId
				},function(response){
					self.isWaiting = false;
					self.candidateWorkRequest = response;
					self.addNewVariantStep1();
					self.stepAddNewSelected=2;
					$('#btnstep'+self.stepAddNewSelected).tab('show');
				},
				self.callApiErrorAddForm
			);
		};

		/**
		 * call api error and throw message
		 * @param error
		 */
		self.callApiErrorAddForm = function(error){
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
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
		 * add new variant
		 */
		self.addNewVariantStep1 = function () {
			self.addVariantsData = [];
			for(var i = 0 ; i<self.numberVariantsAdd;i++){
				var psProduct = angular.copy(self.itemAddTemp);
				psProduct.productDescription = self.productMaster.description;
				var customerFriendlyDescription1 = null;
				angular.forEach(self.productMaster.productDescriptions, function (value, key) {
					if (value.key.descriptionType === self.CUSTOMER_FRIENDLY_DESCRIPTION_1) {
                        customerFriendlyDescription1 = angular.copy(value);
					}
					var cdf2= {
						key: {
							descriptionType:self.CUSTOMER_FRIENDLY_DESCRIPTION_2,
							languageType:self.LANGUAGE_TYPE_ENGLISH
						},
						description:''
					};
					psProduct.customerFriendlyDescription2 = angular.copy(cdf2);

				});
				if (customerFriendlyDescription1 == null){
                    customerFriendlyDescription1 = DEFAULT_CUSTOMER_FRIENDLY_DESCRIPTION_1;
				}
				psProduct.customerFriendlyDescription1 = customerFriendlyDescription1;
				if (self.isSameAssortment === 'Y') {
					psProduct.upc = self.itemMaster.primaryUpc;
				}
				self.addVariantsData.push(psProduct);
			}
			self.loadAddNewTable();
		};

		/**
		 * close the new product popup
		 */
		self.closeAddNewForm = function(mess){
			$rootScope.$broadcast('closeAddNewPopup',mess);
		};

		/**
		 * delete item on add new
		 */
		self.deleteItem = function(){
			var tmpData = [];
			angular.forEach(self.addVariantsData, function(value,key){
				if(value.selected===undefined || !value.selected){
					tmpData.push(value);
				}
			});
			self.addVariantsData = tmpData;
			self.loadAddNewTable();
		};

		/**
		 * add new item
		 */
		self.addNewItem = function(){
			var psProduct = angular.copy(self.itemAddTemp);
			psProduct.productDescription = self.productMaster.description;
			var customerFriendlyDescription1 = null;
			angular.forEach(self.productMaster.productDescriptions, function (value, key) {
				if (value.key.descriptionType === self.CUSTOMER_FRIENDLY_DESCRIPTION_1) {
					psProduct.customerFriendlyDescription1 = angular.copy(value);
				}
				var cdf2= {
					key: {
						descriptionType:self.CUSTOMER_FRIENDLY_DESCRIPTION_2,
						languageType:self.LANGUAGE_TYPE_ENGLISH
					},
					description:''
				};
				psProduct.customerFriendlyDescription2 = angular.copy(cdf2);

            });
            if (customerFriendlyDescription1 == null){
                customerFriendlyDescription1 = DEFAULT_CUSTOMER_FRIENDLY_DESCRIPTION_1;
            }
            psProduct.customerFriendlyDescription1 = customerFriendlyDescription1;
			if (self.isSameAssortment === 'Y') {
				psProduct.upc = self.itemMaster.primaryUpc;
			}
			self.addVariantsData.push(psProduct);
			self.loadAddNewTable();
		};
	}
})();