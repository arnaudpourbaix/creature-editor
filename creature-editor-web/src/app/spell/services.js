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

	spell.service('SpellService', function(Spell, SpellImportService) {
		var service = {
			getSpells : function(modId) {
				if (SpellImportService.running && modId === SpellImportService.modId) {
					return SpellImportService.spells;
				} else {
					return Spell.query({
						modId : modId
					});
				}
			}
		};

		return service;
	});

	spell.service('SpellImportService', function(Spell, $http, $interval, $rootScope, i18nNotifications) {
		var service = {
			running : false,
			spells : [],
			mod : null,
			progressValue : 0,
			interval : null,

			startImport : function(mod) {
				$http({
					method : 'GET',
					url : 'rest/spell/import',
					params : {
						modId : mod.id
					}
				}).then(function(response) {
					service.mod = mod;
					service.spells = [];
					service.spellCount = parseInt(response.data, 10);
					if (service.spellCount === -1) {
						i18nNotifications.pushForCurrentRoute('spell.import.error.running', 'danger');
						return;
					}
					service.running = true;
					service.interval = $interval(service.gatherImportedSpells, 2000);
				});
			},

			endImport : function() {
				$interval.cancel(service.interval);
				service.running = false;
				service.progressValue = 0;
			},

			gatherImportedSpells : function() {
				$http({
					method : 'GET',
					url : 'rest/spell/import/gather'
				}).then(function(response) {
					angular.forEach(response.data, function(value, index) {
						service.spells.push(value);
					});
					service.progressValue = parseInt(100 / service.spellCount * service.spells.length, 10);
					if (service.spells.length === service.spellCount) {
						i18nNotifications.pushForCurrentRoute('spell.import.success', 'success', {
							name : service.mod.name
						});
						service.endImport();
					}
				}, function() {
					service.endImport();
					i18nNotifications.pushForCurrentRoute('spell.import.error', 'danger');
				});
			},

			cancelImport : function() {
				if (!service.running) {
					return;
				}
				$http({
					method : 'GET',
					url : 'rest/spell/import/cancel'
				}).then(function(response) {
					service.endImport();
					i18nNotifications.pushForCurrentRoute('spell.import.cancel', 'warning');
				});
			}
		};

		return service;
	});

})();
