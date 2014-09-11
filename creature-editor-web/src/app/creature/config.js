(function() {
	'use strict';

	angular.module('editor.creature.config', [])

	.config(function($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('app/creature');
	})

	.config(function($stateProvider) {
		$stateProvider.state('creature', {
			url : '/creature',
			controller : 'CreatureListController',
			templateUrl : 'creature/creature-list.tpl.html',
			resolve : {
				creatures : [ 'Creature', function(Creature) {
					return Creature.query().$promise;
				} ]
			}
		});

	})
	
	;

})();
