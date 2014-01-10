(function() {
	'use strict';

	var mod = angular.module('creatureEditor.mod.services', [ 'ngResource' ]);

	mod.factory('Mod', function($resource) {
		var baseUrl = 'rest/mod/';

		return $resource(baseUrl + ':id', {}, {
			'save' : {
				method : 'PUT'
			},
			'getByName' : {
				url : baseUrl + 'name/:name',
				method : 'GET'
			},
			'id' : function() {
				return this.id;
			}
		});
	});

})();
