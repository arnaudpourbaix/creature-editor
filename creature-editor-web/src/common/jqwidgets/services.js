(function(window) {
	'use strict';

	var module = angular.module('jqwidgets.services', []);

	function JqDataAdapterService(dataAdapterOptions) {
		var service = {
			get : function(source, settings) {
				console.log('data adapter test');
				var params = angular.extend({}, dataAdapterOptions, settings);
				return new $.jqx.dataAdapter(source, params);
			}
		};
		return service;
	}
	
	function JqWidgetService(dataAdapterOptions, commonOptions, gridOptions, dropDownListOptions, windowOptions, panelOptions, treeOptions) {
		var dataAdapter = new JqDataAdapterService(dataAdapterOptions);
		var service = {
			dataAdapter : function() {
				return dataAdapter;
			},
			commonOptions : function() {
				return commonOptions;
			},
			gridOptions : function() {
				return gridOptions;
			},
			dropDownListOptions : function() {
				return dropDownListOptions;
			},
			windowOptions : function() {
				return windowOptions;
			},
			panelOptions : function() {
				return panelOptions;
			},
			treeOptions : function() {
				return treeOptions;
			}
		};
		return service;
	}

	module.provider('$jqwidgets', function JqWidgetProvider() {
		var commonOptions = {
			theme : 'classic'
		};
		var gridOptions = {
			altRows : true,
			columnsResize : true,
			sortable : true,
			showfilterrow : true,
			filterable : true,
			selectionMode : 'singlerow',
			pagermode : 'simple',
			enabletooltips : true
		};
		var dropDownListOptions = {
			searchMode : 'containsignorecase'
		};
		var windowOptions = {
			showCollapseButton : true,
			isModal : true
		};
		var panelOptions = {
				
		};
		var treeOptions = {
				
		};
		var dataAdapterOptions = {
			autoBind : true
		};
		this.theme = function(value) {
			commonOptions.theme = value;
		};

		this.$get = [ function jqWidgetService() {
			return new JqWidgetService(dataAdapterOptions, commonOptions, gridOptions, dropDownListOptions, windowOptions, panelOptions, treeOptions);
		} ];
	});
	
}(window));