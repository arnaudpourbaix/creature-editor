(function() {
	'use strict';

	var mod = angular.module('creatureEditor.mod.controllers', [ 'ui.router', 'ngRoute', 'ngResource', /* 'notification.i18n' */]);

	mod.controller('ModListController', function ModListController($scope, $state, mods, i18nNotifications) {

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
				i18nNotifications.pushForCurrentRoute('crud.mod.remove.success', 'success', {
					name : row.entity.name
				});
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

	mod.controller('ModDetailController', function ModDetailController($scope, $modalInstance, mod, i18nNotifications) {

		$scope.mod = mod;

		$scope.create = function() {
			$scope.mod.$save(function() {
				i18nNotifications.pushForCurrentRoute('crud.mod.save.success', 'success', {
					name : $scope.mod.name
				});
				$modalInstance.close();
			});
		};
	});

})();
