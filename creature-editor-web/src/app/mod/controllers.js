(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.controllers', [ 'alertMessage', 'crud', 'ui.bootstrap' ]);

	module.controller('ModListController', function ModListController($scope, mods, crudListMethods, alertMessageService) {

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
				alertMessageService.push('mod.remove.success', 'success', {
					name : mod.name
				});
			}, function() {
				alertMessageService.push('mod.remove.error', 'danger', {
					name : mod.name
				});
			});
		};

	});

	module.controller('ModEditController', function ModEditController($scope, $modalInstance, mod, alertMessageService) {

		$scope.mod = mod;

		$scope.onSave = function(mod) {
			alertMessageService.push('mod.save.success', 'success', {
				name : mod.name
			});
			$modalInstance.close();
		};

		$scope.onSaveError = function() {
			alertMessageService.push('mod.save.error', 'danger');
		};

		$scope.onRemove = function(mod) {
			alertMessageService.push('mod.remove.success', 'success', {
				name : mod.name
			});
			$modalInstance.close();
		};

		$scope.onRemoveError = function() {
			alertMessageService.push('mod.remove.error', 'danger');
		};

	});

})();
