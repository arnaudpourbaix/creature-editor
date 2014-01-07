var mod = angular.module('creatureEditor.mod.services', [ 'ngResource' ]);

mod.factory('Mod', function($resource) {
	'use strict';
	return $resource('rest/mod/:id', {}, {
		'save' : {
			method : 'PUT'
		}
	});
});

mod.factory('modService', [ '$http', function($http) {
	'use strict';
	var serviceBase = 'rest/mod/', modFactory = {};

	modFactory.checkUniqueValue = function(id, name) {
		var idParam = id == null ? -1 : id;
		return $http.get(serviceBase + 'checkUnique/' + name + '/' + idParam).then(function(results) {
			return results.data === 'true';
		});
	};

	return modFactory;
} ]);
