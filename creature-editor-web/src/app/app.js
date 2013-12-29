var app = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'creatureEditor.category', 'creatureEditor.spell', 'creatureEditor.mod',
		'ngRoute', 'ui.router' ]);

app.config(function($stateProvider, $urlRouterProvider) {
	'use strict';
	// $urlRouterProvider.otherwise('/mod');
});

app.controller('AppCtrl', function($scope, $location) {
	'use strict';
	/*
	 * $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) { if (angular.isDefined(toState.data.pageTitle)) {
	 * $scope.pageTitle = toState.data.pageTitle + ' | ngBoilerplate'; } });
	 */
});

app.run(function run($rootScope, $state) {
	'use strict';
	$rootScope.$state = $state;
});
