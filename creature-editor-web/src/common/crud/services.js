(function() {
	'use strict';

	var module = angular.module('crud.services', []);

	module.factory('crudListMethods', [ '$location', function CrudListMethodsFactory($location) {
		return function(pathPrefix) {
			var mixin = {
				'new' : function() {
					$location.path(pathPrefix + '/new');
				},
				'edit' : function(itemId) {
					console.log('edit', pathPrefix + '/' + itemId);
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