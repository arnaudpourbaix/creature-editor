(function() {
	'use strict';

	angular.module('editor.creature.controllers', [])

	.controller('CreatureListController', function($scope, creatures) {
				$scope.creatures = creatures;

	})
	
	;

})();
