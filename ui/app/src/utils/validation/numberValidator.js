/**
 * Created by vn70516 on 8/24/2017.
 */
// This is the script to create the directive number-validator to validation integer number or decimal number
// Integer Number: Only allow user input a number that is integer type.
// Decimal Number: Allow user input a decimal number with a number of decimal places

'use strict';

(function(){
	angular.module('productMaintenanceUiApp').directive('numberValidator', function(){
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				var regex = new RegExp(attrs.numberValidator);
				element.on('keypress', function(event) {
					if (!isNumberValidatorChar()) {
						event.preventDefault();
					}
					function isNumberValidatorChar() {
						var dataText = '';
						if(event.target.value != undefined && event.target.value != '') {
							dataText = event.target.value;
						}
						dataText += String.fromCharCode(event.which);
						if(regex.test(dataText) || regex.test(String.fromCharCode(event.which))){
							return true;
						}
						return false;
					}
				});
				element.on('paste', function() {
					if (!isNumberValidatorChar()) {
						event.preventDefault();
					}
					function isNumberValidatorChar() {
						if(event.clipboardData != undefined) {
							var dataPaste = event.clipboardData.getData('text/plain');
							return regex.test(dataPaste);
						}
						return true;
					}
				});
				element.on('drop', function() {
					if (!isNumberValidatorChar()) {
						event.preventDefault();
					}
					function isNumberValidatorChar() {
						if(event.dataTransfer != undefined) {
							var dataDrop = event.dataTransfer.getData("text");
							return regex.test(dataDrop);
						}
						return true;
					}
				});
				element.on('change', function() {
					if (!isNumberValidatorChar()) {
						element.css("border", "1px solid red");
					}else{
						element.css("border", "");
					}
					function isNumberValidatorChar() {
						if(event.target.value != undefined && event.target.value != '') {
							var dataText = event.target.value;
							return regex.test(dataText);
						}
						return true;
					}
				});
				//Refresh validation input field when data has been changed by bindable for ng-model of component (It's
				// mean value has been set for properties (ng-model)) not use any keyboard action.
				scope.$watch(attrs.ngModel, function (value) {
					if (!isNumberValidatorChar()) {
						element.css("border", "1px solid red");
					}else{
						element.css("border", "");
					}
					function isNumberValidatorChar() {
						if(value != undefined && value != '') {
							return regex.test(value);
						}
						return true;
					}
				});
			}
		};
	});
})();
