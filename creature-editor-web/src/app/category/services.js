(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.services', [ 'ngResource' ]);

	module.factory('Category', [ '$resource', 'appSettings', function CategoryFactory($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'category/';
		
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
			}
		});
		
		res.prototype.$id = function() {
			return this.id;
		};
		
		return res;
	} ]);

	module.service('CategoryService', [ 'Category', 'appSettings', function CategoryService(Category, appSettings) {
		var service = {
			'new' : function(parent) {
				var category = new Category({ id: null, name: null, parent: parent ? parent : null });
				return category;
			}
		};

		return service;
	} ]);
	
})();
