angular.module('crud', [ 'crud.directives', 'crud.services', 'pascalprecht.translate', 'toaster' ])

.config(function($translatePartialLoaderProvider) { 'use strict';
		$translatePartialLoaderProvider.addPart('common/crud');
})

;
