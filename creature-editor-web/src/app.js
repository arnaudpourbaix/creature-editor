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
	'apx-jqwidgets',
	'apx-alertify',
	'apx-tools', 
	'an-error', 
	'an-log4javascript',
	'crud',
	'parameter', 
	
	// Application modules
	'editor.main',
	'editor.creature',
	'editor.mod',
	'editor.category'
	
]);
