angular.module('editor.creature.edit', [ 'editor.creature.edit.config', 'editor.creature.edit.controllers', 'editor.creature.edit.services' ]);

angular.module('editor.creature.edit.config', [])

.config(function($stateProvider) {
	$stateProvider.state('creatureEdit', {
		url : '/creature/:id',
		controller : 'CreatureEditController',
		templateUrl : "creature/edit/creature-edit.tpl.html",
		resolve : {
			creature : function($stateParams, CreatureService) {
				return CreatureService.get($stateParams.id);
			},
			mods: function(Mod) {
				return Mod.query().$promise;
			}
		}
	});
})

;
