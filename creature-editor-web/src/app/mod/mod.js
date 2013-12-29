var mod = angular.module('creatureEditor.mod', [ 'ui.router', 'ngRoute', 'ngResource' ]);

mod.config(function config($routeProvider, $stateProvider) {
	'use strict';
	$stateProvider.state('mod', {
		abstact : true,
		url : '/mod',
		template : '<ui-view/>'
	}).state('mod.list', {
		url : '/list',
		controller : 'ModListController',
		templateUrl : 'mod/mod-list.tpl.html'
	}).state('mod.detail', {
		url : '/detail/:modId',
		controller : 'ModDetailController',
		templateUrl : 'mod/mod-detail.tpl.html'
	});
});

mod.factory('Mod', function($resource) {
	'use strict';
	return $resource('rest/mod/:id', {}, {
		'save' : {
			method : 'PUT'
		}
	});
});

mod.controller('ModListController', function ModListController($scope, $location, $state, Mod) {
	'use strict';
	$scope.mods = Mod.query();

	$scope.newMod = function() {
		$state.go('mod.detail', {
			modId : 'new'
		});
	};
	$scope.deleteMod = function(mod) {
		mod.$delete({
			'id' : mod.id
		}, function() {
			$location.path('/');
		});
	};
});

mod.controller('ModDetailController', function ModDetailController($scope, $routeParams, $state, $stateParams, $location, Mod) {
	'use strict';
	var modId = $stateParams.modId;
	if (modId === 'new') {
		$scope.mod = new Mod();
	} else {
		$scope.mod = Mod.get({
			id : modId
		});
	}

	$scope.save = function() {
		$scope.mod.$save(function() {
			$state.go('mod.list');
		});
	};
});
