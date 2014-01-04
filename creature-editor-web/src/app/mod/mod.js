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
		url : '/:modId',
		onEnter : function($state, $stateParams, $modal, $timeout, Mod) {
			var modal = $modal.open({
				templateUrl : "mod/mod-detail.tpl.html",
				controller : 'ModDetailController',
				backdrop: false,
				resolve: { mod: function(Mod) {
					if ($stateParams.modId !== '-1') {
						return Mod.get({ id: $stateParams.modId });
					} else {
						return new Mod({ id : null, name : '' });
					}
				} }
			});
			modal.opened.then(function() {
				$timeout(function() {
					$(".modal-dialog").draggable({
						handle : $(".modal-content")
					});
				});
			});
			modal.result.then(function(result) {
				
			}).finally(function() {
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

	modFactory.checkUniqueValue = function(name) {
		return $http.get(serviceBase + 'checkUnique/' + name).then(function(results) {
			return results.data === 'true';
		});
	};

	return modFactory;

} ]);

mod.directive('uniqueName', [ 'modService', function(modService) {
	'use strict';
	var toId = null;
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function(scope, elem, attr, ctrl) {
			scope.$watch(attr.ngModel, function(value) { // when the scope changes, check the mod.
				if (angular.isString(value) && value.length === 0) {
					return;
				}
				if(toId) { // if there was a previous attempt, stop it.
					clearTimeout(toId);
				}
				toId = setTimeout(function(){
					modService.checkUniqueValue(value).then(function(unique) {
						console.debug('uniqueName', unique);
						ctrl.$setValidity('unique', unique);
					});
				}, 300);
			});
		}
	};
} ]);

mod.controller('ModListController', function ModListController($scope, $state, $timeout, mods) {
	'use strict';

	console.log(mods.$resolved, mods.length);
	$scope.mods = mods;

	$scope.gridOptions = {
		data : 'mods',
		enableCellSelection : false,
		enableRowSelection : false,
		columnDefs : [ {
			field : 'name',
			displayName : 'Name'
		}, {
			field : '',
			sortable : false,
			enableCellEdit : false,
			cellClass : 'actionColumn',
			width : '120px',
			cellTemplate : '<button class="btn btn-info btn-xs" ng-click="editEntity(row)">Edit</button>&nbsp;&nbsp;<button class="btn btn-danger btn-xs" ng-click="deleteEntity(row)">Delete</button>'
			// cellTemplate : '<span class="glyphicon glyphicon-trash" title="Delete" ng-click="deleteEntity(row)"></span>'
		} ]
	};

	$scope.editEntity = function(row) {
		$state.go('.detail', { modId: row.entity.id });
	};
	
	$scope.deleteEntity = function(row) {
		row.entity.$delete({
			id : row.entity.id
		}).then(function(response) {
			// if (response.status === 'OK') {
			remove($scope.mods, 'id', row.entity.id);
			// }
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
		console.log('saving', $scope.mod);
		$scope.mod.$save(function() {
			$modalInstance.close();
		});
	};
});
