angular.module('editor.creature.config', [])

.config(function($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('app/creature');
})

;
