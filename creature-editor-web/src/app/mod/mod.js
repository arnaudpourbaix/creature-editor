(function() {
	'use strict';

	var mod = angular.module('creatureEditor.mod', [ 'creatureEditor.mod.services', 'creatureEditor.mod.directives', 'creatureEditor.mod.controllers',
			'ui.router', 'ngRoute', 'notification.i18n' ]);

	mod.constant('I18N.MESSAGES', {
		'errors.route.changeError' : 'Route change error',
		'crud.mod.save.success' : "Mod '{{name}}' was saved successfully.",
		'crud.mod.remove.success' : "Mod '{{name}}' was removed successfully.",
		'crud.mod.remove.error' : "Error when removing mod '{{name}}'.",
		'crud.mod.save.error' : "Error when saving a mod..."
	});

	mod.config(function config($stateProvider) {

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

		$stateProvider.state('mods.detail', {
			url : '/:modId',
			onEnter : function($state, $stateParams, $modal, $timeout, Mod) {
				var modal = $modal.open({
					templateUrl : "mod/mod-detail.tpl.html",
					controller : 'ModDetailController',
					backdrop : false,
					resolve : {
						mod : function(Mod) {
							if ($stateParams.modId !== '-1') {
								return Mod.get({
									id : $stateParams.modId
								}).$promise;
							} else {
								return new Mod({
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
