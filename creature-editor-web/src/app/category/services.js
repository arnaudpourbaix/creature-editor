(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.services', [ 'ngResource' ]);

	module.factory('Category', function($resource) {
		return $resource('rest/category/:id', {}, {
			'save' : {
				method : 'PUT'
			}
		});
	});

})();
