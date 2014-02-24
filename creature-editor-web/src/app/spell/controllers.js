(function() {
	'use strict';

	var module = angular.module('creatureEditor.spell.controllers', [ 'creatureEditor.spell.services', 'alert-message', 'crud', 'ui.bootstrap' ]);

	module.controller('SpellController', [ '$scope', 'SpellImportService', 'mods', function SpellController($scope, SpellImportService, mods) {
		$scope.importService = SpellImportService;
		$scope.mods = mods;
	} ]);

	module.controller('SpellListController', [
			'$scope',
			'$stateParams',
			'$location',
			'$translate',
			'spells',
			'crudListMethods',
			'$alertMessage',
			'$q',
			'$interpolate',
			function SpellListController($scope, $stateParams, $location, $translate, spells, crudListMethods, $alertMessage, $q, $interpolate) {
				angular.extend($scope, crudListMethods($location.url()));

				$scope.modId = parseInt($stateParams.modId, 10);

				$scope.spells = spells;

				$scope.$watch('importService.running', function() {
					if ($scope.importService.running && $scope.importService.mod.id === $scope.modId) {
						$scope.spells = $scope.importService.spells;
						$scope.$broadcast('jqGrid-new-data');
					}
				});

				var setSpellGrid = function() {
					$q.all(
							[ $translate('SPELL.RESOURCE_FIELD'), $translate('SPELL.NAME_FIELD'), $translate('SPELL.LEVEL_FIELD'), $translate('SPELL.IDENTIFIER_FIELD'),
									$translate('SPELL.TYPE_FIELD'), $translate('SPELL.SCHOOL_FIELD'), $translate('SPELL.ACTION_COLUMN'), $translate('SPELL.ACTION_DELETE'),
									$translate('SPELL.SECONDARY_TYPE_FIELD') ]).then(function(labels) {
						$scope.spellGrid = {
							data : 'spells',
							columns : [ {
								text : labels[0],
								dataField : 'resource',
								type : 'string',
								align : 'center',
								width : 80
							}, {
								text : labels[1],
								dataField : 'name',
								type : 'string',
								align : 'center',
								width : 200
							}, {
								text : labels[2],
								dataField : 'level',
								type : 'number',
								cellsalign : 'center',
								align : 'center',
								width : 55
							}, {
								text : labels[3],
								dataField : 'identifier',
								type : 'string',
								align : 'center',
								width : 200
							}, {
								text : labels[4],
								dataField : 'type',
								type : 'string',
								filtertype : 'checkedlist',
								align : 'center',
								width : 80
							}, {
								text : labels[5],
								dataField : 'school',
								type : 'string',
								filtertype : 'checkedlist',
								align : 'center',
								width : 100
							}, {
								text : labels[8],
								dataField : 'secondaryType',
								type : 'string',
								filtertype : 'checkedlist',
								align : 'center',
								width : 100
							}, {
								text : labels[6],
								type : 'string',
								width : 60,
								align : 'center',
								cellsalign : 'center',
								filterable : false,
								sortable : false,
								cellsrenderer : function(row, columnfield, value, defaulthtml, columnproperties) {
									return $interpolate('<div class="pointer text-center"><span class="glyphicon glyphicon-trash" title="{{title}}" /></div>')({
										title : labels[7]
									});
								}
							} ],
							options : {
								width : 1000,
								height : 400,
								pageable : true,
								pagerButtonsCount : 10,
								pageSize : 15
							},
							events : {
								cellClick : function($scope, spell, column) {
									if (column === 7) {
										$scope.remove(spell);
									} else {
										$scope.edit(spell.id);
									}
								}
							}
						};
					});
				};

				setSpellGrid();
				$scope.$onRootScope('$translateChangeSuccess', function() {
					setSpellGrid();
				});

				$scope.remove = function(spell) {
					spell.$delete().then(function(response) {
						$scope.removeFromList($scope.spells, 'id', spell.id);
						$translate('SPELL.LABEL').then(function(label) {
							$alertMessage.push('CRUD.REMOVE_SUCCESS', 'info', {
								entity : label,
								name : spell.resource
							});
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

	module.controller('SpellEditController', [ '$scope', '$modalInstance', 'spell', 'flags', function SpellEditController($scope, $modalInstance, spell, flags) {
		$scope.spell = spell;
		console.log(spell);
		console.log(flags);

		$scope.onSave = function(spell) {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onSaveError = function(spell) {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onRemove = function(spell) {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onRemoveError = function(spell) {
			$modalInstance.close({
				spell : spell
			});
		};
	} ]);

})();
