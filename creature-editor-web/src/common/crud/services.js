(function() {
	'use strict';

	var crud = angular.module('crud.services', []);

	crud.factory('crudListMethods', [ '$location', function($location) {
		return function(pathPrefix) {
			var mixin = {
				'new' : function() {
					$location.path(pathPrefix + '/new');
				},
				'edit' : function(itemId) {
					$location.path(pathPrefix + '/' + itemId);
				},
				'removeFromList' : function(array, property, value) {
					angular.forEach(array, function(item, index) {
						if (item[property] === value) {
							array.splice(index, 1);
						}
					});
				}
			};
			return mixin;
		};
	} ]);

})();