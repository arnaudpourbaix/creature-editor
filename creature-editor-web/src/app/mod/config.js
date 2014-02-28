(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.config', [ 'creatureEditor.mod.services', 'jqwidgets', 'pascalprecht.translate', 'ui.router', 'ui.bootstrap' ]);

	module.config([ '$translatePartialLoaderProvider', function ModTranslateConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('app/mod');
	} ]);

	module.config([ '$stateProvider', function ModStateConfig($stateProvider) {
		$stateProvider.state('mods', {
			url : '/mods',
			resolve : {
				mods : [ 'Mod', function(Mod) {
					return Mod.query().$promise;
				} ]
			},
			controller : 'ModListController',
			templateUrl : 'mod/mod-list.tpl.html'
		});

		$stateProvider.state('mods.edit', {
			url : '/:modId',
			resolve : {
				mod : [ 'Mod', '$stateParams', function(Mod, $stateParams) {
					if ($stateParams.modId !== 'new') {
						return Mod.get({
							id : $stateParams.modId
						}).$promise;
					} else {
						return new Mod({
							id : null,
							name : ''
						});
					}
				} ]
			},
			onEnter : [ '$state', '$jqWindow', 'mod', function($state, $jqWindow, mod) {
				var modal = $jqWindow.open({
					title: "{{ mod.name || '&nbsp;' }}",
					templateUrl : "mod/mod-edit.tpl.html",
					controller : 'ModEditController',
					options : {
						width: 500,
						height: 200
					},
					inject : {
						mod : mod
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
	} ]);

})();
