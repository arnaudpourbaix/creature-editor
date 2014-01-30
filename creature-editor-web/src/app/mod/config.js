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
				mods : [ 'Mod', function(Mod) {
					return Mod.query().$promise;
				} ]
			},
			controller : 'ModListController',
			templateUrl : 'mod/mod-list.tpl.html'
		});

		$stateProvider.state('mods.edit', {
			url : '/:modId',
			onEnter : function($state, $stateParams, $modal, $timeout, Mod) {
				var modal = $modal.open({
					templateUrl : "mod/mod-edit.tpl.html",
					controller : 'ModEditController',
					resolve : {
						mod : function(Mod) {
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
