(function() {
	'use strict';

	var mod = angular.module('creatureEditor.mod.controllers', [ 'ui.router', 'ngRoute', 'ngResource', 'crud.services', 'ui.select2' ]);

	mod.controller('ModListController', function ModListController($scope, $state, mods, crudListMethods, i18nNotifications) {

		angular.extend($scope, crudListMethods('/mods'));

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
				cellTemplate : '<div class="ngCellText" ng-class="col.colIndex()" ng-click="edit(row.entity.id)"><span>{{row.getProperty(col.field)}}</span></div>'
			}, {
				sortable : false,
				cellClass : 'actionColumn',
				width : '60px',
				cellTemplate : '<span class="glyphicon glyphicon-trash" title="Delete" ng-click="remove(row.entity)" />'
			} ]
		};

		$scope.remove = function(mod) {
			mod.$delete().then(function(response) {
				$scope.removeFromList($scope.mods, 'id', mod.id);
				i18nNotifications.pushForCurrentRoute('crud.mod.remove.success', 'success', {
					name : mod.name
				});
			});
		};

	});

	mod.controller('ModEditController', function ModEditController($scope, $modalInstance, mod, i18nNotifications) {

		$scope.mod = mod;

		$scope.onSave = function(mod) {
			i18nNotifications.pushForCurrentRoute('crud.mod.save.success', 'success', {
				name : mod.name
			});
			$modalInstance.close();
		};

		$scope.onError = function() {
			i18nNotifications.pushForCurrentRoute('crud.mod.save.error', 'danger');
		};

		$scope.onRemove = function(mod) {
			i18nNotifications.pushForCurrentRoute('crud.mod.remove.success', 'success', {
				name : mod.name
			});
			$modalInstance.close();
		};
	});

	mod.controller('ModSelectController', function ModSelectController($scope, $state, mods) {

		$scope.mods = mods;

		$scope.modId = 3;
		$scope.$watch($scope.modId, function() {
			console.log('mod id=', $scope.modId);
		}, true);

		$scope.select2Options = {
			allowClear : true,
			width : 'element'
		};

	});

})();
