(function() {
	'use strict';

	var mod = angular.module('creatureEditor.mod.controllers', [ 'ui.router', 'ngRoute', 'ngResource', 'crud.services', 'ui.select2' ]);

	mod.controller('ModListController', function ModListController($scope, $state, mods, crudListMethods, i18nNotifications) {

		angular.extend($scope, crudListMethods('/mods'));

		$scope.mods = mods;

		$scope.testAnim = function() {
			i18nNotifications.pushForCurrentRoute('crud.mod.save.success', 'success', {
				name : mod.name
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
				i18nNotifications.pushForCurrentRoute('crud.mod.remove.success', 'success', {
					name : mod.name
				});
			});
		};

	});

	mod.controller('ModEditController', function ModEditController($scope, $modalInstance, mod, i18nNotifications) {

		$scope.mod = mod;

		$scope.onSave = function(mod) {
			i18nNotifications.pushForCurrentRoute('crud.mod.save.success', 'success', {
				name : mod.name
			});
			$modalInstance.close();
		};

		$scope.onError = function() {
			i18nNotifications.pushForCurrentRoute('crud.mod.save.error', 'danger');
		};

		$scope.onRemove = function(mod) {
			i18nNotifications.pushForCurrentRoute('crud.mod.remove.success', 'success', {
				name : mod.name
			});
			$modalInstance.close();
		};
	});

})();
