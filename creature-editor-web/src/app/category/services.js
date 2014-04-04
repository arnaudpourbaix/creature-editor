/* global _ */
(function(_) {
	'use strict';

	var module = angular.module('creatureEditor.category.services', [ 'ngResource' ]);

	module.factory('categorySettings', [ 'appSettings', function categorySettings(appSettings) {
		return {
			url : appSettings.restBaseUrl + 'category/'
		};
	} ]);
	
	module.factory('Category', [ '$resource', 'categorySettings', function CategoryFactory($resource, categorySettings) {
		var res = $resource(categorySettings.url + ':id', {}, {
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
				url : categorySettings.url + 'name/:name',
				method : 'GET'
			}
		});

		res.prototype.$id = function() {
			return this.id;
		};

		return res;
	} ]);

	module.service('CategoryService', [ 'Category', function CategoryService(Category) {
		var service = {
			getNew : function(parent) {
				var category = new Category({
					id : null,
					name : null,
					parent : parent ? parent : null
				});
				return category;
			},
			getById : function(categories, id) {
				return _.find(categories, function(category) { /* jshint -W116 */
					return category.id == id;
				});
			}
		};

		return service;
	} ]);

})(_);
