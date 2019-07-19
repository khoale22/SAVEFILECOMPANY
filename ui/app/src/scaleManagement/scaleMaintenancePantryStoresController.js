/*
 * scaleMaintenancePantryStoresController.js
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the scale maintenance status check of ePlum transactions.
 *
 * @author m314029
 * @since 2.18.3
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('ScaleMaintenancePantryStoresController', scaleMaintenancePantryStoresController);


	scaleMaintenancePantryStoresController.$inject = ['$stateParams', 'ngTableParams', 'ScaleMaintenanceApi', '$scope', '$interval', 'DownloadService', 'urlBase']; 

	/**
	 * Creates the controller for the scale maintenance loads.
	 */
	function scaleMaintenancePantryStoresController($stateParams, ngTableParams, scaleMaintenanceApi, $scope, $interval, downloadService, urlBase){

		var self = this;
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;
		self.isDetailsShowing = false;
        self.isAddShowing = false;
        self.loadSuccess = false;
		
		
		/**
		 * Whether or not a user is adding an ingredient statement.
		 *
		 * @type {boolean}
		 */
		
		self.storeNo = null;
        self.storeDesc = null;
        self.storeToEdit= null
		
		/**
		 * selected store id
		 * @type {number}
		 */
		self.selectedStoreId = null;
        self.storeToDelete =  null;
        self.success = null;
		
        var firstSearch = true;
        var firstResult = true;
		const PAGE_SIZE = 10;
		const FIRST_PAGE = 1;
		var detailsNeeded = "pantryStr";
		
		
		self.columns = [
            {
				title: 'Action',
				field: 'action',
				visible: true},	
			{
				title: 'Store No',
				field: 'storeNum',
				visible: true},
			{
				title: 'Store Description',
				field: 'store Desc',
				visible: true}
			/*{
				title: 'Dept 1',
				field: 'deptId1',
				visible: true},
			{
				title: 'Dept 2',
				field: 'deptId2',
				visible: true},
			{
				title: 'Dept 3',
				field: 'deptId3',
				visible: true},
			{
				title: 'Dept 4',
				field: 'deptId4',
				visible: true},
			{
				title: 'Dept 5',
				field: 'deptId5',
				visible: true},
			{
				title: 'Dept 6',
				field: 'deptId6',
				visible: true},
			{
				title: 'Dept 7',
				field: 'deptId7',
				visible: true},
			{
				title: 'Dept 8',
				field: 'deptId8',
				visible: true},
			{
				title: 'Dept 9',
				field: 'deptId9',
				visible: true},
			{
				title: 'Dept 10',
				field: 'deptId10',
				visible: true},
			{
				title: 'Dept 11',
				field: 'deptId11',
				visible: true},
			{
				title: 'Dept 12',
				field: 'deptId12',
				visible: true},
			{
				title: 'Dept 13',
				field: 'deptId 13',
				visible: true}*/
			
			];
		
		
		
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
            self.isWaiting = true; 
            self.tableParams = buildTable();
        }


        /**
		 * Constructs the ng-table.
		 */
		function buildTable() {
			return new ngTableParams(
				{
					page: FIRST_PAGE,
					count: PAGE_SIZE
				}, {
					counts: [],
					getData: function ($defer, params) {
						self.data = null;
						self.isWaiting = true;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.includeCounts = false;
						if (firstSearch) {
							self.includeCounts = true;
							firstSearch = false;
						}
						getStoreDepartmentData(params.page()-1);
					}
				}
			)
		}
        

        /**
		 * Get tracking info from database with page number
		 * @param page
		 */
		function getStoreDepartmentData(page) {
				scaleMaintenanceApi.findAllStores({
					includeCount: self.includeCounts,
					page: page,
					pageSize: PAGE_SIZE,
					detailsNeeded: detailsNeeded
				}).$promise.then(loadData).catch(function(error) {
					fetchError(error);
				}).finally(function(){
					PMCommons.displayLoading(self, false);
				});
		}
		
		/**
		 * Callback for a successful call to get list tracking data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		function loadData (results) {
			self.isWaiting = false;
			self.error = null;
            firstSearch = false;
			// If this was the first page, it includes record count and total pages .
			if (firstResult) { 
                firstResult = false;
                self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.dataResolvingParams.data = [];
				buildErrorWithMessage("No records found.");
			} else {
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
        }

		/**
		 * Adds an dept to the ingredient store.
		 *
		 * @param index the index of the dept.
		 */
		self.addDept = function(index){
			self.loadSuccess = false;
	        self.success = null;
			//addDepartment(index);
        };


        /**
		 * Adds an dept to the ingredient store.
		 *
		 * @param index the index of the dept.
		 */
		/*function addDepartment(index) {           
			if(self.selectedStoreDetail == null) {
				self.selectedStoreDetail = {storeDetails: []};
			} else if(self.selectedStoreDetail.storeDepartmentDetails == null) {
				self.selectedStoreDetail.storeDepartmentDetails = [];
            }
            if(self.selectedStoreDetail.storeDepartmentDetails.length == 13) {
                 self.loadSuccess = true;
                 self.success = "Store can be mapped with 13 Departments";
            }else{
                self.selectedStoreDetail.storeDepartmentDetails.splice(index, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[index].disabled = false;
            }
        };*/
        
        

        /*self.removeDepartment = function(index){
            self.loadSuccess = false;
            self.success = null;
			if(self.selectedStoreDetail != null) {
				if(self.selectedStoreDetail.storeDepartmentDetails != null){
					self.selectedStoreDetail.storeDepartmentDetails.splice(index, 1);
				}
			}
		};*/
		
		/**
		 * Whether an Department has not been filled in, so user cannot add another Department.
		 *
		 * @returns {boolean}
		 */
		/*self.isAnyDeptNull = function(){
			return false;
		};*/
		/*self.isAnyDeptNull = function(){
			if(self.selectedStoreDetail != null) {
				if(self.selectedStoreDetail.storeDepartmentDetails != null){
					if(self.selectedStoreDetail.storeDepartmentDetails.length > 0) {
						for (var x = 0; x < self.selectedStoreDetail.storeDepartmentDetails.length; x++) {
                            var obj = self.selectedStoreDetail.storeDepartmentDetails[x];
							if(obj == null || obj.deptId == null || obj.deptId.length==0 ){
								return true;
							}
						}
					}
				}
			}
			return false;
        };
        */
        /**
		 * Whether required Department statement information has been filled in or not.
		 *
		 * @returns {boolean}
		 */
		/*self.isRequiredDepartmentInfoNotFilled = function(){
			return (self.selectedStoreDetail == null || self.isAnyDeptNull()
			|| self.selectedIngredientStatement.statementNumber == null);
		};
*/
        

        
       /**
		 * Builds error message object, then calls fetch error function.
		 *
		 * @param message Error message to display to user.
		 */
		function buildErrorWithMessage (message) {
			fetchError({data: {message: message}});
        }


		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		function fetchError(error) {
			self.isWaiting = false;
			if (error && error.data) {
				if(error.data.message) {
					setError(error.data.message);
				} else {
					setError(error.data.error);
				}
			}
			else {
				setError("An unknown error occurred.");
			}
		}

		/**
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		function setError(error) {
			self.error = error;
		}
		
		/**
		 * go to factory detail page, show more information for user. User an edit, add new on here.
		 * @param tab
		 * @param item
		 */
		self.addStoreDeptDetails = function () {
            self.storeToEdit = {};
            self.loadSuccess = false;
            self.success = null;
            self.isAddShowing = true;
			self.selectedStoreDetail = {};
		};
		
		
		
		self.editStoreDeptDetails = function (data) {
            self.loadSuccess = false;
            self.success = null;
            self.selectedStoreDetail = {};
            //self.selectedStoreDetail.storeDepartmentDetails = [];
            self.selectedStoreDetail.storeNo = data.storeNum;
            self.selectedStoreDetail.storeDesc = data.storeDesc;
           /*if(data.deptId1.length != 0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(0, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[0].deptId =  data.deptId1;
                self.selectedStoreDetail.storeDepartmentDetails[0].disabled = true;
                  
            }
            if(data.deptId2.length !=  0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(1, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[1].deptId =  data.deptId2;
                self.selectedStoreDetail.storeDepartmentDetails[1].disabled = true;
            }
            if(data.deptId3.length !=  0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(2, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[2].deptId =  data.deptId3;
                self.selectedStoreDetail.storeDepartmentDetails[2].disabled = true;
            }
            if(data.deptId4.length != 0){
                 self.selectedStoreDetail.storeDepartmentDetails.splice(3, 0, {key: {storeNo: 0}});
                 self.selectedStoreDetail.storeDepartmentDetails[3].deptId =  data.deptId4;
                 self.selectedStoreDetail.storeDepartmentDetails[3].disabled = true;
            }
            if(data.deptId5.length !=  0){
                 self.selectedStoreDetail.storeDepartmentDetails.splice(4, 0, {key: {storeNo: 0}});
                 self.selectedStoreDetail.storeDepartmentDetails[4].deptId =  data.deptId5;
                 self.selectedStoreDetail.storeDepartmentDetails[4].disabled = true;
            }
            if(data.deptId6.length !=  0){
                 self.selectedStoreDetail.storeDepartmentDetails.splice(5, 0, {key: {storeNo: 0}});
                 self.selectedStoreDetail.storeDepartmentDetails[5].deptId =  data.deptId6;
                 self.selectedStoreDetail.storeDepartmentDetails[5].disabled = true;
            }
            if(data.deptId7.length !=  0){
                 self.selectedStoreDetail.storeDepartmentDetails.splice(6, 0, {key: {storeNo: 0}});
                 self.selectedStoreDetail.storeDepartmentDetails[6].deptId =  data.deptId7;
                 self.selectedStoreDetail.storeDepartmentDetails[6].disabled = true;
            }
            if(data.deptId8.length !=  0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(7, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[7].deptId =  data.deptId8;
                self.selectedStoreDetail.storeDepartmentDetails[7].disabled = true;
            }
            if(data.deptId9.length !=  0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(8, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[8].deptId =  data.deptId9;
                self.selectedStoreDetail.storeDepartmentDetails[8].disabled = true;
            }
            if(data.deptId10.length !=  0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(9, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[9].deptId =  data.deptId10;
                self.selectedStoreDetail.storeDepartmentDetails[9].disabled = true;
            }
            if(data.deptId11.length !=  0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(10, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[10].deptId =  data.deptId11;
                self.selectedStoreDetail.storeDepartmentDetails[10].disabled = true;
            }
            if(data.deptId12.length !=  0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(11, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[11].deptId =  data.deptId12;
                self.selectedStoreDetail.storeDepartmentDetails[11].disabled = true;
            }
            if(data.deptId13.length !=  0){
                self.selectedStoreDetail.storeDepartmentDetails.splice(12, 0, {key: {storeNo: 0}});
                self.selectedStoreDetail.storeDepartmentDetails[12].deptId =  data.deptId13;
                self.selectedStoreDetail.storeDepartmentDetails[12].disabled = true;
            }*/
            self.storeToEdit = angular.copy(self.selectedStoreDetail);
            self.isDetailsShowing = true;

        };
		
		self.saveStoreDeptDetails = function () {
            var deptAvailable = true;
            self.loadSuccess = false;
			self.isWaiting = true;
            self.error = null;
            /*if(self.selectedStoreDetail != null && self.selectedStoreDetail.storeDepartmentDetails != null && 
                self.selectedStoreDetail.storeDepartmentDetails.length > 0 ){
                    for (var x = 0; x < self.selectedStoreDetail.storeDepartmentDetails.length; x++) {
                        var obj = self.selectedStoreDetail.storeDepartmentDetails[x];
                        if(obj == null || obj.deptId == null || obj.deptId.length == 0){
                            deptAvailable = false;
                        }
                    }
            }else{
                 deptAvailable = false;
            }*/
            
            if(self.selectedStoreDetail.storeNo == null){
                self.loadSuccess = true;
                self.success = "Store no should be entered";
            }/*else if(!deptAvailable){
                self.loadSuccess = true;
                if(self.selectedStoreDetail.storeDepartmentDetails.length >1){
                    self.success = "Department Id should be entered";
                }else{
                    self.success = "Atleast  one department should be entered";
                }
                if(self.selectedStoreDetail.storeDepartmentDetails.length == 0){
                	addDepartment(0);
                }
               
            }else{
                if(angular.toJson(self.storeToEdit) == angular.toJson(self.selectedStoreDetail)){
                    self.loadSuccess = true;
                    self.success = "No changes made to update";
			    }*/else{
                     saveDetails();
                } 
           // }
        };
        
        /**
		 * Get tracking info from database with page number
		 * @param page
		 */
		function saveDetails() {
          /* var  deptId1 = "";
	       var  deptId2 = "";
	       var  deptId3 = "";
           var  deptId4 = "";
	       var  deptId5 = "";
           var  deptId6 = "";
           var  deptId7 = "";
	       var  deptId8 = "";
           var  deptId9 = "";
           var  deptId10 = "";
	       var  deptId11 = "";
           var  deptId12 = "";
           var  deptId13 = "";*/
           var  updateFlag = "pantryStr";
           var typCdFlag= "pantryStr";
          // var detailsNeeded="pantryStr";
           /* if(self.selectedStoreDetail != null) {
				if(self.selectedStoreDetail.storeDepartmentDetails != null){
					if(self.selectedStoreDetail.storeDepartmentDetails.length > 0) {
						for (var x = 0; x < self.selectedStoreDetail.storeDepartmentDetails.length; x++) {
                            var obj = self.selectedStoreDetail.storeDepartmentDetails[x];
							if(obj != null && obj.deptId != null){
								if(x == 0){
                                  deptId1 =   obj.deptId;
                                }else if(x == 1){
                                  deptId2 =   obj.deptId;
                                }else if(x == 2){
                                  deptId3 =   obj.deptId;
                                }else if(x == 3){
                                  deptId4 =   obj.deptId;
                                }else if(x == 4){
                                  deptId5 =   obj.deptId;
                                }else if(x == 5){
                                  deptId6 =   obj.deptId;
                                }else if(x == 6){
                                  deptId7 =   obj.deptId;
                                }else if(x == 7){
                                  deptId8 =   obj.deptId;
                                }else if(x == 8){
                                  deptId9 =   obj.deptId;
                                }else if(x == 9){
                                  deptId10 =   obj.deptId;
                                }else if(x == 10){
                                  deptId11 =   obj.deptId;
                                }else if(x == 11){
                                  deptId12 =   obj.deptId;
                                }else if(x == 12){
                                  deptId13 =   obj.deptId;
                                }

							}
						}
					}
				}
            }*/
            if(self.isDetailsShowing){
                updateFlag = "true";
            }
            var parameters = {
				storeNum: self.selectedStoreDetail.storeNo,
                /*deptId1:deptId1,
                deptId2:deptId2,
                deptId3:deptId3,
                deptId4:deptId4,
                deptId5:deptId5,
                deptId6:deptId6,
                deptId7:deptId7,
                deptId8:deptId8,
                deptId9:deptId9,
                deptId10:deptId10,
                deptId11:deptId11,
                deptId12:deptId12,
                deptId13:deptId13,*/
                update: updateFlag,
                typCd: typCdFlag,
                //detailsNeeded:"pantryStr"
            };

             scaleMaintenanceApi.addStores(
				    parameters,
				    function(results){
					   reloadStoreData(results)
				},
				fetchError)
        }
        
         function reloadStoreData (results) {
              self.isWaiting = false;
              self.loadSuccess = true;
              self.success = results.responseMessage;
              self.storeToEdit  = angular.copy(self.selectedStoreDetail);
              if(self.isAddShowing){
            	  self.isAddShowing = false;
                  self.includeCounts = true;
                  self.tableParams.page(1);
                  getStoreDepartmentData(0);
              }else{
            	  if(self.selectedStoreDetail != null) {
      				/*if(self.selectedStoreDetail.storeDepartmentDetails != null){
      					if(self.selectedStoreDetail.storeDepartmentDetails.length > 0) {
      						for (var x = 0; x < self.selectedStoreDetail.storeDepartmentDetails.length; x++) {
                                  self.selectedStoreDetail.storeDepartmentDetails[x].disabled = true;
                              }
                    
                           }      
                      }   */
                  }  
              }  
         } 
		
		self.resetStoreDeptDetails = function () {
            self.loadSuccess = false;
            self.success = null;
            self.error = null;
            if(angular.toJson(self.storeToEdit) == angular.toJson(self.selectedStoreDetail)){
                 self.loadSuccess = false;
                 self.success = "No changes made to update";
            }else{
                if(self.isAddShowing){
                    self.selectedStoreDetail = {}; 
                }else{
                    self.selectedStoreDetail = angular.copy(self.storeToEdit);
                }
            }
		};
		
		self.returnStoreDeptDetails = function () {
            self.storeToEdit = null;
			self.isAddShowing = false;
            self.isDetailsShowing = false;
            self.loadSuccess = false;
            self.success = null;
            self.error = null;
            self.includeCounts = true;
            self.tableParams.page(1);
            getStoreDepartmentData(0);
		};
		
		self.deleteStoreDeptDetails = function(attribute) {
		    self.storeToDelete = angular.copy(attribute);
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
		};
		
		

		/**
		 * Do delete factory action.
		 */
		self.doDeleteStore = function() {
            $('#confirmModal').modal("hide");
             self.loadSuccess = false;
             scaleMaintenanceApi.deleteStore({
				storeNum: self.storeToDelete.storeNum,
				detailsNeeded: detailsNeeded 
			 }).$promise.then(reloadDeletedData).catch(function(error) {
				fetchError(error);
			 });   
        };

        function reloadDeletedData (result) {
             self.loadSuccess = true;
             self.success = result.responseMessage;
             self.includeCounts = true;
             self.tableParams.page(1);
             getStoreDepartmentData(0);
        } 
        
        /**
		 * Do delete factory action.
		 */
		self.getStoreDescription = function() {
            if(self.selectedStoreDetail.storeNo != null){
                self.loadSuccess = false;
                self.success = null;
                self.selectedStoreDetail.storeDesc = null;
                  scaleMaintenanceApi.getStoreDesc({
				    storeNum: this.selectedStoreDetail.storeNo,
				    detailsNeeded:"pantryStr"
			        }).$promise.then(displayStoreDescription).catch(function(error) {
				        fetchError(error);
			        });
            };
        }


        function displayStoreDescription(result) {
             if(result.storeDesc != null){
              //  self.selectedStoreDetail.storeDepartmentDetails = [];
                self.selectedStoreDetail.storeDesc = result.storeDesc;
                self.loadSuccess = false;
                self.success = null;
               // addDepartment(0); 
             }else{
                 self.loadSuccess = true;
                 self.success = result.responseMessage;
             }
             
        }

        /**
		 * Data is changed.
		 * @returns {boolean}
		 */
		self.isDataChanged = function () {
			if(angular.toJson(self.storeToEdit) != angular.toJson(self.selectedStoreDetail)){
                return true;
			}
			return false; 
        }
		
		
		/*self.isDuplicateDepartment = function (inputDeptId,index) {
            var duplicateDept = false;
            if(self.selectedStoreDetail != null) {
                if(self.selectedStoreDetail.storeDepartmentDetails != null){
                    if(self.selectedStoreDetail.storeDepartmentDetails.length > 0) {
                        for (var x = 0; x < self.selectedStoreDetail.storeDepartmentDetails.length; x++) {
                        	if(x != index){
                                 if(self.selectedStoreDetail.storeDepartmentDetails[x].deptId == inputDeptId){
            		                duplicateDept  = true;
            	                }
                        	}
                        }
                   }      
               }
            } 
            
            if(duplicateDept){
            	self.selectedStoreDetail.storeDepartmentDetails[index].deptId = null;
            	self.loadSuccess = true;
                self.success = "Duplicate Department Id "+inputDeptId;
            }
            
       } */
		
        
	}
})();
