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
	'jqwidgets',
	'toaster',

	// Templates
	'templates-app', 
	'templates-common',
	
	// Common modules
	'apx-tools', 
	'an-error', 
	'an-log4javascript',
	'translate-wrapper',
	'crud',
	'parameter', 
	
	// Application modules
	'editor.main',
	'editor.creature'
	
]);
