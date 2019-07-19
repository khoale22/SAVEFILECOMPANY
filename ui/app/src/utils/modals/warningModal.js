(function () {
    'use strict';

    angular.module('productMaintenanceUiApp')
        .controller('WarningModalController', WarningModalController);

    WarningModalController.$inject = ['$rootScope', 'Idle', 'StatusApi'];
    function WarningModalController($rootScope, idle, statusApi) {

        var model = this;

        model.title = "Timeout";
        model.stillUploadImageText = "Images are being uploaded. Are you sure you want to continue?";
        model.firstRowFlavorText = "Due to inactivity you will soon be logged out.";
        model.secondRowFlavorText = "Would you like to remain logged into the application?";
        model.yes = function() {
			statusApi.get();
			idle.watch();
        };

        model.no = function() {
            $rootScope.isStillUploadImage = false;
            $rootScope.$broadcast('IdleTimeout', true);
        };
    }

})();

