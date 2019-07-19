'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('ProductFactory', productFactory);

	productFactory.$inject = ['urlBase', '$resource'];
	function productFactory(urlBase, $resource) {
		return $resource(urlBase + '/pm/product/:productId');
	}
})();
