angular.module('editor.mod.config', [])

.config(function($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('app/mod');
})

.config(function($stateProvider) {
	$stateProvider.state('mods', {
		url : '/mods',
		controller : 'ModListController',
		templateUrl : 'mod/mod-list.tpl.html',
		resolve : {
			mods : function(Mod) {
				return Mod.query().$promise;
			}
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
})

;
