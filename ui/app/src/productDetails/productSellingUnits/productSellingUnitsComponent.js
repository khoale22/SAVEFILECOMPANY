/*
 *   productSellingUnitsComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Component of Selling Units data table that is displayed on the Top of the Product detail page.
 *
 * @author vn40486
 * @since 2.2.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('pdSellingUnits', {
		// isolated scope binding.
		bindings: {
			sellingUnits:  "<",
			onSellingUnitChange : '&',
			isCollapseCasePack:'=',
			isCollapseSellingUnit:'=',
			removeCSSClasses : '&'
		},
		// Inline template which is bound to message variable in the component controller.
		templateUrl: 'src/productDetails/productSellingUnits/productSellingUnits.html',
		// The controller that handles our component logic.
		controller: productSellingUnitsController
	});

	productSellingUnitsController.$inject = ['$scope', '$rootScope'];

	/**
	 * Selling Units Component's controller defintion.
	 * @param $scope	scope  of the Selling Units Component.
	 */
	function productSellingUnitsController($scope, $rootScope) {
		var ctrl = this;
		ctrl.selectedSellingUnit;
		ctrl.selectedRow = null;

		/**
		 * get header scroll style for upc table
		 * @returns {{"margin-right": string}}
		 */
		ctrl.getHeaderStyle=function() {
			var heightTable=$(document).find('#upcTable:first').height();
			var marginRight='';
			if(heightTable>65){
				marginRight='17px';
			}else{
				marginRight='0px';
			}
			var styles = {
				'margin-right': marginRight
			};
			return styles;
		}
		/**
		 * get scroll style for grid upc table
		 * @returns {{"overflow-y": string}}
		 */
		ctrl.getGridStyles=function() {
			var heightTable=$(document).find('#upcTable:first').height();
			var overflow='';
			if(heightTable>65){
				overflow='auto';
			}else{
				overflow='hidden';
			}
			var styles = {
				'overflow-y': overflow
			};
			return styles;
		}
		/**
		 * Initializes the controller.
		 */
		this.$onInit = function () {
			console.log('UPC Controller - Initialized');
			if (ctrl.sellingUnits && ctrl.sellingUnits.length>0) {
				//ctrl.selectedSellingUnit = null;//reset user selected row to none as whole table is reloaded.
				resetRowSelection(ctrl.sellingUnits);
				ctrl.sellingUnits[0].isSelected = true;//select item first
				ctrl.selectedSellingUnit =ctrl.sellingUnits[0];
			}
			ctrl.onSellingUnitChange({upc: ctrl.selectedSellingUnit});
			ctrl.drawTextByCanvas();
			$rootScope.$broadcast('setStylesForProductSummary');
		};

		/**
		 * Draw text on collapse button by canvas
		 */
		ctrl.drawTextByCanvas = function() {
			var canvas = document.getElementById('sellingUnitCanvas');
			var context = canvas.getContext('2d');
			context.font = '11px Arial';
			context.textAlign="left";
			context.save();
			context.translate(11, 10);
			context.rotate(0.5*Math.PI);
			context.fillStyle = 'white';
			var rText = 'Selling Units';
			context.fillText(rText , 11, 0);
			context.restore();
		};

		/**
		 * Standard AngularJS function ($onChanges) implementation. Used to catch change events of the objects that
		 * bound to this component (bindings can be found listed in the component definition above).
		 * @param changesObj holds the details of change like name of the binding, current value, previous value etc.
		 */
		this.$onChanges = function (changesObj) {
			if (changesObj.sellingUnits) {
				//ctrl.selectedSellingUnit = null;//reset user selected row to none as whole table is reloaded.
				if(ctrl.sellingUnits !== null){
                    resetRowSelection(ctrl.sellingUnits);
                    ctrl.sellingUnits[0].isSelected = true;//select item first
                    ctrl.selectedSellingUnit =ctrl.sellingUnits[0];
                    ctrl.onSellingUnitChange({upc: ctrl.selectedSellingUnit});
				}
			}

		};
		/**
		 * Opens confirmation modal before row selection
		 * @param row
		 */
		ctrl.confirmRowSelect = function(row) {
			ctrl.selectedRow = row;
			if($rootScope.contentChangedFlag) {
				var result = document.getElementById("confirmationModal");
				var wrappedResult = angular.element(result);
				wrappedResult.modal("show");
			} else {
				ctrl.handleRowSelect(ctrl.selectedRow)
			}
		};
		/**
		 * Handles results of confirmation modal
		 */
		$rootScope.$on('modalResults', function(event, args){
			if(ctrl.selectedRow !== null && args.result){
				ctrl.handleRowSelect(ctrl.selectedRow)
			} else{
				ctrl.selectedRow = null;
			}
		});

		/**
		 * Used to handle user's row select/click event on the data table.
		 * @param row	selected row.
		 */
		ctrl.handleRowSelect = function(row) {
			/** Deselect previously selected Row */
			if (ctrl.selectedSellingUnit) {
				ctrl.selectedSellingUnit.isSelected = false;
			} else {
				resetRowSelection(ctrl.sellingUnits);
			}
			/** Set recently clicked row as Selected */
			row.isSelected = true;
			ctrl.selectedSellingUnit = row;
			$rootScope.contentChangedFlag = false;
			/** Fire new row(upc) selected event. This allows Parent component to decide on appropriate action
			 * like switching to Upc Info related tab, reload of work area section etc.*/
			ctrl.onSellingUnitChange({upc: row});
		};

		/**
		 * Used to reset all the row selection. Sets all the records as deselected.
		 * @param sellingUnits data table referenced object.
		 */
		function resetRowSelection(sellingUnits) {
			_.forEach(sellingUnits, function(sellingUnit){
				sellingUnit.isSelected =false;
			});
		}

		/**
		 * Expand case pack table
		 */
		ctrl.expandSellingUnit = function() {
			ctrl.removeCSSClasses();
			//ctrl.isCollapseCasePack = false;
			ctrl.isCollapseSellingUnit = false;
		};
		/**
		 * Collapse selling unit table
		 */
		ctrl.collapseSellingUnit = function() {
			ctrl.removeCSSClasses();
			if(!ctrl.isCollapseCasePack && !ctrl.isCollapseSellingUnit ){
				angular.element(document.querySelector("#casePack")).addClass("case-pack-expand-left");
				angular.element(document.querySelector("#sellingUnit")).addClass("selling-unit-collapse-right");
			}
			if(ctrl.isCollapseCasePack && !ctrl.isCollapseSellingUnit ){
				angular.element(document.querySelector("#casePack")).removeClass("case-pack-collapse-left");
				angular.element(document.querySelector("#sellingUnit")).removeClass("selling-unit-expand-right");
				angular.element(document.querySelector("#casePack")).addClass("case-pack-expand-left");
				angular.element(document.querySelector("#sellingUnit")).addClass("selling-unit-collapse-right");
			}
			ctrl.isCollapseCasePack = false;
			ctrl.isCollapseSellingUnit = true;

		};
	}
})();
