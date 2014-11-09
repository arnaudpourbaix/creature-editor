angular.module('editor.category.services', [])

.factory('categorySettings', function(appSettings) {
	return {
		url : appSettings.restBaseUrl + 'category/'
	};
})
	
.factory('Category', function($resource, categorySettings) {
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

.service('CategoryService', function($q, Category) {
	var service = {};
	
	service.get = function(parentId, id) {
		if (id !== 'new') {
			return Category.get({
				id: id
			}).$promise;
		}
		var category = new Category({
			id : null,
			name : null
		});
		if (parentId === 'root') {
			return $q.when(category);
		}
		return Category.get({
			id: parentId
		}).$promise.then(function(parent) {
			category.parent = parent;
			return category;
		});		
	};
		
	service.getById = function(categories, id) {
			return _.find(categories, function(category) { /* jshint -W116 */
				return category.id == id;
			});
	};

	return service;
})

;