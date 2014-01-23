(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell', [ 'creatureEditor.mod.services', 'creatureEditor.spell.services', 'creatureEditor.spell.directives', 'creatureEditor.spell.controllers', 'ui.router',
			'ngRoute', 'notification.i18n' ]);

	spell.config(function config($stateProvider) {

		$stateProvider.state('spells', {
			abstract: true,
			url : '/spells',
			controller: 'SpellController',
			templateUrl : 'spell/spell.tpl.html',
			resolve : {
				mods : [ 'Mod', function(Mod) {
					return Mod.query().$promise;
				} ]
			}
		});

		$stateProvider.state('spells.modSelect', {
			url : '',
			controller : 'SpellSelectModController'
		});

		$stateProvider.state('spells.list', {
			url : '/:modId',
			controller : 'SpellListController',
			templateUrl : 'spell/spell-list.tpl.html'
		});
		
		$stateProvider.state('spells.list.edit', {
			url : '/:id',
			onEnter : function($state, $stateParams, $modal, $timeout, Spell) {
				var modal = $modal.open({
					templateUrl : "spell/spell-edit.tpl.html",
					controller : 'SpellEditController',
					backdrop : false,
					resolve : {
						spell : function(Spell) {
							if ($stateParams.spellId !== 'new') {
								return Spell.get({
									id : $stateParams.id
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

		$stateProvider.state('spells.list.import', {
			onEnter : function($state, $stateParams, $modal, $timeout) {
				var modal = $modal.open({
					templateUrl : "spell/spell-import.tpl.html",
					controller : 'SpellImportController',
					backdrop : false,
					resolve : {
						mod : [ 'Mod', function(Mod) {
							return Mod.get({
								id : $stateParams.modId
							}).$promise;
						} ]
					}
				});
				modal.result.finally(function(result) {
					$state.go('^');
				});
			}
		});
	});

})();
