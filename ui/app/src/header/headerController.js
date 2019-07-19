'use strict';

(function () {
	angular.module('productMaintenanceUiApp').controller('HeaderController', headerController);

	headerController.$inject = ['appName', 'StatusApi', 'appVersion', '$rootScope', '$window', 'AuthenticationService'];

	function headerController(appName, statusApi, appVersion, $rootScope, $window, AuthenticationService) {

		var self = this;
        /**
		 * Holds the content message for showing warning message when
		 * mismatch version between api and ui.
         * @type {string}
         */
		const WARNING_MESSAGE_CONTENT = 'Application Version is Mismatch. Please press Ctrl F5.';
        /**
		 * Holds the key to get the number of version checks to check for reload page or show warning message.
         * @type {string}
         */
		const NUMBER_OF_VERSION_CHECKS = 'number_of_version_checks';
		/**
		 * The version of the application as reported by the server.
		 *
		 * @type {string}
		 */
		self.version = "";

		/**
		 * The name of the application server that is serving up information.
		 *
		 * @type {string}
		 */
		self.applicationServerName = "";

		/**
		 * The name of the application.
		 *
		 * @type {string}
		 */
		self.appName = appName;

		/**
		 * Initializes the controller.
		 */
		self.init = function() {
			statusApi.get({}, self.setVersion)
		};

		/**
		 * Callback for the response for the backend.
		 *
		 * @param result The data passed from the backend.
		 */
		self.setVersion = function(result) {
			self.version = result.version;
			self.applicationServerName = result.applicationServerName;
			// Check mismatch version between API & UI.
            self.checkMismatchVersion();
		};
        /**
		 * This method is used to check version between api and ui. If they are difference, then show mismatch version message.
         */
        self.checkMismatchVersion = function () {
            // Check mismatch version.
            if (appVersion != self.version) {
                if (self.canShowWarningMessage()) {
                    //After reload a time, the version is still mismatch then app will show mismatch version message.
                    $rootScope.isShowWarningMessage = true;
                    $rootScope.warningMessage = WARNING_MESSAGE_CONTENT;
                } else {
                	// Clear cache and reload in the first time that version is mismatch.
                	// Clear session
                    AuthenticationService.invalidate();
                    // clear local storage.
                    $window.localStorage.clear();
                    // Reload the app
                    $window.location.reload(true);
                }
            }else{
            	// Hide warning message.
                $rootScope.isShowWarningMessage = false;
            	// Remove number of version checks on session storage.
                delete $window.sessionStorage[NUMBER_OF_VERSION_CHECKS];
			}
        }
        /**
		 * Check can show warning message or not. And reset status to null if numberOfVersionChecks is existed.
         * @returns {boolean} true show warning message, otherwise not show.
         */
        self.canShowWarningMessage = function(){
            var numberOfVersionChecks = $window.sessionStorage[NUMBER_OF_VERSION_CHECKS];
            if(numberOfVersionChecks == undefined || numberOfVersionChecks == null){
            	// if it is none existed, then set it to one.
                $window.sessionStorage[NUMBER_OF_VERSION_CHECKS] = 1;
                return false;
			}
			// else if it is existing, then remove it and return true.
			delete $window.sessionStorage[NUMBER_OF_VERSION_CHECKS];
            return true;
		}
	}
})();
