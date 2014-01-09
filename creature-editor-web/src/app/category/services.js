(function() {
	'use strict';

	var category = angular.module('creatureEditor.category.services', [ 'ngResource' ]);

	category.factory('Category', function($resource) {
		return $resource('rest/category/:id', {}, {
			'save' : {
				method : 'PUT'
			}
		});
	});

})();
