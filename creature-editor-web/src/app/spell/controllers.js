(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell.controllers', [ 'ui.router', 'ngRoute', 'ngResource', 'crud.services' ]);

	spell.controller('SpellController', function SpellController($scope, SpellImportService, mods) {
		$scope.importService = SpellImportService;
		$scope.mods = mods;
	});

	spell.controller('SpellListController', function SpellListController($scope, $stateParams, $location, SpellService, crudListMethods, i18nNotifications) {
		angular.extend($scope, crudListMethods($location.url()));

		$scope.modId = parseInt($stateParams.modId, 10);

		$scope.spells = SpellService.getSpells($scope.modId);

		$scope.$watch('importService.running', function() {
			if ($scope.importService.running && $scope.importService.mod.id === $scope.modId) {
				$scope.spells = $scope.importService.spells;
			}
		});

		$scope.gridOptions = {
			data : 'spells',
			enableRowSelection : false,
			enableColumnResize : true,
			showFilter : true,
			columnDefs : [ {
				field : 'resource',
				displayName : 'Resource',
				cellClass : 'cellName',
				width : '80px',
				cellTemplate : '<div class="ngCellText" ng-class="col.colIndex()" ng-click="edit(row.entity.id)"><span>{{row.getProperty(col.field)}}</span></div>'
			}, {
				field : 'name',
				displayName : 'Name'
			}, {
				field : 'level',
				displayName : 'Level',
				width : '55px'
			}, {
				field : 'identifier',
				displayName : 'Identifier'
			}, {
				sortable : false,
				cellClass : 'actionColumn',
				width : '60px',
				cellTemplate : '<span class="glyphicon glyphicon-trash" title="Delete" ng-click="remove(row.entity)" />'
			} ]
		};

		$scope.remove = function(spell) {
			spell.$delete().then(function(response) {
				$scope.removeFromList($scope.spells, 'id', spell.id);
				i18nNotifications.pushForCurrentRoute('crud.spell.remove.success', 'success', {
					name : spell.name
				});
			});
		};

	});

	spell.controller('SpellImportController', function SpellImportController($scope, $modalInstance, SpellImportService, mod) {
		$scope.mod = mod;

		$scope.confirm = function() {
			SpellImportService.startImport($scope.mod);
			$modalInstance.close();
		};

	});

	spell.controller('SpellEditController', function SpellEditController($scope, $modalInstance, spell, i18nNotifications) {
		$scope.spell = spell;
		$scope.onSave = function(spell) {
			i18nNotifications.pushForCurrentRoute('crud.spell.save.success', 'success', {
				name : spell.name
			});
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onError = function() {
			i18nNotifications.pushForCurrentRoute('crud.spell.save.error', 'danger');
		};

		$scope.onRemove = function(spell) {
			i18nNotifications.pushForCurrentRoute('crud.spell.remove.success', 'success', {
				name : spell.name
			});
			$modalInstance.close();
		};
	});

})();
