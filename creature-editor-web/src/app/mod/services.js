angular.module('editor.mod.services', [])

.factory('modSettings', function(appSettings) {
	return {
		url : appSettings.restBaseUrl + 'mod/'
	};
})
	
.factory('Mod', function($resource, modSettings) {
	var res = $resource(modSettings.url + ':id', {}, {
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
			url : modSettings.url + 'name/:name',
			method : 'GET'
		}
	});

	res.prototype.$id = function() {
		return this.id;
	};

	return res;
})

.service('ModService', function(Mod) {
	var service = {};
	
	service.get = function(id) {
		var mod = new Mod({
			id : null,
			name : null
		});
		if (id !== 'new') {
			mod = Mod.get({
				id: id
			}).$promise;
		}
		return mod;
	};
	
	service.getById = function(mods, id) {
		return _.find(mods, function(mod) { /* jshint -W116 */
			return mod.id == id;
		});
	};

	return service;
})

;
