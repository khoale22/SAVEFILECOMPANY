/**
 * This is where all the logic for the Special Attribute Component
 * @author s753601
 * @version 2.12.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('productSpecialAttribute', {
		// isolated scope binding
		bindings: {
			productMaster: '<'
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/specialAttribute.html',
		// The controller that handles our component logic
		controller: ProductSpecialAttributeController
	});

	ProductSpecialAttributeController.$inject = ['productApi', 'UserApi','$rootScope', 'ProductDetailAuditModal',
		'ProductSearchService', '$filter'];

	/**
	 * Product shelf attributes component's controller definition.
	 * @constructor
	 */
	function ProductSpecialAttributeController(productApi, userApi, $rootScope, ProductDetailAuditModal,
											   productSearchService, $filter) {

		/**
		 * The constant for pharmacy tab.
		 * @type {string}
		 */
		var pharmacyTab= "pharmacy";

		/**
		 * The constant for tobacco tab.
		 * @type {string}
		 */
		var tobaccoTab = "tobacco";

		/**
		 * The constant for code date tab.
		 * @type {string}
		 */
		var codeDateTab = "codeDate";

		/**
		 * The constant for shipping and handling tab.
		 * @type {string}
		 */
		var shippingHandlingTab = "shippingHandling";

		/**
		 * The constant for rating/restrictions tab.
		 * @type {string}
		 */
		var ratingRestrictionsTab = "rating/restrictions";

		/** All CRUD operation controls of Case pack Import page goes here */
		var self = this;
		/**
		 * Constant for the pharmacy tab.
		 * @type {string}
		 */
		var pharmacy = "pharmacy";
		/**
		 * Constant for the code Date tab.
		 * @type {string}
		 */
		var codeDate = "codeDate";
		/**
		 * Constant for the rating or restrictions tab.
		 * @type {string}
		 */
		var ratingOrRestrictions = "rating/restrictions";
		/**
		 * Constant for the rating or restrictions tab.
		 * @type {string}
		 */
		var tobacco = "tobacco";

		var shippingHandling = "shippingHandling";

		var MISSING_TOBACCO_PRODUCT_TYPE = "Missing Tobacco Product Type";

		/**
		 * Determines which tab is currently selected.
		 * @type {null}
		 */
		self.currentTab = pharmacyTab;
		/**
		 * Error message
		 * @type {null}
		 */
		self.error=null;
		/**
		 * Success message
		 * @type {null}
		 */
		self.success=null;
		/**
		 * Flag to display spinner while loading data
		 * @type {boolean}
		 */
		self.isLoading = false;

		/**
		 * These flags are whichever component is currently being loaded in.
		 * @type {boolean}
		 */
		self.isLoadingPharmacy=false;
		self.isLoadingRestriction=false;
		self.isLoadingRestrictionGroups=false;
		self.isLoadingShippingRestriction=false;
		/**
		 * Array to hold all of the drug schedule types
		 * @type {Array}
		 */
		self.drugSchedule=[];

		/**
		 * The complete set of restrictions
		 * @type {Array}
		 */
		self.restrictions=[];

		/**
		 * The filtered set of restrictions based on the selected restriction group
		 * @type {Array}
		 */
		self.restrictionFilter=[];

		/**
		 * The currently selected restriction
		 * @type {null}
		 */
		self.restriction=null;

		/**
		 * The set of restriction types
		 * @type {Array}
		 */
		self.restrictionGroups = [];

		/**
		 * The currently selected restriction type
		 * @type {null}
		 */
		self.restrictionGroup=null;
		/**
		 * The current set of values available to the pharmacy
		 * @type {null}
		 */
		self.currentRx=null;
		/**
		 * The original set of values available to the pharmacy
		 * @type {null}
		 */
		self.originalRx=null;
		/**
		 * Determines which tab is currently selected.
		 * @type {null}
		 */
		self.currentTab = pharmacy;
		/**
		 * The types of tobacco products available
		 * @type {Array}
		 */
		self.tobaccoProductTypes=[];
		/**
		 * This is the original tobacco product if there is one
		 * @type {null}
		 */
		self.originalTobaccoProduct=null;
		/**
		 * This is the current tobacco product
		 * @type {null}
		 */
		self.currentTobaccoProduct=null;
		/**
		 * This is the default class code for tobacco products this is check to see if the new tobacco product is
		 * part of the tobacco commodity class
		 * @type {number}
		 */
		self.tobaccoDefault=62;
		/**
		 * This is the default class code for pharmacy products this is check to see if the new pharmacy product is
		 * part of the pharmacy commodity class
		 * @type {number}
		 */
		self.pharmacyDefault=[32, 34, 68, 69];
		/**
		 * The current list of restrictions on the product
		 * @type {null}
		 */
		self.currentRestrictions=null;
		/**
		 * The original list of restrictions on a product
		 * @type {null}
		 */
		self.originalRestrictions=null;
		/**
		 * The currently selected restriction group to possibly add to the product
		 * @type {null}
		 */
		self.restrictionGroup=null;
		/**
		 * The currently selected restriction to possibly add to the product
		 * @type {null}
		 */
		self.restriction=null;
		/**
		 * A list of indexes to delete from the product's restrictions list
		 * @type {Array}
		 */
		self.deleteList=[];
		/**
		 * This flag is set for when all of the checkboxes in the table are checked.
		 * @type {boolean}
		 */
		self.allChecked=false;

		/**
		 * Hold the data for all shipping restrictions available.
		 *
		 * @type {Array}
		 */
		self.allShippingRestrictionsList = [];

		/**
		 * Keep track of all shipping restrictions that have been checked in UI.
		 *
		 * @type {boolean}
		 */
		self.allShippingRestrictionChecked = false;

		/**
		 * Keep track of all shipping methods checked in the UI.
		 *
		 * @type {boolean}
		 */
		self.allShippingMethodChecked = false;

		/**
		 * Keep track of all state warnings checked in the UI.
		 *
		 * @type {boolean}
		 */
		self.allStateWarningsChecked = false;

		/**
		 * Holds current products shipping restrictions.
		 *
		 * @type {null}
		 */
		self.productShippingRestrictions = null;

		/**
		 * Modified returned shipping restrictions.
		 *
		 * @type {Array}
		 */
		self.returnProductShippingRestrictions = [];

		/**
		 * Modified returned shipping methods.
		 *
		 * @type {Array}
		 */
		self.returnShippingMethods = [];

		/**
		 * Modified retunre state warnings.
		 *
		 * @type {Array}
		 */
		self.returnStateWarnings = [];

		/**
		 * RegExp check number is decimal type (#####.####), and max value is 99999.9999
		 * @type {{}}
		 */
		self.regexDecimal = new RegExp("^[0-9]{1,5}\.?([0-9]{1,4})?$");

		const NO_CHANGE_DETECTED_MSG = "No changes detected.";

		/**
		 * Whenever the view is initialized.
		 */
		this.$onInit =function () {
			productApi.getTobaccoType(self.loadTobaccoType, self.fetchError);
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		};

		/**
		 * Whenever the current page makes a change.
		 */
		this.$onChanges = function () {
			self.isLoading=true;
			self.success = null;
			self.isLoadingPharmacy=true;
			self.isLoadingRestriction=true;
			self.isLoadingShippingRestriction=true;
			self.isLoadingRestrictionGroups=true;
			self.currentRx=null;
			self.originalRx=null;
			self.originalCodeDate=null;
			self.currentCodeDate = null;
			self.currentTobaccoProduct=null;
			self.originalTobaccoProduct=null;
			self.originalRestrictions=null;
			self.currentRestrictions=null;
			self.originalShipping=null;
			self.currentShipping=null;
			self.loadApiCallData();
		};

		/**
		 * Loads all of the api calls that are fetching for data.
		 */
		self.loadApiCallData = function() {
			productApi.getDrugSchedule(self.loadPharmacyData, self.fetchError);
			productApi.getRestrictionGroups(self.loadRestrictionGroup, self.fetchError);
			productApi.getRestrictions(self.loadRestriction, self.fetchError);
			productApi.getProductShippingRestrictions({prodId: self.productMaster.prodId} , self.loadProductShippingRestrictions, self.fetchError);
			productApi.getAllShippingRestrictions(self.loadShippingRestrictions, self.fetchError);
			productApi.getSalesRestrictionsBySubCommodity({subcomcd: self.productMaster.subCommodityCode} , self.loadSalesRestrictionsBySubCommodity, self.fetchError);
			productApi.getShippingMethodExceptions({prodId: self.productMaster.prodId}, self.loadShippingMethodExceptions, self.fetchError);
			productApi.getAllShippingMethodExceptions(self.loadAllShippingMethodRestrictions, self.fetchError);
			productApi.getStateWarnings({prodId: self.productMaster.prodId}, self.loadProductStateWarnings, self.fetchError);
			productApi.getAllStateWarnings(self.loadAllStateWarnings, self.fetchError);
			productApi.getStateWarningBySubcommodity({subcomcd: self.productMaster.subCommodityCode} , self.loadStateWarningBySubcommodity, self.fetchError);
			self.createCurrentShipping();
			self.createCurrentCodeDate();
			self.createCurrentTobaccoProduct();
			self.createCurrentRestrictions();
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
		};

		/**
		 * This method is called when the UI successfully gets All the State Warnings.
		 *
		 * @param results
		 */
		self.loadAllStateWarnings = function (results) {
			self.allStateWarnings = results;
		};

		/**
		 * This method is called when the UI successfully gets Product State Warnings.
		 *
		 * @param results
		 */
		self.loadProductStateWarnings = function (results) {
			self.productStateWarnings = results;
		};

		/**
		 * This method is called when the UI successfully gets Product Shipping Restrictions.
		 *
		 * @param results
		 */
		self.loadProductShippingRestrictions = function (results) {
			self.productShippingRestrictions = results;
		};

		/**
		 * This method is called when the UI successfully get sales restrictions by sub commodity.
		 *
		 * @param results
		 */
		self.loadSalesRestrictionsBySubCommodity = function (results) {
			self.listSalesRestrictionsBySubCommodity = results;
		};

		/**
		 * This method is called when the UI successfully get state warning by sub commodity.
		 *
		 * @param results
		 */
		self.loadStateWarningBySubcommodity = function (results) {
			self.listStateWarningBySubcommodity = results;
		};

		/**
		 * This method is called when the UI successfully gets Shipping Method Exceptions.
		 *
		 * @param results
		 */
		self.loadShippingMethodExceptions = function (results) {
			self.shippingMethodExceptions = results;
		};

		/**
		 * This method is called when the UI successfully gets the tobacco product type code table
		 * @param results
		 */
		self.loadTobaccoType=function (results) {
			self.tobaccoProductTypes=results;
		};

		self.updateLoadingStatus=function () {
			self.isLoading=(self.isLoadingPharmacy || self.isLoadingRestrictionGroups || self.isLoadingRestriction);
		};

		/**
		 * After updating the database get the latest ItemMaster
		 * @param results
		 */
		self.loadPharmacyData = function (results) {
			self.isLoading=false;
			self.isLoadingPharmacy=false;
			self.drugSchedule = [];
			if(results != null) {
				angular.forEach(results, function(item){
					if(self.isNullOrEmptyOrBlank(item.description) == false){
						if(item.description.trim() === 'DEFAULT'){
							item.description = "--Select--";
						}
						item.description = item.description.trim()
						item.id = item.id.trim();
						self.drugSchedule.push(item);
					}
				});
			}
			
			self.currentRx = {};
			self.currentRx.id = self.productMaster.prodId;
			self.currentRx.sellingUnits = self.productMaster.sellingUnits;
			if(self.productMaster.goodsProduct !== null){
				self.currentRx.goodsProduct = {
						pseDisplay: self.productMaster.goodsProduct.psedisplay,
						pseTypeCode: self.productMaster.goodsProduct.pseTypeCode.trim() || null,
						rxProductFlag: self.productMaster.goodsProduct.rxProductFlag,
						medicaidCode: self.productMaster.goodsProduct.rxProductFlag?self.productMaster.goodsProduct.medicaidCode.trim() || null : "",
						medicaidDisplay: self.productMaster.goodsProduct.medicaidDisplay
				};
				if(self.productMaster.goodsProduct.rxProduct !== null || undefined){
					self.currentRx.goodsProduct.rxProduct = {
						drugScheduleTypeCode: self.productMaster.goodsProduct.rxProduct.drugScheduleTypeCode.trim() || null,
						drugScheduleDisplay: self.productMaster.goodsProduct.rxProduct.drugScheduleType.description.trim() || null,
						ndc: self.productMaster.goodsProduct.rxProduct.ndc.trim() || null
					};
				} else {
					self.currentRx.goodsProduct.rxProduct = {};
				}
			}else{
				self.currentRx.goodsProduct = null;
			}
			
			self.originalRx = angular.copy(self.currentRx);
			self.updateLoadingStatus();
		};

		/**
		 * Loads the restriction group.
		 * @param results
		 */
		self.loadRestrictionGroup = function (results) {
			self.restrictionGroups=results;
			self.isLoadingRestrictionGroups= false;
			self.updateLoadingStatus();
		};

		self.loadAllShippingMethodRestrictions = function (results) {
			self.allShippingMethodRestrictions = results;
		};

		self.loadShippingRestrictions = function (results) {
			self.allShippingRestrictions = results;
			self.isLoadingShippingRestriction = false;
			self.updateLoadingStatus();
		};

		/**
		 * Loads the restriction.
		 * @param results
		 */
		self.loadRestriction = function (results) {
			self.restrictions=results;
			self.isLoadingRestriction= false;
			self.updateLoadingStatus();
		};

		/**
		 * Fetches the error from the back end.
		 *
		 * @param error
		 */
		self.fetchError = function(error) {
			self.isLoading = false;
			if(error && error.data) {
				self.isLoading = false;
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
		 *
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.error = error;
		};

		/**
		 * This sorts the rating types.
		 * @param index
		 */
		self.ratingTypeSelected = function (index) {
			self.restrictionFilter = [];
			if(self.restrictionGroup != null) {
				angular.forEach(self.restrictions, function (key, value) {
					if (angular.equals(key.restrictionGroupCode, self.restrictionGroup)){
						self.restrictionFilter.push(key);
					}
				});
			}
		};

		/**
		 * Checks to see if there were any changes made.
		 * @returns {boolean}
		 */
		self.isDifference = function(tab) {
			if (tab === pharmacy) {
				if(angular.toJson(self.originalRx) !== angular.toJson(self.currentRx)){
					$rootScope.contentChangedFlag = true;
					return true;
				} else{
					$rootScope.contentChangedFlag = false;
					return false;
				}
			} else if(tab === codeDate) {
				if(angular.toJson(self.originalCodeDate) !== angular.toJson(self.currentCodeDate)){
					$rootScope.contentChangedFlag = true;
					return true;
				} else{
					$rootScope.contentChangedFlag = false;
					return false;
				}
			} else if(tab === tobacco) {
				if(angular.toJson(self.originalTobaccoProduct) !== angular.toJson(self.currentTobaccoProduct)){
					$rootScope.contentChangedFlag = true;
					return true;
				} else{
					$rootScope.contentChangedFlag = false;
					return false;
				}
			} else if(tab === ratingOrRestrictions) {
				if(angular.toJson(self.originalRestrictions) !== angular.toJson(self.currentRestrictions)){
					$rootScope.contentChangedFlag = true;
					return true;
				} else{
					$rootScope.contentChangedFlag = false;
					return false;
				}
			} else if(tab === shippingHandling) {
				if(angular.toJson(self.originalShipping) !== angular.toJson(self.currentShipping)){
					$rootScope.contentChangedFlag = true;
					return true;
				} else{
					$rootScope.contentChangedFlag = false;
					return false;
				}
			}
		};

		/**
		 * Resets all of the values back to the original.
		 */
		self.reset = function(tab) {
			self.success = null;
			self.error = null;
			if(tab === pharmacy) {
				self.currentRx = angular.copy(self.originalRx);
			} else if(tab === codeDate) {
				self.currentCodeDate = angular.copy(self.originalCodeDate);
				self.resetAllCss();
			} else if(tab === tobacco) {
				self.currentTobaccoProduct = angular.copy(self.originalTobaccoProduct);
			}else if(tab === ratingOrRestrictions) {
				self.currentRestrictions = angular.copy(self.originalRestrictions);
			}else if(tab === shippingHandling) {
				self.currentShipping = angular.copy(self.originalShipping);
			}
		};

		/**
		 * Saves any changes to the rx product.
		 */
		self.saveChanges = function(tab) {
			self.isLoading = true;
			self.success = null;
			self.error = null;
			if(tab === pharmacy) {
				// This will check for any changes and saves the changes to a temporary RX object to send to the back end.
				// If it hasn't been changed then the field will be null.
				var tempRx = self.getProductMasterChanges(self.originalRx, self.currentRx);
				tempRx.prodId = self.currentRx.id;
				if (self.checkCurrentPharmacy(tempRx)){
					productApi.savePharmacyChanges(tempRx, self.reloadSavedData, self.fetchError);
				}else{
					self.isLoading = false;
				}
			}

			if(tab === codeDate) {
				// This will check for any changes and saves the changes to a temporary code date object to send to the
				// back end. If it hasn't been changed then it will be null in the back end.
				self.error = null;
				var tempCodeDate = self.getProductMasterChanges(self.originalCodeDate, self.currentCodeDate);
				tempCodeDate.prodId = self.currentCodeDate.prodId;
				if (self.currentCodeDate.goodsProduct !== null
						&& self.currentCodeDate.goodsProduct.codeDate){
					if (self.checkCurrentCodeDate(self.currentCodeDate)){
						productApi.saveCodeDateChanges(tempCodeDate, self.reloadSavedData, self.fetchError);
					}else {
						self.isLoading = false;
					}
				}else {
					productApi.saveCodeDateChanges(tempCodeDate, self.reloadSavedData, self.fetchError);
				}
			}

			if(tab === shippingHandling) {

				var tempShippingHandling = self.getProductMasterChanges(self.originalShipping, self.currentShipping);
				tempShippingHandling.prodId = self.currentShipping.prodId;
				productApi.updateShippingHandlingChanges(tempShippingHandling, self.reloadSavedData, self.fetchError);
			}

			if(tab === tobacco) {
				// This will check for any changes and saves the changes to a temporary code date object to send to the
				// back end. If it hasn't been changed then it will be null in the back end.
				var tempTobacco = self.getProductMasterChanges(self.originalTobaccoProduct, self.currentTobaccoProduct);
				tempTobacco.prodId = self.currentTobaccoProduct.prodId;
				if(tempTobacco.goodsProduct !== null
						&& (tempTobacco.goodsProduct.tobaccoProduct === undefined 
						|| angular.equals("", tempTobacco.goodsProduct.tobaccoProduct.tobaccoProductTypeCode))){
					self.error = MISSING_TOBACCO_PRODUCT_TYPE;
					self.isLoading=false;
				} else {
					productApi.saveTobaccoChanges(tempTobacco, self.reloadSavedData, self.fetchError);
				}
			}

			if(tab === ratingOrRestrictions){
				var ratingChanges = self.getRestrictionDifference();
				productApi.saveRatingRestrictionsChanges(ratingChanges, self.reloadSavedData, self.fetchError);
			}
		};



		/**
		 * This recursively traverses through an object that may contain lists or other objects inside of it. This will
		 * check each field inside of each object or list to determine whether or not that field has changed. If it has changed,
		 * it saves it to a temporary and then the temporary will be returned.
		 *
		 * @param original
		 * @param current
		 * @returns {{}}
		 */
		self.getProductMasterChanges = function(original, current) {
			var temp = {};
			for (var key in current) {
				if (!current.hasOwnProperty(key)) continue;

				if(angular.toJson(current[key]) !== angular.toJson(original[key])) {
					// It is in a list. This will look through a list of things to check whether any of them have
					// changed.
					if(self.isInt(key)) {
						if(!Array.isArray(temp)) {
							temp = [];
						}
						temp[key] = self.getProductMasterChanges(original[key], current[key]);
						temp[key].upc = current[key].upc;
					} else if(key !== "sellingUnits" && key !== "goodsProduct" && key !== "rxProduct") {
						// If the object is not a sellingUnit, goodsProduct or an rxProduct object and it has been changed
						// then save it into the temporary from the current.
						temp[key] = current[key];
					} else if(key === "sellingUnits" || key === "goodsProduct" || key === "rxProduct") {
						// If it is a sellingUnits List, goodsProduct object or an rxProduct. Go inside and check each
						// object to check and see if any of it has changed.
						temp[key] = self.getProductMasterChanges(original[key], current[key]);
					}
				}
			}
			return temp;
		};

		/**
		 * Whether or not the value is an integer. This determines whether or not it is in a key.
		 * @param value
		 * @returns {boolean}
		 */
		self.isInt = function(value) {
			var x = parseFloat(value);
			return !isNaN(value) && (x | 0) === x;
		};

		/**
		 * This reloads any changes that might have happened.
		 * @param results
		 */
		self.reloadSavedData = function(results) {
			// On an update, copy everything from the updated product master
			// and put it into the current product master.
			for(var i in results.data) {
				self.productMaster[i] = results.data[i];
			}
			self.success = results.message;
			self.loadApiCallData();
		};

		/**
		 * Determines which tab you are currently on.
		 * @param tab
		 */
		self.chooseTab = function(tab) {
			self.reset(self.currentTab);
			self.currentTab = tab;
		};

		/**
		 * Creates a current Code Date.
		 */
		self.createCurrentCodeDate = function() {
			if (self.productMaster.goodsProduct !== null
					&& self.productMaster.goodsProduct.codeDate){
				self.currentCodeDate = {
					prodId: self.productMaster.prodId,
					goodsProduct: {
						prodId: self.productMaster.goodsProduct.prodId,
						codeDate: self.productMaster.goodsProduct.codeDate,
						maxShelfLifeDays: self.productMaster.goodsProduct.maxShelfLifeDays,
						inboundSpecificationDays: self.productMaster.goodsProduct.inboundSpecificationDays,
						warehouseReactionDays: self.productMaster.goodsProduct.warehouseReactionDays,
						guaranteeToStoreDays: self.productMaster.goodsProduct.guaranteeToStoreDays,
						sendCodeDate: self.productMaster.goodsProduct.sendCodeDate
					}
				};
			}else {
				self.currentCodeDate = {};
				self.currentCodeDate.prodId = self.productMaster.prodId;
				if(self.productMaster.goodsProduct !== null){
					self.currentCodeDate.goodsProduct = {
							prodId: self.productMaster.goodsProduct.prodId,
							codeDate: self.productMaster.goodsProduct.codeDate,
							maxShelfLifeDays: null,
							inboundSpecificationDays: null,
							warehouseReactionDays: null,
							guaranteeToStoreDays: null,
							sendCodeDate: null
					};
				}else{
					self.currentCodeDate.goodsProduct = null;
				}
			}
			self.originalCodeDate = angular.copy(self.currentCodeDate);
		};

		/**
		 * Create object to hold the current shipping data.
		 *
		 */
		self.createCurrentShipping = function () {
			self.currentShipping = {};
			self.currentShipping.prodId = self.productMaster.prodId;
			if(self.productMaster.goodsProduct !== null){
				self.currentShipping.goodsProduct = {
						prodId: self.productMaster.goodsProduct.prodId,
						fragile: self.productMaster.goodsProduct.fragile,
						hazardType: self.productMaster.goodsProduct.hazardType,
						ormd: self.productMaster.goodsProduct.ormd,
						shipByItself: self.productMaster.goodsProduct.shipByItself
				};
			}else{
				self.currentShipping.goodsProduct = null;
			}
			self.originalShipping = angular.copy(self.currentShipping);
		};

		/**
		 * This method creates the current tobacco product if the product is already a tobacco product
		 */
		self.createCurrentTobaccoProduct = function () {
			if(self.productMaster.goodsProduct !== null
					&& self.productMaster.goodsProduct.tobaccoProductSwitch){
				var params = {
					prodId: self.productMaster.prodId
				};
				productApi.getTobaccoProduct(params,
					function(results) {
						self.currentTobaccoProduct = {
							prodId: results.goodsProduct.prodId,
							goodsProduct: {
								prodId: results.goodsProduct.prodId,
								tobaccoProductSwitch: results.goodsProduct.tobaccoProductSwitch,
								tobaccoProduct: {
									prodId: results.goodsProduct.prodId,
									tobaccoProductTypeCode: results.tobaccoProductType.tobaccoProductTypeCode,
									tobaccoProductType: results.tobaccoProductType
								}
							}
						};
						self.originalTobaccoProduct = angular.copy(self.currentTobaccoProduct);
					},
					self.fetchError);
			} else {
				self.currentTobaccoProduct = {};
				self.currentTobaccoProduct.prodId = self.productMaster.prodId;
				if(self.productMaster.goodsProduct !== null){
					self.currentTobaccoProduct.goodsProduct = {
							prodId: self.productMaster.goodsProduct.prodId,
							tobaccoProductSwitch: self.productMaster.goodsProduct.tobaccoProductSwitch,
							tobaccoProduct: {
								prodId: self.productMaster.goodsProduct.prodId,
								tobaccoProductTypeCode: null,
								tobaccoProductType: null
							}
					};
				}else{
					self.currentTobaccoProduct.goodsProduct = null;
				}
			}
			self.originalTobaccoProduct = angular.copy(self.currentTobaccoProduct);
		};

		/**
		 * This function will either set the default tobacco product type when sent to true or null the value when set to
		 * false
		 * @param flag
		 */
		self.changeTobaccoProduct= function (flag) {
			if(self.currentTobaccoProduct.goodsProduct !== null){
				if(flag){
					self.currentTobaccoProduct.goodsProduct.tobaccoProduct.tobaccoProductTypeCode="";
				} else {
					self.currentTobaccoProduct.goodsProduct.tobaccoProduct.tobaccoProductTypeCode=null;
				}
			}
		};

		/**
		 * This method is used to check if the current product is part of the tobacco commodity class
		 * @param differentThanDefault
		 * @returns {*}
		 */
		self.differentThanDefault=function (differentThanDefault) {
			if(self.currentTobaccoProduct !== null
					&& self.currentTobaccoProduct.goodsProduct !== null){
				if(self.currentTobaccoProduct.goodsProduct.tobaccoProductSwitch){
					return  (self.currentTobaccoProduct.goodsProduct.tobaccoProductSwitch && self.productMaster.classCode !== differentThanDefault);
				}
			}
			return false;
		};

		/**
		 * This method is used to check if the current product is part of the pharmacy commodity class
		 * @returns {*}
		 */
		self.differentProductDefault=function () {
			if(self.currentRx !== null 
					&& self.currentRx.goodsProduct !== null 
					&& self.currentRx.goodsProduct.rxProductFlag === true 
					&& self.pharmacyDefault.indexOf(self.productMaster.classCode)){
				return  true;
			}
			return false;
		};

		/**
		 * This method is used to check if the current shipping restriction is different with default shipping restriction.
		 */
		self.differentThanDefaultByShippingRestriction=function () {
			var currentSalesRestrictionList = self.getCurrentSalesRestrictionList(self.productShippingRestrictions);
			if(currentSalesRestrictionList && (currentSalesRestrictionList.length > 0) && self.listSalesRestrictionsBySubCommodity){
				var diffArray = this.getDifferenceBetweenTwoArrays(currentSalesRestrictionList, self.listSalesRestrictionsBySubCommodity);
				if(diffArray != null && diffArray.length > 0){
					return true;
				}
			}
			return false;
		};

		/**
		 * This method is used to check if the current state warning is different with default state warning.
		 */
		self.differentThanDefaultByStateWarning=function () {
			var currentStateWarningList = self.getCurrentStateWarningList(self.productStateWarnings);
			if(currentStateWarningList && (currentStateWarningList.length > 0) && self.listStateWarningBySubcommodity){
				var diffArray = this.getDifferenceBetweenTwoArrays(currentStateWarningList, self.listStateWarningBySubcommodity);
				if(diffArray != null && diffArray.length > 0){
					return true;
				}
			}
			return false;
		};

		/**
		 * Get the list of current shipping restriction.
		 */
		self.getCurrentSalesRestrictionList=function (listData) {
			var returnList = [];
			if(listData){
				angular.forEach(listData, function(item){
					returnList.push(item.restriction.restrictionCode.trim());
				});
			}
			return returnList;
		};

		/**
		 * Get the list of current state warning.
		 */
		self.getCurrentStateWarningList=function (listData) {
			var returnList = [];
			if(listData){
				angular.forEach(listData, function(item){
					returnList.push(item.productStateWarning.key.warningCode.trim());
				});
			}
			return returnList;
		};

		/**
		 * Get difference between two arrays.
		 */
		self.getDifferenceBetweenTwoArrays=function (a1, a2) {
			return a1.concat(a2).filter(function(val, index, arr){
				return arr.indexOf(val) === arr.lastIndexOf(val);
			});
		};

		/**
		 * This method creates the current list of (non shipping) restrictions on the product
		 */
		self.createCurrentRestrictions = function () {
			var filteredRestrictions=self.removeStateRestrictions();
			self.currentRestrictions = {
				prodId: self.productMaster.prodId,
				restrictions: filteredRestrictions
			};
			self.originalRestrictions = angular.copy(self.currentRestrictions);
		};

		/**
		 * Method will remove any shipping restrictions from the products current restrictions list
		 * @returns {Array}
		 */
		self.removeStateRestrictions = function () {
			var nonShippingRestrictions = [];
			angular.forEach(self.productMaster.restrictions, function(restriction){
				if(!angular.equals("9", restriction.restriction.restrictionGroupCode)){
					nonShippingRestrictions.push(restriction)
				}
			});
			return nonShippingRestrictions;
		};

		/**
		 * This method will create a new restriction to add to the current products restrictions list
		 */
		self.addRestriction=function(){
			var selectedRestriction=self.restrictionFilter[document.getElementById("restrictionSelect").selectedIndex];
			var newRestriction={
				key:{
					prodId: self.productMaster.prodId,
					restrictionCode: self.restrictionGroup
				},
				restriction: {
					sellingRestriction:{
						restrictionGroupCode:selectedRestriction.sellingRestriction.restrictionGroupCode,
						restrictionAbbreviation:selectedRestriction.sellingRestriction.restrictionAbbreviation,
						restrictionDescription:selectedRestriction.sellingRestriction.restrictionDescription
					},
					dateTimeRestriction: selectedRestriction.dateTimeRestriction,
					minimumRestrictedAge: selectedRestriction.minimumRestrictedAge,
					restrictedQuantity: selectedRestriction.restrictedQuantity,
					restrictionAbbreviation: selectedRestriction.restrictionAbbreviation,
					restrictionCode: selectedRestriction.restrictionCode,
					restrictionGroupCode: selectedRestriction.restrictionGroupCode,
					shippingRestriction: selectedRestriction.shippingRestriction,
					restrictionDescription: selectedRestriction.restrictionDescription
				}
			};
			self.currentRestrictions.restrictions.push(newRestriction);
		};

		/**
		 * This method will insure that you do not add any duplicate restrictions
		 * @returns {boolean}
		 */
		self.validAdd=function () {
			var returnValue = false;
			if(self.currentRestrictions.restrictions.length>0){
				for(var index = 0; index<self.currentRestrictions.restrictions.length; index++) {
					if(angular.equals(self.currentRestrictions.restrictions[index].restriction.restrictionGroupCode.toString().trim(), self.restrictionGroup.toString().trim())){
						returnValue = true;
						break;
					}
				}
			}
			return returnValue;
		};

		/**
		 * Gets all the changes in the restrictions list
		 * @param index
		 */
		self.getRestrictionDifference = function () {
			var arrayIndex;
			var restrictionChanges = {
				prodId: self.productMaster.prodId,
				restrictionsAdded: [],
				restrictionsRemoved: []

			};
			angular.forEach(self.currentRestrictions.restrictions, function (restriction) {
				var foundBanner = false;
				if (self.originalRestrictions.restrictions.length > 0) {
					for (arrayIndex = 0; arrayIndex < self.originalRestrictions.restrictions.length; arrayIndex++) {
						if (angular.equals(restriction.key, self.originalRestrictions.restrictions[arrayIndex].key)) {
							foundBanner = true;
							break;
						}
					}
				}
				if (!foundBanner) {
					restrictionChanges.restrictionsAdded.push(restriction);
				}
			});
			angular.forEach(self.originalRestrictions.restrictions, function (restriction) {
				var foundBanner = false;
				if (self.currentRestrictions.restrictions !== null){
					if (self.currentRestrictions.restrictions.length > 0) {
						for (arrayIndex = 0; arrayIndex < self.currentRestrictions.restrictions.length; arrayIndex++) {
							if (angular.equals(restriction.key, self.currentRestrictions.restrictions[arrayIndex].key)) {
								foundBanner = true;
								break;
							}
						}
					}
				}
				if (!foundBanner) {
					restrictionChanges.restrictionsRemoved.push(restriction)
				}
			});
			return restrictionChanges;
		};

		/**
		 * Whenever one of the check boxes is checked this method will update a list of what indexes need to be deleted
		 * @param index
		 */
		self.updateDeleteList = function (index) {
			if(self.deleteList.indexOf(index)<0){
				self.deleteList.push(index);
			} else {
				self.allChecked = false;
				var iterator = self.deleteList.length;
				while (iterator--) {
					if (index === self.deleteList[iterator]) {
						self.deleteList.splice(iterator, 1);
					}
				}
			}
		};

		/**
		 * API call to update selling restrictions for special attributes.
		 *
		 */
		self.getSellingRestrictionsForUpdate = function () {
			self.error = '';
			self.success = '';
			self.initCheckAllSellingRestrictions();
			var sellingRestrictions = angular.copy(self.allShippingRestrictions);

			for(var index = 0; index < self.productShippingRestrictions.length; index++) {
				for(var innerIndex = 0; innerIndex < sellingRestrictions.length; innerIndex++) {
					if(sellingRestrictions[innerIndex].restrictionCode === self.productShippingRestrictions[index].restriction.restrictionCode) {
						sellingRestrictions[innerIndex].isChecked = true;
						break;
					}
				}
			}

			self.currentShippingRestrictions = angular.copy(sellingRestrictions);
		};

		self.initCheckAllSellingRestrictions = function () {
			self.allShippingRestrictionChecked
				= self.productShippingRestrictions.length === self.allShippingRestrictions.length;
		};

		/**
		 * API call to update shipping method exceptions for special attributes.
		 *
		 */
		self.getShippingMethodExceptionsForUpdate = function () {
			self.error = '';
			self.success = '';
			self.initCheckAllShippingMethodExceptions();
			self.originalShippingMethodExceptions = angular.copy(self.allShippingMethodRestrictions);

			for(var index = 0; index<self.shippingMethodExceptions.length; index++) {
				for(var innerIndex = 0; innerIndex<self.originalShippingMethodExceptions.length; innerIndex++) {
					if(self.originalShippingMethodExceptions[innerIndex].customShippingMethod === self.shippingMethodExceptions[index].customShippingMethod.customShippingMethod) {
						self.originalShippingMethodExceptions[innerIndex].isChecked = true;
						break;
					}
				}
			}

			self.currentShippingMethodExceptions = angular.copy(self.originalShippingMethodExceptions);
		};

		self.initCheckAllShippingMethodExceptions = function () {
			self.allShippingMethodChecked
				= self.shippingMethodExceptions.length === self.allShippingMethodRestrictions.length;
		};

		/**
		 * API call to update state warning for special attributes.
		 *
		 */
		self.getStateWarningsForUpdate = function () {
			self.error = '';
			self.success = '';
			self.initCheckAllStateWarnings();
			self.originalStateWarnings = angular.copy(self.allStateWarnings);

			for(var index = 0; index<self.productStateWarnings.length; index++) {
				for(var innerIndex = 0; innerIndex<self.originalStateWarnings.length; innerIndex++) {
					if(self.originalStateWarnings[innerIndex].key.stateCode === self.productStateWarnings[index].key.stateCode) {
						self.originalStateWarnings[innerIndex].isChecked = true;
						break;
					}
				}
			}

			self.currentStateWarnings = angular.copy(self.originalStateWarnings);
		};

		self.initCheckAllStateWarnings = function () {
			self.allStateWarningsChecked
				= self.productStateWarnings.length === self.allStateWarnings.length;
		};

		self.resetCheckAllRestrictions = function () {
			var currentShippingRestrictions = angular.copy(self.currentShippingRestrictions);
			var count = 0;
			for(var i = 0; i < currentShippingRestrictions.length; i++) {
				if(currentShippingRestrictions[i].isChecked === true) {
					count++;
				}
			}
			self.allShippingRestrictionChecked = self.allShippingRestrictions.length === count;
		};

		self.resetCheckAllShippingMethod = function () {
			var currentShippingMethodExceptions = angular.copy(self.currentShippingMethodExceptions);
			var count = 0;
			for(var i = 0; i < currentShippingMethodExceptions.length; i++) {
				if(currentShippingMethodExceptions[i].isChecked === true) {
					count++;
				}
			}
			self.allShippingMethodChecked = self.allShippingMethodRestrictions.length === count;
		};

		self.resetCheckAllStateWarnings = function () {
			var currentStateWarnings = angular.copy(self.currentStateWarnings);
			var count = 0;
			for(var i = 0; i < currentStateWarnings.length; i++) {
				if(currentStateWarnings[i].isChecked === true) {
					count++;
				}
			}
			self.allStateWarningsChecked = self.allStateWarnings.length === count;
		};

		/**
		 * This method will check all rows
		 */
		self.checkAllRows= function () {
			self.deleteList=[];
			$("#restrictionsTable tr td:nth-child(1) input[type=checkbox]").prop("checked", self.allChecked);
			if(self.allChecked){
				if(self.currentRestrictions.restrictions !== null){
					for(var index=0; index<self.currentRestrictions.restrictions.length; index++){
						self.deleteList.push(index);
					}
				}
			}
		};

		/**
		 * API call to update shipping restrictions for special attributes.
		 *
		 */
		self.updateShippingRestrictions = function () {
			self.isLoading = true;
			self.error = '';
			self.success = '';
			var productRestrictionUpdates  = {};
			productRestrictionUpdates.prodId = angular.copy(self.productMaster.prodId);
			productRestrictionUpdates.productRestrictions = [];
			var productRestrictionUpdate;

			for(var innerIndex = 0; innerIndex<self.allShippingRestrictions.length; innerIndex++) {
				if (self.currentShippingRestrictions[innerIndex].isChecked) {
					productRestrictionUpdate  = {};
					productRestrictionUpdate.key = {};
					productRestrictionUpdate.key.restrictionCode = self.currentShippingRestrictions[innerIndex].restrictionCode;
					productRestrictionUpdate.key.prodId = self.productMaster.prodId;
					productRestrictionUpdates.productRestrictions.push(productRestrictionUpdate);
				}
			}

			var shippingRestrictionsBeforeChange = [];
			var shippingRestrictionsAfterChange = [];
			angular.forEach(self.productShippingRestrictions, function(shippingRestrictions) {
				shippingRestrictionsBeforeChange.push(shippingRestrictions.key.restrictionCode.trim())
			});
			angular.forEach(productRestrictionUpdates.productRestrictions, function(productRestrictionUpdate) {
				shippingRestrictionsAfterChange.push(productRestrictionUpdate.key.restrictionCode.trim());
			});

			shippingRestrictionsBeforeChange.sort();
			shippingRestrictionsAfterChange.sort();
			if (JSON.stringify(shippingRestrictionsBeforeChange) !== JSON.stringify(shippingRestrictionsAfterChange)) {
				productApi.updateShippingRestrictionList(productRestrictionUpdates, self.reloadSavedData, self.fetchError);
			} else {
				self.error = NO_CHANGE_DETECTED_MSG;
				self.isLoading = false;
			}

		};

		/**
		 * API call to update shipping methods for special attributes.
		 *
		 */
		self.updateShippingMethodExceptions = function () {
			self.isLoading = true;
			self.error = '';
			self.success = '';
			var shippingMethods = {};
			shippingMethods.prodId = angular.copy(self.productMaster.prodId);
			shippingMethods.customShippingMethods = [];
			var customShippingMethod;

			for(var innerIndex = 0; innerIndex<self.allShippingMethodRestrictions.length; innerIndex++) {
				if(self.currentShippingMethodExceptions[innerIndex].isChecked) {
					customShippingMethod = {};
					customShippingMethod.customShippingAbbriviation = self.currentShippingMethodExceptions[innerIndex].customShippingAbbriviation;
					customShippingMethod.customShippingMethod = self.currentShippingMethodExceptions[innerIndex].customShippingMethod;
					customShippingMethod.customShippingMethodDescription = self.currentShippingMethodExceptions[innerIndex].customShippingMethodDescription;
					shippingMethods.customShippingMethods.push(customShippingMethod);
				}
			}
			var shippingMethodsBeforeChange = [];
			var shippingMethodsAfterChange = [];
			angular.forEach(shippingMethods.customShippingMethods, function(shippingMethod) {
				shippingMethodsBeforeChange.push(shippingMethod.customShippingMethod.trim())
			});
			angular.forEach(self.shippingMethodExceptions, function(shippingMethod) {
				shippingMethodsAfterChange.push(shippingMethod.customShippingMethod.customShippingMethod.trim())
			});

			shippingMethodsBeforeChange.sort();
			shippingMethodsAfterChange.sort();
			if (JSON.stringify(shippingMethodsBeforeChange) !== JSON.stringify(shippingMethodsAfterChange)) {
				productApi.updateShippingMethods(shippingMethods, self.reloadSavedData, self.fetchError);
			} else {
				self.error = NO_CHANGE_DETECTED_MSG;
				self.isLoading = false;
			}

		};

		/**
		 * API call to update state warnings for special attributes.
		 *
		 */
		self.updateStateWarnings = function () {
			self.isLoading = true;
			self.error = '';
			self.success = '';
			var productStateWarnings = {};
			productStateWarnings.prodId = angular.copy(self.productMaster.prodId);
			productStateWarnings.productStateWarnings = [];
			var stateWarning;

			for(var innerIndex = 0; innerIndex<self.allStateWarnings.length; innerIndex++) {
				if(self.currentStateWarnings[innerIndex].isChecked) {
					stateWarning = {};
					stateWarning.key = {};
					stateWarning.key.stateCode = self.currentStateWarnings[innerIndex].key.stateCode;
					stateWarning.key.warningCode = self.currentStateWarnings[innerIndex].key.warningCode;
					stateWarning.abbreviation = self.currentStateWarnings[innerIndex].abbreviation;
					stateWarning.description = self.currentStateWarnings[innerIndex].description;
					productStateWarnings.productStateWarnings.push(stateWarning);
				}
			}
			var stateWarningsBeforeChange = [];
			var stateWarningsAfterChange = [];
			angular.forEach(self.productStateWarnings, function(stateWarning) {
				stateWarningsBeforeChange.push(stateWarning.productStateWarning.key.warningCode.trim())
			});
			angular.forEach(productStateWarnings.productStateWarnings, function(stateWarning) {
				stateWarningsAfterChange.push(stateWarning.key.warningCode.trim())
			});

			stateWarningsBeforeChange.sort();
			stateWarningsAfterChange.sort();

			if (JSON.stringify(stateWarningsBeforeChange) !== JSON.stringify(stateWarningsAfterChange)) {
				productApi.updateProductStateWarnings(productStateWarnings, self.reloadSavedData, self.fetchError);
			} else {
				self.error = NO_CHANGE_DETECTED_MSG;
				self.isLoading = false;
			}
		};

		/**
		 * This method will check all rows for shipping restrictions.
		 */
		self.checkAllShippingRestrictionRows= function () {
			var isChecked = self.allShippingRestrictionChecked === true;
			var allShippingRestrictionsWithCheck = angular.copy(self.allShippingRestrictions);
			for(var i = 0; i < allShippingRestrictionsWithCheck.length; i++) {
				allShippingRestrictionsWithCheck[i].isChecked = isChecked;
			};
			self.currentShippingRestrictions = angular.copy(allShippingRestrictionsWithCheck);
		};

		/**
		 * This method will check all rows for shipping methods.
		 */
		self.checkAllShippingMethodRows= function () {
			var isChecked = self.allShippingMethodChecked === true;
			var allShippingMethodsWithCheck = angular.copy(self.allShippingMethodRestrictions);
			for(var i = 0; i < allShippingMethodsWithCheck.length; i++) {
				allShippingMethodsWithCheck[i].isChecked = isChecked;
			};
			self.currentShippingMethodExceptions = angular.copy(allShippingMethodsWithCheck);
		};

		/**
		 * This method will check all rows for state warnings.
		 */
		self.checkAllStateWarningsRows = function () {
			var isChecked = self.allStateWarningsChecked === true;
			var allStateWarningsWithCheck = angular.copy(self.allStateWarnings);
			for(var i = 0; i < allStateWarningsWithCheck.length; i++) {
				allStateWarningsWithCheck[i].isChecked = isChecked;
			};
			self.currentStateWarnings = angular.copy(allStateWarningsWithCheck);
		};

		/**
		 * This method will remove the selected restrictions
		 */
		self.deleteRestrictions = function () {
			self.allChecked=false;
			$("#restrictionsTable tr td:nth-child(1) input[type=checkbox]").prop("checked", false);
			for(var index = 0; index<self.deleteList.length; index++){
				self.currentRestrictions.restrictions[self.deleteList[index]].toDelete=true;
			}
			for(var index = self.currentRestrictions.restrictions.length -1; index >=0; index--){
				if(self.currentRestrictions.restrictions[index].toDelete){
					self.currentRestrictions.restrictions.splice(index, 1);
				}
			}
			if(self.currentRestrictions.restrictions.length === 0){
				self.currentRestrictions.restrictions = [];
			}
			self.deleteList=[];
		};

		/**
		 * Show audit folder icon
		 * @return - boolean to show icon
		 */
		self.showAuditFolder = function () {
			return true;
		}

		/**
		 * Show audit information modal
		 */
		self.showSpecialAttributesAuditInfo = function () {
			if (angular.equals(self.currentTab, pharmacyTab)) {
				self.showPharmacyAuditInfo();
			} else if (angular.equals(self.currentTab, tobaccoTab)) {
				self.showTobaccoAuditInfo();
			} else if (angular.equals(self.currentTab, codeDateTab)) {
				self.showCodeDateAuditInfo();
			} else if (angular.equals(self.currentTab, shippingHandlingTab)) {
				self.showShippingHandlingAuditInfo();
			} else if (angular.equals(self.currentTab, ratingRestrictionsTab)) {
				self.showRatingRestrictionsAuditInfo();
			}
		}

		/**
		 * Show code date information modal
		 */
		self.showCodeDateAuditInfo = function () {
			self.codeDateAuditInfo = productApi.getCodeDateAudits;
			var title ="Code Date";
			var parameters = {'prodId' :self.productMaster.prodId};
			ProductDetailAuditModal.open(self.codeDateAuditInfo, parameters, title);
		}

		/**
		 * Show shipping handling information modal
		 */
		self.showShippingHandlingAuditInfo = function () {
			self.shippingHandlingAuditInfo = productApi.getShippingHandlingAudits;
			var title ="Shipping and Handling";
			var parameters = {'prodId' :self.productMaster.prodId};
			ProductDetailAuditModal.open(self.shippingHandlingAuditInfo, parameters, title);
		}

		/**
		 * Show pharmacy audit information modal
		 */
		self.showPharmacyAuditInfo = function () {
			self.pharmacyAuditInfo = productApi.getPharmacyAudits;
			var title ="Pharmacy";
			var parameters = {'prodId' :self.productMaster.prodId};
			ProductDetailAuditModal.open(self.pharmacyAuditInfo, parameters, title);
		}

		/**
		 * Show tobacco audit information modal
		 */
		self.showTobaccoAuditInfo = function () {
			self.tobaccoAuditInfo = productApi.getTobaccoAudits;
			var title ="Tobacco";
			var parameters = {'prodId' :self.productMaster.prodId};
			ProductDetailAuditModal.open(self.tobaccoAuditInfo, parameters, title);
		}

		/**
		 * Show rating/restriction audit information modal
		 */
		self.showRatingRestrictionsAuditInfo = function () {
			self.ratingRestrictionsAuditInfo = productApi.getRatingRestrictionsAudits;
			var title ="Rating/Restrictions";
			var parameters = {'prodId' :self.productMaster.prodId};
			ProductDetailAuditModal.open(self.ratingRestrictionsAuditInfo, parameters, title);
		}
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};

		/**
		 * Returns the added date, or null value if date doesn't exist.
		 */
		self.getAddedDate = function() {
			if(self.productMaster.createdDateTime === null || angular.isUndefined(self.productMaster.createdDateTime)) {
				return '01/01/1901 00:00';
			} else if (parseInt(self.productMaster.createdDateTime.substring(0, 4)) < 1900) {
				return '01/01/0001 00:00';
			} else {
				return $filter('date')(self.productMaster.createdDateTime, 'MM/dd/yyyy HH:mm');
			}
		};

		/**
		 * Returns createUser or '' if not present.
		 */
		self.getCreateUser = function() {
			if(self.productMaster.displayCreatedName === null || self.productMaster.displayCreatedName.trim().length == 0) {
				return '';
			}
			return self.productMaster.displayCreatedName;
		};

		/**
		 * Check current code date.
		 *
		 * @param currentCodeDate current code date.
		 * @returns {boolean} result check.
		 */
		self.checkCurrentCodeDate = function (currentCodeDate) {
			self.resetAllCss();
			var messageContent = '';
			
			if (currentCodeDate.goodsProduct !== null) {
				if (self.isNullOrEmpty(currentCodeDate.goodsProduct.maxShelfLifeDays)) {
					messageContent += '<li>Max Shelf Life Days must be entered.</li>';
					$('#maxSelf').attr('title', 'Max Shelf Life Days must be entered.');
					$('#maxSelf').addClass('ng-touched ng-invalid');
				}else if (currentCodeDate.goodsProduct.maxShelfLifeDays > 3650 || currentCodeDate.goodsProduct.maxShelfLifeDays === 0) {
					messageContent += '<li>Max Shelf Life Days must be greater than 0 and less than or equal to 3650.</li>';
					$('#maxSelf').attr('title', 'Max Shelf Life Days must be greater than 0 and less than or equal to 3650.');
					$('#maxSelf').addClass('ng-touched ng-invalid');
				}
				if (self.isNullOrEmpty(currentCodeDate.goodsProduct.inboundSpecificationDays)) {
					messageContent += '<li>Inbound Specification Days must be entered.</li>';
					$('#inboundSpecification').attr('title', 'Inbound Specification Days must be entered.');
					$('#inboundSpecification').addClass('ng-touched ng-invalid');
				}else if (!self.isNullOrEmpty(currentCodeDate.goodsProduct.maxShelfLifeDays)
						&& (currentCodeDate.goodsProduct.inboundSpecificationDays >= currentCodeDate.goodsProduct.maxShelfLifeDays
						|| currentCodeDate.goodsProduct.inboundSpecificationDays === 0)) {
					messageContent += '<li>Inbound Specification Days must be greater than 0 and less than Max Shelf Life Days.</li>';
					$('#inboundSpecification').attr('title', 'Inbound Specification Days must be greater than 0 and less than Max Shelf Life Days.');
					$('#inboundSpecification').addClass('ng-touched ng-invalid');
				}
				if (self.isNullOrEmpty(currentCodeDate.goodsProduct.warehouseReactionDays)) {
					messageContent += '<li>Warehouse Reaction Days must be entered.</li>';
					$('#warehouseReaction').attr('title', 'Warehouse Reaction Days must be entered.');
					$('#warehouseReaction').addClass('ng-touched ng-invalid');
				}else if(!self.isNullOrEmpty(currentCodeDate.goodsProduct.inboundSpecificationDays) 
						&& (currentCodeDate.goodsProduct.warehouseReactionDays >= currentCodeDate.goodsProduct.inboundSpecificationDays
								|| currentCodeDate.goodsProduct.warehouseReactionDays === 0)){
					messageContent += '<li>Warehouse Reaction Days must be greater than 0 and less than Inbound Specification Days.</li>';
					$('#warehouseReaction').attr('title', 'Warehouse Reaction Days must be greater than 0 and less than Inbound Specification Days.');
					$('#warehouseReaction').addClass('ng-touched ng-invalid');
				}
				if (self.isNullOrEmpty(currentCodeDate.goodsProduct.guaranteeToStoreDays)) {
					messageContent += '<li>Guarantee To Store Days must be entered.</li>';
					$('#guaranteeToStore').attr('title', 'Guarantee To Store Days must be entered.');
					$('#guaranteeToStore').addClass('ng-touched ng-invalid');
				}else if (!self.isNullOrEmpty(currentCodeDate.goodsProduct.warehouseReactionDays) 
						&& (currentCodeDate.goodsProduct.guaranteeToStoreDays >= currentCodeDate.goodsProduct.warehouseReactionDays
						|| currentCodeDate.goodsProduct.guaranteeToStoreDays === 0)){
					messageContent += '<li>Guarantee To Store Days must be greater than 0 and less than Warehouse Reaction Days.</li>'
						$('#guaranteeToStore').attr('title', 'Guarantee To Store Days must be greater than 0 and less than Warehouse Reaction Days.');
					$('#guaranteeToStore').addClass('ng-touched ng-invalid');
				}
			}
			
			if (messageContent !== '') {
				self.error = "Product Special Attribute:" + messageContent;
				return false;
			}
			return true;
		};

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === null || object === "";
		};

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmptyOrBlank = function (object) {
			return object === null || object === undefined || object.trim() === "";
		};


		/**
		 * Reset all html.
		 */
		self.resetAllCss = function () {
			$('#maxSelf').attr('title', '');
			$('#maxSelf').removeClass('ng-touched ng-invalid');
			$('#inboundSpecification').attr('title', '');
			$('#inboundSpecification').removeClass('ng-touched ng-invalid');
			$('#warehouseReaction').attr('title', '');
			$('#warehouseReaction').removeClass('ng-touched ng-invalid');
			$('#guaranteeToStore').attr('title', '');
			$('#guaranteeToStore').removeClass('ng-touched ng-invalid');
		};

		/**
		 * Handle current code date when check or uncheck code date check box.
		 * @param value
		 */
		self.currentCodeDateHandle = function (value) {
			if (self.currentCodeDate.goodsProduct !== null){
				if (value === false){
					self.resetAllCss();
					self.currentCodeDate.goodsProduct.codeDate = false;
					self.currentCodeDate.goodsProduct.guaranteeToStoreDays = null;
					self.currentCodeDate.goodsProduct.maxShelfLifeDays = null;
					self.currentCodeDate.goodsProduct.inboundSpecificationDays = null;
					self.currentCodeDate.goodsProduct.warehouseReactionDays = null;
					self.currentCodeDate.goodsProduct.sendCodeDate = false;
				}else {
					self.currentCodeDate.goodsProduct.codeDate = true;
					self.currentCodeDate.goodsProduct.guaranteeToStoreDays = self.originalCodeDate.goodsProduct.guaranteeToStoreDays;
					self.currentCodeDate.goodsProduct.maxShelfLifeDays = self.originalCodeDate.goodsProduct.maxShelfLifeDays;
					self.currentCodeDate.goodsProduct.inboundSpecificationDays = self.originalCodeDate.goodsProduct.inboundSpecificationDays;
					self.currentCodeDate.goodsProduct.warehouseReactionDays = self.originalCodeDate.goodsProduct.warehouseReactionDays;
					self.currentCodeDate.goodsProduct.sendCodeDate = self.originalCodeDate.goodsProduct.sendCodeDate;
				}
			}
		};

		/**
		 * Clear value for medicail code, nd, drug schedule type code
		 */
		self.uncheckablePharmacySw = function () {
			if (self.currentRx.goodsProduct !== null){
				if(self.currentRx.goodsProduct.rxProductFlag == false){
					self.currentRx.goodsProduct.rxProduct.drugScheduleTypeCode = "";
					self.currentRx.goodsProduct.medicaidCode = "";
					self.currentRx.goodsProduct.rxProduct.ndc = "";
				}
			}
		};

		/**
		 * PSE Type Code changed handle. Set all pse gram weight to zero when PSE type code has been set to NO PSE.
		 */
		self.pseTypeCodeChanged = function () {
			if (self.currentRx.goodsProduct !== null){
				if(self.currentRx.goodsProduct.pseTypeCode === 'N'){
					angular.forEach(self.currentRx.sellingUnits, function(item) {
						item.pseGramWeight = 0;
					});
				}
			}
		};

		/**
		 * Check current pharmacy.
		 *
		 * @param currentCodeDate current pharmacy date.
		 * @returns {boolean} result check.
		 */
		self.checkCurrentPharmacy = function (currentRx) {
			var messageContent = '';
			if(currentRx.goodsProduct != null && currentRx.goodsProduct != undefined){
				if(currentRx.goodsProduct.rxProductFlag === true) {
					if (currentRx.goodsProduct.rxProduct == null || currentRx.goodsProduct.rxProduct == undefined){
						messageContent += '<li>Please select a Drug Schedulle.</li>';
						messageContent += '<li>Please select a Medicaid.</li>';
					}else{
						if(self.isNullOrEmptyOrBlank(currentRx.goodsProduct.rxProduct.drugScheduleTypeCode)) {
							messageContent += '<li>Please select a Drug Schedulle.</li>';
						}
						if (self.isNullOrEmptyOrBlank(currentRx.goodsProduct.medicaidCode)) {
							messageContent += '<li>Please select a Medicaid.</li>';
						}
					}
				}else if(self.currentRx.goodsProduct !== null
						&& self.currentRx.goodsProduct.rxProductFlag === true){
					if (currentRx.goodsProduct.rxProduct != null && currentRx.goodsProduct.rxProduct != undefined
						&& currentRx.goodsProduct.rxProduct.drugScheduleTypeCode != undefined
						&& self.isNullOrEmptyOrBlank(currentRx.goodsProduct.rxProduct.drugScheduleTypeCode)) {
						messageContent += '<li>Please select a Drug Schedulle.</li>';
					}
					if (currentRx.goodsProduct.medicaidCode != undefined && self.isNullOrEmptyOrBlank(currentRx.goodsProduct.medicaidCode)) {
						messageContent += '<li>Please select a Medicaid.</li>';
					}
				}
			}
			if(currentRx.sellingUnits != null && currentRx.sellingUnits != undefined){
				if (currentRx.goodsProduct != null && currentRx.goodsProduct != undefined) {
					if(self.isNullOrEmptyOrBlank(currentRx.goodsProduct.pseTypeCode)){
						messageContent += '<li>Please select a PSE value.</li>';
					}
				}else if(self.isNullOrEmptyOrBlank(self.currentRx.goodsProduct.pseTypeCode)){
					messageContent += '<li>Please select a PSE value.</li>';
				}
				if(self.currentRx.goodsProduct !== null
						&& self.currentRx.goodsProduct.pseTypeCode != 'N') {
					angular.forEach(currentRx.sellingUnits, function (item) {
						if ((item.pseGramWeight && !self.regexDecimal.test(item.pseGramWeight)) || (item.pseGramWeight == 0)) {
							if (messageContent.indexOf("PSE Grams value must be number (#####.####), greater 0 and less than or equal to 99999.9999") == -1) {
								messageContent += '<li>PSE Grams value must be number (#####.####), greater 0 and less than or equal to 99999.9999.</li>';
							}
						}
					});
				}
			}else{
				if (currentRx.goodsProduct != null 
						&& currentRx.goodsProduct != undefined 
						&& currentRx.goodsProduct.pseTypeCode != null 
						&& currentRx.goodsProduct.pseTypeCode != undefined) {
					if (self.isNullOrEmptyOrBlank(currentRx.goodsProduct.pseTypeCode)) {
						messageContent += '<li>Please select a PSE value.</li>';
					}
					if(self.currentRx.sellingUnits != null 
							&& self.currentRx.sellingUnits != undefined 
							&& self.currentRx.goodsProduct != null 
							&& self.currentRx.goodsProduct != undefined 
							&& self.currentRx.goodsProduct.pseTypeCode != 'N'){
						angular.forEach(self.currentRx.sellingUnits, function(item){
							if((item.pseGramWeight && !self.regexDecimal.test(item.pseGramWeight)) ||(item.pseGramWeight == 0)){
								if(messageContent.indexOf("PSE Grams value must be number (#####.####), greater 0 and less than or equal to 99999.9999") == -1){
									messageContent+= '<li>PSE Grams value must be number (#####.####), greater 0 and less than or equal to 99999.9999.</li>';
								}
							}
						});
					}
				}
			}
			if (messageContent !== '') {
				self.error = "Product Special Attribute:" + messageContent;
				return false;
			}
			return true;
		};
	}
})();
