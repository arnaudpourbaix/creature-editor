angular.module('editor.mod.config', [])

.config(function($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('app/mod');
})

.config(function($stateProvider) {
	$stateProvider.state('mod', {
		url : '/mod',
		controller : 'ModListController',
		templateUrl : 'mod/mod-list.tpl.html',
		resolve : {
			mods : function(Mod) {
				return Mod.query().$promise;
			}
		}
	});

	$stateProvider.state('mod.edit', {
		url : '/:id',
		views : {
			'mod-edit' : {
				controller : 'ModEditController',
				templateUrl : "mod/mod-edit.tpl.html",
			}
		},
		resolve : {
			mod : function($stateParams, ModService) {
				return ModService.get($stateParams.id);
			}
		}
	});
	
})

;
