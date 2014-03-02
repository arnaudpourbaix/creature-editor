/* global _ */
(function(_) {
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
							[ $translate('SPELL.FIELDS.RESOURCE'), $translate('SPELL.FIELDS.NAME'), $translate('SPELL.FIELDS.LEVEL'), $translate('SPELL.FIELDS.IDENTIFIER'),
									$translate('SPELL.FIELDS.TYPE'), $translate('SPELL.FIELDS.SCHOOL'), $translate('SPELL.COLUMNS.ACTION'), $translate('SPELL.FIELDS.COLUMNS.DELETE'),
									$translate('SPELL.FIELDS.SECONDARY_TYPE') ]).then(function(labels) {
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

	module.controller('SpellImportController', [ '$scope', '$windowInstance', 'SpellImportService', 'mod',
			function SpellImportController($scope, $windowInstance, SpellImportService, mod) {
				$scope.mod = mod;

				$scope.confirm = function() {
					SpellImportService.startImport($scope.mod);
					$windowInstance.close();
				};

			} ]);

	module.controller('SpellEditController', [ '$scope', '$windowInstance', '$q', '$translate', 'spell', 'flags', 'exclusionFlags',
			function SpellEditController($scope, $windowInstance, $q, $translate, spell, flags, exclusionFlags) {
				$scope.spell = spell;
				console.log(spell);

				function getFlags(flags, num) {
					var result = _.reduce(flags, function(result, flag, key) {
						var value = Math.pow(2, flag.bit);
						if ((num & value) === value) {
							if (result) {
								result += ', ';
							}
							result += flag.label;
						}
						return result;
					}, '');
					return $q.when(result ? result : $translate('SPELL.NONE'));
				}

				getFlags(flags, spell.flags).then(function(result) {
					$scope.flags = result;
				});
				getFlags(exclusionFlags, spell.exclusionFlags).then(function(result) {
					$scope.exclusionFlags = result;
				});

				$scope.onSave = function(spell) {
					$windowInstance.close({
						spell : spell
					});
				};

				$scope.onSaveError = function(spell) {
					$windowInstance.close({
						spell : spell
					});
				};

				$scope.onRemove = function(spell) {
					$windowInstance.close({
						spell : spell
					});
				};

				$scope.onRemoveError = function(spell) {
					$windowInstance.close({
						spell : spell
					});
				};
			} ]);

})(_);
