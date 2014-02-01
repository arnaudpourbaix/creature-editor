(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.controllers', [ 'alertMessage', 'crud', 'ui.bootstrap' ]);

	module.controller('ModListController', [ '$scope', '$translate', 'mods', 'crudListMethods', 'alertMessageService',
			function ModListController($scope, $translate, mods, crudListMethods, alertMessageService) {

				angular.extend($scope, crudListMethods('/mods'));

				$scope.mods = mods;

				$scope.modGrid = {
					data : 'mods',
					columns : [ {
						text : 'Name',
						dataField : 'name',
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

				$scope.remove = function(mod) {
					mod.$delete().then(function(response) {
						$scope.removeFromList($scope.mods, 'id', mod.id);
						alertMessageService.push('CRUD.REMOVE_SUCCESS', 'success', {
							entity : $translate('MOD.LABEL'),
							name : mod.name
						});
					}, function() {
						alertMessageService.push('CRUD.REMOVE_ERROR', 'danger', {
							name : mod.name
						});
					});
				};

			} ]);

	module.controller('ModEditController', [ '$scope', '$translate', '$modalInstance', 'mod', 'alertMessageService',
			function ModEditController($scope, $translate, $modalInstance, mod, alertMessageService) {

				$scope.mod = mod;

				$scope.onSave = function(mod) {
					alertMessageService.push('CRUD.SAVE_SUCCESS', 'success', {
						entity : $translate('MOD.LABEL'),
						name : mod.name
					});
					$modalInstance.close();
				};

				$scope.onSaveError = function(mod) {
					alertMessageService.push('CRUD.SAVE_ERROR', 'danger', {
						entity : $translate('MOD.LABEL'),
						name : mod.name
					});
				};

				$scope.onRemove = function(mod) {
					alertMessageService.push('CRUD.REMOVE_SUCCESS', 'success', {
						entity : $translate('MOD.LABEL'),
						name : mod.name
					});
					$modalInstance.close();
				};

				$scope.onRemoveError = function(mod) {
					alertMessageService.push('CRUD.REMOVE_ERROR', 'danger', {
						entity : $translate('MOD.LABEL'),
						name : mod.name
					});
				};

			} ]);

})();
