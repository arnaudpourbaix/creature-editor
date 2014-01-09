(function() {
	'use strict';

	var app = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'creatureEditor.category', 'creatureEditor.spell', 'creatureEditor.mod',
			'ngRoute', 'ui.router', 'ngGrid', 'ui.bootstrap', 'ui-components', 'notification.i18n', 'crud.directives' ]);

	app.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
		// $locationProvider.html5Mode(true);
		$urlRouterProvider.otherwise('/');
	});

	app.controller('AppCtrl', [ '$scope', 'i18nNotifications', 'localizedMessages', function($scope, i18nNotifications) {
		$scope.notifications = i18nNotifications;

		$scope.removeNotification = function(notification) {
			i18nNotifications.remove(notification);
		};

		$scope.$on('$routeChangeError', function(event, current, previous, rejection) {
			i18nNotifications.pushForCurrentRoute('errors.route.changeError', 'error', {}, {
				rejection : rejection
			});
		});
	} ]);

	app.directive('focusMe', function($timeout) {
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
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
	});

})();
