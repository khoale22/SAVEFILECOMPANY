(function(){
	'use strict';

	angular.module('productMaintenanceUiApp')
		.controller('AuthenticationLoginController', AuthenticationLoginController)
		.controller('AuthenticationLogoutController', AuthenticationLogoutController)
		.directive('authenticationDirective', AuthenticationDirective)
		.run(runnable)
		.factory('AuthenticationService', AuthenticationService)
		.factory('AuthenticationInterceptor', AuthenticationInterceptor)
		.config(AuthenticationConfig);

	AuthenticationConfig.$inject = ['$httpProvider'];
	function AuthenticationConfig($httpProvider) {
		$httpProvider.interceptors.push('AuthenticationInterceptor');
	}

	AuthenticationInterceptor.$inject = ['$q', '$location'];
	function AuthenticationInterceptor($q, $location) {
		return {
			response: function (response) {
				if (response.status === 401) {
					console.log("Response 401");
				}
				// return the response or wrap it into a promise if it is blank
				return response || $q.when(response);
			},
			responseError: function (rejection) {
				if (rejection.status === 401 || rejection.status === 403) {
					var returnTo = $location.search().returnTo;
					if (returnTo === undefined && $location.path().lastIndexOf('/login', 0) !== 0) {
						returnTo = $location.path();
					}
					$location.path('/login').search('returnTo', returnTo);
				}

				return $q.reject(rejection);
			}
		};
	}

	AuthenticationService.$inject = ['$http', 'urlBase', '$rootScope', '$window'];
	function AuthenticationService($http, urlBase, $rootScope, $window) {
        const PERMISSION_VIEW_TYPE = '-VIEW';
		const PERMISSION_EDIT_TYPE = '-EDIT';
		return {
			invalidate: function () {
				delete $rootScope.currentUser;
				delete $window.sessionStorage["currentUser"];
			},
			//getCsrf: function() { return $http.get('/product-maintenance_api/login'); },
			login: function (username, password) {
				return $http.post(
					urlBase + '/login',
					$.param({'username': username, 'password': password}),
					{
						headers: {
							'Content-Type': 'application/x-www-form-urlencoded'
						}
					});
			},
			logout: function () {
				return $http.post(urlBase +'/logout');
			},
			hasRole: function (requiredRole) {
				var roles = requiredRole && requiredRole.split(',');
				// check that there is a user and that he/she has the required role
				return $rootScope.currentUser &&
					_.intersection($rootScope.currentUser.roles, roles).length > 0;
			},
            /**
			 * Check the permission of the input param role with the format: role 1,role 2,...
			 * If there is any role that existing in current user without know it is edit role or view role.
             * @param requiredRole the role string with format as role 1,role 2,...
             * @returns {boolean} true if is has permission or not.
             */
			hasPermission: function (requiredRole) {
				// The user is not login
				if(!$rootScope.currentUser) {
                    return false;
                }
                var roles = requiredRole && requiredRole.split(',');
				// There is no any role.
                if(roles == null || roles.length == 0){
                	return false;
				}
				// Correct input role before check.
				var newRoles = [];
				angular.forEach(roles, function (role) {
                    newRoles[newRoles.length] = role + PERMISSION_VIEW_TYPE;
                    newRoles[newRoles.length] = role + PERMISSION_EDIT_TYPE;
                });
				// Check role.
				return _.intersection($rootScope.currentUser.roles, newRoles).length > 0;
            }
		};
	}

	runnable.$inject = ['$rootScope', '$location', '$state', 'AuthenticationService', '$window', 'Idle', 'ignorePermissionCheckPages', 'appConstants'];

	function runnable($rootScope, $location, $state, AuthenticationService, $window, Idle, ignorePermissionCheckPages, appConstants) {
        /**
		 * Holds the key to get for redirecting after logged.
         * @type {string}
         */
		const RETURN_URL_PAGE_KEY = 'returnTo';

        /**
		 * Replace path param key (<path>/:taskId) by value param (<path>/23333)
         * @param toParams the toParams
         * @param path the path.
         * @returns {*} return path width param as <path>/param value.
         */
		function replacePathParamKeyByPathParamValue(toParams, path){
			if(toParams != undefined && toParams != null){
                var id;
                for (var key in toParams) {
                    id = ':' + key;
                    if(_.endsWith(path, id)){
                        path = path.replace(id, toParams[key]);
                    }
                }
			}
			return path;
		};
		// $stateChangeStart instead of $routeChangeStart since we're using ui-router
		// This function will be called when the url before change
		// to check the permission of the page that url path was config in the routing file.
		// The page that was not config in the routing file, then the Angular auto handle via
		// $urlRouterProvider.otherwise function that was config in routing file.
		$rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
			// Case 1: if the url path is matching with ignore permission check pages,
			// then continue run that page without checking permission.
            if(toState.url != undefined && _.intersection(ignorePermissionCheckPages, toState.url.split(',')).length > 0){
            	return;
            }
            // Case 2: if the url path is empty or is '/', then navigate to login path.
            if(toState.url == '' || toState.url == '/'){
                // Stop last navigation
                event.preventDefault();
                // Navigate to login page
                $state.go(appConstants.LOGIN_STATE);
            	return;
			}
			// Case 3: if the url path that is not notFound and not login and
			// belong to the url path was config in routing file, then check permission.
            if(toState.url){
            	// Case 4: If url path is home, then check login status. It was logged, then stop check permission
				// or not login then navigate to login.
                if(toState.url.indexOf('home') != -1){
                	if(!$rootScope.currentUser){
                        // Stop last navigation
                        event.preventDefault();
                        //Does not login, then Navigate to login page
                        $state.go(appConstants.LOGIN_STATE);
					}
                	return;
				}
				// Case 5: Check the permission of the selected path.
				// If the permission of selected page is undefined or there is not permission access to selected page then
				// continue check if there is not login, then navigate to login page or navigate to not  found page.
                if (toState.data && (toState.data.access == undefined || !AuthenticationService.hasPermission(toState.data.access))) {
                	// Stop last navigation
                    event.preventDefault();
                    if (!$rootScope.currentUser) {
                        // Check return url page on current url if it is not existed on current
						// url then add return url page to param of current url.
                    	if($location.absUrl().indexOf(RETURN_URL_PAGE_KEY) == -1) {
                            var returnUrlPage = replacePathParamKeyByPathParamValue(toParams,  toState.url.replace('/', ''));
                            if (fromState.url === '^') {
                            	// Process case when user open tab and paste an url in the first time.
                                $location.path(appConstants.LOGIN_STATE).search(RETURN_URL_PAGE_KEY, returnUrlPage);
                                $state.go(appConstants.LOGIN_STATE, {RETURN_URL_PAGE_KEY: returnUrlPage});
                            } else {
                                // User enter the url is not first time after open a tab on browser.
                                $location.path(appConstants.LOGIN_STATE);
                                $window.location.href = $location.absUrl() + '?' + RETURN_URL_PAGE_KEY + '=' + returnUrlPage;
                            }
                        }
                    }else{
                    	// The url path has config in the routing config but it is not permission,
						// then navigate to not found page with the message does not have permission.
                        $state.go(appConstants.NOT_FOUND, { 'pageNotAccess': true});
                    }
                }
			}
		});
		// restore currentUser from sessionStorage on refresh of page
		if(angular.isDefined($window.sessionStorage["currentUser"])) {
			//Set currentUser if in sessionStorage
			var currentUser = JSON.parse($window.sessionStorage["currentUser"]);
			// set user id, name, role
			$rootScope.currentUser = {
				id: currentUser.id,
				name: currentUser.name,
				roles: currentUser.roles
			};

			if ($rootScope.currentUser) {
				Idle.watch();
			}

		}
	}

	AuthenticationDirective.$inject = ['AuthenticationService'];
	function AuthenticationDirective(AuthenticationService) {
		return {
			restrict: 'A',
			link: function (scope, element, attrs) {
				var makeVisible = function () {
						element.removeClass('hidden');
					},
					makeHidden = function () {
						element.addClass('hidden');
					},
					determineVisibility = function (resetFirst) {
						if (resetFirst) {
							makeVisible();
						}

						if (AuthenticationService.hasRole(roles)) {
							makeVisible();
						} else {
							makeHidden();
						}
					},
					roles = attrs.permissions.split(',');

				if (roles.length > 0) {
					determineVisibility(true);
				}
			}
		}
	}

	AuthenticationLogoutController.$inject = ['$state','$scope', 'AuthenticationService', '$rootScope'];
	function AuthenticationLogoutController($state, $scope,  AuthenticationService, $rootScope) {
		var vm = this;
		vm.loggedInAs = "You are logged in as ";
		vm.separator = " | ";
		vm.logoutLink = "Log Out";
		vm.helpLink = "Help";
		vm.logout = logout;

		$scope.$on('LogoutEvent', function(event) {
			vm.logout();
		});

		function logout() {
			$rootScope.logOutClicked = true;
			vm.dataLoading = true;
			if(!$rootScope.contentChangedFlag){
				AuthenticationService.logout()
					.success(function () {
						$rootScope.contentChangedFlag = false;
						vm.dataLoading = false;
						$state.go('login');
					})
					.error(function () {
						vm.dataLoading = false;
						$state.go('login');
					});
			}
			else{
					var result = document.getElementById("confirmationModal");
					var wrappedResult = angular.element(result);
					wrappedResult.modal("show");
			}
		}
	}

	AuthenticationLoginController.$inject = ['$scope', '$rootScope','$uibModalStack', '$location', '$state', 'Idle', 'AuthenticationService', '$window'];
	function AuthenticationLoginController($scope, $rootScope, $uibModalStack, $location, $state, Idle, AuthenticationService, $window) {
		var vm = this;
		// reset login status
		AuthenticationService.invalidate();

		vm.dataLoading = false;
		vm.login = login;
		vm.error = null;

		$uibModalStack.dismissAll();

		function login() {
			vm.dataLoading = true;
			if (!$scope.username || !$scope.password) {
				return;
			}

			AuthenticationService.login($scope.username, $scope.password).success(loginSuccess).error(loginFailure);

			function loginSuccess(data, status, headers, config) {
				// set user id, name, role
				$rootScope.currentUser = {
					id: data.id,
					name: data.name,
					roles: data.roles
				};

				$window.sessionStorage["currentUser"] = JSON.stringify($rootScope.currentUser);

				vm.dataLoading = false;
				var redirectTo = $location.search().returnTo;
				if (redirectTo === undefined) {
					$state.go('home');
				} else {
					$location.path(redirectTo).search({});
				}
				Idle.watch();
			}

			function loginFailure(data, status, headers, config) {
				vm.dataLoading = false;

				if (status == 401) {
					vm.error = "Invalid user name or password."
				} else {
					if (data.error != null) {
						vm.error = status + " " + data.error;
					} else {
						vm.error = "An unknown error occurred: " + status;
					}
				}
				return vm.error;
			}
		}
	}
})();
