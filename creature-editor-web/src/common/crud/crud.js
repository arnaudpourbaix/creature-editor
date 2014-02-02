(function() {
	'use strict';

	var module = angular.module('crud', [ 'crud.directives', 'crud.services', 'pascalprecht.translate' ]);

	module.run([ '$translate', '$translatePartialLoader', function run($translate, $translatePartialLoader) {
		$translatePartialLoader.addPart('common/crud');
		$translate.refresh();
	} ]);

})();