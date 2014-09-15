(function() {
	'use strict';

	var module = angular.module('crud', [ 'crud.directives', 'crud.services', 'pascalprecht.translate' ]);

	module.config([ '$translatePartialLoaderProvider', function CrudConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('common/crud');
	} ]);

})();