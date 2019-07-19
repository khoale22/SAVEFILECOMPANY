/**
 * Created by m314029 on 5/30/2016.
 */
// This is the script to create the directive loadingSpinner that shows a spinner image and
// text "Data is loading... Please Wait
// template to use is :
// <loading-spinner ng-if="someMethodOrVariableWhenYouWantToShowSpinner"></loading-spinner>
'use strict';

(function(){
	angular.module('productMaintenanceUiApp').directive('loadingSpinner', function(){
		return {
			restrict: 'E',
			templateUrl: 'src/utils/loadingSpinner/loadingSpinner.html'
		};
	});
})();
