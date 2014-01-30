(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.config', [ 'creatureEditor.mod.services', 'ui.router', 'ui.bootstrap', 'alertMessage' ]);

	module.run(function run(alertMessageService) {
		alertMessageService.addConfig({
			'mod.save.success' : "Mod '{{name}}' was saved successfully.",
			'mod.save.error' : "Error when saving a mod",
			'mod.remove.success' : "Mod '{{name}}' was removed successfully.",
			'mod.remove.error' : "Error when removing mod '{{name}}'."
		});
	});

	module.config(function config($stateProvider) {

		$stateProvider.state('mods', {
			url : '/mods',
			resolve : {
				mods : [ 'Restangular', function(Restangular) {
					return Restangular.all('mod').getList();
				} ]
			},
			controller : 'ModListController',
			templateUrl : 'mod/mod-list.tpl.html'
		});

		$stateProvider.state('mods.edit', {
			url : '/:modId',
			onEnter : function($state, $stateParams, $modal, $timeout, Restangular) {
				var modal = $modal.open({
					templateUrl : "mod/mod-edit.tpl.html",
					controller : 'ModEditController',
					resolve : {
						mod : function(Restangular) {
							if ($stateParams.modId !== 'new') {
								return Restangular.one('mod', $stateParams.modId).get();
							} else {
								return {
									id : null,
									name : ''
								};
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
