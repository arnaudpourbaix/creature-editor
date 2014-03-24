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
		
//		$stateProvider.state('mods.edit', {
//			url : '/:modId',
//			resolve : {
//				mod : [ 'Mod', '$stateParams', function(Mod, $stateParams) {
//					if ($stateParams.modId !== 'new') {
//						return Mod.get({
//							id : $stateParams.modId
//						}).$promise;
//					} else {
//						return new Mod({
//							id : null,
//							name : ''
//						});
//					}
//				} ]
//			},
//			onEnter : [ '$state', '$jqWindow', '$translate', 'mod', function($state, $jqWindow, $translate, mod) {
//				var modal = $jqWindow.open({
//					title : function() {
//						if (mod.id == null) {
//							return $translate('MOD.NEW_TITLE');
//						} else {
//							return $translate('MOD.EDIT_TITLE', {
//								name : mod.name
//							});
//						}
//					},
//					templateUrl : "mod/mod-edit.tpl.html",
//					controller : 'ModEditController',
//					options : {
//						width : 500,
//						height : 200
//					},
//					inject : {
//						mod : mod
//					}
//				});
//				modal.result.then(function(result) {
//					$state.go('^', {}, {
//						reload : true
//					});
//				}, function() {
//					$state.go('^');
//				});
//			} ]
//		});
	} ]);

})();
