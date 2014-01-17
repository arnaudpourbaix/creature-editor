(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell.controllers', [ 'ui.router', 'ngRoute', 'ngResource', 'crud.services' ]);

	spell.controller('SpellListController', function SpellListController($scope, $state, spells, crudListMethods, i18nNotifications) {

		angular.extend($scope, crudListMethods('/spells'));

		$scope.spells = spells;

		$scope.gridOptions = {
			data : 'spells',
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

		$scope.$on('selectedMod', function(e, mod) {
			$scope.mod = mod;
			e.stopPropagation();
		});

		$scope.remove = function(spell) {
			spell.$delete().then(function(response) {
				$scope.removeFromList($scope.spells, 'id', spell.id);
				i18nNotifications.pushForCurrentRoute('crud.spell.remove.success', 'success', {
					name : spell.name
				});
			});
		};

	});

	spell.controller('SpellImportController', function SpellImportController($scope, $modalInstance, SpellService, i18nNotifications) {

		$scope.mod = null;

		$scope.$on('selectedMod', function(e, mod) {
			$scope.mod = mod;
			e.stopPropagation();
		});

		$scope.import = function() {
			$scope.progressValue = 0;
			SpellService.startImportSpells($scope.mod.id);
		};

	});

	spell.controller('SpellEditController', function SpellEditController($scope, $modalInstance, spell, i18nNotifications) {

		$scope.spell = spell;

		$scope.onSave = function(spell) {
			i18nNotifications.pushForCurrentRoute('crud.spell.save.success', 'success', {
				name : spell.name
			});
			$modalInstance.close();
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
