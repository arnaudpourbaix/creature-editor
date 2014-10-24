/**
 * @description Contains all translations. Because french is the only supported language, there are not put in separate json files.  
 */
angular.module('editor.main.translate', []).config(function($translateProvider, $translatePartialLoaderProvider) {
	'use strict';
	$translateProvider.useLocalStorage();
	$translateProvider.useLoader('$translatePartialLoader', {
		urlTemplate : 'src/{part}/locales/messages-{lang}.json'
	});
	$translateProvider.preferredLanguage('en');
	$translateProvider.fallbackLanguage('en');

	$translatePartialLoaderProvider.addPart('app/main');
});	
