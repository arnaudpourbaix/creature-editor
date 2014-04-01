/* global _ */
(function(_) {
	'use strict';

	var module = angular.module('creatureEditor.spell.controllers', [ 'creatureEditor.spell.services', 'alert-message', 'crud', 'ui.bootstrap' ]);

	module.controller('SpellController', [ '$scope', '$translateWrapper', '$state', 'SpellImportService', 'mods', 'flags', 'exclusionFlags',
			function SpellController($scope, $translateWrapper, $state, SpellImportService, mods, flags, exclusionFlags) {
		
				$scope.importService = SpellImportService;
				$scope.mods = mods;
				$scope.flags = flags;
				$scope.exclusionFlags = exclusionFlags;
				$scope.modId = null;

				$scope.layout = {
					options : {
						width : 1200,
						height : 800
					},
					windows : [ {
						id : 'spell-list',
						height : 700
					}, {
						id : 'spell-import',
						height : 100,
					} ]
				};
				
				$translateWrapper('MOD.SELECT_MOD').then(function(translation) {
					$scope.modsSelect = {
						data : 'mods',
						displayMember : 'name',
						valueMember : 'id',
						select : 'selectMod',
						options : {
							placeHolder : translation,
							width : '200px'
						}
					};
				});
				
				$scope.selectMod = function(modId) {
					$state.go('spells.list', {
						modId : $scope.modId
					});
				};

			} ]);

	module.controller('SpellListController', [
			'$scope',
			'$stateParams',
			'$location',
			'$translateWrapper',
			'spells',
			'crudListMethods',
			'$alertMessage',
			'$interpolate',
			function SpellListController($scope, $stateParams, $location, $translateWrapper, spells, crudListMethods, $alertMessage, $interpolate) {
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
					$translateWrapper(
							[ 'SPELL.FIELDS.RESOURCE', 'SPELL.FIELDS.NAME', 'SPELL.FIELDS.LEVEL', 'SPELL.FIELDS.IDENTIFIER', 'SPELL.FIELDS.TYPE',
									'SPELL.FIELDS.SCHOOL', 'SPELL.FIELDS.SECONDARY_TYPE', 'SPELL.COLUMNS.ACTION', 'SPELL.COLUMNS.DELETE' ]).then(function(labels) {
						$scope.spellGrid = {
							data : 'spells',
							columns : [ {
								text : labels['SPELL.FIELDS.RESOURCE'],
								dataField : 'resource',
								type : 'string',
								align : 'center',
								width : 80
							}, {
								text : labels['SPELL.FIELDS.NAME'],
								dataField : 'name',
								type : 'string',
								align : 'center',
								width : 200
							}, {
								text : labels['SPELL.FIELDS.LEVEL'],
								dataField : 'level',
								type : 'number',
								cellsalign : 'center',
								align : 'center',
								width : 55
							}, {
								text : labels['SPELL.FIELDS.IDENTIFIER'],
								dataField : 'identifier',
								type : 'string',
								align : 'center',
								width : 200
							}, {
								text : labels['SPELL.FIELDS.TYPE'],
								dataField : 'type',
								type : 'string',
								filtertype : 'checkedlist',
								align : 'center',
								width : 80
							}, {
								text : labels['SPELL.FIELDS.SCHOOL'],
								dataField : 'school',
								type : 'string',
								filtertype : 'checkedlist',
								align : 'center',
								width : 100
							}, {
								text : labels['SPELL.FIELDS.SECONDARY_TYPE'],
								dataField : 'secondaryType',
								type : 'string',
								filtertype : 'checkedlist',
								align : 'center',
								width : 100
							}, {
								text : labels['SPELL.COLUMNS.ACTION'],
								type : 'string',
								width : 60,
								align : 'center',
								cellsalign : 'center',
								filterable : false,
								sortable : false,
								cellsrenderer : function(row, columnfield, value, defaulthtml, columnproperties) {
									return $interpolate('<div class="pointer text-center"><span class="glyphicon glyphicon-trash" title="{{title}}" /></div>')({
										title : labels['SPELL.COLUMNS.DELETE']
									});
								}
							} ],
							options : {
								width : 1000,
								height : 590,
								pageable : true,
								pagerButtonsCount : 10,
								pageSize : 20
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
						$translateWrapper('SPELL.LABEL').then(function(label) {
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

	module.controller('SpellEditController', [ '$scope', '$q', '$translateWrapper', '$state', '$stateParams', 'SpellService',
			function SpellEditController($scope, $q, $translateWrapper, $state, $stateParams, SpellService) {
		
				$scope.window = {
					options : {
						width: 900,
						height: 800
					}
				};

				$scope.create = $stateParams.spellId === 'new';
				if ($scope.create) {
					$scope.spell = SpellService.getNew();
				} else {
					$scope.spell = angular.copy(SpellService.getById($scope.spells, $stateParams.spellId));
				}
				
				console.log($scope.spell.name, $scope.spell);

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
					return $q.when(result ? result : $translateWrapper('SPELL.NONE'));
				}

				getFlags($scope.flags, $scope.spell.flags).then(function(result) {
					$scope.flags = result;
				});

				getFlags($scope.exclusionFlags, $scope.spell.exclusionFlags).then(function(result) {
					$scope.exclusionFlags = result;
				});

				$scope.onCancel = function() {
					console.log('cancel');
					$state.go('^');
				};

				$scope.onSave = function() {
					if ($scope.create) {
						// $scope.mods.push($scope.mod);
					} else {
						// var item = ModService.getById($scope.mods, $scope.mod.id);
						// angular.extend(item, $scope.mod);
					}
					console.log('save');
					$state.go('^');
				};

				$scope.onSaveError = function() {
					console.error('save error');
					$state.go('^');
				};

				$scope.onRemove = function() {
					// var item = ModService.getById($scope.mods, $scope.mod.id);
					// $scope.mods.splice($scope.mods.indexOf(item), 1);
					console.log('remove');
					$state.go('^');
				};

				$scope.onRemoveError = function() {
					console.error('remove error');
					$state.go('^');
				};

			} ]);

})(_);
