angular.module('apx-jqwidgets', [ 
	'pascalprecht.translate', 
	'apx-jqwidgets.common', 
	'apx-jqwidgets.data-adapter', 
	'apx-jqwidgets.menu' 
//	'apx-jqwidgets.tree', 
//	'apx-jqwidgets.window', 
//	'apx-jqwidgets.grid',
//	'apx-jqwidgets.dropdownlist', 
//	'apx-jqwidgets.panel', 
//	'apx-jqwidgets.tabs', 
//	'apx-jqwidgets.splitter', 
//	'apx-jqwidgets.docking' 
])

.config(function($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('common/jqwidgets');
})
	
;
