var app = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'creatureEditor.category', 'creatureEditor.spell', 'creatureEditor.mod',
		'ngRoute', 'ui.router', 'ngGrid', 'ui.bootstrap', 'ui-components' ]);

app.config(function($stateProvider, $urlRouterProvider) {
	'use strict';
	$urlRouterProvider.otherwise('/');
});

app.controller('AppCtrl', function($scope, $location) {
	'use strict';
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

app.run(function run($rootScope, $state, $stateParams) {
	'use strict';
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
});
