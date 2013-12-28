var mod = angular.module('creatureEditor.mod', [ 'ui.state', 'ngResource' ]);

mod.config(function config($stateProvider) { 'use strict';
	$stateProvider.state('mod', {
		url : '/mod',
		views : {
			"main" : {
				controller : 'ModListController',
				templateUrl : 'mod/mod-list.tpl.html'
			}
		}
	});
});

mod.factory('Mod', function ($resource) { 'use strict';
	return $resource('rest/mod/:id', {}, {
		'save': {method:'PUT'}
	});
});

mod.controller('ModListController', function ModListController($scope, $location, Mod) { 'use strict';
	$scope.title = 'Hello';
	$scope.mods = Mod.query();

	$scope.gotoModNewPage = function() {
		$location.path("/mod/new");
	};
	$scope.deleteMod = function(mod) {
		mod.$delete({
			'id' : mod.id
		}, function() {
			$location.path('/');
		});
	};
});
