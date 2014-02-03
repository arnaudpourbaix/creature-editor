(function() {
	'use strict';

	var module = angular.module('crud', [ 'crud.directives', 'crud.services', 'pascalprecht.translate' ]);

	module.config([ '$translatePartialLoaderProvider', function run($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('common/crud');
	} ]);

})();