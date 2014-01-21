(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell', [ 'creatureEditor.spell.services', 'creatureEditor.spell.directives', 'creatureEditor.spell.controllers', 'creatureEditor.mod.services', 'ui.router',
			'ngRoute', 'notification.i18n' ]);

	spell.constant('I18N.MESSAGES', {
		'errors.route.changeError' : 'Route change error',
		'spell.import.success' : "Spell import for mod {{name}} is done.",
		'spell.import.error' : "Spell import has encountered an internal error.",
		'spell.import.cancel' : "Spell import has been cancelled.",
		'crud.spell.save.success' : "Spell '{{name}}' was saved successfully.",
		'crud.spell.remove.success' : "Spell '{{name}}' was removed successfully.",
		'crud.spell.remove.error' : "Error when removing spell '{{name}}'.",
		'crud.spell.save.error' : "Error when saving a spell..."
	});

	spell.config(function config($stateProvider) {

		$stateProvider.state('spells', {
         abstract: true,
			url : '/spells',
			controller : 'SpellController',
			templateUrl : 'spell/spell.tpl.html'
		});

		$stateProvider.state('spells.list', {
			url : '/list',
			controller : 'SpellListController',
			template : ''
			// ,templateUrl : 'spell/spell-list.tpl.html'
		});
		 
		$stateProvider.state('spells.import', {
			url : '/import/:modId',
			onEnter : function($state, $stateParams, $modal, $timeout) {
				var modal = $modal.open({
					templateUrl : "spell/spell-import.tpl.html",
					controller : 'SpellImportController',
					resolve : {
						mod : [ 'Mod', function(Mod) {
							return Mod.get({
								id : $stateParams.modId
							}).$promise;
						} ]
					}
				});
				modal.result.finally(function(result) {
					$state.go('spells.list');
				});
			}
		});

		$stateProvider.state('spells.edit', {
			url : '/:id',
			onEnter : function($state, $stateParams, $modal, $timeout, Spell) {
				var modal = $modal.open({
					templateUrl : "spell/spell-edit.tpl.html",
					controller : 'SpellEditController',
					backdrop : false,
					windowClass : 'spell-modal',
					resolve : {
						spell : function(Spell) {
							return Spell.get({
								id : $stateParams.id
							}).$promise;
						}
					}
				});
				modal.result.then(function(result) {
// $state.go('^', {}, {
// reload : true
// });
					$state.go('spells.list');
				}, function() {
					// $state.go('^');
					$state.go('spells.list');
				});
			}
		});
	});

})();
