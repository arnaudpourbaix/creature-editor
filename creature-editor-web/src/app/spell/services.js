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

})();
