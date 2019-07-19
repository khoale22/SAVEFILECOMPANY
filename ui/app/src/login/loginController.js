'use strict';

(function () {
    angular.module('productMaintenanceUiApp').controller('LoginController', loginController);

    loginController.$inject = ['appName', 'ProductSearchService'];

    function loginController(appName, productSearchService) {

        var self = this;

        self.appName = appName;
		productSearchService.setSearchSelection(null);
		productSearchService.setSearchType(null);
		productSearchService.setSelectionType(null);
    }
})();
