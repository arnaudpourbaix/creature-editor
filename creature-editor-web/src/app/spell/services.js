(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell.services', [ 'ngResource' ]);

	spell.factory('Spell', function($resource) {
		var baseUrl = 'rest/spell/';

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
			},
			'getByName' : {
				url : baseUrl + 'name/:name',
				method : 'GET'
			}
		});
		res.prototype.$id = function() {
			return this.id;
		};

		return res;
	});

	spell.service('SpellService', function(Spell, $http, $interval) {
		var methods = {};

		methods.startImportSpells = function(modId) {
			$http({
				method : 'GET',
				url : 'rest/spell/import',
				params : {
					modId : modId
				}
			}).then(function(response) {
				var result = response.data === 'true';
				if (!result) {
					console.log('already running!');
					return;
				}
				$interval(function() {
					$http({
						method : 'GET',
						url : 'rest/spell/import/progress'
					}).then(function(response) {
						console.log("progress", response.data);
					}, function(data) {
						console.log("Polling - the following error occured: ", data);
					});
				}, 500);
			});
		};

		methods.importSpellsUpdate = function() {
			// if (request) {
			// request.abort(); // abort any pending request
			// }
			// fire off the request to MatchUpdateController
			// var request = $http({
			$http({
				method : 'GET',
				url : 'rest/spell/deferred'
			}).then(function(message) {
				console.log("Received a message", message.messageText);
				// var update = getUpdate(message);
				// $(update).insertAfter('#first_row');
			}, function(data) {
				// log the error to the console
				console.log("Polling - the following error occured: ", data);
			});

			// callback handler that will be called regardless if the request failed or succeeded
			// request.always(function() {
			// allow = true;
			// });
		};

		return methods;
	});

})();
