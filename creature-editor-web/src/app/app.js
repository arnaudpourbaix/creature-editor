(function() {
	'use strict';

	var app = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'creatureEditor.category', 'creatureEditor.spell', 'creatureEditor.mod',
			'ngRoute', 'ui.router', 'ngGrid', 'ui.bootstrap', 'ui-components', 'notification.i18n', 'crud.directives' ]);

	app.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
		// $locationProvider.html5Mode(true);
		$urlRouterProvider.otherwise('/');
	});

	app.constant('I18N.MESSAGES', {
		'errors.route.changeError' : 'Route change error',
		'crud.user.save.success' : "A user with id '{{id}}' was saved successfully.",
		'crud.user.remove.success' : "A user with id '{{id}}' was removed successfully.",
		'crud.user.remove.error' : "Something went wrong when removing user with id '{{id}}'.",
		'crud.user.save.error' : "Something went wrong when saving a user...",
		'crud.project.save.success' : "A project with id '{{id}}' was saved successfully.",
		'crud.project.remove.success' : "A project with id '{{id}}' was removed successfully.",
		'crud.project.save.error' : "Something went wrong when saving a project...",
		'login.reason.notAuthorized' : "You do not have the necessary access permissions.  Do you want to login as someone else?",
		'login.reason.notAuthenticated' : "You must be logged in to access this part of the application.",
		'login.error.invalidCredentials' : "Login failed.  Please check your credentials and try again.",
		'login.error.serverError' : "There was a problem with authenticating: {{exception}}."
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
