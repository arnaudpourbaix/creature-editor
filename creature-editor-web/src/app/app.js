(function() {
	'use strict';

	var app = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'creatureEditor.category', 'creatureEditor.spell', 'creatureEditor.mod', 'ngRoute',
			'ui.router', 'ngAnimate', 'ngAnimate-animate.css', 'ngGrid', 'ui.bootstrap', 'ui-components', 'notification.i18n', 'crud.directives', 'jqwidgets' ]);

	app.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
		// $locationProvider.html5Mode(true);
		$urlRouterProvider.otherwise('/');
	});

	app.constant('I18N.MESSAGES', {
		'crud.mod.save.success' : "Mod '{{name}}' was saved successfully.",
		'crud.mod.remove.success' : "Mod '{{name}}' was removed successfully.",
		'crud.mod.remove.error' : "Error when removing mod '{{name}}'.",
		'crud.mod.save.error' : "Error when saving a mod...",
		'spell.import.success' : "Spell import for mod {{name}} is done.",
		'spell.import.error' : "Spell import has encountered an internal error.",
		'spell.import.cancel' : "Spell import has been cancelled.",
		'crud.spell.save.success' : "Spell '{{name}}' was saved successfully.",
		'crud.spell.remove.success' : "Spell '{{name}}' was removed successfully.",
		'crud.spell.remove.error' : "Error when removing spell '{{name}}'.",
		'crud.spell.save.error' : "Error when saving a spell..."
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

	app.filter('range', function() {
		return function(input, total) {
			total = parseInt(total, 10);
			for (var i = 0; i < total; i++) {
				input.push(i);
			}
			return input;
		};
	});

	app.run(function run($rootScope, $state, $stateParams) {
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
	});

})();
