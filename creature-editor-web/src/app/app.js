var app = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'creatureEditor.category', 'creatureEditor.spell', 'creatureEditor.mod',
		'ngRoute', 'ui.router', 'ngGrid', 'ui.bootstrap' ]);

app.config(function($stateProvider, $urlRouterProvider) {
	'use strict';
	$urlRouterProvider.otherwise('/mod/list');
});

app.controller('AppCtrl', function($scope, $location) {
	'use strict';
	/*
	 * $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) { if (angular.isDefined(toState.data.pageTitle)) {
	 * $scope.pageTitle = toState.data.pageTitle + ' | ngBoilerplate'; } });
	 */
});

app.directive('focusMe', function($timeout) {
	'use strict';
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

app.directive('validateSubmit', [ '$parse', function($parse) {
	'use strict';
	return {
		restrict : 'A',
		require : 'form',
		link : function(scope, formElement, attributes, formController) {
			var fn = $parse(attributes.validateSubmit);
			formElement.bind('submit', function(event) {
				console.debug('validateSubmit');
				// if form is not valid cancel it.
				if (!formController.$valid) {
					return false;
				}
				scope.$apply(function() {
					fn(scope, {
						$event : event
					});
				});
			});
		}
	};
} ]);

app.run(function run($rootScope, $state) {
	'use strict';
	$rootScope.$state = $state;
});
