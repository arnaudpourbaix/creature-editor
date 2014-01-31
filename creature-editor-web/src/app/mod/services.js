(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.services', [ 'ngResource', 'restangular' ]);

	module.factory('Mod', function($resource, appSettings) {
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
	});

})();
