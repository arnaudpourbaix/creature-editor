(function() {
	'use strict';

	var module = angular.module('creatureEditor.spell.controllers', [ 'creatureEditor.spell.services', 'alert-message', 'crud', 'ui.bootstrap' ]);

	module.controller('SpellController', [ '$scope', 'SpellImportService', 'mods', function SpellController($scope, SpellImportService, mods) {
		$scope.importService = SpellImportService;
		$scope.mods = mods;
	} ]);

	module.controller('SpellListController', [ '$scope', '$stateParams', '$location', '$translate', 'spells', 'crudListMethods', 'alertMessageService',
			function SpellListController($scope, $stateParams, $location, $translate, spells, crudListMethods, alertMessageService) {
				console.log('SpellListController');

				angular.extend($scope, crudListMethods($location.url()));

				$scope.modId = parseInt($stateParams.modId, 10);

				$scope.spells = spells;

				$scope.$watch('importService.running', function() {
					if ($scope.importService.running && $scope.importService.mod.id === $scope.modId) {
						$scope.spells = $scope.importService.spells;
						$scope.$emit('jqGrid-new-data');
					}
				});

				var getSpellGrid = function() {
					return {
						data : 'spells',
						columns : [ {
							text : $translate('SPELL.RESOURCE_FIELD'),
							dataField : 'resource',
							type : 'string',
							align : 'center',
							width : 80
						}, {
							text : $translate('SPELL.NAME_FIELD'),
							dataField : 'name',
							type : 'string',
							align : 'center',
							width : 200
						}, {
							text : $translate('SPELL.LEVEL_FIELD'),
							dataField : 'level',
							cellsalign : 'center',
							type : 'number',
							align : 'center',
							width : 55
						}, {
							text : $translate('SPELL.IDENTIFIER_FIELD'),
							dataField : 'identifier',
							type : 'string',
							align : 'center',
							width : 200
						}, {
							text : $translate('SPELL.ACTION_COLUMN'),
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
				};

				$scope.spellGrid = getSpellGrid();
				$scope.$on('$translateChangeSuccess', function() {
					$scope.spellGrid = getSpellGrid();
				});

				$scope.remove = function(spell) {
					spell.$delete().then(function(response) {
						$scope.removeFromList($scope.spells, 'id', spell.id);
						alertMessageService.push('CRUD.REMOVE_SUCCESS', 'success', {
							name : spell.name
						});
					});
				};

			} ]);

	module.controller('SpellImportController', [ '$scope', '$modalInstance', 'SpellImportService', 'mod',
			function SpellImportController($scope, $modalInstance, SpellImportService, mod) {
				$scope.mod = mod;

				$scope.confirm = function() {
					SpellImportService.startImport($scope.mod);
					$modalInstance.close();
				};

			} ]);

	module.controller('SpellEditController', [ '$scope', '$modalInstance', 'spell', function SpellEditController($scope, $modalInstance, spell) {
		$scope.spell = spell;

		$scope.onSave = function(spell) {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onSaveError = function() {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onRemove = function(spell) {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onRemoveError = function() {
			$modalInstance.close({
				spell : spell
			});
		};
	} ]);

})();
