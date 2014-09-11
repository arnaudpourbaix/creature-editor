angular.module('editor', [ 
                                        
	// External dependencies (vendor)
	'LocalStorageModule',
	'ngAnimate',
	'ngCookies', 
	'ngResource',
	'ngSanitize', 
	'pascalprecht.translate',
	'ui.router', 
	'ui.bootstrap',
	'ui.utils',
	
	// Internal dependencies
	'an-error', 
	'an-log4javascript', 
	'editor.main',
	'editor.creature',
	'templates-app', 
	'templates-common' 
	
]);
