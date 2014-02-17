(function() {
	'use strict';

	var module = angular.module('creatureEditor.main.config', [ 'ngCookies', 'pascalprecht.translate', 'restangular', 'ui.router', 'loading-animation' ]);

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

	module.run([ '$rootScope', '$state', '$stateParams', function MainRun($rootScope, $state, $stateParams) {
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
	} ]);

})();
