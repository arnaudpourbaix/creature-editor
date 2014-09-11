angular.module('editor.main.config', [])

/**
 * @name editor.main.service:appSettings
 * @description application constants.
 */
.constant('appSettings', { 
	timeout : 20000, 
	restBaseUrl : 'rest/' 
})

/**
 * @description default router configuration. Redirect to root state on invalid route. Root state is the application starting point. It loads initial context which is based on
 *              user's settings.
 */
.config(function($urlRouterProvider, $stateProvider) {
	"use strict";
	$urlRouterProvider.otherwise('/creature');
	$stateProvider.state('root', {
		url : '/',
		onEnter : function() {
		}
	});
})

/**
 * 
 * @description providers configuration.
 */
.config(function($anErrorProvider, appSettings) {
	"use strict";
	$anErrorProvider.defaults.active = true; // enable errors catching (should always be enabled in production)
})

/**
 * @description Defines interceptors for $http service.
 */
//.config(function($httpProvider, appSettings) {
//	"use strict";
//	$httpProvider.interceptors.push(function($q) {
//		return {
//			request : function(config) { // Called before send a new XHR request.
//				if (angular.isUndefined(config.timeout)) {
//					// adds a global timeout if none is provided
//					config.timeout = appSettings.timeout;
//				}
//				return config || $q.when(config);
//			},
//			responseError : function(rejection) { // Called when another XHR request returns with an error status code.
//				// set message codes for some status to properly inform user on what is going on
//				if (rejection.status === 0) { // timeout
//					rejection.data = "ERROR.TIMEOUT";
//				} else if (rejection.status === 404) {
//					rejection.data = "ERROR.OFFLINE";
//				} else if (rejection.status === 401) {
//					window.location.replace(rejection.headers('location'));
//				}
//				return $q.reject(rejection);
//			}
//		};
//	});
//})

.run(function($rootScope, $state, $stateParams) {
	"use strict";
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
});
