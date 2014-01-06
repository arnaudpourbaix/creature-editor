var mod = angular.module('creatureEditor.mod', [ 'ui.router', 'ngRoute', 'ngResource' ]);

mod.config(function config($stateProvider) {
	'use strict';
	$stateProvider.state('mods', {
		url : '/mods',
		resolve : {
			mods : [ 'Mod', function(Mod) {
				return Mod.query();
			} ]
		},
		controller : 'ModListController',
		templateUrl : 'mod/mod-list.tpl.html'
	}).state('mods.detail', {
		url : '/detail/:modId',
		onEnter : function($state, $stateParams, $modal, $timeout, Mod) {
			var modal = $modal.open({
				templateUrl : "mod/mod-detail.tpl.html",
				controller : 'ModDetailController',
				backdrop : false,
				resolve : {
					mod : function(Mod) {
						if ($stateParams.modId !== '-1') {
							return Mod.get({
								id : $stateParams.modId
							});
						} else {
							return new Mod({
								id : null,
								name : ''
							});
						}
					}
				}
			});
			modal.result.then(function(result) {
				$state.go('^', {}, {
					reload : true
				});
			}, function() {
				$state.go('^');
			});
		}
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

mod.factory('modService', [ '$http', function($http) {
	'use strict';
	var serviceBase = 'rest/mod/', modFactory = {};

	modFactory.checkUniqueValue = function(id, name) {
		var idParam = id == null ? -1 : id;
		return $http.get(serviceBase + 'checkUnique/' + name + '/' + idParam).then(function(results) {
			return results.data === 'true';
		});
	};

	return modFactory;

} ]);

mod.directive('uniqueName', [ 'modService', function(modService) {
	'use strict';
	var toId = null;
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, elem, attr, ctrl) {
			scope.$watch(attr.ngModel, function(value) { // when the scope changes, check the mod.
				if (angular.isString(value) && value.length === 0) {
					return;
				}
				if (toId) { // if there was a previous attempt, stop it.
					clearTimeout(toId);
				}
				var params = scope.$eval(attr.uniqueName);
				toId = setTimeout(function() {
					modService.checkUniqueValue(params.id, value).then(function(unique) {
						ctrl.$setValidity('unique', unique);
					});
				}, 300);
			});
		}
	};
} ]);

mod.controller('ModListController', function ModListController($scope, $state, $timeout, mods) {
	'use strict';

	$scope.mods = mods;

	$scope.gridOptions = {
		data : 'mods',
		enableRowSelection : false,
		enableColumnResize : true,
		showFilter : true,
		columnDefs : [ {
			field : 'name',
			displayName : 'Name',
			cellClass : 'cellName',
			cellTemplate : '<div class="ngCellText" ng-class="col.colIndex()" ng-click="editEntity(row)"><span>{{row.getProperty(col.field)}}</span></div>'
		}, {
			field : '',
			sortable : false,
			cellClass : 'actionColumn',
			width : '60px',
			cellTemplate : '<span class="glyphicon glyphicon-trash" title="Delete" ng-click="deleteEntity(row)" />'
		} ]
	};

	$scope.editEntity = function(row) {
		$state.go('.detail', {
			modId : row.entity.id
		});
	};

	$scope.deleteEntity = function(row) {
		row.entity.$delete({
			id : row.entity.id
		}).then(function(response) {
			remove($scope.mods, 'id', row.entity.id);
		});
	};

	function remove(array, property, value) {
		$.each(array, function(index, result) {
			if (result[property] === value) {
				array.splice(index, 1);
			}
		});
	}

});

mod.controller('ModDetailController', function ModDetailController($scope, $modalInstance, mod) {
	'use strict';
	$scope.mod = mod;

	$scope.create = function() {
		$scope.mod.$save(function() {
			$modalInstance.close();
		});
	};
});
