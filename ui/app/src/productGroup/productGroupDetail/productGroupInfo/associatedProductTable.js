
/*
 *   associatedProductTable.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * eCommerce View Component.
 *
 * @author vn87351
 * @since 2.14.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.directive("bindCompiledHtml", bindCompiledHtml);
	app.directive("disableClickOnRow", disableClickOnRow);
	bindCompiledHtml.$inject = [];

	function bindCompiledHtml(){
		var directive = {
			restrict: "A",
			controller: bindCompiledHtmlController
		};
		return directive;
	}
	function disableClickOnRow($parse) {
		return {
			scope: {
				disableClickOnRow: '='
			},
			restrict: 'A',
			link: function (scope, element, attrs, ctrl) {
				element.on('focus', function(event) {
					if( attrs.disableClickOnRow==true) {
						event.preventDefault();
					}
				});
			}
		};
	}
	bindCompiledHtmlController.$inject = ["$scope", "$element", "$attrs", "$compile"];
	function bindCompiledHtmlController($scope, $element, $attrs, $compile){
		$scope.$watch($attrs.bindCompiledHtml, compileHtml);

		function compileHtml(html){
		//	debugger;
			var compiledElements = $compile(html)($scope);
			$element.append(compiledElements);
		}
	}
	app.component('associatedProductTable', {
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productGroup/productGroupDetail/productGroupInfo/associatedProductTable.html',
		bindings: {
			product: '&',
			productGroupType: '<',
			isWaitLoadAssociatedProductDetail:'=',
			showAssociatedProductDetailsByIndex:'&',
			isNotDuplicateProductIdOrUpc:'&',
			showMessageForConflictProductId:'&',
			productGroupData:'<',
			productGroupDataTemp:'<',
			isNew:'<',
			currentAssociatedProductPosition:'<',
			showErrorMessageOnPopup:'&',
			clearProductError:'=',
			hierarchyData:'<',
			primaryPath:'<',
			showMessage:'&'
		},
		// The controller that handles our component logic
		controller: AssociatedProductTable
	});


	AssociatedProductTable.$inject = ['$rootScope', '$scope', 'ngTableParams', 'codeTableApi', 'productGroupApi', 'ProductGroupService'];

	/**
	 * product group detail- product associated product table controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @param codeTableApi
	 * @param ngTableParams
	 * @constructor
	 */
	function AssociatedProductTable($rootScope, $scope, ngTableParams, codeTableApi, productGroupApi, productGroupService) {
		/** All CRUD operation controls of product group associated table goes here */

		var self = this;
		/**
		 * The selected associated product, it used to listen event that sent from next/previous button is clicked.
		 */
		const PRODUCT_ID_KEY = '_productID_';
		/**
		 * Message confirm close.
		 */
		self.MESSAGE_CONFIRM_CLOSE="The choices associated with the product "+PRODUCT_ID_KEY+" will be removed as it is not part of any product group";
		/**
		 * Const for selected Associated Product
		 */
		const SELECTED_ASSOCIATED_PRODUCT = 'selectedAssociatedProduct';
		/**
		 * Whether or not the controller is waiting for data
		 */
		self.isWaiting = false;
		/**
		 * Flag check all product
		 */
		self.checkAll = false;
		/**
		 * Action code Add
		 */
		const ACTION_ADD = 'A';
		/**
		 * Action code delete
		 */
		const ACTION_DELETE = 'D';
		/**
		 * Action code Update
		 */
		const ACTION_UPDATE = '';
		/**
		 * Message Successfully Updated
		 */
		const MESS_SUCCESSFULLY_UPDATE = 'Successfully Updated.'
		/**
		 * two column default for dynamic table
		 * @type {[*]}
		 */
		self.colsTemplate = [ {
			field: "checked",
			title: "Name",
			show: true,	class:'col1',
			getValue: renderedInputCheckBox,
			inputType: "checkbox",
			headerTemplateURL: "src/productGroup/productGroupDetail/productGroupInfo/headerCheckbox.html"
		}, {
			field: "productId",
			title: "Product Id",
			show: true,	class:'col2',
			getValue: renderedInput,
			inputType: "text",
			headerTemplateURL: "src/productGroup/productGroupDetail/productGroupInfo/headerDefault.html"
		}];
		/**
		 * The number of records to show on the associated Product Group results.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 10;
		/**
		 * Start position of page that want to show on associated Product Group results.
		 *
		 * @type {number}
		 */
		self.PAGE = 1;
		/**
		 * init function
		 */
		self.init = function(){
			if(self.product !== undefined){
			self.parseDataToTable();
			}
		};
		/**
		 * Parse data associated product to table
		 */
		self.parseDataToTable = function(){
			self.cols=angular.copy(self.colsTemplate);
			angular.forEach(self.product.dataHeader, function(value,key){
				var item = {
					field: value.choiceTypeCode,
					title: value.description,
					show: true,
					scope:true,
					getValue: renderedText,
					inputType: "text",
					headerTemplateURL: "src/productGroup/productGroupDetail/productGroupInfo/headerDefault.html"
				};
				self.cols[key+2] = item;

			});
			self.loadData(self.product.rows);
			self.tableParams.reload();
		}
		/**
		 * watcher for reload form event
		 */
		$scope.$on('reloadAssociatedProduct', function(event, product) {
			self.product=product;
			self.parseDataToTable();
			if(!self.isNew && productGroupService.getSelectedProductId()!==undefined && productGroupService.getSelectedProductId()!=null){
				var isExist = false;
				angular.forEach(self.productGroupData.dataAssociatedProduct.rows, function(value,key){
					if(value.productId!==undefined && value.productId!=null && value.productId!=='' && productGroupService.getSelectedProductId()===value.productId){
						isExist=true;
						self.showAssociatedProductDetailsByIndex({index:key});
					}
				});
				if(!isExist){
					self.showAssociatedProductDetailsByIndex({index:0});
				}
				productGroupService.setSelectedProductId(null);
			}
			else{
				self.showAssociatedProductDetailsByIndex({index:0});
			}
		});
		/**
		 * load data for table
		 * @param data
		 */
		self.loadData = function (data) {
			self.checkAll = false;
			angular.forEach(data, function (value, key) {
				if(value.productId!==undefined && value.productId>0){
					value.isDisabledUpdate = true;
				}
			});
			self.tableParams = new ngTableParams(
				{
					page: self.PAGE, /* show first page */
					count: self.PAGE_SIZE, /* count per page */
				},
				{
					counts: [],
					debugMode: true,
					data: data
				});
		};
		/**
		 * render input for dynamic table
		 * @param $scope
		 * @param row
		 * @returns {string}
		 */
		function renderedInput($scope, row) {
			var styles='';
			var blurEvent = '';
			var numericOnly = '';
			var ngDisabled = '';
			if(this.inputType=='text'){
				blurEvent = 'ng-blur="$ctrl.showAssociatedProductWhenLostFocus(row)"';
				numericOnly = 'numeric-only';
				ngDisabled = 'ng-disabled="$ctrl.isDisabledTextBox(row)"'
				styles = "border-color:#ccc !important;height:24px;";
			}
			return "<input  type='" + this.inputType + "' style='"+styles+"' class='form-control input-sm' ng-model='row[col.field]' "+ngDisabled+ ' ' +blurEvent+" "+numericOnly+ "/>";
		}
		/**
		 * render input for dynamic table
		 * @param $scope
		 * @param row
		 * @returns {string}
		 */
		function renderedInputCheckBox($scope, row) {
			return "<input  type='checkbox' class='form-control input-sm' ng-model='row[col.field]' ng-click='$ctrl.checkBoxItemChange(row)' style='height: 14px;'/> ";
		}
		/**
		 * render text dynamic for table associated
		 * @param $scope
		 * @param row
		 * @returns {string}
		 */
		function renderedText($scope, row) {
			return "<span ng-model='row[col.field]' >{{row[col.field]}}</span>";
		}
		/**
		 * Handle check all when check box item change
		 */
		self.checkBoxItemChange = function(row){
			self.checkAll = true;
			angular.forEach(self.product.rows, function (value, key) {
				if(!value.checked)
					self.checkAll = false;
			});
		}
		/**
		 *Backup selected product for detail product component
		 */
		self.selectProduct = function(product, isReload){
			$scope.selectedItem = product;
			self.showAssociatedProductDetail(isReload);
		};
		/**
		 * call product detail component to show detail
		 */
		self.showAssociatedProductDetail = function(isReload){
			var indexProduct=self.product.rows.indexOf($scope.selectedItem);
			self.PAGE=Math.ceil((indexProduct+1)/self.PAGE_SIZE);
			if(self.PAGE===0)
				self.PAGE=1;
			self.loadData(self.product.rows);
			self.tableParams.reload();
			$rootScope.$broadcast('changeAssociatedProduct',$scope.selectedItem.productId, $scope.selectedItem.optionCodes, $scope.selectedItem.defaultOptionList, isReload);

		}
		/**
		 * is active for row selected
		 * @param item
		 * @returns {boolean}
		 */
		$scope.isActive = function(item) {
			return $scope.selectedItem === item;
		};
		/**
		 * Watcher scope event for change associated product selected when click next or previous on the product details.
		 */
		$scope.$on(SELECTED_ASSOCIATED_PRODUCT, function(event, product, isReload) {
			if(self.isNotDuplicateProductIdOrUpc({product:product})){
				product.isDisabledUpdate = false;
				self.selectProduct(product, isReload);
			}else{
				self.showMessageForConflictProductId({product: product});
			}
		});
		/**
		 * This method will be called when occurs the lost focus on product id text box of associated product table.
		 *
		 * @param product the product object.
		 */
		self.showAssociatedProductWhenLostFocus = function(row){
			row.isDisabledUpdate = false;
			if(!self.isWaitLoadAssociatedProductDetail && row.productId !== undefined && row.productId !=null && row.productId !== ''){
				if(row.actionCode ===ACTION_ADD){
					if(self.isNew && (self.hierarchyData===undefined  || self.hierarchyData==null || self.hierarchyData.length===0)){
						self.clearProductError = true;
						self.showErrorMessageOnPopup({message:'Please assign customer hierarchy to the product group before adding products'});
					}else if(!self.isNew && (self.primaryPath===undefined
						|| self.primaryPath==null
						|| self.primaryPath==='')){
						self.clearProductError = true;
						self.showErrorMessageOnPopup({message:'Please assign customer hierarchy to the product group before adding products'});
					} else{
						row.isDisabledUpdate = true;
						//$scope.selectedItem = row;
						self.showAssociatedProductDetailsByIndex({index:self.product.rows.indexOf(row)});
					}
				}else{
					row.isDisabledUpdate = true;
					//	$scope.selectedItem = row;
					self.showAssociatedProductDetailsByIndex({index:self.product.rows.indexOf(row)});
				}

			}
		}
		/**
		 * item click event
		 * @param row
		 */
		self.itemClick = function (row) {
			if(!self.isWaitLoadAssociatedProductDetail){
				self.showAssociatedProductDetailsByIndex({index:self.product.rows.indexOf(row)});
			}

		}
		/**
		 * Returns disable or enable status for product id input text box.
		 * @param product the associated product.
		 */
		self.isDisabledTextBox = function(row){
			if(self.isWaitLoadAssociatedProductDetail)
				return true;
			return row.isDisabledUpdate;
		}
		/**
		 * Returns disable or enable button clear.
		 */
		self.isEnabledClearButton = function(){
			var isEnabled= false;
			angular.forEach(self.product.rows , function(value,key){
				if(value.checked)
					isEnabled=true;
			});
			return isEnabled;
		}
		/**
		 * do clear associated product
		 */
		self.doClearAssociatedProduct = function(){
			var dataClear = [];
			var lstProductId = [];
			angular.forEach(self.product.rows , function(value,key){
				if(value.checked && (value.productId===undefined ||  value.productId==null ||  value.productId==='')){
					value.checked = false;
					self.checkAll = false;
				}
				if(value.actionCode!==ACTION_ADD && value.checked && value.productId!==undefined &&  value.productId!=null &&  value.productId!==''){
					value.actionCode= ACTION_DELETE;
					lstProductId.push(value.productId);
					dataClear.push(value);
				}
				if(value.actionCode===ACTION_ADD && value.checked && value.productId!==undefined && value.productId!==''){
					delete value.productId;
					value.isDisabledUpdate = false;
					value.checked = false;
					self.checkAll = false;
				}

			});
			if(dataClear.length>0){
				self.messagePopup = self.MESSAGE_CONFIRM_CLOSE.replace(PRODUCT_ID_KEY, lstProductId);
				$('#confirmationModalSave').modal({backdrop: 'static', keyboard: true});
			}
		}
		/**
		 * clear associated product
		 */
		self.clearAssociatedProduct = function(){
			$('#confirmationModalSave').modal("hide");
			productGroupService.setSelectedProductId(null);
			self.showAssociatedProductDetail(false);
			var productGroupDataTmp = {};
			productGroupDataTmp.customerProductGroup = {};
			productGroupDataTmp.customerProductGroup.custProductGroupId=self.productGroupData.customerProductGroup.custProductGroupId;
			productGroupDataTmp.customerProductGroup.productGroupTypeCode=self.productGroupData.customerProductGroup.productGroupTypeCode;
			var dataClear = [];
			angular.forEach(self.product.rows , function(value,key){
				if(value.checked && (value.productId===undefined ||  value.productId==null ||  value.productId==='')){
					value.checked = false;
					self.checkAll = false;
				}
				if(value.actionCode!==ACTION_ADD && value.checked && value.productId!==undefined &&  value.productId!=null &&  value.productId!==''){
					value.actionCode= ACTION_DELETE;
					dataClear.push(value);
				}

				if(value.actionCode===ACTION_ADD && value.checked && value.productId!==undefined && value.productId!==''){
					delete value.productId;
					value.isDisabledUpdate = false;
					value.checked = false;
					self.checkAll = false;
				}
			});
			if(dataClear.length>0){
				self.isWaiting = true;
				productGroupDataTmp.dataAssociatedProduct = {};
				productGroupDataTmp.dataAssociatedProduct.rows= dataClear;
				productGroupApi.deleteAssociatedProduct(productGroupDataTmp,self.callApiSuccess,self.callApiError );
			}
		}
		/**
		 * Processing if call api success
		 * @param results
		 */
		self.callApiSuccess = function (response) {
			self.isWaiting = false;
			angular.forEach(self.product.rows , function(data){
				if(data.checked){
					data.actionCode= ACTION_ADD;
					delete data.productId;
					data.isDisabledUpdate = false;
				}
				data.checked = false;
			});
			self.checkAll = false;
			self.productGroupDataTemp.dataAssociatedProduct = angular.copy(self.productGroupData.dataAssociatedProduct);
			self.productGroupData.lstProductDetails = [];
			self.productGroupData.lstProductDetailsTemp = [];
			self.showMessage({error:'',success:MESS_SUCCESSFULLY_UPDATE});
			self.showAssociatedProductDetailsByIndex({index:self.currentAssociatedProductPosition});
		};
		/**
		 * call api error and throw message
		 * @param error
		 */
		self.callApiError = function (error) {
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
		 * check all function
		 */
		self.checkAllAction = function () {
			angular.forEach(self.product.rows, function (value, key) {
				value.checked = self.checkAll;
			});
		}
	}
})();
