(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.config', [ 'creatureEditor.mod.services', 'pascalprecht.translate', 'ui.router' ]);

	module.config([ '$translatePartialLoaderProvider', function ModTranslateConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('app/mod');
	} ]);

	module.config([ '$stateProvider', function ModStateConfig($stateProvider) {

		$stateProvider.state('mods', {
			url : '/mods',
			controller : 'ModListController',
			templateUrl : 'mod/mod-list.tpl.html',
			resolve : {
				mods : [ 'Mod', function(Mod) {
					return Mod.query().$promise;
				} ]
			}
		});

		$stateProvider.state('mods.edit', {
			url : '/:modId',
			views : {
				'mod-edit' : {
					controller : 'ModEditController',
					templateUrl : 'mod/mod-edit.tpl.html'
				}
			}
		});
		
	} ]);

})();
