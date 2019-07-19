'use strict';

(function() {
	var app = angular.module('productMaintenanceUiApp');
	app.directive('confirmationModal', confirm);
	confirm.$inject = ['$rootScope'];

	function confirm($rootScope) {
		return{
			restrict: 'E',
			templateUrl: 'src/utils/confirm/confirmationModal.directive.html',
			scope: {},
			link: function($scope, element, attrs) {
				/**
				 * Send the results of the confirmation modal to the rootScope
				 * @param result
				 */
				$scope.sendResults = function(result){
					$scope.$emit('modalResults', {result: result});
					if($rootScope.logOutClicked && result){
						$rootScope.contentChangedFlag = false;
						$rootScope.$broadcast('LogoutEvent');
					}
					$rootScope.logOutClicked = false;
				}
			}
		}
	}
})();
