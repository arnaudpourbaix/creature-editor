angular.module('editor.creature.import.services', [])

.service('CreatureImportService', function($http, $interval, creatureSettings) {
	var service = {};
	
	service.running = false;
	service.creatures = [];
	service.options = null;
	service.progressValue = 0;
	service.interval = null;
	
	service.getPanel = function() {
		var panel = apxPanel.open({
			templateUrl : 'creature/import/import-panel.tpl.html',
			controller : 'CreatureImportController',
			resolve : {
				mods : function() {
					return $scope.mods.$promise;
				}
			}
		});
		return panel;
	}

	service.startImport = function(options) {
		$http({
			method : 'POST',
			url : creatureSettings.url + 'import',
			data : options
		}).then(function(response) {
			service.options = options;
			service.creatures = [];
			service.creatureCount = parseInt(response.data, 10);
			if (service.creatureCount === -1) {
				//$console.logMessage.push('CREATURE.ERRORS.IMPORT_ALREADY_RUNNING', 'danger');
				console.log('CREATURE.ERRORS.IMPORT_ALREADY_RUNNING');
				return;
			}
			service.running = true;
			//service.interval = $interval(service.gatherImportedCreatures, 2000);
		}, function(response) {
			//$console.logMessage.push('CREATURE.ERRORS.' + response.data.code, 'danger');
			console.log(response.data.code);
		});
	};

	service.endImport = function() {
		$interval.cancel(service.interval);
		service.running = false;
		service.progressValue = 0;
	};

	service.gatherImportedCreatures = function() {
		$http({
			method : 'GET',
			url : creatureSettings.url + 'import/gather'
		}).then(function(response) {
			angular.forEach(response.data, function(value, index) {
				service.creatures.push(value);
			});
			service.progressValue = parseInt(100 / service.creatureCount * service.creatures.length, 10);
			if (service.creatures.length === service.creatureCount) {
//						$console.logMessage.push('CREATURE.IMPORT.SUCCESS', 'success', {
//							name : service.mod.name
//						});
				console.log('success');
				service.endImport();
			}
		}, function(response) {
			service.endImport();
//					$console.logMessage.push('CREATURE.ERRORS.' + response.data.code, 'danger', {
//						creature : response.data.param
//					});
			console.log('error');
		});
	};

	service.cancelImport = function() {
		if (!service.running) {
			return;
		}
		$http({
			method : 'GET',
			url : creatureSettings.url + 'import/cancel'
		}).then(function(response) {
			service.endImport();
			//$console.logMessage.push('CREATURE.IMPORT.CANCEL', 'warning');
			console.log('cancel');
		});
	};

	return service;
})
	
;
