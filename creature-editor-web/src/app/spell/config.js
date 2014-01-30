(function() {
	'use strict';

	var module = angular.module('creatureEditor.spell.config', [ 'creatureEditor.spell.services', 'ui.router', 'ui.bootstrap', 'alertMessage' ]);

	module.run(function run(alertMessageService) {
		alertMessageService.addConfig({
			'spell.save.success' : "Spell '{{name}}' was saved successfully.",
			'spell.save.error' : "Error when saving a spell...",
			'spell.remove.success' : "Spell '{{name}}' was removed successfully.",
			'spell.remove.error' : "Error when removing spell '{{name}}'.",
			'spell.import.success' : "Spell import for mod {{name}} is done.",
			'spell.import.error' : "Spell import has encountered an internal error.",
			'spell.import.cancel' : "Spell import has been cancelled."
		});
	});
	
	module.config(function config($stateProvider) {

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
				spells : function(SpellService, $stateParams) {
					var spells = SpellService.getSpells($stateParams.modId);
					return angular.isDefined(spells.$promise) ? spells.$promise : spells;
				}
			}
		});
		
		$stateProvider.state('spells.list.edit', {
			url : '/:id',
			onEnter : function($state, $stateParams, $modal, $timeout, Spell) {
				var modal = $modal.open({
					templateUrl : "spell/spell-edit.tpl.html",
					controller : 'SpellEditController',
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
					console.log(result.spell);
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
