angular.module('editor.creature.services', [])

.factory('creatureSettings', function(appSettings) {
	return {
		url : appSettings.restBaseUrl + 'creature/'
	};
})
	
.factory('Creature', function($resource, creatureSettings) {
	var res = $resource(creatureSettings.url + ':id', {}, {
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
			url : creatureSettings.url + 'name/:name',
			method : 'GET'
		}
	});

	res.prototype.$id = function() {
		return this.id;
	};

	return res;
})

.service('CreatureService', function(Creature) {
	var service = {};
	
	service.get = function(id) {
		var creature = Creature.get({ id: id }).$promise;
		return creature;
	};
	
	service.getById = function(creatures, id) {
		return _.find(creatures, function(creature) { /* jshint -W116 */
			return creature.id == id;
		});
	};

	return service;
})
	
;
