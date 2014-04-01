/* global _ */
(function(_) {
	'use strict';

	var module = angular.module('creatureEditor.spell.services', [ 'ngResource' ]);

	module.factory('Spell', [ '$resource', 'appSettings', function SpellFactory($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'spell/';

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
			},
			'getFlags' : {
				url : baseUrl + 'flags',
				method : 'GET',
				isArray : true
			}
		});

		res.prototype.$id = function() {
			return this.id;
		};

		res.prototype.baseUrl = function() {
			return baseUrl;
		};
		
		return res;
	} ]);

	module.service('SpellService', [ 'Spell', 'SpellImportService', '$http',
			function SpellService(Spell, SpellImportService, $http) {
				var service = {};

				service.getNew = function() {
					var spell = new Spell({
						id : null,
						name : null
					});
					return spell;
				};

				service.getById = function(spells, id) {
					return _.find(spells, function(spell) { /* jshint -W116 */
						return spell.id == id;
					});
				};

				service.getSpells = function(modId) {
					if (SpellImportService.running && modId === SpellImportService.modId) {
						return SpellImportService.spells;
					} else {
						return Spell.query({
							modId : modId
						}).$promise;
					}
				};

				service.getFlags = function() {
					return $http({
						method : 'GET',
						url : Spell.baseUrl() + 'flags'
					}).then(function(result) {
						return result.data;
					});
				};

				service.getExclusionFlags = function() {
					return $http({
						method : 'GET',
						url : Spell.baseUrl() + 'exclusionFlags'
					}).then(function(result) {
						return result.data;
					});
				};

				return service;
			} ]);

	module.service('SpellImportService', [ '$http', '$interval', '$alertMessage', 'Spell', function SpellImportService($http, $interval, $alertMessage, Spell) {
		var service = {
			running : false,
			spells : [],
			mod : null,
			progressValue : 0,
			interval : null,

			startImport : function(mod) {
				$http({
					method : 'GET',
					url : Spell.baseUrl() + 'import',
					params : {
						modId : mod.id
					}
				}).then(function(response) {
					service.mod = mod;
					service.spells = [];
					service.spellCount = parseInt(response.data, 10);
					if (service.spellCount === -1) {
						$alertMessage.push('SPELL.ERRORS.IMPORT_ALREADY_RUNNING', 'danger');
						return;
					}
					service.running = true;
					service.interval = $interval(service.gatherImportedSpells, 2000);
				}, function(response) {
					$alertMessage.push('SPELL.ERRORS.' + response.data.code, 'danger');
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
					url : Spell.baseUrl() + 'import/gather'
				}).then(function(response) {
					angular.forEach(response.data, function(value, index) {
						service.spells.push(value);
					});
					service.progressValue = parseInt(100 / service.spellCount * service.spells.length, 10);
					if (service.spells.length === service.spellCount) {
						$alertMessage.push('SPELL.IMPORT.SUCCESS', 'success', {
							name : service.mod.name
						});
						service.endImport();
					}
				}, function(response) {
					service.endImport();
					$alertMessage.push('SPELL.ERRORS.' + response.data.code, 'danger', {
						spell : response.data.param
					});
				});
			},

			cancelImport : function() {
				if (!service.running) {
					return;
				}
				$http({
					method : 'GET',
					url : Spell.baseUrl() + 'import/cancel'
				}).then(function(response) {
					service.endImport();
					$alertMessage.push('SPELL.IMPORT.CANCEL', 'warning');
				});
			}
		};

		return service;
	} ]);

})(_);
