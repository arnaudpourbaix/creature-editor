/* global _ */
(function(_) {
	'use strict';

	var module = angular.module('creatureEditor.mod.services', [ 'ngResource' ]);

	module.factory('modSettings', [ 'appSettings', function modSettings(appSettings) {
		return {
			url : appSettings.restBaseUrl + 'mod/'
		};
	} ]);
	
	module.factory('Mod', [ '$resource', 'modSettings', function ModFactory($resource, modSettings) {
		var res = $resource(modSettings.url + ':id', {}, {
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
				url : modSettings.url + 'name/:name',
				method : 'GET'
			}
		});

		res.prototype.$id = function() {
			return this.id;
		};

		return res;
	} ]);

	module.service('ModService', [ 'Mod', function ModService(Mod) {
		var service = {
			'getNew' : function() {
				var mod = new Mod({
					id : null,
					name : null
				});
				return mod;
			},
			'getById' : function(mods, id) {
				return _.find(mods, function(mod) { /* jshint -W116 */
					return mod.id == id;
				});
			}
		};

		return service;
	} ]);
	
})(_);
