angular.module('editor.creature.list', [ 'editor.creature.list.config', 'editor.creature.list.controllers' ]);

angular.module('editor.creature.list.config', [])


.config(function($stateProvider) {
	$stateProvider.state('creatureList', {
		url : '/creature',
		controller : 'CreatureListController',
		templateUrl : 'creature/list/creature-list.tpl.html',
		resolve : {
			creatures : function(Creature) {
				return Creature.query().$promise;
			},
			mods: function(Mod) {
				return Mod.query().$promise;
			}
		}
	});
})
	
;
