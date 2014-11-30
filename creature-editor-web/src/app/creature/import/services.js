angular.module('editor.creature.import.services', [])

.service('CreatureImportService', function($http, $interval, $q, creatureSettings, Mod, apxPanel) {
	var service = {};
	
	service.getPanel = function() {
		var panel = apxPanel.open({
			templateUrl : 'creature/import/import-panel.tpl.html',
			controller : 'CreatureImportController',
			resolve : {
				mods : Mod.query().$promise
			}
		});
		return panel;
	};

	service.import = function(options) {
		return $http({
			method : 'POST',
			url : creatureSettings.url + 'import',
			data : options
		}).then(function(response) {
			var creatureCount = parseInt(response.data, 10);
			var stop = function() {
				$interval.cancel(service.interval);
				service.container.running = false;
				service.container.progressValue = 0;
			};
			var cancel = function() {
				$http({
					method : 'GET',
					url : creatureSettings.url + 'import/cancel'
				}).then(function(response) {
					service.endImport();
					//$console.logMessage.push('CREATURE.IMPORT.CANCEL', 'warning');
					console.log('cancel');
				});
			};
			var gatherImportedCreatures = function() {
				$http({
					method : 'GET',
					url : creatureSettings.url + 'import/gather'
				}).then(function(response) {
					angular.forEach(response.data, function(value, index) {
						service.container.creatures.push(value);
					});
					service.container.progressValue = parseInt(100 / service.container.creatureCount * service.container.creatures.length, 10);
					if (service.container.creatures.length === service.container.creatureCount) {
//								$console.logMessage.push('CREATURE.IMPORT.SUCCESS', 'success', {
//									name : service.container.mod.name
//								});
						console.log('success');
						service.container.endImport();
					}
				}, function(response) {
					service.container.endImport();
					console.log('error');
				});
			};
			//var interval = $interval(gatherImportedCreatures, 2000);
			var result = {
				cancel: function() {
					cancel();
				},	
				creatureCount: creatureCount,
				creatures: []
			};
			return result;
		});
	};

	return service;
})
	
;
