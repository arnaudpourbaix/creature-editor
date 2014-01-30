(function() {
	'use strict';

	// var app = angular.module('creatureEditor', [ 'ngRoute', 'ui.router', 'ngAnimate', 'ngAnimate-animate.css', 'ngGrid', 'ui.bootstrap', 'ui-components',
	// 'jqwidgets' ]);

	var module = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'alertMessage', 'creatureEditor.mod', 'creatureEditor.spell',
			'creatureEditor.category' ]);

	module.config(function($urlRouterProvider, $locationProvider) {
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
