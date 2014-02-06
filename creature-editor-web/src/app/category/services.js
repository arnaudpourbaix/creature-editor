(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.services', [ 'ngResource' ]);

	module.factory('Category', [ '$resource', 'appSettings', function($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'category/';
		return $resource(baseUrl + ':id', {}, {
			'save' : {
				method : 'PUT'
			}
		});
	} ]);

})();
