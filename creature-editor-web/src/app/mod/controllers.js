(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.controllers', [ 'creatureEditor.mod.services', 'crud', 'alert-message', 'translate-wrapper' ]);

	module.controller('ModListController', [ '$scope', '$translateWrapper', 'crudListMethods', '$alertMessage', '$interpolate', 'mods',
			function ModListController($scope, $translateWrapper, crudListMethods, $alertMessage, $interpolate, mods) {

				angular.extend($scope, crudListMethods('/mods'));

				$scope.splitter = {
					width : 800,
					height : 600,
					panels : [ {
						size : 400,
						min : 200
					}, {
						size : 400,
						min : 100
					} ]
				};

				$scope.mods = mods;

				$scope.remove = function(mod) {
					mod.$delete().then(function(response) {
						$scope.removeFromList($scope.mods, 'id', mod.$id());
						$translateWrapper('MOD.LABEL').then(function(label) {
							$alertMessage.push('CRUD.REMOVE_SUCCESS', 'info', {
								entity : label,
								name : mod.name
							});
						});
					}, function() {
						$alertMessage.push('CRUD.REMOVE_ERROR', 'danger', {
							name : mod.name
						});
					});
				};

				var setModGrid = function() {
					$translateWrapper([ 'MOD.FIELDS.NAME', 'MOD.COLUMNS.ACTION', 'MOD.COLUMNS.DELETE' ]).then(function(labels) {
						$scope.modGrid = {
							data : 'mods',
							columns : [ {
								text : labels['MOD.FIELDS.NAME'],
								datafield : 'name',
								type : 'string',
								align : 'center',
								width : 200
							}, {
								text : labels['MOD.COLUMNS.ACTION'],
								type : 'string',
								width : 60,
								align : 'center',
								cellsalign : 'center',
								filterable : false,
								sortable : false,
								cellsrenderer : function(row, columnfield, value, defaulthtml, columnproperties) {
									return $interpolate('<div class="pointer text-center"><span class="glyphicon glyphicon-trash" title="{{title}}" /></div>')({
										title : labels['MOD.COLUMNS.DELETE']
									});
								}
							} ],
							options : {
								width : 260,
								height : 400
							},
							buttons : {
								add : 'new'
							},
							events : {
								cellClick : function($scope, mod, column) {
									if (column === 1) {
										$scope.remove(mod);
									} else {
										$scope.edit(mod.$id());
									}
								}
							}
						};
					});
				};

				setModGrid();
				$scope.$onRootScope('$translateChangeSuccess', function() {
					setModGrid();
				});

			} ]);

	module.controller('ModEditController', [ '$scope', '$state', '$stateParams', 'ModService', function ModEditController($scope, $state, $stateParams, ModService) {
		$scope.create = $stateParams.modId === 'new';
		if ($scope.create) {
			$scope.mod = ModService.getNew();
		} else {
			$scope.mod = angular.copy(ModService.getById($scope.mods, $stateParams.modId));
		}

		$scope.onCancel = function() {
			$state.go('^');
		};

		$scope.onSave = function() {
			if ($scope.create) {
				$scope.mods.push($scope.mod);
			} else {
				var item = ModService.getById($scope.mods, $scope.mod.$id());
				angular.extend(item, $scope.mod);
			}
			$state.go('^');
		};

		$scope.onSaveError = function() {
			$state.go('^');
		};

		$scope.onRemove = function() {
			var item = ModService.getById($scope.mods, $scope.mod.$id());
			$scope.mods.splice($scope.mods.indexOf(item), 1);
			$state.go('^');
		};

		$scope.onRemoveError = function() {
			$state.go('^');
		};
	} ]);

})();
