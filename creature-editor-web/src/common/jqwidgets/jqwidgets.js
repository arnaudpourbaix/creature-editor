angular.module('apx-jqwidgets', [ 
	'pascalprecht.translate', 
	'apx-jqwidgets.common', 
	'apx-jqwidgets.data-adapter', 
	'apx-jqwidgets.menu', 
	'apx-jqwidgets.tree', 
	'apx-jqwidgets.docking', 
	'apx-jqwidgets.splitter', 
	'apx-jqwidgets.grid'
//	'apx-jqwidgets.window', 
//	'apx-jqwidgets.dropdownlist', 
//	'apx-jqwidgets.panel', 
//	'apx-jqwidgets.tabs', 
])

.config(function($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('common/jqwidgets');
})
	
;
