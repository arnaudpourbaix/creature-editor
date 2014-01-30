(function() {
	'use strict';

	var module = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'alertMessage', 'restangular', 'creatureEditor.mod', 'creatureEditor.spell',
			'creatureEditor.category' ]);

	module.config(function($urlRouterProvider, $locationProvider, RestangularProvider) {
		RestangularProvider.setBaseUrl('/rest');
		// $locationProvider.html5Mode(true);
		$urlRouterProvider.otherwise('/');
	});

	module.controller('AppCtrl', [ '$scope', function($scope) {
	} ]);

	module.directive('focusMe', function($timeout) {
		return function(scope, element, attrs) {
			scope.$watch(attrs.focusMe, function(value) {
				if (value) {
					$timeout(function() {
						element.focus();
					}, 700);
				}
			});
		};
	});

	module.filter('range', function() {
		return function(input, total) {
			total = parseInt(total, 10);
			for (var i = 0; i < total; i++) {
				input.push(i);
			}
			return input;
		};
	});

	module.run(function run($rootScope, $state, $stateParams) {
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
	});

})();
