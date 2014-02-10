(function() {
	'use strict';

	var module = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'ngCookies', 'pascalprecht.translate', 'restangular', 'ui.router', 'alert-message',
			'loading-animation', 'ajoslin.promise-tracker', 'creatureEditor.mod', 'creatureEditor.spell', 'creatureEditor.category' ]);

	module.config([ '$urlRouterProvider', '$locationProvider', '$translateProvider', '$translatePartialLoaderProvider', 'RestangularProvider',
			function AppConfig($urlRouterProvider, $locationProvider, $translateProvider, $translatePartialLoaderProvider, RestangularProvider) {
				RestangularProvider.setBaseUrl('/rest');
				// $locationProvider.html5Mode(true);
				$urlRouterProvider.otherwise('/');
				$translateProvider.useLocalStorage();
				$translatePartialLoaderProvider.addPart('app');
				$translateProvider.useLoader('$translatePartialLoader', {
					urlTemplate : 'src/{part}/locales/messages-{lang}.json'
				});
				$translateProvider.preferredLanguage('en');
				$translateProvider.fallbackLanguage('en');
			} ]);

	module.config([ '$loadingAnimationProvider', function LoadingAnimationConfig($loadingAnimationProvider) {
		$loadingAnimationProvider.activationDelay(0.5);
		$loadingAnimationProvider.minDuration(1);
	} ]);

	module.constant('appSettings', {
		restBaseUrl : 'rest/'
	});

	module.controller('AppController', [ '$scope', '$translate', '$rootScope', function AppController($scope, $translate, $rootScope) {
		$scope.changeLanguage = function(langKey) {
			$translate.uses(langKey);
		};
	} ]);

	module.directive('focusMe', [ '$timeout', function FocusMeDirective($timeout) {
		return function(scope, element, attrs) {
			scope.$watch(attrs.focusMe, function(value) {
				if (value) {
					$timeout(function() {
						element.focus();
					}, 700);
				}
			});
		};
	} ]);

	module.filter('range', function RangeFilter() {
		return function(input, total) {
			total = parseInt(total, 10);
			for (var i = 0; i < total; i++) {
				input.push(i);
			}
			return input;
		};
	});

	module.run([ '$rootScope', '$state', '$stateParams', function AppRun($rootScope, $state, $stateParams) {
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
	} ]);

})();
