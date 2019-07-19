/*
 *
 * directive.js
 *
 * Copyright (c) 2017 MagRabbit
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of MagRabbit.
 *
 */

'use strict';

/**
 * Collection of custom directives to aid in customizing the application.
 *
 * @author vn70529
 ~ @since 2.12
 */
(function () {
	angular.module('productMaintenanceUiApp')
		.directive('uploadFileModel', uploadFileModel)
		.directive('disableEnterBeginWithSpaceChar', disableEnterBeginWithSpaceChar)
		.directive('numericOnly', numericOnly)
		.directive('decimalOnly', decimalOnly)
		.directive('asciiOnly', asciiOnly)
		.directive('emptyValid', emptyValid)
		.directive('minLengthValid', minLengthValid)
		.directive('minValueValid', minValueValid)
		.directive('validationModel', validationModel)
		.directive('capitalize', capitalize)
		.directive('customAutofocus', customAutofocus)
		.directive('paginationCustom', paginationCustom)
		.directive('autoFocus', autoFocus);

	/**
	 * Constant for NON_ASCII_CHARACTERS.
	 * In ASCII, codes 20 hex to 7E hex, known as the printable characters, represent letters, digits,
	 * punctuation marks, and a few miscellaneous symbols.
	 * @type {regular expression}
	 */
	const NON_ASCII_CHARACTERS = /[^\x20-\x7E]/g;
	/**
	 * Constant for EMPTY
	 * @type {string}
	 */
	const EMPTY = "";

	/**
	 * Create a directive for upload file.
	 * @param $parse
	 * @returns {{restrict: string, link: link}}
	 */
	function uploadFileModel($parse) {
		return {
			restrict: 'A',
			link: function (scope, element, attrs) {
				var model = $parse(attrs.uploadFileModel);
				var imagePreviewFilename = attrs.imagePreviewFilename;

				var modelSetter = model.assign;

				element.bind('change', function () {
					scope.$apply(function () {
						var file = element[0].files[0];
						modelSetter(scope, file);
						if(file) {
							$("." + imagePreviewFilename).val(file.name);
						}else{
							$("." + imagePreviewFilename).val("");
						}
					});
				});
			}
		};
	}

	/**
	 * Create a directive for disable enter begin with space char.
	 *
	 * @type {Array}
	 */
	disableEnterBeginWithSpaceChar.$inject = [];
	function disableEnterBeginWithSpaceChar( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {

				element.on('keypress', function(event) {
					if (isSpaceCharFirst()) {
						event.preventDefault();
					}
					function isSpaceCharFirst() {
						var dataText = '';
						if(event.target.value != undefined && event.target.value != '') {
							dataText = event.target.value;
						}
						var key = event.which || event.keyCode;

						dataText += String.fromCharCode(key);

						return dataText.startsWith(' ');
					}
				});
				element.on('keyup', function(event) {
					if (isSpaceCharFirst() && $(element).val().length > 0) {
						$(element).val($(element).val().substr(1,$(element).val().length));
					}
					function isSpaceCharFirst() {
						var dataText = '';
						if(event.target.value != undefined && event.target.value != '') {
							dataText = event.target.value;
						}

						return dataText.startsWith(' ');
					}
				});
				element.on('paste', function() {
					if (isSpaceCharFirst()) {
						event.preventDefault();
					}
					function isSpaceCharFirst() {
						if(event.clipboardData != undefined) {
							var dataPaste = event.clipboardData.getData('text/plain');
							return dataPaste.startsWith(' ');
						}
						return false;
					}
				});
				element.on('drop', function() {
					if (isSpaceCharFirst()) {
						event.preventDefault();
					}
					function isSpaceCharFirst() {
						if(event.dataTransfer != undefined) {
							var dataDrop = event.dataTransfer.getData("text");
							return dataDrop.startsWith(' ');
						}
						return false;
					}
				});
				element.on('change', function() {
					if (isSpaceCharFirst()) {
						element.css("border", "1px solid red");
					}else{
						element.css("border", "");
					}
					function isSpaceCharFirst() {
						if(event.target.value != undefined && event.target.value != '') {
							var dataText = event.target.value;
							return dataText.startsWith(' ');
						}
						return false;
					}
				});
			}
		};
	}

	/**
	 * Create a directive for empty valid.
	 *
	 * @type {Array}
	 */
	emptyValid.$inject = [];
	function emptyValid( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('keypress', function(event) {
					$(element).attr('title', '');
					$(element).removeClass('ng-touched');
					$(element).removeClass('ng-invalid');
				});
				element.on('keyup', function(event) {

					var dataText = '';
					if(event.target.value != undefined && event.target.value != '') {
						dataText = event.target.value;
					}
					if(dataText.length == 0){
						$(element).attr('title', attrs.emptyValid);
						$(element).addClass('ng-invalid ng-touched');
					}
				});
				element.on('blur', function (event) {
					var dataText = '';
					if(event.target.value != undefined && event.target.value != '') {
						dataText = event.target.value;
					}
					if(dataText.length == 0){
						$(element).attr('title', attrs.emptyValid);
						$(element).addClass('ng-invalid ng-touched');
					}
				});
			}
		};
	}
	/**
	 * Create a directive for min length valid.
	 *
	 * @type {Array}
	 */
	minLengthValid.$inject = [];
	function minLengthValid( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('keypress', function(event) {
					$(element).attr('title', '');
					$(element).removeClass('ng-touched');
					$(element).removeClass('ng-invalid');
				});
				element.on('keyup', function(event) {

					var dataText = '';
					if(event.target.value != undefined && event.target.value != '') {
						dataText = event.target.value;
					}
					if(dataText.length < parseInt(attrs.minlength)){
						$(element).attr('title', attrs.minLengthValid);
						$(element).addClass('ng-invalid ng-touched');
					}
				});
			}
		};
	}

	/**
	 * Create a directive for min length valid.
	 *
	 * @type {Array}
	 */
	minValueValid.$inject = [];
	function minValueValid( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('keypress', function(event) {
					$(element).attr('title', '');
					$(element).removeClass('ng-touched');
					$(element).removeClass('ng-invalid');
				});
				element.on('keyup', function(event) {

					var dataText = '';
					if(event.target.value != undefined && event.target.value != '') {
						dataText = event.target.value;
					}

					var value = parseInt(dataText);
					if(value) {
						$(element).val(value);
					}
					if(value < parseInt(attrs.min)){
						$(element).attr('title', attrs.minValueValid);
						$(element).addClass('ng-invalid ng-touched');
					}
				});
			}
		};
	}

	numericOnly.$inject = ['$timeout'];
	function numericOnly( $timeout) {
		return {
			require: 'ngModel',
			restrict: 'A',
			link: function (scope, element, attr, ctrl) {
				element.on('keydown', function(event) {
					var keyCode = event.which || event.keyCode;
					if(keyCode==32){
						event.preventDefault();
					}
				});
				function inputValue(val) {

					if (val) {
						var digits = val.replace(/[^0-9]/g, '');

						if (digits !== val) {
							ctrl.$setViewValue(digits);
							ctrl.$render();
							ctrl.$setValidity('numericOnly', false);
						} else {
							ctrl.$setValidity('numericOnly', true);
						}
						return parseInt(digits, 10);
					}
					return undefined;
				}

				ctrl.$parsers.push(inputValue);
				element.on("focus", function () {
					//Highlight text after parsed
					$timeout(function () {
						element[0].select();
					});
				});
				element.on("blur", function () {
					$timeout(function () {
						ctrl.$setValidity('numericOnly', true);
					});
				});
			}
		};
	}

	/**
	 * Sets the data to validate model.
	 *
	 * @param $parse
	 * @returns {{validationModel: string, link: link}}
	 */
	function validationModel($parse) {
		return {
			validationModel:'=',
			link: function(scope, element, attrs) {
				var model = $parse(attrs.validationModel);
				var modelSetter = model.assign;
				element.on('input', function() {
					modelSetter(scope, $(element).val());
				});
			}
		};
	};

	/**
	 * capitalize user input automatically
	 *
	 * @param $parse
	 * @returns {{validationModel: string, link: link}}
	 */
	function capitalize($parse) {
		return {
			require: 'ngModel',
			link: function(scope, element, attrs, modelCtrl) {
				var capitalize = function (inputValue) {
					if (inputValue == undefined) inputValue = '';
					var capitalized = inputValue.toUpperCase();
					if (capitalized !== inputValue) {
						// see where the cursor is before the update so that we can set it back
						var selection = element[0].selectionStart;
						modelCtrl.$setViewValue(capitalized);
						modelCtrl.$render();
						// set back the cursor after rendering
						element[0].selectionStart = selection;
						element[0].selectionEnd = selection;
					}
					return capitalized;
				};
				modelCtrl.$parsers.push(capitalize);
				capitalize(scope[attrs.ngModel]); // capitalize initial value
			}
		};
	};

	/**
	 * A directive for auto focus on input element.
	 *
	 * @param $timeout
	 * @returns {{require: string, link: link}}
	 */
	function customAutofocus($timeout) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				$timeout(function() {
					element[0].focus();
				}, 0);
			}
		};
	};

	/**
	 * A directive for input decimal number (include integer number).
	 * @returns {{require: string, link: link}}
	 */
	decimalOnly.$inject = ['$timeout'];
	function decimalOnly($timeout) {
		return {
			require: 'ngModel',
			link: function(scope, element, attrs, modelCtrl) {
				element.on('keydown', function(event) {
					var keyCode = event.which || event.keyCode;
					if($(element).val().lastIndexOf('.')!= -1){
						// ensure one dot can enter in text box.
						if(keyCode == 190 || keyCode == 110) {
							event.preventDefault();
						}
					}
					var decimalPlaces = attrs.decimalPlaces;
					if (!isNaN(decimalPlaces) && decimalPlaces != null) {
						var dataText = $(element).val();
						if(event.target.value != undefined && event.target.value != '') {
							dataText = event.target.value;
						}
						if (keyCode >= 96 && keyCode <= 105) {
							// process Numpad keys
							keyCode -= 48;
						}
						// Check decimal place if is has value.
						if (isInValidDecimalPlaces(dataText,decimalPlaces) && !isNaN(parseInt(String.fromCharCode(keyCode)))) {
							event.preventDefault();
						}
					}
				});
				/**
				 * Correct decimal when lost focus.
				 */
				element.on("blur", function () {
					$timeout(function () {
						modelCtrl.$setViewValue(correctDecimal($(element).val()));
						modelCtrl.$render();
					});
				});
				/**
				 * Check decimal place is valid or not.
				 * @param val the string to check
				 * @param decimalPlaces decimalPlaces
				 * @returns {boolean} true if length of decimal places is large then or equals to 2 or return false.
				 */
				function isInValidDecimalPlaces(val, decimalPlaces){
					var parts = val.split('.');
					if(parts != null && parts.length > 1){
						// if the length of decimal places >= 2 the return true or false.
						if(parts[1].length >= parseInt(decimalPlaces)){
							return true;
						}
					}
					return false;
				}
				/**
				 * Correct decimal places to display
				 * @param val the value to parse
				 * @returns {*}
				 */
				function correctDecimal(val){
					// Returns zero if input value is null or undefine.
					if(val== undefined || val == null){
						return '';
					}
					var  valueAsFloat = parseFloat(val);
					// convert to int if parse float is error.
					if(isNaN(valueAsFloat)){
						// Case: user enter the dot only, then show empty.
						val =  '';
					} else {
						// If the value is large than zero, then we format the length of decimal places by
						var parts = val.split('.');
						if(valueAsFloat > 0) {
							if (parts.length > 1) {
								// Case: user enter: 1., then show 1
								val = parseFloat(val).toFixed(parts[1].length);
							}
						}else{
							if(parts.length > 1){
								if(parts[0].length == 0){
									// Case: User enter: .00, then show 0.00
									val = parseFloat(val).toFixed(parts[1].length);
								}else if(parts[1].length == 0){
									// Case User enter: 00., then show 00
									val = parts[0];
								}
							}
						}
					}
					if(val == null || isNaN(val)){
						// returns empty if val is nan.
						val = '';
					}
					return val;
				}
				modelCtrl.$parsers.push(function (inputValue) {
					// this next if is necessary for when using ng-required on your input.
					// In such cases, when a letter is typed first, this parser will be called
					// again, and the 2nd time, the value will be undefined
					if (inputValue == undefined) return '';
					var transformedInput = inputValue.replace(/[^0-9\.]/g,'');
					if (transformedInput!=inputValue) {
						modelCtrl.$setViewValue(transformedInput);
						modelCtrl.$render();
					}
					return transformedInput;
				});
			}
		};
	}

	/**
	 * Create a directive for remove non-ascii characters.
	 * @returns {{require: string, restrict: string, link: link}}
	 */
	function asciiOnly( ) {
		return {
			require: 'ngModel',
			restrict: 'A',
			link: function(scope, element, attrs, ctrl) {
				element.on('paste', function() {
					function removeNonAscii(inputValue) {
						if (inputValue !== undefined && inputValue !== null) {
							var asciiText = inputValue.replace(NON_ASCII_CHARACTERS, EMPTY);
							if (asciiText !== inputValue) {
								ctrl.$setViewValue(asciiText);
								ctrl.$render();
							}
							return asciiText;
						}
					}
					ctrl.$parsers.push(removeNonAscii);
				});
                element.on('keydown', function() {
                    function removeNonAscii(inputValue) {
                        if (inputValue !== undefined && inputValue !== null) {
                            var asciiText = inputValue.replace(NON_ASCII_CHARACTERS, EMPTY);
                            if (asciiText !== inputValue) {
                                ctrl.$setViewValue(asciiText);
                                ctrl.$render();
                            }
                            return asciiText;
                        }
                    }
                    ctrl.$parsers.push(removeNonAscii);
                });
			}
		};
	};

    /**
	 * A directive for customize default pagination of ng-table.
     * @param $templateCache
     * @param $compile
     * @param $timeout
     * @returns {{restrict: string, scope: boolean, link: link}}
     */
    function paginationCustom($templateCache,$compile, $timeout) {
        return {
            restrict: 'A',
            scope: true,
            link: function (scope, element, attr) {
                var template = $templateCache.get(attr.templatePagination);
                var paginationArea = attr.paginationCustom;
                $timeout(function () {
                    angular.element( document.querySelector( '#' + paginationArea ) ).append($compile(template.replace('hidden', ''))(scope));
                }, 1000);
            }
        }
    };
    /**
	 * Set auto focus on input text
     * @param $timeout
     * @returns {{restrict: string, link: link}}
     */
    autoFocus.$inject = ['$timeout'];
    function autoFocus($timeout){
        return {
            restrict: 'A',
            link : function($scope, $element) {
                $timeout(function() {
                    $element[0].focus();
                },200);
            }
        }
	}
})();
