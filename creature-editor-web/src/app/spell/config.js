(function() {
	'use strict';

	var module = angular.module('creatureEditor.spell.config', [ 'creatureEditor.spell.services', 'pascalprecht.translate', 'ui.router' ]);

	module.config([ '$translatePartialLoaderProvider', function SpellTranslateConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('app/spell');
	} ]);

	module.config([ '$stateProvider', function SpellStateConfig($stateProvider) {

		$stateProvider.state('spells', {
			url : '/spells',
			controller : 'SpellController',
			templateUrl : 'spell/spell.tpl.html',
			resolve : {
				mods : [ 'Mod', function(Mod) {
					return Mod.query().$promise;
				} ],
				flags : [ 'SpellService', function(SpellService) {
					return SpellService.getFlags();
				} ],
				exclusionFlags : [ 'SpellService', function(SpellService) {
					return SpellService.getExclusionFlags();
				} ]
			}
		});

		$stateProvider.state('spells.list', {
			url : '/:modId',
			views : {
				'spell-list' : {
					controller : 'SpellListController',
					templateUrl : 'spell/spell-list.tpl.html'
				}
			},
			resolve : {
				spells : [ 'SpellService', '$stateParams', function(SpellService, $stateParams) {
					return SpellService.getSpells($stateParams.modId);
				} ]
			}
		});

		$stateProvider.state('spells.list.edit', {
			url : '/:spellId',
			views : {
				'spell-edit' : {
					controller : 'SpellEditController',
					templateUrl : "spell/spell-edit.tpl.html",
				}
			}
		});
		
		$stateProvider.state('spells.list.import', {
			resolve : {
				mod : [ 'Mod', '$stateParams', function(Mod, $stateParams) {
					return Mod.get({
						id : $stateParams.modId
					}).$promise;
				} ]
			},
			onEnter : [ '$state', 'mod', '$jqWindow', '$translate', function($state, mod, $jqWindow, $translate) {
				var modal = $jqWindow.open({
					templateUrl : "spell/spell-import.tpl.html",
					controller : 'SpellImportController',
					title : $translate('SPELL.IMPORT.TITLE'),
					options : {
						width: 500,
						height: 150
					},
					inject: {
						mod : mod
					}
				});
				modal.result.finally(function(result) {
					$state.go('^');
				});
			} ]
		});

	} ]);

})();
