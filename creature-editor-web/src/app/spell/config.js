(function() {
	'use strict';

	var module = angular.module('creatureEditor.spell.config', [ 'creatureEditor.spell.services', 'pascalprecht.translate', 'ui.router' ]);

	module.config([ '$translatePartialLoaderProvider', function SpellTranslateConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('app/spell');
	} ]);

	module.config([ '$stateProvider', function SpellStateConfig($stateProvider) {

		$stateProvider.state('spells', {
			abstract : true,
			url : '/spells',
			controller : 'SpellController',
			templateUrl : 'spell/spell.tpl.html',
			// template: '<div data-jq-docking="layout"></div>',
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
			views : {
				'spell-list' : {
					controller : 'SpellListController',
					templateUrl : 'spell/spell-list.tpl.html',
				}
			},
			resolve : {
				spells : [ 'SpellService', '$stateParams', function(SpellService, $stateParams) {
					var spells = SpellService.getSpells($stateParams.modId);
					return angular.isDefined(spells.$promise) ? spells.$promise : spells;
				} ]
			}
		});

		$stateProvider.state('spells.list.edit', {
			url : '/:id',
			resolve : {
				spell : [ 'Spell', '$stateParams', function(Spell, $stateParams) {
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
				} ],
				flags : [ 'SpellService', function(SpellService) {
					return SpellService.getFlags();
				} ],
				exclusionFlags : [ 'SpellService', function(SpellService) {
					return SpellService.getExclusionFlags();
				} ]
			},
			onEnter : [ '$state', '$jqWindow', 'spell', 'flags', 'exclusionFlags', function($state, $jqWindow, spell, flags, exclusionFlags) {
				var modal = $jqWindow.open({
					title: spell.resource,
					templateUrl : "spell/spell-edit.tpl.html",
					controller : 'SpellEditController',
					options : {
						width: 900,
						height: 800
					},
					inject : {
						spell : spell,
						flags : flags.data,
						exclusionFlags : exclusionFlags.data
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
