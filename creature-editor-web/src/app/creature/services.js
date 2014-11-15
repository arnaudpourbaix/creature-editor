(function() {
	'use strict';

	angular.module('editor.creature.services', [])

	.factory('creatureSettings', function(appSettings) {
		return {
			url : appSettings.restBaseUrl + 'creature/'
		};
	})
	
	.factory('Creature', function($resource, creatureSettings) {
		var res = $resource(creatureSettings.url + ':id', {}, {
			'save' : {
				method : 'PUT'
			},
			'delete' : {
				method : 'DELETE',
				params : {
					id : '@id'
				}
			},
			'remove' : {
				method : 'DELETE',
				params : {
					id : '@id'
				}
			},
			'getByName' : {
				url : creatureSettings.url + 'name/:name',
				method : 'GET'
			}
		});

		res.prototype.$id = function() {
			return this.id;
		};

		return res;
	})

	.service('CreatureService', function(Creature) {
		var service = {};
		
		service.get = function(id) {
			var creature = Creature.get({ id: id }).$promise;
			return creature;
		};
		
		service.getById = function(creatures, id) {
			return _.find(creatures, function(creature) { /* jshint -W116 */
				return creature.id == id;
			});
		};

		return service;
	})
	
	.service('CreatureImportService', function($http, $interval, creatureSettings) {
		var service = {
			running : false,
			creatures : [],
			options : null,
			progressValue : 0,
			interval : null,

			startImport : function(options) {
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
			},

			endImport : function() {
				$interval.cancel(service.interval);
				service.running = false;
				service.progressValue = 0;
			},

			gatherImportedCreatures : function() {
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
			},

			cancelImport : function() {
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
			}
		};

		return service;
	})
	
	;

})();
