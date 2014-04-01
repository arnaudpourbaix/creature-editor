(function() {
	'use strict';

	var module = angular.module('creatureEditor.main.controllers', [ 'pascalprecht.translate' ]);

	module.controller('MainController', [ '$scope', '$translate', '$rootScope', function MainController($scope, $translate, $rootScope) {

		$scope.$onRootScope('$translateChangeSuccess', function() {
			$scope.langKey = $translate.use();
		});

		$scope.changeLanguage = function(langKey) {
			$translate.use(langKey);
		};
		
		$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
//			console.info('$stateChangeStart', toState.name, 'start');
		});

		$rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
//			console.info('$stateChangeSuccess', toState.name, 'success');
		});
		
		$rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
			console.error('$stateChangeError', toState.name, error);
		});

		$rootScope.$on('$viewContentLoading ', function() {
//			console.info('$viewContentLoading ', 'success');
		});

		$rootScope.$on('$viewContentLoaded', function() {
//			console.info('$viewContentLoaded', 'success');
		});
		
	} ]);

})();
