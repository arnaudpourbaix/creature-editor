/* global _ */
(function(_) {
	'use strict';

	var module = angular.module('creatureEditor.mod.services', [ 'ngResource' ]);

	module.factory('Mod', [ '$resource', 'appSettings', function ModFactory($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'mod/';

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
	} ]);

	module.service('ModService', [ 'Mod', 'appSettings', function ModService(Mod, appSettings) {
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
