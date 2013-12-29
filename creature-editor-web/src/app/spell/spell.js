var spell = angular.module('creatureEditor.spell', [ 'ui.router', 'ngRoute', 'ngResource' ]);

spell.config(function config($stateProvider) {
	'use strict';
	$stateProvider.state('spell', {
		url : '/spell',
		views : {
			"main" : {
				controller : 'SpellListController',
				templateUrl : 'spell/spell-list.tpl.html'
			}
		}
	});
});

spell.factory('Spell', function($resource) {
	'use strict';
	return $resource('spell/:id', {}, {
		'save' : {
			method : 'PUT'
		}
	});
});

spell.controller('SpellListController', function SpellListController($scope, $location, Spell) {
	'use strict';
	$scope.mods = Spell.query();

	$scope.gotoSpellNewPage = function() {
		$location.path("/spell/new");
	};
	$scope.deleteSpell = function(spell) {
		spell.$delete({
			'id' : spell.id
		}, function() {
			$location.path('/');
		});
	};
});
