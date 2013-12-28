angular.module('creatureEditor.mod', [ 'ui.state', 'ngResource' ])

.config(function config($stateProvider) { 'use strict';
	$stateProvider.state('mod', {
		url : '/mod',
		views : {
			"main" : {
				controller : 'ModListController',
				templateUrl : 'mod/mod-list.tpl.html'
			}
		}
	});
})

.factory('Mod', function ($resource) { 'use strict';
	return $resource('mod/:id', {}, {
		'save': {method:'PUT'}
	});
})

.controller('ModListController', function ModListController($scope, $location, Mod) { 'use strict';
	$scope.mods = Mod.query();
	/*Mod.query().$promise.then(function(res) {
		var list = res, source = {
			datatype : "json",
			datafields : [ {
				name : 'id'
			}, {
				name : 'name'
			}, {
				name : 'parentId'
			} ],
			id : 'id',
			localdata : list
		}, dataAdapter = new $.jqx.dataAdapter(source, {
			autoBind : true
		});
		$scope.mods = dataAdapter.getRecordsHierarchy('id', 'parentId', null, [ {
			name : 'name',
			map : 'label'
		} ]);
	});*/

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
