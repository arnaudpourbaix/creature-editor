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
				creatures : function(Creature) {
					return Creature.query().$promise;
				},
				mods: function(Mod) {
					return Mod.query().$promise;
				}
			}
		});

		$stateProvider.state('creature.edit', {
			url : '/:id',
			views : {
				'creature-edit' : {
					controller : 'CreatureEditController',
					templateUrl : "creature/creature-edit.tpl.html",
				}
			},
			resolve : {
				creature : function($stateParams, CreatureService) {
					return CreatureService.get($stateParams.id);
				}
			}
		});
		
	})
	
	;

})();
