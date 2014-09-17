angular.module('crud', [ 'crud.directives', 'crud.services', 'pascalprecht.translate' ])

.config(function($translatePartialLoaderProvider) { 'use strict';
		$translatePartialLoaderProvider.addPart('common/crud');
})

;
