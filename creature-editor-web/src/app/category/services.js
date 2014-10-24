angular.module('editor.category.services', [])

.factory('categorySettings', function categorySettings(appSettings) {
	return {
		url : appSettings.restBaseUrl + 'category/'
	};
})
	
.factory('Category', function CategoryFactory($resource, categorySettings) {
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
})

.service('CategoryService', function CategoryService(Category) {
	var service = {};
	
	service.getNew = function(parent) {
			var category = new Category({
				id : null,
				name : null,
				parent : parent ? parent : null
			});
			return category;
	};
		
	service.getById = function(categories, id) {
			return _.find(categories, function(category) { /* jshint -W116 */
				return category.id == id;
			});
	};

	return service;
})

;