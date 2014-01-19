(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell.services', [ 'ngResource' ]);

	spell.factory('Spell', function($resource) {
		var baseUrl = 'rest/spell/';

		var res = $resource(baseUrl + ':id', {}, {
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
				url : baseUrl + 'name/:name',
				method : 'GET'
			}
		});
		res.prototype.$id = function() {
			return this.id;
		};

		return res;
	});

	spell.service('SpellImportService', function(Spell, $http, $interval, $rootScope, i18nNotifications) {
		var service = {}, running = false, container;

		service.isRunning = function() {
			return running;
		};

		service.getModId = function() {
			return container.modId;
		};

		service.getSpells = function() {
			return container ? container.spells : [];
		};

		service.getProgressValue = function() {
			return running ? 100 / container.spellCount * container.spells : 0;
		};

		service.startImport = function(modId) {
			$http({
				method : 'GET',
				url : 'rest/spell/import',
				params : {
					modId : modId
				}
			}).then(function(response) {
				container = {
					spells : [],
					spellCount : parseInt(response.data, 10),
					modId : modId,
					interval : null
				};
				if (container.spellCount === -1) {
					i18nNotifications.pushForCurrentRoute('spell.import.error.running', 'danger');
					return;
				}
				running = true;
				container.interval = $interval(service.gatherImportedSpells, 2000);
			});
		};

		service.gatherImportedSpells = function() {
			$http({
				method : 'GET',
				url : 'rest/spell/import/gather'
			}).then(function(response) {
				container.spells = container.spells.concat(response.data);
				if (container.spells.length === container.spellCount) {
					$interval.cancel(container.interval);
				}
				console.log(container.spells);
				// $rootScope.$apply();
			}, function() {
				service.cancelImport();
			});
		};

		service.cancelImport = function() {
			if (!running) {
				return;
			}
			$http({
				method : 'GET',
				url : 'rest/spell/import/cancel'
			}).then(function(response) {
				running = false;
				$interval.cancel(container.interval);
				i18nNotifications.pushForCurrentRoute('spell.import.cancel', 'warning');
			});
		};

		return service;
	});

})();
