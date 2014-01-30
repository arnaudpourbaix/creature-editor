(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.services', [ 'ngResource', 'restangular' ]);

	module.factory('Mod', function(Restangular) {
		var resource = Restangular.all('mod'), factory = {};

		factory.getList = function() {
			return resource.getList();
		};

		return factory;
	});

	module.factory('OldMod', function($resource) {
		var baseUrl = 'rest/mod/';

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
