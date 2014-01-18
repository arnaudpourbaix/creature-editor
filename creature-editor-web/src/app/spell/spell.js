(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell', [ 'creatureEditor.spell.services', 'creatureEditor.spell.directives', 'creatureEditor.spell.controllers', 'ui.router',
			'ngRoute', 'notification.i18n' ]);

	spell.constant('I18N.MESSAGES', {
		'errors.route.changeError' : 'Route change error',
		'spell.import.running' : "Spell import is already running.",
		'crud.spell.save.success' : "Spell '{{name}}' was saved successfully.",
		'crud.spell.remove.success' : "Spell '{{name}}' was removed successfully.",
		'crud.spell.remove.error' : "Error when removing spell '{{name}}'.",
		'crud.spell.save.error' : "Error when saving a spell..."
	});

	spell.config(function config($stateProvider) {

		$stateProvider.state('spells', {
			url : '/spells',
			resolve : {
				spells : [ 'Spell', function(Spell) {
					return Spell.query().$promise;
				} ]
			},
			controller : 'SpellListController',
			templateUrl : 'spell/spell-list.tpl.html'
		});

		$stateProvider.state('spells.import', {
			url : '/import',
			onEnter : function($state, $stateParams, $modal, $timeout) {
				var modal = $modal.open({
					templateUrl : "spell/spell-import.tpl.html",
					controller : 'SpellImportController',
					backdrop : false
				});
				modal.result.then(function(result) {
					$state.go('^', {}, {
						reload : true
					});
				}, function() {
					$state.go('^');
				});
			}
		});

		$stateProvider.state('spells.edit', {
			url : 'edit/:spellId',
			onEnter : function($state, $stateParams, $modal, $timeout, Spell) {
				var modal = $modal.open({
					templateUrl : "spell/spell-edit.tpl.html",
					controller : 'SpellEditController',
					backdrop : false,
					resolve : {
						spell : function(Spell) {
							if ($stateParams.spellId !== 'new') {
								return Spell.get({
									id : $stateParams.spellId
								}).$promise;
							} else {
								return new Spell({
									id : null,
									name : ''
								});
							}
						}
					}
				});
				modal.result.then(function(result) {
					$state.go('^', {}, {
						reload : true
					});
				}, function() {
					$state.go('^');
				});
			}
		});
	});

})();
