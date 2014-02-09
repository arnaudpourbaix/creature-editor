(function() {
	'use strict';

	var module = angular.module('loading-animation', [ 'pascalprecht.translate' ]);

	module.config([ '$translatePartialLoaderProvider', function run($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('common/loading-animation');
	} ]);

})();