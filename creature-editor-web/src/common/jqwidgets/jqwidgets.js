angular.module('jqwidgets', [ 
	'pascalprecht.translate', 
	'jqwidgets.common', 
	'jqwidgets.data-adapter', 
	'jqwidgets.window', 
	'jqwidgets.grid',
	'jqwidgets.dropdownlist', 
	'jqwidgets.panel', 
	'jqwidgets.tree', 
	'jqwidgets.treegrid', 
	'jqwidgets.tabs', 
	'jqwidgets.menu', 
	'jqwidgets.splitter', 
	'jqwidgets.docking' 
])

.config(function($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('common/jqwidgets');
})
	
;
