/**
 * Created by m314029 on 5/17/2016.
 */
'use strict';

(function() {
	angular.module('productMaintenanceUiApp').service('ValidationService', validationService)

	validationService.$inject = ['appConstants'];
	function validationService(appConstants){

		var self = this;

		self.isAnInteger = function(value){
			return !isNaN(value) && eval(value).toString().length == parseInt(eval(value)).toString().length;
		};

		self.isIntegerArray = function(array, maxIntLength){
			var value;
			if(array == null){
				return false;
			}
			for(var index = 0; index < array.length; index++){
				value = array[index];

				//if maxIntLength was passed in
				if(maxIntLength != null) {
					if (!self.isAnInteger(value) || value.toString().length > maxIntLength) {
						return false;
					}
				}

				//else maxIntLength was not passed in
				else {
					if (!self.isAnInteger(value)) {
						return false;
					}
				}
			}
			return true;
		};
	}
})();
