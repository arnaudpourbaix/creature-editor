(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell.controllers', [ 'ui.router', 'ngRoute', 'ngResource', 'crud.services' ]);

	spell.controller('SpellListController', function SpellListController($scope, $state, spells, crudListMethods, i18nNotifications) {

		angular.extend($scope, crudListMethods('/spells'));

		$scope.spells = spells;

		$scope.gridOptions = {
			data : 'spells',
			enableRowSelection : false,
			enableColumnResize : true,
			showFilter : true,
			columnDefs : [ {
				field : 'name',
				displayName : 'Name',
				cellClass : 'cellName',
				cellTemplate : '<div class="ngCellText" ng-class="col.colIndex()" ng-click="edit(row.entity.id)"><span>{{row.getProperty(col.field)}}</span></div>'
			}, {
				sortable : false,
				cellClass : 'actionColumn',
				width : '60px',
				cellTemplate : '<span class="glyphicon glyphicon-trash" title="Delete" ng-click="remove(row.entity)" />'
			} ]
		};

		$scope.$on('selectedMod', function(e, mod) {
			$scope.mod = mod;
			e.stopPropagation();
		});

		$scope.remove = function(spell) {
			spell.$delete().then(function(response) {
				$scope.removeFromList($scope.spells, 'id', spell.id);
				i18nNotifications.pushForCurrentRoute('crud.spell.remove.success', 'success', {
					name : spell.name
				});
			});
		};

	});

	spell.controller('SpellImportController', function SpellImportController($scope, $modalInstance, $http, $interval, i18nNotifications) {

		$scope.mod = null;

		$scope.$on('selectedMod', function(e, mod) {
			console.log('SpellImportController', 'selectedMod', mod);
			$scope.mod = mod;
			e.stopPropagation();
		});

		var stop;

		$scope.import = function() {
			$scope.progressValue = 0;
			$http({
				method : 'GET',
				url : 'rest/spell/deferred2',
				params : {
				// modId : $scope.mod.id
				},
			}).then(function(data) {
				console.log("Deferred data", data);
				// stop = $interval(function() {
				// $scope.getUpdate();
				// }, 2000);
			}, function(data, status, headers, config) {
				console.log('Deferred error', data);
			});
		};

		$scope.getUpdate = function() {
			// if (request) {
			// request.abort(); // abort any pending request
			// }
			// fire off the request to MatchUpdateController
			// var request = $http({
			$http({
				method : 'GET',
				url : 'rest/spell/deferred'
			}).then(function(message) {
				console.log("Received a message", message.messageText);
				// var update = getUpdate(message);
				// $(update).insertAfter('#first_row');
			}, function(data) {
				// log the error to the console
				console.log("Polling - the following error occured: ", data);
			});

			// callback handler that will be called regardless if the request failed or succeeded
			// request.always(function() {
			// allow = true;
			// });
		};

	});

	spell.controller('SpellEditController', function SpellEditController($scope, $modalInstance, spell, i18nNotifications) {

		$scope.spell = spell;

		$scope.onSave = function(spell) {
			i18nNotifications.pushForCurrentRoute('crud.spell.save.success', 'success', {
				name : spell.name
			});
			$modalInstance.close();
		};

		$scope.onError = function() {
			i18nNotifications.pushForCurrentRoute('crud.spell.save.error', 'danger');
		};

		$scope.onRemove = function(spell) {
			i18nNotifications.pushForCurrentRoute('crud.spell.remove.success', 'success', {
				name : spell.name
			});
			$modalInstance.close();
		};
	});

})();
