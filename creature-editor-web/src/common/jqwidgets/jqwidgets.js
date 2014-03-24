(function(window) {
	'use strict';

	var module = angular.module('jqwidgets', [ 'pascalprecht.translate', 'jqwidgets.common', 'jqwidgets.data-adapter', 'jqwidgets.window', 'jqwidgets.grid',
			'jqwidgets.dropdownlist', 'jqwidgets.panel', 'jqwidgets.tree', 'jqwidgets.treegrid', 'jqwidgets.menu', 'jqwidgets.splitter', 'jqwidgets.docking' ]);

	module.config([ '$translatePartialLoaderProvider', function run($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('common/jqwidgets');
	} ]);

}(window));