(function() {
	'use strict';

	var module = angular.module('creatureEditor.main.config', [ 'ngCookies', 'pascalprecht.translate', 'restangular', 'ui.router', 'loading-animation', 'alert-message' ]);

	module.config([ '$urlRouterProvider', '$locationProvider', '$translateProvider', '$translatePartialLoaderProvider', 'RestangularProvider',
			function MainConfig($urlRouterProvider, $locationProvider, $translateProvider, $translatePartialLoaderProvider, RestangularProvider) {
				RestangularProvider.setBaseUrl('/rest');
				// $locationProvider.html5Mode(true);
				$urlRouterProvider.otherwise('/');
				$translateProvider.useLocalStorage();
				$translatePartialLoaderProvider.addPart('app/main');
				$translateProvider.useLoader('$translatePartialLoader', {
					urlTemplate : 'src/{part}/locales/messages-{lang}.json'
				});
				$translateProvider.preferredLanguage('en');
				$translateProvider.fallbackLanguage('en');
			} ]);

	module.config([ '$loadingAnimationProvider', function LoadingAnimationConfig($loadingAnimationProvider) {
		$loadingAnimationProvider.minDuration(1);
	} ]);
	
	module.config(['$provide', function($provide) {
		// should be implemented in Angular 1.3.0, see https://github.com/angular/angular.js/issues/4574
		$provide.decorator('$rootScope', ['$delegate', function($delegate) {
			$delegate.constructor.prototype.$onRootScope = function(name, listener) {
				var unsubscribe = $delegate.$on(name, listener);
				this.$on('$destroy', unsubscribe);
			};
			return $delegate;
		}]);
	}]);
	
	module.run([ '$rootScope', '$state', '$stateParams', function MainRun($rootScope, $state, $stateParams) {
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
	} ]);

})();
