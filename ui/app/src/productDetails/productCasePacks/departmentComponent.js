/*
 *   departmentComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
*/
'use strict';

/**
 * CasePack -> department page component.
 *
 * @author l730832
 * @since 2.8.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('department', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			productMaster: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/department.html',
		// The controller that handles our component logic
		controller: DepartmentController
	});

	DepartmentController.$inject = ['ProductCasePackApi', '$rootScope', 'ProductSearchService', '$scope', 'ngTableParams'];

	/**
	 * Case Pack Info component's controller definition.
	 * @constructor
	 * @param productCasePackApi
	 */
	function DepartmentController(productCasePackApi, $rootScope, productSearchService, $scope, ngTableParams) {
		/** All CRUD operation controls of Case pack Info page goes here */
		var self = this;

		/**
		 * The Department list from item master.
		 * @type {Array}
		 */
		self.data = [];

		/**
		 * The Department list from item master to backup.
		 * @type {Array}
		 */
		self.dataOrig = [];

		/**
		 * The default pss department from product master.
		 * @type {null}
		 */
		self.defaultPssDepartment = null;

		/**
		 * The loading icon.
		 */
		self.isLoading = false;

		/**
		 * Token that is used to keep track of the request.
		 * @type {number}
		 */
		self.numberRequest=0;

		/**
		 * The list of departments
		 * @type {Array}
		 */
		self.departments = [];

		/**
		 * The list of departmentsOrg
		 * @type {Array}
		 */
		self.departmentsOrg = [];

		/**
		 * The list of merchandise types;
		 * @type {Array}
		 */
		self.merchandiseTypes = [];

		/**
		 * The list of merchandise types;
		 * @type {Array}
		 */
		self.merchandiseTypesOrg = [];

		/**
		 * This is status search.
		 * @type {boolean}
		 */
		self.firstSearch = false;

		/**
		 * The item master on page.
		 */
		self.itemMasterOnPage = {};

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

		self.isWaitingGetDepartmentAttributeAudit = true;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		}
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isLoading = false;
			if(error && error.data) {
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
		 * Check no request waiting for stop waiting loading.
		 */
		self.hideWaitingWithFirstSearch = function () {
			self.numberRequest--;
			if(self.numberRequest == 0){
				self.isLoading = false;
				self.dataOrig = angular.copy(self.data);
			}
		}

		/**
		 * Get list of department data for Department dropdown list data.
		 */
		self.getAllDepartmentInformation = function () {
			self.numberRequest++;
			productCasePackApi.findAllDepartment(
				//success case
				function (results) {
					self.departments = [];
					for(var i=0;i<results.length;i++) {
						if(results[i] && results[i].key.subDepartment){
							self.departments.push(results[i]);
						}
					}
					self.departmentsOrg = angular.copy(self.departments);
					self.setDepartmentDataSelected();
					self.hideWaitingWithFirstSearch();
				},
				self.fetchError);
		}

		/**
		 * Get list of Merchandise Types data for Merchandise Types dropdown list data.
		 */
		self.getAllMerchandiseTypes = function () {
			self.numberRequest++;
			productCasePackApi.findAllMerchandiseType(
				//success case
				function (results) {
					self.merchandiseTypes = results;
					self.merchandiseTypesOrg = angular.copy(self.merchandiseTypes);
					self.hideWaitingWithFirstSearch();
				},
				self.fetchError);
		}

		/**
		 * Find Pss Department Information by pss department code from product master.
		 */
		self.findPssDepartmentCode = function () {
			if(self.productMaster.classCommodity && self.productMaster.classCommodity.pssDepartment){
				productCasePackApi.findPssDepartmentCodeByPssDepartmentId(
					{"pssDepartmentId":self.productMaster.classCommodity.pssDepartment}
					,//success case
					function (results) {
						if(results && results.displayName){
							self.defaultPssDepartment = results.displayName;
						}else{
							self.defaultPssDepartment = '';
						}
					}
					,
					function (error) {
						self.defaultPssDepartment = '';
					});
			}
		}

		/**
		 * Initializes the component and re-initializes it on changes.
		 */
		this.$onChanges = function () {
			self.error = '';
			self.success = '';
			self.isWarehouse = !!self.itemMaster.key.warehouse;
			self.findPssDepartmentCode();
			if(!self.isWarehouse){
				self.findItemMaster();
			}else{
				//Reset data before get new data
				self.dataOrig = [];
				self.data = [];
			}
		};

		/**
		 * Find item master information
		 */
		self.findItemMaster = function () {
			self.isLoading = true;
			self.numberRequest++;
			productCasePackApi.findItemMaster(
				{
					"itemId":self.itemMaster.key.itemCode,
					"itemTypCd":self.itemMaster.key.itemType
				}
				,//success case
				function (results) {
					self.itemMasterOnPage = angular.copy(results);
					self.initData();
					self.hideWaitingWithFirstSearch();
				}
				, self.fetchError);
		}

		/**
		 * Initializes all data in department page.
		 */
		self.initData = function () {
			//Reset data before get new data
			self.dataOrig = [];
			self.data = [];
			// Only do these calls when if it is a dsd item else don't make the calls for warehouse items.
			self.initializesDepartmentData();
			if (self.departments != null && self.departments != undefined && self.departments.length > 0) {
				self.setDepartmentDataSelected();
			} else {
				self.getAllDepartmentInformation();
			}
			if (self.merchandiseTypes == null || self.merchandiseTypes == undefined || self.merchandiseTypes.length == 0) {
				self.getAllMerchandiseTypes();
			}
		}

		/**
		 * Initializes the department data.
		 */
		self.initializesDepartmentData = function () {
			self.data = [];
			var departmentOne = {};
			departmentOne["merchandiseType"] = self.itemMasterOnPage.merchandiseTypeOne;
			self.data.push(departmentOne);
			var departmentTwo = {};
			departmentTwo["merchandiseType"] = self.itemMasterOnPage.merchandiseTypeTwo;
			self.data.push(departmentTwo);
			var departmentThree = {};
			departmentThree["merchandiseType"] = self.itemMasterOnPage.merchandiseTypeThree;
			self.data.push(departmentThree);
			var departmentFour = {};
			departmentFour["merchandiseType"] = self.itemMasterOnPage.merchandiseTypeFour;
			self.data.push(departmentFour);
		}

		/**
		 * Find Pss Department each row by department and subDepartment.
		 * @param department - The department code
		 * @param subDepartment - The sub department code
		 * @param number - The number of row
		 */
		self.findPssDepartmentByDepartment = function (department, subDepartment, number, change) {
			productCasePackApi.findPssDepartmentsByDepartmentAndSubDepartment(
				{
					"department":department,
					"subDepartment":subDepartment,
				}
				,//success case
				function (results) {
					self.setPssDepartmentDataSelected(results, number, change);
					if(!change){
						self.hideWaitingWithFirstSearch();
					}
				}
				,
				function (error) {
					self.data[number]["pssDepartmentCodes"] = [];
					self.data[number]["pssDepartmentCode"] = null;
					if(!change){
						self.hideWaitingWithFirstSearch();
					}
				});
		}

		/**
		 * Selected Pss Department for each row.
		 * @param results - The list of department search by department and sub department
		 * @param number - The number of row
		 */
		self.setPssDepartmentDataSelected = function (results, number, change) {
			self.data[number]["pssDepartmentCodes"] = results;
			if(change){
				if(results != null && results != undefined && results.length > 0){
					self.data[number]["pssDepartmentCode"] = results[0];
				}else{
					self.data[number]["pssDepartmentCode"] = null;
				}

			}else{
				switch (number){
					case 0:
						if(self.itemMasterOnPage.pssDepartmentCodeOne){
							self.data[0]["pssDepartmentCode"] = self.findPssDepartmentByCode(self.itemMasterOnPage.pssDepartmentCodeOne, results);
						}
						break;
					case 1:
						if(self.itemMasterOnPage.pssDepartmentCodeTwo){
							self.data[1]["pssDepartmentCode"] = self.findPssDepartmentByCode(self.itemMasterOnPage.pssDepartmentCodeTwo, results);
						}
						break;
					case 2:
						if(self.itemMasterOnPage.pssDepartmentCodeThree){
							self.data[2]["pssDepartmentCode"] = self.findPssDepartmentByCode(self.itemMasterOnPage.pssDepartmentCodeThree, results);
						}
						break;
					case 3:
						if(self.itemMasterOnPage.pssDepartmentCodeFour){
							self.data[3]["pssDepartmentCode"] = self.findPssDepartmentByCode(self.itemMasterOnPage.pssDepartmentCodeFour, results);
						}
						break;
				}
			}
		}

		/**
		 * Find pss department code by pss department code in pss department list.
		 * @param pssDeptCode - The pss department code
		 * @param pssDepartmentCodes - The list of pss department.
		 * @returns {*}
		 */
		self.findPssDepartmentByCode = function (pssDeptCode, pssDepartmentCodes) {
			var pssDepartmentCode;
			if(pssDepartmentCodes != null && pssDepartmentCodes != undefined && pssDepartmentCodes.length > 0){
				for(var i=0;i<pssDepartmentCodes.length;i++) {
					if(pssDepartmentCodes[i] && pssDepartmentCodes[i].key.id == pssDeptCode){
						pssDepartmentCode = pssDepartmentCodes[i];
						break;
					}
				}
			}
			return pssDepartmentCode;
		}

		/**
		 * Find Department by department and sub department in list of department.
		 * @param departmentNbr - The department number.
		 * @param subDepartment - The sub department code.
		 * @param departments - The list of department.
		 * @returns {*}
		 */
		self.findDepartmentByDeptAndSubDept = function (departmentNbr, subDepartment, departments) {
			var department;
			for(var i=0;i<departments.length;i++) {
				if(departments[i] && Number(departments[i].key.department) == departmentNbr && departments[i].key.subDepartment == subDepartment){
					department = departments[i];
					break;
				}
			}
			return department;
		}

		/**
		 * Set Department each row.
		 */
		self.setDepartmentDataSelected = function () {
			//Set department selected each row
			if(self.itemMasterOnPage.departmentIdOne && self.itemMasterOnPage.subDepartmentIdOne){
				var department = self.findDepartmentByDeptAndSubDept(self.itemMasterOnPage.departmentIdOne, self.itemMasterOnPage.subDepartmentIdOne, self.departments);
				self.data[0]["department"] = department;
				if(department && department.key.department != '0' && department.key.subDepartment != ''){
					self.numberRequest++;
					self.findPssDepartmentByDepartment(department.key.department, department.key.subDepartment, 0, false);
				}
			}
			if(self.itemMasterOnPage.departmentIdTwo && self.itemMasterOnPage.subDepartmentIdTwo){
				var department = self.findDepartmentByDeptAndSubDept(self.itemMasterOnPage.departmentIdTwo, self.itemMasterOnPage.subDepartmentIdTwo, self.departments);
				self.data[1]["department"] = department;
				if(department && department.key.department != '0' && department.key.subDepartment != ''){
					self.numberRequest++;
					self.findPssDepartmentByDepartment(department.key.department, department.key.subDepartment, 1, false);
				}
			}
			if(self.itemMasterOnPage.departmentIdThree, self.itemMasterOnPage.subDepartmentIdThree){
				var department = self.findDepartmentByDeptAndSubDept(self.itemMasterOnPage.departmentIdThree, self.itemMasterOnPage.subDepartmentIdThree, self.departments);
				self.data[2]["department"] = department;
				if(department && department.key.department != '0' && department.key.subDepartment != ''){
					self.numberRequest++;
					self.findPssDepartmentByDepartment(department.key.department, department.key.subDepartment, 2, false);
				}
			}
			if(self.itemMasterOnPage.departmentIdFour, self.itemMasterOnPage.subDepartmentIdFour){
				var department =  self.findDepartmentByDeptAndSubDept(self.itemMasterOnPage.departmentIdFour, self.itemMasterOnPage.subDepartmentIdFour, self.departments);
				self.data[3]["department"] = department;
				if(department && department.key.department != '0' && department.key.subDepartment != ''){
					self.numberRequest++;
					self.findPssDepartmentByDepartment(department.key.department, department.key.subDepartment, 3, false);
				}
			}
		}

		/**
		 * Department changing handle.
		 * @param department - The department new
		 * @param index - The row number request change.
		 */
		self.departmentChangedHandle = function (department, index) {
			if(department){
				self.findPssDepartmentByDepartment(department.key.department, department.key.subDepartment, index, true);
			}else{
				self.data[index]["pssDepartmentCodes"] = results;
				self.data[index]["pssDepartmentCode"] = null;
			}
		}

		/**
		 * This method checks to see if any of the information of warehouse item locations have not been changed.
		 *
		 * @returns {boolean} Return true if any of the information of warehouse item locations have been changed.
		 */
		self.dataIsChanged = function () {
			if(angular.toJson(self.data) !== angular.toJson(self.dataOrig)){
				$rootScope.contentChangedFlag = true;
				return true;
			}
			$rootScope.contentChangedFlag = false;
			return false;
		};

		/**
		 * This method to return back the value have been changed to old value.
		 */
		self.resetDepartmentData = function () {
			self.data = angular.copy(self.dataOrig);
		}

		/**
		 * This method to delete data in row
		 */
		self.deleteDepartmentRow = function (index) {
			if(self.data[index].merchandiseType){
				self.data[index].merchandiseType = null;
			}
			if(self.data[index].pssDepartmentCode){
				self.data[index].pssDepartmentCode = null;
			}
			if(self.data[index].department){
				self.data[index].department = null;
			}
		}

		/**
		 * This method to update department information.
		 */
		self.updateDepartmentData = function () {
			self.error = '';
			self.success = '';
			var dataChanged = self.getDataChangedToSave();
			if(dataChanged){
				self.isLoading = true;
				productCasePackApi.updateDepartment(dataChanged,
					//success case
					function (results) {
						self.success = results.message;
						//Research Data
						self.findItemMaster();
					},
					//error case
					function(results){
						self.fetchError(results)
					}
				);
			}
		}

		/**
		 * This method to return a list of department, that have been changed.
		 */
		self.getDataChangedToSave = function () {
			var dataChanged = [];
			var departmentEmpty = {};
			departmentEmpty["key"] = {};
			departmentEmpty["key"]["department"] = "0";
			departmentEmpty["key"]["subDepartment"] = " ";
			var pssDepartmentCodeEmpty = {};
			pssDepartmentCodeEmpty["key"] = {};
			pssDepartmentCodeEmpty["key"]["id"] = "0";
			var merchandiseTypeEmpty = {};
			merchandiseTypeEmpty["id"] = " ";

			for(var i = 0; i< self.data.length; i++){
				if(angular.toJson(self.data[i]) != angular.toJson(self.dataOrig[i])){
					var pssDepartment = {};
					//Set index of departmetn
					pssDepartment["index"] = i;
					//Set item master key to update department information.
					pssDepartment["itmId"] = self.itemMasterOnPage.key.itemCode;
					pssDepartment["itmTypCd"] = self.itemMasterOnPage.key.itemType;
					//Set department information updated.
					if(self.data[i].department) {
						pssDepartment["subDepartment"] = self.data[i].department;
					}else{
						pssDepartment["subDepartment"] = departmentEmpty;
					}
					//Set pss department code information updated.
					if(self.data[i].pssDepartmentCode){
						pssDepartment["pssDepartmentCode"] = self.data[i].pssDepartmentCode;
					}else{
						pssDepartment["pssDepartmentCode"] = pssDepartmentCodeEmpty;
					}
					//Set merchandise Type information updated
					if(self.data[i].merchandiseType){
						pssDepartment["merchandiseType"] = self.data[i].merchandiseType;
					}else{
						pssDepartment["merchandiseType"] = merchandiseTypeEmpty;
					}
					dataChanged.push(pssDepartment);
				}
			}
			return dataChanged;
		}

		/**
		 * This method check has department
		 * @param index
		 * @returns {boolean}
		 */
		self.checkHasDepartment = function (index) {
			var hasData = false;
			if(self.data[index].merchandiseType || self.data[index].pssDepartmentCode || self.data[index].department){
				hasData = true;
			}
			return hasData;
		}
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};

		/**
		 * Show Department attribute Changed Log.
		 */
		self.showDepartmentAuditInfo = function(){
			self.isWaitingGetDepartmentAttributeAudit = true;
			//self.getDisplayNameInformation();
			productCasePackApi.getDepartmentAttributesAudits(
				{
					itemCode: self.itemMaster.key.itemCode,
					itemType: self.itemMaster.key.itemType
				},
				//success case
				function (results) {
					self.departmentAttributeAudits = results;
					self.initDepartmentAttributeAuditTable();
					self.isWaitingGetDepartmentAttributeAudit = false;
				}, self.fetchError);

			$('#department-attribute-log-modal').modal({backdrop: 'static', keyboard: true});
		}
		/**
		 * Init data Department attribute Audit.
		 */
		self.initDepartmentAttributeAuditTable = function () {
			$scope.filter = {
				attributeName: '',
			};
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			$scope.departmentAttributeAuditTableParams = new ngTableParams({
				page: 1,
				count: 10,
				filter: $scope.filter,
				sorting: $scope.sorting

			}, {
				counts: [],
				data: self.departmentAttributeAudits
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
		 * Auto filter for search field
		 * @param search
		 */
		self.autoFilterForSearchDepartmentField = function(search){
			var output=[];
			var outputResult=[];
			angular.forEach(self.departmentsOrg , function (element) {
				if(element.displayName.toLowerCase().indexOf(search.toLowerCase())>=0){
					if(element.displayName.toLowerCase().indexOf(search.toLowerCase())==0){
						outputResult.push(element);
					}else{
					output.push(element);
				}
				}
			});
			// angular.forEach(output , function (item) {
			// 	outputResult.push(item)
			// });
			outputResult= outputResult.concat(output);
			//outputResult.pushValues(output);

			self.departments = outputResult;
		}
		/**
		 * Auto filter for search field
		 * @param search
		 */
		self.autoFilterForSearchMerchandiseField = function(search){
			var output=[];
			var outputResult=[];
			angular.forEach(self.merchandiseTypesOrg , function (element) {
				if(element.displayName.toLowerCase().indexOf(search.toLowerCase())>=0){
					if(element.displayName.toLowerCase().indexOf(search.toLowerCase())==0){
						outputResult.push(element);
					}else{
					output.push(element);
				}
				}
			});
			// angular.forEach(output , function (item) {
			// 	outputResult.push(item)
			// });
			outputResult= outputResult.concat(output);
			self.merchandiseTypes = outputResult;
		}
		/**
		 * Auto filter for search field
		 * @param search
		 */
		self.autoFilterForSearchPssField = function(search, index){
			var output=[];
			var outputResult=[];
			angular.forEach( self.dataOrig[index].pssDepartmentCodes , function (element) {
				if(element.displayName.toLowerCase().indexOf(search.toLowerCase())>=0){
					if(element.displayName.toLowerCase().indexOf(search.toLowerCase())==0){
						outputResult.push(element);
					}else{
					output.push(element);
				}

				}
			});
			// angular.forEach(output , function (item) {
			// 	outputResult.push(item)
			// });
			outputResult= outputResult.concat(output);
			self.data[index].pssDepartmentCodes = outputResult;
		}
	}
})();
