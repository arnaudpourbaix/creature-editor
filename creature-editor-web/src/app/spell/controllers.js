(function() {
	'use strict';

	var module = angular.module('creatureEditor.spell.controllers', [ 'creatureEditor.spell.services', 'alertMessage', 'crud', 'ui.bootstrap' ]);

	module.controller('SpellController', function SpellController($scope, SpellImportService, mods) {
		$scope.importService = SpellImportService;
		$scope.mods = mods;
	});

	module.controller('SpellListController', function SpellListController($scope, $stateParams, $location, spells, crudListMethods, alertMessageService) {
		angular.extend($scope, crudListMethods($location.url()));

		$scope.modId = parseInt($stateParams.modId, 10);

		$scope.spells = spells;

		$scope.$watch('importService.running', function() {
			if ($scope.importService.running && $scope.importService.mod.id === $scope.modId) {
				$scope.spells = $scope.importService.spells;
				$scope.$emit('jqGrid-new-data');
			}
		});

		$scope.spellGrid = {
			data : 'spells',
			columns : [ {
				text : 'Resource',
				dataField : 'resource',
				type : 'string',
				align : 'center',
				width : 80
			}, {
				text : 'Name',
				dataField : 'name',
				type : 'string',
				align : 'center',
				width : 200
			}, {
				text : 'Level',
				dataField : 'level',
				cellsalign : 'center',
				type : 'number',
				align : 'center',
				width : 55
			}, {
				text : 'Identifier',
				dataField : 'identifier',
				type : 'string',
				align : 'center',
				width : 200
			}, {
				text : 'Actions',
				type : 'string',
				width : 60,
				align : 'center',
				cellsalign : 'center',
				filterable : false,
				sortable : false,
				cellsrenderer : function(row, columnfield, value, defaulthtml, columnproperties) {
					return '<div class="pointer text-center"><span class="glyphicon glyphicon-trash" title="Delete" /></div>';
				}
			} ],
			options : {
				width : 670,
				height : 400,
				pageable : true,
				pagerButtonsCount : 10,
				pageSize : 15
			},
			events : {
				cellClick : function($scope, spell, column) {
					if (column === 4) {
						$scope.remove(spell);
					} else {
						$scope.edit(spell.id);
					}
				}
			}
		};

		$scope.remove = function(spell) {
			spell.$delete().then(function(response) {
				$scope.removeFromList($scope.spells, 'id', spell.id);
				alertMessageService.push('spell.remove.success', 'success', {
					name : spell.name
				});
			});
		};

	});

	module.controller('SpellImportController', function SpellImportController($scope, $modalInstance, SpellImportService, mod) {
		$scope.mod = mod;

		$scope.confirm = function() {
			SpellImportService.startImport($scope.mod);
			$modalInstance.close();
		};

	});

	module.controller('SpellEditController', function SpellEditController($scope, $modalInstance, spell, alertMessageService) {
		$scope.spell = spell;
		$scope.onSave = function(spell) {
			alertMessageService.push('spell.save.success', 'success', {
				name : spell.name
			});
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onSaveError = function() {
			alertMessageService.push('spell.save.error', 'danger');
		};

		$scope.onRemove = function(spell) {
			alertMessageService.push('spell.remove.success', 'success', {
				name : spell.name
			});
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onRemoveError = function() {
			alertMessageService.push('spell.remove.error', 'danger');
		};
	});

})();
