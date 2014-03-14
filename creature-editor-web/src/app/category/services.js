(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.services', [ 'ngResource' ]);

	module.factory('Category', [ '$resource', 'appSettings', function CategoryFactory($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'category/';
		return $resource(baseUrl + ':id', {}, {
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
			}
		});
	} ]);

})();
