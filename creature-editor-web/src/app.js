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
	'ngGrid',

	// Templates
	'templates-app', 
	'templates-common',
	
	// Common modules
	'an-error', 
	'an-log4javascript',
	'alert-message',
	'translate-wrapper',
	'crud',
	'parameter', 
	
	// Application modules
	'editor.main',
	'editor.creature'
	
]);
