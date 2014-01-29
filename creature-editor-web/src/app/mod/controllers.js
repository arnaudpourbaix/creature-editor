(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.controllers', []);

	module.controller('ModListController', function ModListController($scope, mods, crudListMethods, alertMessageService) {

		angular.extend($scope, crudListMethods('/mods'));

		$scope.mods = mods;

		$scope.testAnim = function() {
			alertMessageService.push('crud.mod.save.success', 'warning', {
				name : 'Creatures'
			});
		};

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
				alertMessageService.push('crud.mod.remove.success', 'success', {
					name : mod.name
				});
			});
		};

	});

	module.controller('ModEditController', function ModEditController($scope, $modalInstance, mod, alertMessageService) {

		$scope.mod = mod;

		$scope.onSave = function(mod) {
			alertMessageService.push('crud.mod.save.success', 'success', {
				name : mod.name
			});
			$modalInstance.close();
		};

		$scope.onError = function() {
			alertMessageService.push('crud.mod.save.error', 'danger');
		};

		$scope.onRemove = function(mod) {
			alertMessageService.push('crud.mod.remove.success', 'success', {
				name : mod.name
			});
			$modalInstance.close();
		};
	});

})();
