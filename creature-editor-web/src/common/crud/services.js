angular.module('crud.services', [])

.factory('crudListMethods', function($location) { 'use strict';
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
})

;
