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
		onEnter : function($state, $stateParams, $modal, $timeout) {
			var modal = $modal.open({
				controller : 'ModEditController',
				templateUrl : "mod/mod-edit.tpl.html",
				resolve : {
					mod : function(Mod, ModService) {
						return Mod.get({
							id: $stateParams.id
						}).$promise;
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
	
})

;
