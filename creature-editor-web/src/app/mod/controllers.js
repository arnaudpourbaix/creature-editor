(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.controllers', [ 'alert-message', 'crud', 'ui.bootstrap' ]);

	module.controller('ModListController', [ '$scope', '$translate', 'mods', 'crudListMethods', '$alertMessage', '$q', '$interpolate',
			function ModListController($scope, $translate, mods, crudListMethods, $alertMessage, $q, $interpolate) {
				angular.extend($scope, crudListMethods('/mods'));
				
				$scope.mods = mods;

				var setModGrid = function() {
					$q.all([ $translate('MOD.NAME_FIELD'), $translate('MOD.ACTION_COLUMN'), $translate('MOD.ACTION_DELETE') ]).then(function(labels) {
						$scope.modGrid = {
								data : 'mods',
								columns : [ {
									text : labels[0],
									dataField : 'name',
									type : 'string',
									align : 'center',
									width : 200
								}, {
									text : labels[1],
									type : 'string',
									width : 60,
									align : 'center',
									cellsalign : 'center',
									filterable : false,
									sortable : false,
									cellsrenderer : function(row, columnfield, value, defaulthtml, columnproperties) {
										return $interpolate('<div class="pointer text-center"><span class="glyphicon glyphicon-trash" title="{{title}}" /></div>')({ title: labels[2] });
									}
								} ],
								options : {
									width : 260,
									height : 400
								},
								events : {
									cellClick : function($scope, mod, column) {
										if (column === 1) {
											$scope.remove(mod);
										} else {
											$scope.edit(mod.id);
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
				
				$scope.remove = function(mod) {
					mod.$delete().then(function(response) {
						$scope.removeFromList($scope.mods, 'id', mod.id);
						$translate('MOD.LABEL').then(function (label) {
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

			} ]);

	module.controller('ModEditController', [ '$scope', '$modalInstance', 'mod', function ModEditController($scope, $modalInstance, mod) {
		$scope.mod = mod;

		$scope.onSave = function(mod) {
			$modalInstance.close({
				mod : mod
			});
		};

		$scope.onSaveError = function(mod) {
			$modalInstance.close({
				mod : mod
			});
		};

		$scope.onRemove = function(mod) {
			$modalInstance.close({
				mod : mod
			});
		};

		$scope.onRemoveError = function(mod) {
			$modalInstance.close({
				mod : mod
			});
		};
	} ]);

})();
