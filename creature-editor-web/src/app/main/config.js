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
//	$stateProvider.state('root', {
//		url : '/',
//		resolve: {
//			gameVersion: function(Parameter) {
//				return Parameter.get({
//					id: 'GAME_VERSION'
//				}).$promise;
//			} 
//		}
//	});
})

/**
 * 
 * @description providers configuration.
 */
.config(function($anErrorProvider, appSettings) {
	"use strict";
	$anErrorProvider.defaults.active = false; // enable errors catching (should always be enabled in production)
})

.run(function($rootScope, $state, $stateParams) {
	"use strict";
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
});
