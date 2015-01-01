angular.module('editor.creature.edit.controllers', [])

.controller('CreatureEditController', function($scope, $translate, $state, mods, creature) {
	if (creature.mod) {
		creature.mod = _.find(mods, function(mod) {
			return mod.id === creature.mod.id;
		});
	}
	$scope.creature = creature;
	console.log(creature);
	
	$scope.mods = mods;
	
	$scope.onCancel = function() {
		$state.go('creatureList');
	};
	
})

;

