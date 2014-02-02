(function() {
	'use strict';

	var module = angular.module('creatureEditor.spell.config', [ 'creatureEditor.spell.services', 'pascalprecht.translate', 'ui.router', 'ui.bootstrap' ]);

	module.run([ '$translate', '$translatePartialLoader', function run($translate, $translatePartialLoader) {
		$translatePartialLoader.addPart('app/spell');
		$translate.refresh();
	} ]);
	
	module.config(['$stateProvider', function config($stateProvider) {

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
			url : ''
		});

		$stateProvider.state('spells.list', {
			url : '/:modId',
			controller : 'SpellListController',
			templateUrl : 'spell/spell-list.tpl.html',
			resolve : {
				spells : [ 'SpellService', '$stateParams', function(SpellService, $stateParams) {
					var spells = SpellService.getSpells($stateParams.modId);
					return angular.isDefined(spells.$promise) ? spells.$promise : spells;
				} ]
			}
		});
		
		$stateProvider.state('spells.list.edit', {
			url : '/:id',
			onEnter : [ '$state', '$stateParams', '$modal', 'Spell', function($state, $stateParams, $modal, Spell) {
				var modal = $modal.open({
					templateUrl : "spell/spell-edit.tpl.html",
					controller : 'SpellEditController',
					resolve : {
						spell : ['Spell', function(Spell) {
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
						}]
					}
				});
				modal.result.then(function(result) {
					$state.go('^', {}, {
						reload : true
					});
				}, function() {
					$state.go('^');
				});
			} ]
		});

		$stateProvider.state('spells.list.import', {
			onEnter : [ '$state', '$stateParams', '$modal', function($state, $stateParams, $modal) {
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
					$state.go('^');
				});
			}]
		});
	}]);

})();